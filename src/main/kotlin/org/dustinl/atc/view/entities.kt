package org.dustinl.atc.view

import com.almasb.fxgl.app.FXGL
import com.almasb.fxgl.ecs.Entity
import com.almasb.fxgl.entity.Entities
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import java.util.*

open class Entity(val node: Node? = null, val texture: String? = null) {
    fun rotate() {

    }
}

//class MovingEntity(private val vehicle: Vehicle, private val world: AtcGameWorld) {
//    private val trails = Trail(10)
//    private val logger = getLogger(this.javaClass)

//    init {
//        setViewFromTexture("aircraft.png")
//        position = ScreenCoordinate.fromGeoCoordinate(vehicle.coordinate).toPoint2D()
//        rotation = vehicle.heading.toDouble()
//        typeComponent.value = EntityType.AIRCRAFT
//    }

//    fun update() {
//        val coordinate = ScreenCoordinate.fromGeoCoordinate(vehicle.coordinate).toPoint2D()
//        trails.add(coordinate)
//        this.position = coordinate
//        this.rotation = vehicle.heading.toDouble()
//        logger.debug({ "Model: ${vehicle.model.name}, " +
//                "heading ${vehicle.heading} degree, " +
//                "speed ${vehicle.speed} kt, " +
//                "altitude ${vehicle.altitude} ft, " +
//                "coordinate: ${vehicle.coordinate}" })
//    }

//    fun command(command: Command) = vehicle.acceptCommand(command)
//}

class Trail(private val size: Int) {
    private val queue: Queue<Entity> = LinkedList()

    fun add(e: Point2D) {
        if (queue.size == size) {
           queue.poll().also { FXGL.getApp().gameWorld.removeEntities(it) }
        }
        val c = Point2D(e.x - 5, e.y + 10)
        queue.offer(
                Entities.builder().at(c)
                        .viewFromNode(Rectangle(4.0, 4.0, Color.WHITE)).buildAndAttach(FXGL.getApp().gameWorld)
        )
    }
}
