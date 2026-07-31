package com.theupquark.games.blockade.bricks;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BrickDebrisTest {

  @Test
  public void testBrickDebrisInitialization() {
    BrickDebris debris = new BrickDebris(100, 150, 20, 12.5, Color.RED, 2.5, -3.0, 10.0);
    assertTrue(debris.isDead(), "Debris should be marked dead on initialization");
    assertFalse(debris.collisionEnabled(), "Debris collision should be disabled");
    assertEquals(100, debris.getX());
    assertEquals(150, debris.getY());
    assertEquals(20, debris.getWidth());
    assertEquals(12.5, debris.getHeight());
    assertEquals(Color.RED, debris.getFill());
  }

  @Test
  public void testBrickDebrisProceedToDieProgression() {
    BrickDebris debris = new BrickDebris(100, 150, 20, 12.5, Color.BLUE, 2.0, -1.0, 5.0);
    
    double initialX = debris.getX();
    double initialY = debris.getY();

    // First 25 steps should return false while animating
    for (int i = 1; i <= 25; i++) {
      boolean expired = debris.proceedToDie();
      assertFalse(expired, "Debris should remain active before MAX_STEPS");
    }

    // Verify position updated over physics steps
    assertTrue(debris.getX() > initialX, "X position should increment by velocityX");
    
    // 26th step should expire and return true for scene graph removal
    boolean expiredFinal = debris.proceedToDie();
    assertTrue(expiredFinal, "Debris should return true for removal after exceeding MAX_STEPS");
  }

  @Test
  public void testBrickSpawnDebrisCountAndProperties() {
    Brick brick = new Brick(200, 100, Color.GREEN);
    List<BrickDebris> shards = brick.spawnDebris();

    assertNotNull(shards);
    assertEquals(8, shards.size(), "Brick should break into 8 shards (4x2 grid)");

    for (BrickDebris shard : shards) {
      assertTrue(shard.isDead(), "Each shard should be marked dead");
      assertFalse(shard.collisionEnabled(), "Each shard collision should be disabled");
      assertEquals(20.0, shard.getWidth(), 0.001);
      assertEquals(12.5, shard.getHeight(), 0.001);
      assertEquals(Color.GREEN, shard.getFill());
    }
  }
}
