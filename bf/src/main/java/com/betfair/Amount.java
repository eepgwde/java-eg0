/**
 * @author weaves
 *
 * @brief Base class for all staked amounts.
 *
 * Simple base class. No implementations for currency, taxes, commissions, etc.
 * Assume default currency is GBP.
 */

package com.betfair;

public class Amount implements Comparable<Amount> {
  protected Double value = null;

  public Amount(double value) {
    this.value = new Double(value);
  }

  public int compareTo(Amount other) {
    return value.compareTo(other.value);
  }
}
