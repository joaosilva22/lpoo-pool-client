/**
 * Created by joaopsilva on 07-06-2016.
 */
import com.mygdx.game.sprites.Cue;
import com.mygdx.game.sprites.CueBall;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class GameLogicTest {

    @Test
    public void testCreateCueball() {
        CueBall cueBall = new CueBall(0, 0);

        assertTrue(((float) Math.PI / 2) == cueBall.getDirection());
        assertTrue(((float) (Math.PI * 3) / 2) == cueBall.getHitAngle());

        cueBall.dispose();
    }

    @Test
    public void testSetHitAngleCueball() {
        CueBall cueBall = new CueBall(0, 0);

        assertTrue(((float) Math.PI / 2) == cueBall.getDirection());
        assertTrue(((float) (Math.PI * 3) / 2) == cueBall.getHitAngle());

        cueBall.setDirection(3);
        cueBall.setHitAngle(3);
        cueBall.update(0);

        assertTrue(3 == cueBall.getDirection());
        assertTrue(3 == cueBall.getHitAngle());

        cueBall.dispose();
    }

    @Test
    public void testCreateCue() {
        Cue cue = new Cue(0, 0);

        assertTrue(0 == cue.getImpulseMultiplier());
        cue.dispose();
    }

    @Test
    public void testSetImpulseMultiplier() {
        Cue cue = new Cue(0, 0);

        assertTrue(0 == cue.getImpulseMultiplier());
        cue.setImpulseMultiplier(2, false);

        cue.update(0);
        assertTrue(2 == cue.getImpulseMultiplier());
        cue.dispose();
    }


}
