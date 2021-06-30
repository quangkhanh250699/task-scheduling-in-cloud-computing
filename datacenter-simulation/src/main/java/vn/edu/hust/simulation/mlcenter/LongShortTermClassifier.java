package vn.edu.hust.simulation.mlcenter;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import vn.edu.hust.simulation.schema.DataFrameManagerGetter;
import vn.edu.hust.simulation.schema.SchemaGetter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LongShortTermClassifier extends AbstractSparkML<Integer, Cloudlet> {

    public LongShortTermClassifier() {
        super();
        this.schemaManager = SchemaGetter.cloudletSchema();
        this.frameManager = DataFrameManagerGetter.getFrameManager();
        this.MODEL_PATH = "src/main/resources/models/lr_model";
    }

    @Override
    protected void evaluateModel(Dataset<Row> data) {

    }

    @Override
    protected Pipeline createPipeline() {
        VectorAssembler assembler = new VectorAssembler();
        assembler.setInputCols(new String[] {"cpu", "ram"});
        assembler.setOutputCol("feature");

        LogisticRegression lr = new LogisticRegression();
        lr.setFeaturesCol("feature");
        lr.setLabelCol("kind");

        Pipeline pipeline = new Pipeline();
        pipeline.setStages(new PipelineStage[]{assembler, lr});

        return pipeline;
    }

    public void test(Dataset<Row> df) {
        this.model.transform(df).show();
    }

    @Override
    public List<Integer> predict(List<Cloudlet> cloudlets) {
        Dataset<Row> df = this.frameManager.createDataFrame(cloudlets);
        Dataset<Row> df_tranformed = this.transform(df);
        Iterator<Integer> iterator = df_tranformed.select("prediction").javaRDD().map(
                                            row -> Math.toIntExact(Math.round(row.getDouble(0)))
                                        ).toLocalIterator();
        List<Integer> result = new ArrayList<>();
        while (iterator.hasNext())
            result.add(iterator.next());
        return result;
    }

    public static void main(String[] args) {
        LongShortTermClassifier classifier = new LongShortTermClassifier();
        CloudletLengthRegressor clr = new CloudletLengthRegressor();
        String dataPath = "src/main/resources/test_data/cloudlets_v3.csv";
        String dataP = "src/main/resources/test_data/cloudlets_v3_short.csv";
        clr.train(dataP);
        classifier.train(dataPath);
    }
}
