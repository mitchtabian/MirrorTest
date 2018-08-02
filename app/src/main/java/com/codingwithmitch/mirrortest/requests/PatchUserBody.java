package com.codingwithmitch.mirrortest.requests;

public class PatchUserBody {

    private String name;
    private String birthdate;
    private String location;

    public PatchUserBody(String name, String birthdate, String location) {
        this.name = name;
        this.birthdate = birthdate;
        this.location = location;
    }

    public PatchUserBody() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "PatchUserBody{" +
                "name='" + name + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
