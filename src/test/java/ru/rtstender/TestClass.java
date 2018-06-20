package ru.rtstender;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.rtstender.pages.Tender;
import ru.rtstender.settings.Driver;
import ru.rtstender.settings.TableTrade;

import java.io.IOException;
import java.util.List;

import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


public class TestClass {

    private static final Logger logger = Logger.getLogger(TestClass.class);
    private Tender tender;

    @BeforeMethod
    public void setUp() throws IOException {
        Driver.setDriver();
        open(baseUrl);
        tender = new Tender();
        tender.searchPage().loadConfigDate();
    }

    @Test
    public void testClass(){
        tender.searchPage().setInputPublicationDateFrom();
        tender.searchPage().setInputPublicationDateTo();
        tender.searchPage().setInputPriceFrom("0");
        tender.searchPage().activeCheckbox223FZ();
        tender.searchPage().activeCheckboxCommercialPurchase();
        tender.searchPage().clickButtonSearch();
        tender.searchPage().setListBox("100");
        List<TableTrade> tableAll = tender.searchPage().getTable(tender.searchPage().getPageCount());
        logger.info("Количество лотов : " + tableAll.size() + ", Всего на сумму: " + tender.searchPage().price(tableAll) + " руб.");
        tender.searchPage().clickTabCancel();
        List<TableTrade> tableCloset = tender.searchPage().getTable(tender.searchPage().getPageCount());
        logger.info("Количество отмененных лотов: " + tableCloset.size() + ", Всего на сумму: " + tender.searchPage().price(tableCloset) + " руб.");
        List<TableTrade> resultTable = tender.searchPage().getResultLots(tableAll, tableCloset);
        logger.info("Общее количество лотов: " + resultTable.size() + ", Всего на сумму: " + tender.searchPage().price(resultTable) + " руб.");
    }

    @AfterMethod
    public void tearDown(){
        getWebDriver().close();
    }

}
