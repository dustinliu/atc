package org.dustinl.atc.model

import com.sun.org.apache.xpath.internal.operations.Bool
import org.testng.Assert.*
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class CourseTest {
    @Test
    fun zeroDegree() {
        val course = Course(359) + 1.0
        assertEquals(course.toInt(), 0)
    }

    @DataProvider(name = "plus")
    fun plus() = arrayOf( arrayOf(10, 20, 30), arrayOf(80, 300, 20) )
    @Test(dataProvider = "plus")
    fun plus(left: Double, right: Double, result: Double) {
        val c = Course(left) + right
        assertEquals(c.toInt(), result.toInt())
    }

    @Test(dataProvider = "plus")
    fun plusArgumentAssign(left: Double, right: Double, result: Double) {
        val c = Course(left)
        c += right
        assertEquals(c.toInt(), result.toInt())
    }

    @DataProvider(name = "minus")
    fun minus() = arrayOf( arrayOf(10, 20, 350), arrayOf(100, 20, 80) )
    @Test(dataProvider = "minus")
    fun minus(left: Double, right: Double, result: Double) {
        val c = Course(left) - right
        assertEquals(c.toInt(), result.toInt())
    }

    @Test(dataProvider = "minus")
    fun minusArgumentAssign(left: Double, right: Double, result: Double) {
        val c = Course(left)
        c -= right
        assertEquals(c.toInt(), result.toInt())
    }

    @DataProvider(name = "illegal_value")
    fun illegalValues() = arrayOf( arrayOf(-2), arrayOf(398) )
    @Test(expectedExceptions = arrayOf(IllegalArgumentException::class), dataProvider = "illegal_value")
    fun illegalValue(degree: Int) {
        Course(387)
    }

    @Test
    fun euqal() {
        val a = Course(20)
        val b = Course(20)
        assertEquals(a, b)
        assertFalse(a === b)
    }

    @Test
    fun notEqual() {
        val a = Course(90)
        val b = Course(38)
        assertNotEquals(a, b)
        assertFalse(a === b)
    }

    @DataProvider(name = "difference")
    fun differenceData() = arrayOf(
            arrayOf(Course(20), Course(90), Direction.RIGHT, 70.0),
            arrayOf(Course(20), Course(90), Direction.LEFT, 290.0),
            arrayOf(Course(290), Course(20), Direction.RIGHT, 90.0),
            arrayOf(Course(20), Course(290), Direction.RIGHT, 270.0),
            arrayOf(Course(20), Course(290), Direction.LEFT, 90.0)
    )
    @Test(dataProvider = "difference")
    fun difference(start: Course, end: Course, direction: Direction, result: Double) {
        assertEquals(difference(start, end, direction), result)
    }

    @DataProvider(name = "between")
    fun betweenData() = arrayOf(
            arrayOf(Course(40), Course(20), Course(90), Direction.RIGHT, true),
            arrayOf(Course(100), Course(20), Course(90), Direction.RIGHT, false),
            arrayOf(Course(50), Course(290), Course(90), Direction.RIGHT, true),
            arrayOf(Course(290), Course(20), Course(270), Direction.LEFT, true),
            arrayOf(Course(270), Course(10), Course(290), Direction.LEFT, false)
    )
    @Test(dataProvider = "between")
    fun between(target: Course, start: Course, end: Course, direction: Direction, result: Boolean) {
        assertTrue(target.between(start, end, direction) == result)
    }
}