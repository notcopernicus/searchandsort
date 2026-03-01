import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CLI {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].startsWith("--")) {
            runWithArgs(args);
        } else {
            runInteractive();
        }
    }

    private static void runWithArgs(String[] args) {
        String type = getArg(args, "type", "int");
        String dist = getArg(args, "dist", "random");
        int n = Integer.parseInt(getArg(args, "n", "1000"));
        String algo = getArg(args, "algo", "");
        String search = getArg(args, "search", "");
        String keyStr = getArg(args, "key", "");
        boolean outputCsv = hasFlag(args, "csv");
        String outFile = getArg(args, "out", "");
        boolean benchmark = hasFlag(args, "benchmark");

        if (benchmark) {
            runBenchmark(outFile.isEmpty() ? "benchmark_results.csv" : outFile);
            return;
        }

        DataGenerator gen = new DataGenerator();
        DataGenerator.Distribution d = parseDist(dist);

        if (!search.isEmpty()) {
            runSearchFromArgs(type, dist, n, search, keyStr, gen, d);
        } else {
            if (algo.isEmpty()) algo = "heap";
            runSortFromArgs(type, dist, n, algo, gen, d, outputCsv, outFile);
        }
    }

    /** Run many (N, distribution, algorithm) combinations and write one CSV for empirical comparison. */
    private static void runBenchmark(String outPath) {
        int[] sizes = { 1000, 5000, 10000, 20000, 50000 };
        String[] dists = { "random", "nearly-sorted", "reverse", "high-duplicate" };
        String[] algos = { "selection", "insertion", "heap", "merge", "quick" };
        DataGenerator gen = new DataGenerator();
        List<RunResult> results = new ArrayList<>();
        for (int n : sizes) {
            for (String dist : dists) {
                DataGenerator.Distribution d = parseDist(dist);
                for (String algo : algos) {
                    int[] a = gen.generateInts(n, d);
                    RunResult r = runIntSort(a, algo, dist);
                    results.add(r);
                    System.err.println("Ran n=" + n + " dist=" + dist + " algo=" + algo + " -> " + r.timeMs + " ms");
                }
            }
        }
        try (PrintWriter w = new PrintWriter(new FileWriter(outPath))) {
            w.println(RunResult.csvHeader());
            for (RunResult r : results) w.println(r.toCsvRow());
        } catch (Exception e) {
            System.err.println("Benchmark write failed: " + e.getMessage());
            return;
        }
        System.out.println("Wrote " + results.size() + " rows to " + outPath);
    }

    private static boolean hasFlag(String[] args, String name) {
        for (String a : args) if (("--" + name).equals(a)) return true;
        return false;
    }

    private static void runSearchFromArgs(String type, String dist, int n, String search, String keyStr,
                                          DataGenerator gen, DataGenerator.Distribution d) {
        if ("int".equalsIgnoreCase(type)) {
            int[] arr = gen.generateInts(n, d);
            Arrays.sort(arr); // binary needs sorted
            int key = keyStr.isEmpty() ? (arr.length > 0 ? arr[arr.length / 2] : 0) : Integer.parseInt(keyStr);
            runIntSearch(arr, key, search);
        } else if ("string".equalsIgnoreCase(type)) {
            String[] arr = gen.generateStrings(n, d);
            Arrays.sort(arr);
            String key = keyStr.isEmpty() ? (arr.length > 0 ? arr[arr.length / 2] : "Apple") : keyStr;
            runStringSearch(arr, key, search);
        } else {
            System.out.println("Search with int or string only for this CLI.");
        }
    }

    private static void runSortFromArgs(String type, String dist, int n, String algo,
                                        DataGenerator gen, DataGenerator.Distribution d,
                                        boolean outputCsv, String outFile) {
        if ("int".equalsIgnoreCase(type)) {
            int[] a = gen.generateInts(n, d);
            RunResult r = runIntSort(a, algo, dist);
            printResult(r, outputCsv, outFile);
        } else if ("string".equalsIgnoreCase(type)) {
            String[] a = gen.generateStrings(n, d);
            RunResult r = runStringSort(a, algo, dist);
            printResult(r, outputCsv, outFile);
        } else if ("record".equalsIgnoreCase(type) || "student".equalsIgnoreCase(type)) {
            Student[] a = gen.generateStudents(n, d);
            RunResult r = runStudentSort(a, algo, dist);
            printResult(r, outputCsv, outFile);
        } else {
            System.out.println("Unknown type: " + type + ". Use int, string, or record.");
        }
    }

    private static void runIntSearch(int[] arr, int key, String search) {
        if ("binary".equalsIgnoreCase(search)) {
            BinarySearch bs = new BinarySearch();
            long t0 = System.currentTimeMillis();
            int idx = bs.searchIterative(arr, key);
            long time = System.currentTimeMillis() - t0;
            System.out.println("Binary search: key=" + key + " index=" + idx +
                " comparisons=" + bs.getComparisons() + " time=" + time + " ms");
            System.out.println("Correct: " + (idx >= 0 ? arr[idx] == key : true));
        } else {
            int[] copy = arr.clone();
            shuffle(copy);
            LinearSearch ls = new LinearSearch();
            long t0 = System.currentTimeMillis();
            int idx = ls.search(copy, key);
            long time = System.currentTimeMillis() - t0;
            System.out.println("Linear search: key=" + key + " index=" + idx +
                " comparisons=" + ls.getComparisons() + " time=" + time + " ms");
        }
    }

    private static void runStringSearch(String[] arr, String key, String search) {
        if ("binary".equalsIgnoreCase(search)) {
            BinarySearch bs = new BinarySearch();
            long t0 = System.currentTimeMillis();
            int idx = bs.searchIterative(arr, key, String.CASE_INSENSITIVE_ORDER);
            long time = System.currentTimeMillis() - t0;
            System.out.println("Binary search: key=" + key + " index=" + idx +
                " comparisons=" + bs.getComparisons() + " time=" + time + " ms");
        } else {
            String[] copy = arr.clone();
            shuffle(copy);
            LinearSearch ls = new LinearSearch();
            long t0 = System.currentTimeMillis();
            int idx = ls.search(copy, key, String.CASE_INSENSITIVE_ORDER);
            long time = System.currentTimeMillis() - t0;
            System.out.println("Linear search: key=" + key + " index=" + idx +
                " comparisons=" + ls.getComparisons() + " time=" + time + " ms");
        }
    }

    private static RunResult runIntSort(int[] a, String algo, String dist) {
        int n = a.length;
        String dataType = "int";
        if (dist == null) dist = "random";
        SortStats stats = new SortStats();
        long t0 = System.currentTimeMillis();
        runIntSortAlgo(a, algo, stats);
        long time = System.currentTimeMillis() - t0;
        boolean inPlace = !"merge".equalsIgnoreCase(algo);
        boolean sorted = SortUtils.isSorted(a);
        return new RunResult(n, dataType, dist, algo, stats.comparisons, stats.swapsOrMoves, time, inPlace, sorted);
    }

    private static void runIntSortAlgo(int[] a, String algo, SortStats stats) {
        switch (algo.toLowerCase()) {
            case "selection": new SelectionSort().sort(a, stats); break;
            case "insertion": new InsertionSort().sort(a, stats); break;
            case "heap": new HeapSort().sort(a, stats); break;
            case "merge": new MergeSort().sort(a, stats); break;
            case "quick": new QuickSort().sort(a, stats); break;
            case "baseline": new BaselineSort().sort(a, stats); break;
            default: new HeapSort().sort(a, stats);
        }
    }

    private static RunResult runStringSort(String[] a, String algo, String dist) {
        int n = a.length;
        if (dist == null) dist = "random";
        SortStats stats = new SortStats();
        long t0 = System.currentTimeMillis();
        runStringSortAlgo(a, algo, stats);
        long time = System.currentTimeMillis() - t0;
        boolean inPlace = !"merge".equalsIgnoreCase(algo);
        boolean sorted = SortUtils.isSorted(a, String.CASE_INSENSITIVE_ORDER);
        return new RunResult(n, "string", dist, algo, stats.comparisons, stats.swapsOrMoves, time, inPlace, sorted);
    }

    private static void runStringSortAlgo(String[] a, String algo, SortStats stats) {
        Comparator<String> cmp = String.CASE_INSENSITIVE_ORDER;
        switch (algo.toLowerCase()) {
            case "selection": new SelectionSort().sort(a, cmp, stats); break;
            case "insertion": new InsertionSort().sort(a, cmp, stats); break;
            case "heap": new HeapSort().sort(a, cmp, stats); break;
            case "merge": new MergeSort().sort(a, cmp, stats); break;
            case "quick": new QuickSort().sort(a, cmp, stats); break;
            case "baseline": new BaselineSort().sort(a, cmp, stats); break;
            default: new HeapSort().sort(a, cmp, stats);
        }
    }

    private static RunResult runStudentSort(Student[] a, String algo, String dist) {
        int n = a.length;
        if (dist == null) dist = "random";
        SortStats stats = new SortStats();
        long t0 = System.currentTimeMillis();
        runStudentSortAlgo(a, algo, stats);
        long time = System.currentTimeMillis() - t0;
        boolean inPlace = !"merge".equalsIgnoreCase(algo);
        boolean sorted = SortUtils.isSorted(a, Student.BY_ID_THEN_NAME);
        return new RunResult(n, "record", dist, algo, stats.comparisons, stats.swapsOrMoves, time, inPlace, sorted);
    }

    private static void runStudentSortAlgo(Student[] a, String algo, SortStats stats) {
        Comparator<Student> cmp = Student.BY_ID_THEN_NAME;
        switch (algo.toLowerCase()) {
            case "selection": new SelectionSort().sort(a, cmp, stats); break;
            case "insertion": new InsertionSort().sort(a, cmp, stats); break;
            case "heap": new HeapSort().sort(a, cmp, stats); break;
            case "merge": new MergeSort().sort(a, cmp, stats); break;
            case "quick": new QuickSort().sort(a, cmp, stats); break;
            case "baseline": new BaselineSort().sort(a, cmp, stats); break;
            default: new HeapSort().sort(a, cmp, stats);
        }
    }

    private static String getArg(String[] args, String name, String def) {
        String prefix = "--" + name + "=";
        for (String a : args) {
            if (a.startsWith(prefix)) return a.substring(prefix.length()).trim();
        }
        return def;
    }

    private static DataGenerator.Distribution parseDist(String dist) {
        switch (dist.toLowerCase()) {
            case "nearly-sorted": return DataGenerator.Distribution.NEARLY_SORTED;
            case "reverse": return DataGenerator.Distribution.REVERSE_SORTED;
            case "high-duplicate": return DataGenerator.Distribution.HIGH_DUPLICATE;
            default: return DataGenerator.Distribution.RANDOM;
        }
    }

    private static void printResult(RunResult r, boolean csv, String outFile) {
        if (outFile != null && !outFile.isEmpty()) {
            try (PrintWriter w = new PrintWriter(new File(outFile))) {
                w.println(RunResult.csvHeader());
                w.println(r.toCsvRow());
            } catch (Exception e) {
                System.err.println("Could not write to " + outFile + ": " + e.getMessage());
            }
        }
        if (csv) {
            System.out.println(RunResult.csvHeader());
            System.out.println(r.toCsvRow());
        } else if (outFile == null || outFile.isEmpty()) {
            System.out.println(RunResult.tableHeader());
            System.out.println(r.toTableRow());
        }
    }

    private static void shuffle(int[] a) {
        Random r = new Random();
        for (int i = a.length - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            int t = a[i]; a[i] = a[j]; a[j] = t;
        }
    }

    private static void shuffle(String[] a) {
        Random r = new Random();
        for (int i = a.length - 1; i > 0; i--) {
            int j = r.nextInt(i + 1);
            String t = a[i]; a[i] = a[j]; a[j] = t;
        }
    }

   
    private static void runInteractive() {
        Scanner kb = new Scanner(System.in);
        Random r = new Random();

        System.out.println("Hello user.");
        System.out.println("To specify your data type, input s for string and i for integer (or r for record/Student).");
        String input_one = kb.nextLine().trim().toLowerCase();

        System.out.println("Please enter a dataset size N (e.g., 1000 to 100000).");
        int n_input = kb.nextInt();
        kb.nextLine();

        if (n_input <= 0 || n_input > 500_000) {
            System.out.println("Using N=10000 for safety.");
            n_input = 10000;
        }

        DataGenerator gen = new DataGenerator();
        DataGenerator.Distribution dist = DataGenerator.Distribution.RANDOM;

        if (input_one.equals("i")) {
            int[] int_arr = gen.generateInts(n_input, dist);
            System.out.println("Type s for search or ss for sort.");
            String input_two = kb.nextLine().trim().toLowerCase();
            if (input_two.equals("s")) {
                System.out.println("Type b for binary search or l for linear search.");
                String search_type = kb.nextLine().trim().toLowerCase();
                System.out.println("Enter an integer key to search for.");
                int key = kb.nextInt();
                kb.nextLine();
                if (search_type.equals("b")) {
                    Arrays.sort(int_arr);
                    BinarySearch bs = new BinarySearch();
                    long t0 = System.currentTimeMillis();
                    int idx = bs.searchIterative(int_arr, key);
                    long time = System.currentTimeMillis() - t0;
                    System.out.println("Binary search: index=" + idx + " comparisons=" + bs.getComparisons() + " time=" + time + " ms");
                    if (idx >= 0) System.out.println("Value at index: " + int_arr[idx]);
                } else {
                    LinearSearch ls = new LinearSearch();
                    long t0 = System.currentTimeMillis();
                    int idx = ls.search(int_arr, key);
                    long time = System.currentTimeMillis() - t0;
                    System.out.println("Linear search: index=" + idx + " comparisons=" + ls.getComparisons() + " time=" + time + " ms");
                    if (idx >= 0) System.out.println("Value at index: " + int_arr[idx]);
                }
            } else if (input_two.equals("ss")) {
                System.out.println("Algorithm: selection, insertion, heap, merge, quick, or baseline?");
                String algo = kb.nextLine().trim().toLowerCase();
                if (algo.isEmpty()) algo = "heap";
                RunResult res = runIntSort(int_arr, algo, "random");
                System.out.println(RunResult.tableHeader());
                System.out.println(res.toTableRow());
            }
        } else if (input_one.equals("s")) {
            System.out.println("A randomized set of technology-related strings will be used.");
            String[] s_arr = gen.generateStrings(n_input, dist);
            System.out.println("Type s for search or ss for sort.");
            String input_two = kb.nextLine().trim().toLowerCase();
            if (input_two.equals("s")) {
                System.out.println("Type b for binary search or l for linear search.");
                String search_type = kb.nextLine().trim().toLowerCase();
                System.out.println("Enter a string key (e.g. Apple, Nvidia).");
                String key = kb.nextLine().trim();
                if (search_type.equals("b")) {
                    Arrays.sort(s_arr, String.CASE_INSENSITIVE_ORDER);
                    BinarySearch bs = new BinarySearch();
                    long t0 = System.currentTimeMillis();
                    int idx = bs.searchIterative(s_arr, key, String.CASE_INSENSITIVE_ORDER);
                    long time = System.currentTimeMillis() - t0;
                    System.out.println("Binary search: index=" + idx + " comparisons=" + bs.getComparisons() + " time=" + time + " ms");
                    if (idx >= 0) System.out.println("Value at index: " + s_arr[idx]);
                } else {
                    LinearSearch ls = new LinearSearch();
                    long t0 = System.currentTimeMillis();
                    int idx = ls.search(s_arr, key, String.CASE_INSENSITIVE_ORDER);
                    long time = System.currentTimeMillis() - t0;
                    System.out.println("Linear search: index=" + idx + " comparisons=" + ls.getComparisons() + " time=" + time + " ms");
                    if (idx >= 0) System.out.println("Value at index: " + s_arr[idx]);
                }
            } else if (input_two.equals("ss")) {
                System.out.println("Algorithm: selection, insertion, heap, merge, quick, or baseline?");
                String algo = kb.nextLine().trim().toLowerCase();
                if (algo.isEmpty()) algo = "heap";
                RunResult res = runStringSort(s_arr, algo, "random");
                System.out.println(RunResult.tableHeader());
                System.out.println(res.toTableRow());
            }
        } else if (input_one.equals("r")) {
            System.out.println("Using Student records (id, name, gpa) sorted by id then name.");
            Student[] rec = gen.generateStudents(n_input, dist);
            System.out.println("Algorithm: selection, insertion, heap, merge, quick, or baseline?");
            String algo = kb.nextLine().trim().toLowerCase();
            if (algo.isEmpty()) algo = "heap";
            RunResult res = runStudentSort(rec, algo, "random");
            System.out.println(RunResult.tableHeader());
            System.out.println(res.toTableRow());
        } else {
            System.out.println("Unrecognized type. Use i, s, or r.");
        }

        kb.close();
    }
}
