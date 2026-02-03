package org.example.tests.base;

import com.codeborne.selenide.Configuration;
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
 * Базовый класс для тестирования приложения Alchemy на физическом устройстве Android.
 *
 * <p><strong>Важное примечание:</strong> Тестирование проводится на физическом устройстве
 * (телефоне), а не на эмуляторе. Это связано с проблемами установки приложения на эмуляторе.
 * Основные функции класса:
 * <ul>
 * <li>Инициализация AndroidDriver с настройками UiAutomator2 для физического устройства</li>
 * <li>Настройка явных ожиданий Selenide (10 секунд)</li>
 * <li>Автоматическая активация приложения перед каждым тестом</li>
 * <li>Корректное завершение работы приложения и очистка ресурсов после тестов</li>
 * <li>Интеграция с Selenide для удобной работы с элементами</li>
 * </ul>
 */
public class BaseTestAlchemy {
    private static final Logger log = LoggerFactory.getLogger(BaseTestAlchemy.class);
    protected AndroidDriver driver;

    private static final String APP_PACKAGE = "com.ilyin.alchemy";
    private static final String APP_ACTIVITY = "com.ilyin.app_google_core.GoogleAppActivity";
    private static final String DEVICE_UDID = "5LY98PFMAIEA4LLR";
    private static final String APPIUM_SERVER_URL = "http://localhost:4723";
    private static final String PLATFORM_NAME = "Android";
    private static final String PLATFORM_VERSION = "15.0";
    private static final String AUTOMATION_NAME = "UiAutomator2";
    private static final boolean NO_RESET = true;
    private static final boolean AUTO_GRANT_PERMISSIONS = false;

    private static final int TIMEOUT = 10000;

    /**
     * Метод настройки перед каждым тестом.
     * Инициализирует соединение с физическим устройством и настраивает окружение для тестирования.
     *
     * <p><strong>Важно:</strong> Этот метод настроен для работы с физическим устройством,
     * так как установка приложения на эмулятор вызывает проблемы.</p>
     *
     * @throws Exception если возникает ошибка при создании драйвера или подключении к Appium серверу
     */
    @BeforeEach
    void setUp() throws Exception {
        Configuration.timeout = TIMEOUT;

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(PLATFORM_NAME)
                .setPlatformVersion(PLATFORM_VERSION)
                .setUdid(DEVICE_UDID)
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
     * Корректно завершает работу приложения и освобождает ресурсы.
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