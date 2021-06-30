package vn.edu.hust.simulation;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.vms.Vm;

import java.util.Map;

public class CloudletToVmDispatcher {

    public static void setCloudletsToVms(Map<Cloudlet, Vm> mapper) {
        if (mapper == null)
            return;

        mapper.entrySet().forEach(
            entry -> entry.getKey().setVm(entry.getValue())
        );
    }
}
