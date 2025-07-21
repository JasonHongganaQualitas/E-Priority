package id.co.qualitas.epriority.model;

import java.io.Serializable;

public class Agent implements Serializable {
    private boolean checked;
    private String name;
    private String languages;
    private double rating_average;
    private double review_count;
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

    public double getRating_average() {
        return rating_average;
    }

    public void setRating_average(double rating_average) {
        this.rating_average = rating_average;
    }

    public double getReview_count() {
        return review_count;
    }

    public void setReview_count(double review_count) {
        this.review_count = review_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
