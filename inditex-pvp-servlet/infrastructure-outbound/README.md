# inditex-pvp-servlet - `INFRASTRUCTURE-OUTBOUND` module

## Assumptions

- All columns are mandatory (they can not have null values)
- The number of products is high so an index is created for product_id
- The number of brands is low so an index is not created because despite it is used in the searches, the low cardinality makes it useless. 
