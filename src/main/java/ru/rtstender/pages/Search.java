package ru.rtstender.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import ru.rtstender.settings.TableTrade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.*;

public class Search {

    private SelenideElement title = $(".auction_title");
    private SelenideElement inputNumberTrade = $("input#BaseMainContent_MainContent_txtNumber_txtText");
    private SelenideElement inputNameTrade = $("input#BaseMainContent_MainContent_txtName_txtText");
    private SelenideElement checkboxUseNameTrade = $("#BaseMainContent_MainContent_cbxUseTradeName");
    private SelenideElement checkboxUseNameLot = $("#BaseMainContent_MainContent_cbxUseLotName");
    private SelenideElement inputNameLot = $("#BaseMainContent_MainContent_txtLotName_txtText");
    private SelenideElement inputPublicationDateFrom = $("#BaseMainContent_MainContent_txtPublicationDate_txtDateFrom");
    private SelenideElement inputPublicationDateTo = $("#BaseMainContent_MainContent_txtPublicationDate_txtDateTo");
    private SelenideElement inputPriceFrom = $("#BaseMainContent_MainContent_txtStartPrice_txtRangeFrom");
    private SelenideElement inputPriceTo = $("#BaseMainContent_MainContent_txtStartPrice_txtRangeTo");
    private SelenideElement checkbox223FZ = $("#BaseMainContent_MainContent_chkPurchaseType_0");
    private SelenideElement checkboxCommercialPurchase = $("#BaseMainContent_MainContent_chkPurchaseType_1");
    private SelenideElement buttonSearch = $("#BaseMainContent_MainContent_btnSearch");
    private SelenideElement buttonReset = $("#BaseMainContent_MainContent_btnCancel");
    private SelenideElement table = $("#BaseMainContent_MainContent_jqgTrade");
    private SelenideElement pageCount = $("#sp_1_BaseMainContent_MainContent_jqgTrade_toppager");
    private SelenideElement listBox = $(".ui-pg-selbox");
    private SelenideElement buttonNextPage = $("#next_t_BaseMainContent_MainContent_jqgTrade_toppager");
    private SelenideElement tabCancel = $$("#lotStateTabs li").get(9);

    private static final Logger logger = Logger.getLogger(Search.class);
    private static Properties properties = new Properties();

    private static String dateFrom;
    private static String dateTo;

    public void loadConfigDate() throws IOException {
        properties.load(ClassLoader.getSystemResource("date.properties").openStream());
        dateFrom = properties.getProperty("dateFrom");
        dateTo = properties.getProperty("dateTo");
    }

    public String setInputPublicationDateFrom(){
        logger.info("Дата начала публикации извещения: " + dateFrom);
        inputPublicationDateFrom.setValue(dateFrom);
        title.click();
        return dateFrom;
    }

    public String setInputPublicationDateTo(){
        logger.info("Дата конца публикации извещения: " + dateTo);
        inputPublicationDateTo.setValue(dateTo);
        title.click();
        return dateTo;
    }

    public String setInputPriceFrom(String priceFrom){
        logger.info("Начальная цена: " + priceFrom);
        inputPriceFrom.setValue(priceFrom);
        return priceFrom;
    }

    public void activeCheckbox223FZ(){
        logger.info("Тип закупки: Закупка в соответствии с нормами 223-ФЗ - Активирован");
        if (!checkbox223FZ.isSelected()){
            checkbox223FZ.click();
        }
    }

    public void activeCheckboxCommercialPurchase(){
        logger.info("Тип закупки: Коммерческая закупка - Активирован");
        if (!checkboxCommercialPurchase.isSelected()){
            checkboxCommercialPurchase.click();
        }
    }

    public void clickButtonSearch(){
        logger.info("Поиск...");
        buttonSearch.click();
        sleep(1000);
    }

    public void clickButtonNextPage(){
        buttonNextPage.click();
        sleep(1000);
    }

    public void setListBox(String value){
        listBox.$$("option").find(Condition.value(value)).click();
        sleep(2000);
    }

    public int getPageCount() {
        return Integer.parseInt(pageCount.getText());
    }

    public void clickTabCancel(){
        logger.info("Отмененные лоты");
        tabCancel.click();
        sleep(1000);
    }

    public List<TableTrade> getTable(int pageCount){
        List<TableTrade> table = new ArrayList<>();
        By byRows = By.cssSelector("tbody tr:not(:first-child)");
        List<SelenideElement> rows = this.table.$$(byRows);
        int index = 0;
        for (int i = 0; i < pageCount; i++){
            for (SelenideElement row : rows) {
                if (!row.$$("td").get(5).getAttribute("title").isEmpty()) {
                    table.add(createTable(row));
                    logger.info(table.get(index));
                    index++;
                }
            }
            clickButtonNextPage();
        }
        return table;
    }

    private TableTrade createTable(SelenideElement row) {
        By byColumn = By.cssSelector("td");
        List<SelenideElement> columns = row.$$(byColumn);
        TableTrade table = new TableTrade();
        table.setTradeNumber(columns.get(4).getText());
        table.setOosNumber(columns.get(5).getText());
        table.setStartPrice(columns.get(10).getText());
        return table;
    }

    public long price(List<TableTrade> lots){
        long price = 0L;
        for (int i = 0; i < lots.size(); i++){
            if (lots.get(i).getStartPrice().contains("USD")){
                Float etc = Float.parseFloat(lots.get(i).getStartPrice().replaceAll("\\s|USD", "").replace(",", "."));
                lots.get(i).setStartPrice(String.valueOf((int)(etc * 62.4f)));
            }
            if (lots.get(i).getStartPrice().contains("EUR")){
                Float etc = Float.parseFloat(lots.get(i).getStartPrice().replaceAll("\\s|EUR", "").replace(",","."));
                lots.get(i).setStartPrice(String.valueOf((int)(etc * 73.7)));
            }
            price = price + (long) Float.parseFloat(lots.get(i).getStartPrice()
                    .replaceAll("\\s|руб.", "")
                    .replace(",", "."));
        }
        return price;
    }

    public List<TableTrade> getResultLots(List<TableTrade> allLots, List<TableTrade> cancelLost){
        List<TableTrade> resultTable = new ArrayList<>();
        for (int i = 0; i < cancelLost.size(); i++){
            for (int k = 0; k < allLots.size(); k++){
                if (allLots.get(k).getTradeNumber().equals(cancelLost.get(i).getTradeNumber())){
                    allLots.remove(k);
                };
            }
        }
        resultTable.addAll(allLots);
        return resultTable;
    }

}
