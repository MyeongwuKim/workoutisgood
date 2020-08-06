package me.mw.workoutisgood.service;

import org.springframework.stereotype.Service;

@Service
public class loginService {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
