package com.furdei.furdroid.components.widget;

import android.database.Cursor;

/**
 * A cursor for data that is not completely loaded into device.
 *
 * @author Stepan Furdey
 */
public interface PartialCursor extends Cursor {
    boolean isCursorPartial();
    void setCursorPartial(boolean partial);
}
