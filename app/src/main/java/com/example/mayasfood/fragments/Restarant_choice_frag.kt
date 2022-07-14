package com.example.mayasfood.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.fragments.ViewModels.Restaurant_frag_ViewModel
import com.example.mayasfood.fragments.ViewModels.popular_frag_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RC
import com.google.firebase.auth.FirebaseAuth


class Restarant_choice_frag : Fragment() {

    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var recyclerView: RecyclerView
    var restaurantFoodName = ArrayList<String>()
    var restaurantFoodPrice = ArrayList<String>()
    var restaurantFoodImg = ArrayList<String>()
    var restaurantFoodRating = ArrayList<String>()
    var restaurantFoodId = ArrayList<String>()
    var restaurantFoodIsFav = ArrayList<Int>()
    var restaurantOfferAmt = ArrayList<String>()
    var restaurantFoodSize = ArrayList<String>()
    lateinit var loading: ProgressBar
    lateinit var viewModel: Restaurant_frag_ViewModel
    lateinit var dashBoard: DashBoard
    lateinit var auth: FirebaseAuth
    lateinit var search: MenuItem
    lateinit var recycleView_adapter: RecycleView_Adapter_RC
    var isLogin = false

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
        val view: View = inflater.inflate(R.layout.fragment_restarant_choice_frag, container, false)

        viewModel = ViewModelProvider(this).get(Restaurant_frag_ViewModel::class.java)

        dashBoard = activity as DashBoard

        auth = FirebaseAuth.getInstance()
        isLogin = dashBoard.getSharedPreferences("LogIn", 0).getBoolean("LogIn", false)

        dashBoard.toolbar_const.setTitle("Restaurant Choices")
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))

        dashBoard.toolbar_const.setOnMenuItemClickListener(object :
            Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {

                if (item!!.itemId == R.id.search) {


                }
                return true
            }

        })

        recyclerView = view.findViewById(R.id.restaurant_rv)
        loading = view.findViewById(R.id.loading_rest)
        loading.visibility = View.VISIBLE

        val layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager

        setHasOptionsMenu(true)

        setRestFoodView()

        return view
    }

    private fun setRestFoodView() {

        viewModel.getRestaurantChoiceFood(this, "1", loading).observe(viewLifecycleOwner, Observer {

            if (it != null) {

                if (it.getSuccess()!!) {

                    restaurantFoodName.clear()
                    restaurantFoodImg.clear()
                    restaurantFoodPrice.clear()
                    restaurantFoodRating.clear()
                    restaurantFoodId.clear()
                    restaurantFoodIsFav.clear()
                    restaurantOfferAmt.clear()
                    restaurantFoodSize.clear()

                    Log.d(
                        "indice",
                        it.getData()!!.ListrestaurantproductResponce!!.indices.toString()
                    )

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

                        restaurantFoodId.add(
                            i,
                            it.getData()!!.ListrestaurantproductResponce!![i].id.toString()
                        )

                        if (auth.currentUser != null || isLogin != false) {

                            restaurantFoodIsFav.add(
                                i,
                                it.getData()!!.ListrestaurantproductResponce!![i].favorite!!
                            )
                        } else {

                            restaurantFoodIsFav.add(i, 0)
                        }
                        if (it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].offerAmount != null) {

                            restaurantOfferAmt.add(
                                i,
                                it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].offerAmount.toString()
                            )
                        } else {

                            restaurantOfferAmt.add(i, "0")
                        }
                        if (it.getData()!!.ListrestaurantproductResponce!![i].Productprices!!.size == 1) {

                            restaurantFoodSize.add(it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].productsize.toString())
                        } else {

                            for (j in it.getData()!!.ListrestaurantproductResponce!![i].Productprices!!.indices) {

                                if (it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![j].productsize.equals(
                                        "M"
                                    )
                                ) {
                                    restaurantFoodSize.add(it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![j].productsize.toString())
                                }
                            }

                            if (restaurantFoodSize.isEmpty()) {

                                restaurantFoodSize.add(it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].productsize.toString())
                            }

                        }

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
                    restaurantFoodSize[i],
                    restaurantOfferAmt[i],
                    restaurantFoodName[i],
                    restaurantFoodPrice[i],
                    restaurantFoodId[i],
                    restaurantFoodImg[i],
                    restaurantFoodRating[i],
                    restaurantFoodIsFav[i]
                )
            )

            Log.d("indiimage1", restaurantFoodName[i])
            Log.d("indiimage1", restaurantFoodPrice[i])
            Log.d("indiimage1", restaurantFoodImg[i])
            Log.d("indiimage1", restaurantFoodRating[i])

        }

        recycleView_adapter = RecycleView_Adapter_RC(activity, recycleView_models)

        recyclerView.adapter = recycleView_adapter

        recycleView_adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

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