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

    public void create(List<Student> students) {
        ValidationUtils.validateCollection(students, "students");

        String sqlQuery = "insert into student (name, major, gpa) values (?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)
        ) {
            conn.setAutoCommit(false);

            for (Student s: students) {
                ValidationUtils.validateNotNull(s, "student");
                if (s.isIdSet()) throw new StudentAlreadyExistsException(s.getName(), s.getId());

                ps.setString(1, s.getName());
                ps.setString(2, s.getMajor());
                ps.setInt(3, s.getYear());
                ps.setDouble(4, s.getGpa());
                ps.addBatch();
            };

            ps.executeBatch();

            ResultSet rs = ps.getGeneratedKeys();
            int i = 0;

            while (rs.next()) {
                students.get(i++).setId(rs.getInt(1));
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Creating students in database failed!", e);
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
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("major"),
                        rs.getInt("year"),
                        rs.getDouble("gpa")
                );
            } else {
                throw new StudentNotFoundException(studentId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Finding student by id failed.", e);
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
                studentsList.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("major"),
                        rs.getInt("year"),
                        rs.getDouble("gpa")
                ));
            }

            if (studentsList.isEmpty()) throw new NoStudentsFoundException();

            return studentsList;
        } catch (SQLException e) {
            throw new RuntimeException("Finding student by id failed.", e);
        }
    }

    public void update(int studentId, Map<String, Object> updatesMap) {
        ValidationUtils.validateId(studentId);
        ValidationUtils.validateMap(updatesMap, "updatesMap");

        String sqlQuery = "update student set ";
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

            ps.setInt(idIndex, studentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Creating students in database failed!", e);
        }
    }
}
