/* Copyright (C) 2015 Stephan Kreutzer
 *
 * This file is part of mobileread_wiki_ebook_list_validation_uploader_statistics1
 *
 * mobileread_wiki_ebook_list_validation_uploader_statistics1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * mobileread_wiki_ebook_list_validation_uploader_statistics1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with mobileread_wiki_ebook_list_validation_uploader_statistics1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/ValidationUploaderStatistics.java
 * @author Stephan Kreutzer
 * @since 2015-11-18
 */



public class ValidationUploaderStatistics
{
    public ValidationUploaderStatistics(String uploader)
    {
        this.uploader = uploader;
    }

    public String getUploader()
    {
        return this.uploader;
    }

    public int getErrors()
    {
        return this.errors;
    }

    public int getWarnings()
    {
        return this.warnings;
    }

    public int getValids()
    {
        return this.valids;
    }

    public int incrementErrors()
    {
        this.errors++;
        return this.errors;
    }

    public int incrementWarnings()
    {
        this.warnings++;
        return this.warnings;
    }

    public int incrementValids()
    {
        this.valids++;
        return this.valids;
    }

    public String getLinkComplain()
    {
        return this.linkComplain;
    }

    public int setLinkComplain(String linkComplain)
    {
        this.linkComplain = linkComplain;
        return 0;
    }

    protected String uploader;
    protected int errors;
    protected int warnings;
    protected int valids;
    protected String linkComplain;
}

