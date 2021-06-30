package vn.edu.hust.simulation.reader;

public class TaskInfo extends Info{
    private int id;
    private long comingTime;
    private int priority;
    private int cpuCores;
    private double cpuRequest;
    private double memoryRequest;
    private double diskspaceRequest;
    private double bandwidthRequest;
    private long length;

    public TaskInfo(long comingTime, int cpuCores, double cpuRequest, double memoryRequest, double diskspaceRequest, double bandwidthRequest, long length) {
        this.comingTime = comingTime;
        this.cpuCores = cpuCores;
        this.cpuRequest = cpuRequest;
        this.memoryRequest = memoryRequest;
        this.diskspaceRequest = diskspaceRequest;
        this.bandwidthRequest = bandwidthRequest;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public TaskInfo setId(int id) {
        this.id = id;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public long getComingTime() {
        return comingTime;
    }

    public int getCpuCores() {
        return cpuCores;
    }

    public double getCpuRequest() {
        return cpuRequest;
    }

    public double getMemoryRequest() {
        return memoryRequest;
    }

    public double getDiskspaceRequest() {
        return diskspaceRequest;
    }

    public double getBandwidthRequest() {
        return bandwidthRequest;
    }

    public long getLength() {
        return length;
    }
}
