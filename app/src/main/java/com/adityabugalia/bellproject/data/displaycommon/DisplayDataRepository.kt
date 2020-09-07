package com.adityabugalia.bellproject.data.displaycommon

import com.adityabugalia.bellproject.data.callbacks.ApiResponseCallback
import com.adityabugalia.bellproject.data.displaycommon.Constants.API_REQUEST
import org.json.JSONObject

class DisplayDataRepository private constructor(private val mDataSource: APIDataSource) :
    ApiResponseCallback {
    private var mCallback: ApiResponseCallback? = null

    fun fetchPlaylist(requestType: API_REQUEST?, callback: ApiResponseCallback?) {
        mCallback = callback
        mDataSource.getAPIData(requestType,callback);
    }

    override fun onApiResponseRecieved(responseObject: JSONObject) {
        mCallback!!.onApiResponseRecieved(responseObject)
    }

    companion object {
        @Volatile
        private var instance: DisplayDataRepository? = null
        fun getInstance(apiDataSource: APIDataSource): DisplayDataRepository? {
            if (instance == null) {
                instance = DisplayDataRepository(apiDataSource)
            }
            return instance
        }
    }

}