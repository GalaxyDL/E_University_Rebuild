package com.galaxydl.e_university.data.source.local

import android.content.Context
import com.galaxydl.e_university.data.bean.ScoreBean
import com.galaxydl.e_university.data.dao.ScoreBeanDao

class ScoreRepository(context: Context) : BaseDatabaseRepository<ScoreBean, ScoreBeanDao>(context) {
    override val dao: ScoreBeanDao by lazy {
        localDatabase.getScoreBeanDao()
    }

}