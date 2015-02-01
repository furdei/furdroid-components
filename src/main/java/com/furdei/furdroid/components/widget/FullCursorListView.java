package com.furdei.furdroid.components.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * A view that displays a full cursor, e.g. all rows in a cursor.
 * This implementation displays all items in a plain list.
 * The list can be either horizontal or vertical. Unlike the standard Android
 * {@link android.widget.ListView ListView}, this implementation creates views for every row in
 * a cursor at once. It is helpful when you are sure that there will always be a few of rows and
 * your app will not run out of memory because of creating too many views.
 *
 * @author Stepan Furdey
 */
public class FullCursorListView extends LinearLayout implements FullCursorView {

    private static final int ITEM_TAG = FullCursorListView.class.getCanonicalName().hashCode();

    private FullCursorAdapter adapter;
    private OnClickListener clickListener;
    private OnItemClickListener itemClickListener;
    private View emptyView;

    private static class ItemTag {
        private int position;
        private long id;
    }

    public FullCursorListView(Context context) {
        super(context);
        init();
    }

    public FullCursorListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FullCursorListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    ItemTag itemTag = (ItemTag) v.getTag(ITEM_TAG);
                    itemClickListener.onItemClick(FullCursorListView.this, v,
                            itemTag.position, itemTag.id);
                }
            }
        };
    }

    @Override
    public ViewGroup getRoot() {
        return this;
    }

    @Override
    public void setAdapter(FullCursorAdapter adapter) {
        this.adapter = adapter;
        adapter.setTargetView(this);
    }

    @Override
    public void addView(View child, int position, long id) {
        super.addView(child);
        ItemTag itemTag = new ItemTag();
        itemTag.position = position;
        itemTag.id = id;
        child.setTag(ITEM_TAG, itemTag);
        child.setOnClickListener(clickListener);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @Override
    public OnItemClickListener getOnItemClickListener() {
        return itemClickListener;
    }

    @Override
    public View getEmptyView() {
        return emptyView;
    }

    @Override
    public void setEmptyView(View view) {
        emptyView = view;
        updateEmptyViewVisibility();
    }

    @Override
    public void onViewRebuilt() {
        updateEmptyViewVisibility();
    }

    private void updateEmptyViewVisibility() {
        if (emptyView != null) {
            emptyView.setVisibility(adapter == null || getChildCount() == 0 ? VISIBLE : GONE);
        }
    }
}
