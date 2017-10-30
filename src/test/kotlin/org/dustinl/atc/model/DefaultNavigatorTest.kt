package org.dustinl.atc.model

import org.mockito.Mockito.mock
import org.testng.Assert.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.util.concurrent.TimeUnit

class DefaultNavigatorTest {
    @Test
    fun takeCommand() {
        val vehicle = mock(Aircraft::class.java)
        val nav = DefaultNavigator(null, vehicle)

        nav.acceptCommand(Command.Heading(Course(200), null))
        val c = Command.Waypoint(arrayListOf(Landmark("fdf", 1, 1, Landmark.Type.WAYPOINT)))
        nav.acceptCommand(c)

        val command = nav.commands[Command.Waypoint::class.toString()]

        assertEquals(nav.commands.size, 1)
        assertEquals(command, c)
    }


    @DataProvider(name = "heading")
    fun headingData() = arrayOf(
            arrayOf(10, 180, Direction.RIGHT, 13)
//            arrayOf(10, 180, Direction.LEFT, 7),
//            arrayOf(10, 180, null, 13),
//            arrayOf(10, 270, null, 7),
//            arrayOf(2, 270, null, 359)
    )

    @Test(dataProvider = "heading")
    fun adjustHeading(currentHeading: Double, finalHeading: Double, direction: Direction?, result: Double) {
        val aircraft = Aircraft(VehicleModel.BOEING747, Course(currentHeading), 100, 100, GeoCoordinate(0, 0, 0))

        val nav = DefaultNavigator(null, aircraft)
        nav.acceptCommand(Command.Heading(Course(finalHeading), direction))
        val dashboard = nav.compute(1, TimeUnit.SECONDS)
        assertEquals(dashboard.heading, Course(result))
    }
}