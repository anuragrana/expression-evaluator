package com.ie.naukri.expressionevaluator.model;

import java.util.HashMap;
import java.util.Map;

/**
 *  Holds the data received from service which is to be evaluated against the expression
 */
public class DataHolder {

    Map<String, Object> data = new HashMap<String, Object>();

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
