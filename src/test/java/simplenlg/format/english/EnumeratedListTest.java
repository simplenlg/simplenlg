/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * https://www.mozilla.org/en-US/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is "Simplenlg".
 *
 * The Initial Developer of the Original Code is Ehud Reiter, Albert Gatt and Dave Westwater.
 * Portions created by Ehud Reiter, Albert Gatt and Dave Westwater are Copyright (C) 2010-11 The University of Aberdeen. All Rights Reserved.
 *
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater, Roman Kutlak, Margaret Mitchell, and Saad Mahamood.
 */
package simplenlg.format.english;

import junit.framework.Assert;
import org.junit.Test;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;

/**
 * This tests that two sentences are realised as a list.
 *
 * @author Rodrigo de Oliveira - Data2Text Ltd
 */
public class EnumeratedListTest {

	@Test
	public void bulletList() {
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);
		realiser.setFormatter(new HTMLFormatter());
		DocumentElement document = nlgFactory.createDocument("Document");
		DocumentElement paragraph = nlgFactory.createParagraph();
		DocumentElement list = nlgFactory.createList();
		DocumentElement item1 = nlgFactory.createListItem();
		DocumentElement item2 = nlgFactory.createListItem();
		// NB: a list item employs orthographical operations only until sentence level;
		// nest clauses within a sentence to generate more than 1 clause per list item. 
		DocumentElement sentence1 = nlgFactory.createSentence("this", "be", "the first sentence");
		DocumentElement sentence2 = nlgFactory.createSentence("this", "be", "the second sentence");
		item1.addComponent(sentence1);
		item2.addComponent(sentence2);
		list.addComponent(item1);
		list.addComponent(item2);
		paragraph.addComponent(list);
		document.addComponent(paragraph);
		String expectedOutput = "<h1>Document</h1>" + "<p>" + "<ul>" + "<li>This is the first sentence.</li>"
		                        + "<li>This is the second sentence.</li>" + "</ul>" + "</p>";

		String realisedOutput = realiser.realise(document).getRealisation();
		//		System.out.println(expectedOutput);
		//		System.out.println(realisedOutput);
		Assert.assertEquals(expectedOutput, realisedOutput);
	}

	@Test
	public void enumeratedList() {
		Lexicon lexicon = Lexicon.getDefaultLexicon();
		NLGFactory nlgFactory = new NLGFactory(lexicon);
		Realiser realiser = new Realiser(lexicon);
		realiser.setFormatter(new HTMLFormatter());
		DocumentElement document = nlgFactory.createDocument("Document");
		DocumentElement paragraph = nlgFactory.createParagraph();
		DocumentElement list = nlgFactory.createEnumeratedList();
		DocumentElement item1 = nlgFactory.createListItem();
		DocumentElement item2 = nlgFactory.createListItem();
		// NB: a list item employs orthographical operations only until sentence level;
		// nest clauses within a sentence to generate more than 1 clause per list item. 
		DocumentElement sentence1 = nlgFactory.createSentence("this", "be", "the first sentence");
		DocumentElement sentence2 = nlgFactory.createSentence("this", "be", "the second sentence");
		item1.addComponent(sentence1);
		item2.addComponent(sentence2);
		list.addComponent(item1);
		list.addComponent(item2);
		paragraph.addComponent(list);
		document.addComponent(paragraph);
		String expectedOutput = "<h1>Document</h1>" + "<p>" + "<ol>" + "<li>This is the first sentence.</li>"
		                        + "<li>This is the second sentence.</li>" + "</ol>" + "</p>";

		String realisedOutput = realiser.realise(document).getRealisation();
		//		System.out.println(expectedOutput);
		//		System.out.println(realisedOutput);
		Assert.assertEquals(expectedOutput, realisedOutput);
	}

}