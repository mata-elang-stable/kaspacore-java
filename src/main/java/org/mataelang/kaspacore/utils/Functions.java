package org.mataelang.kaspacore.utils;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import scala.jdk.CollectionConverters;

import java.util.List;
import java.util.stream.Collectors;

public class Functions {
    public static Dataset<Row> aggregate(Dataset<Row> rowDataset, List<String> fields, String delayThreshold,
                                         String windowDuration) {

        Dataset<Row> windowedCount = rowDataset
                .withWatermark("seconds", delayThreshold)
                .groupBy(
                        CollectionConverters.IteratorHasAsScala(
                                addGetColumn(
                                        fields,
                                        functions.window(rowDataset.col("seconds"), windowDuration)
                                ).iterator()
                        ).asScala().toSeq()
                ).count();

        return windowedCount.select(
                CollectionConverters.IteratorHasAsScala(addGetColumn(
                        fields,
                        functions.col("window.start").alias("seconds")
                ).iterator()).asScala().toSeq()
        );
    }

    private static List<Column> addGetColumn(List<String> fields, Column newColumn) {
        List<Column> newFields = fields.stream().map(Column::new).collect(Collectors.toList());
        newFields.add(newColumn);

        return newFields;
    }
}
