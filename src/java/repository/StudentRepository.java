package repository;

import config.DBConnection;
import exception.NoStudentsFoundException;
import exception.StudentAlreadyExistsException;
import exception.StudentNotFoundException;
import model.Student;
import utils.ValidationUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentRepository {
    private final List<String> validUpdateKeys = List.of("name", "major", "gpa");

    public void create(List<Student> students) throws SQLException {
        String sqlQuery = "insert into student (name, major, gpa) values (?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)
        ) {
            for (Student s: students) {
                ps.setString(1, s.getName());
                ps.setString(2, s.getMajor());
                ps.setInt(3, s.getYear());
                ps.setDouble(4, s.getGpa());
                ps.addBatch();
            };

            ps.executeBatch();

            ResultSet rs = ps.getGeneratedKeys();
            int i = 0;

            while (rs.next()) students.get(i++).setId(rs.getInt(1));
        } catch (SQLException e) {
            throw new SQLException("Creating students in database failed!");
        }
    }

    public Student findById(int studentId) {
        ValidationUtils.validateId(studentId);

        String sqlQuery = "select * from student where id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
        ) {
            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Student s = new Student(
                        rs.getString("name"),
                        rs.getString("major"),
                        rs.getInt("year"),
                        rs.getDouble("gpa")
                );

                s.setId(rs.getInt("id"));

                return s;
            } else {
                throw new StudentNotFoundException(studentId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Finding student by id in database failed.", e);
        }
    }

    public List<Student> findAll() {
        String sqlQuery = "select * from student";

        try (
                Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            ResultSet rs = stmt.executeQuery(sqlQuery);
            List<Student> studentsList = new ArrayList<>();

            while (rs.next()) {
                Student s = new Student(
                        rs.getString("name"),
                        rs.getString("major"),
                        rs.getInt("year"),
                        rs.getDouble("gpa")
                );

                s.setId(rs.getInt("id"));
                studentsList.add(s);
            }

            if (studentsList.isEmpty()) throw new NoStudentsFoundException();

            return studentsList;
        } catch (SQLException e) {
            throw new RuntimeException("Finding all students in database failed.", e);
        }
    }

    public void update(int studentId, Map<String, Object> updatesMap) throws SQLException {
        String sqlQuery = "update student set ";
        int updateCount = 0;

        for (String key: updatesMap.keySet()) {
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

            ps.setInt(idIndex, studentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Updating student in database failed!");
        }
    }

    public void delete(int studentId) {
        ValidationUtils.validateId(studentId);

        String sqlQuery = "delete from student where id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
        ) {
            ps.setInt(1, studentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Deleting student from database failed!", e);
        }
    }
}
