package rw.nexin.codingactivity.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.day_data_layout.view.*

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import rw.nexin.codingactivity.R

class RecordsAdapter(private var context: Context, records: JSONArray) : RecyclerView.Adapter<RecordsAdapter.ViewHolder>() {

    private var records = JSONArray()

    init {
        this.records = records
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.day_data_layout, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        try {
            val record = records.getJSONObject(i)
            val grandTotal = record.getJSONObject("grand_total")
            val range = record.getJSONObject("range")

            val day: String
            val duration: String

            day = range.getString("text")
            duration = grandTotal.getString("text")

            viewHolder.dayView.text = day
            viewHolder.durationView.text = duration

        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return records.length()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val dayView: TextView = itemView.day_view
        val durationView: TextView = itemView.summary_view

    }
}
