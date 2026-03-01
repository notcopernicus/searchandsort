import java.util.Comparator;

/**
 * Merge Sort: top-down divide-and-conquer. Uses auxiliary array.
 * Stable. O(n log n). Not in-place (extra buffer).
 */
public class MergeSort {
    @SuppressWarnings("unchecked")
    public <T> void sort(T[] a, Comparator<T> cmp, SortStats s) {
        if (s != null) s.reset();
        T[] aux = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), a.length);
        mergeSort(a, aux, 0, a.length - 1, cmp, s);
    }

    public void sort(int[] a, SortStats s) {
        if (s != null) s.reset();
        int[] aux = new int[a.length];
        mergeSortInt(a, aux, 0, a.length - 1, s);
    }

    private void mergeSortInt(int[] a, int[] aux, int lo, int hi, SortStats s) {
        if (lo >= hi) return;
        int mid = lo + (hi - lo) / 2;
        mergeSortInt(a, aux, lo, mid, s);
        mergeSortInt(a, aux, mid + 1, hi, s);
        mergeInt(a, aux, lo, mid, hi, s);
    }

    private void mergeInt(int[] a, int[] aux, int lo, int mid, int hi, SortStats s) {
        for (int k = lo; k <= hi; k++) aux[k] = a[k];
        if (s != null) s.swapsOrMoves += (hi - lo + 1);
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else {
                if (s != null) s.comparisons++;
                if (aux[j] < aux[i]) a[k] = aux[j++];
                else a[k] = aux[i++];
            }
        }
    }

    private <T> void mergeSort(T[] a, T[] aux, int lo, int hi, Comparator<T> cmp, SortStats s) {
        if (lo >= hi) return;
        int mid = lo + (hi - lo) / 2;
        mergeSort(a, aux, lo, mid, cmp, s);
        mergeSort(a, aux, mid + 1, hi, cmp, s);
        merge(a, aux, lo, mid, hi, cmp, s);
    }

    private <T> void merge(T[] a, T[] aux, int lo, int mid, int hi, Comparator<T> cmp, SortStats s) {
        for (int k = lo; k <= hi; k++) aux[k] = a[k];
        if (s != null) s.swapsOrMoves += (hi - lo + 1);
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else {
                if (s != null) s.comparisons++;
                if (cmp.compare(aux[j], aux[i]) < 0) a[k] = aux[j++];
                else a[k] = aux[i++];
            }
        }
    }
}
