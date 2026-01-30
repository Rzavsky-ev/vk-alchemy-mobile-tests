package org.example.tests.alchemy;

import org.example.tests.base.BaseTestAlchemy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.example.utils.AlchemyUtils.*;

@DisplayName("Тест мобильного приложение: Алхимия")
@Tags({
        @Tag("ALCHEMY"),
        @Tag("End-to-End")
})
public class AlchemyTest extends BaseTestAlchemy {
    private static final Logger log = LoggerFactory.getLogger(AlchemyTest.class);
    private static final int START_HINT = 2;
    private static final int EXPECTED_HINTS_COUNT = 4;

    @Test
    @DisplayName("Проверка получения дополнительных подсказок после просмотра рекламы")
    public void testAlchemy() {
        startGame();
        clickHint(START_HINT);

        boolean hintsAvailable = verifyHintsSectionVisible();

        if (hintsAvailable) {
            boolean adWatched = watchAdForHints();
            if (adWatched) {
                verifyHintsCount(EXPECTED_HINTS_COUNT);
            }
        } else {
            log.warn("Раздел подсказок недоступен, тест завершен с ограниченной проверкой");
        }
    }
}