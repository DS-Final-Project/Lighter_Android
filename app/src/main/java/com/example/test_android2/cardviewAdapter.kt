package com.example.test_android2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.test_android2.data.Solution

class cardviewAdapter(
    private var solutions: MutableList<Solution?>,
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
            1 -> VIEW_TYPE_LOVER
            2 -> VIEW_TYPE_FRIEND
            3 -> VIEW_TYPE_FAMILY
            4 -> VIEW_TYPE_COLLEAGUE
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
                    holder.itemView.setOnClickListener {
                        // 클릭한 카드뷰의 내용을 보여주는 작업을 여기에 추가
                        // 예를 들어, 다음과 같이 Intent를 사용하여 다른 화면으로 이동할 수 있습니다.
                        val intent = Intent(context, SolutionActivity::class.java)
                        intent.putExtra("solution", solution) // 선택한 솔루션 데이터 전달
                        context.startActivity(intent)
                    }
                }
            }
            is LoverViewHolder -> {
                if (solution != null) {
                    holder.title.text = solution.solutionTitle
                    // Bind other lover-specific views here
                    holder.itemView.setOnClickListener {
                        // 클릭한 카드뷰의 내용을 보여주는 작업을 여기에 추가
                        val intent = Intent(context, SolutionActivity::class.java)
                        intent.putExtra("solution", solution) // 선택한 솔루션 데이터 전달
                        context.startActivity(intent)
                    }
                }
            }
            is FamilyViewHolder -> {
                if (solution != null) {
                    holder.title.text = solution.solutionTitle
                    // Bind other family-specific views here
                    holder.itemView.setOnClickListener {
                        // 클릭한 카드뷰의 내용을 보여주는 작업을 여기에 추가
                        val intent = Intent(context, SolutionActivity::class.java)
                        intent.putExtra("solution", solution) // 선택한 솔루션 데이터 전달
                        context.startActivity(intent)
                    }
                }
            }
            is ColleagueViewHolder -> {
                if (solution != null) {
                    holder.title.text = solution.solutionTitle
                    // Bind other colleague-specific views here
                    holder.itemView.setOnClickListener {
                        // 클릭한 카드뷰의 내용을 보여주는 작업을 여기에 추가
                        val intent = Intent(context, SolutionActivity::class.java)
                        intent.putExtra("solution", solution) // 선택한 솔루션 데이터 전달
                        context.startActivity(intent)
                    }
                }
            }
            is DefaultViewHolder -> {
                // DefaultViewHolder에 대한 처리 추가
                Toast.makeText(context, "먼저 채팅을 등록해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun addCardView(solution: Solution?) {
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
