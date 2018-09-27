package com.galaxydl.e_university.classTable

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.galaxydl.e_university.R
import com.galaxydl.e_university.data.bean.ClassBean
import com.galaxydl.e_university.databinding.ClassItemBinding

class ClassTableAdapter(val mClassList: List<ClassBean>, mContext: Context)
    : RecyclerView.Adapter<ClassTableAdapter.ClassViewHolder>() {

    private val mInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder = ClassViewHolder(DataBindingUtil.inflate(mInflater, R.layout.class_item, parent, false))

    override fun getItemCount(): Int = mClassList.size

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.binding.classBean = mClassList[position]
    }

    class ClassViewHolder(val binding: ClassItemBinding)
        : RecyclerView.ViewHolder(binding.root)
}