# Search-Sort Results Summary  
**2–3 Pages — Export to PDF for submission**

---

## 1. Big-O Complexity Table

Table 1 gives the time and space complexity for each algorithm implemented. *Best*, *average*, and *worst* refer to time complexity; *space* is auxiliary memory (excluding the input array).

**Table 1 — Algorithm complexity (time and space)**

| Algorithm          | Time (best) | Time (average) | Time (worst) | Space (auxiliary)     |
|--------------------|-------------|----------------|--------------|------------------------|
| Linear Search      | O(1)        | O(n)           | O(n)         | O(1)                   |
| Binary Search      | O(1)        | O(log n)       | O(log n)     | O(1) iter / O(log n) rec |
| Selection Sort     | O(n²)       | O(n²)          | O(n²)        | O(1)                   |
| Insertion Sort     | O(n)        | O(n²)          | O(n²)        | O(1)                   |
| Heap Sort          | O(n log n)  | O(n log n)     | O(n log n)   | O(1)                   |
| Merge Sort         | O(n log n)  | O(n log n)     | O(n log n)   | O(n)                   |
| Quick Sort         | O(n log n)  | O(n log n)     | O(n²)        | O(log n) avg / O(n) worst |
| Baseline (Arrays.sort) | O(n log n) | O(n log n) | O(n log n) | O(log n)–O(n)          |

**Notes:** Insertion sort’s best case is O(n) when the input is already sorted or nearly sorted. Quick sort’s worst case occurs when the pivot is always the minimum or maximum (e.g., sorted input with a naive pivot); space is recursion stack depth. Merge sort uses Θ(n) extra space for the temporary array.

---

## 2. Empirical Results

Benchmarks were run for integer arrays with N ∈ {1,000; 5,000; 10,000; 20,000; 50,000}, four distributions (random, nearly-sorted, reverse, high-duplicate), and five algorithms (selection, insertion, heap, merge, quick). Each run was timed in milliseconds and correctness was checked with an *isSorted* test.

### 2.1 Time (ms) by algorithm and distribution — N = 50,000

**Table 2 — Elapsed time (ms) for N = 50,000**

| Algorithm   | Random | Nearly-sorted | Reverse | High-duplicate |
|-------------|--------|----------------|---------|-----------------|
| Selection   | 1,085  | 939            | 1,105   | 937             |
| Insertion   | 417    | **67**         | 730     | 363             |
| Heap        | 7      | 6              | 5       | 7               |
| Merge       | 7      | 3              | 3       | 6               |
| Quick       | **5**  | **2**          | **2**   | **4**           |

Insertion sort is much faster on nearly-sorted data (67 ms) than on random (417 ms) or reverse (730 ms). Quick sort is fastest in wall-clock time for this N across all distributions. Selection and insertion are orders of magnitude slower than heap, merge, and quick at N = 50,000.

### 2.2 Time (ms) by algorithm and N — random distribution

**Table 3 — Elapsed time (ms) for random distribution**

| N      | Selection | Insertion | Heap | Merge | Quick |
|--------|------------|-----------|------|-------|-------|
| 1,000  | 4          | 4         | 1    | 1     | 1     |
| 5,000  | 10         | 4         | 1    | 1     | 1     |
| 10,000 | 39         | 14        | 2    | 1     | 1     |
| 20,000 | 168        | 58        | 3    | 3     | 2     |
| 50,000 | 1,085      | 417       | 7    | 7     | 5     |

As N grows, selection and insertion scale poorly (quadratic), while heap, merge, and quick stay in the low millisecond range. Quick sort is slightly faster than heap and merge in these runs.

### 2.3 How the CSV was generated

From the project folder, the full benchmark (100 runs) was executed with:

`javac *.java` then `java CLI --benchmark --out=benchmark_results.csv`

The file `benchmark_results.csv` contains columns: n, dataType, distribution, algorithm, comparisons, swapsOrMoves, timeMs, inPlace, sorted. It can be opened in Excel or Google Sheets to produce additional tables or plots.

---

## 3. Discussion

- **Insertion sort can beat “faster” sorts on small N.** It has very low constant factors: no extra array, no recursion, and simple compare-and-shift. For small n (e.g., under about 50), the O(n²) term is still small, and cache and instruction count favor insertion over merge, quick, and heap.

- **On nearly-sorted input, insertion sort behaves like O(n).** Each element requires only a few comparisons and moves. Merge, quick, and heap still do O(n log n) work regardless of order. In our benchmarks, insertion on nearly-sorted data at N = 50,000 (67 ms) is much faster than on random (417 ms) or reverse (730 ms), and is competitive in absolute time with the O(n log n) sorts for that distribution.

- **Heap sort vs quick sort on random data.** Heap sort has guaranteed O(n log n) time and O(1) extra space but tends to do more comparisons and has less cache-friendly access. Quick sort (with median-of-three pivot) often wins in practice on random data due to better locality and fewer comparisons, as seen in Table 2 and Table 3.

- **Heap sort vs merge sort.** Merge sort is stable and has a simple, predictable access pattern but uses Θ(n) extra space. Heap sort is in-place and O(1) space but not stable and has more scattered memory access, which can hurt cache performance compared to merge’s linear sweeps.

- **Cache effects.** Algorithms that traverse memory sequentially (e.g., insertion sort, merge sort’s merges) benefit from cache. Heap sort’s index arithmetic and quick sort’s partitioning can cause more cache misses; for large N this can show up as a measurable slowdown even when Big-O is the same.

- **Why theory and empirical results differ.** Constants, cache behavior, and implementation details (pivot choice, treatment of small subarrays) matter. That is why insertion wins for small or nearly-sorted inputs and why quick/merge often beat heap in wall-clock time despite similar asymptotic complexity.

- **Stability and space in practice.** When the order of equal keys must be preserved or memory is limited, insertion (stable, in-place) or merge (stable, extra space) may be preferred. Among our O(n log n) sorts, only heap is in-place; quick and heap are not stable.

---

*End of Results Summary. Export this document to PDF (e.g., open in Word or Google Docs and “Save as PDF”, or use a Markdown-to-PDF tool) to submit as your 2–3 page report.*
