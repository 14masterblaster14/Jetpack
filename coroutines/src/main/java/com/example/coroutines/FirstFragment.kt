package com.example.coroutines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.coroutines.databinding.FragmentFirstBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

/**

THREAD 1 |---> job_1 (coroutine_1)
|---> job_2 (coroutine_2)
|---> job_3 (coroutine_3)
|---> job_4 (coroutine_3)
|       :
|---> job_n (coroutine_n)

Coroutine works in scope i.e. we need to group the coroutines so that we can handle them in a better way and avoid leaks.

Types of Coroutine Scopes:
|---> GlobalScope : It lives till the application is running and rarely used in android.
|---> viewModelScope : Its a coroutine scope tied to a ViewModel so that when ViewModel destroyed then it will cancel
all the coroutines/Job running within this scope.
|---> lifecycleScope : Its a coroutine scope tied to the Activity/Fragment lifecycle so that when corresponding activity/ fragment
gets destroyed then it will cancel all the coroutines/Job running within this scope.

Dispatchers (Context)
|---> Main : For main thread / UI thread i.e. while interaction with UI
|---> Default : For heavy computational work processing i.e. intensive CPU task sorting large data, parsing large json
|---> IO : For background thread i.e. I/O operations, DB, network, file operations
|---> Unconfined : runs in current thread but if it is suspended and resumed it will run on suspending function's thread.
Use it with at most care, and mostly use for testing

Note : Best practice to start coroutines on main thread and switch to other thread

Jobs : We can run/tagged multiple jobs in a particular Coroutine scope.

Coroutine Builders:
|--->   launch      : It's basic builder and doesn't block current thread. It needs scope.
It returns instance of job,which can be used as a ref to the coroutine.
We use this for coroutines that doesn't have any result as the return value.

|--->   async       : It launches new coroutine w/o blocking current thread.
It returns instance of Deferred<T> and need to invoke await() to get the value.
It needs parent coroutine. It doesn't block parent coroutine. But if "await" is called then it blocks.
We use this for coroutines that does have any result as the return value.

#   The main difference between launch() and async() is that
the first one doesn’t allow to obtain a value returned by its coroutine block,
while the second one allows the block to return a value that can be used outside the coroutine.

|--->   Produce :   It's for coroutines which produce a stream of elements and returns an instance of ReceiveChannel
Block current thread to run coroutines. So don't use it.

|--->   runBlocking : Block current thread to run this coroutines and returns a result of type T. So don't use it.
Mostly used for testing.

Coroutine Methods:
|--->   withContext

Suspended Functions:    A suspending function doesn't block a thread but only suspend the coroutine itself.
The thread is returned to the pool while the coroutine is waiting and when waiting is done
the coroutine resumes on the free thread in the pool.
We can call/invoke suspending function from either a coroutine block or
from another suspending function only.
e.g.                withContext
withTimeout
withTimeoutOrnull
join
delay
await
supervisorScope
coroutineScope


# We can convert the callbacks to coroutine

# Structured Concurrency :  It is set of language features and best practises introduced for kotlin coroutines
to avoid leaks and manage them productively.

# CoroutineScope : It's an interface.
# coroutineScope : Its a suspend function which will create child scope and gives the guarantee
to complete all the task of child scope before returning the value

 **/
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        // Basics Understanding
        binding.btnCount.setOnClickListener {
            binding.tvCount.text = count++.toString()
        }

        binding.btnDownloadUserData.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                downloadUserData()
            }
        }

        // Async & Await
        // Sequential Decomposition
        CoroutineScope(IO).launch {
            Log.i("MasterBlaster", "Calculation started...")
            val stock1 = getStock1()
            val stock2 = getStock2()
            val total = stock1 + stock2
            Log.i("MasterBlaster", "Total is $total")
        }
/*
O/P :

2021-07-23 21:33:40.452 18767-19787/com.example.coroutines I/MasterBlaster: Calculation started...
2021-07-23 21:33:50.461 18767-19787/com.example.coroutines I/MasterBlaster: Stock 1 returned...
2021-07-23 21:33:58.467 18767-19789/com.example.coroutines I/MasterBlaster: Stock 2 returned...
2021-07-23 21:33:58.467 18767-19789/com.example.coroutines I/MasterBlaster: Total is 110000
==> It takes total 18 sec

*/
        // Parallel Decomposition
        CoroutineScope(IO).launch {
            Log.i("MasterBlaster", "Calculation started...")
            val stock1 = async { getStock1() }
            val stock2 = async { getStock2() }
            val total = stock1.await() + stock2.await()
            Log.i("MasterBlaster", "Total is $total")
        }

        // Better way of Parallel Decomposition (Concurrency)
        CoroutineScope(Main).launch {
            Log.i("MasterBlaster", "Calculation started...")
            val stock1 = async(IO) { getStock1() }
            val stock2 = async(IO) { getStock2() }
            val total = stock1.await() + stock2.await()
            Toast.makeText(activity, "Total is $total", Toast.LENGTH_SHORT).show()
            Log.i("MasterBlaster", "Total is $total")
        }

/*
O/P :
2021-07-23 21:35:59.252 20521-21043/com.example.coroutines I/MasterBlaster: Calculation started...
2021-07-23 21:36:07.291 20521-21046/com.example.coroutines I/MasterBlaster: Stock 2 returned...
2021-07-23 21:36:09.308 20521-21046/com.example.coroutines I/MasterBlaster: Stock 1 returned...
2021-07-23 21:36:09.310 20521-21045/com.example.coroutines I/MasterBlaster: Total is 110000
==> It takes total 10 sec

*/

        // Structured Concurrency : It guarantees to complete all the works started by coroutines
        //                          within the child scope before the return of the suspending function.
        //                          It helps us to keep track of tasks we started and to cancel them when needed.

        binding.btnDownloadUserData.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {            // Parent scope
                binding.tvUserMessage.text = UserDataManager().getTotalUserCount().toString()
            }
        }
        /*
  O/P :
  ==> It will return 120 after 3 sec.

  */

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun downloadUserData() {
        withContext(Main) {
            for (i in 1..200000) {
                binding.tvUserMessage.text = "Downloading user $i in ${Thread.currentThread().name}"
                delay(1000)
            }
        }
    }

    private suspend fun getStock1(): Int {
        delay(10000)
        Log.i("MasterBlaster", "Stock 1 returned...")
        return 55000
    }

    private suspend fun getStock2(): Int {
        delay(8000)
        Log.i("MasterBlaster", "Stock 2 returned...")
        return 55000
    }

}