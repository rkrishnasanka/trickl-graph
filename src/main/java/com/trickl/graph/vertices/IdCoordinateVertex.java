package com.trickl.graph.vertices;

import com.vividsolutions.jts.geom.Coordinate;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="coordinate-vertex")
@XmlRootElement(name="coordinate-vertex")
public class IdCoordinateVertex extends IdVertex implements Serializable {

   protected BigDecimal x;

   protected BigDecimal y;

   protected BigDecimal z;

   protected MathContext mathContext = MathContext.UNLIMITED;

   private IdCoordinateVertex() {
      super(null);
   }

   public IdCoordinateVertex(Integer id) {
      super(id);
   }

   public IdCoordinateVertex(Integer id, Coordinate coordinate) {
      super(id);
      setCoordinate(coordinate);
   }

   @XmlAttribute
   public BigDecimal getX() {
      return x;
   }

   @XmlAttribute
   public BigDecimal getY() {
      return y;
   }

   @XmlAttribute
   public BigDecimal getZ() {
      return z;
   }

   @XmlTransient
   public Coordinate getCoordinate() {
      return new Coordinate(x == null ? Double.NaN : x.doubleValue(),
              y == null ? Double.NaN : y.doubleValue(),
              z == null ? Double.NaN : z.doubleValue());
   }

   public void setX(BigDecimal x) {
      this.x = x;
   }

   public void setY(BigDecimal y) {
      this.y = y;
   }

   public void setZ(BigDecimal z) {
      this.z = z;
   }

   public final void setCoordinate(Coordinate coordinate) {
      if (!Double.isNaN(coordinate.x)) {
         this.x = new BigDecimal(coordinate.x, mathContext);
      }
      if (!Double.isNaN(coordinate.y)) {
         this.y = new BigDecimal(coordinate.y, mathContext);
      }
      if (!Double.isNaN(coordinate.z)) {
         this.z = new BigDecimal(coordinate.z, mathContext);
      }
   }

   @XmlTransient
   public MathContext getMathContext() {
      return mathContext;
   }

   public void setMathContext(MathContext mathContext) {
      this.mathContext = mathContext;
      
      // Reapply to the coordinate
      setCoordinate(getCoordinate());
   }
}
