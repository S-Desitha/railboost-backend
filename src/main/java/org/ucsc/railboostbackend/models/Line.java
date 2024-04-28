package org.ucsc.railboostbackend.models;

import java.util.List;

public class Line {

    private String lineName;
    private Integer lineId;
    private List<String> stationList;


    public Line() {
    }

    public Line(String lineName, List<String> stationList) {
        this.lineName = lineName;
        this.stationList = stationList;
    }

    public List<String> getStationList() {
        return stationList;
    }

    public void setStationList(List<String> stationList) {
        this.stationList = stationList;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    @Override
    public String toString() {
        return "Line{" +
                "lineName='" + lineName + '\'' +
                '}';
    }
}
