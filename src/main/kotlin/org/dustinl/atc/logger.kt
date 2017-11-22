package org.dustinl.atc

import org.slf4j.Logger

inline fun Logger.error(fn: () -> String) {
    if (this.isErrorEnabled) this.error(fn())
}

inline fun Logger.warn(fn: () -> String) {
    if (this.isWarnEnabled) this.warn(fn())
}

inline fun Logger.info(fn: () -> String) {
    if (this.isInfoEnabled) this.info(fn())
}

inline fun Logger.debug(fn: () -> String) {
    if (this.isDebugEnabled) this.debug(fn())
}

inline fun Logger.trace(fn: () -> String) {
    if (this.isTraceEnabled) this.trace(fn())
}
