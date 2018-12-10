package com.galaxydl.e_university.data.source.network

import android.content.Context
import com.galaxydl.e_university.data.bean.ClassBean
import com.galaxydl.e_university.utils.CLASS_END_TIME
import com.galaxydl.e_university.utils.CLASS_START_TIME
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.util.*
import kotlin.collections.ArrayList

class ClassTableCrawler(context: Context) : BaseCrawler<ClassBean>(context) {

    override val client: OkHttpClient by lazy {
        OkHttpClientManager.getClient(TJUT_CLIENT_KEY, context)
    }

    private var classCount = 0
    private lateinit var classTitles: ArrayList<String>
    private lateinit var classColors: ArrayList<Int>

    override fun load(onLoad: (List<ClassBean>) -> Unit, onError: (Exception) -> Unit) {
        launch(CommonPool) {
            try {
                val result = crawl()
                launch(UI) { onLoad(result) }
            } catch (e: Exception) {
                launch(UI) { onError(e) }
            }
        }
    }

    override suspend fun crawl(): List<ClassBean> {
        val result = ArrayList<ClassBean>()
        val request = Request.Builder().url(makeUrl())
                .get()
                .headers(HEADERS)
                .build()
        val response = client.newCall(request).execute()
        val document = Jsoup.parse(response.body().toString())
        parse(document, result)
        return result
    }

    private fun makeUrl(): String {
        val calendar = Calendar.getInstance()
        val month = calendar[Calendar.MONTH] + 1
        val year = calendar[Calendar.YEAR]
        val sb = StringBuilder()
        sb.append(BASE_CLASS_TABLE_URL)
        sb.append(when {
            month in 2..7 -> "${year - 1}-$year-2"
            month > 7 -> "$year-${year + 1}-1"
            else -> "${year - 1}-$year-1"
        })
        sb.append(BASE_CLASS_TABLE_URL_SUFFIX)
        return sb.toString()
    }

    private fun parse(doc: Document, result: ArrayList<ClassBean>) {
        classTitles = ArrayList()
        classColors = ArrayList()
        val table = doc.getElementsByTag("tbody").first()
        val haveClass = Array(7) { Array(11) { false } }
        var line: Element
        var aClass: Element
        var classLenght: Int
        var tempText: String
        var offset: Int
        classCount = 0
        for (i in 0..6) {
            for (j in 0..10) {
                haveClass[i][j] = false
            }
        }
        for (i in 1..11) {
            line = table.child(i)
            line.child(0).remove()
            line.child(0).remove()
            offset = 0
            for (j in 1..7) {
                if (haveClass[j - 1][i - 1]) {
                    offset++
                    continue
                }
                aClass = line.child(j - 1 - offset)
                if (aClass.toString() != "<td colspan=\"1\" rowspan=\"1\">&nbsp;</td>") {
                    classLenght = Integer.parseInt(aClass.attr("rowspan"))
                    for (k in 0 until classLenght) {
                        haveClass[j - 1][i + k - 1] = true
                    }
                    tempText = aClass.toString()
                    if (!aClass.getElementsByTag("hr").isEmpty()) {
                        val temps = tempText.split(("<hr>").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        for (k in temps.indices) {
                            if (k != 0) {
                                result.addAll(parseClass("&nbsp;" + temps[k], i, j, classLenght))
                            } else {
                                result.addAll(parseClass(temps[k], i, j, classLenght))
                            }
                        }
                    } else {
                        result.addAll(parseClass(tempText, i, j, classLenght))
                    }
                }
            }
        }
    }

    private fun parseClass(classString: String, i: Int, date: Int, classLength: Int): List<ClassBean> {
        val results = ArrayList<ClassBean>()

        val theClass = classString.replace("\n", "")
        val allInfo = theClass.split("<br>")
        val titleAndTimeAndDate = allInfo[0].split(("&nbsp;").toRegex(), 4).toTypedArray()
        val titleAndId = titleAndTimeAndDate[1].split(("\\[").toRegex(), 2).toTypedArray()
        val week = titleAndTimeAndDate[2].split((",").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var whichWeek: Array<String>

        titleAndId[1] = titleAndId[1].replace(("\\] ").toRegex(), "")

        for (k in week.indices) {
            whichWeek = week[k].split(("周").toRegex(), 2).toTypedArray()
            val title = titleAndId[0]
            val teacher = allInfo[1]
            val location = allInfo[2].replace(" </td>", "")
            val classNumber = Integer.parseInt(titleAndId[1])
            val timeEnd = i + classLength - 1
            val time = CLASS_START_TIME[i - 1] + "-" + CLASS_END_TIME[i + classLength - 2]
            val weekStart = Integer.parseInt(whichWeek[0].split(("-").toRegex(), 2).toTypedArray()[0])
            val weekEnd = Integer.parseInt(whichWeek[0].split(("-").toRegex(), 2).toTypedArray()[1])
            var doubleWeek = 0
            var singleWeek = 0
            when {
                whichWeek[1].replace(("\\s").toRegex(), "").replace("\\s\\s", "").replace(" ", "") == "(单)" -> {
                    singleWeek = 1
                }
                whichWeek[1].replace(("\\s").toRegex(), "").replace("\\s\\s", "").replace(" ", "") == "(双)" -> {
                    doubleWeek = 1
                }
                else -> {
                    singleWeek = 1
                    doubleWeek = 1
                }
            }
            var found = false
            var colorId = 0
            for (j in 0 until classTitles.size) {
                if (title == classTitles[j]) {
                    found = true
                    colorId = classColors[j]
                }
            }
            if (!found) {
                colorId = classCount
                classColors.add(classCount)
                classCount++
                classTitles.add(title)
            }

            results.add(ClassBean(
                    UUID.randomUUID().toString(),
                    title,
                    time,
                    location,
                    -1,
                    date,
                    -1,
                    false,
                    teacher,
                    weekStart,
                    weekEnd,
                    singleWeek,
                    doubleWeek,
                    i,
                    timeEnd,
                    classNumber,
                    colorId))
        }
        return results
    }

    private companion object {
        const val BASE_CLASS_TABLE_URL = "http://ssfw.tjut.edu.cn/ssfw/pkgl/kcbxx/4/"
        const val BASE_CLASS_TABLE_URL_SUFFIX = ".do"
    }
}