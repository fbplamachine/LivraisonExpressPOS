package com.israel.livraisonexpresspos.uiComponents;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Israel MEKOMOU [06/07/2020].
 */
public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacingPx;
    private boolean includeEdge;

    public SpacingItemDecoration(int spanCount, int spacingPx, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacingPx = spacingPx;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacingPx - column * spacingPx / spanCount;
            outRect.right = (column + 1) * spacingPx / spanCount;

            if (position < spanCount) { // top edge
                outRect.top = spacingPx;
            }
            outRect.bottom = spacingPx; // item bottom
        } else {
            outRect.left = column * spacingPx / spanCount;
            outRect.right = spacingPx - (column + 1) * spacingPx / spanCount;
            if (position >= spanCount) {
                outRect.top = spacingPx; // item top
            }
        }
    }
}
