package agh.ics.oop.Models.Utils;

import agh.ics.oop.Models.Maps.WorldMap;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayStatisticsExport {
    public String escapeSpecialCharacters(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Input data cannot be null");
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(";"));
    }

    public void toCsv(WorldMap map) throws IOException {
        synchronized (this) {
            List<String[]> dataLines = new ArrayList<>();

            File csvOutputFile = new File("Day_Statistics_Map_" + map.getID() + ".csv");
            if(!csvOutputFile.exists()) {
                dataLines.add(new String[]{"Avg Child Number", "Avg Energy", "Evg Life length", "Number of animals", "Number of free places","Number of grass","Popular genotype"});
            }
            dataLines.add(new String[]
                    {String.valueOf(map.getAverageNumberOfChildren()),
                            String.valueOf(map.getAverageEnergy()),
                            String.valueOf(map.getAverageLifeTime()),
                            String.valueOf(map.getNumberOfAnimals()),
                            String.valueOf(map.getNumberOfFreePlaces()),
                            String.valueOf(map.getNumbersOfGrass()),
                            String.valueOf(map.getTopDna(3))});
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvOutputFile, true))) {
                for (String[] line : dataLines) {
                    bw.write(convertToCSV(line));
                    bw.newLine();
                }
            }

        }
    }
}
