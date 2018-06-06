package pluradj.janusgraph.example;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Optional;


public class JavaExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaExample.class);

    public static void main(String[] args) {
        //JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-berkeleyje-lucene.properties");
        //GraphTraversalSource g = graph.traversal();
        //if (g.V().count().next() == 0) {
            // load the schema and graph data
            //GraphOfTheGodsFactory.load(graph);
        //}
        //Map<String, ?> saturnProps = g.V().has("name", "saturn").valueMap(true).next();
        //LOGGER.info(saturnProps.toString());
        //List<Edge> places = g.E().has("place", Geo.geoWithin(Geoshape.circle(37.97, 23.72, 50))).toList();
        //LOGGER.info(places.toString());
        //System.exit(0);
        //JanusGraph graph = JanusGraphFactory.build()
        //        .set("storage.backend", "hbase")
        //        .open();
        JanusGraph graph = JanusGraphFactory.open("conf/janusgraph-hbase.properties");
        //graph = JanusGraphFactory.open("inmemory");
        GraphTraversalSource g;
        g = graph.traversal();

        //g.addV("person").property("name", "ashwin").next();
        //g.addV("person").property("name", "ramesh").next();
        //System.out.println(g.V().has("name","ashwin"));
        //System.out.println(g.V().has("name","ashwin").id());
        final Optional<Map<String, Object>> v = g.V().has("name", "saturn").valueMap(true).tryNext();
        if (v.isPresent()) {
            LOGGER.info(v.get().toString());
        } else {
            LOGGER.warn("saturn not found");
        }

        //Vertex xyz = graph.addVertex();

        //v[4344]

        // Add a property
        //xyz.property("name", "ramesh");

        // Commit the transaction
        //graph.tx().commit();

        // Check to make sure our new vertex was created
        //g.V().has("name","ramesh");

        //v[4344]

        //final Optional<Map<String, Object>> v = g.V().has("name", "me").valueMap(true).tryNext();
        //if (v.isPresent()) {
            //LOGGER.info(v.get().toString());
        //} else {
            //LOGGER.warn("me not found");
        //}

        System.out.println("Program Ended");
    }
}
