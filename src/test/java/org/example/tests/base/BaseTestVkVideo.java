package org.example.tests.base;

import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static com.codeborne.selenide.Selenide.closeWebDriver;

/**
 * Базовый класс для тестирования приложения VK Video на Android эмуляторе.
 * Основные функции класса:
 * <ul>
 * <li>Инициализация AndroidDriver с настройками UiAutomator2 для эмулятора</li>
 *   <li>Автоматическая активация приложения VK Video перед каждым тестом</li>
 *   <li>Корректное завершение работы приложения и очистка ресурсов после тестов</li>
 *   <li>Интеграция с Selenide для удобной работы с элементами</li>*
 *   </ul>
 */
public class BaseTestVkVideo {

    private static final Logger log = LoggerFactory.getLogger(BaseTestVkVideo.class);
    protected AndroidDriver driver;

    private static final String PLATFORM_NAME = "Android";
    private static final String PLATFORM_VERSION = "11.0";
    private static final String DEVICE_NAME = "emulator-5554";
    private static final String AUTOMATION_NAME = "UiAutomator2";
    private static final String APP_PACKAGE = "com.vk.vkvideo";
    private static final String APP_ACTIVITY = "com.vk.video.screens.main.MainActivity";
    private static final String APPIUM_SERVER_URL = "http://localhost:4723";
    private static final boolean NO_RESET = true;
    private static final boolean AUTO_GRANT_PERMISSIONS = false;

    /**
     * Метод настройки перед каждым тестом.
     * Инициализирует соединение с Android эмулятором и настраивает окружение для тестирования VK Video.
     *
     * @throws Exception если возникает ошибка при создании драйвера или подключении к Appium серверу
     */
    @BeforeEach
    void setUp() throws Exception {

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(PLATFORM_NAME)
                .setPlatformVersion(PLATFORM_VERSION)
                .setDeviceName(DEVICE_NAME)
                .setAutomationName(AUTOMATION_NAME)
                .setAppPackage(APP_PACKAGE)
                .setAppActivity(APP_ACTIVITY)
                .setNoReset(NO_RESET)
                .setAutoGrantPermissions(AUTO_GRANT_PERMISSIONS);

        URI appiumServerUri = URI.create(APPIUM_SERVER_URL);
        this.driver = new AndroidDriver(appiumServerUri.toURL(), options);

        WebDriverRunner.setWebDriver(driver);
        driver.activateApp(APP_PACKAGE);
    }

    /**
     * Метод очистки после каждого теста.
     * Корректно завершает работу приложения VK Video и освобождает ресурсы.
     */
    @AfterEach
    void tearDown() {
        try {
            if (driver != null) {
                driver.terminateApp(APP_PACKAGE);
            }
        } catch (Exception e) {
            log.warn("Не удалось корректно закрыть приложение: {}", e.getMessage());
        } finally {
            closeWebDriver();
        }
    }
}