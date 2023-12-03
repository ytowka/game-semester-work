package com.danilkha.client.presentation;

import org.danilkha.game.LobbyDto;

public sealed interface AppScreen permits AppScreen.Menu, AppScreen.NameInput, AppScreen.LobbyRoom {
    final class Menu implements AppScreen{}
    record NameInput(
            LobbyDto lobby
    ) implements AppScreen{ }

    record LobbyRoom(LobbyDto lobbyDto, String playerName) implements AppScreen{}
}
