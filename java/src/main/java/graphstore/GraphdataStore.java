package graphstore;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import graphstore.GraphStoreBasic.Empty;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Optional;

public class GraphdataStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphdataStore.class);

    protected Server server;
    protected ConfigReader config;


    public GraphdataStore(ConfigReader config) {
        this.config = config;
    }

    private void start(int port, int numThreads) throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new GraphdataStoreImpl())
                .executor(Executors.newFixedThreadPool(numThreads))
                .build()
                .start();
        LOGGER.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                GraphdataStore.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception{
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

        File configf = new File("../configs/configCentralized.txt");
        ConfigReader config = new ConfigReader(configf);

        final GraphdataStore server = new GraphdataStore(config);
        server.start(config.getBlockPort(), 2);
        server.blockUntilShutdown();

        System.out.println("Program Ended");
    }

    static class GraphdataStoreImpl extends GraphdataStoreGrpc.GraphdataStoreImplBase {

        //Map<String, byte[]> blockMap;

        GraphdataStoreImpl() {
            super();
            //this.blockMap = new HashMap<>();
        }

        @Override
        public void ping(Empty req, final StreamObserver<Empty> responseObserver) {
            Empty response = Empty.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        // TODO: Implement the other RPCs!

    }
}
