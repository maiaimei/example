package cn.maiaimei.example.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class XxxService {
    public String hello() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    }

    public String world() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "world";
    }
}
