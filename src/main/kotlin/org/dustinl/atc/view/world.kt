package org.dustinl.atc.view

import com.almasb.fxgl.app.FXGL
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Screen
import javafx.stage.Stage
import org.dustinl.atc.debug
import org.dustinl.atc.model.AtcWorldModel
import org.dustinl.atc.model.GeoCoordinate
import org.dustinl.atc.model.ModelEventListener
import org.dustinl.atc.model.Vehicle
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.HashMap

private const val WINDOW_RATIO = 4.0/5.0

class AtcGameWorld(private val stage: Stage?) {
    private lateinit var model: AtcWorldModel
    private val entities = LinkedList<Entity>()
    private val logger = LoggerFactory.getLogger(this.javaClass)
    val root = Group()

    fun start() {
        model = AtcWorldModel.createWorld(50, 50)
        model.addListener(object: ModelEventListener {
            override fun vehicleCreated(vehicle: Vehicle) {
                logger.debug { "get created event: $vehicle" }
                addEntity(Entity(Rectangle(100.0, 100.0, 10.0, 10.0).apply { fill = Color.WHITE }))
            }
        })
        model.start()

        val screenBounds = Screen.getPrimary().visualBounds
        root.children
        val scene = Scene(root,
                (screenBounds.maxX - screenBounds.minX) * WINDOW_RATIO,
                (screenBounds.maxY - screenBounds.minY) * WINDOW_RATIO
        )
        scene.fill = Color.BLACK
        stage?.scene = scene
        stage?.show()
    }

    fun addEntity(entity: Entity) {
        entities.add(entity)
        root.children.add(entity.node)
    }
}

data class ScreenCoordinate(val x: Double, val y: Double, val z: Double) {
    fun toPoint2D() = Point2D(this.x, this.y)

    companion object {
        fun fromGeoCoordinate(geoCoordinate: GeoCoordinate): ScreenCoordinate {
            val app = FXGL.getApp()
            val center = Point2D(app.width / 2.0, app.height / 2.0)
            val xScale = (app.width) / AtcWorldModel.getWorldModel().width
            val yScale = (app.height) / AtcWorldModel.getWorldModel().height
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
