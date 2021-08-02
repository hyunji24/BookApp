package com.example.bookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.bookapp.adapter.BookAdapter
import com.example.bookapp.adapter.HistoryAdapter
import com.example.bookapp.api.BookService
import com.example.bookapp.databinding.ActivityMainBinding
import com.example.bookapp.model.BestSellerDto
import com.example.bookapp.model.History
import com.example.bookapp.model.SearchBookDto
import kotlinx.android.synthetic.main.item_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.core.view.isVisible as isVisible1

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter:BookAdapter
    private lateinit var historyadapter: HistoryAdapter
    private lateinit var bookService: BookService
    private lateinit var db:AppDatabase
    //room 과 관련된 액션은 Thread,AsyncTask등을 이용해 백그라운드에서 작업해야함

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initBookRecyclerView()
        initHistoryRecyclerView()

        db= Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB" //history 테이블과 리뷰 추가 예정
        ).build()

        val retrofit= Retrofit.Builder()
                .baseUrl("https://book.interpark.com")
                .addConverterFactory(GsonConverterFactory.create()) // Json데이터를 사용자가 정의한 Java 객채로 변환해주는 라이브러리
                .build() //레트로핏 구현체 완성!

        bookService=retrofit.create(BookService::class.java)


        bookService.getBestSeller(getString(R.string.interparkAPIKey))
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
        adapter= BookAdapter(itemClickedListener={
            val intent= Intent(this,DetailActivity::class.java)
            intent.putExtra("bookModel",it)
            startActivity(intent)
        })

        binding.bookRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.bookRecyclerView.adapter=adapter
    }

    fun initHistoryRecyclerView(){
        //historyDeleteClickedLIstener라는 람다를 구현해주어야 함
        historyadapter=HistoryAdapter(historyDeleteClickedListener = {
            deleteSearchKeyword(it)
        })

        binding.historyRecyclerView.layoutManager=LinearLayoutManager(this)
        binding.historyRecyclerView.adapter=historyadapter
        initSearchEditText()
    }

    private fun initSearchEditText(){
        binding.searchEditText.setOnKeyListener { view, keyCode, event -> //EditText에서 어떤 키 눌렀을때 리스터
            if(keyCode== KeyEvent.KEYCODE_ENTER && event.action== MotionEvent.ACTION_DOWN){  //action_down: 처음 눌렀을때
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true //setonkeylistener는 반환값있어야함! true는 이 이벤트를 처리했음을 ㅇ의미
            }
            return@setOnKeyListener false //키이벤트가 실행이 안됐고
        }

        binding.searchEditText.setOnTouchListener{v,event->
            if(event.action==MotionEvent.ACTION_DOWN){ //처음 눌렸을때
                showHistoryView()
                //return@setOnTouchListener true ->actiondown이란 버튼에 대해서 이벤트가 이미 처리돼서 실제로 터치가 되는 과정이 처리되지 않기때문에 없앰
                //키패드가 올라올 수 있도록 true반환하지 x
            }
            return@setOnTouchListener false
        }
    }

    private fun search(keyword:String){
        bookService.getBookbyName(getString(R.string.interparkAPIKey),keyword)
            .enqueue(object: Callback<SearchBookDto>{
                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    //todo 실패처리
                    hideHistoryView()
                    Log.d(TAG,t.toString())
                }

                override fun onResponse(call: Call<SearchBookDto>, response: Response<SearchBookDto>) {
                    //todo 성공처리

                    hideHistoryView()
                    saveSearchKeyword(keyword) //이때 데이터를 저장하면됨

                    if(response.isSuccessful.not()){
                        return
                    }
                    response.body()?.let{
                        //body가 있다면 그안에는 bestSellerDto가 들어있을것

                        adapter.submitList(response.body()?.books.orEmpty()) //반환할 북 없으면 null을 빈값으로 변경
                        //ListAdapter는 내부적으로 AsyncListDiffer를 사용하면서, RecyclerView의 adapter처럼 이용이 가능합니다.
                        // 따라서 우리는 최종적으로 ListAdapter를 상속하는 adapter 클래스를 만들고, ListAdapter의 파라미터에 diffutil의 callback을 구현해서 넘겨주면
                        // 내부에서 submitlist( 바뀔 데이터 ) 라는 하나의 메서드로 모든 작업을 처리 할 수 있습니다!!!!
                    }


                }

            })
    }

    private fun showHistoryView(){

        Thread{ //db에 넣은거 가져온 다음 보여줘야됨
           val keywords= db.historyDao().getAll().reversed() //history의 리스트 형식,reversed통해 최신순서대로

            runOnUiThread{ //?todo
                binding.historyRecyclerView.isVisible1 =true
                historyadapter.submitList(keywords.orEmpty()) //혹시 null처리
            }
        }.start()

    }
    private fun hideHistoryView(){
        binding.historyRecyclerView.isVisible1 =false

    }

    private fun saveSearchKeyword(keyword:String){
        Thread{
            db.historyDao().insertHistory(History(null,keyword))
        }.start() //history db에 검색한 키워드 하나 저장
    }

    private fun deleteSearchKeyword(keyword:String){
        Thread{
            db.historyDao().delete(keyword)
            showHistoryView() //뷰 갱신

        }.start()
    }

    companion object{
        private const val TAG="MainActivity"
    }
}