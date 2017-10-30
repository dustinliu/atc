package org.dustinl.atc.model

data class Landmark(val name: String, val x: Int, val y: Int, val type: Type) {
    enum class Type {
        WAYPOINT,
        AIRFIELD,
    }
}