package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
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
        final Agent ag = new Agent();
        this.up = new JButton("up");
        this.down = new JButton("down");
        this.stop = new JButton("stop");

        this.up.addActionListener(e -> {
            ag.setUp();
            this.up.setEnabled(false);
            this.down.setEnabled(true);
            this.stop.setEnabled(true);
        });

        this.down.addActionListener(e -> {
            ag.setDown();
            this.up.setEnabled(true);
            this.down.setEnabled(false);
            this.stop.setEnabled(true);
        });

        this.stop.addActionListener(e -> {
            ag.stopCounting();
            this.up.setEnabled(false);
            this.down.setEnabled(false);
            this.stop.setEnabled(false);
        });
        ag.start();
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

    /**
     * 
     * Active agent made to avoid EDT to compute long responses to actions happened.
     *
     */
    private class Agent extends Thread {

        private volatile boolean stop;
        private volatile boolean isIncrement;
        private volatile Counter counter;

        /**
         * Constructor.
         */
        Agent() {
            this.stop = false;
            this.isIncrement = true;
            this.counter = new Counter();
        }
        /**
         * Core method.
         */
        @Override
        public synchronized void run() {
            while (!this.stop) {
                try {
                    if (this.isIncrement) {
                        this.counter.increment();
                    } else {
                        this.counter.decrement();
                    }
                    SwingUtilities.invokeLater(() -> ConcurrentGUI.this.counter.setText(Integer.toString(this.counter.getValue())));
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        /**
         *  Sets the counter to increment. 
         */
        public synchronized void setUp() {
            this.isIncrement = true;
        }
        /**
         *  Sets the counter to decrement. 
         */
        public synchronized void setDown() {
            this.isIncrement = false;
        }
        /**
         * It stops the counter.
         */
        public synchronized void stopCounting() {
            this.stop = true;
            this.interrupt();
        }
    }
}
