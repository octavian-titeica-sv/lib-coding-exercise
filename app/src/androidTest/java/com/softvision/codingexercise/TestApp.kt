package com.softvision.codingexercise

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import io.mockk.mockk

class TestApp : Application(), HasAndroidInjector {
    override fun androidInjector(): AndroidInjector<Any> {
        return mockk(relaxed = true)
    }
}
