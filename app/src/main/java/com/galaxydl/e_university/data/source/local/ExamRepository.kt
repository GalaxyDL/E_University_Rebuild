package com.galaxydl.e_university.data.source.local

import android.content.Context
import com.galaxydl.e_university.data.bean.ExamBean
import com.galaxydl.e_university.data.dao.ExamBeanDao

class ExamRepository(context: Context) : BaseDatabaseRepository<ExamBean, ExamBeanDao>(context) {
    override val dao: ExamBeanDao by lazy {
        localDatabase.getExamBeanDao()
    }

}