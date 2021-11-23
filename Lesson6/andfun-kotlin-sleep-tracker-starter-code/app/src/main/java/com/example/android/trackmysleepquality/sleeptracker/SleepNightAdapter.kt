package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding


class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.SleepNightsViewHolder>(SleepNightDiffCallback())/*RecyclerView.Adapter<SleepNightAdapter.SleepNightsViewHolder>()*/ {

/*    var data = listOf<SleepNight>()
        set(value){
            field = value
            notifyDataSetChanged()
        }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepNightsViewHolder {
        return SleepNightsViewHolder.from(parent)
    }

/*    override fun getItemCount() = data.size*/

    override fun onBindViewHolder(holder: SleepNightsViewHolder, position: Int) {
        val item = getItem(position)//data[position]
//        if(item.sleepQuality <= 1){
//            holder.textView.setTextColor(Color.RED)
//        }else{
//            //reset
//            holder.textView.setTextColor(Color.BLACK)
//        }
//        holder.textView.text = item.sleepQuality.toString()

        holder.bind(item)
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
            item: SleepNight
        ) {

            binding.sleep = item
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


}
class SleepNightDiffCallback :
    DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }

}
