package com.example.coroutines

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.coroutines.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


/**

THREAD 1 |---> job_1 (coroutine_1)
|---> job_2 (coroutine_2)
|---> job_3 (coroutine_3)
|---> job_4 (coroutine_3)
|       :
|---> job_n (coroutine_n)

Coroutine works in scope i.e. we need to group the coroutines so that we can handle them in a better way.

Types of Coroutine Scopes:
|--->   GlobalScope : It lives till the application is running.

Dispatchers
|---> Main : For main thread i.e. while interaction with UI
|---> Default : For heavy computational work processing i.e. intensive CPU task
|---> IO : For I/O operations, DB, network
|---> Unconfined : Use it with atmost care, and mostly use for testing

Jobs : We can run/tagged multiple jobs in a particular Coroutine scope.

Coroutine Builders:
|--->   runBlocking : Block current thread to run coroutines. So don't use it.
|--->   launch      : It's basic builder and doesn't block main thread. It needs scope.
|--->   async       : It needs parent coroutine. It doesn't block parent coroutine
but it "await" is called then it blocks.
#   The main difference between launch() and async() is that
the first one doesn’t allow to obtain a value returned by its coroutine block,
while the second one allows the block to return a value that can be used outside the coroutine.

Coroutine Methods:
|--->   withContext

# We can convert the callbacks to coroutine
 **/

class MainActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}