package vn.edu.hust.simulation.algos.myalgo.basic.heap_structure;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MaxHeap<T> implements Heap<T> {

    protected Comparator<T> comparator;
    protected T[] elements;
    protected int currentIndex;
    protected int maxSize;

    public MaxHeap(int size, Comparator<T> comparator) {
        this.comparator = comparator;
        this.maxSize = size;
        this.currentIndex = -1;
    }

    @Override
    public void add(T t) {
        this.currentIndex += 1;
        this.elements[currentIndex] = t;
        this.updatePositionBottomUp(currentIndex);
    }

    private int updatePositionBottomUp(int n) {
        if (n == 0)
            return 0;
        int k = (int) (n-1)/2;
        if (this.comparator.compare(elements[k], elements[n]) > 0) {
            this.swap(k, n);
            return updatePositionBottomUp(k);
        } else {
            return n;
        }
    }

    private void swap(int i, int j) {
        T temp = this.elements[i];
        this.elements[i] = this.elements[j];
        this.elements[j] = temp;
    }

    @Override
    public boolean contains(T t) {
        return true;
    }

    @Override
    public List<T> toList() {
        return Arrays.asList(this.elements);
    }

    @Override
    public T peek() {
        if (currentIndex >= 0)
            return this.elements[0];
        else
            return null;
    }

    @Override
    public T pop() {

        if (currentIndex < 0)
            return null;

        this.swap(0, currentIndex);
        currentIndex -= 1;
        this.updatePositionTopDown(0);
        return this.elements[currentIndex+1];
    }

    @Override
    public int size() {
        return this.currentIndex;
    }

    @Override
    public void clear() {
        this.currentIndex = -1;
    }

    private int updatePositionTopDown(int n) {
        int left = 2 * n + 1;
        int right = 2 * n + 2;
        int maxInd = n;

        if (right <= this.currentIndex) {
            if (this.comparator.compare(elements[left], elements[right]) > 0)
                maxInd = right;
            else
                maxInd = left;
        } else if (left <= this.currentIndex)
            maxInd = left;
        else
            return n;

        if (this.comparator.compare(elements[n], elements[maxInd]) > 0) {
            this.swap(n, maxInd);
            return updatePositionTopDown(maxInd);
        } else
            return n;
    }
}
