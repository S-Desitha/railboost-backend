package org.ucsc.railboostbackend.models;

public class DashboardModel {
    private int trainCount;
    private int noTicket ;

    public int getTrainCount() {
        return trainCount;
    }

    public int getNoTicket() {
        return noTicket;
    }

    public void setNoTicket(int noTicket) {
        this.noTicket = noTicket;
    }

    public void setTrainCount(int trainCount) {
        this.trainCount = trainCount;
    }

    @Override
    public String toString() {
        return "DashboardModel{" +
                "trainCount=" + trainCount +
                ", noTicket=" + noTicket +
                '}';
    }
}
