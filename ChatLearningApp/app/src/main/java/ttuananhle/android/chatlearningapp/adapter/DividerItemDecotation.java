package ttuananhle.android.chatlearningapp.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by leanh on 5/5/2017.
 */

public class DividerItemDecotation extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutCompat.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutCompat.VERTICAL;


    private int paddingLeft = 0;

    private Drawable drawableDivider;
    private int orientation;

    public DividerItemDecotation(Context context, int orientation){
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);

        drawableDivider = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);
    }

    public DividerItemDecotation(Context context, int orientation, int paddingLeft){
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);

        drawableDivider = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);

        this.paddingLeft = paddingLeft;
    }

    public void setOrientation(int orientation){
        if ( orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw  new IllegalArgumentException("invalid orientation");
        }
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (orientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, drawableDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, drawableDivider.getIntrinsicWidth(), 0);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (orientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }


    public void drawVertical(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + drawableDivider.getIntrinsicHeight();
            drawableDivider.setBounds(left + paddingLeft, top, right, bottom);
            drawableDivider.draw(canvas);
        }
    }


    public void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + drawableDivider.getIntrinsicHeight();
            drawableDivider.setBounds(left, top, right, bottom);
            drawableDivider.draw(canvas);
        }
    }

}
