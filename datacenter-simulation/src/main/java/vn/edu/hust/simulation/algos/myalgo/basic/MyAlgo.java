package vn.edu.hust.simulation.algos.myalgo.basic;

import org.apache.commons.collections4.map.HashedMap;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.core.Simulation;
import org.cloudbus.cloudsim.vms.Vm;
import vn.edu.hust.simulation.algos.AbstractSchedulingAlgorithm;
import vn.edu.hust.simulation.algos.CloudletVmMappingSolution;
import vn.edu.hust.simulation.printer.ShortQueuePrinter;
import vn.edu.hust.simulation.printer.VmTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyAlgo extends AbstractSchedulingAlgorithm {

    private AbstractLongShortTermReviser longShortTermReviser;
    private AbstractLongShortTermInference longShortTermInference;
    private AbstractAvailableResourcesEstimator availableResourcesEstimator;
    private AbstractAvailableResourcesEstimator fakeResourcesEstimator;
    private AbstractSubSchedulingAlgorithm bestFitResources;
    private MinimizeExeTimeAlgo minimizingTime;

    private VmTracker tracker;

    private LongTermQueue longTermQueue;
    private ShortTermQueue shortTermQueue;
    private List<Vm> vms;

    private Simulation simulation;

    CloudletVmMappingSolution solution;

    public MyAlgo(Simulation simulation) {
        this();
        this.simulation = simulation;
    }

    private MyAlgo() {
        longTermQueue = new LongTermQueue();
        shortTermQueue = new ShortTermQueue();
        longShortTermInference = LongShortTermInference.getInstance();
        longShortTermReviser = new LongShorttermReviser();
        availableResourcesEstimator = new AvailableResourcesEstimator(StatusPreditor.getInstance());
        fakeResourcesEstimator = new AvailableResourcesEstimator(FakeStatusPredictor.getInstance());
        bestFitResources = new BestFitResourcesAlgo();
        minimizingTime = new MinimizeExeTimeAlgo();
        tracker = VmTracker.getInstance();
    }

    @Override
    public void setVms(List<Vm> vms) {
        this.vms = vms;
    }

    @Override
    public CloudletVmMappingSolution getSolution() {
        return this.solution;
    }

    protected void processing() {
        this.setRunningStatus(true);

        if (this.comingCloudlets.isEmpty()) {
            this.solution = new CloudletVmMappingSolution(null, 1.0);
            this.setRunningStatus(false);
            return;
        }

        List<List<Cloudlet>> list = this.dispatchToQueue();
        this.longShortTermReviser.update(longTermQueue, shortTermQueue, this.simulation.clock());
        Map<Vm, List<Double>> availableResouces = this.availableResourcesEstimator.estimateAvailableResources(
                longTermQueue, shortTermQueue, vms
        );
        tracker.addEstimate(availableResouces);
        tracker.addNumberRunning(vms);

        // Using to compare estimator's accuracy
        Map<Vm, List<Double>> fakeResouces = this.fakeResourcesEstimator.estimateAvailableResources(
                longTermQueue, shortTermQueue, vms
        );
        tracker.addHardEstimate(fakeResouces);
        // -----------------------------------------------------------

        this.minimizingTime.setComingCloudlets(list.get(0));
        this.minimizingTime.setVms(this.vms);
        this.minimizingTime.setAvailableResources(availableResouces);
        this.minimizingTime.solve();
        Map<Cloudlet, Vm> shortTermResult = this.minimizingTime.getSolution().getResult();

        this.bestFitResources.setComingCloudlets(list.get(1));
        this.bestFitResources.setVms(this.vms);
        this.bestFitResources.updateVmInfo(shortTermResult);
        this.bestFitResources.solve();
        Map<Cloudlet, Vm> longTermResult = this.bestFitResources.getSolution().getResult();

        Map<Cloudlet, Vm> result = new HashedMap<>();
        result.putAll(shortTermResult);
        result.putAll(longTermResult);

        this.solution = new CloudletVmMappingSolution(result, 1.0);

        this.setRunningStatus(false);
    }

    private List<List<Cloudlet>> dispatchToQueue() {
        List<Cloudlet> longCloudlets = new ArrayList<>();
        List<Cloudlet> shortCloudlets = new ArrayList<>();

        List<Integer> longShortTerms = this.longShortTermInference.inference(this.comingCloudlets);

        for (int i = 0; i < this.comingCloudlets.size(); ++i) {
            if (longShortTerms.get(i) == 0)
                shortCloudlets.add(comingCloudlets.get(i));
            else
                longCloudlets.add(comingCloudlets.get(i));
        }

        this.longTermQueue.addCloudlets(longCloudlets);
        this.shortTermQueue.addNextQueue();
        this.shortTermQueue.addCloudlets(shortCloudlets);

        List<List<Cloudlet>> list = new ArrayList<>();
        list.add(shortCloudlets);
        list.add(longCloudlets);
        return list;
    }

    public void printShortQueue(String dataPath) {
        ShortQueuePrinter queuePrinter = new ShortQueuePrinter();
        queuePrinter.print(this.shortTermQueue, dataPath);
    }
}
