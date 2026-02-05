package parser;

import game.Movie;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MovieParser {

    private static final int MOVIE_TITLE_INDEX = 0;
    private static final int MOVIE_IMAGE_PATH_INDEX = 1;

    public List<Movie> parseMovies(String csvFilePath) {
        InputStream inputStream = getClass().getResourceAsStream(csvFilePath);

        if (inputStream == null) {
            throw new RuntimeException("Не удалось прочитать файл %s".formatted(csvFilePath));
        }
        List<Movie> movies = new ArrayList<>();
        try {
            CSVFormat csvFormat = CSVFormat.Builder.create()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get();
            CSVParser csvParser = csvFormat.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            for (CSVRecord movieRecord : csvParser) {
                String title = movieRecord.get(MOVIE_TITLE_INDEX);
                String imagePath = movieRecord.get(MOVIE_IMAGE_PATH_INDEX);
                Movie movie = new Movie(title, imagePath);
                movies.add(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
