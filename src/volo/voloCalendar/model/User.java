package volo.voloCalendar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Created by Emin Guliyev on 28/11/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable{
    private static final String defaultBase64Image =
            "data:image/jpg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys/RD84QzQ5Ojf/2wBDAQoKCg0MDRoPDxo3JR8lNzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzf/wAARCABBADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDAqVIi3ODTreLdgmrm1YwMcmvLZ9FYrLb57UjW+Oxq1vOaN57ikOxnuhU9DTK0ZIw6kiqEibXxTE0Noo/Gj8adhGpCoVc8Uh55IqUD92PrQg3OqkcEipcrK5pG7dhixO4ykbH3AppQg4IIPvXQ2sSCEAD1qpq0SrhhjOelcscS3KzR0yopRvcykGDjPFV7qMcmrWOabcLletdaZytoy9oo2ipSnPWk2e9VcRrJzH16UnIP9aow6pGc7VJGP1qWa6G0Yj4YZ4ah03szP6zDdGzb3rJHhkc47gVWu53nYFgQOwxWWdROwkJkA4xupo1JhwyEA8cNURw0Iu4Sx0mrF8KG5UAj60k6seAhP0FUm1FUUkrj05/+tWfd6uWLbUIxzu3dPwxWqppmLxVtzR8p/wDnm/8A3yaPJf8A55v/AN8msv8AtvHBYEjqcn/Cj+3B6j8z/hVeyI+ueRnQ3ahh82BV5L5AuGkXAPUHNc+j+S+0AkZGSR0Hr/h600vJLclYlPGcsBnHH1GK2U4Nao8iGIkdOk8UjBVZQTyAeKSW5SOPKgNnpjqaxo5JcL8wEgHqWHp+fSo5ZGEi7yxIHIH8PQ5+tZykuhq8S0i7cXyshZWP54OKzXlZyclsenWnFAH42cjDM54YdsH1yfSmSQIHcIxJ25yO3HTHet6M6W0zP29wO0nJ6/SjC/5FOXT3ZQRLJyM/cH+NL/Z0n/PWT/vgf/FV1Ww/84e1G3H/AC3/ANxP61Xtf+Pl/wAf5GrFx/y3/wBxP61Xtf8Aj5f8f5GvNOaO5Ncf8hUf7x/9mpT9yT60lx/yFR/vH/2alP3JPrUz2JqCT/dX6L/6EKbbf8hGH/eH/oNOn+6v0X/0IU22/wCQjD/vD/0GiGw0S0UUVJR//9k=";
    public static final int minijobPlan = 45;
    private String id;
    private String email;
    private String password;
    private String name;
    private boolean admin;
    private boolean deleted;
    private String street;
    private String address;
    private String plz;
    private String city;
    private String telephoneNumber;
    private TransportType transportType;
    private TelephoneType telephoneType;
    private String iban;
    private String bic;
    private ContractType contractType;
    private String base64Image;
    private int doneHours;
    private int diffPrevHours;
    //temporary
    private DriverCalendarWeek driverCalendarWeekForever;
    private HashMap<LocalDate, DriverCalendarWeek> driverCalendarWeekHashMap = new HashMap<LocalDate, DriverCalendarWeek>();

    public User(){}

    public User(String id, String email, String password, String name){
        this.deleted = false;
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        setBase64Image(defaultBase64Image);
        setAdmin(true);
    }
    public User(String id, String email, String password, String name, String street, String address, String plz, String city
            , String telephoneNumber, TransportType transportType, TelephoneType telephoneType, String iban, String bic
            , ContractType contractType) {
        this.deleted = false;
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        setBase64Image(defaultBase64Image);
        setAdmin(false);
        this.street = street;
        this.address = address;
        this.plz = plz;
        this.city = city;
        this.telephoneNumber = telephoneNumber;
        this.transportType = transportType;
        this.telephoneType = telephoneType;
        this.iban = iban;
        this.bic = bic;
        this.contractType = contractType;
    }

    public User(User user){
        this.deleted = user.deleted;
        this.id = user.id;
        this.email = user.email;
        this.password = user.password;
        this.name = user.name;
        setBase64Image(defaultBase64Image);
        this.admin = user.admin;
        this.street = user.street;
        this.address = user.address;
        this.plz = user.plz;
        this.city = user.city;
        this.telephoneNumber = user.telephoneNumber;
        this.transportType = user.transportType;
        this.telephoneType = user.telephoneType;
        this.iban = user.iban;
        this.bic = user.bic;
        this.contractType = user.contractType;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
        if (admin){
            setNullNonAdminProperties();
        }
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    private void setNullNonAdminProperties() {
        this.street = null;
        this.address = null;
        this.plz = null;
        this.city = null;
        this.telephoneNumber = null;
        this.transportType = null;
        this.telephoneType = null;
        this.iban = null;
        this.bic = null;
        this.contractType = null;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if (admin){
            this.street = null;
            return;
        }
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (admin){
            this.address = null;
            return;
        }
        this.address = address;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        if (admin){
            this.plz = null;
            return;
        }
        this.plz = plz;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (admin){
            this.city = null;
            return;
        }
        this.city = city;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        if (admin){
            this.telephoneNumber = null;
            return;
        }
        this.telephoneNumber = telephoneNumber;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        if (admin){
            this.transportType = null;
            return;
        }
        this.transportType = transportType;
    }

    public TelephoneType getTelephoneType() {
        return telephoneType;
    }

    public void setTelephoneType(TelephoneType telephoneType) {
        if (admin){
            this.telephoneType = null;
            return;
        }
        this.telephoneType = telephoneType;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        if (admin){
            this.iban = null;
            return;
        }
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        if (admin){
            this.bic = null;
            return;
        }
        this.bic = bic;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        if (admin){
            this.contractType = null;
            return;
        }
        this.contractType = contractType;
    }

    public int getPlannedHours() {
        return (contractType == ContractType.minijob)?User.minijobPlan:0;
    }

    public int getDiffHours() {
        return (contractType == ContractType.minijob)?(getPlannedHours()-getDoneHours()):0;
    }

    public int getDoneHours() {
        return doneHours;
    }

    public void setDoneHours(int doneHours) {
        this.doneHours = doneHours;
    }

    public int getDiffPrevHours() {
        return diffPrevHours;
    }

    public void setDiffPrevHours(int diffPrevHours) {
        this.diffPrevHours = diffPrevHours;
    }

    @JsonIgnore
    public HashMap<LocalDate, DriverCalendarWeek> getDriverCalendarWeekHashMap() {
        return driverCalendarWeekHashMap;
    }

    public void setDriverCalendarWeekHashMap(HashMap<LocalDate, DriverCalendarWeek> driverCalendarWeekHashMap) {
        this.driverCalendarWeekHashMap = driverCalendarWeekHashMap;
    }
}
