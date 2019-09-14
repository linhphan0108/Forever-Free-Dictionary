package com.example.foreverfreedictionary.ui.baseMVVM

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class BaseViewModel : ViewModel() {
    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob: Job by lazy { Job() }
    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    private val uiScope: CoroutineScope by lazy { CoroutineScope(
        Dispatchers.Main + viewModelJob) }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }
}