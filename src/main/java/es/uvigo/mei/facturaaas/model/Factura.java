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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "factura")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Factura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String numeroFactura;
    
    @Column(nullable = false)
    private Integer ejercicio;
    
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Column(nullable = false)
    private LocalDate fechaEmision;
    
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "forma_pago_id", nullable = false)
    private FormaPago formaPago;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoFactura estado = EstadoFactura.EMITIDA;
    
    @Column(length = 500)
    private String comentarios;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal importeTotal;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal ivaTotal;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal sumaTotal;
    
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<LineaFactura> lineas = new ArrayList<>();
    
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    private List<Pago> pagos = new ArrayList<>();

    public enum EstadoFactura {
        EMITIDA,
        ANULADA,
        PAGADA,
        RECLAMADA,
        ABONADA
    }

    public void anadirLineaFacura(LineaFactura linea) {
        Objects.requireNonNull(linea, "La línea no puede ser null");
        if (linea.getFactura() != null && linea.getFactura() != this) {
            throw new IllegalArgumentException("La línea ya pertenece a otra factura");
        }
        if (lineas.stream().noneMatch(existente -> existente == linea)) {
            lineas.add(linea);
        }
        linea.setFactura(this);
    }

    public void eliminarLineFactura(LineaFactura linea) {
        Objects.requireNonNull(linea, "La línea no puede ser null");
        boolean eliminada = lineas.removeIf(existente -> existente == linea);
        if (eliminada && linea.getFactura() == this) {
            linea.setFactura(null);
        }
    }
    
    public void calcularTotales() {
        importeTotal = lineas.stream()
            .map(LineaFactura::getImporteTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        ivaTotal = lineas.stream()
            .map(linea -> {
                BigDecimal base = linea.getImporteTotal();
                BigDecimal porcentajeIva = linea.getTipoIVA().getPorcentaje();
                return base.multiply(porcentajeIva).divide(new BigDecimal("100"));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        sumaTotal = importeTotal.add(ivaTotal);
    }
}
