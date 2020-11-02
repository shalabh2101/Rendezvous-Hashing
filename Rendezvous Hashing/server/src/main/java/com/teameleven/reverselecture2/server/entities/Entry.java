package com.teameleven.reverselecture2.server.entities;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class Entry implements Serializable {

    @NotNull
    private long key;

    @NotEmpty
    private String value;

    @NotEmpty
    private DateTime createdDateTime = new DateTime();

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
