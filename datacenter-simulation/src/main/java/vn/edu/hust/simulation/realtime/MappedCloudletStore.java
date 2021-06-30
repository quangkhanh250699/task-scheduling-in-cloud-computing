package vn.edu.hust.simulation.realtime;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MappedCloudletStore extends AbstractMappedCloudletStore{

    protected Queue<List<Cloudlet>> mappedCloudletQueue;
    protected Queue<Double> submittingTime;

    public MappedCloudletStore() {
        this.mappedCloudletQueue = new LinkedList<>();
        this.submittingTime = new LinkedList<>();
    }

    @Override
    public void addMappedCloudlet(List<Cloudlet> cloudlets, double submittingTime) {
        this.mappedCloudletQueue.add(cloudlets);
        this.submittingTime.add(submittingTime);
    }

    @Override
    public boolean isEmpty() {
        return this.mappedCloudletQueue.isEmpty();
    }

    @Override
    public List<Cloudlet> getSubmittingCloudlet(double now) {
        if (!this.mappedCloudletQueue.isEmpty()) {
            double nextSubmittingTime = this.submittingTime.peek();
            if (now >= nextSubmittingTime) {
                this.submittingTime.poll();
                return this.mappedCloudletQueue.poll();
            }
        }
        return null;
    }
}
