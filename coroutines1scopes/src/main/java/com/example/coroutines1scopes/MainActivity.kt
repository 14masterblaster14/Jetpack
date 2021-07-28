package com.example.coroutines1scopes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coroutines1scopes.ui.main.MainFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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


/**
 * Android Specific Scopes:
 *
 * viewModelScope : Its a coroutine scope tied to a ViewModel so that when ViewModel destroyed then it will cancel
 *                  all the coroutines/Job running within this scope.
 *
 * lifecycleScope : Its a coroutine scope tied to the Activity/Fragment lifecycle so that when corresponding activity/ fragment
 *                  gets destroyed then it will cancel all the coroutines/Job running within this scope.
 *
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        // With Lifecycle KTX library i.e. lifecycleScope

        lifecycleScope.launch(Dispatchers.Main) {
            delay(5000)
            //progressBar.visibility = View.VISIBLE
            delay(10000)
            //progressBar.visibility = View.GONE
        }

        lifecycleScope.launchWhenCreated { }

        lifecycleScope.launchWhenStarted { }

        lifecycleScope.launchWhenResumed { }

    }

}