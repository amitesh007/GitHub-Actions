<# 
.SYNOPSIS
    Validates and extracts Update API class hierarchy information for ANY LoanIQ entity.

.DESCRIPTION
    Universal script that reads existing Java source files for a given Update API Integration class,
    validates source files are present, extracts @LiqAPIFieldMapper annotations, class hierarchy,
    primitive field mappings, and business logic methods.

.PARAMETER EntityName
    The business object name (e.g., "Deal", "Facility", "LoanDrawdown", "MISCode")

.PARAMETER PackagePath
    The relative package path under LoanIQ/srcgen/com/misys/liq/api/rest/executable/
    (e.g., "deal", "facility", "outstanding/drawdown", "miscode")

.EXAMPLE
    .\generate-update-api.ps1 -EntityName "Deal" -PackagePath "deal"
    .\generate-update-api.ps1 -EntityName "LoanDrawdown" -PackagePath "outstanding/drawdown"
    .\generate-update-api.ps1 -EntityName "UserProfile" -PackagePath "user"
    .\generate-update-api.ps1 -EntityName "MISCode" -PackagePath "miscode"
    .\generate-update-api.ps1 -EntityName "OutgoingACHMessage" -PackagePath "cashflow"

.NOTES
    Skill: lending-update-api (unified)
    Supports all 33 business objects in the Update API catalog
#>

param(
    [Parameter(Mandatory = $true)]
    [string]$EntityName,

    [Parameter(Mandatory = $true)]
    [string]$PackagePath,

    [string]$WorkspaceRoot = (Split-Path -Parent (Split-Path -Parent (Split-Path -Parent $PSScriptRoot)))
)

$ErrorActionPreference = "Stop"

# === CONFIGURATION ===
$SourceBasePath = Join-Path $WorkspaceRoot "LoanIQ\srcgen\com\misys\liq\api\rest"
$PackageDir = $PackagePath -replace '/', '\'

# Determine the main class file name
$MainClassName = "LiqAPIUpdate${EntityName}Integration"
$MainClassFile = Join-Path $SourceBasePath "executable\$PackageDir\${MainClassName}.java"

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  LoanIQ Update API Validation" -ForegroundColor Cyan
Write-Host "  Entity: $EntityName" -ForegroundColor Cyan
Write-Host "  Package: $PackagePath" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# === STEP 1: Validate Source Files Exist ===
Write-Host "`n=== Step 1: Validating Source Files ===" -ForegroundColor Cyan

if (Test-Path $MainClassFile) {
    Write-Host "  [OK] $MainClassName" -ForegroundColor Green
} else {
    Write-Host "  [MISSING] $MainClassName at:" -ForegroundColor Red
    Write-Host "    $MainClassFile" -ForegroundColor Red
    Write-Error "Main source file not found. Cannot proceed."
    exit 1
}

# Check for ReturnValue class
$ReturnValueFiles = Get-ChildItem -Path (Join-Path $SourceBasePath "executable\$PackageDir") -Filter "*${EntityName}*AsReturnValue.java" -ErrorAction SilentlyContinue
# Also check data directory
$ReturnValueDataDir = Join-Path $SourceBasePath "data\$PackageDir"
if (Test-Path $ReturnValueDataDir) {
    $ReturnValueFilesData = Get-ChildItem -Path $ReturnValueDataDir -Filter "*${EntityName}*AsReturnValue.java" -ErrorAction SilentlyContinue
    if ($ReturnValueFilesData) {
        $ReturnValueFiles = @($ReturnValueFiles) + @($ReturnValueFilesData) | Where-Object { $_ -ne $null }
    }
}

if ($ReturnValueFiles -and $ReturnValueFiles.Count -gt 0) {
    foreach ($rvf in $ReturnValueFiles) {
        Write-Host "  [OK] ReturnValue: $($rvf.Name)" -ForegroundColor Green
    }
} else {
    Write-Host "  [INFO] No ReturnValue class found (may be inherited)" -ForegroundColor Yellow
}

# === STEP 2: Extract @LiqAPIFieldMapper Annotations ===
Write-Host "`n=== Step 2: Extracting @LiqAPIFieldMapper Annotations ===" -ForegroundColor Cyan

$content = Get-Content $MainClassFile -Raw
$annotationPattern = '@LiqAPIFieldMapper\s*\(\s*name\s*=\s*"([^"]+)"\s*,\s*className\s*=\s*"([^"]+)"\s*\)'
$annotationMatches = [regex]::Matches($content, $annotationPattern)

if ($annotationMatches.Count -gt 0) {
    Write-Host "  Found $($annotationMatches.Count) @LiqAPIFieldMapper annotation(s):" -ForegroundColor Yellow
    foreach ($match in $annotationMatches) {
        $fieldName = $match.Groups[1].Value
        $className = $match.Groups[2].Value
        $shortClass = $className.Split('.')[-1]
        Write-Host "    name=`"$fieldName`" -> $shortClass" -ForegroundColor White
    }
} else {
    Write-Host "  No @LiqAPIFieldMapper annotations found (all fields are primitives or inherited)" -ForegroundColor Yellow
}

# === STEP 3: Extract Class Hierarchy ===
Write-Host "`n=== Step 3: Extracting Class Hierarchy ===" -ForegroundColor Cyan

$classPattern = 'public\s+class\s+(\w+)\s+extends\s+(\w+)'
$implementsPattern = 'implements\s+([\w\s,]+)\{'
$classMatch = [regex]::Match($content, $classPattern)
$implMatch = [regex]::Match($content, $implementsPattern)

if ($classMatch.Success) {
    $className = $classMatch.Groups[1].Value
    $superClass = $classMatch.Groups[2].Value
    $interfaces = if ($implMatch.Success) { $implMatch.Groups[1].Value.Trim() } else { "none" }
    
    Write-Host "  Class: $className" -ForegroundColor White
    Write-Host "  Extends: $superClass" -ForegroundColor White
    Write-Host "  Implements: $interfaces" -ForegroundColor White
    
    if ($superClass -eq "LiqAPIExecutableData") {
        Write-Host "  Pattern: A (Direct Extension)" -ForegroundColor Green
    } else {
        Write-Host "  Pattern: B (Intermediate Base)" -ForegroundColor Green
    }
}

# === STEP 4: Extract Primitive Field Declarations ===
Write-Host "`n=== Step 4: Extracting Primitive Fields ===" -ForegroundColor Cyan

$fieldPattern = '(?:public|protected)\s+(String|Boolean|boolean|BigDecimal|LiqDate|Integer|Long|Date)\s+(\w+)\s*[;=]'
$fieldMatches = [regex]::Matches($content, $fieldPattern)

if ($fieldMatches.Count -gt 0) {
    Write-Host "  Found $($fieldMatches.Count) primitive field(s):" -ForegroundColor Yellow
    foreach ($fieldMatch in $fieldMatches) {
        $fieldType = $fieldMatch.Groups[1].Value
        $fieldName = $fieldMatch.Groups[2].Value
        Write-Host "    $fieldName : $fieldType" -ForegroundColor White
    }
} else {
    Write-Host "  No primitive fields declared (all inherited from parent)" -ForegroundColor Yellow
}

# === STEP 5: Extract Collection Fields ===
Write-Host "`n=== Step 5: Extracting Collection Fields ===" -ForegroundColor Cyan

$listPattern = 'public\s+List<(\w+)>\s+(\w+)\s*;'
$listMatches = [regex]::Matches($content, $listPattern)

if ($listMatches.Count -gt 0) {
    Write-Host "  Found $($listMatches.Count) collection field(s):" -ForegroundColor Yellow
    foreach ($listMatch in $listMatches) {
        $elementType = $listMatch.Groups[1].Value
        $fieldName = $listMatch.Groups[2].Value
        Write-Host "    $fieldName : List<$elementType>" -ForegroundColor White
    }
} else {
    Write-Host "  No collection fields declared" -ForegroundColor Yellow
}

# === STEP 6: Validate Business Logic Methods ===
Write-Host "`n=== Step 6: Validating Business Logic Methods ===" -ForegroundColor Cyan

$methodsToCheck = @(
    "basicExecute",
    "basicValidate",
    "checkDealSecurity",
    "checkCustomerSecurity",
    "lockAPIData",
    "unLockAPIData",
    "response",
    "singleCommit",
    "validateTimeStamp",
    "performUpdate",
    "updateFields"
)

foreach ($method in $methodsToCheck) {
    $found = $content -match "\b$method\s*\("
    $status = if ($found) { "[DECLARED/CALLED]" } else { "[INHERITED/MISSING]" }
    $color = if ($found) { "Green" } else { "Yellow" }
    Write-Host "  $status $method()" -ForegroundColor $color
}

# === STEP 7: Detect basicExecute Pattern ===
Write-Host "`n=== Step 7: Detecting basicExecute Pattern ===" -ForegroundColor Cyan

$hasCheckDealSecurity = $content -match "checkDealSecurity\s*\(\s*\)"
$hasCheckCustomerSecurity = $content -match "checkCustomerSecurity\s*\(\s*\)"
$hasLock = $content -match "lockAPIData\s*\(\s*\)"
$hasUnlock = $content -match "unLockAPIData\s*\(\s*\)|unlockData\s*\(\s*\)"
$hasSuperBasicExecute = $content -match "super\.basicExecute\s*\(\s*\)"
$hasCustomUpdate = $content -match "this\.\w*[Uu]pdate\w+\(\s*\)|this\.perform\w+\(\s*\)"

if (-not $hasCheckDealSecurity -and -not $hasCheckCustomerSecurity) {
    Write-Host "  Pattern: Admin (No security checks)" -ForegroundColor Magenta
} elseif ($hasSuperBasicExecute) {
    Write-Host "  Pattern: Delegate to super.basicExecute()" -ForegroundColor Green
} elseif ($hasCustomUpdate) {
    Write-Host "  Pattern: Custom update method" -ForegroundColor Yellow
} else {
    Write-Host "  Pattern: Standard try/finally" -ForegroundColor Green
}

Write-Host "  Lock: $(if($hasLock){'Yes'}else{'No'}) | Unlock: $(if($hasUnlock){'Yes'}else{'No'})" -ForegroundColor White

# === STEP 8: Extract update*() Methods ===
Write-Host "`n=== Step 8: Extracting update*() Methods ===" -ForegroundColor Cyan

$updateMethodPattern = '(?:public|private|protected)\s+[\w<>\[\]]+\s+(update\w+)\s*\('
$updateMatches = [regex]::Matches($content, $updateMethodPattern)

if ($updateMatches.Count -gt 0) {
    Write-Host "  Found $($updateMatches.Count) update method(s):" -ForegroundColor Yellow
    foreach ($um in $updateMatches) {
        Write-Host "    $($um.Groups[1].Value)()" -ForegroundColor White
    }
} else {
    Write-Host "  No update methods found (delegates to super)" -ForegroundColor Yellow
}

# === STEP 9: Report Summary ===
Write-Host "`n=== Summary ===" -ForegroundColor Cyan
Write-Host "  Entity: $EntityName" -ForegroundColor White
Write-Host "  Main Class: $MainClassName" -ForegroundColor White
Write-Host "  Package: com.misys.liq.api.rest.executable.$($PackagePath -replace '/', '.')" -ForegroundColor White
Write-Host "  @LiqAPIFieldMapper count: $($annotationMatches.Count)" -ForegroundColor White
Write-Host "  Primitive fields: $($fieldMatches.Count)" -ForegroundColor White
Write-Host "  Collection fields: $($listMatches.Count)" -ForegroundColor White
Write-Host "  Update methods: $($updateMatches.Count)" -ForegroundColor White

if ($classMatch.Success) {
    $superClass = $classMatch.Groups[2].Value
    if ($superClass -eq "LiqAPIExecutableData") {
        Write-Host "  Inheritance: Pattern A (extends LiqAPIExecutableData)" -ForegroundColor White
    } else {
        Write-Host "  Inheritance: Pattern B (extends $superClass)" -ForegroundColor White
    }
}

Write-Host "`n  Validation complete." -ForegroundColor Green
