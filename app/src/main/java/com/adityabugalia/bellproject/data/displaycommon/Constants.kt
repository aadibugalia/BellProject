package com.adityabugalia.bellproject.data.displaycommon

object Constants {
    var currentAPIRequest: API_REQUEST? = null

    enum class API_REQUEST {
        FETCH_CHANNEL, FETCH_PLAYLIST, FETCH_VIDEO
    }
}