package com.example.mayasfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_N
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RO
import java.util.ArrayList


class RunningOrders_frag : Fragment() {

    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var  recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_running_orders_frag, container, false)

        recyclerView = view.findViewById(R.id.runOrder_rv)

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager

        setUpRvModel()

        return view
    }

    private fun setUpRvModel() {

        recycleView_models.add(RecycleView_Model("#242525224", "$20", "10:45\npm", "06:30\npm", "1", "Accepted", "27 April 2022", "27cc7c762c9f5b70acf082961f43edcf"))

        val recycleView_adapter_RO = RecycleView_Adapter_RO(activity, recycleView_models)
        recyclerView.adapter = recycleView_adapter_RO
        recycleView_adapter_RO.notifyDataSetChanged()
    }

}