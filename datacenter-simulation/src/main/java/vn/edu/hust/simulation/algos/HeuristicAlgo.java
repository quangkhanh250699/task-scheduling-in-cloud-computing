package vn.edu.hust.simulation.algos;

import org.cloudbus.cloudsim.distributions.UniformDistr;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudsimplus.heuristics.CloudletToVmMappingHeuristic;
import org.cloudsimplus.heuristics.CloudletToVmMappingSimulatedAnnealing;
import org.cloudsimplus.heuristics.CloudletToVmMappingSolution;

import java.util.List;

public class HeuristicAlgo extends SimpleAlgoAbstract{

    public static final double SA_INITIAL_TEMPERATURE = 1.0;
    public static final double SA_COLD_TEMPERATURE = 0.0001;
    public static final double SA_COOLING_RATE = 0.003;
    public static final int    SA_NUMBER_OF_NEIGHBORHOOD_SEARCHES = 50;

    private CloudletToVmMappingHeuristic heuristic;

    public HeuristicAlgo() {
        super();
    }

    private CloudletToVmMappingHeuristic createSimulatedAnnealingHeuristic() {
        CloudletToVmMappingSimulatedAnnealing heuristic =
            new CloudletToVmMappingSimulatedAnnealing(SA_INITIAL_TEMPERATURE, new UniformDistr(0, 1));
        heuristic.setColdTemperature(SA_COLD_TEMPERATURE);
        heuristic.setCoolingRate(SA_COOLING_RATE);
        heuristic.setNeighborhoodSearchesByIteration(SA_NUMBER_OF_NEIGHBORHOOD_SEARCHES);

        return heuristic;
    }

    @Override
    public void processing() {
        this.heuristic = createSimulatedAnnealingHeuristic();
        this.heuristic.setVmList(this.getVms());
        this.heuristic.setCloudletList(this.comingCloudlets);
        CloudletToVmMappingSolution solution = (CloudletToVmMappingSolution) this.heuristic.solve();
        this.setSolution(new CloudletVmMappingSolution(solution.getResult(), 1));
    }

    @Override
    public void setVms(List<Vm> vms) {
        this.vms = vms;
    }
}
