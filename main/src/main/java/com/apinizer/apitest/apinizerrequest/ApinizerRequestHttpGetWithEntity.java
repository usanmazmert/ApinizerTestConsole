/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apinizer.apitest.apinizerrequest;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

public class ApinizerRequestHttpGetWithEntity extends HttpEntityEnclosingRequestBase {

    public final static String METHOD_NAME = "GET";

    public ApinizerRequestHttpGetWithEntity(final String uri) {
        super();
        super.setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
