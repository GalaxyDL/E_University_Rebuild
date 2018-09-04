package com.galaxydl.e_university.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.galaxydl.e_university.data.bean.ClassBean

@Dao
interface ClassBeanDao : BaseDao<ClassBean> {

    @Query("select * from class_bean")
    override fun list(): List<ClassBean>

    @Insert
    override fun add(classBean: ClassBean)

    @Query("delete from class_bean")
    override fun clear()

}