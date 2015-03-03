package volo.voloCalendar.externalModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Emin Guliyev on 03/03/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenPayload implements Serializable{
    private String utype;
    private int uid;

    public TokenPayload() {
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
