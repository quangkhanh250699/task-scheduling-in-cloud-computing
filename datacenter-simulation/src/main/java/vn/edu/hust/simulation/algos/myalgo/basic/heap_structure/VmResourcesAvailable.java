package vn.edu.hust.simulation.algos.myalgo.basic.heap_structure;

public class VmResourcesAvailable {

    private double mips;
    private double cpuPercentage;
    private double ram;
    private double bandwidth;
    private double storage;
    private long id;

    public VmResourcesAvailable(long id, double mips, double cpuPercentage, double ram, double bandwidth, double storage) {
        this.id = id;
        this.mips = mips;
        this.cpuPercentage = cpuPercentage;
        this.ram = ram;
        this.bandwidth = bandwidth;
        this.storage = storage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMips() {
        return mips;
    }

    public void setMips(double mips) {
        this.mips = mips;
    }

    public double getCpuPercentage() {
        return cpuPercentage;
    }

    public void setCpuPercentage(double cpuPercentage) {
        this.cpuPercentage = cpuPercentage;
    }

    public double getRam() {
        return ram;
    }

    public void setRam(double ram) {
        this.ram = ram;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public double getStorage() {
        return storage;
    }

    public void setStorage(double storage) {
        this.storage = storage;
    }
}
