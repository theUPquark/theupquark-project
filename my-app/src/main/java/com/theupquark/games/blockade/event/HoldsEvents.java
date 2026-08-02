package com.theupquark.games.blockade.event;

import java.util.List;

import com.theupquark.games.blockade.Blockade;

public interface HoldsEvents {
  List<Event> getEvents();

  default void registerEvent(Event event) {
    getEvents().add(event);
  }

  default void trigger(Blockade game, Event.GamePhase phase) {
    getEvents().stream()
      .filter(event -> event.getPhase() == phase)
      .forEach(event -> event.takeAction(game));
  }

  default void unregisterEvents() {
    getEvents().removeIf(Event::unregister);
  }
}
