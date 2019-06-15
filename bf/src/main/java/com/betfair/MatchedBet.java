package com.betfair;

/**
 * @author Martin Anderson
 * @author weaves
 *
 * A carrier class for a Bet that has been matched.
 */
public class MatchedBet {
  protected final Bet bet;

  public MatchedBet(Bet b) throws IllegalArgumentException {
    if (!b.isMatched())
      throw new IllegalArgumentException("not matched");
    this.bet = b;
  }
}
