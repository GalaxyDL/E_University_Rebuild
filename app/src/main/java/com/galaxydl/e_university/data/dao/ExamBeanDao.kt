package com.galaxydl.e_university.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.galaxydl.e_university.data.bean.ExamBean

@Dao
interface ExamBeanDao : BaseDao<ExamBean> {

    @Query("select * from exam_bean")
    override fun list(): List<ExamBean>

    @Insert
    override fun add(examBean: ExamBean)

    @Query("delete from exam_bean")
    override fun clear()

}