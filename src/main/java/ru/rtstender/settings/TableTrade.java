package ru.rtstender.settings;

public class TableTrade {

    private String tradeNumber;
    private String oosNumber;
    private String startPrice;


    public String getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public String getOosNumber() {
        return oosNumber;
    }

    public void setOosNumber(String oosNumber) {
        this.oosNumber = oosNumber;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    @Override
    public String toString() {
        return "TableTrade{" +
                "Номер закупки/лота = '" + tradeNumber + '\'' +
                " Номер в ЕИС = '" + oosNumber + '\'' +
                ", Начальная цена = '" + startPrice + '\'' +
                '}';
    }
}
