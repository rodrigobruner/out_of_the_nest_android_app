package app.outofthenest.models;

public class Tag {
    private String name;
    private String color;
    private String icon;

    public Tag(String name, String color, String icon) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
