package com.example.bookapp.model

import com.google.gson.annotations.SerializedName

data class Book (
    @SerializedName("itemid")val id:Long, //서버에서는 itemid라는 값을,  app에선 id라는 값으로
    @SerializedName("title")val title:String,
    @SerializedName("description")val description:String,
    @SerializedName("coverSmallUrl")val coverSmallUrl:String

)