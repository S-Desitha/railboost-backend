package org.ucsc.railboostbackend.models;

public class Train {
    private String trainId;
    private String type;

    public Train(String trainId, String type) {
        this.trainId = trainId;
        this.type = type;
    }

    public String getTrainId() {
        return this.trainId;
    }

    public String getType() {
        return this.type;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainId='" + trainId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
