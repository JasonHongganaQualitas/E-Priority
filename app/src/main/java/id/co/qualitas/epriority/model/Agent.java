package id.co.qualitas.epriority.model;

import java.io.Serializable;

public class Agent implements Serializable {
    private boolean checked;
    private String name;
    private String languages;
    private double avg_rating;
    private double total_reviews;
    private int id;

    public Agent() {
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public double getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(double avg_rating) {
        this.avg_rating = avg_rating;
    }

    public double getTotal_reviews() {
        return total_reviews;
    }

    public void setTotal_reviews(double total_reviews) {
        this.total_reviews = total_reviews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
