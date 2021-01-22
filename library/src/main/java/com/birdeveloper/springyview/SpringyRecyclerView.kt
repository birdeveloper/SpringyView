package com.birdeveloper.springyview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.EdgeEffect
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.birdeveloper.springyview.callback.SpringyStepAdapter
import com.birdeveloper.springyview.callback.SpringyStepCallBack
import com.birdeveloper.springyview.callback.SpringyStepListener
import com.birdeveloper.springyview.library.R
import com.birdeveloper.springyview.holder.SpringyViewHolder


class SpringyRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs)
{
    private lateinit var callBack: SpringyStepCallBack

    var springyStepListener: SpringyStepListener? = null

    var overscrollAnimationSize = 0.5f

    var flingAnimationSize = 0.5f

    var dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        set(value)
        {
            field = value
            this.spring.spring = SpringForce()
                .setFinalPosition(0f)
                .setDampingRatio(value)
                .setStiffness(stiffness)
        }

    var stiffness = SpringForce.STIFFNESS_LOW
        set(value)
        {
            field = value
            this.spring.spring = SpringForce()
                .setFinalPosition(0f)
                .setDampingRatio(dampingRatio)
                .setStiffness(value)
        }

    var longPressDragEnabled = false
        set(value)
        {
            field = value
            if (adapter is SpringyStepAdapter<*>) callBack.setDragEnabled(value)
        }

    var itemSwipeEnabled = false
        set(value)
        {
            field = value
            if (adapter is SpringyStepAdapter<*>) callBack.setSwipeEnabled(value)
        }

    val spring: SpringAnimation = SpringAnimation(this, SpringAnimation.TRANSLATION_Y)
        .setSpring(
            SpringForce()
                .setFinalPosition(0f)
                .setDampingRatio(dampingRatio)
                .setStiffness(stiffness)
        )


    override fun setAdapter(adapter: RecyclerView.Adapter<*>?)
    {
        super.setAdapter(adapter)
        if (adapter is SpringyStepAdapter<*>)
        {
            callBack = SpringyStepCallBack(adapter, longPressDragEnabled, itemSwipeEnabled)
            val touchHelper = ItemTouchHelper(callBack)
            touchHelper.attachToRecyclerView(this)
        }
    }

    inline fun <reified T : ViewHolder> RecyclerView.forEachVisibleHolder(action: (T) -> Unit)
    {
        for (i in 0 until childCount) action(getChildViewHolder(getChildAt(i)) as T)
    }


    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.SpringyRecyclerView, 0, 0)
            .apply{
                longPressDragEnabled = getBoolean(R.styleable.SpringyRecyclerView_dragAgain, false)
                itemSwipeEnabled = getBoolean(R.styleable.SpringyRecyclerView_swipe, false)
                overscrollAnimationSize = getFloat(R.styleable.SpringyRecyclerView_scrollAnimSize, 0.5f)
                flingAnimationSize = getFloat(R.styleable.SpringyRecyclerView_flingAnimSize, 0.5f)
                when (getInt(R.styleable.SpringyRecyclerView_damping, 0))
                {
                    0 -> dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                    1 -> dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY
                    2 -> dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
                    3 -> dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
                }
                when (getInt(R.styleable.SpringyRecyclerView_stiffness, 1))
                {
                    0 -> stiffness = SpringForce.STIFFNESS_VERY_LOW
                    1 -> stiffness = SpringForce.STIFFNESS_LOW
                    2 -> stiffness = SpringForce.STIFFNESS_MEDIUM
                    3 -> stiffness = SpringForce.STIFFNESS_HIGH
                }
                recycle()
            }


        val rc = this
        this.edgeEffectFactory = object : RecyclerView.EdgeEffectFactory()
        {
            override fun createEdgeEffect(recyclerView: RecyclerView, direction: Int): EdgeEffect
            {
                return object : EdgeEffect(recyclerView.context)
                {
                    override fun onPull(deltaDistance: Float)
                    {
                        super.onPull(deltaDistance)
                        onPullAnimation(deltaDistance)
                    }

                    override fun onPull(deltaDistance: Float, displacement: Float)
                    {
                        super.onPull(deltaDistance, displacement)
                        onPullAnimation(deltaDistance)
                        if (direction == DIRECTION_BOTTOM)
                            springyStepListener?.onOverPulledBottom(deltaDistance)
                        else if (direction == DIRECTION_TOP)
                            springyStepListener?.onOverPulledTop(deltaDistance)
                    }

                    private fun onPullAnimation(deltaDistance: Float)
                    {

                        val delta: Float =
                            if (direction == DIRECTION_BOTTOM)
                                -1 * recyclerView.width * deltaDistance * overscrollAnimationSize
                            else
                                1 * recyclerView.width * deltaDistance * overscrollAnimationSize

                        spring.cancel()
                        rc.translationY += delta

                        forEachVisibleHolder{holder: ViewHolder? -> if (holder is SpringyViewHolder)holder.onPulled(deltaDistance)}
                    }

                    override fun onRelease()
                    {
                        super.onRelease()
                        springyStepListener?.onRelease()
                        spring.start()

                        forEachVisibleHolder{holder: ViewHolder? -> if (holder is SpringyViewHolder)holder.onRelease()}
                    }

                    override fun onAbsorb(velocity: Int)
                    {
                        super.onAbsorb(velocity)

                        val v: Float = if (direction == DIRECTION_BOTTOM)
                            -1 * velocity * flingAnimationSize
                        else
                            1 * velocity * flingAnimationSize


                        spring.setStartVelocity(v).start()

                        forEachVisibleHolder{holder: ViewHolder? -> if (holder is SpringyViewHolder)holder.onAbsorb(velocity)}
                    }

                    override fun draw(canvas: Canvas?): Boolean
                    {
                        setSize(0, 0)
                        return super.draw(canvas)
                    }
                }
            }
        }
    }

    abstract class Adapter<T:ViewHolder>: RecyclerView.Adapter<T>(), SpringyStepAdapter<T>
}
