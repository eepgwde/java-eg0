/**
 * @author Martin Anderson
 * @author weaves
 * @brief Control for partitioning, bet matching.
 *
 * See Partitioner and byBet for partitioning.
 */

package com.betfair;

import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Collection;
import java.util.SortedSet;
import java.util.Comparator;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatchingEngine {

  static final Logger logger = Logger.getLogger(MatchingEngine.class);

  protected Collection<Bet> bets;
  protected Partitioner byMarket;

  public java.util.concurrent.ConcurrentSkipListSet<Bet> results =
    new java.util.concurrent.ConcurrentSkipListSet<Bet>();

  void write0(String m, Collection<Bet> s) {
    logger.debug(m);
    if (s == null) {
      logger.debug("is empty");
      return;
    }
    for (Bet x : s) logger.debug(x);
  }
  
  public MatchingEngine(Collection<Bet> bets) throws IllegalArgumentException {
    logger.debug("scenario");

    byMarket = new Partitioner(bets);
    byMarket.sort();

    List<Bet> v = byMarket.getMarks();
    write0("marks", v);
  }

  public int threads0 = 5;
  public ExecutorService executor = null;

  class Matcher implements Runnable {
    protected SortedSet<Bet> bets;
    protected Set<Bet> collector;
     
    public Matcher(SortedSet bets, Set<Bet> collector){
      this.bets=bets;
      this.collector = collector;
    }

    protected ByBet byBet() {
      ByBet bybet = new ByBet(bets);
      bybet.sort();
      return bybet;
    }
 
    @Override
    public void run() {
      logger.debug(String.format("%s; market: %s",
				 Thread.currentThread().getName(),
				 bets.first().market.getId()));
      match1(byBet());
    }

    /**
     * The matching algorithm.
     *
     * We have a Set of bets for one market. Split those by bet-type.
     * If we have two bet-types, we proceed otherwise add all to the collector.
     */
    protected void match1(ByBet aMarket) {
      LinkedList<SortedSet<Bet>> batches = new LinkedList<SortedSet<Bet>>();
      SortedSet<Bet> r1 = null;
      while ( (r1 = aMarket.byQ()) != null) {
	write0("bet-type", r1);
	batches.add(r1);
      }

      // Finally, we can match up.
      if (batches.size() != 2) {
	logger.debug("no-matches");
	for (SortedSet<Bet> x : batches)
	  collector.addAll(x);
	return;
      }

      // Find the smaller of the two.
      SortedSet<Bet> left = batches.get(0);
      SortedSet<Bet> right = batches.get(1);

      if (left.size() > right.size()) {
	SortedSet<Bet> t = left;
	left = right;
	right = t;
      }

      logger.debug(String.format("has-matches: %d %d",
				 left.size(), right.size()));

      // Match those in smaller set with one in the larger and add to the collector.
      Iterator<Bet> ir = right.iterator();
      for (Bet l : left) {
	l.setMatch(ir.next());
	collector.add(l);
      }
      // Add those left in the larger set to the collector unmatched.
      while (ir.hasNext()) {
	collector.add(ir.next());
      }
    }

  }

  /**
   * Split by market and start a thread for each one.
   */
  public void match() {
    executor = Executors.newFixedThreadPool(this.threads0);

    SortedSet<Bet> r0 = null;
    while ( (r0 = byMarket.byQ()) != null) {
      write0("market", r0);
	
      Runnable worker = new Matcher(r0, this.results);
      executor.execute(worker);
    }
  }

  public boolean isTerminated() {
    boolean r = executor.isTerminated();

    return r;
  }

  public void terminate(int time0) {
    if (!executor.isTerminated())
      executor.shutdown(); // Disable new tasks from being submitted
    
    try {
      // Wait a while for existing tasks to terminate
      if (!executor.awaitTermination(time0, TimeUnit.SECONDS)) {
	executor.shutdownNow(); // Cancel currently executing tasks
	// Wait a while for tasks to respond to being cancelled
	if (!executor.awaitTermination(time0, TimeUnit.SECONDS))
	  System.err.println("Executor did not terminate");
      }
    } catch (InterruptedException ie) {
      // (Re-)Cancel if current thread also interrupted
      executor.shutdownNow();
      // Preserve interrupt status
      Thread.currentThread().interrupt();
    }
  }


}
