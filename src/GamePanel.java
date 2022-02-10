import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    //defines how big we want the items to be in the screen
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 125;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    //game starts with snake of size 6
    int bodyParts = 6;
    int applesEaten = 0;
    //y coordinate of apple
    int appleX;
    //x coordinate of apple
    int appleY;
    //snake initially begins to move right
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        if (running) {
            //for code block to draw horizontal and vertical grid lines
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            //draws apple and sets its color to Red
            graphics.setColor(Color.red);
            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //draws head and body of snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(Color.green);
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    graphics.setColor(new Color(45, 180, 0));
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            graphics.setColor(Color.white);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + applesEaten,
                    (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    graphics.getFont().getSize());

        } else {
            gameOver(graphics);
        }

    }

    public void newApple() {
        //x & y coordinates to place apples within the game frame.
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        //for loop to iterate through all the body parts of snake
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }


    public void eatApple() {
        //if snake head touches apple
        if (x[0] == appleX && y[0] == appleY) {
            //increment snake size
            bodyParts++;
            //increment apples eaten to denote score
            applesEaten++;
            //generate new random apple
            newApple();
        }

    }

    public void checkCollisions() {
        //checks if snake head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        //checks if snake head touches Left Frame border
        if (x[0] < 0) {
            running = false;
        }
        //checks if snake head touches Right Frame border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //checks if snake head touches Top Frame border
        if (y[0] < 0) {
            running = false;
        }
        //checks if snake head touches Bottom Frame border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (running == false) {
            timer.stop();
        }

    }

    public void gameOver(Graphics graphics) {
        //set up game over text
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over!")) / 2,
                SCREEN_HEIGHT / 2);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 35));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Total Score: " + applesEaten,
                (SCREEN_WIDTH - metrics.stringWidth("Total Score: " + applesEaten)) / 2,
                (SCREEN_HEIGHT / 2) + 100);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            eatApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R')
                        direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L')
                        direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D')
                        direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U')
                        direction = 'D';
                    break;
            }

        }
    }
}
