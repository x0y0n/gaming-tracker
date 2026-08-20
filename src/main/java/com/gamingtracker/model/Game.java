package com.gamingtracker.model;

/**
 * Repräsentiert ein einzelnes Spiel im Gaming Tracker.
 *
 * Ein Game-Objekt speichert die grundlegenden Informationen
 * zu einem Spiel, wie Name, Genre, Spielzeit, Bewertung und Status.
 */
public class Game {

    private String name;
    private String genre;
    private double playtime;
    private int rating;
    private GameStatus status;

    /**
     * Leerer Konstruktor, der von Jackson beim Laden aus der
     * JSON-Datei benötigt wird.
     */
    public Game() {
    }

    /**
     * Erzeugt ein neues Spiel.
     *
     * Die Setter werden bewusst verwendet, damit die Eingaben
     * direkt über die vorhandene Validierungslogik geprüft werden.
     */
    public Game(
            String name,
            String genre,
            double playtime,
            int rating,
            GameStatus status
    ) {
        setName(name);
        setGenre(genre);
        setPlaytime(playtime);
        setRating(rating);
        setStatus(status);
    }

    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Spiels.
     *
     * Ein leerer oder null-Wert ist nicht erlaubt.
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Der Spielname darf nicht leer sein."
            );
        }

        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    /**
     * Setzt das Genre des Spiels.
     *
     * Auch hier wird verhindert, dass ungültige leere Werte
     * im Game-Objekt gespeichert werden.
     */
    public void setGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException(
                    "Das Genre darf nicht leer sein."
            );
        }

        this.genre = genre;
    }

    public double getPlaytime() {
        return playtime;
    }

    /**
     * Setzt die Spielzeit.
     *
     * Negative Spielzeiten sind logisch nicht möglich und
     * werden deshalb bereits hier abgefangen.
     */
    public void setPlaytime(double playtime) {
        if (playtime < 0) {
            throw new IllegalArgumentException(
                    "Die Spielzeit darf nicht negativ sein."
            );
        }

        this.playtime = playtime;
    }

    public int getRating() {
        return rating;
    }

    /**
     * Setzt die Bewertung des Spiels.
     *
     * Die Bewertung wird auf einen Bereich von 0 bis 5 begrenzt.
     */
    public void setRating(int rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException(
                    "Die Bewertung muss zwischen 0 und 5 liegen."
            );
        }

        this.rating = rating;
    }

    public GameStatus getStatus() {
        return status;
    }

    /**
     * Setzt den aktuellen Status des Spiels.
     *
     * Ein Spiel muss immer einen gültigen Status besitzen.
     */
    public void setStatus(GameStatus status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "Der Status darf nicht null sein."
            );
        }

        this.status = status;
    }

    /**
     * Liefert eine gut lesbare Darstellung des Spiels.
     *
     * Das ist besonders praktisch für Debugging und
     * Ausgaben in der Konsole.
     */
    @Override
    public String toString() {
        return name + " - " + genre
                + " - " + playtime + "h"
                + " - " + rating + "/5"
                + " - " + status;
    }
}
