package projekpaa;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.ImageIcon;

/**
 *
 * @author Muhamad Rivai
 */
public class GameFrame extends JFrame{
    public GameFrame(){
        ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + "/src/img/gameicon.png");
        setIconImage(icon.getImage());
        setTitle("Hide and Seek 2D Game");
        setSize(900,600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().add(new GamePanel());
        setVisible(true);
    }
}
