# 💱 Conversor de Moneda

![Java](https://img.shields.io/badge/Java-17-blue.svg?logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-blue?logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-green.svg)

Proyecto desarrollado como parte del **Challenge Conversor de Moneda** del programa **Oracle Next Education (ONE)**.  
Permite convertir entre diferentes monedas utilizando tasas de cambio obtenidas desde la API pública **[ExchangeRate API](https://www.exchangerate-api.com)**.

---

## 📌 Características

- Conversión entre monedas predefinidas (USD, ARS, BRL, CLP, COP, entre otras).
- Conversión libre mediante códigos ISO de monedas.
- Validaciones para entrada de datos (evita valores no numéricos y códigos inválidos).
- Historial de conversiones realizadas en la sesión.
- Soporte para **más de 150 monedas**.
- Caché de tasas de cambio para mejorar rendimiento.
- Registros con marca de tiempo (`java.time`).

---

## 🖼 Ejemplo de uso

```text
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
1
Monto: 1000
GET https://open.er-api.com/v6/latest/USD
1000.00 USD -> 1331080.00 ARS  (tasa: 1331.080000)
⚙️ Instalación y ejecución
1️⃣ Clonar repositorio
bash
Copiar
Editar
git clone https://github.com/kennysolorzano/conversor-moneda.git
cd conversor-moneda
2️⃣ Compilar proyecto
bash
Copiar
Editar
mvn -U clean package
3️⃣ Ejecutar aplicación
bash
Copiar
Editar
mvn -q exec:java "-Dexec.mainClass=com.kenny.conversor.ConversorApp"
📂 Estructura del proyecto
css
Copiar
Editar
conversor-moneda/
├── src/
│   ├── main/java/com/kenny/conversor/
│   │   ├── ConversorApp.java
│   │   ├── HttpRateProvider.java
│   │   ├── RateProvider.java
│   │   ├── ExchangerateHostClient.java
│   │   ├── ...
├── pom.xml
└── README.md
🛠 Tecnologías utilizadas
Java 17

Maven 3.9

Gson (para parseo de JSON)

HTTP Client nativo (Java 11+)

API pública de tasas de cambio: open.er-api.com

🔮 Mejoras implementadas (Extras)
✅ Historial de Conversiones: Se guarda cada conversión con fecha y hora.

✅ Más monedas: Soporte para más de 150 monedas usando códigos ISO.

✅ Registros con marca de tiempo: Uso de java.time.LocalDateTime para las conversiones.

✅ Validaciones robustas: Evita que el usuario introduzca valores inválidos o monedas no soportadas.

✅ Caché de tasas: Reduce llamadas innecesarias a la API.

🚀 Posibles mejoras futuras
Exportar historial de conversiones a CSV o JSON.

Interfaz gráfica con JavaFX o Swing.

Integración con APIs de criptomonedas (CoinGecko, Binance API).

Soporte multilenguaje.

🌐 Comparativa de APIs de tasas de cambio
API	Plan Gratuito	Límite de solicitudes	Cobertura de monedas	Criptomonedas	URL Oficial
ExchangeRate API	Sí	Ilimitado	+160	No	Visitar
Open Exchange Rates	Sí	1,000/mes	+170	No	Visitar
CoinGecko API	Sí	10-50/min	+50 (fiat) + 12,000 (cripto)	Sí	Visitar

📜 Licencia
Este proyecto se distribuye bajo la licencia MIT.
Puedes ver más detalles en el archivo LICENSE.

👨‍💻 Autor
Kenny Solórzano