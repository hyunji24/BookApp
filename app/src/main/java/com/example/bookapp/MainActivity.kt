package com.example.bookapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bookapp.api.BookService
import com.example.bookapp.model.BestSellerDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit= Retrofit.Builder()
                .baseUrl("https://book.interpark.com")
                .addConverterFactory(GsonConverterFactory.create()) // Json데이터를 사용자가 정의한 Java 객채로 변환해주는 라이브러리
                .build() //레트로핏 구현체 완성!

        val bookService=retrofit.create(BookService::class.java)

        bookService.getBestSeller("38845BE9BD0EBEDF271A2D5BC770C5BEEBB2D38910F504545CE384C6692DA6D4")
                .enqueue(object: Callback<BestSellerDto>{
                    override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                        //todo 실패처리
                        Log.d(TAG,t.toString())
                    }

                    override fun onResponse(call: Call<BestSellerDto>, response: Response<BestSellerDto>) {
                        //todo 성공처리

                        if(response.isSuccessful.not()){
                            return
                        }
                        response.body()?.let{
                            Log.d(TAG,it.toString())

                            it.books.forEach{ book->
                                Log.d(TAG,book.toString())
                            }
                        }
                    }

                })

    }

    companion object{
        private const val TAG="MainActivity"
    }
}