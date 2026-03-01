import java.util.Comparator;

/**
 * Sequential (linear) search on an unsorted array.
 * Returns index of first match or -1; counts comparisons.
 */
public class LinearSearch {
    private long comparisons;

    public long getComparisons() { return comparisons; }

    public int search(int[] a, int key) {
        comparisons = 0;
        for (int i = 0; i < a.length; i++) {
            comparisons++;
            if (a[i] == key) return i;
        }
        return -1;
    }

    public <T> int search(T[] a, T key, Comparator<T> cmp) {
        comparisons = 0;
        for (int i = 0; i < a.length; i++) {
            comparisons++;
            if (cmp.compare(a[i], key) == 0) return i;
        }
        return -1;
    }
}
