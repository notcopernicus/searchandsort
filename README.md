# Search-Sort Library and CLI

Small Java library and CLI for searching and sorting with multiple classic algorithms. Algorithms are implemented from first principles (no `Arrays.sort` in core implementations; a **baseline** option uses it for comparison).

## Build and run

From the `search-sort` folder:

```bash
javac *.java
```

## Data and distributions
- **Types:** `int`, `string`, `record` (Student: id, name, gpa; comparator by id then name).
- **Distributions:** `random`, `nearly-sorted` (5–10% shuffled), `reverse`, `high-duplicate`.

## Algorithms
- **Search:** Linear (unsorted), Binary (sorted; iterative/recursive).
- **Sort:** Selection, Insertion, Heap (array-backed heap: heapify, siftDown, buildHeap), Merge (top-down), Quick. Optional **baseline** = `Arrays.sort` (labeled for comparison only).

## Output
- Each run reports: N, data type, distribution, algorithm, comparisons, swaps/moves, elapsed time (ms), in-place vs auxiliary, and sorted correctness.
- `RunResult.toCsvRow()` and `RunResult.csvHeader()` support optional CSV export for plotting.

See **NOTES.md** for stability and space usage.

### Empirical CSV for report (PDF)

To generate one CSV comparing all algorithms across N and distributions (for tables/plots in your Results Summary):

```bash
java CLI --benchmark --out=benchmark_results.csv
```

See **ResultsSummary.md** for the Big-O table, how to use the CSV, and the required discussion bullets (insertion vs “faster” sorts, heap vs quick/merge, cache).
