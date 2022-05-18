package com.example.mayasfood.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.fragments.ViewModels.Category_frag_ViewModel
import com.example.mayasfood.fragments.ViewModels.Dashboard_category_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_DC
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_MC

class Category_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    var categoryName = ArrayList<String>()
    lateinit var recyclerView : RecyclerView
    lateinit var loading : ProgressBar
    var categoryId = ArrayList<String>()
    lateinit var viewModel: Category_frag_ViewModel
    var recycleView_models = ArrayList<RecycleView_Model>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v : View = inflater.inflate(R.layout.fragment_category_frag, container, false)

        viewModel = ViewModelProvider(this).get(Category_frag_ViewModel::class.java)

        dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("Categories");
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))
        dashBoard.bottomNavigationView.visibility = View.GONE

        loading = v.findViewById(R.id.loading_mainCat)
        loading.visibility = View.VISIBLE

        recyclerView = v.findViewById(R.id.main_cat_rv)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        return v
    }

    private fun setUpMainCategory() {

        viewModel.getDashboardData(this , "1", loading).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    categoryName.clear()
                    recycleView_models.clear()
                    categoryId.clear()

                    for (i in it.getData()!!.ListcategoryResponce!!.indices) {

                        categoryName.add(
                            i,
                            it.getData()!!.ListcategoryResponce!![i].categoryName.toString()
                        )

                        categoryId.add(i , it.getData()!!.ListcategoryResponce!![i].id.toString())

                        Log.d(
                            "indiimage",
                            it.getData()!!.ListcategoryResponce!![i].categoryName.toString()
                        )
                    }

                    loading.visibility = View.GONE
                }

                setUpModel()
            }
        })
    }

    private fun setUpModel() {

        for (s in categoryName.indices) {
            recycleView_models.add(RecycleView_Model(categoryName[s], categoryId[s]))

            Log.d("indiimage1", categoryId[s])
        }

        val recycleView_adapter = RecycleView_Adapter_MC(activity, recycleView_models)

        recyclerView.adapter = recycleView_adapter
        recycleView_adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        setUpMainCategory()
    }
}