# lexware2shopware

Konvertiert einen Lexware XML Export in eine Shopware Instanz per API.

Basiert vollständig auf Spring Batch und Spring Boot.

## Funktion

Es wird ein Lexware XML Export als Datei entgegegen genommen und daraus werden die

* Katgorien aus dem ELement <CATALOG_GROUP_SYSTEM>
* Artikel aus den Elementen <ARTICLE>
* Zuweisungen von Artikeln zu Kategorien aus den Elementen <ARTICLE_TO_CATALOGGROUP_MAP>

per API zu Shopware übertragen.

Sofern die ein Objekt bereits in Shopware existiert, wird es erneuert (Update per PATCH),
ansonsten erzeugt (Create per POST).

Folgende Daten werden von Lexware übernommen:

### Kategorie-Import

Ein Element:

```
<CATALOG_STRUCTURE type="root">
    <GROUP_ID>1</GROUP_ID>
    <GROUP_NAME>PRODUKTGRUPPEN</GROUP_NAME>
	<PARENT_ID>0</PARENT_ID>
	<USER_DEFINED_EXTENSIONS><UDX.ARTICLE_SORT_ORDER>0</UDX.ARTICLE_SORT_ORDER></USER_DEFINED_EXTENSIONS>
</CATALOG_STRUCTURE>
```

* GROUP_ID: Übernahme als Shopware-KategorieID `categories->id`
  Das Attribut "type" wird ebenfalls ausgewertet, aber nicht in Shopware importiert
* GROUP_NAME: Übernahme als Name der Kategorie
* PARENT_ID: Übernahme als Eltern-Kategorie

Alle anderen Elemente werden ignoriert

### Artikel-Import

Ein Element:

```
<ARTICLE>
    <SUPPLIER_AID>153</SUPPLIER_AID>
    <MIME_INFO/>
	<ARTICLE_ORDER_DETAILS><ORDER_UNIT>C62</ORDER_UNIT></ARTICLE_ORDER_DETAILS>
	<ARTICLE_DETAILS>
	    <DESCRIPTION_SHORT>Kurzbeschreibung</DESCRIPTION_SHORT>
		<DESCRIPTION_LONG>Langbeschreibung</DESCRIPTION_LONG>
    </ARTICLE_DETAILS>
	<ARTICLE_FEATURES>
	    <FEATURE><FNAME>Gewicht</FNAME><FVALUE>0.010000</FVALUE></FEATURE>
		<FEATURE><FNAME>artikelnr</FNAME><FVALUE>artikelnummer_1000</FVALUE></FEATURE>
		<FEATURE><FNAME>beschreibung</FNAME></FEATURE>
		<FEATURE><FNAME>menge_bestand</FNAME><FVALUE>10.00</FVALUE></FEATURE>
    </ARTICLE_FEATURES>
	<ARTICLE_PRICE_DETAILS>
	    <ARTICLE_PRICE type="net_list">
	        <PRICE_AMOUNT>99.9900</PRICE_AMOUNT>
	        <TAX>0.1900</TAX>
	    </ARTICLE_PRICE>
    </ARTICLE_PRICE_DETAILS>
</ARTICLE>
```

* SUPPLIER_AID: Wird als `supplierNumber` (Herstellernummer) übernommen
* ARTICLE_DETAILS>DESCRIPTION_SHORT: Wird als `description` (Kurzbeschreibung) übernommen
* ARTICLE_DETAILS>DESCRIPTION_LONG: Wird als `descriptionLong` (Langbeschreibung) übernommen, inkl. HTML Elementen
* ARTICLE_PRICE_DETAILS>ARTICLE_PRICE>PRICE_AMOUNT: Wird als `prices>price` übernommen (Shopkunden Brutto)
  Achtung: Beim Import wird die Felder `from`, `to`, `pseudoPrice`, `percent` automatisch gesetzt
  Ausserdem wird die `priceGroupId` auf den konfigurierten Wert (siehe unten) gesetzt und aktiviert (`priceGroupActive`)
* ARTICLE_PRICE_DETAILS>ARTICLE_PRICE>TAX: Wird als Steuersatz (multipliziert mit 100) übernommen
  
Bei den ARTICLE_FEATURES werden folgende Werte in FNAME ausgewertet:

* Gewicht: Wird als `mainDetail>weight` (Gewicht) übernommen
* artikelnr: Wird als `mainDetail>number` (Artikelnummer) übernommen
* menge_bestand: Wird als `mainDetail>inStock` (Lagerbestand) übernommen

Alle Artikel die erstmalig mit gemeldeten Bestand größer 0 importiert werden, werden automatisch aktiviert (`active`)
alle anderen werden deaktiviert.

#### Anpassungen

Alle Modfikationen nehmen in Masse die `Processor`en vor, siehe `space.schellenberger.etl.shopware2lexware.step.*.*Processor`
Umwandlungen von Feldern von Lexware zu Shopware werden meist über 
JSON und XML Annotationen getätigt, siehe die DTOs in den Packages 
`space.schellenberger.etl.shopware2lexware.dto`
Es gibt einzelne Ausnahmen, in den Gettern/Settern der DTOs 
(bspw. bei der Normalisierung der Artikelnummer).

## Konfiguration

Unter /resources sind alle verfügbaren Konfiguationen in der application.yaml hinterlegt.

Alle Parameter können, wie bei Spring Boot üblich auch per Kommandozeile übergebgen werden, 
bspw: ``--config.lexwareXMLDatei="E:/temp/catalog_text.xml"``

Folgende Paramter konfigurieren den Import selbst:

### Zugriff auf Shopware per API

* config.shopware.rootURI

  Die URI zur Shopware-Instanz, bspw. http://192.168.74.99/
  
* config.shopware.apiUser

  Der Benutzer, der die Shopware API benutzen soll und darf
  Siehe dazu in den Shopware-Einstellungen -> Benutzerverwaltung
  
* config.shopware.apiPassword

  Der API-Schlüssel des Benutzers, kann in der Benutzerverwaltung von Shopware 
  erzeugt werden

### Import-Konfiguration

* config.shopware.priceGroupId

  Legt die sog. priceGroupId fest, die beim Anlegen von Artikeln benutzt werden soll. 
  Die Id sollte existieren, siehe dazu Tabelle ``s_core_pricegroups``