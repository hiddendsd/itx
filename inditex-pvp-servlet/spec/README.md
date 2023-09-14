# itx-pvp-servlet - `SPEC` module

This module gathers together all standard specifications. They might be OpenAPI specs, AsyncAPI specs, Avro definitions...
In this specific project, the Rest API specification is hosted in this module.

Based on the standard specification, this module will auto-generate the related code, so it can be used by the api implementation module, but also by the API consumer projects. Thus, the DTOs and interfaces used by both parts (server and consumers) are the same and don't need to be created and maintained on each project/module.

## Module Structure

    .
    ├── /rest                        # OpenAPI specs folder
    │    └── pvp.yml                 # PVP Api specification
    └── README.md
    └── CONTRIBUTORS.md


## Rest APIs

Additional information about the rest api specs defined in this module.

### PVP Api

This API has just one endpoint to **_get a product PVP for a given brand in a given date_**:

    GET /pvp-api/v1/brands/{brandId}/products/{productId}/prices/pvp?date=2023-09-09T12:22:00Z

#### Alternatives

The endpoint structure might vary depending on the domain context. Some alternatives might be: 

- When each brand is considered as a tenant, and the service has a multi-tenant approach: 
brandId might travel in a header (i.e.: x-brand-id).
- If the same product can be sold in different brands then a different path structure can be used:
  
  for instance `/products/{productId}/brands/{brandId}/prices/pvp`,

  or even `/products/{productId}/prices/pvp` and the brand id be part of the query parameters or be a header... (the global company approach
  should be bore in mind, to follow a standard criteria along all microservices)
        
#### Assumptions

In this specific case, as there is not known company broad standard and there is not deep domain knowledge, the following assumptions are 
done:

- Each product is sold in one and only one brand.
- This service is global, so it is used for any brand.
- The number of possible brands is small, so its identifier is standard integer number
- The number of possible products is huge, so its identifier is a long number                
- The number of priceLists might be high but standard integer numbers can deal with it.
- It is not possible to have brands with zero or negative identifiers.
- It is not possible to have products with zero or negative identifiers.
- It is not possible to have price lists with zero or negative identifiers.

### CodeGeneration configuration

The module is using `openapi-generator-maven-plugin` to automatically create the DTOs and the WebController interface based on the openapi
specification.

The code generator plugin has been customized to :

- Use Lombok Builder in the DTOs.
- Create the web controllers interfaces (without implementation and without default responses) grouping the endpoints by their main (first)
tag.
- Adapt the interface to be SpringBoot3 compliance 
- Force the usage of BigDecimal java data type for all fields defined as `Double` OpenApi data type in the spec.
  
  Note: This is done because there is only one field in the whole openapi defined as double, which is the price and 
  BigDecimal is the recommended data type to manage currencies in Java.
  In a more real situation, where there might be more double fields others than currencies, this configuration might not been applied.

### API Implementation

The project implementing/consuming this API must include the following dependency, which contains the Controller Interface and the
DTOs:

Maven:

```
<dependency>
    <groupId>com.diegosaldiaz.inditex.pvp</groupId>
    <artifactId>spec</artifactId>
    <version>${version}</version>
</dependency>
```

Gradle:

```
compile group: 'com.diegosaldiaz.inditex.pvp', name: 'spec', version: '${version}'
```

## Contributions

[CONTRIBUTORS.md](CONTRIBUTORS.md) is a **mandatory** reading for anyone planning to contribute to the PVP Api
specification.

Please, read it carefully. 
