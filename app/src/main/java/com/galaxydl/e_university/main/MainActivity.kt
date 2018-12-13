package com.galaxydl.e_university.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.Window
import com.galaxydl.e_university.R
import com.galaxydl.e_university.classTable.ClassTableFragment
import com.galaxydl.e_university.databinding.MainActivityBinding
import com.galaxydl.e_university.databinding.MainDrawerHeaderBinding
import com.galaxydl.e_university.login.LoginFragment
import com.galaxydl.e_university.utils.LiveDataEventBus
import com.galaxydl.e_university.utils.obtainViewModel
import com.galaxydl.e_university.utils.replaceFragmentInActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: MainActivityBinding

    private lateinit var mMainActivityViewModel: MainActivityViewModel

    private lateinit var mLastCheckedMenuItem: MenuItem

    private val mClassTableFragment: ClassTableFragment by lazy { ClassTableFragment() }

    private val mLoginFragment: LoginFragment by lazy { LoginFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        mBinding = MainActivityBinding.inflate(layoutInflater)
        mBinding.setLifecycleOwner(this)
        setContentView(mBinding.root)

        mMainActivityViewModel = obtainViewModel(this, MainActivityViewModel::class.java)
        mMainActivityViewModel.start()

        mBinding.viewModel = mMainActivityViewModel
    }

    override fun onResume() {
        super.onResume()
        start()
    }

    private fun start() {
        setupToolBar()
        setupNavigationView()

        setupEvents()

        if (mMainActivityViewModel.userInfo.get() == null) {
            replaceFragmentToLogin()
        } else {
            replaceFragmentToClassTable()
        }
    }

    private fun setupEvents() {
        LiveDataEventBus.get("onUpdated").observe(this, Observer {
            replaceFragmentToClassTable()
        })
    }

    private fun setupToolBar() {
        mBinding.mainToolbar.apply {
            setNavigationIcon(R.drawable.ic_menu)
            setTitleTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
            setNavigationOnClickListener {
                openDrawer()
            }
        }
        setSupportActionBar(mBinding.mainToolbar)
    }

    private fun setupNavigationView() {
        val headerBinding = MainDrawerHeaderBinding.inflate(LayoutInflater.from(this))

        mBinding.mainNavigationView.apply {
            addHeaderView(headerBinding.root)
            inflateMenu(R.menu.main_drawer_menu)
            mLastCheckedMenuItem = menu.findItem(R.id.classTable)
            setNavigationItemSelectedListener {
                mLastCheckedMenuItem.isChecked = false
                it.isChecked = true
                mLastCheckedMenuItem = it
                when (it.itemId) {
                    R.id.classTable -> {
                        replaceFragmentToClassTable()
                    }
                    R.id.login -> {
                        replaceFragmentToLogin()
                    }
                }
                closeDrawer()
                return@setNavigationItemSelectedListener true
            }
        }
    }

    private fun replaceFragmentToClassTable() {
        mBinding.mainToolbar.setTitle(R.string.class_table_title)
        replaceFragmentInActivity(
                supportFragmentManager,
                mClassTableFragment,
                R.id.mainFrame)
    }

    private fun replaceFragmentToLogin() {
        mBinding.mainToolbar.setTitle(R.string.login_title)
        replaceFragmentInActivity(
                supportFragmentManager,
                mLoginFragment,
                R.id.mainFrame)
    }

    private fun openDrawer() {
        mBinding.mainDrawer.openDrawer(mBinding.mainNavigationView, true)
    }

    private fun closeDrawer() {
        mBinding.mainDrawer.closeDrawers()
    }

}