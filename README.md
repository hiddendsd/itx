# INDITEX - Proceso de selección

## Consideraciones

Al analizar la tarea a realizar, se han identificado una serie de puntos ambiguos, que se detallan a continuación, así como las decisiones tomadas al respecto: 

### Tecnología

A dia de hoy, hay dos stack tecnológicos distintos para desarrollar un servicio SpringBoot Rest:
- Stack clásico, conocido como servlet 
- Stack reactivo, basado en la utilización de webflux

Desconociendo cuál es el stack tecnológico utilizado en Inditex, se ha decidido resolver la tarea dos veces:

- /inditex-pvp-servlet: Resuelve la tarea con un proyecto SpringBoot clásico (Servlet)
- /inditex-pvp-reactive: Resuelve la tarea utilizando Webflux


### Fechas de vigencia de los precios

La gestión de la fecha de vigencia de los precios puede realizarse teniendo en cuenta las zonas horarias o no.

#### Fechas con zona horaria

Implica que la hora en la que cambia un precio depende de la localización de la tienda (asumiendo que el sistema recoge las distintas tarifas del producto, ya sean para tienda física o para el canal online).

Si un precio debe entrar en vigor una fecha determinada, por ejemplo a las 14:00 UTC, la hora de vigencia de la nueva tarifa en cada tienda depende de su localización (y del uso horario del cliente online). Significa que en las tiendas de la península el cambio de tarifa sería a las 16:00, mientras que en Tenerife sería a las 15:00. En un país grande como EEUU, dependiendo del estado en el que se encuentre cada tienda, tendrá acceso al nuevo precio a una hora u otra, pudiendo haber grandes diferencias, incluso cambio de día.

#### Fechas sin zona horaria

Implica que si una tarifa entra en vigencia a las 14:00 horas, no importa dónde esté la tienda, debe cambiar la tarifa a sus 14:00 hora local. La tienda online deberá definir cuál es su uso horario (UTC para todos los usuarios, o en función de la localización del usuario).

#### Decisión

Dado que en los datos de ejemplo facilitados en la tarea no se indica zona horaria, y parece tener sentido que el precio entre en vigencia en base a una hora concreta, con independencia de la ubicación de la tienda, se ha decidido desarrollar:

- Proyecto SERVLET: NO tiene en cuenta la zona horaria:
Si una tarifa entra en vigencia a las 14:00, el servicio toma la zona horaria indicada por el usuario en la petición, y devuelve las 14:00 en su mismo uso horario. 

- Proyecto Reactive: SI tiene en cuenta la zona horaria:
En la bbdd se almacena un Instant en UTC. El servicio toma la zona horaria indicada en la petición, y calcula si el precio está vigente en la zona horaria de la petición.
Por ejemplo, un precio que entra en vigencia a las 14:00:00UTC: Si la fecha indicada en la petición está en un uso horario con una hora de diferencia, no le indicará que está vigente hasta las 15:00:00 de su uso horario (las 14:00:00 UTC)


#### Caso Real

En un caso real, la decisión a tomar debe estar guiada por el interlocutor de negocio.


### Colisiones

Tal y como indica la tarea, dado un BrandID, un ProductID y una fecha, puede haber varios registros en la bbdd con distintos precios y se utiliza el campo 'priority' para resolver la ambiguedad.

Ya que está involucrado un rango de fechas, no es posible crear un índice único para asegurar que el campo Priority no se repite.

Aun suponiendo que las especificaciones del servicio encargado de añadir los datos digan que no puede meter los datos por duplicado, es algo que podría ocurrir, por un error por ejemplo.

Nuestro servicio es el encargado de devolver el PVP, de modo que si ese caso ocurriera, no debería dar dos precios a elegir, ni debería devolver uno al azar.

#### Decisión

Se controla si la bbdd tiene más de un precio con la prioridad máxima para los criterios de entrada, y si es así, devuelve un error 409 (Conflicto)

#### Caso Real

En un caso real se validaría con negocio si existe algún criterio de desambiguación adicional (por ejemplo, en caso de conflicto, quedarse con el último actualizado).


### Filtrado de precios

Se puede realizar en BBDD o por código.

#### En BBDD 

Implica:
- hacer un JOIN de la tabla consigo misma (para devolver solo los de mayor priodidad).
- algoritmos de ordenación y filtrado de la bbdd (eficientes)
- en función de la volumetría de los datos, un indice del campo prioridad ordenado descendentemente haría la búsqueda más eficiente aún.
- devuelve SOLO los registros que tienen prioridad máxima.


#### En Código

Implica:
- la bbdd devuelve directamente TODOS los registros que cumplen el criterio de entrada. 
-- No requiere joins en bbdd
-- Aumenta el tráfico de red, ya que devuelve todos y no solo los definitivos. (Si el numero de registros que cumplen los criterios de entrada fuese muy grande, este punto tomaría más relevancia. Pero dado el caso de uso aparentemente no debería ser así.)
- El código se encarga de buscar los de mayor prioridad.


#### Decisión

- El filtrado se realiza en la bbdd y devuelve los registros con max prioridad:
-- 0 -> El API devuelve un 404 Not Found
-- 1 -> El API lo devuelve
-- >1 --> El API devuelve un 409 Conflict

#### Caso Real

- Valorar el nivel de carga soportado por los microservicios y la bbdd.
- Valorar la volumetría de datos:
-- ¿Cuántos precios puede haber vigentes en una misma fecha para un producto? 
-- ¿Cuántos registros tiene la tabla? 
-- ¿Cuál es la cardinalidad del BrandID y ProductID en la tabla para optimizar indices?
- Definir escenario de uso real, realizar POC de ambas aproximaciónes y ejecutar test de stress con volumetrías reales.

#### Alternativas

En caso de optar por realizar la búsqueda de el/los precio/s con mayor prioridad en el micro, el siguiente código sería aplicable:
```java
prices.collect(
    Collectors.collectingAndThen(
        Collectors.groupingBy(Price::priority),
        map -> {
          var pricesWithMaxPriority = Collections.max(map.entrySet(), Map.Entry.comparingByKey()).getValue();
          return switch (pricesWithMaxPriority.size()) {
            case 0 -> throw new PriorityCollisionException();
            case 1 -> return pricesWithMaxPriority.get(0);
            default -> throw new PriorityCollisionException();
          }
        }
    )
);
```

