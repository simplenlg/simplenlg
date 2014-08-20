/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */
package simplenlg.format.english;

import java.util.ArrayList;
import java.util.List;

import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentCategory;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.ElementCategory;
import simplenlg.framework.ListElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGModule;
import simplenlg.framework.StringElement;

/**
 * <p>
 * This processing module adds some simple plain text formatting to the
 * SimpleNLG output. This includes the following:
 * <ul>
 * <li>Adding the document title to the beginning of the text.</li>
 * <li>Adding section titles in the relevant places.</li>
 * <li>Adding appropriate new line breaks for ease-of-reading.</li>
 * <li>Adding list items with ' * '.</li>
 * <li>Adding numbers for enumerated lists (e.g., "1.1 - ", "1.2 - ", etc.)</li>
 * </ul>
 * </p>
 * 
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 * 
 */
public class TextFormatter extends NLGModule {

    static private NumberedPrefix numberedPrefix = new NumberedPrefix();

    @Override
	public void initialise() {
		// Do nothing
	}

	@Override
	public NLGElement realise(NLGElement element) {
		NLGElement realisedComponent = null;
		StringBuffer realisation = new StringBuffer();
		
		if (element != null) {
			ElementCategory category = element.getCategory();
			List<NLGElement> components = element.getChildren();

			//NB: The order of the if-statements below is important!
			
			// check if this is a canned text first
			if (element instanceof StringElement) {
				realisation.append(element.getRealisation());

			} else if (category instanceof DocumentCategory) {
				// && element instanceof DocumentElement
				String title = element instanceof DocumentElement ? ((DocumentElement) element)
						.getTitle()
						: null;
				// String title = ((DocumentElement) element).getTitle();
						
				switch ((DocumentCategory) category) {

				case DOCUMENT:
					appendTitle(realisation, title, 2);
					realiseSubComponents(realisation, components);
					break;
				case SECTION:
					appendTitle(realisation, title, 1);
					realiseSubComponents(realisation, components);
					break;
				case LIST:
					realiseSubComponents(realisation, components);
					break;

                case ENUMERATED_LIST:
                    numberedPrefix.upALevel();
                    if (title != null) {
                        realisation.append(title).append('\n');
                    }

                    if (null != components && 0 < components.size()) {

                        realisedComponent = realise(components.get(0));
                        if (realisedComponent != null) {
                            realisation.append(realisedComponent.getRealisation());
                        }
                        for (int i = 1; i < components.size(); i++) {
                            if (realisedComponent != null && !realisedComponent.getRealisation().endsWith("\n")) {
                                realisation.append(' ');
                            }
                            if(components.get(i).getParent().getCategory() == DocumentCategory.ENUMERATED_LIST) {
                                numberedPrefix.increment();
                            }
                            realisedComponent = realise(components.get(i));
                            if (realisedComponent != null) {
                                realisation.append(realisedComponent.getRealisation());
                            }
                        }
                    }

                    numberedPrefix.downALevel();
                    break;

				case PARAGRAPH:
					if (null != components && 0 < components.size()) {
						realisedComponent = realise(components.get(0));
						if (realisedComponent != null) {
							realisation.append(realisedComponent.getRealisation());
						}
						for (int i = 1; i < components.size(); i++) {
							if (realisedComponent != null) {
								realisation.append(' ');
							}
							realisedComponent = realise(components.get(i));
							if (realisedComponent != null) {
								realisation.append(realisedComponent.getRealisation());
							}
						}
					}
					realisation.append("\n\n");
					break;

				case SENTENCE:
					realisation.append(element.getRealisation());
					break;

				case LIST_ITEM:
                    if(element.getParent() != null) {
                        if(element.getParent().getCategory() == DocumentCategory.LIST) {
                            realisation.append(" * ");
                        } else if(element.getParent().getCategory() == DocumentCategory.ENUMERATED_LIST) {
                            realisation.append(numberedPrefix.getPrefix() + " - ");
                        }
                    }

					for (NLGElement eachComponent : components) {
						realisedComponent = realise(eachComponent);
						
						if (realisedComponent != null) {
							realisation.append(realisedComponent
									.getRealisation());	
							
							if(components.indexOf(eachComponent) < components.size()-1) {
								realisation.append(' ');
							}
						}
					}
					//finally, append newline
					realisation.append("\n");
					break;
				}

				// also need to check if element is a ListElement (items can
				// have embedded lists post-orthography) or a coordinate
			} else if (element instanceof ListElement || element instanceof CoordinatedPhraseElement) {
				for (NLGElement eachComponent : components) {
					realisedComponent = realise(eachComponent);
					if (realisedComponent != null) {
						realisation.append(realisedComponent.getRealisation()).append(' ');
					}
				}				
			} 
		}
		
		return new StringElement(realisation.toString());
	}

	/**
	 * realiseSubComponents -- Realises subcomponents iteratively.
	 * @param realisation -- The current realisation StringBuffer.
	 * @param components -- The components to realise.
	 */
	private void realiseSubComponents(StringBuffer realisation,
			List<NLGElement> components) {
		NLGElement realisedComponent;
		for (NLGElement eachComponent : components) {
			realisedComponent = realise(eachComponent);
			if (realisedComponent != null) {
				realisation.append(realisedComponent
						.getRealisation());
			}
		}
	}
	
	/**
	 * appendTitle -- Appends document or section title to the realised document.
	 * @param realisation -- The current realisation StringBuffer.
	 * @param title -- The title to append.
	 * @param numberOfLineBreaksAfterTitle -- Number of line breaks to append.
	 */
	private void appendTitle(StringBuffer realisation, String title, int numberOfLineBreaksAfterTitle) {
        if (title != null && !title.isEmpty()) {
            realisation.append(title);
            for(int i = 0; i < numberOfLineBreaksAfterTitle; i++) {
                realisation.append("\n");
            }
        }
    }

	@Override
	public List<NLGElement> realise(List<NLGElement> elements) {
		List<NLGElement> realisedList = new ArrayList<NLGElement>();

		if (elements != null) {
			for (NLGElement eachElement : elements) {
				realisedList.add(realise(eachElement));
			}
		}
		return realisedList;
	}
}
