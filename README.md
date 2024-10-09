# OTSW - Oprogramowanie Testujące Serwer WebSocketowy

## Spis Treści

- [Status Projektu](#status-projektu)
- [Opis Projektu](#opis-projektu)
- [Architektura](#architektura)
- [Technologie](#technologie)
- [Instalacja i Uruchomienie](#instalacja-i-uruchomienie)
- [Konfiguracja](#konfiguracja)
- [Testowanie](#testowanie)

## Status Projektu

![](https://img.shields.io/badge/status-in%20progress-yellow)

## Opis Projektu

**OTSW (Oprogramowanie Testujące Serwer WebSocketowy)** to narzędzie służące do testowania wydajności i stabilności serwera WebSocket. Projekt składa się z trzech głównych komponentów:

1. **Serwer WebSocket** napisany w Javie, który ma być testowany.
2. **Aplikacja testująca** (Java + JavaFX) umożliwiająca symulowanie komunikacji między serwerem a klientami.
3. **Klienci** w różnych językach programowania, którzy mogą być uruchamiani w dowolnej liczbie przez aplikację testującą i wykorzystywani do wysyłania wiadomości do serwera.

Celem tego projektu jest umożliwienie w prosty i intuicyjny sposób testowania serwera WebSocket w różnych scenariuszach obciążenia i zachowania klientów.

## Architektura

Projekt OTSW składa się z następujących modułów:

1. **Serwer WebSocket**:

   - Serwer WebSocket obsługujący połączenia od klientów.
   - Wysyła i odbiera wiadomości w czasie rzeczywistym.

2. **Aplikacja Testująca**:

   - Interfejs graficzny zbudowany z wykorzystaniem **JavaFX**.
   - Pozwala na uruchomienie dowolnej liczby klientów w różnych językach.
   - Kontroluje zachowanie klientów, takich jak częstotliwość wysyłania wiadomości, wielkość wiadomości, itp.

3. **Klienci**:
   - Klienci są zaimplementowani w różnych językach programowania (Java, Python).
   - Używają protokołu WebSocket do komunikacji z serwerem.
   - Aplikacja testująca pozwala na automatyczne uruchamianie wielu instancji klientów i zdalne sterowanie ich zachowaniem.

## Technologie

Projekt wykorzystuje następujące technologie:

- **Java (JDK 23)** - do implementacji serwera i aplikacji testującej.
- **JavaFX** - do budowy interfejsu graficznego aplikacji testującej.
- **WebSocket API** - do obsługi komunikacji w czasie rzeczywistym.
- **Różne języki programowania** – klienci są zaimplementowani w różnych technologiach:
  - **Java**
  - **Python**

## Instalacja i Uruchomienie

W przyszłości

## Konfiguracja

W przyszłości

## Testowanie

### Możliwe scenariusze testowe:

- Testy wydajnościowe (np. wysyłanie dużej liczby wiadomości w krótkim czasie).
- Testy stabilności (np. długotrwałe połączenia).
- Testy w warunkach obciążenia (np. 1000 klientów jednocześnie).
