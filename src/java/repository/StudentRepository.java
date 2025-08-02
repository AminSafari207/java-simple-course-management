package repository;

import config.DBConnection;
import exception.StudentAlreadyExistsException;
import model.Student;
import utils.ValidationUtils;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class StudentRepository {
    private final List<String> validUpdateKeys = List.of("name", "major", "gpa");

    public void createStudents(List<Student> students) {
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
                ps.setDouble(3, s.getGpa());
                ps.addBatch();
            };

            ps.executeBatch();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                int i = 0;

                while (rs.next()) {
                    students.get(i++).setId(rs.getInt(1));
                }
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Creating students in database failed!", e);
        }
    }

    public void updateStudent(int studentId, Map<String, Object> updatesMap) {
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
