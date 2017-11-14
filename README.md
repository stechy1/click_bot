# Click bot
Jedná se o jednoduchou verzi klikacího bota psaného v jazyce Java.

## Funkce
- Nastavitelný počet opakování
- Možnost uchovávat body kliknutí v samostatném souboru jako konstanty
- Vlastní pojmenování konstant bodu
- Vnořené smyčky

## Akce
- Kliknutí levým i pravým tlačítkem myši
- Plynulý pohyb myší z bodu A do bodu B
- Vstup z klávesnice

## Spuštění bota
Ke spuštění je potřeba mít nainstalovanou Javu verze alespoň 8.
### Příkaz spuštění
 ```
 java -jar click_bot-1.0.jar [cesta k souboru s konstantami] cesta k souboru s akcemi
 ```

### Parametry
  - cesta k souboru s konstantami - první nepovinný parametr;
  nastavuje cestu k souboru s konstantami bodů
  - cesta k souboru s akcemi - jediný povinný parametr;
  nastavuje cestu k souboru s akcemi, které se mají vykonávat

### Struktura souboru s konstantami bodů
```
<?xml version="1.0" encoding="UTF-8" standalone="no" ?>
<points>
    <point name="constant_name_1" x="560" y="528"/>
    <point name="constant_name_2" x="600" y="528"/>
</points>
```

### Struktura souboru s jednotlivými akcemi
```
<?xml version="1.0" encoding="UTF-8" standalone="no" ?>
<actions delay="3000" repeat="10">
    <!-- Kliknutí levým tlačítkem myši -->
    <action type="click" button="left">
        <!-- Nastavení bodu přímo -->
        <point x="15" y="20"/>
    </action>

    <!-- Kliknutí levým pravým myši -->
    <action type="click" button="right">
        <!-- Nastavení bodu pomocí reference -->
        <point constant="constant_name_1"/>
    </action>

    <!-- Plynulý pohyb myši -->
    <action type="move">
        <!-- V tomto případě se do point vloží souřadnice -->
        <!-- o kolik se má myš posunout -->
        <point x="0" y="20"/>
        <!-- Počet kroků (více - plynulejší, ale pomalejší) -->
        <step>10</step>
    </action>

    <!-- Pauza v ms -->
    <action type="delay">100</action>

    <!-- Napsání textu -->
    <action type="write">Toto se napíše</action>

    <!-- Cyklus, lze vnořovat více cyklů do sebe -->
    <action type="cycle" repeat="5">
      <!-- Akce, které se provedou ve smyčce -->
      <action type="write">Ahoj</action>
    </action>
</actions>
```