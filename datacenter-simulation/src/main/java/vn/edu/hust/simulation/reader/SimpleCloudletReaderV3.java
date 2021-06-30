package vn.edu.hust.simulation.reader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModel;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SimpleCloudletReaderV3 extends SimpleCloudletReaderV2 {

    private final JobInfoManager jobInfoManager;

    public SimpleCloudletReaderV3(Function<TaskInfo, Cloudlet> createCloudletFunction) {
        super(createCloudletFunction);
        this.jobInfoManager = JobInfoManager.getInstance();
    }

    @Override
    protected List<TaskInfo> readInfo(String dataPath) {
        List<TaskInfo> taskInfos = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            String[] lineInArray;
            reader.readNext();
            while ((lineInArray = reader.readNext()) != null) {
                int jobId = Integer.parseInt(lineInArray[0]);
                int id = Integer.parseInt(lineInArray[1]);
                long comingTime = Long.parseLong(lineInArray[2]);
                int priority = Integer.parseInt(lineInArray[3]);
                int cpuCores = Integer.parseInt(lineInArray[4]);
                double cpu = Double.parseDouble(lineInArray[5]);
                double ram = Double.parseDouble(lineInArray[6]);
                double bandwidth = Double.parseDouble(lineInArray[7]);
                double storage = Double.parseDouble(lineInArray[8]);
                long length = Long.parseLong(lineInArray[9]);
                TaskInfo newInfo = new TaskInfo(comingTime, cpuCores, cpu, ram, storage, bandwidth, length);
                newInfo.setId(id);
                newInfo.setPriority(priority);
                taskInfos.add(newInfo);
                this.jobInfoManager.addTaskToJob(id, jobId);
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

    @Override
    public List<Double> readComingTime(String dataPath) {
        List<Double> comingTime = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            String[] lineInArray;
            reader.readNext();
            while((lineInArray = reader.readNext()) != null) {
                double time = Double.parseDouble(lineInArray[2]);
                comingTime.add(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comingTime;
    }

    public JobInfoManager getJobInfoManager() {
        return this.jobInfoManager;
    }

    public static Cloudlet createCloudlet(TaskInfo info) {
        int id = info.getId();
        long length = info.getLength();
        int cpuCores = info.getCpuCores();
        double cpu = info.getCpuRequest();
        double memory = info.getMemoryRequest();
        double diskspace = info.getDiskspaceRequest();
        double bandwidth = info.getBandwidthRequest();

        final UtilizationModelDynamic utilizationCpu =
                new UtilizationModelDynamic(UtilizationModel.Unit.PERCENTAGE, cpu, cpu);
        final UtilizationModelDynamic utilizationMemory =
                new UtilizationModelDynamic(UtilizationModel.Unit.ABSOLUTE, memory, memory);
        final UtilizationModelDynamic utilizationStorage =
                new UtilizationModelDynamic(UtilizationModel.Unit.ABSOLUTE, diskspace, diskspace);
        final UtilizationModelDynamic utilizationBandwidth =
                new UtilizationModelDynamic(UtilizationModel.Unit.ABSOLUTE, bandwidth, bandwidth);

        Cloudlet cloudlet =  new CloudletSimple(length, cpuCores)
                .setOutputSize(10)
                .setFileSize(10)
                .setUtilizationModelCpu(utilizationCpu)
                .setUtilizationModelRam(utilizationMemory)
                .setUtilizationModelBw(utilizationBandwidth);
        cloudlet.setId(id);
        return cloudlet;
    }

    public static void main(String[] args) {
        SimpleCloudletReaderV3 reader = new SimpleCloudletReaderV3(SimpleCloudletReaderV3::createCloudlet);
        String path = "src/main/resources/test_data/cloudlets_v6.csv";
        List<Cloudlet> cloudlets = reader.read(path);
        JobInfoManager manager = reader.getJobInfoManager();
        manager.getTaskIds(2).forEach(
                System.out::println
        );
    }
}
