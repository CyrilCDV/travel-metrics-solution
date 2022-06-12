package org.cyril.travelmetricssolution.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.assertj.core.api.Assertions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {
        final JSONObject rawTravelData = new JSONObject();
        rawTravelData.put("startTripDate", "2022-06-12");
        rawTravelData.put("departureCity", "Paris");
        rawTravelData.put("arrivalCity", "Marseille");
        rawTravelData.put("tripData", createTripData(new int[]{0}, new double[]{0}, new double[]{0}, new double[]{0}));

        final JSONObject travelMetrics = new JSONObject();
        travelMetrics.put("distance", 100.);
        travelMetrics.put("duration", 5);
        travelMetrics.put("meanSpeed", 25.);
        travelMetrics.put("accelerationMax", 0.);
        travelMetrics.put("startToStopDistance", 0.);
        travelMetrics.put("accelerations", new JSONArray(new double[] {1.1, 1.2, 1.3}));

        final MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/transform_data_to_metrics").contentType(MediaType.APPLICATION_JSON_VALUE).content(rawTravelData.toString()).accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(builder).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.content().json(travelMetrics.toString(), false));
    }

    private static JSONObject createTripData(int[] time, double[] speed, double[] latitude, double[] longitude) throws JSONException {
        final JSONObject tripData = new JSONObject();
        tripData.put("time", new JSONArray(time));
        tripData.put("speed", new JSONArray(speed));
        tripData.put("latitude", new JSONArray(latitude));
        tripData.put("longitude",new JSONArray(longitude));
        return tripData;
    }

}
