package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * Multithreaded sum function for matrix.
 *
 */
public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nThreads;

    /**
     * Constructor for MultiThreadedSumMatrix.
     * @param nThreads
     *              no. of Threads performing the sum.
     */
    public MultiThreadedSumMatrix(final int nThreads) {
        this.nThreads = nThreads;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double sum(final double[][] matrix) {
        final int offset = matrix.length % this.nThreads + matrix.length / this.nThreads;

        final List<Worker> workers = new ArrayList<>(this.nThreads);
        for (int start = 0; start < matrix.length; start += offset) {
            workers.add(new Worker(matrix, start, offset));
        }

        for (final Worker worker: workers) {
            worker.start();
        }

        double sum = 0.0;
        for (final Worker worker: workers) {
            try {
                worker.join();
                sum += worker.getSum();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return sum;
    }

    /**
     * Thread class used to sum elements inside the matrix.
     */
    private class Worker extends Thread {

        private final double[][] matrix;
        private final int startPos;
        private final int length;
        private double sum;

        /**
         * @param matrix
         *          the list in which workers have to perform a sum.
         * @param startPos
         *          the start index for this worker.
         * @param length
         *          no. of cells to be summed from this worker
         */
        Worker(final double[][] matrix, final int startPos, final int length) {
            super();
            this.matrix = Arrays.copyOf(matrix, matrix.length);
            this.startPos = startPos;
            this.length = length;
            this.sum = 0.0;
        }

        /**
         * Calculate the sum within the boundaries of this thread.
         */
        @Override
        public void run() {
            System.out.println("Working from position " + this.startPos + " to position " + (this.startPos + this.length - 1));
            for (int i = this.startPos; i < matrix.length && i < this.startPos + this.length; i++) {
                for (final var num: matrix[i]) {
                    this.sum += num;
                }
            }
        }

        /**
         * @return the sum of elements in the array calculated from this thread.
         */
        public double getSum() {
            return this.sum;
        }
    }

}
