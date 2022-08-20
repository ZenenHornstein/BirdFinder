package com.bcit.birdfinder;

public class Bird {

    String sciName;
    String commName;
    String birdCode;

    public String getSciName() {
        return sciName;
    }
    public String getCommName() {
        return commName;
    }

    Bird(String sciName, String commName, String birdCode){
        this.sciName = sciName;
        this.commName = commName;
        this.birdCode = birdCode;
    }

    @Override
    public String toString() {
        return "Bird{" +
                "sciName='" + sciName + '\'' +
                ", commName='" + commName + '\'' +
                ", birdCode='" + birdCode + '\'' +
                '}';
    }
}
