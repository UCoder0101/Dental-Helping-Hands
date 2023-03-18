package com.dentalhelpinghands.common

import android.util.Log

class Logger {
    companion object {
        fun d(tag: String?, message: Any) {
            if (Constants.IS_SHOW_LOG) Log.d(tag, message.toString())
        }

        fun i(tag: String?, message: String?) {
            if (Constants.IS_SHOW_LOG) Log.i(tag, message!!)
        }

        fun e(tag: String?, message: Any) {
            if (Constants.IS_SHOW_LOG) {
                Log.e(tag, "" + message.toString())
            }
        }
    }
}