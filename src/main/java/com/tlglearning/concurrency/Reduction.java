package com.tlglearning.concurrency;

import java.util.LinkedList;
import java.util.List;

public class Reduction implements Computation {
//NOTE Reduction class goal is to now have each thread to have its own sum then add to the running total at the very end. Now accessing logsum just 4 times. High Speeddd. (500 ms)
  private static final int NUM_THREADS = 4;

  private final Object lock = new Object(); //NOTE now can use this Object as a lock.
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

  private Thread spawn(int[] data, int startIndex, int endIndex) { //NOTE This will be the thread constructor. Passing the array indexes for a quarter of the work
    //NOTE Runnable is functional interface. The unit of work passed around on dif threads
    Runnable work = () -> {
      double logSubtotal = 0; //NOTE defining a local variable inside a lambda.  Bc lambda is the implementation of a method you do need ot assign a value here. Therefore it can update the value as it goes. Lambdas can't modify local variables in the method its defined, out there it must be effectively final
      for (int i = startIndex; i < endIndex; i++) {
        logSubtotal += Math.log(data[i]);
      }
      synchronized (lock){//NOTE now each thread can add the finished subtotal in one time.
        logSum += logSubtotal;
      }
    };

    Thread worker = new Thread(work); //NOTE creating Thread around work Runnable created above.
    worker.start();
    return worker; //NOTE now returning a thread that has ben started.
  }

//  private void update(int data) { //NOTE method created so Threads now wait turns to update logSum
//    double logData = Math.log(data); //NOTE this can still be completed in parallel for speed
//    synchronized (lock) { //NOTE you can just make the exact part that you need a Critical Section so that Threads access one at a time.
//      logSum += logData;
//    }
//  }

}
