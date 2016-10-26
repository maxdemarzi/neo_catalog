package com.maxdemarzi;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.ArrayList;
import java.util.HashMap;

public class PromotionsTest {

    @Rule
    public final Neo4jRule neo4j = new Neo4jRule()
            .withExtension("/v1", Service.class)
            .withFixture(TEST_DATA);

    @Test
    public void shouldGetPromotions() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/service/promotions/211").toString());
        HashMap actual = response.content();
        Assert.assertEquals(expected, actual);
    }

    private static final HashMap<String, ArrayList<HashMap<String, Object>>> expected = new HashMap<String, ArrayList<HashMap<String, Object>>>() {
        {
            put("promotions", new ArrayList<HashMap<String, Object>>() {{
                    add(new HashMap<String, Object>() {{
                        put("id", "p211");
                        put("name", "promo 211");
                    }});
                    add(new HashMap<String, Object>() {{
                        put("id", "p2");
                        put("name", "promo 2");
                    }});
                }});
        }};

    private static final String TEST_DATA =
            "CREATE (root:Item {id:'0', name:'Root'})" +
                    "CREATE (child1:Item {id:'1', name:'child1'})" +
                    "CREATE (child11:Item {id:'11', name:'child11'})" +
                    "CREATE (child12:Item {id:'12', name:'child12'})" +
                    "CREATE (child2:Item {id:'2', name:'child2'})" +
                    "CREATE (child21:Item {id:'21', name:'child21'})" +
                    "CREATE (child211:Item {id:'211', name:'child211'})" +
                    "CREATE (child3:Item {id:'3', name:'child3'})" +
                    "CREATE (promotion211:Promotion {id:'p211', name:'promo 211'})" +
                    "CREATE (promotion2:Promotion {id:'p2', name:'promo 2'})" +
                    "MERGE (root)-[:HAS_CHILD]->(child1)" +
                    "MERGE (child1)-[:HAS_CHILD]->(child11)" +
                    "MERGE (child1)-[:HAS_CHILD]->(child12)" +
                    "MERGE (root)-[:HAS_CHILD]->(child2)" +
                    "MERGE (child2)-[:HAS_CHILD]->(child21)" +
                    "MERGE (child21)-[:HAS_CHILD]->(child211)" +
                    "MERGE (root)-[:HAS_CHILD]->(child3)" +
                    "MERGE (child211)-[:HAS_PROMOTION]->(promotion211)" +
                    "MERGE (child2)-[:HAS_PROMOTION]->(promotion2)";


}
