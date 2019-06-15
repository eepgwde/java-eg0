/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artikus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.EnumSet;

import java.util.Optional;

import java.util.stream.Collectors;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.Collections;

import java.util.stream.Collector.Characteristics;
import java.util.function.Supplier;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * @author weaves
 *
 * A collector is more useful than a reduce. It can implement some history.
 */

public class HistogramCollector
    implements Collector<Double, Map<Integer, Integer>, Map<Integer, Integer>> {

    private int bucketSize;

    public HistogramCollector(int bucketSize) {
        this.bucketSize = bucketSize;
    }

    @Override
    public Supplier<Map<Integer, Integer>> supplier() {
        return HashMap::new;
    }

    /**
     * This does the increment of the count.
     * Either sets it to 1 or updates it with the increment.
     */
    @Override
    public BiConsumer<Map<Integer, Integer>, Double> accumulator() {
        return (map, val) -> map.merge((int)(val / bucketSize), 1,
                                       (a, b) -> a + 1);
    }

    /**
     * Used to convert the final type of the result.
     */
    @Override
    public Function<Map<Integer, Integer>, Map<Integer, Integer>> finisher() {
        return Function.identity();
    }

    /**
     * When used in parallel this merges the results.
     */
    @Override
    public BinaryOperator<Map<Integer, Integer>> combiner() {
        return (map1, map2) -> {
            map2.forEach((k, v) -> map1.merge(k, v, (v1, v2) -> v1 + v2));
            return map1;
        };
    }

    /**
     * Specification of how processing maybe carried out.
     */
    @Override
    public Set<java.util.stream.Collector.Characteristics> characteristics() {
        return Collections
            .unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH, 
                                        Characteristics.UNORDERED));
    }

    /**
     * utility method to get an instance.
     */
    public static HistogramCollector toHistogram(int bucketSize) {
        return new HistogramCollector(bucketSize);
    }

}
