# PowerShell script to run IntegrationAPITool Excel Reader
# Usage: .\run-excel-reader.ps1 <excel-file-path>

param(
    [Parameter(Mandatory=$true, Position=0)]
    [string]$ExcelFilePath
)

$ErrorActionPreference = "Stop"

# Navigate to the repository root
# Script is at: .github/skills/lending-rest-excel-reader/scripts/run-excel-reader.ps1
# Need to go up 4 levels: scripts -> lending-rest-excel-reader -> skills -> .github -> FLIQ-liqjava
$repoRoot = Split-Path (Split-Path (Split-Path (Split-Path $PSScriptRoot -Parent) -Parent) -Parent) -Parent
$integrationApiToolDir = Join-Path $repoRoot "IntegrationAPITool"
$jarFile = Join-Path $integrationApiToolDir "artifacts\executable\IntegrationAPITool-1.0.jar"

if (-not (Test-Path $ExcelFilePath)) {
    Write-Error "Error: Excel file not found at '$ExcelFilePath'"
    exit 1
}

if (-not (Test-Path $jarFile)) {
    Write-Error "Error: JAR file not found at '$jarFile'"
    Write-Host "Please build the project first using: .\mvnw.cmd clean package -DskipTests" -ForegroundColor Yellow
    exit 1
}

# Change to IntegrationAPITool directory so generated files go to correct location
$currentDir = Get-Location

try {
    Set-Location $integrationApiToolDir

    Write-Host "Running IntegrationAPITool with file: $ExcelFilePath" -ForegroundColor Green
    # Pipe the file path to ExcelReader's stdin (it prompts for the path)
    $ExcelFilePath | java -jar "$jarFile"

    if ($LASTEXITCODE -ne 0) {
        Write-Error "Error: Failed to execute the tool"
        exit $LASTEXITCODE
    }
}
finally {
    # Return to original directory
    Set-Location $currentDir
}

Write-Host ""
Write-Host "Execution completed successfully!" -ForegroundColor Green
Write-Host "Generated classes can be found in: IntegrationAPITool\artifacts\temp_generated_class\" -ForegroundColor Cyan
