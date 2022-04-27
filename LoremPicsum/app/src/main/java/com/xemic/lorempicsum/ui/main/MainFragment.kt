package com.xemic.lorempicsum.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.xemic.lorempicsum.viewmodel.MainViewModel
import com.xemic.lorempicsum.R
import com.xemic.lorempicsum.common.base.BaseFragment
import com.xemic.lorempicsum.databinding.FragmentMainBinding
import com.xemic.lorempicsum.ui.main.adapter.ImageListViewPagerAdapter
import com.xemic.lorempicsum.ui.main.adapter.ImagePageAdapter
import dagger.hilt.android.AndroidEntryPoint

/*** Image 전체 리스트를 확인하는 Fragment ***/
@AndroidEntryPoint
class MainFragment :
    BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        // imageListPages update
        viewModel.imagePages.observe(viewLifecycleOwner) { imageListPages ->
            binding.mainViewpager2.adapter = ImageListViewPagerAdapter(imageListPages).apply {
                pageItemClickListener = object : ImageListViewPagerAdapter.OnItemClickListener {
                    override fun onItemClick(
                        holder: ImagePageAdapter.ViewHolder,
                        view: View,
                        position: Int,
                        page: Int
                    ) {
                        // 이미지 클릭 했을 때
                        viewModel.clickImage(imageListPages[page].imageList[position].id)
                    }
                }
            }
        }

        // totalPage update
        viewModel.totalPage.observe(viewLifecycleOwner) { totalPage ->
            updateTotalPage(totalPage)
        }

        // currentPage update
        viewModel.currentPage.observe(viewLifecycleOwner) { currentPage ->
            updateCurrentPage(currentPage)
        }

        // update currentPage when viewpager2 page changed
        binding.mainViewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.updateCurrentPage(position+1)
            }
        })

        // update UI event
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventChannel.collect { event ->
                when (event) {
                    is MainViewModel.MainEvent.MoveToDetail -> {
                        // detail 화면으로 이동하기
                        moveToDetail(event.imageId)
                    }
                    is MainViewModel.MainEvent.UpdateLikeBtn -> {
                        // 좋아요 필터 버튼 UI update
                        updateLikeBtn(event.isLike)
                    }
                    is MainViewModel.MainEvent.UpdateNonLikeText -> {
                        // 이미지가 존재하지 않습니다 문장 UI update
                        updateNonLikedImageText(event.flag)
                    }
                }
            }
        }
    }

    // current page TextView update
    private fun updateCurrentPage(page: Int) {
        binding.mainHeaderCurrentPage.text = "$page"
    }

    // total page TextView update
    private fun updateTotalPage(page: Int) {
        binding.mainHeaderTotalPage.text = "$page"
    }

    // total page TextView update
    private fun updateNonLikedImageText(flag: Boolean) {
        if(flag){
            binding.mainNoLikedImage.visibility = View.VISIBLE
        } else {
            binding.mainNoLikedImage.visibility = View.INVISIBLE
        }
    }

    // like filter Button update
    private fun updateLikeBtn(isLike: Boolean) {
        if (isLike) {
            // 좋아요 버튼 노랗게 채우기
            binding.mainLikeBtn.setBackgroundResource(R.drawable.ic_star_fill)
        } else {
            // 좋아요 버튼 색깔 비우기
            binding.mainLikeBtn.setBackgroundResource(R.drawable.ic_star_empty)
        }
    }

    // detail 화면으로 이동
    private fun moveToDetail(id: String) {
        navController.navigate(MainFragmentDirections.actionMainFragmentToImageDetailFragment(id))
    }
}