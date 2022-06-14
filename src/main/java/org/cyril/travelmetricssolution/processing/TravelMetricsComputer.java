package org.cyril.travelmetricssolution.processing;

import com.google.common.base.Preconditions;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TravelMetricsComputer {

    private static final double EARTH_RADIUS_IN_METERS = 6371e3;


    public TravelMetricsComputer() {}

    /**
     * Compute distance  between two coordinates
     * <a href="http://www.movable-type.co.uk/scripts/latlong.html">...</a>
     * @param initialCoordinates longitude and latitude coordinates in degrees of initial point
     * @param finalCoordinates longitude and latitude coordinates in degrees of final point
     * @return distance in meters
     */
    public double computeDistanceFromLatitudeAndLongitude(@NonNull DegreeCoordinates initialCoordinates, @NonNull DegreeCoordinates finalCoordinates) {
        final DegreeCoordinates coordinatesDiff = initialCoordinates.minus(finalCoordinates);
        final double a = Math.pow(Math.sin(coordinatesDiff.getLatitudeInRad() / 2), 2) + Math.cos(initialCoordinates.getLatitudeInRad()) * Math.cos(finalCoordinates.getLatitudeInRad()) * Math.pow(Math.sin(coordinatesDiff.getLongitudeInRad() / 2), 2);
        return 2 * EARTH_RADIUS_IN_METERS * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public double[] computeCumulativeDistances(double[] speed, int[] cumulativeTime) {
        Preconditions.checkArgument(speed.length == cumulativeTime.length);

        if (speed.length == 0) {
            return new double[] {};
        }

        final double[] cumulativeDistances = new double[speed.length];
        cumulativeDistances[0] = 0.;
        for (int index = 1; index < speed.length; ++index) {
            final double previousSpeed = speed[index - 1];
            final double nextSpeed = speed[index];
            final int deltaTime = cumulativeTime[index] - cumulativeTime[index - 1];
            cumulativeDistances[index] = cumulativeDistances[index - 1] + computeDistance(previousSpeed, nextSpeed, deltaTime);
        }
        return cumulativeDistances;
    }

    public double[] computeAccelerations(double[] speed, int[] cumulativeTime) {
        Preconditions.checkArgument(speed.length == cumulativeTime.length);

        if (speed.length == 0) {
            return new double[] {};
        }

        final double[] accelerations = new double[speed.length - 1];
        for (int index = 1; index < speed.length; ++index) {
            final double previousSpeed = speed[index - 1];
            final double nextSpeed = speed[index];
            final int deltaTime = cumulativeTime[index] - cumulativeTime[index - 1];
            accelerations[index - 1] = computeAcceleration(previousSpeed, nextSpeed, deltaTime);
        }
        return accelerations;
    }

    /**
     * Compute distance considering a linear speed between initial speed and final speed over deltaTime
     * @param initialSpeed in meter per second
     * @param finalSpeed in meter per second
     * @param deltaTime time interval between initialSpeed and finalSpeed in seconds
     * @return distance in meters
     */
    public double computeDistance(double initialSpeed, double finalSpeed, int deltaTime) {
        return (initialSpeed + finalSpeed) / 2 * deltaTime;
    }

    public double computeSpeed(double distance, double deltaTime) {
        return distance / deltaTime;
    }

    public double computeAcceleration(double initialSpeed, double finalSpeed, double deltaTime) {
        return (finalSpeed - initialSpeed) / deltaTime;
    }
}
