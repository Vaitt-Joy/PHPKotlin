package com.vz.phpkotlin.di.component;

import com.vz.phpkotlin.BaseApplication;
import com.vz.phpkotlin.di.moduls.AppModule;

import dagger.Component;

/**
 * Created by huangwz on 2017/4/25.
 */

@Component(
        modules = {
                AppModule.class,
        }
)
public interface AppComponent {
    BaseApplication inject(BaseApplication rxRetrofitApplication);
}
