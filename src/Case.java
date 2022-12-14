import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Case extends JPanel implements MouseListener {
    private int DIM = 50;
    private String text;

    private boolean leftClick = false;
    private boolean rightClick = false;
    private GUI gui;
    private int flagPlaced = 0;
    private boolean openedCase = false;

    private ImageIcon bomb = new ImageIcon("bomb.png");
    private ImageIcon flag = new ImageIcon("flag.png");

    Case(int x, int y, GUI gui) {
        this.gui = gui;
        text = gui.getFieldFromGUI().getElementFromXY(x, y, true);
        setPreferredSize(new Dimension(DIM, DIM));
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {

        if (leftClick) {
            super.paintComponent(g);
            setBorder(BorderFactory.createLoweredBevelBorder());
            if (text.equals("x")) {
                g.drawImage(bomb.getImage(), 0, 0, getWidth(), getHeight(), this);
                gui.gameOver();
            } else {
                g.setColor(Color.lightGray);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setFont(new Font("TimesRoman", Font.BOLD, 14));
                switch (Integer.valueOf(text)) { // Set the Color of the number depending on
                    // // its value
                    case 0:
                        g.setColor(Color.GRAY);
                        break;
                    case 1:
                        g.setColor(Color.BLUE);
                        break;
                    case 2:
                        g.setColor(Color.GREEN);
                        break;
                    case 3:
                        g.setColor(Color.RED);
                        break;
                    case 4:
                        g.setColor(Color.ORANGE);
                        break;
                    case 5:
                        g.setColor(Color.MAGENTA);
                        break;
                    case 6:
                        g.setColor(Color.CYAN);
                        break;
                }
                if (text.equals("0")) { // Draw an empty case
                    g.drawString(" ", getWidth() / 2, getHeight() / 2);
                } else { // Draw the case with the specified number of mines around it
                    g.drawString(text, -3 + (getWidth() + 1) / 2, 3 + (getHeight() + 1) / 2);
                }
                if(!openedCase){
                    gui.incrementCasesOpened();
                    openedCase = true;
                }
                gui.checkIfWin(); // Check if the game is winned.

            }
        } else if (rightClick) {
            super.paintComponent(g);
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, getWidth(), getHeight());
            setBorder(BorderFactory.createRaisedBevelBorder());

            if (flagPlaced == 0) {
                g.drawImage(flag.getImage(), 0, 0, getWidth(), getHeight(), this);
                gui.downScore();
                flagPlaced = 1 - flagPlaced;
            } else {
                gui.upScore();
                flagPlaced = 1 - flagPlaced;
            }
            rightClick = false;
        } else if (!leftClick && !rightClick) {
            super.paintComponent(g);
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, getWidth(), getHeight());
            setBorder(BorderFactory.createRaisedBevelBorder());
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isLeftMouseButton(e)) { // left mouse button
            leftClick = true;
        } else if (isRightMouseButton(e)) {// right mouse button
            rightClick = true;
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // enter = true;
        // repaint();
        // // enter = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // exit = true;
        // repaint();
        // // exit = false;

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // nothing to do here

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // released = true;
        // released = false;
    }

}
