/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artikus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author weaves
 */
public class Helper {

    private static Helper instance_ = null;

    public static Helper instance() {
        if (instance_ == null) {
            instance_ = new Helper();
        }
        return instance_;
    }

    protected Helper() {
    }

    List<Integer> fibonacci(int series) {
        return Stream.iterate(new int[]{0, 1}, i -> new int[]{i[1], i[0] + i[1]})
                .limit(series)
                .map(i -> i[0])
                .collect(Collectors.toList());
    }
}

