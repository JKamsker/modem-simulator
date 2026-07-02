# Downloaded Reference Artifacts

Stand: 2026-07-02

Dieser Ordner enthaelt lokale Arbeitskopien der in `../reference_index.md` gelisteten Quellen, soweit sie anonym und ueber offizielle Anbieter-URLs abrufbar waren. Zusaetzlich sind einige klar markierte Drittanbieter-Kopien enthalten, wenn die offizielle Sierra-Quelle anonym nicht herunterladbar war.

## Ergebnis

| Datei | Typ | Bytes | SHA-256 |
|---|---:|---:|---|
| 3gpp-27005.pdf | pdf | 407787 | `9855f614a54ca5603b28977624150cac597c9bed8bf03a33d33d35b4bd22722b` |
| 3gpp-27007.pdf | pdf | 2366371 | `5b0d6d84f5e3d821f593ec0a7a1e47b13c03e3a9588991c2793080057717aaba` |
| itu-v250.pdf | pdf | 660553 | `1e29ec28a967cfc5e9ce6effa1bcf9f98242ae524fe6e55ae369776c068edf39` |
| sierra-em75xx-v8.html | html | 84160 | `9dd5dd74375c33e647f9e640d3238d745a228981da5e0199c4b7549e0949524f` |
| sierra-em9-v14.html | html | 86252 | `c1f1fa7fc8d010b6ada0edf749faa8f4e2b82aa78bcf4ea3ed8b0c3d86331f24` |
| sierra-hl6-hl8-v20.html | html | 89019 | `be53db5523b53f022d617306d1eb90ebce03a14497bb99d00051a51e08670f13` |
| sierra-hl78xx-v29.html | html | 85949 | `6de7e8f5383cc2e2fece76e77b5540a59418ac60beedc3fde9ee120eb32b9aaf` |
| sierra-mc-sl-umts-lte-v8.html | html | 83128 | `000bf5ccb1a390d3b748fcb61a90430fa7dd0cfdfd511f333694e3574bbd79a2` |
| sierra-mc-sl-umts-lte-v8.pdf | pdf | 1476900 | `49dccf935568acf174611319d01f240a624fc49035bcf929340be23d72e4d384` |
| silabs-usb-cdc.pdf | pdf | 1910130 | `b7882a58e34fa4ddd31f93e8673ceb296e55d33e29c082f5e330aaf8250d2e03` |
| thirdparty-bipom-sierra-em75xx-at-command-reference-rev2.pdf | pdf | 760660 | `46090bf6ce8039be3bcf5376af1ff9de9dd0c09cbf4adaff48f0161f017432aa` |
| thirdparty-bipom-sierra-hl78xx-at-commands-rev11.pdf | pdf | 1487341 | `e85740b2d11bcff50ef9bb9265c336d1343e1c36dbbc51a17a275376fb23b04a` |
| thirdparty-digikey-airprime-hl78xx-devkit.pdf | pdf | 2368719 | `901902316fed5a228dd13707208b3ba39a200516d7580a68a28997d2af5d383c` |
| thirdparty-infopulsas-sierra-hl6-hl8-at-commands-rev16.pdf | pdf | 6266220 | `f953b9d2d3ff7af462cbbf630564def07f996ff6f1e7ffa717f489efcbe36067` |
| thirdparty-mikroe-hl781x-datasheet.pdf | pdf | 3024522 | `6926875bbd9f0e05ca701656f749cc293f47fed1a881892b967e01047f4835d2` |
| thirdparty-techship-sierra-em9-r14-download-page.html | html | 88363 | `e788394a336bc61abfc743384845efe58fbf690d8d26b84018c1b3c848348156` |
| westermo-gd01.pdf | pdf | 1009187 | `3b43b9d55adf5fea7f0e38558ed1f3696cae02e60ac654ef0a22c3ff6df8d99c` |
| westermo-gdw11.pdf | pdf | 1458399 | `67192933dddaec2cf6a52ec5b13cd9affc5d67609010bd7b4370c2bd74fbce61` |
| westermo-idw90.pdf | pdf | 523891 | `5b802904cd454cb4d3a5f90a6978f7ebc2a5c3bbef790dfce4153565bf883a61` |
| westermo-td22.pdf | pdf | 734671 | `d85d3db6333d2d79e887f0823c0c1cf0405df081bf723c84419e2f4dbe45ef43` |
| westermo-td32.pdf | pdf | 1069335 | `f2445bf6e2b17aa11df22051e5750e1f1dd8997d56d0cdc3c675bae7526d62a2` |
| westermo-td33.pdf | pdf | 431355 | `4520764f02297511fa6d1f6a709622daba9eb915afb27dc5f978973652cd7b4a` |
| westermo-td36.pdf | pdf | 2042436 | `27a605e162cd7fbb0aa184ad14b7a22e92e9dbf68ef05d9494eb4055afd64dee` |

## Sierra-Hinweise

Die Sierra-Landing-Pages wurden als HTML gespeichert. Fuer die PDF-Dateien wurde zusaetzlich ein Playwright-Browserflow versucht.

| ID | Ergebnis |
|---|---|
| `sierra-mc-sl-umts-lte-v8` | PDF anonym ueber den offiziellen Sierra-Media-Endpunkt abrufbar. |
| `sierra-hl78xx-v29` | Playwright bestaetigt Redirect zu Sierra SSO. |
| `sierra-hl6-hl8-v20` | Playwright bestaetigt Redirect zu Sierra SSO. |
| `sierra-em9-v14` | Playwright bestaetigt Redirect zu Sierra SSO. |
| `sierra-em75xx-v8` | Offizieller Media-Endpunkt liefert anonym keine PDF-Datei; Browserflow landet auf Sierra-Fehlerseite. |

## Drittanbieter-Funde

Diese Dateien stammen nicht von der offiziellen Sierra-Quelle und muessen entsprechend vorsichtig verwendet werden.

| Datei | Quelle | Einordnung |
|---|---|---|
| `thirdparty-bipom-sierra-hl78xx-at-commands-rev11.pdf` | <https://www.bipom.com/documents/sierra/AirPrime%20-%20HL78xx%20-%20AT%20Commands%20Interface%20Guide%20-%20Rev11.pdf> | Aeltere HL78xx AT-Commands-Referenz, nicht Version 29. |
| `thirdparty-infopulsas-sierra-hl6-hl8-at-commands-rev16.pdf` | <https://www.infopulsas.lt/image/catalog/files/eshop/387/1-1-AirPrime_HL6_and_HL8_Series_AT_Commands_Interface_Guide_Rev16_0.pdf> | Aeltere HL6/HL8 AT-Commands-Referenz, nicht Version 20. |
| `thirdparty-bipom-sierra-em75xx-at-command-reference-rev2.pdf` | <https://www.bipom.com/documents/sierra/41111748%20AirPrime%20EM75XX%20AT%20Command%20Reference%20r2.pdf> | Aeltere EM75xx AT-Command-Referenz, nicht Version 8. |
| `thirdparty-techship-sierra-em9-r14-download-page.html` | <https://techship.com/downloads/sierra-wireless-airprime-em91-em92-series-and-em7690-module-at-command-reference-guide/> | Techship-Metadatenseite nennt `41113480_em9_at_command_reference_r14.pdf`, aber der Download lieferte anonym keine PDF-Datei. |
| `thirdparty-digikey-airprime-hl78xx-devkit.pdf` | <https://mm.digikey.com/Volume0/opasdata/d220001/medias/docus/4212/AirPrime_HL78xx.pdf> | Verwandte HL78xx-Development-Kit-Dokumentation, keine AT-Commands-Referenz. |
| `thirdparty-mikroe-hl781x-datasheet.pdf` | <https://download.mikroe.com/documents/datasheets/HL781x_datasheet.pdf> | Verwandtes HL781x-Datenblatt, keine AT-Commands-Referenz. |

Keine Zugangsdaten wurden gelesen oder verwendet.
