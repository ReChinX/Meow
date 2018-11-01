package top.rechinx.meow.module.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import top.rechinx.meow.R

class HomeFragment: Fragment() {

    @BindView(R.id.homeTabLayout) lateinit var mTab: TabLayout
    @BindView(R.id.homeViewPager) lateinit var mViewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        ButterKnife.bind(this, view)
        mViewPager.adapter = HomePagerAdapter(context!!, childFragmentManager)
        mTab.setupWithViewPager(mViewPager)
        return view
    }
}