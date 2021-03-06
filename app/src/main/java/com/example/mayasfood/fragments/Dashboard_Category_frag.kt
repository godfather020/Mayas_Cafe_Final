package com.example.mayasfood.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.Dashboard_category_ViewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_DC


class Dashboard_Category_frag : Fragment() {

    var categoryName = ArrayList<String>()
    lateinit var recyclerView : RecyclerView
   // lateinit var loading : ProgressBar
    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var viewModel: Dashboard_category_ViewModel
    var categoryId = ArrayList<String>()
    lateinit var dashBoard: DashBoard

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
        val view : View = inflater.inflate(R.layout.fragment_dashboard__category_frag, container, false)

        viewModel = ViewModelProvider(this).get(Dashboard_category_ViewModel::class.java)

        dashBoard = activity as DashBoard

        dashBoard.bottomNavigationView.visibility = View.GONE

        val value  = Constants.categoryId

        Log.d("value", value)

        //loading = view.findViewById(R.id.loading_catD)
        //loading.visibility = View.VISIBLE

        recyclerView = view.findViewById(R.id.category_rv)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        (activity as DashBoard).supportFragmentManager.beginTransaction()
            .replace(R.id.frag_cont_cat, CategoryDetails_frag()).commit()

        //Functions.loadFragment(fragmentManager, CategoryDetails_frag(), R.id.frag_cont_cat, true, "Details", null)

        Constants.popBack = 1

        setUpCategoryView()

        return view
    }

    private fun setUpCategoryView() {

        viewModel.getDashboardData(this, "1").observe(viewLifecycleOwner, Observer {

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

                   // loading.visibility = View.GONE
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

        val recycleView_adapter = RecycleView_Adapter_DC(activity, recycleView_models)

        recyclerView.adapter = recycleView_adapter
        recycleView_adapter.notifyDataSetChanged()
    }


}