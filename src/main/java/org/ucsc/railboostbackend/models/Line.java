package org.ucsc.railboostbackend.models;

public class Line {

    private String lineName;


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
