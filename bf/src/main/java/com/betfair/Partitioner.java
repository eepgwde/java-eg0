/**
 * @author weaves
 * @brief Partitioning sets.
 *
 * In later Java editions, it might be easier to use lambda functions.
 * For now, we partition using Comparator.
 */

package com.betfair;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Collection;
import java.util.SortedSet;
import java.util.Comparator;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * A set partitioner for Bet.
 *
 * This base implementation will sort and split on the market.
 *
 * By changing the comptor Comparator and the marking() method in
 * sub-classes, it is possible to split on other qualities of the Bet
 * class.
 *
 */
public class Partitioner {

  static final Logger logger = Logger.getLogger(Partitioner.class);
  
  protected Collection<Bet> bets;

  public Partitioner(Collection<Bet> bets) throws IllegalArgumentException {
    if (bets == null || bets.size() < 1) throw new IllegalArgumentException("empty bets");

    this.bets = bets;
  }

  protected TreeSet<Bet> byQ;
  protected LinkedList<Bet> marks = new LinkedList<Bet>();

  protected Comparator<Bet> comptor = null;

  /**
   * Sort the bets by market and priority.
   *
   * Construct a set of marks within the set of where the new markets begin.
   *
   * @note
   * Sadly, I can never get the higher() method of SortedSet to iterate through the markets.
   * @note
   * Java 7 has lambda expressions and predicates for this sort of thing.
   */
  public SortedSet<Bet> sort() {
    if (comptor == null)
      byQ = new TreeSet<Bet>(bets);
    else {
      byQ = new TreeSet<Bet>(comptor);
      byQ.addAll(bets);
    }
    assert byQ.size() == bets.size();

    marks.add(byQ.first());

    for (Bet x : byQ) marking(x);

    marks.remove();

    return byQ;
  }

  /** 
   * The marking method.
   *
   * @note
   * This should be over-ridden by sub-classes.
   */
  protected void marking(Bet x) {
    if (x.market != marks.getLast().market)
      marks.add(x);
  }

  public List<Bet> getMarks() {
    return marks;
  }

  /**
   * Iteratively retrieve each partition for each market.
   *
   * @note 
   * Each market can be matched in a thread.
   */
  public SortedSet<Bet> byQ() {
    if (byQ == null) return null;

    SortedSet<Bet> r0 = byQ;

    if (marks.size() <= 0 && byQ.size() > 0) {
      byQ = null;
      return r0;
    }
    
    Bet mark = marks.poll();
    r0 = byQ.headSet(mark);
    byQ = new TreeSet<Bet>(byQ.tailSet(mark));

    return r0;
  }

  class BetComparator implements Comparator<Bet> {
    public BetComparator() {}

    @Override
    public int compare(Bet o1, Bet o2) {
      // The same object
      if (o1.priority == o2.priority) return 0;

      int r = 1;
      if (o1.getClass() == o2.getClass())
	r = 0;
      else if (o1 instanceof Lay && o2 instanceof Back)
	r = -1;

      if (r == 0)
	r = o1.priority > o2.priority ? 1 : -1;

      return r;
    }
  }
}
