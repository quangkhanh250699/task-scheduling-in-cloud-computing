package vn.edu.hust.simulation.schema;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

public class StatusSchema implements SchemaManager{
    @Override
    public StructType createSchema() {
        StructField queueId = DataTypes.createStructField("queueId", DataTypes.IntegerType, true);
        StructField cloudletId = DataTypes.createStructField("cloudletId", DataTypes.IntegerType, true);
        StructField vmId = DataTypes.createStructField("vmId", DataTypes.IntegerType, true);
        StructField duration = DataTypes.createStructField("duration", DataTypes.DoubleType, true);
        StructField priority = DataTypes.createStructField("priority", DataTypes.IntegerType, true);
        StructField cpuRequest = DataTypes.createStructField("cpuRequest", DataTypes.DoubleType, true);
        StructField MIPS = DataTypes.createStructField("MIPS", DataTypes.DoubleType, true);
        StructField numberRunningCloudlets = DataTypes.createStructField("numberRunningCloudlets", DataTypes.DoubleType, true);
        StructField status = DataTypes.createStructField("status", DataTypes.DoubleType, true);

        List<StructField> fields = new ArrayList<>();
        fields.add(queueId);
        fields.add(cloudletId);
        fields.add(vmId);
        fields.add(duration);
        fields.add(priority);
        fields.add(cpuRequest);
        fields.add(MIPS);
        fields.add(numberRunningCloudlets);
        fields.add(status);

        StructType schema = DataTypes.createStructType(fields);
        return schema;
    }
}
