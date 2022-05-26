package com.example.mayasfood.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.fragments.ViewModels.PastOrders_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PO
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RO
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PastOrders_frag : Fragment() {

    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var  recyclerView: RecyclerView
    lateinit var viewModel : PastOrders_ViewModel
    lateinit var loading : ProgressBar
    var pastOrder_num = ArrayList<String>()
    var pastOrder_quantity = ArrayList<String>()
    var pastOrder_total = ArrayList<String>()
    var pastOrder_createdAt = ArrayList<String>()
    var pastOrder_date = ArrayList<String>()
    var pastOrder_status = ArrayList<String>()
    var pastOrder_rating = ArrayList<String>()
    var pastOrder_comment = ArrayList<String>()
    var pastOrder_img = ArrayList<String>()

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
        val view : View = inflater.inflate(R.layout.fragment_past_orders_frag, container, false)

        viewModel = ViewModelProvider(this).get(PastOrders_ViewModel::class.java)

        recyclerView = view.findViewById(R.id.pastOrder_rv)
        loading = view.findViewById(R.id.loading_past)
        loading.visibility = View.VISIBLE

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager

        getPastOrderDetails()

        //setUpRvModel()

        return view
    }

    private fun getPastOrderDetails() {

        viewModel.getAllOrders(this, "1", loading).observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    clearArrayLists()

                    val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())

                    for (i in it.getData()!!.ListOrderResponce!!.indices){

                        val createdDateTime = it.getData()!!.ListOrderResponce!![i].createdAt!!
                        val pickUpTime = it.getData()!!.ListOrderResponce!![i].pickupAt!!

                        val date1: Date = sdf.parse(createdDateTime) as Date
                        val date2: Date = sdf.parse(pickUpTime) as Date

                        val createdDate = SimpleDateFormat("dd MMMM yy", Locale.getDefault()).format(date1)
                        val pickupDate = SimpleDateFormat("dd MMMM yy", Locale.getDefault()).format(date2)

                        Log.d("createdDate", createdDate)
                        Log.d("createdDate", pickupDate)

                        val createdTime = getFormatedTime(date1)
                        val pickedTime = getFormatedTime(date2)

                        Log.d("createdDate", createdTime)
                        Log.d("createdDate", pickedTime)

                        if (it.getData()!!.ListOrderResponce!![i].orderStatus == "6" || it.getData()!!.ListOrderResponce!![i].orderStatus == "5"){

                            pastOrder_num.add(it.getData()!!.ListOrderResponce!![i].transactionId.toString())
                           pastOrder_status.add(it.getData()!!.ListOrderResponce!![i].orderStatus.toString())
                            pastOrder_total.add(it.getData()!!.ListOrderResponce!![i].amount.toString())
                            pastOrder_quantity.add(it.getData()!!.ListOrderResponce!![i].toalQuantity.toString())
                            pastOrder_img.add(it.getData()!!.ListOrderResponce!![i].Orderlists!![0].Productprice!!.productPic.toString())
                            //pastO.add(pickTime)
                            pastOrder_createdAt.add(createdDate+"-"+createdTime)
                            pastOrder_date.add(pickupDate+" "+pickedTime)
                            pastOrder_rating.add(it.getData()!!.ListOrderResponce!![i].orderRating.toString())
                            pastOrder_comment.add(it.getData()!!.ListOrderResponce!![i].orderComment.toString())

                        }

                    }
                }

                loading.visibility = View.GONE
                setUpRvModel()
            }
        })
    }

    private fun clearArrayLists() {

        pastOrder_comment.clear()
        pastOrder_rating.clear()
        pastOrder_status.clear()
        pastOrder_total.clear()
        pastOrder_num.clear()
        pastOrder_createdAt.clear()
        pastOrder_date.clear()
        pastOrder_img.clear()
        pastOrder_quantity.clear()
        recycleView_models.clear()
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

        return newCreatedTime+" "+newAMPM

    }

    private fun setUpRvModel() {

        for (i in pastOrder_num.indices) {
            recycleView_models.add(RecycleView_Model(pastOrder_num[i],pastOrder_total[i], "", pastOrder_createdAt[i], pastOrder_quantity[i], pastOrder_status[i], pastOrder_date[i], pastOrder_img[i], pastOrder_rating[i], pastOrder_comment[i]))
        }
        //recycleView_models.add(RecycleView_Model("#242525224", "20", "", "27 May 2022 - 06:30 PM", "1", "5", "27 April 2022 12:00 AM", "27cc7c762c9f5b70acf082961f43edcf", "2.6", "Nice Food"))

        val recycleView_adapter_PO = RecycleView_Adapter_PO(activity, recycleView_models)
        recyclerView.adapter = recycleView_adapter_PO
        recycleView_adapter_PO.notifyDataSetChanged()
    }


}