package com.furdei.furdroid.components.widget;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

/**
 * <p>
 * Adapter for loading large cursors backed by RESTful services with a local cache in a database.
 * This adapter is responsible for displaying "loading" items in the end of already loaded items
 * in case when there is at least one more portion of unfetched data and for calling for that
 * unfetched portion of data either.
 * </p><p>
 * Adapter does NOT provide any functionality for loading data itself, calling supplied listener
 * PartialLoadingListener to start a loading process. loadNextPortionOfData method is called
 * on the background thread by default unless you change this by calling
 * setLoadNextPortionOnBackgroundThread(false).
 * </p><p>
 * Adapter always tries to load one more portion of data while user is looking through the end
 * of the already shown portion. The threshold of "the end" can be configured through the
 * setCursorPositionLoadingThreshold method. The default value is 15, which means that in case
 * when cursor's length is 50 then the loading starts when the user goes further than 50 - 15 = 35
 * records. Note that loading doesn't start immediately. It is required that the threshold is
 * crossed 3 times at least. It prevents us from reacting to "false" views when a ListView
 * triggers adapter's getView method to determine it's length for example.
 * </p>
 *
 * @author Stepan Furdey
 */
public class PartialLoadingAdapter extends BaseAdapter implements Filterable, WrapperListAdapter {

    public interface PartialLoadingListener {
        /**
         * loadNextPortionOfData is always called on the background thread, so you can (but not
         * limited to) start a new loading synchronously.
         */
        void loadNextPortionOfData();
    }

    private enum Status {
        UNDEFINED, LOADING, READY
    }

    private CursorAdapter          cursorAdapter;
    private PartialLoadingListener listener;
    private Context                context;
    private int                    loadingViewResId;
    private View                   loadingView;
    private boolean                isCursorPartial;
    private Status                 status;
    private int                    cursorLength;
    private int                    scrollPositionThreshold;
    private int                    scrollPositionThresholdCrossed;
    private int                    cursorPositionLoadingThreshold;
    private boolean                loadNextPortionOnBackgroundThread;

    public PartialLoadingAdapter(CursorAdapter cursorAdapter, PartialLoadingListener listener, Context context, int loadingViewResId) {
        if (context == null)
            throw new IllegalArgumentException("context == null");

        this.context = context;
        this.loadingViewResId = loadingViewResId;
        init(cursorAdapter, listener);
    }

    public PartialLoadingAdapter(CursorAdapter cursorAdapter, PartialLoadingListener listener, View loadingView) {
        if (loadingView == null)
            throw new IllegalArgumentException("loadingView == null");

        this.loadingView = loadingView;
        init(cursorAdapter, listener);
    }

    public int getCursorPositionLoadingThreshold() {
        return cursorPositionLoadingThreshold;
    }

    public void setCursorPositionLoadingThreshold(int cursorPositionLoadingThreshold) {
        this.cursorPositionLoadingThreshold = cursorPositionLoadingThreshold;

        if (status != Status.UNDEFINED)
            resetScrollThreshold();
    }

    public boolean isLoadNextPortionOnBackgroundThread() {
        return loadNextPortionOnBackgroundThread;
    }

    public void setLoadNextPortionOnBackgroundThread(boolean loadNextPortionOnBackgroundThread) {
        this.loadNextPortionOnBackgroundThread = loadNextPortionOnBackgroundThread;
    }

    public Cursor swapCursor(Cursor newCursor) {
        Cursor oldCursor = cursorAdapter.swapCursor(newCursor);
        initCursor(newCursor);

        // decide when to load the next portion of data
        loadNextPortionOfDataIfNeeded();

        if (newCursor != null)
            notifyDataSetChanged();
        else
            notifyDataSetInvalidated();

        return oldCursor;
    }

    public ListAdapter getWrappedAdapter() {
        return cursorAdapter;
    }

    public int getCount() {
        return cursorAdapter.getCount() + (isCursorPartial ? 1 : 0);
    }

    public Object getItem(int position) {
        return position < cursorAdapter.getCount() ? cursorAdapter.getItem(position) : null;
    }

    public long getItemId(int position) {
        return position < cursorAdapter.getCount() ? cursorAdapter.getItemId(position) : -1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // check position threshold to start loading on scrolling down the list
        if (isCursorPartial && position >= scrollPositionThreshold) {
            ++scrollPositionThresholdCrossed;
            loadNextPortionOfDataIfNeeded();
        }

        if (position < cursorAdapter.getCount())
            return cursorAdapter.getView(position, convertView, parent);

        if (position > cursorAdapter.getCount())
            return null;

        if (!isCursorPartial)
            return null;

        if (loadingView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            loadingView = inflater.inflate(loadingViewResId, parent, false);
        }

        return loadingView;
    }

    public Filter getFilter() {
        return cursorAdapter.getFilter();
    }

    private void init(CursorAdapter cursorAdapter, PartialLoadingListener listener) {
        if (cursorAdapter == null)
            throw new IllegalArgumentException("cursorAdapter == null");

        if (listener == null)
            throw new IllegalArgumentException("listener == null");

        this.cursorAdapter = cursorAdapter;
        this.listener = listener;
        this.status = Status.UNDEFINED;
        this.loadNextPortionOnBackgroundThread = true;
        setCursorPositionLoadingThreshold(CURSOR_POSITION_THRESHOLD);
        initCursor(cursorAdapter.getCursor());
    }

    private static final int CURSOR_POSITION_THRESHOLD               = 15;
    private static final int CURSOR_POSITION_THRESHOLD_CROSSED_LIMIT = 3;

    private void initCursor(Cursor cursor) {
        isCursorPartial = isCursorPartial(cursor);
        cursorLength = cursor != null ? cursor.getCount() : 0;
        status = Status.READY;
        resetScrollThreshold();
    }

    private void resetScrollThreshold() {
        int scrollPositionThreshold = 0;
        if (isCursorPartial) {
            scrollPositionThreshold = cursorLength > getCursorPositionLoadingThreshold() ?
                    cursorLength - getCursorPositionLoadingThreshold() : 0;
        }
        setScrollPositionThreshold(scrollPositionThreshold);
    }

    private void loadNextPortionOfDataIfNeeded() {
        if (isCursorPartial && status == Status.READY) {
            if (cursorLength <= getCursorPositionLoadingThreshold()) {
                loadNextPortionOfData();
            } else {
                if (scrollPositionThresholdCrossed >= CURSOR_POSITION_THRESHOLD_CROSSED_LIMIT) {
                    loadNextPortionOfData();
                }
            }
        }
    }

    private void loadNextPortionOfData() {
        status = Status.LOADING;

        if (isLoadNextPortionOnBackgroundThread()) {
            new Thread(new Runnable() {
                public void run() {
                    listener.loadNextPortionOfData();
                }
            }).start();
        } else {
            listener.loadNextPortionOfData();
        }
    }

    private void setScrollPositionThreshold(int scrollPositionThreshold) {
        this.scrollPositionThreshold = scrollPositionThreshold;
        scrollPositionThresholdCrossed = 0;
    }

    private boolean isCursorPartial(Cursor cursor) {
        return cursor != null && cursor instanceof PartialCursor && ((PartialCursor) cursor).isCursorPartial();
    }

}
