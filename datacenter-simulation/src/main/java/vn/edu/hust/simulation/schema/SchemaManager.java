package vn.edu.hust.simulation.schema;

import org.apache.spark.sql.types.StructType;

public interface SchemaManager {

    StructType createSchema();
}
