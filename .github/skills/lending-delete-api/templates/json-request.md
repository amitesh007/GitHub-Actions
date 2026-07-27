# Delete API — JSON Request Template

Generic JSON request structure for any LoanIQ Delete API Integration class.

Replace `{EntityName}` with the actual business object name (e.g., `UpfrontFee`, `DealAdministrator`, `RepaymentSchedule`).

## Template

```json
{
  "header": {
    "appId": "INTR",
    "isB2B": true,
    "isAdmin": false
  },
  "attributes": {
    "liqBusinessObjects": {
      "liqBusinessObject": [
        {
          "name": "Delete{EntityName}Integration",
          "className": "Delete{EntityName}Integration",
          "group": [
            {
              "name": "heading",
              "item": [
                {
                  "attribute": "{entityName}Identifier",
                  "valueType": "List",
                  "valueList": {
                    "liqBusinessObject": [
                      {
                        "name": "{EntityName}Identifier",
                        "className": "{EntityName}Identifier",
                        "group": [
                          {
                            "item": [
                              {
                                "attribute": "version",
                                "valueType": "String",
                                "value": "1.0"
                              },
                              {
                                "attribute": "identifierValue",
                                "valueType": "String",
                                "value": "{entityIdentifierValue}"
                              },
                              {
                                "attribute": "identifierType",
                                "valueType": "String",
                                "value": "{id|alias|feeAlias}"
                              }
                            ]
                          }
                        ]
                      }
                    ]
                  }
                }
              ]
            }
          ]
        }
      ]
    }
  }
}
```

## Variant: Owner-Based Delete Pattern (DealAdministrator, MISCode)

```json
{
  "header": {
    "appId": "INTR",
    "isB2B": true,
    "isAdmin": false
  },
  "attributes": {
    "liqBusinessObjects": {
      "liqBusinessObject": [
        {
          "name": "Delete{EntityName}Integration",
          "className": "Delete{EntityName}Integration",
          "group": [
            {
              "name": "heading",
              "item": [
                {
                  "attribute": "version",
                  "valueType": "String",
                  "value": "1.0"
                },
                {
                  "attribute": "ownerIdentifier",
                  "valueType": "List",
                  "valueList": {
                    "liqBusinessObject": [
                      {
                        "name": "OwnerIdentifier",
                        "group": [
                          {
                            "name": "heading",
                            "item": [
                              {
                                "attribute": "version",
                                "value": "1.0",
                                "valueType": "String"
                              },
                              {
                                "attribute": "ownerType",
                                "value": "{DEA|FAC|LNID|OST|QLR}",
                                "valueType": "String"
                              },
                              {
                                "attribute": "ownerIdentifierType",
                                "value": "{id|alias|name}",
                                "valueType": "String"
                              },
                              {
                                "attribute": "ownerIdentifierValue",
                                "value": "{ownerIdentifierValue}",
                                "valueType": "String"
                              }
                            ]
                          }
                        ]
                      }
                    ]
                  }
                },
                {
                  "attribute": "{entityName}Identifier",
                  "valueType": "List",
                  "valueList": {
                    "liqBusinessObject": [
                      {
                        "name": "{EntityName}Identifier",
                        "className": "{EntityName}Identifier",
                        "group": [
                          {
                            "item": [
                              {
                                "attribute": "version",
                                "valueType": "String",
                                "value": "1.0"
                              },
                              {
                                "attribute": "identifierValue",
                                "valueType": "String",
                                "value": "{entityIdentifierValue}"
                              },
                              {
                                "attribute": "identifierType",
                                "valueType": "String",
                                "value": "{id|alias}"
                              }
                            ]
                          }
                        ]
                      }
                    ]
                  }
                }
              ]
            }
          ]
        }
      ]
    }
  }
}
```

## Variant: Transaction-Based Delete Pattern (LoanPrincipalPayment, FlexUnscheduledTransaction)

```json
{
  "header": {
    "appId": "INTR",
    "isB2B": true,
    "isAdmin": false
  },
  "attributes": {
    "liqBusinessObjects": {
      "liqBusinessObject": [
        {
          "name": "Delete{EntityName}Integration",
          "className": "Delete{EntityName}Integration",
          "group": [
            {
              "name": "heading",
              "item": [
                {
                  "attribute": "version",
                  "valueType": "String",
                  "value": "1.0"
                },
                {
                  "attribute": "outstandingTransactionIdentifier",
                  "valueType": "List",
                  "valueList": {
                    "liqBusinessObject": [
                      {
                        "name": "OutstandingTransactionIdentifier",
                        "className": "OutstandingTransactionIdentifier",
                        "group": [
                          {
                            "item": [
                              {
                                "attribute": "version",
                                "valueType": "String",
                                "value": "1.0"
                              },
                              {
                                "attribute": "loanTransactionId",
                                "valueType": "String",
                                "value": "{transactionId}"
                              }
                            ]
                          }
                        ]
                      }
                    ]
                  }
                },
                {
                  "attribute": "matchUpdatedTimestamp",
                  "valueType": "String",
                  "value": "{yyyy-MM-dd HH:mm:ss.S}"
                }
              ]
            }
          ]
        }
      ]
    }
  }
}
```

## Field Reference

| Placeholder | Description | Example Values |
|---|---|---|
| `{EntityName}` | Business object name (PascalCase) | `UpfrontFee`, `DealAdministrator`, `RepaymentSchedule` |
| `{entityName}` | Business object name (camelCase) | `upfrontFee`, `dealAdministrator`, `repaymentSchedule` |
| `{entityIdentifierValue}` | Entity ID to delete | `"UFF000000000021"`, system-generated ID |
| `{id\|alias\|feeAlias}` | Identifier type | `id`, `alias`, `feeAlias` |
| `{DEA\|FAC\|LNID\|OST\|QLR}` | Owner type | `DEA`, `FAC`, `LNID`, `OST`, `QLR` |
| `{ownerIdentifierValue}` | Owner identifier value | Deal alias, facility ID |
| `{transactionId}` | Outstanding transaction ID | Loan transaction ID |
| `{yyyy-MM-dd HH:mm:ss.S}` | Timestamp for optimistic locking | `"2024-01-15 10:30:45.0"` |

## Pattern Selection Guide

| Pattern | When to Use | Entities |
|---|---|---|
| **Direct Identifier** | Entity identified by its own ID/alias | UpfrontFee, PayoffStatement |
| **Owner-Based** | Entity belongs to an owner (Deal/Facility/Loan) | DealAdministrator, MISCode, ProductGuarantee, DealInterestPricingOption, FacilityInterestPricing |
| **Transaction-Based** | Outstanding transaction entities | LoanPrincipalPayment, FlexUnscheduledTransaction, RepaymentSchedule |

## Notes

- Delete requests are identifier-only — no data fields are included (unlike Create/Update)
- `matchUpdatedTimestamp` is required for transaction-based deletes (optimistic concurrency)
- The `header.isAdmin` flag should be `false` for all Delete operations
- Some entities require state validation before deletion (e.g., Deal must not be in active workflow)
- Delete is a destructive operation — the entity is permanently removed
- Response will confirm the deletion and return the deleted entity identifier
