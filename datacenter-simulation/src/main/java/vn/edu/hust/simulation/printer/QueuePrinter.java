package vn.edu.hust.simulation.printer;

import vn.edu.hust.simulation.algos.myalgo.basic.CloudletQueue;

import java.util.List;

public abstract class QueuePrinter<T extends CloudletQueue> implements CSVPrinter{

    protected String[] titles;

    public void print(T t, String dataPath) {
        List<String[]> data = this.convert(t);
        data.add(0, titles);
        try {
            this.csvWriterOneByOne(data, dataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract List<String[]> convert(T t);
}
