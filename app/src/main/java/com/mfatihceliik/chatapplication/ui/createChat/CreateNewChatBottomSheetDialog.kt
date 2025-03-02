package com.mfatihceliik.chatapplication.ui.createChat

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mfatihceliik.chatapplication.data.entity.enums.ViewPagerType
import com.mfatihceliik.chatapplication.databinding.CreateNewChatBottomsheetFragmentBinding
import com.mfatihceliik.chatapplication.ui.createChat.member.MembersFragment
import com.mfatihceliik.chatapplication.ui.createChat.member.MembersViewModel
import com.mfatihceliik.chatapplication.ui.createChat.member.group.CreateConversationFragment
import com.mfatihceliik.chatapplication.util.ZoomOutPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.annotation.meta.When


@AndroidEntryPoint
class CreateNewChatBottomSheetDialog: BottomSheetDialogFragment() {

    private lateinit var binding: CreateNewChatBottomsheetFragmentBinding
    private lateinit var membersViewPagerAdapter: MembersViewPagerAdapter
    private val viewModel: MembersViewModel by activityViewModels()

    companion object {
        const val TAG = "CreateNewChatBottomS"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateNewChatBottomsheetFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullHeightBottomSheet(view = view)
        initialize()
        Log.v(TAG, findNavController().currentBackStack.value.toString())
    }

    private fun fullHeightBottomSheet(view: View) {
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        binding.root.minHeight = Resources.getSystem().displayMetrics.heightPixels
    }
    private fun initialize() {
        setViewPager()
        viewPagerChangeFragment()
    }

    private fun setViewPager() {
        membersViewPagerAdapter = MembersViewPagerAdapter(childFragmentManager, lifecycle)
        val fragments = arrayListOf(MembersFragment(), CreateConversationFragment())
        membersViewPagerAdapter.addAllFragment(fragments = fragments)
        binding.membersViewPager.adapter = membersViewPagerAdapter
        binding.membersViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        //binding.membersViewPager.setPageTransformer(ZoomOutPageTransformer())
        binding.membersViewPager.isUserInputEnabled = false
    }

    private fun viewPagerChangeFragment() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewpagerChangePage.collect { pageName ->
                //binding.membersViewPager.currentItem = page
                changePage(pageName = pageName)
            }
        }
    }

    private fun changePage(pageName: String) {
        when(pageName) {
            ViewPagerType.MEMBERS_FRAGMENT.pageName -> binding.membersViewPager.currentItem = ViewPagerType.MEMBERS_FRAGMENT.position
            ViewPagerType.CREATE_CONVERSATION_FRAGMENT.pageName -> binding.membersViewPager.currentItem = ViewPagerType.CREATE_CONVERSATION_FRAGMENT.position
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
        viewModel.clearTickedUsers()
    }
}