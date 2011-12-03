package com.trickl.graph.edges;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="id-edge")
@XmlRootElement(name="id-edge")
public class UndirectedIdEdge<V> extends UndirectedEdge<V>
                      implements Serializable {
   private Integer id;
   
   private UndirectedIdEdge() {
      this(null, null, null);
   }

   public UndirectedIdEdge(Integer id, V source, V target) {
      super(source, target);
      this.id = id;
   }

   @XmlID
   @XmlAttribute(name="id")
   public String getIdString() {      
      String prefix = getIdStringPrefix();
      return prefix + (id == null ? "" : Integer.toString(id));
   }

   protected void setIdString(String idString) {
      String prefix = getIdStringPrefix();
      if (idString.startsWith(prefix)) idString = idString.substring(prefix.length());
      this.id = Integer.parseInt(idString);
   }

   public String getIdStringPrefix() {
      // XML ids need to be unique across the entire document, so a unique prefix per class
      // is required
      XmlType classXmlType = this.getClass().getAnnotation(XmlType.class);
      return (classXmlType == null ? "" : classXmlType.name() + "-");
   }

   @XmlTransient
   public Integer getId() {
      return id;
   }

   @Override
   public String toString() {
      return "(" +getIdString()
             + ")["
             + ((source == null) ? "null" : source.toString())
             + "-"
             + ((target == null) ? "null" : target.toString())
             + "]";
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      final UndirectedIdEdge<V> other = (UndirectedIdEdge<V>) obj;
      if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
         return false;
      }
      return true;
   }

   @Override
   public int hashCode() {
      int hash = 7;
      hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
      return hash;
   }
}
