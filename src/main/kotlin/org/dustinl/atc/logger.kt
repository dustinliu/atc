package org.dustinl.atc

import org.slf4j.LoggerFactory
import org.slf4j.Logger as slf4jLogger

fun getLogger(name: String) = Logger(LoggerFactory.getLogger(name))
fun getLogger(clazz: Class<*>) = Logger(LoggerFactory.getLogger(clazz))

class Logger(val logger: slf4jLogger): slf4jLogger by logger {
    inline fun debug(fn: () -> String) {
        if (logger.isDebugEnabled) logger.debug(fn())
    }

    inline fun info(fn: () -> String) {
        if (logger.isInfoEnabled) logger.info(fn())
    }

    inline fun warn(fn: () -> String) {
        if (logger.isWarnEnabled) logger.warn(fn())
    }

    inline fun trace(fn: () -> String) {
        if (logger.isTraceEnabled) logger.trace(fn())
    }
}

