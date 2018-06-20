package graphstore;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;

public class GraphdataStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphdataStore.class);

    JanusGraph graph;
    GraphTraversalSource g;
    String analysisPath;
    long nodeCsvId;
    long edgeCsvId;
    boolean firstCsvAppend;
    boolean captureGephiData;
    Map<String, Long> nodeCsvIdDict;
    Map<String, String> personMap;

    public GraphdataStore() {
        //ConfiguredGraphFactory.open("graph1");
        graph = JanusGraphFactory.open("conf/janusgraph-hbase.properties");
        g = graph.traversal();
        analysisPath = "analysis/";
        nodeCsvId = 1;
        edgeCsvId = 1;
        firstCsvAppend = true;
        captureGephiData = true;
        nodeCsvIdDict = new HashMap<>();
        personMap = new HashMap<String,String>() {
            {
                put("President Trump", "Trump");
                put("Mr. Trump", "Trump");
                put("Mr. Kim", "Kim Jong-un");
            }
        };
    }

    private void writeToFile(String fname, String data) throws IOException {
        PrintWriter writer = new PrintWriter(analysisPath+fname, "UTF-8");
        writer.println(data);
        writer.close();
    }

    private void writeToCsvFile(String fname, String data) throws IOException {
        //nodes.csv-Id,Label,Entity
        //edges.csv-Source,Target,Type,Id,Label,Weight
        Writer output;
        output = new BufferedWriter(new FileWriter(analysisPath+fname,true));  //clears file every time
        output.append(data);
        output.close();
    }

    private Vertex checkIfVertexPresent(String value) {
        Vertex ret = null;
        final Optional<Vertex> vert = g.V().has("value", value).tryNext();
        if (vert.isPresent()) {
            ret = vert.get();
            System.out.println("Vertex already present:" + ret.toString());
        } else {
            System.out.println("Vertex not present");
        }
        return ret;
    }

    private void appendGraphData(JSONObject jsonObj) throws JSONException, IOException {
        JSONArray vertices = (JSONArray) jsonObj.get("vertices");
        JSONArray edges = (JSONArray) jsonObj.get("edges");
        JSONArray entities = (JSONArray) jsonObj.get("entities");
        JSONArray sentiment = (JSONArray) jsonObj.get("sentiment");

        String nodeCsvData = "";
        String edgeCsvData = "";
        if(captureGephiData) {
            if (firstCsvAppend) {
                nodeCsvData += "Id,Label,Entity";
                edgeCsvData += "Source,Target,Type,Id,Label,Weight";
                firstCsvAppend = false;
            }
        }

        int vertexCounter;
        int edgeCounter = 0;
        String v1_str = "";
        String v2_str = "";
        String value_str ="";
        for(vertexCounter=1; vertexCounter<vertices.length(); vertexCounter+=2,edgeCounter += 1) {
            v1_str = (String) vertices.get(vertexCounter-1);
            v2_str = (String) vertices.get(vertexCounter);
            if(personMap.containsKey(v1_str)) {
                v1_str = personMap.get(v1_str);
            }
            if(personMap.containsKey(v2_str)) {
                v2_str = personMap.get(v2_str);
            }
            Vertex v1 = checkIfVertexPresent(v1_str);
            if (v1 == null) {
                v1 = graph.addVertex();
                v1.property("value", v1_str);
                v1.property("entity", entities.get(vertexCounter-1));
                System.out.println("NOUN:ENTITY: " + v1_str + ":" + entities.get(vertexCounter-1));
                if(captureGephiData) {
                    nodeCsvIdDict.put(v1_str, nodeCsvId);
                    nodeCsvData += "\n" + String.valueOf(nodeCsvId) + "," + v1_str + "," + entities.get(vertexCounter - 1);
                    nodeCsvId += 1;
                }
            }
            Vertex v2 = checkIfVertexPresent(v2_str);
            if (v2 == null) {
                v2 = graph.addVertex();
                v2.property("value", v2_str);
                v2.property("entity", entities.get(vertexCounter));
                System.out.println("NOUN:ENTITY: " + v2_str + ":" + entities.get(vertexCounter));
                if(captureGephiData) {
                    nodeCsvIdDict.put(v2_str, nodeCsvId);
                    nodeCsvData += "\n" + String.valueOf(nodeCsvId) + "," + v2_str + "," + entities.get(vertexCounter);
                    nodeCsvId += 1;
                }
            }

            //v1.addEdge(String.valueOf(edges.get(edgeCounter)),v2);
            g.V(v2).as("a").V(v1).addE(String.valueOf(edges.get(edgeCounter))).property("sentiment", sentiment.get(edgeCounter)).from("a").next();
            if(captureGephiData) {
                edgeCsvData += "\n" + nodeCsvIdDict.get(v2.value("value")) + "," + nodeCsvIdDict.get(v1.value("value")) + "," + "directed" + "," + edgeCsvId + "," + String.valueOf(edges.get(edgeCounter)) + "," + String.valueOf(Integer.parseInt(sentiment.get(edgeCounter).toString()));
                edgeCsvId += 1;
            }
        }
        // Commit the transaction
        graph.tx().commit();

        if(captureGephiData) {
            writeToCsvFile("nodes.csv", nodeCsvData);
            writeToCsvFile("edges.csv", edgeCsvData);
        }
    }

    private String process(String text, Socket clientSocket) throws JSONException, IOException {
        JSONObject jsonObj = new JSONObject(text);
        String operation = (String) jsonObj.get("operation");
        String ret = "";

        if (operation.compareTo("append_graph_data") == 0) {
            appendGraphData(jsonObj);
        } else if (operation.compareTo("get_all_persons") == 0) {
            String persons = g.V().has("entity", "PERSON").values("value").toList().toString();
            writeToFile("get_all_persons.txt", persons.replace("[", "").replace("]", "").replaceAll(",", "\n"));
        } else if (operation.compareTo("get_all_persons_with_degree") == 0) {
            final List<Object> persons = g.V().has("entity", "PERSON").values("value").toList();
            Map<String, String> result = new HashMap<>();
            for(Object person: persons) {
                result.put(String.valueOf(person),String.valueOf(g.V().has("value", String.valueOf(person)).both().values("value").dedup().toList().size()));
            }
            writeToFile("get_all_persons_with_degree.txt", GraphUtils.sortByValue(result).toString().replace("{", "").replace("}", "").replaceAll(",", "\n"));
        } else if (operation.compareTo("get_sentiment_around_person") == 0) {
            String person = (String) jsonObj.get("person");
            Vertex personV = (Vertex) g.V().has("value", person).toList().get(0);
            List<Edge> neighboringEList = g.V(personV).outE().toList();
            Map<String, Integer> sentimentMap = new HashMap<>();
            for(Edge neighborE: neighboringEList) {
                System.out.println(neighborE.outVertex().value("value").toString());
                System.out.println(neighborE.inVertex().value("value").toString());
                System.out.println(neighborE.value("sentiment").toString());
                String inVertex =  neighborE.inVertex().value("value").toString();
                int sentimentValue = Integer.parseInt(neighborE.value("sentiment").toString());
                if(sentimentMap.containsKey(inVertex)) {
                    sentimentMap.put(inVertex, sentimentMap.get(inVertex) + sentimentValue);
                } else {
                    sentimentMap.put(inVertex, sentimentValue);
                }
            }
            System.out.println( GraphUtils.sortByValue(sentimentMap).toString());
            writeToFile(person + "_sentiments.txt", GraphUtils.sortByValue(sentimentMap).toString().replace("{", "").replace("}", "").replaceAll(",", "\n"));
        }
        //GraphTraversalSource g = graph.traversal();

        return ret;
    }

    private void start(int port) throws IOException, JSONException {
        String request;
        ServerSocket Server = new ServerSocket(port);
        System.out.println("TCPServer Waiting for client on port " + port);
        BufferedReader br;
        PrintWriter outs;
        String response;
        while (true) {
            Socket connected = Server.accept();
            System.out.println(" THE CLIENT" + " " + connected.getInetAddress() + ":" + connected.getPort() + " IS CONNECTED ");

            br = new BufferedReader(new InputStreamReader(connected.getInputStream()));
            request = br.readLine();
            response = process(request, connected);
            System.out.println("RECEIVED FROM CLIENT: " + request);
        }
    }

    private void deleteGraph() {
        g.V().drop().iterate();
        g.tx().commit();
    }

    private void test() throws IOException {
        final List<Object> persons = g.V().has("entity", "LOCATION").values("value").toList();
        System.out.println(persons);
    }

    public static void main(String[] args) throws Exception {
        final GraphdataStore server = new GraphdataStore();
        server.deleteGraph();
        server.start(7183);

        //server.test();

        System.out.println("Program Ended");
    }
}
