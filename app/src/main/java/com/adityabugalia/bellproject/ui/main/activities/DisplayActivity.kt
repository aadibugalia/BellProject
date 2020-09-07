package com.adityabugalia.bellproject.ui.main.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.adityabugalia.bellproject.R
import com.adityabugalia.bellproject.ui.main.fragments.DisplayFragment
import com.adityabugalia.bellproject.ui.main.fragments.support.callbacks.MessageToActivity
import java.lang.reflect.Array.newInstance

class DisplayActivity : AppCompatActivity(), MessageToActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DisplayFragment.newInstance())
                .commitNow()
        }
    }

    override fun OnFragmentReady(mFragment: Fragment?) {
        TODO("Not yet implemented")
    }

    override fun toggleFabVisibility(mVisibility: Int) {
        TODO("Not yet implemented")
    }

    override fun toggleAddSearch(isAdd: Boolean) {
        TODO("Not yet implemented")
    }


}