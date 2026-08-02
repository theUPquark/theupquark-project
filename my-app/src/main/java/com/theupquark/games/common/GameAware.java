package com.theupquark.games.common;

import com.theupquark.games.blockade.Blockade;

public interface GameAware {
  Blockade getGame();
  void setGame(Blockade game);
}
