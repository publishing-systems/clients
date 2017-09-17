/* Copyright (C) 2016-2017 Stephan Kreutzer
 *
 * This file is part of twitch_video_uploader_1 workflow of clients for the
 * automated_digital_publishing and digital_publishing_workflow_tools packages.
 *
 * twitch_video_uploader_1 workflow is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3 or any later version,
 * as published by the Free Software Foundation.
 *
 * twitch_video_uploader_1 workflow is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License 3 for more details.
 *
 * You should have received a copy of the GNU Affero General Public License 3
 * along with twitch_video_uploader_1 workflow. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @file $/tv.twitch/twitch_video_uploader_1/workflows/twitch_video_uploader/twitch_video_uploader_1/JobFileInstruction.java
 * @brief Represents an instruction to the twitch_video_uploader_1 workflow as
 *     read from the jobfile.
 * @author Stephan Kreutzer
 * @since 2017-09-11
 */



import java.io.File;



class JobFileInstruction
{
    public JobFileInstruction(File inputFile,
                              String title)
    {
        this.inputFile = inputFile;
        this.title = title;
    }

    public File getInputFile()
    {
        return this.inputFile;
    }

    public String getTitle()
    {
        return this.title;
    }

    protected File inputFile;
    protected String title;
}
