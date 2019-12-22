package com.example.jetpack.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.jetpack.R
import com.example.jetpack.databinding.ActivityGameBinding

/*
Refer :     https://codelabs.developers.google.com/android-kotlin-fundamentals/

            #   Safe Args plugin for passing the data in between fragments
*/

class GameActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_game)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityGameBinding>(this,R.layout.activity_game)

        drawerLayout = binding.drawerLayout

        //Setting up the Up button in AppBar
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
        //Add the drawerLayout as the third parameter to the setupActionBarWithNavController() method for Navigation drawer

        // Setting up navigation drawer
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    //Setting up the Up button in AppBar
    override fun onSupportNavigateUp(): Boolean {
        //return super.onSupportNavigateUp()
        val navController = this.findNavController(R.id.myNavHostFragment)
        // return navController.navigateUp()        //Setting up the Up button in AppBar
        return NavigationUI.navigateUp(navController, drawerLayout) // Setting up Navigation drawer
    }

    // TODO (01) Create the new TitleFragment
    // Select File->New->Fragment->Fragment (Blank)

    // TODO (02) Clean up the new TitleFragment
    // In our new TitleFragment

    // TODO (03) Use DataBindingUtil.inflate to inflate and return the titleFragment in onCreateView
    // In our new TitleFragment
    // R.layout.fragment_title
}
