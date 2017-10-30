package org.dustinl.atc.view

import com.almasb.fxgl.entity.GameEntity
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import org.dustinl.atc.model.Vehicle
import java.time.Duration

class MovingEntity(private val model: Vehicle, val screen: ActWorld)
    :GameEntity() {

    init {
        setView(Rectangle(10.0, 10.0, Color.BLUE))
        position = screen.getScreenCoordinate(model.coordinate).toPoint2D()
        typeComponent.value = EntityType.AIRCRAFT
    }

    fun move(duration: Duration) {
        model.move(duration)
        this.position =  screen.getScreenCoordinate(model.coordinate).toPoint2D()
    }
}