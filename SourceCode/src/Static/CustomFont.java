package Static;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;


/**
 * Load .ttf file and register the font and use it
 */
public class CustomFont {
    public static Font mistral = loadFont("/Static/fonts/mistral.TTF");

    public static Font loadFont(String fontUrl) {
        Font font = null;
        try {
            InputStream inputStream = CustomFont.class.getResourceAsStream(fontUrl);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            assert inputStream != null;
            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);

            if (font != null) {
                ge.registerFont(font);
            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return font;
    }
}
