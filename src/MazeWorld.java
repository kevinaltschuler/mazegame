//Assignment 9
//Kevin Altschuler
//kaltschu
//Andrew Barrett
//andrew
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Stack;

import tester.*;
import javalib.colors.*;
import javalib.worldimages.*;
import javalib.impworld.*;

import java.awt.Color;

//Represents an Edge of a Cell
class Edge implements Comparator<Edge>, Comparable<Edge> {
    Integer weight;
    Edge()
    {
        Random r = new Random();
        weight = r.nextInt(100);
    }
    public int compare(Edge arg0, Edge arg1) {
        return arg0.weight - arg1.weight;
    }
    public int compareTo(Edge arg0) {
        return this.weight.compareTo(arg0.weight);
    }
}
// Represents a single square of the game area
class Cell {
    // In logical coordinates, with the origin at the top-
    // left corner of the screen
    int x;
    int y;
    //size in pixels
    static final int SIZE = 650/MazeWorld.WIDTH;
    Cell(double height, int x, int y) {
        this.x = x;
        this.y = y;
    }
    WorldImage cellImage(double waterHeight) {
        return new RectangleImage(new Posn(this.x, this.y), 
                Cell.SIZE, Cell.SIZE, new Color(192,192,192));
    }
}

class Player {
    int x;
    int y;
    Cell cell;
}

class MazeWorld extends World {
    static final int WIDTH = 64;
    static final int HEIGHT = 64;
    //player
    Player player = new Player();
    // all the cells
    ArrayList<ArrayList<Cell>> board;
    ArrayList<Edge> edges;
    MazeWorld() {
        //default constructor
    }
    void reset(int width, int height) {
        int numedges = ((MazeWorld.HEIGHT + 1) * MazeWorld.WIDTH) + ((MazeWorld.WIDTH + 1) * MazeWorld.HEIGHT);
        for (int i = numedges; i >= 0; i -= 1)
        {
            edges.add(new Edge());
        }
            Collections.sort(edges);
    }
    //The entire background image for this world
    public WorldImage background = 
            new RectangleImage(new Posn(0, 0), 0, 0, new White());
    // overlay background onto cells
    public WorldImage makeImage() {
        WorldImage acc = new RectangleImage(new Posn(0, 0), 
                0, 0, new Black());
        
        return acc;
    }
    void updateCells() {

    }
    // handle key events
    public void onKeyEvent(String ke) {
        updatePlayer(ke);
    }
    public void updatePlayer(String ke) {
        if (ke.equals("up")) {
            this.player.y -= 1;
            //this.player.cell = this.player.cell.top;
        }
        if (ke.equals("down")) {
            this.player.y += 1;
            //this.player.cell = this.player.cell.bottom;
        }
        if (ke.equals("left")) {
            this.player.x -= 1;
            //this.player.cell = this.player.cell.left;
        }
        if (ke.equals("right")) {
            this.player.x += 1;
            //this.player.cell = this.player.cell.right;
        }
    }
    //update the player's position
    // method for each tick
    public void onTick() {
        this.updateCells();
    }
}

class ExamplesWorld {
    //TODO
    Cell c3 = new Cell(50.0, 20, 10);
    //Cell c4 = new Cell(25.0, 10, 20);
    //Cell c5 = new Cell(25.0, 10, 0);


    int runAnimation() {
        MazeWorld m1 = new MazeWorld();
        m1.bigBang(Cell.SIZE * MazeWorld.WIDTH, Cell.SIZE * MazeWorld.HEIGHT, 1);
        return 1;
    }
    int run = this.runAnimation();
}
