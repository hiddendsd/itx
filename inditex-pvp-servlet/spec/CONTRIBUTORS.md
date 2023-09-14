# Product-Master API: Contributors & Reviewers Guide

This document describes de basic expectations and measures, as well as resources available to ensure consistent, high
quality, contributions to PVP API specification and quicker approvals of new changes.

This is a mandatory reading for anyone planning to contribute to the PVP API specification.

This is a living document, so improvements and discussions are welcome.
Please, see section at the end on how to discuss, debate, improve and revisit the process and requirements outlined
below.

---

## API Designers Quick Start

### 1. Principles

#### API as a Product

* Treat your API as product and act like a product owner
* Put yourself into the place of your customers; be an advocate for their needs
* Emphasize simplicity, comprehensibility, and usability of APIs to make them irresistible for client engineers
* Actively improve and maintain API consistency over the long term
* Make use of customer feedback and provide service level support

#### API First

API First is one of our key principles. In a nutshell API First requires two aspects:

* define APIs first, before coding its implementation, using a standard specification language
* get early review feedback from peers and client developers

By defining APIs outside the code, we want to facilitate early review feedback and also a development discipline that
focus service interface design on…

* profound understanding of the domain and required functionality
* generalized business entities / resources, i.e. avoidance of use case specific APIs
* clear separation of WHAT vs. HOW concerns, i.e. abstraction from implementation aspects — APIs should be stable even
  if we replace complete service implementation including its underlying technology stack

Moreover, API definitions with standardized specification format also facilitate…

* single source of truth for the API specification; it is a crucial part of a contract between service provider and
  client users
* infrastructure tooling for API discovery, API GUIs, API documents, automated quality checks

### 2. API Design Guideline

The titles are marked with the corresponding labels: Must:, Should:, May:.

#### Must: Follow API First Principle

You must follow the API First Principle, more specifically:
* You must define APIs first, before coding its implementation, using OpenAPI as specification language
* You must design your APIs consistently with this guidelines
* You must call for early review feedback from peers and client developers

#### Must: Provide API Specification using OpenAPI

We are using OpenAPI v3 as specification standard to define API specifications files.

API designers have to provide the API specification files using YAML to improve readability.

The API specification files should be subject to version control in this repository.

#### Should: Provide API User Manual
In addition to the API Specification, it is good practice to provide an API user manual to improve client developer
experience, especially of engineers that are less experienced in using this API. A helpful API user manual typically
describes the following API aspects:

* API scope, purpose, and use cases
* concrete examples of API usage
* edge cases, error situation details, and repair hints
* architecture context and major dependencies - including figures and sequence flows

The user manual must be published online, e.g. via our documentation hosting platform service, GHE pages, or specific
team web servers. Please do not forget to include a link to the API user manual into the API specification using the
`#/externalDocs/url property`.

#### Must: Write APIs in U.S. English

#### Must: Contain API Meta Information

API specifications must contain the following OpenAPI meta information to allow for API management:

* #/info/title as (unique) identifying, functional descriptive name of the API
* #/info/version to distinguish API specifications versions following semantic rules
* #/info/description containing a proper description of the API
* #/info/contact/{name,url,email} containing the responsible team

Following OpenAPI extension properties must be provided in addition:

* #/info/x-api-id unique identifier of the API
* #/info/x-audience intended target audience of the API

Example:

```yml
openapi: 2.0
info:
  title: Inditex PVP API
  description: This API provides access to Inditex's PVP management microservice.
  contact:
    email: info@diegosaldiaz.com
  version: 1.0.0
  x-api-id: d0184f38-b98d-11e7-9c56-68f728c1ba70
  x-audience: company-internal  
```

#### Must: Use Semantic Versioning

To share a common semantic of version
information we expect API designers to comply to [Semantic Versioning](https://semver.org/spec/v2.0.0.html) 2.0 rules 1
to 8 and 11 restricted to the format <MAJOR>.<MINOR>.<PATCH> for versions as follows:

* Increment the **MAJOR** version when you make incompatible API changes after having aligned this changes with consumers,
* Increment the **MINOR** version when you add new functionality in a backwards-compatible manner, and
* Optionally increment the **PATCH** version when you make backwards-compatible bug fixes or editorial changes not affecting
  the functionality.
* Pre-release versions (rule 9) and build metadata (rule 10) must not be used in API version information.
* While patch versions are useful for fixing typos etc, API designers are free to decide whether they increment it or not.
* API designers should consider to use API version **0.y.z** for initial API design.

#### Must: Provide API Identifiers

Each API specification must be provisioned with a globally unique and immutable API identifier defined in
`info.x-api-id` using UUID format.

The API id allows to track the evolution and history of an API specification as a sequence of versions.
API specifications will evolve and any aspect of an OpenAPI specification may change. We require API identifiers
because we want to support API clients and providers with API lifecycle management features, like change trackability
and history or automated backward compatibility checks. The immutable API identifier allows the identification of all
API specification versions of an API evolution.

#### May: Provide API Audience (TO-DO)

Each API must be classified with respect to the intended target audience supposed to consume the API, to facilitate
differentiated standards on APIs for discoverability, changeability, quality of design and documentation, as well as
permission granting. We differentiate the following API audience groups with clear organisational and legal boundaries:

* company-internal
  The API consumers with this audience are restricted to applications owned by INditex and it’s affiliates.

* external-partner
  The API consumers with this audience are restricted to applications of business partners of the company owning the API
  and the company itself.

* external-public
  APIs with this audience can be accessed by anyone with Internet access.

The API audience is provided as API meta information in `info.x-api-id` of the Open API specification.

#### Must: Don’t Break Backward Compatibility

Change APIs, but keep all consumers running. Consumers usually have independent release lifecycles, focus on stability,
and avoid changes that do not provide additional value. APIs are contracts between service providers and service
consumers that cannot be broken via unilateral decisions.

There are two techniques to change APIs without breaking them:

* follow rules for compatible extensions
* introduce new API versions and still support older versions

We strongly encourage using compatible API extensions and discourage versioning when possible.

#### Should: Prefer Compatible Extensions

API designers should apply the following rules to evolve RESTful APIs for services in a backward-compatible way:
* Add only optional, never mandatory fields.
* Never change the semantic of fields (e.g. changing the semantic from customer-number to customer-id)
* Input fields may have (complex) constraints being validated via server-side business logic. Never change the
  validation logic to be more restrictive and make sure that all constraints are clearly defined in description.
* Enum ranges can be reduced when used as input parameters, only if the server is ready to accept and handle old range
  values too. Enum range can be reduced when used as output parameters.
* Enum ranges cannot not be extended when used for output parameters — clients may not be prepared to handle it.
  However, enum ranges can be extended when used for input parameters.
* Use `x-extensible-enum`, if range is used for output parameters and likely to be extended with growing functionality.
  It defines an open list of explicit values and clients must be agnostic to new values.
* Support redirection in case an URL has to change (301 Moved Permanently).

#### Should: Design APIs Conservatively

Designers of service provider APIs should be conservative and accurate in what they accept from clients:

* Unknown input fields in payload or URL should not be ignored; servers should provide error feedback to clients via an
  HTTP 400 response code.
* Be accurate in defining input data constraints (like formats, ranges, lengths etc.) — and check constraints and return
  dedicated error information in case of violations.
* Prefer being more specific and restrictive (if compliant to functional requirements), e.g. by defining length range of
  strings. It may simplify implementation while providing freedom for further evolution as compatible extensions.

#### Must: Always Return JSON Objects As Top-Level Data Structures To Support Extensibility

In a response body, you must always return a JSON object (and not e.g. an array) as a top level data structure to
support future extensibility. JSON objects support compatible extension by additional attributes. This allows you to
easily extend your response and e.g. add pagination later, without breaking backwards compatibility.

Maps, even though technically objects are also forbidden as top level data structures, since they don’t support
compatible, future extensions.

#### Should: Used Open-Ended List of Values (x-extensible-enum) Instead of Enumerations

Enumerations are per definition closed sets of values, that are assumed to be complete and not intended for extension.
This closed principle of enumerations imposes compatibility issues when an enumeration must be extended. To avoid these
issues, we strongly recommend to use an open-ended list of values instead of an enumeration unless:

1. the API has full control of the enumeration values, i.e. the list of values does not depend on any external tool or
   interface, and

1. the list of value is complete with respect to any thinkable and unthinkable future feature.

To specify an open-ended list of values use the marker x-extensible-enum as follows:

```
deliver_methods:
  type: string
  x-extensible-enum:
    - parcel
    - letter
    - email
```

### 3. Requesting changes to this guide

Please contact any member from the Architecture Team to suggest changes or improvements.
