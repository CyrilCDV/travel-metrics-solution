package org.cyril.travelmetricssolution.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cyril.travelmetricssolution.processing.DegreeCoordinates;
import org.joda.time.LocalDate;
import org.springframework.lang.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RawTravelDataDTO {
    @NotNull
    private final LocalDate startTripDate;
    @NotEmpty
    @Size(max = 50, message = "city should have at most 50 characters")
    private final String departureCity;
    @NotEmpty
    @Size(max = 50, message = "city should have at most 50 characters")
    private final String arrivalCity;
    @NotNull
    @RawTravelDataArraysConstraint
    @Valid
    private final RawTravelDataArrays tripData;

    @JsonCreator
    public RawTravelDataDTO(@JsonProperty("startTripDate") @JsonDeserialize(using = LocalDateDeserializer.class) LocalDate startTripDate, @JsonProperty("departureCity") String departureCity, @JsonProperty("arrivalCity") String arrivalCity, @JsonProperty("tripData") RawTravelDataArrays tripData) {
        this.startTripDate = startTripDate;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.tripData = tripData;
    }

    @NonNull
    public LocalDate getStartTripDate() {
        return startTripDate;
    }

    @NonNull
    public String getDepartureCity() {
        return departureCity;
    }

    @NonNull
    public String getArrivalCity() {
        return arrivalCity;
    }

    public int numberOfDataPoints() {
        return tripData.time.length;
    }

    @NonNull
    public int[] getCumulativeTime() {
        return tripData.time;
    }

    @NonNull
    public double[] getSpeed() {
        return tripData.speed;
    }

    @NonNull
    public DegreeCoordinates[] getCoordinates() {
        final DegreeCoordinates[] degreeCoordinates = new DegreeCoordinates[numberOfDataPoints()];
        for (int index = 0; index < numberOfDataPoints(); ++index) {
            degreeCoordinates[index] = new DegreeCoordinates(tripData.latitude[index], tripData.longitude[index]);
        }
        return degreeCoordinates;
    }

    public static class RawTravelDataArrays {
        private final int[] time;
        private final double[] speed;
        private final double[] latitude;
        private final double[] longitude;

        @JsonCreator
        public RawTravelDataArrays(@JsonProperty("time") int[] time, @JsonProperty("speed") double[] speed,
                @JsonProperty("latitude") double[] latitude, @JsonProperty("longitude") double[] longitude) {
            this.time = time;
            this.speed = speed;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public int[] getTime() {
            return time;
        }

        public double[] getSpeed() {
            return speed;
        }

        public double[] getLatitude() {
            return latitude;
        }

        public double[] getLongitude() {
            return longitude;
        }

        public boolean isValid() {
            return time != null && speed != null && latitude != null && longitude != null && time.length == speed.length && time.length == latitude.length && time.length == longitude.length;
        }
    }
}
