package com.neko.changelog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neko.v2ray.R

class ChangelogAdapter(private val changelogList: List<ChangelogEntry>) :
    RecyclerView.Adapter<ChangelogAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val versionText: TextView = view.findViewById(R.id.versionText)
        val dateText: TextView = view.findViewById(R.id.dateText)
        val changesList: TextView = view.findViewById(R.id.changesList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_changelog_entry, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = changelogList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = changelogList[position]
        holder.versionText.text = "Version ${entry.version}"
        holder.dateText.text = entry.date
        holder.changesList.text = entry.changes.joinToString("\n") { "• $it" }
    }
}
