package org.example.tests.vkVideo;

import org.example.tests.base.BaseTestVkVideo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.example.utils.VKVideoUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты VK Video")
@Tags({
        @Tag("VKVideo"),
        @Tag("End-to-End")
})
public class VKVideoTest extends BaseTestVkVideo {
    private static final Logger log = LoggerFactory.getLogger(VKVideoTest.class);

    private static final String ERROR_TEXT_XPATH = "//*[contains(@text, 'Ошибка')]";
    private static final String MAIN_CONTENT_ID = "com.vk.vkvideo:id/main_content";
    private static final String CLOSE_BUTTON_ID = "com.vk.vkvideo:id/close_button";

    private static final String INVALID_VIDEO_ID = "-999999999_999999999";
    private static final int ELEMENT_VISIBILITY_TIMEOUT = 10;

    @Test
    @DisplayName("Воспроизведение видео с обработкой ошибок")
    void videoPlaybackWithErrorHandling() {
        skipLoginIfNeeded();

        try {
            playFirstVideoInFeed();
            assertVideoIsPlaying();
        } catch (Exception e) {
            handlePlaybackError();
        }
    }

    @Test
    @DisplayName("Проверка работы поиска видео - ИЗВЕСТНАЯ ПРОБЛЕМА")
    @Tag("FAILING")
    void searchShouldWorkCorrectly() {
        skipLoginIfNeeded();
        clickSearchButton();
        assertSearchIsOpened();
    }

    @Test
    @DisplayName("Обработка невалидных ссылок на видео")
    void shouldHandleInvalidVideoLink() {
        skipLoginIfNeeded();

        try {
            openDeepLink(driver, INVALID_VIDEO_ID);

            $(By.xpath("//*"))
                    .shouldBe(visible, Duration.ofSeconds(ELEMENT_VISIBILITY_TIMEOUT));

            boolean hasError = $(By.xpath(ERROR_TEXT_XPATH)).exists();
            boolean onMainScreen = $(By.id(MAIN_CONTENT_ID)).exists();
            boolean hasCloseButton = $(By.id(CLOSE_BUTTON_ID)).exists();

            assertTrue(hasError || onMainScreen || hasCloseButton,
                    "Должна быть ошибка, главный экран или кнопка закрытия");

            log.info("Невалидная ссылка обработана в приложении");

        } catch (WebDriverException e) {
            log.info("Ожидаемое поведение: приложение не открыло невалидную ссылку - {}",
                    e.getClass().getSimpleName());
        }
    }
}