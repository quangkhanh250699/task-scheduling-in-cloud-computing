package vn.edu.hust.simulation.reader;

public class VmInfo extends Info{
    private int id;
    private int cpuCores;
    private long mips;
    private long ram;
    private long storage;
    private long bandwidth;

    public VmInfo(int id, int cpuCores, long mips, long ram, long storage, long bandwidth) {
        this.id = id;
        this.cpuCores = cpuCores;
        this.mips = mips;
        this.ram = ram;
        this.storage = storage;
        this.bandwidth = bandwidth;
    }

    public int getId() {
        return id;
    }

    public int getCpuCores() {
        return cpuCores;
    }

    public long getMips() {
        return mips;
    }

    public long getRam() {
        return ram;
    }

    public long getStorage() {
        return storage;
    }

    public long getBandwidth() {
        return bandwidth;
    }
}
