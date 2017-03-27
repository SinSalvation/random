package org.randomheroes.bean;

public class Bike {
    private Integer bike_id;

    private Integer price;

    private Integer code;

    private String addrx;

    private String addry;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bike)) return false;

        Bike bike = (Bike) o;

        if (!bike_id.equals(bike.bike_id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return bike_id.hashCode();
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getAddrx() {
        return addrx;
    }

    public void setAddrx(String addrx) {
        this.addrx = addrx;
    }

    public Integer getBike_id() {
        return bike_id;
    }

    public void setBike_id(Integer bike_id) {
        this.bike_id = bike_id;
    }

    public String getAddry() {
        return addry;
    }

    public void setAddry(String addry) {
        this.addry = addry;
    }
}