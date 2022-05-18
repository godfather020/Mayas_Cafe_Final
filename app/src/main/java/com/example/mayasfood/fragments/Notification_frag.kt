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
import com.example.mayasfood.fragments.ViewModels.Notification_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_N
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_N2
import java.text.SimpleDateFormat
import java.util.*

class Notification_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    var recycleView_models = ArrayList<RecycleView_Model>()
    var recycleView_models1 = ArrayList<RecycleView_Model>()
    lateinit var viewModel : Notification_ViewModel
    lateinit var loading : ProgressBar
    var notificationTodayTxt = ArrayList<String>()
    var notificationTodayTime = ArrayList<String>()
    var notificationTodayTitle = ArrayList<String>()
    var notificationPreviousTxt = ArrayList<String>()
    var notificationPreviousTime = ArrayList<String>()
    var notificationPreviousTitle = ArrayList<String>()
    lateinit var  recyclerView: RecyclerView
    lateinit var recyclerView2: RecyclerView
    var today = 0
    lateinit var today_txt: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_notification_frag, container, false)

        viewModel = ViewModelProvider(this).get(Notification_ViewModel::class.java)

        dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("My Notification");
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))

        setHasOptionsMenu(true)

        loading = view.findViewById(R.id.loading_notify)
        loading.visibility = View.VISIBLE

        today_txt = view.findViewById(R.id.today_txt)

        recyclerView= view.findViewById(R.id.today_rv)
        recyclerView2 = view.findViewById(R.id.yesterday_rv)

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        val layoutManager2 = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager
        recyclerView2.layoutManager = layoutManager2

        setUpNotifyView()

        return view
    }

    private fun setUpNotifyView() {

        viewModel.getNotificationData(this, loading).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    notificationTodayTxt.clear()
                    notificationPreviousTime.clear()
                    notificationPreviousTxt.clear()
                    notificationPreviousTitle.clear()
                    notificationTodayTime.clear()
                    notificationTodayTime.clear()
                    recycleView_models.clear()
                    recycleView_models1.clear()

                    //val dateTime = it.getData()!!.notifications!!.rows!![0].createdAt.toString()

                    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                    Log.d("timeDateCurrent", todayDate.toString())

                    //val date = "2022-05-12"

                    val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())


                    /*val currentTime =
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

                    Log.d("timeC", currentTime)*/

                    for (i in it.getData()!!.notifications!!.rows!!.indices){

                        val createdDateTime = it.getData()!!.notifications!!.rows!![i].createdAt!!

                        val date1: Date = sdf.parse(createdDateTime) as Date

                        val createdDate = SimpleDateFormat("yyy-MM-dd", Locale.getDefault()).format(date1)

                        Log.d("DateC", createdDate)

                        val notyTime =
                            SimpleDateFormat("HH:mm a", Locale.getDefault()).format(date1)

                        Log.d("timeC", notyTime)

                        val newAMPM = notyTime.substring(5,notyTime.length)
                        val newNotyTime = notyTime.substring(0,5)

                        if (newAMPM.contains("am")){

                            newAMPM.replace("am", "AM")
                        }
                        else{

                            newAMPM.replace("pm", "PM")
                        }

                        if (createdDate == todayDate){

                            Log.d("inside", "inside")

                            today = 2

                            notificationTodayTime.add( newNotyTime+"\n"+newAMPM)
                            notificationTodayTxt.add( it.getData()!!.notifications!!.rows!![i].description.toString())
                            notificationTodayTitle.add(it.getData()!!.notifications!!.rows!![i].title.toString())

                        }
                        else{

                            Log.d("inside1", "inside1")
                            notificationPreviousTime.add( newNotyTime+"\n"+newAMPM)
                            notificationPreviousTxt.add( it.getData()!!.notifications!!.rows!![i].description.toString())
                            notificationPreviousTitle.add(it.getData()!!.notifications!!.rows!![i].title.toString())
                        }
                    }

                    if (today == 0){

                        today_txt.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                    }

                    loading.visibility = View.GONE
                }

                setUpNotifyRv()
            }

        })
    }

    private fun setUpNotifyRv() {

        for (i in notificationTodayTime.indices){

            recycleView_models.add(RecycleView_Model(notificationTodayTime[i], notificationTodayTxt[i], notificationTodayTitle[i]))
        }

        for (i in notificationPreviousTitle.indices){

            recycleView_models1.add(RecycleView_Model(notificationPreviousTime[i], notificationPreviousTxt[i], notificationPreviousTitle[i]))
        }

        val recycleView_adapter_N = RecycleView_Adapter_N(activity, recycleView_models)
        val recycleView_adapter_N2 = RecycleView_Adapter_N2(activity, recycleView_models1)
        recyclerView.adapter = recycleView_adapter_N
        recyclerView2.adapter = recycleView_adapter_N2
        recycleView_adapter_N.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.getItem(1).setVisible(false)
    }

}