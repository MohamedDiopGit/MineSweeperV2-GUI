import javax.swing.JFrame;

/**
 * {@code Main} application : Minesweeper program.
 */
public class Main extends JFrame {
    /**
     * Main GUI for the game : Minesweeper.
     */
    private final GUI gui;
    // private final ButtonGUI buttonGUI;
    /**
     * Field to start with in the game.
     */
    private Field field;

    Main() {
        setTitle("Minesweeper GUI");
        loadGameLevel(); // Load the game level
        this.field.initField(); // initialisation of the field

        gui = new GUI(this);
        setContentPane(gui); // Set the center Panel for the frame
        // buttonGUI = new ButtonGUI(this);
        // setContentPane(buttonGUI); // Set the center Panel for the frame

        pack();
        // setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Close correctly the frame
    }

    /**
     * Runs the minesweeper program.
     * 
     * @param args : optional arguments to pass to the program.
     */
    public static void main(String[] args) {
        new Main();
    }

    /**
     * Returns the current field that {@code Main} is running.
     * 
     * @return {@code Field}
     */
    public Field getField() { // Getter of the field
        return this.field;
    }

    /**
     * Loads the saved level's configuration from "LevelRegistred.dat"
     * 
     * @see LevelsFileReader
     */
    public void loadGameLevel() {

        try {
            LevelsFileReader fileReader = new LevelsFileReader();

            // Waiting for the reader thread to finish loading the level mmode
            fileReader.geThread().join();

            // Configure the field with the level mode.
            field = new Field(fileReader.getLevelFromFile());

        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Failed to load last save... EASY_Mode selected.");
        }
    }
}