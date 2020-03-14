package com.thy;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.thy.selector.Selector;
import com.thy.selector.SelectorFactory;
import com.thy.selector.SelectorType;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class hookImpl {
    public Logger logger = LoggerFactory.getLogger(getClass());
    public static AppiumDriver<MobileElement> driver;
    public boolean localAndroid=true;
    public static Selector selector;
    public static FluentWait<AppiumDriver<MobileElement>> appiumFluentWait;


    @BeforeScenario
    public void beforeScenario() throws MalformedURLException {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!Test baslıyor!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (StringUtils.isEmpty(System.getenv("key"))) {
            if (localAndroid) {
                logger.info("Local Browser");
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities
                        .setCapability(MobileCapabilityType.PLATFORM, MobilePlatform.ANDROID);
                desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
                desiredCapabilities.setCapability(MobileCapabilityType.VERSION, "10.0");
                desiredCapabilities.setCapability("avd", "NEXUS_5X_API_29");
                desiredCapabilities
                        .setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                                "com.turkishairlines.mobile");
                desiredCapabilities
                        .setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                                "com.turkishairlines.mobile.ui.main.MainActivity");
                desiredCapabilities.setCapability(MobileCapabilityType.UDID, "emulator-5554");

                // desiredCapabilities
                //       .setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                //             "com.enerjisa.perakende.mobilislem");
                //desiredCapabilities
                //      .setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                //            "com.enerjisa.perakende.mobilislem.activities.SplashActivity");
                desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
                desiredCapabilities
                        .setCapability(MobileCapabilityType.NO_RESET, true);
                desiredCapabilities
                        .setCapability(MobileCapabilityType.FULL_RESET, false);
                desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 3000);
                desiredCapabilities.setCapability("unicodeKeyboard", true);
                desiredCapabilities.setCapability("resetKeyboard", true);
                URL url = new URL("http://127.0.0.1:4723/wd/hub");
                driver = new AndroidDriver(url, desiredCapabilities);
            }

            else {
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities
                        .setCapability(MobileCapabilityType.PLATFORM, MobilePlatform.IOS);
                desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
                desiredCapabilities
                        .setCapability(MobileCapabilityType.UDID, "b3cff391ba9dab26e91d2166fd5ea426930c8a");
                desiredCapabilities
                        .setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.koton");
                desiredCapabilities
                        .setCapability(MobileCapabilityType.DEVICE_NAME, "Emre iPhone");

                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.0");
                desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, true);
                desiredCapabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
                desiredCapabilities.setCapability("connectHardwareKeyboard", false);

                desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);

                desiredCapabilities.setCapability("sendKeyStrategy","setValue");

                URL url = new URL("http://127.0.0.1:4723/wd/hub");
                driver = new IOSDriver(url, desiredCapabilities);


            }
        } else {
            String hubURL = "http://hub.testinium.io/wd/hub";
            DesiredCapabilities capabilities = new DesiredCapabilities();
            System.out.println("key:" + System.getenv("key"));
            System.out.println("platform" + System.getenv("platform"));
            System.out.println("version" + System.getenv("version"));

            if (System.getenv("platform").equals("ANDROID")) {
                capabilities.setCapability("key", System.getenv("key"));
                capabilities
                        .setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                                "com.turkishairlines.mobile");
                capabilities
                        .setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                                "com.turkishairlines.mobile.ui.main.MainActivity");
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,"uiautomator2");
                capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
                capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
                capabilities.setCapability("unicodeKeyboard", false);
                capabilities.setCapability("resetKeyboard", false);
                driver = new AndroidDriver(new URL(hubURL), capabilities);
                localAndroid = true;
            } else {
                // capabilities.setCapability(CapabilityType.PLATFORM, Platform.MAC);
                capabilities.setCapability("usePrebuiltWDA", true);
                //capabilities.setCapability("appium:maxTypeFrequency", 5);
                capabilities.setCapability("key", System.getenv("key"));
                capabilities.setCapability("waitForAppScript", "$.delay(1000);");
                capabilities.setCapability("bundleId", "com.koton");
                capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,60000);

                capabilities.setCapability("usePrebuiltWDA",true); //TimeOut
                driver = new IOSDriver(new URL(hubURL), capabilities);
                localAndroid = false;
            }
        }
        selector = SelectorFactory
                .createElementHelper(localAndroid ? SelectorType.ANDROID : SelectorType.IOS);
        //appiumDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        appiumFluentWait = new FluentWait<AppiumDriver<MobileElement>>(driver);
        appiumFluentWait.withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(450))
                .ignoring(NoSuchElementException.class);
    }

    @AfterScenario
    public void afterScenario() {
        if(driver != null)
            driver.quit();
    }
}
