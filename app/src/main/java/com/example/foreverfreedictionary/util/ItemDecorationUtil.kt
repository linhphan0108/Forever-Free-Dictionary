package com.example.foreverfreedictionary.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.core.content.ContextCompat


class VerticalSpaceItemDecoration(private val verticalSpace: Int = 0,
                                  private val firstItem: Int = 0,
                                  private val lastItem: Int = 0,
                                  dividerDrawableRes: Int? = null,
                                  context: Context? = null)
    : RecyclerView.ItemDecoration() {
    private var dividerDrawable: Drawable? = null
    init {
        if (dividerDrawableRes != null && context != null) {
            setDividerItemDecoration(context, dividerDrawableRes)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        val lastPos = parent.adapter!!.itemCount - 1
        outRect.bottom = verticalSpace
        val position = parent.getChildAdapterPosition(view)
        if (position == 0){
            outRect.top = firstItem
        }else if (position == lastPos){
            outRect.bottom = lastItem
        }
    }

    /**
     * Custom dividerDrawable will be used
     */
    fun setDividerItemDecoration(context: Context, resId: Int) {
        dividerDrawable = ContextCompat.getDrawable(context, resId)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        if (dividerDrawable == null) return

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + dividerDrawable!!.intrinsicHeight

            dividerDrawable!!.setBounds(left, top, right, bottom)
            dividerDrawable!!.draw(c)
        }
    }
}

class VerticalStaggeredSpaceItemDecoration(private val verticalSpace: Int = 0,
                                  private val firstItem: Int = 0,
                                  private val lastItem: Int = 0)
    : RecyclerView.ItemDecoration() {

//    private val divider: Drawable? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        val count = parent.adapter!!.itemCount
        if(count == 0) return
        val lastPos = count - 1
        val lastSecondPos = count - 2
        outRect.bottom = verticalSpace
        val position = parent.getChildAdapterPosition(view)
        //set top's and bottom's space
        if (position == 0 || position == 1) {
            outRect.top = firstItem
        } else if (position == lastPos || position == lastSecondPos) {
            outRect.bottom = lastItem
        }

        //set middle space
        if (position % count == 0 && position != 0){
            outRect.right = verticalSpace /2
        }else{
            outRect.left = verticalSpace /2
        }
    }
}