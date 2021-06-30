package vn.edu.hust.simulation.printer;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractCloudletPrinter implements IPrinter<Cloudlet>, CSVPrinter {

    protected String[] titles;

    protected abstract String[] generateTitles();

    @Override
    public void print(List<Cloudlet> data, String dataPath) {
        List<String[]> stringData = new ArrayList<>();
        this.titles = this.generateTitles();
        stringData.add(this.titles);
        stringData.addAll(data.stream().map(this::convertToString).collect(Collectors.toList()));
        try {
            this.csvWriterOneByOne(stringData, dataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract String[] convertToString(Cloudlet cloudlet);
}
