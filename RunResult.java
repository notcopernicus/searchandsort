/**
 * Metrics for one run: N, data type, distribution, algorithm,
 * comparisons, swaps/moves, elapsed time (ms), in-place vs extra buffer.
 */
public class RunResult {
    public final int n;
    public final String dataType;
    public final String distribution;
    public final String algorithm;
    public final long comparisons;
    public final long swapsOrMoves;
    public final long timeMs;
    public final boolean inPlace;
    public final boolean sorted;  // correctness

    public RunResult(int n, String dataType, String distribution, String algorithm,
                     long comparisons, long swapsOrMoves, long timeMs, boolean inPlace, boolean sorted) {
        this.n = n;
        this.dataType = dataType;
        this.distribution = distribution;
        this.algorithm = algorithm;
        this.comparisons = comparisons;
        this.swapsOrMoves = swapsOrMoves;
        this.timeMs = timeMs;
        this.inPlace = inPlace;
        this.sorted = sorted;
    }

    public String toTableRow() {
        return String.format("%6d | %-6s | %-14s | %-12s | %10d | %10d | %6d ms | %-8s | %s",
            n, dataType, distribution, algorithm, comparisons, swapsOrMoves, timeMs,
            inPlace ? "in-place" : "aux buffer", sorted ? "OK" : "FAIL");
    }

    public String toCsvRow() {
        return n + "," + dataType + "," + distribution + "," + algorithm + ","
            + comparisons + "," + swapsOrMoves + "," + timeMs + "," + (inPlace ? "true" : "false") + "," + sorted;
    }

    public static String tableHeader() {
        return "     N | type   | distribution   | algorithm    | comparisons | swaps/moves | time     | space     | sorted";
    }

    public static String csvHeader() {
        return "n,dataType,distribution,algorithm,comparisons,swapsOrMoves,timeMs,inPlace,sorted";
    }
}
