package com.galaxydl.e_university.classTable

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.galaxydl.e_university.databinding.ClassTableFragmentBinding
import com.galaxydl.e_university.utils.obtainViewModel

class ClassTableFragment : Fragment() {

    private lateinit var mBinding: ClassTableFragmentBinding

    private lateinit var mViewModel: ClassTableViewModel

    private lateinit var mAdapter: ClassTableAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        mBinding = ClassTableFragmentBinding.inflate(inflater, container, false)
        mViewModel = obtainViewModel(activity!!, ClassTableViewModel::class.java)

        setupRecyclerView()

        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mViewModel.start()
    }

    private fun setupRecyclerView() {
        val recyclerView = mBinding.classTableRV
        if (!::mAdapter.isInitialized) {
            mAdapter = ClassTableAdapter(ArrayList(), context!!)
        }
        recyclerView.layoutManager = LinearLayoutManager(context!!)
        recyclerView.adapter = mAdapter
    }

}