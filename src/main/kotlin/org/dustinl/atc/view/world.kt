package org.dustinl.atc.view

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.ecs.Entity
import javafx.geometry.Point2D
import org.dustinl.atc.model.GeoCoordinate
import org.dustinl.atc.model.GeoMap

class ActWorld(val map: GeoMap, val app: GameApplication) {

    fun getScreenCoordinate(geoCoordinate: GeoCoordinate): ScreenCoordinate {
        val center = Point2D(app.width/2.0, app.height/2.0)
        val xScale = (app.width) / map.width
        val yScale = (app.height) / map.height
        return ScreenCoordinate(geoCoordinate.x * xScale + center.x, -geoCoordinate.y * yScale + center.y,
                geoCoordinate.z)
    }

    fun addEntity(entity: Entity) = app.gameWorld.addEntity(entity)
}

data class ScreenCoordinate(val x: Double, val y: Double, val z: Double) {
    fun toPoint2D() = Point2D(this.x, this.y)
}

enum class EntityType {
    AIRCRAFT,
    AIRFIELD,
    WAYPOINT
}
