/**
 * @author Martin Anderson
 * @author weaves
 *
 * @brief Base class for all bets.
 *
 * This class is abstract but has some book-keeping included.
 * This Bet keeps a record of the matched Bet within it. For consistency, and set Arithmetic reasons.
 */
package com.betfair;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Bet implements Comparable<Bet> {
  static private AtomicInteger priority0 = new AtomicInteger(0);
  protected final int priority = priority0.incrementAndGet();

  protected Bet(Market market) {
    this.market = market;
  }

  protected Market market = null;
  protected Amount amount = null;
  protected Odds odds = null;

  protected Bet(Market market, Amount amount, Odds odds) {
    this.market = market;
    this.amount = amount;
    this.odds = odds;
  }

  protected Bet match = null;

  public Bet getMatch() {
    return match;
  }

  public boolean isMatched() {
    return match != null;
  }

  /**
   * Allow a bet to be matched with another bet.
   */
   
  public void setMatch(Bet bet) throws IllegalStateException {
    if (match != null)
      throw new IllegalStateException("already matched");
    this.match = bet;
  }

  public Amount getAmount() {
    return amount;
  }

  public Odds getOdds() {
    return odds;
  }

  protected String id() {
    return String.format("%s:%s:%d:%f:%f", this.getClass().getName(), this.market.getId(),
			 this.priority, this.amount.value, this.odds.value);
  }
  

  public String toString() {
    return String.format("%s: match: %s", id(), (isMatched() ? match.id() : "none"));
  }

  /**
   * compare is based on market id and priority.
   *
   * Use the same convention as Unix - lower the priority number, more important.
   */
  public int compareTo(Bet other) {
    // The same object
    if (this.priority == other.priority) return 0;

    // Otherwise by market.
    int r = this.market.getId().compareTo( other.market.getId() );
    // Lowest priority is greater, ie. has higher priority
    if (r == 0)
      r = this.priority > other.priority ? 1 : -1;
		   
    return r ;
  }

}
