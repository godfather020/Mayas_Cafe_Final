package com.example.mayasfood.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.fragments.ViewModels.popular_frag_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_C
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF


class popular_frag : Fragment() {

    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var recyclerView : RecyclerView
    var popularFoodName = ArrayList<String>()
    var popularFoodPrice = ArrayList<String>()
    var popularFoodImg = ArrayList<String>()
    var popularFoodRating = ArrayList<String>()
    lateinit var loading : ProgressBar
    lateinit var viewModel: popular_frag_ViewModel

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
        val view : View = inflater.inflate(R.layout.fragment_popular_frag, container, false)

        viewModel = ViewModelProvider(this).get(popular_frag_ViewModel::class.java)

        recyclerView = view.findViewById(R.id.popular_rv)
        loading = view.findViewById(R.id.loading_pop)
        loading.visibility = View.VISIBLE

        val layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager

        setPopularFoodView()

        return view
    }

    private fun setPopularFoodView() {

        viewModel.getPopularFood(this, "1", loading).observe(viewLifecycleOwner , Observer {

            if (it != null) {

                if (it.getSuccess()!!){

                    popularFoodName.clear()
                    popularFoodImg.clear()
                    popularFoodPrice.clear()
                    popularFoodRating.clear()

                    for (i in it.getData()!!.ListpopularproductResponce!!.indices) {

                        popularFoodName.add(
                            i,
                            it.getData()!!.ListpopularproductResponce!![i].productName.toString()
                        )
                        popularFoodPrice.add(
                            i,
                            it.getData()!!.ListpopularproductResponce!![i].Productprices!![0].amount.toString()
                        )
                        popularFoodRating.add(
                            i,
                            it.getData()!!.ListpopularproductResponce!![i].customerrating.toString()
                        )
                        popularFoodImg.add(
                            i,
                            it.getData()!!.ListpopularproductResponce!![i].productPic.toString()
                        )

                        Log.d(
                            "indiimage",
                            it.getData()!!.ListpopularproductResponce!![i].productName.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListpopularproductResponce!![i].Productprices!![0].amount.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListpopularproductResponce!![i].customerrating.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListpopularproductResponce!![i].productPic.toString()
                        )
                    }

                    loading.visibility = View.GONE
                }

                setUpPopularRv()

            }

        })

    }

    private fun setUpPopularRv() {

        for (i in popularFoodName.indices) {
            recycleView_models.add(
                RecycleView_Model(
                    popularFoodName[i],
                    popularFoodPrice[i],
                    popularFoodImg[i],
                    popularFoodRating[i]
                )
            )

            Log.d("indiimage1", popularFoodName[i])
            Log.d("indiimage1", popularFoodPrice[i])
            Log.d("indiimage1", popularFoodImg[i])
            Log.d("indiimage1", popularFoodRating[i])

        }

        val recycleView_adapter = RecycleView_Adapter_PF(activity, recycleView_models)

        recyclerView.adapter = recycleView_adapter

        recycleView_adapter.notifyDataSetChanged()
    }

}