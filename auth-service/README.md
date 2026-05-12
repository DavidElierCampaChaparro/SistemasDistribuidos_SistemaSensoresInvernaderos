# Auth Service (Java 17)

Proyecto SOAP contract-first para autenticación usando Spring Web Services sobre Java 17.

## Requisitos

- Java 17
- Maven 3.9+

## Contrato

- XSD: `src/main/resources/ws/auth.xsd`
- WSDL (en runtime): `http://localhost:8080/ws/auth.wsdl`
- Namespace: `http://auth-service.dev/soap/auth`

## Operacion disponible

- `LoginRequest(username, password)`
- `LoginResponse(success, message, token)`

## Ejecutar

```bash
mvn spring-boot:run
```

## Probar

### 1) Ver WSDL

```bash
curl http://localhost:8080/ws/auth.wsdl
```

### 2) Enviar request SOAP

Archivo de ejemplo:

- `requests/login-request.xml`

Con curl:

```bash
curl -X POST http://localhost:8080/ws \
  -H "Content-Type: text/xml; charset=utf-8" \
  --data @requests/login-request.xml
```

Respuesta esperada para credenciales válidas:

```xml
<ns2:LoginResponse xmlns:ns2="http://auth-service.dev/soap/auth">
  <ns2:success>true</ns2:success>
  <ns2:message>authentication successful</ns2:message>
  <ns2:token>...</ns2:token>
</ns2:LoginResponse>
```

## Tests

```bash
mvn test
```
