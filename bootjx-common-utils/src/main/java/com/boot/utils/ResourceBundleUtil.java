package com.boot.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author prashant
 *
 */
public class ResourceBundleUtil {

    public static String getString(ResourceBundle receiptMessageBundle, String key) {
        String value = null;
        try {
            value = receiptMessageBundle.getString(key);
        } catch (MissingResourceException ex) {
        }
        return value;
    }

}
