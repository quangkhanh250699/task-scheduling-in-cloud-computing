package vn.edu.hust.simulation.printer;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import vn.edu.hust.simulation.algos.myalgo.basic.ShortTermQueue;
import vn.edu.hust.simulation.algos.myalgo.basic.SimpleCloudletQueue;

import java.util.ArrayList;
import java.util.List;

public class ShortQueuePrinter extends QueuePrinter<ShortTermQueue> {

    public ShortQueuePrinter() {
        this.titles = new String[] {"queueId", "taskId"};
    }

    @Override
    public List<String[]> convert(ShortTermQueue shortTermQueue) {
        List<SimpleCloudletQueue> queues = shortTermQueue.getQueues();
        List<String[]> data = new ArrayList<>();

        for (int i = 0; i < queues.size(); ++i) {
            List<Cloudlet> currentQueue = queues.get(i).currentQueue();
            for (int j = 0; j < currentQueue.size(); ++j) {
                long taskId = currentQueue.get(j).getId();
                data.add(
                        new String[] {Integer.toString(i), Long.toString(taskId)}
                );
            }
        }

        return data;
    }
}
