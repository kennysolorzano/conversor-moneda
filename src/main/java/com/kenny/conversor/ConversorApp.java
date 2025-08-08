package com.kenny.conversor;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public class ConversorApp {
    private static final String MENU = """
        ******************************************************
        Sea bienvenido/a al Conversor de Moneda

        1) USD  => ARS
        2) ARS  => USD
        3) USD  => BRL
        4) BRL  => USD
        5) USD  => CLP
        6) USD  => COP
        7) Conversión libre (cualquier ISO válido)
        8) Ver historial (últimas 10)
        9) Exportar historial a CSV
        10) Filtrar historial por fecha (YYYY-MM-DD .. YYYY-MM-DD)
        11) Filtrar historial por par (FROM->TO)
        0) Salir

        Elija una opción válida:
        ******************************************************
        """;

    public static void main(String[] args) throws Exception {
        var provider = new HttpRateProvider();
        var service  = new ConverterService(provider);
        var history  = new HistoryService();

        try (Scanner sc = new Scanner(System.in)) {
            sc.useLocale(Locale.US);

            // precargar ISO soportados
            Set<String> iso = ((HttpRateProvider)provider).getSupportedCodes();

            while (true) {
                System.out.print(MENU);
                String opt = sc.next().trim();

                switch (opt) {
                    case "0" -> {
                        Console.ok("¡Hasta luego!");
                        return;
                    }
                    case "8" -> {
                        if (history.isEmpty()) { Console.warn("Historial vacío.\n"); break; }
                        Console.info("=== Últimas conversiones ===");
                        for (var rec : history.last(10)) System.out.println(rec.toPrettyString());
                        System.out.println();
                        break;
                    }
                    case "9" -> {
                        try {
                            Path out = Path.of("historial.csv");
                            history.exportCsv(out);
                            Console.ok("Historial exportado a: " + out.toAbsolutePath() + "\n");
                        } catch (Exception e) {
                            Console.err("No se pudo exportar el historial: " + e.getMessage() + "\n");
                        }
                        break;
                    }
                    case "10" -> {
                        System.out.print("Desde (YYYY-MM-DD): ");
                        LocalDate d1 = LocalDate.parse(sc.next().trim());
                        System.out.print("Hasta (YYYY-MM-DD): ");
                        LocalDate d2 = LocalDate.parse(sc.next().trim());
                        var from = LocalDateTime.of(d1, LocalTime.MIN);
                        var to   = LocalDateTime.of(d2, LocalTime.MAX);
                        var list = history.filterByDateRange(from, to);
                        if (list.isEmpty()) Console.warn("Sin resultados.\n");
                        else {
                            Console.info("=== Resultados ===");
                            list.forEach(r -> System.out.println(r.toPrettyString()));
                            System.out.println();
                        }
                        break;
                    }
                    case "11" -> {
                        String from = pedirMoneda(sc, "Desde", iso);
                        String to   = pedirMoneda(sc, "Hasta", iso);
                        var list = history.filterByPair(from, to);
                        if (list.isEmpty()) Console.warn("Sin resultados.\n");
                        else {
                            Console.info("=== Resultados ===");
                            list.forEach(r -> System.out.println(r.toPrettyString()));
                            System.out.println();
                        }
                        break;
                    }
                    default -> {
                        String from, to;
                        switch (opt) {
                            case "1" -> { from = "USD"; to = "ARS"; }
                            case "2" -> { from = "ARS"; to = "USD"; }
                            case "3" -> { from = "USD"; to = "BRL"; }
                            case "4" -> { from = "BRL"; to = "USD"; }
                            case "5" -> { from = "USD"; to = "CLP"; }
                            case "6" -> { from = "USD"; to = "COP"; }
                            case "7" -> {
                                from = pedirMoneda(sc, "Moneda origen", iso);
                                to   = pedirMoneda(sc, "Moneda destino", iso);
                            }
                            default -> { Console.warn("Opción inválida.\n"); continue; }
                        }

                        double amount = pedirMonto(sc);

                        boolean exito = false;
                        while (!exito) {
                            try {
                                double converted = service.convert(from, to, amount);
                                double rate = converted / amount;
                                Console.ok(String.format(Locale.US,
                                        "%.2f %s -> %.2f %s  (tasa: %.6f)%n",
                                        amount, from, converted, to, rate));

                                var rec = new ConversionRecord(LocalDateTime.now(), from, to, amount, rate, converted);
                                history.add(rec);
                                exito = true;
                            } catch (Exception e) {
                                Console.err("Error: " + e.getMessage());
                                System.out.print("¿Desea reintentar? (S/N): ");
                                String resp = sc.next().trim().toUpperCase();
                                if (!resp.equals("S")) break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static String pedirMoneda(Scanner sc, String label, Set<String> iso) {
        while (true) {
            System.out.print(label + " (código ISO de 3 letras, ej. USD): ");
            String m = sc.next().trim().toUpperCase();
            if (m.length() == 3 && iso.contains(m)) return m;
            Console.warn("Código inválido. Intente nuevamente.");
        }
    }

    private static double pedirMonto(Scanner sc) {
        while (true) {
            System.out.print("Monto: ");
            if (sc.hasNextDouble()) {
                double v = sc.nextDouble();
                if (v > 0) return v;
            }
            Console.warn("Monto inválido. Intente nuevamente.");
            sc.next(); // limpiar token inválido
        }
    }
}
