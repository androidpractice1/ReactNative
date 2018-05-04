package com.manu.reactiveexample;

import android.database.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Observer;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Disposable disposable;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Observable
        io.reactivex.Observable<String> animalsObservable = getAnimalObservable();


        //Observer
        //io.reactivex.Observer<String> animalsObserver = getAnimalsObserver();

        DisposableObserver<String> animalsObserver = (DisposableObserver<String>) getAnimalsObserver();

        DisposableObserver<String> animalsObserverAllCaps = getAnimalsAllCapsObserver();

        //Observer subscribing to Observable
        compositeDisposable.add(
        animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("b");
                    }
                })
                .subscribeWith(animalsObserver)
        );

    }

    private DisposableObserver<String> getAnimalsAllCapsObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {


            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private io.reactivex.Observable<String> getAnimalObservable() {
        return io.reactivex.Observable.just("ant", "bee", "cat", "dog", "elephant");
    }

    private io.reactivex.Observer<String> getAnimalsObserver() {

        return new io.reactivex.Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(String s) {

                Log.d(TAG, "Name: " + s);

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error: " + e.getMessage());
            }

            @Override
            public void onComplete() {

                Log.d(TAG, "All Items are Emitted.");
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //don't send the data once the activity is destroyed.
        disposable.dispose();
    }
}
