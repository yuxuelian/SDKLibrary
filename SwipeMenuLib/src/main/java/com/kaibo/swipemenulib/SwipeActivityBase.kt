package com.kaibo.swipemenulib

import android.view.View
import android.view.ViewGroup.LayoutParams

interface SwipeActivityBase {

    /**
     * Set the behind view content to an explicit view. This view is placed directly into the behind view 's view hierarchy.
     * It can itself be a complex view hierarchy.
     *
     * @param view         The desired content to display.
     * @param layoutParams Layout parameters for the view.
     */
    fun setBehindContentView(view: View, layoutParams: LayoutParams)

    /**
     * Set the behind view content to an explicit view. This view is placed directly into the behind view 's view hierarchy.
     * It can itself be a complex view hierarchy. When calling this method, the layout parameters of the specified
     * view are ignored. Both the width and the height of the view are set by default to MATCH_PARENT. To use your
     * own layout parameters, invoke setContentView(android.view.View, android.view.ViewGroup.LayoutParams) instead.
     *
     * @param view The desired content to display.
     */
    fun setBehindContentView(view: View)

    /**
     * Set the behind view content from a layout resource. The resource will be inflated, adding all top-level views
     * to the behind view.
     *
     * @param layoutResID Resource ID to be inflated.
     */
    fun setBehindContentView(layoutResID: Int)

    /**
     * Toggle the SlidingMenu. If it is open, it will be closed, and vice versa.
     */
    fun toggle()

    /**
     * Close the SlidingMenu and show the content view.
     */
    fun showContent()

    /**
     * Open the SlidingMenu and show the menu view.
     */
    fun showMenu()

    /**
     * Open the SlidingMenu and show the secondary (right) menu view. Will default to the regular menu
     * if there is only one.
     */
    fun showSecondaryMenu()

    /**
     * Controls whether the ActionBar slides along with the above view when the menu is opened,
     * or if it stays in place.
     *
     * @param slidingActionBarEnabled True if you want the ActionBar to slide along with the SlidingMenu,
     * false if you want the ActionBar to stay in place
     */
    fun setSlidingActionBarEnabled(slidingActionBarEnabled: Boolean)

}
