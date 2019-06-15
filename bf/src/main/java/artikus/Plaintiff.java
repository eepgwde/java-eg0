/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artikus;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Collections;

/**
 *
 * @author weaves
 */
public class Plaintiff {
    
    public List<Integer> values = null;
    
  protected Double value = null;

  public Plaintiff(int [] values) {
      this.values = Arrays.stream(values).boxed().collect(Collectors.toList());
  }

    public Optional<Integer> getGap() {
        if (values == null || values.isEmpty())
            return Optional.empty();

        Collections.sort(values);
        // values.stream().filter(n -> n > 0).collect(Collectors.toList());                
        return Optional.empty();
    }
  
  // @note 
  // joining has a prefix and a suffic 
  public String toString() {
    return Arrays.asList(values).stream().map(n -> n.toString()).collect(Collectors.joining(",")) ;
  }
    
}
