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

        CREATE (root:Item {id: '0', name:'Root'})
        CREATE (child1:Item {name:'child1'})
        CREATE (child11:Item {name:'child11'})
        CREATE (child12:Item {name:'child12'})
        CREATE (child2:Item {name:'child2'})
        CREATE (child21:Item {name:'child21'})
        CREATE (child211:Item {name:'child211'})
        CREATE (child3:Item {name:'child3'})
        MERGE (root)-[:HAS_CHILD]->(child1)
        MERGE (child1)-[:HAS_CHILD]->(child11)
        MERGE (child1)-[:HAS_CHILD]->(child12)
        MERGE (root)-[:HAS_CHILD]->(child2)
        MERGE (child2)-[:HAS_CHILD]->(child21)
        MERGE (child21)-[:HAS_CHILD]->(child211)
        MERGE (root)-[:HAS_CHILD]->(child3)


6. Check that it is installed correctly over HTTP:

        :GET /v1/service/helloworld
        
7. Warm up the database:

        :GET /v1/service/warmup
                
8. Get the hiearchy (run at least twice to see actual speed):                 

        :GET /v1/service/catalog/0
        :GET /v1/service/catalog/0?depth=1
        :GET /v1/service/catalog/0?depth=2