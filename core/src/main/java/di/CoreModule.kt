package di

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.revoluttestapp.core.network.RevolutTestApiFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Module
class CoreModule {

    @Provides
    fun provideRevolutTestApiFactory(): RevolutTestApiFactory {
        val httpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(OkHttpProfilerInterceptor())
            .build()

        return RevolutTestApiFactory(httpClient)
    }
}
