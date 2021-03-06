package ywhjenglee.chess.GUI;

import ywhjenglee.chess.Pieces.Piece;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BoardGUI {

    private ChessGUI aChessGUI;

    private static StackPane aChessBoardView;
    private static GridPane aTilesPane;
    private static GridPane aPiecesPane;
    private static GridPane aLegalMovesPane;
    private static GridPane aControllerPane;
    private static GridPane aPromotionPane;
    private ColumnConstraints aColumnConstraints;
    private RowConstraints aRowConstraints;

    private BackgroundFill white = new BackgroundFill(Color.valueOf("#e5e7e8"), new CornerRadii(0), new Insets(0));
    private BackgroundFill black = new BackgroundFill(Color.valueOf("#3352fc"), new CornerRadii(0), new Insets(0));
    
    public BoardGUI(ChessGUI pChessGUI) {
        aChessGUI = pChessGUI;

        // Initialize layout
        aChessBoardView = new StackPane();
        aChessBoardView.setPadding(new Insets(25));
        aChessBoardView.setAlignment(Pos.CENTER);
        aChessBoardView.setPrefHeight(600);
        aChessBoardView.setPrefWidth(600);

        // Set column and row constraints for BoardGUI
        aColumnConstraints = new ColumnConstraints();
        aColumnConstraints.setPercentWidth(100.0/8.0);
        aColumnConstraints.setHalignment(HPos.CENTER);
        aRowConstraints = new RowConstraints();
        aRowConstraints.setPercentHeight(100.0/8.0);
        aRowConstraints.setValignment(VPos.CENTER);

        // Setup Board GUI
        createTilesPane();
        createPiecesPane();
        createLegalMovesPane();
        createControllerPane();
        aChessBoardView.getChildren().addAll(aTilesPane, aPiecesPane, aLegalMovesPane, aControllerPane);
    }

    private void createTilesPane() {
        aTilesPane = new GridPane();
        for (int i = 0; i < 8; i++) {
            aTilesPane.getColumnConstraints().add(aColumnConstraints);
            aTilesPane.getRowConstraints().add(aRowConstraints);
        }
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                HBox aTile = new HBox();
                if ((i + j) % 2 == 0) {
                    aTile.setBackground(new Background(black));
                } else {
                    aTile.setBackground(new Background(white));
                }
                aTilesPane.add(aTile, i, 7-j);
            }
        }
    }

    private void createPiecesPane() {
        aPiecesPane = new GridPane();
        for (int i = 0; i < 8; i++) {
            aPiecesPane.getColumnConstraints().add(aColumnConstraints);
            aPiecesPane.getRowConstraints().add(aRowConstraints);
        }
        refreshPieces();
    }

    private void createLegalMovesPane() {
        aLegalMovesPane = new GridPane();
        for (int i = 0; i < 8; i++) {
            aLegalMovesPane.getColumnConstraints().add(aColumnConstraints);
            aLegalMovesPane.getRowConstraints().add(aRowConstraints);
        }
        refreshMoves();
    }

    private void createControllerPane() {
        aControllerPane = new GridPane();
        aControllerPane.setGridLinesVisible(true);
        for (int i = 0; i < 8; i++) {
            aControllerPane.getColumnConstraints().add(aColumnConstraints);
            aControllerPane.getRowConstraints().add(aRowConstraints);
        }
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                aControllerPane.add(new ChessController(aChessGUI, i, j), i, 7-j);
            }
        }
    }

    public void openPromotionPane() {
        aPromotionPane = new GridPane();
        for (int i = 0; i < 8; i++) {
            aPromotionPane.getColumnConstraints().add(aColumnConstraints);
            aPromotionPane.getRowConstraints().add(aRowConstraints);
        }
        int xPos = aChessGUI.getChessModel().getSelectedPiece().getX() - 2;
        int yPos = aChessGUI.getChessModel().getSelectedPiece().getY() - 2;
        if (yPos == 7) {
            for (int i = 0; i < 4; i++) {
                aPromotionPane.add(new PromotionController(aChessGUI, yPos-i), xPos, 7-(yPos-i));
            }
        } else if (yPos == 0) {
            for (int i = 0; i < 4; i++) {
                aPromotionPane.add(new PromotionController(aChessGUI, yPos+i), xPos, 7-(yPos-i));
            }
        }
        aChessBoardView.getChildren().add(aPromotionPane);
    }

    public void closePromotionPane() {
        aChessBoardView.getChildren().remove(aPromotionPane);
    }

    private void refreshPieces() {
        aPiecesPane.getChildren().clear();
        for (Piece piece : aChessGUI.getChessModel().getAllPieces()) {
            int i = piece.getX()-2;
            int j = piece.getY()-2;
            Text aPieceText = new Text(piece.getName());
            aPieceText.setFont(Font.font(40));
            aPiecesPane.add(aPieceText, i, 7-j);
        }
    }

    public void refreshMoves() {
        aLegalMovesPane.getChildren().clear();
        if (aChessGUI.getChessModel().getSelectedPiece() != null) {
            int[][] legalMoves = aChessGUI.getChessModel().getVisibleSelectedLegalMoves();
            for (int j = 0; j < 8; j++) {
                for (int i = 0; i < 8; i++) {
                    if(legalMoves[i][j] > 0) {
                        Circle aLegalMove = new Circle(8);
                        aLegalMove.setFill(Color.rgb(128, 128, 128, 0.5));
                        aLegalMovesPane.add(aLegalMove, i, 7-j);
                    }
                }
            }
        }
    }

    public void refreshView() {
        aLegalMovesPane.getChildren().clear();
        refreshPieces();
    }

    public StackPane getView() {
        return aChessBoardView;
    }
}
