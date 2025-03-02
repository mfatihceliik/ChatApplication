package com.mfatihceliik.chatapplication.ui.mapper

interface Mapper<F, T> {
    fun from(from: F): T
}