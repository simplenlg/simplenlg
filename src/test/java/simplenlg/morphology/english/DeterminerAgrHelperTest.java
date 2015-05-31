package simplenlg.morphology.english;

import static org.junit.Assert.*;

import org.junit.Test;

public class DeterminerAgrHelperTest {

	@Test
	public void testRequiresAn() {

		assertTrue(DeterminerAgrHelper.requiresAn("elephant"));
		
		assertFalse(DeterminerAgrHelper.requiresAn("cow"));
		
		// Does not hand phonetics
		assertFalse(DeterminerAgrHelper.requiresAn("hour"));
		
		// But does have exceptions for some numerals
		assertFalse(DeterminerAgrHelper.requiresAn("one"));
		
		assertFalse(DeterminerAgrHelper.requiresAn("100"));
		
	}

	@Test
	public void testCheckEndsWithIndefiniteArticle1() {
		
		String cannedText = "I see a";
		
		String np = "elephant";
		
		String expected = "I see an";
		
		String actual = DeterminerAgrHelper.checkEndsWithIndefiniteArticle(cannedText, np);
		
		assertEquals(expected, actual);
		
	}
	
	@Test
	public void testCheckEndsWithIndefiniteArticle2() {
		
		String cannedText = "I see a";
		
		String np = "cow";
		
		String expected = "I see a";
		
		String actual = DeterminerAgrHelper.checkEndsWithIndefiniteArticle(cannedText, np);
		
		assertEquals(expected, actual);
		
	}
	
	@Test
	public void testCheckEndsWithIndefiniteArticle3() {
		
		String cannedText = "I see an";
		
		String np = "cow";
		
		// Does not handle "an" -> "a"
		String expected = "I see an";
		
		String actual = DeterminerAgrHelper.checkEndsWithIndefiniteArticle(cannedText, np);
		
		assertEquals(expected, actual);
		
	}
	
	
	

}
