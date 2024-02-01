package com.example.test_android2.upload.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.test_android2.analysisresult.ui.ResultAnalysisActivity
import com.example.test_android2.analysisresult.data.Chat
import com.example.test_android2.upload.data.ChatData
import com.example.test_android2.analysisresult.data.ResponseChat
import com.example.test_android2.ServiceCreator
import com.example.test_android2.databinding.FragmentUploadFileBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadFileFragment : Fragment(), ConfirmDialogInterface {

    private var _binding: FragmentUploadFileBinding? = null
    private val binding get() = _binding!!

    // 파일 open
    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openFileStorage()
            }
        }

    // 가져온 파일 uri 보여주기
    private val pickFileLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    with(binding) {
                        btnUpload.visibility = View.INVISIBLE
                        layoutFile.visibility = View.VISIBLE
                        tvExplain1.visibility = View.INVISIBLE
                        tvExplain2.visibility = View.INVISIBLE
                        tvFileName.visibility = View.VISIBLE
                        tvFileName.text = uri.toString()
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadFileBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initButtonClickEvent()
        uploadButtonClickEvent()
        deleteButtonClickEvent()

    }

    //+버튼 클릭 시
    private fun uploadButtonClickEvent() = binding.btnUpload.setOnClickListener {
        if (ContextCompat.checkSelfPermission(
                requireContext(), READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openFileStorage()
        } else {
            requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
        }
    }

    //업로드 페이지에서 채팅 분석 버튼 클릭 시
    private fun initButtonClickEvent() = binding.btnAnalysis.setOnClickListener {
        val chatFile = binding.tvFileName.text.toString()
        val chatData = ChatData(chatFile)
        showCustomDialog(chatData)
    }

    //파일 삭제 버튼 클릭 시
    private fun deleteButtonClickEvent() = binding.btnDelete.setOnClickListener {
        with(binding) {
            tvFileName.text = ""
            layoutFile.visibility = View.INVISIBLE
            tvFileName.visibility = View.INVISIBLE
            btnUpload.visibility = View.VISIBLE
            tvExplain1.visibility = View.VISIBLE
            tvExplain2.visibility = View.VISIBLE
        }
    }

    private fun chatNetwork(relation: Int, fileUri: Uri) { // 로딩 바 보이기
        showProgress(true)

        // 파일 Uri로부터 InputStream을 가져옵니다.
        val inputStream = context?.contentResolver?.openInputStream(fileUri) // InputStream을 MultipartBody.Part로 변환합니다.
        val requestFile = okhttp3.RequestBody.create(
            "text/plain".toMediaTypeOrNull(), inputStream!!.readBytes()
        )
        val filePart = MultipartBody.Part.createFormData("file", "uploaded_file.txt", requestFile)

        // relation 값을 RequestBody로 변환합니다.
        val relationRequestBody = okhttp3.RequestBody.create(
            "text/plain".toMediaTypeOrNull(), relation.toString()
        )

        val call: Call<ResponseChat> = ServiceCreator.chatService.uploadChat(filePart, relationRequestBody)

        call.enqueue(object : Callback<ResponseChat> {
            override fun onResponse(
                call: Call<ResponseChat>, response: Response<ResponseChat>
            ) { // 로딩 바 숨기기
                showProgress(false)

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
                        val relation = data.relation

                        var mychat = Chat(
                            resultNum,
                            doubtText1,
                            doubtText2,
                            doubtText3,
                            doubtText4,
                            doubtText5,
                            avoidScore,
                            anxietyScore,
                            testType,
                            relation
                        )
                        val intent = Intent(context, ResultAnalysisActivity::class.java)
                        intent.putExtra("mychat", mychat)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseChat>, t: Throwable) {
                showProgress(false)
                Log.d("문장 분석 실패", t.message.toString())
            }
        })
    }

    private fun openFileStorage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "text/*"
        pickFileLauncher.launch(intent)
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
        val fileUri = Uri.parse(binding.tvFileName.text.toString())
        chatNetwork(chatData.relation, fileUri)
    }

    //파일 명을 보낼 경우 사용하는 함수
    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null
        context?.contentResolver?.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = cursor.getString(displayNameIndex)
            }
        }
        return fileName
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
