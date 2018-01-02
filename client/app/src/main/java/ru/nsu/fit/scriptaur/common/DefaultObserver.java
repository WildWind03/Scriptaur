package ru.nsu.fit.scriptaur.common;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DefaultObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        try{
            onNextElement(t);
        }
        catch (Throwable e){
            onError(e);
        }
    }

    public void onNextElement(T t) throws Throwable{

    }

    @Override
    public void onError(Throwable e) {
        Log.e("Error:", "when observing", e);
    }

    @Override
    public void onComplete() {

    }
}
