package com.maxdemarzi;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.HashMap;

public class WarmUpTest {
    @Rule
    public final Neo4jRule neo4j = new Neo4jRule()
            .withFixture(MODEL_STATEMENT)
            .withExtension("/v1", Service.class);

    @Test
    public void shouldRespondToWarmUp() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/service/warmup").toString());
        HashMap actual = response.content();
        Assert.assertEquals(expected, actual);
    }

    private static final HashMap expected = new HashMap<String, Object>() {{
        put("warmed", "up");
    }};

    private static final String MODEL_STATEMENT =
            // Create 2 nodes and a relationship, each with a property
            "CREATE (n1:Item {key:'value'})" +
                    "CREATE (n2:Item {key:'value'})" +
                    "CREATE (n1)-[:RELATED {key:'value'}]->(n2)";
}

