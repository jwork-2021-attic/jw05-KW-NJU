package com.game.com.anish.creatures;

import java.awt.Color;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Calabash extends Creature implements Runnable {

    ExecutorService exec;
    int radius;

    public Calabash(World world, int[][] maze) {
        super(Color.GREEN, (char) 2, world);
        hp = 100;
        maxhp = 100;
        this.maze = maze;
        exec = Executors.newCachedThreadPool();
        radius = 1;
        attack = 10;
        Bomb.setbomb(1);
    }

    public void run() {
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
    }

    @Override
    public void move(World world, int pos) {
        if (!is_alive())
            return;
        int x = this.getX();
        int y = this.getY();
        int offsetx = 0, offsety = 0;
        if (pos == 24)
            offsety = -1;
        else if (pos == 25)
            offsety = 1;
        else if (pos == 26)
            offsetx = 1;
        else if (pos == 27)
            offsetx = -1;
        int newx = x + offsetx;
        int newy = y + offsety;
        try {
            world.lock.lock();

            if (newx >= 0 && newx < maze.length && newy >= 0 && newy < maze[x].length && (maze[newx][newy] == 1
                    || maze[newx][newy] == 5 || maze[newx][newy] == 6 || maze[newx][newy] == 7)) {
                this.moveTo(x + offsetx, y + offsety);
                if (maze[x][y] == 3)
                    world.put(new Bomb(world, maze, 1), x, y);
                else {
                    world.put(new Floor(world), x, y);
                    maze[x][y] = 1;
                }
                if (maze[newx][newy] == 5) {
                    hp = Math.min(hp + 10, maxhp);
                    printhp(3, maze.length);
                } else if (maze[newx][newy] == 6) {
                    Bomb.bombitem();
                    printbomb(17, maze.length);
                } else if (maze[newx][newy] == 7) {
                    addradius();
                    printr(28, maze.length);
                }
                maze[newx][newy] = 2;
            }
        } finally {
            world.lock.unlock();

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
        printhp(3, maze.length);
    }

    private void addradius() {
        ++radius;
    }

    private void is_dead() {
        hp = 0;
        printhp(3, maze.length);
        world.put(new Floor(world), this.getX(), this.getY());
        world.put(new Character(world, 'Y'), maze.length - 9, maze.length + 1);
        world.put(new Character(world, 'O'), maze.length - 8, maze.length + 1);
        world.put(new Character(world, 'U'), maze.length - 7, maze.length + 1);
        world.put(new Character(world, ' '), maze.length - 6, maze.length + 1);
        world.put(new Character(world, 'L'), maze.length - 5, maze.length + 1);
        world.put(new Character(world, 'O'), maze.length - 4, maze.length + 1);
        world.put(new Character(world, 'S'), maze.length - 3, maze.length + 1);
        world.put(new Character(world, 'T'), maze.length - 2, maze.length + 1);
        world.put(new Character(world, '!'), maze.length - 1, maze.length + 1);
    }

    public void printhp(int x, int y) {
        int temp = Math.max(0, hp);
        String str = toString(temp);
        for (int i = 0; i < str.length(); ++i)
            world.put(new Character(world, str.charAt(i)), i + x, y);
    }

    public void printbomb(int x, int y) {
        int cnt = Bomb.getbomb();
        if (cnt > 9)
            world.put(new Character(world, (char) (cnt / 10 + '0')), x, y);
        else
            world.put(new Character(world, ' '), x, y);
        world.put(new Character(world, (char) (cnt % 10 + '0')), x + 1, y);
    }

    public void printr(int x, int y) {
        if (radius > 9)
            world.put(new Character(world, (char) (radius / 10 + '0')), x, y);
        else
            world.put(new Character(world, ' '), x, y);
        world.put(new Character(world, (char) (radius % 10 + '0')), x + 1, y);
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
        Bomb bomb = new Bomb(world, maze, radius);
        world.put(bomb, x, y);
        exec.execute(bomb);
        Bomb.usebomb();
    }

    private void win() {
        world.put(new Character(world, 'Y'), maze.length - 8, maze.length + 1);
        world.put(new Character(world, 'O'), maze.length - 7, maze.length + 1);
        world.put(new Character(world, 'U'), maze.length - 6, maze.length + 1);
        world.put(new Character(world, ' '), maze.length - 5, maze.length + 1);
        world.put(new Character(world, 'W'), maze.length - 4, maze.length + 1);
        world.put(new Character(world, 'I'), maze.length - 3, maze.length + 1);
        world.put(new Character(world, 'N'), maze.length - 2, maze.length + 1);
        world.put(new Character(world, '!'), maze.length - 1, maze.length + 1);
    }
}
