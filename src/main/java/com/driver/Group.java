package com.driver;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private int numberOfParticipants;

    public Group(String name, int numberOfParticipants) {
        this.name = name;
        this.numberOfParticipants = numberOfParticipants;
    }


    List<User>userList=new ArrayList<>();
    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }


}
