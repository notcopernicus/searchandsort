import java.util.Comparator;

/**
 * Record type for optional custom comparator demo.
 * Sort by id, then by name as tie-breaker.
 */
public class Student {
    private final int id;
    private final String name;
    private final double gpa;

    public Student(int id, String name, double gpa) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getGpa() { return gpa; }

    /** Compare by id, then by name as tie-breaker. */
    public static final Comparator<Student> BY_ID_THEN_NAME = (a, b) -> {
        int c = Integer.compare(a.id, b.id);
        return c != 0 ? c : a.name.compareTo(b.name);
    };

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', gpa=" + gpa + "}";
    }
}
