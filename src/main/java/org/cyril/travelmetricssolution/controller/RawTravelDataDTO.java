package org.cyril.travelmetricssolution.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.joda.time.LocalDate;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

public class RawTravelDataDTO {
    @NonNull
    private final LocalDate startTripDate;
    @NonNull
    private final String departureCity;
    @NonNull
    private final String arrivalCity;
    @NonNull
    private final RawTravelDataArrays tripData;

    @JsonCreator
    public RawTravelDataDTO(
            @JsonProperty("startTripDate") @JsonDeserialize(using = LocalDateDeserializer.class) LocalDate startTripDate,
            @JsonProperty("departureCity") String departureCity,
            @JsonProperty("arrivalCity") String arrivalCity,
            @JsonProperty("tripData") RawTravelDataArrays tripData
    ) {
        this.startTripDate = startTripDate;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.tripData = tripData;
    }

    private static class RawTravelDataArrays {
        private final int[] time;
        private final double[] speed;
        private final double[] latitude;
        private final double[] longitude;

        @JsonCreator
        public RawTravelDataArrays(
                @JsonProperty("time") int[] time,
                @JsonProperty("speed") double[] speed,
                @JsonProperty("latitude") double[] latitude,
                @JsonProperty("longitude")  double[] longitude
        ) {
            this.time = time;
            this.speed = speed;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
