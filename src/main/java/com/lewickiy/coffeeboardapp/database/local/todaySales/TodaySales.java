package com.lewickiy.coffeeboardapp.database.local.todaySales;

import com.lewickiy.coffeeboardapp.database.currentSale.SaleProduct;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.sql.Date;
import java.sql.Time;

// Расширенный класс от SaleProduct
public class TodaySales extends SaleProduct {

    private Date saleDate; //Помимо полей Продукта имеет Дату продажи
    private Time saleTime; //И время продажи
    private String paymentType; //А также тип оплаты типа String

    public TodaySales() {
        super();
    }
    public TodaySales(int saleProdId
            , int prodSaleId
            , String prodName
            , double priceProdSale
            , int discountId
            , int discount
            , int amountProdSale
            , double sumProdSale) {

        super(saleProdId
                , prodSaleId
                , prodName
                , priceProdSale
                , discountId
                , discount
                , amountProdSale
                , sumProdSale);
    }

    public TodaySales(int prodSaleId
            , String prodName
            , double priceProdSale) {

        super(prodSaleId
                , prodName
                , priceProdSale);
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Time getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Time saleTime) {
        this.saleTime = saleTime;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}