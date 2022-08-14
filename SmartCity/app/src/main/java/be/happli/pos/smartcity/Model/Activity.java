package be.happli.pos.smartcity.Model;

public class Activity {
    private int id;
    private String name;
    private String description;
    private int categoryId;

    public Activity(int id, String name, String description, int categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
