package com.example.locationalarm.domain.usecase

import android.location.Location
import javax.inject.Inject

class CalculateDistanceUseCase @Inject constructor() {
    /**
     * Calculate the distance between two coordinates in meters
     */
    operator fun invoke(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    /**
     * Check if the current location is within the specified radius of the target
     */
    fun isWithinRadius(
        currentLat: Double,
        currentLon: Double,
        targetLat: Double,
        targetLon: Double,
        radiusMeters: Int
    ): Boolean {
        val distance = invoke(currentLat, currentLon, targetLat, targetLon)
        return distance <= radiusMeters
    }
}
