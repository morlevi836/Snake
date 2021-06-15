import javax.swing.*;

public class GameFrame extends JFrame {

    ImageIcon icon = new ImageIcon("images/SnakeIcon.jpg");

    public static void main(String[] args) {
        new GameFrame();
    }

    GameFrame() {
        GamePanel panel = new GamePanel();
        this.add(panel);
        this.setTitle("Java Project - Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
        setIconImage(icon.getImage());
    }
}
