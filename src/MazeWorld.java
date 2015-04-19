//Assignment 9
//Kevin Altschuler
//kaltschu
//Andrew Barrett
//andrew
import java.util.ArrayList;
import java.util.Stack;

import tester.*;
import javalib.colors.*;
import javalib.worldimages.*;
import javalib.impworld.*;

import java.awt.Color;

//Represents an Edge of a Cell
class Edge {
    double weight;
    Edge()
    {
        weight = Math.random();
    }
}
// Represents a single square of the game area
class Cell {
    // In logical coordinates, with the origin at the top-
    // left corner of the screen
    int x;
    int y;
    Edge N;
    Edge E;
    Edge S;
    Edge W;
    //size in pixels
    static final int SIZE = 650/MazeWorld.WIDTH;
    Cell(double height, int x, int y, Edge N, Edge E, Edge S, Edge W) {
        this.x = x;
        this.y = y;
        this.N = N;
        this.E = E;
        this.S = S;
        this.W = W;
    }
    WorldImage cellImage(double waterHeight) {
        return new RectangleImage(new Posn(this.x, this.y), 
                Cell.SIZE, Cell.SIZE, new Color(1,1,1));
    }
}

class Player {
    int x;
    int y;
    Cell cell;
}

class MazeWorld extends World {
    static final int WIDTH = 100;
    static final int HEIGHT = 64;
    //player
    Player player = new Player();
    // all the cells
    ArrayList<ArrayList<Cell>> board;
    ArrayList<Edge> edges;
    MazeWorld() {
        // default constructor
    }
    void reset(int width, int height) {
        int i = ((MazeWorld.HEIGHT + 1) * MazeWorld.WIDTH) + ((MazeWorld.WIDTH + 1) * MazeWorld.HEIGHT);
        for (i = i; i >= 0; i -= 1)
        {
            edges.add(new Edge());
        }
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
    Cell c3 = new Cell(50.0, 20, 10, new Edge(), new Edge(), new Edge(), new Edge());
    //Cell c4 = new Cell(25.0, 10, 20);
    //Cell c5 = new Cell(25.0, 10, 0);


    int runAnimation() {
        MazeWorld m1 = new MazeWorld();
        m1.bigBang(Cell.SIZE * MazeWorld.WIDTH, Cell.SIZE * MazeWorld.HEIGHT, 1);
        return 1;
    }
    int run = this.runAnimation();
}
