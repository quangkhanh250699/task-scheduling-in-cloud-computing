package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.List;

public class LongTermQueue implements CloudletQueue{

    SimpleCloudletQueue queue;

    public LongTermQueue() {
        this.queue = new SimpleCloudletQueue();
    }

    @Override
    public List<Cloudlet> currentQueue() {
        this.queue.update();
        return this.queue.currentQueue();
    }

    @Override
    public void addCloudlet(Cloudlet cloudlet) {
        this.queue.addCloudlet(cloudlet);
    }

    @Override
    public void addCloudlets(List<Cloudlet> cloudlets) {
        this.queue.addCloudlets(cloudlets);
    }

    public boolean contain(Cloudlet cloudlet) {
        return this.queue.contain(cloudlet);
    }
}
