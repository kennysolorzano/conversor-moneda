# 💱 Conversor de Moneda

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Maven](https://img.shields.io/badge/Maven-3.9-orange?logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-green)
![Status](https://img.shields.io/badge/Status-Finalizado-brightgreen)

Conversor de monedas desarrollado en **Java 17** como parte del *Challenge ONE*.  
Permite realizar conversiones entre distintas divisas en tiempo real utilizando la API pública de **ExchangeRate**.

---

## 📋 Características

- Conversión entre monedas predefinidas:
  - USD ↔ ARS
  - USD ↔ BRL
  - USD ↔ CLP
  - USD ↔ COP
- Opción de **conversión libre** mediante código ISO.
- Obtención de tasas de cambio en tiempo real desde la API.
- Interfaz por consola sencilla y fácil de usar.
- Código modular y mantenible.

---

## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Maven**
- **Gson** para manejo de JSON
- **HttpClient** (Java) para consumo de API REST
- API: [ExchangeRate](https://open.er-api.com/)

---

## 🚀 Ejecución del proyecto

### 1️⃣ Clonar el repositorio
```bash
git clone https://github.com/kennysolorzano/conversor-moneda.git
cd conversor-moneda
2️⃣ Compilar y empaquetar
bash
Copiar
Editar
mvn clean package
3️⃣ Ejecutar
bash
Copiar
Editar
mvn exec:java -Dexec.mainClass=com.kenny.conversor.ConversorApp
📦 Estructura del proyecto
bash
Copiar
Editar
conversor-moneda/
│
├── src/
│   ├── main/
│   │   ├── java/com/kenny/conversor/
│   │   │   ├── ConversorApp.java
│   │   │   ├── Conversor.java
│   │   │   ├── ExchangerateHostClient.java
│   │   │   └── RateProvider.java
│   └── test/  # (Opcional, si agregas pruebas)
│
├── pom.xml
└── README.md
📖 Uso
Al iniciar, se mostrará un menú con opciones de conversión.

Selecciona la opción deseada.

Ingresa el monto a convertir.

El sistema mostrará el resultado usando la tasa de cambio actual.

Ejemplo:

markdown
Copiar
Editar
******************************************************
Sea bienvenido/a al Conversor de Moneda

1) Dólar (USD)  => Peso argentino (ARS)
2) Peso argentino (ARS) => Dólar (USD)
...

Elija una opción válida:
******************************************************
1
Monto: 1000
GET https://open.er-api.com/v6/latest/USD
1000.00 USD -> 1331080.00 ARS  (tasa: 1331.080000)
📝 Mejoras futuras (Opcional)
Historial de conversiones.

Soporte para más monedas.

Registros con fecha y hora usando java.time.

Interfaz gráfica en JavaFX o Swing.

📄 Licencia
Este proyecto está bajo la licencia MIT.