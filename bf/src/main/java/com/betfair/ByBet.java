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

public class ByBet extends Partitioner {

  public ByBet(Collection<Bet> bets) throws IllegalArgumentException {
    super(bets);
    this.comptor = new BetComparator();
  }

  /**
   * Override for the partitioning code.
   */
  @Override
  protected void marking(Bet x) {
    if (x.getClass() != marks.getLast().getClass())
      marks.add(x);
  }

  /**
   * A comparator that uses bet type.
   */
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
      logger.debug("comparator: " + r);
      return r;
    }
  }
}
