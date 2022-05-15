package com.example.mayasfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard

class Search_frag : Fragment() {

    lateinit var dashBoard: DashBoard

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_search_frag, container, false)

        val dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("Search");
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))

        return view
    }

}