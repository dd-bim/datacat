MERGE (n:User) SET n.roles = [x in n.roles | replace(x, "ROLE_", "")]
