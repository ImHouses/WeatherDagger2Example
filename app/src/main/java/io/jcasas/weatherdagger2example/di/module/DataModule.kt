/*
 * Copyright 2019, Juan Casas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jcasas.weatherdagger2example.di.module

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.jcasas.weatherdagger2example.data.location.LocationDataSource
import io.jcasas.weatherdagger2example.data.weather.WeatherDataSource
import io.jcasas.weatherdagger2example.domain.weather.WeatherEntity
import dagger.Module
import dagger.Provides
import io.jcasas.weatherdagger2example.data.config.ConfigurationDataSource
import io.jcasas.weatherdagger2example.domain.forecast.ForecastResponse
import io.jcasas.weatherdagger2example.domain.forecast.ForecastEntity
import io.jcasas.weatherdagger2example.framework.WeatherService
import io.jcasas.weatherdagger2example.framework.AppConfigDataSource
import io.jcasas.weatherdagger2example.framework.AppLocationDataSource
import io.jcasas.weatherdagger2example.framework.AppWeatherDataSource
import io.jcasas.weatherdagger2example.framework.deserializer.CurrentWeatherDeserializer
import io.jcasas.weatherdagger2example.framework.deserializer.ForecastDeserializer
import io.jcasas.weatherdagger2example.framework.deserializer.SingleWeekForecastResponseDeserializer
import io.jcasas.weatherdagger2example.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class DataModule {

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Provides
    @Singleton
    fun provideGson(
            currentWeatherDeserializer: CurrentWeatherDeserializer,
            forecastDeserializer: ForecastDeserializer,
            singleWeekForecastDeserializer: SingleWeekForecastResponseDeserializer
    ): Gson = GsonBuilder()
                .registerTypeAdapter(WeatherEntity::class.java, currentWeatherDeserializer)
                .registerTypeAdapter(ForecastEntity::class.java, forecastDeserializer)
                .registerTypeAdapter(ForecastResponse::class.java, singleWeekForecastDeserializer)
                .create()

    @Provides
    @Singleton
    fun provideWeatherService(retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationSource(
            appContext: Context,
            @Named("internal_config") sharedPreferences: SharedPreferences
    ): LocationDataSource = AppLocationDataSource(appContext, sharedPreferences)


    @Provides
    @Singleton
    fun provideWeatherSource(
            weatherService: WeatherService,
            sharedPreferences: SharedPreferences
    ): WeatherDataSource {
        return AppWeatherDataSource(weatherService, sharedPreferences)
    }

    @Provides
    @Singleton
    fun providePreferencesSource(sharedPreferences: SharedPreferences): ConfigurationDataSource =
            AppConfigDataSource(sharedPreferences)

    @Provides
    @Singleton
    fun provideForecastDeserializer(): ForecastDeserializer = ForecastDeserializer()

    @Provides
    @Singleton
    fun provideOneWeekForecastDeserializer(): SingleWeekForecastResponseDeserializer =
            SingleWeekForecastResponseDeserializer()
}