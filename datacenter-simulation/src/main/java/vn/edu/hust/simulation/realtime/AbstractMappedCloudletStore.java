package vn.edu.hust.simulation.realtime;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.List;

public abstract class AbstractMappedCloudletStore {

    public abstract void addMappedCloudlet(List<Cloudlet> cloudlets, double submittingTime);

    public abstract boolean isEmpty();

    public abstract List<Cloudlet> getSubmittingCloudlet(double now);
}
