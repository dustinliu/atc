package org.dustinl.atc.model

class GeoMap(val width: Int, val height: Int) {
    val landmarkMap = mutableMapOf<String, Landmark>()

    fun addLandmark(landmark: Landmark) {
        landmarkMap[landmark.name] = landmark
    }

    fun addLandmarks(landmarks: List<Landmark>) = landmarks.forEach { landmarkMap[it.name] = it }

    fun getLandmark(name: String) = landmarkMap[name]
}