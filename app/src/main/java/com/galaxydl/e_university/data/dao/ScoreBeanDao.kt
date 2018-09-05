package com.galaxydl.e_university.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.galaxydl.e_university.data.bean.ScoreBean

@Dao
interface ScoreBeanDao : BaseDao<ScoreBean> {

    @Query("select * from score_bean limit 1")
    override fun get(): ScoreBean?

    @Query("select * from score_bean")
    override fun list(): List<ScoreBean>

    @Insert
    override fun add(bean: ScoreBean)

    @Query("delete from score_bean")
    override fun clear()

}