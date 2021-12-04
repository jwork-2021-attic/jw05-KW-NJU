package com.game.com.anish.creatures;

import java.awt.Color;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Calabash extends Creature implements Runnable {

    ExecutorService exec;
    int len;
    private Lock lock = new ReentrantLock();

    public Calabash(World world, int[][] maze) {
        super(Color.GREEN, (char) 2, world);
        hp = 100;
        this.maze = maze;
        exec = Executors.newCachedThreadPool();
        len = 1;
        attack = 10;
        Bomb.setbomb(1);
    }

    public void run() {
        try {
            lock.lock();
            maze[0][0] = 2;
            while (is_alive() && Monster.hasmonster()) {
                // --hp;
                gethurt();
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (Monster.hasmonster())
                is_dead();
            else
                win();
        } finally {
            lock.unlock();
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

    @Override
    public void getattack(int num) {
        hp -= num;
        Printhp();
    }

    private void is_dead() {
        hp = 0;
        Printhp();
        world.put(new Floor(world), this.getX(), this.getY());
        world.put(new Character(world, 'Y'), maze.length - 9, maze.length);
        world.put(new Character(world, 'O'), maze.length - 8, maze.length);
        world.put(new Character(world, 'U'), maze.length - 7, maze.length);
        world.put(new Character(world, ' '), maze.length - 6, maze.length);
        world.put(new Character(world, 'L'), maze.length - 5, maze.length);
        world.put(new Character(world, 'O'), maze.length - 4, maze.length);
        world.put(new Character(world, 'S'), maze.length - 3, maze.length);
        world.put(new Character(world, 'T'), maze.length - 2, maze.length);
        world.put(new Character(world, '!'), maze.length - 1, maze.length);
    }

    private void Printhp() {
        String str = toString(hp);
        for (int i = 0; i < str.length(); ++i)
            world.put(new Character(world, str.charAt(i)), i + 3, 30);
    }

    private String toString(int num) {
        int a = num % 10;
        num /= 10;
        int b = num % 10;
        num /= 10;
        int c = num;
        String str = "";
        if (c == 0 && b == 0) {
            str += "  ";
            str += (char) (a + '0');
        } else if (c == 0) {
            str += ' ';
            str += (char) (b + '0');
            str += (char) (a + '0');
        } else
            str += "100";
        return str;
    }

    public void setbomb(World world) {
        if (Bomb.isempty())
            return;
        int x = this.getX();
        int y = this.getY();
        maze[x][y] = 3;
        Bomb bomb = new Bomb(world, maze, len);
        world.put(bomb, x, y);
        exec.execute(bomb);
        Bomb.usebomb();
    }

    private void win() {
        world.put(new Character(world, 'Y'), maze.length - 8, maze.length);
        world.put(new Character(world, 'O'), maze.length - 7, maze.length);
        world.put(new Character(world, 'U'), maze.length - 6, maze.length);
        world.put(new Character(world, ' '), maze.length - 5, maze.length);
        world.put(new Character(world, 'W'), maze.length - 4, maze.length);
        world.put(new Character(world, 'I'), maze.length - 3, maze.length);
        world.put(new Character(world, 'N'), maze.length - 2, maze.length);
        world.put(new Character(world, '!'), maze.length - 1, maze.length);
    }
}
