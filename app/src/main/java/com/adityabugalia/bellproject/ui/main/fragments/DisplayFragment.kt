package com.adityabugalia.bellproject.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.adityabugalia.bellproject.R
import com.adityabugalia.bellproject.data.displaycommon.APIDataSource.gson
import com.adityabugalia.bellproject.data.displaycommon.Constants.API_REQUEST
import com.adityabugalia.bellproject.data.model.PlaylistDataModel
import com.adityabugalia.bellproject.data.model.PlaylistModel
import com.adityabugalia.bellproject.ui.main.fragments.support.DisplayViewModel
import com.adityabugalia.bellproject.ui.main.fragments.support.PlaylistAdapter
import com.adityabugalia.bellproject.ui.main.fragments.support.ViewModelFactory
import com.adityabugalia.bellproject.ui.main.fragments.support.callbacks.MessageFromActivity
import com.adityabugalia.bellproject.ui.main.fragments.support.callbacks.MessageToActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DisplayFragment : Fragment(), MessageFromActivity , AdapterView.OnItemClickListener{
    private var sendMessageToActivity: MessageToActivity? = null
    private val mLoggedInUser: Any? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: PlaylistAdapter? = null
    private val dialogItems: Array<String>? = null
    private val unclickableRows: List<Int>? = null
    private val unswipeableRows: List<Int>? = null
    private val openOptionsPosition = 0
    private var viewModel: DisplayViewModel? = null
    private var pullToRefereshLayout: SwipeRefreshLayout? = null
    private val dialog: AlertDialog? = null
    var titleEditText: EditText? = null
    var contextEditText: EditText? = null
    var contentEditText: EditText? = null

    companion object {
        fun newInstance() = DisplayFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.display_fragment, container, false)
//        sendMessageToActivity = activity as DisplayActivity?
//        sendMessageToActivity!!.OnFragmentReady(this)
//        sendMessageToActivity!!.toggleFabVisibility(View.VISIBLE)
//        sendMessageToActivity!!.toggleAddSearch(true)
        mRecyclerView = root.findViewById<View>(R.id.mList_videos) as RecyclerView
        pullToRefereshLayout =
            root.findViewById<View>(R.id.swipeContainer) as SwipeRefreshLayout
        return root
    }

    override fun OnDataRecieved(mObject: Any?) {

        //this.mLoggedInUser = (LoggedInUser) mObject;
    }

    override fun onDispatchTouchEvent(ev: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProviders.of(this, ViewModelFactory())
            .get(DisplayViewModel::class.java)
       // viewModel = ViewModelProviders.of(this).get(DisplayViewModel::class.java)
        //implement paging: app side and server
        pullToRefereshLayout!!.setOnRefreshListener {
            viewModel!!.fetchPlaylist(API_REQUEST.FETCH_PLAYLIST) }
        // Configuring the refreshing colors
        pullToRefereshLayout!!.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        viewModel!!.aPIResult.observe(this, Observer { apiResult ->
            pullToRefereshLayout!!.isRefreshing = false
            if (apiResult == null) {
                return@Observer
            }
            if (apiResult.error != null) {
                Toast.makeText(activity!!.applicationContext, "", Toast.LENGTH_LONG)
                    .show()
            }
            if (apiResult.success != null) {
                when (apiResult.requestType) {
                    API_REQUEST.FETCH_PLAYLIST -> try {

                        val responseModel = gson.fromJson<PlaylistModel>(
                            apiResult.success.toString(),
                            PlaylistModel::class.java
                        )

                        Log.e("",""+responseModel.etag);


setUpRecylerviewData(responseModel.items as ArrayList<PlaylistModel.Item>)



                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })



    }
    suspend fun get(responseModel: PlaylistModel) =
        // Dispatchers.Main
        withContext(Dispatchers.IO) {



        }
    private fun setUpRecylerviewData(playList: ArrayList<PlaylistModel.Item>) {
        if (mAdapter == null) {
            mAdapter = PlaylistAdapter(activity, playList ,this)
            mRecyclerView!!.layoutManager = LinearLayoutManager(activity)
            mRecyclerView!!.setHasFixedSize(true)
            mRecyclerView!!.setItemViewCacheSize(5)
            mRecyclerView!!.setAdapter(mAdapter)
        } else {
            mAdapter!!.setPlaylist(playList)
            mAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }
}