package com.init_spring_bean_mvn.demo.datastructures;

import java.util.Arrays;

public class DSA {
    public static void main(String[] args) {
        // Data

        // algos - steps to perform when accomplishing a task
        // Making a cup of tea - 1.
        // 2. boil water
        // 3. add tea bag,
        // 4. steep,
        // 5. remove tea bag,
        // 6. add milk/sugar if desired
        // data structures - way to organize and store data in a computer so that it can be accessed and modified efficiently

            // Many algorithms rely on data structures to efficiently store and manipulate data - e.g. sorting algorithms rely on arrays or linked lists to store the data being sorted,
        // graph algorithms rely on adjacency lists or matrices to represent the graph structure, etc.
        // Implementation is code you write; many data structures and algorithms are implemented in Java's standard library - e.g. ArrayList, LinkedList, HashMap, etc. - but it's important to understand how they work and when to use them
        // example boil water -       // 1. fill kettle with water, implement steps in many different ways

        // examples: sort algorithms - bubble sort, selection sort, insertion sort, merge sort, quicksort, etc. - each with different time and space complexity and use cases

        // Arrays - random access when knowing the specific index for an item in array

        int[] arr = new int[7]; // required to specify size of array when creating it - fixed size, not dynamic
        // size cannot be changed

        arr[0] = 20;
        arr[1] = 35;
        arr[2] = -15;
        arr[3] = 7;
        arr[4] = 55;
        arr[5] = 1;
        arr[6] = -22;

        // Big O worst case - 7 is at end of array - have to check every element until we find it
        int index = -1;
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] == 7) {
                System.out.println("Found 7 at index " + i);
                index = i;
                break;
            }
            System.out.println(arr[i] + " " + index + " is not 7");
        }

        // Big(O) hardware affects time complexity of algorithms - e.g. O(1) is constant time, O(n) is linear time, O(n^2) is quadratic time, etc. - important to understand how to analyze and compare the efficiency of different algorithms and data structures
        // Can't just compare running time of same algorithm
        // Look at number of steps - time complexity - how number of steps grows as input size grows - e.g. O(1) means number of steps is constant regardless of input size, O(n) means number of steps grows linearly with input size, O(n^2) means number of steps grows quadratically with input size, etc.
        // memory cheap - usually concerned with time complexity
        // Look at worst case instead of average or best case ( upper bound )
        // Compare worst case of one algorithm and another

        // tea
        // adding sugar to tea
        // get bowl
        // get spoon
        // scoop out sugar
        // add to tea
        // more than one ? repeat step 3 and 4 until desired sweetness is reached
        // 2 sugars ? 6 steps, 1 sugar 4 steps
        // as sugars increases, steps increases linearly - O(n) time complexity - linear time
        // Tells how algo will scale - 1000, 10000, 1m
        // Big O notation - O(n) - linear time, O(1) - constant time, O(n^2) - quadratic time, O(nlogn) - n log star n, O(logbase2n) - logarithmic etc. - describes upper bound of time complexity of an algorithm - how number of steps grows as input size grows
        // Wikipedia - see big O notation - https://en.wikipedia.org/wiki/Big_O_notation - degradation of algorithms
        // 1. number of sugars = n
        // 2. steps = 2(n) + 2 - O(n) - linear time 2's don't change so not considered
        // time complexity is 0(n) - linear time - as number of sugars increases, steps increases linearly

        /* Array storage - one continuous block of memory
        * ex: starts at memory address 1000, if each int takes 4 bytes, then arr[0] is at 1000, arr[1] is at 1004, arr[2] is at 1008, etc. - allows for fast access to any element in the array using its index - O(1) time complexity for access
        *   2. Every item occupies same amount of space - java is 4 bytes for int, 8 bytes for long, etc. - allows for fast access to any element in the array using its index - O(1) time complexity for access
        *  3. Fixed size - once an array is created, its size cannot be changed - if you need to add more elements than the size of the array, you need to create a new array with a larger size and copy the elements from the old array to the new array - this can be inefficient if you need to add a lot of elements - O(n) time complexity for adding an element to an array
        * 4. Objects ? What's stored in variable is object reference ( always same size )- memory address where object is stored - allows for fast access to any element in the array using its index - O(1) time complexity for access
        * 5. x + i * y - Easily calculate memory address of any element in the array using its index - memory address of arr[i] = base address + (i * size of each element) - allows for fast access to any element in the array using its index - O(1) time complexity for access
        * 6. Number of steps will be same regardless of input size - O(1) time complexity for access - constant time - as input size increases, number of steps remains constant
        *7. Why array indices are zero-based - historical reasons, allows for efficient memory access and pointer arithmetic - memory address of arr[i] = base address + (i * size of each element) - if indices were one-based, memory address of arr[i] would be base address + ((i-1) * size of each element) - would require an extra subtraction operation to calculate memory address of each element - O(1) time complexity for access would still hold, but would be less efficient due to extra subtraction operation
        * Arrays are good at retrieving items if we know index - O(1) time complexity for access - but not good at adding or removing items - O(n) time complexity for adding or removing an element from an array - because we need to shift elements to fill the gap when removing an element, or to make room for a new element when adding an element
        *  */

        /*
        * Big O values for array operations - Retrieve element by index - O(1) - constant time - as input size increases, number of steps remains constant - multiply size by index - gives offset from beginging of array than add address of first element - O(1) time complexity for access
        * array requires loop linear time
        * array doesn't require array - O(1) time complexity for access - can calculate memory address of any element in the array using its index - memory address of arr[i] = base address + (i * size of each element) - allows for fast access to any element in the array using its index - O(1) time complexity for access
        *
        *
        * */


        /*
        * Sort algorithms - bubble sort, selection sort, insertion sort, merge sort, quicksort, etc. - each with different time and space complexity and use cases
        *
        *
        * */

        int unsortedPartitionIndex = 6; // last index of unsorted partition
        // start at left i
        // compare element at index 0 and 1 - if element at index 0 is greater than element at index 1, swap them - otherwise, do nothing until i = unsortedPartitionIndex - repeat process until i = unsortedPartitionIndex - greatest element will be at the end of the array === unsorted partition
        // first pass - greatest element will be at the end of the array - unsorted partition is now 0 to 5
        //set unsortedPartitionIndex to 5
        // Set i back to 0 and repeat process until unsortedPartitionIndex is 0 - array is now sorted

        while(unsortedPartitionIndex > 0) {
            for(int indexPartition=0; indexPartition< unsortedPartitionIndex; indexPartition++) {
                // improvement - no traversing the sorted partition - only need to compare elements in unsorted partition - as unsorted partition gets smaller, number of comparisons decreases - O(n^2) time complexity for bubble sort - nested loops - as input size increases, number of steps grows quadratically - inefficient for large input sizes - not used in practice for large datasets
                if(arr[indexPartition] > arr[indexPartition + 1]) {
                    // swap elements at index and index + 1
                    int temp = arr[indexPartition];
                    arr[indexPartition] = arr[indexPartition + 1];
                    arr[indexPartition+1] = temp;
                    System.out.println("Swapped " + arr[indexPartition] + " and " + arr[indexPartition + 1]);
                }
            }
            unsortedPartitionIndex--;
        }
        System.out.println("Array Sorted " + Arrays.toString(arr));

        // Called a bubble sort
        // Variations - right to left but same steps used

        // 2. Performance - in place algorithm : other space and array not neeeded - extra memory doesn't care about number of elements your sorting
        // 3. O(N^2) time complexity - nested loops - as input size increases, number of steps grows quadratically - inefficient for large input sizes - not used in practice for large datasets
        // 4. High degradation

        // Implementation of bubble sort - not efficient but easy to understand and implement - good for small datasets or as an educational tool to introduce sorting algorithms

        /* Stable vs Unable sorting alorithms explained */
        // unstable - relative ordering of dup items not preserved - black 9 will come before white 9 - not stable
        // stable - relative ordering of dup items preserved - white 9 will come before black
        // not want a sort to change dub items.

        // Ex: bubble sort ? Stable - if two elements are equal, they will not be swapped - relative ordering of duplicate items is preserved - stable sorting algorithm
        // Be aware of stable algorithm isn't inadvertently unstable by implementation

        /*Selection sort - In-place sorting */

        // steps - Divide array into sorted and unsorted partitions - initially, sorted partition is empty and unsorted partition is the entire array - find smallest element in unsorted partition and swap it with the first element of the unsorted partition - now the first element of the unsorted partition is in its correct position in the sorted partition - repeat process until unsorted partition is empty - array is now sorted
        // i = 1 - index used to traverse array from left to right
        // largest= 0-index of largest element in unsorted partition
        // no extra memory - O(n^2) time complexity for selection sort - nested loops - as input size increases, number of steps grows quadratically - inefficient for large input sizes - not used in practice for large datasets
        // less swap than bubble sort - only one swap per pass through the array - can be more efficient than bubble sort for large datasets with many duplicate elements - but still O(n^2) time complexity - inefficient for large input sizes - not used in practice for large datasets
        // unstable - relative ordering of duplicate items not preserved - black 9 will come before white 9 - not stable
        for (int iunsorted = 0; iunsorted > 0; iunsorted--) {
            int largest = 0; // index of largest element in unsorted partition
            for (int i = 1; i <= iunsorted; i++) { // compare every element against largest
                if(arr[i] > arr[largest]) {
                    largest = i;
                }
            }
            
            swap(arr, largest, iunsorted);
        }

        /* Insertion sort - in place algorithm, quadratic, stable */
        // Paratitioned to sorted from unsorted
        // grows sorted partition from left to right
        // elements 1 to length are unsorted partition
        // grows by 1 each iteration
        // ex - take 35 and insert to sorted partition
        // compare value sorting  - sorted partition right to left
        // element index [i] < =  value to insert - shift element at index i to the right - continue comparing and shifting until we find the correct position for the value to insert - insert value at correct position in sorted partition - repeat process for each element in unsorted partition until unsorted partition is empty - array is now sorted
        // next -15 <= 35 - shift  35 to position 2 after saving -15, keep shifting until item is in correct position - insert -15 at index 0

        // first index is in sorted partition
        // traverse sorted partition left to write and grow as well as adding more values from unsorted partition
        for(int firstSortedIndex= 1; firstSortedIndex < arr.length; firstSortedIndex++) {
          int valueToInsert = arr[firstSortedIndex];

            int j = 0;
             // index used to traverse sorted partition from right to left
            for(j = firstSortedIndex; j > 0 && arr[j-1] > valueToInsert; j--) {
                    arr[j] = arr[j-1]; // shift element at index j-1 to the right

            }
            arr[j] = valueToInsert;
        }
        System.out.println("Insertion Sort-- Array Sorted " + Arrays.toString(arr));
        // Improvements can be made - Shell sort - efficiency
        // Con : many

        //Shell:  linear time if most values already sorted - Donald Shell - 1959 - gap sequence - compare elements that are a certain gap apart and sort them using insertion sort - repeat process with smaller gaps until gap is 1 - array is now sorted - can be more efficient than insertion sort for large datasets with many duplicate elements or partially sorted arrays - but still O(n^2) time complexity in worst case - inefficient for large input sizes - not used in practice for large datasets


        //Variation of insertion sort -
        // Gap value of 1
        // Compares value to neighbor
        // shifts to right until insert found
        // Shell - compares elements farther apart
        // reduces gap
        // goal - reduce shifting
        // reduces gap until value is 1 - which will do insertion sort
        // preliminary sorting done ? much less shifting
        // gap value to choose - wikipedia - wiki/Shellsort - gap calculation incluence time complexity
        //  - kanuth sequence - 2^k - 1 - O(n^(3/2)) time complexity in worst case - inefficient for large input sizes - not used in practice for large datasets
        // (3^k - 1) / 2 - set k based on length of array, close as possible to length of array, w/o being greater than length
        // - Simple: base gap by arrays length

        // Algorithm start steps;
        // - initlaize to array.length/2
        // gap = 3
        // end gap will be 1
        // i = gap
        // j = i
        // save arr[i] to newElement
        // arr[j-gap] > newElement - shift arr[j-gap] to right
        // j = j - gap
        // repeat
        // Now array more sorted - less shifting for insertion sort when gap is 1
        // Divide 3/2 - gap = 1 and switch to insertion sort after premilinary sort

        // in place algorithm, unstable, time complexity depends on gap - worst case: O(n^2) - inefficient for large input sizes - not used in practice for large datasets - but can be more efficient than insertion sort for large datasets with many duplicate elements or partially sorted arrays
        // right most element could be shifted to left most position - O(n) time complexity for shifting elements in worst case - inefficient for large input sizes - not used in practice for large datasets

        // Can improve bubble sort as well by Shell sort - cut down on swap numbers

        // Implementation

        int[] intArray = { 20, 35, -15, 7, 55, 1, -22};

        for(int gap = intArray.length /2; gap>0; gap /=2) {
            System.out.println("array first for loop -- Array Sorted gap: " + gap + " " + Arrays.toString(intArray));
            // comparing and shifting - basically insertion sort
            for(int i = gap; i < intArray.length; i++) {
                System.out.println("array second for loop -- i: " + i + " gap: " + gap + " Array: " + Arrays.toString(intArray));
                int newElement = intArray[i]; // new element = current value at arr[gap] because i = gap
                System.out.println("array second for loop -- newElement " + newElement);
                int j = i;
                System.out.println("array second for loop -- j: " + j + " j - gap: " + (j-gap) + " newElement: " + newElement + " Array: " + Arrays.toString(intArray));
                while (j >= gap && intArray[j-gap] > newElement) { // Have not hit the front
                    intArray[j] = intArray[j - gap]; // shift up array right to left by gap
                    System.out.println("while loop intArray[j - gap]: " + intArray[j - gap] + "  " + " is greater than " + newElement + " intArray[j]: " + intArray[j]);
                    j -= gap; // now compare newElement to 3 positions over, hitting from of array
                    System.out.println("While loop decrement j -= gap " + Arrays.toString(intArray) + " j: " + j + " gap: " + gap);
                }
                System.out.println("Shell Sort After while before-- intArray[j]: " + intArray[j] + " newElement: " + newElement);

                intArray[j] = newElement;

                System.out.println("Shell Sort After while after -- intArray[j]: " + intArray[j] + " newElement: " + newElement);

            }

        }

        System.out.println("Shell Sort-- Array Sorted " + Arrays.toString(intArray));






    }

    private static void swap(int[] arr, int largest, int iunsorted) {
        int temp = arr[largest];
        arr[largest] = arr[iunsorted];
        arr[iunsorted] = temp;
    }
}
