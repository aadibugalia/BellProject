package com.adityabugalia.bellproject.ui.main.fragments.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adityabugalia.bellproject.data.displaycommon.APIDataSource
import com.adityabugalia.bellproject.data.displaycommon.DisplayDataRepository

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DisplayViewModel::class.java)) {
            DisplayViewModel(DisplayDataRepository.getInstance(APIDataSource)!!) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}