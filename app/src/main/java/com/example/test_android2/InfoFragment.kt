import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.test_android2.R
import com.example.test_android2.ResultAnalysisActivity
import com.example.test_android2.SolutionActivity
import com.example.test_android2.databinding.FragmentInfoBinding
import com.example.test_android2.cardviewAdapter
import com.example.test_android2.data.*
import com.example.test_android2.googleLogin.goo
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null // 뷰 바인딩 변수 선언
    private val binding get() = _binding!! // 뷰 바인딩 가져오기
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: cardviewAdapter
    var solutions: MutableList<Solution?> = mutableListOf() // Initialize with an empty list

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false) // 뷰 바인딩 인플레이트
        val view = binding.root // 뷰 바인딩으로 뷰 참조

        viewPager = binding.viewPager2 // 뷰 바인딩으로 뷰 참조

        val models: MutableList<String> = mutableListOf()
        models.add("친구 간 대화방식이\n고민인가요? ")
        models.add("갈등으로 치우지지\n않으려면?")
        models.add("나는 왜 관계가\n어려울까?")
        models.add("불안정 애착\n극복하기")

        getCardView()

        val dpValue = 54
        val d = resources.displayMetrics.density
        val margin = (dpValue * d).toInt()

        adapter = cardviewAdapter(solutions, requireContext())
        viewPager.adapter = adapter
        viewPager.setClipToPadding(false)
        viewPager.setPadding(margin, 0, margin, 0)
        //viewPager.pageMargin = margin/2;

        val textView = binding.tv1
        val textData: String = textView.text.toString()
        val builder = SpannableStringBuilder(textData)
        val colorBlueSpan = ForegroundColorSpan(Color.rgb(244,172,63))
        builder.setSpan(colorBlueSpan, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = builder

        setupButtonClickListeners() // 버튼 클릭 리스너 설정

        return view
    }

    private fun setupButtonClickListeners() {
        val button1 = binding.call1
        val button2 = binding.call2
        val button3 = binding.call3

        button1.setOnClickListener {
            showDialog("청소년 사이버 상담센터","1388")
        }

        button2.setOnClickListener {
            showDialog("서울시 청소년 상담복지센터","02-2285-1318")
        }

        button3.setOnClickListener {
            showDialog("한국 여성의 전화","02-3156-5400")
        }
    }

    private fun showDialog(Cen: String, Num: String) {
        val dialogView = layoutInflater.inflate(R.layout.info_dialog, null)
        val tvCenter = dialogView.findViewById<TextView>(R.id.tv_center)
        val tvNum = dialogView.findViewById<TextView>(R.id.tv_num)
        val noBtn = dialogView.findViewById<Button>(R.id.noBtn)
        val yesBtn = dialogView.findViewById<Button>(R.id.yesBtn)

        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()
        //안 적으면 drawable 적용 안 됨
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        tvCenter.text = Cen
        tvNum.text = Num

        noBtn.setOnClickListener {
            dialog.dismiss()
        }

        yesBtn.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$Num"))
            startActivity(myIntent)
            dialog.dismiss()
        }

        dialog.show()
        //직접 크기 조절
        dialog.window?.setLayout(600, 500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 뷰 바인딩 해제
    }

    private fun getCardView() {
        val call: Call<ResponseSolution> = ServiceCreator.solutionService.getSolution()

        call.enqueue(object : Callback<ResponseSolution> {
            override fun onResponse(
                call: Call<ResponseSolution>, response: Response<ResponseSolution>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { responseData ->
                        // "data" 필드를 가져옴
                        val solutionsList = responseData?.data

                        if (solutionsList.isNullOrEmpty()) {
                            // 만약 데이터가 비어있는 경우 기본 뷰 홀더를 추가
                            val mysolution = Solution(
                                "none",
                                0,
                                "keyword",
                                "solutionTitle",
                                "solutionContent"
                            )
                            adapter.addCardView(mysolution)

                        } else {
                            solutionsList?.forEach { solutionObject ->
                                // 각 solutionObject에서 필요한 데이터 추출
                                val solutionId = solutionObject.solutionId ?: ""
                                val relation = solutionObject.relation ?: 0
                                val keyword = solutionObject.keyword ?: ""
                                val solutionTitle = solutionObject.solutionTitle ?: ""
                                val solutionContent = solutionObject.solutionContent ?: ""

                                // 추출한 데이터를 사용하여 Solution 객체를 생성
                                val mysolution = Solution(
                                    solutionId,
                                    relation,
                                    keyword,
                                    solutionTitle,
                                    solutionContent
                                )

                                // Solution 객체를 어댑터에 추가
                                adapter.addCardView(mysolution)

                                // Solution 객체의 정보를 로그에 출력
                                Log.d("Solution", mysolution.toString())
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<ResponseSolution>, t: Throwable) {
                Log.d("솔루션 실패", t.message.toString())
            }
        })
    }
}
