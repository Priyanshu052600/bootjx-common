package com.amx.jax.limitter;

import java.util.Calendar;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RefreshTokenData {

    private String sac;

    // generation time in ms
    private long sacGenerationTime;

    public String getSac() {
        return sac;
    }

    public void setSac(String sac) {
        this.sac = sac;
        this.sacGenerationTime = Calendar.getInstance().getTimeInMillis();
    }

    public long getSacGenerationTime() {
        return sacGenerationTime;
    }

    public void setSacGenerationTime(long sacGenerationTime) {
        this.sacGenerationTime = sacGenerationTime;
    }

}
