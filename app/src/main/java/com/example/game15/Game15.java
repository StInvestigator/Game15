package com.example.game15;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game15 {

    public class Coord{
        public int x;
        public int y;
        public Coord(int x, int y){
            this.x = x;
            this.y = y;
        }
        public Coord(){
            this(-1,-1);
        }

        public boolean isValid(){
            return x > -1 && y > -1 && x < 4 && y < 4;
        }
    }

    private int[][] gameField ={
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 0, 15}
    };
    private int turnsCount;
    Game15(){
        shuffle();
    }

    public void shuffle() {
        turnsCount = 0;
        List<Integer> list = new ArrayList<>();

        for (int[] row : gameField) {
            for (int num : row) {
                list.add(num);
            }
        }

        do {
            Collections.shuffle(list);
        } while (!isSolvable15Puzzle(list));

        int index = 0;
        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField[i].length; j++) {
                gameField[i][j] = list.get(index++);
            }
        }
    }

    private boolean isSolvable15Puzzle(List<Integer> list) {
        int parity = 0;
        int gridWidth = (int) Math.sqrt(list.size());
        int row = 0;
        int blankRow = 0;

        for (int i = 0; i < list.size(); i++)
        {
            if (i % gridWidth == 0) {
                row++;
            }
            if (list.get(i) == 0) {
                blankRow = row;
                continue;
            }
            for (int j = i + 1; j < list.size(); j++)
            {
                if (list.get(i) > list.get(j) && list.get(j) != 0)
                {
                    parity++;
                }
            }
        }

        if (gridWidth % 2 == 0) {
            if (blankRow % 2 == 0) {
                return parity % 2 == 0;
            } else {
                return parity % 2 != 0;
            }
        } else {
            return parity % 2 == 0;
        }
    }

    public int getValue(int i, int j) {
        return gameField[i][j];
    }

    public Coord findValue(int val){
        for (int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(gameField[i][j] == val){
                    return new Coord(i,j);
                }
            }
        }
        return new Coord();
    }

    public Coord go(int val){
        Coord zero = findValue(0);
        Coord coord = findValue(val);
        if(Math.abs(zero.x-coord.x) + Math.abs(zero.y-coord.y) !=1){
            return new Coord();
        }
        gameField[zero.x][zero.y] = val;
        gameField[coord.x][coord.y] = 0;
        turnsCount++;

        return zero;
    }

    public boolean isWin(){
        List<Integer> list = new ArrayList<>();

        for (int[] row : gameField) {
            for (int num : row) {
                list.add(num);
            }
        }
        int prev = 0;
        for (int i = 0; i<15;i++){
            if(list.get(i) <= prev){
                return false;
            }
            prev = list.get(i);
        }
        return true;
    }

    public int getTurnsCount() {
        return turnsCount;
    }

    public void setTurnsCount(int turnsCount) {
        this.turnsCount = turnsCount;
    }

    public int[] getGameState() {
        int[] state = new int[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i * 4 + j] = getValue(i, j);
            }
        }
        return state;
    }

    public void setGameState(int[] state) {
        for (int i = 0; i < 4; i++) {
            System.arraycopy(state, i * 4, gameField[i], 0, 4);
        }
    }
}
