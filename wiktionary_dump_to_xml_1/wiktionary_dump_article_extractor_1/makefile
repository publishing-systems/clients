# Copyright (C) 2016-2017 Stephan Kreutzer
#
# This file is part of wiktionary_dump_article_extractor_1, a submodule of the
# wiktionary_dump_to_xml_1 package.
#
# wiktionary_dump_article_extractor_1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# wiktionary_dump_article_extractor_1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with wiktionary_dump_article_extractor_1. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all wiktionary_dump_article_extractor_1 clean



all: wiktionary_dump_article_extractor_1
wiktionary_dump_article_extractor_1: wiktionary_dump_article_extractor_1.class



InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

wiktionary_dump_article_extractor_1.class: wiktionary_dump_article_extractor_1.java ProgramTerminationException.class InfoMessage.class
	javac -encoding UTF-8 wiktionary_dump_article_extractor_1.java

clean:
	rm -f wiktionary_dump_article_extractor_1.class
	rm -f ProgramTerminationException.class
	rm -f InfoMessage.class
