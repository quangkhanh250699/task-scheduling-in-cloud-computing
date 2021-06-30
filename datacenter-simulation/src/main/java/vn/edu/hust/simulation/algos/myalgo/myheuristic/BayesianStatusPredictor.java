package vn.edu.hust.simulation.algos.myalgo.myheuristic;

import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletExecution;
import org.cloudbus.cloudsim.vms.Vm;
import vn.edu.hust.simulation.algos.myalgo.basic.AbstractStatusPredictor;
import vn.edu.hust.simulation.reader.JobInfoManager;

import java.util.*;
import java.util.stream.Collectors;

public class BayesianStatusPredictor extends AbstractStatusPredictor {

    private final JobInfoManager jobInfoManager;
    private final TaskBayesianNetwork taskInference;
    private final List<Vm> availableVms;

    private Set<Integer> runningTasks;

    public BayesianStatusPredictor(List<Vm> availableVms) {
        this.jobInfoManager = JobInfoManager.getInstance();
        this.taskInference = new TaskBayesianNetwork();
        this.availableVms = availableVms;
    }

    @Override
    public List<Double> estimateStatusProb(Cloudlet cloudlet) {
        Double[] probs = this.estimate(cloudlet);
        return Arrays.asList(probs);
    }

    @Override
    public List<Double[]> estimateStatusProb(List<Cloudlet> cloudlets) {
        List<Double[]> probs = new ArrayList<>();
        for (Cloudlet cloudlet : cloudlets) {
            probs.add(this.estimate(cloudlet));
        }
        return probs;
    }

    private Double[] estimate(Cloudlet cloudlet) {
        int cloudletId = Math.toIntExact(cloudlet.getId());
        int jobId = this.jobInfoManager.getJobId(cloudletId);
        List<Integer> cloudletOfJob = this.jobInfoManager.getTaskIds(jobId);
        int n = cloudletOfJob.size();
        int k = 0;
        for (Integer id : cloudletOfJob) {
            if (! this.runningTasks.contains(id))
                k += 1;
        }
        return this.taskInference.inference(n, k);
    }

    @Override
    public void fit() {
        Set<Integer> running = new HashSet<>();
        for (Vm vm : this.availableVms) {
            List<Cloudlet> cloudlets = vm.getCloudletScheduler().getCloudletExecList().stream().map(
                    CloudletExecution::getCloudlet
            ).collect(Collectors.toList());
            cloudlets.forEach(cloudlet -> running.add(Math.toIntExact(cloudlet.getId())));
        }
        this.runningTasks = running;
    }
}
