package org.dustinl.atc.view

import com.almasb.fxgl.app.FXGL
import com.almasb.fxgl.core.logging.Logger
import com.almasb.fxgl.ecs.Entity
import com.almasb.fxgl.entity.Entities
import com.almasb.fxgl.entity.GameEntity
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import org.dustinl.atc.model.Command
import org.dustinl.atc.model.Vehicle
import java.util.*

val logger = Logger.get(MovingEntity::class.java)
class MovingEntity(private val vehicle: Vehicle, private val world: ActGameWorld) : GameEntity() {
    private val trails = Trail(10)
    init {
        setViewFromTexture("aircraft.png")
        position = ScreenCoordinate.fromGeoCoordinate(vehicle.coordinate).toPoint2D()
        rotation = vehicle.heading.toDouble()
        typeComponent.value = EntityType.AIRCRAFT
    }

    fun update() {
        val coordinate = ScreenCoordinate.fromGeoCoordinate(vehicle.coordinate).toPoint2D()
        trails.add(coordinate)
        this.position = coordinate
        this.rotation = vehicle.heading.toDouble()
        logger.debugf("Model: %s, heading %s degree, speed %s kt, altitude %s ft, coordinate: %s",
                vehicle.model.name, vehicle.heading.toDouble(), vehicle.speed, vehicle.altitude, vehicle.coordinate)
    }

    fun command(command: Command) = vehicle.acceptCommand(command)
}

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
