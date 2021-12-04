package com.game.com.anish.maze;

import java.util.ArrayList;
import java.util.Random;

public class MazeGenerator {

    private ArrayList<Node> arr = new ArrayList<>();
    private Random rand = new Random();
    private int[][] maze;
    private int dimension;

    public MazeGenerator(int dim) {
        maze = new int[dim][dim];
        dimension = dim;
    }

    public void addplayer(ArrayList<Node> list) {
        for (int i = 0; i < list.size(); ++i) {
            int x = list.get(i).x, y = list.get(i).y;
            for (int j = x - 1; j < x + 2; ++j)
                for (int k = y - 1; k < y + 2; ++k)
                    if (j >= 0 && j < dimension && k >= 0 && k < dimension) {
                        maze[j][k] = 1;
                        arr.add(new Node(j, k));
                    }
        }
    }

    public void generateMaze() {
        for (int i = 0; i < 2; ++i) {
            ArrayList<Node> temp = new ArrayList<>();
            for (int j = 0; j < arr.size(); ++j) {
                Node next = arr.get(j);
                // System.out.println("x:" + next.x + " y:" + next.y);
                ArrayList<Node> neighbors = findNeighbors(next);
                randomlyAddNodesToStack(neighbors, temp);
            }
            arr = temp;
        }
    }

    public int[][] getMaze() {
        return maze;
    }

    private void randomlyAddNodesToStack(ArrayList<Node> nodes, ArrayList<Node> temp) {
        int targetIndex;
        for (int i = 0; i < nodes.size(); ++i) {
            targetIndex = rand.nextInt(10);
            if (targetIndex < 7) {
                Node next = nodes.get(i);
                temp.add(next);
                maze[next.x][next.y] = 1;
            }
        }
    }

    private ArrayList<Node> findNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<>();
        for (int y = node.y - 1; y < node.y + 2; y++) {
            for (int x = node.x - 1; x < node.x + 2; x++) {
                if (pointOnGrid(x, y) && pointNotCorner(node, x, y) && pointNotNode(node, x, y) && maze[x][y] == 0) {
                    neighbors.add(new Node(x, y));
                }
            }
        }
        return neighbors;
    }

    private Boolean pointOnGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < dimension && y < dimension;
    }

    private Boolean pointNotCorner(Node node, int x, int y) {
        return (x == node.x || y == node.y);
    }

    private Boolean pointNotNode(Node node, int x, int y) {
        return !(x == node.x && y == node.y);
    }
}