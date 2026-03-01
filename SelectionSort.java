import java.util.Comparator;

/**
 * Selection Sort: elementary comparison sort.
 * In-place, not stable. O(n^2) comparisons, O(n) swaps.
 */
public class SelectionSort {
    public void sort(int[] a, SortStats s) {
        if (s != null) s.reset();
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (s != null) s.comparisons++;
                if (a[j] < a[min]) min = j;
            }
            if (min != i) {
                int t = a[i]; a[i] = a[min]; a[min] = t;
                if (s != null) s.swapsOrMoves++;
            }
        }
    }

    public <T> void sort(T[] a, Comparator<T> cmp, SortStats s) {
        if (s != null) s.reset();
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                if (s != null) s.comparisons++;
                if (cmp.compare(a[j], a[min]) < 0) min = j;
            }
            if (min != i) {
                T t = a[i]; a[i] = a[min]; a[min] = t;
                if (s != null) s.swapsOrMoves++;
            }
        }
    }
}
