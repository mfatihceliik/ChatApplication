package com.mfatihceliik.chatapplication.data.entity.enums

enum class ViewPagerType(val pageName: String, val position: Int) {
    MEMBERS_FRAGMENT("MemberFragment", 0),
    CREATE_CONVERSATION_FRAGMENT("CreateConversationFragment", 1)
}
