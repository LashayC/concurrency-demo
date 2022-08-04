package com.tlglearning.concurrency;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ForkJoin implements Computation {
//NOTE
private static final int FORK_THRESHOLD = 10_000_000; //NOTE this is the threshold a thread will take before the work is split into a new one.

  private final Object lock = new Object(); //NOTE now can use this Object as a lock.
  private double logSum;



  @Override
  public double arithmeticMean(int[] data) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public double geometricMean(int[] data) {
    Worker worker = new Worker(data, 0, data.length);
    ForkJoinPool pool = new ForkJoinPool();
    pool.invoke(worker);
    return Math.exp(logSum / data.length);
  }



  private class Worker extends RecursiveAction {

    private final int[] data;
    private final int startIndex;
    private final int endIndex;

    private Worker(int[] data, int startIndex, int endIndex) {
      this.data = data;
      this.startIndex = startIndex;
      this.endIndex = endIndex;
    }

//    @Override
//    protected void compute() {//NOTE Will fork when thread threshold is hit.
//      if(endIndex - startIndex <= FORK_THRESHOLD){ //NOTE well do the work ourselves, otherwise create new workers.
//        double logSubtotal = 0;
//        for (int i = startIndex; i < endIndex; i++) {
//          logSubtotal += Math.log(data[i]);
//        }
//        synchronized (lock){//NOTE Synchronized block. Now each thread can add the finished subtotal in one time.
//          logSum += logSubtotal;
//        }
//
//      }else{
//        int midpoint = (startIndex + startIndex) / 2; //NOTE have 2 workers from start to mid, mid to end.
//        invokeAll(new Worker(data, startIndex, midpoint), new Worker(data, midpoint, endIndex));
//
//      }
//
//    }


    @Override
    protected void compute() {
      if (endIndex - startIndex <= FORK_THRESHOLD) {
        double logSubtotal = 0;
        for (int i = startIndex; i < endIndex; i++) {
          logSubtotal += Math.log(data[i]);
        }
        synchronized (lock) {
          logSum += logSubtotal;
        }
      } else {
        int midpoint = (startIndex + endIndex) / 2;
        invokeAll(new Worker(data, startIndex, midpoint), new Worker(data, midpoint, endIndex));
      }
    }

  }

}
