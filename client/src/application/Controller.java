package application;

import connection.TCPConnector;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import sun.font.FontFamily;

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

    private List<Rectangle> letterButtons = new ArrayList<Rectangle>();
    private Rectangle[][] gameFieldRect = new Rectangle[5][5];

    private final char[] alphabet = {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'Й',
                                     'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч',
                                     'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    private TCPConnector connector;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createButtonsOnForm();
        createGameField();
        setRectangleProperties(connectButton);
        setRectangleProperties(createGameButton);
    }

    @FXML
    private void createGameClick() {
        connector = new TCPConnector();
    }

    @FXML
    private void connectClick() {
        connector = new TCPConnector(ipTextField.getText());
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
            Text text = new Text(String.valueOf(alphabet[i]));
            text.setX(currentX + 20);
            text.setY(currentY + 20);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Colibri", FontWeight.BOLD, 15));
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
                gameFieldPane.getChildren().add(curRectangle);
                gameFieldRect[i][j] = curRectangle;
            }
        }
    }

    private void setRectangleProperties(final Rectangle rectangle) {
        rectangle.setFill(Color.LIGHTBLUE);
        rectangle.setStroke(Color.HONEYDEW);
        EventHandler mouseEnteredHandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                rectangle.setFill(Color.GOLD);
            }
        };
        EventHandler mouseExitedHandler = new EventHandler() {
            @Override
            public void handle(Event event) {
                rectangle.setFill(Color.LIGHTBLUE);
            }
        };
        rectangle.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnteredHandler);
        rectangle.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedHandler);
    }
}
