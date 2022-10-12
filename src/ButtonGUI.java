import java.awt.*;
import java.awt.event.*;
import java.util.Random;
// import java.util.Scanner;  // For terminal entries mode, uncomment it if you want to use terminal entries. 

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

/**
 * {@code ButtonGUI} : Graphic User Interface class, extends {@code JPanel}.
 * The main component that runs the Graphic interface,
 * and manages the front and back-end processes to call entities and specific
 * functions
 * in order to display the information.
 * It allows to display the grid, the menubar on the main frame and the pop-ups
 * to give and collect data correctly.
 */
public class ButtonGUI extends JPanel {

    /**
     * Field to be process for the grid and display in the ButtonGUI.
     */
    private Field field;

    /**
     * imported Main from the "main.java".
     */
    private Main main;

    /**
     * Timer for the session which update every second the {@code timeSession}.
     */
    private Timer timer;

    /**
     * Seconds elapsed since the beginning.
     */
    private int seconds = 0;
    /**
     * score of the current game session.
     */
    private JLabel score = new JLabel();
    /**
     * Score of the current game session to be copy on the {@code JLabel} instance.
     */
    private int scoreTemp = 0;

    /**
     * Time session (elapsed) information to display
     */
    private JLabel timeSession = new JLabel();

    /**
     * restart button's text
     */
    private JButton restart = new JButton("🙂 Restart");

    /**
     * Pane in the center of the screen that displays the grid
     */
    private JPanel panelCenter = new JPanel();
    /**
     * Current game level of the session
     * 
     * @see Levels
     */
    private Levels levelGame;

    /**
     * Game mode's text
     */
    private JLabel levelGameModeInfo = new JLabel();
    JPanel panelNorth = new JPanel();

    /**
     * Constructor for the ButtonGUI, which starts the game.
     * 
     * @param {@code Main} : the main component that contains a {@code Field}.
     * @see #startNewGame()
     */
    ButtonGUI(Main main) {
        this.main = main;
        this.field = main.getField();
        startNewGame();
    }

    /**
     * Global starter method which starts and initializes the game
     * by launching {@code displayButtonGUI()}.
     * {@code Field.initField()} methods and catch the game level in the ButtonGUI.
     * 
     * @see #displayButtonGUI()
     */
    public void startNewGame() {
        field.initField();

        // Deprecated method of level initialization
        this.levelGame = field.getLevel();
        this.displayButtonGUI();
    }


    /**
     * Main ButtonGUI initialization's method for the main frame, it displays the menu,
     * the time elapsed
     * the current score, the restart button, and display the field at the beginning
     * (with hidden boxes)
     * on the frame.
     * 
     * @see #displayMenu()
     * @see #displayScore()
     * @see #timeElapsed()
     * @see #restartButton()
     * @see #reInitField()
     * @see #displayStartEmptyField()
     */
    public void displayButtonGUI() {
        setLayout(new BorderLayout());
        this.timeElapsed();
        this.displayMenu();
        this.restartButton();
        this.reInitField();
        this.displayStartEmptyField();  // Comment to have "Case" version of box
        // this.initializationField(0, 0); // Uncomment to have "Case" version of box
    }

    /**
     * Displays the menu bar for choosing between multiple difficulties
     * and display their informations.
     * It adds the {@code ActionListener} on the difficulty options,
     * in order to {@code startNewGame()} with parameters depending on level game
     * mode.
     * 
     * @see #startNewGame()
     */
    public void displayMenu() {
        remove(panelNorth);
        add(panelNorth, BorderLayout.NORTH);
        panelNorth.removeAll();
        panelNorth.setLayout(new FlowLayout());



        JMenuBar menuBar = new JMenuBar();
        JMenuItem menu = new JMenu("Mode");
        JMenuItem easyMode = new JMenuItem("EASY");
        JMenuItem mediumMode = new JMenuItem("MEDIUM");
        JMenuItem hardMode = new JMenuItem("HARD");
        JMenuItem customMode = new JMenuItem("CUSTOM");
        JButton quit = new JButton("Quit");
        JButton saveGame = new JButton("Save");


        // Server options
        JMenuItem connectedClients = new JMenuItem("Connected clients");
        JMenuItem initConnection = new JMenuItem("Connection to server");
        JMenu infoServer = new JMenu("Server");
  
        infoServer.add(connectedClients);
        infoServer.add(initConnection);

        levelGameModeInfo.setText(String.valueOf(levelGame));

        quit.setBackground(Color.RED);
        quit.setForeground(Color.WHITE);
        saveGame.setBackground(Color.ORANGE);
        saveGame.setForeground(Color.WHITE);

        menu.add(easyMode);
        menu.add(mediumMode);
        menu.add(hardMode);
        menu.add(customMode);
        
        menuBar.add(infoServer);
        menuBar.add(menu);
        menuBar.add(levelGameModeInfo);


        panelNorth.add(quit);
        panelNorth.add(saveGame);
        panelNorth.add(new JLabel("Score: "));
        panelNorth.add(score);
        panelNorth.add(new JLabel(" | Time : "));
        panelNorth.add(timeSession);

        

        // Add menu options
        saveGame.addActionListener(evt -> saveGameLevel());
        quit.addActionListener(evt -> System.exit(0));

        // Add different mode in the menu
        easyMode.addActionListener(evt -> selectorLevelGame(Levels.EASY));
        mediumMode.addActionListener(evt -> selectorLevelGame(Levels.MEDIUM));
        hardMode.addActionListener(evt -> selectorLevelGame(Levels.HARD));
        customMode.addActionListener(evt -> selectorLevelGame(Levels.CUSTOM));



        // Addd the menu bar to the main frame.
        main.setJMenuBar(menuBar);




    }

    public void selectorLevelGame(Levels level) {
        field = new Field(level);
        levelGameModeInfo.setText(String.valueOf(level));
        startNewGame();
    }

    /**
     * Restarts and displays the grid field with hidden boxes
     * and add the start {@code ActionListener} on each of them
     * to know where to start the game,
     * in order to trigger the {@code initializationField()} method on a specific
     * box.
     * 
     * @see #initializationField(int, int)
     */
    public void displayStartEmptyField() {
        remove(panelCenter); // initialization of the panel
        panelCenter = new JPanel();
        add(panelCenter, BorderLayout.CENTER);

        int dimParam = this.field.getDim(); // Get the dimensions of the field
        panelCenter.setLayout(new GridLayout(dimParam, dimParam));

        for (int x = 0; x < dimParam; x++) {
            for (int y = 0; y < dimParam; y++) { // For loop on the matrix to display all objects
                JButton box = new JButton(); // Clickable button on each minefield's boxes
                box.setBackground(Color.WHITE);
                box.setPreferredSize(new Dimension(70, 60));
                panelCenter.add(box);

                final int xOnStart = x;
                final int yOnStart = y;
                box.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                        initializationField(xOnStart, yOnStart);
                        setVisible(true); // Update of the frame fixing frozen frame bug
                    }
                });
            }
        }
    }

    /**
     * This function takes the first "clicked" box {@code JButton}
     * to initialize the grid {@code GridLayout} from {@code JPanel} with the field
     * and uncover some boxes around it. It also adds {@code MouseAdapter} or
     * {@code ActionListener} events depending
     * on the value of the box (mine, hidden box, non-mine) and the user behaviour
     * (right or left click).
     * 
     * @param xOnStart : the x position of the first "clicked" on the hidden boxes'
     *                 field
     * @param yOnStart : the y position of the same box
     */
    public void initializationField(int xOnStart, int yOnStart) { // Initialization of boxes with different values for a
                                                                  // certain area / allow to place flags on mines

        remove(panelCenter); // initialization of the panel
        panelCenter = new JPanel();
        add(panelCenter, BorderLayout.CENTER);

        int dimParam = this.field.getDim(); // Get the dimensions of the field
        panelCenter.setLayout(new GridLayout(dimParam, dimParam));

        // Loop on the entire field elements
        for (int x = 0; x < dimParam; x++) {
            for (int y = 0; y < dimParam; y++) { // For loop on the matrix to display all objects
                final int xBox = x;
                final int yBox = y;


                /**
                 * Case mode for boxes. Uncomment panelCenter.add(boxCase) to get it.
                 */
                // Add a box on the grid
                // minefield's boxes
                // Case boxCase = new Case(xOnStart, yOnStart, xBox, yBox,this.field.getElementFromXY(x, y, true), dimParam );
                // panelCenter.add(boxCase);

                /**
                 * Box case with JButton mode. Uncomment panelCenter.add(box) to get it.
                 */
                JButton box = new JButton(this.field.getElementFromXY(x, y, false)); // Clickeable button on each
                box.setBackground(Color.WHITE);
                box.setPreferredSize(new Dimension(70, 60));
                panelCenter.add(box);
                
                if (box.getText() == "x") { // If there is a mine
                    box.setText(""); // Hide it with a white background and not text

                    box.addMouseListener(new MouseAdapter() { // OnClick event : Place a flag or trigger the "Game over
                                                              // event"
                        @Override
                        public void mouseClicked(MouseEvent event) {

                            if (isRightMouseButton(event)) // Set the box with a red flag
                            {
                                if (field.getElementFromXY(xBox, yBox, false) == "x" && box.getText() != "🚩") { // Check
                                                                                                                 // if
                                                                                                                 // there
                                                                                                                 // is a
                                                                                                                 // mine
                                                                                                                 // and
                                                                                                                 // not
                                                                                                                 // flagged
                                                                                                                 // before
                                    scoreTemp++;
                                    score.setText(String.valueOf(scoreTemp));
                                    if (scoreTemp == field.getNumberOfMines()) {
                                        JOptionPane.showMessageDialog(main, "You won ! : what a player 💯",
                                                "Game win", JOptionPane.WARNING_MESSAGE);
                                        reInitField();
                                    }
                                }
                                box.setText("🚩");
                            } else if (isLeftMouseButton(event) && box.getText() != "🚩") { // Check if Left click and
                                                                                            // not a mine discovered :
                                                                                            // GAME OVER
                                // Code To popup an Game Over message :
                                JOptionPane.showMessageDialog(main, "You clicked on a mine : Game Over LOOSER 🤣",
                                        "GAME OVER", JOptionPane.WARNING_MESSAGE);
                                reInitField();
                            }
                        }
                    });
                }

                else if (box.getText() == "0") { // Operations on non-mined boxes
                    double deltaX = Math.abs(xOnStart - xBox);
                    double deltaY = Math.abs(yOnStart - yBox);
                    double result = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                    Random alea = new Random();

                    if (result <= alea.nextDouble((int) dimParam / 1.5)) { // Undisover boxes that are in a calculated
                                                                           // area with euclidian distance as radius

                        box.setBackground(Color.GRAY); // Color the unblocked boxes with gray color
                        box.setText(field.getElementFromXY(xBox, yBox, true)); // Update the text in the box with the
                                                                               // computed value

                        switch (Integer.valueOf(box.getText())) { // Set the Color of the number depending on its value
                            case 0:
                                box.setBackground(Color.GRAY);
                                break;
                            case 1:
                                box.setForeground(Color.BLUE);
                                break;
                            case 2:
                                box.setForeground(Color.GREEN);
                                break;
                            case 3:
                                box.setForeground(Color.RED);
                                break;
                            case 4:
                                box.setForeground(Color.ORANGE);
                                break;
                            case 5:
                                box.setForeground(Color.MAGENTA);
                                break;
                            case 6:
                                box.setForeground(Color.CYAN);
                                break;

                        }
                    } else { // Boxes outside the calculated area
                        box.setText(""); // The boxes are still hidden
                        box.addActionListener(new ActionListener() { // OnClick event : Discover them and their computed
                                                                     // value
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                box.setText(field.getElementFromXY(xBox, yBox, true)); // Change the value with the
                                                                                       // computed one
                                box.setBackground(Color.GRAY);
                                switch (Integer.valueOf(box.getText())) { // Set the Color of the number depending on
                                                                          // its value
                                    case 0:
                                        box.setBackground(Color.GRAY);
                                        break;
                                    case 1:
                                        box.setForeground(Color.BLUE);
                                        break;
                                    case 2:
                                        box.setForeground(Color.GREEN);
                                        break;
                                    case 3:
                                        box.setForeground(Color.RED);
                                        break;
                                    case 4:
                                        box.setForeground(Color.ORANGE);
                                        break;
                                    case 5:
                                        box.setForeground(Color.MAGENTA);
                                        break;
                                    case 6:
                                        box.setForeground(Color.CYAN);
                                        break;

                                }

                            }
                        });
                        box.addMouseListener(new MouseAdapter() { // OnClick event : Place a flag or trigger the "Game
                                                                  // over event"
                            @Override
                            public void mouseClicked(MouseEvent event) {

                                if (isRightMouseButton(event)) // Set the box with a red flag
                                {
                                    if (field.getElementFromXY(xBox, yBox, false) == "x" && box.getText() != "🚩") { // Chech
                                                                                                                     // if
                                                                                                                     // there
                                                                                                                     // is
                                                                                                                     // a
                                                                                                                     // mine
                                                                                                                     // and
                                                                                                                     // not
                                                                                                                     // flagged
                                                                                                                     // before
                                        scoreTemp++;
                                        score.setText(String.valueOf(scoreTemp));
                                    }
                                    box.setText("🚩");
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * Activates the restart button by adding an {@code ActionListener} event
     * on the restart button. It will call the {@code reInitField()} method.
     * 
     * @see #reInitField()
     */
    public void restartButton() { // Restart a game
        restart.setBackground(Color.WHITE);
        add(restart, BorderLayout.SOUTH);
        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reInitField();
            }
        });

    }

    /**
     * Generates a new field, and restarts the timer, and the score of the current
     * game.
     * It also calls the {@code displayStartEmptyField()} method to clear the
     * field/grid.
     * 
     * @see #displayStartEmptyField()
     */
    public void reInitField() {
        seconds = 0;
        scoreTemp = 0;
        score.setText(String.valueOf(scoreTemp));
        timeSession.setText(String.valueOf(seconds));
        timer.start();

        this.main.getField().initField();
        this.displayStartEmptyField();
    }

    /**
     * Processes the time elapsed since the beginning of the start of a game
     * session.
     * It also checks if the time session has outdated the time limit, if so,
     * it will reinitialize the game after showing a popup (Game over) to the user.
     * 
     * @see #reInitField()
     */
    public void timeElapsed() { //
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                timeSession.setText(String.valueOf(seconds));
            }
        });
    }

    /**
     * Saves the game level in a local file "LevelRegistred.dat"
     */
    public void saveGameLevel() {
        new LevelsFileWriter(this.levelGame);
    }

}