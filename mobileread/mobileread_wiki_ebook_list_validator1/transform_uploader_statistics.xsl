<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2015  Stephan Kreutzer

This file is part of mobileread_wiki_ebook_list_validator1.

mobileread_wiki_ebook_list_validator1 is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3 or any later version,
as published by the Free Software Foundation.

mobileread_wiki_ebook_list_validator1 is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License 3 for more details.

You should have received a copy of the GNU Affero General Public License 3
along with mobileread_wiki_ebook_list_validator1. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" doctype-public="-//W3C//DTD XHTML 1.1//EN" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"/>

  <xsl:template match="/validation-uploader-statistics">
    <xsl:comment> This file was generated by transform_result.xsl for mobileread_wiki_ebook_list_validator1, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/publishing-systems/clients/ and http://www.publishing-systems.org). </xsl:comment><xsl:text>&#xA;</xsl:text>
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
      <head>
        <title>Ergebnis von mobileread_wiki_ebook_list_validator1: Gültigkeit per Uploader</title>
        <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8" />
        <style type="text/css">
          h2
          {
              text-align: center;
          }

          .error
          {
              color: red;
          }

          .warning
          {
              color: red;
          }

          .valid
          {
              color: green;
          }
        </style>
      </head>
      <body>
        <div>
          <h2>Ruhmeshalle</h2>
          <div>
            <table>
              <tr>
                <th>Uploader</th>
                <th>Fehler</th>
                <th>Warnungen</th>
                <th>gültig</th>
                <th>Bilanz</th>
              </tr>
              <xsl:apply-templates/>
              <tr>
                <td>Summe</td>
                <td><span class="error"><xsl:value-of select="//sum/@errors"/></span></td>
                <td><span class="warning"><xsl:value-of select="//sum/@warnings"/></span></td>
                <td><span class="valid"><xsl:value-of select="//sum/@valids"/></span></td>
                <td></td>
              </tr>
            </table>
            <p>
              <a href="uploader_statistics.xml">Quelle</a>, <a href="transform_uploader_statistics.xsl">Stylesheet</a>.
            </p>
          </div>
        </div>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="//uploader">
    <tr>
      <td>
        <xsl:choose>
          <xsl:when test="@errors = 0 and @warnings = 0">
            <span class="valid">
              <xsl:value-of select="./@name"/>
            </span>
          </xsl:when>
          <xsl:otherwise>
            <span class="error">
              <a href="{./@user_url}">
                <xsl:value-of select="./@name"/>
              </a>
            </span>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td>
        <xsl:choose>
          <xsl:when test="@errors > 0">
            <span class="error">
              <xsl:value-of select="./@errors"/>
            </span>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="./@errors"/>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td>
        <xsl:choose>
          <xsl:when test="@warnings > 0">
            <span class="warning">
              <xsl:value-of select="./@warnings"/>
            </span>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="./@warnings"/>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td>
        <xsl:value-of select="@valids"/>
      </td>
        <xsl:choose>
          <xsl:when test="@errors + @warnings > 0">
            <span class="error">
              <xsl:value-of select="0 - (@errors + @warnings)"/>
            </span>
          </xsl:when>
          <xsl:otherwise>
            <span class="valid">
              <xsl:value-of select="./@valids"/>
            </span>
          </xsl:otherwise>
        </xsl:choose>
    </tr>
  </xsl:template>

  <xsl:template match="text()|@*"/>

</xsl:stylesheet>