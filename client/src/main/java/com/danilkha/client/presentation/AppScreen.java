package com.danilkha.client.presentation;

import org.danilkha.game.LobbyDto;

public sealed interface AppScreen permits AppScreen.Game, AppScreen.LobbyRoom, AppScreen.Menu, AppScreen.NameInput {
    final class Menu implements AppScreen{}
    record NameInput(
            LobbyDto lobby
    ) implements AppScreen{ }

    record LobbyRoom(LobbyDto lobbyDto, String playerName) implements AppScreen{}

    final record Game(
            String[] playerName,
            String me
    ) implements AppScreen{}
}
