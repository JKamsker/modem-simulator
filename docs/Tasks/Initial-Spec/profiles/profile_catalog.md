# Profile Catalog

## Basisprofile

| Profil | Status | Zweck |
|---|---|---|
| `generic-hayes-v250` | normative-base | Klassische AT-Syntax, Result Codes, S-Register-Grundlage. |
| `3gpp-27007-r18` | normative-base | Mobilfunk-AT-Befehle: Netz, SIM, Geräteidentität, Fehler. |
| `3gpp-27005-r16` | normative-base | SMS/CBS-AT-Verhalten. |

## Sierra Wireless / Semtech

| Profil | Status | Geplante Abdeckung |
|---|---|---|
| `sierra-common` | manufacturer-base | Identität, Fehlerpolicy, Sierra-Grundverhalten. |
| `sierra-umts-airprime-standard-2130617-r7` | candidate | Ältere/klassische UMTS AirCard/AirPrime-Basis, post-v1. |
| `sierra-mc-sl-umts-lte-v8` | out-of-scope | Sierra-LTE-Anteile sind nicht Bestandteil der aktuellen Implementierung. |
| `sierra-hl6-hl8-v20` | device-family | HL6528x / HL85xxx Embedded 2G/3G. |
| `sierra-hl78xx-v29` | out-of-scope | HL78xx LPWA/LTE-M/NB-IoT ist out-of-scope. |
| `sierra-em74xx-mc74xx-r3` | out-of-scope | EM/MC74xx LTE Embedded ist out-of-scope. |
| `sierra-em75xx-emmc74x1-v8` | out-of-scope | EM75xx und EM/MC74x1 LTE/9x50 sind out-of-scope. |
| `sierra-em9-v14` | out-of-scope | EM919X/EM7690/EM929X LTE/5G-orientiert ist out-of-scope. |

## Westermo

| Profil | Status | Geplante Abdeckung |
|---|---|---|
| `westermo-common` | manufacturer-base | Gemeinsame Westermo-Konventionen. |
| `westermo-td20-candidate` | candidate | Gewünschtes TD20-Profil, noch ohne belastbare Quelle. |
| `westermo-td22-6177-2203` | device-target | TD-22 AT/S-Register. |
| `westermo-td32-6178-2203` | candidate | TD-32 AT/S-Register, post-v1. |
| `westermo-td33-6179-2203` | candidate | TD-33 AT/S-Register, post-v1. |
| `westermo-td36-6618-2202` | device-target | TD-36 Industrie-PSTN/Leased-Line. |
| `westermo-gd01-6196-2220` | device-target | GD-01 GSM/SMS. |
| `westermo-gdw11-6615-2220` | device-target | GDW-11 GSM/GPRS. |
| `westermo-idw90-6620-3200` | candidate | IDW-90 ISDN/Analog, AT!/AT**, post-v1. |

## Profil-Abnahme

Ein Profil ist abnahmefähig, wenn:

```text
unknown == 0
mandatory_smoke_tests == passed
identity_commands == deterministic
error_policy == documented
line_endings == documented
effective_register_catalog == valid
coverage_schema == passed
```
