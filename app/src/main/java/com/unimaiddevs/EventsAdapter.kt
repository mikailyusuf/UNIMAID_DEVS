package com.unimaiddevs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_layout.view.*

class EventsAdapter(private val ItemsList: List<DataModel>, val listener: OnUserClick) :
    RecyclerView.Adapter<EventsAdapter.RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        val items = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.event_layout, parent
                , false
            )

        return RecyclerViewHolder(items)

    }

    override fun getItemCount(): Int {
        return ItemsList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val currentItem = ItemsList[position]
        holder.initialise(ItemsList.get(position), listener)
    }

    class RecyclerViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val host: TextView = itemview.hostId
        val image: ImageView = itemview.eventImage

        fun initialise(datamodel: DataModel, listener: OnUserClick) {
            host.text = datamodel.host
            Picasso.get().load(datamodel.image).into(image)

            itemView.setOnClickListener {
                listener.onUserClick(datamodel, adapterPosition)
            }
            itemView.setOnClickListener {
                listener.onUserClick(datamodel, adapterPosition)
            }

        }
    }
}

interface OnUserClick {
    fun onUserClick(datamodel: DataModel, position: Int)
}
