package main.programming_language_ii_team4;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RecommendSceneController implements Initializable {
    private final Scrapper scrapper = new Scrapper();
    private final UserDB userDB = new UserDB();
    private List<String> upper;
    private List<String> lower;
    private List<String> shoes;
    private List<String> accessory;

    @FXML
    private ToggleGroup accessoryBtnGroup;

    @FXML
    private ImageView accessoryImageView;

    @FXML
    private Label accessoryLabel;

    @FXML
    private ToggleGroup lowerBtnGroup;

    @FXML
    private ImageView lowerImageView;

    @FXML
    private Label lowerLabel;

    @FXML
    private ToggleGroup shoesBtnGroup;

    @FXML
    private ImageView shoesImageView;

    @FXML
    private Label shoesLabel;

    @FXML
    private ToggleGroup upperBtnGroup;

    @FXML
    private ImageView upperImageView;

    @FXML
    private Label upperLabel;

    @FXML
    void backBtnOnAction(ActionEvent event) {
        try {
            MainStage.changeScene("MainScene.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void refreshBtnOnAction(ActionEvent event) {
        try {
            generateImages();
            ToggleButton selectedUpper = (ToggleButton) upperBtnGroup.getSelectedToggle();
            upperImageView.setImage(new Image(upper.get(Integer.parseInt(selectedUpper.getText())-1)));

            ToggleButton selectedLower = (ToggleButton) lowerBtnGroup.getSelectedToggle();
            lowerImageView.setImage(new Image(lower.get(Integer.parseInt(selectedLower.getText())-1)));

            ToggleButton selectedShoes = (ToggleButton) shoesBtnGroup.getSelectedToggle();
            shoesImageView.setImage(new Image(shoes.get(Integer.parseInt(selectedShoes.getText()) -1)));

            ToggleButton selectedAccessory = (ToggleButton) accessoryBtnGroup.getSelectedToggle();
            accessoryImageView.setImage(new Image(accessory.get(Integer.parseInt(selectedAccessory.getText())-1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateImages();
        loadImages();
        upperImageView.setImage(new Image(upper.getFirst()));
        lowerImageView.setImage(new Image(lower.getFirst()));
        shoesImageView.setImage(new Image(shoes.getFirst()));
        accessoryImageView.setImage(new Image(accessory.getFirst()));
    }
    private void loadImages() {
        upperBtnGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton selectedButton = (ToggleButton) newValue;
                int index = Integer.parseInt(selectedButton.getText());
                upperImageView.setImage(new Image(upper.get(index-1)));
            }
        });

        lowerBtnGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton selectedButton = (ToggleButton) newValue;
                int index = Integer.parseInt(selectedButton.getText());
                lowerImageView.setImage(new Image(lower.get(index-1)));
            }
        });

        shoesBtnGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton selectedButton = (ToggleButton) newValue;
                int index = Integer.parseInt(selectedButton.getText());
                shoesImageView.setImage(new Image(shoes.get(index-1)));
            }
        });

        accessoryBtnGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ToggleButton selectedButton = (ToggleButton) newValue;
                int index = Integer.parseInt(selectedButton.getText());
                accessoryImageView.setImage(new Image(accessory.get(index-1)));
            }
        });
    }

    private void generateImages() {
        String upperKeyWord = User.getUpperColor() + " " + userDB.getUpper();
        upper = scrapper.getImage(upperKeyWord);
        upperLabel.setText(upperKeyWord);

        String lowerKeyWord = User.getLowerColor() + " " + userDB.getLower();
        lower = scrapper.getImage(lowerKeyWord);
        lowerLabel.setText(lowerKeyWord);

        String shoesKeyWord = userDB.getShoes();
        shoes = scrapper.getImage(shoesKeyWord);
        shoesLabel.setText(shoesKeyWord);

        String accessoryKeyWord = userDB.getAccessory();
        accessory = scrapper.getImage(accessoryKeyWord);
        accessoryLabel.setText(accessoryKeyWord);
    }
}
