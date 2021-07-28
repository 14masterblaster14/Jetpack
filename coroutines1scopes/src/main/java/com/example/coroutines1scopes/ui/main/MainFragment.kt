package com.example.coroutines1scopes.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.coroutines1scopes.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

/*
        // Way 1 : With View Model KTX library i.e. viewModelScope

        viewModel.getUserData()

        viewModel.users.observe(viewLifecycleOwner, Observer { myUsers ->
            myUsers.forEach{
                Log.i("MasterBlaster","Name is ${it.name} ")

            }
        })

 */
        // Preferred, Efficient Way 2 : With Live KTX library i.e. Live data builder

        viewModel.users.observe(viewLifecycleOwner, Observer { myUsers ->
            myUsers.forEach {
                Log.i("MasterBlaster", "Name is ${it.name} ")

            }
        })

        // With Lifecycle KTX library i.e. lifecycleScope

        lifecycleScope.launch(Dispatchers.IO) {
            Log.i("MasterBlaster", "thread is : ${Thread.currentThread().name}")
        }


    }

}