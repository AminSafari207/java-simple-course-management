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
import java.util.Optional;

public class EnrollmentRepository {
    public void create(List<Enrollment> enrollmentList) throws SQLException {
        String sqlQuery = "insert into enrollment (student_id, course_id, enrollment_date, credits) values (?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)
        ) {
            for (Enrollment enrollment: enrollmentList) {
                ps.setInt(1, enrollment.getStudentId());
                ps.setInt(2, enrollment.getCourseId());
                ps.setDate(3, Date.valueOf(enrollment.getDate()));
                ps.setInt(4, enrollment.getGrade());
                ps.addBatch();
            };

            ps.executeBatch();

            ResultSet rs = ps.getGeneratedKeys();
            int i = 0;

            while (rs.next()) enrollmentList.get(i++).setId(rs.getInt(1));
        } catch (SQLException e) {
            throw new SQLException("Creating enrollments in database failed!", e);
        }
    }

    public Optional<Enrollment> find(int studentId, int courseId) throws SQLException {
        String sqlQuery = "select * from enrollment where student_id = ? and course_id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
                ){
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Enrollment enrollment = new Enrollment(
                        studentId,
                        courseId,
                        rs.getInt("grade"),
                        rs.getDate("date").toLocalDate()
                );

                enrollment.setId(rs.getInt("id"));

                return Optional.of(enrollment);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLException("Finding enrollment by student id and course id in database failed.", e);
        }
    }

    public Optional<Enrollment> findById(int enrollmentId) throws SQLException {
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

                return Optional.of(enrollment);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLException("Finding enrollment by id in database failed.", e);
        }
    }

    public Optional<List<Enrollment>> findAll() throws SQLException {
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

            if (enrollmentsList.isEmpty()) return Optional.empty();

            return Optional.of(enrollmentsList);
        } catch (SQLException e) {
            throw new SQLException("Finding all enrollments in database failed.", e);
        }
    }

    public void update(int enrollmentId, Map<String, Object> updateMap) throws SQLException {
        String sqlQuery = "update enrollment set ";
        int updateCount = 0;

        for (String key: updateMap.keySet()) {
            sqlQuery += key + " = ?";
            updateCount++;

            if (updateCount < updateMap.size()) sqlQuery += ", ";
        }

        sqlQuery += "where id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
        ) {
            int idIndex = 1;

            for (String key: updateMap.keySet()) {
                Object value = updateMap.get(key);
                ps.setObject(idIndex, value);
            }

            ps.setInt(idIndex, enrollmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Updating enrollment in database failed!", e);
        }
    }

    public void delete(int enrollmentId) throws SQLException {
        String sqlQuery = "delete from enrollment where id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
        ) {
            ps.setInt(1, enrollmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Deleting enrollment from database failed!", e);
        }
    }
}
