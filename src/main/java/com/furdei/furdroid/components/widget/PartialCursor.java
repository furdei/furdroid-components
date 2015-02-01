package com.furdei.furdroid.components.widget;

import android.database.Cursor;

/**
 * Created by furdey on 14.06.14.
 */
public interface PartialCursor extends Cursor {
    boolean isCursorPartial();
    void setCursorPartial(boolean partial);
}
