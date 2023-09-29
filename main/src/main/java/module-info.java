module com.poster.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;
    requires javafx.graphics;
    requires java.desktop;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.commons.lang3;
    requires commons.collections;
    requires org.apache.httpcomponents.httpmime;
    requires okhttp3;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.jsoup;
    requires jdk.jsobject;
    requires org.slf4j;
    requires com.google.gson;
    requires com.h2database;
    requires java.sql;

    opens com.apinizer.apitest to javafx.fxml;
    exports com.apinizer.apitest;
    exports com.apinizer.apitest.apinizerrequest;
}