package org.cyril.travelmetricssolution.controller;

import com.google.common.primitives.Doubles;
import org.cyril.travelmetricssolution.processing.DegreeCoordinates;
import org.cyril.travelmetricssolution.processing.TravelMetricsComputer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TravelProcessingController {

    private final TravelMetricsComputer computer;

    public TravelProcessingController(TravelMetricsComputer computer) {
        this.computer = computer;
    }

    @PostMapping(value = "/transform_data_to_metrics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TravelMetricsDTO transformRawTravelDataToMetrics(@Valid @NotNull @RequestBody RawTravelDataDTO travelDataDTO) {
        final int numberOfDataPoints = travelDataDTO.numberOfDataPoints();
        final int[] time = travelDataDTO.getCumulativeTime();
        final int duration = time[numberOfDataPoints - 1];

        final double[] cumulativeDistances = computer.computeCumulativeDistances(travelDataDTO.getSpeed(), travelDataDTO.getCumulativeTime());
        final double distance = cumulativeDistances[numberOfDataPoints - 1];
        final double averageSpeed = computer.computeSpeed(cumulativeDistances[numberOfDataPoints - 1], time[numberOfDataPoints - 1]);
        final double asTheCrowFliesDistance = computeAsTheCrowFliesDistance(travelDataDTO);
        final double[] accelerations = computer.computeAccelerations(travelDataDTO.getSpeed(), travelDataDTO.getCumulativeTime());

        return new TravelMetricsDTO(distance, duration, averageSpeed, Doubles.max(accelerations), asTheCrowFliesDistance, accelerations);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    public double computeAsTheCrowFliesDistance(@NonNull RawTravelDataDTO travelDataDTO) {
        final DegreeCoordinates[] coordinates = travelDataDTO.getCoordinates();
        if (coordinates.length < 2) {
            return 0;
        }

        return computer.computeDistanceFromLatitudeAndLongitude(coordinates[0], coordinates[coordinates.length - 1]);
    }

}
