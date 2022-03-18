package com.atguigu.easyExcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestWrite {
    public static void main(String[] args) {
        String fileName = "/Users/sunny/Desktop/毕业设计/excel/01.xlxs";
        List<UserData> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            UserData userData = new UserData();
            userData.setUsername("lucy" + i);
            userData.setUid(i);
            list.add(userData);
        }
        EasyExcel.write(fileName, UserData.class).sheet("用户信息").doWrite(list);
    }
}
