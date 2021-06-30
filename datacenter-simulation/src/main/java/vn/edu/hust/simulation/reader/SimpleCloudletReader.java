package vn.edu.hust.simulation.reader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SimpleCloudletReader extends SimpleAbstractReader<TaskInfo, Cloudlet> {

    public SimpleCloudletReader(Function<TaskInfo, Cloudlet> createCloudletFunction) {
        super(createCloudletFunction);
    }

    @Override
    protected List<TaskInfo> readInfo(String dataPath) {
        List<TaskInfo> taskInfos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            String[] lineInArray;
            reader.readNext();
            while ((lineInArray = reader.readNext()) != null) {
                int id = Integer.parseInt(lineInArray[0]);
                long comingTime = Long.parseLong(lineInArray[1]);
                int cpuCores = Integer.parseInt(lineInArray[2]);
                double cpu = Double.parseDouble(lineInArray[3]);
                double ram = Double.parseDouble(lineInArray[4]);
                double storage = Double.parseDouble(lineInArray[5]);
                double bandwidth = Double.parseDouble(lineInArray[6]) / 100;
                long length = Long.parseLong(lineInArray[7]);
                taskInfos.add(
                    (new TaskInfo(comingTime, cpuCores, cpu, ram, storage, bandwidth, length)).setId(id)
                );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }

        return taskInfos;
    }

    public List<Double> readComingTime(String dataPath) {
        List<Double> comingTime = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            String[] lineInArray;
            reader.readNext();
            while((lineInArray = reader.readNext()) != null) {
                double time = Double.parseDouble(lineInArray[0]);
                comingTime.add(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comingTime;
    }
}
