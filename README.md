# JavaFX Elevator

Semestrálna úloha v JavaFX: animovaný výťah sa pohybuje medzi poschodiami 0 až 8 a spracúva požiadavky používateľa v poradí.

## Funkcie

- výťah vykreslený ako obdĺžnik,
- tlačidlá poschodí 0 až 8,
- zobrazenie aktuálneho aj cieľového poschodia,
- plynulý pohyb medzi poschodiami,
- front požiadaviek zadaných počas jazdy,
- dvojsekundové čakanie pred spracovaním ďalšej požiadavky.

## Spustenie

Projekt vyžaduje JDK 21 a Maven.

```bash
mvn clean javafx:run
```

V IntelliJ IDEA otvorte koreňový súbor `pom.xml` ako Maven projekt a spustite cieľ `javafx:run`.

## Scene Builder

Rozhranie je uložené v súbore:

`src/main/resources/sk/koshi/elevator/elevator-view.fxml`

Súbor je možné otvoriť priamo v Scene Builderi alebo cez IntelliJ IDEA po nastavení cesty k aplikácii Scene Builder.
