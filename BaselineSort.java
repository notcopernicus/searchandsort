import java.util.Comparator;
import java.util.Arrays;

/**
 * Baseline using Arrays.sort for comparison only.
 * Labeled clearly: not our implementation; for empirical comparison.
 */
public class BaselineSort {
    public void sort(int[] a, SortStats s) {
        if (s != null) s.reset();
        Arrays.sort(a);
        // We do not count internal comparisons/swaps; leave at 0 for baseline.
    }

    public <T> void sort(T[] a, Comparator<T> cmp, SortStats s) {
        if (s != null) s.reset();
        Arrays.sort(a, cmp);
    }
}
