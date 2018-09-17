package com.galaxydl.e_university.data.bean

import com.alibaba.fastjson.annotation.JSONCreator
import com.alibaba.fastjson.annotation.JSONField

data class HolidayInfoBean(val formerDay: Day, val actualDay: Day) : BaseBean() {

    @JSONCreator
    constructor(
            @JSONField(name = "formerWeek") formerWeek: Int,
            @JSONField(name = "formerDay") formerDay: Int,
            @JSONField(name = "actualWeek") actualWeek: Int,
            @JSONField(name = "actualDay") actualDay: Int)
            : this(Day(formerWeek, formerDay), Day(actualWeek, actualDay))

    data class Day(val week: Int, val day: Int)
}