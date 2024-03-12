package com.amx.jax.limitter.exp;

import com.boot.jx.exception.IExceptionEnum;

public enum LimitterError implements IExceptionEnum {
    REFRESH_TOKEN_DISABLED, INVALID_SESSION_TOKEN, COMMON_LIMITTER_ERROR;

    public String getStatusKey() {
        return this.toString();
    }

    @Override
    public int getStatusCode() {
        return this.ordinal();
    }

}
