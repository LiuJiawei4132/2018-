package D2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Window extends JFrame {
    private static int score = 0; // 分数
    final Font[] fonts = {new Font("", Font.BOLD, 48)
            , new Font("", Font.BOLD, 42)
            , new Font("", Font.BOLD, 36)
            , new Font("", Font.BOLD, 30)
            , new Font("", Font.BOLD, 24)
    };

    private GameBoard gameBoard;
    private JLabel ltitle;
    private JLabel lsctip;
    private JLabel lgatip;
    private JLabel lscore;

    public Window() {
        this.setLayout(null);
    }

    public void initView() {
        ltitle = new JLabel("2048", JLabel.CENTER);
        ltitle.setFont(new Font("", Font.BOLD, 50));
        ltitle.setForeground(new Color(0x776e65));
        ltitle.setBounds(0, 0, 120, 60);

        lsctip = new JLabel("SCORE", JLabel.CENTER);
        lsctip.setFont(new Font("", Font.BOLD, 18));
        lsctip.setForeground(new Color(0xeee4da));
        lsctip.setOpaque(true);
        lsctip.setBackground(new Color(0xbbada0));
        lsctip.setBounds(290, 5, 100, 25);

        lgatip = new JLabel("按方向键可以控制方块的移动，按ESC键可以重新开始游戏.", JLabel.CENTER);
        lgatip.setFont(new Font("", Font.ITALIC, 13));
        lgatip.setForeground(new Color(0x776e65));
        lgatip.setBounds(10, 75, 340, 15);

        lscore = new JLabel("0", JLabel.CENTER);
        lscore.setFont(new Font("", Font.BOLD, 25));
        lscore.setForeground(Color.WHITE);
        lscore.setOpaque(true);
        lscore.setBackground(new Color(0xbbada0));
        lscore.setBounds(290, 30, 100, 25);

        gameBoard = new GameBoard();
        gameBoard.setPreferredSize(new Dimension(400, 400));
        gameBoard.setBackground(new Color(0xbbada0));
        gameBoard.setBounds(0, 100, 400, 400);
        gameBoard.setFocusable(true);

        this.add(ltitle);
        this.add(lsctip);
        this.add(lgatip);
        this.add(lscore);
        this.add(gameBoard);
    }

    class GameBoard extends JPanel implements KeyListener {
        // 主游戏界面配置，
        private static final int GAP_TILE = 16; //瓦片之间间隙
        private static final int ARC_TILE = 16; //瓦片圆角弧度
        private static final int SIZE_TILE = 80;//瓦片的大小

        private Tile[][] tiles = new Tile[4][4];
        private boolean isEnd;
        private boolean isMove;


        public GameBoard() {
            initGame();
            addKeyListener(this);
        }

        public void initGame() {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    tiles[i][j] = new Tile();
                }
            }

            score = 0;
            createTile();
            createTile();

            lscore.setText("0");
            isMove = false;
            isEnd = false;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    initGame();
                    break;
                case KeyEvent.VK_UP:
                    moveUp();
                    movedCreateTile();
                    isGameOver();
                    break;
                case KeyEvent.VK_DOWN:
                    moveDown();
                    movedCreateTile();
                    isGameOver();
                    break;
                case KeyEvent.VK_LEFT:
                    moveLeft();
                    movedCreateTile();
                    isGameOver();
                    break;
                case KeyEvent.VK_RIGHT:
                    moveRight();
                    movedCreateTile();
                    isGameOver();
                    break;
            }
            repaint();
        }

        public boolean moveUp() {
            for (int i = 0; i < 4; i++) {
                for (int j = 1; j < 4; j++) {
                    // 判断当前瓦片数值不能为空，前一个瓦片不能是合并瓦片，不能是瓦片的边界
                    for (int k = j; k > 0 && tiles[k][i].value != 0 && !tiles[k-1][i].isMerge; k--) {
                        if (tiles[k-1][i].value == 0) {
                            doMove(tiles[k-1][i], tiles[k][i]);
                        } else if (tiles[k-1][i].value == tiles[k][i].value) {
                            doMerge(tiles[k-1][i], tiles[k][i]);
                            break; // 这里不能用continue;
//                            continue;
                        } else {
                            break;
                        }
                    }
                }
            }
            return isMove;
        }

        public boolean moveDown() {
            for (int i = 0; i < 4; i++) {
                for (int j = 2; j >= 0; j--) {
                    for (int k = j; k < 3 && tiles[k][i].value != 0 && !tiles[k+1][i].isMerge; k++) {
                        if (tiles[k+1][i].value == 0) {
                            doMove(tiles[k+1][i], tiles[k][i]);
                        } else if (tiles[k+1][i].value == tiles[k][i].value) {
                            doMerge(tiles[k+1][i], tiles[k][i]);
                            break; // 这里不能用continue;
//                            continue;
                        } else {
                            break;
                        }
                    }
                }
            }
            return isMove;
        }
        
        public boolean moveLeft() {
            for (int i = 0; i < 4; i++) {
                for (int j = 1; j < 4; j++) {
                    for (int k = j; k > 0 && tiles[i][k].value != 0 && !tiles[i][k-1].isMerge; k--) {
                        if (tiles[i][k-1].value == 0) {
                            doMove(tiles[i][k-1], tiles[i][k]);
                        } else if (tiles[i][k-1].value == tiles[i][k].value) {
                            doMerge(tiles[i][k-1], tiles[i][k]);
                            break; // 这里不能用continue;
//                            continue;
                        } else {
                            break;
                        }
                    }
                }
            }
            return isMove;
        }

        public boolean moveRight() {
            for (int i = 0; i < 4; i++) {
                for (int j = 2; j > -1; j--) {
                    for (int k = j; k < 3 && tiles[i][k].value != 0 && !tiles[i][k+1].isMerge; k++) {
                        if (tiles[i][k+1].value == 0) {
                            doMove(tiles[i][k+1], tiles[i][k]);
                        } else if (tiles[i][k+1].value == tiles[i][k].value) {
                            doMerge(tiles[i][k+1], tiles[i][k]);
                            break; // 这里不能用continue;
//                            continue;
                        } else {
                            break;
                        }
                    }
                }
            }
            return isMove;
        }

        public void doMove(Tile fir, Tile sec) {
//            fir.value = sec.value;
            fir.swap(sec);
            sec.clearData();
            isMove = true;
        }

        public void doMerge(Tile fir, Tile sec) {
            fir.value = fir.value << 1;
            fir.isMerge = true;
            sec.clearData();
            score += fir.value;
            isMove = true;
        }

        public void movedCreateTile() {
            if (isMove) {
                createTile();
                isMove = false;
            }
        }

        public void isGameOver() {
            lscore.setText("" + score);

            if (!getBlankTile().isEmpty()) {
                return;
            }

            for (int i = 0; i < 4; i++) {
                for (int j = 3; j > 0; j--) {
                    if (tiles[j-1][i].value == tiles[j][i].value || tiles[i][j-1].value == tiles[i][j].value)
                        return;
                }
            }
            isEnd = true;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    drawTile(g, i, j);
                }
            }
            if (isEnd) {
                g.setColor(new Color(255,255,255,180));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(new Color(0x3d79ca));
                g.setFont(fonts[0]);
                FontMetrics fms = getFontMetrics(fonts[0]);
                String value = "Game over";
                String score = "SCORE:" + Window.score;
                g.drawString(value, (getWidth() - fms.stringWidth(value)) / 2 , getHeight() / 3);
                g.drawString(score + "", (getWidth() - fms.stringWidth(score)) / 2, getHeight() / 2 + getHeight() / 4);
            }
        }

        public void drawTile(Graphics gg, int i, int j) {
            Graphics2D g = (Graphics2D)gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_NORMALIZE);
            Tile tile = tiles[i][j];
            // 瓦片背景色
            g.setColor(tile.getBackground());
            g.fillRoundRect(GAP_TILE + (GAP_TILE + SIZE_TILE) * j
                        , GAP_TILE + (GAP_TILE + SIZE_TILE) * i
                        , SIZE_TILE, SIZE_TILE, ARC_TILE, ARC_TILE);
            // 字体颜色
            g.setColor(tile.getForeground());
            Font font = tile.getFont();
            g.setFont(font);
            FontMetrics fms = getFontMetrics(font);
            String value = String.valueOf(tile.value);
            g.drawString(value, GAP_TILE + (GAP_TILE + SIZE_TILE) * j
                    + (SIZE_TILE - fms.stringWidth(value)) / 2
                    ,GAP_TILE + (GAP_TILE + SIZE_TILE) * i
                    + (SIZE_TILE - fms.getAscent() - fms.getDescent()) / 2
                    + fms.getAscent());
            tiles[i][j].isMerge = false;
        }

        public void createTile() {
            List<Tile> tileList = getBlankTile();
            if (!tileList.isEmpty()) {
                Random random = new Random();
                int index = random.nextInt(tileList.size());
                tileList.get(index).value = random.nextInt(100) > 50 ? 2 : 4;
            }
        }

        public List<Tile> getBlankTile() {
            List<Tile> list = new ArrayList<Tile>();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (tiles[i][j].value == 0)
                        list.add(tiles[i][j]);
                }
            }
            return list;
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    class Tile {
        public int value;
        public boolean isMerge;

        public Tile() {
            clearData();
        }

        public void clearData() {
            this.value = 0;
            this.isMerge = false;
        }

        public Font getFont() {
            int index = value < 100 ? 1 : value < 1000 ? 2 : value < 10000 ? 3 : 4;
            return fonts[index];
        }

        public void swap(Tile tile) {
            this.value = tile.value;
            this.isMerge = tile.isMerge;
        }

        public Color getForeground() {  // 设置字体
            switch (value) {
                case 0:
                    return new Color(0xcdc1b4); // 颜色与瓦块背景色相同
                case 2:
                case 4:
                    return new Color(0x776e65);
                default:
                    return new Color(0xf9f6f2);
            }
        }

        public Color getBackground() {
            switch (value) {
                case 0:
                    return new Color(0xcdc1b4);
                case 2:
                    return new Color(0xeee4da);
                case 4:
                    return new Color(0xede0c8);
                case 8:
                    return new Color(0xf2b179);
                case 16:
                    return new Color(0xf59563);
                case 32:
                    return new Color(0xf67c5f);
                case 64:
                    return new Color(0xf65e3b);
                case 128:
                    return new Color(0xedcf72);
                case 256:
                    return new Color(0xedcc61);
                case 512:
                    return new Color(0xedc850);
                case 1024:
                    return new Color(0xedc53f);
                case 2048:
                    return new Color(0xedc22e);
                case 4096:
                    return new Color(0x65da92);
                case 8192:
                    return new Color(0x5abc65);
                case 16384:
                    return new Color(0x248c51);
                default:
                    return new Color(0x248c51);
            }
        }
    }
}

