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
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding


// adapter class will receive a list item clickListener from the fragment for clicking on a list item
class SleepNightAdapter(val clickListener: SleepNightListener) : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    // sets the position/index of list to display viewholder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // position is passed by RecyclerView for use here
        // getItem is a helper provided by ListAdapter
        val item = getItem(position)
        // updating and modifying the views is delegated to the ViewHolder
        // this hides the details of how to handle the view so the Adapter doesn't have to worry about them.
        holder.bind(item, clickListener)
    }

    // onCreateViewHolder returns the ViewHolder defined in this class
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // arguments are parent - what viewgroup the view will be placed in before it gets displayed to the screen
        // inflate the view using view layout created in res/layout folder:
        return ViewHolder.from(parent)
    }

    // Extend RecyclerView.ViewHolder to get performance magic and enhancements on the views passed in and used
    // constructor is made private since there is not a need to call it with the added from companion object method to inflate views
    // the val in front of binding makes it a property
    // pass in the root view of the binding object to the parent ViewHolder (since it does not know about binding objects and needs a view)
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding): RecyclerView.ViewHolder(binding.root) {

        // this extension function was created to support using more than one view in onBindViewHolder
        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            // Tell binding object about new list item
            // sleep is the variable name you gave to the item in the <data><variable> block in the list item layout xml as the view to use with the ViewHolder
            binding.sleep = item
            // getting the click listener passed into adapter from the view to get it to each ViewHolder and let data binding know about it:
            binding.clickListener = clickListener
            // always execute pending bindings as an optimization when using binding adapters with a recycler view:
            binding.executePendingBindings()
        }

        // method to inflate view so onCreateViewHolder doesn't have to know about details is put in companion object to be able to call it off of the class instead of an instance of the class
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                // this is not needed after data binding added to list item layout xml;
                //val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)

                // Note: always pass the parent and false as the second and third arguments
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)

                // 2) RETURN VIEWHOLDER WITH VIEW INFO
                // return the ViewHolder instance passing in the view created:
                return ViewHolder(binding)
            }
        }
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        // check the ids of items if they have unique ids:
        // only check the ids here since this is seeing if items have been moved, replaced, deleted, etc. - check contents in the next method
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        // because the items are data classes, builtin equality helper is used for deep equality check
        return oldItem == newItem
    }

}

// Click Listener class to listen for clicks and notify the ViewModel with passed on data for handling them.
// When user selected an item the onClick method will be triggered with the selected item - this callback will be used to have the ViewHolder inform the Fragment that a click happened
// This class is essentially a fancy way of defining and allowing the passing of a lambda(from the fragment) to the Adapter class and does not need to hold a reference to the view
class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}