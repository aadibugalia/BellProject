package com.adityabugalia.bellproject.data.model

import android.os.Parcel
import android.os.Parcelable

class PlaylistDataModel() : Parcelable {
    var title: String? = ""
    var description: String? = ""
    var publishedAt: String? = ""
    var thumbnail: String? = ""
    var video_id: String? = ""

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(description)
        dest.writeString(publishedAt)
        dest.writeString(thumbnail)
        dest.writeString(video_id)
    }

    protected constructor(`in`: Parcel) : this() {
        readFromParcel(`in`)
    }

    fun readFromParcel(`in`: Parcel) {
        title = `in`.readString()
        description = `in`.readString()
        publishedAt = `in`.readString()
        thumbnail = `in`.readString()
        video_id = `in`.readString()
    }

    companion object {
        val CREATOR: Parcelable.Creator<PlaylistDataModel?> =
            object : Parcelable.Creator<PlaylistDataModel?> {
                override fun createFromParcel(`in`: Parcel): PlaylistDataModel? {
                    return PlaylistDataModel(`in`)
                }

                override fun newArray(size: Int): Array<PlaylistDataModel?> {
                    return arrayOfNulls(size)
                }
            }
    }
}