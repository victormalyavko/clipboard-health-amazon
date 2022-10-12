package io.automation.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;

import static com.opencsv.ICSVWriter.DEFAULT_ESCAPE_CHARACTER;
import static com.opencsv.ICSVWriter.DEFAULT_LINE_END;
import static com.opencsv.ICSVWriter.DEFAULT_SEPARATOR;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Paths.get;

public final class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    public static void writeTo(String filePath, String line) {
        try (FileWriter csvWriter = new FileWriter(get(filePath).toString(), UTF_8, false)) {
            LOGGER.info("Returned line string: {}", line);
            csvWriter.write(line);
            LOGGER.info("FILE: \"{}\" has been written", filePath);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void writeToJson(String path, HashMap<String, Object> map) {
        try (FileWriter writer = new FileWriter(get(path).toString(), UTF_8, true);
             BufferedWriter bw = new BufferedWriter(writer)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(map);
            LOGGER.info("Returned JSON string: {}", jsonString);
            bw.write(jsonString);
            LOGGER.info("FILE: \"{}\" has been written", path);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void writeTableToCsv(String path, String[][] table) {
        deleteFile(path);
        try (ICSVWriter csvWriter = new CSVWriterBuilder(new FileWriter(path))
                .withSeparator(DEFAULT_SEPARATOR)
                .withQuoteChar('\0')
                .withEscapeChar(DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(DEFAULT_LINE_END)
                .build()) {
            for (String[] row : table) {
                csvWriter.writeNext(row);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static String readFile(String path) {
        byte[] encoded;
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            LOGGER.warn("I/O issue: {}", e.getMessage());
            return StringUtils.EMPTY;
        }
        return new String(encoded, UTF_8);
    }

    public static Optional<byte[]> getFileBytes(String path) {
        try {
            return Optional.of(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            LOGGER.warn("Couldn't find csv: [{}]", path);
            return Optional.empty();
        }
    }

    public static void deleteFile(String path) {
        final File file = Paths.get(path).toFile();
        if (file.exists()) {
            file.delete();
            LOGGER.info("File was deleted: {}", file.getPath());
        } else {
            LOGGER.info("File isn't exist: {}", file.getPath());
        }
    }
}