package com.apinizer.apitest.apinizerrequest;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ApinizerHistory implements Serializable {

    private String id;
    private String date;

    private List<ApinizerRequestTab> requestTabs;

    public ApinizerHistory(){
        LocalDate currentDate = LocalDate.now();

        // Define a custom date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd", Locale.ENGLISH);

        date = currentDate.format(formatter);
        requestTabs = new ArrayList<>();

        id = UUID.randomUUID().toString();
    }
    public ApinizerHistory(String date) {
        this.date = date;
        requestTabs = new ArrayList<>();
        id = UUID.randomUUID().toString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ApinizerRequestTab> getRequestTabs() {
        return requestTabs;
    }

    public void setRequestTabs(List<ApinizerRequestTab> requestTabs) {
        this.requestTabs = requestTabs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
