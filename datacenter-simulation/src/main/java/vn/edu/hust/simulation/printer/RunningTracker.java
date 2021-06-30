package vn.edu.hust.simulation.printer;

public interface RunningTracker <T>{

    void addInfo(T t, double timestamp, String mode);

    void printCSV(String dataPath);
}
