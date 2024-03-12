package com.amx.jax.limitter.model;

public class RefreshTokenResponse {

    public RefreshTokenResponse() {
        super();
    }

    public RefreshTokenResponse(String refreshToken, Integer retryTimeLimit) {
        super();
        this.refreshToken = refreshToken;
        this.retryTimeLimit = retryTimeLimit;
    }

    String refreshToken;

    Integer retryTimeLimit;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getRetryTimeLimit() {
        return retryTimeLimit;
    }

    public void setRetryTimeLimit(Integer retryTimeLimit) {
        this.retryTimeLimit = retryTimeLimit;
    }

}
