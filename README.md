[![Build Status](https://travis-ci.org/simplenlg/simplenlg.svg?branch=master)](https://travis-ci.org/simplenlg/simplenlg)


SimpleNLG
=========

SimpleNLG is a simple Java API designed to facilitate the generation of Natural Language. It was originally developed by Ehud Reiter, Professor at the [University of Aberdeen's Department of Computing Science](http://www.abdn.ac.uk/ncs/departments/computing-science/index.php) and co-founder of [Arria NLG](https://www.arria.com). The [discussion list for SimpleNLG is on Google Groups](https://groups.google.com/forum/#!forum/simplenlg).

SimpleNLG is intended to function as a "[realisation engine](http://en.wikipedia.org/wiki/Realization_(linguistics))" for [Natural Language Generation](http://en.wikipedia.org/wiki/Natural_language_generation) architectures, and has been used successfully in a number of projects, both academic and commercial. It handles the following:

* Lexicon/morphology system: The default lexicon computes inflected forms (morphological realisation). We believe this has fair coverage. Better coverage can be obtained by using the [NIH Specialist Lexicon](http://lexsrv3.nlm.nih.gov/LexSysGroup/Projects/lexicon/current/web/) (which is supported by SimpleNLG).
* Realiser: Generates texts from a syntactic form. Grammatical coverage is limited compared to tools such as [KPML](http://www.fb10.uni-bremen.de/anglistik/langpro/kpml/README.html) and [FUF/SURGE](http://www.cs.bgu.ac.il/surge/index.html), but we believe it is adequate for many NLG tasks.
* Microplanning: Currently just simple aggregation, hopefully will grow over time.

Current release (English)
-------------------------
The current release of SimpleNLG is V4.4.8 ([API](https://cdn.rawgit.com/simplenlg/simplenlg/master/docs/javadoc/index.html)). The "official" version of SimpleNLG only produces texts in English. However, versions for other languages are under development, see the Papers and Publications page and [SimpleNLG discussion list](https://groups.google.com/forum/#!forum/simplenlg) for details.

Please note that earlier versions of SimpleNLG have different licensing, in particular versions before V4.0 cannot be used commercially.

Getting started
---------------
For information on how to use SimpleNLG, please see the [tutorial](https://github.com/simplenlg/simplenlg/wiki/Section-0-–-SimpleNLG-Tutorial) and [API](https://cdn.rawgit.com/simplenlg/simplenlg/master/docs/javadoc/index.html).

If you have a technical question about using SimpleNLG, please check the [SimpleNLG discussion list](https://groups.google.com/forum/#!forum/simplenlg).

If you wish to be informed about SimpleNLG updates and events, please subscribe to the [SimpleNLG announcement list](https://groups.google.com/forum/#!forum/simplenlg-announce).

If you wish to cite SimpleNLG in an academic publication, please cite the following paper:

* A Gatt and E Reiter (2009). [SimpleNLG: A realisation engine for practical applications](http://aclweb.org/anthology/W/W09/W09-0613.pdf). Proceedings of ENLG-2009

If you have other questions about SimpleNLG, please contact Professor Ehud Reiter via email: [ehud.reiter@arria.com](mailto:ehud.reiter@arria.com)

SimpleNLG for other languages
-----------------------------
A version of SimpleNLG has now been developed for *French* by *Pierre-Luc Vaudry* at the Université de Montreal. This is a development based on the version 4 architecture. It is current being distributed separately from [this page](http://www-etud.iro.umontreal.ca/~vaudrypl/snlgbil/snlgEnFr_english.html).

*Marcel Bollman* has been working on an adaptation of SimpleNLG version 3 to German. This is available from [this page](http://www.linguistics.rub.de/~bollmann/simplenlg-ger.html). Please remember that SimpleNLG version 3 is not licensed for commercial use

SimpleNLG License 
-----------------------------
SimpleNLG is licensed under the terms and conditions of the [Mozilla Public Licence (MPL)](http://www.mozilla.org/MPL/).
