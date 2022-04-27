package com.xemic.lorempicsum.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xemic.lorempicsum.database.ImageLike
import com.xemic.lorempicsum.models.retrofit.Image
import com.xemic.lorempicsum.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/*** Image 의 세부 정보를 확인하는 ViewModel ***/
@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    companion object {
        const val TAG = "ImageDetailViewModel"
    }

    // fragment 에 전송하는 channel
    private val _eventChannel = Channel<ImageDetailEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    // 현재 image 의 정보
    private var _image = MutableLiveData<Image>()
    val image: LiveData<Image> get() = _image

    // 현재 image 의 URL
    private var _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> get() = _imageUrl

    // image filter 값
    private var isColored = true  // 흑백 버튼 눌렀는지 (true = colored // false = grayscale)
    private var blurLevel = 0 // 현재 blur level

    // 현재 좋아요 상태
    private var isLike = false

    // UI 에서 id 보내주면 이를 바탕으로 image 값 업데이트
    fun updateImage(id: String) = viewModelScope.launch {
        repository.getImageInfo(id).let { response ->
            if(response.isSuccessful){
                // image update
                _image.postValue(response.body()!!)

                // imageUrl update
                _imageUrl.postValue(response.body()!!.download_url)

                // isLike update
                repository.checkIsLike(response.body()!!.id).first().let { _isLike ->
                    isLike = (_isLike == 1)
                    _eventChannel.send(ImageDetailEvent.UpdateLikeBtn(isLike))
                }
            } else {
                Log.e(TAG, "failed to get ImageList")
            }
        }
    }

    fun clickColoredBtn() = viewModelScope.launch {
        updateIsColored(true)
    }

    fun clickGrayscaleBtn() = viewModelScope.launch {
        updateIsColored(false)
    }

    fun clickBackBtn() = viewModelScope.launch {
        _eventChannel.send(ImageDetailEvent.MoveToBackStack)
    }

    fun clickLikeBtn() = viewModelScope.launch {
        image.value?.also {
            isLike = !isLike
            _eventChannel.send(ImageDetailEvent.UpdateLikeBtn(isLike))
            updateImageLike(it, isLike)
        }
    }

    // image 와 좋아요 정보 DB 업데이트
    private fun updateImageLike(image: Image, isLike: Boolean) = viewModelScope.launch {
        val isExists = repository.checkIsExists(image.id).first()
        if(isExists == 0){
            // not exists
            repository.insertImageLike(ImageLike(
                imageId = image.id,
                author = image.author,
                width = image.width,
                height = image.height,
                url = image.url,
                download_url = image.download_url,
                isLike = isLike
            ))
        } else {
            // exists
            repository.updateImageLike(ImageLike(
                imageId = image.id,
                author = image.author,
                width = image.width,
                height = image.height,
                url = image.url,
                download_url = image.download_url,
                isLike = isLike
            ))
        }
    }

    fun updateBlurLevel(_blurLevel: Int) {
        blurLevel = _blurLevel
        invalidateImage()
    }

    private fun updateIsColored(_isColored: Boolean) {
        isColored = _isColored
        invalidateImage()
    }

    private fun invalidateImage(){
        getCurrentImageUrl().also { currentImageUrl ->
            if(currentImageUrl != imageUrl.value) {
                _imageUrl.postValue(currentImageUrl)
            }
        }
    }

    private fun getCurrentImageUrl():String{
        return if(isColored && blurLevel == 0) {
            image.value!!.download_url
        } else if(isColored && blurLevel > 0) {
            "${image.value!!.download_url}?blur=${blurLevel}"
        } else if(!isColored && blurLevel == 0) {
            "${image.value!!.download_url}?grayscale"
        } else {
            "${image.value!!.download_url}?grayscale&blur=${blurLevel}"
        }
    }

    sealed class ImageDetailEvent {
        object MoveToBackStack: ImageDetailEvent()
        data class UpdateLikeBtn(val isLike: Boolean): ImageDetailEvent()
    }
}