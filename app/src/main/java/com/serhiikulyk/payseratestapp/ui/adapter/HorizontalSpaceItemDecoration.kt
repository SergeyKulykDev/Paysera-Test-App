package com.serhiikulyk.payseratestapp.ui.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalSpaceItemDecoration(private val horizontalSpaceWidth: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        
        // Add horizontal spacing only to the right of each item
        outRect.right = horizontalSpaceWidth

        // Optionally, you can check if the item is the last one and avoid adding space to it
        val position = parent.getChildAdapterPosition(view)
        if (position == parent.adapter?.itemCount?.minus(1)) {
            outRect.right = 0 // Remove spacing for the last item
        }
    }

}