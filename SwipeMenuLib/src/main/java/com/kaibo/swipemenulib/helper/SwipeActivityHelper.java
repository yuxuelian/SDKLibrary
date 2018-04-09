package com.kaibo.swipemenulib.helper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.kaibo.swipemenulib.R;
import com.kaibo.swipemenulib.weight.SwipeMenuLayout;


public class SwipeActivityHelper {

    private Activity mActivity;

    private SwipeMenuLayout mSwipeMenuLayout;

    private View mViewAbove;

    private View mViewBehind;

    private boolean mOnPostCreateCalled = false;

    private boolean mEnableSlideActionBar = false;

    /**
     * Instantiates a new SwipeActivityHelper.
     *
     * @param activity the associated activity
     */
    public SwipeActivityHelper(Activity activity) {
        mActivity = activity;
        mSwipeMenuLayout = (SwipeMenuLayout) mActivity.getLayoutInflater().inflate(R.layout.swipe_menu_main, null);
    }

    /**
     * Further SlidingMenu initialization. Should be called within the activitiy's onPostCreate()
     *
     * @param savedInstanceState the saved instance state (unused)
     */
    public void onPostCreate(Bundle savedInstanceState) {
        if (mViewBehind == null || mViewAbove == null) {
            throw new IllegalStateException("Both setBehindContentView must be called in onCreate in addition to setContentView.");
        }

        mOnPostCreateCalled = true;
        mSwipeMenuLayout.attachToActivity(mActivity, mEnableSlideActionBar ? SwipeMenuLayout.SLIDING_WINDOW : SwipeMenuLayout.SLIDING_CONTENT, true);
        final boolean open;
        final boolean secondary;
        if (savedInstanceState != null) {
            open = savedInstanceState.getBoolean("SwipeActivityHelper.open");
            secondary = savedInstanceState.getBoolean("SwipeActivityHelper.secondary");
        } else {
            open = false;
            secondary = false;
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (open) {
                    if (secondary) {
                        mSwipeMenuLayout.showSecondaryMenu(false);
                    } else {
                        mSwipeMenuLayout.showMenu(false);
                    }
                } else {
                    mSwipeMenuLayout.showContent(false);
                }
            }
        });
    }

    /**
     * Controls whether the ActionBar slides along with the above view when the menu is opened,
     * or if it stays in place.
     *
     * @param slidingActionBarEnabled True if you want the ActionBar to slide along with the SlidingMenu,
     *                                false if you want the ActionBar to stay in place
     */
    public void setSlidingActionBarEnabled(boolean slidingActionBarEnabled) {
        if (mOnPostCreateCalled) {
            throw new IllegalStateException("enableSlidingActionBar must be called in onCreate.");
        }
        mEnableSlideActionBar = slidingActionBarEnabled;
    }

    /**
     * Finds a view that was identified by the id attribute from the XML that was processed in onCreate(Bundle).
     *
     * @param id the resource id of the desired view
     * @return The view if found or null otherwise.
     */
    public <T extends View> T findViewById(int id) {
        T v;
        if (mSwipeMenuLayout != null) {
            v = mSwipeMenuLayout.findViewById(id);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    /**
     * Called to retrieve per-instance state from an activity before being killed so that the state can be
     * restored in onCreate(Bundle) or onRestoreInstanceState(Bundle) (the Bundle populated by this method
     * will be passed to both).
     *
     * @param outState Bundle in which to place your saved state.
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("SwipeActivityHelper.open", mSwipeMenuLayout.isMenuShowing());
        outState.putBoolean("SwipeActivityHelper.secondary", mSwipeMenuLayout.isSecondaryMenuShowing());
    }

    /**
     * Register the above content view.
     *
     * @param view the above content view to register
     */
    public void registerAboveContentView(View view) {
        mViewAbove = view;
    }

    /**
     * Set the behind view content to an explicit view. This view is placed directly into the behind view 's view hierarchy.
     * It can itself be a complex view hierarchy.
     *
     * @param view The desired content to display.
     */
    public void setBehindContentView(View view) {
        mViewBehind = view;
        mSwipeMenuLayout.setMenu(mViewBehind);
    }

    /**
     * Gets the SlidingMenu associated with this activity.
     *
     * @return the SlidingMenu associated with this activity.
     */
    public SwipeMenuLayout getSlidingMenu() {
        return mSwipeMenuLayout;
    }
}
