package com.example.bookapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book (
    @SerializedName("itemId")val id:Long, //서버에서는 itemid라는 값을,  app에선 id라는 값으로
    @SerializedName("title")val title:String,
    @SerializedName("description")val description:String,
    @SerializedName("coverSmallUrl")val coverSmallUrl:String

):Parcelable