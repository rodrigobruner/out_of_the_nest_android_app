package app.outofthenest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    private String name;
    private String email;
    private String password;
    private ArrayList<FamilyMember> familyMembers;

    public User(String id, String name, String email, String password, ArrayList<FamilyMember> familyMembers) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.familyMembers = familyMembers;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(ArrayList<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }
}
