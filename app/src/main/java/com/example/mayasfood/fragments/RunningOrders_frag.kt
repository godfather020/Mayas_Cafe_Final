package com.example.mayasfood.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.fragments.ViewModels.RunningOrders_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RO
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RunningOrders_frag : Fragment() {

    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var  recyclerView: RecyclerView
    lateinit var viewModel : RunningOrders_ViewModel
    lateinit var loading : ProgressBar
    var runOrder_num = ArrayList<String>()
    var runOrder_total = ArrayList<String>()
    var runOrder_pickup = ArrayList<String>()
    var runOrder_createdAt = ArrayList<String>()
    var runOrder_quantity = ArrayList<String>()
    var runOrder_status = ArrayList<String>()
    var runOrder_date = ArrayList<String>()
    var runOrder_img = ArrayList<String>()
    var runOrder_id = ArrayList<String>()

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

        viewModel = ViewModelProvider(this).get(RunningOrders_ViewModel::class.java)

        loading = view.findViewById(R.id.loading_runOrder)
        loading.visibility = View.VISIBLE

        recyclerView = view.findViewById(R.id.runOrder_rv)

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager

        getOrderDetails()

        //setUpRvModel()

        return view
    }

    private fun getOrderDetails() {

        viewModel.getAllOrders(this, "1", loading).observe(viewLifecycleOwner, Observer {

            if (it!=null){

                if (it.getSuccess()!!){

                    clearArrayLists()

                    val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())

                    for (i in it.getData()!!.ListOrderResponce!!.indices){

                        val createdDateTime = it.getData()!!.ListOrderResponce!![i].createdAt!!
                        val pickUpTime = it.getData()!!.ListOrderResponce!![i].pickupAt!!

                        val date1: Date = sdf.parse(createdDateTime) as Date
                        val date2: Date = sdf.parse(pickUpTime) as Date

                        val createdDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date1)

                        Log.d("createdDate", createdDate)

                        val createdTime = getFormatedTime(date1)
                        val pickTime = getFormatedTime(date2)

                        Log.d("Time", createdTime + "   " + pickTime)

                        if (it.getData()!!.ListOrderResponce!![i].orderStatus != "6" && it.getData()!!.ListOrderResponce!![i].orderStatus != "5" && it.getData()!!.ListOrderResponce!![i].cancelStatus != true){

                            runOrder_id.add(it.getData()!!.ListOrderResponce!![i].id.toString())
                            runOrder_num.add(it.getData()!!.ListOrderResponce!![i].id.toString())
                            runOrder_status.add(it.getData()!!.ListOrderResponce!![i].orderStatus.toString())
                            runOrder_total.add(it.getData()!!.ListOrderResponce!![i].amount.toString())
                            runOrder_quantity.add(it.getData()!!.ListOrderResponce!![i].toalQuantity.toString())
                            runOrder_img.add(it.getData()!!.ListOrderResponce!![i].Orderlists!![0].Productprice!!.productPic.toString())
                            runOrder_pickup.add(pickTime)
                            runOrder_createdAt.add(createdTime)
                            runOrder_date.add(createdDate)

                        }

                    }

                    loading.visibility = View.GONE
                }

                setUpRvModel()
            }

        })
    }

    private fun clearArrayLists() {

        recycleView_models.clear()
        runOrder_num.clear()
        runOrder_createdAt.clear()
        runOrder_date.clear()
        runOrder_img.clear()
        runOrder_pickup.clear()
        runOrder_quantity.clear()
        runOrder_total.clear()
        runOrder_status.clear()
        runOrder_id.clear()
    }

    private fun getFormatedTime(time: Date): String {

        val createdTime =
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(time)

        Log.d("timeC", createdTime)

        var newAMPM = createdTime.substring(5,createdTime.length)
        val newCreatedTime = createdTime.substring(0,5)

        if (newAMPM.contains("am")){

            newAMPM = newAMPM.replace("am", "AM")
        }
        else{

            newAMPM = newAMPM.replace("pm", "PM")
        }

        return newCreatedTime+"\n"+newAMPM

    }

    private fun setUpRvModel() {

        for (i in runOrder_num.indices){

            recycleView_models.add(RecycleView_Model(runOrder_id[i], runOrder_num[i], runOrder_total[i], runOrder_pickup[i], runOrder_createdAt[i], runOrder_quantity[i], runOrder_status[i], runOrder_date[i], runOrder_img[i]))
        }

        //recycleView_models.add(RecycleView_Model("#242525224", "$20", "10:45\npm", "06:30\npm", "1", "Accepted", "27 April 2022", "27cc7c762c9f5b70acf082961f43edcf"))

        val recycleView_adapter_RO = RecycleView_Adapter_RO(activity, recycleView_models)
        recyclerView.adapter = recycleView_adapter_RO
        recycleView_adapter_RO.notifyDataSetChanged()
    }

}