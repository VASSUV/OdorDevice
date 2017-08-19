package ru.vassuv.fl.odordivice.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View

class DividerDecoration(context: Context, resId: Int) : RecyclerView.ItemDecoration() {

    var divider: Drawable = ContextCompat.getDrawable(context, resId)

    var leftMargin = 0
    var rightMargin = 0
    var startItem = 0
    var isTop = false
    var isBottom = false

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val left = parent.paddingLeft + leftMargin
        val right = parent.width - parent.paddingRight - rightMargin
        val childCount = parent.childCount
        val intrinsicHeight = divider.intrinsicHeight

        var child: View
        var params: RecyclerView.LayoutParams
        var top: Int
        var bottom: Int
        var i = startItem

        while (i < childCount - 1) {
            child = parent.getChildAt(i)
            params = child.layoutParams as RecyclerView.LayoutParams
            top = child.bottom + params.bottomMargin
            bottom = top + intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
            i++
        }
        if (isTop) {
            divider.setBounds(0, parent.paddingTop, parent.width, parent.paddingTop + intrinsicHeight)
            divider.draw(c)
        }
        if (isBottom) {
            divider.setBounds(0, parent.height  - parent.paddingBottom - intrinsicHeight, parent.width, parent.height -  parent.paddingBottom)
            divider.draw(c)
        }
    }
}
