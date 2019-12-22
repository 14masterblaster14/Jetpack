package com.example.jetpack.game

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.example.jetpack.R
import com.example.jetpack.databinding.FragmentGameWonBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GameWonFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [GameWonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameWonFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_game_won, container, false)
        val binding = DataBindingUtil.inflate<FragmentGameWonBinding>(inflater,R.layout.fragment_game_won,container,false)

        // Add OnClick Handler for Next Match button
        binding.nextMatchButton.setOnClickListener {
            // view?.findNavController()?.navigate(R.id.action_gameWonFragment_to_gameFragment)
            //  Using directions to navigate to the GameFragment
            view?.findNavController()?.navigate(GameWonFragmentDirections.actionGameWonFragmentToGameFragment())
        }

        // Accessing the parameters passed from GameFragment
        var args = GameWonFragmentArgs.fromBundle(arguments!!)
        Toast.makeText(context,"NumCorrect: ${args.numCorrect}, NumQuestions: ${args.numQuestions}",Toast.LENGTH_LONG).show()

        // Setting up the menu
        setHasOptionsMenu(true)

        return binding.root
    }


    // Starting an Activity with our new Intent
    private fun shareSuccess(){
        startActivity(getShareIntent())
    }

    // Creating our Share Intent
    private fun getShareIntent(): Intent {
        val args = GameWonFragmentArgs.fromBundle(arguments!!)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_success_text,args.numCorrect,args.numQuestions))

        return shareIntent
    }


    // Showing the Share Menu Item Dynamically
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.winner_menu,menu)
        // check if the activity resolves
        if (null == getShareIntent().resolveActivity(activity!!.packageManager)){
            // hide the menu item if it doesn't resolve
            menu.findItem(R.id.share).setVisible(false)
        }
    }

    // Sharing from the Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }*/
    }

    override fun onDetach() {
        super.onDetach()
        //listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    /*interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }*/

   }
