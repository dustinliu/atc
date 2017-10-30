package org.dustinl.atc.view

import javafx.application.Application
import javafx.stage.Stage
import org.dustinl.atc.model.AtcWorldModel

private const val TITLE = "Air Traffic Control simulator"

class AtcApplication: Application() {
    lateinit var world: AtcGameWorld; private set

    companion object {
        lateinit var app: AtcApplication
    }

    override fun start(stage: Stage?) {
        app = this
        stage?.title = TITLE
        world = AtcGameWorld(stage)
        world.start()
    }

    override fun stop() {
        AtcWorldModel.getWorldModel().shutdown()
    }
}
