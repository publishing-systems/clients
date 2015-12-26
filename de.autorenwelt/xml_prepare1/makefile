# Copyright (C) 2015  Stephan Kreutzer
#
# This file is part of xml_prepare1.
#
# xml_prepare1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# xml_prepare1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with xml_prepare1. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all xml_prepare1 clean



all: xml_prepare1
xml_prepare1: xml_prepare1.class



xml_prepare1.class: xml_prepare1.java
	javac -encoding UTF-8 xml_prepare1.java


clean:
	rm -f xml_prepare1.class
