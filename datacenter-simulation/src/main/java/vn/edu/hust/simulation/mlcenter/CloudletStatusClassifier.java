package vn.edu.hust.simulation.mlcenter;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import vn.edu.hust.simulation.schema.DataFrameManagerGetter;
import vn.edu.hust.simulation.schema.SchemaGetter;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CloudletStatusClassifier extends AbstractSparkML<Double, Cloudlet> {

    public CloudletStatusClassifier() {
        super();
        this.schemaManager = SchemaGetter.statusSchema();
        this.frameManager = DataFrameManagerGetter.getStatusFrameManager();
        this.MODEL_PATH = "src/main/resources/models/status_predictor";
    }

    @Override
    protected Pipeline createPipeline() {
        VectorAssembler assembler = new VectorAssembler();
        assembler.setInputCols(
                new String[] {"duration", "priority", "cpuRequest", "MIPS", "numberRunningCloudlets"});
        assembler.setOutputCol("feature");

        LogisticRegression lr = new LogisticRegression();
        lr.setFeaturesCol("feature");
        lr.setLabelCol("status");

        Pipeline pipeline = new Pipeline();
        pipeline.setStages(new PipelineStage[] {assembler, lr});

        return pipeline;
    }

    @Override
    public List<Double> predict(List<Cloudlet> cloudlets) {
        Dataset<Row> df = this.frameManager.createDataFrame(cloudlets);
        Dataset<Row> df_transformed = this.transform(df);
//        df_transformed.show();
        Iterator<String> iterator = df_transformed.select("probability").javaRDD().map(
                row -> row.get(0).toString()
        ).toLocalIterator();

        List<Double> t = new ArrayList<>();
        while (iterator.hasNext()) {
            String s = iterator.next();
            s = s.replace("[", "");
            s = s.replace("]", "");
            String[] ss = s.split(",");
            t.add(Double.parseDouble(ss[0]));
        }
        return t;
    }

    @Override
    protected void evaluateModel(Dataset<Row> data) {
        Dataset<Row> data_transformed = this.transform(data);
        JavaPairRDD<Object, Object> predictionAndLabels = data_transformed.select("prediction", "status")
                .javaRDD().mapToPair(
                            row -> new Tuple2<>(row.getDouble(0), row.getDouble(1))
                    );
        MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());
        Matrix confusion = metrics.confusionMatrix();
        System.out.println("Confusion matrix: \n" + confusion);
    }

    public static void main(String[] args) {
        CloudletStatusClassifier clr = new CloudletStatusClassifier();
        clr.train("src/main/resources/learning_data/status_data_v3.csv");
//        List<Cloudlet> cloudlets = DataFrameManagerGetter.getFrameManager().createDataFrame(cl)
//        clr.predict()
    }
}
