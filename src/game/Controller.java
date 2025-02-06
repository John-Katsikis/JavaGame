package game;
import city.cs.engine.SoundClip;
import org.jbox2d.common.Vec2;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import static java.awt.event.KeyEvent.*;

public class Controller implements KeyListener{
    Player player1;
    mapGenerator currentLevel;
    private boolean isPaused = false;
    private boolean isOpen=false;
    private SoundClip reloading;
    private Game game;

    /**
     * Instantiates a new Controller to the player.
     *
     * @param p     the p
     * @param level the level
     * @param g     the g
     */
    public Controller(Player p,mapGenerator level, Game g) {
        player1 = p;
        currentLevel = level;
        this.game = g;
    }

    @Override
    public void keyTyped(KeyEvent d) {
    }


    @Override
    public void keyPressed(KeyEvent d) {
        int code = d.getKeyCode();
        boolean lastPressed=false;
        if (code == VK_D) {
            player1.startWalking(15);
            player1.PlayerMovingRight();
            player1.facingRight();
        } else if (code == VK_A) {
            player1.startWalking(-15);
            player1.PlayerMovingLeft();
            player1.facingLeft();
       } else if (code == VK_SPACE) {
            player1.jump(15);
            player1.isYGoingUp();
        }else if (code == VK_S) {
            player1.setGravityScale(5);
            player1.isYGoingDown();
        } else if (code == VK_E) {
               player1.punch();
    }else if(code == VK_R){
            player1.reload();
           if(player1.isFireEnabled()) {
               try {
                   reloading = new SoundClip("data/pistolReload.mp3");
                   reloading.play();
               } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                   System.out.println(e);
               }
           }
        }else if(code == VK_ESCAPE){
            isPaused = !isPaused;
            if (isPaused) {
                game.getCurrentLevel().stop();
            } else {
                game.getCurrentLevel().start();
            }
        }else if(code==VK_BACK_SPACE){
            game.updateHighScore(game.getLevelCounter());
            System.exit(0);
        }else if(code==VK_0){
            player1.killPlayer();
        }else if(code==VK_1){
            player1.setGunActive(true);
            player1.enableTeleport(false);
        }else if(code==VK_2){
            player1.setGunActive(false);
            player1.enableTeleport(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent d) {
        int code = d.getKeyCode();
        if (code == VK_D) {
            player1.startWalking(0);
            player1.PlayerNotMoving();
            System.out.println(VK_D);
        } else if (code == VK_A) {
            player1.startWalking(0);
            player1.PlayerNotMovingLeft();
        }else if (code == VK_S){
            player1.setGravityScale(1);
        }else if (code == VK_E){
            player1.isPunching=false;
        }
    }

    /**
     * Get game state boolean.
     *
     * @return the boolean
     */
    public boolean getGameState(){
        return isPaused;
    }

    /**
     * Update player.
     *
     * @param player the player
     */
    public void updatePlayer(Player player){
        player1 = player;
    }

}
