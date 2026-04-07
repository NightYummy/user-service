package com.nightyummy.dto;

public record UserEvent(Operation operation, String email) {

    public enum Operation { CREATE, DELETE }
}
