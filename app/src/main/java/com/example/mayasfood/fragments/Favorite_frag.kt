package com.example.mayasfood.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.response.FavoriteListResponce
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.fragments.ViewModels.Favorite_frag_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_F
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF


class Favorite_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    lateinit var viewModel : Favorite_frag_ViewModel
    lateinit var loading : ProgressBar
    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var recyclerView : RecyclerView
    var foodName = ArrayList<String>()
    var foodPrice = ArrayList<String>()
    var foodImg = ArrayList<String>()
    var foodRating = ArrayList<String>()
    var foodId = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_favorite_frag, container, false)

        viewModel = ViewModelProvider(this).get(Favorite_frag_ViewModel::class.java)

        dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("My Favorites");
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))

        dashBoard.bottomNavigationView.visibility = View.VISIBLE
        loading = view.findViewById(R.id.loading_fav)
        loading.visibility = View.VISIBLE

        setHasOptionsMenu(true)

        recyclerView = view.findViewById(R.id.favFood_rv)
        val layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        recyclerView.layoutManager = layoutManager

        getFavoriteList()

        return view
    }

    private fun getFavoriteList() {

        viewModel.getFavoriteList(this, "1", loading).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    foodName.clear()
                    foodImg.clear()
                    foodPrice.clear()
                    foodRating.clear()
                    recycleView_models.clear()
                    foodId.clear()

                    for (i in it.getData()!!.FavoriteListResponce!!.indices){

                        foodName.add(it.getData()!!.FavoriteListResponce!![i].Product!!.productName.toString())
                        foodImg.add(it.getData()!!.FavoriteListResponce!![i].Product!!.productPic.toString())
                        foodPrice.add(it.getData()!!.FavoriteListResponce!![i].Product!!.Productprices[0].amount.toString())
                        foodRating.add(it.getData()!!.FavoriteListResponce!![i].Product!!.customerrating.toString())
                        foodId.add(it.getData()!!.FavoriteListResponce!![i].productId.toString())
                    }

                }
                loading.visibility = View.GONE
            }

            setUpFavList()

        })

    }

    private fun setUpFavList() {

        for (i in foodName.indices) {
            recycleView_models.add(
                RecycleView_Model(
                    foodName[i],
                    foodPrice[i],
                    foodId[i],
                    foodImg[i],
                    foodRating[i]
                )
            )

            Log.d("indiimage1", foodName[i])
            Log.d("indiimage1", foodPrice[i])
            Log.d("indiimage1", foodImg[i])
            Log.d("indiimage1", foodRating[i])

        }

        val recycleView_adapter = RecycleView_Adapter_F(activity, recycleView_models)

        recyclerView.adapter = recycleView_adapter

        recycleView_adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.getItem(0).setVisible(true)
        menu.getItem(1).setVisible(true)
        menu.getItem(2).setVisible(true)
        dashBoard.navigationView.setCheckedItem(R.id.invisibleNav)
    }
}