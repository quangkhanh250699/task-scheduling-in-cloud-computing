package vn.edu.hust.simulation.mlcenter;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import vn.edu.hust.simulation.schema.DataFrameManagerGetter;
import vn.edu.hust.simulation.schema.SchemaGetter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CloudletLengthRegressor extends AbstractSparkML<Double, Cloudlet>{

    public CloudletLengthRegressor() {
        super();
        this.schemaManager = SchemaGetter.cloudletSchema();
        this.frameManager = DataFrameManagerGetter.getFrameManager();
        this.MODEL_PATH = "src/main/resources/models/length_predictor";
    }

    @Override
    protected void evaluateModel(Dataset<Row> data) {

    }

    @Override
    protected Pipeline createPipeline() {
        VectorAssembler assembler = new VectorAssembler();
        assembler.setInputCols(new String[] {"cpu", "ram"});
        assembler.setOutputCol("feature");

        LinearRegression lr = new LinearRegression();
        lr.setFeaturesCol("feature");
        lr.setLabelCol("length");

        Pipeline pipeline = new Pipeline();
        pipeline.setStages(new PipelineStage[] {assembler, lr});

        return pipeline;
    }

    @Override
    public List<Double> predict(List<Cloudlet> cloudlets) {
        Dataset<Row> df = this.frameManager.createDataFrame(cloudlets);
        Dataset<Row> df_transformed = this.transform(df);
        Iterator<Double> iterator = df_transformed.select("prediction").javaRDD().map(
                row -> row.getDouble(0)
        ).toLocalIterator();

        List<Double> result = new ArrayList<>();
        while(iterator.hasNext())
            result.add(iterator.next());
        return result;
    }

    public static void main(String[] args) {
        CloudletLengthRegressor clr = new CloudletLengthRegressor();
        clr.train("src/main/resources/test_data/cloudlets_v5.csv");
//        List<Cloudlet> cloudlets = DataFrameManagerGetter.getFrameManager().createDataFrame(cl)
//        clr.predict()
    }
}
