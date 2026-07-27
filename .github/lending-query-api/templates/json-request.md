# Query API — JSON Request Template

Generic JSON request structure for any LoanIQ Query API Integration class.

Replace `{EntityName}` with the actual business object name (e.g., `UpfrontFee`, `Deal`, `MISCode`).

## Template

```json
{
  "header": {
    "appId": "INTR",
    "isB2B": true
  },
  "attributes": {
    "liqBusinessObjects": {
      "liqBusinessObject": [
        {
          "name": "Query{EntityName}Integration",
          "className": "Query{EntityName}Integration",
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
                  "attribute": "{entityName}Identifier",
                  "valueType": "List",
                  "valueList": {
                    "liqBusinessObject": [
                      {
                        "name": "{EntityName}Identifier",
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
                                "attribute": "identifierType",
                                "value": "{id|alias|name}",
                                "valueType": "String"
                              },
                              {
                                "attribute": "identifierValue",
                                "value": "{entityIdentifierValue}",
                                "valueType": "String"
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

## Variant: Polymorphic Owner Pattern (MISCode, AdditionalFields)

```json
{
  "header": {
    "appId": "INTR",
    "isB2B": true
  },
  "attributes": {
    "liqBusinessObjects": {
      "liqBusinessObject": [
        {
          "name": "Query{EntityName}Integration",
          "className": "Query{EntityName}Integration",
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
                                "value": "{DEA|FAC}",
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

## Variant: Transaction Identifier Pattern (Loan Transactions)

```json
{
  "header": {
    "appId": "INTR",
    "isB2B": true
  },
  "attributes": {
    "liqBusinessObjects": {
      "liqBusinessObject": [
        {
          "name": "Query{EntityName}Integration",
          "className": "Query{EntityName}Integration",
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
                                "attribute": "loanTransactionId",
                                "value": "{transactionId}",
                                "valueType": "String"
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

## Field Reference

| Placeholder | Description | Example Values |
|---|---|---|
| `{EntityName}` | Business object name (PascalCase) | `UpfrontFee`, `Deal`, `MISCode` |
| `{entityName}` | Business object name (camelCase) | `upforntFee`, `deal`, `misCode` |
| `{id\|alias\|name}` | Identifier type | `id`, `alias`, `name`, `feeAlias` |
| `{entityIdentifierValue}` | Entity identifier value | System ID, alias, or name |
| `{DEA\|FAC}` | Owner type (polymorphic pattern) | `DEA` (Deal), `FAC` (Facility) |
| `{ownerIdentifierValue}` | Owner identifier value | Deal alias or facility ID |
| `{transactionId}` | Outstanding transaction ID | Loan transaction ID |

## Pattern Selection Guide

| Pattern | When to Use | Entities |
|---|---|---|
| **Standard** (entity identifier) | Top-level entities with direct ID lookup | Deal, Facility, Circle, HolidayCalendar, UserProfile, UpfrontFee |
| **Polymorphic Owner** | Entities belonging to different owner types | MISCode, AdditionalFields |
| **Transaction Identifier** | Outstanding transactions | LoanDrawdown, LoanIncrease, LoanInterestPayment, LoanPrincipalPayment, QuickLoanRepricing, SBLCDecrease, SBLCIncrease |

## Notes

- Query requests are minimal — only the identifier is required (no data fields)
- The `header.isAdmin` flag is typically omitted for Query (read-only)
- No `matchUpdatedTimestamp` is needed (read-only operation)
- No locking occurs during Query operations
- Response will contain the full entity data (all fields populated)
