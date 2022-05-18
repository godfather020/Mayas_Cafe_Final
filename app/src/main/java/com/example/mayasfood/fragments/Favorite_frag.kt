package com.example.mayasfood.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard


class Favorite_frag : Fragment() {

    lateinit var dashBoard: DashBoard

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_favorite_frag, container, false)

        dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("My Favorites");
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))

        dashBoard.bottomNavigationView.visibility = View.VISIBLE

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.getItem(0).setVisible(true)
        menu.getItem(1).setVisible(true)
        menu.getItem(2).setVisible(true)
        dashBoard.navigationView.setCheckedItem(R.id.invisibleNav)
    }
}