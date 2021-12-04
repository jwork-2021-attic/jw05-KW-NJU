package com.game.com.anish.creatures;

import java.awt.Color;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bomb extends Thing implements Runnable {

    int[][] maze;
    int len;
    int attack;
    private static int cnt;
    // private ArrayList<Node> arr = new ArrayList<>();
    private Lock lock = new ReentrantLock();

    public Bomb(World world, int[][] maze, int len) {
        super(Color.YELLOW, (char) 9, world);
        this.maze = maze;
        this.len = len;
        this.attack = 25;
    }

    public void run() {
        try {
            lock.lock();
            int x = this.getX(), y = this.getY();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            explode(new Character(world, (char) 15), attack, x, y);
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            explode(new Floor(world), 1, x, y);
            Bomb.addbomb();
        } finally {
            lock.unlock();
        }
    }

    private int max(int a, int b) {
        if (a > b)
            return a;
        else
            return b;
    }

    private int min(int a, int b) {
        if (a < b)
            return a;
        else
            return b;
    }

    private void explode(Thing thing, int stat, int x, int y) {
        int a = max(0, x - len), b = min(maze.length - 1, x + len), c = max(0, y - len),
                d = min(maze.length - 1, y + len);
        for (int i = a; i <= b; ++i) {
            for (int j = c; j <= d; ++j) {
                if (maze[i][j] != 2)
                    world.put(thing, i, j);
                // else {
                // arr.add(new Node(i, j));
                // }
                maze[i][j] = stat;
            }
        }
    }

    public static boolean isempty() {
        return Bomb.cnt == 0;
    }

    public static void setbomb(int num) {
        Bomb.cnt = num;
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
