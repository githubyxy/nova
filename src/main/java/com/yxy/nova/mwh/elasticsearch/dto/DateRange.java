package com.yxy.nova.mwh.elasticsearch.dto;

import java.util.Date;

public class DateRange {
    public static final long SEARCH_NO_LIMIT = -1;

    private Date startTime;
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void greater(long value) {
        greater(new Date(value));
    }

    public void greater(Date value) {
        if (startTime == null) {
            startTime = value;
        } else if (startTime.before(value)) {
            startTime = value;
        }
    }

    public void less(long value) {
        less(new Date(value));
    }

    public void less(Date value) {
        if (endTime == null) {
            endTime = value;
        } else if (endTime.after(value)) {
            endTime = value;
        }
    }

    public void equalsValue(long value) {
        greater(value);
        less(value);
    }

    public boolean hasStartTime() {
        return this.startTime != null;
    }

    public boolean hasEndTime() {
        return this.endTime != null;
    }

    public long milliSeconds() {
        if (endTime == null) return Long.MAX_VALUE;
        if (startTime == null) return Long.MAX_VALUE;
        return endTime.getTime() - startTime.getTime();
    }
}
