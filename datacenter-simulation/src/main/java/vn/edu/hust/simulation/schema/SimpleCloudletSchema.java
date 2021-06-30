package vn.edu.hust.simulation.schema;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

public class SimpleCloudletSchema implements SchemaManager {

    @Override
    public StructType createSchema() {
        StructField id = DataTypes.createStructField("id", DataTypes.LongType, true);
        StructField comingTime = DataTypes.createStructField("coming_time", DataTypes.IntegerType, true);
        StructField priority = DataTypes.createStructField("priority", DataTypes.IntegerType, true);
        StructField cpuCores = DataTypes.createStructField("cpu_cores", DataTypes.LongType, true);
        StructField cpu = DataTypes.createStructField("cpu", DataTypes.DoubleType, true);
        StructField ram = DataTypes.createStructField("ram", DataTypes.DoubleType, true);
        StructField bandwidth = DataTypes.createStructField("bandwidth", DataTypes.DoubleType, true);
        StructField storage = DataTypes.createStructField("storage", DataTypes.DoubleType, true);
        StructField length = DataTypes.createStructField("length", DataTypes.IntegerType, true);
        StructField kind = DataTypes.createStructField("kind", DataTypes.IntegerType, true);
        List<StructField> fields = new ArrayList<>();
        fields.add(id);
        fields.add(comingTime);
        fields.add(priority);
        fields.add(cpuCores);
        fields.add(cpu);
        fields.add(ram);
        fields.add(bandwidth);
        fields.add(storage);
        fields.add(length);
        fields.add(kind);
        StructType schema = DataTypes.createStructType(fields);
        return schema;
    }
}
