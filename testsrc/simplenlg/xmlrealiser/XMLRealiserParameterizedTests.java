package simplenlg.xmlrealiser;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import simplenlg.xmlrealiser.XMLRealiser.LexiconType;
import simplenlg.xmlrealiser.wrapper.DocumentRealisation;
import simplenlg.xmlrealiser.wrapper.RecordSet;
import simplenlg.xmlrealiser.wrapper.XmlDocumentElement;

@RunWith(Parameterized.class)
public class XMLRealiserParameterizedTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String lexDB = "res/NIHLexicon/lexAccess2011.data";
		LexiconType lexType = LexiconType.NIHDB; // Some tests require this.
		XMLRealiser.setLexicon(lexType, lexDB);
	}

	XmlDocumentElement testElement;
	String expectedResult;
	String failMessage;
	
	public XMLRealiserParameterizedTests(XmlDocumentElement test, String expected, String message ) {
		testElement = test;
		expectedResult = expected;
		failMessage = message;
	}
	
	@Parameters
	public static Collection<Object[]> getTest()
	{
		Collection<Object[]> tests = new ArrayList<Object[]>();
		
		tests.addAll(testRecordings(clauseTest));
		tests.addAll(testRecordings(AppositiveTest));
		tests.addAll(testRecordings(aProblemTest));
		tests.addAll(testRecordings(CoordPhraseNegationTest));
		tests.addAll(testRecordings(CoordPhraseTest));
		tests.addAll(testRecordings(docExamplesTest));
		tests.addAll(testRecordings(lexicalVarTest));
		tests.addAll(testRecordings(npTest));
		tests.addAll(testRecordings(singleTest));
		tests.addAll(testRecordings(someTest));
	
		return tests;
	}
	
	@Test
	public void performTest() {
		String realisation = null;
		try {
			realisation = XMLRealiser.realise(testElement);
		} catch (XMLRealiserException e) {
			fail("Error processing XmlDocumentElement");
		}

		assertEquals(failMessage, expectedResult, realisation.trim());
	}

	static Collection<Object[]> testRecordings(String recording) {
		Collection<Object[]> tests = new ArrayList<Object[]>();
		Object testElement[];
		RecordSet records = null;
		StringReader reader = new StringReader(recording);
		try {
			records = XMLRealiser.getRecording(reader);
		} catch (XMLRealiserException e) {
			fail("Error parsing test xml: " + " " + e.getMessage());
		} finally {
			reader.close();
		}
		
		for (DocumentRealisation test : records.getRecord()) {
			testElement = new Object[3];
			testElement[0] = test.getDocument();
			testElement[1] = test.getRealisation().trim();
			testElement[2] = String.format("Test :<%s>", test.getName());
			tests.add(testElement);
		}
		
		return tests;
	}
	
	static String AppositiveTest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<NLGSpec xmlns=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"  <Recording name=\"Clause tests\">\r\n" + 
			"    <Record name=\"Negation on VP\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" PASSIVE=\"true\">\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <postMod xsi:type=\"NPPhraseSpec\" appositive=\"true\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>D701000000992</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>the</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>angioplasty balloon catheter</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>deploy</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>An angioplasty balloon catheter, the D701000000992 was deployed.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"  </Recording>\r\n" + 
			"</NLGSpec>\r\n" + 
			"";

	static String aProblemTest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NLGSpec xmlns=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"  <Recording name=\"\">\r\n" + 
			"    <Record name=\"\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"ADVERB\">\r\n" + 
			"              <base>there</base>\r\n" + 
			"            </head>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"CoordinatedPhraseElement\" conj=\",\">\r\n" + 
			"                <coord xsi:type=\"AdjPhraseSpec\">\r\n" + 
			"                  <head cat=\"ADJECTIVE\">\r\n" + 
			"                    <base>eccentric</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </coord>\r\n" + 
			"                <coord xsi:type=\"AdjPhraseSpec\">\r\n" + 
			"                  <head cat=\"ADJECTIVE\">\r\n" + 
			"                    <base>tubular</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </coord>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>stenosis</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>third obtuse marginal branch</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>the</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"PREPOSITION\">\r\n" + 
			"                <base>in</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>be</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"ADVERB\">\r\n" + 
			"              <base>there</base>\r\n" + 
			"            </head>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>80 %</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>stenosis</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"StringElement\">\r\n" + 
			"                <val>an</val>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <preMod xsi:type=\"AdjPhraseSpec\">\r\n" + 
			"                  <head cat=\"ADJECTIVE\">\r\n" + 
			"                    <base>proximal</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </preMod>\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>right coronary artery</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>the</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"PREPOSITION\">\r\n" + 
			"                <base>in</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>be</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>There is an eccentric, tubular stenosis in the third obtuse marginal branch.There is an 80 % stenosis in the proximal right coronary artery.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"  </Recording>\r\n" + 
			"</NLGSpec>\r\n" + 
			"";

	static String clauseTest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NLGSpec xmlns=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"  <Recording name=\"Clause tests\">\r\n" + 
			"    <Record name=\"Negation on VP\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" NEGATED=\"true\" TENSE=\"PAST\">\r\n" + 
			"            <postMod xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>table</base>\r\n" + 
			"                  </head>\r\n" + 
			"                  <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                    <base>a</base>\r\n" + 
			"                  </spec>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>upon</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>etherise</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\" var=\"IRREGULAR\">\r\n" + 
			"              <base>lie</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The patient did not lie etherised upon a table.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Negation on S\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" NEGATED=\"true\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <postMod xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>table</base>\r\n" + 
			"                  </head>\r\n" + 
			"                  <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                    <base>a</base>\r\n" + 
			"                  </spec>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>upon</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>etherise</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\" var=\"IRREGULAR\">\r\n" + 
			"              <base>lie</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The patient did not lie etherised upon a table.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"S with WordElement front modifier\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"          <frontMod xsi:type=\"WordElement\" cat=\"ADVERB\">\r\n" + 
			"            <base>however</base>\r\n" + 
			"          </frontMod>\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <postMod xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>table</base>\r\n" + 
			"                  </head>\r\n" + 
			"                  <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                    <base>a</base>\r\n" + 
			"                  </spec>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>upon</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>etherise</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\" var=\"IRREGULAR\">\r\n" + 
			"              <base>lie</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>However the patient lay etherised upon a table.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"S with WordElement complement and postmod\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>man</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"WordElement\" cat=\"NOUN\">\r\n" + 
			"              <base>bill</base>\r\n" + 
			"            </compl>\r\n" + 
			"            <postMod xsi:type=\"WordElement\" cat=\"ADVERB\">\r\n" + 
			"              <base>slowly</base>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>kill</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The man killed bill slowly.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Form test 1: S + infinitive\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" FORM=\"INFINITIVE\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>man</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"WordElement\" cat=\"NOUN\">\r\n" + 
			"              <base>bill</base>\r\n" + 
			"            </compl>\r\n" + 
			"            <postMod xsi:type=\"WordElement\" cat=\"ADVERB\">\r\n" + 
			"              <base>slowly</base>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>kill</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>To kill bill slowly.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Form test 2: S + GERUND\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" FORM=\"GERUND\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>man</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"WordElement\" cat=\"NOUN\">\r\n" + 
			"              <base>bill</base>\r\n" + 
			"            </compl>\r\n" + 
			"            <postMod xsi:type=\"WordElement\" cat=\"ADVERB\">\r\n" + 
			"              <base>slowly</base>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>kill</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The man's killing bill slowly.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Form test 2: S + IMPERATIVE\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" FORM=\"IMPERATIVE\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>man</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"WordElement\" cat=\"NOUN\">\r\n" + 
			"              <base>bill</base>\r\n" + 
			"            </compl>\r\n" + 
			"            <postMod xsi:type=\"WordElement\" cat=\"ADVERB\">\r\n" + 
			"              <base>slowly</base>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>kill</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>Kill bill slowly.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Subordinate clause complement\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>man</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"SPhraseSpec\" PROGRESSIVE=\"true\" TENSE=\"PAST\">\r\n" + 
			"              <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>child</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>the</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </subj>\r\n" + 
			"              <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"WordElement\" cat=\"ADJECTIVE\">\r\n" + 
			"                  <base>difficult</base>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"VERB\">\r\n" + 
			"                  <base>be</base>\r\n" + 
			"                </head>\r\n" + 
			"              </vp>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>said</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The man said that the child was being difficult.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Subordinate clause subject\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"SPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"            <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>child</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </subj>\r\n" + 
			"            <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"              <compl xsi:type=\"WordElement\" cat=\"ADJECTIVE\">\r\n" + 
			"                <base>difficult</base>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>be</base>\r\n" + 
			"              </head>\r\n" + 
			"            </vp>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>man</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>upset</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The child's being difficult upset the man.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Passive Sentence\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" PASSIVE=\"true\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>child</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" PASSIVE=\"true\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>man</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>upset</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The man was upset by the child.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Modal\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" MODAL=\"should\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"SPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"            <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>child</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </subj>\r\n" + 
			"            <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"              <compl xsi:type=\"WordElement\" cat=\"ADJECTIVE\">\r\n" + 
			"                <base>difficult</base>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>be</base>\r\n" + 
			"              </head>\r\n" + 
			"            </vp>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>man</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>upset</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The child's being difficult should have upset the man.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Modal+Passive\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" MODAL=\"may\" PASSIVE=\"true\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>child</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>man</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>upset</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The man may have been upset by the child.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Multiple complements\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>child</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\" discourseFunction=\"INDIRECT_OBJECT\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>man</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\" discourseFunction=\"OBJECT\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>flower</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>give</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The child gave the man a flower.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Multiple complements -- inverted linear order\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>child</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\" discourseFunction=\"OBJECT\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>flower</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\" discourseFunction=\"INDIRECT_OBJECT\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>man</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>give</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The child gave the man a flower.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Interrogative WH Object\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" INTERROGATIVE_TYPE=\"WHAT_OBJECT\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>dog</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\" discourseFunction=\"OBJECT\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>man</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>upset</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>What did the dog upset?\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Interrogative WH Subject\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" INTERROGATIVE_TYPE=\"WHAT_SUBJECT\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>dog</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\" discourseFunction=\"OBJECT\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>man</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>upset</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>What upset the man?\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Interrogative WH Subject\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" INTERROGATIVE_TYPE=\"WHO_SUBJECT\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>dog</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\" discourseFunction=\"OBJECT\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>man</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>upset</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>Who upset the man?\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Incorrect predicative passive\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>transfusion of whole blood</base>\r\n" + 
			"            </head>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" PASSIVE=\"true\" TENSE=\"PRESENT\">\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>indicate</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>Is indicated by transfusion of whole blood.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Correct predicative passive\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>transfusion of whole blood</base>\r\n" + 
			"            </head>\r\n" + 
			"          </compl>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" PASSIVE=\"true\" TENSE=\"PRESENT\">\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>indicate</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>Transfusion of whole blood is indicated.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Coordinate complement passive\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <compl xsi:type=\"CoordinatedPhraseElement\" conj=\"and\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>cardiovascular therapy</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>endomyocardial biopsy</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"          </compl>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" PASSIVE=\"true\" TENSE=\"PRESENT\">\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>recommend</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>Cardiovascular therapy and endomyocardial biopsy are recommended.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record>\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"          <frontMod xsi:type=\"AdvPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <compl xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>procedure</base>\r\n" + 
			"                  </head>\r\n" + 
			"                  <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                    <base>the</base>\r\n" + 
			"                  </spec>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>of</base>\r\n" + 
			"                </head>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>result</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"ADVERB\">\r\n" + 
			"              <base>as</base>\r\n" + 
			"            </head>\r\n" + 
			"          </frontMod>\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"CoordinatedPhraseElement\" conj=\"and\">\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>adverse contrast media reaction</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>a</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>have</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>decreased platelet count</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>a</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>have</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>cardiogenic shock</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>into</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>go</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <preMod xsi:type=\"AdjPhraseSpec\">\r\n" + 
			"                  <head cat=\"ADJECTIVE\">\r\n" + 
			"                    <base>cardiac</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </preMod>\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>tamponade</base>\r\n" + 
			"                </head>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>have</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>vascular complication requiring treatment</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>a</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>have</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>hemorrhagic cerebral infarction</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>a</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>have</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>renal failure</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>into</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>go</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>myocardial infarction</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>a</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>have</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>congestive heart failure</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>into</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>go</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" PASSIVE=\"true\" TENSE=\"PRESENT\">\r\n" + 
			"          <compl xsi:type=\"CoordinatedPhraseElement\" conj=\"and\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>emergency percutaneous coronary intervention</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>renal dialysis</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>transfusion of whole blood</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"          </compl>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>indicate</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>As a result of the procedure the patient had an adverse contrast media reaction, had a decreased platelet count, went into cardiogenic shock, had cardiac tamponade, had a vascular complication requiring treatment, had a hemorrhagic cerebral infarction, went into renal failure, had a myocardial infarction and went into congestive heart failure. Emergency percutaneous coronary intervention, renal dialysis and transfusion of whole blood are indicated.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"non-restrictive modifier\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>doctor</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" PASSIVE=\"true\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <postMod xsi:type=\"NPPhraseSpec\" appositive=\"true\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>Howell's Roto-Rootor</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>a</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>device</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>deploy</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>A device, a Howell's Roto-Rootor was deployed by the doctor.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"non-restrictive modifier\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>the doctor on this shift</base>\r\n" + 
			"            </head>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" PASSIVE=\"true\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <postMod xsi:type=\"NPPhraseSpec\" appositive=\"true\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>Howell's Roto-Rootor</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>a</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>device</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>deploy</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>A device, a Howell's Roto-Rootor was deployed by the doctor on this shift.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"  </Recording>\r\n" + 
			"</NLGSpec>\r\n" + 
			"";
	
	static String CoordPhraseNegationTest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NLGSpec xmlns=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"  <Recording name=\"CoordinatedPhraseProperties\">\r\n" + 
			"    <Record name=\"NegationOverride\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"CoordinatedPhraseElement\" conj=\"and\" cat=\"VERB_PHRASE\">\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" NEGATED=\"true\" PROGRESSIVE=\"true\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>hypertension</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>from</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>suffer</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" TENSE=\"PRESENT\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>normal left heart hemodynamics</base>\r\n" + 
			"                </head>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>have</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The patient suffers from hypertension and has normal left heart hemodynamics.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"       <Record name=\"NegationAppied\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"CoordinatedPhraseElement\" conj=\"and\" cat=\"VERB_PHRASE\" NEGATED=\"true\">\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" PROGRESSIVE=\"true\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>hypertension</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>from</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>suffer</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" TENSE=\"PRESENT\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>normal left heart hemodynamics</base>\r\n" + 
			"                </head>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>have</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The patient does not suffer from hypertension and does not have normal left heart hemodynamics.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"  </Recording>\r\n" + 
			"</NLGSpec>\r\n" + 
			"";
	
	static String CoordPhraseTest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NLGSpec xmlns=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"  <Recording name=\"\">\r\n" + 
			"    <Record name=\"\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" NEGATED=\"true\" PROGRESSIVE=\"true\">\r\n" + 
			"            <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>hypertension</base>\r\n" + 
			"                </head>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"PREPOSITION\">\r\n" + 
			"                <base>from</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>suffer</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" TENSE=\"PRESENT\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>normal left heart hemodynamics</base>\r\n" + 
			"              </head>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>have</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"CoordinatedPhraseElement\" conj=\"and\" cat=\"VERB_PHRASE\">\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" NEGATED=\"true\" PROGRESSIVE=\"true\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>hypertension</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>from</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>suffer</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\" TENSE=\"PRESENT\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>normal left heart hemodynamics</base>\r\n" + 
			"                </head>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>have</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"CoordinatedPhraseElement\" conj=\"and\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>Bob</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>Sally</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" PROGRESSIVE=\"false\" TENSE=\"PRESENT\">\r\n" + 
			"            <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>store</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>the</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"PREPOSITION\">\r\n" + 
			"                <base>to</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>go</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"CoordinatedPhraseElement\" conj=\"and\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>Bob</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>Sally</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"CoordinatedPhraseElement\" conj=\"and\" TENSE=\"PAST\">\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>store</base>\r\n" + 
			"                  </head>\r\n" + 
			"                  <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                    <base>the</base>\r\n" + 
			"                  </spec>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>to</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>go</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"VPPhraseSpec\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>ice cream</base>\r\n" + 
			"                </head>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>buy</base>\r\n" + 
			"              </head>\r\n" + 
			"            </coord>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The patient is not suffering from hypertension. The patient has normal left heart hemodynamics. The patient suffers from hypertension and has normal left heart hemodynamics. Bob and Sally go to the store. Bob and Sally went to the store and bought ice cream.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"  </Recording>\r\n" + 
			"</NLGSpec>\r\n" + 
			"";
	
	static String docExamplesTest = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" + 
			"<NLGSpec xmlns=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\"\r\n" + 
			"	xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"	<Recording name=\"cch examples\">\r\n" + 
			"		<Record name=\"doc example 1\">\r\n" + 
			"			<Document>\r\n" + 
			"				<child xsi:type=\"SPhraseSpec\">\r\n" + 
			"					<subj xsi:type=\"VPPhraseSpec\" FORM=\"PRESENT_PARTICIPLE\">\r\n" + 
			"						<head cat=\"VERB\">\r\n" + 
			"							<base>refactor</base>\r\n" + 
			"						</head>\r\n" + 
			"					</subj>\r\n" + 
			"					<vp xsi:type=\"VPPhraseSpec\" TENSE=\"PRESENT\" >\r\n" + 
			"						<head cat=\"VERB\">\r\n" + 
			"							<base>be</base>\r\n" + 
			"						</head>\r\n" + 
			"						<compl xsi:type=\"VPPhraseSpec\" FORM=\"PAST_PARTICIPLE\">\r\n" + 
			"							<head cat=\"VERB\">\r\n" + 
			"								<base>need</base>\r\n" + 
			"							</head>\r\n" + 
			"						</compl>\r\n" + 
			"\r\n" + 
			"					</vp>\r\n" + 
			"				</child>\r\n" + 
			"			</Document>\r\n" + 
			"			<Realisation>Refactoring is needed.</Realisation>\r\n" + 
			"		</Record>\r\n" + 
			"		<Record name=\"doc example 2\">\r\n" + 
			"			<Document>\r\n" + 
			"				<child xsi:type=\"SPhraseSpec\">\r\n" + 
			"					<preMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"						<head cat=\"PREPOSITION\">\r\n" + 
			"							<base>as a result of</base>\r\n" + 
			"						</head>\r\n" + 
			"						<compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"							<head cat=\"NOUN\">\r\n" + 
			"								<base>procedure</base>\r\n" + 
			"							</head>\r\n" + 
			"							<spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"								<base>the</base>\r\n" + 
			"							</spec>\r\n" + 
			"						</compl>\r\n" + 
			"					</preMod>\r\n" + 
			"					<subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"						<head cat=\"NOUN\">\r\n" + 
			"							<base>patient</base>\r\n" + 
			"						</head>\r\n" + 
			"						<spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"							<base>the</base>\r\n" + 
			"						</spec>\r\n" + 
			"					</subj>\r\n" + 
			"					<vp xsi:type=\"CoordinatedPhraseElement\" conj=\"and\">\r\n" + 
			"						<coord xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"							<head cat=\"VERB\">\r\n" + 
			"								<base>have</base>\r\n" + 
			"							</head>\r\n" + 
			"							<compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"								<head cat=\"NOUN\">\r\n" + 
			"									<base>adverse contrast media reaction</base>\r\n" + 
			"								</head>\r\n" + 
			"								<spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"									<base>a</base>\r\n" + 
			"								</spec>\r\n" + 
			"							</compl>\r\n" + 
			"						</coord>\r\n" + 
			"						<coord xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"							<head cat=\"VERB\">\r\n" + 
			"								<base>have</base>\r\n" + 
			"							</head>\r\n" + 
			"							<compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"								<head cat=\"NOUN\">\r\n" + 
			"									<base>decreased platelet count</base>\r\n" + 
			"								</head>\r\n" + 
			"								<spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"									<base>a</base>\r\n" + 
			"								</spec>\r\n" + 
			"							</compl>\r\n" + 
			"						</coord>\r\n" + 
			"						<coord xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"							<head cat=\"VERB\">\r\n" + 
			"								<base>go</base>\r\n" + 
			"							</head>\r\n" + 
			"							<postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"								<head cat=\"PREPOSITION\">\r\n" + 
			"									<base>into</base>\r\n" + 
			"								</head>\r\n" + 
			"								<compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"									<head cat=\"NOUN\">\r\n" + 
			"										<base>cardiogenic shock</base>\r\n" + 
			"									</head>\r\n" + 
			"								</compl>\r\n" + 
			"							</postMod>\r\n" + 
			"						</coord>\r\n" + 
			"					</vp>\r\n" + 
			"				</child>\r\n" + 
			"			</Document>\r\n" + 
			"			<Realisation>The patient as a result of the procedure had an adverse contrast media reaction, had a decreased platelet count and went into cardiogenic shock.</Realisation>\r\n" + 
			"		</Record>\r\n" + 
			"	</Recording>\r\n" + 
			"</NLGSpec>\r\n" + 
			"";
	
	static String lexicalVarTest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NLGSpec xmlns=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"  <Recording name=\"Lexical variants\">\r\n" + 
			"    <Record name=\"InflVariantiUncount\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>right coronary artery</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\" NUMBER=\"PLURAL\">\r\n" + 
			"              <preMod xsi:type=\"AdjPhraseSpec\">\r\n" + 
			"                <head cat=\"ADJECTIVE\">\r\n" + 
			"                  <base>mild</base>\r\n" + 
			"                </head>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\" var=\"UNCOUNT\">\r\n" + 
			"                <base>calcification</base>\r\n" + 
			"              </head>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>have</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>right coronary artery</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\" NUMBER=\"PLURAL\">\r\n" + 
			"              <preMod xsi:type=\"AdjPhraseSpec\">\r\n" + 
			"                <head cat=\"ADJECTIVE\">\r\n" + 
			"                  <base>mild</base>\r\n" + 
			"                </head>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head>\r\n" + 
			"                <base>calcification</base>\r\n" + 
			"              </head>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>have</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The right coronary artery has mild calcification. The right coronary artery has mild calcifications.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Spell variant1\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"            <postMod xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>table</base>\r\n" + 
			"                  </head>\r\n" + 
			"                  <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                    <base>a</base>\r\n" + 
			"                  </spec>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>upon</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>etherise</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\" var=\"IRREGULAR\">\r\n" + 
			"              <base>lie</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The patient lay etherised upon a table.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Spell variant2\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>Adam-Stokes disease</base>\r\n" + 
			"              </head>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\" var=\"IRREGULAR\">\r\n" + 
			"              <base>has</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>Adam Stokes disease</base>\r\n" + 
			"              </head>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\" var=\"IRREGULAR\">\r\n" + 
			"              <base>has</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The patient had Adam-Stokes disease. The patient had Adam Stokes disease.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"  </Recording>\r\n" + 
			"</NLGSpec>\r\n" + 
			"";
		
	static String npTest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NLGSpec xmlns=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"  <Recording name=\"NPPhraseSpec examples\">\r\n" + 
			"    <Record name=\"Possessive Spec\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"NPPhraseSpec\">\r\n" + 
			"          <head cat=\"NOUN\">\r\n" + 
			"            <base>patient</base>\r\n" + 
			"          </head>\r\n" + 
			"          <spec xsi:type=\"NPPhraseSpec\" POSSESSIVE=\"true\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>John</base>\r\n" + 
			"            </head>\r\n" + 
			"          </spec>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>John's patient.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Determiner Spec\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"NPPhraseSpec\">\r\n" + 
			"          <head cat=\"NOUN\">\r\n" + 
			"            <base>patient</base>\r\n" + 
			"          </head>\r\n" + 
			"          <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"            <base>the</base>\r\n" + 
			"          </spec>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The patient.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Complex NP\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"NPPhraseSpec\">\r\n" + 
			"          <head cat=\"NOUN\">\r\n" + 
			"            <base>house</base>\r\n" + 
			"          </head>\r\n" + 
			"          <spec xsi:type=\"CoordinatedPhraseElement\" conj=\"and\" POSSESSIVE=\"true\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\" PRONOMINAL=\"true\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>dog</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>woman</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </spec>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>It and the woman's house.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Pronominal1\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"NPPhraseSpec\" NUMBER=\"PLURAL\" PRONOMINAL=\"true\">\r\n" + 
			"          <head cat=\"NOUN\">\r\n" + 
			"            <base>house</base>\r\n" + 
			"          </head>\r\n" + 
			"          <spec xsi:type=\"WordElement\">\r\n" + 
			"            <base>the</base>\r\n" + 
			"          </spec>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>They.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Pronominal2\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"NPPhraseSpec\" GENDER=\"FEMININE\" PRONOMINAL=\"true\">\r\n" + 
			"          <head cat=\"NOUN\">\r\n" + 
			"            <base>singer</base>\r\n" + 
			"          </head>\r\n" + 
			"          <spec xsi:type=\"WordElement\">\r\n" + 
			"            <base>the</base>\r\n" + 
			"          </spec>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>She.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Pronominal3\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"NPPhraseSpec\" GENDER=\"FEMININE\" POSSESSIVE=\"true\" PRONOMINAL=\"true\">\r\n" + 
			"          <head cat=\"NOUN\">\r\n" + 
			"            <base>singer</base>\r\n" + 
			"          </head>\r\n" + 
			"          <spec xsi:type=\"WordElement\">\r\n" + 
			"            <base>the</base>\r\n" + 
			"          </spec>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>Her.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Plural1\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"NPPhraseSpec\" NUMBER=\"PLURAL\">\r\n" + 
			"          <head cat=\"NOUN\">\r\n" + 
			"            <base>house</base>\r\n" + 
			"          </head>\r\n" + 
			"          <spec xsi:type=\"WordElement\">\r\n" + 
			"            <base>the</base>\r\n" + 
			"          </spec>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The houses.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Plural2\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"NPPhraseSpec\" NUMBER=\"PLURAL\">\r\n" + 
			"          <head cat=\"NOUN\">\r\n" + 
			"            <base>house</base>\r\n" + 
			"          </head>\r\n" + 
			"          <spec xsi:type=\"CoordinatedPhraseElement\" conj=\"and\" POSSESSIVE=\"true\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\" PRONOMINAL=\"true\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>dog</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>woman</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </spec>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>They and the women's houses.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"NP + StringElement Premodifier\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"NPPhraseSpec\">\r\n" + 
			"          <preMod xsi:type=\"StringElement\">\r\n" + 
			"            <val>difficult</val>\r\n" + 
			"          </preMod>\r\n" + 
			"          <head cat=\"NOUN\">\r\n" + 
			"            <base>patient</base>\r\n" + 
			"          </head>\r\n" + 
			"          <spec xsi:type=\"NPPhraseSpec\" POSSESSIVE=\"true\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>John</base>\r\n" + 
			"            </head>\r\n" + 
			"          </spec>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>John's difficult patient.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"NP + wordElement Premodifier\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"NPPhraseSpec\">\r\n" + 
			"          <preMod xsi:type=\"WordElement\" cat=\"ADJECTIVE\">\r\n" + 
			"            <base>difficult</base>\r\n" + 
			"          </preMod>\r\n" + 
			"          <head cat=\"NOUN\">\r\n" + 
			"            <base>patient</base>\r\n" + 
			"          </head>\r\n" + 
			"          <spec xsi:type=\"NPPhraseSpec\" POSSESSIVE=\"true\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>John</base>\r\n" + 
			"            </head>\r\n" + 
			"          </spec>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>John's difficult patient.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"NP + wordElement complement\">\r\n" + 
			"      <Document>\r\n" + 
			"        <child xsi:type=\"NPPhraseSpec\">\r\n" + 
			"          <compl xsi:type=\"WordElement\" cat=\"ADJECTIVE\">\r\n" + 
			"            <base>jack</base>\r\n" + 
			"          </compl>\r\n" + 
			"          <head cat=\"NOUN\">\r\n" + 
			"            <base>patient</base>\r\n" + 
			"          </head>\r\n" + 
			"          <spec xsi:type=\"NPPhraseSpec\" POSSESSIVE=\"true\">\r\n" + 
			"            <head cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </head>\r\n" + 
			"          </spec>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The patient jack.</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"  </Recording>\r\n" + 
			"</NLGSpec>\r\n" + 
			"";
	
	static String singleTest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NLGSpec xmlns=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"  <Recording name=\"Clause tests\">\r\n" + 
			"    <Record name=\"Interrogative WH Object\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\" INTERROGATIVE_TYPE=\"WHAT_OBJECT\" TENSE=\"PAST\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>dog</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\" discourseFunction=\"OBJECT\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>man</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>the</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>upset</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>What did the dog upset?\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"  </Recording>\r\n" + 
			"</NLGSpec>\r\n" + 
			"";
	
	static String someTest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><NLGSpec xmlns=\"http://simplenlg.googlecode.com/svn/trunk/res/xml\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"  <Recording name=\"Some Tests\">\r\n" + 
			"    <Record name=\"Hello World\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"StringElement\">\r\n" + 
			"          <val>hello, world</val>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>Hello, world.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"Lexical and spelling variants\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"            <postMod xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>table</base>\r\n" + 
			"                  </head>\r\n" + 
			"                  <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                    <base>a</base>\r\n" + 
			"                  </spec>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>upon</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>etherise</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>lie</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>patient</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"            <postMod xsi:type=\"VPPhraseSpec\" TENSE=\"PAST\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>table</base>\r\n" + 
			"                  </head>\r\n" + 
			"                  <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                    <base>a</base>\r\n" + 
			"                  </spec>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>upon</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"VERB\">\r\n" + 
			"                <base>etherise</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\" var=\"IRREGULAR\">\r\n" + 
			"              <base>lie</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The patient lied etherised upon a table. The patient lay etherised upon a table.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"aAn8Test\">\r\n" + 
			"      <Document cat=\"LIST\">\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"CoordinatedPhraseElement\" conj=\"but\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>18</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>180</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"CoordinatedPhraseElement\" conj=\"but\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>18x</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>08</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"CoordinatedPhraseElement\" conj=\"but\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>11g</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>9i</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"CoordinatedPhraseElement\" conj=\"but\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>8th</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>9th</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"CoordinatedPhraseElement\" conj=\"but\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>11th</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>1100</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"CoordinatedPhraseElement\" conj=\"but\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>11.000</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>11000</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"CoordinatedPhraseElement\" conj=\"but\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>81000</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>180,000</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"CoordinatedPhraseElement\" conj=\"but\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>81,000</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>01834</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"CoordinatedPhraseElement\" conj=\"but\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>8%</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>9%</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"CoordinatedPhraseElement\" conj=\"but\">\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>8432425</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"            <coord xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>42nd</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>thing</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </coord>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>* an 18 thing but a 180 thing\r\n" + 
			"* an 18x thing but a 08 thing\r\n" + 
			"* an 11g thing but a 9i thing\r\n" + 
			"* an 8th thing but a 9th thing\r\n" + 
			"* an 11th thing but a 1100 thing\r\n" + 
			"* an 11.000 thing but an 11000 thing\r\n" + 
			"* an 81000 thing but a 180,000 thing\r\n" + 
			"* an 81,000 thing but a 01834 thing\r\n" + 
			"* an 8% thing but a 9% thing\r\n" + 
			"* an 8432425 thing but a 42nd thing\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"punctuation problem 1\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"ADVERB\">\r\n" + 
			"              <base>there</base>\r\n" + 
			"            </head>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>80 %</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>in-stent restenosis</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>an</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <preMod xsi:type=\"AdjPhraseSpec\">\r\n" + 
			"                  <head cat=\"ADJECTIVE\">\r\n" + 
			"                    <base>proximal</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </preMod>\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>right coronary artery</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>the</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"PREPOSITION\">\r\n" + 
			"                <base>in</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>be</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>There is an 80 % in-stent restenosis in the proximal right coronary artery.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"punctuation problem 2\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"ADVERB\">\r\n" + 
			"              <base>there</base>\r\n" + 
			"            </head>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <preMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>95 %</val>\r\n" + 
			"              </preMod>\r\n" + 
			"              <preMod xsi:type=\"AdjPhraseSpec\">\r\n" + 
			"                <head cat=\"ADJECTIVE\">\r\n" + 
			"                  <base>eccentric</base>\r\n" + 
			"                </head>\r\n" + 
			"              </preMod>\r\n" + 
			"              <postMod xsi:type=\"StringElement\">\r\n" + 
			"                <val>(8 mm length)</val>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>in-stent restenosis</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"              <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                <preMod xsi:type=\"AdjPhraseSpec\">\r\n" + 
			"                  <head cat=\"ADJECTIVE\">\r\n" + 
			"                    <base>proximal</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </preMod>\r\n" + 
			"                <head cat=\"NOUN\">\r\n" + 
			"                  <base>right coronary artery</base>\r\n" + 
			"                </head>\r\n" + 
			"                <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                  <base>the</base>\r\n" + 
			"                </spec>\r\n" + 
			"              </compl>\r\n" + 
			"              <head cat=\"PREPOSITION\">\r\n" + 
			"                <base>in</base>\r\n" + 
			"              </head>\r\n" + 
			"            </postMod>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>be</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>There is a 95 %, eccentric in-stent restenosis (8 mm length) in the proximal right coronary artery.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"whitespace problem\">\r\n" + 
			"      <Document cat=\"PARAGRAPH\">\r\n" + 
			"        <child xsi:type=\"SPhraseSpec\">\r\n" + 
			"          <subj xsi:type=\"NPPhraseSpec\">\r\n" + 
			"            <head cat=\"NOUN\">\r\n" + 
			"              <base>right coronary artery</base>\r\n" + 
			"            </head>\r\n" + 
			"            <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"              <base>the</base>\r\n" + 
			"            </spec>\r\n" + 
			"          </subj>\r\n" + 
			"          <vp xsi:type=\"VPPhraseSpec\">\r\n" + 
			"            <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <postMod xsi:type=\"PPPhraseSpec\">\r\n" + 
			"                <compl xsi:type=\"NPPhraseSpec\">\r\n" + 
			"                  <head cat=\"NOUN\">\r\n" + 
			"                    <base>luminal irregularities</base>\r\n" + 
			"                  </head>\r\n" + 
			"                </compl>\r\n" + 
			"                <head cat=\"PREPOSITION\">\r\n" + 
			"                  <base>with</base>\r\n" + 
			"                </head>\r\n" + 
			"              </postMod>\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>vessel</base>\r\n" + 
			"              </head>\r\n" + 
			"              <spec xsi:type=\"WordElement\" cat=\"DETERMINER\">\r\n" + 
			"                <base>a</base>\r\n" + 
			"              </spec>\r\n" + 
			"            </compl>\r\n" + 
			"            <head cat=\"VERB\">\r\n" + 
			"              <base>be</base>\r\n" + 
			"            </head>\r\n" + 
			"          </vp>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>The right coronary artery is a vessel with luminal irregularities.\r\n" + 
			"\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"    <Record name=\"whitespace at end of list item\">\r\n" + 
			"      <Document cat=\"LIST\">\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"DocumentElement\" cat=\"SENTENCE\">\r\n" + 
			"            <child xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>normal coronary arteries</base>\r\n" + 
			"              </head>\r\n" + 
			"            </child>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"DocumentElement\" cat=\"SENTENCE\">\r\n" + 
			"            <child xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>normal left heart hemodynamics</base>\r\n" + 
			"              </head>\r\n" + 
			"            </child>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"        <child xsi:type=\"DocumentElement\" cat=\"LIST_ITEM\">\r\n" + 
			"          <child xsi:type=\"DocumentElement\" cat=\"SENTENCE\">\r\n" + 
			"            <child xsi:type=\"NPPhraseSpec\">\r\n" + 
			"              <head cat=\"NOUN\">\r\n" + 
			"                <base>normal right heart hemodynamics</base>\r\n" + 
			"              </head>\r\n" + 
			"            </child>\r\n" + 
			"          </child>\r\n" + 
			"        </child>\r\n" + 
			"      </Document>\r\n" + 
			"      <Realisation>* Normal coronary arteries.\r\n" + 
			"* Normal left heart hemodynamics.\r\n" + 
			"* Normal right heart hemodynamics.\r\n" + 
			"</Realisation>\r\n" + 
			"    </Record>\r\n" + 
			"  </Recording>\r\n" + 
			"</NLGSpec>\r\n" + 
			"";

}
