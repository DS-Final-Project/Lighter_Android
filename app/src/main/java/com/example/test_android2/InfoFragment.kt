import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.test_android2.databinding.FragmentInfoBinding
import com.example.test_android2.cardviewAdapter

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null // 뷰 바인딩 변수 선언
    private val binding get() = _binding!! // 뷰 바인딩 가져오기
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: cardviewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false) // 뷰 바인딩 인플레이트
        val view = binding.root // 뷰 바인딩으로 뷰 참조

        viewPager = binding.viewPager2 // 뷰 바인딩으로 뷰 참조

        val models: MutableList<String> = mutableListOf()
        models.add("친구 간 대화방식이\n 고민인가요? ")
        models.add("갈등으로 치우지지\n 않으려면?")
        models.add("나는 왜 관계가\n 어려울까?")
        models.add("불안정 애착\n 극복하기")

        val dpValue = 54
        val d = resources.displayMetrics.density
        val margin = (dpValue * d).toInt()

        adapter = cardviewAdapter(models, requireContext())
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
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:1388"))
            startActivity(myIntent)
        }

        button2.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:02-2285-1318"))
            startActivity(myIntent)
        }

        button3.setOnClickListener {
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:02-3156-5400"))
            startActivity(myIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 뷰 바인딩 해제
    }
}
