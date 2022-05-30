package com.example.mayasfood.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.popular_frag_ViewModel
import com.example.mayasfood.functions.Functions
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF
import com.google.firebase.auth.FirebaseAuth


class popular_frag : Fragment() {

    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var recyclerView : RecyclerView
    var popularFoodName = ArrayList<String>()
    var popularFoodPrice = ArrayList<String>()
    var popularFoodImg = ArrayList<String>()
    var popularFoodRating = ArrayList<String>()
    var popularFoodIsFav = ArrayList<Int>()
    lateinit var loading : ProgressBar
    var popularFoodId = ArrayList<String>()
    lateinit var viewModel: popular_frag_ViewModel
    lateinit var dashBoard : DashBoard
    lateinit var auth : FirebaseAuth
    lateinit var search : MenuItem
    lateinit var recycleView_adapter: RecycleView_Adapter_PF

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

        dashBoard = activity as DashBoard

        auth = FirebaseAuth.getInstance()

        dashBoard.toolbar_const.setTitle("Popular Food")
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))

        dashBoard.toolbar_const.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {

                if (item!!.itemId == R.id.search){


                }
                return true
            }

        })

        recyclerView = view.findViewById(R.id.popular_rv)
        loading = view.findViewById(R.id.loading_pop)
        loading.visibility = View.VISIBLE

        setHasOptionsMenu(true)

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
                    recycleView_models.clear()
                    popularFoodId.clear()
                    popularFoodIsFav.clear()

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

                        popularFoodId.add(i, it.getData()!!.ListpopularproductResponce!![i].id.toString())

                        if (auth.currentUser != null) {

                            popularFoodIsFav.add(
                                i,
                                it.getData()!!.ListpopularproductResponce!![i].favorite!!
                            )
                        }
                        else{

                            popularFoodIsFav.add(
                                i,
                                0
                            )
                        }

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
                    popularFoodId[i],
                    popularFoodImg[i],
                    popularFoodRating[i],
                    popularFoodIsFav[i]
                )
            )

            Log.d("indiimage1", popularFoodName[i])
            Log.d("indiimage1", popularFoodPrice[i])
            Log.d("indiimage1", popularFoodImg[i])
            Log.d("indiimage1", popularFoodRating[i])

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

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.search) {
            Constants.currentFrag = "S"
            //Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item)
    }*/

}
