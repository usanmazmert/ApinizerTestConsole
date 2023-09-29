package com.apinizer.apitest.apinizerrequest;

import com.apinizer.apitest.apinizerrequest.EnumBodyPartType;
import org.apache.http.entity.ContentType;

public class ApinizerRequestTextBody extends ApinizerRequestBodyPart {

    private String text;
    private ContentType contentType = ContentType.DEFAULT_TEXT;

    public ApinizerRequestTextBody() {
        setBodyPartType(EnumBodyPartType.BINARY);
    }

    public ApinizerRequestTextBody(String name, String text, ContentType contentType) {
        setBodyPartType(EnumBodyPartType.BINARY);
        setName(name);
        this.text = text;
        this.contentType = contentType;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
