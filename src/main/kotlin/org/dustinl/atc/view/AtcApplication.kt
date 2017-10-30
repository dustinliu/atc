package org.dustinl.atc.view

import com.almasb.fxgl.app.ApplicationMode
import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.settings.GameSettings
import javafx.scene.Cursor
import javafx.stage.Screen


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
        settings?.applicationMode = ApplicationMode.DEBUG
    }

    override fun initGame() {
        gameScene.root.cursor = Cursor.DEFAULT;
        val world = ActGameWorld().apply { run() }
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
