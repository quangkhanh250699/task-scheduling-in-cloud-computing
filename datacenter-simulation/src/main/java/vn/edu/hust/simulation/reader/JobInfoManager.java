package vn.edu.hust.simulation.reader;

import org.apache.commons.collections4.map.HashedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobInfoManager{

    private static JobInfoManager instance;

    private Map<Integer, Integer> taskIdToJobId;
    private Map<Integer, List<Integer>> jobIdToTaskId;

    private JobInfoManager() {
        taskIdToJobId = new HashedMap<>();
        jobIdToTaskId = new HashedMap<>();
    }

    public static JobInfoManager getInstance() {
        if (instance == null) {
            instance = new JobInfoManager();
        }
        return instance;
    }

    public int getJobId(int taskId) {
        return this.taskIdToJobId.get(taskId);
    }

    public List<Integer> getTaskIds(int jobId) {
        return this.jobIdToTaskId.get(jobId);
    }

    public void addTaskToJob(int taskId, int jobId) {
        if (this.jobIdToTaskId.containsKey(jobId)) {
            this.jobIdToTaskId.get(jobId).add(taskId);
        } else {
            this.jobIdToTaskId.put(jobId, new ArrayList<>());
            this.jobIdToTaskId.get(jobId).add(taskId);
        }
        this.taskIdToJobId.put(taskId, jobId);
    }
}
