package com.antuansoft.async;

public class NonBlockingFuture<R> {
    private FutureHandler<R> handler;
    private R result;
    private Throwable failure;
    private boolean isCompleted;

    public NonBlockingFuture<R> setHandler(FutureHandler<R> handler) {
        this.handler = handler;
        if (isCompleted) {
            if (failure != null) handler.onFailure(failure);
            else handler.onSuccess(result);
        }
        return null;
    }

    void setResult(R result) {
        this.result = result;
        this.isCompleted = true;
        if (handler != null) {
            handler.onSuccess(result);
        }
    }

    void setFailure(Throwable failure) {
        this.failure = failure;
        this.isCompleted = true;
        if (handler != null) {
            handler.onFailure(failure);
        }
    }
}