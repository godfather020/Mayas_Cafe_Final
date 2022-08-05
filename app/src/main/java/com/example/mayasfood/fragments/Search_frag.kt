package com.example.mayasfood.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.fragments.ViewModels.Search_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF
import com.google.firebase.auth.FirebaseAuth


class Search_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    lateinit var viewModel : Search_ViewModel
    lateinit var loading : ProgressBar
    var foodImg = ArrayList<String>()
    var foodName = ArrayList<String>()
    var foodPrice = ArrayList<String>()
    var foodRating = ArrayList<String>()
    var foodIsFav = ArrayList<Int>()
    var foodId = ArrayList<String>()
    var foodSize = ArrayList<String>()
    var foodOfferAmt = ArrayList<String>()
    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var auth : FirebaseAuth
    lateinit var recycleView_adapter : RecycleView_Adapter_PF
    lateinit var recyclerView : RecyclerView
    lateinit var searchAll : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_search_frag, container, false)

        viewModel = ViewModelProvider(this).get(Search_ViewModel::class.java)

        dashBoard = activity as DashBoard

        auth = FirebaseAuth.getInstance()

        dashBoard.toolbar_const.setTitle("Search All");
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))
        dashBoard.bottomNavigationView.visibility = View.GONE

        searchAll = view.findViewById(R.id.searchAllproduct)
        loading = view.findViewById(R.id.loading_all)
        loading.visibility = View.VISIBLE

        recyclerView = view.findViewById(R.id.allProduct_rv)
        val layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        setHasOptionsMenu(true)

        getProductList()

        searchAll.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                recycleView_adapter.filter.filter(s)
            }
            override fun afterTextChanged(s: Editable) {}
        })

        return view
    }

    private fun getProductList() {

        viewModel.getProductList(this, "1",loading).observe(viewLifecycleOwner, Observer {

            if (it!=null){

                if (it.getSuccess()!!){

                    foodImg.clear()
                    foodName.clear()
                    foodPrice.clear()
                    foodRating.clear()
                    foodIsFav.clear()
                    foodId.clear()
                    recycleView_models.clear()
                    foodOfferAmt.clear()
                    foodSize.clear()

                    for(i in it.getData()!!.ListproductResponce!!.indices){

                        foodName.add( it.getData()!!.ListproductResponce!![i].productName.toString())
                        foodImg.add( it.getData()!!.ListproductResponce!![i].productPic.toString())
                        foodPrice.add( it.getData()!!.ListproductResponce!![i].Productprices!![0].amount.toString())
                        foodRating.add( it.getData()!!.ListproductResponce!![i].customerrating.toString())
                        foodSize.add(it.getData()!!.ListproductResponce!![i].Productprices!![0].productsize.toString())
                        if (auth.currentUser != null) {
                            foodIsFav.add(
                                it.getData()!!.ListproductResponce!![i].favorite!!
                            )
                        }
                        else{
                            foodIsFav.add( 0)
                        }
                        foodId.add( it.getData()!!.ListproductResponce!![i].id.toString())
                        if (it.getData()!!.ListproductResponce!![i].Productprices!![0].offerAmount != null){

                            foodOfferAmt.add( it.getData()!!.ListproductResponce!![i].Productprices!![0].offerAmount.toString())
                        }
                        else {

                            foodOfferAmt.add("0")
                        }
                    }

                    loading.visibility = View.GONE
                }

                setUpRView()
            }

        })

    }

    private fun setUpRView() {

        for (i in foodName.indices){

            recycleView_models.add(
                RecycleView_Model(
                    foodSize[i],
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

        menu.getItem(0).setVisible(false)
    }

}