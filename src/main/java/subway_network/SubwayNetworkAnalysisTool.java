package subway_network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.InputStream;


class SubwayNetworkAnalysisTool {

    public int calculate (String input) throws FileNotFoundException {

      int testNumber = Integer.parseInt(input);

      InputStream fileInput = getClass()
        .getClassLoader()
        .getResourceAsStream("data/test" + testNumber + ".txt");

      Scanner scanner = new Scanner(fileInput);

      int nv, ne; 


      while (true) { // This loop repeats for each problem that is included in the same input file (until 0 0 is read)

      	nv=scanner.nextInt(); // read number of vertices
      	ne=scanner.nextInt(); // read number of edges
        // System.out.println(nv +" " + ne);
        if ((nv==0) & (ne==0)) break; // if both are zero it is indication to stop 
        

        ArrayList<String> vertexNames=new ArrayList<String>(); // reading vertex names into an Array List
      	for (int v=0; v<nv; v++) {
          String vName = scanner.next(); // here a vertex name is read
          vertexNames.add(vName);  
          // System.out.println(vName);
      	}

        // you should do something here to add the vertices to the graph
        Graph<String> graph = new Graph<>(ne);

      	for (int e=0; e<ne; e++) {
          String v1Name = scanner.next(); // a vertex name that is one end of the edge
          String v2Name = scanner.next(); // a vertex name that is the other end of the edge
          int weight=scanner.nextInt(); // edge weight is given
          // System.out.println(v1Name+" "+v2Name+" "+weight);
          // here you have a new edge information: {v1Name,v2Name} with weight 'weight'
          // you should do something here in order to add this edge to the graph
          graph.addEdge(v1Name, v2Name, weight);
      	} 

        String home=scanner.next(); // here is the name of the vertex of Peter's home
        // System.out.println(home);

        // Below is a silly printout that no matter the input give the answer "Impossible"
        // You should substitute this to call your algorithms/methods that will produce the correct answer         

        graph.sortList();

        Partition<String> subwayPartition = new Partition<String>();
        partitionPair<String> [] subwayPartitionPositions = new partitionPair[nv];

        for (int i = 0; i < vertexNames.size() ; i++){
          String vertex = vertexNames.get(i);
          Node<String> node = subwayPartition.makeCluster(vertexNames.get(i));
          partitionPair<String> pair = new partitionPair(node, vertex);
          subwayPartitionPositions[i] = pair;
        }
        int cost = 0;
        for (int i = 0; i < graph.size(); i++){
          String v1 = graph.returnv1(i);
          String v2 = graph.returnv2(i);
          Node<String> v1Node = null;
          Node<String> v2Node = null;
          for (int j = 0; j < subwayPartitionPositions.length; j++){
            if (v1.equals(subwayPartitionPositions[j].vertexName)){
              v1Node = subwayPartitionPositions[j].node;
            };
            if (v2.equals(subwayPartitionPositions[j].vertexName)){
              v2Node = subwayPartitionPositions[j].node;
            };
          }
          if (subwayPartition.find(v1Node) != subwayPartition.find(v2Node) && v1Node != null && v2Node != null){
            subwayPartition.union(v1Node, v2Node);
            cost += graph.returnw(i);
          };
        }
        if (subwayPartition.numberOfClusters() > 1){
          return -1;
        }
        else{
          return cost;
        }
        
      }
      return -1;

   }

}

class partitionPair <E> {
  String vertexName;
  Node<E> node;
  partitionPair(Node<E> node, String vertexName){
    this.vertexName = vertexName;
    this.node = node;
  }
}

// Name: Vineeth Ravi 
// Student #: 300380109

class Partition<E> {

    private LinkedList<LinkedList<Node<E>>> partition;
    
    public Partition(){
        partition = new LinkedList<>();
    }

    public Node<E> makeCluster(E x){ //Creates a singleton cluster containing new element x and returns its position

        LinkedList<Node<E>> sequence = new LinkedList<>();
        Node<E> node = new Node<>(x, sequence);
        sequence.add(node);
        partition.add(sequence);
        return node; //position of leader

    }

    public void union(Node<E> p, Node<E> q){ //Merges the clusters containing positions p and q.
        LinkedList<Node<E>> pSequence = p.getSequence();
        LinkedList<Node<E>> qSequence = q.getSequence();
        if (pSequence.getFirst() != qSequence.getFirst()){
            if(pSequence.size() >= qSequence.size()){
                for (Node<E> node : qSequence){
                    pSequence.add(node);
                    node.setSequence(pSequence);
                }
                partition.remove(qSequence);
            }
            else{
                for (Node<E> node : pSequence){
                    qSequence.add(node);
                    node.setSequence(qSequence);
                }
                partition.remove(pSequence);
            }
        }
    }
    
    public Node<E> find(Node<E> p){ //Returns the position of the leader of the cluster containing position p.
        return p.getSequence().getFirst();
    }
    
    public E element(Node<E> p){ //returns the element that was stored at position p
        return p.getElement();
    }
    
    public int numberOfClusters(){ //returns the current number of different clusters
        return partition.size();
    }
    
    public int clusterSize(Node<E> p){ //returns the size of the cluster containing p
        return p.getSequence().size();
    }
    
    public LinkedList<Node<E>> clusterPositions(Node<E> p){ //returns a list of all positions that belong to the same cluster as position p
        return p.getSequence();
    }
    
    public LinkedList<Integer> clusterSizes(){ //returns a list of all the cluster sizes in decreasing order of size
        LinkedList<Integer> clusterSizes = new LinkedList<>();
        for (LinkedList<Node<E>> cluster : partition){
            clusterSizes.add(cluster.size());
        }
        return clusterSizes;
    }
    public int partitionSize(){
        LinkedList<Integer> clustersList = this.clusterSizes();
        int sum = 0;
        for (int clusterSize : clustersList){
            sum += clusterSize;
        }
        return sum;
    }
    public LinkedList<LinkedList<Node<E>>> getPartition(){
        return partition;
    }
    public void removeCluster(Node<E> p){
        partition.remove(p.getSequence());
    }
    public Iterator<LinkedList<Node<E>>> partitionIterator(){
        return partition.iterator();
    }
}

class Node<E>{

    private E element;
    private LinkedList<Node<E>> sequence;
    
    public Node(E element, LinkedList<Node<E>> sequence){
        this.element = element;
        this.sequence = sequence;
    }

    public void setSequence(LinkedList<Node<E>> sequence){
        this.sequence = sequence;
    }

    public E getElement(){
        return element;
    } 

    public LinkedList<Node<E>> getSequence(){
        return sequence;
    }
}

class Graph <E> {

    private ArrayList<Edge<E>> edgeList;

    public Graph(int size){
        edgeList = new ArrayList<Edge<E>>(size);
    }

    public void addEdge(E v1, E v2, int weight){
        Edge<E> edge = new Edge<> (v1, v2, weight);
        edgeList.add(edge);
    }

    public E returnv1(int i){
        return edgeList.get(i).v1;
    }
    public E returnv2(int i){
        return edgeList.get(i).v2;
    }
    public int returnw(int i){
        return edgeList.get(i).weight;
    }

    public int size(){
        return edgeList.size();
    }

    public void sortList(){
        for (int i = 0; i < edgeList.size() - 1; i++){
            for(int j = i + 1; j < edgeList.size(); j++){
                if (edgeList.get(j).weight < edgeList.get(i).weight){
                    Edge temp = edgeList.get(i);
                    edgeList.set(i, edgeList.get(j));
                    edgeList.set(j, temp);
                }
            }
        }
    }

}

class Edge<E> {

    E v1;
    E v2;
    int weight;

    public Edge(E v1, E v2, int weight){
        this.v1 = v1;
        this.v2 = v2;
        this.weight = weight;
    }

}


