package vn.edu.hust.simulation.printer;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.util.List;

public interface CSVPrinter {

    default void csvWriterOneByOne(List<String[]> stringArray, String path) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(path));
        for (String[] array : stringArray) {
            writer.writeNext(array);
        }
        writer.close();
    }
}
