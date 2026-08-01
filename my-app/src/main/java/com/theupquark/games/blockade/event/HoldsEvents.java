package com.theupquark.games.blockade.event;

import java.util.List;

public interface HoldsEvents {
  List<Event> getEvents();

  default void registerEvent(Event event) {
    getEvents().add(event);
  }

  default void trigger(Event.GamePhase phase) {
    getEvents().stream()
      .filter(event -> event.getPhase() == phase)
      .forEach(Event::takeAction);
  }

  default void unregisterEvents() {
    getEvents().removeIf(Event::unregister);
  }
}
