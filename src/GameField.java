import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {

    private final int DOT_SIZE = 16; // Размер картинок
    private final int SIZE = 20; //Размер игрового поля в еденицах
    private final int PIXEL_SIZE = DOT_SIZE * SIZE; //размер игрового поля в пикселях
    private final int ALL_DOTS = SIZE * SIZE;
    private final int APPLE_MAX_NUMBER = 10; //максимальное количество яблок
    private final int BACKSLASH_TOP = 16;
    private final int BACKSLASH_LINE = 2;
    private final int START_LENGHT_SNAKE = 3;//начальная длина змеи

    private DirectionsMove directionsMove;
    Image dot;
    String dotFile = "dot.png";
    Image headUp;
    String headUpFile = "head.png";
    Image headLeft;
    String headLeftFile = "headleft.png";
    Image headDown;
    String headDownFile = "headdown.png";
    Image headRight;
    String headRightFile = "headright.png";

    Image apple;
    String appleFile = "apple.png";

    private ArrayList<ChainSnake> chainSnakesList;
    private ArrayList<Apple> applesList;
    private Timer timer;

    private boolean inGame = true;
    private boolean snakeGrow;
    private int cornerCount; //подсчет поворотов. не может быть больше 50. Обнуляется при съедании яблока. Нужен для подсчета очков.
    private int cornerCountAll; //подсчет всех поворотов
    private int score; //счет
    private int mileage;//пробег змеи :-)))


    class FiledKEyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (inGame) {
                super.keyPressed(e);
                int keyPressed = e.getKeyCode();
                if ((keyPressed == KeyEvent.VK_LEFT)) {
                    directionsMove = directionsMove.counterclockwise();
                    cornerCountIncriment();
                }
                if ((keyPressed == KeyEvent.VK_RIGHT)) {
                    directionsMove = directionsMove.clockwise();
                    cornerCountIncriment();
                }
            }
        }
    }

    private void cornerCountIncriment() {
        cornerCountAll++;
        if (cornerCount < 50) {
            cornerCount++;
        }
    }

    public GameField() {
        setBackground(Color.BLACK);
        loadImages();
        directionsMove = DirectionsMove.right;
        initGAme();
        addKeyListener(new FiledKEyListener());
        setFocusable(true);
    }

    public void loadImages() {
        String fileName;
        fileName = "img/" + appleFile.toLowerCase();
        ImageIcon tempImageIconApple = new ImageIcon(getClass().getResource(fileName));
        apple = tempImageIconApple.getImage();
        fileName = "img/" + dotFile.toLowerCase();
        ImageIcon tempDotIconImage = new ImageIcon(getClass().getResource(fileName));
        dot = tempDotIconImage.getImage();
        fileName = "img/" + headUpFile.toLowerCase();
        ImageIcon tempHeadIconImage = new ImageIcon(getClass().getResource(fileName));
        headUp = tempHeadIconImage.getImage();
        fileName = "img/" + headDownFile.toLowerCase();
        tempHeadIconImage = new ImageIcon(getClass().getResource(fileName));
        headDown = tempHeadIconImage.getImage();
        fileName = "img/" + headLeftFile.toLowerCase();
        tempHeadIconImage = new ImageIcon(getClass().getResource(fileName));
        headLeft = tempHeadIconImage.getImage();
        fileName = "img/" + headRightFile.toLowerCase();
        tempHeadIconImage = new ImageIcon(getClass().getResource(fileName));
        headRight = tempHeadIconImage.getImage();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        scoreCalculate();
        int numberInfoLines = 4;

        String str1 = "Length: " + chainSnakesList.size();
        String str2 = "Apples: " + applesList.size();
        // String str3 = "Points: " + score;
        String str4 = "Turns: " + cornerCountAll;
        String str5 = "Miles: " + mileage;
        g.setColor(Color.WHITE);
        g.drawString(str1, 1, BACKSLASH_TOP);
        g.drawString(str2, PIXEL_SIZE / numberInfoLines, BACKSLASH_TOP);
        //   g.drawString(str3, 2 * PIXEL_SIZE / 5-BACKSLASH_TOP/2, BACKSLASH_TOP);
        g.drawString(str4, 2 * PIXEL_SIZE / numberInfoLines, BACKSLASH_TOP);
        g.drawString(str5, 3 * PIXEL_SIZE / numberInfoLines, BACKSLASH_TOP);

        if (inGame) {
            mileage++;
            //Линии ограничивающие игровое поле
            int beetwenLines = 2;
            int x1 = 0;
            int y1 = BACKSLASH_TOP + BACKSLASH_LINE;
            int x2 = PIXEL_SIZE + BACKSLASH_TOP + 3 * BACKSLASH_LINE;
            int y2 = y1;
            int x3 = x2;
            int y3 = PIXEL_SIZE + 2 * BACKSLASH_TOP + 2 * BACKSLASH_LINE + 2;
            int x4 = x1;
            int y4 = y3;


            g.drawLine(x1, y1, x2, y2);
            g.drawLine(x1 + beetwenLines, y1 + beetwenLines, x2 - beetwenLines, y2 + beetwenLines);

            g.drawLine(x2, y2, x3, y3);
            g.drawLine(x2 - beetwenLines, y2 + beetwenLines, x3 - beetwenLines, y3 - beetwenLines);

            g.drawLine(x3, y3, x4, y4);
            g.drawLine(x3 - beetwenLines, y3 - beetwenLines, x4 + beetwenLines, y4 - beetwenLines);

            g.drawLine(x1, y1, x4, y4);
            g.drawLine(x1 + beetwenLines, y1 + beetwenLines, x4 + beetwenLines, y4 - beetwenLines);

            //рисуем яблоки
            for (Apple appleCoord : applesList) {
                g.drawImage(apple, appleCoord.getX() * DOT_SIZE + 2 * BACKSLASH_LINE,
                        appleCoord.getY() * DOT_SIZE + BACKSLASH_TOP + 3 * BACKSLASH_LINE, this);
            }
            //рисуем тело змеи
            for (int i = 0; i < chainSnakesList.size() - 1; i++) {
                ChainSnake chainSnakeCoord = chainSnakesList.get(i);
                g.drawImage(dot, chainSnakeCoord.getX() * DOT_SIZE + 2 * BACKSLASH_LINE,
                        chainSnakeCoord.getY() * DOT_SIZE + BACKSLASH_TOP + 3 * BACKSLASH_LINE, this);
            }

             //рисуем голову
            Image headImage;
            switch (directionsMove) {
                case right:
                    headImage = headRight;
                    break;
                case left:
                    headImage = headLeft;
                    break;
                case down:
                    headImage = headDown;
                    break;
                case up:
                    headImage = headUp;
                    break;
                default:
                    headImage = headUp;
            }

            g.drawImage(headImage, chainSnakesList.get(chainSnakesList.size() - 1).getX() * DOT_SIZE + 2 * BACKSLASH_LINE,
                    chainSnakesList.get(chainSnakesList.size() - 1).getY() * DOT_SIZE + BACKSLASH_TOP + 3 * BACKSLASH_LINE, this);
        } else {
            String str = "Game over";
            g.setColor(Color.WHITE);
            g.drawString(str, PIXEL_SIZE / 2, PIXEL_SIZE / 2);
        }
    }

    private void scoreCalculate() {
        score = cornerCount + (chainSnakesList.size() - START_LENGHT_SNAKE) * 100 + mileage / 100;
    }


    public void initGAme() {
        chainSnakesList = new ArrayList<>();
        applesList = new ArrayList<>();
        snakeGrow = false;
        cornerCount = 0;
        cornerCountAll = 0;
        score = 0;
        for (int i = 0; i < START_LENGHT_SNAKE; i++) {
            chainSnakesList.add(new ChainSnake(SIZE / 2 - i, SIZE / 2));
        }
        timer = new Timer(250, this);
        timer.start();
        createApple();

    }

    private void createApple() {
        if (applesList.size() < APPLE_MAX_NUMBER) {
            int randomForApplesNumber = new Random().nextInt(100);
            int numberNewApples;
            if (randomForApplesNumber > 85) {
                numberNewApples = 3;
            } else if (randomForApplesNumber > 50) {
                numberNewApples = 2;
            } else if (randomForApplesNumber > 10) {
                numberNewApples = 1;
            } else if (applesList.size() > 0) numberNewApples = 0;
            else numberNewApples = 1;
            if ((applesList.size() + chainSnakesList.size() + numberNewApples) > ALL_DOTS) numberNewApples = 0;
            for (int i = 0; i < numberNewApples; i++) {
                int appleX, appleY;
                Apple apple;
                do {
                    appleX = new Random().nextInt(SIZE);
                    appleY = new Random().nextInt(SIZE);
                    apple = new Apple(appleX, appleY);
                }
                while (chainSnakesList.contains(new ChainSnake(appleX, appleY)) ||
                        applesList.contains(new Apple(appleX, appleY)));
                applesList.add(apple);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            moveSnake();
            checkApple();
            checkCollisions();
        }
        repaint();
    }


    private void checkCollisions() {
        /*1. проходим по всей змее- нет ли пересечения с головой*/
        for (int i = 0; i < chainSnakesList.size() - 2; i++) {
            if ((i > 4) &&
                    chainSnakesList.get(chainSnakesList.size() - 1).getX() ==
                            chainSnakesList.get(i).getX() &&
                    chainSnakesList.get(chainSnakesList.size() - 1).getY() ==
                            chainSnakesList.get(i).getY()) {
                inGame = false;
            }
            if (chainSnakesList.get(chainSnakesList.size() - 1).getX() > SIZE) {
                inGame = false;
            }
            if (chainSnakesList.get(chainSnakesList.size() - 1).getX() < 0) {
                inGame = false;
            }
            if (chainSnakesList.get(chainSnakesList.size() - 1).getY() > SIZE) {
                inGame = false;
            }
            if (chainSnakesList.get(chainSnakesList.size() - 1).getY() < 0) {
                inGame = false;
            }
        }
    }

    private void checkApple() {
        ArrayList<Apple> appleArrayListClone = applesList;
        for (int i = 0; i < appleArrayListClone.size(); i++) {
            if ((appleArrayListClone.get(i).getX() == chainSnakesList.get(chainSnakesList.size() - 1).getX()) &&
                    (appleArrayListClone.get(i).getY() == chainSnakesList.get(chainSnakesList.size() - 1).getY())) {
                applesList.remove(i);
                createApple();
                snakeGrow = true;
                cornerCount = 0;
                return;
            }
            snakeGrow = false;
        }

    }

    private void moveSnake() {
        int nextX = chainSnakesList.get(chainSnakesList.size() - 1).getX();
        int nextY = chainSnakesList.get(chainSnakesList.size() - 1).getY();

        switch (directionsMove) {
            case up: {
                nextY -= 1;
                break;

            }
            case down: {
                nextY += 1;
                break;
            }
            case left: {
                nextX -= 1;
                break;
            }
            case right: {
                nextX += 1;
                break;
            }
        }
        chainSnakesList.add(new ChainSnake(nextX, nextY));
        if (!snakeGrow) chainSnakesList.remove(0);
    }
}
