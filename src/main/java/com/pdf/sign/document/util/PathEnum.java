package com.pdf.sign.document.util;

public enum PathEnum {

    FILE_PATH("/uploads"),
    USER_DIR("user.dir");

    String pathName;

    PathEnum(String pathName) {
        this.pathName = pathName;
    }

    public String getPathName() {
        return pathName;
    }
}
