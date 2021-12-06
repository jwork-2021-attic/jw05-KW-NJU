package com.game.com.anish.creatures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Monster extends Creature implements Runnable {

    Random rand;

    Calabash player;
    Bfs bfs;
    static int maxcnt;
    static int cnt;

    public Monster(World world, Calabash player, int[][] maze) {
        super(Color.RED, (char) 1, world);
        rand = new Random();
        this.maze = maze;
        this.player = player;
        this.hp = 20;
        bfs = new Bfs(maze);
        attack = 15;
    }

    public void run() {
        ArrayList<Integer> plan = new ArrayList<Integer>();
        while (is_alive() && player.is_alive()) {
            int x = player.getX(), y = player.getY();
            if (bfs.has_changed(x, y) || plan.isEmpty()) {
                try {
                    world.lock.lock();
                    bfs.makePlan(maze, x, y, this.getX(), this.getY());
                } finally {
                    world.lock.unlock();
                    plan = bfs.getPlan();
                }

            }
            if (!plan.isEmpty()) {
                move(world, plan.get(0));
                plan.remove(0);
            }
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
            try {
                world.lock.lock();
                --cnt;
                printcnt(13, maze.length + 1);
                createitem(this.getX(), this.getY());
            } finally {
                world.lock.unlock();
            }
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
            // world.put(this, this.getX(), this.getY());
        }
    }

    public void createitem(int i, int j) {
        int seed = rand.nextInt(10);
        if (seed < 3) {
            world.put(new Floor(world), this.getX(), this.getY());
            maze[this.getX()][this.getY()] = 1;
        } else if (seed < 6) {
            world.put(new Thing(Color.ORANGE, (char) 3, world), i, j);
            maze[i][j] = 5;
        } else if (seed < 8) {
            world.put(new Thing(Color.ORANGE, (char) 7, world), i, j);
            maze[i][j] = 6;
        } else if (seed < 10) {
            world.put(new Thing(Color.ORANGE, (char) 43, world), i, j);
            maze[i][j] = 7;
        }
        return;
    }

    public void printcnt(int x, int y) {
        int num = maxcnt - cnt;
        if (num > 9)
            world.put(new Character(world, (char) (num / 10 + '0')), x, y);
        else
            world.put(new Character(world, ' '), x, y);
        world.put(new Character(world, (char) (num % 10 + '0')), x + 1, y);
    }

    public static void setcnt(int cnt) {
        Monster.maxcnt = cnt;
        Monster.cnt = cnt;
    }

    public static boolean hasmonster() {
        return Monster.cnt > 0;
    }
}