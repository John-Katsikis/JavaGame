package game;
import city.cs.engine.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;



public class Enemy extends Walker {
    /**
     * Generates random integer between the ranges of min and max.
     *
     * @param min number
     * @param max number
     * @return the integer that is randomly picked, will be used for X coordinate
     */
    public static int randomX(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    /**
     * Generates random integer between the ranges of min and max.
     *
     * @param min number
     * @param max number
     * @return the integer that is randomly picked, will be used for Y coordinate
     */
    public static int randomY(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    /**
     * Picks random integer.
     *
     * @param min number
     * @param max number
     * @return the random integer
     */
    public static int randomPick(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    private static Shape enemyShape = new BoxShape(1, 1);
    private BodyImage goingRight;
    private BodyImage goingLeft;
    private BodyImage enemyDied;
    public static int lifecounter = 0;
    Player player;
    private Timer timer;
    private int enemyCount;
    private World world;
    private Game game;
    GameView view;
    mapGenerator currentLevel;
    public boolean enemyEliminated = false;
    private static int speed = 50;
    public int enemyHealth = 60;
    public String timeline;
    private static SoundClip soundFire;
    private static SoundClip soundAnalogFire;
    private static SoundClip punchMetal;
    private static SoundClip punch;
    //Below is static instantiation of sound files
    static{
        try{
            soundFire = new SoundClip("data/firing.mp3");
        }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    static{
        try{
            soundAnalogFire = new SoundClip("data/analogFire.mp3");
        }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    static{
        try{
            punch = new SoundClip("data/punching.mp3");
        }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    static{
        try{
            punchMetal = new SoundClip("data/punchingMetal.mp3");
        }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    /**
     * Gets enemy location.
     *
     * @return the enemy location
     */
    public Vec2 getEnemyLocation() {
        return this.getPosition();
    }

    /**
     * Enemy won.
     */
    public void enemyWon() {
        this.removeAllImages();
    }

    /**
     * Instantiates a new Enemy, t indicates which animations will be used, player in the parameter is the target.
     *
     * @param world    the world
     * @param fireTime the fire time
     * @param player   the player
     * @param g        the g
     * @param t        the t
     */
    public Enemy(World world, int fireTime, Player player, Game g, String t) {
        super(world, enemyShape);
        this.world = world;
        this.setGravityScale(0);
        this.addCollisionListener(new enemyCollisionListener());
        timer = new Timer(fireTime, new FireListener());
        timer.start();
        this.player = player;
        lifecounter++;
        this.game = g;
        this.timeline=t;
        difficultyModifier();
        if(timeline=="Past"){
            goingRight = new BodyImage("data/angel-idle.gif",6);
            this.addImage(goingRight);
            goingLeft = new BodyImage("data/angel-idle.gif",6);
        }

        if(timeline=="Present"){
            goingRight = new BodyImage("data/EnemyAssets/presentEnemyRight.gif",3);
            goingLeft = new BodyImage("data/EnemyAssets/presentEnemyLeft.gif",3);
            this.addImage(goingLeft);
        }

        if(timeline=="Future"){
            goingRight = new BodyImage("data/EnemyAssets/droneFlyingRight.gif",3);
            goingLeft = new BodyImage("data/EnemyAssets/droneFlyingleft.gif",3);
            this.addImage(goingLeft);
        }
    }

    /**
     * Difficulty modifier, increases health and speed as levels go on.
     */
    public void difficultyModifier(){
        int currentLevel = game.getLevelcounter();
        if(currentLevel>=2){
            increaseSpeed(10);
            increaseHealth(10);
        }
        if(currentLevel>=4){
            increaseSpeed(20);
            increaseHealth(20);
        }
        if(currentLevel>=6){
            increaseSpeed(25);
            increaseHealth(30);
        }
        if(currentLevel>=8){
            increaseSpeed(30);
            increaseHealth(40);
        }
        if(currentLevel>=10){
            increaseSpeed(35);
            increaseHealth(50);
        }
    }

    /**
     * Increases max enemy health.
     *
     * @param amount the amount
     */
    public void increaseHealth(int amount){
        enemyHealth = enemyHealth + amount;
    }

    /**
     * Increase speed.
     *
     * @param amount the amount
     */
    public void increaseSpeed(int amount){
        speed = speed + amount;
    }

    /**
     * Picks random number, and then chooses between different speed ranges.
     */
    public void enemyMovement() {
        int chooser = randomPick(0, 2);
        switch (chooser) {
            case 0:
                this.setLinearVelocity(new Vec2(randomX(-25, 25), randomY(-25, 25)));
                if(this.getLinearVelocity().x>0){
                    this.removeAllImages();
                    this.addImage(goingRight);
                }else{
                    this.removeAllImages();
                    this.addImage(goingLeft);
                }
                break;
            case 1:
                this.setLinearVelocity(new Vec2(randomX(-15, 15), randomY(-15, 15)));
                if(this.getLinearVelocity().x>0){
                    this.removeAllImages();
                    this.addImage(goingRight);
                }else{
                    this.removeAllImages();
                    this.addImage(goingLeft);
                }
                break;
            case 2:
                this.setLinearVelocity(new Vec2(randomX(-5, 5), randomY(-5, 5)));
                if(this.getLinearVelocity().x>0){
                    this.removeAllImages();
                    this.addImage(goingRight);
                }else{
                    this.removeAllImages();
                    this.addImage(goingLeft);
                }
        }
    }


    /**
     * Calculates coordinates and direction to fire at.
     */
    public void enemyFire() { //enemy fires at players position
        if (player.playerIsAlive == true) {
            if (player.getPosition().x > getPosition().x) {
                if (enemyEliminated == false) {
                    Projectile bullet = new Projectile(world, new Vec2(getEnemyLocation().x + 3, getEnemyLocation().y),timeline);
                    Vec2 playerDirection = player.getPosition().sub(getPosition());
                    playerDirection.normalize();
                    Vec2 velocity = playerDirection.mul(speed);
                    bullet.applyImpulse(velocity, getPosition());
                    bullet.setGravityScale(0);
                    if(timeline=="Future") {
                        soundFire.play();
                    }else{
                        soundAnalogFire.play();
                    }
                }
                if (enemyEliminated == true) {
                    timer.stop();
                    System.out.println("Enemy eliminated");
                }
            }
            if (player.getPosition().x < getPosition().x) {
                if (enemyEliminated == false) {
                    Projectile bullet = new Projectile(world, new Vec2(getEnemyLocation().x - 3, getEnemyLocation().y),timeline);
                    Vec2 playerDirection = player.getPosition().sub(getPosition());
                    playerDirection.normalize();
                    Vec2 velocity = playerDirection.mul(speed);
                    bullet.applyImpulse(velocity, getPosition());
                    bullet.setGravityScale(0);
                }
                if (enemyEliminated == true) {
                    timer.stop();
                    System.out.println("Enemy eliminated");
                }
            }
        } else {
            enemyWon();
            this.setLinearVelocity(new Vec2(0, 0));
        }
    }

    /* Firelistener that triggers the enemyFire and enemyMovement
     *according to a timer in the constructor
     */
    private class FireListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            enemyFire();
            enemyMovement();

        }
    }

    /**
     * Collision listener that listens to player punching or being shot at by player.
     */
    public class enemyCollisionListener implements CollisionListener {
        @Override
        public void collide(CollisionEvent e) { //checks to see if enemy has been shot with a Projectile
            if(e.getOtherBody() instanceof Player){
                if(player.isPunching){
                    enemyHealth=enemyHealth-player.playerStrength;
                    if(timeline=="Past"){
                        punch.play();
                    }else{
                        punchMetal.play();
                    }
                    if(enemyHealth<=0){
                        timer.stop();
                        destroy();
                        lifecounter--;
                    }
                }
            }

            if (e.getOtherBody() instanceof Projectile) {
                e.getOtherBody().destroy();
                enemyHealth = enemyHealth - 20;
                if (enemyHealth <= 0) {
                    timer.stop();
                    destroy();
                    lifecounter--;
                }
            }
        }
    }
}
