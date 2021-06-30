package vn.edu.hust.simulation.schema;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.apache.spark.sql.types.StructType;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import vn.edu.hust.simulation.mlcenter.SparkEntry;

import java.util.ArrayList;
import java.util.List;

public class SimpleCloudletDataFrame implements DataFrameManager<Cloudlet> {

    @Override
    public Dataset<Row> createDataFrame(List<Cloudlet> cloudlets) {
        StructType schema = SchemaGetter.cloudletSchema().createSchema();
        List<Row> rows = new ArrayList<>();
        cloudlets.forEach(
                cloudlet -> {
                    Row row = new GenericRowWithSchema(
                            new Object[] {
                                    cloudlet.getId(),
                                    0,
                                    cloudlet.getPriority(),
                                    cloudlet.getNumberOfPes(),
                                    cloudlet.getUtilizationOfCpu(),
                                    cloudlet.getUtilizationOfRam(),
                                    cloudlet.getUtilizationOfBw(),
                                    0.0,
                                    0,
                                    0
                            }, schema
                    );
                    rows.add(row);
                }
        );
        Dataset<Row> df = SparkEntry.sparkSession.createDataFrame(rows, schema);
        return df;
    }
}
