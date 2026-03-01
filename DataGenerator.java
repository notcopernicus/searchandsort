import java.util.Random;

/**
 * Generates datasets: integers, strings, and optional Student records
 * with distributions: random, nearly-sorted, reverse-sorted, high-duplicate.
 */
public class DataGenerator {
    private static final String[] TECH_WORDS = {
        "Apple", "Bloomberg", "Gemini", "Blackstone", "Nvidia",
        "Google", "Microsoft", "Amazon", "Meta", "Tesla",
        "Oracle", "Adobe", "Salesforce", "Netflix", "Intel"
    };

    private final Random r;

    public DataGenerator() { this(new Random()); }
    public DataGenerator(long seed) { this(new Random(seed)); }
    public DataGenerator(Random r) { this.r = r; }

    public enum Distribution { RANDOM, NEARLY_SORTED, REVERSE_SORTED, HIGH_DUPLICATE }

    // ---------- Integers ----------
    public int[] generateInts(int n, Distribution dist) {
        int[] a = new int[n];
        switch (dist) {
            case RANDOM:
                for (int i = 0; i < n; i++) a[i] = r.nextInt();
                break;
            case NEARLY_SORTED:
                for (int i = 0; i < n; i++) a[i] = i;
                shuffleFraction(a, 0.05 + r.nextDouble() * 0.05); // 5–10% shuffled
                break;
            case REVERSE_SORTED:
                for (int i = 0; i < n; i++) a[i] = n - 1 - i;
                break;
            case HIGH_DUPLICATE:
                int range = Math.max(1, n / 10);
                for (int i = 0; i < n; i++) a[i] = r.nextInt(range);
                break;
        }
        return a;
    }

    private void shuffleFraction(int[] a, double fraction) {
        int swaps = (int) (a.length * fraction);
        for (int i = 0; i < swaps; i++) {
            int j = r.nextInt(a.length);
            int k = r.nextInt(a.length);
            int t = a[j]; a[j] = a[k]; a[k] = t;
        }
    }

    // ---------- Strings ----------
    public String[] generateStrings(int n, Distribution dist) {
        String[] a = new String[n];
        for (int i = 0; i < n; i++) {
            a[i] = TECH_WORDS[r.nextInt(TECH_WORDS.length)];
        }
        if (dist == Distribution.RANDOM) return a;
        java.util.Arrays.sort(a);
        if (dist == Distribution.REVERSE_SORTED) reverse(a);
        else if (dist == Distribution.NEARLY_SORTED) shuffleFraction(a, 0.07);
        // HIGH_DUPLICATE: already many duplicates from small word list
        return a;
    }

    private void shuffleFraction(String[] a, double fraction) {
        int swaps = (int) (a.length * fraction);
        for (int i = 0; i < swaps; i++) {
            int j = r.nextInt(a.length);
            int k = r.nextInt(a.length);
            String t = a[j]; a[j] = a[k]; a[k] = t;
        }
    }

    private void reverse(String[] a) {
        for (int i = 0, j = a.length - 1; i < j; i++, j--) {
            String t = a[i]; a[i] = a[j]; a[j] = t;
        }
    }

    // ---------- Students ----------
    public Student[] generateStudents(int n, Distribution dist) {
        Student[] a = new Student[n];
        for (int i = 0; i < n; i++) {
            int id = dist == Distribution.RANDOM ? r.nextInt(1_000_000) : i;
            String name = TECH_WORDS[r.nextInt(TECH_WORDS.length)] + "-" + i;
            double gpa = 2.0 + r.nextDouble() * 2.0;
            a[i] = new Student(id, name, gpa);
        }
        if (dist == Distribution.NEARLY_SORTED || dist == Distribution.REVERSE_SORTED) {
            java.util.Arrays.sort(a, Student.BY_ID_THEN_NAME);
            if (dist == Distribution.REVERSE_SORTED) reverse(a);
            else shuffleFraction(a, 0.07);
        } else if (dist == Distribution.HIGH_DUPLICATE) {
            int range = Math.max(1, n / 10);
            for (int i = 0; i < n; i++) {
                Student s = a[i];
                a[i] = new Student(r.nextInt(range), s.getName(), s.getGpa());
            }
        }
        return a;
    }

    private void shuffleFraction(Student[] a, double fraction) {
        int swaps = (int) (a.length * fraction);
        for (int i = 0; i < swaps; i++) {
            int j = r.nextInt(a.length);
            int k = r.nextInt(a.length);
            Student t = a[j]; a[j] = a[k]; a[k] = t;
        }
    }

    private void reverse(Student[] a) {
        for (int i = 0, j = a.length - 1; i < j; i++, j--) {
            Student t = a[i]; a[i] = a[j]; a[j] = t;
        }
    }
}
