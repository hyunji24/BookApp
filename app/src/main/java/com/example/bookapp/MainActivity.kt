package com.example.bookapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.adapter.BookAdapter
import com.example.bookapp.api.BookService
import com.example.bookapp.databinding.ActivityMainBinding
import com.example.bookapp.model.BestSellerDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter:BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter= BookAdapter()
        initBookRecyclerView()



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
                            //body가 있다면 그안에는 bestSellerDto가 들어있을것
                            Log.d(TAG,it.toString())

                            it.books.forEach{ book->
                                Log.d(TAG,book.toString())
                            }
                            adapter.submitList(it.books)
                            //ListAdapter는 내부적으로 AsyncListDiffer를 사용하면서, RecyclerView의 adapter처럼 이용이 가능합니다.
                            // 따라서 우리는 최종적으로 ListAdapter를 상속하는 adapter 클래스를 만들고, ListAdapter의 파라미터에 diffutil의 callback을 구현해서 넘겨주면
                            // 내부에서 submitlist( 바뀔 데이터 ) 라는 하나의 메서드로 모든 작업을 처리 할 수 있습니다!!!!
                        }


                    }

                })

    }

    fun initBookRecyclerView(){

        binding.bookRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.bookRecyclerView.adapter=adapter
    }
    companion object{
        private const val TAG="MainActivity"
    }
}