package com.adityabugalia.bellproject.data.callbacks

import org.json.JSONObject

interface ApiResponseCallback {
     fun onApiResponseRecieved(responseObject: JSONObject)

}