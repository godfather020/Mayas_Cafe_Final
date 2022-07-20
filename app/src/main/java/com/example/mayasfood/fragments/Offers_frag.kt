package com.example.mayasfood.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.fragments.ViewModels.Offers_frag_viewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_O
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Offers_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    var recycleView_models = ArrayList<RecycleView_Model>()
    val offers_img = ArrayList<String>()
    val offers_txt = ArrayList<String>()
    val offers_code = ArrayList<String>()
    lateinit var offers_frag_viewModel: Offers_frag_viewModel
    lateinit var recyclerView: RecyclerView
    lateinit var loading: ProgressBar
    lateinit var search: MenuItem
    lateinit var recycleView_adapter: RecycleView_Adapter_O

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_offers_frag, container, false)
        dashBoard = activity as DashBoard

        offers_frag_viewModel = ViewModelProvider(this).get(Offers_frag_viewModel::class.java)

        dashBoard.toolbar_const.setTitle("All Offers");
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))
        dashBoard.bottomNavigationView.visibility = View.VISIBLE

        dashBoard.toolbar_const.setOnMenuItemClickListener(object :
            Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {

                if (item!!.itemId == R.id.search) {


                }
                return true
            }

        })

        setHasOptionsMenu(true)

        loading = v.findViewById(R.id.loading_offer)

        loading.visibility = View.VISIBLE

        recyclerView = v.findViewById(R.id.offers_rv)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        getCoupons()

        return v
    }

    private fun getCoupons() {

        offers_frag_viewModel.getAllCoupons(this, "1", loading)
            .observe(viewLifecycleOwner, Observer {

                if (it != null) {

                    if (it.getSuccess()!!) {

                        offers_img.clear()
                        offers_txt.clear()
                        offers_code.clear()

                        for (i in it.getData()!!.ListcouponResponce!!.indices) {

                            val dateFormat: DateFormat =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                                        Locale.getDefault()
                                    )
                                } else {
                                    TODO("VERSION.SDK_INT < N")
                                }
                            val date =
                                dateFormat.parse(it.getData()!!.ListcouponResponce!![i].stopAt.toString())

                            val formatter: DateFormat =
                                SimpleDateFormat("dd-MM-yyyy") //If you need time just put specific format for time like 'HH:mm:ss'

                            val dateStr = formatter.format(date)

                            val valid_until = dateStr
                            val sdf = SimpleDateFormat("dd-MM-yyyy")
                            var strDate: Date? = null
                            try {
                                strDate = sdf.parse(valid_until)
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }

                            if (it.getData()!!.ListcouponResponce!![i].status != false && !Date().after(
                                    strDate
                                )
                            ) {

                                offers_img.add(
                                    it.getData()!!.ListcouponResponce!![i].bannerImage.toString()
                                )
                                offers_txt.add(
                                    it.getData()!!.ListcouponResponce!![i].title.toString() + " " + it.getData()!!.ListcouponResponce!![i].desc
                                            + " CODE :- " + it.getData()!!.ListcouponResponce!![i].code
                                )
                                offers_code.add(
                                    it.getData()!!.ListcouponResponce!![i].code.toString()
                                )
                            }
                        }

                        for (i in offers_txt.indices) {
                            recycleView_models.add(
                                RecycleView_Model(
                                    offers_txt[i],
                                    offers_img[i],
                                    offers_code[i]
                                )
                            )
                        }

                        recycleView_adapter = RecycleView_Adapter_O(activity, recycleView_models)
                        recyclerView.adapter = recycleView_adapter
                        recycleView_adapter.notifyDataSetChanged()

                        loading.visibility = View.GONE
                    } else {

                        Log.d("failure", "Failed")
                    }

                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.getItem(0).setVisible(true)
        menu.getItem(1).setVisible(false)
        menu.getItem(2).setVisible(false)
        menu.getItem(3).setVisible(false)

        dashBoard.navigationView.setCheckedItem(R.id.offersNav)

        search = menu.findItem(R.id.search)
        val searchView: androidx.appcompat.widget.SearchView =
            search.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                recycleView_adapter.filter.filter(newText)
                return false
            }
        })
    }
}