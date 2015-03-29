package com.furdei.furdroid.components.graphics.drawable;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.*;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;

import java.util.Arrays;

/**
 * <p>
 * <code>TintedDrawable</code> implements support for color tinting introduced in Android API 21.
 * It takes another drawable as a 'pattern' for tinting and a tint color or
 * a {@link android.content.res.ColorStateList ColorStateList} for state-based tinting.
 * </p><p>
 * You should provide pattern drawable and tint color or {@link android.content.res.ColorStateList
 * ColorStateList} through constructor arguments. You can also customize tint mode by calling a
 * {@link #setTintMode(android.graphics.PorterDuff.Mode)} method. The default mode is the most
 * commonly used {@link android.graphics.PorterDuff.Mode#SRC_IN PorterDuff.Mode.SRC_IN} mode.
 * </p><p>
 * This implementation tints drawable resource by changing a pattern's color filter based on the
 * state of <code>TintedDrawable</code> instance. It stores only one copy of the pattern drawable
 * so does not consume a significant amount of additional memory.
 * </p><p>
 * Feel free to use <code>TintedDrawable</code> for both API 21 and lower. This implementation
 * will use API 21 if running on Lollipop devices or higher and fall back to custom implementation
 * on the lower API versions.
 * </p>
 *
 * @see android.graphics.PorterDuff.Mode
 * @author Stepan Furdey
 */
public class TintedDrawable extends StateListDrawable {

    /**
     * This color means 'no tint', e.g. the source drawable will be shown as is.
     */
    public static final int NO_TINT = 0x00000000;

    private Drawable patternDrawable;
    private ColorStateList tint;
    private PorterDuff.Mode tintMode = DEFAULT_TINT_MODE;
    private ColorFilter cf;

    private final static PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;

    public TintedDrawable(Drawable patternDrawable, ColorStateList colorStateList) {
        this.patternDrawable = patternDrawable;
        setTintList(colorStateList);
    }

    public TintedDrawable(Drawable patternDrawable, int tint) {
        this.patternDrawable = patternDrawable;
        setTint(tint);
    }

    /**
     * Draw in its bounds (set via setBounds) respecting optional effects such
     * as alpha (set via setAlpha) and color filter (set via setColorFilter).
     *
     * @param canvas The canvas to draw into
     */
    @Override
    public void draw(Canvas canvas) {
        System.out.println("TintedDrawable.draw state=" + Arrays.toString(getState()));
        patternDrawable.draw(canvas);
    }

    /**
     * Set a mask of the configuration parameters for which this drawable
     * may change, requiring that it be re-created.
     *
     * @param configs A mask of the changing configuration parameters, as
     *                defined by {@link android.content.pm.ActivityInfo}.
     * @see android.content.pm.ActivityInfo
     */
    @Override
    public void setChangingConfigurations(int configs) {
        patternDrawable.setChangingConfigurations(configs);
        super.setChangingConfigurations(configs);
    }

    /**
     * Set to true to have the drawable dither its colors when drawn to a device
     * with fewer than 8-bits per color component. This can improve the look on
     * those devices, but can also slow down the drawing a little.
     *
     * @param dither
     */
    @Override
    public void setDither(boolean dither) {
        patternDrawable.setDither(dither);
        super.setDither(dither);
    }

    /**
     * Set to true to have the drawable filter its bitmap when scaled or rotated
     * (for drawables that use bitmaps). If the drawable does not use bitmaps,
     * this call is ignored. This can improve the look when scaled or rotated,
     * but also slows down the drawing.
     *
     * @param filter
     */
    @Override
    public void setFilterBitmap(boolean filter) {
        patternDrawable.setFilterBitmap(filter);
        super.setFilterBitmap(filter);
    }

    /**
     * Use the current {@link android.graphics.drawable.Drawable.Callback} implementation to have this Drawable
     * redrawn.  Does nothing if there is no Callback attached to the
     * Drawable.
     *
     * @see android.graphics.drawable.Drawable.Callback#invalidateDrawable
     * @see #getCallback()
     * @see #setCallback(android.graphics.drawable.Drawable.Callback)
     */
    @Override
    public void invalidateSelf() {
        patternDrawable.invalidateSelf();
        super.invalidateSelf();
    }

    /**
     * Use the current {@link android.graphics.drawable.Drawable.Callback} implementation to have this Drawable
     * scheduled.  Does nothing if there is no Callback attached to the
     * Drawable.
     *
     * @param what The action being scheduled.
     * @param when The time (in milliseconds) to run.
     * @see android.graphics.drawable.Drawable.Callback#scheduleDrawable
     */
    @Override
    public void scheduleSelf(Runnable what, long when) {
        patternDrawable.scheduleSelf(what, when);
        super.scheduleSelf(what, when);
    }

    /**
     * Use the current {@link android.graphics.drawable.Drawable.Callback} implementation to have this Drawable
     * unscheduled.  Does nothing if there is no Callback attached to the
     * Drawable.
     *
     * @param what The runnable that you no longer want called.
     * @see android.graphics.drawable.Drawable.Callback#unscheduleDrawable
     */
    @Override
    public void unscheduleSelf(Runnable what) {
        patternDrawable.unscheduleSelf(what);
        super.unscheduleSelf(what);
    }

    /**
     * Specify an alpha value for the drawable. 0 means fully transparent, and
     * 255 means fully opaque.
     *
     * @param alpha
     */
    @Override
    public void setAlpha(int alpha) {
        patternDrawable.setAlpha(alpha);
    }

    /**
     * Gets the current alpha value for the drawable. 0 means fully transparent,
     * 255 means fully opaque. This method is implemented by
     * Drawable subclasses and the value returned is specific to how that class treats alpha.
     * The default return value is 255 if the class does not override this method to return a value
     * specific to its use of alpha.
     */
    @Override
    public int getAlpha() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return patternDrawable.getAlpha();
        }

        return 0xFF;
    }

    /**
     * Specify an optional color filter for the drawable. Pass {@code null} to
     * remove any existing color filter.
     *
     * @param cf the color filter to apply, or {@code null} to remove the
     *           existing color filter
     */
    @Override
    public void setColorFilter(ColorFilter cf) {
        this.cf = cf;
        patternDrawable.setColorFilter(cf);
    }

    /**
     * Specifies a tint for this drawable.
     * <p/>
     * Setting a color filter via {@link #setColorFilter(android.graphics.ColorFilter)} overrides
     * tint.
     *
     * @param tint Color to use for tinting this drawable
     * @see #setTintMode(android.graphics.PorterDuff.Mode)
     */
    @SuppressLint("NewApi")
    @Override
    public void setTint(int tint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            patternDrawable.setTint(tint);
        } else {
            setTintList(ColorStateList.valueOf(tint));
        }
    }

    /**
     * Specifies a tint for this drawable as a color state list.
     * <p/>
     * Setting a color filter via {@link #setColorFilter(android.graphics.ColorFilter)} overrides
     * tint.
     *
     * @param tint Color state list to use for tinting this drawable, or null to
     *             clear the tint
     * @see #setTintMode(android.graphics.PorterDuff.Mode)
     */
    @SuppressLint("NewApi")
    @Override
    public void setTintList(ColorStateList tint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            patternDrawable.setTintList(tint);
        } else {
            this.tint = tint;
            applyTint();
        }
    }

    /**
     * Specifies a tint blending mode for this drawable.
     * <p/>
     * Setting a color filter via {@link #setColorFilter(android.graphics.ColorFilter)} overrides
     * tint.
     *
     * @param tintMode Color state list to use for tinting this drawable, or null to
     *                 clear the tint
     */
    @SuppressLint("NewApi")
    @Override
    public void setTintMode(PorterDuff.Mode tintMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            patternDrawable.setTintMode(tintMode);
        } else {
            this.tintMode = tintMode;
            applyTint();
        }
    }

    /**
     * Returns the current color filter, or {@code null} if none set.
     *
     * @return the current color filter, or {@code null} if none set
     */
    @SuppressLint("NewApi")
    @Override
    public ColorFilter getColorFilter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return patternDrawable.getColorFilter();
        } else {
            return cf;
        }
    }

    /**
     * Specifies the hotspot's location within the drawable.
     *
     * @param x The X coordinate of the center of the hotspot
     * @param y The Y coordinate of the center of the hotspot
     */
    @SuppressLint("NewApi")
    @Override
    public void setHotspot(float x, float y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            patternDrawable.setHotspot(x, y);
            super.setHotspot(x, y);
        }
    }

    /**
     * Sets the bounds to which the hotspot is constrained, if they should be
     * different from the drawable bounds.
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @SuppressLint("NewApi")
    @Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            patternDrawable.setHotspotBounds(left, top, right, bottom);
            super.setHotspotBounds(left, top, right, bottom);
        }
    }

    /**
     * Indicates whether this drawable will change its appearance based on
     * state. Clients can use this to determine whether it is necessary to
     * calculate their state and call setState.
     *
     * @return True if this drawable changes its appearance based on state,
     * false otherwise.
     * @see #setState(int[])
     */
    @Override
    public boolean isStateful() {
        return true;
    }

    /**
     * If this Drawable does transition animations between states, ask that
     * it immediately jump to the current state and skip any active animations.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void jumpToCurrentState() {
        patternDrawable.jumpToCurrentState();
        super.jumpToCurrentState();
    }

    /**
     * Set whether this Drawable is visible.  This generally does not impact
     * the Drawable's behavior, but is a hint that can be used by some
     * Drawables, for example, to decide whether run animations.
     *
     * @param visible Set to true if visible, false if not.
     * @param restart You can supply true here to force the drawable to behave
     *                as if it has just become visible, even if it had last
     *                been set visible.  Used for example to force animations
     *                to restart.
     * @return boolean Returns true if the new visibility is different than
     * its previous state.
     */
    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        boolean patternVisible = patternDrawable.setVisible(visible, restart);
        return super.setVisible(visible, restart) | patternVisible;
    }

    /**
     * Set whether this Drawable is automatically mirrored when its layout direction is RTL
     * (right-to left). See {@link android.util.LayoutDirection}.
     *
     * @param mirrored Set to true if the Drawable should be mirrored, false if not.
     */
    @Override
    public void setAutoMirrored(boolean mirrored) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            patternDrawable.setAutoMirrored(mirrored);
            super.setAutoMirrored(mirrored);
        }
    }

    /**
     * Tells if this Drawable will be automatically mirrored  when its layout direction is RTL
     * right-to-left. See {@link android.util.LayoutDirection}.
     *
     * @return boolean Returns true if this Drawable will be automatically mirrored.
     */
    @Override
    public boolean isAutoMirrored() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            boolean patternMirrored = patternDrawable.isAutoMirrored();
            return super.isAutoMirrored() | patternMirrored;
        }

        return false;
    }

    /**
     * Applies the specified theme to this Drawable and its children.
     *
     * @param t
     */
    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void applyTheme(Resources.Theme t) {
        patternDrawable.applyTheme(t);
        super.applyTheme(t);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean canApplyTheme() {
        return super.canApplyTheme() | patternDrawable.canApplyTheme();
    }

    /**
     * Return the opacity/transparency of this Drawable.  The returned value is
     * one of the abstract format constants in
     * {@link android.graphics.PixelFormat}:
     * {@link android.graphics.PixelFormat#UNKNOWN},
     * {@link android.graphics.PixelFormat#TRANSLUCENT},
     * {@link android.graphics.PixelFormat#TRANSPARENT}, or
     * {@link android.graphics.PixelFormat#OPAQUE}.
     * <p/>
     * <p>Generally a Drawable should be as conservative as possible with the
     * value it returns.  For example, if it contains multiple child drawables
     * and only shows one of them at a time, if only one of the children is
     * TRANSLUCENT and the others are OPAQUE then TRANSLUCENT should be
     * returned.  You can use the method {@link #resolveOpacity} to perform a
     * standard reduction of two opacities to the appropriate single output.
     * <p/>
     * <p>Note that the returned value does <em>not</em> take into account a
     * custom alpha or color filter that has been applied by the client through
     * the {@link #setAlpha} or {@link #setColorFilter} methods.
     *
     * @return int The opacity class of the Drawable.
     * @see android.graphics.PixelFormat
     */
    @Override
    public int getOpacity() {
        return patternDrawable.getOpacity();
    }

    /**
     * Returns a Region representing the part of the Drawable that is completely
     * transparent.  This can be used to perform drawing operations, identifying
     * which parts of the target will not change when rendering the Drawable.
     * The default implementation returns null, indicating no transparent
     * region; subclasses can optionally override this to return an actual
     * Region if they want to supply this optimization information, but it is
     * not required that they do so.
     *
     * @return Returns null if the Drawables has no transparent region to
     * report, else a Region holding the parts of the Drawable's bounds that
     * are transparent.
     */
    @Override
    public Region getTransparentRegion() {
        return patternDrawable.getTransparentRegion();
    }

    /**
     * Override this in your subclass to change appearance if you recognize the
     * specified state.
     *
     * @param state
     * @return Returns true if the state change has caused the appearance of
     * the Drawable to change (that is, it needs to be drawn), else false
     * if it looks the same and there is no need to redraw it since its
     * last state.
     */
    @Override
    protected boolean onStateChange(int[] state) {
        if (patternDrawable == null) {
            return super.onStateChange(state);
        }

        boolean patternState = patternDrawable.setState(state);
        boolean superState;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            superState = super.onStateChange(state);
        } else {
            applyTint();
            super.onStateChange(state);
            superState = isStateful();
        }

        return patternState | superState;
    }

    /**
     * Override this in your subclass to change appearance if you vary based
     * on level.
     *
     * @param level
     * @return Returns true if the level change has caused the appearance of
     * the Drawable to change (that is, it needs to be drawn), else false
     * if it looks the same and there is no need to redraw it since its
     * last level.
     */
    @Override
    protected boolean onLevelChange(int level) {
        boolean patternLevelChange = patternDrawable.setLevel(level);
        return super.onLevelChange(level) | patternLevelChange;
    }

    /**
     * Override this in your subclass to change appearance if you vary based on
     * the bounds.
     *
     * @param bounds
     */
    @Override
    protected void onBoundsChange(Rect bounds) {
        patternDrawable.setBounds(bounds);
        super.onBoundsChange(bounds);
    }

    /**
     * Return the intrinsic width of the underlying drawable object.  Returns
     * -1 if it has no intrinsic width, such as with a solid color.
     */
    @Override
    public int getIntrinsicWidth() {
        return patternDrawable.getIntrinsicWidth();
    }

    /**
     * Return the intrinsic height of the underlying drawable object. Returns
     * -1 if it has no intrinsic height, such as with a solid color.
     */
    @Override
    public int getIntrinsicHeight() {
        return patternDrawable.getIntrinsicHeight();
    }

    /**
     * Returns the minimum width suggested by this Drawable. If a View uses this
     * Drawable as a background, it is suggested that the View use at least this
     * value for its width. (There will be some scenarios where this will not be
     * possible.) This value should INCLUDE any padding.
     *
     * @return The minimum width suggested by this Drawable. If this Drawable
     * doesn't have a suggested minimum width, 0 is returned.
     */
    @Override
    public int getMinimumWidth() {
        return patternDrawable.getMinimumWidth();
    }

    /**
     * Returns the minimum height suggested by this Drawable. If a View uses this
     * Drawable as a background, it is suggested that the View use at least this
     * value for its height. (There will be some scenarios where this will not be
     * possible.) This value should INCLUDE any padding.
     *
     * @return The minimum height suggested by this Drawable. If this Drawable
     * doesn't have a suggested minimum height, 0 is returned.
     */
    @Override
    public int getMinimumHeight() {
        return patternDrawable.getMinimumHeight();
    }

    /**
     * Return in padding the insets suggested by this Drawable for placing
     * content inside the drawable's bounds. Positive values move toward the
     * center of the Drawable (set Rect.inset).
     *
     * @param padding
     * @return true if this drawable actually has a padding, else false. When false is returned,
     * the padding is always set to 0.
     */
    @Override
    public boolean getPadding(Rect padding) {
        return patternDrawable.getPadding(padding);
    }

    /**
     * Called to get the drawable to populate the Outline that defines its drawing area.
     * <p/>
     * This method is called by the default {@link android.view.ViewOutlineProvider} to define
     * the outline of the View.
     * <p/>
     * The default behavior defines the outline to be the bounding rectangle of 0 alpha.
     * Subclasses that wish to convey a different shape or alpha value must override this method.
     *
     * @param outline
     * @see android.view.View#setOutlineProvider(android.view.ViewOutlineProvider)
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void getOutline(Outline outline) {
        patternDrawable.getOutline(outline);
    }

    /**
     * Make this drawable mutable. This operation cannot be reversed. A mutable
     * drawable is guaranteed to not share its state with any other drawable.
     * This is especially useful when you need to modify properties of drawables
     * loaded from resources. By default, all drawables instances loaded from
     * the same resource share a common state; if you modify the state of one
     * instance, all the other instances will receive the same modification.
     * <p/>
     * Calling this method on a mutable Drawable will have no effect.
     *
     * @return This drawable.
     * @see android.graphics.drawable.Drawable.ConstantState
     * @see #getConstantState()
     */
    @Override
    public Drawable mutate() {
        return patternDrawable.mutate();
    }

    /**
     * Return a {@link android.graphics.drawable.Drawable.ConstantState} instance that holds the shared state of this Drawable.
     *
     * @return The ConstantState associated to that Drawable.
     * @see android.graphics.drawable.Drawable.ConstantState
     * @see android.graphics.drawable.Drawable#mutate()
     */
    @Override
    public ConstantState getConstantState() {
        return patternDrawable.getConstantState();
    }

    /**
     * Returns a pattern drawable
     */
    public Drawable getPatternDrawable() {
        return patternDrawable;
    }

    /**
     * Compute tint color and apply it to the drawable.
     * Called only when current API level is less than {@link android.os.Build.VERSION_CODES#LOLLIPOP}
     */
    private void applyTint() {
        mutate();
        int tintColor = tint.getColorForState(getState(), tint.getDefaultColor());
        if (tintColor != NO_TINT) {
            setColorFilter(tintColor, tintMode);
        } else {
            setColorFilter(null);
        }
        invalidateSelf();
    }

}
