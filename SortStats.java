/** Mutable counters for comparisons and swaps/moves during sort. */
public class SortStats {
    public long comparisons;
    public long swapsOrMoves;

    public void reset() {
        comparisons = 0;
        swapsOrMoves = 0;
    }
}
