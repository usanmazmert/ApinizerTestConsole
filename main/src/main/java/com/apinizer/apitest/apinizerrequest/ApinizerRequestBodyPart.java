package com.apinizer.apitest.apinizerrequest;

import com.apinizer.apitest.apinizerrequest.EnumBodyPartType;

import java.io.Serializable;

public abstract class ApinizerRequestBodyPart implements Serializable {
    private String name;
    private EnumBodyPartType bodyPartType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnumBodyPartType getBodyPartType() {
        return bodyPartType;
    }

    public void setBodyPartType(EnumBodyPartType bodyPartType) {
        this.bodyPartType = bodyPartType;
    }
}
