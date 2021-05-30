package Static;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * All the Image of this game
 */
public class ImageGame {
    public static BufferedImage background;
    public static BufferedImage turn_focus;
    public static BufferedImage avatar_player1;
    public static BufferedImage avatar_player2;

    // boss
    public static BufferedImage boss0;
    public static BufferedImage boss6;
    public static BufferedImage eat1boss;
    public static BufferedImage eat2boss;

    // house chosen
    public static BufferedImage houseChosen_1;
    public static BufferedImage houseChosen1_left;
    public static BufferedImage houseChosen1_right;
    public static BufferedImage houseChosen_2;
    public static BufferedImage houseChosen2_left;
    public static BufferedImage houseChosen2_right;

    // pick stone action
    public static BufferedImage dropStone;
    public static BufferedImage pickStone;
    public static BufferedImage pickStone_1;
    public static BufferedImage pickStone_2;
    public static BufferedImage btn_rollBack;

    // stones
    public static BufferedImage[] Stones = new BufferedImage[9];

    // hand action
    public static BufferedImage putHandDown;
    public static BufferedImage holdHand;
    public static BufferedImage emptyHand;

    // HVTDat
    public static BufferedImage HVTDat1;
    public static BufferedImage HVTDat2;
    public static BufferedImage[] HVTDat = new BufferedImage[8];

    // MenuGame
    public static BufferedImage bg_introduce;
    public static BufferedImage btn_pause, pause_panel;
    public static BufferedImage bg_menu;
    public static BufferedImage btn_options, btn_exit, btn_rules, btn_start;
    public static BufferedImage sound_option, btn_sound_on, btn_sound_off;
    public static BufferedImage result_img, victory_icon, lose_icon, playAgain, btn_menu;

    // Rule of game
    public static BufferedImage btn_previous, btn_back, btn_next;
    public static BufferedImage[] rules = new BufferedImage[9];
    static {
        try {
            
            background = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/background.png")));
            turn_focus = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/avatar/turn_focus.png")));
            avatar_player1 = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/avatar/avatar_an.png")));
            avatar_player2 = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/avatar/avatar_dang.png")));


            boss0 = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/boss0.png")));
            boss6 = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/boss6.png")));
            eat1boss = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/eat1boss.png")));
            eat2boss = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/eat2boss.png")));

            houseChosen_1 = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/chosen/houseChosen_1.png")));
            houseChosen1_left = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/chosen/houseChosen1_left.png")));
            houseChosen1_right = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/chosen/houseChosen1_right.png")));
            houseChosen_2 = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/chosen/houseChosen_2.png")));
            houseChosen2_left = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/chosen/houseChosen2_left.png")));
            houseChosen2_right = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/chosen/houseChosen2_right.png")));

            pickStone = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/handAction/pickStone.png")));
            dropStone = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/handAction/pickStone.png")));
            pickStone_1 = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/handAction/pickStone_1.png")));
            pickStone_2 = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/handAction/pickStone_2.png")));

            btn_rollBack = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/rollBack_btn.png")));

            Stones[1] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/Stones_1.png")));
            Stones[2] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/Stones_2.png")));
            Stones[3] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/Stones_3.png")));
            Stones[4] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/Stones_4.png")));
            Stones[5] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/Stones_5.png")));
            Stones[6] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/Stones_6.png")));
            Stones[7] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/Stones_7.png")));
            Stones[8] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/Stones/Stones_8.png")));

            putHandDown = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/handAction/putHandDown.png")));
            holdHand = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/handAction/holdHand.png")));
            emptyHand = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/handAction/emptyHand.png")));

            HVTDat1 = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/HVTDat/HVTDat1.png")));
            HVTDat2 = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/HVTDat/HVTDat2.png")));

            HVTDat[1] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/HVTDat/HVTDat_1.png")));
            HVTDat[2] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/HVTDat/HVTDat_2.png")));
            HVTDat[3] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/HVTDat/HVTDat_3.png")));
            HVTDat[4] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/HVTDat/HVTDat_4.png")));
            HVTDat[5] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/HVTDat/HVTDat_5.png")));
            HVTDat[6] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/HVTDat/HVTDat_6.png")));
            HVTDat[7] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/HVTDat/HVTDat_7.png")));

            bg_introduce = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/introduce.png")));
            btn_pause = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/btn_pause.png")));
            pause_panel = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/pause_panel.png")));

            bg_menu = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/menu/bg_menu.png")));
            btn_options = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/menu/btn_options.png")));
            btn_exit = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/menu/btn_exit.png")));
            btn_start = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/menu/btn_start.png")));
            btn_rules = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/menu/btn_rules.png")));

            sound_option = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/options/sound_option.png")));
            btn_sound_on = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/options/sound_option_on.png")));
            btn_sound_off = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/options/sound_option_off.png")));

            result_img = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/gameOver/result_img.png")));
            victory_icon = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/gameOver/victory_icon.png")));
            lose_icon = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/gameOver/lose_icon.png")));
            btn_menu = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/gameOver/btn_menu.png")));
            playAgain = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/gameOver/playAgain.png")));

            btn_previous = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/menu/btn_previous.png")));
            btn_next = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/menu/btn_next.png")));
            btn_back = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/menu/btn_back.png")));

            for (int i = 1; i <= 8; i++) {
                rules[i] = ImageIO.read(Objects.requireNonNull(ImageGame.class.getResource("/Static/images/menu/rule_" + i + ".png")));
            }

        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger(ImageGame.class.getName()).log(Level.SEVERE, null, e);
        }
    }


}
