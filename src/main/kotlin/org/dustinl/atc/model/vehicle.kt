package org.dustinl.atc.model

import java.time.Duration
import java.util.concurrent.TimeUnit

open class Vehicle(heading: Int, speed: Int, coordinate: GeoCoordinate) {
    var heading = heading
        private set
    var speed = speed
        private set
    var coordinate = coordinate
        private set
    var orders: List<Order> = listOf(None())

    open fun move(duration: Duration): GeoCoordinate {
        val x = speed * Math.sin(Math.toRadians(heading.toDouble())) * duration.seconds / TimeUnit.HOURS.toSeconds(1)
        val y = speed * Math.cos(Math.toRadians(heading.toDouble())) * duration.seconds / TimeUnit.HOURS.toSeconds(1)
        coordinate = coordinate.copy(x = coordinate.x + x, y = coordinate.y + y)
        return coordinate
    }
}

sealed class Order

data class Turn(val course: Int, val direction: DIRECTION): Order() {
    enum class DIRECTION  {
        LEFT,
        RIGHT
    }
}

data class Descend(val height: Int): Order()

class None(): Order()
