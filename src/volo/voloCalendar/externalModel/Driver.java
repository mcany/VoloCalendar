package volo.voloCalendar.externalModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import volo.voloCalendar.entity.User;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 28/01/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Driver implements Serializable{
    private int id;
    private String name;
    private String phone;
    private String status;
    private String account_status;
    private String device_token;
    private String device_os;
    private boolean has_active_orders;
    private String last_went_online_at;
    private String last_went_offline_at;
    private int current_location_id;

    public User convertToUser() {
        User user = new User(this.id, this.name, this.phone, 2, null);
        user.setAdmin(false);
        return user;
    }

    public Driver() {
    }

    //TODO: mail location fix
    public Driver(int id, String name, String phone, String account_status, int mail_location_id) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.account_status = account_status;
        //this.mail_location_id = mail_location_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_os() {
        return device_os;
    }

    public void setDevice_os(String device_os) {
        this.device_os = device_os;
    }

    public boolean isHas_active_orders() {
        return has_active_orders;
    }

    public void setHas_active_orders(boolean has_active_orders) {
        this.has_active_orders = has_active_orders;
    }

    public String getLast_went_online_at() {
        return last_went_online_at;
    }

    public void setLast_went_online_at(String last_went_online_at) {
        this.last_went_online_at = last_went_online_at;
    }

    public String getLast_went_offline_at() {
        return last_went_offline_at;
    }

    public void setLast_went_offline_at(String last_went_offline_at) {
        this.last_went_offline_at = last_went_offline_at;
    }

    public int getCurrent_location_id() {
        return current_location_id;
    }

    public void setCurrent_location_id(int current_location_id) {
        this.current_location_id = current_location_id;
    }

}
