import java.util.Comparator;
import java.util.Arrays;


public class BaselineSort {
    public void sort(int[] a, SortStats s) {
        if (s != null) s.reset();
        Arrays.sort(a);
    }

    public <T> void sort(T[] a, Comparator<T> cmp, SortStats s) {
        if (s != null) s.reset();
        Arrays.sort(a, cmp);
    }
}
