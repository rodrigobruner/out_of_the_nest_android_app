package app.outofthenest.models;

/**
 * Tag model class
 */
public class Tag {
    private String name;
    private String icon;

    public Tag(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
