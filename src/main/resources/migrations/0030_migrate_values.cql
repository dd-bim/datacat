MATCH (n:XtdValue) SET n.toleranceType = replace(n.toleranceType, "Xtd", "") SET n.valueRole = replace(n.valueRole, "Xtd", "") SET n.valueType = replace(n.valueType, "Xtd", "") RETURN n
MATCH (n:XtdValue) WHERE n.toleranceType = "Nil" SET n.toleranceType = null RETURN n
MATCH (n:XtdValue) WHERE n.valueRole = "Nil" SET n.valueRole = null RETURN n
MATCH (n:XtdValue) WHERE n.valueType = "Nil" SET n.valueType = null RETURN n
