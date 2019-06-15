/**
 * @author weaves
 *
 * @brief Back Bet.
 *
 * The Back bet sub-class of Bet, uses a static factory method, currently set to return a test value.
 */
package com.betfair;

public class Back extends Bet {
  protected Back(Market market, Amount amount, Odds odds) {
    super(market, amount, odds);
  }

  public static Bet newInstance(Market market, Amount amount, Double odds) {
    return new Back(market, new Amount(10.0), new Odds(2.0));
  }
}
