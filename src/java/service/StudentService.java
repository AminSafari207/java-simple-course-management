package service;

import exception.NoStudentsFoundException;
import exception.StudentAlreadyExistsException;
import exception.StudentNotFoundException;
import model.Student;
import repository.StudentRepository;
import utils.ValidationUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class StudentService {
    private final StudentRepository studentRepository;

    private final List<String> validUpdateKeys = List.of("name", "major", "gpa");

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

    public void updateStudent(int studentId, Map<String, Object> updateMap) {
        ValidationUtils.validateId(studentId);
        ValidationUtils.validateMap(updateMap, "updateMap");

        for (String key: updateMap.keySet()) {
            if (!validUpdateKeys.contains(key)) throw new IllegalArgumentException("Key '" + key + "' is not valid.");
        }

        try {
            studentRepository.update(studentId, updateMap);
        } catch (SQLException e) {
            throw new RuntimeException("Updating student failed.", e);
        }
    }

    public void removeStudent(int studentId) {
        ValidationUtils.validateId(studentId);

        try {
            studentRepository.delete(studentId);
        } catch (SQLException e) {
            throw new RuntimeException("Removing student failed.", e);
        }
    }

    public Student findStudentById(int studentId) {
        ValidationUtils.validateId(studentId);

        try {
            return studentRepository.findById(studentId)
                    .orElseThrow(() -> new StudentNotFoundException(studentId));
        } catch (SQLException e) {
            throw new RuntimeException("Finding student by id failed.", e);
        }
    }

    public List<Student> findAllStudents() {
        try {
            return studentRepository.findAll()
                    .orElseThrow(() -> new NoStudentsFoundException());
        } catch (SQLException e) {
            throw new RuntimeException("Finding all students failed.", e);
        }
    }
}
