package com.init_spring_bean_mvn.demo.arrays;

public class ArraysIntro {
    /*Support multiple values of the same type
    * Can have arrays for any class
    * */

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   SORTING INTEGERS IN DESCENDING ORDER (Highest to Lowest)  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // Create sample integer arrays
        int[] unsortedArray = {42, 15, 89, 23, 56, 78, 12, 94, 31, 67};

        System.out.println("Original Array:");
        printArray(unsortedArray);

        // METHOD 1: Using java.util.Arrays.sort() with manual reverse
        System.out.println("\n" + "─".repeat(60));
        System.out.println("METHOD 1: Arrays.sort() with manual reverse");
        System.out.println("─".repeat(60));
        int[] method1Array = unsortedArray.clone();
        sortDescendingMethod1(method1Array);
        printArray(method1Array);

        // METHOD 2: Using Bubble Sort (manual implementation)
        System.out.println("\n" + "─".repeat(60));
        System.out.println("METHOD 2: Bubble Sort Algorithm (Manual Implementation)");
        System.out.println("─".repeat(60));
        int[] method2Array = unsortedArray.clone();
        sortDescendingMethod2(method2Array);
        printArray(method2Array);

        // METHOD 3: Using Collections with Integer List
        System.out.println("\n" + "─".repeat(60));
        System.out.println("METHOD 3: Collections.sort() with Integer List");
        System.out.println("─".repeat(60));
        Integer[] method3Array = convertToInteger(unsortedArray);
        sortDescendingMethod3(method3Array);
        printArray(method3Array);

        // METHOD 4: Using Stream API (Java 8+)
        System.out.println("\n" + "─".repeat(60));
        System.out.println("METHOD 4: Stream API (Java 8+)");
        System.out.println("─".repeat(60));
        int[] method4Array = unsortedArray.clone();
        method4Array = sortDescendingMethod4(method4Array);
        printArray(method4Array);

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║               All Methods Demonstrate Sorting!               ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }

    /**
     * METHOD 1: Sort using Arrays.sort() then reverse the array
     * Time Complexity: O(n log n) for sorting + O(n) for reverse = O(n log n)
     * Space Complexity: O(1)
     *
     * @param array The array to sort in descending order
     */
    public static void sortDescendingMethod1(int[] array) {
        System.out.println("\nUsing: java.util.Arrays.sort() with manual reverse");
        System.out.println("Approach: Sort ascending, then reverse");

        // Sort in ascending order
        java.util.Arrays.sort(array);
        System.out.println("Step 1 - After ascending sort: ");
        printArray(array);

        // Reverse to get descending order
        reverseArray(array);
        System.out.println("Step 2 - After reverse: ");
    }

    /**
     * METHOD 2: Bubble Sort Algorithm (manually implemented)
     * Repeatedly steps through the list, compares adjacent elements
     * Time Complexity: O(n²)
     * Space Complexity: O(1)
     *
     * @param array The array to sort in descending order
     */
    public static void sortDescendingMethod2(int[] array) {
        System.out.println("\nUsing: Bubble Sort Algorithm");
        System.out.println("Approach: Compare adjacent elements, swap if needed");
        System.out.println("Compare larger first, move larger to front (descending)");

        int n = array.length;
        int swapCount = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // For descending order: swap if array[j] < array[j + 1]
                if (array[j] < array[j + 1]) {
                    // Swap
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapCount++;
                }
            }
        }

        System.out.println("Total swaps performed: " + swapCount);
        System.out.println("Result: ");
    }

    /**
     * METHOD 3: Using Collections.sort() with Integer wrapper objects
     * Time Complexity: O(n log n) - Uses Timsort algorithm
     * Space Complexity: O(n) for the list
     *
     * @param array The Integer array to sort in descending order
     */
    public static void sortDescendingMethod3(Integer[] array) {
        System.out.println("\nUsing: java.util.Collections.sort()");
        System.out.println("Approach: Convert to List, sort with Comparator.reverseOrder()");

        java.util.List<Integer> list = java.util.Arrays.asList(array);
        java.util.Collections.sort(list, java.util.Collections.reverseOrder());

        // Copy sorted values back to array
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }

        System.out.println("Result: ");
    }

    /**
     * METHOD 4: Using Stream API (Java 8+)
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     * Modern, functional approach
     *
     * @param array The array to sort in descending order
     * @return A new sorted array in descending order
     */
    public static int[] sortDescendingMethod4(int[] array) {
        System.out.println("\nUsing: Stream API with boxed() and sorted()");
        System.out.println("Approach: Convert to stream, box to Integer, sort descending");

        return java.util.Arrays.stream(array)
                .boxed()  // Convert int to Integer
                .sorted(java.util.Collections.reverseOrder())  // Sort descending
                .mapToInt(Integer::intValue)  // Convert back to int
                .toArray();  // Collect to array
    }

    /**
     * Prints an array of integers in a formatted way
     *
     * @param array The array to print
     */
    public static void printArray(int[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(" ]");
    }

    /**
     * Prints an array of Integer objects in a formatted way
     *
     * @param array The Integer array to print
     */
    public static void printArray(Integer[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(" ]");
    }

    /**
     * Reverses an array in place
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * @param array The array to reverse
     */
    public static void reverseArray(int[] array) {
        int left = 0;
        int right = array.length - 1;

        while (left < right) {
            // Swap
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;

            left++;
            right--;
        }
    }

    /**
     * Converts a primitive int array to an Integer wrapper array
     *
     * @param intArray The primitive int array
     * @return A new Integer wrapper array with same values
     */
    public static Integer[] convertToInteger(int[] intArray) {
        Integer[] integerArray = new Integer[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            integerArray[i] = intArray[i];
        }
        return integerArray;
    }
}
