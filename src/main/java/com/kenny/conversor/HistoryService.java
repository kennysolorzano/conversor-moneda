package com.kenny.conversor;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HistoryService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Deque<ConversionRecord> records = new ArrayDeque<>();
    private final Path csvPath;

    public HistoryService() { this(Path.of("historial.csv")); }
    public HistoryService(Path csvPath) {
        this.csvPath = csvPath;
        load();
    }

    public void add(ConversionRecord record) {
        records.addLast(record);
        // Persistir inmediatamente
        try { appendCsv(record); } catch (Exception ignored) {}
        // Limitar tamaño si quieres:
        // if (records.size() > 5000) records.removeFirst();
    }

    public List<ConversionRecord> last(int n) {
        List<ConversionRecord> all = new ArrayList<>(records);
        List<ConversionRecord> out = new ArrayList<>();
        for (int i = all.size() - 1; i >= 0 && out.size() < n; i--) out.add(all.get(i));
        return out;
    }

    public boolean isEmpty() { return records.isEmpty(); }

    public List<ConversionRecord> filterByDateRange(LocalDateTime from, LocalDateTime to) {
        List<ConversionRecord> out = new ArrayList<>();
        for (var r : records) {
            if ((r.timestamp().isEqual(from) || r.timestamp().isAfter(from))
                    && (r.timestamp().isEqual(to) || r.timestamp().isBefore(to))) {
                out.add(r);
            }
        }
        return out;
    }

    public List<ConversionRecord> filterByPair(String from, String to) {
        from = from.toUpperCase(); to = to.toUpperCase();
        List<ConversionRecord> out = new ArrayList<>();
        for (var r : records) {
            if (r.from().equals(from) && r.to().equals(to)) out.add(r);
        }
        return out;
    }

    public void exportCsv(Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("timestamp,from,to,amount,rate,result");
        for (ConversionRecord r : records) lines.add(r.toCsvLine());
        Files.write(path, lines);
    }

    private void load() {
        if (!Files.exists(csvPath)) return;
        try {
            List<String> lines = Files.readAllLines(csvPath);
            for (int i = 1; i < lines.size(); i++) { // saltar cabecera
                String[] parts = lines.get(i).split(",");
                if (parts.length < 6) continue;
                LocalDateTime ts = LocalDateTime.parse(parts[0], FMT);
                String from = parts[1], to = parts[2];
                double amount = Double.parseDouble(parts[3]);
                double rate = Double.parseDouble(parts[4]);
                double result = Double.parseDouble(parts[5]);
                records.addLast(new ConversionRecord(ts, from, to, amount, rate, result));
            }
        } catch (Exception ignored) {}
    }

    private void appendCsv(ConversionRecord r) throws IOException {
        boolean exists = Files.exists(csvPath);
        if (!exists) {
            Files.writeString(csvPath, "timestamp,from,to,amount,rate,result\n",
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        Files.writeString(csvPath, r.toCsvLine() + "\n",
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
