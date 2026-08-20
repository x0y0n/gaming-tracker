package com.gamingtracker.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gamingtracker.model.Game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Verantwortlich für das dauerhafte Speichern und Laden
 * der Spielebibliothek.
 *
 * Die Daten werden als JSON-Datei gespeichert, sodass die
 * Spiele auch nach dem Beenden der Anwendung erhalten bleiben.
 */
public class GameRepository {

    /*
     * ObjectMapper übernimmt die Umwandlung zwischen
     * Java-Objekten und JSON.
     */
    private final ObjectMapper objectMapper;

    /*
     * Pfad zur Datei, in der die Spieldaten gespeichert werden.
     */
    private final Path filePath;

    public GameRepository() {

        objectMapper = new ObjectMapper();

        /*
         * Formatiert die JSON-Datei übersichtlich mit Einrückungen.
         * Dadurch bleibt die Datei auch für Menschen gut lesbar.
         */
        objectMapper.enable(
                SerializationFeature.INDENT_OUTPUT
        );

        /*
         * Die Daten werden unter data/games.json gespeichert.
         */
        filePath = Paths.get(
                "data",
                "games.json"
        );
    }

    /**
     * Speichert die übergebene Spieleliste als JSON-Datei.
     */
    public void save(List<Game> games) {

        try {

            /*
             * Erstellt den data-Ordner automatisch, falls er
             * noch nicht existiert.
             */
            Files.createDirectories(
                    filePath.getParent()
            );

            /*
             * Jackson serialisiert die Liste von Game-Objekten
             * und schreibt sie direkt in die JSON-Datei.
             */
            objectMapper.writeValue(
                    filePath.toFile(),
                    games
            );

        } catch (IOException e) {

            /*
             * Ein Fehler beim Speichern wird als RuntimeException
             * weitergegeben, damit der aufrufende Code nicht
             * mit IOException arbeiten muss.
             */
            throw new RuntimeException(
                    "Spiele konnten nicht gespeichert werden.",
                    e
            );
        }
    }

    /**
     * Lädt die gespeicherten Spiele aus der JSON-Datei.
     *
     * Falls noch keine Datei existiert, wird eine leere Liste
     * zurückgegeben.
     */
    public List<Game> load() {

        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {

            /*
             * TypeReference teilt Jackson mit, dass die JSON-Datei
             * eine Liste von Game-Objekten enthält.
             */
            return objectMapper.readValue(
                    filePath.toFile(),
                    new TypeReference<List<Game>>() {}
            );

        } catch (IOException e) {

            /*
             * Fehler beim Lesen oder Parsen der JSON-Datei
             * werden verständlich weitergegeben.
             */
            throw new RuntimeException(
                    "Spiele konnten nicht geladen werden.",
                    e
            );
        }
    }
}