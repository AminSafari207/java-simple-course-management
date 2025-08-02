package model;

import exception.InvalidIdSetException;
import utils.ValidationUtils;

import java.time.LocalDate;

public class Enrollment {
    private int id;
    private int studentId;
    private int courseId;
    private LocalDate date;
    private int grade;

    private boolean isIdSet;

    public Enrollment(int studentId, int courseId, int grade) {
        ValidationUtils.validateId(studentId);
        ValidationUtils.validateId(courseId);
        ValidationUtils.validateGrade(grade);

        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
        this.date = LocalDate.now();
    }

    public Enrollment(int studentId, int courseId, int grade, LocalDate date) {
        ValidationUtils.validateId(studentId);
        ValidationUtils.validateId(courseId);
        ValidationUtils.validateGrade(grade);

        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getGrade() {
        return grade;
    }

    public boolean isIdSet() {
        return isIdSet;
    }

    public void setId(int id) {
        if (isIdSet) throw new InvalidIdSetException("Enrollment", id);

        ValidationUtils.validateId(id);

        this.id = id;
        this.isIdSet = true;
    }

    public void setStudentId(int studentId) {
        ValidationUtils.validateId(studentId);
        this.studentId = studentId;
    }

    public void setCourseId(int courseId) {
        ValidationUtils.validateId(studentId);
        this.courseId = courseId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setGrade(int grade) {
        ValidationUtils.validateGrade(grade);
        this.grade = grade;
    }
}
