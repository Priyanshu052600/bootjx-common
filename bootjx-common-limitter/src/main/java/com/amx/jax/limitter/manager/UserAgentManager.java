package com.amx.jax.limitter.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.amx.jax.limitter.config.SupportedUserAgentVznConf;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.annotation.PostConstruct;

@Service
public class UserAgentManager {

    private static final Logger log = LoggerFactory.getLogger(UserAgentManager.class);

    public static SupportedUserAgentVznConf SUPPORTED_USER_AGENT_VERSION_CONF;

    public static final String CONFIG_KEY_SEPARATER = "_";

    @PostConstruct
    public void init() {
        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("config/supported-user-agent-version.yml");
            inputStream = classPathResource.getInputStream();
            log.info("Initializing supported browser configuration");
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            SUPPORTED_USER_AGENT_VERSION_CONF = om.readValue(inputStream, SupportedUserAgentVznConf.class);
        } catch (IOException e) {
            log.warn("Useragent manager init method: {}", e.getMessage());
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }

    public boolean isSupportedUserAgent(String userAgentStr) {
        boolean isSupported = true;
        if (StringUtils.isNotBlank(userAgentStr)) {
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
            String browserVersion = null;
            if (userAgent.getBrowserVersion() != null) {
                browserVersion = userAgent.getBrowserVersion().getMajorVersion();
            }
            Browser browser = userAgent.getBrowser().getGroup();
            Map<String, Double> supportedBrowsers = SUPPORTED_USER_AGENT_VERSION_CONF.getBrowser();
            if (browserVersion != null && browser != null) {
                if (supportedBrowsers != null) {
                    String versionStr = userAgent.getBrowserVersion().getMajorVersion();
                    if (versionStr != null && StringUtils.isNumeric(versionStr)) {
                        Integer version = Integer.parseInt(versionStr);
                        BrowserType browserType = userAgent.getBrowser().getBrowserType();
                        Double supportedVzn = supportedBrowsers
                                .get(browser.name() + CONFIG_KEY_SEPARATER + browserType.name());
                        if (supportedVzn == null) {
                            supportedVzn = supportedBrowsers.get(browser.name());
                        }
                        if (supportedVzn != null) {
                            isSupported = (version > supportedVzn);
                        }
                    }
                }
            }
        }
        return isSupported;
    }

    public boolean isUnsupportedUserAgent(String userAgentStr) {
        boolean isUnSupported = false;
        if (StringUtils.isNotBlank(userAgentStr)) {
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
            Browser browser = userAgent.getBrowser().getGroup();
            List<String> unSupportedBrowsers = SUPPORTED_USER_AGENT_VERSION_CONF.getUnsupportedBrowser();
            if (CollectionUtils.isNotEmpty(unSupportedBrowsers)) {
                if (unSupportedBrowsers.contains(browser.name())) {
                    isUnSupported = true;
                }
            }
        }
        return isUnSupported;
    }

    public String getUserAgentNotSupportMsg(String userAgentStr, boolean isUnsupported) {
        String message = String.format("User agent- %s not supported", userAgentStr);
        if (isUnsupported) {
            return message;
        }
        if (StringUtils.isNotBlank(userAgentStr)) {
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
            message = String.format("Browser %s with version %s is not supported. Use browser version greater than %s",
                    userAgent.getBrowser().name(), userAgent.getBrowserVersion().getVersion(),
                    userAgent.getBrowserVersion().getVersion());
        }
        return message;
    }
}
