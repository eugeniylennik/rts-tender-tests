package ru.rtstender.settings;

import com.codeborne.selenide.WebDriverRunner;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URI;
import java.util.Arrays;

import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Configuration.remote;

public class Driver {

    private static final String CHROME = "chrome";
    private static final String FIREFOX = "firefox";
    private static final String OPERA = "opera";
    private static final boolean IS_USE_SELENIUM_GRID = false;
    private static final String CHROME_DRIVER_PATH = "src/test/resources/chromedriver";
    private static final Logger LOG = Logger.getLogger(Driver.class.getName());
    private static ChromeDriverService service;

    public static void setDriver() {
        timeout = 10000;
        collectionsTimeout = 10000;
        savePageSource = false;
        pageLoadStrategy = "normal";
        browser = "chrome";
        baseUrl = "https://223.rts-tender.ru/supplier/auction/Trade/Search.aspx";
        remote = "";
        if (IS_USE_SELENIUM_GRID) {
            setUpGrid(browser);
        } else {
            setUp(browser);
        }
    }

    private static void setUp(String browser) {
        switch (browser) {
            case CHROME:
            default:
                service = new ChromeDriverService.Builder()
                        .usingDriverExecutable(new File(CHROME_DRIVER_PATH))
                        .usingAnyFreePort()
                        .build();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("disable-infobars");
                options.addArguments("--disable-save-password-bubble");
                options.addArguments("test-type");
                options.addArguments("start-fullscreen");
                options.addArguments("--headless");
                WebDriverRunner.setWebDriver(new ChromeDriver(service, options));
        }
    }

    private static void setUpGrid(String browser) {
        DesiredCapabilities capabilities = null;
        ChromeOptions options = null;
        switch (browser) {
            case CHROME:
            default:
                capabilities = new DesiredCapabilities();
                capabilities.setBrowserName(browser);
                capabilities.setVersion(browserVersion);
                capabilities.setCapability("chrome.switches", Arrays.asList("--disable-extensions"));
                capabilities.setCapability("enableVNC", true);
                options = new ChromeOptions();
                options.addArguments("disable-infobars");
                options.addArguments("--disable-save-password-bubble");
                options.addArguments("test-type");
                options.merge(capabilities);
                break;
            case FIREFOX:
                capabilities = new DesiredCapabilities();
                capabilities.setBrowserName(browser);
                capabilities.setVersion(browserVersion);
                capabilities.setPlatform(Platform.LINUX);
                capabilities.setCapability("enableVNC", true);
                break;
            case OPERA:
                capabilities = new DesiredCapabilities();
                capabilities.setBrowserName(browser);
                capabilities.setVersion(browserVersion);
                capabilities.setPlatform(Platform.LINUX);
                capabilities.setCapability("enableVNC", true);
                OperaOptions opera = new OperaOptions();
                opera.setCapability("browserName", "opera");
                opera.setBinary("/usr/bin/opera");
                capabilities.merge(opera);
                break;
        }

        try {
            RemoteWebDriver remoteWebDriver = null;
            if (browser.equals("chrome")){
                remoteWebDriver = new RemoteWebDriver(URI.create(remote).toURL(), options);
            }
            else{
                remoteWebDriver = new RemoteWebDriver(URI.create(remote).toURL(), capabilities);
            }
            if  (!browser.equals("opera")){
                remoteWebDriver.manage().window().setSize(new Dimension(1920, 1080));
            }
            WebDriverRunner.setWebDriver(remoteWebDriver);
        } catch (Exception e) {
            throw new RuntimeException("HUB is not running on server '" + remote + "'");
        }

    }

}
