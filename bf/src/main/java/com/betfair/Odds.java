/**
 * @author weaves
 *
 * @brief Base class for all odds.
 *
 * Simple base class. No implementations for fair or odds-chart
 * basis. It might be better to use a factory method here too.
 */

package com.betfair;

public class Odds implements Comparable<Odds> {
  protected Double value = null;

  public Odds(double value) {
    this.value = new Double(value);
  }

  public int compareTo(Odds other) {
    return value.compareTo(other.value);
  }
}
