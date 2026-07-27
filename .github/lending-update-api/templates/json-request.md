# Update API — JSON Request Template

Generic JSON request structure for any LoanIQ Update API Integration class.

Replace `{EntityName}` with the actual business object name (e.g., `UpfrontFee`, `Deal`, `LoanDrawdown`).

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
          "name": "Update{EntityName}Integration",
          "className": "Update{EntityName}Integration",
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
                  "attribute": "branchCode",
                  "valueType": "String",
                  "value": "{branchCode}"
                },
                {
                  "attribute": "effectiveDate",
                  "valueType": "LiqDate",
                  "value": "{YYYY-MM-DD}"
                },
                {
                  "attribute": "amount",
                  "valueType": "Money",
                  "value": "{amount}"
                },
                {
                  "attribute": "fxRate",
                  "valueType": "BigDecimal",
                  "value": "{fxRate}"
                },
                {
                  "attribute": "commentText",
                  "valueType": "String",
                  "value": "{comment}"
                },
                {
                  "attribute": "currencyCode",
                  "valueType": "String",
                  "value": "{currencyCode}"
                },
                {
                  "attribute": "sourceRefNum",
                  "valueType": "String",
                  "value": "{sourceReferenceNumber}"
                },
                {
                  "attribute": "systemSourceId",
                  "valueType": "String",
                  "value": "{systemSourceId}"
                },
                {
                  "attribute": "ownerIdentifiers",
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
                },
                {
                  "attribute": "borrowerIdentifier",
                  "valueType": "List",
                  "valueList": {
                    "liqBusinessObject": [
                      {
                        "name": "CustomerIdentifier",
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
                                "value": "{id|name}",
                                "valueType": "String"
                              },
                              {
                                "attribute": "identifierValue",
                                "value": "{customerIdentifierValue}",
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
                  "attribute": "servicingGroup",
                  "valueType": "List",
                  "valueList": {
                    "liqBusinessObject": [
                      {
                        "name": "ServicingGroup",
                        "className": "ServicingGroup",
                        "group": [
                          {
                            "item": [
                              {
                                "attribute": "version",
                                "value": "1.0",
                                "valueType": "String"
                              },
                              {
                                "attribute": "customerIdentifier",
                                "valueType": "List",
                                "valueList": {
                                  "liqBusinessObject": [
                                    {
                                      "name": "CustomerIdentifier",
                                      "className": "CustomerIdentifier",
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
                                              "value": "{customerIdentifierValue}"
                                            },
                                            {
                                              "attribute": "identifierType",
                                              "valueType": "String",
                                              "value": "{id|name}"
                                            }
                                          ]
                                        }
                                      ]
                                    }
                                  ]
                                }
                              },
                              {
                                "attribute": "alias",
                                "value": "{servicingGroupAlias}",
                                "valueType": "String"
                              },
                              {
                                "attribute": "locationCode",
                                "value": "{locationCode}",
                                "valueType": "String"
                              },
                              {
                                "attribute": "profileType",
                                "value": "{BORR|LEND}",
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
                  "attribute": "{entityName}Identifiers",
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
                                "value": "{id|alias|name}"
                              }
                            ]
                          }
                        ]
                      }
                    ]
                  }
                },
                {
                  "attribute": "feeDetails",
                  "valueType": "List",
                  "valueList": {
                    "liqBusinessObject": [
                      {
                        "name": "FeeDetails",
                        "className": "FeeDetails",
                        "group": [
                          {
                            "item": [
                              {
                                "attribute": "version",
                                "valueType": "String",
                                "value": "1.0"
                              },
                              {
                                "attribute": "amount",
                                "valueType": "BigDecimal",
                                "value": "{feeAmount}"
                              },
                              {
                                "attribute": "feeType",
                                "valueType": "String",
                                "value": "{feeTypeCode}"
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
| `{EntityName}` | Business object name (PascalCase) | `UpfrontFee`, `Deal`, `LoanDrawdown` |
| `{entityName}` | Business object name (camelCase) | `upfrontFee`, `deal`, `loanDrawdown` |
| `{branchCode}` | Processing branch code | `00002`, `00003` |
| `{YYYY-MM-DD}` | Effective date in ISO format | `2024-01-15` |
| `{amount}` | Transaction amount | `1000.00` |
| `{fxRate}` | Foreign exchange rate | `1.0`, `0.1` |
| `{comment}` | Comment/description text | `"Update Comment Text 1"` |
| `{currencyCode}` | ISO 4217 currency code | `USD`, `EUR`, `GBP` |
| `{sourceReferenceNumber}` | External source reference | `"UpdateUpfront123"` |
| `{systemSourceId}` | Source system identifier | `"LIQ"`, `"EXT"` |
| `{DEA\|FAC}` | Owner type | `DEA` (Deal), `FAC` (Facility) |
| `{entityIdentifierValue}` | Entity ID to update | System-generated ID |
| `{feeAmount}` | Fee detail amount | `200.00` |
| `{feeTypeCode}` | Fee type code | `SYNM`, `ORIG`, `UPFF`, `PREC` |

## Key Differences from Create

- Update requires `{entityName}Identifiers` to locate the entity being updated
- Update includes `sourceRefNum` and `systemSourceId` for audit traceability
- Update may include collection fields (e.g., `feeDetails`) for partial updates
- `matchUpdatedTimestamp` may be required (passed as a separate header or field for optimistic locking)

## Notes

- Entity identifier is **mandatory** for Update — it locates the entity to modify
- `feeDetails` is entity-specific — only entities with fee collections include this
- Negative amounts in `feeDetails` are valid for fee adjustments/reversals
- Multiple `feeDetails` entries can be included in a single Update request
- `servicingGroup` and `borrowerIdentifier` are optional — include only if updating those fields
- The `header.isAdmin` flag should be `true` only for administrative entities (UserProfile, UserSecurityProfile)
