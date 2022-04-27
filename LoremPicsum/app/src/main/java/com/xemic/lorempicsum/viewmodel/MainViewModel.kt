package com.xemic.lorempicsum.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xemic.lorempicsum.common.constant.ImagePageConst
import com.xemic.lorempicsum.models.ImagePage
import com.xemic.lorempicsum.models.ImageSimple
import com.xemic.lorempicsum.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/*** Image 전체 리스트를 확인하는 ViewModel ***/
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    // fragment 에 전송하는 channel
    private val _eventChannel = Channel<MainEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    // 전체 image List
    private var _imageListPages = MutableLiveData<List<ImagePage>>()
    val imagePages: LiveData<List<ImagePage>> get() = _imageListPages

    // 총 페이지
    private var _totalPage = MutableLiveData(1)
    val totalPage: LiveData<Int> get() = _totalPage
    
    // 현재 페이지
    private var _currentPage = MutableLiveData(1)
    val currentPage: LiveData<Int> get() = _currentPage

    // 현재 좋아요 필터 상태
    var isLike = false

    init {
        initImageList()
    }

    fun clickImage(id: String) = viewModelScope.launch {
        _eventChannel.send(MainEvent.MoveToDetail(id))
    }

    fun clickLikeBtn() = viewModelScope.launch {
        isLike = !isLike
        _eventChannel.send(MainEvent.UpdateLikeBtn(isLike))
        if (isLike) {
            initLikedImageList()
        } else {
            initImageList()
        }
    }

    fun updateCurrentPage(page: Int) = viewModelScope.launch {
        _currentPage.postValue(page)
    }

    // 좋아요 누른 이미지 리스트로 초기화하기
    private fun initLikedImageList() = viewModelScope.launch {
        _totalPage.postValue(1)
        _currentPage.postValue(1)

        val imagePageList = mutableListOf<ImagePage>()
        for (page in 1..ImagePageConst.INITIAL_PAGE_COUNT) {
            val imageList = repository.getLikedImageList(page).first().map { imageLike ->
                ImageSimple(
                    id = imageLike.imageId,
                    imageUrl = imageLike.download_url
                )
            }
            if (imageList.isNotEmpty()) {
                imagePageList.add(ImagePage(imageList))
                _totalPage.postValue(page)
            } else {
                break
            }
        }

        if(imagePageList.isEmpty()){
            _eventChannel.send(MainEvent.UpdateNonLikeText(true))
        } else {
            _eventChannel.send(MainEvent.UpdateNonLikeText(false))
        }

        _imageListPages.postValue(imagePageList)
    }

    // 전체 이미지 리스트로 초기화하기
    private fun initImageList() = viewModelScope.launch {
        _totalPage.postValue(1)
        _currentPage.postValue(1)

        val imagePageList = mutableListOf<ImagePage>()
        for (page in 1..ImagePageConst.INITIAL_PAGE_COUNT) {
            val response = repository.getImageList(page)
            if (response.isSuccessful && response.body()!!.isNotEmpty()) {
                imagePageList.add(ImagePage(response.body()!!.map { imageLike ->
                    ImageSimple(
                        id = imageLike.id,
                        imageUrl = imageLike.download_url
                    )
                }))
                _totalPage.postValue(page)
            } else if (response.body()!!.isEmpty()) {
                break
            } else {
                Log.e(TAG, "failed to get ImageList")
                break
            }
        }

        _imageListPages.postValue(imagePageList)
    }

    sealed class MainEvent {
        data class UpdateLikeBtn(val isLike: Boolean) : MainEvent()
        data class UpdateNonLikeText(val flag: Boolean) : MainEvent()
        data class MoveToDetail(val imageId: String) : MainEvent()
    }
}