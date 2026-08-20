package com.gamingtracker.service;

import java.util.ArrayList;
import java.util.List;

import com.gamingtracker.model.Game;

/**
 * Verwaltet die Sammlung aller Spiele der Anwendung.
 *
 * Die GameLibrary übernimmt dabei die Verwaltung der Spiele
 * sowie grundlegende Auswertungen wie Gesamtspielzeit und
 * durchschnittliche Bewertung.
 *
 * Das Speichern und Laden selbst wird an das GameRepository
 * ausgelagert.
 */
public class GameLibrary {

    /*
     * Interne Liste aller Spiele.
     *
     * final bedeutet hier, dass die Referenz auf die Liste
     * nicht geändert werden kann. Die enthaltenen Spiele
     * können aber weiterhin hinzugefügt oder entfernt werden.
     */
    private final List<Game> games = new ArrayList<>();

    /*
     * Das Repository kümmert sich um die dauerhafte Speicherung
     * der Spiele in der JSON-Datei.
     */
    private final GameRepository repository =
            new GameRepository();

    /**
     * Fügt ein neues Spiel zur Bibliothek hinzu.
     *
     * Nach dem Hinzufügen wird die Bibliothek direkt gespeichert,
     * damit die Änderung auch nach einem Neustart erhalten bleibt.
     */
    public void addGame(Game game) {

        if (game == null) {
            throw new IllegalArgumentException(
                    "Das Spiel darf nicht null sein."
            );
        }

        games.add(game);

        save();
    }

    /**
     * Entfernt ein Spiel aus der Bibliothek und speichert
     * anschließend den aktualisierten Zustand.
     */
    public void removeGame(Game game) {

        games.remove(game);

        save();
    }

    /**
     * Liefert eine unveränderliche Kopie der aktuellen Spieleliste.
     *
     * Dadurch kann Code außerhalb der Klasse die interne Liste
     * nicht direkt verändern.
     */
    public List<Game> getGames() {

        return List.copyOf(games);
    }

    /**
     * Gibt die Anzahl der gespeicherten Spiele zurück.
     */
    public int getGameCount() {

        return games.size();
    }

    /**
     * Berechnet die gesamte Spielzeit aller Spiele.
     *
     * mapToDouble wandelt jedes Game in seine Spielzeit um.
     * sum() addiert anschließend alle Werte.
     */
    public double getTotalPlaytime() {

        return games.stream()
                .mapToDouble(Game::getPlaytime)
                .sum();
    }

    /**
     * Berechnet die durchschnittliche Bewertung aller Spiele.
     *
     * Falls noch keine Spiele vorhanden sind, wird 0 zurückgegeben,
     * um eine Berechnung auf einer leeren Liste zu vermeiden.
     */
    public double getAverageRating() {

        if (games.isEmpty()) {
            return 0;
        }

        return games.stream()
                .mapToInt(Game::getRating)
                .average()
                .orElse(0);
    }

    /**
     * Lädt die gespeicherten Spiele aus dem Repository.
     *
     * Zuerst wird der aktuelle Inhalt der Liste gelöscht,
     * damit keine doppelten Einträge entstehen.
     */
    public void load() {

        games.clear();

        games.addAll(
                repository.load()
        );
    }

    /**
     * Speichert den aktuellen Zustand der Spielebibliothek
     * über das GameRepository.
     */
    public void save() {

        repository.save(games);
    }
}