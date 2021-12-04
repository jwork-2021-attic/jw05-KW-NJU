package com.game.com.anish.creatures;

import java.util.ArrayList;
import java.util.Random;
import com.game.com.anish.maze.Node;

public class Bfs {
    int[][] maze;
    ArrayList<Integer> plan;
    int setx, sety;
    Random rand = new Random();

    Bfs(int[][] maze) {
        this.maze = new int[maze.length][maze[0].length];
        plan = new ArrayList<Integer>();
        this.setx = -1;
        this.sety = -1;
    }

    private void setmaze(int[][] maze) {
        for (int i = 0; i < maze.length; ++i)
            for (int j = 0; j < maze[0].length; ++j)
                this.maze[i][j] = maze[i][j];
    }

    public void makePlan(int[][] maze, int targetx, int targety, int curx, int cury) {
        // System.out.println("makeplan");
        clearPlan();
        setxy(targetx, targety);
        setmaze(maze);
        ArrayList<Node> cur;
        cur = new ArrayList<Node>();
        cur.add(new Node(curx, cury));
        this.maze[curx][cury] = -1;
        int depth = -2;
        while (!cur.isEmpty()) {
            ArrayList<Node> nxt;
            nxt = new ArrayList<Node>();
            boolean flag = false;
            for (int i = 0; i < cur.size(); ++i) {
                int x = cur.get(i).x, y = cur.get(i).y;
                if (is_close(targetx, targety, x, y)) {
                    this.maze[targetx][targety] = depth;
                    flag = true;
                    break;
                }
                next(nxt, x, y, depth);
            }
            if (flag)
                break;
            cur.clear();
            for (int i = 0; i < nxt.size(); ++i)
                cur.add(nxt.get(i));
            --depth;
            // for (int i = 0; i < 30; ++i) {
            // for (int j = 0; j < 30; ++j)
            // System.out.print(this.maze[i][j]);
            // System.out.println();
            // }
            // System.out.println(depth);
        }
        if (this.maze[targetx][targety] >= 0) {
            plan.add(randomwalk(curx, cury));
            return;
        }
        int x = targetx, y = targety;
        while (depth != -1) {
            ++depth;
            switch (findway(depth, x, y)) {
                case "left":
                    --x;
                    break;
                case "right":
                    ++x;
                    break;
                case "up":
                    --y;
                    break;
                case "down":
                    ++y;
                    break;
            }
        }
    }

    private int randomwalk(int x, int y) {
        ArrayList<Integer> able = new ArrayList<Integer>();
        if (x > 0 && maze[x - 1][y] == -2)
            able.add(27);
        if (x < maze.length - 1 && maze[x + 1][y] == -2)
            able.add(26);
        if (y > 0 && maze[x][y - 1] == -2)
            able.add(24);
        if (y < maze[0].length - 1 && maze[x][y + 1] == -2)
            able.add(25);
        return able.get(rand.nextInt(able.size()));
    }

    private void next(ArrayList<Node> nxt, int x, int y, int depth) {
        if (x > 0 && maze[x - 1][y] == 1) {
            maze[x - 1][y] = depth;
            nxt.add(new Node(x - 1, y));
        }
        if (x < maze.length - 1 && maze[x + 1][y] == 1) {
            maze[x + 1][y] = depth;
            nxt.add(new Node(x + 1, y));
        }
        if (y > 0 && maze[x][y - 1] == 1) {
            maze[x][y - 1] = depth;
            nxt.add(new Node(x, y - 1));
        }
        if (y < maze[0].length - 1 && maze[x][y + 1] == 1) {
            maze[x][y + 1] = depth;
            nxt.add(new Node(x, y + 1));
        }
    }

    private String findway(int depth, int x, int y) {
        String pos = "";
        if (x > 0 && maze[x - 1][y] == depth) {
            pos = "left";
            plan.add(0, 26);
        } else if (x < maze.length - 1 && maze[x + 1][y] == depth) {
            pos = "right";
            plan.add(0, 27);
        } else if (y > 0 && maze[x][y - 1] == depth) {
            pos = "up";
            plan.add(0, 25);
        } else if (y < maze[0].length && maze[x][y + 1] == depth) {
            pos = "down";
            plan.add(0, 24);
        }
        // for (int i = 0; i < plan.size(); ++i)
        // System.out.print(plan.get(i) + " ");
        // System.out.println();
        return pos;
    }

    private void setxy(int x, int y) {
        this.setx = x;
        this.sety = y;
    }

    public boolean has_changed(int x, int y) {
        return !(this.setx == x && this.sety == y);
    }

    public boolean is_close(int targetx, int targety, int x, int y) {
        return (Math.abs(targetx - x) + Math.abs(targety - y)) == 1;
    }

    public void clearPlan() {
        plan.clear();
    }

    public ArrayList<Integer> getPlan() {
        return plan;
    }

}
