package com.revoluttestapp.core.network

import io.reactivex.*
import io.reactivex.functions.BiPredicate
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.Request
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

class RxAdapterFactory private constructor(
    private val rxOnErrorHandler: RxOnErrorHandler
) : CallAdapter.Factory() {

    private val original: RxJava2CallAdapterFactory

    init {
        original = RxJava2CallAdapterFactory.create()
    }

    override fun get(
        returnType: Type, annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        return RxCallAdapterWrapper(
            wrappedСallAdapter = original.get(returnType, annotations, retrofit)!! as CallAdapter<Any, Any>,
            rxOnErrorHandler = rxOnErrorHandler
        )
    }

    interface RxOnErrorHandler {
        fun doOnError(e: Throwable, request: Request)
    }

    private class RxCallAdapterWrapper<R>(
        private val wrappedСallAdapter: CallAdapter<R, Any>,
        private val rxOnErrorHandler: RxOnErrorHandler
    ) : CallAdapter<R, Any> {

        private val retryPredicate = object : BiPredicate<Int, Throwable> {
            @Throws(Exception::class)
            override fun test(i: Int, e: Throwable): Boolean {
                return false
            }
        }

        override fun responseType(): Type? {
            return wrappedСallAdapter.responseType()
        }

        override fun adapt(call: Call<R>): Any {
            val adaptedCallResult = wrappedСallAdapter.adapt(call)
            val request = call.request()

            if (adaptedCallResult is Completable) {
                return prepareCompletable(adaptedCallResult, request)
            }

            if (adaptedCallResult is Single<*>) {
                return prepareSingle(adaptedCallResult, request)
            }

            if (adaptedCallResult is Maybe<*>) {
                return prepareMaybe(adaptedCallResult, request)
            }

            if (adaptedCallResult is Observable<*>) {
                return prepareObservable(adaptedCallResult, request)
            }

            return adaptedCallResult
        }

        private fun prepareCompletable(completable: Completable, request: Request): Completable {
            return completable.subscribeOn(Schedulers.io())
                .retry(retryPredicate)
                .onErrorResumeNext { throwable ->
                    rxOnErrorHandler.doOnError(throwable, request)
                    Completable.error(customizeException(throwable))
                }
        }

        private fun <T> prepareSingle(single: Single<T>, request: Request): Single<T> {
            return single.subscribeOn(Schedulers.io())
                .retry(retryPredicate)
                .onErrorResumeNext { throwable ->
                    rxOnErrorHandler.doOnError(throwable, request)
                    Single.error(customizeException(throwable))
                }
        }

        private fun <T> prepareMaybe(maybe: Maybe<T>, request: Request): Maybe<T> {
            return maybe.subscribeOn(Schedulers.io())
                .retry(retryPredicate)
                .onErrorResumeNext(Function<Throwable, MaybeSource<T>> { throwable ->
                    rxOnErrorHandler.doOnError(throwable, request)
                    Maybe.error<T>(customizeException(throwable))
                })
        }

        private fun <T> prepareObservable(observable: Observable<T>, request: Request): Observable<T> {
            return observable.subscribeOn(Schedulers.io())
                .retry(retryPredicate)
                .onErrorResumeNext(
                    Function<Throwable, ObservableSource<T>> { throwable ->
                        rxOnErrorHandler.doOnError(throwable, request)
                        Observable.error<T>(customizeException(throwable))
                    })
        }

        private fun customizeException(throwable: Throwable): Throwable {
            return throwable
        }
    }

    companion object {
        private val DEFAULT_RX_ERROR_HANDER = object : RxOnErrorHandler {
            override fun doOnError(e: Throwable, request: Request) {
                //no-op
            }
        }

        fun create(rxOnErrorHandler: RxOnErrorHandler): CallAdapter.Factory {
            return RxAdapterFactory(rxOnErrorHandler)
        }

        fun create(): CallAdapter.Factory {
            return RxAdapterFactory(DEFAULT_RX_ERROR_HANDER)
        }
    }
}
