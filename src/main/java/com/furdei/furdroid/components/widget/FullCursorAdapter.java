package com.furdei.furdroid.components.widget;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

/**
 * An adapter that produces a view for all the rows in a cursor. Unlike the standard Android
 * {@link android.widget.Adapter}, this implementation creates views for every row in a cursor
 * at once. It is helpful when you are sure that there will always be a few of rows and your app
 * will not run out of memory because of creating too many views. Widgets that use FullCursorAdapter
 * do not provide neither scrolling nor view recycling because all views are always visible.
 * However you can include such view into a ScrollView just in case some child views wouldn't fit
 * on some small screens.
 *
 * @author Stepan Furdey
 */
public abstract class FullCursorAdapter {

    private static final String ID_COLUMN = "_id";

    private Context context;
    private Cursor cursor;
    private FullCursorView targetView;

    public FullCursorAdapter(Context context) {
        this.context = context;
    }

    public FullCursorAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    public Cursor swapCursor(Cursor cursor) {
        Cursor oldCursor = this.cursor;
        this.cursor = cursor;
        rebuildView();
        return oldCursor;
    }

    public Cursor getCursor() {
        return cursor;
    }

    private void rebuildView() {
        targetView.removeAllViews();

        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(ID_COLUMN);

            for(boolean cursorRowExists = cursor.moveToFirst(); cursorRowExists; cursorRowExists = cursor.moveToNext()) {
                View view = createView(cursor, targetView.getRoot());

                if (view != null) {
                    bindView(cursor, view);
                    targetView.addView(view, cursor.getPosition(), cursor.getLong(idIndex));
                }
            }
        }

        targetView.onViewRebuilt();
    }

    public FullCursorView getTargetView() {
        return targetView;
    }

    public void setTargetView(FullCursorView targetView) {
        this.targetView = targetView;
    }

    public Context getContext() {
        return context;
    }

    protected abstract View createView(Cursor cursor, ViewGroup root);

    protected abstract void bindView(Cursor cursor, View view);
}
