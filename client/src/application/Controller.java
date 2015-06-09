package application;

import connection.HTTPWorker;
import connection.TCPConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    AnchorPane anchorPane;

    @FXML
    AnchorPane alphabetPane;

    @FXML
    AnchorPane gameFieldPane;

    @FXML
    Rectangle connectButton;

    @FXML
    Rectangle createGameButton;

    @FXML
    TextField ipTextField;

    @FXML
    Rectangle buttonMakeWord;

    @FXML
    Rectangle buttonSend;

    @FXML
    Rectangle buttonReset;

    @FXML
    Rectangle wordRectangle;

    @FXML
    Label resultWordLabel;

    @FXML
    TitledPane titledPane1, titledPane2;

    @FXML
    ListView listView1, listView2;

    @FXML
    Label statusLabel;

    private List<String> usedWords = new ArrayList<String>();
    private ObservableList<String> wordsList1 = FXCollections.observableArrayList();
    private ObservableList<String> wordsList2 = FXCollections.observableArrayList();
    private Rectangle[][] gameFieldRect = new Rectangle[5][5];
    private Text[][] textGameField = new Text[5][5];
    private boolean[][] letterUsed = new boolean[5][5];
    private int lastTakeX, lastTakeY;
    private int total1 = 0, total2 = 0;
    private final char[] alphabet = {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'Й',
                                     'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч',
                                     'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};
    private int chosenX = -1, chosenY = -1;
    private TCPConnector connector;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createButtonsOnForm();
        createGameField();
        setRectangleProperties(connectButton);
        setRectangleProperties(createGameButton);
        setRectangleProperties(buttonSend);
        setRectangleProperties(buttonReset);
        setRectangleProperties(buttonMakeWord);
        wordRectangle.setFill(Color.LIGHTBLUE);
    }

    @FXML
    private void createGameClick() {
        connector = new TCPConnector();
        initGameField(connector.getInitWord());
        statusLabel.setText("Ваш ход");
    }

    @FXML
    private void connectClick() {
        connector = new TCPConnector(ipTextField.getText());
        String startWord = connector.getInitWord();
        initGameField(startWord);
        usedWords.add(startWord);
        statusLabel.setText("Ход соперника");
        connector.waitMove(titledPane2);
    }

    private void createButtonsOnForm() {
        int currentX = 0;
        int currentY = -40;
        for (int i = 0; i < alphabet.length; i++) {
            if (i % 13 == 0) {
                currentX = 30;
                currentY += 40;
            }
            final Rectangle curRectangle = new Rectangle(40, 40);
            curRectangle.setX(currentX);
            curRectangle.setY(currentY);
            setRectangleProperties(curRectangle);

            final int finalI = i;
            EventHandler letterClick = new EventHandler() {
                @Override
                public void handle(Event event) {
                    if (chosenX != -1) {
                        textGameField[chosenX][chosenY].setText(String.valueOf(alphabet[finalI]));
                    }
                }
            };

            curRectangle.setOnMouseClicked(letterClick);
            Text text = new Text(String.valueOf(alphabet[i]));
            text.setX(currentX + 20);
            text.setY(currentY + 20);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Colibri", FontWeight.BOLD, 15));
            text.setMouseTransparent(true);
            alphabetPane.getChildren().add(curRectangle);
            alphabetPane.getChildren().add(text);
            currentX += 40;
        }
    }

    private void createGameField() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int currentX = 45 * i;
                int currentY = 45 * j;
                final Rectangle curRectangle = new Rectangle(45, 45);
                curRectangle.setX(currentX);
                curRectangle.setY(currentY);
                curRectangle.setFill(Color.LIGHTBLUE);
                curRectangle.setStroke(Color.HONEYDEW);
                setRectangleProperties(curRectangle);
                final int finalI = i, finalJ = j;
                EventHandler gameFieldClick = new EventHandler() {
                    @Override
                    public void handle(Event event) {
                        if (chosenX != -1) {
                            gameFieldRect[chosenX][chosenY].setFill(Color.LIGHTBLUE);
                            textGameField[chosenX][chosenY].setText("");
                        }
                        chosenX = finalI;
                        chosenY = finalJ;
                        curRectangle.setFill(Color.CADETBLUE);
                    }
                };
                curRectangle.setOnMouseClicked(gameFieldClick);
                gameFieldPane.getChildren().add(curRectangle);
                gameFieldRect[i][j] = curRectangle;

                textGameField[i][j] = new Text();
                textGameField[i][j].setX(i * 45 + 15);
                textGameField[i][j].setY(j * 45 + 28);
                textGameField[i][j].setMouseTransparent(true);
                textGameField[i][j].setFont(Font.font("Colibri", FontWeight.BOLD, 20));
                textGameField[i][j].setFill(Color.WHITE);
                gameFieldPane.getChildren().add(textGameField[i][j]);
            }
        }
    }

    private void setRectangleProperties(final Rectangle rectangle) {
        rectangle.setFill(Color.LIGHTBLUE);
        rectangle.setStroke(Color.HONEYDEW);
        EventHandler mouseEnteredHandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                if (chosenX == -1)
                    rectangle.setFill(Color.GOLD);
                else {
                    if (rectangle != gameFieldRect[chosenX][chosenY])
                        rectangle.setFill(Color.GOLD);
                }
            }
        };
        EventHandler mouseExitedHandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                if (chosenX == -1)
                    rectangle.setFill(Color.LIGHTBLUE);
                else {
                    if (rectangle != gameFieldRect[chosenX][chosenY])
                        rectangle.setFill(Color.LIGHTBLUE);
                }
            }
        };
        rectangle.setOnMouseEntered(mouseEnteredHandler);
        rectangle.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedHandler);
    }

    private void initGameField(String startWord) {
        for (int i = 0; i < 5; i++) {
            textGameField[i][2].setText(String.valueOf(upCase(startWord.charAt(i))));
            gameFieldRect[i][2].setOnMouseEntered(null);
            gameFieldRect[i][2].setOnMouseClicked(null);
        }
    }

    private char upCase(char letter) {
        return (char)(letter - 'а' + 'А');
    }

    private char lowCase(char letter) {
        return (char)(letter - 'А' + 'а');
    }

    @FXML
    private void makeWordClick() {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                if (textGameField[i][j].getText().isEmpty()) {
                    gameFieldRect[i][j].setOnMouseClicked(null);
                    continue;
                }

                final int finalJ = j;
                final int finalI = i;
                EventHandler wordMakeHandler = new EventHandler() {
                    @Override
                    public void handle(Event event) {
                        if (canTakeLetter(finalI, finalJ)) {
                            resultWordLabel.setText(resultWordLabel.getText() + textGameField[finalI][finalJ].getText());
                            letterUsed[finalI][finalJ] = true;
                            lastTakeX = finalI;
                            lastTakeY = finalJ;
                        }
                    }
                };
                gameFieldRect[i][j].setOnMouseClicked(wordMakeHandler);
            }
    }

    private boolean canTakeLetter(int indexI, int indexJ) {
        if (letterUsed[indexI][indexJ])
            return false;

        boolean isNotFirstLetter = false;
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                isNotFirstLetter |= letterUsed[i][j];
            }

        if (!isNotFirstLetter)
            return true;

        return Math.abs(indexI - lastTakeX) + Math.abs(indexJ - lastTakeY) == 1;
    }

    @FXML
    private void resetClick() {
        resultWordLabel.setText("");
        if (chosenX != -1) {
            textGameField[chosenX][chosenY].setText("");
            gameFieldRect[chosenX][chosenY].setOnMouseClicked(null);
            gameFieldRect[chosenX][chosenY].setFill(Color.LIGHTBLUE);
        }
        chosenX = -1;
        chosenY = -1;
        lastTakeY = -1;
        lastTakeX = -1;
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                letterUsed[i][j] = false;
                if (textGameField[i][j].getText().isEmpty()) {
                    final int finalI = i, finalJ = j;
                    EventHandler gameFieldClick = new EventHandler() {
                        @Override
                        public void handle(Event event) {
                            if (chosenX != -1) {
                                gameFieldRect[chosenX][chosenY].setFill(Color.LIGHTBLUE);
                                textGameField[chosenX][chosenY].setText("");
                            }
                            chosenX = finalI;
                            chosenY = finalJ;
                            gameFieldRect[finalI][finalJ].setFill(Color.CADETBLUE);
                        }
                    };
                    gameFieldRect[i][j].setOnMouseClicked(gameFieldClick);
                }
                else {
                    gameFieldRect[i][j].setOnMouseClicked(null);
                }
            }
    }

    @FXML
    private void sendClick() {
        if (chosenX == -1)
            return;

        if (!letterUsed[chosenX][chosenY]) {
            showInfoMessage("Новая буква должна входить в слово");
            return;
        }

        String word = resultWordLabel.getText().toLowerCase();

        if (usedWords.contains(word)) {
            showInfoMessage("Это слово уже использовано в текущей игре");
            return;
        }

        if (connector.checkWord(word)) {
            connector.sendMove(word, textGameField[chosenX][chosenY].getText(), chosenX, chosenY);
            total1 += word.length();
            titledPane1.setText("Счет: " + String.valueOf(total1));
            wordsList1.add(word + " (" + String.valueOf(word.length()) + ")");
            listView1.setItems(wordsList1);
            statusLabel.setText("Ход соперника...");
            lockField(true);
            checkSituation();
            connector.waitMove(titledPane1);
            checkSituation();
            lockField(false);
        }
        else {
            showInfoMessage("Слово не найдено в словаре");
            return;
        }

        gameFieldRect[chosenX][chosenY].setOnMouseClicked(null);
        gameFieldRect[chosenX][chosenY].setOnMouseEntered(null);
        gameFieldRect[chosenX][chosenY].setFill(Color.LIGHTBLUE);
        chosenX = -1;
        resetClick();
    }

    private void recieveMove() {
        //TCPConnector.Response response = new TCPConnector.Response();


    }


    private void showInfoMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private void lockField(boolean value) {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                gameFieldPane.setDisable(!value);
    }

    private void checkSituation() {
        if (usedWords.size() == 21) {
            if (total1 > total2)
                showInfoMessage("Поздравляю! Вы победили");
            else {
                if (total2 > total1)
                    showInfoMessage("Вы проиграли");
                else
                    showInfoMessage("Ничья");
            }
        }
    }
}
