package org.ucsc.railboostbackend.models;

public class ParcelCategory {

    private Integer itemId;
    private  Integer Charges;
    private  String specialItem;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getCharges() {
        return Charges;
    }

    public void setCharges(Integer charges) {
        Charges = charges;
    }

    public String getSpecialItem() {
        return specialItem;
    }

    public void setSpecialItem(String specialItem) {
        this.specialItem = specialItem;
    }

    @Override
    public String toString() {
        return "ParcelCategory{" +
                "itemId=" + itemId +
                ", Charges=" + Charges +
                ", specialItem='" + specialItem + '\'' +
                '}';
    }
}
