package org.cyril.travelmetricssolution.processing;

import org.springframework.lang.NonNull;

public final class DegreeCoordinates {
    private final double latitude;
    private final double longitude;

    public DegreeCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLatitudeInRad() {
        return Math.toRadians(latitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLongitudeInRad() {
        return Math.toRadians(longitude);
    }

    public DegreeCoordinates minus(@NonNull final DegreeCoordinates coordinates) {
        return new DegreeCoordinates(latitude - coordinates.latitude, longitude - coordinates.longitude);
    }
}
