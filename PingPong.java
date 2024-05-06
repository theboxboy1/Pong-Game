



//Ping Pong Game 
// Zakariya Yahmad
// May 5, 2024




import java.awt.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;

public class Pong extends JFrame implements KeyListener, ActionListener {
    private Paddle paddleOne;
    private Paddle paddleTwo;
    private PongPanel panel;
    private int playerOneScore;
    private int playerTwoScore;
    private Timer timer;
    private boolean[] keysPressed;
    private boolean playing;
    private boolean twoPlayerMode;
    private Ball ball;

    private int windowHeight;
    private int windowWidth;

    private int gameDifficulty;

    private final static int BALL_START_X = 255;
    private final static int BALL_START_Y = 190;

    private final static int PADDLE_ONE_X = 30;
    private final static int PADDLE_TWO_X = 470;

    private final static int UP_KEY = 38;
    private final static int DOWN_KEY = KeyEvent.VK_DOWN;

    private final static int W_KEY = 87;
    private final static int S_KEY = 83;

    private final static int BORDER_SIZE = 10;

    public Pong() {
        super("Pong Game");
        setSize(1000, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       
        panel = new PongPanel();
        Container container = getContentPane();
        container.add(panel);

        keysPressed = new boolean[256];
        paddleOne = null;
        paddleTwo = null;

        playing = false;
        gameDifficulty = 1;
        twoPlayerMode = true;
        playerOneScore = 0;
        playerTwoScore = 0;
        


        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(createGameMenu());
        menuBar.add(createSettingsMenu());

        requestFocus();
        addKeyListener(this);
        panel.repaint();

        timer = new Timer(30, this);
    }

    public static void main(String[] args) {
        Pong game = new Pong();
        game.setVisible(true);
    }

    public static void wait(int milliseconds) {
        try {
            Thread.currentThread().sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void keyPressed(KeyEvent e) {
        keysPressed[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        keysPressed[e.getKeyCode()] = false;
    }

    public void keyTyped(KeyEvent e) {}

    public void score(int player) {
        timer.stop();
        if (player == 1)
            playerTwoScore++;
        else if (player == 2)
            playerOneScore++;

        if (!checkWinner())
            timer.start();
        ball.reset();
        wait(100);
    }

    public boolean checkWinner() {
        if (playerOneScore > 5) {
            if (twoPlayerMode)
                setTitle("P1 WINS!");
            else
                setTitle("YOU LOSE");
            playing = false;
            timer.stop();
            return true;
        } else if (playerTwoScore > 5) {
            if (twoPlayerMode)
                setTitle("P2 WINS!");
            else
                setTitle("YOU WIN!");
            playing = false;
            timer.stop();
            return true;
        }
        return false;
    }

    public void actionPerformed(ActionEvent e) {
        ball.move();
        if (!twoPlayerMode)
            compStep++;
        panel.repaint();
    }

    public JMenu createGameMenu() {
        JMenu menu = new JMenu("Game");
        menu.add(newGame());
        menu.add(onePlayer());
        menu.add(twoPlayer());
        menu.add(exit());
        return menu;
    }

    public JMenu createSettingsMenu() {
        JMenu menu = new JMenu("Settings");
        menu.add(increaseDifficulty());
        menu.add(decreaseDifficulty());
        return menu;
    }

    public JMenuItem newGame() {
        JMenuItem item = new JMenuItem("New Game");
        class MenuItemListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                resetScores();
                startGame();
            }
        }
        ActionListener l = new MenuItemListener();
        item.addActionListener(l);
        return item;
    }

    public JMenuItem increaseDifficulty() {
        JMenuItem item = new JMenuItem("Increase Difficulty");
        class MenuItemListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (!twoPlayerMode && gameDifficulty < 6)
                    gameDifficulty++;
            }
        }
        ActionListener l = new MenuItemListener();
        item.addActionListener(l);
        return item;
    }

    public JMenuItem decreaseDifficulty() {
        JMenuItem item = new JMenuItem("Decrease Difficulty");
        class MenuItemListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (!twoPlayerMode && gameDifficulty > 1)
                    gameDifficulty--;
            }
        }
        ActionListener l = new MenuItemListener();
        item.addActionListener(l);
        return item;
    }

    public JMenuItem onePlayer() {
        JMenuItem item = new JMenuItem("One Player");
        class MenuItemListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                twoPlayerMode = false;
            }
        }
        ActionListener l = new MenuItemListener();
        item.addActionListener(l);
        return item;
    }

    public JMenuItem twoPlayer() {
        JMenuItem item = new JMenuItem("Two Player");
        class MenuItemListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                twoPlayerMode = true;
            }
        }
        ActionListener l = new MenuItemListener();
        item.addActionListener(l);
        return item;
    }

    public JMenuItem exit() {
        JMenuItem item = new JMenuItem("Exit");
        class MenuItemListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }
        ActionListener l = new MenuItemListener();
        item.addActionListener(l);
        return item;
    }

    private int compStep;

    public void compMove(Paddle p, int difficulty) {
        if (difficulty == 6)
            p.moveTo(ball.getY());
        else if (compStep % (6 - difficulty) == 0) {
            if (ball.getY() < p.getY())
                p.moveUp();
            else if (ball.getY() > p.getY() + p.getLength())
                p.moveDown();
            compStep = 0;
        }
    }

    public void startGame() {
        playing = true;
        playerOneScore = 0;
        playerTwoScore = 0;
        paddleOne = new Paddle(40, 330, Color.BLUE);
        paddleTwo = new Paddle(40, 330, Color.BLUE);
        ball = new Ball(5, new Location(BALL_START_X, BALL_START_Y), 470, 330, Color.RED);
        ball.reset();
        timer.start();
    }

    public void resetScores() {
        playerOneScore = 0;
        playerTwoScore = 0;
    }

    public class PongPanel extends JPanel {
        public PongPanel() {
            setBackground(Color.WHITE);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.DARK_GRAY);
            g.fillRect(20, 40, 460, 10);
            g.fillRect(20, 40, 10, 300);
            g.fillRect(480, 40, 10, 300);
            g.fillRect(20, 330, 460, 10);
            Font hugeFont = new Font("Comic Sans", Font.BOLD, 40);
            g.setFont(hugeFont);
            g.drawString(playerOneScore + "", 125, 40);
            g.drawString(playerTwoScore + "", 375, 40);
            if (!twoPlayerMode) {
                Font af = new Font("Comic Sans", Font.BOLD, 12);
                g.setFont(af);
                if (gameDifficulty < 6)
                    g.drawString("Computer Level" + gameDifficulty, 180, 40);
                else
                    g.drawString("Computer Level MAX", 180, 40);
            }
            if (playing) {
                if (keysPressed[UP_KEY])
                    paddleTwo.moveUp();
                if (keysPressed[DOWN_KEY])
                    paddleTwo.moveDown();
                if (twoPlayerMode) {
                    if (keysPressed[W_KEY])
                        paddleOne.moveUp();
                    if (keysPressed[S_KEY])
                        paddleOne.moveDown();
                } else {
                    Font af = new Font("Comic Sans", Font.BOLD, 12);
                    g.setFont(af);
                    if (gameDifficulty < 6)
                        g.drawString("Computer Level" + gameDifficulty, 180, 40);
                    else
                        g.drawString("Computer Level MAX", 180, 40);
                    compMove(paddleOne, gameDifficulty);
                }
                g.setColor(paddleOne.getColor());
                g.fillRect(PADDLE_ONE_X, paddleOne.getY(), paddleOne.WIDTH, paddleOne.getLength());

                g.setColor(paddleTwo.getColor());
                g.fillRect(PADDLE_TWO_X, paddleTwo.getY(), paddleTwo.WIDTH, paddleTwo.getLength());
                g.setColor(ball.getColor());
                g.fillRect(ball.getX(), ball.getY(), ball.getRadius() * 2, ball.getRadius() * 2);

            }
        }
    }

    public class Paddle {
        private Color color;
        private int yLocation;
        private int screenHeight;
        private int size;
        public static final int INCREMENT = 6;
        public static final int WIDTH = 10;

        public Paddle() {
            size = 0;
            screenHeight = 0;
            yLocation = 0;
            color = Color.BLUE;
        }

        public Paddle(int s, int screen, Color c) {
            size = s;
            screenHeight = screen;
            yLocation = BALL_START_Y - (s / 2);
            color = c;
        }

        public int getY() {
            return yLocation;
        }

        public void setColor(Color c) {
            color = c;
        }

        public Color getColor() {
            return color;
        }

        public int getLength() {
            return size;
        }

        public void moveUp() {
            if (validPaddleLocation(yLocation - INCREMENT))
                yLocation -= INCREMENT;
            else if (yLocation - INCREMENT < 0)
                yLocation = 0;
        }

        public void moveDown() {
            if (validPaddleLocation(yLocation + INCREMENT))
                yLocation += INCREMENT;
            else if (yLocation + size + INCREMENT > screenHeight)
                yLocation = screenHeight - size;
        }

        public void moveTo(int y) {
            if (validPaddleLocation(y))
                yLocation = y;
            else if (y + size > screenHeight)
                yLocation = screenHeight - size;
        }

        public boolean validPaddleLocation(int y) {
            return y >= 50 && y + size <= screenHeight;
        }
    }

    public class Ball {
        private Color color;
        private int radius;
        private Location location;
        private int screenWidth;
        private int screenHeight;
        private int xDirection;
        private int yDirection;
        private int xIncrement;
        private int yIncrement;
        public static final int RIGHT = 1;
        public static final int LEFT = -1;
        public static final int DOWN = 1;
        public static final int UP = -1;

        private boolean moving;

        public Ball() {
            radius = 0;
            color = Color.WHITE;
            location = new Location();
            screenWidth = 0;
            screenHeight = 0;
            xDirection = RIGHT;
            yDirection = UP;
            xIncrement = 0;
            yIncrement = 0;
            moving = false;
        }

        public Ball(int r, Location l, int sw, int sh, Color c) {
            radius = r;
            color = c;
            location = l;
            screenWidth = sw;
            screenHeight = sh;
            xIncrement = 2;
            yIncrement = 0;
            xDirection = RIGHT;
            yDirection = UP;
            moving = false;
        }

        public void setColor(Color c) {
            color = c;
        }

        public void setRadius(int r) {
            radius = r;
        }

        public void reset() {
            location.setX(BALL_START_X);
            location.setY(BALL_START_Y);
            xDirection *= -1;
            yDirection = 1;
            xIncrement = 4;
            yIncrement = 0;
        }

        public boolean moving() {
            return moving;
        }

        public int getRadius() {
            return radius;
        }

        public int getDiameter() {
            return radius * 2;
        }

        public boolean validBallLocation(Location l) {
            return l.getY() >= 50 && l.getY() + getDiameter() <= screenHeight && l.getX() >= 30 && l.getX() + getDiameter() <= screenWidth;
        }

        public Color getColor() {
            return color;
        }

        public int getX() {
            return location.getX();
        }

        public int getY() {
            return location.getY();
        }

        public boolean hitPaddle(Paddle p) {
            return getY() + getDiameter() >= p.getY() && getY() <= p.getY() + p.getLength();
        }

        public void reflectPaddle(Paddle p) {
            int difference = getY() - (p.getLength() / 2 + p.getY());
            // negative above, positive below
            if (Math.abs(difference) > 10) {
                if ((difference > 0 && yDirection == UP) || (difference < 0 && yDirection == DOWN)) {
                    yDirection *= -1;
                }
                if (yIncrement < 14)
                    yIncrement += 2;
            } else {
                if (xIncrement > 20)
                    xIncrement -= 2;
                if (yIncrement >= 2)
                    yIncrement -= 2;
            }

            if (xIncrement < 20)
                xIncrement += 2;
        }

        public void move() {
            boolean out = false;
            int newX = getX() + xDirection * xIncrement;
            int rightBorder = screenWidth - getDiameter();
            if (newX < 40 || newX >= rightBorder) {
                if (xDirection == RIGHT) {
                    if (hitPaddle(paddleTwo))
                        reflectPaddle(paddleTwo);
                    else {
                        score(1);
                        out = true;
                    }
                } else if (xDirection == LEFT) {
                    if (hitPaddle(paddleOne))
                        reflectPaddle(paddleOne);
                    else {
                        score(2);
                        out = true;
                    }
                }
                xDirection *= -1;
            }
            if (!out) {
                if (newX < 40)
                    newX = (40 - newX) % screenWidth;
                else if (rightBorder > 1 && newX >= rightBorder)
                    newX = rightBorder - ((newX - 30) % (rightBorder - 30));

                int newY = getY() + yDirection * yIncrement;
                int bottomBorder = screenHeight - getDiameter();
                if (newY < 50 || newY >= bottomBorder) {
                    yDirection *= -1;
                }
                if (newY < 50)
                    newY = (50 - newY) % screenHeight;
                else if (bottomBorder > 1 && newY >= bottomBorder)
                    newY = bottomBorder - ((newY - 50) % (bottomBorder - 50));

                if (validBallLocation(new Location(newX, newY))) {
                    location.setX(newX);
                    location.setY(newY);
                }
            }
        }
    }

    public class Location {
        private int x;
        private int y;

        public Location() {
            x = 0;
            y = 0;
        }

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public boolean equals(Object obj) {
            Location other = (Location) obj;
            return other.x == x && other.y == y;
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}

