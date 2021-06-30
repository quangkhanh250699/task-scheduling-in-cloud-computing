package vn.edu.hust.simulation.algos.myalgo.basic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.List;

public interface CloudletQueue {

    List<Cloudlet> currentQueue();

    void addCloudlet(Cloudlet cloudlet);

    void addCloudlets(List<Cloudlet> cloudlets);
}
