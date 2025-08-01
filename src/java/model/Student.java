package model;

import exception.InvalidIdSetException;

public class Student {
    private int id;
    private boolean isIdSet = false;
    private String name;
    private String major;
    private int year;
    private double gpa;

    public Student(String name, String major, int year, double gpa) {
        this.name = name;
        this.major = major;
        this.year = year;
        this.gpa = gpa;
    }

    public void setId(int id) {
        if (isIdSet) throw new InvalidIdSetException("Student", id);
        this.id = id;
        this.isIdSet = true;
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
}
