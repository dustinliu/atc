package org.dustinl.atc.model

import spock.lang.Specification

import java.time.Duration

import static spock.util.matcher.HamcrestSupport.that
import static spock.util.matcher.HamcrestMatchers.closeTo


class VehicleTest extends Specification {

    def 'test move'() {
        setup:
        def vehicle = new Vehicle(heading, speed, new GeoCoordinate(3.0, 4.0, 5.0))

        when:
        def result = vehicle.move(duration)

        then:
        that result.x, closeTo(x, 0.0001)
        that result.y, closeTo(y, 0.0001)
        that result.z, closeTo(z, 0.0001)

        where:
        heading | speed | duration             | x              | y                                | z
        0       | 10    | Duration.ofHours(1)  | 3.0 as Double  | 14.0                             | 5.0
        90      | 10    | Duration.ofHours(1)  | 13.0 as Double | 4.0                              | 5.0
        30      | 10    | Duration.ofHours(10) | 53.0 as Double | 4.0 + 100 * Math.sqrt(3.0) / 2.0 | 5.0

    }
}