# Generate Query API Skill - Unified Script
# ============================================
# This script validates and resolves source file paths for any supported
# Query API business object in the LoanIQ repository.
#
# Usage:
#   .\generate-query-api.ps1 -BusinessObject "Deal"
#   .\generate-query-api.ps1 -BusinessObject "LoanDrawdown"
#   .\generate-query-api.ps1 -ListAll
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
        # Try to find FLIQ-liqjava root by looking for LoanIQ folder
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
# Maps each business object to its domain package and source file path

$BusinessObjectRegistry = @{
    "AdditionalFields" = @{
        Domain = "additionalfields"
        SourceFile = "additionalfields\LiqAPIQueryAdditionalFieldsIntegration.java"
        Pattern = "Polymorphic Owner"
    }
    "OutgoingDDAMessage" = @{
        Domain = "cashflow"
        SourceFile = "cashflow\LiqAPIQueryOutgoingDDAMessageIntegration.java"
        Pattern = "Cashflow Message"
    }
    "Deal" = @{
        Domain = "deal"
        SourceFile = "deal\LiqAPIQueryDealIntegration.java"
        Pattern = "Standard Entity"
    }
    "Facility" = @{
        Domain = "facility"
        SourceFile = "facility\LiqAPIQueryFacilityIntegration.java"
        Pattern = "Lazy Loading"
    }
    "FacilityInterestPricing" = @{
        Domain = "facility"
        SourceFile = "facility\LiqAPIQueryFacilityInterestPricingIntegration.java"
        Pattern = "Lazy Loading"
    }
    "ProductGuarantee" = @{
        Domain = "guarantee"
        SourceFile = ""
        Pattern = "Fee/Payment"
    }
    "HolidayCalendarCode" = @{
        Domain = "holidaycalendar"
        SourceFile = "holidaycalendar\LiqAPIQueryHolidayCalendarCodeIntegration.java"
        Pattern = "Standard Entity"
    }
    "HolidayCalendarDate" = @{
        Domain = "holidaycalendar"
        SourceFile = "holidaycalendar\LiqAPIQueryHolidayCalendarDateIntegration.java"
        Pattern = "Standard Entity"
    }
    "MISCode" = @{
        Domain = "miscode"
        SourceFile = "miscode\LiqAPIQueryMISCodeIntegration.java"
        Pattern = "Polymorphic Owner"
    }
    "OutgoingACHMessage" = @{
        Domain = "cashflow"
        SourceFile = "cashflow\LiqAPIQueryOutgoingACHMessageIntegration.java"
        Pattern = "Cashflow Message"
    }
    "OutgoingBOJMessage" = @{
        Domain = "cashflow"
        SourceFile = "cashflow\LiqAPIQueryOutgoingBOJMessageIntegration.java"
        Pattern = "Cashflow Message"
    }
    "OutgoingIMTMessage" = @{
        Domain = "cashflow"
        SourceFile = "cashflow\LiqAPIQueryOutgoingIMTMessageIntegration.java"
        Pattern = "Cashflow Message"
    }
    "OutgoingISOMessage" = @{
        Domain = "cashflow"
        SourceFile = "cashflow\LiqAPIQueryOutgoingISOMessageIntegration.java"
        Pattern = "Cashflow Message"
    }
    "OutgoingMTMessage" = @{
        Domain = "cashflow"
        SourceFile = "cashflow\LiqAPIQueryOutgoingMTMessageIntegration.java"
        Pattern = "Cashflow Message"
    }
    "OutgoingZenginMessage" = @{
        Domain = "cashflow"
        SourceFile = "cashflow\LiqAPIQueryOutgoingZenginMessageIntegration.java"
        Pattern = "Cashflow Message"
    }
    "Circle" = @{
        Domain = "circle"
        SourceFile = "circle\LiqAPIQueryCircleIntegration.java"
        Pattern = "Standard Entity"
    }
    "FXRate" = @{
        Domain = "fxrate"
        SourceFile = ""
        Pattern = "Standard Entity"
    }
    "FlexUnscheduledTransaction" = @{
        Domain = "outstanding"
        SourceFile = ""
        Pattern = "Transaction"
    }
    "LoanDrawdown" = @{
        Domain = "outstanding/drawdown"
        SourceFile = "outstanding\drawdown\LiqAPIQueryLoanDrawdownIntegration.java"
        Pattern = "Transaction"
    }
    "LoanIncrease" = @{
        Domain = "outstanding/increase"
        SourceFile = "outstanding\increase\LiqAPIQueryLoanIncreaseIntegration.java"
        Pattern = "Transaction"
    }
    "LoanInterestPayment" = @{
        Domain = "outstanding/interest"
        SourceFile = "outstanding\interest\LiqAPIQueryLoanInterestPaymentIntegration.java"
        Pattern = "Transaction"
    }
    "DiscountLoanDrawdown" = @{
        Domain = "outstanding/discountloandrawdown"
        SourceFile = "outstanding\discountloandrawdown\LiqAPIQueryDiscountLoanDrawdownIntegration.java"
        Pattern = "Transaction"
    }
    "LoanPrincipalPayment" = @{
        Domain = "outstanding/principal"
        SourceFile = "outstanding\principal\LiqAPIQueryLoanPrincipalPaymentIntegration.java"
        Pattern = "Transaction"
    }
    "QuickLoanRepricing" = @{
        Domain = "outstanding/qlr"
        SourceFile = "outstanding\qlr\LiqAPIQueryQuickLoanRepricingIntegration.java"
        Pattern = "Transaction"
    }
    "SBLCDecrease" = @{
        Domain = "outstanding/sblc"
        SourceFile = "outstanding\sblc\LiqAPIQuerySBLCDecreaseIntegration.java"
        Pattern = "Transaction"
    }
    "SBLCIncrease" = @{
        Domain = "outstanding/sblc"
        SourceFile = "outstanding\sblc\LiqAPIQuerySBLCIncreaseIntegration.java"
        Pattern = "Transaction"
    }
    "UnscheduledLoanPrincipalPayment" = @{
        Domain = "outstanding"
        SourceFile = ""
        Pattern = "Transaction"
    }
    "SBLCIssuance" = @{
        Domain = "sblc"
        SourceFile = "sblc\LiqAPIQuerySBLCIssuanceIntegration.java"
        Pattern = "Fee/Payment"
    }
    "SBLCFacingFeePayment" = @{
        Domain = "sblcfeepayment"
        SourceFile = "sblcfeepayment\LiqAPIQuerySBLCFacingFeePaymentIntegration.java"
        Pattern = "Fee/Payment"
    }
    "SBLCIssuanceFeePayment" = @{
        Domain = "sblcfeepayment"
        SourceFile = "sblcfeepayment\LiqAPIQuerySBLCIssuanceFeePaymentIntegration.java"
        Pattern = "Fee/Payment"
    }
    "FacilityOngoingFee" = @{
        Domain = "slmb"
        SourceFile = "slmb\LiqAPIQueryFacilityOngoingFeeIntegration.java"
        Pattern = "Fee/Payment"
    }
    "FacilityOngoingFeePayment" = @{
        Domain = "slmb"
        SourceFile = "slmb\LiqAPIQueryFacilityOngoingFeePaymentIntegration.java"
        Pattern = "Fee/Payment"
    }
    "UpfrontFee" = @{
        Domain = "upfrontfee"
        SourceFile = "upfrontfee\LiqAPIQueryUpfrontFeeIntegration.java"
        Pattern = "Fee/Payment"
    }
    "UserProfile" = @{
        Domain = "user"
        SourceFile = "user\LiqAPIQueryUserProfileIntegration.java"
        Pattern = "Standard Entity"
    }
    "UserSecurityProfile" = @{
        Domain = "user"
        SourceFile = "user\LiqAPIQueryUserSecurityProfileIntegration.java"
        Pattern = "Standard Entity"
    }
}

# ============================================
# Functions
# ============================================

function Get-SourceFilePath {
    param([string]$BOName)

    $entry = $BusinessObjectRegistry[$BOName]
    if ($null -eq $entry) {
        Write-Error "Business Object '$BOName' is not registered. Use -ListAll to see available objects."
        return $null
    }

    $srcBase = Join-Path $RepoRoot "LoanIQ\srcgen\com\misys\liq\api\rest\executable"

    if ([string]::IsNullOrEmpty($entry.SourceFile)) {
        Write-Warning "No source file available yet for '$BOName'. It needs to be generated."
        return @{
            BusinessObject = $BOName
            Domain = $entry.Domain
            Pattern = $entry.Pattern
            SourceFile = "(not yet available)"
            FullPath = $null
            Exists = $false
        }
    }

    $fullPath = Join-Path $srcBase $entry.SourceFile

    return @{
        BusinessObject = $BOName
        Domain = $entry.Domain
        Pattern = $entry.Pattern
        SourceFile = $entry.SourceFile
        FullPath = $fullPath
        Exists = (Test-Path $fullPath)
    }
}

function Show-AllBusinessObjects {
    Write-Host "`n=== Supported Query API Business Objects ===" -ForegroundColor Cyan
    Write-Host ("-" * 80)
    Write-Host ("{0,-35} {1,-25} {2,-20}" -f "Business Object", "Domain", "Pattern") -ForegroundColor Yellow
    Write-Host ("-" * 80)

    $BusinessObjectRegistry.GetEnumerator() | Sort-Object Name | ForEach-Object {
        $status = if ([string]::IsNullOrEmpty($_.Value.SourceFile)) { "[NO SOURCE]" } else { "" }
        Write-Host ("{0,-35} {1,-25} {2,-20} {3}" -f $_.Key, $_.Value.Domain, $_.Value.Pattern, $status)
    }

    Write-Host ("-" * 80)
    Write-Host "Total: $($BusinessObjectRegistry.Count) business objects`n"
}

function Validate-BusinessObject {
    param([string]$BOName)

    $result = Get-SourceFilePath -BOName $BOName

    if ($null -eq $result) { return }

    Write-Host "`n=== Query API Validation: $BOName ===" -ForegroundColor Cyan
    Write-Host "  Business Object : $($result.BusinessObject)"
    Write-Host "  Domain Package  : $($result.Domain)"
    Write-Host "  Query Pattern   : $($result.Pattern)"
    Write-Host "  Source File     : $($result.SourceFile)"

    if ($result.Exists) {
        Write-Host "  Status          : FOUND" -ForegroundColor Green
        Write-Host "  Full Path       : $($result.FullPath)"
    } elseif ($null -eq $result.FullPath) {
        Write-Host "  Status          : NOT YET AVAILABLE (needs generation)" -ForegroundColor Yellow
    } else {
        Write-Host "  Status          : NOT FOUND at expected path" -ForegroundColor Red
        Write-Host "  Expected Path   : $($result.FullPath)"
    }

    Write-Host ""
    Write-Host "  Generated Class : LiqAPIQuery${BOName}Integration.java"
    Write-Host "  ReturnValue     : LiqAPI${BOName}IntegrationAsReturnValue.java"
    Write-Host "  Test Class      : LiqAPIQuery${BOName}IntegrationTest.java"
    Write-Host ""
    Write-Host "  Target Paths:"
    Write-Host "    Integration   : LoanIQ/srcgen/main/java/com/misys/liq/api/rest/executable/$($result.Domain.Replace('\','/'))/"
    Write-Host "    ReturnValue   : LoanIQ/srcgen/main/java/com/misys/liq/api/rest/data/$($result.Domain.Replace('\','/'))/"
    Write-Host "    Test          : LoanIQ/test/com/misys/liq/api/rest/executable/$($result.Domain.Replace('\','/'))/"
    Write-Host ""
}

# ============================================
# Main Execution
# ============================================

Write-Host "LoanIQ Query API - Unified Skill Script" -ForegroundColor Green
Write-Host "Repository Root: $RepoRoot`n"

if ($ListAll) {
    Show-AllBusinessObjects
} elseif (-not [string]::IsNullOrEmpty($BusinessObject)) {
    Validate-BusinessObject -BOName $BusinessObject
} else {
    Write-Host "Usage:" -ForegroundColor Yellow
    Write-Host "  .\generate-query-api.ps1 -BusinessObject 'Deal'     # Validate a specific BO"
    Write-Host "  .\generate-query-api.ps1 -ListAll                    # List all supported BOs"
    Write-Host ""
    Show-AllBusinessObjects
}
