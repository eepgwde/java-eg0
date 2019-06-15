/**
 * @author weaves
 * @file Test0.java
 * @brief Test instantiations by clients.
 *
 */

package com.example.betfair;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
 
import java.io.Closeable;
import java.io.IOException;

import java.util.List;
import java.util.LinkedList;
import java.util.SortedSet;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.*;

import com.betfair.*;

public class Test0 {
  static final Logger logger = Logger.getLogger(Test0.class);

  static {
    BasicConfigurator.configure();
    Logger.getRootLogger().setLevel(org.apache.log4j.Level.ERROR);
    logger.debug("Hello World!");
  }

  Market mA;
  Market mB;
  List<Market> markets;

  List<Bet> s1;
  List<Bet> s2; 
  List<Bet> s3;
  List<Bet> s4;
  List<Bet> s5;
  List<Bet> s6;

  List<List<Bet>> scenarios = new java.util.ArrayList<List<Bet>>();

  @Before
  public void setUp() {
    mA = new Market("A");
    mB = new Market("B");
    markets = java.util.Arrays.asList(new Market[]{ mA, mB });

    Bet[] a;

    a = new Bet[] {
      Back.newInstance(mA, null, null),
      Lay.newInstance(mA, null, null)
    };
    s1 = new LinkedList<Bet>( java.util.Arrays.asList(a) );
    scenarios.add(s1);

    a = new Bet[] {
      Back.newInstance(mA, null, null),
      Lay.newInstance(mB, null, null)
    };
    s2 = new LinkedList<Bet>( java.util.Arrays.asList(a) );
    scenarios.add(s2);

    a = new Bet[] {
      Back.newInstance(mA, null, null),
      Back.newInstance(mB, null, null)
    };
    s3 = new LinkedList<Bet>( java.util.Arrays.asList(a) );
    scenarios.add(s3);

    a = new Bet[] {
      Back.newInstance(mA, null, null),
      Back.newInstance(mA, null, null),
      Lay.newInstance(mA, null, null)
    };
    s4 = new LinkedList<Bet>( java.util.Arrays.asList(a) );
    scenarios.add(s4);

    a = new Bet[] {
      Back.newInstance(mA, null, null),
      Lay.newInstance(mA, null, null),
      Back.newInstance(mB, null, null),
      Lay.newInstance(mB, null, null)
    };
    s5 = new LinkedList<Bet>( java.util.Arrays.asList(a) );
    scenarios.add(s5);

    a = new Bet[] {
      Back.newInstance(mA, null, null),
      Back.newInstance(mA, null, null),
      Lay.newInstance(mA, null, null),
      Back.newInstance(mB, null, null),
      Lay.newInstance(mB, null, null),
      Lay.newInstance(mB, null, null)
    };
    s6 = new LinkedList<Bet>( java.util.Arrays.asList(a) );
    scenarios.add(s6);
  }

  @Test
  public void Test0_1() {
    assertNotNull(mA);
    assertNotNull(mB);
    Bet a, b;
    a = Lay.newInstance(mA, null, null);
    assertNotNull(a);
    b = Back.newInstance(mA, null, null);
    assertSame(b, b);
    assertFalse(a.equals(b));
  }

  void write0(String m, java.util.Collection<Bet> s) {
    logger.info(m);
    if (s == null) {
      logger.info("is empty");
      return;
    }
    for (Bet x : s) logger.info(x);
  }

  @Test
  public void Test0_2() {
    write0("s1", s1);
  }

  @Test
  public void Test0_3() {
    for (List<Bet> s : scenarios) {
      Partitioner byMarket = new Partitioner(s);
      write0(String.format("size: %d", s.size()),
	     byMarket.sort());
    }
  }

  @Test
  public void Test0_4() {
    for (List<Bet> s : scenarios) {
      logger.debug("scenario");
      write0("before", s);
      Partitioner byMarket = new Partitioner(s);
      byMarket.sort();
      List<Bet> v = byMarket.getMarks();
      write0("marks", v);

      SortedSet<Bet> r0 = null;
      while ( (r0 = byMarket.byQ()) != null) {
	write0("market", r0);

	ByBet byBet = new ByBet(r0);
	byBet.sort();
	List<Bet> w = byBet.getMarks();
	write0("marks", w);
	
	LinkedList<SortedSet<Bet>> batches = new LinkedList<SortedSet<Bet>>();
	SortedSet<Bet> r1 = null;
	while ( (r1 = byBet.byQ()) != null) {
	  write0("bet-type", r1);
	  batches.add(r1);
	}

	// Finally, we can match up.
	if (batches.size() != 2) {
	  logger.debug("no-matches");
	  continue;
	}

	SortedSet<Bet> left = batches.get(0);
	SortedSet<Bet> right = batches.get(1);

	if (left.size() > right.size()) {
	  SortedSet<Bet> t = left;
	  left = right;
	  right = t;
	}

	logger.debug(String.format("has-matches: %d %d",
				   left.size(), right.size()));

      }
    }
  }

  void match0(List<Bet> s) {
    MatchingEngine e0 = new MatchingEngine(s);
    e0.match();
    e0.terminate(15);
    write0("final", e0.results);
  }

  @Test
  public void Test0_5() {
    match0(s1);
  }

  @Test
  public void Test0_6() {
    match0(s6);
  }

  @Test
  public void Test0_7() {
    Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
    int i=1;
    for (List<Bet> s : scenarios) {
      write0(String.format("input: scenario: %d", i++), s);
      match0(s);
    }
  }

}
