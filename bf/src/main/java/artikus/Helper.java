/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artikus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.Optional;
import java.util.Collections;

/**
 *
 * @author weaves
 */
public class Helper<T extends Number> {

    private static Helper instance_ = null;

    public static Helper instance() {
        if (instance_ == null) {
            instance_ = new Helper();
        }
        return instance_;
    }

    protected Helper() {
    }

    public List<Integer> fibonacci(int series) {
        return Stream.iterate(new int[]{0, 1}, i -> new int[]{i[1], i[0] + i[1]})
                .limit(series)
                .map(i -> i[0])
                .collect(Collectors.toList());
    }

    public Stream<Integer> as(int[] values) {
        return Arrays.stream(values).boxed();
    }

    public List<T> as(Stream<T> values) {
        return values.collect(Collectors.toList());
    }

    public List<Integer> monotonic0(Stream<Integer> values) {
        List<Integer> v;
        v = values.filter(n -> n > 0).collect(Collectors.toList());
        Collections.sort(v);
        v = v.stream().map(i -> i + 1)
            .collect(Collectors.toList());
        return v;
    }

}

