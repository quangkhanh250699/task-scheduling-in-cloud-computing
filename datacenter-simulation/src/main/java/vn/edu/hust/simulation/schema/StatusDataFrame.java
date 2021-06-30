package vn.edu.hust.simulation.schema;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.apache.spark.sql.types.StructType;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import vn.edu.hust.simulation.mlcenter.SparkEntry;

import java.util.ArrayList;
import java.util.List;

public class StatusDataFrame implements DataFrameManager<Cloudlet>{

    private static int queueId = -1;

    @Override
    public Dataset<Row> createDataFrame(List<Cloudlet> cloudlets) {
        StructType schema = SchemaGetter.statusSchema().createSchema();
        List<Row> rows = new ArrayList<>();
        queueId += 1;
        double now = -1;
        if (cloudlets.size() > 0) {
            now = cloudlets.get(0).getSimulation().clock();
        }
        final double time = now;
        cloudlets.forEach(
                cloudlet -> {
                    Row row = new GenericRowWithSchema(
                            new Object[] {
                                    queueId,
                                    Math.toIntExact(cloudlet.getId()),
                                    Math.toIntExact(cloudlet.getVm().getId()),
                                    time - cloudlet.getExecStartTime() + 2,
                                    cloudlet.getPriority(),
                                    cloudlet.getUtilizationOfCpu(),
                                    (double) cloudlet.getVm().getMips(),
                                    (double) cloudlet.getVm().getCloudletScheduler().getCloudletExecList().size(),
                                    (double) -1
                            }, schema
                    );
                    rows.add(row);
                }
        );
        Dataset<Row> df = SparkEntry.sparkSession.createDataFrame(rows, schema);
        return df;
    }
}
