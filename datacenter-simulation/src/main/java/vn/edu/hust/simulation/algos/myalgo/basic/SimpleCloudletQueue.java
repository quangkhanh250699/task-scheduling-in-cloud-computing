package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.ArrayList;
import java.util.List;

public class SimpleCloudletQueue implements CloudletQueue{

    private double startRunningTime;

    public double getStartRunningTime() {
        return startRunningTime;
    }

    public void setStartRunningTime(double startRunningTime) {
        this.startRunningTime = startRunningTime;
    }

    private List<Cloudlet> cloudlets;
    private List<Cloudlet> runningCloudlets;
    private List<Cloudlet> finishedCloudlets;

    public SimpleCloudletQueue() {
        this.cloudlets = new ArrayList<>();
        this.runningCloudlets = new ArrayList<>();
        this.finishedCloudlets = new ArrayList<>();
    }

    public List<Cloudlet> runningCloudlets() {
        return runningCloudlets;
    }

    public List<Cloudlet> finishedCloudlets() {
        return finishedCloudlets;
    }

    @Override
    public List<Cloudlet> currentQueue() {
        return this.cloudlets;
    }

    @Override
    public void addCloudlet(Cloudlet cloudlet) {
        this.cloudlets.add(cloudlet);
    }

    public void addCloudlets(List<Cloudlet> cloudlets) {
        this.cloudlets.addAll(cloudlets);
    }

    public void removeCloudlet(Cloudlet cloudlet) {
//        this.cloudlets.remove(cloudlet);
    }

    public void removeCloudlets(List<Cloudlet> cloudlets) {
//        this.cloudlets.removeAll(cloudlets);
    }

    public void update() {
        List<Cloudlet> running = new ArrayList<>();
        List<Cloudlet> finished = new ArrayList<>();

        this.cloudlets.forEach(
                cloudlet -> {
                    if (cloudlet.getStatus() == Cloudlet.Status.INEXEC) {
                        running.add(cloudlet);
                    } else {
                        finished.add(cloudlet);
                    }
                }
        );

        this.runningCloudlets = running;
        this.finishedCloudlets = finished;
    }

    public boolean contain(Cloudlet cloudlet) {
        return this.cloudlets.contains(cloudlet);
    }
}
