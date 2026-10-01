# BackJava

Backend Spring Boot para correspondencia, configurado para conectarse a Oracle mediante JDBC y JPA.

## Configurar la conexión a Oracle

La aplicación lee estos valores de variables de entorno:

- `DB_URL`: URL JDBC de Oracle. Formato recomendado: `jdbc:oracle:thin:@//HOST:PUERTO/NOMBRE_SERVICIO`.
- `DB_USERNAME`: usuario del esquema de la aplicación.
- `DB_PASSWORD`: contraseña de ese usuario.

Por ejemplo, en PowerShell, reemplaza los valores de usuario y contraseña por los de tu entorno:

```powershell
$env:DB_URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1"
$env:DB_USERNAME = "<usuario-del-esquema>"
$env:DB_PASSWORD = "<contraseña>"
mvn spring-boot:run
```

Usa un usuario de esquema dedicado para la aplicación, no una cuenta administrativa como `SYSTEM`. No guardes contraseñas en el repositorio.

La configuración JPA actual usa `spring.jpa.hibernate.ddl-auto=update`, que permite a Hibernate actualizar el esquema al iniciar la aplicación. Para ejecutar en producción, revisa y administra los cambios de esquema de acuerdo con el proceso de tu organización.
