package com.game.com.anish.creatures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monster extends Creature implements Runnable {

    Lock lock = new ReentrantLock();
    Random rand = new Random();

    Calabash player;
    Bfs bfs;
    static int cnt;

    public Monster(World world, Calabash player, int[][] maze) {
        super(Color.RED, (char) 1, world);
        this.maze = maze;
        this.player = player;
        this.hp = 20;
        bfs = new Bfs(maze);
        attack = 15;
    }

    public void run() {
        try {
            lock.lock();
            ArrayList<Integer> plan = new ArrayList<Integer>();
            while (is_alive() && player.is_alive()) {
                int x = player.getX(), y = player.getY();
                if (bfs.has_changed(x, y) || plan.isEmpty()) {
                    // System.out.println(bfs.has_changed(x, y));
                    bfs.makePlan(maze, x, y, this.getX(), this.getY());
                    plan = bfs.getPlan();
                    // for (int i = 0; i < plan.size(); ++i)
                    // System.out.print(plan.get(i) + " ");
                }
                if (!plan.isEmpty()) {
                    move(world, plan.get(0));
                    plan.remove(0);
                }
                // System.out.println(plan.isEmpty());
                // System.out.println(bfs.is_close(x, y, this.getX(), this.getY()));
                // System.out.println("(x,y): (" + x + ", " + y + "), (getx,gety): " +
                // this.getX() + ", " + this.getY());
                // for (int i = 0; i < plan.size(); ++i)
                // System.out.print(plan.get(i) + " ");
                // System.out.println();
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (bfs.is_close(player.getX(), player.getY(), this.getX(), this.getY()))
                    attack(player);
                gethurt();
            }
            if (player.is_alive()) {
                world.put(new Floor(world), this.getX(), this.getY());
                maze[this.getX()][this.getY()] = 1;
                --cnt;
            }
        } finally {
            lock.unlock();
        }
    }

    public void attack(Calabash player) {
        player.getattack(attack);
        getattack(player.attack);
        if (is_alive()) {
            try {
                TimeUnit.MILLISECONDS.sleep(800);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void gethurt() {
        int x = this.getX(), y = this.getY();
        if (maze[x][y] >= 10) {
            getattack(maze[x][y]);
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            world.put(this, this.getX(), this.getY());
        }
    }

    public static void setcnt(int cnt) {
        Monster.cnt = cnt;
    }

    public static boolean hasmonster() {
        return Monster.cnt > 0;
    }

    // public void automove(World world) {
    // ArrayList<Integer> moveplan = dfs.getPlan();
    // if (moveplan.size() == 0) {
    // dfs.set(this.getX(), this.getY());
    // dfs.makePlan(player.getX(), player.getY());
    // moveplan = dfs.getPlan();
    // }
    // if (moveplan.size() > 0) {
    // move(world, moveplan.get(0));
    // moveplan.remove(0);
    // }
    // }

}
