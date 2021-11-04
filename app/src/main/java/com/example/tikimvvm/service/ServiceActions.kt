package com.example.tikimvvm.service

import android.content.Intent
import android.os.Build
import android.widget.Button
import androidx.core.content.ContextCompat.startForegroundService
import androidx.databinding.BindingAdapter

enum class Actions {
    START,
    STOP
}
