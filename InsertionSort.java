import java.util.Comparator;

/**
 * Insertion Sort: elementary comparison sort.
 * In-place, stable. Good for nearly-sorted data.
 */
public class InsertionSort {
    public void sort(int[] a, SortStats s) {
        if (s != null) s.reset();
        for (int i = 1; i < a.length; i++) {
            int v = a[i], j = i - 1;
            while (j >= 0) {
                if (s != null) s.comparisons++;
                if (a[j] <= v) break;
                a[j + 1] = a[j];
                if (s != null) s.swapsOrMoves++;
                j--;
            }
            a[j + 1] = v;
        }
    }

    public <T> void sort(T[] a, Comparator<T> cmp, SortStats s) {
        if (s != null) s.reset();
        for (int i = 1; i < a.length; i++) {
            T v = a[i];
            int j = i - 1;
            while (j >= 0) {
                if (s != null) s.comparisons++;
                if (cmp.compare(a[j], v) <= 0) break;
                a[j + 1] = a[j];
                if (s != null) s.swapsOrMoves++;
                j--;
            }
            a[j + 1] = v;
        }
    }
}
