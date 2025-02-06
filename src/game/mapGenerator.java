package game;

import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;
import city.cs.engine.World;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import java.util.*;
import javax.swing.*;

import java.awt.*;

public abstract class mapGenerator extends World {
    /**
     * Random pick int.
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    public static int randomPick(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    private int levelcounter=0;
    private Player player1;
    private Enemy enemy1;
    private Enemy enemy2;
    private Enemy enemy3;
    private Enemy[] enemies = new Enemy[3];
    private Gun pistol;
    private World world;
    private Vec2[] platformCoordinates;
    private Vec2[] oppositePlatformCoordinates;
    private BoxShape[] platformShapes;
    private StaticBody[] platforms;
    private StaticBody[] oppositePlatforms;
    /**
     * The Game.
     */
    public Game game;
    private HealthPickup medkit1;
    private Projectile bullet1;
    /**
     * The X coords.
     */
    int[] xCoords = new int[6];
    /**
     * The Y coords.
     */
    int[] yCoords = new int[6];

    /**
     * Random width int.
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    public static int randomWidth(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    /**
     * Random height int.
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    public static int randomHeight(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    /**
     * Random x int.
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    public static int randomX(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    /**
     * Random y int.
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    public static int randomY(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    /**
     * Instantiates a new Map generator.
     *
     * @param g the g
     * @param v the v
     */
    public mapGenerator(Game g, GameView v) {
        //super();
        this.game = g;


        this.world=world;
        levelcounter++;
        platformCoordinates = new Vec2[randomPick(0,5)];
        oppositePlatformCoordinates = new Vec2[platformCoordinates.length];
        platforms = new StaticBody[platformCoordinates.length];
        oppositePlatforms = new StaticBody[oppositePlatformCoordinates.length];
        platformShapes = new BoxShape[platformCoordinates.length];

        xCoords[0] = randomX(15, 20);
        xCoords[1] = xCoords[0] - (2 * xCoords[0]);

        xCoords[2] = randomX(5, 20);
        xCoords[3] = xCoords[2] - (2 * xCoords[2]);

        yCoords[0] = randomY(3, 8);
        yCoords[1] = yCoords[0] - (2 * yCoords[0]);


        //BOUNDARIES - DO NOT TOUCH
        Shape properGround = new BoxShape(100.5f,0.75f);
        StaticBody properGround1 = new StaticBody(this,properGround);
        properGround1.setPosition(new Vec2(0,-18.73f));
        properGround1.setFillColor(Color.blue);

        Shape properWallLeft = new BoxShape(1f,100f);
        StaticBody properWallLeft1 = new StaticBody(this,properWallLeft);
        properWallLeft1.setPosition(new Vec2(-34f,0));
        properWallLeft1.setAngleDegrees(0);

        Shape properWallRight = new BoxShape(1f,100f);
        StaticBody properWallRight1 = new StaticBody(this,properWallRight);
        properWallRight1.setPosition(new Vec2(34f,0));
        properWallRight1.setAngleDegrees(0);

        Shape roof = new BoxShape(100.5f,0.75f);
        StaticBody roof1 = new StaticBody(this,roof);
        roof1.setPosition(new Vec2(0,18.73f));

    }


    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
            return player1;
        }
}



