package com.apinizer.apitest.apinizerrequest;

import com.apinizer.apitest.apinizerrequest.EnumBodyPartType;
import org.apache.http.entity.ContentType;

import java.io.InputStream;

public class ApinizerRequestBinaryBody extends ApinizerRequestBodyPart {


    private byte[] content;
    private InputStream contentInputStream;
    private ContentType contentType = ContentType.DEFAULT_BINARY;
    private String fileName;

    public ApinizerRequestBinaryBody() {
        setBodyPartType(EnumBodyPartType.BINARY);
    }

    public ApinizerRequestBinaryBody(String name, byte[] content, ContentType contentType, String fileName) {
        setBodyPartType(EnumBodyPartType.BINARY);
        setName(name);
        this.content = content;
        this.contentType = contentType;
        this.fileName = fileName;
    }


    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getContentInputStream() {
        return contentInputStream;
    }

    public void setContentInputStream(InputStream contentInputStream) {
        this.contentInputStream = contentInputStream;
    }
}
