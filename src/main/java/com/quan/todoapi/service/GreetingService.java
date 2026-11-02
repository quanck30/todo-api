package com.quan.todoapi.service;

/*
      課題名：GreetingService
      作成日：2026/10/27
      作成者：ディンビンクアン
 */

import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    public GreetingService(){
        // Chỉ để quan sát: Spring tạo Bean này lúc nào và tạo bao nhiêu lần.
        // Viết không dấu để tránh lỗi bảng mã trên console Windows.
        System.out.println(">>> GreetingService da duoc Spring tao ra");
    }

    public String greet(String name){
        return "Hello " + name + "!";
    }
}
