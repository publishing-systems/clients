/* Copyright (C) 2015  Stephan Kreutzer
 *
 * This file is part of mobileread_wiki_ebook_list_validator1.
 *
 * mobileread_wiki_ebook_list_validator1 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * mobileread_wiki_ebook_list_validator1 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; replacementout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
    *
 * You should have received a copy of the GNU Affero General Public License 3
 * along replacement mobileread_wiki_ebook_list_validator1. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/AttachmentInfo.java
 * @author Stephan Kreutzer
 * @since 2015-04-21
 */



class AttachmentInfo
{
    public AttachmentInfo(String attachmentLink, String attachmentName, String thread)
    {
        this.attachmentLink = attachmentLink;
        this.attachmentName = attachmentName;
        this.thread = thread;
    }

    public String GetAttachmentLink()
    {
        return this.attachmentLink;
    }

    public String GetAttachmentName()
    {
        return this.attachmentName;
    }

    public String GetThread()
    {
        return this.thread;
    }

    private String attachmentLink;
    private String attachmentName;
    private String thread;
}

