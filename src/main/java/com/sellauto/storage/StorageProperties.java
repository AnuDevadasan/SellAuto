package com.sellauto.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "C:\\Users\\Anu\\Desktop\\Anu\\Masters\\Coarse Materials\\Adv Sys Project\\SpringWorkspace\\SellAuto\\src\\main\\resources\\image";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
