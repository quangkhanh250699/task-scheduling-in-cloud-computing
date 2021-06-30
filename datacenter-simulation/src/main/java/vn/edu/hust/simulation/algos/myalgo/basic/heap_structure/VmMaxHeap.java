package vn.edu.hust.simulation.algos.myalgo.basic.heap_structure;

import org.cloudbus.cloudsim.vms.Vm;
import vn.edu.hust.simulation.reader.SimpleVmReader;
import vn.edu.hust.simulation.realtime.RealtimeSimulation;

import java.util.Comparator;
import java.util.List;

public class VmMaxHeap extends MaxHeap<Vm>{

    public VmMaxHeap(int size, Comparator<Vm> comparator) {
        super(size, comparator);
        this.elements = new Vm[maxSize + 1];
    }

    @Override
    public void clear() {
        this.elements = new Vm[maxSize + 1];
    }

    public static void main(String[] args) {
        SimpleVmReader reader = new SimpleVmReader(RealtimeSimulation::createVm);
        List<Vm> vms = reader.read("src/main/resources/test_data/vms_v1.csv");
        VmMaxHeap head = new VmMaxHeap(10000, new VmComparator());
        vms.forEach(
                vm -> head.add(vm)
        );
        while (head.size() > 0) {
            Vm vm = head.pop();
            System.out.println(vm.getId());
        }
    }
}
