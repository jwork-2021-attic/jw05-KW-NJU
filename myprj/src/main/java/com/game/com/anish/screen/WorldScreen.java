package com.game.com.anish.screen;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.game.com.anish.creatures.Calabash;
import com.game.com.anish.creatures.World;
import com.game.com.anish.creatures.Wall;
import com.game.com.anish.creatures.Floor;
import com.game.com.anish.creatures.Character;
import com.game.com.anish.creatures.Monster;
import com.game.asciiPanel.AsciiPanel;

import com.game.com.anish.maze.MazeGenerator;
import com.game.com.anish.maze.Node;

public class WorldScreen implements Screen {

    int dimension;
    World world;
    Calabash player;
    ExecutorService exec;
    int[][] maze;// 0墙 1地板 2玩家 3炸弹 4爆炸 5加HP 6加炸弹 7加范围
    ArrayList<Node> playerlist;
    Random rand;

    public WorldScreen() {
        dimension = 30;
        world = new World();
        playerlist = new ArrayList<Node>();
        rand = new Random();
        randomaddplayer(8);
        init_world();
        exec = Executors.newCachedThreadPool();
        init_creature();
        exec.shutdown();
    }

    private void randomaddplayer(int num) {
        Monster.setcnt(num);
        playerlist.add(new Node(4, 4));
        ArrayList<Integer> temp = new ArrayList<Integer>();
        int len = dimension / 3;
        for (int i = 0; i < num; ++i) {
            if (i % 8 == 0)
                temp.clear();
            int xrand = rand.nextInt(3), yrand = rand.nextInt(3);
            while (temp.contains(xrand * 3 + yrand) || (xrand == 0 && yrand == 0)) {
                xrand = rand.nextInt(3);
                yrand = rand.nextInt(3);
            }
            temp.add(xrand * 3 + yrand);
            int x = xrand * len + rand.nextInt(len), y = yrand * len + rand.nextInt(len);
            playerlist.add(new Node(x, y));
        }
        // for (int i = 0; i < temp.size(); ++i)
        // System.out.println(temp.get(i));
    }

    private void init_world() {
        MazeGenerator mazeGenerator = new MazeGenerator(dimension);
        mazeGenerator.addplayer(playerlist);
        mazeGenerator.generateMaze();
        maze = mazeGenerator.getMaze();
        player = new Calabash(world, maze);
        // for (int i = 0; i < 30; ++i) {
        // for (int j = 0; j < 30; ++j)
        // maze[i][j] = 1;
        // }
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (maze[i][j] == 0) {
                    Wall wall = new Wall(world);
                    world.put(wall, i, j);
                } else {
                    Floor floor = new Floor(world);
                    world.put(floor, i, j);
                }
            }
        }
        world.put(new Character(world, 'H'), 0, dimension);
        world.put(new Character(world, 'P'), 1, dimension);
        world.put(new Character(world, ':'), 2, dimension);
        player.printhp(3, dimension);
        world.put(new Character(world, '/'), 6, dimension);
        player.printhp(7, dimension);
        world.put(new Character(world, ' '), 10, dimension);
        world.put(new Character(world, ' '), 11, dimension);
        world.put(new Character(world, 'B'), 12, dimension);
        world.put(new Character(world, 'O'), 13, dimension);
        world.put(new Character(world, 'M'), 14, dimension);
        world.put(new Character(world, 'B'), 15, dimension);
        world.put(new Character(world, ':'), 16, dimension);
        player.printbomb(17, dimension);
        world.put(new Character(world, ' '), 19, dimension);
        world.put(new Character(world, ' '), 20, dimension);
        world.put(new Character(world, 'R'), 21, dimension);
        world.put(new Character(world, 'A'), 22, dimension);
        world.put(new Character(world, 'D'), 23, dimension);
        world.put(new Character(world, 'I'), 24, dimension);
        world.put(new Character(world, 'U'), 25, dimension);
        world.put(new Character(world, 'S'), 26, dimension);
        world.put(new Character(world, ':'), 27, dimension);
        player.printr(28, dimension);
        for (int i = 30; i < dimension; ++i)
            world.put(new Character(world, ' '), i, dimension);
        world.put(new Character(world, 'E'), 0, dimension + 1);
        world.put(new Character(world, 'N'), 1, dimension + 1);
        world.put(new Character(world, 'E'), 2, dimension + 1);
        world.put(new Character(world, 'M'), 3, dimension + 1);
        world.put(new Character(world, 'Y'), 4, dimension + 1);
        world.put(new Character(world, ' '), 5, dimension + 1);
        world.put(new Character(world, 'K'), 6, dimension + 1);
        world.put(new Character(world, 'I'), 7, dimension + 1);
        world.put(new Character(world, 'L'), 8, dimension + 1);
        world.put(new Character(world, 'L'), 9, dimension + 1);
        world.put(new Character(world, 'E'), 10, dimension + 1);
        world.put(new Character(world, 'D'), 11, dimension + 1);
        world.put(new Character(world, ':'), 12, dimension + 1);
        world.put(new Character(world, ' '), 13, dimension + 1);
        world.put(new Character(world, '0'), 14, dimension + 1);
        world.put(new Character(world, '/'), 15, dimension + 1);
        world.put(new Character(world, '8'), 16, dimension + 1);
        world.put(new Character(world, ' '), 17, dimension + 1);
        for (int i = 18; i < dimension; ++i)
            world.put(new Character(world, ' '), i, dimension + 1);
    }

    private void init_creature() {
        // for (int i = 0; i < monster; ++i) {
        exec.execute(player);
        world.put(player, playerlist.get(0).x, playerlist.get(0).y);
        for (int i = 1; i < playerlist.size(); ++i) {
            Monster monster = new Monster(world, player, maze);
            exec.execute(monster);
            world.put(monster, playerlist.get(i).x, playerlist.get(i).y);
        }
        // Monster monster1 = new Monster(world, player, maze);
        // exec.execute(monster1);
        // world.put(monster1, 0, 29);
        // }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (!Monster.hasmonster())
            return this;
        switch (key.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.move(world, 27);
                break;
            case KeyEvent.VK_RIGHT:
                player.move(world, 26);
                break;
            case KeyEvent.VK_UP:
                player.move(world, 24);
                break;
            case KeyEvent.VK_DOWN:
                player.move(world, 25);
                break;
            case KeyEvent.VK_SPACE:
                player.setbomb(world);
                break;
        }
        return this;
    }

}
