
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TicTacToe extends JFrame {

    private static final String[] PLAYER_SYMBOLS = {"X", "O", " "};
    private static final String[][] GAME_BUTTONS = {
            new String[]{"A3", "B3", "C3"},
            new String[]{"A2", "B2", "C2"},
            new String[]{"A1", "B1", "C1"}
    };

    private final Player[] players = {new HumanPlayer(PLAYER_SYMBOLS[0]), new HumanPlayer(PLAYER_SYMBOLS[1])};

    private final JLabel gameStatusLabel = new JLabel("Game is not started");
    private final JButton startAndResetButton = new JButton("Start");
    private final JButton player1Button = new JButton("Human");
    private final JButton player2Button = new JButton("Human");
    private final JButton[][] gameBoard = new JButton[3][3];
    private final JMenu gameMenu = new JMenu("Game");
    private final JMenuItem humanHumanMenuItem = new JMenuItem("Human vs Human");
    private final JMenuItem humanRobotMenuItem = new JMenuItem("Human vs Robot");
    private final JMenuItem robotHumanMenuItem = new JMenuItem("Robot vs Human");
    private final JMenuItem robotRobotMenuItem = new JMenuItem("Robot vs Robot");
    private final JMenuItem exitMenuItem = new JMenuItem("Exit");

    private static final Font FONT_ARIAL_12 = new Font("Arial", Font.PLAIN, 12);
    private static final Font FONT_ARIAL_BOLD_50 = new Font("Arial", Font.BOLD, 50);

    private int currentPlayer = 1;
    private boolean gameOver = false;
    
    public TicTacToe() {
        initializeGame();
    }

    private void initializeGame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        setLayout(new BorderLayout());
        setSize(350, 395);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        startAndResetButton.addActionListener(e -> startResetGame());
        startAndResetButton.setFont(FONT_ARIAL_12);
        startAndResetButton.setName("ButtonStartReset");

        player1Button.addActionListener(e -> togglePlayerType(player1Button, 0));
        player1Button.setName("ButtonPlayer1");
        player1Button.setFont(FONT_ARIAL_12);

        player2Button.addActionListener(e -> togglePlayerType(player2Button, 1));
        player2Button.setName("ButtonPlayer2");
        player2Button.setFont(FONT_ARIAL_12);

        gameStatusLabel.setFont(FONT_ARIAL_12);
        gameStatusLabel.setName("LabelStatus");

        humanHumanMenuItem.setName("MenuHumanHuman");
        humanHumanMenuItem.setMnemonic(KeyEvent.VK_H);
        humanHumanMenuItem.addActionListener(e -> startQuickGame("Robot", "Robot"));

        humanRobotMenuItem.setName("MenuHumanRobot");
        humanRobotMenuItem.setMnemonic(KeyEvent.VK_R);
        humanRobotMenuItem.addActionListener(e -> startQuickGame("Robot", "Human"));

        robotHumanMenuItem.setName("MenuRobotHuman");
        robotHumanMenuItem.setMnemonic(KeyEvent.VK_U);
        robotHumanMenuItem.addActionListener(e -> startQuickGame("Human", "Robot"));

        robotRobotMenuItem.setName("MenuRobotRobot");
        robotRobotMenuItem.setMnemonic(KeyEvent.VK_O);
        robotRobotMenuItem.addActionListener(e -> startQuickGame("Human", "Human"));

        exitMenuItem.setName("MenuExit");
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.addActionListener(e -> System.exit(0));

        gameMenu.setName("MenuGame");
        gameMenu.add(humanHumanMenuItem);
        gameMenu.add(humanRobotMenuItem);
        gameMenu.add(robotHumanMenuItem);
        gameMenu.add(robotRobotMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(exitMenuItem);

        Panel statusPanel = new Panel();
        statusPanel.setSize(300, 30);
        statusPanel.setPreferredSize(new Dimension(300, 30));
        statusPanel.setLayout(new GridLayout(1, 1));
        statusPanel.add(gameStatusLabel);

        Panel gameControlPanel = new Panel();
        gameControlPanel.setSize(300, 35);
        gameControlPanel.setPreferredSize(new Dimension(300, 40));
        gameControlPanel.setLayout(new GridLayout(1, 3));
        gameControlPanel.add(player1Button);
        gameControlPanel.add(startAndResetButton);
        gameControlPanel.add(player2Button);

        Panel mainPanel = new Panel();
        mainPanel.setPreferredSize(new Dimension(300, 270));
        mainPanel.setLayout(new GridLayout(3, 3));
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                JButton button = new JButton(" ");
                gameBoard[y][x] = button;
                button.setName("Button" + GAME_BUTTONS[y][x]);
                button.setFont(FONT_ARIAL_BOLD_50);
                button.setEnabled(false);
                button.addActionListener(e -> endTurn((JButton) e.getSource()));
                mainPanel.add(button);
                button.setFocusPainted(false);
            }
        }

        menuBar.add(gameMenu);
        getContentPane().add(gameControlPanel, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void startQuickGame(String player1Type, String player2Type) {
        player1Button.setText(player1Type);
        togglePlayerType(player1Button, 0);
        player2Button.setText(player2Type);
        togglePlayerType(player2Button, 1);
        startAndResetButton.setText("Start");
        startResetGame();
    }

    private void nextPlayer() {
        currentPlayer = (currentPlayer + 1) % 2;
        gameStatusLabel.setText(String.format("The turn of %s Player (%s)", (players[currentPlayer] instanceof HumanPlayer ? "Human" : "Robot"), players[currentPlayer].getIcon()));
        players[currentPlayer].makeMove(this);
    }

    private void endTurn(JButton button) {
        if (!gameOver && button.getText().equals(" ")) {
            button.setText(players[currentPlayer].getIcon());
            gameOver = checkForGameOver();
            if (!gameOver) {
                nextPlayer();
            }
        }
    }

    private void togglePlayerType(JButton playerTypeButton, int playerNumber) {
        playerTypeButton.setText(playerTypeButton.getText().equals("Human") ? "Robot" : "Human");
        if (playerTypeButton.getText().equals("Human")) {
            players[playerNumber] = new HumanPlayer(PLAYER_SYMBOLS[playerNumber]);
        } else {
            players[playerNumber] = new ComputerPlayer(PLAYER_SYMBOLS[playerNumber]);
        }
    }

    private void toggleGameBoardButtons(boolean enabled, boolean clearValues) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (clearValues) {
                    gameBoard[y][x].setText(" ");
                }
                gameBoard[y][x].setEnabled(enabled);
            }
        }
    }

    private void startResetGame() {
        toggleGameBoardButtons(startAndResetButton.getText().equals("Start"), true);
        if (startAndResetButton.getText().equals("Start")) {
            startAndResetButton.setText("Reset");
            player1Button.setEnabled(false);
            player2Button.setEnabled(false);
            gameOver = false;
            currentPlayer = 1;
            nextPlayer();
        } else {
            startAndResetButton.setText("Start");
            player1Button.setEnabled(true);
            player2Button.setEnabled(true);
            gameStatusLabel.setText("Game is not started");
            gameOver = false;
            currentPlayer = 1;
        }
    }


    private boolean checkForGameOver() {
        int occupiedCellCount = 0;
        int[] wins = {7, 56, 448, 73, 146, 292, 273, 84};
        int sumX = 0;
        int sumO = 0;
        int index = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (!gameBoard[y][x].getText().equals(" ")) {
                    occupiedCellCount++;
                    if (gameBoard[y][x].getText().equals("X")) {
                        sumX += (int) Math.pow(2, index);
                    } else {
                        sumO += (int) Math.pow(2, index);
                    }
                }
                index++;
            }
        }
        for (int win : wins) {
            if ((win & sumX) == win) {
                gameStatusLabel.setText(String.format("The %s Player (X) wins", (players[0] instanceof HumanPlayer ? "Human" : "Robot")));
                toggleGameBoardButtons(false, false);
                return true;
            }
            if ((win & sumO) == win) {
                gameStatusLabel.setText(String.format("The %s Player (O) wins", (players[1] instanceof HumanPlayer ? "Human" : "Robot")));
                toggleGameBoardButtons(false, false);
                return true;
            }
        }
        if (occupiedCellCount == 9) {
            gameStatusLabel.setText("Draw");
            toggleGameBoardButtons(false, false);
            return true;
        }
        return false;
    }

    interface Player {
        void makeMove(TicTacToe game);

        String getIcon();
    }

    static abstract class AbstractPlayer implements Player {
        private final String icon;

        AbstractPlayer(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return icon;
        }
    }

    static class HumanPlayer extends AbstractPlayer {
        HumanPlayer(String icon) {
            super(icon);
        }

        @Override
        public void makeMove(TicTacToe game) {

        }
    }

    static class ComputerPlayer extends AbstractPlayer {
        ComputerPlayer(String icon) {
            super(icon);
        }

        @Override
        public void makeMove(TicTacToe game) {
            Random rng = new Random();
            int row;
            int col;

            do {
                row = rng.nextInt(3);
                col = rng.nextInt(3);
            } while (!game.gameBoard[row][col].getText().equals(" "));
            game.endTurn(game.gameBoard[row][col]);
        }
    }
}