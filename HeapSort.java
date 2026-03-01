import java.util.Comparator;

/**
 * Heap Sort: array-backed binary heap with heapify, siftDown, buildHeap.
 * In-place, not stable.
 */
public class HeapSort {
    public void sort(int[] a, SortStats s) {
        if (s != null) s.reset();
        buildHeap(a, s);
        for (int end = a.length - 1; end > 0; end--) {
            swap(a, 0, end);
            if (s != null) s.swapsOrMoves++;
            siftDown(a, 0, end, s);
        }
    }

    private void buildHeap(int[] a, SortStats s) {
        for (int i = (a.length - 2) / 2; i >= 0; i--)
            siftDown(a, i, a.length, s);
    }

    private void siftDown(int[] a, int i, int size, SortStats s) {
        while (true) {
            int left = 2 * i + 1, right = 2 * i + 2, largest = i;
            if (left < size) {
                if (s != null) s.comparisons++;
                if (a[left] > a[largest]) largest = left;
            }
            if (right < size) {
                if (s != null) s.comparisons++;
                if (a[right] > a[largest]) largest = right;
            }
            if (largest == i) break;
            swap(a, i, largest);
            if (s != null) s.swapsOrMoves++;
            i = largest;
        }
    }

    private void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }

    // ----- Generic (Comparator) -----
    public <T> void sort(T[] a, Comparator<T> cmp, SortStats s) {
        if (s != null) s.reset();
        buildHeap(a, cmp, s);
        for (int end = a.length - 1; end > 0; end--) {
            swap(a, 0, end);
            if (s != null) s.swapsOrMoves++;
            siftDown(a, 0, end, cmp, s);
        }
    }

    private <T> void buildHeap(T[] a, Comparator<T> cmp, SortStats s) {
        for (int i = (a.length - 2) / 2; i >= 0; i--)
            siftDown(a, i, a.length, cmp, s);
    }

    private <T> void siftDown(T[] a, int i, int size, Comparator<T> cmp, SortStats s) {
        while (true) {
            int left = 2 * i + 1, right = 2 * i + 2, largest = i;
            if (left < size) {
                if (s != null) s.comparisons++;
                if (cmp.compare(a[left], a[largest]) > 0) largest = left;
            }
            if (right < size) {
                if (s != null) s.comparisons++;
                if (cmp.compare(a[right], a[largest]) > 0) largest = right;
            }
            if (largest == i) break;
            swap(a, i, largest);
            if (s != null) s.swapsOrMoves++;
            i = largest;
        }
    }

    private <T> void swap(T[] a, int i, int j) {
        T t = a[i]; a[i] = a[j]; a[j] = t;
    }
}
