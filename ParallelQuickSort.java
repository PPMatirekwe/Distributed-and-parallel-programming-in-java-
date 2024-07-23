import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelQuickSort<T extends Comparable<? super T>> extends RecursiveAction {

    private final T[] list;
    private final int low;
    private final int high;
    private static final int SEQUENTIAL_THRESHOLD = 100; // Threshold for switching to sequential sort

    public ParallelQuickSort(T[] list, int low, int high) {
        this.list = list;
        this.low = low;
        this.high = high;
    }

    @Override
    protected void compute() {
        if (low < high && (high - low) > SEQUENTIAL_THRESHOLD) {
            int partitionPoint = partition(list, low, high);
            // Create subtasks recursively for the left and right halves
            ParallelQuickSort<T> leftTask = new ParallelQuickSort<>(list, low, partitionPoint - 1);
            ParallelQuickSort<T> rightTask = new ParallelQuickSort<>(list, partitionPoint + 1, high);
            // Invoke subtasks in ForkJoinPool
            invokeAll(leftTask, rightTask);
        } else {
            // If sub-array size is small, sort sequentially
            Sort.quickSort(list); // Call original QuickSort for small arrays
        }
    }

    private int partition(T[] list, int low, int high) {
        int pivot = low;
        T pivotValue = list[pivot];
        while (low < high) {
            while (low < high && list[low].compareTo(pivotValue) <= 0) {
                low++;
            }
            while (low < high && list[high].compareTo(pivotValue) > 0) {
                high--;
            }
            if (low < high) {
                T temp = list[low];
                list[low] = list[high];
                list[high] = temp;
            }
        }
        list[pivot] = list[high];
        list[high] = pivotValue;
        return high;
    }

    public static <T extends Comparable<? super T>> void parallelQuickSort(T[] list) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new ParallelQuickSort<>(list, 0, list.length - 1));
    }

    // Testing and Benchmarking Functions

    public static void testSort(Integer[] arr) {
        Integer[] arrCopy = arr.clone();
        parallelQuickSort(arr);
        Sort.quickSort(arrCopy); // Use original QuickSort for comparison
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].equals(arrCopy[i])) {
                throw new RuntimeException("Sorting failed!");
            }
        }
        System.out.println("Sorting successful!");
    }

    public static long benchmark(Integer[] arr, int numRuns, boolean isParallel) {
        long startTime, endTime, totalTime = 0;
        for (int i = 0; i < numRuns; i++) {
            Integer[] copy = arr.clone();
            startTime = System.nanoTime();
            if (isParallel) {
                parallelQuickSort(copy);
            } else {
                Sort.quickSort(copy);
            }
            endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }
        return totalTime / numRuns; // Average time per run
    }

    public static void main(String[] args) {
        // Test with a small array
        Integer[] testArr = { 5, 3, 1, 8, 7, 2 };
        testSort(testArr);
        System.out.println("Sorted array: " + java.util.Arrays.toString(testArr));

        // Benchmarking with a large array (1 million elements)
        int numRuns = 10;
        int arraySize = 1000000;
        Integer[] largeArr = new Integer[arraySize];
        for (int i = 0; i < arraySize; i++) {
            largeArr[i] = (int) (Math.random() * 1000); // Random integers
        }

        long sequentialTime = benchmark(largeArr, numRuns, false);
        long parallelTime = benchmark(largeArr, numRuns, true);

        System.out.println("Sequential Time: " + sequentialTime + " ns");
        System.out.println("Parallel Time: " + parallelTime + " ns");

        double speedup = (double) sequentialTime / parallelTime;
        System.out.println("Speedup: " + speedup);
    }
}
