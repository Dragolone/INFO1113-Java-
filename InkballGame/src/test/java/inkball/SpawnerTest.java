package inkball;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpawnerTest {

    @Test
    void testNoBall() {
        Spawner spawner=new Spawner(1,1,' ');
        ArrayList<Ball> canList=new ArrayList<>();
        spawner.spawnerBall(new ArrayList<>(),canList);
        assertTrue(canList.size()==0);
        canList.add(new Ball(1,1,'1'));
        spawner.spawnerBall(new ArrayList<>(),canList);

    }
}
