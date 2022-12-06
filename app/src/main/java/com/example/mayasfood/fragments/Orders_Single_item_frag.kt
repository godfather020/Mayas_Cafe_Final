package com.example.mayasfood.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.Order_Single_item_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_SIO
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*


class Orders_Single_item_frag : Fragment() {

    lateinit var singleOrder_img : CircleImageView
    lateinit var singleOrder_num : TextView
    lateinit var singleOrder_date : TextView
    lateinit var singleOrder_createdAt : TextView
    lateinit var singleOrder_pickup : TextView
    lateinit var singleOrder_quantity : TextView
    lateinit var singleOrder_total : TextView
    lateinit var dashBoard : DashBoard
    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var  recyclerView: RecyclerView
    lateinit var loading : ProgressBar
    lateinit var viewModel : Order_Single_item_ViewModel
    var singleItem_name = ArrayList<String>()
    var singleItem_img = ArrayList<String>()
    var singleItem_qty = ArrayList<String>()
    var singleItem_total = ArrayList<String>()
    var singleItem_id = ArrayList<String>()

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
        val view : View = inflater.inflate(R.layout.fragment_orders__single_item_frag, container, false)

        viewModel = ViewModelProvider(this).get(Order_Single_item_ViewModel::class.java)

        dashBoard = (activity as DashBoard)
        dashBoard.toolbar_const.title = "My Order Details"
        dashBoard.bottomNavigationView.visibility = View.VISIBLE

        setHasOptionsMenu(true)

        recyclerView = view.findViewById(R.id.singleOrder_rv)
        loading = view.findViewById(R.id.loading_singleOrder)
        singleOrder_img = view.findViewById(R.id.single_order_img)
        singleOrder_total = view.findViewById(R.id.singleOrder_total)
        singleOrder_quantity = view.findViewById(R.id.singleOrder_quantity)
        singleOrder_date = view.findViewById(R.id.singleOrder_date)
        singleOrder_num = view.findViewById(R.id.singleOrder_num)
        singleOrder_createdAt = view.findViewById(R.id.singleOrder_time)
        singleOrder_pickup = view.findViewById(R.id.singleOrder_pickup)
        loading.visibility = View.VISIBLE

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager

        getOrderDetails()

        return view
    }

    private fun getOrderDetails() {

        viewModel.getAllOrders(this, "1", loading).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    clearArrayLists()

                    val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())

                    for (i in it.getData()!!.ListOrderResponce!!.indices) {

                        Log.d("SingleId", Constants.singleID)

                        if (it.getData()!!.ListOrderResponce!![i].id.toString() == Constants.singleID) {

                            val createdDateTime = it.getData()!!.ListOrderResponce!![i].createdAt!!
                            val pickUpTime = it.getData()!!.ListOrderResponce!![i].pickupAt!!

                            val date1: Date = sdf.parse(createdDateTime) as Date
                            val date2: Date = sdf.parse(pickUpTime) as Date

                            val createdDate =
                                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(date1)

                            Log.d("createdDate", createdDate)

                            val createdTime = getFormatedTime(date1)
                            val pickTime = getFormatedTime(date2)

                            Log.d("Time", createdTime + "   " + pickTime)

                            singleOrder_pickup.setText(pickTime)
                            singleOrder_createdAt.setText(createdTime)
                            singleOrder_date.setText(createdDate)
                            singleOrder_num.setText("Order Id - #"+Constants.singleID)
                            singleOrder_quantity.setText("Number of items "+it.getData()!!.ListOrderResponce!![i].toalQuantity.toString())
                            singleOrder_total.setText(resources.getString(R.string.Rupee)+it.getData()!!.ListOrderResponce!![i].amount.toString())

                            Picasso.get().load(Constants.UserProduct_Path+ it.getData()!!.ListOrderResponce!![i].Orderlists!![0].Product!!.productPic.toString())
                                .into(singleOrder_img)

                            for (j in it.getData()!!.ListOrderResponce!![i].Orderlists!!.indices) {

                                //singleItem_name.add(it.getData()!!.ListOrderResponce!![i].Orderlists!![j])

                                singleItem_id.add(it.getData()!!.ListOrderResponce!![i].Orderlists!![j].productId.toString())
                                singleItem_name.add(it.getData()!!.ListOrderResponce!![i].Orderlists!![j].Product!!.productName.toString())
                                  singleItem_total.add(it.getData()!!.ListOrderResponce!![i].Orderlists!![j].totalAmount.toString())
                                singleItem_qty.add(it.getData()!!.ListOrderResponce!![i].Orderlists!![j].noItems.toString())
                                singleItem_img.add(it.getData()!!.ListOrderResponce!![i].Orderlists!![j].Product!!.productPic.toString())
                            }

                        }
                    }
                }

                loading.visibility = View.GONE
                setUpRvModel()
            }
        })
    }

    private fun clearArrayLists() {

        recycleView_models.clear()
        singleItem_name.clear()
        singleItem_img.clear()
        singleItem_qty.clear()
        singleItem_total.clear()
        singleItem_id.clear()
    }

    private fun setUpRvModel() {

        for (i in singleItem_qty.indices){

            recycleView_models.add(RecycleView_Model(singleItem_name[i], singleItem_img[i], singleItem_id[i], singleItem_total[i], singleItem_qty[i]))
        }

        //recycleView_models.add(RecycleView_Model("Samosa", "27cc7c762c9f5b70acf082961f43edcf", "20", "3"))

        val recycleView_adapter_SIO = RecycleView_Adapter_SIO(activity, recycleView_models)
        recyclerView.adapter = recycleView_adapter_SIO
        recycleView_adapter_SIO.notifyDataSetChanged()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.getItem(0).setVisible(false)
        menu.getItem(1).setVisible(false)
        menu.getItem(2).setVisible(false)

    }

}
