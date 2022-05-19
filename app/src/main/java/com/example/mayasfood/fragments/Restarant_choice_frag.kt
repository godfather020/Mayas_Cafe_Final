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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.fragments.ViewModels.Restaurant_frag_ViewModel
import com.example.mayasfood.fragments.ViewModels.popular_frag_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RC


class Restarant_choice_frag : Fragment() {

    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var recyclerView : RecyclerView
    var restaurantFoodName = ArrayList<String>()
    var restaurantFoodPrice = ArrayList<String>()
    var restaurantFoodImg = ArrayList<String>()
    var restaurantFoodRating = ArrayList<String>()
    var restaurantFoodId = ArrayList<String>()
    lateinit var loading : ProgressBar
    lateinit var viewModel: Restaurant_frag_ViewModel

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
        val view : View = inflater.inflate(R.layout.fragment_restarant_choice_frag, container, false)

        viewModel = ViewModelProvider(this).get(Restaurant_frag_ViewModel::class.java)

        recyclerView = view.findViewById(R.id.restaurant_rv)
        loading = view.findViewById(R.id.loading_rest)
        loading.visibility = View.VISIBLE

        val layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager

        setRestFoodView()

        return view
    }

    private fun setRestFoodView() {

        viewModel.getRestaurantChoiceFood(this, "1", loading).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    restaurantFoodName.clear()
                    restaurantFoodImg.clear()
                    restaurantFoodPrice.clear()
                    restaurantFoodRating.clear()
                    restaurantFoodId.clear()

                    Log.d("indice", it.getData()!!.ListrestaurantproductResponce!!.indices.toString())

                    for (i in it.getData()!!.ListrestaurantproductResponce!!.indices) {

                        restaurantFoodName.add(
                            i,
                            it.getData()!!.ListrestaurantproductResponce!![i].productName.toString()
                        )
                        restaurantFoodPrice.add(
                            i,
                            it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].amount.toString()
                        )
                        restaurantFoodImg.add(
                            i,
                            it.getData()!!.ListrestaurantproductResponce!![i].productPic.toString()
                        )
                        restaurantFoodRating.add(
                            i,
                            it.getData()!!.ListrestaurantproductResponce!![i].systemrating.toString()
                        )

                        restaurantFoodId.add(i , it.getData()!!.ListrestaurantproductResponce!![i].id.toString())

                        Log.d(
                            "indiimage",
                            it.getData()!!.ListrestaurantproductResponce!![i].productName.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].amount.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListrestaurantproductResponce!![i].customerrating.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListrestaurantproductResponce!![i].productPic.toString()
                        )
                    }
                    loading.visibility = View.GONE
                }

                setUpRestChoiceRV()
            }
        })
    }

    private fun setUpRestChoiceRV() {

        for (i in restaurantFoodName.indices) {
            recycleView_models.add(
                RecycleView_Model(
                    restaurantFoodName[i],
                    restaurantFoodPrice[i],
                    restaurantFoodId[i],
                    restaurantFoodImg[i],
                    restaurantFoodRating[i]
                )
            )

            Log.d("indiimage1", restaurantFoodName[i])
            Log.d("indiimage1", restaurantFoodPrice[i])
            Log.d("indiimage1", restaurantFoodImg[i])
            Log.d("indiimage1", restaurantFoodRating[i])

        }

        val recycleView_adapter = RecycleView_Adapter_RC(activity, recycleView_models)

        recyclerView.adapter = recycleView_adapter

        recycleView_adapter.notifyDataSetChanged()
    }


}