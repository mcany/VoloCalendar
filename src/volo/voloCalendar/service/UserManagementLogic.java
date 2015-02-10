package volo.voloCalendar.service;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import volo.voloCalendar.externalModel.*;
import volo.voloCalendar.entity.User;
import volo.voloCalendar.util.UtilMethods;
import volo.voloCalendar.viewModel.user.UserTable;
import volo.voloCalendar.viewModel.user.UserTableItems;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Emin Guliyev on 28/01/2015.
 */
//TODO 8:uncomment next line
//@Service
public class UserManagementLogic implements UserManagement {

    public boolean authenticate(String email, String password) throws HttpClientErrorException {
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
        bodyMap.add("username", email);
        bodyMap.add("password", password);
        bodyMap.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(bodyMap, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<AuthenticationResponse> entity = restTemplate.exchange(
                "http://staging.volo.de/auth/token", HttpMethod.POST,
                requestEntity, AuthenticationResponse.class);
        AuthenticationResponse result = entity.getBody();

        if (UtilMethods.isTestingRestApi) {
            UtilMethods.temp = "Bearer " + result.getAccess_token();
        } else {
            RequestContextHolder.currentRequestAttributes().setAttribute(UtilMethods.tokenVariableName, "Bearer " + result.getAccess_token(), RequestAttributes.SCOPE_SESSION);
        }

        boolean isAdmin;
        try {
            //TODO 9: implement it, we need to get user role on authentication, for example by "isAdmin" boolean header as shown
            String isAdminString = entity.getHeaders().get("isAdmin").get(0);
            isAdmin = isAdminString.equals("true");
        } catch (Exception ex) {
            isAdmin = false;
        }
        return isAdmin;
    }

    //rest
    //TODO 10.1: filtering, filtering should be done by email, name and phone fields, fix after and according server side is done
    public UserTableItems getSortedFilteredPagedUsersWithoutStatistics(UserTable userTable, boolean isKeywordGlobal) {
        if (isKeywordGlobal) {
            userTable.setKeyword("email like %" + userTable.getKeyword() + "%" + " || " + "name like %" + userTable.getKeyword() + "%"
                    + " || " + "phone like %" + userTable.getKeyword() + "%");
        }
        HttpEntity<Object> httpEntity = UtilMethods.getAuthenticatedObjectHttpEntity(null);
        RestTemplate restTemplate = new RestTemplate();

        String sortOrder = userTable.isReverse() ? "-" : "+";
        String created_at = convertFieldName(userTable.getSortingField());
        int itemsPerPage = userTable.getItemsPerPage();
        int pageNumber = userTable.getCurrentPage();
        ResponseEntity<Drivers> entity = restTemplate.exchange(
                "http://staging.volo.de/drivers?sort=" + sortOrder + created_at + "&per_page=" + itemsPerPage + "&page=" + pageNumber, HttpMethod.GET,
                httpEntity, Drivers.class);
        int allCount = Integer.parseInt(entity.getHeaders().get("Total").get(0));
        Drivers drivers = entity.getBody();
        User[] users = drivers.convertToUserArray();

        for (User user : users) {
            setMailLocationDetails(user);
        }
        return new UserTableItems(allCount, users);
    }

    private void setMailLocationDetails(User user) {
        int locationId = user.getLocationId();
        Location location = getLocationById(locationId);
        user.setMailLocationDetails(location);
    }

    private Location getLocationById(int locationId) {
        Location location = null;
        HttpEntity<Object> httpEntity = UtilMethods.getAuthenticatedObjectHttpEntity(null);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LocationHolder> locationEntity = restTemplate.exchange(
                "http://staging.volo.de/locations/" + locationId, HttpMethod.GET,
                httpEntity, LocationHolder.class);
        location = locationEntity.getBody().getLocation();
        return location;
    }

    //TODO 11: implement it, mapping between local field name and server side sort parameter desired value
    private String convertFieldName(String sortingField) {
        return sortingField;
    }

    //rest
    public User getUserById(String id) {
        Driver driver = getDriverById(id);
        User user = driver.convertToUser();
        setMailLocationDetails(user);
        return user;
    }

    private Driver getDriverById(String id) {
        HttpEntity<Object> httpEntity = UtilMethods.getAuthenticatedObjectHttpEntity(null);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DriverHolder> entity = restTemplate.exchange(
                "http://staging.volo.de/drivers/" + id, HttpMethod.GET,
                httpEntity, DriverHolder.class);
        return entity.getBody().getDriver();
    }

    //rest
    //TODO 10.2: filtering, filtering should be done by email
    public User getUserByEmail(String email) {
        UserTable userTable = new UserTable("created_at", false, "email = " + email, 1, 1);
        UserTableItems userTableItems = getSortedFilteredPagedUsersWithoutStatistics(userTable, false);
        return userTableItems.getItems().length == 0 ? null : userTableItems.getItems()[0];
    }

    //rest
    //TODO 10.3: filtering, filtering should be done by status
    public List<User> getActiveDrivers() {
        UserTable userTable = new UserTable("created_at", false, "status = active", 1, Integer.MAX_VALUE);
        UserTableItems userTableItems = getSortedFilteredPagedUsersWithoutStatistics(userTable, false);
        return Arrays.asList(userTableItems.getItems());
    }

    //rest
    public User insertOrUpdateUser(User user) {
        if (user.getId() != null && !user.getId().isEmpty()) {
            updateUser(user);
        } else {
            user = insertUser(user);
        }
        return user;
    }

    private void updateUser(User user) {
        updateUserAccount(user.convertToUserAccount());
        if (!user.isAdmin()) {
            Driver driver = getDriverById(user.getId());
            updateDriver(user.convertToDriver(driver));
            Location location = getLocationById(user.getLocationId());
            updateLocation(user.convertToLocation(location));
        }
    }

    private Location updateLocation(Location location) {
        HttpEntity<LocationHolder> httpEntity = UtilMethods.getAuthenticatedObjectHttpEntity(new LocationHolder(location));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LocationHolder> entity = restTemplate.exchange(
                "http://staging.volo.de/locations/" + location.getId(), HttpMethod.PUT,
                httpEntity, LocationHolder.class);
        Location updatedLocation = entity.getBody().getLocation();
        return updatedLocation;
    }

    private Driver updateDriver(Driver driver) {
        HttpEntity<DriverHolder> httpEntity = UtilMethods.getAuthenticatedObjectHttpEntity(new DriverHolder(driver));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DriverHolder> entity = restTemplate.exchange(
                "http://staging.volo.de/drivers/" + driver.getId(), HttpMethod.PUT,
                httpEntity, DriverHolder.class);
        Driver updatedDriver = entity.getBody().getDriver();
        return updatedDriver;
    }

    //TODO 12: implement, somehow you should provide api to change password of driver or you can add password directly to Driver object in put request(update of whole driver)
    private void updateUserAccount(UserAccount userAccount) {

    }

    private User insertUser(User user) {
        RegistrationRequest registrationRequest = user.convertToRegistrationRequest();
        HttpEntity<RegistrationRequest> httpEntity = UtilMethods.getAuthenticatedObjectHttpEntity(user.convertToRegistrationRequest());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DriverHolder> entity = restTemplate.exchange(
                "http://staging.volo.de/drivers/" + user.getId(), HttpMethod.PUT,
                httpEntity, DriverHolder.class);
        User insertedDriver = entity.getBody().getDriver().convertToUser();
        return insertedDriver;
    }

    public static void main(String[] args) {
        final AbstractApplicationContext context = new ClassPathXmlApplicationContext("file:D:/projects/VoloCalendar/web/WEB-INF/config/applicationContext.xml");
        UserManagementLogic userManagementLogic = context.getBean(UserManagementLogic.class);
        userManagementLogic.authenticate("sergei@volo.de", "test1111");
        User user = userManagementLogic.getUserById("1");
        user.setName("emin");
        userManagementLogic.insertOrUpdateUser(user);
        user = userManagementLogic.getUserById("1");
        System.out.println(user.getName());
    }

}