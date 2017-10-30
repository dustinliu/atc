package org.dustinl.atc.model

import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.math3.util.Precision
import org.dustinl.atc.getLogger
import java.util.concurrent.ExecutorService

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

internal val threadPool: ExecutorService = Executors.newCachedThreadPool()
private val computerInterval: Long = 100

data class Dashboard(val heading: Course, val speed: Double, val altitude: Double)

enum class VehicleModel(val turnRate: Double, val climbRate: Double, val cruiseSpeed: Double, val maxSpeed: Double) {
    BOEING747(3.0, 20.0, 504.0, 600.0)
}

abstract class Vehicle(val model: VehicleModel,
                       heading: Course, speed: Int, altitude: Int,
                       coordinate: GeoCoordinate, val flightPlan: FlightPlan?)
    : Runnable {
    var dashboard = Dashboard(heading, speed.toDouble(), altitude.toDouble()); private set
    private var _run = true

    val heading: Course; get() { return dashboard.heading }
    val speed: Double; get() { return dashboard.speed }
    val altitude: Double; get() { return dashboard.altitude }
    var coordinate = coordinate; private set

    fun start() {
        threadPool.submit(this)
    }

    fun stop() {
        _run = false
    }

    override fun run() {
        var lastExecTime = System.nanoTime()
        while (_run) {
            val elapseTime = System.nanoTime() - lastExecTime
            dashboard = navigate(elapseTime, TimeUnit.NANOSECONDS)
            move(elapseTime, TimeUnit.NANOSECONDS)
            lastExecTime = System.nanoTime()
            TimeUnit.MILLISECONDS.sleep(computerInterval)
        }
    }

    open fun move(time: Long, unit: TimeUnit) {
        dashboard = navigate(time, unit)
        val nanoSpeed: Double = speed / TimeUnit.HOURS.toNanos(1)
        val x = nanoSpeed * unit.toNanos(time) * Math.sin(Math.toRadians(heading.toDouble()))
        val y = nanoSpeed  * unit.toNanos(time) * Math.cos(Math.toRadians(heading.toDouble()))
        coordinate = coordinate.copy(x = coordinate.x + x, y = coordinate.y + y)
    }

    abstract fun navigate(time: Long, unit: TimeUnit): Dashboard
    abstract fun acceptCommand(command: Command)
}

val worldUpdateRate: Long = 1
class AtcWorldModel private constructor(val width: Int, val height: Int): Runnable {
    private val logger = getLogger(this.javaClass)
    private val vehicles = mutableListOf<Vehicle>()
    private val eventListeners = mutableListOf<ModelEventListener>()

    private var _run: Boolean = true
    companion object {
        private var world: AtcWorldModel? = null

        fun createWorld(width: Int, height: Int): AtcWorldModel {
            getLogger(this.javaClass).debug { "world created" }
            world = AtcWorldModel(width, height)
            return world?:throw RuntimeException("create world failed")
        }

        fun getWorldModel(): AtcWorldModel {
            return world?: throw IllegalStateException("no world defined")
        }
    }

    override fun run() {
        val aircraft =Aircraft(VehicleModel.BOEING747, Course(0), 250, 12000, GeoCoordinate(2.0, 2.0, 6000.0), null)
        aircraft.start()
        addVehicle(aircraft)
        while(_run) {
            TimeUnit.SECONDS.sleep(worldUpdateRate)
        }
    }

    fun start() {
        logger.debug { "world started" }
        threadPool.submit(this)
    }

    fun addListener(l: ModelEventListener) = eventListeners.add(l)

    fun shutdown() {
        _run = false
        for (v in vehicles) { v.stop() }
        threadPool.shutdown()
    }

    private fun addVehicle(vehicle: Vehicle) {
        vehicles.add(vehicle)
        for (listener in eventListeners) {
            logger.debug { "vehicle added: ${vehicle}" }
            listener.vehicleCreated(vehicle)
        }
    }
}

interface ModelEventListener {
    fun vehicleCreated(vehicle: Vehicle)
}

val doublePrecision = 0.01
val doubleScale = 2
data class GeoCoordinate(val x: Double, val y: Double, val z: Double) {
    constructor(x: Int, y: Int, z: Int): this(x.toDouble(), y.toDouble(), z.toDouble())

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is GeoCoordinate) return false
        return Precision.equals(x, other.x, doublePrecision) &&
                Precision.equals(y, other.y, doublePrecision) &&
                Precision.equals(z, other.z, doublePrecision)

    }

    override fun hashCode(): Int = HashCodeBuilder().
            append(Precision.round(x, doubleScale)).append(Precision.round(y, doubleScale)).
            append(Precision.round(z, doubleScale)) .build()
}

data class Landmark(val name: String, val x: Int, val y: Int, val type: Type) {
    enum class Type {
        WAYPOINT,
        AIRFIELD,
    }
}
