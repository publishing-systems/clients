# Copyright (C) 2016-2017 Stephan Kreutzer
#
# This file is part of twitch_video_uploader_2 workflow of clients for the
# automated_digital_publishing and digital_publishing_workflow_tools packages.
#
# twitch_video_uploader_2 workflow is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# twitch_video_uploader_2 workflow is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with twitch_video_uploader_2 workflow. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all twitch_video_uploader_2 clean



all: twitch_video_uploader_2
twitch_video_uploader_2: twitch_video_uploader_2.class



InfoMessage.class: InfoMessage.java
	javac -encoding UTF-8 InfoMessage.java

ProgramTerminationException.class: ProgramTerminationException.java
	javac -encoding UTF-8 ProgramTerminationException.java

JobFileInstruction.class: JobFileInstruction.java
	javac -encoding UTF-8 JobFileInstruction.java

twitch_video_uploader_2.class: twitch_video_uploader_2.java JobFileInstruction.class InfoMessage.class ProgramTerminationException.class
	javac -encoding UTF-8 twitch_video_uploader_2.java

clean:
	rm -f twitch_video_uploader_2.class
	rm -f JobFileInstruction.class
	rm -f ProgramTerminationException.class
	rm -f InfoMessage.class
