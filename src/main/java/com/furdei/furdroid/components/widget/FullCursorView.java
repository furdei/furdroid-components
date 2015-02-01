package com.furdei.furdroid.components.widget;

import android.view.View;
import android.view.ViewGroup;

/**
 * A view that displays a full cursor, e.g. all rows in a cursor.
 *
 * @author Stepan Furdey
 */
public interface FullCursorView {

    /**
     * Interface definition for a callback to be invoked when an item in this
     * FullCursorView has been clicked.
     */
    public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this FullCursorView has
         * been clicked.
         *
         * @param parent The FullCursorView where the click happened.
         * @param view The view within the FullCursorView that was clicked (this
         *            will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id The row id of the item that was clicked.
         */
        void onItemClick(FullCursorView parent, View view, int position, long id);
    }

    void removeAllViews();

    ViewGroup getRoot();

    void addView(View view, int position, long id);

    void setAdapter(FullCursorAdapter adapter);

    void setOnItemClickListener(OnItemClickListener listener);

    OnItemClickListener getOnItemClickListener();

    View getEmptyView();

    void setEmptyView(View view);

    void onViewRebuilt();
}
