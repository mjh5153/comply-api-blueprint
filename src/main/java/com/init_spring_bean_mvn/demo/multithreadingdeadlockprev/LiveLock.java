package com.init_spring_bean_mvn.demo.multithreadingdeadlockprev;

import java.util.Arrays;
import java.util.concurrent.Executors;



public class LiveLock {

    public static final int MAZE_SIZE = 4;

    private final String[][] cells = new String[MAZE_SIZE][MAZE_SIZE];

    public LiveLock() {
        for( var row : cells) {
            Arrays.fill(row, "");
        }
    }

    public int[] getNextLocation(int[] lastSpot) {

        int[] nextSpot = new int[2];
        nextSpot[1] = (lastSpot[1] == LiveLock.MAZE_SIZE - 1) ? 0 : lastSpot[1] + 1;
        nextSpot[0] = lastSpot[0];
        if(nextSpot[1] == 0) {
            nextSpot[0] = (lastSpot[0] == LiveLock.MAZE_SIZE - 1 ) ? 0 : lastSpot[0] + 1;
        }
        return nextSpot;
    }

    // move person to other location
    public void moveLocation(int locX, int locY, String name) {
        cells[locX][locY] = name.substring(0,1);
        resetSearchedCells(name);
    }
    // algo only good if one player stays put
    public void resetSearchedCells(String name) {
        for(var row: cells) {
            Arrays.asList(row).replaceAll(c ->
                    c.equals("!" + name.charAt(0)) ? "" : c); // unsearched
        }
    }

    public boolean searchCell(String partner, int[] nextSpot, int[] lastSpot) {
        if(cells[nextSpot[0]][nextSpot[1]].equals(partner.substring(0,1))) {
            return true;
        }
        cells[lastSpot[0]][lastSpot[1]] = "!" + partner.charAt(0);
        return false;
    }

    @Override
    public String toString() {
        return "LiveLock{" +
                "cells=" + Arrays.deepToString(cells) +
                '}';
    }

    public static void main(String[] args) {


        // two or more threads are continuously reacting, each responding to others actions. Can
        // never successfully complete



    }
}
