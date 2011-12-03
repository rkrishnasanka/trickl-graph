// Distributed under the Boost Software License, Version 1.0.
//    (See accompanying file LICENSE_1_0.txt or copy at
//          http://www.boost.org/LICENSE_1_0.txt)
package com.trickl.graph;

import java.util.*;
import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;

public class Connectivity<V, E> {

   // This visitor is used both in the connected_components algorithm
   // and in the kosaraju strong components algorithm during the
   // second DFS traversal.
   private static class ConnectedComponentIndexer<V, E> implements SpanningSearchVisitor<V, E> {

      protected int index = 0;
      protected Map<V, Integer> vertexComponent;

      public ConnectedComponentIndexer() {
         vertexComponent = new HashMap<V, Integer>();
      }

      @Override
      public void startVertex(V u) {
         ++index;
      }

      @Override
      public void discoverVertex(V u) {
         vertexComponent.put(u, index);
      }

      @Override
      public void initializeVertex(V u) {
      }

      @Override
      public void examineEdge(V source, V target) {
      }

      @Override
      public void treeEdge(V source, V target) {
      }

      @Override
      public void backEdge(V source, V target) {
      }

      @Override
      public void forwardOrCrossEdge(V source, V target) {
      }

      @Override
      public void finishVertex(V u) {
      }
   };

   private static class ComponentComparator<V> implements Comparator<V> {

      protected Map<V, Integer> vertexComponent;

      public ComponentComparator(Map<V, Integer> vertexComponent) {
         this.vertexComponent = vertexComponent;
      }

      @Override
      public int compare(V lhs, V rhs) {
         int lhsComponent = vertexComponent.get(lhs);
         int rhsComponent = vertexComponent.get(rhs);
         return lhsComponent > rhsComponent
                 ? 1
                 : (lhsComponent < rhsComponent ? -1 : 0);
      }
   }
   private Graph<V, E> graph;
   private DepthFirstSearch<V, E> depthFirstSearch;
   private ConnectedComponentIndexer<V, E> componentVisitor;

   public Connectivity(Graph<V, E> graph) {
      this.graph = graph;
      this.depthFirstSearch = new DepthFirstSearch<V, E>(graph);
   }

   private void lazyImpl() {
      if (componentVisitor == null) {
         this.componentVisitor = new ConnectedComponentIndexer<V, E>();
         depthFirstSearch.traverse(componentVisitor);
      }
   }

   public int getComponents() {
      if (graph.vertexSet().isEmpty()) {
         return 0;
      }

      lazyImpl();

      return componentVisitor.index;
   }
   
   public int getComponent(V vertex) {
      lazyImpl();
      return componentVisitor.vertexComponent.get(vertex);
   }

   public boolean isConnected()
   {
      return getComponents() == 1;
   }

   public void makeConnected(EdgeFactory<V, E> edgeFactory) {
      List<V> verticesByComponent = new ArrayList<V>(graph.vertexSet());

      int componentCount = getComponents();

      if (componentCount < 2) {
         return;
      }

      Collections.sort(verticesByComponent,
              new ComponentComparator<V>(componentVisitor.vertexComponent));

      for (int i = 1; i < verticesByComponent.size(); ++i) {
         V u = verticesByComponent.get(i - 1);
         V v = verticesByComponent.get(i);
         if (componentVisitor.vertexComponent.get(u)
                 != componentVisitor.vertexComponent.get(v)) {
            edgeFactory.createEdge(u, v);
         }
      }
   }

   public void makeConnected() {
      makeConnected(graph.getEdgeFactory());
   }
} 

