package es.uvigo.mei.facturaaas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "datos_facturacion")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DatosFacturacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(length = 200)
    private String nombreComercial;
    
    @Column(length = 20)
    private String nif;
    
    @Column(length = 200)
    private String domicilio;
    
    @Column(length = 100)
    private String localidad;
    
    @Column(length = 10)
    private String codigoPostal;
    
    @Column(length = 100)
    private String provincia;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(length = 100)
    private String emailContacto;
    
    @Column(length = 50)
    private String cuentaBancaria;
    
    @Enumerated(EnumType.STRING)
    private TipoEntidad tipo;
    
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "tipo_iva_defecto_id")
    private TipoIVA tipoIVADefecto;
    
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "forma_pago_defecto_id")
    private FormaPago formaPagoDefecto;
    
    public enum TipoEntidad {
        PARTICULAR,
        AUTONOMO,
        SOCIEDAD_LIMITADA,
        SOCIEDAD_ANONIMA,
        COOPERATIVA,
        ONG,
        OTRO
    }

}
