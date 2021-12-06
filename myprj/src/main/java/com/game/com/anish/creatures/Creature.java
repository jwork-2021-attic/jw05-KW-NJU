package com.game.com.anish.creatures;

import java.awt.Color;

public class Creature extends Thing {

    int[][] maze;
    int hp;
    int maxhp;
    int attack;

    Creature(Color color, char glyph, World world) {
        super(color, glyph, world);
    }

    public void moveTo(int xPos, int yPos) {
        this.world.put(this, xPos, yPos);
    }

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
            if (newx >= 0 && newx < maze.length && newy >= 0 && newy < maze[x].length && maze[newx][newy] == 1) {
                this.moveTo(x + offsetx, y + offsety);
                if (maze[x][y] == 3)
                    world.put(new Bomb(world, maze, 1), x, y);
                else {
                    world.put(new Floor(world), x, y);
                    maze[x][y] = 1;
                }
                maze[newx][newy] = 2;
            }
        } finally {
            world.lock.unlock();
        }
    }

    public void gethurt() {
        hp -= maze[this.getX()][this.getY()];
    }

    protected boolean is_alive() {
        return hp > 0;
    }

    public void getattack(int num) {
        hp -= num;
    }
}
