# FACTURAaaS Backend

Backend desarrollado en Spring Boot para la gestión de facturas del proyecto FACTURAaaS.

La especificación funcional de la herramienta se detalla en las [historias de usuario](doc/historias.md).

- Semana 1: Mapeo JPA + repositorios de ejemplo


## 1. Creación del Proyecto con Spring Initializr

Para crear este proyecto desde cero, utilizar [Spring Initializr](https://start.spring.io/) con la siguiente configuración:

### 1.1 Configuración Base

| Parámetro | Valor |
|-----------|-------|
| **Project** | Maven |
| **Language** | Java |
| **Spring Boot** | 4.1.2 |
| **Group** | es.uvigo.mei |
| **Artifact** | facturaaas |
| **Name** | FACTURAaaS Backend |
| **Description** | Backend para gestión de facturas - FACTURAaaS |
| **Package name** | es.uvigo.mei.facturaaas |
| **Packaging** | Jar |
| **Configuration** | Porperties |
| **Java** | 17 |

### 1.2 Dependencias a Seleccionar

- **Spring Web** - Para crear APIs REST
- **Spring Data JPA** - Para persistencia con JPA/Hibernate
- **Spring Security** - Para autenticación y autorización
- **Validation** - Para validación de beans con Jakarta Validation
- **H2 Database** - Base de datos en memoria para desarrollo
- **MySQL Driver** - Driver para base de datos MySQL (producción)
- **Lombok** - Para reducir código boilerplate


### 1.3 Enlace Directo

Puede usarse este enlace para generar el proyecto con la configuración base:

[Generar proyecto en Spring Initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=4.1.2&packaging=jar&configurationFileFormat=properties&jvmVersion=17&groupId=es.uvigo.mei&artifactId=facturaaas&packageName=es.uvigo.mei.facturaaas&dependencies=lombok,web,data-jpa,validation,h2,mysql,security)

---

## 2. Tecnologías

- **Java 17**
- **Spring Boot 4.1.2**
- **Spring Data JPA** - Persistencia de datos
- **H2 Database** - Base de datos en memoria (desarrollo)
- **MySQL** - Base de datos (producción)
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias
- **Spring MVC** - API REST del backend  _[a añadir en semana 2]_
- **Spring Security** - Autenticación y autorización _[a añadir en semana 3]_


## 3. Arquitectura

El proyecto sigue una arquitectura en capas:

```
├── controller/     → Controladores REST (API endpoints)
├── service/        → Lógica de negocio
├── repository/     → Acceso a datos (Spring Data JPA)
├── model/          → Entidades JPA
├── dto/            → Data Transfer Objects
├── security/       → Configuración de seguridad y JWT
├── config/         → Configuración de Spring
└── exception/      → Manejo global de excepciones
```

## 4. Modelo de Datos

### 4.1 Entidades principales:
- **Usuario** - Datos de usuario y credenciales
- **TipoIVA** - Tipos de IVA (21%, 10%, 4%, 0%)
- **FormaPago** - Formas de pago configurables
- **DatosFacturacion** - Datos fiscales del usuario
- **Cliente** - Clientes del usuario
- **Factura** - Facturas emitidas
- **LineaFactura** - Líneas de detalle de factura
- **Pago** - Vencimientos de pago


### 4.2 NOTA: Uso de Lombok en capa `model`

[Lombok](https://projectlombok.org/) genera código Java durante la
compilación a partir de anotaciones. En este proyecto se utiliza principalmente
en las entidades para evitar escribir métodos y constructores repetitivos.
La dependencia y el procesador de anotaciones ya están configurados en
[`pom.xml`](pom.xml), tanto para el código principal como para las pruebas.

| Anotación | Uso en las entidades |
|-----------|----------------------|
| `@Getter` y `@Setter` | Generan los métodos de lectura y escritura de los campos. |
| `@NoArgsConstructor` | Genera el constructor sin argumentos que necesita JPA. |
| `@AllArgsConstructor` | Genera un constructor con todos los campos. |
| `@Builder` | Genera un constructor fluido mediante `builder()`. |
| `@Builder.Default` | Conserva el valor inicial de un campo cuando no se especifica en el builder. |
| `@EqualsAndHashCode(onlyExplicitlyIncluded = true)` | Genera `equals` y `hashCode` usando únicamente los campos marcados con `@EqualsAndHashCode.Include`. (En las entidades del modelo se incluye únicamente el `id`) |
| `@ToString` y `@ToString.Exclude` | Generan la representación textual y excluyen campos concretos, como relaciones y contraseñas. (Las relaciones excluidas de `toString` evitan recorridos recursivos y cargas accidentales)|

Al habilitar `@Builder`, puede crearse una factura mediante el builder, encadenando llamadas:

```java
Factura factura = Factura.builder()
    .numeroFactura("F-001")
    .ejercicio(2026)
    .build();
```

En este ejemplo, `estado`, `lineas` y `pagos` conservan sus valores
predeterminados gracias a `@Builder.Default`. La factura todavía necesita el
resto de datos obligatorios antes de persistirse. 

**Importante:** Para añadir o eliminar líneas, deben utilizarse los métodos `anadirLineaFacura` y `eliminarLineFactura` de
`Factura`, que actualizan ambos lados de la relación.

**Precaución con JPA:** la igualdad actual se basa únicamente en el `id`.
- Dos entidades nuevas del mismo tipo con `id` nulo pueden compararse como
iguales y su `hashCode` cambia al asignarse el identificador
- Evitar depender de esa igualdad o usarlas como claves de mapas mientras no tengan un `id`
estable asignado por la BD. 




## 5. Ejecución

### 5.1 Requisitos
- Java 17 o superior
- Maven 3.6+

### 5.2 Clonado/descarga desde github

```bash
git clone https://github.com/ESEI-MEI-SI/facturaaas-jpa-26.git
cd facturaaas-jpa-26
```

```
wget https://github.com/ESEI-MEI-SI/facturaaas-jpa-26/archive/refs/heads/main.zip
unzip mail.zip
cd facturaaas-jpa-26-main
```


### 5.3 (opcional) Crear la base de datos de producción en Linux

Estos pasos preparan MySQL en la misma máquina que la aplicación, usando
`localhost:3306`. 

Conectar al servidor MySQL/MariaDB como administrador
```bash
sudo mysql -u root -p     # pedirá la contraseña de root
```

Ejecutar el siguiente SQL para crear el usuario `facturaaas` con contraseña `facturaaas` y la BD `facturaaas`.

```sql
CREATE USER 'facturaaas'@'localhost' IDENTIFIED BY 'facturaaas';

CREATE DATABASE facturaaas CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON facturaaas.* TO 'facturaaas'@'localhost';
EXIT;
```

Comprobar la conexión TCP con el usuario creado, MySQL solicitará la contraseña:

```bash
mysql -h 127.0.0.1 -P 3306 -u facturaaas -p facturaaas -e "SELECT DATABASE();"
```

El resultado debe mostrar `facturaaas`. 


### 5.4 Compilar y ejecutar (modo desarrollo)

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

El backend estará disponible en: `http://localhost:8080` (por ahora no implementado)

En desarrollo, Hibernate muestra el SQL generado en la consola. 
- En el perfil `prod`, esta salida está desactivada.

#### Consola H2 (BD de desarrollo)
Para acceder a la consola de H2 (desarrollo):
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:facturaaas`
- Usuario: `sa`
- Password: `<vacio>`
 
En desarrollo, Spring Security permite todas las peticiones sin autenticación
ni protección CSRF. 
- Es una configuración didáctica provisional, que se sustituirá más adelante por seguridad con JWT. 
- La consola permite ejecutar SQL. 
- La consola está desactivada en `prod`.


#### Usuarios de Prueba

Fuera del perfil `prod` se utiliza un encoder de contraseñas dummy que las
almacena en texto plano. 
- Es solo para desarrollo y no protege las contraseñas.
- El perfil `prod` requiere configurar un `PasswordEncoder` seguro antes de
ejecutar la aplicación.

Al iniciar la aplicación se crean automáticamente:

| Usuario | Password | Rol |
|---------|----------|-----|
| admin | admin123 | ADMINISTRADOR |
| user | user123 | USUARIO |

En desarrollo también se crea para `user` un cliente de prueba y la factura
`DEMO-001`, con tres líneas y un total de 229,90 € (IVA incluido). La factura
no se vuelve a crear si ya existe una con ese número.



## 6. Configuración

Editar `src/main/resources/application.properties` para:
- Cambiar puerto del servidor
- Configurar base de datos


## Autor

Proyecto académico - FACTURAaaS
