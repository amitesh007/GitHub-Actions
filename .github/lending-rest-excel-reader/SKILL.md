---
name: lending-rest-excel-reader
description: 'Executes PowerShell script to generate LoanIQ REST API baseline classes (Create, Update, Query) from Excel requirement spreadsheets. Validates inputs, runs IntegrationAPITool JAR, and provides clear error messages for developer troubleshooting.'
---

# LoanIQ REST API Excel Reader Skill

## Purpose

This skill provides a **deterministic interface** to the `run-excel-reader.ps1` PowerShell script, which generates baseline LoanIQ REST API classes from Excel requirement spreadsheets.

**Scope:**
- Execute `IntegrationAPITool` JAR to parse Excel spreadsheets
- Generate baseline API classes: `LiqAPICreate{Entity}Integration`, `LiqAPIUpdate{Entity}Integration`, `LiqAPIQuery{Entity}Integration`
- Generate baseline Return Value classes: `LiqAPI{Entity}IntegrationAsReturnValue`
- Generate baseline test classes: `LiqAPICreate{Entity}IntegrationTest`, etc.
- Validate inputs before execution
- Provide **clear, actionable error messages** for developers

## When to Use This Skill

Use this skill when:
- ✅ Starting new LoanIQ REST API generation from Excel requirements
- ✅ Generating baseline classes before applying API-specific SKILL.md patterns
- ✅ Need to re-generate classes after Excel spreadsheet updates
- ✅ First step in the `lending-api-developer` agent workflow

**DO NOT use this skill for:**
- ❌ Modifying generated classes per SKILL.md patterns (use API-specific skills instead)
- ❌ Generating test implementations (use test-specific skills)
- ❌ Adding Javadoc to generated classes (done in later workflow steps)

## Sample Prompts

### Using the Skill Directly

**Prompt 1: Generate API classes from Excel spreadsheet**
```
@workspace Use the lending-rest-excel-reader skill to generate API classes from the Excel file at:
C:\Auto\API\Deal API Requirements v2.xlsx
```

**Prompt 2: Generate with error handling**
```
@workspace Run the lending-rest-excel-reader skill with the spreadsheet:
C:\Requirements\Facility API Specification.xlsx

If the script fails, provide clear error messages explaining what went wrong and how to fix it.
```

**Prompt 3: Generate for specific entity**
```
@workspace I need to generate LoanIQ REST API classes for the AdditionalFields entity. 
The Excel requirement spreadsheet is located at:
C:\API Specs\Additional Fields API v1.xlsx

Please use the lending-rest-excel-reader skill to generate the baseline classes.
```

### Using Through the Lending API Developer Agent

**Prompt 1: Full API generation workflow (invokes this skill automatically)**
```
@lending-api-developer Generate Create, Update, and Query APIs for the Deal entity using:
C:\Auto\API\Deal API Requirements v2.xlsx
```

**Prompt 2: Generate specific API types**
```
@lending-api-developer Generate Update and Query APIs (no Create) for the Facility entity.
Excel file: C:\Requirements\Facility Update Query APIs.xlsx
```

**Prompt 3: Re-generate after spreadsheet updates**
```
@lending-api-developer I've updated the Excel spreadsheet at:
C:\API Specs\Loan Drawdown API v3.xlsx

Please regenerate all API classes (Create, Update, Query) with the latest changes.
```

### Direct Script Invocation (PowerShell)

**Command Line Usage:**
```powershell
# Navigate to repository root
cd C:\Users\asrivas3\git\7750_1\FLIQ-liqjava

# Run the script directly
.\.github\skills\lending-rest-excel-reader\scripts\run-excel-reader.ps1 "C:\Auto\API\Deal API Requirements v2.xlsx"
```

**Expected Output:**
```
Running IntegrationAPITool with file: C:\Auto\API\Deal API Requirements v2.xlsx
Processing Excel file...
Generating Create API class...
Generating Update API class...
Generating Query API class...
Generating ReturnValue class...
Generating test classes...

Execution completed successfully!
Generated classes can be found in: IntegrationAPITool\artifacts\temp-generated_class\
```

## Prerequisites

Before invoking this skill, ensure:
1. ✅ Excel requirement spreadsheet exists and is accessible
2. ✅ Excel file has `.xlsx` or `.xls` extension
3. ✅ IntegrationAPITool JAR exists at: `FLIQ-liqjava/IntegrationAPITool/artifacts/executable/IntegrationAPITool-1.0.jar`
4. ✅ Output directory exists: `FLIQ-liqjava/IntegrationAPITool/artifacts/temp-generated_class/`
5. ✅ PowerShell execution policy allows script execution

## Script Location

**Script:** `.github/skills/lending-rest-excel-reader/scripts/run-excel-reader.ps1`

**JAR Dependency:** `FLIQ-liqjava/IntegrationAPITool/artifacts/executable/IntegrationAPITool-1.0.jar`

## Input Requirements

### Required Input

1. **Excel File Path** (String) - Full absolute path to the Excel requirement spreadsheet
   - Example: `C:\Auto\API\Additional Fields API v1.xlsx`
   - Validation: Must exist, must have `.xlsx` or `.xls` extension
   - Must NOT contain path traversal characters (`..`)

### Optional Context

- **Entity Name** (String) - Name of the LoanIQ entity (e.g., "Deal", "Facility", "AdditionalFields")
  - Used for context in error messages
  - Not required by the script itself

## Execution Workflow

### Step 1: Validate Inputs

Before executing the script, validate:

```powershell
# Check 1: File path provided
if (-not $ExcelFilePath) {
    Write-Error "❌ Excel file path is required"
    exit 1
}

# Check 2: File exists
if (-not (Test-Path $ExcelFilePath)) {
    Write-Error "❌ Excel file not found at: $ExcelFilePath"
    exit 1
}

# Check 3: Valid extension
$extension = [System.IO.Path]::GetExtension($ExcelFilePath)
if ($extension -notin @('.xlsx', '.xls')) {
    Write-Error "❌ Invalid file extension: $extension (expected .xlsx or .xls)"
    exit 1
}

# Check 4: No path traversal
if ($ExcelFilePath -match '\.\.') {
    Write-Error "❌ Path traversal detected in file path (contains '..')"
    exit 1
}
```

### Step 2: Execute PowerShell Script

Run the script with the validated file path:

```powershell
.github\skills\lending-rest-excel-reader\scripts\run-excel-reader.ps1 "<ExcelFilePath>"
```

**Example:**
```powershell
.github\skills\lending-rest-excel-reader\scripts\run-excel-reader.ps1 "C:\Auto\API\Additional Fields API v1.xlsx"
```

### Step 3: Monitor Execution and Capture Output

The script will:
1. Validate JAR file exists at `IntegrationAPITool/artifacts/executable/IntegrationAPITool-1.0.jar`
2. Change directory to `IntegrationAPITool/`
3. Execute: `java -jar IntegrationAPITool-1.0.jar` with Excel file path piped to stdin
4. Return to original directory
5. Report success or failure

### Step 4: Handle Errors

**Common Error Scenarios:**

| Error | Cause | Developer Action |
|-------|-------|-----------------|
| **Excel file not found** | File path incorrect or file doesn't exist | Verify file path is correct and file exists |
| **JAR file not found** | IntegrationAPITool not built | Run `mvnw.cmd clean package -DskipTests` to build the JAR |
| **Java execution failed** | Invalid Excel format or missing required tabs | Open Excel file and verify all required tabs exist (Create, Update, Query) with correct structure |
| **Invalid entity name in Excel** | Entity name mismatch or typo | Check Excel spreadsheet entity name matches expected LoanIQ business object |
| **Missing columns in Excel** | Required columns not present in spreadsheet | Add required columns: FieldName, FieldType, Mandatory, etc. |
| **Script execution policy error** | PowerShell execution policy blocks scripts | Run: `Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser` |

### Step 5: Verify Generated Output

After successful execution, verify:

```powershell
# Check output directory
$outputDir = "FLIQ-liqjava\IntegrationAPITool\artifacts\temp-generated_class\"

# Expected files (depending on API types in Excel):
# - LiqAPICreate{Entity}Integration.java
# - LiqAPIUpdate{Entity}Integration.java
# - LiqAPIQuery{Entity}Integration.java
# - LiqAPI{Entity}IntegrationAsReturnValue.java
# - LiqAPICreate{Entity}IntegrationTest.java
# - LiqAPIUpdate{Entity}IntegrationTest.java
# - LiqAPIQuery{Entity}IntegrationTest.java

if (-not (Test-Path $outputDir)) {
    Write-Error "❌ Output directory not found: $outputDir"
    exit 1
}

$generatedFiles = Get-ChildItem -Path $outputDir -Filter "*.java"
if ($generatedFiles.Count -eq 0) {
    Write-Error "❌ No Java files generated in: $outputDir"
    exit 1
}

Write-Host "✅ Generated $($generatedFiles.Count) Java files successfully"
```

## Error Messages and Troubleshooting

### Developer-Friendly Error Messages

When reporting errors, provide:
1. ✅ **What failed** - Specific operation that failed
2. ✅ **Why it failed** - Root cause or likely reason
3. ✅ **How to fix** - Concrete action developer can take
4. ✅ **Context** - File paths, command executed, etc.

**Example Error Message Format:**

```
❌ SCRIPT EXECUTION FAILED

Error: Excel file not found
File: C:\Auto\API\Additional Fields API v1.xlsx
Reason: The specified file does not exist or is not accessible

Action Required:
1. Verify the file path is correct
2. Ensure the file exists at the specified location
3. Check file permissions (read access required)
4. Try using absolute path instead of relative path

Tip: Use Tab completion in PowerShell to auto-complete file paths and avoid typos.
```

### Common Excel Spreadsheet Issues

**Issue 1: Missing Required Tabs**

Error: `Sheet 'Create' not found in workbook`

**Fix:**
1. Open Excel file
2. Add missing tab: "Create", "Update", or "Query"
3. Populate tab with required columns:
   - FieldName
   - FieldType
   - Mandatory (Y/N)
   - Description

**Issue 2: Invalid Field Type**

Error: `Unknown field type: 'STRNG' at row 5`

**Fix:**
1. Open Excel file → Navigate to row 5
2. Verify field type is one of: `STRING`, `BIGDECIMAL`, `DATE`, `BOOLEAN`, `LIST`
3. Correct typo (e.g., `STRNG` → `STRING`)
4. Save Excel file
5. Re-run script

**Issue 3: Entity Name Mismatch**

Error: `Entity 'Addtional Fields' does not match expected pattern`

**Fix:**
1. Check entity name in Excel (first sheet, cell A1 or header row)
2. Verify spelling matches LoanIQ business object name
3. Use PascalCase (e.g., `AdditionalFields`, not `Additional Fields`)
4. Re-run script

**Issue 4: JAR File Missing**

Error: `JAR file not found at 'IntegrationAPITool/artifacts/executable/IntegrationAPITool-1.0.jar'`

**Fix:**
1. Navigate to repository root
2. Run: `mvnw.cmd clean package -DskipTests`
3. Wait for Maven build to complete
4. Verify JAR exists: `dir IntegrationAPITool\artifacts\executable\IntegrationAPITool-1.0.jar`
5. Re-run script

## Output Structure

**Generated files location:** `FLIQ-liqjava/IntegrationAPITool/artifacts/temp-generated_class/`

**Expected files:**

```
temp-generated_class/
├── LiqAPICreate{Entity}Integration.java          (if Create tab exists in Excel)
├── LiqAPIUpdate{Entity}Integration.java          (if Update tab exists in Excel)
├── LiqAPIQuery{Entity}Integration.java           (if Query tab exists in Excel)
├── LiqAPI{Entity}IntegrationAsReturnValue.java   (always generated)
├── LiqAPICreate{Entity}IntegrationTest.java      (if Create tab exists)
├── LiqAPIUpdate{Entity}IntegrationTest.java      (if Update tab exists)
└── LiqAPIQuery{Entity}IntegrationTest.java       (if Query tab exists)
```

**File characteristics:**
- ✅ Baseline implementations (may have TODO comments)
- ✅ Basic structure with field declarations
- ✅ Placeholder methods (to be completed by API-specific skills)
- ✅ Import statements (may need additions)

**Next steps after generation:**
1. Apply API-specific SKILL.md patterns (lending-create-api, lending-update-api, lending-query-api)
2. Complete method implementations
3. Add complete import statements
4. Add Javadoc
5. Enhance test classes with comprehensive tests

## Integration with Lending API Developer Agent

This skill is **Step 1** in the `lending-api-developer` agent workflow:

```
Step 1: Run lending-rest-excel-reader skill (THIS SKILL)
         ↓
Step 2: Determine API Type Scope
         ↓
Step 3: Load respective SKILL.md files
         ↓
Step 4: Modify generated classes per SKILL.md + other repo patterns
         ↓
Step 5: Modify test classes
         ↓
Step 6: Add Javadoc
         ↓
Step 7: Check for conflicts and copy to repository
         ↓
Step 8: Generate JSON examples
         ↓
Step 9: Validate and summarize
         ↓
Step 10: Generate Review.md
```

**Agent must:**
1. ✅ Call this skill FIRST before any modifications
2. ✅ Check exit code: If non-zero, STOP workflow immediately
3. ✅ Report clear error messages from this skill to user
4. ✅ Only proceed to Step 2 if this skill succeeds

## Guardrails

**Hard-stop conditions** (script must exit with error):

1. ❌ Excel file path not provided → Exit code 1
2. ❌ Excel file does not exist → Exit code 1
3. ❌ Excel file has invalid extension → Exit code 1
4. ❌ Path traversal detected (`..` in path) → Exit code 1
5. ❌ JAR file not found → Exit code 1
6. ❌ Java execution failed → Exit code from Java process
7. ❌ No Java files generated → Exit code 1

**Success condition:**
- ✅ Script exits with code 0
- ✅ At least one `.java` file exists in `temp-generated_class/`
- ✅ Console output shows "Execution completed successfully!"

## Example Usage

### Successful Execution

```powershell
PS> .github\skills\lending-rest-excel-reader\scripts\run-excel-reader.ps1 "C:\Auto\API\Deal API v2.xlsx"

Running IntegrationAPITool with file: C:\Auto\API\Deal API v2.xlsx
Processing Excel file...
Generating Create API class...
Generating Update API class...
Generating Query API class...
Generating ReturnValue class...
Generating test classes...

Execution completed successfully!
Generated classes can be found in: IntegrationAPITool\artifacts\temp-generated_class\

PS> dir IntegrationAPITool\artifacts\temp-generated_class\

    Directory: C:\...\IntegrationAPITool\artifacts\temp-generated_class

Mode                 LastWriteTime         Length Name
----                 -------------         ------ ----
-a---           6/2/2026   2:30 PM          15234 LiqAPICreateDealIntegration.java
-a---           6/2/2026   2:30 PM          18456 LiqAPIUpdateDealIntegration.java
-a---           6/2/2026   2:30 PM          12345 LiqAPIQueryDealIntegration.java
-a---           6/2/2026   2:30 PM          10234 LiqAPIDealIntegrationAsReturnValue.java
-a---           6/2/2026   2:30 PM           8234 LiqAPICreateDealIntegrationTest.java
-a---           6/2/2026   2:30 PM           8456 LiqAPIUpdateDealIntegrationTest.java
-a---           6/2/2026   2:30 PM           7234 LiqAPIQueryDealIntegrationTest.java
```

### Failed Execution - File Not Found

```powershell
PS> .github\skills\lending-rest-excel-reader\scripts\run-excel-reader.ps1 "C:\Auto\API\NonExistent.xlsx"

❌ SCRIPT EXECUTION FAILED

Error: Excel file not found
File: C:\Auto\API\NonExistent.xlsx
Reason: The specified file does not exist or is not accessible

Action Required:
1. Verify the file path is correct
2. Ensure the file exists at the specified location
3. Check file permissions (read access required)
4. Try using absolute path instead of relative path

Tip: Use Tab completion in PowerShell to auto-complete file paths and avoid typos.
```

### Failed Execution - JAR Not Built

```powershell
PS> .github\skills\lending-rest-excel-reader\scripts\run-excel-reader.ps1 "C:\Auto\API\Deal API v2.xlsx"

❌ SCRIPT EXECUTION FAILED

Error: JAR file not found
Location: IntegrationAPITool\artifacts\executable\IntegrationAPITool-1.0.jar
Reason: IntegrationAPITool has not been built yet

Action Required:
1. Navigate to repository root
2. Run: mvnw.cmd clean package -DskipTests
3. Wait for Maven build to complete
4. Verify JAR exists: dir IntegrationAPITool\artifacts\executable\IntegrationAPITool-1.0.jar
5. Re-run this script

Tip: The build takes ~2-3 minutes. You only need to build once per repository clone.
```

## Summary

This skill provides a **safe, validated, and user-friendly interface** to the Excel-to-API baseline generation script. It:

✅ Validates all inputs before execution  
✅ Provides clear, actionable error messages  
✅ Handles common failure scenarios gracefully  
✅ Integrates seamlessly with `lending-api-developer` agent  
✅ Follows LoanIQ security and validation standards  
✅ Outputs baseline classes ready for modification by API-specific skills

**Next Step:** After this skill succeeds, proceed with API-specific SKILL.md patterns to complete method implementations, add business logic, and generate production-ready code.

---

## Quick Start Guide

**Minimal example to generate API classes:**

1. **Prepare your Excel spreadsheet** with required tabs: Create, Update, and/or Query
2. **Save the file** (e.g., `C:\API\MyEntity.xlsx`)
3. **Use the skill** with this simple prompt:

```
@workspace Use lending-rest-excel-reader to generate API classes from:
C:\API\MyEntity.xlsx
```

4. **Check the output** at: `FLIQ-liqjava\IntegrationAPITool\artifacts\temp-generated_class\`

**That's it!** The baseline API classes are now generated and ready for modification using API-specific skills.

---

## Skill Metadata

| Property | Value |
|----------|-------|
| **Skill Name** | `lending-rest-excel-reader` |
| **Skill Type** | Script Executor / Code Generator |
| **Primary Agent** | `lending-api-developer` |
| **Script Language** | PowerShell |
| **Output Format** | Java source files (`.java`) |
| **Deterministic** | Yes (same Excel → same baseline classes) |
| **Idempotent** | Yes (can be re-run safely) |
| **Side Effects** | Generates files in `temp-generated_class/` folder |
