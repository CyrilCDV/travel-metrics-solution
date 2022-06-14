package org.cyril.travelmetricssolution.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.assertj.core.api.Assertions;
import org.cyril.travelmetricssolution.RawTravelDataJsonBuilder;
import org.cyril.travelmetricssolution.TravelMetricsBuilder;
import org.cyril.travelmetricssolution.processing.DegreeCoordinates;
import org.cyril.travelmetricssolution.processing.TravelMetricsComputer;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(TravelProcessingController.class)
public class TravelProcessingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TravelMetricsComputer computer;

    @Test
    public void transformDataToMetricsWithValidData() throws Exception {
        // Test case
        final int[] time = new int[]{0, 2, 5, 7, 10};
        final double[] speed = new double[]{2.1, 3.5, 4.1, 3.2, 0.5};
        final double[] latitude = {43.3017349062319, 43.30106636470944, 43.30068102146434, 43.30003352054343,
                43.29943998906283};
        final double[] longitude = {5.379756460478574, 5.379801116306573, 5.380196639354559, 5.380712425815264,
                5.380826222306457};
        final double[] cumulativeDistance = new double[]{0., 5.6, 17, 24.3, 29.85};

        final double distance = 29.85;
        final double duration = time[time.length - 1];
        final double meanSpeed = distance / duration;
        final double accelerationMax = 0.7;
        final double startToStopDistance = 269.42;
        final double[] accelerations = new double[]{0.7, 0.2, -0.45, -0.9};

        // Input
        final RawTravelDataJsonBuilder rawTravelDataJsonBuilder = new RawTravelDataJsonBuilder();
        final JSONObject rawTravelData =
                rawTravelDataJsonBuilder.startTripDate(new LocalDate(2022, 6, 12)).departureCity("Paris").arrivalCity("Marseille").tripData(time, speed, latitude, longitude).build();

        // Result
        final TravelMetricsBuilder travelMetricsBuilder = new TravelMetricsBuilder();
        final JSONObject travelMetrics =
                travelMetricsBuilder.distance(distance).duration(duration).meanSpeed(meanSpeed).accelerationMax(accelerationMax).startToStopDistance(startToStopDistance).accelerations(accelerations).build();

        // Mocking
        Mockito.when(computer.computeCumulativeDistances(Mockito.any(double[].class), Mockito.any(int[].class))).thenReturn(cumulativeDistance);
        Mockito.when(computer.computeSpeed(Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(meanSpeed);
        Mockito.when(computer.computeDistanceFromLatitudeAndLongitude(Mockito.any(DegreeCoordinates.class),
                Mockito.any(DegreeCoordinates.class))).thenReturn(startToStopDistance);
        Mockito.when(computer.computeAccelerations(Mockito.any(double[].class), Mockito.any(int[].class))).thenReturn(accelerations);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/transform_data_to_metrics").contentType(MediaType.APPLICATION_JSON_VALUE).content(rawTravelData.toString()).accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(builder).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.content().json(travelMetrics.toString(), false));
    }

    @Test
    public void transformDataToMetricsWithNoDepartureCity() throws Exception {
        // Test case
        final int[] time = new int[]{0, 2, 5, 7, 10};
        final double[] speed = new double[]{2.1, 3.5, 4.1, 3.2, 0.5};
        final double[] latitude = {43.3017349062319, 43.30106636470944, 43.30068102146434, 43.30003352054343,
                43.29943998906283};
        final double[] longitude = {5.379756460478574, 5.379801116306573, 5.380196639354559, 5.380712425815264,
                5.380826222306457};

        // Input
        final RawTravelDataJsonBuilder rawTravelDataJsonBuilder = new RawTravelDataJsonBuilder();
        final JSONObject rawTravelData =
                rawTravelDataJsonBuilder.startTripDate(new LocalDate(2022, 6, 12)).departureCity("").arrivalCity(
                        "Marseille").tripData(time, speed, latitude, longitude).build();


        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/transform_data_to_metrics").contentType(MediaType.APPLICATION_JSON_VALUE).content(rawTravelData.toString()).accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(builder).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void transformDataToMetricsWithArrivalCityWithNameToLarge() throws Exception {
        // Test case
        final int[] time = new int[]{0, 2, 5, 7, 10};
        final double[] speed = new double[]{2.1, 3.5, 4.1, 3.2, 0.5};
        final double[] latitude = {43.3017349062319, 43.30106636470944, 43.30068102146434, 43.30003352054343,
                43.29943998906283};
        final double[] longitude = {5.379756460478574, 5.379801116306573, 5.380196639354559, 5.380712425815264,
                5.380826222306457};

        // Input
        final RawTravelDataJsonBuilder rawTravelDataJsonBuilder = new RawTravelDataJsonBuilder();
        final JSONObject rawTravelData =
                rawTravelDataJsonBuilder.startTripDate(new LocalDate(2022, 6, 12)).departureCity("Paris").arrivalCity("a".repeat(51)).tripData(time, speed, latitude, longitude).build();

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/transform_data_to_metrics").contentType(MediaType.APPLICATION_JSON_VALUE).content(rawTravelData.toString()).accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(builder).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void transformDataToMetricsWithArrivalCityWithNullArrays() throws Exception {
        // Test case
        final int[] time = new int[]{0, 2, 5, 7, 10};
        final double[] speed = new double[]{2.1, 3.5, 4.1, 3.2, 0.5};
        final double[] latitude = {43.3017349062319, 43.30106636470944, 43.30068102146434, 43.30003352054343,
                43.29943998906283};
        final double[] longitude = {5.379756460478574, 5.379801116306573, 5.380196639354559, 5.380712425815264,
                5.380826222306457};

        // Input
        final RawTravelDataJsonBuilder rawTravelDataJsonBuilder = new RawTravelDataJsonBuilder();
        final JSONObject rawTravelData =
                rawTravelDataJsonBuilder.startTripDate(new LocalDate(2022, 6, 12)).departureCity("Paris").arrivalCity("Marseille").tripData(null, null, null, null).build();

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/transform_data_to_metrics").contentType(MediaType.APPLICATION_JSON_VALUE).content(rawTravelData.toString()).accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(builder).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
