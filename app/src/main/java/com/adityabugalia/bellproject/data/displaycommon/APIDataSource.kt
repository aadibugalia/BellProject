package com.adityabugalia.bellproject.data.displaycommon

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.adityabugalia.bellproject.MyApplication
import com.adityabugalia.bellproject.R
import com.adityabugalia.bellproject.data.callbacks.ApiResponseCallback
import com.adityabugalia.bellproject.data.model.PlaylistModel
import com.adityabugalia.bellproject.ui.login.LoginActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject

object APIDataSource {

    val TAG = "ApiData"

    val GOOGLE_YOUTUBE_API_KEY = "AIzaSyAcV4ZspylitkY7Bc7r4tJgaxF5-zWIs2U";//here you should use your api key for testing purpose you can use this api also
    val PLAYLIST_ID = "UU7V6hW6xqPAiUfataAZZtWA";//here you should use your playlist id for testing purpose you can use this api also
    val CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + PLAYLIST_ID + "&maxResults=20&key=" + GOOGLE_YOUTUBE_API_KEY + "";

    val gson: Gson

    init {
        gson = Gson()
    }

    lateinit var progressDialog: ProgressDialog



fun getAPIData(requestType: Constants.API_REQUEST?, callback: ApiResponseCallback?) {

    when(requestType){

        Constants.API_REQUEST.FETCH_PLAYLIST -> if (requestType == Constants.API_REQUEST.FETCH_PLAYLIST) {

            getPlayList(callback)
 }

    }

}

    fun getPlayList( callback: ApiResponseCallback?) {


        var getURL = CHANNLE_GET_URL
        val requestParam = null

        val jsonRequest =
                object : JsonObjectRequest(Method.GET, getURL, requestParam,
                Response.Listener { response ->
                Log.i(TAG, "Todo Success: ${response}")


                    response.put("resultCode","0")
                    response.put("resultDescription","success")
                    callback?.onApiResponseRecieved(response)


        },
        Response.ErrorListener { error ->
             var errorObj = JSONObject()
            errorObj.put("resultCode","99")
            errorObj.put("resultDescription",error.message)
            callback?.onApiResponseRecieved(errorObj)
                Log.i(TAG, "reservationList Error: ${error.message}")


        }
            ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                return HashMap<String, String>()
            }
        }

        MyApplication.getRequestQueue().add(jsonRequest)
    }

    operator fun invoke() {

    }
}