package com.dushane.weather.dependencyinjection;

import com.dushane.weather.data.GeocodeServices;
import com.dushane.weather.data.HomeRepository;
import com.dushane.weather.ui.components.HomeViewModel;
import com.dushane.weather.data.WeatherServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class HomeViewModelModule {

    @Singleton
    @Provides
    WeatherServices provideWeatherServices(Retrofit retrofit){
        return retrofit.create(WeatherServices.class);
    }

    @Singleton
    @Provides
    GeocodeServices provideGeocodeServices(Retrofit retrofit){
        return retrofit.create(GeocodeServices.class);
    }

    @Singleton
    @Provides
    HomeRepository provideHomeRepository(WeatherServices weatherServices, GeocodeServices geocodeServices){
        return new HomeRepository(weatherServices, geocodeServices);
    }

    @Singleton
    @Provides
    HomeViewModel provideHomeViewModel(HomeRepository homeRepository){
        return new HomeViewModel(homeRepository);
    }
}
