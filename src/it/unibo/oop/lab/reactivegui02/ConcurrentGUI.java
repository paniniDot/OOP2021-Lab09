package it.unibo.oop.lab.reactivegui02;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Simple Reactive GUI realized by PANINI.
 */
public class ConcurrentGUI extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JPanel display;
    private final JButton up;
    private final JButton down;
    private final JButton stop;
    private final JTextField counter;

    /**
     * Builds a new GUI.
     */
    public ConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.up = new JButton("up");
        this.down = new JButton("down");
        this.stop = new JButton("stop");
        this.counter = new JTextField("0");
        this.counter.setEditable(false);
        this.display = new JPanel();
        this.display.add(counter);
        this.display.add(up);
        this.display.add(down);
        this.display.add(stop);
        this.getContentPane().add(this.display);
        this.setVisible(true);
    }
}
