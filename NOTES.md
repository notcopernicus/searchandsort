# Stability and Space Usage

## Stability (preserves relative order of equal keys)
- **Stable:** Insertion Sort, Merge Sort. (Baseline `Arrays.sort` for object arrays is stable.)
- **Not stable:** Selection Sort, Heap Sort, Quick Sort.

## Space
- **In-place (O(1) extra):** Selection Sort, Insertion Sort, Heap Sort, Quick Sort.
- **Auxiliary array (O(n) extra):** Merge Sort (top-down uses a single temporary array).

## Binary Search
- **Precondition:** Input array must be sorted (ascending with respect to the same comparator used for sorting).
- **Not found:** Returns index `-1`; caller should verify before using the index.
