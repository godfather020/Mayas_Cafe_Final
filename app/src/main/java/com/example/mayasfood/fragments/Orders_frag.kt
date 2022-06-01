package com.example.mayasfood.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class Orders_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    private lateinit var labels: ArrayList<String>
    lateinit var indicator : TabLayout
    lateinit var vpSample : ViewPager
    lateinit var viewAdapter : CustomViewAdapter
    lateinit var tab_click_1 : TextView
    lateinit var tab_click_2 : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View =  inflater.inflate(R.layout.fragment_orders_frag, container, false)

        dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("My Orders");
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))
        vpSample = view.findViewById(R.id.vpSample)

        indicator = view.findViewById(R.id.orders_tab)

        dashBoard.bottomNavigationView.visibility = View.VISIBLE

        setHasOptionsMenu(true)

        val adapter = CustomViewAdapter(childFragmentManager)

        adapter.addFragment(RunningOrders_frag(), "Running Orders")
        adapter.addFragment(PastOrders_frag(), "Past Orders")

        vpSample.adapter = adapter

        indicator.setupWithViewPager(vpSample)

        indicator.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                vpSample.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
            /*Log.d(
                "tab", "out1"
            )

            if (vpSample.currentItem == 0){

                Log.d(
                    "tab", "in"
                )
                vpSample.setCurrentItem(1, true)
            }
            else{

                Log.d(
                    "tab", "out"
                )

                vpSample.currentItem = 0
            }*/

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.getItem(0).setVisible(false)
        menu.getItem(1).setVisible(true)
        menu.getItem(3).setVisible(false)
        dashBoard.navigationView.setCheckedItem(R.id.orderNav)
    }
}

class CustomViewAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    val arrayFragment = ArrayList<Fragment>()
    val stringArray = ArrayList<String>()

    fun addFragment (fragment: Fragment , s : String) {

        this.arrayFragment.add(fragment)
        this.stringArray.add(s)
    }

    override fun getCount(): Int {
        return arrayFragment.size
    }

    override fun getItem(position: Int): Fragment {
        return arrayFragment.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return stringArray.get(position)
    }

}






