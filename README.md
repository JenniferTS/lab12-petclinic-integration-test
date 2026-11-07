# Spring Boot : Application PetClinic

## Features  

### 1.  Unit Test  --> Tag v1.0.0
### 2.  Integration Test  --> Tag v2.0.0
mvn clean test -Dspring.profiles.active=h2

## Lab 12 : Pruebas de Integración

Curso: Construcción y Pruebas de Software - Tecsup (4 C24)

Cada uno trabaja en su rama `feature/ejercicioN-...`, sube su rama y abre un Pull Request hacia `main`.
Antes de fusionar hay que actualizar la rama con `main` y verificar que todas las pruebas pasen:

```
mvnw.cmd clean test "-Dspring.profiles.active=h2"
```

### Reparto

| Integrante | GitHub | Ejercicios |
|---|---|---|
| Sharon Jennifer Trujillo Sulca | JenniferTS | 1 (Vet) y 6 (VetSpecialty) |
| Piero Nicolás Gómez Lucano | NICO-bestacclaimed | 2 (Owner) |
| Fabricio Sebastián Rojas Agüero | fabriciorojas-10 | 3 (Specialty) y 5 (Types) |
| Avril Alondra Enríquez Vilca | aenriquezvilca-pixel | 4 (Visit) |

### Avance

| Ejercicio | Endpoint | Clase de prueba | Responsable |
|---|---|---|---|
| 1 | `/vets` | VetControllerTest | Sharon |
| 2 | `/owners` | OwnerControllerTest | Piero |
<!-- cada integrante agrega su fila al terminar su ejercicio -->
