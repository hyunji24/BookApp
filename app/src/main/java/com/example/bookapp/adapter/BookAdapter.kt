package com.example.bookapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookapp.databinding.ItemBookBinding
import com.example.bookapp.model.Book

class BookAdapter(private val itemClickedListener:(Book)->Unit):ListAdapter<Book,BookAdapter.BookItemViewHolder>(diffUtil) {
//어댑터: 데이터를 받아오고 이를 레이아웃에 직접 연결하는 함수를 실행시키는 클래스.
//diffUtil: 기존 리스트와 업데이트 된 리스트의 차이 계산하고 실제로 변환할 리스트 아이템들의 결과 반환하는 클래스
//ListAdapter<데이터클래스,리사이클러뷰 뷰홀더> 를 인자로 받는다-> 어댑터 내에서 리스트 정의하는게 아니라 리스트 자체에서 데이터리스트 정의하기 때문
    //그래서 getItemCount구현 안해도됨(일반RecyclerViewAdapter와 다르게)
    //사용할수 있는 메서드:
    //1. getCurrentList() : 현재 리스트를 반환한다.
    //2. onCurrentListChanged() : 리스트가 업데이트 되었을 때 실행할 콜백을 지정할 수 있다.
    //3. submitList(List<T>) : 리스트 데이터를 교체할 때 사용한다.
    //ListAdapter: difutil을 활용하여 리스트를 업데이트할수 있는 기능을 추가한 adapter,
    //diffutil사용하려면 diffutil.callback이라는 기능을 구현해야함

    inner class BookItemViewHolder(private val binding: ItemBookBinding):RecyclerView.ViewHolder(binding.root){
//뷰홀더: 내가 넣고자하는 data를 실제 레이아웃의 데이터로 연결시키는 기능
        fun bind(bookModel:Book){//view와 데이터를 연결시키는 함수-/>뷰에 데이터 넣음
            binding.titleTextview.text=bookModel.title
            binding.descriptionTextView.text=bookModel.description

            binding.root.setOnClickListener {
                itemClickedListener(bookModel)
            }
            Glide
                .with(binding.coverImageView.context) //context가 어댑터에 없다 -> 뷰에 있겠죠?
                .load(bookModel.coverSmallUrl)
                .into(binding.coverImageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        //미리 만들어진 뷰홀더가 없는 경우 새로 생성하는 함수(레이아웃 생성)
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        //실제로 뷰홀더가 뷰에 그려졌을때 데이터를 뿌려주는 바인드해주는 함수(뷰홀더가 재활용될때 실행)
        holder.bind(currentList[position])
    }

    //diffutil -> 새로운 아이템 할당할지 말지 판단해주는 ->콜백 구현해야함
    companion object{
        val diffUtil=object:DiffUtil.ItemCallback<Book>(){
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
               return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem==newItem
            }


        }
    }
}