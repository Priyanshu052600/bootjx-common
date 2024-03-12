package com.amx.jax.limitter.config;

import java.util.List;
import java.util.Map;

public class SupportedUserAgentVznConf {

    private Map<String, Double> browser;

    private Map<String, Double> os;

    private List<String> unsupportedBrowser;

    public Map<String, Double> getBrowser() {
        return browser;
    }

    public void setBrowser(Map<String, Double> browser) {
        this.browser = browser;
    }

    public Map<String, Double> getOs() {
        return os;
    }

    public void setOs(Map<String, Double> os) {
        this.os = os;
    }

    public List<String> getUnsupportedBrowser() {
        return unsupportedBrowser;
    }

    public void setUnsupportedBrowser(List<String> unsupportedBrowser) {
        this.unsupportedBrowser = unsupportedBrowser;
    }

}
