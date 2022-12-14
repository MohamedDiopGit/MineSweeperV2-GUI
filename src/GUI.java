import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.List;
/**
 * {@code GUI} : Graphic User Interface class, extends {@code JPanel}.
 * The main component that runs the Graphic interface,
 * and manages the front and back-end processes to call entities and specific
 * functions
 * in order to display the information.
 * It allows to display the grid, the menubar on the main frame and the pop-ups
 * to give and collect data correctly.
 */
public class GUI extends JPanel {

    /**
     * Field to be process for the grid and display in the GUI.
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
     * scoreLabel of the current game session.
     */
    private JLabel scoreLabel = new JLabel();
    /**
     * scoreLabel of the current game session to be copy on the {@code JLabel}
     * instance.
     */
    private int score;
    private boolean propagated = false;
    private int openedCases = 0;
    /**
     * Time session (elapsed) information to display
     */
    private JLabel timeSession = new JLabel();

    /**
     * restart button's text
     */
    private ImageIcon restartImg = new ImageIcon("restart.png");
    // private ImageIcon restartPressedImg = new ImageIcon("restartPressed.png");
    private JButton restart = new JButton(
            new ImageIcon(restartImg.getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    // private JButton restart = new JButton(new ImageIcon("restart.png"));
    private JMenuItem saveGame = new JMenuItem("Save");
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
    private JPanel panelNorth = new JPanel(new FlowLayout());
    private JPanel panelNorthCenter = new JPanel(new FlowLayout());
    private List<Case> cases = new ArrayList<Case>();
    /**
     * Constructor for the GUI, which starts the game.
     * 
     * @param {@code Main} : the main component that contains a {@code Field}.
     * @see #startNewGame()
     */
    GUI(Main main) {
        this.main = main;
        this.field = main.getField();

        setLayout(new BorderLayout());
        panelNorth.setBackground(Color.lightGray);
        setBorder(BorderFactory.createRaisedBevelBorder());
        panelNorth.setBorder(BorderFactory.createLoweredBevelBorder());

        startNewGame();

    }

    public void propagationCase(int xOrigin, int yOrigin) {

        propagated = true;
    }

    /**
     * Global starter method which starts and initializes the game.
     * {@code Field.initField()} methods and catch the game level in the GUI.
     */
    public void startNewGame() {
        field.initField();
        saveGame.setText("Not saved");
        saveGame.setForeground(Color.RED);
        this.levelGame = field.getLevel();
        this.timeElapsed();
        this.displayMenu();
        this.restartButton();
        this.reInitField();
        this.initializationField(); // Comment to have "Case" version of box
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
        panelNorth.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenuItem menu = new JMenu("Mode");
        JMenuItem easyMode = new JMenuItem("EASY");
        JMenuItem mediumMode = new JMenuItem("MEDIUM");
        JMenuItem hardMode = new JMenuItem("HARD");
        JMenuItem customMode = new JMenuItem("CUSTOM");
        JMenu option = new JMenu("Options");

        option.add(saveGame);

        // Server options
        JMenuItem connectionToServer = new JMenuItem("Connection to server");
        JMenu infoServer = new JMenu("Server");

        infoServer.add(connectionToServer);

        levelGameModeInfo.setText(String.valueOf(levelGame));

        menu.add(easyMode);
        menu.add(mediumMode);
        menu.add(hardMode);
        menu.add(customMode);

        menuBar.add(option);
        menuBar.add(infoServer);
        menuBar.add(menu);
        menuBar.add(levelGameModeInfo);
        menuBar.setBorder(BorderFactory.createRaisedBevelBorder());

        timeSession.setForeground(Color.RED);
        scoreLabel.setForeground(Color.RED);

        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        timeSession.setFont(new Font("Monospaced", Font.BOLD, 30));

        JPanel panelNorthWest = new JPanel(new FlowLayout());
        JPanel panelNorthEast = new JPanel(new FlowLayout());
        panelNorthCenter = new JPanel(new FlowLayout());

        panelNorthWest.add(scoreLabel);
        panelNorthEast.add(timeSession);

        panelNorthWest.setBackground(Color.black);
        panelNorthEast.setBackground(Color.black);

        restart.setBackground(Color.lightGray);
        restart.setBorder(BorderFactory.createEmptyBorder());
        panelNorthCenter.add(restart);
        panelNorthCenter.setBackground(Color.lightGray);

        panelNorth.add(panelNorthEast, BorderLayout.EAST);
        panelNorth.add(panelNorthCenter, BorderLayout.CENTER);
        panelNorth.add(panelNorthWest, BorderLayout.WEST);

        // Add menu options
        saveGame.addActionListener(evt -> saveGameLevel());

        // Add connection to server
        connectionToServer.addActionListener(evt -> modeOnline());

        // Add different mode in the menu
        easyMode.addActionListener(evt -> selectorLevelGame(Levels.EASY));
        mediumMode.addActionListener(evt -> selectorLevelGame(Levels.MEDIUM));
        hardMode.addActionListener(evt -> selectorLevelGame(Levels.HARD));
        customMode.addActionListener(evt -> selectorLevelGame(Levels.CUSTOM));

        // Addd the menu bar to the main frame.
        main.setJMenuBar(menuBar);

    }

    public void modeOnline() {
        // Launch online mode.
    }

    public void selectorLevelGame(Levels level) {
        field = new Field(level);
        levelGameModeInfo.setText(String.valueOf(level));
        saveGame.setForeground(Color.RED);
        startNewGame();
    }

    /**
     * Initialization method for the field.
     */
    public void initializationField() { // Initialization of boxes with different values for a
                                        // certain area / allow to place flags on mines

        remove(panelCenter); // initialization of the panel
        panelCenter = new JPanel();
        add(panelCenter, BorderLayout.CENTER);
        panelCenter.setBorder(BorderFactory.createLoweredBevelBorder());
        int dimParam = this.field.getDim(); // Get the dimensions of the field
        panelCenter.setLayout(new GridLayout(dimParam, dimParam));

        cases.clear();
        // Loop on the entire field elements
        for (int x = 0; x < dimParam; x++) {
            for (int y = 0; y < dimParam; y++) { // loop on the matrix to display all objects

                Case caseToAdd = new Case(x, y, this);
                cases.add(caseToAdd);
                panelCenter.add(cases.get(cases.size()-1));

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

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });


    }

    /**
     * Generates a new field, and restarts the timer, and the scoreLabel of the
     * current
     * game.
     * It also calls the {@code displayStartEmptyField()} method to clear the
     * field/grid.
     * 
     * @see #displayStartEmptyField()
     */
    public void reInitField() {
        seconds = 0;
        score = field.getNumberOfMines();
        scoreLabel.setText(String.valueOf(score));
        timeSession.setText(String.valueOf(seconds));
        timer.start();

        this.main.getField().initField();
        this.initializationField();
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
        saveGame.setText("Saved");
        saveGame.setForeground(new Color(57, 163, 18));
    }

    public Field getFieldFromGUI() {
        return field;
    }

    public void upScore() {
        score++;
        scoreLabel.setText(String.valueOf(score));
    }

    public void downScore() {
        score--;
        scoreLabel.setText(String.valueOf(score));
    }

    public void gameOver() {
        JOptionPane.showMessageDialog(this, "Mine clicked on.",
                "Game over", JOptionPane.INFORMATION_MESSAGE);
        startNewGame();
    }

    public void checkIfWin() {
        int totalCases = field.getDim() * field.getDim();
        if ( ((totalCases - openedCases) == field.getNumberOfMines()) && score == 0) {
            JOptionPane.showMessageDialog(this, "You won.",
                    "Game win", JOptionPane.INFORMATION_MESSAGE);
            openedCases = 0;
            startNewGame();
        }
    }

    public void incrementCasesOpened() {
        openedCases++;
    }

}
