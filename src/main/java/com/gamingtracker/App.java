package com.gamingtracker;

import com.gamingtracker.model.Game;
import com.gamingtracker.model.GameStatus;
import com.gamingtracker.service.GameLibrary;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class App extends Application {

    /*
     * Die GameLibrary verwaltet alle Spiele und übernimmt
     * zusätzlich das Speichern und Laden der Daten.
     */
    private final GameLibrary library = new GameLibrary();

    /*
     * Container für die Spielkarten im Dashboard.
     * Er wird bei Änderungen der Spieleliste aktualisiert.
     */
    private final VBox gamesContainer = new VBox(15);

    /*
     * Diese Labels werden separat gespeichert, damit die
     * Statistiken nach dem Hinzufügen, Bearbeiten oder Löschen
     * eines Spiels aktualisiert werden können.
     */
    private final Label gamesCountLabel = new Label();
    private final Label playtimeLabel = new Label();
    private final Label averageRatingLabel = new Label();

    @Override
    public void start(Stage stage) {

        /*
         * Beim Start werden die zuvor gespeicherten Spiele
         * aus der JSON-Datei geladen.
         */
        library.load();

        // --------------------------------------------------
        // Hauptlayout
        // --------------------------------------------------

        /*
         * BorderPane teilt das Fenster in verschiedene Bereiche
         * wie oben, Mitte und unten auf.
         */
        BorderPane root = new BorderPane();

        root.setPadding(new Insets(25));

        // --------------------------------------------------
        // Kopfbereich
        // --------------------------------------------------

        Label title = new Label("🎮 GameTracker");

        title.setStyle(
                "-fx-font-size: 30px;"
                + "-fx-font-weight: bold;"
        );

        Label subtitle = new Label(
                "Meine persönliche Spielebibliothek"
        );

        subtitle.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-text-fill: #777777;"
        );

        VBox header = new VBox(
                5,
                title,
                subtitle
        );

        root.setTop(header);

        // --------------------------------------------------
        // Statistiken
        // --------------------------------------------------

        /*
         * Die Statistik-Karten greifen auf die gespeicherten
         * Labels zurück. Dadurch können die Werte später
         * aktualisiert werden, ohne die Karten neu zu erstellen.
         */
        VBox gamesStat = createStatCard(
                "Spiele",
                gamesCountLabel
        );

        VBox playtimeStat = createStatCard(
                "Spielzeit",
                playtimeLabel
        );

        VBox ratingStat = createStatCard(
                "Ø Bewertung",
                averageRatingLabel
        );

        HBox statistics = new HBox(
                20,
                gamesStat,
                playtimeStat,
                ratingStat
        );

        statistics.setPadding(
                new Insets(30, 0, 30, 0)
        );

        root.setCenter(statistics);

        // --------------------------------------------------
        // Bereich für die Spiele
        // --------------------------------------------------

        VBox gamesSection = new VBox(15);

        Label gamesTitle = new Label("Meine Spiele");

        gamesTitle.setStyle(
                "-fx-font-size: 20px;"
                + "-fx-font-weight: bold;"
        );

        /*
         * Öffnet das Fenster zum Hinzufügen eines neuen Spiels.
         */
        Button addGameButton = new Button("+ Spiel hinzufügen");

        addGameButton.setOnAction(
                event -> showAddGameWindow()
        );

        addGameButton.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 10 16 10 16;"
        );

        HBox gamesHeader = new HBox(
                20,
                gamesTitle,
                addGameButton
        );

        gamesHeader.setAlignment(Pos.CENTER_LEFT);

        gamesSection.getChildren().add(gamesHeader);

        /*
         * Der gamesContainer enthält die einzelnen Karten
         * der gespeicherten Spiele.
         */
        gamesSection.getChildren().add(gamesContainer);

        /*
         * Initiale Anzeige der Spiele und Statistiken.
         */
        refreshGames();
        refreshStatistics();

        root.setBottom(gamesSection);

        // --------------------------------------------------
        // Szene und Fenster
        // --------------------------------------------------

        Scene scene = new Scene(
                root,
                900,
                600
        );

        stage.setTitle("GameTracker");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Aktualisiert die Anzeige aller Spielekarten.
     *
     * Zuerst wird der bisherige Inhalt entfernt.
     * Anschließend wird für jedes Spiel eine neue Karte erstellt.
     */
    private void refreshGames() {

        gamesContainer.getChildren().clear();

        HBox gameCards = new HBox(15);

        for (Game game : library.getGames()) {

            VBox card = createGameCard(game);

            gameCards.getChildren().add(card);
        }

        gamesContainer.getChildren().add(gameCards);
    }

    /**
     * Aktualisiert die drei Statistikwerte des Dashboards.
     */
    private void refreshStatistics() {

        gamesCountLabel.setText(
                String.valueOf(
                        library.getGameCount()
                )
        );

        playtimeLabel.setText(
                String.format(
                        "%.1f h",
                        library.getTotalPlaytime()
                )
        );

        averageRatingLabel.setText(
                String.format(
                        "%.1f ⭐",
                        library.getAverageRating()
                )
        );
    }

    /**
     * Öffnet ein modales Fenster zum Hinzufügen eines Spiels.
     *
     * Die eingegebenen Werte werden anschließend validiert
     * und an die GameLibrary übergeben.
     */
    private void showAddGameWindow() {

        Stage window = new Stage();

        window.setTitle("Spiel hinzufügen");

        /*
         * Das Fenster muss geschlossen werden, bevor
         * wieder mit dem Hauptfenster gearbeitet werden kann.
         */
        window.initModality(Modality.APPLICATION_MODAL);

        // --------------------------------------------------
        // Eingabefelder
        // --------------------------------------------------

        Label nameLabel = new Label("Spielname");

        TextField nameField = new TextField();

        nameField.setPromptText(
                "z. B. Minecraft"
        );

        Label genreLabel = new Label("Genre");

        TextField genreField = new TextField();

        genreField.setPromptText(
                "z. B. Sandbox"
        );

        Label playtimeLabel = new Label("Spielzeit");

        TextField playtimeField = new TextField();

        playtimeField.setPromptText(
                "z. B. 120"
        );

        Label ratingLabel = new Label("Bewertung");

        ComboBox<Integer> ratingBox =
                new ComboBox<>();

        /*
         * Erlaubte Bewertungen von 0 bis 5 Sternen.
         */
        for (int i = 0; i <= 5; i++) {
            ratingBox.getItems().add(i);
        }

        ratingBox.setValue(0);

        Label statusLabel = new Label("Status");

        ComboBox<GameStatus> statusBox =
                new ComboBox<>();

        /*
         * Das Enum liefert alle gültigen Spielstatus.
         */
        statusBox.getItems().addAll(
                GameStatus.BACKLOG,
                GameStatus.PLAYING,
                GameStatus.COMPLETED
        );

        statusBox.setValue(
                GameStatus.BACKLOG
        );

        // --------------------------------------------------
        // Buttons
        // --------------------------------------------------

        Button cancelButton = new Button(
                "Abbrechen"
        );

        cancelButton.setOnAction(
                event -> window.close()
        );

        Button addButton = new Button(
                "Hinzufügen"
        );

        addButton.setDefaultButton(true);

        /*
         * Beim Klick werden die Eingaben ausgelesen und
         * ein neues Game-Objekt erstellt.
         */
        addButton.setOnAction(event -> {

            try {

                String name = nameField.getText();

                String genre = genreField.getText();

                double playtime =
                        Double.parseDouble(
                                playtimeField.getText()
                        );

                int rating =
                        ratingBox.getValue();

                GameStatus status =
                        statusBox.getValue();

                Game game = new Game(
                        name,
                        genre,
                        playtime,
                        rating,
                        status
                );

                /*
                 * GameLibrary fügt das Spiel hinzu und
                 * speichert den aktualisierten Zustand.
                 */
                library.addGame(game);

                window.close();

                /*
                 * Dashboard und Statistiken sofort aktualisieren.
                 */
                refreshGames();
                refreshStatistics();

            } catch (NumberFormatException e) {

                /*
                 * Die Spielzeit muss als Zahl eingegeben werden.
                 */
                showError(
                        "Die Spielzeit muss eine Zahl sein."
                );

            } catch (IllegalArgumentException e) {

                /*
                 * Fängt Validierungsfehler aus Game.java ab.
                 */
                showError(
                        e.getMessage()
                );
            }
        });

        HBox buttons = new HBox(
                10,
                cancelButton,
                addButton
        );

        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );

        // --------------------------------------------------
        // Layout des Dialogfensters
        // --------------------------------------------------

        VBox layout = new VBox(
                10,
                nameLabel,
                nameField,
                genreLabel,
                genreField,
                playtimeLabel,
                playtimeField,
                ratingLabel,
                ratingBox,
                statusLabel,
                statusBox,
                buttons
        );

        layout.setPadding(
                new Insets(20)
        );

        Scene scene = new Scene(
                layout,
                400,
                500
        );

        window.setScene(scene);

        window.showAndWait();
    }

    /**
     * Öffnet ein Fenster zum Bearbeiten eines bestehenden Spiels.
     */
    private void showEditGameWindow(Game game) {

        Stage window = new Stage();

        window.setTitle("Spiel bearbeiten");

        window.initModality(
                Modality.APPLICATION_MODAL
        );

        // --------------------------------------------------
        // Name
        // --------------------------------------------------

        Label nameLabel = new Label(
                "Spielname"
        );

        TextField nameField = new TextField(
                game.getName()
        );

        // --------------------------------------------------
        // Genre
        // --------------------------------------------------

        Label genreLabel = new Label(
                "Genre"
        );

        TextField genreField = new TextField(
                game.getGenre()
        );

        // --------------------------------------------------
        // Spielzeit
        // --------------------------------------------------

        Label playtimeLabel = new Label(
                "Spielzeit"
        );

        TextField playtimeField = new TextField(
                String.valueOf(
                        game.getPlaytime()
                )
        );

        // --------------------------------------------------
        // Bewertung
        // --------------------------------------------------

        Label ratingLabel = new Label(
                "Bewertung"
        );

        ComboBox<Integer> ratingBox =
                new ComboBox<>();

        for (int i = 0; i <= 5; i++) {
            ratingBox.getItems().add(i);
        }

        ratingBox.setValue(
                game.getRating()
        );

        // --------------------------------------------------
        // Status
        // --------------------------------------------------

        Label statusLabel = new Label(
                "Status"
        );

        ComboBox<GameStatus> statusBox =
                new ComboBox<>();

        statusBox.getItems().addAll(
                GameStatus.BACKLOG,
                GameStatus.PLAYING,
                GameStatus.COMPLETED
        );

        statusBox.setValue(
                game.getStatus()
        );

        // --------------------------------------------------
        // Abbrechen
        // --------------------------------------------------

        Button cancelButton =
                new Button("Abbrechen");

        cancelButton.setOnAction(
                event -> window.close()
        );

        // --------------------------------------------------
        // Speichern
        // --------------------------------------------------

        Button saveButton =
                new Button("Speichern");

        saveButton.setDefaultButton(true);

        saveButton.setOnAction(event -> {

            try {

                String name = nameField.getText();

                String genre = genreField.getText();

                double playtime =
                        Double.parseDouble(
                                playtimeField.getText()
                        );

                int rating =
                        ratingBox.getValue();

                GameStatus status =
                        statusBox.getValue();

                /*
                 * Das bestehende Game-Objekt wird aktualisiert.
                 * Die Setter übernehmen dabei erneut die Validierung.
                 */
                game.setName(name);
                game.setGenre(genre);
                game.setPlaytime(playtime);
                game.setRating(rating);
                game.setStatus(status);

                /*
                 * Änderungen dauerhaft speichern.
                 */
                library.save();

                window.close();

                /*
                 * Dashboard und Statistiken aktualisieren.
                 */
                refreshGames();
                refreshStatistics();

            } catch (NumberFormatException e) {

                showError(
                        "Die Spielzeit muss eine Zahl sein."
                );

            } catch (IllegalArgumentException e) {

                showError(
                        e.getMessage()
                );
            }
        });

        HBox buttons = new HBox(
                10,
                cancelButton,
                saveButton
        );

        buttons.setAlignment(
                Pos.CENTER_RIGHT
        );

        // --------------------------------------------------
        // Layout des Bearbeitungsfensters
        // --------------------------------------------------

        VBox layout = new VBox(
                10,
                nameLabel,
                nameField,
                genreLabel,
                genreField,
                playtimeLabel,
                playtimeField,
                ratingLabel,
                ratingBox,
                statusLabel,
                statusBox,
                buttons
        );

        layout.setPadding(
                new Insets(20)
        );

        Scene scene = new Scene(
                layout,
                400,
                500
        );

        window.setScene(scene);

        window.showAndWait();
    }

    /**
     * Zeigt einen einfachen Fehlerdialog an.
     */
    private void showError(String message) {

        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.ERROR
                );

        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Erstellt eine Karte für einen Statistikwert.
     *
     * Das Label wird von außen übergeben, damit sein Wert
     * später durch refreshStatistics() aktualisiert werden kann.
     */
    private VBox createStatCard(
            String title,
            Label valueLabel
    ) {

        Label titleLabel = new Label(
                title
        );

        titleLabel.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-text-fill: #777777;"
        );

        valueLabel.setStyle(
                "-fx-font-size: 24px;"
                + "-fx-font-weight: bold;"
        );

        VBox card = new VBox(
                5,
                titleLabel,
                valueLabel
        );

        card.setPadding(
                new Insets(15)
        );

        card.setPrefWidth(180);

        card.setStyle(
                "-fx-background-color: #f4f4f4;"
                + "-fx-background-radius: 12;"
        );

        return card;
    }

    /**
     * Erstellt die grafische Karte für ein einzelnes Spiel.
     *
     * Die Karte enthält Informationen zum Spiel und Buttons
     * zum Bearbeiten und Löschen.
     */
    private VBox createGameCard(Game game) {

        // --------------------------------------------------
        // Name
        // --------------------------------------------------

        Label name = new Label(
                game.getName()
        );

        name.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
        );

        // --------------------------------------------------
        // Genre
        // --------------------------------------------------

        Label genre = new Label(
                game.getGenre()
        );

        genre.setStyle(
                "-fx-text-fill: #777777;"
        );

        // --------------------------------------------------
        // Spielzeit
        // --------------------------------------------------

        Label playtime = new Label(
                String.format(
                        "%.1f Stunden",
                        game.getPlaytime()
                )
        );

        // --------------------------------------------------
        // Bewertung
        // --------------------------------------------------

        Label rating = new Label(
                "⭐".repeat(
                        game.getRating()
                )
        );

        // --------------------------------------------------
        // Status
        // --------------------------------------------------

        /*
         * Je nach Status werden unterschiedliche Symbole
         * und Farben verwendet, damit der Zustand des Spiels
         * auf einen Blick erkennbar ist.
         */
        String statusText;
        String statusColor;

        switch (game.getStatus()) {

            case BACKLOG:
                statusText = "📌 BACKLOG";
                statusColor = "#d97706";
                break;

            case PLAYING:
                statusText = "🎮 PLAYING";
                statusColor = "#2563eb";
                break;

            case COMPLETED:
                statusText = "✅ COMPLETED";
                statusColor = "#16a34a";
                break;

            default:
                statusText =
                        game.getStatus().toString();

                statusColor = "#666666";
        }

        Label status = new Label(
                statusText
        );

        status.setStyle(
                "-fx-font-weight: bold;"
                + "-fx-text-fill: " + statusColor + ";"
        );

        // --------------------------------------------------
        // Bearbeiten
        // --------------------------------------------------

        Button editButton = new Button(
                "Bearbeiten"
        );

        /*
         * Übergibt das aktuelle Game-Objekt an das
         * Bearbeitungsfenster.
         */
        editButton.setOnAction(
                event -> showEditGameWindow(game)
        );

        // --------------------------------------------------
        // Löschen
        // --------------------------------------------------

        Button deleteButton = new Button(
                "Löschen"
        );

        deleteButton.setStyle(
                "-fx-text-fill: #c62828;"
        );

        /*
         * Entfernt das Spiel aus der Bibliothek und aktualisiert
         * anschließend die Anzeige und die Statistiken.
         */
        deleteButton.setOnAction(event -> {

            library.removeGame(game);

            refreshGames();
            refreshStatistics();
        });

        // --------------------------------------------------
        // Buttons
        // --------------------------------------------------

        HBox buttons = new HBox(
                8,
                editButton,
                deleteButton
        );

        // --------------------------------------------------
        // Karte
        // --------------------------------------------------

        VBox card = new VBox(
                8,
                name,
                genre,
                playtime,
                rating,
                status,
                buttons
        );

        card.setPadding(
                new Insets(15)
        );

        card.setPrefWidth(230);

        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #dddddd;"
                + "-fx-border-radius: 12;"
                + "-fx-background-radius: 12;"
        );

        return card;
    }

    public static void main(String[] args) {
        launch(args);
    }
}