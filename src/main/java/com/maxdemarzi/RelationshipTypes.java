package com.maxdemarzi;

import org.neo4j.graphdb.RelationshipType;

enum RelationshipTypes implements RelationshipType {
    HAS_CHILD,
    HAS_PROMOTION
}
