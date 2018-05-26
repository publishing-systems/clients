#!/bin/sh
# Copyright (C) 2018 Stephan Kreutzer
#
# This file is part of wiktionary_xml_to_all_1, a submodule of
# clients for the digital_publishing_workflow_tools package.
#
# wiktionary_xml_to_all_1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# wiktionary_xml_to_all_1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with wiktionary_xml_to_all_1. If not, see <http://www.gnu.org/licenses/>.

java -cp ../../../digital_publishing_workflow_tools/xml_xslt_transformator/xml_xslt_transformator_1/ xml_xslt_transformator_1 jobfile_xml_xslt_transformator_1.xml resultinfo_xml_xslt_transformator_1.xml_xslt_tranformator
