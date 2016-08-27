/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antuansoft.async;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class NonBlockingExecutor {
    private ExecutorService executor;

    public NonBlockingExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public <R> NonBlockingFuture<R> submitNonBlocking(final Callable<R> userTask) {
        final NonBlockingFuture<R> nbFuture = new NonBlockingFuture<>();
        executor.submit(new Callable<R>() {
            @Override
            public R call() throws Exception {
                try {
                    R result = userTask.call();
                    nbFuture.setResult(result);
                    return result;
                } catch (Exception e) {
                    nbFuture.setFailure(e);
                    throw e;
                }
            }
        });

        return nbFuture;
    }
}