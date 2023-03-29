package com.wxy.demo;

import com.baomidou.mybatisplus.extension.api.R;
import org.apache.poi.ss.formula.functions.T;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface DemoInterface {

    R apply(T t);

    //    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
//        Objects.requireNonNull(before);
//        return (V v) -> apply(before.apply(v));
//    }
    default void test() {

    }

}
