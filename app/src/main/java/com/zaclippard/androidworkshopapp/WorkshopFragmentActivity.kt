package com.zaclippard.androidworkshopapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity
import com.zaclippard.androidworkshopapp.ui.fragments.FirstWorkshopFragment

class WorkshopFragmentActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.workshop_fragment_activity)
    }
}
