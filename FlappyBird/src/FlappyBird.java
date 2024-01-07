import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;

class Bird {
    int x, y, velocity;
    public Bird() {
        x = 50;
        y = 200;
        velocity = 0;
    }
    public void jump() {
        velocity = -10;
    }
    public void move() {
        velocity += 1;
        y += velocity;
    }
}
class Pipe {
    int x, height, gap, speed;
    public Pipe(int x) {
        this.x = x;
        height = new Random().nextInt(200) + 50;
        gap = 150;
        speed = 5;
    }
    public void move() {
        x -= speed;
    }
    public boolean isOutOfScreen() {
        return x < -50;
    }
    public boolean isColliding(Bird bird) {
        return bird.x + 30 > x && bird.x < x + 50 && (bird.y < height || bird.y + 30 > height + gap);
    }
}
class FlappyPanel extends JPanel implements ActionListener, KeyListener {
    private Bird bird;
    private List<Pipe> pipes;
    private Timer timer;
    private boolean gameRunning;
    private int score;
    private Image backgroundImage;
    public FlappyPanel() {
        bird = new Bird();
        pipes = new ArrayList<>();
        gameRunning = false;
        score = 0;
        timer = new Timer(20, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        backgroundImage = new ImageIcon("src/background.jpg").getImage();
    }

    private void resetGame() {
        bird = new Bird();
        pipes.clear();
        gameRunning = true;
        score = 0;
        generatePipes();
    }

    private void generatePipes() {
        pipes.clear();
        for (int i = 0; i < 3; i++) {
            pipes.add(new Pipe(400 + i * 200));
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            bird.move();

            for (Pipe pipe : pipes) {
                pipe.move();
                if (pipe.isOutOfScreen()) {
                    pipe.x += 600;
                    pipe.height = new Random().nextInt(200) + 50;
                    score++;
                }

                if (pipe.isColliding(bird)) {
                    gameRunning = false;
                }
            }

            if (bird.y > getHeight() || bird.y < 0) {
                gameRunning = false;
            }

            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        g.setColor(Color.CYAN);
        g.fillRect(bird.x, bird.y, 30, 30);

        g.setColor(Color.GREEN);
        for (Pipe pipe : pipes) {
            g.fillRect(pipe.x, 0, 50, pipe.height);
            g.fillRect(pipe.x, pipe.height + pipe.gap, 50, getHeight() - pipe.height - pipe.gap);
        }

        if (!gameRunning) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            FontMetrics metrics = g.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth("OYUNU KAYBETTİN EŞEK OYNAMA DAHA")) / 2;
            int y = getHeight() / 2;
            g.drawString("Oyunu Oynamak İçin SPACE", x, y);
        }

        // Skor kısmını çiz
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Skorun LA GARDAS: " + score, getWidth() - 250, 30);
    }

    public void jump() {
        if (gameRunning) {
            bird.jump();
        } else {
            resetGame();
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            jump();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}

public class FlappyBird extends JFrame {
    public FlappyBird() {
        setTitle("Flappy Bird Gardas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);
        setResizable(true);
        setLocationRelativeTo(null);

        FlappyPanel flappyPanel = new FlappyPanel();
        add(flappyPanel);

        pack();
        setSize(400, 400);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FlappyBird();
        });
    }
}
