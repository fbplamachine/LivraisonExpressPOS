package com.israel.livraisonexpresspos.models.from_steed_app;

public class Badge {
    private Integer unassigned = 0;
    private Integer assigned = 0;
    private Integer running = 0;
    private Integer delivered = 0;
    private Integer oftoday = 0;
    private Integer myOrders = 0;
    private Integer toBeTreated = 0;
    private Integer cancelled;
    private Integer toValidate;
    private Integer started;
    private Integer inprogress;

    public Badge() {
        unassigned = 0;
        assigned = 0;
        running = 0;
        delivered = 0;
        started = 0;
        inprogress = 0;
        cancelled = 0;
        toValidate = 0;
        oftoday = 0;
        myOrders = 0;
        toBeTreated = 0;
    }

    public int getMyOrder(){
        return running + assigned + delivered;
    }

    public Integer getUnassigned() {
        return unassigned;
    }

    public void setUnassigned(Integer unassigned) {
        this.unassigned = unassigned;
    }

    public Integer getAssigned() {
        return assigned;
    }

    public void setAssigned(Integer assigned) {
        this.assigned = assigned;
    }

    public Integer getRunning() {
        return running;
    }

    public void setRunning(Integer running) {
        this.running = running;
    }

    public Integer getDelivered() {
        return delivered;
    }

    public void setDelivered(Integer delivered) {
        this.delivered = delivered;
    }

    public Integer getOftoday() {
        return oftoday;
    }

    public void setOftoday(Integer oftoday) {
        this.oftoday = oftoday;
    }

    public Integer getMyOrders() {
        return myOrders;
    }

    public void setMyOrders(Integer myOrders) {
        this.myOrders = myOrders;
    }

    public Integer getToBeTreated() {
        return toBeTreated;
    }

    public void setToBeTreated(Integer toBeTreated) {
        this.toBeTreated = toBeTreated;
    }

    public Integer getCancelled() {
        return cancelled;
    }

    public void setCancelled(Integer cancelled) {
        this.cancelled = cancelled;
    }

    public Integer getStarted() {
        return started;
    }

    public void setStarted(Integer started) {
        this.started = started;
    }

    public Integer getInprogress() {
        return inprogress;
    }

    public void setInprogress(Integer inprogress) {
        this.inprogress = inprogress;
    }

    public Integer getToValidate() {
        return toValidate;
    }

    public void setToValidate(Integer toValidate) {
        this.toValidate = toValidate;
    }

    public Integer getTotal(){
        if (unassigned == null)return null;
        return unassigned + assigned + running + delivered + cancelled;
    }
}
