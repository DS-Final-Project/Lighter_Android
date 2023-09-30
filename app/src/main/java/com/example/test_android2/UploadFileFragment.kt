package com.example.test_android2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test_android2.data.Chat
import com.example.test_android2.data.ChatData
import com.example.test_android2.data.ResponseChat
import com.example.test_android2.data.ServiceCreator
import com.example.test_android2.databinding.FragmentUploadFileBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class UploadFileFragment : Fragment(), ConfirmDialogInterface {

    private var _binding: FragmentUploadFileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadFileBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    companion object {
        private const val FILE_REQUEST_CODE = 1001
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
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"  // 모든 파일 타입 선택 가능하도록
        startActivityForResult(intent, FILE_REQUEST_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { fileUri -> //val fileName = getFileNameFromUri(fileUri)
                if (fileUri != null) { // 파일 이름 받아와서 보여주기
                    with(binding) {
                        btnUpload.visibility = View.INVISIBLE
                        layoutFile.visibility = View.VISIBLE
                        tvExplain1.visibility = View.INVISIBLE
                        tvExplain2.visibility = View.INVISIBLE
                        tvFileName.visibility = View.VISIBLE
                        tvFileName.text = fileUri.toString()
                    }
                }
            }
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

