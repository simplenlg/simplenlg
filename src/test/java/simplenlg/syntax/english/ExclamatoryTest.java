package simplenlg.syntax.english;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.PhraseElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;

/**
 * {@link ExclamatoryTest} is a set of JUnit test cases that tests support for exclamatory sentences.
 *
 * @author Saad Mahamood
 */
public class ExclamatoryTest {

	private Lexicon    lexicon       = null;
	private NLGFactory phraseFactory = null;
	private Realiser   realiser      = null;

	@Before
	public void setup() {
		lexicon = Lexicon.getDefaultLexicon();
		phraseFactory = new NLGFactory(lexicon);
		realiser = new Realiser(lexicon);
	}

	/**
	 * Simple clause test case with {@link Feature#EXCLAMATORY} set to true.
	 */
	@Test
	public void simpleClauseExclamatoryTest() {

		PhraseElement clause = this.phraseFactory.createClause("Mary", "chase", "George");

		clause.setFeature(Feature.EXCLAMATORY, true);

		PhraseElement prepositionalPhrase = this.phraseFactory.createPrepositionPhrase("in", "the park");
		clause.addPostModifier(prepositionalPhrase);

		Assert.assertEquals("Mary chases George in the park!",
		                    this.realiser.realise(this.phraseFactory.createSentence(clause)).getRealisation());

	}

	/**
	 * Coordinated Phrase test case with {@link Feature#EXCLAMATORY} set to true.
	 */
	@Test
	public void coordinatedPhraseExclamatoryTest() {
		PhraseElement clause_1 = this.phraseFactory.createClause("Mary", "chase", "George");
		PhraseElement prepositionalPhrase_1 = this.phraseFactory.createPrepositionPhrase("in", "the park");
		clause_1.addPostModifier(prepositionalPhrase_1);
		clause_1.setFeature(Feature.TENSE, Tense.PAST);

		PhraseElement clause_2 = this.phraseFactory.createClause("she", "run", "him");
		PhraseElement prepositionalPhrase_2 = this.phraseFactory.createPrepositionPhrase("into", "the pond");
		clause_2.addPostModifier(prepositionalPhrase_2);
		clause_2.setFeature(Feature.TENSE, Tense.PAST);

		CoordinatedPhraseElement coord = new CoordinatedPhraseElement();
		coord.addCoordinate(clause_1);
		coord.addCoordinate(clause_2);
		coord.setConjunction("and then");

		DocumentElement sentence = this.phraseFactory.createSentence(coord);
		sentence.setFeature(Feature.EXCLAMATORY, true);

		Assert.assertEquals("Mary chased George in the park and then she ran him into the pond!",
		                    this.realiser.realise(sentence).getRealisation());
	}

	/**
	 * Test case where Interrogative and Exclamatory are set for the same sentence. Interrogative should override
	 * Exclamatory.
	 */
	@Test
	public void interrogativeExclamatoryTest() {
		PhraseElement clause = this.phraseFactory.createClause("Mary", "chase", "George");
		clause.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
		clause.setFeature(Feature.EXCLAMATORY, true);

		Assert.assertEquals("What does Mary chase?",
		                    this.realiser.realise(this.phraseFactory.createSentence(clause)).getRealisation());
	}

}
