package com.amx.jax.limitter.exp;

import com.boot.jx.exception.AmxApiError;
import com.boot.jx.exception.AmxApiException;
import com.boot.jx.exception.IExceptionEnum;

public class LimitterException extends AmxApiException {

    public LimitterException(LimitterError error, String errorMessage) {
        super(error, errorMessage);
    }

    public LimitterException() {
        super();
    }
    
    

    public LimitterException(AmxApiError amxApiError) {
        super(amxApiError);
    }



    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public IExceptionEnum getErrorIdEnum(String errorId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isReportable() {
        return false;
    }

}
