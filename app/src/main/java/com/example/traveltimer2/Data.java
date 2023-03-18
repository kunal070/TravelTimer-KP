package com.example.traveltimer2;

public class Data {
    private String address;
    private int flag;

    public Data(String address,
                int flag) {
        this.address = address;
        this.flag=flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
