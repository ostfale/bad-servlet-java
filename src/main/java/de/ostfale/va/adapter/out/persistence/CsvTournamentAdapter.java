package de.ostfale.va.adapter.out.persistence;

import de.ostfale.va.adapter.out.LoadTournamentsPort;
import de.ostfale.va.application.domain.model.TourCategory;
import de.ostfale.va.application.domain.model.Tournament;
import de.ostfale.va.application.domain.model.TournamentType;
import de.ostfale.va.common.UseLogging;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvTournamentAdapter implements LoadTournamentsPort, UseLogging {

    @Override
    public List<Tournament> loadAll() {
        if (tournaments.isEmpty()) {
            tournaments.addAll(parseTournamentCalendar(new File(getClass().getClassLoader().getResource("Turniere_25.csv").getFile())));
        }
        log().debug("LoadTournamentsFromCSV :: Tournaments loaded from CSV file = {}", tournaments.size());
        return tournaments;
    }

    private static final int DEFAULT_TOURNAMENT_ORDER = 0;
    private static final String HEADER_START_MARKER = "Start-Datum";

    static String CSV_SEPARATOR = ";";
    static String DATE_FORMAT = "dd.MM.yyyy";
    static String EMPTY_STRING = "";
    static int START_DATE_INDEX = 0;
    static int END_DATE_INDEX = 1;
    static int TOURNAMENT_NAME_INDEX = 2;
    static int TOURNAMENT_TYPE_INDEX = 3;
    static int TOURNAMENT_ORD_NO_INDEX = 4;
    static int COUNTRY_INDEX = 5;
    static int LOCATION_INDEX = 6;
    static int POSTAL_CODE_INDEX = 7;
    static int REGION_INDEX = 8;
    static int OPEN_NAME_INDEX = 9;
    static int ORGANIZER_INDEX = 10;
    static int CATEGORY_INDEX = 11;
    static int CLOSE_DATE_INDEX = 12;
    static int WEB_URL_INDEX = 13;
    static int PDF_URL_INDEX = 14;
    static int PDF_AVAILABLE_INDEX = 15;
    static int TOUR_CREATION_DATE_INDEX = 16;
    static int TOUR_VISIBLE_DATE_INDEX = 17;
    static int INVITATION_CREATION_DATE_INDEX = 18;
    static int TURNIER_LINK_CREATION_DATE_INDEX = 19;
    static int AK_U9_INDEX = 20;
    static int AK_U11_INDEX = 21;
    static int AK_U13_INDEX = 22;
    static int AK_U15_INDEX = 23;
    static int AK_U17_INDEX = 24;
    static int AK_U19_INDEX = 25;
    static int AK_U22_INDEX = 26;
    static int AK_O19_INDEX = 27;
    static int AK_O35_INDEX = 27;
    private final List<Tournament> tournaments = new ArrayList<>();

    private static String readCSVValue(String[] splitRow, int index) {
        if (index >= splitRow.length) {
            return EMPTY_STRING;
        }
        return splitRow[index].trim();
    }

    private static int getTournamentOrderNo(String[] splitRow) {
        String tournamentOrderNo = splitRow[TOURNAMENT_ORD_NO_INDEX];

        if (tournamentOrderNo.isBlank()) {
            return DEFAULT_TOURNAMENT_ORDER;
        }

        if (!isNumeric(tournamentOrderNo)) {
            return DEFAULT_TOURNAMENT_ORDER;
        }

        return Integer.parseInt(tournamentOrderNo);
    }

    private static TourCategory getCategory(String[] splitRow) {
        String category = splitRow[CATEGORY_INDEX];
        return TourCategory.lookup(category);
    }

    static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.trim().matches("\\d+");
    }

    public List<Tournament> parseTournamentCalendar(File aFile) {
        List<Tournament> tournamentsList = new ArrayList<>();

        try (Scanner scanner = new Scanner(aFile)) {
            while (scanner.hasNext()) {
                String currentLine = scanner.nextLine().trim();

                if (isHeaderOrEmptyLine(currentLine)) {
                    continue;
                }

                tournamentsList.add(parseRow(currentLine));
            }
        } catch (FileNotFoundException e) {
            log().error("File not found: {}", aFile.getName());
        }
        return tournamentsList;
    }

    public Tournament parseRow(String row) {
        String fixedRow = fixRow(row);
        String[] splitRow = fixedRow.split(CSV_SEPARATOR);
        return buildPlannedTournament(splitRow);
    }

    private Tournament buildPlannedTournament(String[] splitRow) {
        var startDate = readCSVValue(splitRow, START_DATE_INDEX);
        var endDate = readCSVValue(splitRow, END_DATE_INDEX);
        var tournamentName = readCSVValue(splitRow, TOURNAMENT_NAME_INDEX);
        var tournamentType = TournamentType.lookup(splitRow[TOURNAMENT_TYPE_INDEX]);
        var tournamentOrderNo = getTournamentOrderNo(splitRow);
        var countryCode = readCSVValue(splitRow, COUNTRY_INDEX);
        var location = readCSVValue(splitRow, LOCATION_INDEX);
        var postalCode = readCSVValue(splitRow, POSTAL_CODE_INDEX);
        var region = readCSVValue(splitRow, REGION_INDEX);
        var openName = readCSVValue(splitRow, OPEN_NAME_INDEX);
        var organizer = readCSVValue(splitRow, ORGANIZER_INDEX);
        var category = getCategory(splitRow);
        var closeDate = readCSVValue(splitRow, CLOSE_DATE_INDEX);
        var webLinkUrl = readCSVValue(splitRow, WEB_URL_INDEX);
        var pdfLinkUrl = readCSVValue(splitRow, PDF_URL_INDEX);
        var pdfAvailable = readCSVValue(splitRow, PDF_AVAILABLE_INDEX);
        var tourCreationDate = readCSVValue(splitRow, TOUR_CREATION_DATE_INDEX);
        var tourVisibleDate = readCSVValue(splitRow, TOUR_VISIBLE_DATE_INDEX);
        var tourInvitationCreationDate = readCSVValue(splitRow, INVITATION_CREATION_DATE_INDEX);
        var tourLinkCreationDate = readCSVValue(splitRow, TURNIER_LINK_CREATION_DATE_INDEX);
        var akU9 = readCSVValue(splitRow, AK_U9_INDEX);
        var akU11 = readCSVValue(splitRow, AK_U11_INDEX);
        var akU13 = readCSVValue(splitRow, AK_U13_INDEX);
        var akU15 = readCSVValue(splitRow, AK_U15_INDEX);
        var akU17 = readCSVValue(splitRow, AK_U17_INDEX);
        var akU19 = readCSVValue(splitRow, AK_U19_INDEX);
        var akU22 = readCSVValue(splitRow, AK_U22_INDEX);
        var akO19 = readCSVValue(splitRow, AK_O19_INDEX);
        var akO35 = readCSVValue(splitRow, AK_O35_INDEX);

        return new Tournament(
                startDate, endDate, tournamentName, tournamentType, tournamentOrderNo, countryCode, location, postalCode,
                region, openName, organizer, category, closeDate, webLinkUrl, pdfLinkUrl, pdfAvailable, tourCreationDate,
                tourVisibleDate, tourInvitationCreationDate, tourLinkCreationDate,
                akU9, akU11, akU13, akU15, akU17, akU19, akU22, akO19, akO35
        );
    }

    private boolean isHeaderOrEmptyLine(String line) {
        return line.startsWith(HEADER_START_MARKER) || line.isBlank();
    }

    private String fixRow(String row) {
        return row.replace("\"", "");
    }
}
