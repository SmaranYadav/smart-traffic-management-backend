package traffic_management.ml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.Tuple;
import smile.data.formula.Formula;
import smile.data.vector.IntVector;

@Component
public class RandomForestModel {

    private RandomForest model;

    public RandomForestModel() {
        trainModel();
    }

    private void trainModel() {

        try {

            ClassPathResource resource =
                    new ClassPathResource("ml/traffic_training_dataset.csv");

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    resource.getInputStream(),
                                    StandardCharsets.UTF_8
                            )
                    );

            List<double[]> features = new ArrayList<>();
            List<Integer> labels = new ArrayList<>();

            // Skip header
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length < 15) {
                    continue;
                }

                // CSV columns:
                // 10 = vehicle_speed
                // 11 = traffic_density
                // 12 = accident_count
                // 14 = risk_level

                double speed =
                        Double.parseDouble(data[10]);

                double density =
                        Double.parseDouble(data[11]);

                double accidents =
                        Double.parseDouble(data[12]);

                String risk =
                        data[14].trim();

                int label;

                switch (risk) {

                    case "LOW":
                        label = 0;
                        break;

                    case "MEDIUM":
                        label = 1;
                        break;

                    case "HIGH":
                        label = 2;
                        break;

                    default:
                        continue;
                }

                features.add(
                        new double[]{
                                speed,
                                density,
                                accidents
                        }
                );

                labels.add(label);
            }

            reader.close();

            // Convert features to array
            double[][] x =
                    features.toArray(new double[0][]);

            // Convert labels to int array
            int[] y =
                    labels.stream()
                            .mapToInt(Integer::intValue)
                            .toArray();

            System.out.println(
                    "Training Random Forest with "
                            + x.length
                            + " records..."
            );

            // Create Smile DataFrame
            DataFrame dataFrame =
                    DataFrame.of(
                            x,
                            "vehicle_speed",
                            "traffic_density",
                            "accident_count"
                    );

            // Add target column
            dataFrame.add(
                    new IntVector(
                            "risk",
                            y
                    )
            );

            // Train Random Forest
            model =
                    RandomForest.fit(
                            Formula.lhs("risk"),
                            dataFrame
                    );

            System.out.println(
                    "Random Forest trained successfully!"
            );

        } catch (Exception e) {

            System.out.println(
                    "Error training Random Forest: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public String predict(
            double vehicleSpeed,
            double trafficDensity,
            double accidentCount
    ) {

        if (model == null) {
            return "MODEL_NOT_READY";
        }

        double[] input = {
                vehicleSpeed,
                trafficDensity,
                accidentCount
        };

        // Smile 4.3.0 expects a Tuple
        Tuple inputTuple =
                Tuple.of(
                        model.schema(),
                        input
                );

        int prediction =
                model.predict(inputTuple);

        switch (prediction) {

            case 0:
                return "LOW";

            case 1:
                return "MEDIUM";

            case 2:
                return "HIGH";

            default:
                return "UNKNOWN";
        }
    }
}