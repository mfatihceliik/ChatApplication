package com.mfatihceliik.chatapplication.util.onClickListeners

import com.mfatihceliik.chatapplication.data.entity.remote.user.User

interface TickedUserRemoveSetOnClickListener {
    fun onClick(user: User)
}