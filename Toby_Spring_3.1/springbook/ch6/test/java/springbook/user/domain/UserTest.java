package springbook.user.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    User user;

    @BeforeEach
    public void setUp(){
        user = new User();
    }

    @Test
    public void 레벨업그레이드(){
        Level[] levels = Level.values();
        for (Level level : levels){
            if (level.getNextLevel() == null)continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.getNextLevel());
        }
    }

    @Test
    public void cannotUpgradeLevel(){
        Level[] levels = Level.values();
        for (Level level : levels){
            if(level.getNextLevel() != null) continue;
            user.setLevel(level);


            assertThrows(IllegalStateException.class,() -> user.upgradeLevel());
        }

    }

}