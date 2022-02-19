# Finding maximum common subgraph and minimum common supergraph of two graphs

The goal of this project is to find a maximal common subgraph and a minimum common supergraph of two graphs. These problems are not trivial as they are both NP-hard. The maximum common subgraph problem has many definitions in the literature. This project uses the one commonly known as a maximum common induced subgraph, where we want to maximize the number of vertices of the common induced subgraph. Analogously, we want to minimize the number of vertices of the common supergraph for the minimum common supergraph problem. Input graphs are undirected, unweighted, and connected.

There are two solutions:
 - Exact - implementation of the [McSplit algorithm](https://www.ijcai.org/proceedings/2017/0099.pdf)
 - Approximate - implementation of a genetic algorithm

The program is a console-based application. There are two implementations. At one point, Java implementation ([mcs-java](mcs-java)) was abandoned and migrated to C# ([Mcs-cs](Mcs-cs)), which was further developed.