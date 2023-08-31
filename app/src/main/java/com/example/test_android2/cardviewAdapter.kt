package com.example.test_android2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test_android2.data.ResponseSolution

class cardviewAdapter(
    var solutions: MutableList<ResponseSolution?>,
    var context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_FRIEND = 0
        private const val VIEW_TYPE_LOVER = 1
        private const val VIEW_TYPE_FAMILY = 2
        private const val VIEW_TYPE_COLLEAGUE = 3
        private const val VIEW_TYPE_DEFAULT = 4
    }

    override fun getItemCount(): Int {
        return solutions.size
    }

    override fun getItemViewType(position: Int): Int {
        val relation = solutions[position]?.relation
        return when (relation) {
            "friend" -> VIEW_TYPE_FRIEND
            "lover" -> VIEW_TYPE_LOVER
            "family" -> VIEW_TYPE_FAMILY
            "colleague" -> VIEW_TYPE_COLLEAGUE
            else -> VIEW_TYPE_DEFAULT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_FRIEND -> {
                val friendView = inflater.inflate(R.layout.cardview_friend, parent, false)
                FriendViewHolder(friendView)
            }
            VIEW_TYPE_LOVER -> {
                val loverView = inflater.inflate(R.layout.cardview_lover, parent, false)
                LoverViewHolder(loverView)
            }
            VIEW_TYPE_FAMILY -> {
                val familyView = inflater.inflate(R.layout.cardview_family, parent, false)
                FamilyViewHolder(familyView)
            }
            VIEW_TYPE_COLLEAGUE -> {
                val colleagueView = inflater.inflate(R.layout.cardview_colleague, parent, false)
                ColleagueViewHolder(colleagueView)
            }
            else -> {
                val defaultView = inflater.inflate(R.layout.cardview_none, parent, false)
                DefaultViewHolder(defaultView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val solution = solutions[position]

        when (holder) {
            is FriendViewHolder -> {
                if (solution != null) {
                    holder.title.text = solution.solutionTitle
                }
            }
            is LoverViewHolder -> {
                if (solution != null) {
                    holder.title.text = solution.solutionTitle
                }
                // Bind other lover-specific views here
            }
            is FamilyViewHolder -> {
                if (solution != null) {
                    holder.title.text = solution.solutionTitle
                }
                // Bind other family-specific views here
            }
            is ColleagueViewHolder -> {
                if (solution != null) {
                    holder.title.text = solution.solutionTitle
                }
                // Bind other colleague-specific views here
            }
            is DefaultViewHolder -> {
            }
        }
    }

    fun addCardView(solution: ResponseSolution?) {
        if (solution != null) {
            solutions.add(solution)
        }
        notifyItemInserted(solutions.size - 1)
    }

    inner class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
    }

    inner class LoverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        // Declare other lover-specific views here
    }

    inner class FamilyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        // Declare other family-specific views here
    }

    inner class ColleagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        // Declare other colleague-specific views here
    }

    inner class DefaultViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}
