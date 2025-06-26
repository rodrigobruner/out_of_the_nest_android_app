package app.outofthenest.models;

public class FamilyMember {
    private FamilyMemberType type;
    private int age;

    public FamilyMember(FamilyMemberType type, int age) {
        this.type = type;
        this.age = age;
    }

    public FamilyMemberType getType() {
        return type;
    }

    public void setType(FamilyMemberType type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
