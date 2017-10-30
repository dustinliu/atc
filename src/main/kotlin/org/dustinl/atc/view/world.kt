package org.dustinl.atc.view

import com.almasb.fxgl.app.FXGL
import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.ecs.Entity
import javafx.geometry.Point2D
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.paint.Color
import javafx.util.Duration
import org.dustinl.atc.model.ActWorldModel
import org.dustinl.atc.model.GeoCoordinate
import org.dustinl.atc.model.ModelEventListener
import org.dustinl.atc.model.Vehicle

class ActGameWorld(private val app: GameApplication, private val world: ActWorldModel) {
    constructor(): this(FXGL.getApp(), ActWorldModel.createWorld(50, 50)) {
        app.gameScene.contentRoot.background = Background(BackgroundFill(Color.BLACK, null, null))

        world.addListener(object: ModelEventListener {
            override fun vehicleCreated(vehicle: Vehicle) {
                addEntity(MovingEntity(vehicle, this@ActGameWorld))
            }
        })

        app.addExitListener({
            world.shutdown()
        })
        world.start()
    }

    fun run() {
        app.masterTimer.runAtInterval(Runnable {
            app.gameWorld.getEntitiesByType(EntityType.AIRCRAFT).onEach {
                if (it is MovingEntity) {
                    it.update()
                }
            }
        }, Duration.seconds(1.0))
    }

    fun addEntity(entity: Entity) = app.gameWorld.addEntity(entity)
}

data class ScreenCoordinate(val x: Double, val y: Double, val z: Double) {
    fun toPoint2D() = Point2D(this.x, this.y)

    companion object {
        fun fromGeoCoordinate(geoCoordinate: GeoCoordinate): ScreenCoordinate {
            val app = FXGL.getApp()
            val center = Point2D(app.width / 2.0, app.height / 2.0)
            val xScale = (app.width) / ActWorldModel.getWorldModel().width
            val yScale = (app.height) / ActWorldModel.getWorldModel().height
            return ScreenCoordinate(geoCoordinate.x * xScale + center.x, -geoCoordinate.y * yScale + center.y,
                    geoCoordinate.z)
        }
    }
}

enum class EntityType {
    AIRCRAFT,
    AIRFIELD,
    WAYPOINT,
}
