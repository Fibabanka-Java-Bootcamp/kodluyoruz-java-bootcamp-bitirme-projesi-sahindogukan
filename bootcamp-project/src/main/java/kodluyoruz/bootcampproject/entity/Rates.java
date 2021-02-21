package com.kodluyoruz.bootcampproject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value = "Rates Model")
public class Rates implements Serializable {
    @JsonProperty("CAD")
    public double cAD;
    @JsonProperty("HKD")
    public double hKD;
    @JsonProperty("ISK")
    public double iSK;
    @JsonProperty("PHP")
    public double pHP;
    @JsonProperty("DKK")
    public double dKK;
    @JsonProperty("HUF")
    public double hUF;
    @JsonProperty("CZK")
    public double cZK;
    @JsonProperty("GBP")
    public double gBP;
    @JsonProperty("RON")
    public double rON;
    @JsonProperty("SEK")
    public double sEK;
    @JsonProperty("IDR")
    public double iDR;
    @JsonProperty("INR")
    public double iNR;
    @JsonProperty("BRL")
    public double bRL;
    @JsonProperty("RUB")
    public double rUB;
    @JsonProperty("HRK")
    public double hRK;
    @JsonProperty("JPY")
    public double jPY;
    @JsonProperty("THB")
    public double tHB;
    @JsonProperty("CHF")
    public double cHF;
    @JsonProperty("EUR")
    public double eUR;
    @JsonProperty("MYR")
    public double mYR;
    @JsonProperty("BGN")
    public double bGN;
    @JsonProperty("TRY")
    public double tRY;
    @JsonProperty("CNY")
    public double cNY;
    @JsonProperty("NOK")
    public double nOK;
    @JsonProperty("NZD")
    public double nZD;
    @JsonProperty("ZAR")
    public double zAR;
    @JsonProperty("USD")
    public double uSD;
    @JsonProperty("MXN")
    public double mXN;
    @JsonProperty("SGD")
    public double sGD;
    @JsonProperty("AUD")
    public double aUD;
    @JsonProperty("ILS")
    public double iLS;
    @JsonProperty("KRW")
    public double kRW;
    @JsonProperty("PLN")
    public double pLN;
}