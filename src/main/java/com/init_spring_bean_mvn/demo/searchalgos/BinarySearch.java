package com.init_spring_bean_mvn.demo.searchalgos;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BinarySearch {
    public static void main (String[] args) {
        int[] intArray = { 20, 35, -15, 7, 55, 1, -22};
    }

    public static int linearSearch(int[] input, int value) {

        // linear search i.e for loop, condition checkinig value in input
        return -1;
        // Time Complexity O(n) - worst case
    }

    public static int binarySearch(List<Integer> nums, int value) {
        // similar to merge sort
        // Data sorted - Make sure application always has arrays / lists etc.. sorted on insert of values
        // choose element in middle of array
        // element equal to value ? finish ? > searchLeft : searchRight
        // rinse and repeat step allows to write recursive



        Collections.sort(nums);
        int start = 0;
        int end = nums.size();
        while(start < end) {
            int mid = (start + end)/2;
            if( nums.get(mid) == value) {
                //found
                return mid;
            }
            else if ( nums.get(mid) < value ) {
                start = mid + 1;
            } else {

                end = mid;
            }
        }
        return -1;
    }




}



