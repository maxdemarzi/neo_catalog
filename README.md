# neo_catalog
POC Catalog and Hiearchy for Neo4j

# Instructions

1. Build it:

        mvn clean package

2. Copy target/catalog-1.0-SNAPSHOT.jar to the plugins/ directory of your Neo4j server.

3. Configure Neo4j by adding a line to conf/neo4j.conf:

        dbms.unmanaged_extension_classes=com.maxdemarzi=/v1

4. Start Neo4j server.

5. Create some sample data (optional)

        CREATE (root:Item {id:'0', name:'Street Samurai Catalog'})
        CREATE (child1:Item {id:'1', name:'Rifles'})
        CREATE (child11:Item {id:'11', name:'Sniper'})
        CREATE (child12:Item {id:'12', name:'Machine Gun'})
        CREATE (child2:Item {id:'2', name:'Pistols'})
        CREATE (child21:Item {id:'21', name:'Heavy'})
        CREATE (child211:Item {id:'211', name:'Ares Predator'})
        CREATE (child3:Item {id:'3', name:'Shotguns'})
        CREATE (promotion211:Promotion {id:'p211', name:'Includes Ares Smartgun Link'})
        CREATE (promotion2:Promotion {id:'p2', name:'Free Two-Day Shipping'})
        MERGE (root)-[:HAS_CHILD]->(child1)
        MERGE (child1)-[:HAS_CHILD]->(child11)
        MERGE (child1)-[:HAS_CHILD]->(child12)
        MERGE (root)-[:HAS_CHILD]->(child2)
        MERGE (child2)-[:HAS_CHILD]->(child21)
        MERGE (child21)-[:HAS_CHILD]->(child211)
        MERGE (root)-[:HAS_CHILD]->(child3)
        MERGE (child211)-[:HAS_PROMOTION]->(promotion211)
        MERGE (child2)-[:HAS_PROMOTION]->(promotion2)


6. Check that it is installed correctly over HTTP:

        :GET /v1/service/helloworld
        
7. Warm up the database:

        :GET /v1/service/warmup
                
8. Get the hiearchy (run at least twice to see actual speed):                 

        :GET /v1/service/catalog/0
        :GET /v1/service/catalog/0?depth=1
        :GET /v1/service/catalog/0?depth=2
               
9. Get the promotions:
        
        :GET /v1/service/promotions/211
        
        
Cypher queries:
        
        MATCH (parent:Item)-[:HAS_CHILD]->(item)
        RETURN parent, COLLECT(item) AS children
        
        MATCH (start:Item {id:'211'}) <-[:HAS_CHILD*0..999]-(item)-[:HAS_PROMOTION]->(p)
        RETURN {promotions: COLLECT(p)} AS promotions
        
        MATCH (start:Item {id:'211'}) <-[:HAS_CHILD*0..999]-(item)
        WITH COLLECT (item) AS items
        UNWIND items AS item 
        MATCH (item)-[:HAS_PROMOTION]->(p)
        RETURN {promotions: COLLECT(p)} AS promotions