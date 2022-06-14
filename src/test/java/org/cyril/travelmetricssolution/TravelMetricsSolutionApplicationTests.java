package org.cyril.travelmetricssolution;

import org.assertj.core.api.Assertions;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TravelMetricsSolutionApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private final static double precision = 1e-14;

    @Test
    public void transformDataToMetricsWithValidData() throws Exception {
        // Test case
        final int[] time = new int[]{0, 2, 5, 7, 10};
        final double[] speed = new double[]{2.1, 3.5, 4.1, 3.2, 0.5};
        final double[] latitude = {43.3017349062319, 43.30106636470944, 43.30068102146434, 43.30003352054343,
                43.29943998906283};
        final double[] longitude = {5.379756460478574, 5.379801116306573, 5.380196639354559, 5.380712425815264,
                5.380826222306457};

        final double distance = 29.85;
        final int duration = time[time.length - 1];
        final double meanSpeed = distance / duration;
        final double accelerationMax = 0.7;
        final double startToStopDistance = 269.4673888312964;
        final double[] accelerations = new double[]{0.7, 0.2, -0.45, -0.9};

        // Input
        final RawTravelDataJsonBuilder rawTravelDataJsonBuilder = new RawTravelDataJsonBuilder();
        final JSONObject rawTravelData =
                rawTravelDataJsonBuilder.startTripDate(new LocalDate(2022, 6, 12)).departureCity("Paris").arrivalCity("Marseille").tripData(time, speed, latitude, longitude).build();

        // Result
        final TravelMetricsBuilder travelMetricsBuilder = new TravelMetricsBuilder();
        final JSONObject travelMetrics =
                travelMetricsBuilder.distance(distance).duration(duration).meanSpeed(meanSpeed).accelerationMax(accelerationMax).startToStopDistance(startToStopDistance).accelerations(accelerations).build();

        // Processing
        final RequestEntity<String> requestEntity = RequestEntity.post("http://localhost:" + port +
                "/transform_data_to_metrics").contentType(MediaType.APPLICATION_JSON).body(rawTravelData.toString());
        final ResponseEntity<String> exchange = restTemplate.exchange(requestEntity, String.class);

        // Check result
        Assertions.assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        final JSONObject jsonObject = new JSONObject(exchange.getBody());
        assertDoubleEquals(jsonObject.getDouble("distance"), distance);
        Assertions.assertThat(jsonObject.getInt("duration")).isEqualTo(duration);
        assertDoubleEquals(jsonObject.getDouble("meanSpeed"), meanSpeed);
        assertDoubleEquals(jsonObject.getDouble("accelerationMax"), accelerationMax);
        assertDoubleEquals(jsonObject.getDouble("startToStopDistance"), startToStopDistance);

        final JSONArray jsonArray = jsonObject.getJSONArray("accelerations");
        Assertions.assertThat(jsonArray.length()).isEqualTo(accelerations.length);
        for (int index = 0; index <accelerations.length; ++index) {
            assertDoubleEquals(jsonArray.getDouble(index), accelerations[index]);
        }
    }


    private static void assertDoubleEquals(double actual, double expected) {
        Assertions.assertThat(actual).isEqualTo(expected, Assertions.within(precision * Math.abs(expected)));
    }

}
