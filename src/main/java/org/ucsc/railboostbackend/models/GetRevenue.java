package org.ucsc.railboostbackend.models;

import java.util.ArrayList;
import java.util.List;

public class GetRevenue {

    int totalTicketRevenue;
    int totalParcelBookingRevenue;
    List<Integer> monthlyParcelRevenueList = new ArrayList<>();
    List<Integer> monthlyTicketRevenueList = new ArrayList<>();

    List<String> monthList = new ArrayList<>();

    public List<String> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<String> monthList) {
        this.monthList = monthList;
    }

    public int getTotalTicketRevenue() {
        return totalTicketRevenue;
    }

    public void setTotalTicketRevenue(int totalTicketRevenue) {
        this.totalTicketRevenue = totalTicketRevenue;
    }

    public int getTotalParcelBookingRevenue() {
        return totalParcelBookingRevenue;
    }

    public void setTotalParcelBookingRevenue(int totalParcelBookingRevenue) {
        this.totalParcelBookingRevenue = totalParcelBookingRevenue;
    }

    public List<Integer> getMonthlyParcelRevenueList() {
        return monthlyParcelRevenueList;
    }

    public void setMonthlyParcelRevenueList(List<Integer> monthlyParcelRevenueList) {
        this.monthlyParcelRevenueList = monthlyParcelRevenueList;
    }

    public List<Integer> getGeMonthlyTicketRevenueList() {
        return monthlyTicketRevenueList;
    }

    public void setGeMonthlyTicketRevenueList(List<Integer> geMonthlyTicketRevenueList) {
        this.monthlyTicketRevenueList = geMonthlyTicketRevenueList;
    }

    @Override
    public String toString() {
        return "GetRevenue{" +
                "totalTicketRevenue=" + totalTicketRevenue +
                ", totalParcelBookingRevenue=" + totalParcelBookingRevenue +
                ", monthlyParcelRevenueList=" + monthlyParcelRevenueList +
                ", geMonthlyTicketRevenueList=" + monthlyTicketRevenueList +
                '}';
    }
}
