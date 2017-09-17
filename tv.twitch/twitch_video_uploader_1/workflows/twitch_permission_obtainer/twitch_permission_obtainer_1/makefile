# Copyright (C) 2016-2017 Stephan Kreutzer
#
# This file is part of twitch_permission_obtainer_1 workflow of clients for the
# automated_digital_publishing and digital_publishing_workflow_tools packages.
#
# twitch_permission_obtainer_1 workflow is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# twitch_permission_obtainer_1 workflow is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with twitch_permission_obtainer_1 workflow. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all twitch_permission_obtainer_1 clean



all: twitch_permission_obtainer_1
twitch_permission_obtainer_1: twitch_permission_obtainer_1.class



InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

AccessTokenObtainerServer.class: AccessTokenObtainerServer.java
	javac -encoding UTF-8 AccessTokenObtainerServer.java

twitch_permission_obtainer_1.class: twitch_permission_obtainer_1.java AccessTokenObtainerServer.class InfoMessage.class ProgramTerminationException.class
	javac -encoding UTF-8 twitch_permission_obtainer_1.java

clean:
	rm -f twitch_permission_obtainer_1.class
	rm -f AccessTokenObtainerServer.class
	rm -f ProgramTerminationException.class
	rm -f InfoMessage.class
