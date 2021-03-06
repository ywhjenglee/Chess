package ywhjenglee.chess.GUI;

import ywhjenglee.chess.ChessModel;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;

public class ChessGUI extends Application {

	private ChessModel aChessModel;
	private final String[] gameModes = {"Standard", "Fischer Random"};

	private static GridPane aMainPane;
	private static GridPane aGamePane;
	private static VBox aMenuPane;

	private BoardGUI aBoardGUI;
	private ProfileGUI aWhiteProfileGUI;
	private ProfileGUI aBlackProfileGUI;

	public static void main(String[] pArgs) {
		launch(pArgs);
	}

	@Override
	public void start(Stage pStage){
		// Create model
		aChessModel = new ChessModel("Standard");

		// Initialize layouts
		aMainPane = new GridPane();
		aGamePane = new GridPane();
		aMenuPane = new MenuGUI(this);

		// Setup GUI
		setupMainPane();
		setupGamePane();
	
		// Set scene and show
		Scene aScene = new Scene(aMainPane);
		pStage.setTitle("ywhjenglee Chess Game");
		pStage.setMinWidth(1000);
		pStage.setMinHeight(800);
		pStage.setScene(aScene);
		pStage.show();
	}

	private void setupMainPane() {
		// Set column constraints for aMainPane
		ColumnConstraints gameConstraints = new ColumnConstraints();
		gameConstraints.setPercentWidth(60);
		ColumnConstraints menuConstraints = new ColumnConstraints();
		menuConstraints.setPercentWidth(40);
		aMainPane.getColumnConstraints().addAll(gameConstraints, menuConstraints);

		// Set row constraints for aMainPane
		RowConstraints aRowConstraints = new RowConstraints();
		aRowConstraints.setVgrow(Priority.ALWAYS);
		aMainPane.getRowConstraints().add(aRowConstraints);

		// Set nodes in aMainPane
		aMainPane.add(aGamePane, 0, 0);
		aMainPane.add(aMenuPane, 1, 0);
	}

	private void setupGamePane() {
		// Set column constraints for aGamePane
		ColumnConstraints aColumnConstraints = new ColumnConstraints();
		aColumnConstraints.setHgrow(Priority.ALWAYS);
		aGamePane.getColumnConstraints().add(aColumnConstraints);

		// Set row constraints for aGamePane
		RowConstraints profileConstraints = new RowConstraints();
		profileConstraints.setPercentHeight(10);
		RowConstraints boardConstraints = new RowConstraints();
		boardConstraints.setPercentHeight(80);
		aGamePane.getRowConstraints().addAll(profileConstraints, boardConstraints, profileConstraints);

		// Set nodes in aGamePane
		aGamePane.add(createProfile(false), 0, 0);
		aGamePane.add(createBoard(), 0, 1);
		aGamePane.add(createProfile(true), 0, 2);
	}

	private StackPane createBoard() {
		aBoardGUI = new BoardGUI(this);
		return aBoardGUI.getView();
	}

	private HBox createProfile(boolean pColor) {
		if (pColor) {
			aWhiteProfileGUI = new ProfileGUI(this, pColor);
			return aWhiteProfileGUI.getView();
		} else {
			aBlackProfileGUI = new ProfileGUI(this, pColor);
			return aBlackProfileGUI.getView();
		}
	}

	public ChessModel getChessModel() {
		return aChessModel;
	}

	public BoardGUI getBoardGUI() {
		return aBoardGUI;
	}

	public ProfileGUI getProfileGUI(boolean pColor) {
		if (pColor) {
			return aWhiteProfileGUI;
		} else {
			return aBlackProfileGUI;
		}
	}

	public void showGameOver() {
		Stage gameOverWindow = new Stage();
		gameOverWindow.initModality(Modality.APPLICATION_MODAL);
		gameOverWindow.setTitle("Game Over");
		VBox gameOverPane = new VBox();
		gameOverPane.setSpacing(20);
		gameOverPane.setAlignment(Pos.CENTER);
		Text gameOverText = new Text(aChessModel.getResult());
		Button newGameButton = new Button("New Game");
		newGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent pEvent) {
				showNewGame();
				gameOverWindow.close();
			}
		});
		gameOverPane.getChildren().addAll(gameOverText, newGameButton);
		Scene aScene = new Scene(gameOverPane);
		gameOverWindow.setMinWidth(300);
		gameOverWindow.setMinHeight(200);
		gameOverWindow.setResizable(false);
		gameOverWindow.setScene(aScene);
		gameOverWindow.showAndWait();
	}

	public void showNewGame() {
		Stage newGameWindow = new Stage();
		newGameWindow.initModality(Modality.APPLICATION_MODAL);
		newGameWindow.setTitle("New Game");
		VBox newGamePane = new VBox();
		newGamePane.setSpacing(20);
		newGamePane.setAlignment(Pos.CENTER);
		ComboBox<String> gameModeComboBox = new ComboBox<>(FXCollections.observableArrayList(gameModes));
		gameModeComboBox.getSelectionModel().selectFirst();
		Button playButton = new Button("New Game");
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent pEvent) {
				createNewGame(gameModeComboBox.getValue());
				newGameWindow.close();
			}
		});
		newGamePane.getChildren().addAll(gameModeComboBox, playButton);
		Scene aScene = new Scene(newGamePane);
		newGameWindow.setMinWidth(300);
		newGameWindow.setMinHeight(200);
		newGameWindow.setResizable(false);
		newGameWindow.setScene(aScene);
		newGameWindow.showAndWait();
	}

	private void createNewGame(String pGameMode) {
		aChessModel = new ChessModel(pGameMode);

		aGamePane = new GridPane();
		aMenuPane = new MenuGUI(this);

		setupGamePane();

		aMainPane.getChildren().clear();
		aMainPane.add(aGamePane, 0, 0);
		aMainPane.add(aMenuPane, 1, 0);
	}
}
