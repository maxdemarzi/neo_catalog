package com.maxdemarzi;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.*;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Path("/service")
public class Service {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HashMap<Long, HashMap> properties = new HashMap<>(1_00_000);
    private static final HashMap<String, Long> roots = new HashMap<>();

    @GET
    @Path("/helloworld")
    public Response helloWorld() throws IOException {
        Map<String, String> results = new HashMap<String,String>(){{
            put("hello","world");
        }};
        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }

    @GET
    @Path("/warmup")
    public Response warmUp(@Context GraphDatabaseService db) throws IOException {
        Map<String, String> results = new HashMap<String,String>(){{
            put("warmed","up");
        }};

        try (Transaction tx = db.beginTx()) {
            for (Node node : db.getAllNodes()) {
                properties.put(node.getId(), (HashMap) node.getAllProperties());
                node.getPropertyKeys();
                for (Relationship relationship : node.getRelationships()) {
                    relationship.getPropertyKeys();
                    relationship.getStartNode();
                }
            }

            for (Relationship relationship : db.getAllRelationships()) {
                relationship.getPropertyKeys();
                relationship.getNodes();
            }
            tx.success();
        }

        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }

    @GET
    @Path("/catalog/{id}")
    @Produces({"application/json"})
    public Response catalog(@PathParam("id") String id, @DefaultValue("999") @QueryParam("depth") int maxDepth, @Context final GraphDatabaseService db) throws IOException {
        // Since graph is so small, we're going to pre-load it into memory the first time this end point is called
        loadPropertiesCache(db);
        // We will stream our output so we don't build a big nested object or hashmap
        StreamingOutput stream = os -> {
            JsonGenerator jg = objectMapper.getJsonFactory().createJsonGenerator(os, JsonEncoding.UTF8);
            // All Neo4j access requires a transaction
            try (Transaction tx = db.beginTx()) {
                // Let's find our root node and save it so next time we skip the search
                final Node root;
                if (roots.containsKey(id)) {
                    root = db.getNodeById(roots.get(id));
                } else {
                    root = db.findNode(Labels.Item, "id", id);
                    roots.put(id, root.getId());
                }
                // Start by sending the root at depth zero and maxdepth
                if (root != null) {
                    doStuffRecursively(root, jg, 0, maxDepth);
                }

                tx.success();
            } catch (Exception e) {
                e.printStackTrace();
            }
            jg.flush();
            jg.close();
        };
        return Response.ok().entity(stream).type(MediaType.APPLICATION_JSON).build();
    }

    private void doStuffRecursively(Node node, JsonGenerator jg, int depth, int maxDepth) throws IOException {
        // Write our object but don't close it since we will include Promotions and children
        jg.writeStartObject();
        for (Map.Entry<String, Object> entry : ((HashMap<String, Object>) properties.get(node.getId())).entrySet()) {
            jg.writeFieldName(entry.getKey());
            jg.writeObject(entry.getValue());
        }
        // add promotions by following the HAS_PROMOTION relationship
        jg.writeArrayFieldStart("promotions");
        for (Relationship rel : node.getRelationships(RelationshipTypes.HAS_PROMOTION, Direction.OUTGOING)) {
            Node promotion = rel.getEndNode();
            jg.writeObject(properties.get(promotion.getId()));
        }
        jg.writeEndArray();

        // add children if we haven't passed our max depth yet
        jg.writeArrayFieldStart("children");
        depth++;
        if (depth <= maxDepth) {
            // recursively do the same thing for every child node
            for (Relationship rel : node.getRelationships(RelationshipTypes.HAS_CHILD, Direction.OUTGOING)) {
                Node nextNode = rel.getEndNode();
                doStuffRecursively(nextNode, jg, depth, maxDepth);
            }
        }
        // close out children and object
        jg.writeEndArray();
        jg.writeEndObject();
    }


    private void loadPropertiesCache(@Context GraphDatabaseService db) {
        if (properties.isEmpty()) {
            try (Transaction tx = db.beginTx()) {
                for (Node node : db.getAllNodes()) {
                    properties.put(node.getId(), (HashMap) node.getAllProperties());
                }
                tx.success();
            }
        }
    }

}
