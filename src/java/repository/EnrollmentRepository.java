package repository;

import config.DBConnection;
import exception.EnrollmentAlreadyExistsException;
import exception.EnrollmentNotFoundException;
import exception.NoEnrollmentsFoundException;
import model.Enrollment;
import utils.ValidationUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnrollmentRepository {
    private final List<String> validUpdateKeys = List.of("grade");

    public void create(List<Enrollment> enrollments) {
        ValidationUtils.validateCollection(enrollments, "enrollments");

        String sqlQuery = "insert into enrollment (student_id, course_id, enrollment_date, credits) values (?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)
        ) {
            conn.setAutoCommit(false);

            for (Enrollment enrollment: enrollments) {
                ValidationUtils.validateNotNull(enrollment, "enrollment");
                if (enrollment.isIdSet()) throw new EnrollmentAlreadyExistsException("Enrollment with ID '" + enrollment.getId() + "' already exists.");

                ps.setInt(1, enrollment.getStudentId());
                ps.setInt(2, enrollment.getCourseId());
                ps.setDate(3, Date.valueOf(enrollment.getDate()));
                ps.setInt(4, enrollment.getGrade());
                ps.addBatch();
            };

            ps.executeBatch();

            ResultSet rs = ps.getGeneratedKeys();
            int i = 0;

            while (rs.next()) {
                enrollments.get(i++).setId(rs.getInt(1));
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Creating enrollments in database failed!", e);
        }
    }

    public Enrollment findById(int enrollmentId) {
        ValidationUtils.validateId(enrollmentId);

        String sqlQuery = "select * from enrollment where id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
        ) {
            ps.setInt(1, enrollmentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Enrollment enrollment = new Enrollment(
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getInt("grade"),
                        rs.getDate("date").toLocalDate()
                );

                enrollment.setId(rs.getInt("id"));

                return enrollment;
            } else {
                throw new EnrollmentNotFoundException(enrollmentId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Finding enrollment by id in database failed.", e);
        }
    }

    public List<Enrollment> findAll() {
        String sqlQuery = "select * from enrollment";

        try (
                Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            ResultSet rs = stmt.executeQuery(sqlQuery);
            List<Enrollment> enrollmentsList = new ArrayList<>();

            while (rs.next()) {
                Enrollment enrollment = new Enrollment(
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getInt("grade"),
                        rs.getDate("date").toLocalDate()
                );

                enrollment.setId(rs.getInt("id"));
                enrollmentsList.add(enrollment);
            }

            if (enrollmentsList.isEmpty()) throw new NoEnrollmentsFoundException();

            return enrollmentsList;
        } catch (SQLException e) {
            throw new RuntimeException("Finding all enrollments in database failed.", e);
        }
    }

    public void update(int enrollmentId, Map<String, Object> updatesMap) {
        ValidationUtils.validateId(enrollmentId);
        ValidationUtils.validateMap(updatesMap, "updatesMap");

        String sqlQuery = "update enrollment set ";
        int updateCount = 0;

        for (String key: updatesMap.keySet()) {
            if (!validUpdateKeys.contains(key)) throw new IllegalArgumentException("Key '" + key + "' is not valid.");

            sqlQuery += key + " = ?";
            updateCount++;

            if (updateCount < updatesMap.size()) sqlQuery += ", ";
        }

        sqlQuery += "where id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
        ) {
            int idIndex = 1;

            for (String key: updatesMap.keySet()) {
                Object value = updatesMap.get(key);
                ps.setObject(idIndex, value);
            }

            ps.setInt(idIndex, enrollmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Updating enrollment in database failed!", e);
        }
    }

    public void delete(int enrollmentId) {
        ValidationUtils.validateId(enrollmentId);

        String sqlQuery = "delete from enrollment where id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
        ) {
            ps.setInt(1, enrollmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Deleting enrollment from database failed!", e);
        }
    }
}
