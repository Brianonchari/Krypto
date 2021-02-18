package com.app.krypto.utils

/**
 *Created by Brian Onchari on 16/01/2021.
 * Make livedata objects emit onetime events
 */
open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    /**Returns the content and prevents is use again**/
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}