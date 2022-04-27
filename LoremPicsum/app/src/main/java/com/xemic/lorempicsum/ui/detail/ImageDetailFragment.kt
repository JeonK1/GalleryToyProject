package com.xemic.lorempicsum.ui.detail

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.work.*
import com.xemic.lorempicsum.R
import com.xemic.lorempicsum.common.base.BaseFragment
import com.xemic.lorempicsum.common.constant.CustomViewConst
import com.xemic.lorempicsum.common.constant.DownloadWorkerConst
import com.xemic.lorempicsum.common.constant.PermissionConst
import com.xemic.lorempicsum.common.custom.OnStopTrackingTouchListener
import com.xemic.lorempicsum.common.custom.TwoChoiceButton
import com.xemic.lorempicsum.common.ext.setImageByUrl
import com.xemic.lorempicsum.common.util.DownloadWorker
import com.xemic.lorempicsum.databinding.FragmentImageDetailBinding
import com.xemic.lorempicsum.viewmodel.ImageDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

/*** Image 의 세부 정보를 확인하는 Fragment ***/
@AndroidEntryPoint
class ImageDetailFragment :
    BaseFragment<FragmentImageDetailBinding>(FragmentImageDetailBinding::inflate) {

    private val viewModel: ImageDetailViewModel by viewModels()
    private val args: ImageDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        // image update to viewmodel
        viewModel.updateImage(args.imageId)

        // image update
        viewModel.image.observe(viewLifecycleOwner) { imageInfo ->
            binding.imageInfo = imageInfo
        }

        // imageUrl update
        viewModel.imageUrl.observe(viewLifecycleOwner) { imageUrl ->
            binding.imageDetailMainImage.setImageByUrl(imageUrl)
        }

        // update UI event
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventChannel.collect { event ->
                when(event) {
                    is ImageDetailViewModel.ImageDetailEvent.MoveToBackStack -> {
                        // 이전 화면으로 되돌아기
                        navController.popBackStack()
                    }
                    is ImageDetailViewModel.ImageDetailEvent.UpdateLikeBtn -> {
                        // 좋아요 버튼 UI update
                        updateLikeBtn(event.isLike)
                    }
                }
            }
        }

        // seekbar onStop tracking listener
        binding.imageDetailBlurSeekBar.OnStopTrackingTouchListener {
            viewModel.updateBlurLevel(it?.progress?: 0)
        }

        // grayscale 버튼에서 Colored 클릭
        binding.imageDetailGrayscale.setOnLeftButtonClickListener {
            binding.imageDetailGrayscale.invalidateUI(TwoChoiceButton.CHOICE_LEFT)
            viewModel.clickColoredBtn()
        }

        // grayscale 버튼에서 Grayscale 클릭
        binding.imageDetailGrayscale.setOnRightButtonClickListener {
            binding.imageDetailGrayscale.invalidateUI(TwoChoiceButton.CHOICE_RIGHT)
            viewModel.clickGrayscaleBtn()
        }
        
        // 다운로드 버튼 클릭
        binding.imageDetailDownloadBtn.setOnClickListener {
            verifyStoragePermission()
            viewModel.imageUrl.value?.also { url ->
                donwloadImage(url)
            }
        }
    }

    // 이미지 다운로드 받기
    private fun donwloadImage(url: String) {
        val data = Data.Builder()
            .putString(DownloadWorkerConst.IMAGE_URL, url)
            .build()

        val workRequest: WorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setInputData(data)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        // set workmanager listener
        WorkManager
            .getInstance(requireContext())
            .getWorkInfoByIdLiveData(workRequest.id)
            .observe(viewLifecycleOwner) { workInfo ->
                if(workInfo != null && workInfo.state.isFinished){
                    when(workInfo.outputData.getInt(DownloadWorkerConst.WORK_RESULT, DownloadWorkerConst.RESULT_FAILED)){
                        DownloadWorkerConst.RESULT_OK -> {
                            Toast.makeText(requireContext(), "이미지가 성공적으로 다운로드 되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                        DownloadWorkerConst.RESULT_FAILED -> {
                            Toast.makeText(requireContext(), "이미지 다운로드에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        // enqueue workManager
        WorkManager
            .getInstance(requireContext())
            .enqueue(workRequest)
    }

    // Storage 권한 체크
    private fun verifyStoragePermission() {
        val permission =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                PermissionConst.PERMISSION_STORAGE,
                PermissionConst.REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    // 좋아요 버튼 업데이트
    private fun updateLikeBtn(isLike: Boolean) {
        if (isLike) {
            // 좋아요 버튼 노랗게 채우기
            binding.imageDetailLikeBtn.setBackgroundResource(R.drawable.ic_star_fill)
        } else {
            // 좋아요 버튼 색깔 비우기
            binding.imageDetailLikeBtn.setBackgroundResource(R.drawable.ic_star_empty)
        }
    }
}