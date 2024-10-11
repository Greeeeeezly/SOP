package com.example.Sop;
import org.springframework.hateoas.Link;

public class ActionModel {
    private String name;
    private String method;
    private Link link;

    public ActionModel(String name, String method, Link link) {
        this.name = name;
        this.method = method;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
