package com.example.mayasfood.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
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
import kotlin.time.Duration.Companion.milliseconds


class Favorite_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    lateinit var viewModel : Favorite_frag_ViewModel
    lateinit var loading : ProgressBar
    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var recyclerView : RecyclerView
    lateinit var noFav_img : ImageView
    lateinit var noFav_text : TextView
    var foodName = ArrayList<String>()
    var foodPrice = ArrayList<String>()
    var foodImg = ArrayList<String>()
    var foodRating = ArrayList<String>()
    var foodId = ArrayList<String>()
    var foodOfferAmt = ArrayList<String>()
    var foodSize = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_favorite_frag, container, false)

        viewModel = ViewModelProvider(this).get(Favorite_frag_ViewModel::class.java)

        dashBoard = activity as DashBoard

        dashBoard.toolbar_const.title = "My Favorites";
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))
        noFav_text = view.findViewById(R.id.noFav_txt)
        noFav_img = view.findViewById(R.id.noFav_img)

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
                    foodOfferAmt.clear()
                    foodSize.clear()

                    if (it.getData()!!.FavoriteListResponce!!.isEmpty()){

                        noFav_img.visibility = View.VISIBLE
                        noFav_text.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                    else{

                        noFav_img.visibility = View.GONE
                        noFav_text.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }

                    for (i in it.getData()!!.FavoriteListResponce!!.indices){

                        foodName.add(it.getData()!!.FavoriteListResponce!![i].Product!!.productName.toString())
                        foodImg.add(it.getData()!!.FavoriteListResponce!![i].Product!!.productPic.toString())
                        foodPrice.add(it.getData()!!.FavoriteListResponce!![i].Product!!.Productprices[0].amount.toString())
                        foodRating.add(it.getData()!!.FavoriteListResponce!![i].Product!!.customerrating.toString())
                        foodId.add(it.getData()!!.FavoriteListResponce!![i].productId.toString())
                        if (it.getData()!!.FavoriteListResponce!![i].Product!!.Productprices[0].offerAmount != null) {
                            foodOfferAmt.add(it.getData()!!.FavoriteListResponce!![i].Product!!.Productprices[0].offerAmount.toString())
                        }
                        else{
                            foodOfferAmt.add("0")
                        }
                        if (it.getData()!!.FavoriteListResponce!![i].Product!!.Productprices.size == 1){

                            foodSize.add(it.getData()!!.FavoriteListResponce!![i].Product!!.Productprices[0].productsize.toString())
                        }
                        else{

                            for (j in it.getData()!!.FavoriteListResponce!![i].Product!!.Productprices.indices) {

                                if (it.getData()!!.FavoriteListResponce!![i].Product!!.Productprices[j].productsize.equals(
                                        "M"
                                    )
                                ) {
                                    foodSize.add(it.getData()!!.FavoriteListResponce!![i].Product!!.Productprices[j].productsize.toString())
                                }
                            }

                            if (foodSize.isEmpty()){

                                foodSize.add(it.getData()!!.FavoriteListResponce!![i].Product!!.Productprices[0].productsize.toString())
                            }

                        }
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
                    foodSize[i],
                    foodOfferAmt[i],
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

        menu.getItem(0).setVisible(false)
        menu.getItem(1).setVisible(true)
        menu.getItem(3).setVisible(true)
        dashBoard.navigationView.setCheckedItem(R.id.invisibleNav)
    }
}