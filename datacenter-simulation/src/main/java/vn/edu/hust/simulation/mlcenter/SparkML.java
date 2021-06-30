package vn.edu.hust.simulation.mlcenter;

import java.util.List;

public interface SparkML<T, U> {

    void train(String dataPath);

    void setup();

    List<T> predict(List<U> us);
}
