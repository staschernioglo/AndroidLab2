package com.example.lab2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import static android.graphics.Color.*;

public abstract class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private final Drawable deleteIcon;
    private final int intrinsicWidth;
    private final int intrinsicHeight;
    private final ColorDrawable background;
    private final int backgroundColor;
    private final Paint clearPaint;
    public SwipeToDeleteCallback(Context context){
        super (0,ItemTouchHelper.LEFT);
        deleteIcon= ContextCompat.getDrawable(context,R.drawable.ic_baseline_delete_24);
        intrinsicWidth=deleteIcon.getIntrinsicWidth();
        intrinsicHeight=deleteIcon.getIntrinsicHeight();
        background=new ColorDrawable();
        backgroundColor=parseColor("#f44336");
        clearPaint=new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        return viewHolder.getAdapterPosition() == 10 ? 0 : super.getMovementFlags(recyclerView, viewHolder);
    }
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getBottom() - itemView.getTop();
        boolean isCanceled = dX == 0.0F && !isCurrentlyActive;
        if (isCanceled) {
            this.clearCanvas(c, (float)itemView.getRight() + dX, (float)itemView.getTop(), (float)itemView.getRight(), (float)itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        } else {
            this.background.setColor(this.backgroundColor);
            this.background.setBounds(itemView.getRight() + (int)dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            this.background.draw(c);
            int deleteIconTop = itemView.getTop() + (itemHeight - this.intrinsicHeight) / 2;
            int deleteIconMargin = (itemHeight - this.intrinsicHeight) / 2;
            int deleteIconLeft = itemView.getRight() - deleteIconMargin - this.intrinsicWidth;
            int deleteIconRight = itemView.getRight() - deleteIconMargin;
            int deleteIconBottom = deleteIconTop + this.intrinsicHeight;
            this.deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
            this.deleteIcon.draw(c);
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
    private final void clearCanvas(Canvas c, float left, float top, float right, float bottom) {
        if (c != null) {
            c.drawRect(left, top, right, bottom, this.clearPaint);
        }

    }
}