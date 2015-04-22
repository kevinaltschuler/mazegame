//Assignment 9
//Kevin Altschuler
//kaltschu
//Andrew Barrett
//andrew
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import tester.*;
import javalib.colors.*;
import javalib.worldimages.*;
import javalib.impworld.*;

import java.awt.Color;

//Represents an Edge of a Cell
class Edge implements Comparator<Edge>, Comparable<Edge> {
    Integer weight;
    Cell c1;
    Cell c2;
    Edge(Cell c1, Cell c2)
    {
        this.c1 = c1;
        this.c2 = c2;
        Random r = new Random();
        weight = r.nextInt(100);
    }
    public int compare(Edge arg0, Edge arg1) {
        return arg0.weight - arg1.weight;
    }
    public int compareTo(Edge arg0) {
        return this.weight.compareTo(arg0.weight);
    }
    public boolean sameEdge(Edge that) {
        return ((this.c1.sameCell(that.c1) && this.c2.sameCell(that.c2)) ||
                this.c1.sameCell(that.c2) && this.c2.sameCell(that.c1));

    }
    WorldImage edgeImage() {
        if(this.c1.x == this.c2.x) {
            return new LineImage(new Posn((this.c1.x * Cell.SIZE),
                    this.c1.y * Cell.SIZE + Cell.SIZE),
                          new Posn(this.c1.x * Cell.SIZE + Cell.SIZE,
                                  this.c1.y * Cell.SIZE + Cell.SIZE), new Black());
        }
        else
        {
            return new LineImage(new Posn(this.c1.x * Cell.SIZE + Cell.SIZE, 
                    this.c1.y * Cell.SIZE),
                          new Posn(this.c1.x * Cell.SIZE + Cell.SIZE, 
                                  this.c1.y * Cell.SIZE + Cell.SIZE), new Black());
        }
    }
}
// Represents a single square of the game area
class Cell {
    // In logical coordinates, with the origin at the top-
    // left corner of the screen
    int x;
    int y;
    //size in pixels
    static final int SIZE = 10;
    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean equals(Cell that) {
        return (this.x == that.x && this.y == that.y);
    }
    public boolean sameCell(Cell that)
    {
        return this.equals(that);
    }
    public int hashCode()
    {
        return this.x + this.y;
    }
    WorldImage cellImage(double waterHeight) {
        return new RectangleImage(new Posn(this.x, this.y),
                Cell.SIZE, Cell.SIZE, new Color(192,192,192));
    }
    Cell find(HashMap<Cell, Cell> h) {
        if(this.sameCell(h.get(this)))
        {
            return this;
        }
        else {
            return h.get(this).find(h);
        }
    }
}

class Player {
    int x;
    int y;
    Cell cell;
}

class MazeWorld extends World {
    static final int WIDTH = 64;
    static final int HEIGHT = 60;
    //player
    Player player = new Player();
    // all the cells
    ArrayList<ArrayList<Cell>> board;
    ArrayList<Edge> edges;
    ArrayList<Edge> walls;
    HashMap<Cell, Cell> representatives;
    MazeWorld() {
        //default constructor
        this.reset(MazeWorld.WIDTH * Cell.SIZE, MazeWorld.HEIGHT * Cell.SIZE);
    }
    void reset(int width, int height) {
        board = new ArrayList<ArrayList<Cell>>();
        edges = new ArrayList<Edge>();
        representatives = new HashMap<Cell, Cell>();
        walls = new ArrayList<Edge>();
        for (int i = 0; i <= MazeWorld.WIDTH; i += 1) {
            ArrayList<Cell> row = new ArrayList<Cell>();
            for (int j = 0; j <= MazeWorld.HEIGHT; j += 1) {
                row.add(new Cell(i, j));
            }
            board.add(row);
        }
        for (int i = 0; i <= MazeWorld.WIDTH; i += 1) {
            for (int j = 0; j <= MazeWorld.HEIGHT; j += 1) {
                if(board.get(i).get(j).x != MazeWorld.WIDTH)
                {
                    edges.add(new Edge(board.get(i).get(j), board.get(i + 1).get(j)));
                }
                if(board.get(i).get(j).y != MazeWorld.HEIGHT)
                {
                    edges.add(new Edge(board.get(i).get(j), board.get(i).get((j + 1))));
                }
            }
        }
        Collections.sort(edges);
        for (int i = 0; i <= MazeWorld.WIDTH; i += 1) {
            for (int j = 0; j <= MazeWorld.HEIGHT; j += 1) {
                representatives.put(board.get(i).get(j), board.get(i).get(j));
            }
        }
        ArrayList<Edge> tempEdges = new ArrayList<Edge>();
        for(Edge e : edges) {
            tempEdges.add(e);
        }
        edges.clear();
        while(tempEdges.size() > 1)
        {
            Edge e1 = tempEdges.get(0);
            if(e1.c1.find(representatives).sameCell(e1.c2.find(representatives))) {
                tempEdges.remove(0);
                this.walls.add(e1);
            }
            else {
                this.edges.add(e1);
                Union(representatives, e1.c1.find(representatives), e1.c2.find(representatives));
                tempEdges.remove(0);
            }
            System.out.println(edges.size() + " " + tempEdges.size());
        }
    }
    void Union(HashMap<Cell, Cell> h, Cell c1, Cell c2) {
        h.put(c1, c2);
    }
    // overlay background onto cells
    public WorldImage makeImage() {
        WorldImage acc = new RectangleImage(new Posn(0, 0), 
                0, 0, new Black());
        for(Edge e: walls)
        {
            acc = new OverlayImages(acc, e.edgeImage());
        }
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
    Cell c1 = new Cell(1, 1);
    Cell c2 = new Cell(1, 1);
    Cell c3 = new Cell(1, 2);
    boolean testSameCell(Tester t) {
        return t.checkExpect(c1.sameCell(c2), true) &&
                t.checkExpect(c1.sameCell(c3), false);
    }
    int runAnimation() {
        MazeWorld m1 = new MazeWorld();
        m1.bigBang(Cell.SIZE * MazeWorld.WIDTH, Cell.SIZE * MazeWorld.HEIGHT, 1);
        return 1;
    }
    int run = this.runAnimation();
}
