<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2018 Stephan Kreutzer

This file is part of wiktionary_xml_to_all_1, a submodule of
clients for the digital_publishing_workflow_tools package.

wiktionary_xml_to_all_1 is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3 or any later version,
as published by the Free Software Foundation.

wiktionary_xml_to_all_1 is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License 3 for more details.

You should have received a copy of the GNU Affero General Public License 3
along with wiktionary_xml_to_all_1. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="no" doctype-public="-//W3C//DTD XHTML 1.1//EN" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"/>

  <xsl:template match="/wiktionary-articles">
    <html version="-//W3C//DTD XHTML 1.1//EN" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/1999/xhtml http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="de" lang="de">
      <head>
        <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8"/>
        <xsl:comment> This file was created by stylesheet_xhtml_de_nouns_article_unique.xsl of wiktionary_xml_to_all_1, a submodule of clients for the digital_publishing_workflow_tools package, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). </xsl:comment>
        <title>Unmehrdeutige deutsche Substantive mit Artikel</title>
      </head>
      <body>
        <h1>Unmehrdeutige deutsche Substantive mit Artikel</h1>
        <p>
          Diese Aufbereitung wurde erzeugt für <a href="https://refugee-it.de">refugee-it.de</a> mithilfe der Tools von <a href="http://www.publishing-systems.org">publishing-systems.org</a> auf Basis der Daten des <a href="https://de.wiktionary.org">deutschsprachigen Wiktionarys</a>. Die Daten des Wiktionarys sind stets unter der <a href="https://creativecommons.org/licenses/by-sa/3.0/legalcode.de">Creative Commons Namensnennung - Weitergabe unter gleichen Bedingungen 3.0 Deutschland</a> lizenziert oder gemeinfrei, für manche Datensätze werden optional zusätzliche freie Lizenzen angeboten.
        </p>
        <xsl:apply-templates/>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="/wiktionary-articles/wiktionary-article">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/wiktionary-articles/wiktionary-article/word">
    <xsl:if test="./definition/wordtype/text() = 'Substantiv' and
                  count(./definition) = 1">
      <xsl:apply-templates/>
    </xsl:if>
  </xsl:template>

  <xsl:template match="/wiktionary-articles/wiktionary-article/word/definition">
    <xsl:choose>
      <xsl:when test="./gender/text() = 'm'">
        <xsl:text>der </xsl:text>
      </xsl:when>
      <xsl:when test="./gender/text() = 'f'">
        <xsl:text>die </xsl:text>
      </xsl:when>
      <xsl:when test="./gender/text() = 'n'">
        <xsl:text>das </xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>? </xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:value-of select="../title/text()"/>
    <br/>
  </xsl:template>

  <xsl:template match="node()|@*|text()"/>

</xsl:stylesheet>
