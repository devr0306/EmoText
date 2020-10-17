package com.example.chatapp.Models.app;

import android.util.Log;

import com.example.chatapp.Models.API.Chat;
import com.example.chatapp.Models.API.Message;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class MergeSort {

    public static void mergeSort(Chat[] arr) throws ParseException {
        mergeSortRecursive(arr, 0, arr.length - 1);
    }

    private static void mergeSortRecursive(Chat[] arr, int begin, int end) throws ParseException {

        if(begin < end) {

            int mid = (end + begin)/2;

            mergeSortRecursive(arr, begin, mid);
            mergeSortRecursive(arr, mid + 1, end);

            merge(arr, begin, mid, end);
        }
    }

    // merge two portions that are sorted respectively by creating an auxilliary
    // array as a place holder then copy it back
    private static void merge(Chat[] arr, int start, int middle, int end) throws ParseException {

        int n1 = middle - start + 1;
        int n2 = end - middle;

        Chat[] ary1 = new Chat[n1];
        Chat[] ary2 = new Chat[n2];

        for(int i = 0; i < n1; i++)
            ary1[i] = arr[start + i];

        for(int i = 0; i < n2; i++)
            ary2[i] = arr[middle + 1 + i];


        int i = 0;
        int j = 0;
        int k = start;

        while(i < ary1.length && j < ary2.length) {

            if(ary1[i].compareDateTo(ary2[j]) > 0) {

                arr[k] = ary1[i];
                i++;
            }

            else {

                arr[k] = ary2[j];
                j++;
            }

            k++;
        }

        while(i < ary1.length) {
            arr[k] = ary1[i];
            i++;
            k++;
        }

        while(j < ary2.length) {
            arr[k] = ary2[j];
            j++;
            k++;
        }
    }
}
