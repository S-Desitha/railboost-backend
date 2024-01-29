package org.ucsc.railboostbackend.models;

public class Line {

    private String lineId;

    private  String lineName;

    public String getLineName() {
        return lineName;
    }

    public Line() {
        this.lineId = lineId;
        this.lineName = lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    @Override
    public String toString() {
        return "Line{" +
                "lineId='" + lineId + '\'' +
                ", lineName='" + lineName + '\'' +
                '}';
    }
}
