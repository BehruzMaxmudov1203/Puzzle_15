package com.example.puzle_15_mukammal;

import java.util.ArrayList;

public class UnsolvableCase {
    private static final int CELL_COUNT = 4;

    // A utility function to count inversions in given
    // array 'arr[]'. Note that this function can be
    // optimized to work in O(n Log n) time. The idea
    // here is to keep code small and simple.
    private static int getInvCount(int[] arr) {
        int inv_count = 0;
        for (int i = 0; i < CELL_COUNT * CELL_COUNT - 1; i++) {
            for (int j = i + 1; j < CELL_COUNT * CELL_COUNT; j++) {
                if (arr[j] != 0 && arr[i] != 0 && arr[i] > arr[j])
                    inv_count++;
            }
        }
        return inv_count;
    }

    private static int findXPosition(ArrayList<Integer> puzzle) {
        for (int i = CELL_COUNT * CELL_COUNT - 1; i >= 0; i--)
            if (puzzle.get(i) == 0)
                return CELL_COUNT - i;
        return -1;
    }

    public static boolean isSolvable(ArrayList<Integer> puzzle) {
        ArrayList<Integer> puzzle15 = new ArrayList<>(puzzle);
        int[] arr = new int[CELL_COUNT * CELL_COUNT];

        for (int i = 0; i < CELL_COUNT * CELL_COUNT - 1; i++)
            arr[i] = puzzle15.get(i);
        arr[CELL_COUNT * CELL_COUNT - 1] = 0;

        int invCount = getInvCount(arr);

        if (CELL_COUNT % 2 == 1)
            return invCount % 2 == 0;
        else {
            puzzle15.add(0);
            int pos = findXPosition(puzzle15);
            if (pos % 2 == 1)
                return invCount % 2 == 0;
            else
                return invCount % 2 == 1;
        }
    }
}
