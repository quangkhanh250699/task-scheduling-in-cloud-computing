package vn.edu.hust.simulation.schema;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public interface DataFrameManager<T> {

    Dataset<Row> createDataFrame(List<T> ts);
}
