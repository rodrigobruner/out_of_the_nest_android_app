package app.outofthenest.models;

import java.util.ArrayList;

public class User {
    private String name;
    private String email;
    private String password;
    private ArrayList<FamilyMember> familyMembers;

    public User(String name, String email, String password, ArrayList<FamilyMember> familyMembers) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.familyMembers = familyMembers;
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
