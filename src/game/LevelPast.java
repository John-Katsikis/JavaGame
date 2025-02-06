package game;
import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class LevelPast extends mapGenerator {
    private Player player1;
    private Enemy enemy1;
    private Enemy enemy2;
    private Enemy enemy3;
    private HealthPickup medkit1;
    private Game game;
    private boolean enableFire = true;// Initialize enableFire to true by default
    private Gun pistol;
    /**
     * The Levelcounter.
     */
    int levelcounter = 0;
    private Vec2[] platformCoordinates;
    private Vec2[] oppositePlatformCoordinates;
    private BoxShape[] platformShapes;
    private StaticBody[] platforms;
    private StaticBody[] oppositePlatforms;
    private static final float minimumDistance = 15.0f; // Adjust this value as needed
    private SoundClip pastMusic;

    /**
     * Instantiates a new Level past.
     *
     * @param game the game
     * @param view the view
     */
    public LevelPast(Game game, GameView view) {
        super(game, view);
        this.game = game; // Assign the game object
        levelcounter++;

        final int maxPlatforms = 5;
        final int maxAttemps = 100; // Maximum number of attempts to generate valid platforms

        platformCoordinates = new Vec2[maxPlatforms];
        oppositePlatformCoordinates = new Vec2[maxPlatforms];
        platforms = new StaticBody[maxPlatforms];
        oppositePlatforms = new StaticBody[maxPlatforms];
        platformShapes = new BoxShape[maxPlatforms];


        for (int attempt = 0; attempt < maxAttemps; attempt++) {
            boolean validConfiguration = true;

            for (int i = 0; i < maxPlatforms; i++) {
                platformCoordinates[i] = new Vec2(randomX(2, 15), randomY(-15, 15));
                oppositePlatformCoordinates[i] = new Vec2(-platformCoordinates[i].x, platformCoordinates[i].y);
            }

            // Check if any platforms are too close to each other
            for (int i = 0; i < maxPlatforms; i++) {
                for (int j = i + 1; j < maxPlatforms; j++) {
                    if (platformCoordinates[i].sub(platformCoordinates[j]).length() < minimumDistance) {
                        validConfiguration = false;
                        break;
                    }
                }
                if (!validConfiguration) {
                    break;
                }
            }
            // If a valid configuration is found, exit the loop
            if (validConfiguration) {
                break;
            }
        }

        // Create static bodies for the platforms and populate arrays with them
        for (int j = 0; j < maxPlatforms; j++) {
            platformShapes[j] = new BoxShape(10, 0.5f);
            platforms[j] = new StaticBody(this, platformShapes[j]);
            platforms[j].setPosition(platformCoordinates[j]);
            platforms[j].addImage(new BodyImage("data/PlatformAssets/WoodenPastPlatform.png", 4));
            oppositePlatforms[j] = new StaticBody(this, platformShapes[j]);
            oppositePlatforms[j].setPosition(oppositePlatformCoordinates[j]);
            oppositePlatforms[j].addImage(new BodyImage("data/PlatformAssets/WoodenPastPlatform.png", 4));
        }

        player1 = new Player(this, game, view,"Past");
        player1.setPosition(new Vec2(xCoords[0],yCoords[0]+5));

        enemy1 = new Enemy(this,500,player1,game,"Past");
        enemy1.setPosition(new Vec2(randomX(-15,15),randomY(-15,15)));

        enemy2 = new Enemy(this,2000,player1,game, "Past");
        enemy2.setPosition(new Vec2(randomX(-15,15),randomY(-15,15)));

        enemy3 = new Enemy(this,1000,player1,game, "Past");
        enemy3.setPosition(new Vec2(randomX(-15,15),randomY(-15,15)));

        pistol = new Gun(this, "Past");
        pistol.setPosition(new Vec2(xCoords[1], yCoords[0] + 2));

        collectablePickup powerUp = new collectablePickup(player1);
        player1.addCollisionListener(powerUp);

        medkit1 = new HealthPickup(this);
        medkit1.setPosition(new Vec2(randomX(-10,10),randomY(-10,10)));
    }

    public Player getPlayer() {
        return player1;
    }
}