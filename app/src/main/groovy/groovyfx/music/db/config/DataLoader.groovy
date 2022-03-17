package groovyfx.music.db.config

import groovy.util.logging.Slf4j
import groovyfx.music.db.models.Artist
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser

import java.nio.file.Files
import java.nio.file.Paths

@Slf4j
enum DataLoader {
    INSTANCE;

    private static final String FILE_NAME = "Artists.csv"
    private static final String FILE_DIR = System.getProperty("user.dir") + "/sql/backup/"
    private static final String[] CSV_HEADERS = ["First Name", "Last Name", "Phone Number", "Notes"]

    private final List<Artist> artists = []

    /**
     * Reads data from a csv file.
     *
     * @throws IOException if the file cannot be accessed for any reason.
     */
    void loadData() throws IOException {
        CSVFormat format = CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).setIgnoreHeaderCase(true).setTrim(true).build()
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(FILE_DIR + FILE_NAME))
             CSVParser csvParser = new CSVParser(bufferedReader, format)) {
            csvParser.getRecords().forEach(csvRecord -> {
                Artist artist = new Artist(
                        Integer.parseInt(csvRecord.get(CSV_HEADERS[0])),
                        csvRecord.get(CSV_HEADERS[1])
                )
                artists.add(artist)
            })
            log.info("The file contained " + artists.size() + " contact(s).")
        } catch (IllegalArgumentException | IOException e) {
            log.error("Failed to read the file: " + e.getMessage())
            throw new IOException(e)
        }
    }

}
