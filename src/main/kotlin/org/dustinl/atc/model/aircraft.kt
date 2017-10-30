package org.dustinl.atc.model

import java.util.concurrent.TimeUnit

class Aircraft(model: VehicleModel, heading: Course, speed: Int, altitude: Int,
               coordinate: GeoCoordinate, flightPlan: FlightPlan? = null)
    : Vehicle(model, heading, speed, altitude, coordinate, flightPlan) {

    private val nav = DefaultNavigator(flightPlan, this)

    override fun navigate(time: Long, unit: TimeUnit) = nav.compute(time, unit)
    override fun acceptCommand(command: Command) = nav.acceptCommand(command)

}



