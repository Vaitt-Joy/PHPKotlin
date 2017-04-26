package com.vz.phpkotlin.di.moduls;

import android.content.Context;

import com.vz.phpkotlin.BaseApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by huangwz on 2017/4/25.
 */

@Module
public class AppModule {
    public static final String TAG = "AppModule";
    private final BaseApplication application;

    public AppModule(BaseApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application;
    }
}
