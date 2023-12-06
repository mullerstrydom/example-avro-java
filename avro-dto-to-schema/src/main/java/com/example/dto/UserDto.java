package com.example.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.apache.avro.reflect.AvroDoc;

public class UserDto {

    @JsonPropertyDescription("This is jsonproperty description")
    private String src;
    @AvroDoc("This is a username")
    private String username;
    private String firstName;
    private String lastName;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
