package com.adityabugalia.bellproject.data.model

import com.google.gson.annotations.SerializedName

data class PlaylistModel(
    @SerializedName("etag")
    val etag: String?,
    @SerializedName("resultCode")
    val resultCode: String?,
    @SerializedName("resultDescription")
    val resultDescription: String?,
    @SerializedName("items")
    val items: List<Item?>?,
    @SerializedName("kind")
    val kind: String?,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo?,
    @SerializedName("regionCode")
    val regionCode: String?
) {
    data class Item(
        @SerializedName("etag")
        val etag: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("kind")
        val kind: String?,
        @SerializedName("snippet")
        val snippet: Snippet?
    ) {
        data class Id(
            @SerializedName("kind")
            val kind: String?,
            @SerializedName("playlistId")
            val playlistId: String?
        )

        data class Snippet(
            @SerializedName("channelId")
            val channelId: String?,
            @SerializedName("channelTitle")
            val channelTitle: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("liveBroadcastContent")
            val liveBroadcastContent: String?,
            @SerializedName("publishTime")
            val publishTime: String?,
            @SerializedName("publishedAt")
            val publishedAt: String?,
            @SerializedName("thumbnails")
            val thumbnails: Thumbnails?,
            @SerializedName("title")
            val title: String?
        ) {
            data class Thumbnails(
                @SerializedName("default")
                val default: Default?,
                @SerializedName("high")
                val high: High?,
                @SerializedName("medium")
                val medium: Medium?
            ) {
                data class Default(
                    @SerializedName("height")
                    val height: Int?,
                    @SerializedName("url")
                    val url: String?,
                    @SerializedName("width")
                    val width: Int?
                )

                data class High(
                    @SerializedName("height")
                    val height: Int?,
                    @SerializedName("url")
                    val url: String?,
                    @SerializedName("width")
                    val width: Int?
                )

                data class Medium(
                    @SerializedName("height")
                    val height: Int?,
                    @SerializedName("url")
                    val url: String?,
                    @SerializedName("width")
                    val width: Int?
                )
            }
        }
    }

    data class PageInfo(
        @SerializedName("resultsPerPage")
        val resultsPerPage: Int?,
        @SerializedName("totalResults")
        val totalResults: Int?
    )
}