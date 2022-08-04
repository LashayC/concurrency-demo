package com.tlglearning.concurrency;

public class SingleThread implements Computation {

  @Override
  public double arithmeticMean(int[] data) {
    long sum = 0;
    for (int value : data) {
      sum += value;
    }
    return sum / (double) data.length;
  }

  @Override
  public double geometricMean(int[] data) {
    double logSum = 0;
    for (int value : data) {
      logSum += Math.log(value);//NOTE taking the logarithm base of each number and adding to the sum.
    }
    return Math.exp(logSum / data.length);
  }

}
