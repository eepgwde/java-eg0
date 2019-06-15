/**
 * @author weaves
 * @file Test0.java
 * @brief Test instantiations by clients.
 *
 */

package artikus;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
 
import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Collectors;

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

public class Test0 {
  static final Logger logger = Logger.getLogger(Test0.class);

  static {
    BasicConfigurator.configure();
    Logger.getRootLogger().setLevel(org.apache.log4j.Level.ERROR);
    logger.debug("Hello World!");
  }

  @Before
  public void setUp() {
  }

  @Test
  public void Test0_1() {
    assertNotNull(new Double(1));
    
    int [] values = {3,1,2};
    Plaintiff p = new Plaintiff(values);
    {
    String s = p.toString();
    System.out.println(s);
    }
    
    Collections.sort(p.values);

    {
    String s = p.toString();
    System.out.println(s);
    }

    p.values = p.values.stream().filter(n -> n > 0).collect(Collectors.toList());

    {
    String s = p.toString();
    System.out.println(s);
    }

    
    {
    String s = p.toString();
    System.out.println(s);
    }
  }
  
  @Test
  public void Test0_2() {
    assertNotNull(new Double(1));
    
    int [] values = {-3,1,-2, 2, 3, 5};
    Plaintiff p = new Plaintiff(values);
    {
    String s = p.toString();
    System.out.println(s);
    }
    
    Collections.sort(p.values);

    {
    String s = p.toString();
    System.out.println(s);
    }

    p.values = p.values.stream().filter(n -> n > 0).collect(Collectors.toList());
    
    {
    String s = p.toString();
    System.out.println(s);
    }

    int sum = p.values.stream().peek(System.out::println).reduce(0, (x, y) -> y);
    System.out.println(sum);
    
    Helper.instance().fibonacci(4);

  }

  @Test
  public void Test0_3() {
    assertNotNull(new Double(1));
    int x;
    x = 8;
    List<Integer> y = Helper.instance().fibonacci(x);
    System.out.println("fibonacci: " + y);
            
  }

}
