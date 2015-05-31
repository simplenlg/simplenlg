package simplenlg.format.english;

import junit.framework.Assert;
import org.junit.Test;


public class NumberedPrefixTest {

    @Test
    public void testNewInstancePrefixIsZero() {
        NumberedPrefix prefix = new NumberedPrefix();
        Assert.assertEquals("0", prefix.getPrefix());
    }

    @Test
    public void testIncrementFromNewInstanceIsOne() {
        NumberedPrefix prefix = new NumberedPrefix();
        prefix.increment();
        Assert.assertEquals("1", prefix.getPrefix());
    }

    @Test
    public void testIncrementForTwoPointTwoIsTwoPointThree() {
        NumberedPrefix prefix = new NumberedPrefix();
        prefix.setPrefix("2.2");
        prefix.increment();
        Assert.assertEquals("2.3", prefix.getPrefix());
    }

    @Test
    public void testIncrementForThreePointFourPointThreeIsThreePointFourPointFour() {
        NumberedPrefix prefix = new NumberedPrefix();
        prefix.setPrefix("3.4.3");
        prefix.increment();
        Assert.assertEquals("3.4.4", prefix.getPrefix());
    }

    @Test
    public void testUpALevelForNewInstanceIsOne() {
        NumberedPrefix prefix = new NumberedPrefix();
        prefix.upALevel();
        Assert.assertEquals("1", prefix.getPrefix());
    }

    @Test
    public void testDownALevelForNewInstanceIsZero() {
        NumberedPrefix prefix = new NumberedPrefix();
        prefix.downALevel();
        Assert.assertEquals("0", prefix.getPrefix());
    }

    @Test
    public void testDownALevelForSevenIsZero() {
        NumberedPrefix prefix = new NumberedPrefix();
        prefix.setPrefix("7");
        prefix.downALevel();
        Assert.assertEquals("0", prefix.getPrefix());
    }

    @Test
    public void testDownALevelForTwoPointSevenIsTwo() {
        NumberedPrefix prefix = new NumberedPrefix();
        prefix.setPrefix("2.7");
        prefix.downALevel();
        Assert.assertEquals("2", prefix.getPrefix());
    }

    @Test
    public void testDownALevelForThreePointFourPointThreeIsThreePointFour() {
        NumberedPrefix prefix = new NumberedPrefix();
        prefix.setPrefix("3.4.3");
        prefix.downALevel();
        Assert.assertEquals("3.4", prefix.getPrefix());
    }
}