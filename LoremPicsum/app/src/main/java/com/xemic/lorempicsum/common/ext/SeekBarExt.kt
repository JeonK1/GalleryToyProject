package com.xemic.lorempicsum.common.custom

import android.widget.SeekBar

/*** SeekBar의 onStopTrackingTouch 만 사용하는 listener  ***/
inline fun SeekBar.OnStopTrackingTouchListener(crossinline listener: (SeekBar?) -> Unit){
    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
            listener(p0)
        }
    })
}