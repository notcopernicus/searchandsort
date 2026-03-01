# Search-Sort Library and CLI

Small Java library and CLI for searching and sorting with multiple classic algorithms. Algorithms are implemented from first principles (no `Arrays.sort` in core implementations; a **baseline** option uses it for comparison).

## Build and run

From the `search-sort` folder:

```bash
javac *.java
```

**Jar-style (non-interactive):**
```bash
java CLI --type=int --dist=random --n=50000 --algo=heap
java CLI --type=string --dist=nearly-sorted --n=10000 --algo=insertion
java CLI --type=int --dist=reverse --n=20000 --algo=merge
java CLI --type=int --dist=random --n=50000 --search=binary --key=12345
```

**Interactive (no arguments):**
```bash
java CLI
```
Then follow the prompts: data type (i/s/r), dataset size N, search (s) or sort (ss), then algorithm or key as asked.

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
# searchandsort
