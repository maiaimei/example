package cn.maiaimei.example.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OtherService {
    public String methodA() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    }

    public String methodB() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "world";
    }
}