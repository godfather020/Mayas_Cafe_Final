package com.example.mayasfood.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_C
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_O
import kotlin.collections.ArrayList

class Offers_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    var recycleView_models = ArrayList<RecycleView_Model>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_search_frag, container, false)
        val dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("All Offers");
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))

        val recyclerView: RecyclerView = v.findViewById(R.id.offers_rv)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        val offers_txt = resources.getStringArray(R.array.Offer_txt)
        val offers_img : ArrayList<Int> = arrayListOf<Int>(R.drawable.image_2, R.drawable._01_1__1_, R.drawable._02_1)

        for (i in offers_txt.indices) {
            recycleView_models.add(RecycleView_Model(offers_txt[i], offers_img[i]))
        }

        val recycleView_adapter = RecycleView_Adapter_O(activity, recycleView_models)
        recyclerView.adapter = recycleView_adapter
        recycleView_adapter.notifyDataSetChanged()

        return v
    }
}