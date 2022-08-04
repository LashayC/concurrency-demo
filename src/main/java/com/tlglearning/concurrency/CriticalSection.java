package com.tlglearning.concurrency;

import java.util.LinkedList;
import java.util.List;

public class CriticalSection implements Computation {

  private static final int NUM_THREADS = 4;

  private double logSum;

  @Override
  public double arithmeticMean(int[] data) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public double geometricMean(int[] data) {

    int slice = data.length / NUM_THREADS; //NOTE gives us equal lengths of the array.

    List<Thread> workers = new LinkedList<>(); //NOTE linkedList because there won't be random accesses here link in an aarrayList
    for (int i = 0; i < NUM_THREADS; i++) {
      workers.add(spawn(data, i * slice, (i + 1)
          * slice)); //NOTE worker 1 starts from beginning to end of section. End index is same plus 1 because count starts from 0.
    }
    for (Thread worker : workers) {
      try {
        worker.join(); //NOTE this joins each thread together
      } catch (InterruptedException ignored) {
        // Ignore this exception
      }

    }
    return Math.exp(logSum / data.length);
  }

  private Thread spawn(int[] data, int startIndex,
      int endIndex) { //NOTE This will be the thread constructor. Passing the array indexes for a quarter of the work
    //NOTE Runnable is functional interface. The unit of work passed around on dif threads
    Runnable work = () -> {
      for (int i = startIndex; i < endIndex; i++) {
        update(data[i]); //NOTE extracted out to method to isolate logSum update.
      }
    };

    Thread worker = new Thread(work); //NOTE creating Thread around work Runnable created above.
    worker.start();
    return worker; //NOTE now returning a thread that has ben started.
  }

  private synchronized void update(int data) { //NOTE created so Threads now wait turns to update logSum
    logSum += Math.log(data);
  }


}
