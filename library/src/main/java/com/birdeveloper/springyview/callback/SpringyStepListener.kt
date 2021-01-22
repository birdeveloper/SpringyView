package com.birdeveloper.springyview.callback

interface SpringyStepListener
{
    fun onOverPulledTop(distance: Float)

    fun onOverPulledBottom(distance: Float)

    fun onRelease()
}