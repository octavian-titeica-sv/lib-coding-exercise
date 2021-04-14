package com.softvision.codingexercise.app.dagger

import com.softvision.codingexercise.ui.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UIModule {

    @ContributesAndroidInjector
    abstract fun mainFragment(): MainFragment
}
