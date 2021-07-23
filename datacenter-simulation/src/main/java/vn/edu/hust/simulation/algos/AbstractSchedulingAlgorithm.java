package vn.edu.hust.simulation.algos;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public abstract class AbstractSchedulingAlgorithm implements SchedulingAlgorithm<CloudletVmMappingSolution> {

    protected List<Cloudlet> comingCloudlets = new ArrayList<>();

    protected boolean isRunning = false;
    protected Semaphore semaphore = new Semaphore(1, true);

    protected double runningTime = 0;

    @Override
    public void solve() {
        this.isRunning = true;
        long startTime = System.nanoTime();
        this.processing();
        long endTime = System.nanoTime();
        this.runningTime = (double) (endTime - startTime) / 1000000000;
        this.runningTime += 2;
        this.isRunning = false;
    }

    protected abstract void processing();

    public boolean isRunning() {
        return this.isRunning;
    }

    public void setRunningStatus(boolean runningStatus) {
        try {
            this.semaphore.acquire();
            this.isRunning = runningStatus;
            this.semaphore.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void setVms(List<Vm>vms);

    public void setComingCloudlets(List<Cloudlet> cloudlets) {
        this.comingCloudlets.clear();
        this.comingCloudlets.addAll(cloudlets);
    }

    public List<Cloudlet> getComingCloudlets() {
        return this.comingCloudlets;
    }

    @Override
    public double getSolveTime() {
        return this.runningTime;
    }
}
