package vn.edu.hust.simulation.algos.myalgo.basic.cluster_structure;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.ArrayList;
import java.util.List;

public class CloudletCluster implements ICluster<Cloudlet> {

    private List<Cloudlet> elements;

    public CloudletCluster() {
        this.elements = new ArrayList<>();
    }

    public CloudletCluster(List<Cloudlet> elements) {
        this.elements = elements;
    }

    public void addElement(Cloudlet cloudlet) {
        this.elements.add(cloudlet);
    }

    public void removeElement(Cloudlet cloudlet) {
        this.elements.remove(cloudlet);
    }

    @Override
    public List<Cloudlet> getElements() {
        return elements;
    }
}
