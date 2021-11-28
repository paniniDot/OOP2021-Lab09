package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    private final JButton up;
    private final JButton down;
    private final JButton stop;
    private final JLabel counter;

    /**
     * Builds a new GUI.
     */
    public ConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel display = new JPanel();
        final Agent ag = new Agent();
        this.up = new JButton("up");
        this.down = new JButton("down");
        this.stop = new JButton("stop");
        this.counter = new JLabel("0");
        this.counter.setSize(this.up.getPreferredSize());
        display.add(counter);
        display.add(up);
        display.add(down);
        display.add(stop);
        this.getContentPane().add(display);
        this.setVisible(true);

        this.up.addActionListener(e -> {
            if (!ag.isOn()) {
                ag.start();
            }
            ag.setUp();
            this.up.setEnabled(false);
            this.down.setEnabled(true);
            this.stop.setEnabled(true);
        });

        this.down.addActionListener(e -> {
            if (!ag.isOn()) {
                ag.start();
            }
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
    }

    /**
     * 
     * Active agent made to avoid EDT to compute long responses to actions happened.
     *
     */
    private class Agent extends Thread {

        private volatile boolean stop;
        private volatile boolean isIncrement;
        private volatile boolean isOn;
        private final Counter counter;

        /**
         * Constructor.
         */
        Agent() {
            this.stop = false;
            this.isIncrement = true;
            this.isOn = false;
            this.counter = new Counter();
        }
        /**
         * Core method.
         */
        @Override
        public void run() {
            this.isOn = true;
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
        public void setUp() {
            this.isIncrement = true;
        }
        /**
         *  Sets the counter to decrement. 
         */
        public void setDown() {
            this.isIncrement = false;
        }
        /**
         * It stops the counter.
         */
        public void stopCounting() {
            this.stop = true;
        }
        /**
         * It allows to understand if the thread already started.
         * 
         *  @return true if the thread is already active, false otherwise.
         */
        public boolean isOn() {
            return this.isOn;
        }
    }
}
