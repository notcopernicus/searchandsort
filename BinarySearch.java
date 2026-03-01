import java.util.Comparator;

public class BinarySearch {
    private long comparisons;

    public long getComparisons() { return comparisons; }

    public int searchIterative(int[] a, int key) {
        comparisons = 0;
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            comparisons++;
            int c = Integer.compare(a[mid], key);
            if (c == 0) return mid;
            if (c < 0) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    public int searchRecursive(int[] a, int key) {
        comparisons = 0;
        return searchRec(a, key, 0, a.length - 1);
    }

    private int searchRec(int[] a, int key, int lo, int hi) {
        if (lo > hi) return -1;
        int mid = lo + (hi - lo) / 2;
        comparisons++;
        int c = Integer.compare(a[mid], key);
        if (c == 0) return mid;
        if (c < 0) return searchRec(a, key, mid + 1, hi);
        return searchRec(a, key, lo, mid - 1);
    }

    public <T> int searchIterative(T[] a, T key, Comparator<T> cmp) {
        comparisons = 0;
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            comparisons++;
            int c = cmp.compare(a[mid], key);
            if (c == 0) return mid;
            if (c < 0) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }
}
