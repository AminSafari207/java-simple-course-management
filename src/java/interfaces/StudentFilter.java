package interfaces;

import model.Student;

public interface StudentFilter {
    boolean matches(Student s);
}
