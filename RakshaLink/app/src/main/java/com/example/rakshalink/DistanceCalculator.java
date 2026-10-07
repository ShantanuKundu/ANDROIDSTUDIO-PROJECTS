package com.example.rakshalink;

import android.location.Location;

public class DistanceCalculator {

    /**
     * Mumbai Urban Circuity Factor:
     * In Mumbai, actual driving road distance is on average 1.28x to 1.32x
     * longer than straight line due to railway crossings, creek bridges, and arterial corridors.
     */
    private static final double MUMBAI_ROAD_FACTOR = 1.30;

    /**
     * Calculates calibrated road driving distance in kilometers.
     */
    public static double calculateDistance(double startLat, double startLng, double endLat, double endLng) {
        float[] results = new float[1];

        // Uses Android's high-precision WGS84 ellipsoidal geodesic distance
        Location.distanceBetween(startLat, startLng, endLat, endLng, results);

        double straightLineKm = results[0] / 1000.0;

        // For very close distances (< 1.5 km), detour factor is smaller (~1.15)
        // For medium/long suburban corridors, detour factor is ~1.30
        double factor = (straightLineKm < 1.5) ? 1.15 : MUMBAI_ROAD_FACTOR;
        double roadDistanceKm = straightLineKm * factor;

        // Round to 1 decimal place
        return Math.round(roadDistanceKm * 10.0) / 10.0;
    }
}