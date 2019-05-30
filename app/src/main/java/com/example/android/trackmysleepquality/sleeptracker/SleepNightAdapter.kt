/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

// COMPLETED (06) Change RecyclerView.Adapter’s parameter to <SleepNightAdapter.ViewHolder>.
class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {

    // This is the data the adapter holds on to. It's a list of SleepNight objects, which - in turn -
    // is retrieved from the database as a LiveData object in the fragment's ViewModel.
    // Whenever the data in the db changes, the ViewModel
    var data = listOf<SleepNight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    // COMPLETED (08) Change onBindViewHolder’s holder parameter type to ViewHolder, and update views.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]  // that's a SleepNight object
        val res = holder.itemView.context.resources
        holder.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
        holder.sleepQuality.text = convertNumericQualityToString(item.sleepQuality, res)
        holder.qualityIcon.setImageResource(when(item.sleepQuality) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_active
        })
    } // close method onBindViewHolder()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // COMPLETED (07) Update view to inflate list_item_sleep_night, and change
        // return type to ViewHolder.
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
                .inflate(R.layout.list_item_sleep_night, parent, false)

        return ViewHolder(view)
    }

    // COMPLETED (04) Create a ViewHolder class that extends RecyclerView.ViewHolder.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // COMPLETED (05) Inside the ViewHolder, use findViewById() to create properties for sleepLength,
        // quality, and qualityImage.
        // Get & `hold` references to the list item views we'll later have to update.
        val qualityIcon:  ImageView = itemView.findViewById(R.id.quality_image)
        val sleepLength:  TextView  = itemView.findViewById(R.id.sleep_length)
        val sleepQuality: TextView  = itemView.findViewById(R.id.quality_string)

    } // close inner class ViewHolder

} // close class SleepNightAdapter
