package com.adityabugalia.bellproject.ui.main.fragments.support.callbacks

import androidx.fragment.app.Fragment

interface MessageToActivity {


    fun OnFragmentReady(mFragment: Fragment?)
    fun toggleFabVisibility(mVisibility: Int)
    fun toggleAddSearch(isAdd: Boolean)
}