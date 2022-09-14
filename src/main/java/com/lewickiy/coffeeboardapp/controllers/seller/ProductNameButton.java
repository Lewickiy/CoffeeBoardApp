package com.lewickiy.coffeeboardapp.controllers.seller;

import com.lewickiy.coffeeboardapp.database.product.Product;
import com.lewickiy.coffeeboardapp.database.product.ProductList;
import javafx.scene.control.Button;
/**
 * Данный класс занимается именованием кнопок с продукцией. Если появится такая необходимость, <br>
 * существует возможность добавлять данные к именам кнопок, например: стоимость товара или любые <br>
 * иные данные о продукции из базы данных.
 */
public class ProductNameButton {
    /**
     * Данный метод непосредственно занимается именованием кнопок с продукцией в классе SellerController <br>
     * он принимает в себя параметр (массив кнопок), далее, итерацией проходится по ProductList <br>,
     * параллельно присваивая кнопкам имена button[].setText..., а также устанавливая AccessibleText <br>
     * Этот текст является product_id of product и, в дальнейшем, планируется для осуществления операций с продуктами <br>
     * при нажатии на кнопку.
     * Этим предполагается будет заниматься класс
     * Также данный метод делает кнопки доступными (productButtons[].setDisable())
     * @param buttons - это массив кнопок, созданных в SellerController.
     */
    static void productNameButton(Button[] buttons) {
        int count = 0;
        for (Product product : ProductList.products) {
            buttons[count].setAccessibleText(String.valueOf(product.getProductId()));
            buttons[count].setText(product.getProduct()
                    + "\n"
                    + product.getNumberOfUnit()
                    + " "
                    + product.getUnitOfMeasurement()
                    + "\n"
                    + product.getPrice()
                    + "руб.");
            buttons[count].setVisible(true);
            buttons[count].setDisable(false);
            count++;
        }
    }
}