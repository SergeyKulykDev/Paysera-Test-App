package com.serhiikulyk.payseratestapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PayseraTestApp @Inject constructor(): Application()