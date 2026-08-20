package com.gamingtracker.model;

/**
 * Definiert die möglichen Status eines Spiels.
 *
 * Durch ein Enum können nur diese drei gültigen Werte
 * verwendet werden. Dadurch werden Tippfehler und ungültige
 * Statuswerte vermieden.
 */
public enum GameStatus {

    BACKLOG,

    PLAYING,

    COMPLETED
}
