package repository;

import config.DBConnection;
import exception.NoCoursesFoundException;
import exception.CourseAlreadyExistsException;
import exception.CourseNotFoundException;
import model.Course;
import model.Student;
import utils.ValidationUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CourseRepository {
    public void create(List<Course> courses) throws SQLException {
        String sqlQuery = "insert into course (title, department, credits) values (?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)
        ) {
            for (Course course: courses) {
                ps.setString(1, course.getTitle());
                ps.setString(2, course.getDepartment());
                ps.setInt(3, course.getCredits());
                ps.addBatch();
            };

            ps.executeBatch();

            ResultSet rs = ps.getGeneratedKeys();
            int i = 0;

            while (rs.next()) {
                courses.get(i++).setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new SQLException("Creating courses in database failed!", e);
        }
    }

    public Optional<Course> findById(int courseId) throws SQLException {
        String sqlQuery = "select * from course where id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
        ) {
            ps.setInt(1, courseId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Course course = new Course(
                        rs.getString("title"),
                        rs.getString("department"),
                        rs.getInt("credits")
                );

                course.setId(rs.getInt("id"));

                return Optional.of(course);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new SQLException("Finding course by id in database failed.", e);
        }
    }

    public Optional<List<Course>> findAll() throws SQLException {
        String sqlQuery = "select * from course";

        try (
                Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            ResultSet rs = stmt.executeQuery(sqlQuery);
            List<Course> coursesList = new ArrayList<>();

            while (rs.next()) {
                Course course = new Course(
                        rs.getString("title"),
                        rs.getString("department"),
                        rs.getInt("credits")
                );

                course.setId(rs.getInt("id"));
                coursesList.add(course);
            }

            if (coursesList.isEmpty()) return Optional.empty();

            return Optional.of(coursesList);
        } catch (SQLException e) {
            throw new SQLException("Finding all courses in database failed.", e);
        }
    }

    public void update(int courseId, Map<String, Object> updateMap) throws SQLException {
        String sqlQuery = "update course set ";
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
                ps.setObject(idIndex++, value);
            }

            ps.setInt(idIndex, courseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Updating course in database failed!", e);
        }
    }

    public void delete(int courseId) throws SQLException {
        String sqlQuery = "delete from course where id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
        ) {
            ps.setInt(1, courseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Deleting course from database failed!", e);
        }
    }
}
