package model;

import exception.InvalidIdSetException;
import utils.ValidationUtils;

public class Course {
    private int id;
    private String title;
    private String department;
    private int credits;

    private boolean isIdSet;

    public Course(String title, String department, int credits) {
        ValidationUtils.validateString(title, "title");
        ValidationUtils.validateString(department, "department");
        validateCredits(credits);

        this.title = title;
        this.department = department;
        this.credits = credits;
    }

    private void validateCredits(int credits) {
        if (credits < 1 || credits > 6) throw new IllegalArgumentException("Credits must be between 1 and 6.");
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDepartment() {
        return department;
    }

    public int getCredits() {
        return credits;
    }

    public void setId(int id) {
        if (isIdSet) throw new InvalidIdSetException("Student", id);

        ValidationUtils.validateId(id);

        this.id = id;
        this.isIdSet = true;
    }
}
