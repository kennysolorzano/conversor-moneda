Conversor de Moneda

Pequeña app de consola en Java que convierte montos entre monedas usando HttpClient y Gson.

No requiere API key: consume el endpoint público de open.er-api.com.



🚀 Demo rápida

bash

Copiar

Editar

mvn -U clean package

\# PowerShell (Windows)

mvn -q exec:java "-Dexec.mainClass=com.kenny.conversor.ConversorApp"

\# macOS/Linux

mvn -q exec:java -Dexec.mainClass=com.kenny.conversor.ConversorApp

Salida de ejemplo:



bash

Copiar

Editar

GET https://open.er-api.com/v6/latest/USD

1000.00 USD -> 1331080.00 ARS  (tasa: 1331.080000)

🧱 Tecnologías

Java SDK 24 (target de compilación --release 17)



Maven 3.9+



Gson 2.10.1



Java HttpClient (Java 11+)



📦 Estructura

bash

Copiar

Editar

conversor-moneda/

├─ src/main/java/com/kenny/conversor/

│  ├─ ConversorApp.java          # Menú en consola (Scanner)

│  ├─ ConverterService.java      # Lógica de conversión

│  ├─ HttpRateProvider.java      # Cliente HTTP + parseo JSON

│  └─ RateProvider.java          # Interfaz (permite mocks / otros providers)

├─ pom.xml

└─ .gitignore

🔌 API utilizada (sin clave)

GET https://open.er-api.com/v6/latest/{BASE}

Respuesta JSON contiene conversion\_rates con las tasas respecto a {BASE}.



Nota: el código también tolera respuestas en formato rates (compatibilidad).



💡 ¿Qué hace?

Muestra un menú con opciones rápidas (USD⇄ARS, USD⇄BRL, USD→CLP, USD→COP) y una conversión libre ingresando códigos ISO.



Descarga las tasas (filtradas) y calcula: monto \* tasa.



Monedas soportadas por defecto: USD, ARS, BOB, BRL, CLP, COP (puedes ampliarlo).



⚙️ Requisitos

Java 11+ (desarrollo sobre JDK 24)



Maven 3.9+



Internet (para obtener tasas)



▶️ Cómo ejecutar

Con Maven Exec (recomendado):



bash

Copiar

Editar

mvn -U clean package

\# PowerShell

mvn -q exec:java "-Dexec.mainClass=com.kenny.conversor.ConversorApp"

\# macOS/Linux

mvn -q exec:java -Dexec.mainClass=com.kenny.conversor.ConversorApp

Desde IntelliJ IDEA:



Open el proyecto (Maven se importa solo).



File → Project Structure → Project SDK = JDK 24.



Crear Run Configuration → Application

Main class: com.kenny.conversor.ConversorApp → Run.



PowerShell a veces “rompe” -D.... Si ves Unknown lifecycle phase, usa comillas como en el ejemplo o --%:

mvn --% -q exec:java -Dexec.mainClass=com.kenny.conversor.ConversorApp



🧪 Tests

Aún no hay tests. Siguiente paso sugerido:



FakeRateProvider para pruebas determinísticas.



JUnit 5 para validar ConverterService.



🛠️ Configuración y notas

Proxy corporativo: si estás detrás de uno, crea C:\\Users\\<tu-usuario>\\.m2\\settings.xml con el bloque <proxies> de Maven.



Endpoint alternativo sin clave (opcional):

https://api.exchangerate.host/latest?base={BASE}\&symbols=USD,ARS,BOB,BRL,CLP,COP



📈 Roadmap / Extras (opcional)

Historial de conversiones (lista en memoria o archivo).



Más monedas (extender set soportado).



Registro con timestamps (java.time.Instant/LocalDateTime).



Manejo de reintentos con backoff si la API tarda.



Fat JAR ejecutable (maven-shade-plugin) para java -jar conversor.jar.



📝 Licencia

MIT

