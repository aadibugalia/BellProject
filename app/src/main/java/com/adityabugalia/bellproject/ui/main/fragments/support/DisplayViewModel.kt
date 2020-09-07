package com.adityabugalia.bellproject.ui.main.fragments.support

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adityabugalia.bellproject.data.callbacks.ApiResponseCallback
import com.adityabugalia.bellproject.data.displaycommon.APIResult
import com.adityabugalia.bellproject.data.displaycommon.Constants.API_REQUEST
import com.adityabugalia.bellproject.data.displaycommon.DisplayDataRepository
import org.json.JSONObject

class DisplayViewModel(private val mRepository: DisplayDataRepository) : ViewModel(),
    ApiResponseCallback {
    private val apiResult = MutableLiveData<APIResult>()
    private var mRequestType: API_REQUEST? = null
    val aPIResult: LiveData<APIResult>
        get() = apiResult

    fun fetchPlaylist(requestType: API_REQUEST?) {

         mRepository.fetchPlaylist(requestType, this);
        mRequestType = requestType
    }




    override fun onApiResponseRecieved(responseObject: JSONObject) {
        try {
            if (responseObject["resultCode"].toString().equals("0", ignoreCase = true)) {
                apiResult.postValue(APIResult(responseObject, mRequestType))
            } else {
                apiResult.postValue(APIResult(responseObject.getString("message"), mRequestType))
            }
        } catch (e: Exception) {
            apiResult.postValue(APIResult("Please Try Again Later", mRequestType))
        }
    }

}