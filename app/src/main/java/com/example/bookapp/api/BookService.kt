package com.example.bookapp.api

import com.example.bookapp.model.BestSellerDto
import com.example.bookapp.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("/api/search.api?&output=json")
    fun getBookbyName(
        @Query("key") apiKey:String,
        @Query("query") keyword: String

        ): Call<SearchBookDto>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSeller(
        @Query("key") apiKey:String

    ):Call<BestSellerDto>
    }
