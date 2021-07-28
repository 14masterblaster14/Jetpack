package com.example.coroutines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.coroutines.databinding.FragmentSecondBinding
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"
    val JOB_TIMEOUT = 3000L
    private val PROGRESS_MAX = 100
    private val PROGRESS_START = 0
    private val JOB_TIME = 4000 // 4000ms
    private lateinit var job: CompletableJob
    private lateinit var test_var: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        // Basics Understanding

        binding.btn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                fakeApiRequest("Scope:IO")
            }

            CoroutineScope(Dispatchers.Default).launch {
                fakeApiRequest("Scope:DEFAULT")
            }
        }

        /*O/P :

2019-12-11 21:22:27.795 31757-31808/com.example.coroutines I/System.out:MasterBlaster -> Debug Printing : getResultFromApi : DefaultDispatcher-worker-2
2019-12-11 21:22:27.795 31757-31807/com.example.coroutines I/System.out: MasterBlaster -> Debug Printing : getResultFromApi : DefaultDispatcher-worker-1
2019-12-11 21:22:29.805 31757-31809/com.example.coroutines I/System.out: MasterBlaster -> Scope:DEFAULT + Debug: Result #1
2019-12-11 21:22:29.807 31757-31808/com.example.coroutines I/System.out: MasterBlaster -> Scope:IO + Debug: Result #1
2019-12-11 21:22:29.868 31757-31809/com.example.coroutines I/System.out: MasterBlaster -> Debug Printing : getResult2FromAPi : DefaultDispatcher-worker-3
2019-12-11 21:22:29.871 31757-31808/com.example.coroutines I/System.out: MasterBlaster -> Debug Printing : getResult2FromAPi : DefaultDispatcher-worker-2
2019-12-11 21:22:31.893 31757-31809/com.example.coroutines I/System.out: MasterBlaster -> Scope:IO + Debug: Result #2
2019-12-11 21:22:31.897 31757-31808/com.example.coroutines I/System.out: MasterBlaster -> Scope:DEFAULT + Debug: Result #2
        */

        // Handling network calls
        binding.btn1.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                fakeNwtRequest()
            }
        }

        // Handling Jobs
        binding.btn2.setOnClickListener {
            // if(!::test_var.isInitialized){}  // Way to check lateinit variable initialised or not
            if (!::job.isInitialized) {
                initJob()
            }

            binding.progressBar.startJobOrCancel(job)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private suspend fun setTextOnMainThread(input: String) {
        withContext(Dispatchers.Main) {
            // We are passing the info to new thread
            setNewText(input)
        }
    }

    private fun setNewText(input: String) {
        val newText = binding.txtVw.text.toString() + "\n$input"
        binding.txtVw.text = newText
    }

    private suspend fun fakeApiRequest(tag: String) {

        val result1 = getResultFromApi()
        println("MasterBlaster -> $tag + Debug: $result1")
        setTextOnMainThread(result1)

        var result2 = getResult2FromAPi()
        println("MasterBlaster -> $tag + Debug: $result2")
        setTextOnMainThread(result2)
    }

    private suspend fun getResultFromApi(): String {
        logThread("getResultFromApi")
        delay(2000)      // It will delay the particular coroutine pertaining to thread
        //Thread.sleep(2000)  // It will delay entire thread and disable all coroutines on that thread
        return RESULT_1
    }

    private suspend fun getResult2FromAPi(): String {
        logThread("getResult2FromAPi")
        delay(2000)
        return RESULT_2
    }

    private fun logThread(methodName: String) {
        println("MasterBlaster -> Debug Printing : $methodName : ${Thread.currentThread().name}")
    }

    // Nwt Timeout
    private suspend fun fakeNwtRequest() {
        withContext(Dispatchers.IO) {
            // val job = launch {
            val job = withTimeoutOrNull(JOB_TIMEOUT) {

                val result1 = getResultFromApi()    //waiting 2000ms
                setTextOnMainThread("Got $result1")

                val result2 = getResult2FromAPi()   //waiting 2000ms
                setTextOnMainThread("Got $result2")
            }   // Total waiting 4000ms

            if (job == null) {
                val cancelMessage = "Cancelling job...Job took longer than $JOB_TIMEOUT ms"
                println("MasterBlaster -> debug : $cancelMessage")
                setTextOnMainThread(cancelMessage)
            }
        }
    }

    // Jobs
    fun initJob() {
        binding.btn2.text = "Start Job #1"
        //txt_vw2.setText("")
        updateJobCompletedTxtView("")
        job = Job()
        job.invokeOnCompletion {
            it?.message.let {
                var msg = it
                if (msg.isNullOrBlank()) {
                    msg = "Unknown cancellation error"
                }
                println("$job was cancelled.Reason : $msg")
                showToast(msg)
            }
        }
        binding.progressBar.max = PROGRESS_MAX
        binding.progressBar.progress = PROGRESS_START
    }

    private fun ProgressBar.startJobOrCancel(job: Job) {
        if (this.progress > 0) {
            println("$job is already started/Active. Cancelling...")
            resetJob()
        } else {
            binding.btn2.text = "Cancel Job #1"
            CoroutineScope(Dispatchers.IO + job).launch {
                // IO+job will be a new context apart from IO, so that if you cancel this job
                // then only that particular job will be cancelled and not the all jobs running in IO scope
                println("coroutine $this is activated with job $job")
                for (i in PROGRESS_START..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    this@startJobOrCancel.progress = i
                }
                updateJobCompletedTxtView("Job is complete")
            }
        }
    }

    private fun resetJob() {
        if (job.isActive || job.isCompleted) {
            job.cancel(CancellationException("Resetting job"))
        }
        initJob()
    }

    private fun updateJobCompletedTxtView(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            binding.txtVw2.text = text
        }
    }

    private fun showToast(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        }
    }
}