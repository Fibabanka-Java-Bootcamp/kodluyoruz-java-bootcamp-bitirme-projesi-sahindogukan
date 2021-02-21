package com.kodluyoruz.bootcampproject.entity;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "Rates Root Model")
public class Root {
    public Rates rates;
    public String base;
    public String date;
}
