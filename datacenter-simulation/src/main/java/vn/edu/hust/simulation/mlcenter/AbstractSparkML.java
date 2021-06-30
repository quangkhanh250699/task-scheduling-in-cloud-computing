package vn.edu.hust.simulation.mlcenter;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import vn.edu.hust.simulation.schema.DataFrameManager;
import vn.edu.hust.simulation.schema.SchemaManager;

import java.io.IOException;

public abstract class AbstractSparkML<T, U> implements SparkML<T, U> {

    protected String MODEL_PATH;
    protected SchemaManager schemaManager;
    protected DataFrameManager<U> frameManager;
    protected PipelineModel model;

    public AbstractSparkML() {
        SparkEntry.sparkSession = SparkSession.builder().appName("Khanh").master("local").getOrCreate();
        SparkEntry.sparkSession.sparkContext().setLogLevel("ERROR");
    }

    @Override
    public void setup() {
        this.model = PipelineModel.load(this.MODEL_PATH);
    }

    public void train(String dataPath) {

        Dataset<Row>[] data = this.splitData(dataPath);

        Dataset<Row> trainData = data[0];
        Dataset<Row> testData = data[1];


        Pipeline pipeline = this.createPipeline();
        PipelineModel model = pipeline.fit(trainData);

        Dataset<Row> result = model.transform(testData);
        result.show();

        this.model = model;
        this.evaluateModel(testData);

        this.saveModel(model, this.MODEL_PATH);
    }

    protected abstract void evaluateModel(Dataset<Row> data);

    protected Dataset<Row>[] splitData(String dataPath) {
        StructType schema = this.schemaManager.createSchema();
        Dataset<Row> df = SparkEntry.sparkSession.read()
                .option("header", true)
                .schema(schema)
                .csv(dataPath);

        double[] weights = new double[] {0.7, 0.3};
        Dataset<Row>[] data = df.randomSplit(weights);
        return data;
    }

    protected Dataset<Row> transform(Dataset<Row> df) {
        if (this.model == null)
            this.setup();

        return this.model.transform(df);
    }

    abstract protected Pipeline createPipeline();

    protected void saveModel(PipelineModel model, String modelPath) {
        try {
            model.write().overwrite().save(modelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    protected List<Double[]> predictDouble(List<U> us) {
//        Dataset<Row> df = this.frameManager.createDataFrame(us);
//        Dataset<Row> df_transformed = this.transform(df);
//        Iterator<Double[]> iterator = df_transformed.select("prediction", "probability").javaRDD().map(
//                row -> new Double[] {row.getDouble(0)
//        ).toLocalIterator();
//
//        List<Double[]> result = new ArrayList<>();
//        while(iterator.hasNext())
//            result.add(iterator.next());
//        return result;
//    }
}
