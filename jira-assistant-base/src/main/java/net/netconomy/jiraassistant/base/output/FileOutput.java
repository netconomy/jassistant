/**
*  Copyright 2018 The JASSISTANT Authors.
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
**/
package net.netconomy.jiraassistant.base.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.netconomy.jiraassistant.base.JiraAssistantException;
import net.netconomy.jiraassistant.base.data.csv.CSVLine;
import net.netconomy.jiraassistant.base.data.csv.CSVTable;

@Service
public class FileOutput {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileOutput.class);

    public static final String UTF8_BOM = "\uFEFF";

    @Autowired
    JsonOutput jsonOutput;

    /**
     * Writes the given Object to a File as JSON. The conversion to JSON is done by Gson.
     * 
     * @param thisObject
     * @param file
     * @param encoding
     *            supported are: US-ASCII, ISO-8859-1, UTF-8, UTF-16BE, UTF-16LE, UTF-16. If null is given UTF-8 will be
     *            used.
     */
    public void writeObjectAsJsonToFile(Object thisObject, Path file, String encoding) {

        Charset charSet;
        String jsonString = jsonOutput.convertObjectToJSONString(thisObject);

        if (encoding != null && !encoding.trim().isEmpty() && Charset.isSupported(encoding)) {
            charSet = Charset.forName(encoding);
        } else {
            charSet = StandardCharsets.UTF_8;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file, charSet)) {

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("The given object of type {} is converted to JSON and written to {}",
                        thisObject.getClass().getName(), file.getFileName());
            }

            if (charSet.equals(StandardCharsets.UTF_8)) {
                writer.write(UTF8_BOM);
            }

            writer.write(jsonString);

        } catch (IOException e) {
            throw new JiraAssistantException("Exception during JSON File creation.", e);
        }

    }

    /**
     * Writes the given CSVTable Object into an Excel compatible CSV. If no delimiter is given ; will be used.
     * 
     * @param table
     * @param file
     * @param delimiter
     *            the delimiter for the CSV. If null ; will be used.
     * @param encoding
     *            supported are: US-ASCII, ISO-8859-1, UTF-8, UTF-16BE, UTF-16LE, UTF-16. If null is given UTF-8 will be
     *            used.
     */
    public void writeCSVTableToFile(CSVTable table, Path file, Character delimiter, String encoding) {

        char delimiterChar;
        Charset charSet;

        if (delimiter == null) {
            delimiterChar = ';';
        } else {
            delimiterChar = delimiter;
        }

        if (encoding != null && !encoding.trim().isEmpty() && Charset.isSupported(encoding)) {
            charSet = Charset.forName(encoding);
        } else {
            charSet = StandardCharsets.UTF_8;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file, charSet);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL.withDelimiter(delimiterChar));) {

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("The given data is converted to CSV and written to {}", file.getFileName());
            }

            if (charSet.equals(StandardCharsets.UTF_8)) {
                writer.write(UTF8_BOM);
            }

            for (CSVLine currentLine : table.getCsvTable()) {
                csvPrinter.printRecord(currentLine.getLine());
            }

        } catch (IOException e) {
            throw new JiraAssistantException("Exception during CSV file creation.", e);
        }

    }

    /**
     * Writes the given Object to a File as JSON. The conversion to JSON is done by Gson.
     * 
     * @param thisObject
     * @param fileName
     * @param encoding
     *            supported are: US-ASCII, ISO-8859-1, UTF-8, UTF-16BE, UTF-16LE, UTF-16. If null is given UTF-8 will be
     *            used.
     */
    public void writeObjectAsJsonToFile(Object thisObject, String fileName, String encoding) {

        Path jsonFile = Paths.get(fileName);

        writeObjectAsJsonToFile(thisObject, jsonFile, encoding);

    }

    /**
     * Writes the given CSVTable Object into an Excel compatible CSV. If no delimiter is given ; will be used.
     * 
     * @param table
     * @param fileName
     * @param delimiter
     *            the delimiter for the CSV. If null ; will be used.
     * @param encoding
     *            supported are: US-ASCII, ISO-8859-1, UTF-8, UTF-16BE, UTF-16LE, UTF-16. If null is given UTF-8 will be
     *            used.
     */
    public void writeCSVTableToFile(CSVTable table, String fileName, Character delimiter, String encoding) {

        Path csvFile = Paths.get(fileName);

        writeCSVTableToFile(table, csvFile, delimiter, encoding);

    }

}
