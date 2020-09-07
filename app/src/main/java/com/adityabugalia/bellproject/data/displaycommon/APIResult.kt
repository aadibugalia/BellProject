package com.adityabugalia.bellproject.data.displaycommon

import com.adityabugalia.bellproject.data.displaycommon.Constants.API_REQUEST
import org.json.JSONObject

class APIResult {
    var success: JSONObject? = null
        private set
    var error: String? = null
        private set
    var requestType: API_REQUEST?
        private set

    internal constructor(error: String?, requestType: API_REQUEST?) {
        this.error = error
        this.requestType = requestType
    }

    internal constructor(success: JSONObject?, requestType: API_REQUEST?) {
        this.success = success
        this.requestType = requestType
    }

}