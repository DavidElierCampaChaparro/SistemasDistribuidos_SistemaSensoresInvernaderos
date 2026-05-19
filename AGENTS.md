# AGENTS.md

## Design, TODO: We still need to clarify the design of the second, third, and sixth
The proposed system will a Microservices Architecture. This approach
divides the system into independent services.

As a core architectural decision, each service can hae its own database.

1. Auth Service
Responsibility: Login for the administrator. It must have its own database just 
to store the administrator.

2. Greenhouse Management Service
Responsibility: It will manage the structural information of the greenhouse. It will handle
specific static data such as the name, location, and the identification number of the assigned
owner.

3. Sensor Management Service 
Responsibility: It will control the inventory of the sensors. It stores the sensor serial
number, the required data format, and the identification number of the greenhouse
where it is located. 

4. Ingestion Service
Responsibility: Acts as the entry point and universal translator for sensors's raw telemetry.
It will receive binary streams via TCP protocols from the Gateway and should convert 
different manufacturer formats into a single standard format but this abstraction may 
be deprioritized due to time constraints.

5. Analytics Service
Responsibility: It will handle the storage of historical records, so it will be able to
make graphical reports, and predictive modeling. It will provide an API so our customer can use
Business Intelligence tools or obtain the information to make other stuff. This service
MUST have its own database.

6. Notification Service
Responsibility: Continuously monitors data streams against configured thresholds
and dispatches asynchronous external alerts, such as emails or mobile push
notifications.

## Architecture
- Treat the services as independent boundaries. Do not assume shared persistence or shared runtime state unless a module already documents it.
- Use [nginx.conf](nginx.conf) as the source of truth for local routing and port mappings.
- Auth is contract-first SOAP. Check [auth-service/README.md](auth-service/README.md), [auth-service/src/main/resources/ws/auth.xsd](auth-service/src/main/resources/ws/auth.xsd), and [auth-service/requests/login-request.xml](auth-service/requests/login-request.xml) before changing that service.
- gRPC contract changes belong in [grpc-contracts/src/main/proto/greenhouse.proto](grpc-contracts/src/main/proto/greenhouse.proto).
- Keep database changes scoped to the owning service. The auth service uses [auth-service/database.sql](auth-service/database.sql).

## Working Rules
- Prefer small, readable implementations over abstractions.
- Do not edit `target/` or generated source folders directly; regenerate them from the owning module.
- Preserve the existing package conventions in each module (`com.mycompany.*`, `com.greenhouse.*`, `frontend.*`).
- Read the closest module README or contract file before duplicating behavior in instructions or comments.

## Build And Test
- Use `mvn test` in the touched module for the first verification step.
- Use `mvn spring-boot:run` when you need to start a service locally.
- Use `mvn clean install` only when a full module build is needed.
- The workspace targets Java 17 and Maven 3.9+.


## Docker
Pasos para arrancarlo desde la raíz del repo.
Ejecutar desde la carpeta raíz del repo (donde está docker-compose.yml).

Para probar build real
docker compose build --no-cache

Build + arrancar en background:
docker compose up -d --build

Si ya compilaste y solo quieres iniciar:
docker compose up -d

Ver estado de servicios:
docker compose ps

Seguir logs (todo el stack o un servicio):
docker compose logs -f         # tododocker compose logs -f api-gateway   # un servicio

Entrar en un contenedor (shell):
docker compose exec sensor-service sh

Parar y borrar contenedores/redes (preservar volúmenes):
docker compose down
