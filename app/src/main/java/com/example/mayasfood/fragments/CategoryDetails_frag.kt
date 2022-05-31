package com.example.mayasfood.fragments

import android.os.Bundle
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
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.CategoryDetails_ViewModel
import com.example.mayasfood.fragments.ViewModels.Dashboard_category_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF
import com.google.firebase.auth.FirebaseAuth


class CategoryDetails_frag : Fragment() {

    lateinit var recyclerView : RecyclerView
    var foodName = ArrayList<String>()
    var foodPrice = ArrayList<String>()
    var foodImg = ArrayList<String>()
    var foodRating = ArrayList<String>()
    var foodIsFav = ArrayList<Int>()
    var foodId = ArrayList<String>()
    var foodOfferAmt = ArrayList<String>()
    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var viewModel : CategoryDetails_ViewModel
    lateinit var dashBoard: DashBoard
    lateinit var loading : ProgressBar
    lateinit var auth : FirebaseAuth
    lateinit var search : MenuItem
    lateinit var recycleView_adapter : RecycleView_Adapter_PF

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
        val view : View = inflater.inflate(R.layout.fragment_category_details_frag, container, false)

        viewModel = ViewModelProvider(this).get(CategoryDetails_ViewModel::class.java)

        dashBoard = activity as DashBoard

        auth = FirebaseAuth.getInstance()

        dashBoard.toolbar_const.setTitle(Constants.categoryName);
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))
        loading = view.findViewById(R.id.loading_catDetails)
        loading.visibility = View.VISIBLE

        dashBoard.toolbar_const.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {

                if (item!!.itemId == R.id.search){


                }
                return true
            }

        })

        recyclerView = view.findViewById(R.id.category_details_rv)
        val layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        setHasOptionsMenu(true)

        setUpCategoryView()

        return view
    }

    private fun setUpCategoryView() {

        viewModel.getCategoryDetails(this, Constants.categoryId, "1", loading).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    foodImg.clear()
                    foodName.clear()
                    foodPrice.clear()
                    foodRating.clear()
                    foodIsFav.clear()
                    foodId.clear()
                    recycleView_models.clear()
                    foodOfferAmt.clear()

                    for(i in it.getData()!!.ListproductResponce!!.indices){

                        foodName.add(i, it.getData()!!.ListproductResponce!![i].productName.toString())
                        foodImg.add(i, it.getData()!!.ListproductResponce!![i].productPic.toString())
                        foodPrice.add(i, it.getData()!!.ListproductResponce!![i].Productprices!![0].amount.toString())
                        foodRating.add(i, it.getData()!!.ListproductResponce!![i].customerrating.toString())
                        if (auth.currentUser != null) {
                            foodIsFav.add(i, it.getData()!!.ListproductResponce!![i].favorite!!)
                        }
                        else{
                            foodIsFav.add(i, 0)
                        }

                        if(it.getData()!!.ListproductResponce!![i].Productprices!![0].offerAmount != null){

                            foodOfferAmt.add(i , it.getData()!!.ListproductResponce!![i].Productprices!![0].offerAmount.toString())
                        }
                        else{

                            foodOfferAmt.add(i , "0")
                        }
                        foodId.add(i , it.getData()!!.ListproductResponce!![i].id.toString())
                    }

                    loading.visibility = View.GONE
                }

                setUpModel()
            }
        })
    }

    private fun setUpModel() {

        for (i in foodName.indices){

            recycleView_models.add(
                RecycleView_Model(
                    foodOfferAmt[i],
                    foodName[i],
                    foodPrice[i],
                    foodId[i],
                    foodImg[i],
                    foodRating[i],
                    foodIsFav[i]
                )
            )
        }



        recycleView_adapter = RecycleView_Adapter_PF(activity, recycleView_models)
        recyclerView.adapter = recycleView_adapter
        recycleView_adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        search = menu.findItem(R.id.search)
        val searchView : androidx.appcompat.widget.SearchView = search.actionView as androidx.appcompat.widget.SearchView

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