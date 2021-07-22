package com.example.bookapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookapp.databinding.ItemBookBinding
import com.example.bookapp.databinding.ItemHistoryBinding
import com.example.bookapp.model.Book
import com.example.bookapp.model.History

class HistoryAdapter(val historyDeleteClickedListener:(String)->Unit): ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {
    //생성자에 historyDeleteClickedListener:String을 인자로 받고 리턴값은 없는
    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        //뷰홀더: 내가 넣고자하는 data를 실제 레이아웃의 데이터로 연결시키는 기능
        fun bind(historyModel: History) {//view와 데이터를 연결시키는 함수-/>뷰에 데이터 넣음
            binding.historyKeywordTextView.text=historyModel.keyword

            binding.historyKeywordDeleteButton.setOnClickListener { //여기서 호출
                historyDeleteClickedListener(historyModel.keyword.orEmpty()) //?todo
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        //미리 만들어진 뷰홀더가 없는 경우 새로 생성하는 함수(레이아웃 생성)
        return HistoryItemViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        //실제로 뷰홀더가 뷰에 그려졌을때 데이터를 뿌려주는 바인드해주는 함수(뷰홀더가 재활용될때 실행)
        holder.bind(currentList[position])
    }

    //diffutil -> 새로운 아이템 할당할지 말지 판단해주는 ->콜백 구현해야함
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }


        }
    }
}
