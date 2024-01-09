MATCH (x:XtdProperty) - [:ASSIGNS_MEASURE] -> (y:XtdRelAssignsMeasures) - [:ASSIGNS_MEASURE] -> (z:XtdMeasureWithUnit) CREATE (x) - [:POSSIBLE_VALUES] -> (z) DETACH DELETE y RETURN x,y,z
MATCH (n:XtdMeasureWithUnit) SET n:XtdValueList REMOVE n:XtdMeasureWithUnit RETURN n
