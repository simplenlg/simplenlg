[![Build Status](https://travis-ci.org/simplenlg/simplenlg.svg?branch=master)](https://travis-ci.org/simplenlg/simplenlg)
[![SourceSpy Dashboard](https://sourcespy.com/shield.svg)](https://sourcespy.com/github/simplenlgsimplenlg/)


SimpleNLG
=========

SimpleNLG is a simple Java API designed to facilitate the generation of Natural Language. It was originally developed by Ehud Reiter, Professor at the [University of Aberdeen's Department of Computing Science](http://www.abdn.ac.uk/ncs/departments/computing-science/index.php) and co-founder of [Arria NLG](https://www.arria.com). The [discussion list for SimpleNLG is on Google Groups](https://groups.google.com/forum/#!forum/simplenlg).

SimpleNLG is intended to function as a "[realisation engine](http://en.wikipedia.org/wiki/Realization_(linguistics))" for [Natural Language Generation](http://en.wikipedia.org/wiki/Natural_language_generation) architectures, and has been used successfully in a number of projects, both academic and commercial. It handles the following:

* Lexicon/morphology system: The default lexicon computes inflected forms (morphological realisation). We believe this has fair coverage. Better coverage can be obtained by using the [NIH Specialist Lexicon](http://lexsrv3.nlm.nih.gov/LexSysGroup/Projects/lexicon/current/web/) (which is supported by SimpleNLG).
* Realiser: Generates texts from a syntactic form. Grammatical coverage is limited compared to tools such as [KPML](http://www.fb10.uni-bremen.de/anglistik/langpro/kpml/README.html) and [FUF/SURGE](http://www.cs.bgu.ac.il/surge/index.html), but we believe it is adequate for many NLG tasks.
* Microplanning: Currently just simple aggregation, hopefully will grow over time.

Current release (English)
-------------------------
The current release of SimpleNLG is V4.5.0 ([API](https://cdn.rawgit.com/simplenlg/simplenlg/master/docs/javadoc/index.html)). The "official" version of SimpleNLG only produces texts in English. However, versions for other languages are under development, see the Papers and Publications page and [SimpleNLG discussion list](https://groups.google.com/forum/#!forum/simplenlg) for details.

Please note that earlier versions of SimpleNLG have different licensing, in particular versions before V4.0 cannot be used commercially.

Getting started
---------------
For information on how to use SimpleNLG, please see the [tutorial](https://github.com/simplenlg/simplenlg/wiki/Section-0-–-SimpleNLG-Tutorial) and [API](https://cdn.rawgit.com/simplenlg/simplenlg/master/docs/javadoc/index.html).

The quickest way to use SimpleNLG is to add it as a dependency to your maven `pom.xml` file:

```
<dependency>
  <groupId>uk.ac.abdn</groupId>
  <artifactId>SimpleNLG</artifactId>
  <version>4.5.0</version>
</dependency>
```

If you have a technical question about using SimpleNLG, please check the [SimpleNLG discussion list](https://groups.google.com/forum/#!forum/simplenlg). Or if you wish to be informed about SimpleNLG updates and events, please subscribe to the [SimpleNLG announcement list](https://groups.google.com/forum/#!forum/simplenlg-announce).

If you wish to cite SimpleNLG in an academic publication, please cite the following paper:

* A Gatt and E Reiter (2009). [SimpleNLG: A realisation engine for practical applications](http://aclweb.org/anthology/W/W09/W09-0613.pdf). Proceedings of ENLG-2009

The [sourcespy dashboard](https://sourcespy.com/github/simplenlgsimplenlg/) provides a high level overview of the repository including [class diagram](https://sourcespy.com/github/simplenlgsimplenlg/xx-omodel-.html), Ant [tasks and dependencies](https://sourcespy.com/github/simplenlgsimplenlg/xx-otasks-.html), [module dependencies](https://sourcespy.com/github/simplenlgsimplenlg/xx-omodulesc-.html), [external libraries](https://sourcespy.com/github/simplenlgsimplenlg/xx-ojavalibs-.html), and other components of the system.

If you have other questions about SimpleNLG, please contact Professor Ehud Reiter via email: [ehud.reiter@arria.com](mailto:ehud.reiter@arria.com).

SimpleNLG for other languages
-----------------------------

French 🇫🇷: A version of SimpleNLG for *French* of SimpleNLG4 is avaliable from [this page](http://www-etud.iro.umontreal.ca/~vaudrypl/snlgbil/snlgEnFr_english.html).

Italian 🇮🇹: The *Italian* version of SimpleNLG 4 is avaliable from [this page](https://github.com/alexmazzei/SimpleNLG-IT).

Spanish 🇪🇸: The *Spanish* version of SimpleNLG 4 is avaliable from [this page](https://github.com/citiususc/SimpleNLG-ES). 

Brazilian Portguese 🇧🇷: The *Brazilian Portguese* version of SimpleNLG 4 is avaliable from [this page](https://github.com/rdeoliveira/simplenlg-bp).

Dutch 🇳🇱: The *Dutch* version of SimpleNLG 4 is avaliable from [this page](https://github.com/rfdj/SimpleNLG-NL).

German 🇩🇪: The *German* version of SimpleNLG 4 is avaliable from [this page](https://github.com/sebischair/SimpleNLG-DE). There is also an adaptation of SimpleNLG version 3.x to German. This is available from [this page](http://www.linguistics.rub.de/~bollmann/simplenlg-ger.html). Please remember that SimpleNLG version 3.x is not licensed for commercial use.

Galician: The *Galician* version of SimpleNLG 4 is avaliable from [this page](https://github.com/citiususc/SimpleNLG-GL).

C# implementations of SimpleNLG are also avaliable. One by *Gert-Jan de Vries* [here](https://github.com/gjdv/simplenlg) and a second by *Nick Hodge* [here](https://github.com/nickhodge/SharpSimpleNLG).

SimpleNLG License 
-----------------------------
SimpleNLG is licensed under the terms and conditions of the [Mozilla Public Licence (MPL)](https://www.mozilla.org/en-US/MPL/) version 2.0.
