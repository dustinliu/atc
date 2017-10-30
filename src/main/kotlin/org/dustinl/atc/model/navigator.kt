package org.dustinl.atc.model

import java.util.concurrent.TimeUnit

interface Navigator {
    fun acceptCommand(command: Command)
    fun compute(time: Long, unit: TimeUnit): Dashboard
}

data class FlightPlan(val speed: Int, val height: Int, val waypoints: List<Landmark>)

sealed class Command {
    data class Heading(val course: Course, val direction: Direction? = null) : Command()
    data class Altitude(val altitude: Int) : Command()
    data class Waypoint(val waypoints: List<Landmark>) : Command()
    class Plan : Command()
}

enum class Direction { LEFT, RIGHT }

data class Course(private var h: Double = 0.0) {
    constructor(h: Int): this(h.toDouble())

    init {
        require(h >= 0 && h < 360) { "Course must between 0 and 360" }
    }

    operator fun plus(b: Double) = Course(normalize(this.h + b))
    operator fun plus(b: Course) = Course(normalize(this.h + b.h))

    operator fun plusAssign(b: Double) {
        this.h = normalize( this.h + b)
    }

    operator fun minus(b: Double) = Course(normalize(this.h - b))
    operator fun minus(b: Course) = Course(normalize(this.h - b.h))

    operator fun minusAssign(b: Double) {
        this.h = normalize(this.h - b)
    }

    operator fun compareTo(b: Double)  = this.h.compareTo(b)
    operator fun compareTo(b: Course)  = this.h.compareTo(b.h)

    private fun normalize(v: Double): Double {
        var result = v
        while(result > 360) { result -= 360 }
        while (result < 0) { result += 360 }
        return if(result == 360.0) 0.0 else result
    }


    fun between(start: Course, end: Course, direction: Direction): Boolean =
        difference(start, end, direction) > difference(this, end, direction)

    fun toInt() = h.toInt()
    fun toDouble() = h
}

fun difference(one: Course, two: Course, direction: Direction): Double {
    val start: Double
    val end: Double
    if (direction == Direction.LEFT) {
        start = two.toDouble()
        end = one.toDouble()
    } else {
        start = one.toDouble()
        end = two.toDouble()
    }

    return if (end < start) {
        360 - Math.abs(end - start)
    } else {
       Math.abs(end - start)
    }
}

internal fun turn(course: Course, degree: Double, direction: Direction) = when(direction) {
    Direction.LEFT -> course - degree
    Direction.RIGHT -> course + degree
}

class DefaultNavigator(private var flightPlan: FlightPlan?,
                       private val vehicle: Vehicle)
    : Navigator {
    val commands: MutableMap<String, Command> = mutableMapOf()

    override fun acceptCommand(command: Command) {
        if (command is Command.Heading) {
            commands.remove(Command.Waypoint::class.toString())
        }

        if (command is Command.Waypoint) {
            commands.remove(Command.Heading::class.toString())
        }
        commands[command.javaClass.toString()] = command
    }

    override fun compute(time: Long, unit: TimeUnit) = when {
        !commands.isEmpty() -> executeCommand(time, unit)
        flightPlan != null -> executeFlightPlan(time, unit)
        else -> vehicle.dashboard
    }

    private fun executeFlightPlan(time: Long, unit: TimeUnit): Dashboard {
        TODO("not implemented")
    }

    private fun executeCommand(time: Long, unit: TimeUnit): Dashboard {
        var dashboard = Dashboard(vehicle.heading, vehicle.speed, vehicle.altitude)
        for ((_, command) in commands) {
            when(command) {
                is Command.Heading -> dashboard = adjustHeading(time, unit, command)
            }
        }
        return dashboard
    }

    private fun adjustHeading(time: Long, unit: TimeUnit, command: Command.Heading): Dashboard {
        val difference = command.course.toDouble() - vehicle.heading.toDouble()
        val direction = when {
            difference > 0.0 -> command.direction?: if (difference > 180.0) Direction.LEFT else Direction.RIGHT
            difference < 0.0 -> command.direction?: if (difference < -180.0) Direction.RIGHT else Direction.LEFT
            else -> Direction.RIGHT
        }

        val turningRate = vehicle.model.turnRate / TimeUnit.SECONDS.toNanos(1)
        var newHeading = turn(vehicle.heading, turningRate * unit.toNanos(time), direction)
        newHeading = if (command.course.between(vehicle.heading, newHeading, direction)) {
            command.course
        } else {
            newHeading
        }

        return vehicle.dashboard.copy(heading = newHeading)
    }
}
