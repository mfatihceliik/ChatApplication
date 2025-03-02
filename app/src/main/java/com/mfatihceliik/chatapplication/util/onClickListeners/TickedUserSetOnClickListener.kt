package com.mfatihceliik.chatapplication.util.onClickListeners

import com.mfatihceliik.chatapplication.data.entity.remote.user.User

interface TickedUserSetOnClickListener {
    fun onClick(user: User)
}