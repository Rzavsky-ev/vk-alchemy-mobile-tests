package org.example.utils;

import io.appium.java_client.AppiumBy;
import org.example.exceptions.UtilityClassException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.appium.SelenideAppium.$;

/**
 * Утилитарный класс для работы с приложением Alchemy.
 * Содержит методы для взаимодействия с элементами интерфейса и выполнения
 * основных операций в тестах приложения Alchemy.
 */
public class AlchemyUtils {
    private static final Logger log = LoggerFactory.getLogger(AlchemyUtils.class);
    private static final Duration LONG_TIMEOUT = Duration.ofSeconds(60);

    private static final String PLAY_BUTTON = "new UiSelector().text(\"Играть\")";
    private static final String HINTS_SECTION = "new UiSelector().text(\"Ваши подсказки\")";
    private static final String WATCH_BUTTON = "new UiSelector().text(\"Смотреть\")";

    /**
     * Запускает игру, нажимая на кнопку "Играть".
     *
     * <p>Метод ищет кнопку с текстом "Играть" и выполняет нажатие.
     * После успешного выполнения логируется сообщение.</p>
     *
     * @throws com.codeborne.selenide.ex.ElementNotFound  если кнопка "Играть" не найдена
     * @throws org.openqa.selenium.NoSuchElementException если элемент отсутствует в DOM
     */
    public static void startGame() {
        log.info("Нажимаем кнопку 'Играть'");
        $(AppiumBy.androidUIAutomator(PLAY_BUTTON))
                .click();
        log.debug("Кнопка 'Играть' успешно нажата");
    }

    /**
     * Нажимает на подсказку с указанным количеством.
     *
     * <p>Метод ищет элемент, содержащий указанное количество подсказок,
     * проверяет его видимость и выполняет нажатие.</p>
     *
     * @param quantityHint количество подсказок, которое нужно найти и нажать
     * @throws com.codeborne.selenide.ex.ElementNotFound если элемент с указанным количеством не найден
     * @throws com.codeborne.selenide.ex.ElementShould   если элемент не видим
     */
    public static void clickHint(int quantityHint) {
        log.info("Нажимаем на подсказку с количеством: {}", quantityHint);
        $(AppiumBy.androidUIAutomator("new UiSelector().text(\"" + quantityHint + "\")"))
                .shouldBe(visible)
                .click();
    }

    /**
     * Проверяет видимость раздела "Ваши подсказки".
     *
     * <p>Метод выполняет безопасную проверку наличия и видимости
     * раздела с подсказками. В случае ошибки не выбрасывает исключение,
     * а возвращает {@code false}.</p>
     *
     * @return {@code true} если раздел "Ваши подсказки" видим на экране,
     * {@code false} в противном случае
     */
    public static boolean verifyHintsSectionVisible() {
        log.debug("Проверка раздела 'Ваши подсказки'");
        try {
            $(AppiumBy.androidUIAutomator(HINTS_SECTION))
                    .shouldBe(visible);
            return true;
        } catch (Exception e) {
            log.warn("Раздел 'Ваши подсказки' не найден или не отображается: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Нажимает на кнопку "Смотреть" для просмотра рекламы и получения подсказок.
     *
     * <p>Метод пытается найти и нажать кнопку "Смотреть". Используется
     * для получения дополнительных подсказок через просмотр рекламного ролика.</p>
     *
     * <p><strong>Примечание для тестирования:</strong> Так как метод тестовый, то метод
     * написан для рекламы, которая автоматически закрывается после просмотра.
     * Это упрощает тестирование, так как не требуется дополнительной логики
     * для закрытия рекламного окна.</p>
     *
     * @return {@code true} если кнопка "Смотреть" была успешно найдена и нажата,
     * {@code false} в случае любой ошибки
     */
    public static boolean watchAdForHints() {
        log.info("Попытка запуска рекламы для подсказок");
        try {
            $(AppiumBy.androidUIAutomator(WATCH_BUTTON))
                    .shouldBe(visible)
                    .click();
            return true;
        } catch (Exception e) {
            log.warn("Кнопка 'Смотреть' недоступна");
            return false;
        }
    }

    /**
     * Проверяет количество доступных подсказок.
     *
     * <p>Метод ищет элемент, содержащий указанное количество подсказок,
     * и проверяет его видимость и текст. Использует длинный таймаут
     * ({@link #LONG_TIMEOUT}) для ожидания.</p>
     *
     * @param expectedCount ожидаемое количество подсказок
     * @throws AssertionError если проверка количества подсказок не пройдена
     */
    public static void verifyHintsCount(int expectedCount) {
        log.info("Проверяем количество подсказок. Ожидаем: {}", expectedCount);

        $(AppiumBy.androidUIAutomator("new UiSelector().text(\"" + expectedCount + "\")"))
                .shouldBe(visible, LONG_TIMEOUT)
                .shouldHave(text(String.valueOf(expectedCount)));
    }

    /**
     * Приватный конструктор для предотвращения создания экземпляров утилитарного класса.
     *
     * @throws UtilityClassException всегда, при попытке инстанцирования
     */
    private AlchemyUtils() {
        throw new UtilityClassException(getClass());
    }
}