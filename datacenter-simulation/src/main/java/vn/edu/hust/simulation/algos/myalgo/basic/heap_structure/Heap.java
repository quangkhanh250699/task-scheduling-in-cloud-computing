package vn.edu.hust.simulation.algos.myalgo.basic.heap_structure;

import java.util.List;

public interface Heap<T> {

    void add(T t);

    void clear();

    boolean contains(T t);

    List<T> toList();

    T peek();

    T pop();

    int size();
}
