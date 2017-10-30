package org.dustinl.atc

import com.almasb.fxgl.app.FXGL
import org.dustinl.atc.view.AtcApplication

fun main(args: Array<String>) {
    FXGL.setProperty("dev.showbbox", false)
    val app = AtcApplication().apply { start(args) }
}

