package com.adityabugalia.bellproject.ui.main.fragments.support.callbacks

import android.view.MotionEvent

interface MessageFromActivity {

    fun OnDataRecieved(mObject: Any?)
    fun onDispatchTouchEvent(ev: MotionEvent?)
   // fun setOnActivityTouchListener(listener: OnActivityTouchListener?)

}