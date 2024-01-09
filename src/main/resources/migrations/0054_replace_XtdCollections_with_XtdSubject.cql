MATCH (x:XtdBag) SET x:XtdSubject, x:XtdObject REMOVE x:XtdCollection, x:XtdBag RETURN x
MATCH (x:XtdNest) SET x:XtdSubject, x:XtdObject REMOVE x:XtdCollection, x:XtdNest RETURN x