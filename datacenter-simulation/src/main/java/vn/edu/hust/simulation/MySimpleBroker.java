package vn.edu.hust.simulation;

import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.core.CloudSim;

public class MySimpleBroker extends DatacenterBrokerSimple{


    public MySimpleBroker(CloudSim simulation) {
        super(simulation);
    }

    public MySimpleBroker(CloudSim simulation, String name) {
        super(simulation, name);
    }
}
