// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package com.sun.j2ee.blueprints.consumerwebsite.actions;


public class Activity {
    protected java.lang.String activityId;
    protected java.util.Calendar endDate;
    protected int headCount;
    protected java.lang.String location;
    protected java.lang.String name;
    protected float price;
    protected java.util.Calendar startDate;
    
    public Activity() {
    }
    
    public Activity(java.lang.String activityId, java.util.Calendar endDate, int headCount, java.lang.String location, java.lang.String name, float price, java.util.Calendar startDate) {
        this.activityId = activityId;
        this.endDate = endDate;
        this.headCount = headCount;
        this.location = location;
        this.name = name;
        this.price = price;
        this.startDate = startDate;
    }
    
    public java.lang.String getActivityId() {
        return activityId;
    }
    
    public void setActivityId(java.lang.String activityId) {
        this.activityId = activityId;
    }
    
    public java.util.Calendar getEndDate() {
        return endDate;
    }
    
    public void setEndDate(java.util.Calendar endDate) {
        this.endDate = endDate;
    }
    
    public int getHeadCount() {
        return headCount;
    }
    
    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }
    
    public java.lang.String getLocation() {
        return location;
    }
    
    public void setLocation(java.lang.String location) {
        this.location = location;
    }
    
    public java.lang.String getName() {
        return name;
    }
    
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    public float getPrice() {
        return price;
    }
    
    public void setPrice(float price) {
        this.price = price;
    }
    
    public java.util.Calendar getStartDate() {
        return startDate;
    }
    
    public void setStartDate(java.util.Calendar startDate) {
        this.startDate = startDate;
    }
}
