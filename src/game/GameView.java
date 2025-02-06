package game;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import city.cs.engine.UserView;
import org.jbox2d.common.Vec2;
import city.cs.engine.*;

public class GameView extends UserView {
    private Image pistol;
    private Image fist;
    private Image teleport;
    private Image background;
    private Player player;
    private Enemy enemy;
    private boolean playerIsDead;
    private boolean over;
    private Timer timer;
    private Game game;
    StepListener listen;
    mapGenerator levelType;
    Vec2 pauseCoords = new Vec2(-11,12);
    private SoundClip empty;
    public static Font TEST_FONT = new Font("Monospaced",Font.PLAIN,15);


    /**
     * Random integer pick.
     *
     * @param min the min
     * @param max the max
     * @return the int
     */
    public static int randomPick(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    /**
     * Instantiates a new Game view.
     *
     * @param w      the w
     * @param width  the width
     * @param height the height
     * @param player the player
     * @param game   the game
     */
    public GameView(mapGenerator w, int width, int height, Player player, Game game) {
        super(w, width, height);
        this.levelType = w;
        background = new ImageIcon("data/nebula.jpg").getImage();
        this.player = player;
        this.over=false;
        this.timer = new Timer();
        this.game = game;
        changeBackground(game.currentLevel);
    }

    /**
     * Changes background according to the instance of the currentLevel
     *
     * @param currentLevel the current level
     */
    public void changeBackground(mapGenerator currentLevel) {
        if (currentLevel instanceof LevelPast) {
            background = new ImageIcon("data/PlatformAssets/feudalJapanPastBackground.jpeg").getImage();
        } else if (currentLevel instanceof LevelPresent) {
            background = new ImageIcon("data/PlatformAssets/modernCityPresent.jpeg").getImage();
        } else if (currentLevel instanceof LevelFuture) {
            background = new ImageIcon("data/PlatformAssets/dystopiaFuture.jpg").getImage();
        }
    }



    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(background,0,0,getWidth(),getHeight(),this);
    }

    @Override
    public void paintForeground(Graphics2D g) {
        if(player.isHasGun()) {
            drawEquipment(g, 0, 12, "Pistol");
        }
        if(player.isCanTeleport()){
            drawEquipment(g,0,12,"Teleport");
        }
        int health = player.getPlayerHealth();
        String infinity = "∞";
        g.setColor(new Color(255, 165, 0));
        g.setFont(TEST_FONT);
        g.drawString("Current level: "+String.valueOf(game.getLevelcounter()),11,12);
        if (player.playerIsAlive) {
            g.drawString(String.valueOf(health), worldToView(player.getPosition()).x - 12, worldToView(player.getPosition()).y - 28);
            drawHealthBar(g);
            if(game.isGamePaused()){
                drawPauseScreen(g);
            }else{
                if (over){
                    centerTextInput(g,"Progressing level",Color.YELLOW);
                    player.enableFire(false);
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            gameRestart();
                        }
                    }, 3000);
                }
                if(player.isFireEnabled()) {
                    g.setColor(new Color(255,165,0));
                    g.drawString(player.amountOfBulletsShot() + "/" + infinity, worldToView(player.getPosition()).x + 35, worldToView(player.getPosition()).y - 0);
                }
            }
        }else{
            //DEATH CODE
            game.updateHighScore(game.getLevelCounter());
            g.drawString("X_X",worldToView(player.getPosition()).x - 5, worldToView(player.getPosition()).y - 28);
            centerTextInput(g,"YOU DIED",Color.RED);
            player.enableFire(false);
        }

    }

    /* Once pause is pressed, freezes game and draws a semi-transparent rectangle and writes "GAME PAUSED" in the middle
     *
     */
    private void drawPauseScreen(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180)); // 128 apparently means transparency level, right in the middle 0 is fully transparent and 255 is fully opaque
        g.fillRect(0, 0, getWidth(), getHeight());
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.setColor(Color.WHITE); // Set text color to white
        FontMetrics fontMetrics = g.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth("GAME PAUSED");
        int stringHeight = fontMetrics.getHeight();
        int x = centerX - stringWidth / 2;
        int y = centerY + stringHeight / 4;
        g.drawString("GAME PAUSED", x, y);
    }

    /**
     * Draw equipment.
     *
     * @param g    the g
     * @param x    the x
     * @param y    the y
     * @param name the name
     */
    public void drawEquipment(Graphics2D g, int x, int y,String name){
        if(player.getGunActive()) {
            if (name == "Pistol") {
                g.setColor(new Color(255, 255, 255, 128));
                if(levelType instanceof LevelPast){
                    pistol = new ImageIcon("data/pastRevolver.png").getImage();
                }
                if(levelType instanceof LevelPresent){
                    pistol = new ImageIcon("data/Glock18.png").getImage();
                }
                if(levelType instanceof LevelFuture) {
                    pistol = new ImageIcon("data/scifiPistol.png").getImage();
                }
                // Draw a rectangle with the same size as the image, but shifted down by a bit
                int rectWidth = pistol.getWidth(this);
                int rectHeight = pistol.getHeight(this);
                g.fillRect(x, y + 10, rectWidth, rectHeight);
                // Draw the image on top of the rectangle
                g.drawImage(pistol, x, y, this);
            }
            if(name=="Teleport"){
                g.setColor(new Color(255, 255, 255, 128));
                Image teleport = new ImageIcon("data/teleportation.png").getImage();
                int rectWidth = pistol.getWidth(this);
                int rectHeight = pistol.getHeight(this);
                g.fillRect(x, y + 10, rectWidth, rectHeight); // Adjust the y-coordinate to shift the rectangle down
                // Draw the image on top of the rectangle
                g.drawImage(teleport, x, y, this);
            }
        }
    }

    /**
     * Centers text input, used to make the loss screen and the progression screen
     *
     * @param g         the g
     * @param textInput the text input
     * @param color     the color
     */
    public void centerTextInput(Graphics2D g,String textInput, Color color){
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.setColor(color); // Set text color to white
        FontMetrics fontMetrics = g.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(textInput);
        int stringHeight = fontMetrics.getHeight();
        int x = centerX - stringWidth / 2;
        int y = centerY + stringHeight / 4;
        g.drawString(textInput, x, y);
    }



    /*
     * Draws a healthbar on top of the player
     *
     */
    private void drawHealthBar(Graphics2D g) {
        int health = player.getPlayerHealth();
        int maxHealth = 150;
        int maxBarWidth = 100;
        Vec2 barPosition = new Vec2(worldToView(player.getPosition()).x-5, worldToView(player.getPosition()).y - 28);
        int barWidth = (int) ((double) health / maxHealth * maxBarWidth);

        int barX = (int) barPosition.x - 40;
        int barY = (int) barPosition.y - 28;

        int lostWidth = maxBarWidth - barWidth;

        // Draw the full life healthbar (green)
        g.setColor(Color.green);
        g.fillRect(barX, barY, barWidth, 10);

        // Draw the red bit(health lost)
        g.setColor(Color.red);
        g.fillRect(barX + barWidth, barY, lostWidth, 10);
    }

    /**
     * Game over.
     */
    public void gameOver(){
        over=true;
    }

    /**
     * Game restart.
     */
    public void gameRestart(){
        over=false;

    }

    /**
     * Set player.
     *
     * @param player the player
     */
    public void setPlayer(Player player){
        this.player=player;
    }
}