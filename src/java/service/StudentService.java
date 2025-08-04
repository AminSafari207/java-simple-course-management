package service;

import exception.StudentAlreadyExistsException;
import model.Student;
import repository.StudentRepository;
import utils.ValidationUtils;

import java.sql.SQLException;
import java.util.List;

public class StudentService {
    StudentRepository studentRepository;

    public StudentService() {
        studentRepository = new StudentRepository();
    }

    public void registerStudents(List<Student> studentList) {
        ValidationUtils.validateCollection(studentList, "studentList");

        for (Student student: studentList) {
            ValidationUtils.validateNotNull(student, "student");
            if (student.isIdSet()) throw new StudentAlreadyExistsException(student.getName(), student.getId());
        }

        try {
            studentRepository.create(studentList);
        } catch (SQLException e) {
            throw new RuntimeException("Registering students failed.", e);
        }
    }
}
