import java.util.Comparator;

public class QuickSort {
    public void sort(int[] a, SortStats s) {
        if (s != null) s.reset();
        quickSort(a, 0, a.length - 1, s);
    }

    private void quickSort(int[] a, int lo, int hi, SortStats s) {
        if (lo >= hi) return;
        int p = partition(a, lo, hi, s);
        quickSort(a, lo, p - 1, s);
        quickSort(a, p + 1, hi, s);
    }

    private int partition(int[] a, int lo, int hi, SortStats s) {
        medianOfThree(a, lo, hi, s);
        int pivot = a[hi], i = lo;
        for (int j = lo; j < hi; j++) {
            if (s != null) s.comparisons++;
            if (a[j] <= pivot) {
                int t = a[i]; a[i] = a[j]; a[j] = t;
                if (s != null && i != j) s.swapsOrMoves++;
                i++;
            }
        }
        int t = a[i]; a[i] = a[hi]; a[hi] = t;
        if (s != null) s.swapsOrMoves++;
        return i;
    }

    public <T> void sort(T[] a, Comparator<T> cmp, SortStats s) {
        if (s != null) s.reset();
        quickSort(a, 0, a.length - 1, cmp, s);
    }

    private <T> void quickSort(T[] a, int lo, int hi, Comparator<T> cmp, SortStats s) {
        if (lo >= hi) return;
        int p = partition(a, lo, hi, cmp, s);
        quickSort(a, lo, p - 1, cmp, s);
        quickSort(a, p + 1, hi, cmp, s);
    }

    private void medianOfThree(int[] a, int lo, int hi, SortStats s) {
        int mid = lo + (hi - lo) / 2;
        if (s != null) s.comparisons += 2;
        if (a[lo] > a[mid]) { int t = a[lo]; a[lo] = a[mid]; a[mid] = t; if (s != null) s.swapsOrMoves++; }
        if (a[mid] > a[hi]) { int t = a[mid]; a[mid] = a[hi]; a[hi] = t; if (s != null) s.swapsOrMoves++; }
        if (a[lo] > a[mid]) { int t = a[lo]; a[lo] = a[mid]; a[mid] = t; if (s != null) s.swapsOrMoves++; }
        int t = a[mid]; a[mid] = a[hi]; a[hi] = t;
        if (s != null) s.swapsOrMoves++;
    }

    private <T> void medianOfThree(T[] a, int lo, int hi, Comparator<T> cmp, SortStats s) {
        int mid = lo + (hi - lo) / 2;
        if (s != null) s.comparisons += 2;
        if (cmp.compare(a[lo], a[mid]) > 0) { T t = a[lo]; a[lo] = a[mid]; a[mid] = t; if (s != null) s.swapsOrMoves++; }
        if (cmp.compare(a[mid], a[hi]) > 0) { T t = a[mid]; a[mid] = a[hi]; a[hi] = t; if (s != null) s.swapsOrMoves++; }
        if (cmp.compare(a[lo], a[mid]) > 0) { T t = a[lo]; a[lo] = a[mid]; a[mid] = t; if (s != null) s.swapsOrMoves++; }
        T t = a[mid]; a[mid] = a[hi]; a[hi] = t;
        if (s != null) s.swapsOrMoves++;
    }

    private <T> int partition(T[] a, int lo, int hi, Comparator<T> cmp, SortStats s) {
        medianOfThree(a, lo, hi, cmp, s);
        T pivot = a[hi];
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (s != null) s.comparisons++;
            if (cmp.compare(a[j], pivot) <= 0) {
                T t = a[i]; a[i] = a[j]; a[j] = t;
                if (s != null && i != j) s.swapsOrMoves++;
                i++;
            }
        }
        T t = a[i]; a[i] = a[hi]; a[hi] = t;
        if (s != null) s.swapsOrMoves++;
        return i;
    }
}
