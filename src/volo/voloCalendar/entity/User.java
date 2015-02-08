package volo.voloCalendar.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import volo.voloCalendar.externalModel.Driver;
import volo.voloCalendar.externalModel.Location;
import volo.voloCalendar.externalModel.RegistrationRequest;
import volo.voloCalendar.externalModel.UserAccount;
import volo.voloCalendar.util.Settings;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Emin Guliyev on 28/11/2014.
 */
@Entity
@Table(name = "\"User\"")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {// any user in system: admin or driver
    private static final String defaultBase64Image =
            "data:image/jpg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys/RD84QzQ5Ojf/2wBDAQoKCg0MDRoPDxo3JR8lNzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzf/wAARCABBADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDAqVIi3ODTreLdgmrm1YwMcmvLZ9FYrLb57UjW+Oxq1vOaN57ikOxnuhU9DTK0ZIw6kiqEibXxTE0Noo/Gj8adhGpCoVc8Uh55IqUD92PrQg3OqkcEipcrK5pG7dhixO4ykbH3AppQg4IIPvXQ2sSCEAD1qpq0SrhhjOelcscS3KzR0yopRvcykGDjPFV7qMcmrWOabcLletdaZytoy9oo2ipSnPWk2e9VcRrJzH16UnIP9aow6pGc7VJGP1qWa6G0Yj4YZ4ah03szP6zDdGzb3rJHhkc47gVWu53nYFgQOwxWWdROwkJkA4xupo1JhwyEA8cNURw0Iu4Sx0mrF8KG5UAj60k6seAhP0FUm1FUUkrj05/+tWfd6uWLbUIxzu3dPwxWqppmLxVtzR8p/wDnm/8A3yaPJf8A55v/AN8msv8AtvHBYEjqcn/Cj+3B6j8z/hVeyI+ueRnQ3ahh82BV5L5AuGkXAPUHNc+j+S+0AkZGSR0Hr/h600vJLclYlPGcsBnHH1GK2U4Nao8iGIkdOk8UjBVZQTyAeKSW5SOPKgNnpjqaxo5JcL8wEgHqWHp+fSo5ZGEi7yxIHIH8PQ5+tZykuhq8S0i7cXyshZWP54OKzXlZyclsenWnFAH42cjDM54YdsH1yfSmSQIHcIxJ25yO3HTHet6M6W0zP29wO0nJ6/SjC/5FOXT3ZQRLJyM/cH+NL/Z0n/PWT/vgf/FV1Ww/84e1G3H/AC3/ANxP61Xtf+Pl/wAf5GrFx/y3/wBxP61Xtf8Aj5f8f5GvNOaO5Ncf8hUf7x/9mpT9yT60lx/yFR/vH/2alP3JPrUz2JqCT/dX6L/6EKbbf8hGH/eH/oNOn+6v0X/0IU22/wCQjD/vD/0GiGw0S0UUVJR//9k=";
    private String id; //GUID string but it is int in service side
    private String email;
    private String password;
    private String name;
    private boolean admin; // if user is admin
    private boolean deleted; //if marked as deleted
    private String street;
    private String address;
    private String plz;
    private String city;
    private String telephoneNumber;
    private short transportTypeShort;
    private short telephoneTypeShort;
    private String iban;
    private String bic;
    private short contractTypeShort;
    private String base64Image; //image binary array in base64 form
    private int doneHours; // computable field , selected hours for current month
    private int diffPrevHours; //computable field, (planned hours(45) - selected hours) for current month if user contract is minijob else =0
    private int locationId;

    public User() {
    }

    public User(String id, String email, String password, String name) {
        this.deleted = false;
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        setBase64Image(defaultBase64Image);
        setAdmin(true);
    }

    public User(int locationId, String id, String email, String password, String name, String street, String address, String plz, String city
            , String telephoneNumber, TransportType transportType, TelephoneType telephoneType, String iban, String bic
            , ContractType contractType) {
        this.locationId = locationId;
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
        this.setTransportType(transportType);
        this.setTelephoneType(telephoneType);
        this.iban = iban;
        this.bic = bic;
        this.setContractType(contractType);
    }

    public User(User user) {
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
        this.transportTypeShort = user.transportTypeShort;
        this.telephoneTypeShort = user.telephoneTypeShort;
        this.iban = user.iban;
        this.bic = user.bic;
        this.contractTypeShort = user.contractTypeShort;
    }

    public User(int id, String name, String phone, int locationId, String otherFields) {
        this.id = id + "";
        this.name = name;
        this.telephoneNumber = phone;
        this.locationId = locationId;
        //TODO: convert otherFields
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    @Basic
    @Column(name = "\"password\"")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Basic
    @Column(name = "\"email\"")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Id
    @Column(name = "\"id\"")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Basic
    @Column(name = "\"name\"")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Transient
    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
    @Basic
    @Column(name = "\"admin\"")
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
        if (admin) {
            setNullNonAdminProperties();
        }
    }
    @Basic
    @Column(name = "\"deleted\"")
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
        this.transportTypeShort = 0;
        this.telephoneTypeShort = 0;
        this.iban = null;
        this.bic = null;
        this.contractTypeShort = 0;
    }
    @Basic
    @Column(name = "\"street\"")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if (admin) {
            this.street = null;
            return;
        }
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (admin) {
            this.address = null;
            return;
        }
        this.address = address;
    }
    @Basic
    @Column(name = "\"plz\"")
    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        if (admin) {
            this.plz = null;
            return;
        }
        this.plz = plz;
    }
    @Basic
    @Column(name = "\"city\"")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (admin) {
            this.city = null;
            return;
        }
        this.city = city;
    }
    @Basic
    @Column(name = "\"telephoneNumber\"")
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        if (admin) {
            this.telephoneNumber = null;
            return;
        }
        this.telephoneNumber = telephoneNumber;
    }
    @Transient
    public TransportType getTransportType() {
        return TransportType.values()[transportTypeShort];
    }

    public void setTransportType(TransportType transportType) {
        if (admin) {
            this.transportTypeShort = 0;
            return;
        }
        this.transportTypeShort = (short)transportType.ordinal();
    }
    @Transient
    public TelephoneType getTelephoneType() {
        return TelephoneType.values()[telephoneTypeShort];
    }

    public void setTelephoneType(TelephoneType telephoneType) {
        if (admin) {
            this.telephoneTypeShort = 0;
            return;
        }
        this.telephoneTypeShort = (short)telephoneType.ordinal();
    }
    @Basic
    @Column(name = "\"iban\"")
    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        if (admin) {
            this.iban = null;
            return;
        }
        this.iban = iban;
    }
    @Basic
    @Column(name = "\"bic\"")
    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        if (admin) {
            this.bic = null;
            return;
        }
        this.bic = bic;
    }
    @Transient
    public ContractType getContractType() {
        return ContractType.values()[contractTypeShort];
    }

    public void setContractType(ContractType contractType) {
        if (admin) {
            this.contractTypeShort = 0;
            return;
        }
        this.contractTypeShort = (short)contractType.ordinal();
    }
    @Transient
    public int getPlannedHours() {
        return (contractTypeShort == ContractType.minijob.ordinal()) ? Settings.minijobMonthlyPlan : 0;
    }
    @Transient
    public int getDiffHours() {
        return (contractTypeShort == ContractType.minijob.ordinal()) ? (getPlannedHours() - getDoneHours()) : 0;
    }
    @Transient
    public int getDoneHours() {
        return doneHours;
    }

    public void setDoneHours(int doneHours) {
        this.doneHours = doneHours;
    }
    @Transient
    public int getDiffPrevHours() {
        return diffPrevHours;
    }

    public void setDiffPrevHours(int diffPrevHours) {
        this.diffPrevHours = diffPrevHours;
    }
    @Transient
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
    @Basic
    @Column(name = "\"telephoneType\"")
    public short getTelephoneTypeShort() {
        return telephoneTypeShort;
    }

    public void setTelephoneTypeShort(short telephoneTypeShort) {
        this.telephoneTypeShort = telephoneTypeShort;
    }
    @Basic
    @Column(name = "\"contractType\"")
    public short getContractTypeShort() {
        return contractTypeShort;
    }

    public void setContractTypeShort(short contractTypeShort) {
        this.contractTypeShort = contractTypeShort;
    }
    @Basic
    @Column(name = "\"transportType\"")
    public short getTransportTypeShort() {
        return transportTypeShort;
    }

    public void setTransportTypeShort(short transportTypeShort) {
        this.transportTypeShort = transportTypeShort;
    }

    public void setMailLocationDetails(Location location) {
        this.address = location.getStreet();
        this.city = location.getCity();
        this.plz = location.getZip_code();
    }

    public UserAccount convertToUserAccount() {
        return new UserAccount(email, password, null, name);
    }

    public Driver convertToDriver() {
        Driver driver = new Driver();
        return convertToDriver(driver);
    }

    public Location convertToLocation() {
        Location location = new Location();
        return convertToLocation(location);
    }

    public RegistrationRequest convertToRegistrationRequest() {
        return new RegistrationRequest(admin, convertToUser(), convertToLocation(), convertToUserAccount());
    }

    private volo.voloCalendar.externalModel.User convertToUser() {
        return new volo.voloCalendar.externalModel.User(name);
    }

    public Driver convertToDriver(Driver driver) {
        return driver.copy(Integer.parseInt(id), name, telephoneNumber, deleted ? "banned" : "active", locationId);
    }

    public Location convertToLocation(Location location) {
        return location.copy(locationId, address, plz, city);
    }
}
