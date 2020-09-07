package com.adityabugalia.bellproject.ui.main.fragments.support

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adityabugalia.bellproject.R
import com.adityabugalia.bellproject.data.model.PlaylistModel
import com.adityabugalia.bellproject.ui.main.fragments.support.PlaylistAdapter.YoutubePostHolder
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class PlaylistAdapter(
    context: Context?,
    private var dataSet: ArrayList<PlaylistModel.Item>,
    listener: OnItemClickListener
) : RecyclerView.Adapter<YoutubePostHolder>() {
    private var mContext: Context? = null
    private val listener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubePostHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.youtube_item_structure, parent, false)
        return YoutubePostHolder(view)
    }

    override fun onBindViewHolder(holder: YoutubePostHolder, position: Int) {

        //set the views here
        val textViewTitle = holder.textViewTitle
        val textViewDes = holder.textViewDes
        val textViewDate = holder.textViewDate
        val ImageThumb = holder.ImageThumb
        val `object` = dataSet[position]

        textViewTitle.text=`object`.snippet?.title
        textViewDes.text=`object`.snippet?.description
        textViewDate.text=`object`.snippet?.publishedAt


        holder.bind(dataSet[position], listener)

        //TODO: image will be downloaded from url
       Picasso.with(mContext).load(`object`.snippet?.thumbnails?.default?.url).into(ImageThumb)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class YoutubePostHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView
        var textViewDes: TextView
        var textViewDate: TextView
        var ImageThumb: ImageView
        fun bind(item: PlaylistModel.Item, listener: OnItemClickListener) {
          //  itemView.setOnClickListener { listener.onItemClick(item) }
        }

        init {
            textViewTitle =
                itemView.findViewById<View>(R.id.textViewTitle) as TextView
            textViewDes =
                itemView.findViewById<View>(R.id.textViewDes) as TextView
            textViewDate =
                itemView.findViewById<View>(R.id.textViewDate) as TextView
            ImageThumb =
                itemView.findViewById<View>(R.id.ImageThumb) as ImageView
        }
    }
fun setPlaylist(newdata:ArrayList<PlaylistModel.Item>){
    this.dataSet=newdata
}
    init {
        this.mContext = context
        this.listener = listener
    }
}