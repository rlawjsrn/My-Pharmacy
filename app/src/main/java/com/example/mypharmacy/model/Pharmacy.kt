package com.example.mypharmacy.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "response")
data class Response(
    @Element(name = "header")
    var header: Header? = null,

    @Element(name = "body")
    var body: Body? = null
)

@Xml(name = "header")
data class Header(
    @PropertyElement(name = "resultCode")
    var resultCode: String? = null,

    @PropertyElement(name = "resultMsg")
    var resultMsg: String? = null
)

@Xml(name = "body")
data class Body(
    @Element(name = "items")
    var items: Items? = null,

    @PropertyElement(name = "numOfRows")
    var numOfRows: String? = null,

    @PropertyElement(name = "pageNo")
    var pageNo: String? = null,

    @PropertyElement(name = "totalCount")
    var totalCount: String? = null
)

@Xml(name = "items")
data class Items(
    @Element(name = "item")
    var item: List<Item>? = null
)

@Xml(name = "item")
data class Item(
    @PropertyElement(name = "dutyAddr", writeAsCData = false)
    var dutyAddr: String? = null,

    @PropertyElement(name = "dutyMapimg", writeAsCData = false)
    var dutyMapimg: String? = null,

    @PropertyElement(name = "dutyName", writeAsCData = false)
    var dutyName: String? = null,

    @PropertyElement(name = "dutyTel1", writeAsCData = false)
    var dutyTel1: String? = null,

    @PropertyElement(name = "dutyTime1c", writeAsCData = false)
    var dutyTime1c: String? = null,

    @PropertyElement(name = "dutyTime1s", writeAsCData = false)
    var dutyTime1s: String? = null,

    @PropertyElement(name = "dutyTime2c", writeAsCData = false)
    var dutyTime2c: String? = null,

    @PropertyElement(name = "dutyTime2s", writeAsCData = false)
    var dutyTime2s: String? = null,

    @PropertyElement(name = "dutyTime3c", writeAsCData = false)
    var dutyTime3c: String? = null,

    @PropertyElement(name = "dutyTime3s", writeAsCData = false)
    var dutyTime3s: String? = null,

    @PropertyElement(name = "dutyTime4c", writeAsCData = false)
    var dutyTime4c: String? = null,

    @PropertyElement(name = "dutyTime4s", writeAsCData = false)
    var dutyTime4s: String? = null,

    @PropertyElement(name = "dutyTime5c", writeAsCData = false)
    var dutyTime5c: String? = null,

    @PropertyElement(name = "dutyTime5s", writeAsCData = false)
    var dutyTime5s: String? = null,

    @PropertyElement(name = "dutyTime6c", writeAsCData = false)
    var dutyTime6c: String? = null,

    @PropertyElement(name = "dutyTime6s", writeAsCData = false)
    var dutyTime6s: String? = null,

    @PropertyElement(name = "dutyTime7c", writeAsCData = false)
    var dutyTime7c: String? = null,

    @PropertyElement(name = "dutyTime7s", writeAsCData = false)
    var dutyTime7s: String? = null,

    @PropertyElement(name = "dutyTime8c", writeAsCData = false)
    var dutyTime8c: String? = null,

    @PropertyElement(name = "dutyTime8s", writeAsCData = false)
    var dutyTime8s: String? = null,

    @PropertyElement(name = "hpid", writeAsCData = false)
    var hpid: String? = null,

    @PropertyElement(name = "postCdn1", writeAsCData = false)
    var postCdn1: String? = null,

    @PropertyElement(name = "postCdn2", writeAsCData = false)
    var postCdn2: String? = null,

    @PropertyElement(name = "rnum", writeAsCData = false)
    var rnum: String? = null,

    @PropertyElement(name = "wgs84Lat", writeAsCData = false)
    var wgs84Lat: String? = null,

    @PropertyElement(name = "wgs84Lon", writeAsCData = false)
    var wgs84Lon: String? = null,

    @PropertyElement(name = "distance")
    var distance: Int? = null
)