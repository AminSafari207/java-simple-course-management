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

public class CourseRepository {
    private final List<String> validUpdateKeys = List.of("name", "major", "gpa");

    public void create(List<Course> courses) {
        ValidationUtils.validateCollection(courses, "courses");

        String sqlQuery = "insert into course (name, major, gpa) values (?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)
        ) {
            conn.setAutoCommit(false);

            for (Course course: courses) {
                ValidationUtils.validateNotNull(course, "course");
                if (course.isIdSet()) throw new CourseAlreadyExistsException(course.getTitle(), course.getId());

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

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Creating courses in database failed!", e);
        }
    }

    public Course findById(int courseId) {
        ValidationUtils.validateId(courseId);

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

                return course;
            } else {
                throw new CourseNotFoundException(courseId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Finding course by id in database failed.", e);
        }
    }

    public List<Course> findAll() {
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

            if (coursesList.isEmpty()) throw new NoCoursesFoundException();

            return coursesList;
        } catch (SQLException e) {
            throw new RuntimeException("Finding all courses in database failed.", e);
        }
    }

    public void update(int courseId, Map<String, Object> updatesMap) {
        ValidationUtils.validateId(courseId);
        ValidationUtils.validateMap(updatesMap, "updatesMap");

        String sqlQuery = "update course set ";
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

            ps.setInt(idIndex, courseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Updating course in database failed!", e);
        }
    }

    public void delete(int courseId) {
        ValidationUtils.validateId(courseId);

        String sqlQuery = "delete from course where id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
        ) {
            ps.setInt(1, courseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Deleting course from database failed!", e);
        }
    }
}
