package com.example.navigations1

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.navigateUp



/**
 * A simple activity demonstrating use of a NavHostFragment with a navigation drawer.

In Kotlin, it's recommended you use one of the following extension functions,
depending on whether you're calling the navigation command from within a fragment, activity or view:

->  Fragment.findNavController()
->  View.findNavController()
->  Activity.findNavController(viewId: Int)

Your NavController is associated with a NavHostFragment. Thus whichever method you use,
you must be sure that the fragment, view, or view ID is either a NavHostFragment itself,
or has a NavHostFragment as a parent. Otherwise you will get an IllegalStateException.

 **/


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        // Set up Action Bar
        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        // TODO STEP 9.5 - Create an AppBarConfiguration with the correct top-level destinations
        /**
         * How to determine top-level destinations

        Destinations reachable via global navigation UI, such as bottom nav or side nav,
        all appear to users as on the same top level of the hierarchy. Therefore,
        they are top level destinations. home_dest and deeplink_dest are in the bottom nav and
        we want the drawer icon to show on both of these destinations, so they are top-level destinations.

        Note that the start destination is always considered a top-level destination.
        If you don't specify a list of top-level destinations, then the only top-level destination
        is your start destination. You can learn more about AppBarConfiguration in the documentation.
         **/

        // You should also remove the old appBarConfiguration setup above
        val drawerLayout : DrawerLayout? = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
               setOf(R.id.home_dest, R.id.deeplink_dest),
               drawerLayout)
        // TODO END STEP 9.5

        setupActionBar(navController, appBarConfiguration)

        setupNavigationMenu(navController)

        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                Integer.toString(destination.id)
            }

            Toast.makeText(
                this@MainActivity, "Navigated to $dest",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("NavigationActivity", "Navigated to $dest")
        }
    }

    private fun setupBottomNavMenu(navController: NavController) {
        // TODO STEP 9.3 - Use NavigationUI to set up Bottom Nav
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
        // TODO END STEP 9.3
    }

    private fun setupNavigationMenu(navController: NavController) {
        // TODO STEP 9.4 - Use NavigationUI to set up a Navigation View
//        // In split screen mode, you can drag this view out from the left
//        // This does NOT modify the actionbar
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)
        // TODO END STEP 9.4
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        // TODO STEP 9.6 - Have NavigationUI handle what your ActionBar displays
//        // This allows NavigationUI to decide what label to show in the action bar
//        // By using appBarConfig, it will also determine whether to
//        // show the up arrow or drawer menu icon
        setupActionBarWithNavController(navController, appBarConfig)
        // TODO END STEP 9.6
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
        if (navigationView == null) {
            menuInflater.inflate(R.menu.overflow_menu, menu)
            return true
        }
        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)
        // TODO STEP 9.2 - Have Navigation UI Handle the item selection - make sure to delete
        //  the old return statement above
        // Have the NavigationUI look for an action or destination matching the menu
        // item id and navigate there if found.
        // Otherwise, bubble up to the parent.
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
        // TODO END STEP 9.2
    }

    // TODO STEP 9.7 - Have NavigationUI handle up behavior in the ActionBar
    override fun onSupportNavigateUp(): Boolean {
//        // Allows NavigationUI to support proper up navigation or the drawer layout
//        // drawer menu, depending on the situation
       /* Note, there are a few different navigateUp methods. Make sure you are using the following
        import from Navigation UI, which accepts the AppBarConfiguration parameter:
        import androidx.navigation.ui.navigateUp*/
        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)
    }
    // TODO END STEP 9.7
}