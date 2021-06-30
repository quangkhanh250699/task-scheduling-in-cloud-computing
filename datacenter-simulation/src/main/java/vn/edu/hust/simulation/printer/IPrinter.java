package vn.edu.hust.simulation.printer;

import java.util.List;

public interface IPrinter<T> {

    void print(List<T> data, String dataPath);
}
