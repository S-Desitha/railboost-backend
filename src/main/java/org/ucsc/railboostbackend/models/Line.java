package org.ucsc.railboostbackend.models;

public class Line {

    private String lineName;

    private Integer lineId;

    public String lineStartStation;

    public String lineEndStation;

//    setters and getters
    public Integer getLineId() {
        return lineId;
    }

            public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public String getLineStartStation() {
        return lineStartStation;
    }

    public void setLineStartStation(String lineStartStation) {
        this.lineStartStation = lineStartStation;
    }

    public String getLineEndStation() {
        return lineEndStation;
    }

    public void setLineEndStation(String lineEndStation) {
        this.lineEndStation = lineEndStation;
    }


    public Line() {
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
