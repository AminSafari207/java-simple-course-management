import model.Course;
import model.Student;
import model.Enrollment;
import service.CourseService;
import service.EnrollmentService;
import service.StudentService;
import utils.SqlUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        SqlUtils.truncateTables("student", "course", "enrollment");

        StudentService studentService = new StudentService();
        CourseService courseService = new CourseService();
        EnrollmentService enrollmentService = new EnrollmentService();

        List<Student> students = buildStudentList();
        List<Course> courses = buildCourseList();

        studentService.registerStudents(students);
        courseService.registerCourses(courses);

        List<Enrollment> enrollments = buildEnrollmentList(students, courses);

        enrollmentService.registerEnrollment(enrollments);


    }

    private static List<Student> buildStudentList() {
        return List.of(
                new Student("Ali Reza", "Computer Science", 2001, 3.8),
                new Student("Sara Jafari", "Mathematics", 2002, 3.4),
                new Student("Mehdi Mahdavi", "Physics", 2000, 3.0),
                new Student("Narges Sadeghi", "Biology", 2001, 3.2),
                new Student("Amir Hossein", "Chemistry", 2003, 2.8),
                new Student("Laleh Moradi", "Computer Science", 2000, 3.6),
                new Student("Reza Ghasemi", "Engineering", 1999, 3.1),
                new Student("Maryam Ranjbar", "History", 2002, 3.9),
                new Student("Kaveh Jahan", "Philosophy", 2001, 2.5),
                new Student("Shirin Khosravi", "Literature", 2003, 3.7)
        );
    }

    private static List<Course> buildCourseList() {
        return List.of(
                new Course("Data Structures", "Computer Science", 3),
                new Course("Organic Chemistry", "Chemistry", 4),
                new Course("Modern Algebra", "Mathematics", 3),
                new Course("Quantum Mechanics", "Physics", 4),
                new Course("World Literature", "Literature", 2)
        );
    }

    private static List<Enrollment> buildEnrollmentList(List<Student> students, List<Course> courses) {
        return List.of(
                new Enrollment(students.get(0).getId(), courses.get(0).getId(), 88),
                new Enrollment(students.get(1).getId(), courses.get(0).getId(), 92),
                new Enrollment(students.get(2).getId(), courses.get(1).getId(), 75),
                new Enrollment(students.get(3).getId(), courses.get(2).getId(), 67),
                new Enrollment(students.get(4).getId(), courses.get(3).getId(), 81),
                new Enrollment(students.get(5).getId(), courses.get(0).getId(), 90),
                new Enrollment(students.get(6).getId(), courses.get(4).getId(), 74),
                new Enrollment(students.get(7).getId(), courses.get(2).getId(), 85),
                new Enrollment(students.get(8).getId(), courses.get(1).getId(), 70),
                new Enrollment(students.get(9).getId(), courses.get(3).getId(), 66),
                new Enrollment(students.get(0).getId(), courses.get(1).getId(), 83),
                new Enrollment(students.get(1).getId(), courses.get(2).getId(), 88),
                new Enrollment(students.get(2).getId(), courses.get(3).getId(), 79),
                new Enrollment(students.get(3).getId(), courses.get(4).getId(), 91),
                new Enrollment(students.get(4).getId(), courses.get(0).getId(), 85)
        );
    }
}
