# Generate Delete API Skill - Unified Script
# =============================================
# This script validates and resolves source file paths for any supported
# Delete API business object in the LoanIQ repository.
#
# Usage:
#   .\generate-delete-api.ps1 -BusinessObject "DealAdministrator"
#   .\generate-delete-api.ps1 -BusinessObject "Deal"
#   .\generate-delete-api.ps1 -ListAll
#
# Prerequisites:
#   - PowerShell 5.1+
#   - Access to the FLIQ-liqjava repository

param(
    [Parameter(Mandatory=$false)]
    [string]$BusinessObject,

    [Parameter(Mandatory=$false)]
    [switch]$ListAll,

    [Parameter(Mandatory=$false)]
    [string]$RepoRoot
)

# Resolve RepoRoot if not provided
if ([string]::IsNullOrEmpty($RepoRoot)) {
    if (-not [string]::IsNullOrEmpty($PSScriptRoot)) {
        $RepoRoot = (Split-Path -Parent (Split-Path -Parent (Split-Path -Parent (Split-Path -Parent $PSScriptRoot))))
    } else {
        $RepoRoot = (Get-Location).Path
        while ($RepoRoot -ne "" -and -not (Test-Path (Join-Path $RepoRoot "LoanIQ"))) {
            $RepoRoot = Split-Path -Parent $RepoRoot
        }
        if ([string]::IsNullOrEmpty($RepoRoot)) {
            $RepoRoot = "c:\Users\asrivas3\git\7750_1\FLIQ-liqjava"
        }
    }
}

$ErrorActionPreference = "Stop"

# ============================================
# Business Object Registry
# ============================================
# Maps each business object to its domain package, source file path, and pattern

$BusinessObjectRegistry = @{
    "DealAdministrator" = @{
        Domain = "dealadministrator"
        SourceFile = "LiqAPIDeleteDealAdministratorIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\rest\executable\dealadministrator"
        Pattern = "Owner-Based Single Entity"
        OwnerTypes = @("DEA")
        IdentifierType = "OwnerIdentifier"
    }
    "Deal" = @{
        Domain = "deal"
        SourceFile = "LiqAPIDeleteDealIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\rest\executable\deal"
        Pattern = "Direct Entity"
        OwnerTypes = @()
        IdentifierType = "DealIdentifier"
    }
    "DemandBill" = @{
        Domain = "demandbill"
        SourceFile = "LiqAPIDeleteDemandBillIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\rest\executable\demandbill"
        Pattern = "Direct Entity"
        OwnerTypes = @()
        IdentifierType = "DemandBillIdentifier"
    }
    "FacilityInterestPricing" = @{
        Domain = "facility"
        SourceFile = "LiqAPIDeleteFacilityInterestPricingIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\rest\executable\facility"
        Pattern = "Owner-Based Polymorphic"
        OwnerTypes = @("FAC")
        IdentifierType = "OwnerIdentifier"
    }
    "RepaymentSchedule" = @{
        Domain = "flex"
        SourceFile = "LiqAPIDeleteRepaymentScheduleIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\rest\executable\flex"
        Pattern = "Owner-Based Polymorphic"
        OwnerTypes = @("OST", "QLR")
        IdentifierType = "OwnerIdentifier"
    }
    "ProductGuarantee" = @{
        Domain = "guarantor"
        SourceFile = "LiqAPIDeleteProductGuaranteeIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\rest\executable\guarantor"
        Pattern = "List-Based"
        OwnerTypes = @("FAC", "DEA")
        IdentifierType = "OwnerIdentifier + ProductGuaranteeIdentifier"
    }
    "DealInterestPricingOption" = @{
        Domain = "interestpricingoption"
        SourceFile = "LiqAPIDeleteDealInterestPricingOptionIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\rest\executable\interestpricingoption"
        Pattern = "Owner-Based Single Entity"
        OwnerTypes = @("DEA")
        IdentifierType = "OwnerIdentifier"
    }
    "MISCode" = @{
        Domain = "miscode"
        SourceFile = "LiqAPIDeleteMISCodeIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\rest\executable\miscode"
        Pattern = "List-Based"
        OwnerTypes = @("DEA", "FAC", "LNID")
        IdentifierType = "OwnerIdentifier + MISCodeIntegration"
    }
    "FlexUnscheduledTransaction" = @{
        Domain = "outstanding"
        SourceFile = "LiqAPIDeleteFlexUnscheduledTransactionIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\executable\outstanding"
        Pattern = "Inherited Delegation"
        OwnerTypes = @()
        IdentifierType = "(inherited from parent)"
    }
    "LoanPrincipalPayment" = @{
        Domain = "outstanding\principal"
        SourceFile = "LiqAPIDeleteLoanPrincipalPaymentIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\rest\executable\outstanding\principal"
        Pattern = "Direct Entity"
        OwnerTypes = @()
        IdentifierType = "OutstandingTransactionIdentifier"
    }
    "PayoffStatement" = @{
        Domain = "outstanding"
        SourceFile = "LiqAPIDeletePayoffStatementIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\rest\data\outstanding"
        Pattern = "Direct Entity"
        OwnerTypes = @()
        IdentifierType = "PayoffIdentifier"
    }
    "UpfrontFee" = @{
        Domain = "upfrontfee"
        SourceFile = "LiqAPIDeleteUpfrontFeeIntegration.java"
        SourcePath = "LoanIQ\srcgen\com\misys\liq\api\rest\executable\upfrontfee"
        Pattern = "Direct Entity"
        OwnerTypes = @()
        IdentifierType = "UpfrontFeeIdentifier"
    }
}

$OutputPath = Join-Path $RepoRoot "IntegrationAPITool\artifacts\temp-generated_class"

# ============================================
# Functions
# ============================================

function Show-AllBusinessObjects {
    Write-Host ""
    Write-Host "=== Supported Delete API Business Objects ===" -ForegroundColor Cyan
    Write-Host ""
    Write-Host ("{0,-30} {1,-15} {2,-50} {3,-25} {4}" -f "Business Object", "Domain", "Source File", "Pattern", "Owner Types")
    Write-Host ("{0,-30} {1,-15} {2,-50} {3,-25} {4}" -f ("-" * 30), ("-" * 15), ("-" * 50), ("-" * 25), ("-" * 15))

    foreach ($bo in ($BusinessObjectRegistry.Keys | Sort-Object)) {
        $entry = $BusinessObjectRegistry[$bo]
        $owners = if ($entry.OwnerTypes.Count -gt 0) { $entry.OwnerTypes -join ", " } else { "N/A" }
        Write-Host ("{0,-30} {1,-15} {2,-50} {3,-25} {4}" -f $bo, $entry.Domain, $entry.SourceFile, $entry.Pattern, $owners)
    }
    Write-Host ""
    Write-Host "Total: $($BusinessObjectRegistry.Count) business objects" -ForegroundColor Green
    Write-Host ""
}

function Process-BusinessObject {
    param([string]$BOName)

    if (-not $BusinessObjectRegistry.ContainsKey($BOName)) {
        Write-Host "[ERROR] Unknown business object: '$BOName'" -ForegroundColor Red
        Write-Host "Use -ListAll to see supported business objects." -ForegroundColor Yellow
        exit 1
    }

    $entry = $BusinessObjectRegistry[$BOName]
    $sourcePath = Join-Path $RepoRoot $entry.SourcePath
    $sourceFile = Join-Path $sourcePath $entry.SourceFile

    Write-Host ""
    Write-Host "=== Delete $BOName API Generation ===" -ForegroundColor Cyan
    Write-Host "Business Object  : $BOName"
    Write-Host "Domain           : $($entry.Domain)"
    Write-Host "Source File      : $($entry.SourceFile)"
    Write-Host "Pattern          : $($entry.Pattern)"
    Write-Host "Identifier Type  : $($entry.IdentifierType)"
    if ($entry.OwnerTypes.Count -gt 0) {
        Write-Host "Owner Types      : $($entry.OwnerTypes -join ', ')"
    }
    Write-Host "Source Path      : $sourceFile"
    Write-Host "Output Path      : $OutputPath"
    Write-Host ""

    # Check if source file exists
    if (Test-Path $sourceFile) {
        Write-Host "[OK] Source file exists: $sourceFile" -ForegroundColor Green
    } else {
        Write-Host "[MISSING] Source file NOT found: $sourceFile" -ForegroundColor Red
        exit 1
    }

    # Create output directory if needed
    if (-not (Test-Path $OutputPath)) {
        New-Item -ItemType Directory -Path $OutputPath -Force | Out-Null
        Write-Host "[CREATED] Output directory: $OutputPath" -ForegroundColor Yellow
    }

    # Check if already generated at output path
    $generatedFile = Join-Path $OutputPath $entry.SourceFile
    if (Test-Path $generatedFile) {
        Write-Host "[EXISTS] Generated file already present: $generatedFile" -ForegroundColor Yellow
        Write-Host "         Use existing file as base context for skill augmentation."
    } else {
        Write-Host "[INFO] No pre-generated file found. Copying source as reference." -ForegroundColor Cyan
        Copy-Item -Path $sourceFile -Destination $generatedFile -Force
        Write-Host "[COPIED] Source copied to output path for processing." -ForegroundColor Green
    }

    Write-Host ""
    Write-Host "=== Generation Complete ===" -ForegroundColor Cyan
    Write-Host ""
}

# ============================================
# Main Execution
# ============================================

if ($ListAll) {
    Show-AllBusinessObjects
} elseif (-not [string]::IsNullOrEmpty($BusinessObject)) {
    Process-BusinessObject -BOName $BusinessObject
} else {
    Write-Host "[ERROR] Please provide -BusinessObject <name> or -ListAll" -ForegroundColor Red
    Write-Host ""
    Write-Host "Usage:" -ForegroundColor Yellow
    Write-Host "  .\generate-delete-api.ps1 -BusinessObject 'DealAdministrator'"
    Write-Host "  .\generate-delete-api.ps1 -BusinessObject 'Deal'"
    Write-Host "  .\generate-delete-api.ps1 -ListAll"
    Write-Host ""
    exit 1
}
