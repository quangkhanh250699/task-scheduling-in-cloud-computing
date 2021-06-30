package vn.edu.hust.simulation.reader;

import java.util.List;

public interface IReader<T> {

    void setMaxLines(int maxLines);

    List<T> read(String dataPath);
}
