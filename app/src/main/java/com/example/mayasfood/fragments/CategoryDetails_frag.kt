package com.example.mayasfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.CategoryDetails_ViewModel
import com.example.mayasfood.fragments.ViewModels.Dashboard_category_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF


class CategoryDetails_frag : Fragment() {

    lateinit var recyclerView : RecyclerView
    var foodName = ArrayList<String>()
    var foodPrice = ArrayList<String>()
    var foodImg = ArrayList<String>()
    var foodRating = ArrayList<String>()
    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var viewModel : CategoryDetails_ViewModel

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

        recyclerView = view.findViewById(R.id.category_details_rv)
        val layoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        setUpCategoryView()

        return view
    }

    private fun setUpCategoryView() {

        viewModel.getCategoryDetails(this, Constants.categoryId, "1").observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    foodImg.clear()
                    foodName.clear()
                    foodPrice.clear()
                    foodRating.clear()

                    for(i in it.getData()!!.ListproductResponce!!.indices){

                        foodName.add(i, it.getData()!!.ListproductResponce!![i].productName.toString())
                        foodImg.add(i, it.getData()!!.ListproductResponce!![i].productPic.toString())
                        foodPrice.add(i, it.getData()!!.ListproductResponce!![i].Productprices!![0].amount.toString())
                        foodRating.add(i, it.getData()!!.ListproductResponce!![i].customerrating.toString())
                    }
                }

                setUpModel()
            }
        })
    }

    private fun setUpModel() {

        for (i in foodName.indices){

            recycleView_models.add(
                RecycleView_Model(
                    foodName[i],
                    foodPrice[i],
                    foodImg[i],
                    foodRating[i]
                )
            )
        }

        val recycleView_adapter = RecycleView_Adapter_PF(activity, recycleView_models)
        recyclerView.adapter = recycleView_adapter
        recycleView_adapter.notifyDataSetChanged()
    }


}