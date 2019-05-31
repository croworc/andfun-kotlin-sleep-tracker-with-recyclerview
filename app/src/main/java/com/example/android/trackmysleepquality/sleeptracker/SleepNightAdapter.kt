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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    // COMPLETED (03) Update SleepNightAdapter class to extend ListAdapter.

    // COMPLETED (04) Delete the data field and getItemCount() function.


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // COMPLETED (05) Replace data[position] with getItem().
        val item = getItem(position)

        holder.bind(item)
    } // close function bind()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor (itemView: View) : RecyclerView.ViewHolder(itemView){

        // Retrieve & hold references onto the list item views we'll need to update later on.
        private val sleepLength:  TextView  = itemView.findViewById(R.id.sleep_length)
        private val quality:      TextView  = itemView.findViewById(R.id.quality_string)
        private val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)

        fun bind(item: SleepNight) {
            // The list item's root view (ConstraintLayout, in this case) will provide us with
            // access to the resources.
            val res = itemView.context.resources

            sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            quality.text     = convertNumericQualityToString(item.sleepQuality, res)
            qualityImage.setImageResource(when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })
        } // close function bind()

        companion object {
            /**
             * Inflates the layout for the list item:
             * a single row, holding one sleep recording record
             */
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                        .inflate(R.layout.list_item_sleep_night, parent, false)

                return ViewHolder(view)
            }
        } // close ViewHolder's companion object

    } // close inner class ViewHolder

    // COMPLETED (01) Create a new class called SleepNightDiffCallback that extends
    // DiffUtil.ItemCallback<SleepNight>.
    class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {

        // COMPLETED (02) In SleepNightDiffCallback, override areItemsTheSame() and areContentsTheSame().

        override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            // Two SleepNight objects will be compared on the basis of the identifying property,
            // nightId in this case.
            return oldItem.nightId == newItem.nightId
        }

        override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            // Two SleepNight objects will be compared on a per-property basis.
            // As the SleepNight class is a data class, we can just compare two objects w/ the
            // equality operator.
            return oldItem == newItem
        }
    } // close inner class SleepNightDiffCallback

} // close outer class SleepNightAdapter
