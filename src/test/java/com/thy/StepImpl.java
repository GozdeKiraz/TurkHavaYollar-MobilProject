package com.thy;

import com.thoughtworks.gauge.Step;
import com.thy.model.SelectorInfo;
import com.thy.models.Passenger;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.KeyEventMetaModifier;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.annotation.Nullable;
import java.sql.*;
import java.time.Duration;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class StepImpl extends hookImpl {
    private Passenger yolcuBilgileri;


    public void databaseConnection() {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/thy", "root", "12345678");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT kalkis,varis FROM ucus_yonu Where kalkis='SAW' AND varis='ESB'");
            while (rs.next())
                System.out.println(rs.getString("kalkis") + "  " + rs.getString("varis"));
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM thy.yolcu_bilgileri");
            while (rs1.next()) {
                Passenger yolcuBilgileri = new Passenger();
                yolcuBilgileri.ad = rs1.getString("ad");
                yolcuBilgileri.soyad = rs1.getString("soyad");
                yolcuBilgileri.email = rs1.getString("email");
                yolcuBilgileri.cep_telefon_no = rs1.getNString("cep_telefon_no");

            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public List<MobileElement> findElements(By by) throws Exception {
        List<MobileElement> webElementList = null;
        try {
            webElementList = appiumFluentWait.until(new ExpectedCondition<List<MobileElement>>() {
                @Nullable
                @Override
                public List<MobileElement> apply(@Nullable WebDriver driver) {
                    List<MobileElement> elements = driver.findElements(by);
                    return elements.size() > 0 ? elements : null;
                }
            });
            if (webElementList == null) {
                throw new NullPointerException(String.format("by = %s Web element list not found", by.toString()));
            }
        } catch (Exception e) {
            throw e;
        }
        return webElementList;
    }

    public List<MobileElement> findElementsWithoutAssert(By by) {

        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(by);
        } catch (Exception e) {
        }
        return mobileElements;
    }

    public List<MobileElement> findElementsWithAssert(By by) {

        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(by);
        } catch (Exception e) {
            Assertions.fail("by = %s Elements not found ", by.toString());
            e.printStackTrace();
        }
        return mobileElements;
    }


    public MobileElement findElement(By by) throws Exception {
        MobileElement mobileElement;
        try {
            mobileElement = findElements(by).get(0);
        } catch (Exception e) {
            throw e;
        }
        return mobileElement;
    }

    public MobileElement findElementWithoutAssert(By by) {
        MobileElement mobileElement = null;
        try {
            mobileElement = findElement(by);
        } catch (Exception e) {
            //   e.printStackTrace();
        }
        return mobileElement;
    }

    public MobileElement findElementWithAssertion(By by) {
        MobileElement mobileElement = null;
        try {
            mobileElement = findElement(by);
        } catch (Exception e) {
            Assertions.fail("by = %s Element not found ", by.toString());
            e.printStackTrace();
        }
        return mobileElement;
    }

    public void sendKeysTextByAndroidKey(String text) {

        AndroidDriver androidDriver = (AndroidDriver) driver;
        char[] chars = text.toCharArray();
        String stringValue = "";
        for (char value : chars) {
            stringValue = String.valueOf(value);
            if (Character.isDigit(value)) {
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.valueOf("DIGIT_" + String.valueOf(value))));
            } else if (Character.isLetter(value)) {
                if (Character.isLowerCase(value)) {
                    androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.valueOf(stringValue.toUpperCase())));
                } else {
                    androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.valueOf(stringValue))
                            .withMetaModifier(KeyEventMetaModifier.SHIFT_ON));
                }
            } else if (stringValue.equals("@")) {
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.AT));
            } else if (stringValue.equals(".")) {
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.PERIOD));
            } else if (stringValue.equals(" ")) {
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.SPACE));
            } else {
                Assert.fail("Metod " + stringValue + " desteklemiyor.");
            }
        }
        logger.info(text + " texti AndroidKey yollanarak yazıldı.");
    }

    public MobileElement findElementByKeyWithoutAssert(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);

        MobileElement mobileElement = null;
        try {
            mobileElement = selectorInfo.getIndex() > 0 ? findElements(selectorInfo.getBy())
                    .get(selectorInfo.getIndex()) : findElement(selectorInfo.getBy());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileElement;
    }

    public MobileElement findElementByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);

        MobileElement mobileElement = null;
        try {
            mobileElement = selectorInfo.getIndex() > 0 ? findElements(selectorInfo.getBy())
                    .get(selectorInfo.getIndex()) : findElement(selectorInfo.getBy());
        } catch (Exception e) {
            Assertions.fail("key = %s by = %s Element not found ", key, selectorInfo.getBy().toString());
            e.printStackTrace();
        }
        return mobileElement;
    }


    public List<MobileElement> findElemenstByKeyWithoutAssert(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(selectorInfo.getBy());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileElements;
    }

    public List<MobileElement> findElemenstByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(selectorInfo.getBy());
        } catch (Exception e) {
            Assertions.fail("key = %s by = %s Elements not found ", key, selectorInfo.getBy().toString());
            e.printStackTrace();
        }
        return mobileElements;
    }

    @Step("<seconds> saniye bekle")
    public void waitBySecond(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Step("<key> li elementi bul ve varsa tıkla")
    public void elementeTıkla(String key) {
        MobileElement element;
        element = findElementByKey(key);
        element.click();
    }

    @Step("<text> değerini klavye ile yaz")
    public void sendKeysWithAndroidKey(String text) {
        sendKeysTextByAndroidKey(text);


    }

    @Step("<key> li elementi bul ve <text> değerini yaz")
    public void sendKeysByKey(String key, String text) {
        MobileElement webElement = findElementByKey(key);
        webElement.sendKeys(text);

    }

    @Step("<key> günün tarihinden 2 gun sonraya seçilir")
    public void tarihbilgisi(String key) {

        List<MobileElement> elements = findElemenstByKey(key);
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getText().equals(tarihSaatBilgisi())) {
                elements.get(i).click();
            }
        }

    }

    public String tarihSaatBilgisi() {
        Calendar simdi = Calendar.getInstance();
        int tarih = simdi.get(Calendar.DATE) + 2;
        return String.valueOf(tarih);
    }

    @Step("Uçuşların listesi kontrol edilir ve <key> seçilir")
    public void ucuslistesikontrol(String key) {
        try {
            rastgeleSec(key);
        } catch (Exception ex) {
            logger.info("Seçilen Sefer Dolu");
            throw ex;
        }

    }

    public void rastgeleSec(String key) {
        List<MobileElement> element = findElemenstByKey(key);
        Random random = new Random();
        int index = random.nextInt(element.size());
        element.get(index).click();

    }

    @Step("Swipe Et")
    public void kaydir() {
        ekraniKaydir();
    }

    public void ekraniKaydir() {
        if (driver instanceof AndroidDriver) {
            Dimension d = driver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 80) / 100;
            int swipeEndHeight = (height * 40) / 100;

            new TouchAction(driver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release().perform();

        } else {
            Dimension d = driver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 90) / 100;
            int swipeEndHeight = (height * 40) / 100;

            new TouchAction(driver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release().perform();

        }
    }

    @Step("<key> koltuk seçilir")
    public void bosKoltuk(String key) {
        int turSayisi = 0;
        Random random = new Random();
        try {
            while (turSayisi < 3) {
                List<MobileElement> elements1 = findElemenstByKey(key);
                int index = random.nextInt(elements1.size());
                if (elements1.size() >= 2) {
                    elements1.get(index).click();
                    break;
                } else {
                    ekraniKaydir();
                    turSayisi = turSayisi + 1;
                }

            }
        } catch (Exception ex) {
            logger.info("Uygun Koltuk Bulunamadı");
            throw ex;
        }

    }

    @Step("<key> li elemente ad yaz")
    public void sendToAd(String key) throws SQLException {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/thy", "root", "12345678");
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT ad FROM thy.yolcu_bilgileri");
            while (rs1.next()) {
                Passenger yolcuBilgileri = new Passenger();
                yolcuBilgileri.ad = rs1.getString("ad");


            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Step("<key> li elemente soyad yaz")
    public void sendToSoyad(String key) throws SQLException {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/thy", "root", "12345678");
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT soyad FROM thy.yolcu_bilgileri");
            while (rs1.next()) {
                Passenger yolcuBilgileri = new Passenger();
                yolcuBilgileri.soyad = rs1.getString("soyad");


            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Step("<key> li elemente email yaz")
    public void sendToEmail(String key) throws SQLException {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/thy", "root", "12345678");
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT email FROM thy.yolcu_bilgileri");
            while (rs1.next()) {
                Passenger yolcuBilgileri = new Passenger();
                yolcuBilgileri.soyad = rs1.getString("email");


            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Step("<key> li elemente cep telefonu yaz")
    public void sendToCepNo(String key) throws SQLException {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/thy", "root", "12345678");
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery("SELECT cep_telefon_no FROM thy.yolcu_bilgileri");
            while (rs1.next()) {
                Passenger yolcuBilgileri = new Passenger();
                yolcuBilgileri.cep_telefon_no = rs1.getString("cep_telefon_no");


            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}


