package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.ArrayList;
import java.util.List;

public class ShortTermQueue implements CloudletQueue{

    private List<SimpleCloudletQueue> queues;

    public ShortTermQueue() {
        this.queues = new ArrayList<>();
    }

    @Override
    public List<Cloudlet> currentQueue() {
        return this.queues.get(this.queues.size() - 1).currentQueue();
    }

    @Override
    public void addCloudlet(Cloudlet cloudlet) {
        this.currentQueue().add(cloudlet);
    }

    @Override
    public void addCloudlets(List<Cloudlet> cloudlets) {
        this.currentQueue().addAll(cloudlets);
    }

    public void addNextQueue() {
        this.queues.add(new SimpleCloudletQueue());
    }

    public List<SimpleCloudletQueue> getQueues() {
        return this.queues;
    }

    public void update() {
//        this.queues.stream().filter(
//                simpleClouletQueue -> {
//                    simpleClouletQueue.update();
//                    return simpleClouletQueue.finishedCloudlets().size() < simpleClouletQueue.currentQueue().size();
//                }
//        );
    }
}
