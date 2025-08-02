package repository;

import config.DBConnection;
import exception.StudentAlreadyExistsException;
import model.Student;
import utils.ValidationUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StudentRepository {
    public void createStudents(List<Student> students) {
        ValidationUtils.validateObjectList(students, "students");

        String sqlQuery = "insert into student (name, major, gpa) values (?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlQuery)
                ) {
            for (Student s: students) {
                ValidationUtils.validateNotNullObject(s, "student");
                if (s.isIdSet()) throw new StudentAlreadyExistsException(s.getName(), s.getId());

                ps.setString(1, s.getName());
                ps.setString(2, s.getMajor());
                ps.setDouble(3, s.getGpa());
                ps.addBatch();
            };

            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Creating students in database failed!", e);
        }
    }
}
