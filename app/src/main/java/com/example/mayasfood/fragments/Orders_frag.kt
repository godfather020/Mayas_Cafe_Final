package com.example.mayasfood.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard


class Orders_frag : Fragment() {

    lateinit var dashBoard: DashBoard

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =  inflater.inflate(R.layout.fragment_profile_frag, container, false)

        val dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("My Orders");
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))

        return view
    }
}