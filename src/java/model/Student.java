package model;

import exception.InvalidIdSetException;
import utils.ValidationUtils;

import java.time.LocalDate;

public class Student {
    private int id;
    private String name;
    private String major;
    private int year;
    private double gpa;

    private boolean isIdSet = false;

    public Student(String name, String major, int year, double gpa) {
        ValidationUtils.validateString(name, 3, "name");
        ValidationUtils.validateString(name, "major");
        validateYear(year);
        ValidationUtils.validateGpa(gpa);

        this.name = name;
        this.major = major;
        this.year = year;
        this.gpa = gpa;
    }

    private void validateYear(int year) {
        if (year < 1900 || year > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Year must be between 1900 and current year.");
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMajor() {
        return major;
    }

    public int getYear() {
        return year;
    }

    public double getGpa() {
        return gpa;
    }

    public void setId(int id) {
        if (isIdSet) throw new InvalidIdSetException("Student", id);

        ValidationUtils.validateId(id);

        this.id = id;
        this.isIdSet = true;
    }
}
