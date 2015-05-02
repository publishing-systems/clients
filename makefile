# Copyright (C) 2015  Stephan Kreutzer
#
# This file is part of clients for automated_digital_publishing.
#
# clients for automated_digital_publishing is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# clients for automated_digital_publishing is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with clients for automated_digital_publishing. If not, see <http://www.gnu.org/licenses/>.



directories = ./mobileread ./setup



.PHONY: all $(directories) install



all: $(directories)



$(directories):
	$(MAKE) --directory=$@

install:
	java -classpath ./setup/setup1/ setup1

