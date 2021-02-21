package com.kodluyoruz.bootcampproject.service;

public class Static {

    public static String createRandomValue(int size){

        StringBuilder result = new StringBuilder();

        char digits[] = {'0','1','2','3','4','5','6','7','8','9'};

        for(int i=0; i<size; i++) {
            result.append(digits[(int)Math.floor(Math.random() * 10)]);
        }
        return result.toString();
    }

    public static String createRandomIBAN(){

        StringBuilder ibanNo = new StringBuilder().append("TR");
        return ibanNo.append(createRandomValue(22)).toString();

    }


}
