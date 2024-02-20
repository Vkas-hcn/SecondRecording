package com.flie.best.tato.secondrecording

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class TabAdapter (val dataList: MutableList<AutoBean>) :
    RecyclerView.Adapter<TabAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: AppCompatTextView = itemView.findViewById(R.id.appCompatTextView)
        var imgEditText: ImageView = itemView.findViewById(R.id.img_edit)
        var imgDelete: ImageView = itemView.findViewById(R.id.img_delete)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(position)
                }
            }
            imgEditText.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemNameClick(position)
                }
            }
            imgDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemDeleteClick(position)
                }
            }
        }
    }

    // 定义点击事件的回调接口
    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onItemNameClick(position: Int)
        fun onItemDeleteClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null
    private var onItemNameClickListener: OnItemClickListener? = null
    private var onItemDeleteClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
        onItemNameClickListener = listener
        onItemDeleteClickListener = listener
    }

    // 在 item 点击事件中触发回调
    private fun onItemClick(position: Int) {
        onItemClickListener?.onItemClick(position)
    }
    private fun onItemNameClick(position: Int) {
        onItemNameClickListener?.onItemNameClick(position)
    }

    private fun onItemDeleteClick(position: Int) {
        onItemDeleteClickListener?.onItemDeleteClick(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView: View = inflater.inflate(R.layout.layout_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.tvName.text = item.name
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    //更新数据
    fun updateData(dataList: MutableList<AutoBean>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }
}