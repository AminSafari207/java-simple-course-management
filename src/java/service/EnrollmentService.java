package service;

import exception.EnrollmentAlreadyExistsException;
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
}
