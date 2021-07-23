package vn.edu.hust.simulation.realtime;

import org.cloudbus.cloudsim.core.Simulation;
import vn.edu.hust.simulation.algos.AbstractSchedulingAlgorithm;
import vn.edu.hust.simulation.algos.SimpleAlgo;
import vn.edu.hust.simulation.algos.myalgo.basic.BestFitResourcesAlgo;
import vn.edu.hust.simulation.algos.myalgo.basic.MyAlgo;
import vn.edu.hust.simulation.algos.myalgo.myheuristic.LoadBalancingAlgo;
import vn.edu.hust.simulation.algos.myalgo.myheuristic.WorstfitAlgo;

public class AlgoSelector {

    public final String DATA_PATH = "/home/quangkhanh/F/task-scheduling-in-cloud-computing/data-exploration/experiments/data/new/";
    private String CLOUDLET_DATA_PATH;
    private String VM_DATA_PATH;
    private String ALGO_SOLVE_TIME;
    private String CLOUDLET_RUNNING_PATH;
    private String CPU_USAGE_PATH;
    private AbstractSchedulingAlgorithm algo;
    private Simulation simulation;

    public AlgoSelector(Simulation simulation) {
        this.simulation = simulation;
    }

    public String getCLOUDLET_DATA_PATH() {
        return CLOUDLET_DATA_PATH;
    }

    public String getVM_DATA_PATH() {
        return VM_DATA_PATH;
    }

    public String getALGO_SOLVE_TIME() {
        return ALGO_SOLVE_TIME;
    }

    public String getCLOUDLET_RUNNING_PATH() {
        return CLOUDLET_RUNNING_PATH;
    }

    public String get_COMPARISION_DATA_PATH() {
        return DATA_PATH + "comparison.csv";
    }

    public String getCPU_USAGE_PATH() {
        return CPU_USAGE_PATH;
    }

    public AbstractSchedulingAlgorithm getAlgo() {
        return this.algo;
    }

    public void loadMyAlgo() {
        CLOUDLET_DATA_PATH = DATA_PATH + "cloudlet_me.csv";
        VM_DATA_PATH = DATA_PATH + "vm_usage_me.csv";
        ALGO_SOLVE_TIME = DATA_PATH + "my_algo_solve_time.csv";
        CLOUDLET_RUNNING_PATH = DATA_PATH + "my_algo_cloudlet_running.csv";
        CPU_USAGE_PATH = DATA_PATH + "my_algo_cpu_usage.csv";
        this.algo = new MyAlgo(simulation);
    }

    public void loadRR() {
        CLOUDLET_DATA_PATH = DATA_PATH + "cloudlet_rr.csv";
        VM_DATA_PATH = DATA_PATH + "vm_usage_rr.csv";
        ALGO_SOLVE_TIME = DATA_PATH + "rr_solve_time.csv";
        CLOUDLET_RUNNING_PATH = DATA_PATH + "rr_cloudlet_running.csv";
        CPU_USAGE_PATH = DATA_PATH + "rr_cpu_usage";
        this.algo = new SimpleAlgo();
    }

    public void loadBestfit() {
        CLOUDLET_DATA_PATH = DATA_PATH + "cloudlet_bestfit.csv";
        VM_DATA_PATH = DATA_PATH + "vm_usage_bestfit.csv";
        ALGO_SOLVE_TIME = DATA_PATH + "bestfit_solve_time.csv";
        CLOUDLET_RUNNING_PATH = DATA_PATH + "bestfit_cloudlet_running.csv";
        CPU_USAGE_PATH = DATA_PATH + "bestfit_cpu_usage.csv";
        this.algo = new BestFitResourcesAlgo();
    }

    public void loadBalancingAlgo() {
        CLOUDLET_DATA_PATH = DATA_PATH + "cloudlet_balancing.csv";
        VM_DATA_PATH = DATA_PATH + "vm_usage_balancing.csv";
        ALGO_SOLVE_TIME = DATA_PATH + "balancing_solve_time.csv";
        CLOUDLET_RUNNING_PATH = DATA_PATH + "balancing_cloudlet_running.csv";
        CPU_USAGE_PATH = DATA_PATH + "balancing_cpu_usage.csv";
        this.algo = new LoadBalancingAlgo();
    }

    public void loadWorstfit() {
        CLOUDLET_DATA_PATH = DATA_PATH + "cloudlet_worstfit.csv";
        VM_DATA_PATH = DATA_PATH + "vm_usage_worstfit.csv";
        ALGO_SOLVE_TIME = DATA_PATH + "worstfit_solve_time.csv";
        CLOUDLET_RUNNING_PATH = DATA_PATH + "worstfit_cloudlet_running.csv";
        CPU_USAGE_PATH = DATA_PATH + "worstfit_cpu_usage.csv";
        this.algo = new WorstfitAlgo();
    }
}
