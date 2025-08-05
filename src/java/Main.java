import model.Course;
import model.Student;
import model.Enrollment;
import service.CourseService;
import service.EnrollmentService;
import service.StudentService;
import utils.PrintUtils;
import utils.SqlUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Main {
    private static StudentService studentService = new StudentService();
    private static CourseService courseService = new CourseService();
    private static EnrollmentService enrollmentService = new EnrollmentService();

    public static void main(String[] args) {
        SqlUtils.truncateTables("student", "course", "enrollment");

        List<Student> students = buildStudentList();
        List<Course> courses = buildCourseList();

        studentService.registerStudents(students);
        courseService.registerCourses(courses);

        List<Enrollment> enrollments = buildEnrollmentList(students, courses);

        enrollmentService.registerEnrollment(enrollments);

//        List<Student> filteredStudents = studentService.findAndFilterStudents(s -> s.getGpa() > 3.5);
//        long studentsCount = studentService.countStudents(s -> LocalDate.now().getYear() - s.getYear() > 24);

//        PrintUtils.printList(filteredStudents, "Students GPA > 3.5");
//        System.out.println("Students older than 24 years old count: " + studentsCount);
//        printStudentsByGpaAndDepartment(3.5, "Computer Science");
//        printStudentsByCourseIdAfterDate(4);
//        printCoursesWithAverageHigherThan(85);
//        printStudentsNameInUppercase(s -> {
//            System.out.println();
//            System.out.println(s.getName().toUpperCase());
//            System.out.println();
//            System.out.println("-----------------");
//        });

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

    public static void printStudentsByGpaAndDepartment(double gpa, String department) {
        List<Integer> studentIdsByDepartment = enrollmentService.getStudentIdsByDepartment(department);

        List<Student> students = studentService.findAndFilterStudents(
                student -> student.getGpa() > gpa && studentIdsByDepartment.contains(student.getId())
        );

        PrintUtils.printList(students, "Students by gpa and department");
    }

    public static void printStudentsByCourseIdAfterDate(int courseId) {
        LocalDate date = LocalDate.of(2024, 1, 1);

        List<Student> students = enrollmentService.findStudentsByEnrollment(
                enrollment -> enrollment.getCourseId() == courseId && enrollment.getDate().isAfter(date)
        );

        PrintUtils.printList(students, "Students by course id after date");
    }

    public static void printCoursesWithAverageHigherThan(double minAvg) {
        List<Course> courses = courseService.findAllCourses();
        List<Enrollment> enrollments = new EnrollmentService().findAllEnrollments();

        Map<Integer, List<Integer>> courseIdWithGrades = new HashMap<>();

        for (Enrollment enrollment: enrollments) {
            int courseId = enrollment.getId();
            int grade = enrollment.getGrade();

            if (!courseIdWithGrades.containsKey(courseId)) {
                courseIdWithGrades.put(courseId, new ArrayList<>());
            }

            courseIdWithGrades.get(courseId).add(grade);
        }

        System.out.println("-------------------------");
        System.out.println();

        courses
                .stream()
                .forEach(course -> {
                    List<Integer> gradeList = courseIdWithGrades.get(course.getId());

                    double avg = gradeList
                            .stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0);

                    if (avg > minAvg) {
                        System.out.println(course.toString() + "\nAverage Grade: " + avg);
                        System.out.println();
                        System.out.println("-------------------------");
                        System.out.println();
                    }
                });
    }

    public static void printStudentsNameInUppercase(Consumer<Student> consumer) {
        List<Student> students = studentService.findAllStudents();

        students
                .stream()
                .forEach(consumer);
    }
}
