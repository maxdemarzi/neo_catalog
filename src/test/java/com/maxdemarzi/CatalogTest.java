package com.maxdemarzi;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.ArrayList;
import java.util.HashMap;

public class CatalogTest {
    @Rule
    public final Neo4jRule neo4j = new Neo4jRule()
            .withExtension("/v1", Service.class)
            .withFixture(TEST_DATA);

    @Test
    public void shouldGetProducts() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/service/catalog/0").toString());
        HashMap actual = response.content();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldGetProductsAtDepth1() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/service/catalog/0?depth=1").toString());
        HashMap actual = response.content();
        Assert.assertEquals(expectedAtDepth1, actual);
    }

    @Test
    public void shouldGetProductsAtDepth2() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/service/catalog/0?depth=2").toString());
        HashMap actual = response.content();
        Assert.assertEquals(expectedAtDepth2, actual);
    }

    private static final HashMap<String,Object> expectedAtDepth1 = new HashMap<String, Object>() {{
        put("id", "0");
        put("name", "Street Samurai Catalog");
        put("promotions",  new ArrayList<HashMap<String, Object>>());
        put("children", new ArrayList<HashMap<String, Object>>(){{
            add(new HashMap<String, Object>() {{
                put("id", "3");
                put("name", "Shotguns");
                put("promotions",  new ArrayList<HashMap<String, Object>>());
                put("children", new ArrayList<HashMap<String, Object>>());
            }});
            add(new HashMap<String, Object>() {{
                put("id", "2");
                put("name", "Pistols");
                put("promotions",  new ArrayList<HashMap<String, Object>>() {{
                    add(new HashMap<String, Object>() {{
                        put("id", "p2");
                        put("name", "Free Two-Day Shipping");
                    }});
                }});
                put("children", new ArrayList<HashMap<String, Object>>());
            }});
            add(new HashMap<String, Object>() {{
                put("id", "1");
                put("name", "Rifles");
                put("promotions",  new ArrayList<HashMap<String, Object>>());
                put("children",  new ArrayList<HashMap<String, Object>>());
            }});
        }});
    }};

    private static final HashMap<String,Object> expectedAtDepth2 = new HashMap<String, Object>() {{
        put("id", "0");
        put("name", "Street Samurai Catalog");
        put("promotions",  new ArrayList<HashMap<String, Object>>());
        put("children", new ArrayList<HashMap<String, Object>>(){{
            add(new HashMap<String, Object>() {{
                put("id", "3");
                put("name", "Shotguns");
                put("promotions",  new ArrayList<HashMap<String, Object>>());
                put("children", new ArrayList<HashMap<String, Object>>());
            }});
            add(new HashMap<String, Object>() {{
                put("id", "2");
                put("name", "Pistols");
                put("promotions",  new ArrayList<HashMap<String, Object>>() {{
                    add(new HashMap<String, Object>() {{
                        put("id", "p2");
                        put("name", "Free Two-Day Shipping");
                    }});
                }});
                put("children", new ArrayList<HashMap<String, Object>>(){{
                    add(new HashMap<String, Object>() {{
                        put("id", "21");
                        put("name", "Heavy");
                        put("promotions",  new ArrayList<HashMap<String, Object>>());
                        put("children",new ArrayList<HashMap<String, Object>>());
                    }});
                }});
            }});
            add(new HashMap<String, Object>() {{
                put("id", "1");
                put("name", "Rifles");
                put("promotions",  new ArrayList<HashMap<String, Object>>());
                put("children",  new ArrayList<HashMap<String, Object>>() {{
                    add(new HashMap<String, Object>() {{
                        put("id", "12");
                        put("name", "Machine Gun");
                        put("promotions",  new ArrayList<HashMap<String, Object>>());
                        put("children", new ArrayList<HashMap<String, Object>>());
                    }});
                    add(new HashMap<String, Object>() {{
                        put("id", "11");
                        put("name", "Sniper");
                        put("promotions",  new ArrayList<HashMap<String, Object>>());
                        put("children", new ArrayList<HashMap<String, Object>>());
                    }});
                }});
            }});
        }});
    }};

    private static final HashMap<String,Object> expected = new HashMap<String, Object>() {{
        put("id", "0");
        put("name", "Street Samurai Catalog");
        put("promotions",  new ArrayList<HashMap<String, Object>>());
        put("children", new ArrayList<HashMap<String, Object>>(){{
            add(new HashMap<String, Object>() {{
                put("id", "3");
                put("name", "Shotguns");
                put("promotions",  new ArrayList<HashMap<String, Object>>());
                put("children", new ArrayList<HashMap<String, Object>>());
            }});
            add(new HashMap<String, Object>() {{
                put("id", "2");
                put("name", "Pistols");
                put("promotions",  new ArrayList<HashMap<String, Object>>() {{
                    add(new HashMap<String, Object>() {{
                        put("id", "p2");
                        put("name", "Free Two-Day Shipping");
                    }});
                }});
                put("children", new ArrayList<HashMap<String, Object>>(){{
                    add(new HashMap<String, Object>() {{
                        put("id", "21");
                        put("name", "Heavy");
                        put("promotions",  new ArrayList<HashMap<String, Object>>());
                        put("children", new ArrayList<HashMap<String, Object>>() {
                            {
                                add(new HashMap<String, Object>() {{
                                    put("id", "211");
                                    put("name", "Ares Predator");
                                    put("promotions",  new ArrayList<HashMap<String, Object>>() {{
                                        add(new HashMap<String, Object>() {{
                                            put("id", "p211");
                                            put("name", "Includes Ares Smartgun Link");
                                        }});
                                    }});
                                    put("children", new ArrayList<HashMap<String, Object>>());
                                }});
                            }});
                    }});
                }});
            }});
            add(new HashMap<String, Object>() {{
                put("id", "1");
                put("name", "Rifles");
                put("promotions",  new ArrayList<HashMap<String, Object>>());
                put("children",  new ArrayList<HashMap<String, Object>>() {{
                    add(new HashMap<String, Object>() {{
                        put("id", "12");
                        put("name", "Machine Gun");
                        put("promotions",  new ArrayList<HashMap<String, Object>>());
                        put("children", new ArrayList<HashMap<String, Object>>());
                    }});
                    add(new HashMap<String, Object>() {{
                        put("id", "11");
                        put("name", "Sniper");
                        put("promotions",  new ArrayList<HashMap<String, Object>>());
                        put("children", new ArrayList<HashMap<String, Object>>());
                    }});
                }});
            }});
        }});
    }};

    private static final String TEST_DATA =
            "CREATE (root:Item {id:'0', name:'Street Samurai Catalog'})" +
            "CREATE (child1:Item {id:'1', name:'Rifles'})" +
            "CREATE (child11:Item {id:'11', name:'Sniper'})" +
            "CREATE (child12:Item {id:'12', name:'Machine Gun'})" +
            "CREATE (child2:Item {id:'2', name:'Pistols'})" +
            "CREATE (child21:Item {id:'21', name:'Heavy'})" +
            "CREATE (child211:Item {id:'211', name:'Ares Predator'})" +
            "CREATE (child3:Item {id:'3', name:'Shotguns'})" +
            "CREATE (promotion211:Promotion {id:'p211', name:'Includes Ares Smartgun Link'})" +
            "CREATE (promotion2:Promotion {id:'p2', name:'Free Two-Day Shipping'})" +
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

