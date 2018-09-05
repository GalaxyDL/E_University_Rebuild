package com.galaxydl.e_university.data.source.local

import android.content.Context
import com.galaxydl.e_university.data.bean.ClassBean
import com.galaxydl.e_university.data.dao.ClassBeanDao

class ClassRepository(context: Context) : BaseDatabaseRepository<ClassBean, ClassBeanDao>(context) {
    override val dao: ClassBeanDao by lazy {
        localDatabase.getClassBeanDao()
    }

}