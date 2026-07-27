# Create API — JSON Request Template

Generic JSON request structure for any LoanIQ Create API Integration class.

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
          "name": "Create{EntityName}Integration",
          "className": "Create{EntityName}Integration",
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
                  "attribute": "amount",
                  "valueType": "Money",
                  "value": "{amount}"
                },
                {
                  "attribute": "effectiveDate",
                  "valueType": "LiqDate",
                  "value": "{YYYY-MM-DD}"
                },
                {
                  "attribute": "fxRate",
                  "valueType": "BigDecimal",
                  "value": "{fxRate}"
                },
                {
                  "attribute": "eventComment",
                  "valueType": "String",
                  "value": "{comment}"
                },
                {
                  "attribute": "currencyCode",
                  "valueType": "String",
                  "value": "{currencyCode}"
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
| `{branchCode}` | Processing branch code | `00003`, `00002` |
| `{amount}` | Transaction amount (optional for some entities) | `1000.00`, `500` |
| `{YYYY-MM-DD}` | Effective date in ISO format | `2024-01-15` |
| `{fxRate}` | Foreign exchange rate | `1.0`, `0.85` |
| `{comment}` | Event comment text | `"Created via API"` |
| `{currencyCode}` | ISO 4217 currency code | `USD`, `EUR`, `GBP` |
| `{DEA\|FAC}` | Owner type (Deal or Facility) | `DEA`, `FAC` |
| `{id\|alias\|name}` | Identifier type | `id`, `alias`, `name` |
| `{ownerIdentifierValue}` | Owner identifier value | Deal alias or facility ID |
| `{customerIdentifierValue}` | Customer/borrower ID | System-generated ID |
| `{BORR\|LEND}` | Profile type | `BORR` (borrower), `LEND` (lender) |

## Notes

- The `amount` field is optional for some entities — omit if not applicable
- `ownerIdentifier` and `borrowerIdentifier` are entity-specific — not all Create APIs require them
- `servicingGroup` is optional and only applies to entities that support servicing groups
- Always include `version: "1.0"` at the entity level and within nested objects
- The `header.isAdmin` flag should be `true` only for administrative entities (UserProfile, UserSecurityProfile)
