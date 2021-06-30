package vn.edu.hust.simulation.schema;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

public class DataFrameManagerGetter {

    private static DataFrameManager<Cloudlet> frameManager;

    public static DataFrameManager<Cloudlet> getFrameManager() {
        if (frameManager == null)
            frameManager = new SimpleCloudletDataFrame();
        return frameManager;
    }

    public static DataFrameManager<Cloudlet> getStatusFrameManager() {
        return new StatusDataFrame();
    }
}
