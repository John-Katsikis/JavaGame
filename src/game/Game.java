package game;
import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.io.*;



public class Game {

    mapGenerator currentLevel;
    GameView view;
    Controller control;
    private Timer timer;
    private int levelcounter=1;
    StepListener listen;
    MouseControls mouseControls;
    private ArrayList<mapGenerator> levels;
    private int currentLevelPos;
    private SoundClip backgroundMusic;
    public int windowWidth, windowHeight;
    private boolean pastSelected=false;
    private boolean presentSelected=false;
    private boolean futureSelected=false;
    public int choiceX;
    public int choiceY;
    public int levelPoints;
    private JFrame startFrame;
    public Vec2 screenBoundaries;

    /**
     * Random integer pick.
     *
     * @param min the min
     * @param max the max
     * @return the integer
     */
    public static int randomPick(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }


    /**
     * Creates the game menu JFrame
     */
    public Game() {

        // Create a new JFrame for the start panel
        startFrame = new JFrame("Start Yetijax's time-travelling adventure");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(500, 150);
        startFrame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton startButton = new JButton("START GAME");
        panel.add(startButton, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new FlowLayout());
        JButton settingsButton = new JButton("Settings");
        centerPanel.add(settingsButton);
        panel.add(centerPanel, BorderLayout.CENTER);
        JButton exitButton = new JButton("Exit");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(exitButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        startFrame.add(panel);


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGameChoicePanel(); //This panel leads to the level selection, from there the game truly begins
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSettingsPanel(); //Opens settings to delete gamedata

            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); //Closes game
            }
        });

        startFrame.setLocationRelativeTo(null);
        startFrame.setVisible(true);
        Dimension startSize = startFrame.getSize();
        choiceX = startFrame.getX() + startSize.width+20;
        choiceY = startFrame.getY();
    }

    /*Creates settings menu*/
    private void openSettingsPanel(){
        JFrame settingsFrame = new JFrame("Settings");
        settingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settingsFrame.setSize(500, 100);
        settingsFrame.setResizable(true);
        JPanel settingsPanel = new JPanel();
        JButton eraseData = new JButton("Erase high score");
        JButton exitButton = new JButton("Return");
        settingsPanel.add(eraseData);
        settingsPanel.add(exitButton);
        settingsPanel.setVisible(true);
        settingsFrame.setLocation(choiceX,choiceY);
        settingsFrame.add(settingsPanel);
        settingsFrame.setVisible(true);
        eraseData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteHighScore();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsFrame.dispose();
            }
        });
    }
  /* Game level choice panel, from here game truly begins
   *
   */
    private void openGameChoicePanel() {
        JFrame gameChoice = new JFrame("Timeline choice");
        gameChoice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameChoice.setSize(500, 100);
        JPanel gameChoicePanel = new JPanel();
        JButton past = new JButton("Past");
        JButton present = new JButton("Present");
        JButton future = new JButton("Future");
        JButton goBack = new JButton("Return");
        gameChoicePanel.add(past);
        gameChoicePanel.add(present);
        gameChoicePanel.add(future);
        gameChoicePanel.add(goBack);
        gameChoice.add(gameChoicePanel);
        gameChoice.setLocation(choiceX,choiceY);
        gameChoice.setVisible(true);
        past.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pastSelected = true;
                presentSelected = false;
                futureSelected = false;
                gameChoice.dispose();
                startGame();
            }
        });

        present.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pastSelected = false;
                presentSelected = true;
                futureSelected = false;
                gameChoice.dispose();
                startGame();
            }
        });

        future.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pastSelected = false;
                presentSelected = false;
                futureSelected = true;
                gameChoice.dispose();
                startGame();
            }
        });
        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameChoice.dispose();
            }
        });
    }

/*Game starts here, background music is chosen based on which level the player selected
 *
 */
    private void startGame() {
        levels = new ArrayList<mapGenerator>();
        timer = new Timer();

        if(pastSelected) {
            currentLevel = new LevelPast(this, view);
            try{
                backgroundMusic = new SoundClip("data/pastMusic.mp3");
                backgroundMusic.loop();
            }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println(e);
            }
        }else if(presentSelected){
            currentLevel = new LevelPresent(this, view);
            try{
                backgroundMusic = new SoundClip("data/presentMusic.mp3");
                backgroundMusic.loop();
            }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println(e);
            }
        }else if(futureSelected){
            currentLevel = new LevelFuture(this, view);
            try{
                backgroundMusic = new SoundClip("data/neon-gaming.mp3");
                backgroundMusic.loop();
            }catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.out.println(e);
            }
        }

        levels.add(currentLevel);
        currentLevelPos=0;
        currentLevel.getSimulationSettings().setTargetFrameRate(60);

        view = new GameView(currentLevel, 1280,720, currentLevel.getPlayer(),this);

        control = new Controller(currentLevel.getPlayer(),currentLevel,this);
        view.addKeyListener(control);

        mouseControls = new MouseControls(currentLevel.getPlayer(),view);
        view.addMouseListener(mouseControls);

        //optional: draw a 1-metre grid over the view
        //view.setGridResolution(2);

        //4. create a Java window (frame) and add the game
        //   view to it
        final JFrame frame = new JFrame("Yetijax's inebriated space adventure");
        frame.add(view);
        view.addMouseMotionListener(new MouseControls(currentLevel.getPlayer(),view));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        ImageIcon logo = new ImageIcon("data/YetijaxLogo.jpg");
        frame.setIconImage(logo.getImage());
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screen.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((screen.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        //optional: uncomment this to make a debugging view
        //JFrame debugView = new DebugViewer(world, 1280, 720);
        currentLevel.start();
        view.requestFocus();


        listen = new StepListener() {
            @Override
            public void preStep(StepEvent stepEvent) {
            }

            @Override
            public void postStep(StepEvent stepEvent) {
                if (Enemy.lifecounter == 0) {
                    backgroundMusic.stop();
                    currentLevel.stop();
                    view.gameOver();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            goToNextLevel();
                            view.gameRestart();
                            levelCountIncrease();
                        }
                    }, 1000);
                }
            }
        };
        currentLevel.addStepListener(listen);
    }

    /**
     * Level count increase.
     */
    public void levelCountIncrease(){
        levelcounter++;
    }

    /**
     * Get levelcounter int.
     *
     * @return the int
     */
    public int getLevelcounter(){
        return levelcounter;
    }


    /**
     * Go to next level method, creates new currentLevel object according to what the player chose.
     */
    public void goToNextLevel() {
        // music1.stop();
        currentLevel.stop();
        currentLevel.removeStepListener(listen);
        levelPoints++;

        if(pastSelected) {
            currentLevel = new LevelPast(this, view);
        }
        if(presentSelected){
            currentLevel = new LevelPresent(this, view);
        }
        if(futureSelected) {
            currentLevel = new LevelFuture(this, view);
        }
        // Update the GameView with the new level and player
        view.setWorld(currentLevel);
        view.setPlayer(currentLevel.getPlayer());
        // Update the controller and mouse controls with the new player
        control.updatePlayer(currentLevel.getPlayer());
        mouseControls.updatePlayer(currentLevel.getPlayer());
        // Start the new level and set framerate to 60
        currentLevel.start();
        currentLevel.addStepListener(listen);
        currentLevel.getPlayer().enableFire(true);
        backgroundMusic.loop();
        currentLevel.getSimulationSettings().setTargetFrameRate(60);


    }

    /**
     * Update high score.
     *
     * @param highScore the high score
     */
    public static void updateHighScore(int highScore) {
        String filePath = "data/high_score.txt";

            try {
                File file = new File(filePath);
                boolean fileExists = file.exists();
                if (!fileExists) {
                    file.createNewFile();
                    System.out.println("New file created.");
                }
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                reader.close();
                int currentHighScore = 0;
                if (fileExists) {
                    // Extract the high score from the txt file
                    currentHighScore = Integer.parseInt(line.split(": ")[1]);
                }
                // Compare with existing high score
                if (highScore > currentHighScore) {
                    FileWriter writer = new FileWriter(file);
                    writer.write("High score: " + highScore);
                    writer.close();
                    System.out.println("New high score updated: " + highScore);
                } else {
                    System.out.println("Current high score is higher: " + currentHighScore);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * Delete high score.
     */
    public static void deleteHighScore(){
        String filePath = "data/high_score.txt";
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("High score file deleted successfully.");
                } else {
                    System.out.println("Failed to delete the high score file.");
                }
            } else {
                System.out.println("High score file does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get level counter int.
     *
     * @return the int
     */
    public int getLevelCounter(){
        return levelcounter;
    }

    /**
     * Get current level map generator.
     *
     * @return the currentLevel, useful to check instance or handle values
     */
    public mapGenerator getCurrentLevel(){
        return currentLevel;
    }

    /**
     * Is game paused boolean.
     *
     * @return the boolean
     */
    public boolean isGamePaused(){
        return control.getGameState();
    }

    /**
     * Run the game.  @param args the input arguments
     */
    public static void main(String[] args) {
        new Game();
    }
}