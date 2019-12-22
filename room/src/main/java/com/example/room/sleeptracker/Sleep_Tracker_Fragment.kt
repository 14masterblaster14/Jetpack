package com.example.room.sleeptracker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.room.R
import com.example.room.database.SleepDatabase
import com.example.room.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 */
class Sleep_Tracker_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_sleep__tracker_, container, false)
        val binding = DataBindingUtil
            .inflate<FragmentSleepTrackerBinding>(inflater,R.layout.fragment_sleep__tracker_,container,false)

        val application = requireNotNull(this.activity).application
        //The requireNotNull Kotlin function throws an IllegalArgumentException if the value is null

        // Create an instance of the ViewModel Factory.
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepTrackerViewModelFactory(dataSource,application)

        val sleepTrackerViewModel = ViewModelProviders
            .of(this,viewModelFactory)
            .get(SleepTrackerViewModel::class.java)


        binding.sleepTrackerViewModel = sleepTrackerViewModel

        val adapter = SleepNightAdapter(SleepNightListener { nightId ->
            //Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
            sleepTrackerViewModel.onSleepNightClicked(nightId)
        })

        binding.sleepList.adapter = adapter

        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })


        //  Specify the current activity as the lifecycle owner of the binding.
        //  This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)


        // We need to get the navController from this, because button is not ready, and it
        // just has to be a view. For some reason, this only matters if we hit stop again
        // after using the back button, not if we hit stop and choose a quality.
        // Also, in the Navigation Editor, for Quality -> Tracker, check "Inclusive" for
        // popping the stack to get the correct behavior if we press stop multiple times
        // followed by back.
        sleepTrackerViewModel.navigateToSleepQuality.observe(this, Observer {
                night ->
            night?.let {
                this.findNavController().navigate(
                    Sleep_Tracker_FragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))

                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                sleepTrackerViewModel.doneNavigating()
            }
        })

        sleepTrackerViewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()

                // Reset state to make sure the snackbar is only shown once, even if the device
                // has a configuration change.
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })

        // Add an Observer on the state variable for Navigating when and item is clicked.
        sleepTrackerViewModel.navigateToSleepDetail.observe(this, Observer { night ->
            night?.let {

                this.findNavController().navigate(
                    Sleep_Tracker_FragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(night))
                sleepTrackerViewModel.onSleepDetailNavigated()
            }
        })

        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =  when (position) {
                0 -> 3
                else -> 1
            }
        }

        binding.sleepList.layoutManager = manager

        return binding.root
    }


}
