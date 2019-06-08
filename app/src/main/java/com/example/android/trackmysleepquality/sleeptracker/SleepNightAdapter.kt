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
import java.lang.ClassCastException

// COMPLETED (04) Create variables for Header and SleepNight item types.
private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM   = 1

// COMPLETED (03) In ListAdapter<>, replace SleepNight with DataItem
// and SleepNightAdapter.ViewHolder with RecyclerView.ViewHolder.
class SleepNightAdapter(val clickListener: SleepNightListener):
        ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    // COMPLETED (09) Define a CoroutineScope with Dispatchers.Default.
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    // COMPLETED (10) Create a new addHeaderAndSubmitList() function, using the coroutine scope
    // you defined above, convert a List<SleepNight> to a List<DataItem>,
    // then submit the list to the adapter on the main thread.
    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when(list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    // COMPLETED (06) Change onBindViewHolder to take a RecyclerView.ViewHolder,
    // and change the item from a SleepNight to a DataItem.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(clickListener, nightItem.sleepNight)
            }
        }
    }

    // COMPLETED (05) Change onCreateViewHolder's return type to RecyclerView.ViewHolder,
    // and update it to return the correct view holder, based on the item's view type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM   -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    // COMPLETED (02) Copy and paste the TextViewHolder class from the exercise.
    class TextViewHolder (rootView: View): RecyclerView.ViewHolder(rootView) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)

                return TextViewHolder(view)
            }
        }
    } // close class TextViewHolder

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ViewHolder private constructor(val binding: ListItemSleepNightBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: SleepNightListener, item: SleepNight) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
// COMPLETED (08) Update DiffCallback to compare DataItem instead SleepNight objects.
class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return if (oldItem is DataItem.Header && newItem is DataItem.Header) {
            true
        } else if (oldItem is DataItem.SleepNightItem && newItem is DataItem.SleepNightItem) {
            oldItem  as DataItem.SleepNightItem == newItem as DataItem.SleepNightItem
        } else false
    }

} // close class SleepNightCallback

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

// COMPLETED (01) Add a sealed class called DataItem,
// containing a SleepNightItem data class, and a Header object.
/**
 * Class DataItem represents a single item that can be displayed in the RecyclerView.
 * Defines two concrete subclasses:
 *   - SleepNightItem: a wrapper for a SleepNight object, and
 *   - Header: a header singleton object
 * Both subclasses override an id property; in the case of SleepNightItem that's just the nightId,
 * in case of the (only) header, its a constant value.
 */
sealed class DataItem {
    data class SleepNightItem(val sleepNight: SleepNight): DataItem() {
        override val id = sleepNight.nightId
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE

        override fun equals(other: Any?): Boolean {
            return true
        }
    }

    abstract val id: Long
} // close class DataItem