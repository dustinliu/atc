package org.dustinl.atc.model

import org.testng.Assert.*
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.util.concurrent.TimeUnit

class MockVehicle(model: VehicleModel, heading: Course, speed: Int, altitude: Int,
                  coordinate: GeoCoordinate): Vehicle(model, heading, speed, altitude, coordinate, null) {
    override fun navigate(time: Long, unit: TimeUnit) = Dashboard(heading, speed, altitude)
    override fun acceptCommand(command: Command) {}
}

class VehicleTest {
    @DataProvider(name = "move")
    fun moveData() = arrayOf(
            arrayOf(0, 100, GeoCoordinate(0, 0, 0), GeoCoordinate(0, 100, 0)),
            arrayOf(90, 100, GeoCoordinate(0, 0, 0), GeoCoordinate(100, 0, 0)),
            arrayOf(60, 100, GeoCoordinate(0, 0, 0), GeoCoordinate(86.60, 50.0, 0.0))
    )

    @Test(dataProvider = "move")
    fun move(heading: Int, speed: Int, startCoordindate: GeoCoordinate, finalCoordinate: GeoCoordinate) {
        val vehicle = MockVehicle(VehicleModel.BOEING747, Course(heading), speed, 600, startCoordindate)
        vehicle.move(1, TimeUnit.HOURS)
        assertEquals(vehicle.coordinate, finalCoordinate)
    }
}
