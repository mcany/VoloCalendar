package volo.voloCalendar.externalModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 28/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location implements Serializable {
    private int id;
    private String company;
    private String care_of;
    private String street;
    private String zip_code;
    private String city;
    private String country_code;

    public Location() {
    }
    //TODO: DE is ok?
    public Location(int id, String street, String zip_code, String city) {
        this.id = id;
        this.street = street;
        this.zip_code = zip_code;
        this.city = city;
        this.country_code = "DE";
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCare_of() {
        return care_of;
    }

    public void setCare_of(String care_of) {
        this.care_of = care_of;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //TODO:DE is ok?
    public Location copy(int id, String street, String zip_code, String city) {
        this.id = id;
        this.street = street;
        this.zip_code = zip_code;
        this.city = city;
        this.country_code = "DE";
        return this;
    }
}
