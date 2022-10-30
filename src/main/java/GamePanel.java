import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 60;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;
    int turboX;
    int turboY;
    boolean turbo;
    int turboRun;
    int slowX;
    int slowY;
    boolean slow;
    int slowRun;


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(250, 188, 220));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        turboRun = DELAY / 2;
        slowRun = DELAY * 2;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        //Linhas opcionais
        //OPTIONAL (matrix grid)
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
                g.setColor(new Color(250, 198, 224, 255));
            }

            //Apples / maçãs
            g.setColor(new Color(85, 192, 21));
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            g.setColor(new Color(239, 197, 28));
            g.fillOval(turboX, turboY, UNIT_SIZE, UNIT_SIZE);

            g.setColor(new Color(199, 0, 0));
            g.fillOval(slowX, slowY, UNIT_SIZE, UNIT_SIZE);

            //snake body
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.black);
                    g.fillRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, 1, 15);
                } else {
                    //g.setColor(new Color(56, 56, 56));
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            //portfolio text
            g.setColor(new Color(0, 0, 0, 29));
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics5 = getFontMetrics(g.getFont());
            g.drawString("mgaia.tech", (SCREEN_WIDTH - metrics5.stringWidth("mgaia.tech")) / 2, SCREEN_HEIGHT / 2);

            //Score Text
            g.setColor(Color.black);
            g.setFont(new Font("Calibri", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        turboX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        turboY = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        slowX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        slowY = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;

    }


    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        /* switch (direction) {
            case 'W':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'S':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'A':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'D':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }*/

        //Lambda da função acima
        //Lambda Expression
        switch (direction) {
            case 'W' -> y[0] = y[0] - UNIT_SIZE;
            case 'S' -> y[0] = y[0] + UNIT_SIZE;
            case 'A' -> x[0] = x[0] - UNIT_SIZE;
            case 'D' -> x[0] = x[0] + UNIT_SIZE;

        }

    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            slow = false;
            turbo = false;
            newApple();
        }

        if ((x[0] == slowX) && (y[0] == slowY)) {
            if (slowRun > 0) {
                turbo = false;
                slow = true;
                applesEaten--;
                newApple();
            }
        }
        if ((x[0] == turboX) && (y[0] == turboY)) {
            if (turboRun > 0) {
                turbo = true;
                slow = false;
                applesEaten++;
                newApple();
            }

        }

    }

    public void checkCollisions() {
        //verifica se a cabeça colide com o corpo
        //checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //verifica se a cabeça toca a borda esquerda
        //checks if head touches left border
        if (x[0] < 0) {
            running = false;
        }

        //verifica se a cabeça toca a borda direita
        //checks if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }

        //verifica se a cabeça toca a borda superior
        //checks if head touches top border
        if (y[0] < 0) {
            running = false;
        }

        //verifica se a cabeça toca a borda inferior
        //checks if head touches bottom border
        if (y[0] > SCREEN_WIDTH) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }

    }

    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.black);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        //Restart text
        g.setColor(Color.red);
        g.setFont(new Font("Calibri", Font.BOLD, 25));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Click on the button to play another game", (SCREEN_WIDTH - metrics3.stringWidth("Click on button to play another game")) / 2, 500);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //implementa o modo turbo / implements turbo mode
        if (running) {
            if (turbo) {
                timer.setDelay(30);
            } else if (slow) {
                timer.setDelay(150);
            } else {
                timer.setDelay(65);
            }

        }
        move();
        checkApple();
        checkCollisions();

        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                //Esquerda / Left
                case KeyEvent.VK_A:
                    if (direction != 'D') {
                        direction = 'A';
                    }
                    break;
                //Direita / Right
                case KeyEvent.VK_D:
                    if (direction != 'A') {
                        direction = 'D';
                    }
                    break;
                //Para baixo / Down
                case KeyEvent.VK_S:
                    if (direction != 'W') {
                        direction = 'S';
                    }
                    break;
                //Para cima / Up
                case KeyEvent.VK_W:
                    if (direction != 'S') {
                        direction = 'W';
                    }
                    break;

            }

        }
    }
}
