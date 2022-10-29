package org.mataelang.kaspacore;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.mataelang.kaspacore.models.*;
import org.mataelang.kaspacore.schemas.EventSchema;
import org.mataelang.kaspacore.utils.Functions;
import org.mataelang.kaspacore.utils.PropertyManager;

public class Stream {
    public static void main(String[] args) throws Exception {
        SparkSession sparkSession = SparkSession
                .builder()
                .appName(PropertyManager.getInstance().getProperty("applicationName"))
                .master(PropertyManager.getInstance().getProperty("sparkMaster"))
                .config("spark.sql.session.timeZone", "Asia/Jakarta")
                .getOrCreate();

        Dataset<Row> rowDataset = sparkSession
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", PropertyManager.getInstance().getProperty("inputBootstrapServers"))
                .option("startingOffsets", PropertyManager.getInstance().getProperty("autoOffsetReset"))
                .option("subscribe", PropertyManager.getInstance().getProperty("outputTopic"))
                .load();

        Dataset<Row> valueDF =
                rowDataset.select(
                        functions.from_json(functions.col("value").cast("string"), EventSchema.getSchema()).alias(
                                "parsed_value"),
                        functions.col("timestamp")
                ).select(functions.col("parsed_value.*"), functions.col("timestamp"));

        // Event Example
//        Dataset<Row> aggrEvent = Functions.aggregate(valueDF, new AggrEvent());

        Dataset<Row> aggrSourceIP = Functions.aggregate(valueDF, new AggrSourceIP());
//        Dataset<Row> aggrDestIP = Functions.aggregate(valueDF, new AggrDestIP());
//        Dataset<Row> aggrAlertInfo = Functions.aggregate(valueDF, new AggrAlertInfo());

        aggrSourceIP.writeStream()
                .outputMode("complete")
                .format("console")
                .option("truncate", false)
                .start()
                .awaitTermination();

    }
}
