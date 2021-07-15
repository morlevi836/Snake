import javax.swing.*;

public class GameFrame extends JFrame {

    ImageIcon icon = new ImageIcon("images/SnakeIcon.jpg");

    public static void main(String[] args) {
        new GameFrame();
    }

    GameFrame() {
        GamePanel panel = new GamePanel();
        setIconImage(icon.getImage());
        this.add(panel);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
    }
}
