package bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyBoardBuilder {

    public ReplyKeyboard build(List<String> options, int columnsCount) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        int index = 0;
        while (index < options.size()) {
            KeyboardRow row = new KeyboardRow();
            for (int i = 0; i < columnsCount && index < options.size(); i++) {
                row.add(new KeyboardButton(options.get(index++)));
            }
            keyboardRows.add(row);
        }
        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboardRows)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }
}
