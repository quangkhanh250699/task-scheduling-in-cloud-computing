package vn.edu.hust.simulation.algos.myalgo.basic.heap_structure;

import java.util.Comparator;

public class VmResourcesMaxHeap extends MaxHeap<VmResourcesAvailable> {


    public VmResourcesMaxHeap(int size, Comparator<VmResourcesAvailable> comparator) {
        super(size, comparator);
        this.elements = new VmResourcesAvailable[this.maxSize + 1];
    }
}
