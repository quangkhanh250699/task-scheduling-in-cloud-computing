package vn.edu.hust.simulation.printer;

import java.util.ArrayList;
import java.util.List;

public class AlgoPrinter implements CSVPrinter{

    private List<Double> schedulingTimes;
    private List<Integer> numCloudlets;

    public AlgoPrinter() {
        this.schedulingTimes = new ArrayList<>();
        this.numCloudlets = new ArrayList<>();
    }

    public void addElements(int numCloudlets, double schedulingTimes) {
        this.schedulingTimes.add(schedulingTimes);
        this.numCloudlets.add(numCloudlets);
    }

    public void print(String dataPath) {
        List<String[]> list = new ArrayList<>();
        String[] title = new String[] {"num_cloudlets", "solve_time"};
        list.add(title);
        list.addAll(this.convertToString());
        try {
            this.csvWriterOneByOne(list, dataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String[]> convertToString() {
        List<String[]> result = new ArrayList<>();
        for (int i = 0; i < this.schedulingTimes.size(); ++i) {
            String nCloudlets = Integer.toString(this.numCloudlets.get(i));
            String time = Double.toString(this.schedulingTimes.get(i));
            result.add(new String[] {nCloudlets, time});
        }
        return result;
    }
}
