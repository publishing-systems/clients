
  0. Make sure that ./../../org.wiktionary/ was compiled (call "make" in that directory in case of doubt).
  1. Download and extract or clone https://github.com/publishing-systems/automated_digital_publishing/ to ./../../../ (above $/clients/) with the directory name "automated_digital_publishing".
  2. Call "make" in ./../../../automated_digital_publishing/.
  3. Call "java setup1" in ./../../../automated_digital_publishing/workflows/.
  4. Download and extract or clone https://github.com/publishing-systems/digital_publishing_workflow_tools/ to ./../../../ (above $/clients/) with the directory name "digital_publishing_workflow_tools".
  5. Call "make" in ./../../../digital_publishing_workflow_tools/.
  6. Call "java setup_1" ./../../../digital_publishing_workflow_tools/workflows/setup/setup_1/.
  7. Consider if ./wiktionary_article_to_xml_1/wiktionary_article_to_xml_1.java should continue to fail on error with the termination messages "messageTokenizerCharacterIsRepeating", "messageTokenizerInvalidCharacter" and "messageTokenizerUnknownCharacter", or if those exceptions should be removed and their optional logging directly above them activated by writing repeatFile, invalidFile and/or codepointFile. Don't forget to compile ("make") wiktionary_article_to_xml_1.java if you change it.
  8. Download the latest version of the dump called "dewiktionary-<date>-pages-articles.xml.bz2" from https://dumps.wikimedia.org/backup-index.html or a mirror listed under https://dumps.wikimedia.org/mirrors.html and extract the archive.
  9. Reference the extracted .xml file in ./workflows/wiktionary_dump_to_xml_1/jobfile.xml in the "path" attribute of the <input-file/> element.
 10. Call "java wiktionary_dump_to_xml_1 jobfile.xml resultinfo.xml" in ./workflows/wiktionary_dump_to_xml_1/. It can take a long time to complete. Consider redirecting the output by adding " > out.log" to the call, and eventually an additional " 2>&1" for the error output.
 11. Go to ./workflows/wiktionary_dump_to_xml_1/output/ and inspect the result.xml file if the call was successful.
 12. You can optionally call ../wiktionary_xml_to_all_1/wiktionary_xml_to_all_1.sh to generate output files from ./workflows/wiktionary_dump_to_xml_1/output/result.xml.
