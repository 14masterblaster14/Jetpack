package com.example.navigations1


import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass.
 */
/**
 * Fragment used to show how to navigate to another destination
 */

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO STEP 5 - Set an OnClickListener, using Navigation.createNavigateOnClickListener()
        /*val button = view.findViewById<Button>(R.id.navigate_destination_button)
        button?.setOnClickListener {
            //findNavController().navigate(R.id.flowStep1Fragment, null)
            Navigation.createNavigateOnClickListener(R.id.flowStep1Fragment, null)
        }*/
        //TODO END STEP 5

        //TODO STEP 6 - Set NavOptions
        /*val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        view.findViewById<Button>(R.id.navigate_destination_button)?.setOnClickListener {
            findNavController().navigate(R.id.flowStep1Fragment, null, options)
        }*/
        //TODO END STEP 6

        //TODO STEP 7.2 - Update the OnClickListener to navigate using an action
        /*view.findViewById<Button>(R.id.navigate_action_button)?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.next_action, null)
        )*/

        // Note the usage of curly braces since we are defining the click listener lambda
        /**
         Note that in your navigation graph XML you can provide a defaultValue for each argument.
        If you do not then you must pass the argument into the action, as shown:
        HomeFragmentDirections.nextAction(flowStepNumberArg)*/

        view.findViewById<Button>(R.id.navigate_action_button)?.setOnClickListener {
            //val flowStepNumberArg = 1
            //val action = HomeFragmentDirections.nextAction(flowStepNumberArg)
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFlowStep1Fragment())
        }

        //TODO END STEP 7.2
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }
}
