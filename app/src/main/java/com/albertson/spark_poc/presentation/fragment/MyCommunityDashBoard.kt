package com.albertson.spark_poc.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.databinding.FragmentMyCommunityDashBoardBinding
import com.albertson.spark_poc.presentation.fragment.ProductListFragment.Companion.COMMUNITY_ID
import com.albertson.spark_poc.presentation.fragment.ProductListFragment.Companion.COMMUNITY_NAME
import com.albertson.spark_poc.presentation.fragment.ProductListFragment.Companion.PRODUCT_ADDED
import com.albertson.spark_poc.presentation.viewmodel.MemberViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class MyCommunityDashBoard : Fragment() {

    private lateinit var binding : FragmentMyCommunityDashBoardBinding
    private var communityName = ""
    private var communityId : Int? = 0
    private lateinit var memberViewModel: MemberViewModel
    private var memberInfoList = emptyList<Member>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMyCommunityDashBoardBinding.inflate(inflater,container,false)
        memberViewModel = ViewModelProvider(requireActivity())[MemberViewModel::class.java]


        communityName = arguments?.getString(COMMUNITY_NAME).toString()
        communityId = arguments?.getInt(COMMUNITY_ID)

        val isProductAdded = arguments?.getBoolean(PRODUCT_ADDED)

        binding.tvCommunityName.text = communityName

        val viewPager = binding.viewPager
        viewPager.isSaveEnabled = false
        val tabs = binding.tabs

        val adapter = MyViewPagerAdapter(childFragmentManager,lifecycle)
        adapter.addFragment(TabTwoFragment.newInstance(communityName, communityId),getString(R.string.member_heading))
        adapter.addFragment(TabThreeFragment.newInstance(communityName, communityId),getString(R.string.product_list_heading))
        adapter.notifyDataSetChanged()
        viewPager.adapter =adapter
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)

            when (isProductAdded) {
               true -> viewPager.setCurrentItem(1, true)

               else -> viewPager.setCurrentItem(tab.position, true)
            }
        }.attach()

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(p0: TabLayout.Tab?) {
                when(p0?.position){
                    0 -> {
                        if (memberInfoList.isNotEmpty())
                        binding.ivEdit.visibility = View.VISIBLE
                    }
                    1->{
                        binding.ivEdit.visibility = View.GONE
                    }
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

        })

        val tabPosition = tabs.selectedTabPosition
        if (tabPosition == 1){
            binding.ivEdit.visibility = View.GONE
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                memberViewModel.memberList.collect {
                    memberInfoList = it.success!!
                    if (memberInfoList.isNotEmpty() && tabPosition != 1) {
                        binding.ivEdit.visibility = View.VISIBLE
                    } else {
                        binding.ivEdit.visibility = View.GONE
                    }
                }
            }
        }

        binding.ivEdit.setOnClickListener {
            if (memberViewModel.obj.get()){
                memberViewModel.obj.set(false)
            }else{
                memberViewModel.obj.set(true)
            }
        }
        return binding.root
    }


}

class MyViewPagerAdapter(childFragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(childFragmentManager,lifecycle){

    private val fragmentList : MutableList<Fragment> =ArrayList()
    private val titleList : MutableList<String> =ArrayList()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String){
        fragmentList.add(fragment)
        titleList.add(title)
    }

    fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

}
