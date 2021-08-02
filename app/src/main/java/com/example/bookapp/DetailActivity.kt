package com.example.bookapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.bookapp.databinding.ActivityDetailBinding
import com.example.bookapp.model.Book
import com.example.bookapp.model.Review

class DetailActivity :AppCompatActivity(){

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db= Room.databaseBuilder(applicationContext,AppDatabase::class.java,"BookSearchDB").build() //일단 여기까지

        //리사이클러뷰에 있는 데이터 가져와야됨 -> bookadapter

        val model=intent.getParcelableExtra<Book>("bookModel")

        binding.titleTextView.text=model?.title.orEmpty()
        binding.descriptionTextView.text=model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        Thread{
            val review=db.reviewDao().getOneReview(model?.id?.toInt()?:0) //리뷰에서 가져옴
            runOnUiThread{
                binding.reviewEditText.setText(review?.review.orEmpty()) //리뷰 데이터가 없을수도 있으므로 nullable처리
            }
        }.start()

        binding.saveButton.setOnClickListener{
            Thread{
                db.reviewDao().saveReview(
                    Review(model?.id?.toInt()?:0,
                    binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }

    }


}