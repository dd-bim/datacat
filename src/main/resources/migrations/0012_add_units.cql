UNWIND [
["Second","Sekunde","s"],
["Minute","Minute","min"],
["Hour","Stunde","h"],
["Day","Tag","d"],
["Hertz","Hertz","Hz"],
["Baud","Baud","Bd"],
["Meter","Meter","m"],
["Liter","Liter","L"],
["Radian","Radiant","rad"],
["Steradian","Steradiant","sr"],
["Revolution","Umdrehung","r"],
["Degree","Grad","o"],
["Bit","Bit","bit"],
["Byte","Octet","Byte"," Oktett","B"],
["Gram","Gramm","g"],
["Ton","Tonne","t"],
["Unified Atomic Mass Unit","Einheitliche Atomare Masseneinheit","u"],
["Mole","Mol","mol"],
["Katal","Katal","kat"],
["Kelvin","Kelvin","K"],
["Degree Celsius","Grad Celsius","oC"],
["Candela","Candela","cd"],
["Lumen","Lumen","lm"],
["Lux","Lux","lx"],
["Newton","Newton","N"],
["Pascal","Pascal","Pa"],
["Joule","Joule","J"],
["Electronvolt","Elektronvolt","eV"],
["Watt","Watt","W"],
["Neper","Neper","Np"],
["Decibel","Dezibel","dB"],
["Ampere","Ampere","A"],
["Coulomb","Coulomb","C"],
["Volt","Volt","V"],
["Farad","Farad","F"],
["Ohm","Ohm","Ohm"],
["Siemens","Siemens","S"],
["Weber","Weber","Wb"],
["Tesla","Tesla","T"],
["Henry","Henry","H"],
["Becquerel","Becquerel","Bq"],
["Gray","Gray","Gy"],
["Sievert","Sievert","Sv"],
["Square Meter","Quadratmeter","m^2"],
["Cubic Meter","Kubikmeter","m^3"],
["Meter Per Second","Meter pro Sekunde","m/s"],
["Meter Per Second Squared","Meter pro Sekunde zum Quadrat","m/s^2"],
["Reciprocal Meter","Reziproker Meter","m^-1"],
["Kilogram Per Cubic Meter","Kilogramm pro Kubikmeter","kg/m^3"],
["Cubic Meter Per Kilogram","Kubikmeter pro Kilogramm","m^3/kg"],
["Ampere Per Square Meter","Ampere pro Quadratmeter","A/m^2"],
["Ampere Per Meter","Ampere pro Meter","A/m"],
["Mole Per Cubic Meter","Mol pro Kubikmeter","mol/m^3"],
["Candela Per Square Meter","Candela pro Quadratmeter","cd/m^2"],
["Radian Per Second","Radiant pro Sekunde","rad/s"],
["Radian Per Second Squared","Radiant pro Sekunde zum Quadrat","rad/s^2"],
["Pascal Second","Pascal Sekunde","Pa.s"],
["Newton Meter","Newtonmeter","N.m"],
["Newton Per Meter","Newton pro Meter","N/m"],
["Watt Per Square Meter","Watt pro Quadratmeter","W/m^2"],
["Watt Per Steradian","Watt pro Steradiant","W/sr"],
["Watt Per Square Meter Steradian","Watt pro Quadratmeter Steradiant","W/(m^2.sr)"],
["Joule Per Kelvin","Joule pro Kelvin","J/K"],
["Joule Per Kilogram Kelvin","Joule pro Kilogramm Kelvin","J/(kg.K)"],
["Joule Per Kilogram","Joule pro Kilogramm","J/kg"],
["Watt Per Meter Kelvin","Watt pro Meter Kelvin","W/(m.K)"],
["Joule Per Cubic Meter","Joule pro Kubikmeter","J/m^3"],
["Volt Per Meter","Volt pro Meter","V/m"],
["Coulomb Per Cubic Meter","Coulomb pro Kubikmeter","C/m^3"],
["Coulomb Per Square Meter","Coulomb pro Quadratmeter","C/m^2"],
["Farad Per Meter","Farad pro Meter","F/m"],
["Henry Per Meter","Henry pro Meter","H/m"],
["Joule Per Mole","Joule pro Mol","J/mol"],
["Joule Per Mole Kelvin","Joule pro Mol Kelvin","J/(mol.K)"],
["Coulomb Per Kilogram","Coulomb pro Kilogramm","C/kg"],
["Revolution Per Minute","Umdrehungen pro Minute","r/min"],
["Katal Per Cubic Meter","Katal pro Kubikmeter","kat/m^3"],
["Mebibit Per Second","Mebibit pro Sekunde","Mib/s"],
["Nanovolt Per Root Hertz","Nanovolt pro Quadratwurzel Hertz","nV/Hz^(1/2)"]
] AS row 
MERGE (x:XtdUnit:XtdConcept:XtdObject:XtdRoot:CatalogRecord:Entity {id: randomUUID()})
ON CREATE SET 
    x.`labels.en` = row[0], 
    x.`labels.de` = row[1], 
    x.created = toString(datetime()), 
    x.createdBy = "SYSTEM", 
    x.lastModified = toString(datetime()), 
    x.lastModifiedBy = "SYSTEM",
    x.majorVersion = 1,
    x.minorVersion = 0,
    x.status = "XTD_ACTIVE"
MERGE (y:XtdText:XtdRoot:CatalogRecord:Entity {id: randomUUID()})
ON CREATE SET 
    y.text = row[2], 
    y.created = toString(datetime()), 
    y.createdBy = "SYSTEM", 
    y.lastModified = toString(datetime()), 
    y.lastModifiedBy = "SYSTEM"
MERGE (z:XtdMultiLanguageText:XtdRoot:CatalogRecord:Entity {id: randomUUID()})
ON CREATE SET 
    z.created = toString(datetime()), 
    z.createdBy = "SYSTEM", 
    z.lastModified = toString(datetime()), 
    z.lastModifiedBy = "SYSTEM"
MERGE (a:XtdText:XtdRoot:CatalogRecord:Entity {id: randomUUID()})
ON CREATE SET 
    a.text = row[1], 
    a.created = toString(datetime()), 
    a.createdBy = "SYSTEM", 
    a.lastModified = toString(datetime()), 
    a.lastModifiedBy = "SYSTEM"
MERGE (b:XtdMultiLanguageText:XtdRoot:CatalogRecord:Entity {id: randomUUID()})
ON CREATE SET 
    b.created = toString(datetime()), 
    b.createdBy = "SYSTEM", 
    b.lastModified = toString(datetime()), 
    b.lastModifiedBy = "SYSTEM"
MERGE (c:XtdText:XtdRoot:CatalogRecord:Entity {id: randomUUID()})
ON CREATE SET 
    c.text = row[0], 
    c.created = toString(datetime()), 
    c.createdBy = "SYSTEM", 
    c.lastModified = toString(datetime()), 
    c.lastModifiedBy = "SYSTEM"
MERGE (x)-[:SYMBOL]->(z)
MERGE (z)-[:TEXTS]->(y)
MERGE (x)-[:NAMES]->(b)
MERGE (b)-[:TEXTS]->(a)
MERGE (b)-[:TEXTS]->(c)
WITH x, y, a, c
MATCH (l:XtdLanguage {code: "en"})
MATCH (ll:XtdLanguage {code: "de"})
MATCH (t:Tag {id:"09da1ebb-8641-47fa-b82e-8588c7fef09e"})
MERGE (y)-[:LANGUAGE]->(l)
MERGE (c)-[:LANGUAGE]->(l)
MERGE (a)-[:LANGUAGE]->(ll)
MERGE (x)-[:TAGGED]->(t)
RETURN x;