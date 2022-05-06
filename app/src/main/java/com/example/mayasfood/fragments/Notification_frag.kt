package com.example.mayasfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_C
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_N
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_N2
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF
import java.util.ArrayList

class Notification_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    var recycleView_models = ArrayList<RecycleView_Model>()
    var recycleView_models1 = ArrayList<RecycleView_Model>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_notification_frag, container, false)

        val dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("My Notification");
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))

        val recyclerView: RecyclerView = view.findViewById(R.id.today_rv)
        val recyclerView2: RecyclerView = view.findViewById(R.id.yesterday_rv)

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val layoutManager2 = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager
        recyclerView2.layoutManager = layoutManager2

        setUpNotifyRv()

        val recycleView_adapter_N = RecycleView_Adapter_N(activity, recycleView_models)
        val recycleView_adapter_N2 = RecycleView_Adapter_N2(activity, recycleView_models1)
        recyclerView.adapter = recycleView_adapter_N
        recyclerView2.adapter = recycleView_adapter_N2
        recycleView_adapter_N.notifyDataSetChanged()

        return view
    }

    private fun setUpNotifyRv() {

        val notifyToday_txt = resources.getStringArray(R.array.NotifyToday_txt)
        val notifyToday_time = resources.getStringArray(R.array.NotifyToday_time)
        val notifyYesterday_txt = resources.getStringArray(R.array.NotifyYesterday_txt)
        val notifyYesterday_time = resources.getStringArray(R.array.NotifyYesterday_time)

        for (i in notifyToday_txt.indices){

            recycleView_models.add(RecycleView_Model(notifyToday_txt[i], notifyToday_time[i]))
        }

        for (i in notifyYesterday_txt.indices){

            recycleView_models1.add(RecycleView_Model(notifyYesterday_txt[i], notifyYesterday_time[i]))
        }
    }

}