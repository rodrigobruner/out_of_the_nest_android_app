package app.outofthenest.models;

import java.io.Serializable;
import java.util.Date;

/**
 * FamilyMember model class
 */
public class FamilyMember implements Serializable {
    private String type;
    private Date birth;

    public FamilyMember(String type, Date birth) {
        this.type = type;
        this.birth = birth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }
}
