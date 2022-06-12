package org.cyril.travelmetricssolution.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class TravelProcessingController {

    @PostMapping(value = "/transform_data_to_metrics", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TravelMetricsDTO transformRawTravelDataToMetrics(@RequestBody RawTravelDataDTO travelDataDTO) {
        return new TravelMetricsDTO(100., 5, 25, 0, 0., new double[]{1.1, 1.2, 1.3});
    }

}
