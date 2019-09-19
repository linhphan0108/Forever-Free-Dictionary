package com.example.foreverfreedictionary.ui.baseMVVM

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.ui.R
import com.example.foreverfreedictionary.ui.dialog.LoadingDialog

abstract class BaseActivity : AppCompatActivity(){
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog.newInstance() }

    fun showLoading(){
        loadingDialog.showLoading(supportFragmentManager)
    }

    fun dismissLoading(){
        loadingDialog.dismissLoading()
    }

    /**
     * Attempt to navigate to the [NavDestination] associated with the given MenuItem. This
     * MenuItem should have been added via one of the helper methods in this class.
     *
     *
     * Importantly, it assumes the [menu item id][MenuItem.getItemId] matches a valid
     * [action id][NavDestination.getAction] or
     * [destination id][NavDestination.getId] to be navigated to.
     *
     *
     * By default, the back stack will be popped back to the navigation graph's start destination.
     * Menu items that have `android:menuCategory="secondary"` will not pop the back
     * stack.
     *
     * @param item The selected MenuItem.
     * @param navController The NavController that hosts the destination.
     * @return True if the [NavController] was able to navigate to the destination
     * associated with the given MenuItem.
     */
    protected fun onNavDestinationSelected(
        item: MenuItem,
        navController: NavController
    ): Boolean {
        val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.nav_default_enter_anim)
            .setExitAnim(R.anim.nav_default_exit_anim)
            .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
        if (item.order and Menu.CATEGORY_SECONDARY == 0) {
            builder.setPopUpTo(findStartDestination(navController.graph).id, false)
        }
        val options = builder.build()
        return try {
            navController.navigate(item.itemId, null, options)
            true
        } catch (e: IllegalArgumentException) {
            false
        }

    }

    /**
     * Finds the actual start destination of the graph, handling cases where the graph's starting
     * destination is itself a NavGraph.
     */
    private /* synthetic access */ fun findStartDestination(graph: NavGraph): NavDestination {
        var startDestination: NavDestination = graph
        while (startDestination is NavGraph) {
            val parent = startDestination
            parent.findNode(parent.startDestination)?.let {
                startDestination = it
            }
        }
        return startDestination
    }
}