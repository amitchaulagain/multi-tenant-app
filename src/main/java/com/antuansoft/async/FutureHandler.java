package com.antuansoft.async;

import com.antuansoft.herospace.Model;

/**
 * Created by nasir on 12/16/13.
 */
public interface FutureHandler<R> {
    void onSuccess(R result);
    void onFailure(Throwable e);
}