import java.util.Comparator;

/** Correctness checks: isSorted. */
public final class SortUtils {
    private SortUtils() {}

    public static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i] < a[i - 1]) return false;
        return true;
    }

    public static <T> boolean isSorted(T[] a, Comparator<T> cmp) {
        for (int i = 1; i < a.length; i++)
            if (cmp.compare(a[i], a[i - 1]) < 0) return false;
        return true;
    }
}
