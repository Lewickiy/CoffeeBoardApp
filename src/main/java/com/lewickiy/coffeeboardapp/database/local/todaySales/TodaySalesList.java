package com.lewickiy.coffeeboardapp.database.local.todaySales;

import com.lewickiy.coffeeboardapp.database.currentSale.CurrentSale;
import com.lewickiy.coffeeboardapp.database.currentSale.SaleProduct;
import com.lewickiy.coffeeboardapp.database.paymentType.PaymentType;

import java.sql.Time;
import java.util.ArrayList;

import static com.lewickiy.coffeeboardapp.database.paymentType.PaymentTypeList.paymentTypes;

public class TodaySalesList { //Список сегодняшних продаж
    public static ArrayList<SaleProduct> todaySalesArrayList = new ArrayList<>();

    //Данный метод считает все продажи сегодняшнего дня (сумму)
    public static double sumAll() { //сумма всех продаж
        double sumAll = 0.00;
        for (SaleProduct saleProduct : todaySalesArrayList) {
            sumAll = sumAll + saleProduct.getSum();
        }
        return sumAll;
    }

    //Данный метод считает все продажи сегодняшнего дня за наличные.
    public static double sumCash() {
        double sumCash = 0.00;
        for (int i = 0; i < todaySalesArrayList.size(); i++) {
            TodaySales tempTodaySale = (TodaySales) todaySalesArrayList.get(i);
            if (tempTodaySale.getPaymentType().equals("Наличные деньги")) {
                    sumCash = sumCash + tempTodaySale.getSum();
            }
        }
        return sumCash;
    }

    //Данный метод считает все продажи сегодняшнего дня по банковской карте.
    public static double sumCard() {
        double sumCard = 0.00;
        for (int i = 0; i < todaySalesArrayList.size(); i++) {
            TodaySales tempTodaySale = (TodaySales) todaySalesArrayList.get(i);
            if (tempTodaySale.getPaymentType().equals("Банковская карта")) {
                sumCard = sumCard + tempTodaySale.getSum();
            }
        }
        return sumCard;
    }

    /**
     * Данный метод добавляет сформированный объект tempSale класса TodaySales в Array todaySalesArrayList
     * для этого он принимает следующие параметры:
     * @param currentSaleProducts - ArrayList продуктов текущей продажи
     * @param currentSale - объект класса CurrentSale.
     * TODO для того чтобы при перезагрузке системы не происходил сброс этого списка,
     *                    он должен формироваться отдельно в базе данных. Пока это локальная база данных.
     *                    То есть, данный метод должен принимать параметры чего-то в базе данных.
     *
     */
    public static void addCurrentSaleToArray(ArrayList<SaleProduct> currentSaleProducts, CurrentSale currentSale) {
        for (SaleProduct currentSaleProduct : currentSaleProducts) {
            TodaySales tempSale = new TodaySales(currentSale.getSaleId()
                    , currentSaleProduct.getProductId()
                    , currentSaleProduct.getProduct()
                    , currentSaleProduct.getPrice()
                    , currentSaleProduct.getDiscountId()
                    , currentSaleProduct.getDiscount()
                    , currentSaleProduct.getAmount()
                    , currentSaleProduct.getSum());

            for (PaymentType paymentType : paymentTypes) {
                if (currentSale.getPaymentTypeId() == paymentType.getPaymentTypeId()) {
                    tempSale.setPaymentType(paymentType.getPaymentType());
                }
            }
            long nowTime = System.currentTimeMillis();
            Time saleTime = new Time(nowTime);
            tempSale.setSaleTime(saleTime);
            todaySalesArrayList.add(tempSale);
        }
    }
}