package game;
import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.Timer;
import static game.MouseControls.mouseCoords;


public class Player extends Walker {

    /**
     * The View.
     */
    GameView view;
    private static final Shape playerShape = new BoxShape(0.5f, 1);
    private final BodyImage imageStationaryRight = new BodyImage("data/PlayerAssets/idlingRight.gif", 2);
    private final BodyImage imageStationaryLeft = new BodyImage("data/PlayerAssets/idlingLeft.gif", 2);
    private final BodyImage imageMovingRight = new BodyImage("data/PlayerAssets/RunningRight.gif", 2);
    private final BodyImage imageMovingLeft = new BodyImage("data/PlayerAssets/RunningLeft.gif", 2);
    private final BodyImage shootingRight = new BodyImage("data/PlayerAssets/shootingRight.gif", 2);
    private final BodyImage shootingLeft = new BodyImage("data/PlayerAssets/shootingLeft.gif", 2);
    private final BodyImage goingUpRight = new BodyImage("data/PlayerAssets/playerJumpRight.gif", 2);
    private final BodyImage goingUpLeft = new BodyImage("data/PlayerAssets/playerJumpLeft.gif", 2);
    private final BodyImage goingDownRight = new BodyImage("data/PlayerAssets/playerFallRight.gif", 2);
    private final BodyImage goingDownLeft = new BodyImage("data/PlayerAssets/playerFallLeft.gif", 2);
    private final BodyImage punchingRight = new BodyImage("data/PlayerAssets/playerPunchRight.gif", 4);
    private final BodyImage punchingLeft = new BodyImage("data/PlayerAssets/playerPunchLeft.gif", 4);
    private Timer timer;
    private boolean faceRight = true;
    /**
     * The Player is alive.
     */
    public boolean playerIsAlive = true;
    private boolean hasGun = false;
    private boolean gunActive = true;
    /**
     * The Player health.
     */
    public int playerHealth = 150;
    private int bulletShot = 8;
    /**
     * The Playercounter.
     */
    public int playercounter = 0;
    private World world;
    private Game g;
    /**
     * The Current level.
     */
    mapGenerator currentLevel;
    // private SoundClip reloading;
    private boolean canTeleport;
    private SoundClip empty;
    private Vec2 teleportCoords;
    /**
     * The Is punching.
     */
    public boolean isPunching = false;
    /**
     * The Is shooting.
     */
    public boolean isShooting = false;
    /**
     * The Is moving.
     */
    public boolean isMoving = false;
    //  private SoundClip soundClip=null;
    private static SoundClip soundFire;
    private static SoundClip soundAnalogFire;
    private static SoundClip emptyFire;
    /**
     * The Shop open.
     */
    public boolean shopOpen = false;
    /**
     * The V.
     */
    public GameView v;
    /**
     * The Level points.
     */
    public int levelPoints = 0;
    /**
     * The Player strength.
     */
    public int playerStrength = 60;
    /**
     * The Timeline.
     */
    String timeline;
    static{
        try{
            soundFire = new SoundClip("data/firing.mp3");
        }catch (UnsupportedAudioFileException|IOException|LineUnavailableException e) {
            System.out.println(e);
        }
    }

    static{
        try{
            soundAnalogFire = new SoundClip("data/analogFire.mp3");
        }catch (UnsupportedAudioFileException|IOException|LineUnavailableException e) {
            System.out.println(e);
        }
    }

    static{
        try{
            emptyFire = new SoundClip("data/emptyGun.mp3");
        }catch (UnsupportedAudioFileException|IOException|LineUnavailableException e) {
            System.out.println(e);
        }
    }


    /**
     * Instantiates a new Player.
     *
     * @param world the world
     * @param game  the game
     * @param view  the view
     * @param t     the t
     */
    public Player(World world, Game game, GameView view, String t) {
        super(world, playerShape);
        this.addImage(imageStationaryRight);
        this.world = world;
        this.addCollisionListener(new playerCollisionListener());
        timer = new Timer();
        playercounter++;
        this.g = game;
        this.v = view;
        this.timeline = t;

    }

    /**
     * Is has gun boolean.
     *
     * @return the boolean
     */
    public boolean isHasGun() {
        return hasGun;
    }

    /**
     * Enable teleport.
     *
     * @param canPlayerTeleport the can player teleport
     */
    public void enableTeleport(boolean canPlayerTeleport) {
        canTeleport = canPlayerTeleport;
    }

    /**
     * Is can teleport boolean.
     *
     * @return the boolean
     */
    public boolean isCanTeleport() {
        return canTeleport;
    }

    /**
     * Teleport.
     *
     * @param coords the coords
     */
    public void teleport(Vec2 coords) {
        enableTeleport(false);
        setGunActive(true);
        this.setPosition(coords);
    }

    /**
     * Gets player health.
     *
     * @return the player health
     */
    public int getPlayerHealth() {
        return playerHealth;
    }

    /**
     * Gets gun active.
     *
     * @return the gun active
     */
    public boolean getGunActive() {
        return gunActive;
    }

    /**
     * Sets gun active.
     *
     * @param isGunActive the is gun active
     */
    public void setGunActive(boolean isGunActive) {
        gunActive = isGunActive;
    }

    /**
     * Bullet got shot.
     */
    public void bulletGotShot(){
       if(hasGun&&gunActive) {
           bulletShot = bulletShot - 1;
       }
}

    /**
     * Reload.
     */
    public void reload() {
        if (hasGun&&gunActive) {
            bulletShot = 8;
        }
    }

    /**
     * Amount of bullets shot int.
     *
     * @return the int
     */
    public int amountOfBulletsShot() {
        return bulletShot;
    }

    /**
     * Set player health.
     *
     * @param health the health
     */
    public void setPlayerHealth(int health){
        playerHealth = playerHealth + health;
    }

    /**
     * Get player position vec 2.
     *
     * @return the vec 2
     */
    public Vec2 getPlayerPosition(){
        return getPosition();
    }

    /**
     * Player moving right.
     */
    public void PlayerMovingRight() {
        this.removeAllImages();
        this.addImage(imageMovingRight);
    }

    /**
     * Player moving left.
     */
    public void PlayerMovingLeft() {
        this.removeAllImages();
        this.addImage(imageMovingLeft);
    }

    /**
     * Player not moving.
     */
    public void PlayerNotMoving() {
        this.removeAllImages();
        this.addImage(imageStationaryRight);
    }

    /**
     * Player not moving left.
     */
    public void PlayerNotMovingLeft() {
        this.removeAllImages();
        this.addImage(imageStationaryLeft);
    }

    /**
     * Shooting right.
     */
    public void shootingRight() {
        this.removeAllImages();
        this.addImage(shootingRight);
    }

    /**
     * Shooting left.
     */
    public void shootingLeft() {
        this.removeAllImages();
        this.addImage(shootingLeft);
    }

    /**
     * Stop shooting image right.
     */
    public void stopShootingImageRight(){
        this.removeAllImages();
        this.addImage(imageStationaryRight);
    }

    /**
     * Punch.
     */
    public void punch(){
      isPunching=true;
       if(faceRight) {
           this.removeAllImages();
           this.addImage(punchingRight);
       } else {
           this.removeAllImages();
           this.addImage(punchingLeft);
       }

    }

    /**
     * Is y going up.
     */
    public void isYGoingUp() {
        if(getLinearVelocity().y>0 && getLinearVelocity().x>0){
            this.removeAllImages();
            this.addImage(goingUpRight);
        }else if(getLinearVelocity().y>0 && getLinearVelocity().x<0){
            this.removeAllImages();
            this.addImage(goingUpLeft);
        }
    }

    /**
     * Is y going down.
     */
    public void isYGoingDown() {
        if(getLinearVelocity().y<0 && getLinearVelocity().x>0){
            this.removeAllImages();
            this.addImage(goingDownRight);
        }else if(getLinearVelocity().y<0 && getLinearVelocity().x<0){
            this.removeAllImages();
            this.addImage(goingDownLeft);
        }
    }

    /**
     * Is falling boolean.
     *
     * @return the boolean
     */
    public boolean isFalling() {
        return getLinearVelocity().y < 0; // Check if character is going down
    }

    /**
     * Facing right boolean.
     *
     * @return the boolean
     */
    public boolean facingRight() {
        faceRight = true;
        return faceRight;
    }

    /**
     * Facing left boolean.
     *
     * @return the boolean
     */
    public boolean facingLeft() { //if faceRight == false then the character is facing left
        faceRight = false;
        return faceRight;
    }

    /**
     * Enable fire.
     *
     * @param hasGun the has gun
     */
    public void enableFire(boolean hasGun){
        this.hasGun=hasGun;
    }

    /**
     * Is fire enabled boolean.
     *
     * @return the boolean
     */
    public boolean isFireEnabled() {
        return hasGun;
    }

    /**
     * Kill player.
     */
    public void killPlayer(){
        playerIsAlive=false;
    }

    /**
     * The Speed.
     */
    float speed = 35f;

    /**
     * Fire config.
     *
     * @param faceRight the face right
     */
    public void fireConfig(boolean faceRight){ //math to calculate bullet direction is done here
       if(faceRight==true) {
           Projectile bullet = new Projectile(world, new Vec2(this.getPosition().x + 3, getPosition().y),timeline);
           Vec2 direction = mouseCoords.sub(getPosition());
           direction.normalize();
           Vec2 velocity = direction.mul(speed);
           bullet.applyImpulse(velocity, getPosition());
           bullet.setGravityScale(0);
         if(timeline=="Future") {
             soundFire.play();
         }else{
             soundAnalogFire.play();
         }
           shootingRight();
       }if(faceRight==false){
           Projectile bullet = new Projectile(world, new Vec2(getPosition().x - 3, getPosition().y),timeline);
           Vec2 direction = mouseCoords.sub(getPosition());
           direction.normalize();
           Vec2 velocity = direction.mul(speed);
           bullet.applyImpulse(velocity, getPosition());
           bullet.setAngleDegrees(-180);
           bullet.setGravityScale(0);
            if(timeline=="Future") {
                soundFire.play();
            }else{
                soundAnalogFire.play();
            }
           shootingLeft();
       }
    }

    /**
     * Fire.
     */
    public void fire() {
        if(bulletShot>=0) {
            if (playerIsAlive) {
                if (hasGun && gunActive) {
                    if (faceRight) {
                        if (mouseCoords.x > getPosition().x) {
                           fireConfig(true);
                        } else {fireConfig(false);
                        }
                    } else if (!faceRight) {
                        if (mouseCoords.x < getPosition().x) {
                            fireConfig(false);
                        } else {
                           fireConfig(true);
                        }
                    }
                }
            }
        }
    }


    /**
     * The type Player collision listener.
     */
    public class playerCollisionListener implements CollisionListener{
        @Override
        public void collide(CollisionEvent e) { //checks to see if player has been shot by enemies
            if (e.getOtherBody() instanceof Projectile) {
                e.getOtherBody().destroy();
                playerHealth = playerHealth - 20;
                if(playerHealth<=0) {
                    destroy();
                    playerIsAlive=false;
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                        }
                    }, 1000);
                }
            }
        }
    }



}
