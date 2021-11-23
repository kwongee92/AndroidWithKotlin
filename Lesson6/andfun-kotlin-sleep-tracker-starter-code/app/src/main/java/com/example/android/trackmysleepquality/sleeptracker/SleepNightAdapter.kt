package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SleepNightAdapter(val clickListener : SleepNightListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback())/*RecyclerView.Adapter<SleepNightAdapter.SleepNightsViewHolder>()*/ {

    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_ITEM = 1
/*    var data = listOf<SleepNight>()
        set(value){
            field = value
            notifyDataSetChanged()
        }*/


    private val adapterScope = CoroutineScope(Dispatchers.Default)
    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map {DataItem.SleepNightItem(it)}
            }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> SleepNightsViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }
/*    override fun getItemCount() = data.size*/

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)//data[position]
//        if(item.sleepQuality <= 1){
//            holder.textView.setTextColor(Color.RED)
//        }else{
//            //reset
//            holder.textView.setTextColor(Color.BLACK)
//        }
//        holder.textView.text = item.sleepQuality.toString()

//        holder.bind(item, clickListener)
        when (holder) {
            is SleepNightsViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight, clickListener)
            }
        }
    }


    /**
     * ViewHolder that holds a single [TextView].
     *
     * A ViewHolder holds a view for the [RecyclerView] as well as providing additional information
     * to the RecyclerView such as where on the screen it was last drawn during scrolling.
     */
    class TextItemViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    class SleepNightsViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(
            item: SleepNight,
            clickListener: SleepNightListener
        ) {

            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
//            val res = itemView.context.resources
//            binding.sleepLength//itemView.findViewById(R.id.sleep_length)
//                .text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
//            binding.qualityString//itemView.findViewById(R.id.quality_string)
//                .text = convertNumericQualityToString(item.sleepQuality, res)
//
//            binding.qualityImage//itemView.findViewById(R.id.quality_image)
//                .setImageResource(
//                    when (item.sleepQuality) {
//                        0 -> R.drawable.ic_sleep_0
//                        1 -> R.drawable.ic_sleep_1
//                        2 -> R.drawable.ic_sleep_2
//                        3 -> R.drawable.ic_sleep_3
//                        4 -> R.drawable.ic_sleep_4
//                        5 -> R.drawable.ic_sleep_5
//                        else -> R.drawable.ic_sleep_active
//                    }
//                )
        }

        companion object {
            fun from(parent: ViewGroup): SleepNightsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater,parent,false)
               return SleepNightsViewHolder(binding)//SleepNightsViewHolder(view)
            }
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

class SleepNightDiffCallback :
    DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

sealed class DataItem{
    data class SleepNightItem(val sleepNight: SleepNight) : DataItem(){
        override val id = sleepNight.nightId
    }
    object Header:DataItem(){
        override val id = Long.MIN_VALUE
    }
    abstract val id:Long
}

