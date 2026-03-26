package com.socialgraph.core;

public class UserNode {
    private int id;
    private String name;

    public UserNode(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() { return "UserNode{id=" + id + ", name='" + name + "'}"; }
}
