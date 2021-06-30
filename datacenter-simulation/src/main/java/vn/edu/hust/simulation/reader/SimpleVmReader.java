package vn.edu.hust.simulation.reader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.cloudbus.cloudsim.vms.Vm;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SimpleVmReader extends SimpleAbstractReader<VmInfo, Vm> {

    public SimpleVmReader(Function<VmInfo, Vm> createFuntion) {
        super(createFuntion);
    }

    @Override
    protected List<VmInfo> readInfo(String dataPath) {
        List<VmInfo> vmInfos = new ArrayList<>();

        try(CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            String[] lineInArray;
            reader.readNext();
            while ((lineInArray = reader.readNext()) != null) {
                int id = Integer.parseInt(lineInArray[0]);
                int cpuCores = Integer.parseInt(lineInArray[1]);
                long mips = Long.parseLong(lineInArray[2]);
                long ram = Long.parseLong(lineInArray[3]);
                long storage = Long.parseLong(lineInArray[4]);
                long bandwidth = Long.parseLong(lineInArray[5]);

                vmInfos.add(new VmInfo(id, cpuCores, mips, ram, storage, bandwidth));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }

        return vmInfos;
    }
}
