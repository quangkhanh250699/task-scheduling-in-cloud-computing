package vn.edu.hust.simulation.schema;

public class SchemaGetter {

    private static SchemaManager schemaManager;

    public static SchemaManager cloudletSchema() {
        if (schemaManager == null)
            schemaManager = new SimpleCloudletSchema();
        return schemaManager;
    }

    public static SchemaManager statusSchema() {
        return new StatusSchema();
    }
}
