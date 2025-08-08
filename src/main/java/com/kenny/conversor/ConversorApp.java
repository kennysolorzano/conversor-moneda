// src/main/java/com/kenny/conversor/ConversorApp.java
package com.kenny.conversor;

import java.util.Locale;
import java.util.Scanner;

public class ConversorApp {
    private static final String MENU = """
        ******************************************************
        Sea bienvenido/a al Conversor de Moneda

        1) Dólar (USD)  => Peso argentino (ARS)
        2) Peso argentino (ARS) => Dólar (USD)
        3) Dólar (USD)  => Real brasileño (BRL)
        4) Real brasileño (BRL) => Dólar (USD)
        5) Dólar (USD)  => Peso chileno (CLP)
        6) Dólar (USD)  => Peso colombiano (COP)
        7) Conversión libre (códigos ISO: USD, ARS, BOB, BRL, CLP, COP)
        0) Salir

        Elija una opción válida:
        ******************************************************
        """;

    public static void main(String[] args) throws Exception {
        var provider = new HttpRateProvider();
        var service  = new ConverterService(provider);

        try (Scanner sc = new Scanner(System.in)) {
            sc.useLocale(Locale.US);

            while (true) {
                System.out.print(MENU);
                String opt = sc.next().trim();

                if ("0".equals(opt)) {
                    System.out.println("¡Hasta luego!");
                    break;
                }

                String from, to;
                switch (opt) {
                    case "1" -> { from = "USD"; to = "ARS"; }
                    case "2" -> { from = "ARS"; to = "USD"; }
                    case "3" -> { from = "USD"; to = "BRL"; }
                    case "4" -> { from = "BRL"; to = "USD"; }
                    case "5" -> { from = "USD"; to = "CLP"; }
                    case "6" -> { from = "USD"; to = "COP"; }
                    case "7" -> {
                        System.out.print("Moneda origen (USD/ARS/BOB/BRL/CLP/COP): ");
                        from = sc.next().trim().toUpperCase();
                        System.out.print("Moneda destino (USD/ARS/BOB/BRL/CLP/COP): ");
                        to = sc.next().trim().toUpperCase();
                    }
                    default -> {
                        System.out.println("Opción inválida.\n");
                        continue;
                    }
                }

                System.out.print("Monto: ");
                double amount = sc.nextDouble();

                try {
                    double converted = service.convert(from, to, amount);
                    double rate = converted / amount;
                    System.out.printf(Locale.US,
                            "%.2f %s -> %.2f %s  (tasa: %.6f)%n%n",
                            amount, from, converted, to, rate);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage() + "\n");
                }
            }
        }
    }
}
