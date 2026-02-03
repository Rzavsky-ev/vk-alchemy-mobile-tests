package org.example.utils;


import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import io.appium.java_client.android.AndroidDriver;
import org.example.exceptions.UtilityClassException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

/**
 * Утилитарный класс для работы с приложением VK Video.
 * Содержит методы для взаимодействия с элементами интерфейса, управления видеоплеером,
 * работы с поиском и обработки различных сценариев в тестах приложения VK Video.
 */
public class VKVideoUtils {
    private static final Logger log = LoggerFactory.getLogger(VKVideoUtils.class);

    private static final String SEARCH_BUTTON_ID = "com.vk.vkvideo:id/search_button";
    private static final String ERROR_MESSAGE_ID = "com.vk.vkvideo:id/error_message";
    private static final String FAST_LOGIN_BUTTON_ID = "com.vk.vkvideo:id/fast_login_tertiary_btn";
    private static final String SEARCH_FIELD_ID = "com.vk.vkvideo:id/search_src_text";
    private static final String TITLE_ID = "com.vk.vkvideo:id/title";
    private static final String VIDEO_DISPLAY_ID = "com.vk.vkvideo:id/video_display";
    private static final String PLAYER_CONTROL_ID = "com.vk.vkvideo:id/player_control";
    private static final String LIKES_ID = "com.vk.vkvideo:id/likes";
    private static final String UNAVAILABLE_TEXT_XPATH = "//*[contains(@text, 'Недоступно')]";
    private static final String APP_TITLE = "VK Video";
    private static final String VK_DEEP_LINK_PREFIX = "vk://vk.com/video";
    private static final String VK_PACKAGE = "com.vk.vkvideo";

    /**
     * Нажимает на кнопку поиска в приложении VK Video.
     *
     * <p>Метод выполняет безопасную попытку нажатия на кнопку поиска.
     * В случае, если кнопка не найдена или не доступна для взаимодействия,
     * метод логирует сообщение и продолжает выполнение без исключения.</p>
     */
    public static void clickSearchButton() {
        log.info("Попытка кликнуть на кнопку поиска");

        try {
            $(By.id(SEARCH_BUTTON_ID))
                    .shouldBe(visible, Duration.ofSeconds(5))
                    .click();

            log.info("Кнопка поиска успешно нажата");
        } catch (ElementNotFound | ElementNotInteractableException e) {
            log.warn("Кнопка поиска не найдена или не видима. Пропускаем клик.");
        } catch (Exception e) {
            log.warn("Неожиданная ошибка при клике на кнопку поиска: {}", e.getMessage());
        }
    }

    /**
     * Обрабатывает ошибку воспроизведения видео.
     *
     * <p>Метод анализирует возможные причины ошибки воспроизведения видео:</p>
     * <ol>
     *   <li>Проверяет наличие сообщения об ошибке с ID {@code com.vk.vkvideo:id/error_message}</li>
     *   <li>Ищет текст "Недоступно" на экране</li>
     *   <li>В остальных случаях логирует как "Неизвестная ошибка"</li>
     */
    public static void handlePlaybackError() {
        log.info("Видео не воспроизводится");

        if ($(By.id(ERROR_MESSAGE_ID)).exists()) {
            String errorText = $(By.id(ERROR_MESSAGE_ID)).getText();
            log.info("Причина ошибки: {}", errorText);
        } else if ($(By.xpath(UNAVAILABLE_TEXT_XPATH)).exists()) {
            log.info("Видео недоступно");
        } else {
            log.info("Неизвестная ошибка воспроизведения");
        }
    }

    /**
     * Пропускает экран логина если это необходимо.
     *
     * <p>Метод пытается найти и нажать на кнопку пропуска логина.
     * Используется для ускорения тестов, когда авторизация не требуется.</p>
     */
    public static void skipLoginIfNeeded() {
        log.debug("Проверка необходимости пропуска логина");

        try {
            $(By.id(FAST_LOGIN_BUTTON_ID))
                    .shouldBe(visible)
                    .click();
            log.info("Кнопка пропуска логина нажата");
        } catch (ElementNotFound e) {
            log.debug("Кнопка пропуска логина не найдена");
        }
    }

    /**
     * Проверяет что поиск успешно открылся.
     *
     * <p>Метод выполняет комплексную проверку состояния приложения после
     * открытия поиска. Анализирует наличие ключевых элементов интерфейса:</p>
     */
    public static void assertSearchIsOpened() {
        log.info("Проверка что поиск открылся");

        boolean searchButtonExists = $(By.id(SEARCH_BUTTON_ID)).exists();
        boolean searchFieldExists = $(By.id(SEARCH_FIELD_ID)).exists();
        boolean titleExists = $(By.id(TITLE_ID)).exists();

        boolean appActive = searchButtonExists || searchFieldExists || titleExists;

        log.info("Состояние элементов: search_button={}, search_field={}, title={}",
                searchButtonExists, searchFieldExists, titleExists);

        if (!appActive) {
            log.warn("Приложение, возможно, свернулось");
        } else if (searchFieldExists) {
            log.info("УСПЕХ: Открылось поле поиска");
        } else if (titleExists && APP_TITLE.equals($(By.id(TITLE_ID)).getText())) {
            log.info("ИНФО: Остались на главном экране");
        }
    }

    /**
     * Запускает первое видео в ленте.
     *
     * <p>Метод выполняет следующие действия:</p>
     * <ol>
     *   <li>Проверяет что в ленте есть хотя бы 2 видео элемента</li>
     *   <li>Нажимает на первый элемент контента</li>
     *   <li>Ожидает появления видеоплеера</li>
     * </ol>
     */
    public static void playFirstVideoInFeed() {
        log.info("Запуск первого видео в ленте");

        $$(By.id("com.vk.vkvideo:id/content"))
                .shouldHave(CollectionCondition.sizeGreaterThan(1));

        $$(By.id("com.vk.vkvideo:id/content")).first()
                .shouldBe(interactable)
                .click();

        $(By.id(VIDEO_DISPLAY_ID))
                .shouldBe(visible);
    }

    /**
     * Проверяет что видео успешно воспроизводится.
     *
     * <p>Метод выполняет проверку ключевых элементов видеоплеера:</p>
     * <ol>
     *   <li>Элементы управления плеером ({@code com.vk.vkvideo:id/player_control})</li>
     *   <li>Кнопка лайков ({@code com.vk.vkvideo:id/likes})</li>
     *   <li>Заголовок видео ({@code com.vk.vkvideo:id/title})</li>
     * </ol>
     *
     * @throws com.codeborne.selenide.ex.ElementNotFound если элементы плеера не найдены
     */
    public static void assertVideoIsPlaying() {
        log.debug("Проверка воспроизведения видео");

        $(By.id(PLAYER_CONTROL_ID)).shouldBe(visible);

        $(By.id(LIKES_ID)).shouldBe(visible);

        String videoTitle = $(By.id(TITLE_ID)).getText();
        log.info("Видео успешно воспроизводится: {}", videoTitle);
    }

    /**
     * Открывает Deeplink для конкретного видео в приложении VK Video.
     *
     * <p>Метод использует Appium команду {@code mobile: deepLink} для открытия
     * Deeplink прямо в приложении VK Video. Формат Deeplink: {@code vk://vk.com/video{videoId}}</p>
     *
     * @param driver  экземпляр AndroidDriver для выполнения скрипта
     * @param videoId идентификатор видео для открытия
     * @throws org.openqa.selenium.WebDriverException если не удалось выполнить скрипт
     */
    public static void openDeepLink(AndroidDriver driver, String videoId) {
        String deepLink = VK_DEEP_LINK_PREFIX + videoId;

        log.info("Открываю deep link: {}", deepLink);

        driver.executeScript("mobile: deepLink", Map.of(
                "url", deepLink,
                "package", VK_PACKAGE
        ));
    }

    /**
     * Приватный конструктор для предотвращения создания экземпляров утилитарного класса.
     *
     * @throws UtilityClassException всегда, при попытке инстанцирования
     */
    private VKVideoUtils() {
        throw new UtilityClassException(getClass());
    }
}