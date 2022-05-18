package com.example.mayasfood.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
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
import kotlin.collections.ArrayList

class Offers_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    var recycleView_models = ArrayList<RecycleView_Model>()
    val offers_img = ArrayList<String>()
    val offers_txt = ArrayList<String>()
    val offers_code = ArrayList<String>()
    lateinit var offers_frag_viewModel : Offers_frag_viewModel
    lateinit var recyclerView: RecyclerView
    lateinit var loading : ProgressBar

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

        offers_frag_viewModel.getAllCoupons(this, "1", loading).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    offers_img.clear()
                    offers_txt.clear()
                    offers_code.clear()

                    for (i in it.getData()!!.ListcouponResponce!!.indices){

                        offers_img.add(i , it.getData()!!.ListcouponResponce!![i].bannerImage.toString())
                        offers_txt.add(i , it.getData()!!.ListcouponResponce!![i].title.toString()+" "+it.getData()!!.ListcouponResponce!![i].desc
                                + " CODE :- " +it.getData()!!.ListcouponResponce!![i].code)
                        offers_code.add(i , it.getData()!!.ListcouponResponce!![i].code.toString())
                    }

                    for (i in offers_txt.indices) {
                        recycleView_models.add(RecycleView_Model(offers_txt[i], offers_img[i], offers_code[i]))
                    }

                    val recycleView_adapter = RecycleView_Adapter_O(activity, recycleView_models)
                    recyclerView.adapter = recycleView_adapter
                    recycleView_adapter.notifyDataSetChanged()

                    loading.visibility = View.GONE
                }
                else{

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

        dashBoard.navigationView.setCheckedItem(R.id.offersNav)
    }
}