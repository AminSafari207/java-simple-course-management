package service;

import exception.EnrollmentAlreadyExistsException;
import exception.EnrollmentNotFoundException;
import exception.NoEnrollmentsFoundException;
import model.Enrollment;
import repository.EnrollmentRepository;
import utils.ValidationUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    private final List<String> validUpdateKeys = List.of("course_id", "grade");

    public EnrollmentService() {
        enrollmentRepository = new EnrollmentRepository();
    }

    public void registerEnrollment(List<Enrollment> enrollmentList) {
        ValidationUtils.validateCollection(enrollmentList, "enrollmentList");

        for (Enrollment enrollment: enrollmentList) {
            ValidationUtils.validateNotNull(enrollment, "enrollment");
            if (enrollment.isIdSet()) throw new EnrollmentAlreadyExistsException("Enrollment with ID '" + enrollment.getId() + "' already exists.");
        }

        try {
            enrollmentRepository.create(enrollmentList);
        } catch (SQLException e) {
            throw new RuntimeException("Registering enrollment failed.", e);
        }
    }

    public void updateEnrollment(int enrollmentId, Map<String, Object> updateMap) {
        ValidationUtils.validateId(enrollmentId);
        ValidationUtils.validateMap(updateMap, "updateMap");

        for (String key: updateMap.keySet()) {
            if (!validUpdateKeys.contains(key)) throw new IllegalArgumentException("Key '" + key + "' is not valid.");
        }

        try {
            enrollmentRepository.update(enrollmentId, updateMap);
        } catch (SQLException e) {
            throw new RuntimeException("Updating enrollment failed.", e);
        }
    }

    public void removeEnrollment(int enrollmentId) {
        ValidationUtils.validateId(enrollmentId);

        try {
            enrollmentRepository.delete(enrollmentId);
        } catch (SQLException e) {
            throw new RuntimeException("Removing enrollment failed.", e);
        }
    }

    public Enrollment findEnrollment(int studentId, int courseId) {
        ValidationUtils.validateId(studentId);
        ValidationUtils.validateId(courseId);

        try {
            return enrollmentRepository.find(studentId, courseId)
                    .orElseThrow(() -> new EnrollmentNotFoundException(studentId, courseId));
        } catch (SQLException e) {
            throw new RuntimeException("Finding enrollment by student id and course id failed.", e);
        }
    }

    public Enrollment findEnrollmentById(int enrollmentId) {
        ValidationUtils.validateId(enrollmentId);

        try {
            return enrollmentRepository.findById(enrollmentId)
                    .orElseThrow(() -> new EnrollmentNotFoundException(enrollmentId)) ;
        } catch (SQLException e) {
            throw new RuntimeException("Finding enrollment by id failed.", e);
        }
    }

    public List<Enrollment> findAllEnrollments() {
        try {
            return enrollmentRepository.findAll()
                    .orElseThrow(() -> new NoEnrollmentsFoundException());
        } catch (SQLException e) {
            throw new RuntimeException("Finding all enrollments failed.", e);
        }
    }
}
