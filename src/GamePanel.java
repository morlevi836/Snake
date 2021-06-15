import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private Image backgroundImage;
    private final JButton resetButton = new JButton();
    private final JButton continueButton = new JButton();
    private final JButton playButton = new JButton();

    private final Timer timer = new Timer(Def.DELAY, this);
    private final Random random = new Random();
    private final int[] x = new int[Def.GAME_DIMENSION];
    private final int[] y = new int[Def.GAME_DIMENSION];
    private int snakeBody = 5;
    private int highScore;
    private int score;
    private int point_X;
    private int point_Y;
    private char snakeDirection = 'R';
    private boolean gameRunning = false;
    private boolean spaceNeeded = false;
    private boolean pauseGame = false;
    private String status = "Start";

    GamePanel() {
        this.setPreferredSize(new Dimension(Def.WINDOW_WIDTH, Def.WINDOW_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new movement());
        try {
            backgroundImage = ImageIO.read(new File("images/ImageBackground.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        startGame();
    }

    public void startGame() {
        resetButton.setVisible(false);
        continueButton.setVisible(false);
        gameRunning = true;
        timer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (status.equals("Start")) {
            Start(graphics);
        } else if (pauseGame) {
            Pause(graphics);
        } else if (!gameRunning && status.equals("Running")) {
            if (highScore < score)
                highScore = score;
            spaceNeeded = false;
            this.setBackground(Color.BLACK);
            gameOver(graphics);
        } else {
            graphics.drawImage(backgroundImage, 0, 0, Def.WINDOW_WIDTH, Def.WINDOW_HEIGHT, this);
            draw(graphics);
        }
    }

    private void Start(Graphics graphics) {
        this.setBackground(new Color(37, 158, 1));
        gameRunning = false;
        spaceNeeded = true;

        //SNAKE text
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Algerian", Font.BOLD, 120));
        FontMetrics metrics_1 = getFontMetrics(graphics.getFont());
        graphics.drawString("SNAKE", (Def.WINDOW_WIDTH - metrics_1.stringWidth("SNAKE")) / 2, Def.WINDOW_HEIGHT / 2 - 60);
        //Space text
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Dubai", Font.BOLD, 60));
        FontMetrics metrics_2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Press Space To Start", (Def.WINDOW_WIDTH - metrics_2.stringWidth("Press Space To Start")) / 2, Def.WINDOW_HEIGHT / 2 + 15);
        //Creators text
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Serif", Font.BOLD, 20));
        FontMetrics metrics_3 = getFontMetrics(graphics.getFont());
        graphics.drawString("© Mor Levi, David Gruzmark", (Def.WINDOW_WIDTH - metrics_3.stringWidth("© Mor Levi, David Gruzmark")) / 2, Def.WINDOW_HEIGHT - 10);

        playButton.setVisible(true);
        playButton.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
        playButton.setForeground(Color.BLACK);
        playButton.setText("Play");
        playButton.setSize(150, 50);
        playButton.setLocation(Def.WINDOW_WIDTH / 2 - 80, Def.WINDOW_HEIGHT / 2 + 110);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(Def.WINDOW_WIDTH / 2 - 84, Def.WINDOW_HEIGHT / 2 + 106, 157, 57);
        playButton.setBackground(Color.RED);
        this.add(playButton);
        playButton.addActionListener(e -> {
            playButton.setVisible(false);
            status = "Running";
            gameRunning = true;
        });
        newPoint();
    }

    private void Pause(Graphics graphics) {
        //Pause text
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Algerian", Font.BOLD, 90));
        FontMetrics metrics_1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Paused", (Def.WINDOW_WIDTH - metrics_1.stringWidth("Game Paused")) / 2, Def.WINDOW_HEIGHT / 2 - 60);

        setFocusable(true);
        continueButton.setVisible(true);
        continueButton.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        continueButton.setForeground(Color.BLACK);
        continueButton.setText("Continue");
        continueButton.setSize(225, 75);
        continueButton.setLocation(Def.WINDOW_WIDTH / 2 - 119, Def.WINDOW_HEIGHT / 2 + 70);
        continueButton.setBackground(Color.GREEN);
        timer.stop();
        this.add(continueButton);
        continueButton.addActionListener(e -> {
            continueButton.setVisible(false);
            gameRunning = true;
            pauseGame = false;
            startGame();
        });

        for (int i = snakeBody; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        //Space text
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Serif", Font.BOLD, 30));
        FontMetrics metrics_2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Press Space To Continue", (Def.WINDOW_WIDTH - metrics_2.stringWidth("Press Space To Continue")) / 2 - 6, Def.WINDOW_HEIGHT - 95 );
    }

    public void gameOver(Graphics graphics) {
        //game over text
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Algerian", Font.BOLD, 95));
        FontMetrics metrics_1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (Def.WINDOW_WIDTH - metrics_1.stringWidth("Game Over")) / 2, Def.WINDOW_HEIGHT / 2 - 60);
        //score text
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Comic Sans MS", Font.BOLD, 55));
        FontMetrics metrics_2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + score, (Def.WINDOW_WIDTH - metrics_2.stringWidth("Score: " + score)) / 2 - 5, Def.WINDOW_HEIGHT / 2 + 20);
        //high score text
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        FontMetrics metrics_3 = getFontMetrics(graphics.getFont());
        graphics.drawString("High Score: " + highScore, (Def.WINDOW_WIDTH - metrics_3.stringWidth("High Score: " + highScore)) / 2 - 5, Def.WINDOW_HEIGHT / 2 + 65);

        setFocusable(true);
        resetButton.setVisible(true);
        resetButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        resetButton.setForeground(Color.BLACK);
        resetButton.setText("Reset");
        resetButton.setSize(150, 50);
        resetButton.setLocation(Def.WINDOW_WIDTH / 2 - 80, Def.WINDOW_HEIGHT / 2 + 110);
        resetButton.setBackground(Color.GREEN);
        timer.stop();

        this.add(resetButton);
        resetButton.addActionListener(e -> {
            resetButton.setVisible(false);
            score = 0;
            snakeBody = 5;
            snakeDirection = 'R';
            gameRunning = true;
            Def.DELAY = 100;
            timer.setDelay(Def.DELAY);
            setBackground(new Color(random.nextInt()));
            newPoint();
            startGame();
        });

        for (int i = snakeBody; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        //Space text
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Serif", Font.BOLD, 20));
        FontMetrics metrics_4 = getFontMetrics(graphics.getFont());
        graphics.drawString("Press Space To Reset", (Def.WINDOW_WIDTH - metrics_4.stringWidth("Press Space To Reset")) / 2, Def.WINDOW_HEIGHT - 100 );
    }

    public void draw(Graphics graphics) {
            //points setting
            graphics.setColor(Color.RED);
            graphics.fillOval(point_X, point_Y, Def.DIMENSION, Def.DIMENSION);
            graphics.setColor(Color.BLACK);
            graphics.drawRoundRect(point_X, point_Y, Def.DIMENSION, Def.DIMENSION, 20, 20);
            //snake setting
            for (int i = 0; i < snakeBody; i++) {
                if (i == 0) {
                    graphics.setColor(new Color(35, 229, 0));
                } else {
                    graphics.setColor(new Color(35, 137, 0));
                }
                graphics.fill3DRect(x[i], y[i], Def.DIMENSION, Def.DIMENSION, true);
                graphics.setColor(Color.BLACK);
                graphics.drawRoundRect(x[i], y[i], Def.DIMENSION, Def.DIMENSION, 0, 0);
            }
            //Score tex
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Comic Sans MS", Font.BOLD, 35));
            FontMetrics metrics_1 = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + score, (Def.WINDOW_WIDTH - metrics_1.stringWidth("Score: " + score)) - 10, Def.WINDOW_HEIGHT - 15);
            //high score text
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Comic Sans MS", Font.ITALIC, 20));
            FontMetrics metrics_2 = getFontMetrics(graphics.getFont());
            graphics.drawString("High Score: " + highScore, (Def.WINDOW_WIDTH - metrics_2.stringWidth("High Score: " + highScore)) - 22, Def.WINDOW_HEIGHT - 52);
    }

    public void newPoint() {
        point_X = random.nextInt((Def.WINDOW_WIDTH / Def.DIMENSION)) * Def.DIMENSION;
        point_Y = random.nextInt((Def.WINDOW_HEIGHT / Def.DIMENSION)) * Def.DIMENSION;
    }

        public void snakeMovement() {
        for (int i = snakeBody; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (snakeDirection) {
            case 'U' -> y[0] -= Def.DIMENSION;
            case 'D' -> y[0] += Def.DIMENSION;
            case 'R' -> x[0] += Def.DIMENSION;
            case 'L' -> x[0] -= Def.DIMENSION;
        }
    }

    public void checkPoints() {
        if ((x[0] == point_X) && (y[0] == point_Y)) {
            snakeBody++;
            score++;
            if (Def.DELAY > 37) {
                if (score % 5 == 0) {
                    Def.DELAY = Def.DELAY - 3;
                    timer.setDelay(Def.DELAY);
                }
            }
            newPoint();
        }
    }

    public void checkCollisions() {
        //checks if head collides with body
        for (int i = snakeBody; i > 0; i--) {
            if ((x[0] == x[i] && y[0] == y[i])) {
                gameRunning = false;
                break;
            }
        }
        //checks if head touches left border
        if (x[0] < 0) {
            x[0] = Def.WINDOW_WIDTH;
        }
        //checks if head touches right border
        if (x[0] > Def.WINDOW_WIDTH) {
            x[0] = 0;
        }
        //checks if head touches top border
        if (y[0] < 0) {
            y[0] = Def.WINDOW_HEIGHT;
        }
        //checks if head touches bottom border
        if (y[0] > Def.WINDOW_HEIGHT) {
            y[0] = 0;
        }
        if (!gameRunning) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (gameRunning) {
            snakeMovement();
            checkPoints();
            checkCollisions();
        }
        repaint();
    }

    public class movement extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event) {
            int keyCode = event.getKeyCode();

            //for the start panel
            if (keyCode == KeyEvent.VK_SPACE && spaceNeeded) {
                playButton.setVisible(false);
                status = "Running";
                gameRunning = true;
            }

            //for the pause panel
            if (keyCode == KeyEvent.VK_ESCAPE && gameRunning) {
                pauseGame = true;
            }

            //for the pause panel
            if (keyCode == KeyEvent.VK_SPACE && pauseGame) {
                gameRunning = true;
                pauseGame = false;
                for (int i = snakeBody; i > 0; i--) {
                    x[i] = x[i - 1];
                    y[i] = y[i - 1];
                }
                startGame();
            }

            //for the game over panel
            if (keyCode == KeyEvent.VK_SPACE && !gameRunning) {
                resetButton.setVisible(false);
                score = 0;
                snakeBody = 5;
                snakeDirection = 'R';
                gameRunning = true;
                for (int i = snakeBody; i > 0; i--) {
                    x[i] = x[i - 1];
                    y[i] = y[i - 1];
                }
                Def.DELAY = 100 ;
                timer.setDelay(Def.DELAY);
                setBackground(new Color(random.nextInt()));
                newPoint();
                startGame(); 
            }

            //for the snake movement
            if (status.equals("Running")) {
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_D:
                        if (snakeDirection != 'L') {
                            snakeDirection = 'R';
                        }
                    case KeyEvent.VK_RIGHT:
                        if (snakeDirection != 'L') {
                            snakeDirection = 'R';
                        }
                        break;
                    case KeyEvent.VK_A:
                        if (snakeDirection != 'R') {
                            snakeDirection = 'L';
                        }
                    case KeyEvent.VK_LEFT:
                        if (snakeDirection != 'R') {
                            snakeDirection = 'L';
                        }
                        break;
                    case KeyEvent.VK_W:
                        if (snakeDirection != 'D') {
                            snakeDirection = 'U';
                        }
                    case KeyEvent.VK_UP:
                        if (snakeDirection != 'D') {
                            snakeDirection = 'U';
                        }
                        break;
                    case KeyEvent.VK_S:
                        if (snakeDirection != 'U') {
                            snakeDirection = 'D';
                        }
                    case KeyEvent.VK_DOWN:
                        if (snakeDirection != 'U') {
                            snakeDirection = 'D';
                            break;
                        }
                }
            }
        }
    }
}

