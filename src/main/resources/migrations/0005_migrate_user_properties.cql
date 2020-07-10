MATCH (n:User) WHERE NOT exists(n.expired) SET n.expired = false
MATCH (n:User) WHERE NOT exists(n.credentialsExpired) SET n.credentialsExpired = false
MATCH (n:User) WHERE NOT exists(n.emailConfirmed) SET n.emailConfirmed = false
MATCH (n:User) WHERE NOT exists(n.locked) SET n.locked = false
