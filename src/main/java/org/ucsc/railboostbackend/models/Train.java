package org.ucsc.railboostbackend.models;

public class Train {
    private String trainId;
    private String trainType;

    public Train(String trainId, String type) {
        this.trainId = trainId;
        this.trainType = type;
    }

    public Train() {
    }

    public String getTrainId() {
        return this.trainId;
    }

    public String getTrainType() {
        return this.trainType;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainId='" + trainId + '\'' +
                ", type='" + trainType + '\'' +
                '}';
    }
}
