package com.game.com.anish.creatures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.game.com.anish.maze.Node;

public class Bomb extends Thing implements Runnable {

    int[][] maze;
    int radius;
    int attack;
    private static int maxcnt;
    private static int cnt;

    public Bomb(World world, int[][] maze, int radius) {
        super(Color.YELLOW, (char) 9, world);
        this.maze = maze;
        this.radius = radius;
        this.attack = 25;
    }

    public void run() {
        int x = this.getX(), y = this.getY();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (maze[x][y] == 3)
            explode(new Character(world, (char) 15), new Floor(world), attack, 1, x, y, false);
        Bomb.addbomb();
    }

    private void explode(Thing thing1, Thing thing2, int stat1, int stat2, int x, int y, Boolean create) {
        // int a = max(0, x - radius), b = min(maze.length - 1, x + radius), c = max(0,
        // y - radius),
        // d = min(maze.length - 1, y + radius);
        Bfs bfs = new Bfs(maze);
        ArrayList<Node> arr = bfs.getrange(maze, x, y, radius);
        // new ArrayList<Node>();
        // for (int i = 0; i < 30; ++i) {
        // for (int j = 0; j < 30; ++j)
        // arr.add(new Node(i, j));
        // }
        for (int i = 0; i < arr.size(); ++i) {
            int a = arr.get(i).x, b = arr.get(i).y;
            if (maze[a][b] != 2)
                world.put(thing1, a, b);
            maze[a][b] = stat1;
        }
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (int i = 0; i < arr.size(); ++i) {
            int a = arr.get(i).x, b = arr.get(i).y;
            if (maze[a][b] != 2)
                world.put(thing2, a, b);
            maze[a][b] = stat2;
        }
    }

    public static boolean isempty() {
        return Bomb.cnt == 0;
    }

    public static int getbomb() {
        return Bomb.maxcnt;
    }

    public static void setbomb(int num) {
        Bomb.maxcnt = num;
        Bomb.cnt = num;
    }

    public static void bombitem() {
        ++Bomb.maxcnt;
        ++Bomb.cnt;
    }

    public static void addbomb() {
        ++Bomb.cnt;
    }

    public static void usebomb() {
        --Bomb.cnt;
    }

    // private void recover() {
    // for (int i = 0; i < arr.size(); ++i)
    // maze[arr.get(i).x][arr.get(i).y] = 2;
    // }
    // public void automove(World world) {
    // ArrayList<Integer> moveplan = dfs.getPlan();
    // if (moveplan.size() == 0) {
    // dfs.set(this.getX(), this.getY());
    // dfs.makePlan();
    // moveplan = dfs.getPlan();
    // }
    // if (moveplan.size() > 0){
    // move(world, moveplan.get(0));
    // moveplan.remove(0);
    // }
    // }

}
