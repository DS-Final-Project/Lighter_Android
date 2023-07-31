import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.test_android2.ConfirmDialogInterface
import com.example.test_android2.RelationDialog
import com.example.test_android2.ResultAnalysisActivity
import com.example.test_android2.data.Chat
import com.example.test_android2.data.ChatData
import com.example.test_android2.data.ResponseChat
import com.example.test_android2.data.ServiceCreator
import com.example.test_android2.databinding.FragmentUploadImageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class UploadImageFragment : Fragment(), ConfirmDialogInterface {

    private var _binding: FragmentUploadImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadImageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        private const val PERMISSION_Album = 101 // 앨범 권한 처리
        private const val REQUEST_STORAGE = 102 // 갤러리 요청 코드
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initButtonClickEvent()
        uploadButtonClickEvent()
    }

    // +버튼 클릭 시
    private fun uploadButtonClickEvent() {
        binding.btnUpload.setOnClickListener {
            // Android 10 이상인 경우 권한 요청
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    openGallery()
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_Album
                    )
                }
            } else {
                // Android 10 미만인 경우 갤러리 열기
                openGallery()
            }
        }
    }

    //업로드 페이지에서 채팅 분석 버튼 클릭 시
    private fun initButtonClickEvent() = binding.btnAnalysis.setOnClickListener {
        //변수명 변경하기
        val chatImage = binding.tvPhotoName.text.toString()
        val chatData = ChatData(chatImage)
        showCustomDialog(chatData)
    }

    private fun chatNetwork(chatInfo: ChatData) {
        val call: Call<ResponseChat> = ServiceCreator.chatService.uploadChatImage(chatInfo)

        call.enqueue(object : Callback<ResponseChat> {
            override fun onResponse(
                call: Call<ResponseChat>, response: Response<ResponseChat>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { responseData ->
                        val data = responseData.data
                        val resultNum = data.resultNum
                        val doubtText1 = data.doubtText1
                        val doubtText2 = data.doubtText2
                        val doubtText3 = data.doubtText3
                        val doubtText4 = data.doubtText4
                        val doubtText5 = data.doubtText5
                        val avoidScore = data.avoidScore
                        val anxietyScore = data.anxietyScore
                        val testType = data.testType

                        var mychat = Chat(
                            resultNum,
                            doubtText1,
                            doubtText2,
                            doubtText3,
                            doubtText4,
                            doubtText5,
                            avoidScore,
                            anxietyScore,
                            testType
                        )
                        val intent = Intent(context, ResultAnalysisActivity::class.java)
                        intent.putExtra("mychat", mychat)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseChat>, t: Throwable) {
                Log.d("문장 분석 실패", t.message.toString())
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_Album) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "저장소 권한을 승인해야 앨범에서 이미지를 불러올 수 있습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQUEST_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_STORAGE -> {
                    data?.data?.let { uri ->
                        with(binding) {
                            btnUpload.visibility = View.INVISIBLE
                            imgPhoto.visibility = View.VISIBLE
                            tvExplain1.visibility = View.INVISIBLE
                            tvExplain2.visibility = View.INVISIBLE
                            tvPhotoName.visibility = View.VISIBLE
                            tvPhotoName.text = uri.toString()
                        }
                    }
                }
            }
        }
    }

    //로딩 바 초기 설정
    private fun init() {
        showProgress(false)
    }

    //로딩 바 보이기
    private fun showProgress(isShow: Boolean) {
        if (isShow) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    //다이얼로그 보이기
    private fun showCustomDialog(chatData: ChatData) {
        val dialog = RelationDialog(this, chatData)
        dialog.show(childFragmentManager, "relation_dialog")
    }

    //다이얼로그에서 완료 버튼 클릭 시
    override fun onOkButtonClick(chatData: ChatData) {
        chatNetwork(chatData)

        showProgress(true)
        binding.progressBar.bringToFront()
        thread(start = true) {
            Thread.sleep(3000)

        }
    }

    override fun onResume() {
        super.onResume() // Fragment가 다시 시작될 때 로딩 바(프로그레스 바)를 숨김
        showProgress(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
