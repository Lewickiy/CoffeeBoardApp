package com.lewickiy.coffeeboardapp.controllers.seller;

import com.lewickiy.coffeeboardapp.CoffeeBoardApp;
import com.lewickiy.coffeeboardapp.database.currentSale.CurrentSale;
import com.lewickiy.coffeeboardapp.database.currentSale.SaleProduct;
import com.lewickiy.coffeeboardapp.database.currentSale.SaleProductList;
import com.lewickiy.coffeeboardapp.database.discount.Discount;
import com.lewickiy.coffeeboardapp.database.outlet.Outlet;
import com.lewickiy.coffeeboardapp.database.paymentType.PaymentType;
import com.lewickiy.coffeeboardapp.database.product.Product;
import com.lewickiy.coffeeboardapp.database.user.UserList;
import com.lewickiy.coffeeboardapp.idgenerator.UniqueIdGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import static com.lewickiy.coffeeboardapp.controllers.seller.ProductNameButton.productNameButton;
import static com.lewickiy.coffeeboardapp.database.currentSale.CurrentSale.createNewSale;
import static com.lewickiy.coffeeboardapp.database.currentSale.SaleProduct.addProductsToSale;
import static com.lewickiy.coffeeboardapp.database.currentSale.SaleProductList.currentSaleProducts;
import static com.lewickiy.coffeeboardapp.database.discount.DiscountList.createDiscountList;
import static com.lewickiy.coffeeboardapp.database.discount.DiscountList.discounts;
import static com.lewickiy.coffeeboardapp.database.outlet.OutletList.outlets;
import static com.lewickiy.coffeeboardapp.database.paymentType.PaymentTypeList.createPaymentTypeAL;
import static com.lewickiy.coffeeboardapp.database.paymentType.PaymentTypeList.paymentTypes;
import static com.lewickiy.coffeeboardapp.database.product.ProductList.products;

public class SellerController {

    private boolean newSale = true; //boolean значение необходимости создания нового чека

    private int saleId; //Идентификатор текущей продажи. Создаётся в классе UniqueIdGenerator

    private int positionsCount;

    private CurrentSale currentSale; //объект - текущая продажа.

    private SaleProduct currentProduct; //объект - зона сбора данных.

    static ObservableList<SaleProduct> saleProductsObservableList = FXCollections.observableList(currentSaleProducts);

    /*____________________________________start___________________________________________
     * Панель информации в верхней части экрана.
     * Здесь присутствуют кнопки:
     * Закрытие смены -
     * ...
     * А также метод логики нажатия на кнопку Закрытия смены
     _____________________________________˅˅˅____________________________________________*/
    @FXML
    private Button closeShiftButton; //кнопка закрытия смены

    @FXML
    private Label userEarnings; //Не действует. Должно помещаться на отдельном окне при закрытии смены.

    //Действие при нажатии на кнопку Закрытия смены.
    @FXML
    void closeShiftButtonOnAction() throws IOException {
        currentSaleProducts.clear();
        products.clear();
        discounts.clear();
        paymentTypes.clear();
        outlets.clear();
        Stage stage = (Stage) closeShiftButton.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(CoffeeBoardApp.class.getResource("login.fxml"));
        Stage stageLogin = new Stage();
        Scene sceneLogin = new Scene(fxmlLoader.load());
        stageLogin.initStyle(StageStyle.UNDECORATED);
        stageLogin.setTitle("CoffeeApp");
        stageLogin.setScene(sceneLogin);
        stageLogin.show();
    }
    /*____________________________________˄˄˄_____________________________________________
     ___________________________________the end__________________________________________*/

    /*____________________________________start___________________________________________
     * Панель кнопок продуктов
     * Здесь присутствуют кнопки продуктов
     * Кнопки одинаковые, несут один и тот же функционал,
     * только вписываются в них разные заголовки и идентификаторы товара.
     * Также здесь прописана логика при нажатии на кнопку с Продуктом.
     _____________________________________˅˅˅____________________________________________*/
    @FXML
    private GridPane mainGridPane;

    private ArrayList <Button> productButtons = new ArrayList<>();

    EventHandler<ActionEvent> eventProductButtons = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            Button button = (Button) event.getSource();
            endThisTale.setDisable(false); //Кнопка Чек становится доступна

            if (newSale) {
                saleId = UniqueIdGenerator.getId(); //получаем новый уникальный идентификатор продажи.
                currentSale = new CurrentSale(saleId, UserList.currentUser.getUserId(), Outlet.currentOutlet.getOutletId()); //Создаётся текущая продажа в буфере.
                newSale = false; //последующие действия уже не должны создавать новую продажу.

                int idProductButton = Integer.parseInt(button.getAccessibleText()); //Записывается id нажатой кнопки (Продукт)
                // Здесь мы будем вставлять позицию в SaleProductList ПРИ СОЗДАНИИ НОВОГО ЧЕКА. Пока без загрузки в базу.

                for (Product product : products) { //Циклом перебираются все продукты из products ArrayList

                    if (product.getProductId() == idProductButton) { //Если id продукта соответствует id нажатой кнопки продукта,
                        buttonsIsDisable(productButtons, true); //Спрятать кнопки продукта.
                        buttonsIsDisable(numberButtons, false); //Показать цифровые кнопки.
                        productCategoryIco.setVisible(true); //Иконка продукта отображается (пока без логики).
                        productNameLabel.setText(product.getProduct()); //Рядом с иконкой продукта отображается наименование продукта.
                        currentProduct = new SaleProduct(product.getProductId(), product.getProduct(), product.getPrice());
                        //Добавляем данные в currentProduct
                        break;
                    }
                }
            } else {
                int idProductButton = Integer.parseInt(button.getAccessibleText());

                for (Product product : products) { //Циклом перебираются все продукты из products ArrayList

                    if (product.getProductId() == idProductButton) { //Если id продукта соответствует id нажатой кнопки продукта,
                        buttonsIsDisable(productButtons, true); //Спрятать кнопки продукта.
                        buttonsIsDisable(numberButtons, false); //Показать цифровые кнопки.
                        productCategoryIco.setVisible(true); //Иконка продукта отображается (пока без логики).
                        productNameLabel.setText(product.getProduct()); //Рядом с иконкой продукта отображается наименование продукта.
                        currentProduct = new SaleProduct(product.getProductId(), product.getProduct(), product.getPrice());
                        break;
                    }
                }
            }
            xLabel.setVisible(true);
            productNameLabel.setVisible(true);
            addProduct.setVisible(true);
            discountButtonActivate.setVisible(true);
        }
    };
    /*____________________________________˄˄˄_____________________________________________
     ___________________________________the end__________________________________________*/


    /*____________________________________start___________________________________________
     * Панель цифровых кнопок
     * Набор кнопок от 0 до 9 для использования при выборе количества продуктов.
     * Также содержит логику при нажатии на Цифровую кнопку.
     _____________________________________˅˅˅____________________________________________*/
    @FXML
    private GridPane numbersGridPane;

    private ArrayList <Button> numberButtons = new ArrayList<>();

    EventHandler<ActionEvent> eventNumberButtons = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            Button button = (Button) event.getSource();
            if (Integer.parseInt(button.getAccessibleText()) != 0) {
                amountLabel.setText(button.getAccessibleText()); //Label количества берёт данные из AccessibleText цифровой кнопки.
                amountLabel.setVisible(true);
                currentProduct.setAmount(Integer.parseInt(button.getAccessibleText())); //для currentProduct устанавливается количество продукта.
                currentProduct.setSum(currentProduct.getPrice() * currentProduct.getAmount()); //сумма стоимости продукта исходя из выбранного количества.
                buttonsIsDisable(numberButtons, true);
                productOperationButtonsIsDisable(false);
            } else {
                currentProduct = null; //Текущий продукт становится null.
                productCategoryIco.setVisible(false); //Картинка Продукта перестаёт быть видимой
                xLabel.setVisible(false); //Символ количества перестаёт отображаться
                productNameLabel.setVisible(false); //Название продукта перестаёт отображаться
                amountLabel.setVisible(false); //Количество продукта перестаёт отображаться
                addProduct.setDisable(true); //Кнопка добавления продукта становится неактивной
                productOperationButtonsIsDisable(true); //Кнопки с операциями по текущему продукту становятся недоступными.
                buttonsIsDisable(numberButtons, true); //Цифровые кнопки становятся недоступными.
                buttonsIsDisable(productButtons,false); //Кнопки с Продуктами становятся активными.
            }
        }
    };
    /*____________________________________˄˄˄_____________________________________________
     ___________________________________the end__________________________________________*/

    /*____________________________________start___________________________________________
     * Панель таблицы текущего чека
     * Таблица содержит в себе несколько столбцов:
     * Продукт - здесь отображаются добавленные продукты
     * Цена - цена отражённая в прайсе
     * Количество - количество добавленного продукта.
     * На данный момент редактирование данных по нажатию на ячейку не реализовано
     * //TODO создать редактирование данных в таблице по нажатию на ячейку с данными
     * Также присутствует Label с отображением суммы заказа sumLabel
     * //TODO добавить отображение суммы чека со скидкой
     _____________________________________˅˅˅____________________________________________*/
    @FXML
    private TableView<SaleProduct> saleTable; //таблица продаж

    @FXML
    private TableColumn<SaleProduct, String> productColumn; //колонка с наименованием продукта

    @FXML
    private TableColumn<SaleProduct, Double> priceColumn; //колонка со стоимостью продукта

    @FXML
    private TableColumn<SaleProduct, Integer> amountColumn; //количество продукта

    @FXML
    private TableColumn<SaleProduct, Integer> discountColumn;

    @FXML
    private TableColumn<SaleProduct, Double> sumColumn; //сумма стоимости продукта исходя из количества

    @FXML
    private Label sumLabel; //Сумма стоимости товара
    /*____________________________________˄˄˄_____________________________________________
     ___________________________________the end__________________________________________*/

    /*____________________________________start___________________________________________
     * Панель текущего товара.
     * Здесь происходят операции с товаром, который ещё не добавлен в текущий чек.
     * Изображение продукта или его символическое изображение;
     * Символ количества "Х";
     * Цифровое отображение количества единиц продукта;
     * Наименование позиции;
     * Кнопка Продажа;
     * Кнопка Скидка;
     * Кнопка Отмена.
     _____________________________________˅˅˅____________________________________________*/
    @FXML
    private ImageView productCategoryIco; //Графическое отображение товара
    @FXML
    private Label xLabel; //Символ X количество товара
    @FXML
    private Label amountLabel; //Цифровое отображение количества товара
    @FXML
    private Label productNameLabel; //наименование продукта
    @FXML
    private Button addProduct; //кнопка добавления продукта в текущий чек (Зелёный плюсик)
    @FXML
    private Button discountButtonActivate; //Кнопка скидки (в настоящее время не действует). (Знак процента)
    @FXML
    private Button delProduct; //"С" - удалить продукт (действует так же как и 0???)
    @FXML
    private Button endThisTale; //сформировать чек.
    @FXML
    private Button endThisTaleAnother; //отменить чек.

    //Логика нажатия на кнопку со Скидкой
    @FXML
    void discountButtonActivateOnAction(ActionEvent event) {
        discountPanel.setVisible(true);
    }

    //На данный момент кнопка не имеет действия. По сути, просто зарезервированное место.
    @FXML
    void delProductOnAction() {
        //TODO кнопка отмены ButtonOnAction
    }
    @FXML
    void oupsOnAction() {
        //TODO
    }
    //Логика при нажатии на кнопку "+" добавления продукта в текущий чек.
    @FXML
    void addProductOnAction() {
        SaleProductList.addProductToArray(positionsCount, currentProduct);
        positionsCount++;
        saleTable.setItems(saleProductsObservableList); //Установка значений в таблицу.
        saleTable.refresh(); //Обновление таблицы. Без этого отображается только первая строка.

        //Подсчёт суммы продажи под таблицей текущей продажи.
        double total = 0.0;

        for (SaleProduct saleProduct : saleTable.getItems()) {
            total = total + saleProduct.getSum();
        }
        sumLabel.setText(String.valueOf(total)); //Сумма устанавливается в sumLabel
        productCategoryIco.setVisible(false); //Картинка Продукта перестаёт быть видимой
        xLabel.setVisible(false); //Символ количества перестаёт отображаться
        productNameLabel.setVisible(false); //Название продукта перестаёт отображаться
        amountLabel.setVisible(false); //Количество продукта перестаёт отображаться
        addProduct.setDisable(true); //Кнопка добавления продукта становится неактивной
        productOperationButtonsIsDisable(true);
        buttonsIsDisable(productButtons, false); //Кнопки с Продуктами становятся активными.
    }
    @FXML
    void endThisTaleOnAction(ActionEvent event) {
        paymentTypePanel.setVisible(true);
    }
    /*____________________________________˄˄˄_____________________________________________
     ___________________________________the end__________________________________________*/

    /*____________________________________start___________________________________________
     * Панель скидок на позицию.
     * Данная панель открывается поверх панели Текущего товара нажатием на кнопку "%" находящуюся в панели
     * текущего товара.
     _____________________________________˅˅˅____________________________________________*/
    @FXML
    private AnchorPane discountPanel; //Непосредственно сама панель. В ней содержатся все актуальные скидки.

    @FXML
    private GridPane discountGridPane;

    private ArrayList<Button> discountButtons = new ArrayList<>();

    EventHandler<ActionEvent> eventDiscountButtons = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            Button button = (Button) event.getSource();
            discountPanel.setVisible(false); // Скрываем панель скидок
            currentProduct.setDiscountId(Integer.parseInt(button.getAccessibleText()));
            for (Discount discount : discounts) {
                if (currentProduct.getDiscountId() == discount.getDiscountId()) {
                    currentProduct.setDiscount(discount.getDiscount());
                }
            }
            currentProduct.setSum((currentProduct.getPrice() //Устанавливаем текущему продукту сумму исходя из количества и скидки
                    - (currentProduct.getPrice() * currentProduct.getDiscount() / 100))
                    * currentProduct.getAmount());
        }
    };
    /*____________________________________˄˄˄_____________________________________________
     ___________________________________the end__________________________________________*/

    @FXML
    private AnchorPane paymentTypePanel;

    @FXML
    Button[] paymentTypeButtons = new Button[2]; //массив кнопок продуктов

    @FXML
    private Button paymentType1;

    @FXML
    private Button paymentType2;

    @FXML
    void paymentTypeOnAction(ActionEvent event) throws SQLException {
        Button button = (Button) event.getSource();
        currentSale.setPaymentTypeId(Integer.parseInt(button.getAccessibleText()));
        paymentTypePanel.setVisible(false);
        endThisTale.setDisable(true);
        long nowDate = System.currentTimeMillis(); //Дата сейчас.
        Date saleDate = new Date(nowDate);
        long nowTime = System.currentTimeMillis(); //Время сейчас.
        Time saleTime = new Time(nowTime);
        currentSale.setCurrentDate(saleDate);
        currentSale.setCurrentTime(saleTime);
        currentSale.setClientId(1); //Временное назначение клиента
        createNewSale(currentSale); //Создаётся новая продажа в базе из currentSale.
        addProductsToSale(currentSaleProducts, currentSale);
        currentSale = null;
        newSale = true;
        positionsCount = 0;
        currentSaleProducts.clear();
        sumLabel.setText("0.00");
    //    userEarnings.setText(String.valueOf(reloadUserEarnings()));
        saleTable.refresh();
    }

    /*____________________________________start___________________________________________
     * Инициализация
     _____________________________________˅˅˅____________________________________________*/
    @FXML
    void initialize() throws SQLException {
        createDiscountList();
        createPaymentTypeAL();
        paymentTypePanel.setVisible(false);
        paymentTypeButtons[0] = paymentType1;
        paymentTypeButtons[1] = paymentType2;
        int count = 0;
        for (PaymentType paymentType : paymentTypes) {
            paymentTypeButtons[count].setAccessibleText(String.valueOf(paymentType.getPaymentTypeId()));
            paymentTypeButtons[count].setText(paymentType.getPaymentType());
            count++;
        }
        //Изменения в currentSaleProducts происходят также в saleProductsObservableList благодаря Listener.
        saleProductsObservableList.addListener((ListChangeListener<SaleProduct>) change -> {
        });

        for (int i = 0; i < numbersGridPane.getColumnCount(); i++) {
            Button numberButton = new Button();
            numberButtons.add(i, numberButton);
            int finalI = i;
            numberButtons.get(i).layoutBoundsProperty().addListener((observable, oldValue, newValue) -> numberButtons.get(finalI).setFont(Font.font(Math.sqrt(newValue.getHeight() * 10))));
            numberButtons.get(i).setWrapText(true);
            numberButtons.get(i).setStyle("-fx-text-alignment: CENTER; -fx-font-weight: BOLDER");
            numberButtons.get(i).setPrefSize(85.0, 85.0);
            numberButtons.get(i).setVisible(true);
            GridPane.setConstraints(numberButtons.get(i), i, 0);
            numbersGridPane.getChildren().add(numberButtons.get(i));
            numberButtons.get(i).setOnAction(eventNumberButtons);

            if (i < 9) {
                numberButtons.get(i).setText(String.valueOf(i + 1));
                numberButtons.get(i).setAccessibleText(String.valueOf(i + 1));
            } else {
                numberButtons.get(i).setText("0");
                numberButtons.get(i).setAccessibleText("0");
            }

        }

        /*____________________________________________________________________________________
         * Блок текущего Продукта при добавлении его в Текущую продажу
         * Изначально, до создания текущей продажи, данное графическое представление не отображается.
         * Отображение происходит при первом нажатии на кнопку Продукта.
         * После нажатия на кнопку (пока её нет) переноса Продукта в текущий чек блок возвращается
         * в изначальное состояние (не отображается).
         ____________________________________________________________________________________*/
        productCategoryIco.setVisible(false);
        xLabel.setVisible(false);
        amountLabel.setVisible(false);
        productNameLabel.setVisible(false);
        productOperationButtonsIsDisable(true);
        buttonsIsDisable(numberButtons, true);
        endThisTale.setDisable(true);
        endThisTaleAnother.setDisable(true);

        int countD = 0;
        for (int l = 0; l < discountGridPane.getColumnCount(); l++) {
            for (int h = 0; h < discountGridPane.getRowCount(); h++) {
                Button discountButton = new Button();
                discountButtons.add(countD, discountButton);
                int finalDiscountButtonsCount = countD;
                discountButtons.get(countD).layoutBoundsProperty().addListener((observable, oldValue, newValue) ->
                        discountButtons.get(finalDiscountButtonsCount).setFont(
                                Font.font(Math.sqrt(newValue.getHeight() * 1.5))));
                discountButtons.get(countD).setWrapText(true);
                discountButtons.get(countD).setStyle("-fx-text-alignment: CENTER; -fx-font-weight: BOLDER");
                discountButtons.get(countD).setPrefSize(75.0, 75.0);
                discountButtons.get(countD).setVisible(false);
                GridPane.setConstraints(discountButtons.get(countD), l, h);
                discountGridPane.getChildren().add(discountButtons.get(countD));
                discountButtons.get(countD).setOnAction(eventDiscountButtons);
                countD++;
            }
        }
        initializationDiscountButton();

        /*____________________________________________________________________________________
         * Здесь происходит инициализация столбцов таблицы текущей продажи.
         * В неё добавляются позиции Продуктов.
         * setCellValueFactory определяет что добавляется и в какой столбец.
         ____________________________________________________________________________________*/
        saleTable.setEditable(true);
        productColumn.setEditable(true);
        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        priceColumn.setEditable(true);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        amountColumn.setEditable(true);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        saleTable.setItems(saleProductsObservableList);
        discountColumn.setEditable(true);
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        sumColumn.setEditable(true);
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));

        int countP = 0;
        for (int l = 0; l < mainGridPane.getColumnCount(); l++) {

            for (int h = 0; h < mainGridPane.getRowCount(); h++) {
                Button productButton = new Button();
                productButtons.add(countP, productButton);
                int finalProdButtonsCount = countP;
                productButtons.get(countP).layoutBoundsProperty().addListener((observable, oldValue, newValue) ->
                        productButtons.get(finalProdButtonsCount).setFont(
                                Font.font(Math.sqrt(newValue.getHeight() * 1.5))));
                productButtons.get(countP).setWrapText(true);
                productButtons.get(countP).setStyle("-fx-text-alignment: CENTER; -fx-font-weight: BOLDER");
                productButtons.get(countP).setPrefSize(85.0, 85.0);
                productButtons.get(countP).setVisible(false);
                GridPane.setConstraints(productButtons.get(countP), l, h);
                mainGridPane.getChildren().add(productButtons.get(countP));
                productButtons.get(countP).setOnAction(eventProductButtons);
                countP++;
            }
        }
        productNameButton(productButtons);
    }
    /*____________________________________˄˄˄_____________________________________________
     ___________________________________the end__________________________________________*/

    /*____________________________________start___________________________________________
     * Прочие методы.
     _____________________________________˅˅˅____________________________________________*/
    public void initializationDiscountButton() {
        int countD = 0;
        for (Discount discount : discounts) {
            if (discount.isActive()) {
                discountButtons.get(countD).setAccessibleText(String.valueOf(discount.getDiscountId())); //Id позиции скидки назначается getAccessibleText кнопки.
                discountButtons.get(countD).setText(discount.getDiscount() + "%");
                discountButtons.get(countD).setVisible(true);
                countD++;
            }
        }
        //TODO логика назначения кнопкам процента скидки, если у объекта в ArrayList дисконта в переменной active установлено значение true.
    }

    /**
     * Данный метод делает кнопки с Продуктами/Цифровые кнопки доступными/недоступными.
     * @param buttons - принимаемый параметр - массив кнопок.
     * @param res - значение boolean отражающее действие, которое необходимо совершить с кнопками.
     */
    public void buttonsIsDisable(ArrayList<Button> buttons, boolean res) {
        for (Button button : buttons) {
            button.setDisable(res);
        }
    }

    /**
     * Данный метод делает кнопки с действиями с Добавляемым продуктом активными/неактивными
     * Например: кнопки Работы с Выбранным продуктом недоступны пока не выбран продукт.
     * @param res - тип boolean, который работает как переключатель доступности.
     */
    public void productOperationButtonsIsDisable(boolean res) {
        addProduct.setDisable(res);
        discountButtonActivate.setDisable(res);
        delProduct.setDisable(res);
    }
}