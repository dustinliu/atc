package org.dustinl.atc

import com.almasb.fxgl.app.FXGL
import org.dustinl.atc.view.AtcApplication

class AtcMain {
    companion object {
        init {
            FXGL.setProperty("dev.showbbox", false)
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val app = AtcApplication()
            app.start(args)
        }
    }
}