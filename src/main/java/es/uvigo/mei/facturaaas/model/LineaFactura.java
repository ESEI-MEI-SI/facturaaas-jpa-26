package es.uvigo.mei.facturaaas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

@Entity
@Table(name = "linea_factura")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LineaFactura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;
    
    @Column(nullable = false)
    private Integer numeroLinea;
    
    @Column(nullable = false, length = 300)
    private String concepto;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal cantidad = BigDecimal.ONE;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @Column(precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal porcentajeDescuento = BigDecimal.ZERO;
    
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "tipo_iva_id", nullable = false)
    private TipoIVA tipoIVA;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal importeTotal;

    public void calcularImporteTotal() {
        BigDecimal subtotal = precioUnitario.multiply(cantidad);
        BigDecimal descuento = subtotal.multiply(porcentajeDescuento).divide(new BigDecimal("100"));
        importeTotal = subtotal.subtract(descuento);
    }
}
