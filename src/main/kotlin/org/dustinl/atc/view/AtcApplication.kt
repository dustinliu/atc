package org.dustinl.atc.view

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.settings.GameSettings
import javafx.stage.Screen
import javafx.util.Duration
import org.dustinl.atc.model.GeoCoordinate
import org.dustinl.atc.model.GeoMap
import org.dustinl.atc.model.Vehicle


class AtcApplication : GameApplication() {
    private val version = "0.1"
    private val title = "Air Traffic Control simulator"
    private val windowRatio = 4.0/5.0

    fun start(args: Array<String>) {
        launch(AtcApplication::class.java, *args)
    }

    override fun initSettings(settings: GameSettings?) {
        val screenBounds = Screen.getPrimary().visualBounds
        settings?.width = ((screenBounds.maxX - screenBounds.minX) * windowRatio).toInt()
        settings?.height = ((screenBounds.maxY - screenBounds.minY) * windowRatio).toInt()
        settings?.title = title;
        settings?.version = version
        settings?.isProfilingEnabled = false  // turn off fps
        settings?.isCloseConfirmation = false // turn off exit dialog
        settings?.isIntroEnabled = false      // turn off intro
        settings?.isMenuEnabled = false       // turn off menus
    }

    override fun initGame() {
        val screen = ActWorld(GeoMap(50, 50), this)
        val aircraft = MovingEntity(Vehicle(45, 500, GeoCoordinate(2.0, 2.0, 6000.0)), screen)

        screen.addEntity(aircraft)

        masterTimer.runAtInterval(Runnable {
            aircraft.move(java.time.Duration.ofSeconds(1))
        }, Duration.seconds(2.0))
    }

//    override fun initInput() {
//        val input = input // get input service
//
//        input.addAction(object : UserAction("Move Right") {
//            override fun onAction() {
//                aircraft.translateX(5.0) // move right 5 pixels
//            }
//        }, KeyCode.D)
//    }
}
