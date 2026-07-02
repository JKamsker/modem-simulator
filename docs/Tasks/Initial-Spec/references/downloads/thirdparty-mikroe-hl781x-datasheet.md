# thirdparty-mikroe-hl781x-datasheet

> Generated from the sibling PDF for local reference search and review.
> Source PDF: `thirdparty-mikroe-hl781x-datasheet.pdf`
> Source SHA-256: `6926875bbd9f0e05ca701656f749cc293f47fed1a881892b967e01047f4835d2`
> Generated: 2026-07-02
> Extractor: pypdf 6.14.2
> Note: This is text extraction only; tables, columns, images, and scanned pages may need manual verification against the PDF.

## Extraction Summary

- Pages: 85
- Pages without extractable text: 0

## PDF Metadata

| Key | Value |
|---|---|
| Author | Sierra Wireless |
| CreationDate | D:20230814114454Z |
| Creator | FrameMaker 2019.0.8 |
| Keywords | <41114133>; Wireless CPU; wireless module; wireless modules; wireless modem; wireless modems; modem wireless; GSM module; GSM modules; GPRS module; GSM modem; GSM modems; GSM GPRS modem; CDMA modem; modem CDMA; CDMA wireless modem; EDGE modem; cellular modem; wireless modem; modem GSM; wireless GPS module; HSxPA modem; HSDPA modem; HSUPA modem; WISMO; cellular software; software cellular; wireless software; GSM software; Lua; GSM GPS; GPS GSM; CDMA GSM; EDGE; HSDPA; HSUPA; HSxPA; GSM satellite; satellite GPS; cellular satellite; GPS satellite; M2M; machine-to-machine; embedded wireless; gsm tracking; fleet management; GSM POS; GSM automotive; telematics; GPS wireless; satellite location; tracking system; satellite tracking; wireless alarm system; smart metering; wireless automotive; tracking systems; eCall |
| ModDate | D:20230814120755+08'00' |
| Producer | Adobe PDF Library 15.0 |
| Title | HL781x Product Technical Specification |

## Page 1

HL781x
    Product Technical Specification


                                          41114133
                                            Rev. 9

## Page 2

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Important                         Due to the nature of wireles s communications, transmission and reception of data can
Notice                            never be guaranteed. Data may be delayed, corrupted (i.e., have errors) or be totally lost.
                                  Although significant delays or losses of data are rare when wireless devices such as the
                                  Sierra Wireless product are used in a normal manner with a well-constructed network, the
                                  Sierra Wireless product should not be used in situations where failure to transmit or
                                  receive data could result in damage of any kind to the user or any other party, including
                                  but not limited to personal injury, death, or loss of property. Sierra Wireless accepts no
                                  responsibility for damages of any kind resulting from delays or errors in data transmitted
                                  or received using the Sierra Wireless product, or for failure of the Sierra Wireless product
                                  to transmit or receive such data.

Safety and                        Do not operate the Sierra Wireless product in areas where blasting is in progress, where
Hazards                           explosive atmospheres  may be present, near medical equipment, near life support
                                  equipment, or any equipment which may be s usceptible to any form of radio interference.
                                  In such areas, the Sierra Wireless product MUST BE POWERED OFF. The Sierra
                                  Wireless product can transmit signals that could interfere with this equipment.
                                  Do not operate the Sierra Wireless product in any aircraft, whether the aircraft is on the
                                  ground or in flight. In aircraft, the Sierra Wireless product MUST BE POWERED OFF         .
                                  When operating, the Sierra Wireless product can transmit signals that could interfere with
                                  various onboard systems.


                                  Note:                Some airlines may permit the use of cellular phones while the aircraft is on the ground and
                                  the door is open. Sierra Wireless products may be used at this time.

                                  The driver or operator of any vehicle should not operate the Sierra Wireless product while
                                  in control of a vehicle. Doing so will detract from the driver or operator’s control and
                                  operation of that vehicle. In some states and provinces, operating such communications
                                  devices while in control of a vehicle is an offence.

Limitation of                     The information in this manual is subject to change without notice and does not represent
Liability                         a commitment on the part of Sierra Wireless. SIERRA WIRELESS AND ITS AFFILIATES
                                  SPECIFICALLY DISCLAIM LIABILITY FOR ANY AND ALL DIRECT, INDIRECT,
                                  SPECIAL, GENERAL, INCIDENTAL, CONSEQUENTIAL, PUNITIVE OR EXEMPLARY
                                  DAMAGES INCLUDING, BUT NOT LIMITED TO, LOSS OF PROFITS OR REVENUE OR
                                  ANTICIPATED PROFITS OR REVENUE ARISING OUT OF THE USE OR INABILITY TO
                                  USE ANY SIERRA WIRELESS PRODUCT, EVEN IF SIERRA WIRELESS AND/OR ITS
                                  AFFILIATES HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES OR
                                  THEY ARE FORESEEABLE OR FOR CLAIMS BY ANY THIRD PARTY.
                                  Notwithstanding the foregoing, in no event shall Sierra Wireless and/or its affiliates
                                  aggregate liability arising under or in connection with the Sierra Wireless product,
                                  regardless of the number of events, occurrences, or claims giving rise to liability, be in
                                  exces s of the price paid by the purchaser for the Sierra Wireless product.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        2                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 41114133

## Page 3

P r e f a c e


Copyright                                                                                     © 2023                          Sierra Wireless. All rights reserved.

Trademarks                                         Sierra Wireless®, AirLink®, AirVantage® and the Sierra Wireless  logo are registered
                                                          trademarks of Sierra Wireless.
                                                          Windows® and Windows Vista® are registered trademarks of Microsoft Corporation.
                                                          Macintos h® and Mac OS X® are registered trademarks  of Apple Inc., registered in the
                                                          U.S. and other countries.
                                                          QUALCOMM® is a registered trademark of QUALCOMM Incorporated. Used under
                                                          license.
                                                          Other trademarks are the property of their respective owners.

Contact
Information
                                                            Sales information and technical                                                                             Web: sierrawireless.com/company/contact-us/
                                                            support, including warranty and returns                                                                     Global toll-free number: 1-877-687-7795
                                                                                                                                                                        6:00 am to 5:00 pm PST

                                                            Corporate and product information                                                                           Web: sierrawireless.com

Revision
History


  Re v i s i o n                    R e l e as e   d at  e                                 C h an g e s
  nu m b e r

  1                                 June 2021                                             Creation

  2                                 May 2022                                              Updated:
                                                                                          •                                                    Current consumption values

  3                                 May 2022                                              Added:
                                                                                          •                                                    Taiwan NCC Statement


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          3                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                41114133

## Page 4

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


   4                                        September 2022                                                     Updated:
                                                                                                               •                                                    Table        3-7, Table         3-8, Table         3-9, Table        4-10 values
                                                                                                               •                                                    Table        3-9 changed to Cat-NB
                                                                                                               •                                                    For Table        3-14:
                                                                                                                             •                          NB1 UL peak throughput value changed from 62.5 to 45.7
                                                                                                                             •                          Subcarriers uplink changed from 3 to 12
                                                                                                               •                                                    For Table        3-15
                                                                                                                             •                          Changed HL7810 to HL7812
                                                                                                                             •                          Removed duplicate MCS.TBS:13
                                                                                                                             •                          Changed NB2 UL peak throughput from 109 kbps to 159 kbps
                                                                                                                             •                          Changed Subcarriers uplink from 3 to 12
                                                                                                               •                                                    Table        8-1 and Table        8-2 values
                                                                                                               Added:
                                                                                                               •                                                    Added Standby for Table         3-6, Table        3-7, Table        3-8, Table        3-9
                                                                                                               •                                                    Added note for flash wear out feature under Table         3-5

   5                                        October 2022                                                       Updated:
                                                                                                               •                                                    Table        8-1 Band 8
                                                                                                               Added:
                                                                                                               •                                                    Added RF Circuit

   6                                        December 2022                                                      Updated:
                                                                                                               •                                                    Current Consumption—Added TX power in note
                                                                                                               •                                                    Table        3-6, Table         3-7, Table         3-8, Table        3-9—Updated the PSM values, eDRX
                                                                                                                             values, DRX running current value
                                                                                                               •                                                    Table        4-19—Modified max wakeup timing for T1 and T2
                                                                                                               •                                                    Table        4-20—Modified typ wakeup timing
                                                                                                               •                                                    Table        4-21—Modified max wakeup timing for T2
                                                                                                               •                                                    Added information under Current Consumption

   7                                        April 2023                                                         Updated:
                                                                                                               •                                                    Updated pin definition for C21 on Table         2-2
                                                                                                               •                                                    Removed BAT_RTC from  Figure         1-2
                                                                                                               •                                                    Added Japan Radio and Telecom Approval

   8                                        May 2023                                                           Updated:
                                                                                                               •                                                    Updated Figure         2-1 for C21, changed VBAT_BB to NC

   9                                        August 2023                                                        Updated:
                                                                                                               •                                                    Removed Patents section
                                                                                                               •                                                    Updated Software Power Off in Unmanaged Mode steps


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        4                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 41114133

## Page 5

Contents


1: Introduction   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    8
        1.1 Supported RF Bands/Connectivity           . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    8
        1.2 Common Flexible Form Factor (CF3)     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    10
        1.3 Physical Dimensions and Connection Interface         . . . . . . . . . . . . . . . . . . . . . . . . . . .    10
        1.4 General Features . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    12
        1.5 Architecture          . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    14
        1.6 Interfaces   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    15
        1.7 Environmental Specifications           . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    16


2: Pad Definition  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    17
        2.1 Pin Types . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    17
        2.2 Pad Configuration    . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    23


3: Power Specifications  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .                24
        3.1 Power Supply            . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    24
        3.2 Electrical Specifications   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    25
                3.2.1 Digital I/O Characteristics  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    25
        3.3 3GPP Power Saving Features . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    26
                3.3.1 Power Saving Mode (PSM)    . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .     26
                3.3.2 Extended DRX (eDRX)          . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    28
        3.4 HL781x Low Power Modes  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .     31
        3.5 Current Consumption           . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    33


4: Detailed Interface Specifications            . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    42
        4.1 VGPIO        . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    42
                4.1.1 I/O Behavior in Hibernate Mode    . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    43
        4.2 USIM Interface . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .     43
                4.2.1 eSIM Interface   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    43
                4.2.2 External UIM1 Interface           . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    43
                4.2.3 UIM1_DET           . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    44
        4.3 USB Interface         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    44


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    5                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                41114133

## Page 6

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


        4.4 General Purpose Input/Output (GPIO)  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           45
        4.5 Main Serial Link (UART1)        . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           46
                4.5.1 Ring Indicator (UART1_RI or Alternative)            . . . . . . . . . . . . . . . . . . . . . . . . . . .           48
                4.5.2 UART1_RTS/UART1_CTS . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           48
                4.5.3 UART Application Examples            . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           48
        4.6 Power On Signal (POWER_ON_N)      . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           49
                4.6.1 Unmanaged POWER_ON_N (Default) . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           50
        4.7 Power Down, Off, and VBAT Removal        . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           51
                4.7.1 Software Power Off in Unmanaged Mode       . . . . . . . . . . . . . . . . . . . . . . . . . . .           51
                4.7.2 Emergency Power Removal      . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           52
        4.8 Reset Signal (RESET_IN_N)      . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           52
        4.9 Analog to Digital Converter (ADC)       . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           53
        4.10 Clock Interface       . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           54
        4.11 Debug Interfaces . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           54
                4.11.1 Diagnostic Interface     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           54
                4.11.2 Modem Logs Interface (MLI)           . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           56
        4.12 Wake Up Signal (WAKEUP)        . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           57
                4.12.1 Wakeup from Low Power Modes  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           58
                4.12.2 Wakeup from OFF Mode       . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           58
                4.12.3 Wakeup from Lite Hibernate Mode   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           59
                4.12.4 Wakeup from Hibernate Mode  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           60
        4.13 RF Interface         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           61
                4.13.1 RF Antenna Connection        . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           61
                4.13.2 LTE RF Interface         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           61
                4.13.3 2G RF Interface (HL7812 only)            . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           63
        4.14 TX Burst Indicator (TX_ON)    . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           64
        4.15 Tx/Rx Activity Indicator; External RF Voltage Control . . . . . . . . . . . . . . . . . . . . . .           65
        4.16 GNSS    . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           66
                4.16.1 GNSS Performance     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           66


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                41114133

## Page 7

Co n t  e nt  s


5: Mechanical Drawings . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           67


6: Design Guidelines    . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           69
        6.1 Power Supply Design  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .                       69
        6.2 UIM1          . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           69
        6.3 USB Interface  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           70
        6.4 ESD Protection for I/Os . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           71
        6.5 Hibernate—Isolation Requirements      . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           71
                6.5.1 VGPIO Monitoring and Buffer Control         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           72
        6.6 Radio Frequency Integration . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           73
                6.6.1 Antenna Matching Circuit           . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           73
                6.6.2 RF Circuit  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           73


7: Reliability Specification   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           75
        7.1 Preconditioning Test       . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           75
        7.2 Performance Test          . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           75
        7.3 Aging Tests     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           76
        7.4 Characterization Tests       . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           77


8: Legal Information      . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           78
        8.1 Disposing of the Product   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           78
        8.2 Compliance Acceptance and Certification         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           78
        8.3 Regulatory and Industry Approvals/ Certifications       . . . . . . . . . . . . . . . . . . . . . . . . .           78
        8.4 Japan Radio and Telecom Approval   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           79
        8.5 Important Compliance Information for North American Users          . . . . . . . . . . . . . . . .           79
        8.6 Legal Information – Taiwan NCC Statement           . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           81


9: Appendix   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           82
        9.1 Website Support          . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           82
        9.2 Reference Documents         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           82
        9.3 Terms and Abbreviations      . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           83
        9.4 Ordering Information      . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           85

Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     7                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                41114133

## Page 8

1: Introduction                                                                                                                                                                               1


                                             This document defines the high-level product features and illustrates the interfaces for
                                             Sierra Wireless HL781x Modules (HL7810, HL7812), designed for M2M and Internet
                                             of Things (IoT) markets. It covers the hardware aspects of the product series,
                                             including electrical and mechanical. For additional documentation (e.g. Firmware
                                             Customer Release Notes, AT Command Reference, etc.), refer to the module page at
                                             source.sierrawireless.com.
                                             HL781x collectively identifies HL7810 and HL7812. Variant-specific content is
                                             identified where applicable. The HL781x supports  a variety of interfaces such as USB
                                             FS, UART, ADC, GPIOs, and also supports the low power consumption hibernation
                                             modes to provide customers with flexibility in implementing high-end solutions.The
                                             key differentiators between HL781x variants are regulatory and industrial approvals/
                                             certifications, and supported radio access  technologies (RATs)-HL7810 supports Cat-
                                             M1/NB-IoT while HL7812 supports Cat-M1/NB-IoT/2G.


                                             Note:                Sierra Wireless modules are shipped factory-programmed with industry or mobile
                                             operator approved firmware, according to the specific SKU ordered. Periodically, newer
                                             firmware versions become available and can include new features, bug fixes, or critical security
                                             updates. Sierra Wireless strongly recommends that customers establish their own production
                                             capability for updating module firmware on their assembled end platform, in the event that a
                                             newer firmware must be installed before deployment. Sierra Wireless also recommends
                                             customers design their products to support post-deployment FOTA upgrades using the
                                             AirVantage cloud platform.

                                             1.1 Supported RF Bands/Connectivity

                                             The HL781x is a Sierra Wireless Ready-to-Connect (R2C) module that supports the
                                             use of its embedded SIM (eSIM) or an external SIM for global data connectivity on the
                                             RF bands detailed in the following module-specific tables.
                                             For details about using the HL781x's eSIM with Sierra Smart Connectivity, refer to [6]
                                             Sierra Wireless Ready-to-Connect Module Integration Guide (Doc#          41113385). For
                                             additional information on Sierra Smart Connectivity, explore www.sierrawireless.com
                                             or contact Sierra Wireless.


                                             Note:                The Sierra Wireless eSIM is SKU-dependent and not included in all modules. Contact
                                             Sierra Wireless for details.


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    8                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                41114133

## Page 9

I  n t r o d u c t  i o n


Ta b  l  e    1 - 1 :                     H L 7  8 1 x    S u p  p  o r t e  d    R F    B a n  d  s / C  o n  n e c  t i v i  t y

  M od u l e                           RF    Ba n d                         Tr a n sm  i t    ( T X )                                     R e c ei  ve   (  R x)                                        C at  - M 1                        C a t -  N B2                      2 G
                                                                            F r e q u en c y   ( M H z )                                  F r  eq u e nc y   ( M H z )

                                       LTE B1                              1920–1980                                                      2110–2170                                                     Y                                 Y

                                       LTE B2                              1850–1910                                                      1930–1990                                                     Y                                 Ya

                                       LTE B3                              1710–1785                                                      1805–1880                                                     Y                                 Y

                                       LTE B4                              1710–1755                                                      2110–2155                                                     Y                                 Ya

                                       LTE B5                              824–849                                                        869–894                                                       Y                                 Ya

                                       LTE B8                              880–915                                                        925–960                                                       Y                                 Y

                                       LTE B12                             699–716                                                        729–746                                                       Y                                 Ya

  HL7810                               LTE B13                             777–787                                                        746–756                                                       Y                                 Ya
  HL7812                               LTE B18                             815–830                                                        860–875                                                       Y                                 Y

                                       LTE B19                             830–845                                                        875–890                                                       Y                                 Y

                                       LTE B20                             832–862                                                        791–821                                                       Y                                 Y

                                       LTE B25                             1850–1915                                                      1930–1995                                                     Y                                 Ya

                                       LTE B26                             814–849                                                        859–894                                                       Y                                 Ya

                                       LTE B28                             703–748                                                        758–803                                                       Y                                 Y

                                       LTE B66                             1710–1780                                                      2110–2200                                                     Y                                 Ya

                                       LTE B85                             698-716                                                        728-746                                                       Y                                 Ya

                                       GSM 850                             824–849                                                        869–894                                                                                                                            Y

                                       E-GSM 900                           880–915                                                        925–960                                                                                                                            Y
  HL7812
                                       DCS 1800                            1710–1785                                                      1805–1880                                                                                                                          Y

                                       PCS 1900                            1850–1910                                                      1930–1990                                                                                                                          Y

        a.                           To ensure FCC compliance near NB band edges, Cat-NB2 supported TX channel ranges do not include outer channels. Sup-
                 ported channel ranges are:
                 • B2: 18602–19198           • B4: 19952–20398   • B5: 20402–20648   • B12: 23012–23178
                 • B13: 23182–23278         • B25: 26042–26688 • B26: 26692–27038 • B66: 131974 - 132670
                 • B85: 134004–134179


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     9                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                41114133

## Page 10

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n

                                                                         1.2 Common Flexible Form Factor (CF3)

                                                                         The HL781x belongs to Sierra Wireless’ Common Flexible Form Factor (CF3) family
                                                                         of WWAN modules. These modules share a compatible footprint. The CF3 form factor
                                                                         provides a unique solution to a series of problems faced commonly in the WWAN
                                                                         module space as it:
                                                                         •                                                    Accommodates multiple radio technologies (from GSM to LTE advanced) and
                                                                                   band groupings
                                                                         •                                                    Offers electrical and functional compatibility
                                                                         •                                                    Provides direct mount, as well as socket mount (depending on customer needs,
                                                                         •                                                    e.g. for use in development kits or for prototype development)
                                                                         1.3 Physical Dimensions and Connection
                                                                         Interface

                                                                         HL781x modules are compact, robust, fully shielded industrial-grade embedded
                                                                         modules with the dimensions noted in Table          1-2

Ta b  l  e    1 - 2 :                     M o d  u l  e    D i  m e n s  i o  n s  a

  Pa r  am e t e r                                            N o m i n a l                                       To l e r a n ce                                                  U n i t s

  Length                                                      18.0                                               ±0.10                                                             mm

  Width                                                       15.0                                               ±0.10                                                             mm

  Thickness                                                   2.4                                                ±0.20                                                             mm

  Weight                                                      1.17                                               ±0.24                                                             g

        a.                           Typical dimensional values, accurate as of the release date of this document.

                                                                         All electrical and mechanical connections to the HL781x module are made through
                                                                         the 86 Land Grid Array (LGA) pads on the bottom s ide of the PCB.


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           10                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41114133

## Page 11

I  n t r o d u c t  i o n


                                                                                                                                                              Figure 1-1:                       Mechanical Overview

                                                                                                                                                              Table          1-                   3 describes the LGA pads.

                                                                                                                                                              Ta b  l  e    1 - 3 :                     L G  A   P a d    Ty p  e s    /    D i  s t r i  b  u t i  o n

                                                                                                                                                                    Pa d   Ty p e                                                                                                                Q u a nt  i t y                                                                                                              D i m e n si  o ns                                                                                                           P i t c h

                                                                                                                                                                   Signal pads                                                                                                                  66 pads                                                                                                                      1.0×0.5 mm                                                                                                                   0.8 mm

                                                                                                                                                                                                                                                                                                16 inner pads                                                                                                                1.0×1.0 mm                                                                                                                   1.825 mm/1.475 mm
                                                                                                                                                                   Ground pads
                                                                                                                                                                                                                                                                                                4 outer corner pads                                                                                                          0.85×0.97 mm                                                                                                                 -


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           11                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41114133

## Page 12

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n

                                                                          1.4 General Features

                                                                          Table          1-                   4 s ummarizes the HL781x's features.

Ta b  l  e    1 - 4 :                     G e  n e r  a l    F  e a t u  r e s

  Fe a t u r e                                                 D es c r i  pt  i o n

                                                               Small form factor (86-pad solderable LGA pad). See Physical Dimensions and Connection
                                                               Interface for details.
  Physical                                                     Metal shield can
                                                               RF connection pads (RF_MAIN and RF_GNSS)
                                                               Baseband signals connection

                                                               3.2–4.35 V supply voltage (VBAT_BB, VBAT_RF)
  Power supply                                                 •                                                    Single supply (recommended)—VBAT (VBAT_BB tied to VBAT_RF) or
                                                               •                                                    Dual supplies—Single supply each for VBAT_BB and VBAT_RF

                                                               2G (HL7812 only)
                                                               •                                                    850/900 Power Class 4 (33 dBm), GPRS Class 10
                                                               •                                                    1800/1900 Power Class 1 (30 dBm), GPRS Class 10
                                                               Cat-M1
                                                               •                                                    Power Class 3 (23 dBm)
                                                               •                                                    Cat-NB2
                                                               •                                                    Power Class 3 (23 dBm)
                                                               GNSS
                                                               •                                                    GPS—1575.42 MHz
  RF                                                           •                                                    GLONASS—1589.0625–1605.375 MHz
                                                               See GNSS details.
                                                               Note:                The GNSS receiver and LTE/GSM receiver share the same RF resources, therefore
                                                               GNSS can only be used when the module is not actively connected on LTE/GSM. An example
                                                               of a suitable implementation of GNSS in an end product would be the use of GNSS positioning
                                                               for asset management applications where infrequent and no real-time position updates are
                                                               required.


                                                               Note:                The GNSS feature is not supported in NB-IoT mode.

                                                               1.8V support
                                                               SIM extraction / hot plug detection
  SIM interface                                                SIM/USIM support
                                                               Conforms with ETSI UICC Specifications
                                                               Supports SIM application tool kit with proactive UICC commands

                                                               AT command interface—3GPP 27.007 standard, plus proprietary extended AT commands
  Application interface                                        CMUX multiplexing over UART
                                                               USB Full Speed (FS)


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           12                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41114133

## Page 13

I  n t r o d u c t  i o n


Ta b  l  e    1 - 4 :                     G e  n e r  a l    F  e a t u  r e s     ( C o  n t i  n u  e d  )

   Fe a t u r e                                                                            D es c r i  pt  i o n

                                                                                          2G (HL7812 only)
                                                                                          •                                                    GPRS Class 10
                                                                                          Cat-M1
                                                                                          •                                                    3GPP Rel. 14:
                                                                                                          •                          Up to 1100 kbit/s UL, 590 kbit/s DL
                                                                                                          •                          HARQ-ACK bundling in HD-FDD
                                                                                                          •                          10 DL HARQ processes
                                                                                                          •                          Faster frequency returning
                                                                                                          •                          Release Assistance Indication
                                                                                          •                                                    Half-duplex
                                                                                          •                                                    Channel bandwidth—1.4 MHz
                                                                                          •                                                    LTE carrier bandwidth—1.4/3/5/10 /15/20 MHz
                                                                                          •                                                    Extended Coverage Mode A
                                                                                          •                                                    PSM (Power Save Mode)
                                                                                          •                                                    I-DRX (Idle Mode Discontinuous Reception)
                                                                                          •                                                    C-DRX (Connected Mode Discontinuous Reception)
                                                                                          •                                                    Idle mode mobility
                                                                                          •                                                    Connected mode mobility
                                                                                          •                                                    eDRX (Extended Discontinuous Reception)
                                                                                          •                                                    Control Plane CIoT Optimization (Data over NAS)
                                                                                          NB-IoT
   Protocol stack                                                                         •                                                    3GPP Rel. 14:
                                                                                                          •                          Up to 158 kbit/s UL, 127 kbit/s DL
                                                                                                          •                          2 HARQ processes
                                                                                                          •                          Release Assistance Indication
                                                                                                          •                          Long DRX values with regular wake-up cycle)
                                                                                                          •                          Cat-NB2
                                                                                          •                                                    Half-duplex
                                                                                          •                                                    Channel bandwidth—180 kHz
                                                                                          •                                                    LTE carrier bandwidth—1.4/3/5/10 /15/20 MHz
                                                                                          •                                                    Operational mode—In-band, Guard band, Standalone
                                                                                          •                                                    Control Plane CIoT Optimization (Data over NAS)
                                                                                          •                                                    NIDD over SGi tunneling
                                                                                          •                                                    NIDD over SCEF
                                                                                          •                                                    Extended coverage
                                                                                          •                                                    PSM (Power Save Mode)
                                                                                          •                                                    I-DRX (Idle Mode Discontinuous Reception)
                                                                                          •                                                    C-DRX (Connected Mode Discontinuous Reception)
                                                                                          •                                                    Idle mode mobility
                                                                                          •                                                    eDRX (Extended Discontinuous Reception)
                                                                                          Flexible selection
                                                                                          •                                                    Manual system selection across RATs
                                                                                          •                                                    Dynamic system selection across RATs (preferred RAT)


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           13                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41114133

## Page 14

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    1 - 4 :                     G e  n e r  a l    F  e a t u  r e s     ( C o  n t i  n u  e d  )

   Fe a t u r e                                                                       D es c r i  pt  i o n

                                                                                     Multiple cellular packet data profiles
                                                                                     Sleep mode for minimum idle power draw
                                                                                     Mobile-originated PDP context activation / deactivation
   Connectivity                                                                      Static and Dynamic IP address. The network may assign a fixed IP address or dynamically
                                                                                     assign one using DHCP (Dynamic Host Configuration Protocol).
                                                                                     PDP context type (IPv4, IPv6, IPv4v6)
                                                                                     RFC1144 TCP/IP header compression

                                                                                     Operating temperature ranges
   Environmental                                                                     •                                                    Class A: -30°C to +70°C
                                                                                     •                                                    Class B: -40°C to +85°C

   RTC                                                                               Real Time Clock (RTC)

                                                                                                    1.5 Architecture

                                                                                                    Figure          1-                   2 presents  an overview of the HL781x's internal architecture and external
                                                                                                    interfaces.


                                                                                                    Figure 1-2:                       Architecture Overview


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           14                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41114133

## Page 15

I  n t r o d u c t  i o n

                                                                                       1.6 Interfaces

                                                                                       The HL781x provides the following interfaces and peripheral connectivity:
                                                                                       •                                                    (1) VGPIO (1.8V)— See VGPIO
                                                                                       •                                                    (1) 1.8V USIM— See USIM Interface
                                                                                       •                                                    (1) USB 2.0 FS— See USB Interface.
                                                                                       •                                                    (12) GPIOs— See General Purpose Input/Output (GPIO).
                                                                                       •                                                    (1) 8-wire UART— See Main Serial Link (UART1).
                                                                                       •                                                    (1) Active low power on signal (will be available in a future firmware release)—
                                                                                                    See Power On Signal (POWER_ON_N).
                                                                                       •                                                    (1) Active low reset signal— See Reset Signal (RESET_IN_N)         .
                                                                                       •                                                    (2) ADC— See Analog to Digital Converter (ADC).
                                                                                       •                                                    (2) System clock out (32.768 kHz and 26 MHz)— See Clock Interface.
                                                                                       •                                                    (1) 4-wire UART for debug interface only— See Debug Interfaces.
                                                                                       •                                                    (1) Wake up signal— See Wake Up Signal (WAKEUP).
                                                                                       •                                                    (1) Main RF Antenna— See RF Interface.
                                                                                       •                                                    (1) TX_ON indicator— See TX Burst Indicator (TX_ON).
                                                                                       •                                                    (1) GNSS Antenna — See GNSS.
                                                                                       •                                                    (1) External PA Voltage Control Indicator— SeeTx/Rx Activity Indicator; External
                                                                                                    RF Voltage Control.


Ta b  l  e    1 - 5 :                     E S D   S p  e c i  f i c  a t i  o n  s a

   Ca t e g o r y                                          C o nn e c t i o n                                                                                               S p ec i f  i c at  i o n
                                                           •                                                    Power supply (C61, C62, C63)                               IEC-61000-4-2 (Electrostatic Discharge Immunity Test)
  Operational                                              •                                                    RF ports (C38, C49)                                        •                                                    ±6 kV Contact
                                                                                                                                                                           •                                                    ±8 kV Air

                                                                                                                                                                           Unless otherwise specified:
  Non-operational                                          All pins                                                                                                        •                                                    JESD22-A114 ± 250 V Human Body Model
                                                                                                                                                                           •                                                    JESD22-C101C ± 250V Charged Device Model

         a.                           ESD protection is highly recommended on customer platform. For details, see ESD Protection for I/Os


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           15                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41114133

## Page 16

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n

                                                                                 1.7 Environmental Specifications

                                                                                 The environmental specifications for operation and storage of the HL781x are defined
                                                                                 in Table          1-                   6.

                                                                                 Ta b  l  e    1 - 6 :                     E n v i  r o  n m  e n t a  l    S p e c  i f i  c a t i  o  n s

                                                                                    Pa r  am e t e r                                                                                             R a ng e                                                                   O p er  a t i n g   C l a s s

                                                                                                                                                                                                -30°C to +70°C                                                              Class A
                                                                                   Ambient Operating Temperature
                                                                                                                                                                                                -40°C to +85°C                                                              Class B

                                                                                   Ambient Storage Temperature                                                                                  -40°C to +85°C                                                              -

                                                                                 Class A is defined as the operating temperature range within which the device:
                                                                                 •                                                    Shall exhibit normal function during and after environmental exposure.
                                                                                 •                                                    Shall meet the minimum requirements of 3GPP or appropriate wireless
                                                                                             standards.
                                                                                 Class B is defined as the operating temperature range within which the device:
                                                                                 •                                                    Shall remain fully functional during and after environmental exposure
                                                                                 •                                                    Shall exhibit the ability to establish any of the device’s supported call modes
                                                                                             (SMS, Data, and emergency calls) at all times even when one or more environ-
                                                                                             mental constraint exceeds the specified tolerance.
                                                                                 •                                                    Unless otherwise stated, full performance should return to normal after the
                                                                                             exces sive constraint(s) have been removed.


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           16                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41114133

## Page 17

2: Pad Definition                                                                                                                                                                                                                    2


                                                       Sierra Wireless HL781x pins are divided into three categories.
                                                       •                                                    Core functions and associated pins— Cover all the mandatory features for M2M
                                                                connectivity and will be available by default across the CF3 module family. These
                                                                Core functions are always available and always at the same physical pad
                                                                locations. A customer platform using only thes e functions and associated pads is
                                                                guaranteed to be forward and/or backward compatible with the next generation of
                                                                CF3 modules.
                                                       •                                                    Extension functions and associated pins— Bring additional capabilities to the
                                                                customer. Whenever an Extension function is available on a module, it is always
                                                                at the same pad location.
                                                       •                                                    Custom functions and associated pins— Module-specific functionality. If a custom
                                                                function is available on another module, there is no guarantee that it will be at the
                                                                same pad location.
                                                       For example:
                                                       •                                                    UART1 interface is a "Core" function on pins C2–C9 that is available on all CF3
                                                                modules (including HL781x).
                                                       •                                                    USB interface is an "Extension" function on pins C12–C13 that is available on
                                                                HL781x modules, but may not be available on certain other CF3 modules.
                                                       •                                                    UART0 signals are "Custom" functions on pins C57 and C58. These signals may
                                                                or may not be available on other CF3 modules and, if available, may be on
                                                                different pins.
                                                       Pins marked as "Not connected" should not be used.
                                                       2.1 Pin Types

                                                       Table          2-                   1 lists a series of codes used to identify pin characteristics throughout this
                                                       document.

                                                       Ta b  l  e    2 - 1 :                     P i n     Ty p  e    C o d  e s

                                                         Co d e                  D e f i  ni  t i o n                                                                 C od e                 D ef  i n i t i  on

                                                         AI                     Analog Input                                                                          O                      Digital Output

                                                         ANT                    Antenna                                                                               PD                     Pull-down enabled

                                                         GND                    Ground                                                                                PI                     Power In

                                                         I                      Digital Input                                                                         PO                     Power Out

                                                         I/O                    Digital Input/Output                                                                  PU                     Pull-up enabled

                                                         N/A                    Not applicable


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          17                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41114133

## Page 18

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    2 - 2 :                     P i n     D e f i  n i  t i o  n s

  Pi  n               S i g na l    N am e                                   G r o u p                            I /  O             Vo l t  ag e  S u pp l y                          F un c t i o n                                                                       R e co m m e n d at  i o n   f o r                                      I s ol  at  e                     C F3
                                                                                                                                     D o m a i n                                                                                                                            u n us e d   pa d s                                                     r e qu i r  e d a

  C1                 GPIO1                                                   GPIOb                                I/O               1.8V (VGPIO)                                      General purpose input/output                                                          Leave open                                                              Yes                               Extension

  C2                 UART1_RIc                                               UART1b                               O                 1.8V (VGPIO)                                      UART1 Ring Indicator                                                                  Leave open                                                              Yes                               Core

  C3                 UART1_RTS                                               UART1b                               I                 1.8V (VGPIO)                                      UART1 Request To Send                                                                 Mandatory connection                                                    Yes                               Core

  C4                 UART1_CTS                                               UART1b                               O                 1.8V (VGPIO)                                      UART1 Clear To Send                                                                   Mandatory connection                                                    Yes                               Core

  C5                 UART1_TX                                                UART1b                               I                 1.8V (VGPIO)                                      UART1 Transmit Data                                                                   Mandatory connection                                                    Yes                               Core

  C6                 UART1_RX                                                UART1b                               O                 1.8V (VGPIO)                                      UART1 Receive Data                                                                    Mandatory connection                                                    Yes                               Core

  C7                 UART1_DTR                                               UART1b                               I                 1.8V (VGPIO)                                      UART1 Data Terminal Ready                                                             Leave open                                                              Yes                               Core

  C8                 UART1_DCD                                               UART1b                               O                 1.8V (VGPIO)                                      UART1 Data Carrier Detect                                                             Leave open                                                              Yes                               Core

  C9                 UART1_DSR                                               UART1b                               O                 1.8V (VGPIO)                                      UART1 Data Set Ready                                                                  Leave open                                                              Yes                               Core

  C10                GPIO2                                                   GPIOb                                I/O               1.8V (VGPIO)                                      General purpose input/output                                                          Leave open                                                              Yes                               Core

  C11                RESET_IN_N                                              H/W Controld                         I                 Internal Bias                                     Input reset signal                                                                    Leave open                                                              No                                Core

  C12                USB_D-                                                  USB                                  I/O               3.3V                                              USB Data Negative (Full Speed)                                                        Leave open                                                              No                                Extension

  C13                USB_D+                                                  USB                                  I/O               3.3V                                              USB Data Positive (Full Speed)                                                        Leave open                                                              No                                Extension

  C14                NC                                                      Not                                                                                                      Not Connected                                                                         See footnotee                                                           No                                Not
                                                                             connected                                                                                                                                                                                                                                                                                                connected

  C15                NC                                                      Not                                                                                                      Not Connected                                                                         See footnotee                                                           No                                Not
                                                                             connected                                                                                                                                                                                                                                                                                                connected

                                                                                                                                                                                                                                                                            If USB is:
                                                                                                                                                                                                                                                                            •                                                    Not used—Leave
  C16                USB_VBUS                                                USB                                  PI                5V                                                USB VBUS                                                                                         open                                                         No                                Extension
                                                                                                                                                                                                                                                                            •                                                    Used—Mandatory
                                                                                                                                                                                                                                                                                       connection

  C17                NC                                                      Not                                                                                                      Not Connected                                                                         See footnotee                                                           No                                Not
                                                                             connected                                                                                                                                                                                                                                                                                                connected


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              18                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          41114133

## Page 19

P a  d  D  e f i  n i t  i o  n


Ta b  l  e    2 - 2 :                     P i n     D e f i  n i  t i o  n s    ( C o  n t i  n  u e d  )

  Pi  n               S i g na l    N am e                                    G r o u p                             I /  O             Vo l t  ag e  S u pp l y                          F un c t i o n                                                                         R e co m m e n d at  i o n   f o r                                       I s ol  at  e                      C F3
                                                                                                                                       D o m a i n                                                                                                                              u n us e d   pa d s                                                      r e qu i r  e d a

  C18                 NC                                                      Not                                                                                                        Not Connected                                                                          See footnotee                                                            No                                Not
                                                                              connected                                                                                                                                                                                                                                                                                                    connected

  C19                 NC                                                      Not                                                                                                        Not Connected                                                                          See footnotee                                                            No                                Not
                                                                              connected                                                                                                                                                                                                                                                                                                    connected

  C20                 NC                                                      Not                                                                                                        Not Connected                                                                          See footnotee                                                            No                                Not
                                                                              connected                                                                                                                                                                                                                                                                                                    connected

  C21                 NC                                                      Not                                                                                                        Not Connected                                                                          Leave open                                                               No                                Not
                                                                              connected                                                                                                                                                                                                                                                                                                    connected

  C22                 26M_CLKOUT                                              Clockb                                O                 1.8V (VGPIO)                                       26 MHz System Clock Output                                                             Leave open                                                               Yes                               Extension

  C23                 32K_CLKOUT                                              Clockb                                O                 1.8V (VGPIO)                                       32.768 kHz System Clock Output                                                         Leave open                                                               Yes                               Extension

  C24                 ADC1                                                    ADCb                                  AI                1.8V (VGPIO)                                       Analog to digital converter                                                            Leave open                                                               Yes                               Extension

  C25                 ADC0                                                    ADCb                                  AI                1.8V (VGPIO)                                       Analog to digital converter                                                            Leave open                                                               Yes                               Extension

  C26                 UIM1_VCC                                                UIMb                                  PO                1.8V                                               USIM1 Power supply                                                                     Leave open                                                               No                                Core

  C27                 UIM1_CLK                                                UIMb                                  O                 1.8V (VGPIO)                                       USIM1 Clock                                                                            Leave open                                                               No                                Core

  C28                 UIM1_DATA                                               UIMb                                  I/O               1.8V (VGPIO)                                       USIM1 Data                                                                             Leave open                                                               No                                Core

  C29                 UIM1_RESET                                              UIMb                                  O                 1.8V (VGPIO)                                       USIM1 Reset                                                                            Leave open                                                               No                                Core

  C30                 RF_DIV_GND_1                                            Ground                                GND               Ground                                             Ground                                                                                 Mandatory connection                                                     No                                Extension

  C31                 NC                                                      Not                                                                                                        Not Connected                                                                          See footnotee                                                            No                                Not
                                                                              connected                                                                                                                                                                                                                                                                                                    connected

  C32                 RF_DIV_GND_2                                            Ground                                GND               Ground                                             Ground                                                                                 Mandatory connection                                                     No                                Extension

  C33                 Reserved                                                Reserved                                                                                                   Reserved                                                                               Leave openf                                                              No                                Extension

  C34                 Reserved                                                Reserved                                                                                                   Reserved                                                                               Leave openf                                                              No                                Extension

  C35                 Reserved                                                Reserved                                                                                                   Reserved                                                                               Leave openf                                                              No                                Extension


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              19                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          41114133

## Page 20

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    2 - 2 :                     P i n     D e f i  n i  t i o  n s    ( C o  n t i  n  u e d  )

  Pi  n              S i g na l    N am e                                  G r o u p                           I /  O            Vo l t  ag e  S u pp l y                         F un c t i o n                                                                     R e co m m e n d at  i o n   f o r                                    I s ol  at  e                    C F3
                                                                                                                                 D o m a i n                                                                                                                         u n us e d   pa d s                                                   r e qu i r  e d a

  C36                Reserved                                             Reserved                                                                                               Reserved                                                                            Leave openf                                                          No                                Extension

  C37                RF_GNSS_GND_1                                        Ground                               GND               Ground                                          Ground (RF_GNSS)                                                                    Mandatory connection                                                 No                                Core

  C38                RF_GNSS                                              Antenna                              ANT                                                               GNSS antenna input                                                                  Leave open                                                           No                                Extension

  C39                RF_GNSS_GND_2                                        Ground                               GND               Ground                                          Ground (RF_GNSS)                                                                    Mandatory connection                                                 No                                Core

  C40                GPIO7                                                GPIOb                                I/O               1.8V (VGPIO)                                    General purpose input/output                                                        Leave open                                                           Yes                               Core

                     GPIO8                                                                                     I/O                                                               General purpose input/output                                                                                                                                                               Core
  C41                                                                     GPIOb                                                  1.8V (VGPIO)                                    Tx/Rx activity indicator/External                                                   Leave open                                                           Yes
                     VBAT_PA_EN                                                                                O                                                                 RF voltage control                                                                                                                                                                         Custom

  C42                NC                                                   Not                                                                                                    Not Connected                                                                       See footnotee                                                        No                                Not
                                                                          connected                                                                                                                                                                                                                                                                                         connected

  C43                Reserved                                             Reserved                                                                                               Reserved                                                                            Leave openf                                                          No                                Extension

  C44                WAKEUP                                               H/W Controld                         I                 1.8V                                            Wake up signal                                                                      Mandatory connection                                                 No                                Extension

  C45                VGPIO                                                Power                                PO                1.8V (VGPIO)                                    GPIO voltage output (reference                                                      Leave open                                                           No                                Core
                                                                                                                                                                                 voltage)

  C46                GPIO6                                                GPIOb                                I/O               1.8V (VGPIO)                                    General purpose input/output                                                        Leave open                                                           Yes                               Core

  C47                NC                                                   Not                                                                                                    Not Connected                                                                       Leave opene                                                          No                                Not
                                                                          connected                                                                                                                                                                                                                                                                                         connected

  C48                RF_MAIN_GND_1                                        Ground                               GND               Ground                                          Ground (RF_MAIN)                                                                    Mandatory connection                                                 No                                Core

  C49                RF_MAIN                                              Antenna                              ANT                                                               Main RF antenna input/output                                                        Mandatory connection                                                 No                                Core
                                                                                                                                                                                 (Rx/Tx)

  C50                RF_MAIN_GND_2                                        Ground                               GND               Ground                                          Ground (RF_MAIN)                                                                    Mandatory connection                                                 No                                Core

                     GPIO14                                               GPIOb                                I/O                                                               General purpose input/output                                                                                                                                                               Extension
  C51                                                                                                                            1.8V (VGPIO)                                    (MLI debug) UART3 Clear To                                                          Leave open                                                           Yes
                     UART3_CTS                                            UART3b                               O                                                                 Send                                                                                                                                                                                       Custom


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              20                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          41114133

## Page 21

P a  d  D  e f i  n i t  i o  n


Ta b  l  e    2 - 2 :                     P i n     D e f i  n i  t i o  n s    ( C o  n t i  n  u e d  )

  Pi  n               S i g na l    N am e                                     G r o u p                              I /  O             Vo l t  ag e  S u pp l y                           F un c t i o n                                                                          R e co m m e n d at  i o n   f o r                                        I s ol  at  e                      C F3
                                                                                                                                         D o m a i n                                                                                                                                u n us e d   pa d s                                                       r e qu i r  e d a

                      GPIO10                                                   GPIOb                                  I/O                                                                   General purpose input/output                                                                                                                                                                         Extension
  C52                                                                                                                                   1.8V (VGPIO)                                        (MLI debug) UART3 Transmit                                                              Leave open                                                                Yes
                      UART3_TX                                                 UART3b                                 I                                                                     data                                                                                                                                                                                                 Custom

                      GPIO11                                                   GPIOb                                  I/O                                                                   General purpose input/output                                                                                                                                                                         Extension
  C53                                                                                                                                   1.8V (VGPIO)                                        (MLI debug) UART3 Request To                                                            Leave open                                                                Yes
                      UART3_RTS                                                UART3b                                 I                                                                     Send                                                                                                                                                                                                 Custom

                      GPIO15                                                   GPIOb                                  I/O                                                                   General purpose input/output                                                                                                                                                                         Extension
  C54                                                                                                                                   1.8V (VGPIO)                                        (MLI debug) UART3 Receive                                                               Leave open                                                                Yes
                      UART3_RX                                                 UART3b                                 O                                                                     data                                                                                                                                                                                                 Custom

  C55                 UART0_RX                                                 UART0b                                 O                 1.8V (VGPIO)                                        Debug Receive data                                                                      Leave open                                                                Yes                                Extension

  C56                 UART0_TX                                                 UART0b                                 I                 1.8V (VGPIO)                                        Debug Transmit data                                                                     Leave open                                                                Yes                                Extension

  C57                 UART0_CTS                                                UART0b                                 O                 1.8V (VGPIO)                                        Debug Clear To Send                                                                     Leave open                                                                Yes                                Custom

  C58                 UART0_RTS                                                UART0b                                 I                 1.8V (VGPIO)                                        Debug Request To Send                                                                   Leave open                                                                Yes                                Custom

  C59                 POWER_ON_N                                               H/W Controld                           I                 Internal Bias                                       Active-low Power On control                                                             Leave open                                                                No                                 Core
                                                                                                                                                                                            signal

  C60                 TX_ON                                                    Indicationb                            O                 1.8V (VGPIO)                                        TX transmission indication                                                              Leave open                                                                Yes                                Extension

                                                                                                                                        3.2V (min)
  C61                 VBAT_RF                                                  Power                                  PI                3.7V (typ) 4.35V                                    Power supply                                                                            Mandatory connection                                                      No                                 Core
                                                                                                                                        (max)

                                                                                                                                        3.2V (min)
  C62                 VBAT_RF                                                  Power                                  PI                3.7V (typ) 4.35V                                    Power supply                                                                            Mandatory connection                                                      No                                 Core
                                                                                                                                        (max)

                                                                                                                                        3.2V (min)
  C63                 VBAT_BB                                                  Power                                  PI                3.7V (typ) 4.35V                                    Power supply                                                                            Mandatory connection                                                      No                                 Core
                                                                                                                                        (max)


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              21                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          41114133

## Page 22

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    2 - 2 :                     P i n     D e f i  n i  t i o  n s    ( C o  n t i  n  u e d  )

  Pi  n               S i g na l    N am e                                     G r o u p                              I /  O             Vo l t  ag e  S u pp l y                            F un c t i o n                                                                          R e co m m e n d at  i o n   f o r                                        I s ol  at  e                       C F3
                                                                                                                                         D o m a i n                                                                                                                                 u n us e d   pa d s                                                       r e qu i r  e d a

                      UIM1_DET                                                 UIM1b                                  I                                                                     UIM1 Detection                                                                                                                                                                                        Core
  C64                                                                                                                                    1.8V (VGPIO)                                                                                                                                Leave open                                                                Yes
                      GPIO3                                                    GPIOb                                  I/O                                                                   General purpose input/output                                                                                                                                                                          Extension

  C65                 GPIO4                                                    GPIOb                                  I/O                1.8V (VGPIO)                                       General purpose input/output                                                             Leave open                                                                Yes                                Extension

  C66                 GPIO5                                                    GPIOb                                  I/O                1.8V (VGPIO)                                       General purpose input/output                                                             Leave open                                                                Yes                                Extension

  CG1-                GND                                                      Ground                                 GND                Ground                                             Ground                                                                                   Mandatory connection                                                      No                                 Core
  CG4

  G1-                 GND                                                      Ground                                 GND                Ground                                             Ground                                                                                   Mandatory connection                                                      No                                 Core
  G16

        a.                           The host platform should isolate these signals during module Hibernate mode to prevent back-powering the module. For details, see Hibernate—Isolation Requirements.
        b.                           By default, signals in group (GPIO, UART, UIM1, ADC, Clock, Indication) are hardware-configured as inputs and are in an undefined state during OFF, reset, and Hibernate
                 modes. The host should ignore all activity on these signals until the module has initialized and reached AT-READY (UART1_CTS transitions from high to low (and stays low) and
                 VGPIO is high, indicating the UART and USB interfaces are ready). For timing details, see Unmanaged POWER_ON_N (Default) and Wakeup from OFF Mode. For further infor-
                 mation regarding pre- and post-AT-READY signal states, contact Sierra Wireless.
        c.                             UART1_RI cannot be used in Hibernate mode. A GPIO (GPIO2 by default) can be configured as an alternate ring indicator. For details, see Ring Indicator (UART1_RI or Alterna-
                 tive).
        d.                           Hardware Control signals are available in all module operational modes and determine module behavior. For recommendations on managing these signals, see associated signal
                 topics in Detailed Interface Specifications.
        e.                           Pin is not connected internally, but is reserved for future use. Leave unconnected to ensure compatibility with other Sierra Wireless CF3 modules.
        f.                                   Pin is connected internally, leave open.


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              22                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          41114133

## Page 23

P a  d  D  e f i  n i t  i o  n

                                                                                                                                     2.2 Pad Configuration


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        Core pin
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  Ext en si o n  pi n
                                                                                                                                                                                                                         GND                 CG4                                                                                                                                                                                                      CG3                                            Custom pin
                                                                                                                                                                                                                                                                                                                                                                                                                                                                        GND

                                                                                                                                                                                   GPIO10 / UART3_TX                                         C52C52                                                                                                                                                                                                    C33              Reserved
                                                                                                                                                                                 GPIO11 / UART3_RTS                                          C53C53                                                                                                                                                                                                    C32              RF_DIV_GND_2
                                                                                                                                                                                   GPIO15 / UART3_RX                                         C54                                                                                                                                                                                                       C31              NC
                                                                                                                                                                                                                                             C54
                                                                                                                                                                                                          UART0_RX                           C55                                                                                                                                                                                                       C30              RF_DIV_GND_1
                                                                                                                                                                                                           UART0_TX                          C56                                                               G13                    G14                    G15                   G16                                                                 C29              UIM1_RESET
                                                                                                                                                                                                        UART0_CTS                            C57                                                                                                                                                                                                       C28              UIM1_DATA
                                                                                                                                                                                                        UART0_RTS                            C58                                                                 G9                   G10                    G11                   G12                                                                 C27              UIM1_CLK
                                                                                                                                                                                                POWER_ON_N                                   C59                                                                                                 GND                                                                                                   C26              UIM1_VCC
                                                                                                                                                                                                                    TX_O N                   C60                                                                 G5                     G6                    G7                    G8                                                                 C25              ADC0
                                                                                                                                                                                                              VBAT_RF                        C61                                                                                                                                                                                                       C24              ADC1
                                                                                                                                                                                                              VBAT_RF                        C62                                                                 G1                     G2                    G3                    G4                                                                 C23              32K_CLKOUT

                                                                                                                                                                                                              VBAT_BB                        C63                                                                                                                                                                                                       C22              26 M_C L KO UT
                                                                                                                                                                                        UIM1_DET/ GPIO3                                      C64                                                                                                                                                                                                       C21              NC
                                                                                                                                                                                                                     GPIO4                   C65                                                                                                                                                                                                       C20              NC
                                                                                                                                                                                                                     GPIO5                   C66                                                                                                                                                                                                       C19              NC

                                                                                                                                                                                                                        GND                  CG1                                                                                                                                                                                                      CG2               GND


                                                                                                                                     Figure 2-1:                       Pad Configuration (Top View through Module)


Rev 9                      August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           23                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41114133

## Page 24

3: Power Specifications                                                                                                                                                                                                                                                                                           3


                                                     Note:                If not specified, all electrical values are given for VBAT_BB and VBAT_RF = 3.7V, operating
                                                     temperature of 25°C. and with conducted 50 load on RF port(s).

                                                     3.1 Power Supply

                                                     The module is supplied through the VBAT_BB and VBAT_RF signals.
                                                     For standard applications, VBAT_BB and VBAT_RF must be tied externally to the same
                                                     power supply. For some specific applications (e.g. applications requiring a lower
                                                     VBAT_RF), the module supports separate VBAT_BB and VBAT_RF connection as per
                                                     Table          3-                   1.
                                                     Table          3-                   1 and Table 3-2 describe the Power Supply interface.

                                                     Ta b  l  e    3 - 1 :                     P o w  e r    S u p  p l  y    P i n     D e s c r  i p  t i  o n

                                                       Pa d   #                                                                                                     S i  gn a l   N a m e                   I / O               D e s cr  i p t i o n

                                                       C63                                                                                                         VBAT_BB                                  PI                 Power supply (baseband)

                                                       C61, C62                                                                                                    VBAT_RF                                  PI                 Power supply (radio
                                                                                                                                                                                                                               frequency)

                                                       C30, C32, C37, C39, C48, C50, CG1– CG4,                                                                                                              GND                Ground
                                                       G1–G16


                                                     Caution:                Operation outside the minimum/maximum specified operating voltage (Table         3-2) is not
                                                     recommended, and functional operation of the device and specified typical performance are neither
                                                     implied nor guaranteed.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               24                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 25

P o w e r  S p e c i f  i c a t  i o  n s


Ta b  l  e    3 - 2 :                     P o w  e r    S u p  p l  y    C u  r r e n  t   R  e q u  i  r e m e n  t s

  Pa r  am e t e r                                                                                        M i n                         Ty p                       M a x                                          Un i  t                       N ot  e s

  VBAT_BB voltage                                                                                         3.2                           3.7                        4.35                                          V                              Must be within
                                                                                                                                                                                                                                                min/max values
  VBAT_RF voltage Full Specification                                                                      3.2                           3.7                        4.35                                          V                              overall operating
  VBAT_RF voltage Extended Range                                                                          2.8a                          3.7                        4.35                                          V                              conditions (including
                                                                                                                                                                                                                                                voltage ripple, droop,
                                                                                                                                                                                                                                                and transient)

  Power Supply Ripple                                                                                     -                             -                          100b                                          mVpp

                                                      VBAT_BB                                             -                             -                          180                                           mA

                                                      VBAT_RF (LTE)                                       -                             -                          (HL7810) 300                                  mA
  Max Supply                                                                                                                                                       (HL7812) 400
  Current                                             (HL7812 only)                                       -                             1.9                        2.5                                           A
                                                      VBAT_RF (2G)
                                                      Peak Current

        a.                           3GPP performance is not guaranteed for VBAT_RF from 2.8-3.2V. Note that operation in this range requires a separate
                 VBAT_RF supply.
        b.                           Measured at nominal supply voltage (3.7V), nominal ambient temperature (25°C), and with conducted 50 load on RF port(s).


                                                      Note:                The host power supply should be capable of supplying VBAT_BBmax + VBAT_RFmax.

                                                      3.2 Electrical Specifications

                                                      3.2.1 Digital I/O Characteristics

                                                      The I/O characteristics  for supported digital interfaces/signals are described in Table          3-                   3.
                                                      These interfaces/signals include:
                                                      •                                                    UARTs
                                                      •                                                    GPIOs
                                                      •                                                    Clock output signals
                                                      •                                                    UIM1
                                                      •                                                    TX_ON
                                                      •                                                    External PA voltage control indicator
                                                      These signals are not available in Hibernate mode since VGPIO is OFF.


                                                      Note:                The host platform should isolate these signals during module Hibernate mode to prevent
                                                      back-powering the module. For details, see Hibernate—Isolation Requirements.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               25                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 26

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    3 - 3 :                     D i  g i  t a l    I  /  O    E l e  c t r i  c a l    C  h a r  a c t e r  i s  ti  c s    ( 1 .  8 0  V )  a

  Pa r  am e t e r                        D e sc r i  pt  i o n                                                                    M i  n                                           M a x                                             Un i t

  VIH                                    Logic High Input Voltage                                                                  0.7 × VGPIO                                      VGPIO                                             V

  VIL                                    Logic High Input Voltage                                                                  0                                                0.3 × VGPIO                                       V

  VOH                                    Logic High Input Voltage                                                                  0.8 × VGPIO                                                                                        V

  VOL                                    Logic High Input Voltage                                                                                                                   0.2 × VGPIO                                       V

  IO                                     Output Current                                                                            2                                                4                                                 mA

  IRPD                                   Internal Pull-Down Resistor current                                                       11                                               43                                                A

  IRPU                                   Internal Pull-Up Resistor current                                                         11                                               44                                                A

  RPU                                    Internal Pull-Up Resistor                                                                 13                                               45                                                k

  RPD                                    Internal Pull-Down Resistor                                                               13.6                                             45                                                k

       a.                           VGPIO=1.8V (See VGPIO.)

                                                3.3 3GPP Power Saving Features

                                                This section describes 3GPP power saving features (PSM, eDRX) that are supported by
                                                the HL781x module. Per 3GPP specifications, these features pertain to the module’s
                                                cellular communication.
                                                The HL781x also features low power modes that contribute to power savings by
                                                selectively limiting or turning off other elements of the module, such as memory states, I/O
                                                states, etc. (For details, see HL781x Low Power Modes.)

                                                3.3.1 Power Saving Mode (PSM)

                                                Power Saving Mode (PSM) is a 3GPP feature that allows the Sierra Wireless HL781x to
                                                minimize power consumption by registering on a PSM-supporting LTE network and then
                                                entering PSM state for a configured duration.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               26                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 27

P o w e r  S p e c i f  i c a t  i o  n s


                                                                               Module connected, typi cal current cons umpti on


                                                                                                          Idle mode, access ible from network, l ower current cons umpti on than connec  ted mode
                                                    Conne cted

                                                                                 Idle                                        PSM (  very low current c  ons  umpti on compared to connected mode)

                                                                                                                                                       PSM dorm ant


                                                                               T3324                                                   PSM d ormant perio d (T3412 – T3324)
                                                                         (A  ct i v e T i mer )

                                                                                                                          PSM Duration Timer (Extended‐T3412)
                                                                           Module may be wo ken at an y time by h ost app lic ation , or wakes a ut om  at i cal ly w he n T 3412 e x pires

                                           Figure 3-1:                       PSM—Timers

                                           When the module enters the PSM state:
                                           1.                                  The module remains active (accessible from the network) in a lower-power idle s tate
                                                   for a short period (T3324 Active Timer).
                                           2.                                  The module then drops to a very-low power ‘dormant’ state for the remainder of the
                                                   PSM duration or until the host platform wakes the module to initiate a network contact.
                                                   During this dormant period, the module is not accessible from the network.
                                           3.                                  After the module contacts the network (for either reason), the process repeats.
                                           Using PSM, an HL781x-based host platform can reduce power consumption significantly
                                           because:
                                           •                                                    It can enter a very low power state (~1.8 A) during a very long PSM dormant period.
                                           •                                                    The platform can wake the HL781x at any time to initiate data transaction immediately
                                                   with minimal overhead (signaling/procedure) since the network keeps the module
                                                   registered during the entire PSM period.
                                           Typical candidates for PSM are systems (such as monitors and sensors) that:
                                           •                                                    Require long battery life (low power consumption)
                                           •                                                    Infrequently send mobile originated data (every few hours, days, weeks , etc.), with
                                                   optional reply data from the network
                                           •                                                    Tolerate modules being inaccessible for long periods of time
                                           •                                                    Do not use mobile-terminated voice/data/SMS. If the host platform needs the module
                                                   to be able to receive mobile-terminated data, eDRX is a more suitable option.
                                           Figure 3-2 describes an example of a module operating in PSM. In a typical application,
                                           the module will always be woken from the dormant state to transmit data (illustrated in the
                                           ’Typical MO Use Case’ portion of the figure). This is accomplis hed by setting the T3412
                                           timer much longer than anticipated transmission frequency.
                                           However, if the module is not woken by the host, a TAU will be sent when T3412 expires
                                           (illustrated in the ’Default PSM Use Case’ portion of the figure). By setting the T3412
                                           longer, unnecessary TAU transmissions can be avoided.
                                           For a more detailed explanation of PSM, refer to [4] HL78xx Low Power Modes
                                           Application Note (Doc#          2174229).


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               27                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 28

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


                                                                                                                                                               T y pi ca l  M O  Us e  C a se                                                                                 Default PSM Behaviour ON T3412 Expiry
                                                                                                                                                              (most power‐efficient)
                                                                                                          2.  Use r ena bl es P SM.
                                                                                                          3. Module  re quests user ‐specifie d Act ive
                                                                                                          Timer  and PSM Timer.
                                                                                                          4.  Network  responds  with negotiated                8. Module transmits data to                                                                                                  11. Module transmits periodic TAU
                                                                             1. Module attaches on        timers to use.                                        the net work.                                                                                                                to the network .
                                                                             network                      5. Module enters and stays in idle mode
                                                                                                          u nti l  it  re mai ns  u ni nt err up ted  fo r  the
                                                                                                          Active Timer duration.                                                                      9.  PSM p ro ce ss r ep eats ( ena bl e P SM,
                                                                                                                                                                                                      re quest t ime rs, etc.)                                                                                                               12 .  PSM  pro ces s  re pea ts
                                                                                                                                6.  Module  ente rs
                                                                                                                                PS M  (dorma nt,  very
                                                                                                                                low power)
                                                                                                                                                                                                                                                                      10. PSM Timer expire s
                                                                                                                                                      7. Interrupted                                                                                                  an d  mo du le wakes  t o
                                                                                                                                                      by WAKE_UP.                Transmit                                                                             s end  p er io dic  TAU.
                                                                                         ATTA CH                                                                                    data                                                                                                                                TAU
                                                                                                       Connected                                                                 (Possible
                                                                                                                                                                                     Rx)
                                                                              BOOT                                            Idle                                  BOOT                                  Idle                                                                                      BOOT                                         Idle

                                                                                                                                             PSM dormant                                                                                              PSM dormant                                                                                               PSM dormant


                                                                                                                        T3324 (Active Timer)                                                        T3324 (Active Timer)

                                                                                                                                                                                                                                 Extended‐T3412 (PSM Timer)
                                                                                                                                  Extended‐T3412 (PSM Timer) (Interrupted by UE using WAKE_UP)                                     (Runs until timer expires)

                                                                     Figure 3-2:                       Power Saving Mode—Use Cases Example

                                                                     3.3.2 Extended DRX (eDRX)

                                                                     3.3.2.1 eDRX Overview

                                                                     Extended Idle DRX (I-eDRX) is  a 3GPP-specified extension of the Discontinuous
                                                                     Reception (DRX) low power consumption feature. This extension reduces the number of
                                                                     paging opportunities (PO) the module must monitor while in idle state, resulting in a
                                                                     corres ponding decrease in power consumption.
                                                                     Many data module applications are tolerant to delays in downlink data packets so
                                                                     extending the period between paging opportunities would allow for current consumption
                                                                     savings for these applications.


                                                                                          Module operating in connected
                                                                                          sta te
                                                                                                   Network releases UE due to                                                                            eD RX
                                                                                                   in  a ct i  vit  y
                                                                                                                                           Paging Opportunity
                                                                                                        Calibrations


                                                                             Connected                           Idle


                                                                                                                                                                                 Ve  ry   lo  ng   sl ee  p d  urat io  n  –  low  c o  ns u  mp  tion   for e  ntir e  dura  tion


                                                                                                                             PTW


                                                                                                                                                                                               eDRX Cycle
                                                                                                                                                                             Module operating in Idle eDRX (TI‐eDRX)


                                                                                          Module operating in connected
                                                                                          sta te
                                                                                                   Network releases UE due to                                                                              DRX
                                                                                                   in  a ct i  vit  y
                                                                                                                                           Paging Opportunity


                                                                                                                                                                                                                                                    Regular DRX intervals
                                                                             Connected                          Idle


                                                                                                                                                   Modul e  op er at ing  i n  Idle DRX  ( TI‐DRX)
                                                                     Figure 3-3:                       eDRX vs DRX


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               28                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 29

P o w e r  S p e c i f  i c a t  i o  n s


                                             As shown in Figure         3-                   3, the HL7812 supports eDRX, taking advantage of the feature by
                                             monitoring a set number of paging opportunities in a Paging Time Window (PTW) and
                                             then entering a low power state between PTWs. This sequence (PTW followed by low
                                             power state) comprises a single eDRX cycle. The size of the PTW and the length of the
                                             eDRX cycle (TI-eDRX) are negotiated between the module (which submits desired values
                                             when enabling eDRX) and the network (which indicates the values that will actually be
                                             used).
                                             The module remains in I-eDRX until it detects a page from the network during a PO or
                                             needs to access the network (e.g. to make a data connection, send a mobility TAU or
                                             periodic TAU, etc.), at which time it returns to the connected state.
                                             Note that for a short period of time immediately after the module is released from
                                             connected state by the network and enters idle state, it has a few extra short wake ups for
                                             clock calibration (shorter than a single PO). Figure         3-                   4 shows an eDRX power
                                             consumption profile with a periodic TAU event. Notice that after the TAU, the eDRX
                                             81.92s cycle is restored slowly by several iterations from 10s to 20s then to 40s before
                                             reaching the 81.92s wake. This behavior is an HL781x design feature and cannot be
                                             modified.


                                             Figure 3-4:                       eDRX Power Consumption Profile Interruption

                                             For a more detailed explanation of eDRX, refer to HL78xx Low Power Modes Application
                                             Note.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               29                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 30

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


                                              3.3.2.2 Configuring eDRX

                                              Table          3-                   4 describes available methods for configuring eDRX.

                                              Ta b  l  e    3 - 4 :                     e D R X - R e  l a t e  d    C o  m m a n  d s

                                                 AT   C om  m an d                                                      D e sc r  i p t i o n

                                                AT+CEDRXS AT+KEDRXCFG                                                   Enable/disable eDRX and configure related settings


                                                AT+CEDRXRDP                                                             Display current eDRX settings

                                              For example:
                                              •                                                    Use AT+CEDRXS to configure the desired TI-eDRX value.
                                              •                                                    During the network attach or TAU process:
                                                        ·                    Module sends eDRX request with the settings (as specified in AT+CEDRXS) to the
                                                            network.
                                                        ·                    Network response indicates if the module may use eDRX and the eDRX param-
                                                            eters that should be used. The network may adjust the eDRX parameters from
                                                            those requested by the module.
                                              •                                                    If eDRX is accepted by the network, the module only needs to monitor during the
                                                        eDRX paging opportunities. The module may enter low power mode state between
                                                        the eDRX paging opportunities (depending on the module configuration).
                                              Note that:
                                              •                                                    eDRX parameters must be carefully selected to match the intended use case(s) for
                                                        the module.
                                                        ·                    Given that the module can only be paged at an eDRX paging opportunity:
                                                        ·                    Longer eDRX cycles will delay (increase the latency of) mobile terminated data
                                                            reception.
                                                        ·                    Shorter eDRX cycles will reduce the latency but will also reduce the eDRX power
                                                            savings.
                                                        ·                    Setting a cycle longer than 81.92s may not improve power saving significantly,
                                                            since the module will wake every 81.92s to do a clock calibration.
                                                        The duration of the eDRX cycle should be appropriately selected for the specific use
                                                        case.
                                              •                                                    Network-side store and forward is supported— Packets will be stored until the
                                                        module's next eDRX paging opportunity or, if the network has a storage time limit,
                                                        until that limit is reached.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               30                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 31

P o w e r  S p e c i f  i c a t  i o  n s


                                                   3.3.2.3 Concurrent PSM and eDRX

                                                   eDRX may be performed during the Active Timer (T3324) window of PSM. For example, if
                                                   PSM and eDRX are configured with the following settings:
                                                   •                                                    PSM:
                                                             ·                    T3412 (PSM Timer)— 86400s (24 hours)
                                                             ·                    T3324 (Active Timer)— 327.68s (~5.5 minutes)
                                                   •                                                    eDRX:
                                                             ·                    eDRX cycle time— 81.92s
                                                   Assuming the network does not attempt to contact the module after the module leaves the
                                                   connected state and enters PSM idle state, the module will stay in the idle state for
                                                   327.68 seconds (the Active Timer).
                                                   While in the idle state, the module will be in eDRX power saving mode for 4 cycles of
                                                   81.92 seconds each, and then go to PSM dormant state for ~23h55m until the T3412
                                                   timer expires. At that point the module wakes, sends a periodic TAU, and then the PSM
                                                   process repeats.
                                                   3.4 HL781x Low Power Modes

                                                   In addition to the 3GPP power saving features Power Saving Mode (PSM) and Extended
                                                   DRX (eDRX), the HL781x supports the low power modes in Table          3-                   5.

Ta b  l  e    3 - 5 :                     L o  w    P o w  e r    M o d  e s

                                                                                                                                                                                                             H ar  d w ar  e Wak e - U p  S i g n al
  Po w e r    M od e                       Po s s i b l e  M o d em  S t a t e                             I  m pa c t    on   M o d u l e                                                                   S ou r  ce s

  Sleep                                    Stack OFF, DRX, eDRX,                                           •                                                    26 MHz system clock is OFF                   WAKEUP
                                           PSM, No service                                                 •                                                    Application processor is idle                UART1_DTRa
                                                                                                           •                                                    Modem is out-of-coverage, sleeping,          RTC alarm event
                                                                                                                     or off
                                                                                                           •                                                    I/Os are retained

  Lite Hibernate                           Stack OFF, eDRX, PSM,                                           •                                                    26 MHz system clock is OFF                   WAKEUP
                                           No service                                                      •                                                    Application processor is OFF                 UART1_DTRa
                                                                                                           •                                                    Modem is out-of-coverage, sleeping,          RTC timeout interrupt
                                                                                                                     or off
                                                                                                           •                                                    Flash memory and most RAM is off
                                                                                                                     (some retention memory remains
                                                                                                                     on)
                                                                                                           •                                                    I/Os are retained


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               31                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 32

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    3 - 5 :                     L o  w    P o w  e r    M o d  e s     ( C o  n t i  n u  e d  )

                                                                                                                                                                                                    H ar  d w ar  e Wak e - U p  S i g n al
  Po w e r    M od e                     Po s s i b l e  M o d em  S t a t e                           I  m pa c t    on   M o d u l e                                                              S ou r  ce s

  Hibernate                              Stack OFF, eDRX, PSM,                                        •                                                    26 MHz system clock is OFF               WAKEUP
                                         No service                                                   •                                                    Application processor is OFF             RTC timeout interrupt
                                                                                                      •                                                    Modem is OFF
                                                                                                      •                                                    Flash memory and most RAM is off
                                                                                                                (some retention memory may
                                                                                                                remain on, PSM/eDRX-dependent)
                                                                                                      •                                                    I/Os are not retained (e.g. in an
                                                                                                                undefined state)

  OFF                                    Stack OFF                                                    •                                                    26 MHz system clock is OFF & RTC         WAKEUP
                                                                                                                clock is OFF
                                                                                                      •                                                    Application processor is OFF
                                                                                                      •                                                    Modem is OFF
                                                                                                      •                                                    Flash memory and RAM off
                                                                                                      •                                                    I/Os are not retained (e.g. in an
                                                                                                                undefined state)

       a.                           Only if configured with +KSLEEP <mngt> parameter set to 0

                                                 An end product uses the AT+KSLEEP command to specify the preferred lowest power
                                                 mode. Then when the module sleeps, its power management algorithm determines the
                                                 appropriate mode based on the module's current operating requirements.


                                                 Note:                When a module that is configured for PSM enters Hibernate mode, its non-persistent configu-
                                                 rations are lost (just like when it power cycles). Refer to HL78xx AT Commands Interface Guide
                                                 (Doc# 41111821), Command Timeout and Other Information to identify commands that manage
                                                 persistent configurations.


                                                 Warning:                  If USB_VBUS is powered and the USB interface is enabled, it will not be possible to
                                                 enter Lite Hibernate or Hibernate mode.

                                                 For additional low power mode details (including the relationship between 3GPP power
                                                 saving features and HL781x power modes), refer to HL78xx Low Power Modes
                                                 Application Note (Doc# 2174229). For band selection details (which impact power
                                                 consumption), refer to HL78xx Customization Guide Application Note (Doc# 2174213).


                                                 Note:                To prevent flash wear out, the module includes a feature for flash wear out protection. This
                                                 feature prevents entering Hibernate mode if less than 30 minutes passed since the last Hibernate
                                                 mode, or less than 30 minutes of Hibernate sleep is expected.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               32                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 33

P o w e r  S p e c i f  i c a t  i o  n s

                                                 3.5 Current Consumption

                                                 This section describes the HL781x module’s current consumption under various power
                                                 states/modes.
                                                 •                                                    Low Power Current Consumption Modes— Table                   3-                   6 to                     Table         3-                   9
                                                 •                                                    Connected Mode— Table         3-                   10 to Table         3-                   14

                                                 Important:                 The module’s current consumption will depend on the actual operating  /  environmental
                                                 conditions of the customer platform.
                                                 The current consumption measurements presented in this section (Table        3-6 to Table         3-14) are
                                                 typical values obtained under the following test conditions:
                                                          • Nominal supply voltage  —  3.7V, TX power  —  0        dBm
                                                          • Nominal ambient temperature  —  25°C
                                                          • PSM connect type (call box equipment setting)  —  test mode
                                                          • eDRX test conditions:
                                                                • Cat-M1 eDRX paging cycle  —  1.28         sec
                                                                • Cat-NB eDRX paging cycle  —  2.56         sec
                                                          • Conducted 50 load on RF port(s)
                                                          • External UICC  /  USIM that can be activated

                                                          • In addition, the following conditions apply to Hibernate and OFF mode measurements:
                                                                    • VGPIO is OFF
                                                                    • Customer platform ensures module I/Os are not driven >        0.2V
                                                                    • External UICC  /  USIM that is pre-configured to allow the module to automatically disable
                                                                      the USIM power.
                                                                     (See [4] HL78xx Low Power Modes Application Note (Doc#         2174229) for details.)
                                                                    • WAKEUP signal Low

                                                 For detailed low power current consumption information, refer to %%[4] HL78xx Low Power Modes
                                                 Application Note (Doc#         2174229).


                                                 Note:                To be able to enter PSM mode when the module’s lowest attainable power state is
                                                 Lite         Hibernate or Hibernate (i.e.,         +KSLEEP  <level> is 1 or 2) and LwM2M is enabled (AutoConnect
                                                 is enabled by default), the host must not de-assert the WAKEUP pin until it receives a CEREG:4
                                                 unsolicited result code.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               33                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 34

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               34                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 35

P o w e r  S p e c i f  i c a t  i o  n s


Ta b  l  e    3 - 6 :                     H L 7  8 1 0    L  P M    C u  r r e n  t    C o n  s u  m p t i  o n    -    C a t - M 1  a

  M od e m   R a d i o                                  L ow  es t    Po w e r
  St  at  e                                             M o d e                                                          De t a i l  s                                                                                                                                 Ty p                           U n i t
                                                                                                                        •                                                    Module is switched off by AT command
  OFF                                                   OFF                                                                         (+CPWROFF)                                                                                                                        2.8                            A
                                                                                                                        •                                                    Power supplies (VBAT_BB, VBAT_RF) are
                                                                                                                                    connected

                                                                                                                        TAU—Occurrence is network dependent                                                                                                           41                             Ah

                                                        Hibernate                                                                                                                                                                                                     2.8                            A
                                                                                                                        Floor current during PSM dormant
                                                        Lite Hibernate                                                                                                                                                                                                31                             A

  PSM                                                   Hibernate Cycleb                                                •                                                    T3412 = 24h                                                                              10c                            A
                                                        Lite Hibernate Cycleb                                           •                                                    T3324 = 20s                                                                              36c                            A

                                                        Hibernate Cycleb                                                •                                                    T3412 = 1h                                                                               95c                            A
                                                        Lite Hibernate Cycleb                                           •                                                    T3324 = 20s                                                                              90c                            A

                                                                                                                        Calibration—Applies to eDRX 81.92s and longer                                                                                                 3                              Ah

                                                        Hibernate                                                                                                                                                                                                     27                             A
                                                                                                                        Floor current during eDRX
                                                        Lite Hibernate                                                                                                                                                                                                29                             A

  eDRXd                                                 Hibernate Cycleb                                                •                                                    eDRX cycle (TI-eDRX) = 81.92s                                                            41e                            A
                                                        Lite Hibernate Cycleb                                           •                                                    PTW and DRX = 1.28s                                                                      42e                            A

                                                        Hibernate Cycleb                                                •                                                    eDRX cycle (TI-eDRX) = 20.48s                                                            90e                            A
                                                        Lite Hibernate Cycleb                                           •                                                    PTW and DRX = 1.28s                                                                      93e                            A

                                                        Sleep                                                                                                                                                                                                         3                              mA
                                                                                                                        1.28s
                                                        Hibernate                                                                                                                                                                                                     2                              mA

  DRX                                                   Sleep                                                                                                                                                                                                         2.5                            mA
                                                                                                                        2.56s
                                                        Hibernate                                                                                                                                                                                                     1.3                            mA

                                                        Running                                                         DRX independent, +KSLEEP=2 or Wake active                                                                                                     40                             mA

  Standby                                                                                                               Module registered, Idle mode, without TX                                                                                                      15                             mA
                                                                                                                        power  /  data transfer

         a.                           Values measured under following conditions:
                  - Good channel conditions (SINR > 5 dB)
                  - Static scenario
         b.                           Cycle (Lite Hibernate or Hibernate) includes boot, cell acquisition, network attach, wait for timer expiry, and back to Sleep
         c.                             Values are T3324-dependent.
         d.                           See Extended DRX (eDRX) for details.
         e.                           Values are PTW and DRX-dependent. Values will have some difference due to a number of active cycles being sampled.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               35                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 36

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    3 - 7 :                     H L 7  8 1 2    L  P M    C u  r r e n  t    C o n  s u  m p t i  o n    -    C a t - M 1  a

  M od e m   R a d i o                               L o w e st    P ow e r
  St  at  e                                          M o d e                                                            D et  ai  l s                                                                                                                                 Typ                          U n i t
                                                                                                                       •                                                    Module is switched off by AT command and
  OFF                                                OFF                                                                           VBATs are connected                                                                                                               1.8                           A
                                                                                                                       •                                                    Power supplies (VBAT_BB, VBAT_RF) are
                                                                                                                                   connected

                                                                                                                       TAU—Occurrence is network dependent                                                                                                           43                            Ah

                                                     Hibernate                                                                                                                                                                                                       1.8                           A
                                                                                                                       Floor current during PSM dormant
                                                     Lite Hibernate                                                                                                                                                                                                  30                            A

  PSM                                                Hibernate Cycleb                                                  •                                                    T3412 = 24h                                                                              8c                            A
                                                     Lite Hibernate Cycleb                                             •                                                    T3324 = 20s                                                                              33c                           A

                                                     Hibernate Cycleb                                                  •                                                    T3412 = 1h                                                                               85c                           A
                                                     Lite Hibernate Cycleb                                             •                                                    T3324 = 20s                                                                              90c                           A

                                                                                                                       Calibration—Applies to eDRX 81.92s and longer                                                                                                 3                             Ah

                                                     Hibernate                                                                                                                                                                                                       30                            A
                                                                                                                       Floor current during eDRX
                                                     Lite Hibernate                                                                                                                                                                                                  32                            A

  eDRXd                                              Hibernate Cycleb                                                  •                                                    eDRX cycle (TI-eDRX) = 81.92s                                                            50e                           A
                                                     Lite Hibernate Cycleb                                             •                                                    PTW and DRX = 1.28s                                                                      52e                           A

                                                     Hibernate Cycleb                                                  •                                                    eDRX cycle (TI-eDRX) = 20.48s                                                            100e                          A
                                                     Lite Hibernate Cycleb                                             •                                                    PTW and DRX = 1.28s                                                                      110e                          A

                                                     Sleep                                                                                                                                                                                                           3                             mA
                                                                                                                       1.28s
                                                     Hibernate                                                                                                                                                                                                       2                             mA

  DRX                                                Sleep                                                                                                                                                                                                           2.5                           mA
                                                                                                                       2.56s
                                                     Hibernate                                                                                                                                                                                                       1.3                           mA

                                                     Running                                                           DRX independent, +KSLEEP=2 or Wake active                                                                                                     40                            mA

  Standby                                                                                                              Module registered, Idle mode, without TX                                                                                                      15                            mA
                                                                                                                       power   /  data transfer

         a.                           Values measured under following conditions:
                  - Good channel conditions (SINR > 5 dB)
                  - Static scenario
         b.                           Cycle (Lite Hibernate or Hibernate) includes boot, cell acquisition, network attach, wait for timer expiry, and back to Sleep
         c.                             Values are T3324-dependent.
         d.                           See Extended DRX (eDRX) for details.
         e.                           Values are PTW and DRX-dependent. Values will have some difference due to a number of active cycles being sampled.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               36                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 37

P o w e r  S p e c i f  i c a t  i o  n s


Ta b  l  e    3 - 8 :                     H L 7  8 1 0    L  P M    C u  r r e n  t    C o n  s u  m p t i  o n    -    N B  a

  M od e m   R a d i o
  St  at  e                                         L o w es t    P ow e r    M o de                                    De t a i l  s                                                                                                                               Typ                          U n i t
                                                                                                                       •                                                    Module is switched off by AT command.
  OFF                                               OFF                                                                •                                                    Power supplies (VBAT_BB, VBAT_RF) are                                                   2.8                          A
                                                                                                                                   connected.

                                                                                                                       TAU—Occurrence is network dependent                                                                                                          43                           Ah

                                                    Hibernate                                                                                                                                                                                                       2.8                          A
                                                                                                                       Floor current during PSM dormant
                                                    Lite Hibernate                                                                                                                                                                                                  31                           A

  PSM                                               Hibernate Cycleb                                                   •                                                    T3412 = 24h                                                                             12c                          A
                                                    Lite Hibernate Cycleb                                              •                                                    T3324 = 20s                                                                             33c                          A

                                                    Hibernate Cycleb                                                   •                                                    T3412 = 1h                                                                              120c                         A
                                                    Lite Hibernate Cycleb                                              •                                                    T3324 = 20s                                                                             115c                         A

                                                                                                                       Calibration—Applies to eDRX 81.92s and longer                                                                                                4                            Ah

                                                    Hibernate                                                                                                                                                                                                       28                           A
                                                                                                                       Floor current during eDRX
                                                    Lite Hibernate                                                                                                                                                                                                  32                           A

  eDRXd                                             Hibernate Cycleb                                                   •                                                    eDRX cycle (TI-eDRX) = 81.92s                                                           70e                          A
                                                    Lite Hibernate Cycleb                                              •                                                    PTW and DRX = 2.56s                                                                     72e                          A

                                                    Hibernate Cycleb                                                   •                                                    eDRX cycle (TI-eDRX) = 20.48s                                                           210e                         A
                                                    Lite Hibernate Cycleb                                              •                                                    PTW and DRX = 2.56s                                                                     212e                         A

                                                    Sleep                                                                                                                                                                                                           4.5                          mA
                                                                                                                       1.28s
                                                    Hibernate                                                                                                                                                                                                       3.8                          mA

                                                    Sleep                                                                                                                                                                                                           3.5                          mA
                                                                                                                       2.56s
  DRX                                               Hibernate                                                                                                                                                                                                       2.3                          mA

                                                    Sleep                                                                                                                                                                                                           2.5                          mA
                                                                                                                       10.24s
                                                    Hibernate                                                                                                                                                                                                       1                            mA

                                                    Running                                                            DRX independent, +KSLEEP=2 or Wake active                                                                                                    45                           mA

  Standby                                                                                                              Module registered, Idle mode, without TX                                                                                                     15                           mA
                                                                                                                       power  /  data transfer

         a.                           Values measured under following conditions:
                  - Good channel conditions (SINR > 5 dB)
                  - Static scenario
         b.                           Cycle (Lite Hibernate or Hibernate) includes boot, cell acquisition, network attach, wait for timer expiry, and back to Sleep
         c.                             Values are T3324-dependent.
         d.                           See Extended DRX (eDRX) for details.
         e.                           Values are PTW and DRX-dependent. Values will have some difference due to a number of active cycles being sampled.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               37                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 38

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    3 - 9 :                     H L 7  8 1 2    L  P M    C u  r r e n  t    C o n  s u  m p t i  o n    -    C a t - N  B a

  M od e m   R a d i o
  St  at  e                                        L o w es t    P ow e r    M o de                                   De t a i l  s                                                                                                                             Typ                          U n i t

  OFF                                              OFF                                                               Module is switched off by AT command and VBATs                                                                                             1.8                         A
                                                                                                                     are connected

                                                   Hibernate                                                                                                                                                                                                    1.8                         A
                                                                                                                     Floor current during PSM dormant
                                                   Lite Hibernate                                                                                                                                                                                               30                          A

                                                   Hibernate Cycleb                                                  •                                                    T3412 = 24h                                                                           8c                          A
  PSM                                                                                                                •                                                    T3324 = 20s
                                                   Lite Hibernate Cycleb                                                                                                                                                                                        33c                         A

                                                   Hibernate Cycleb                                                  •                                                    T3412 = 1h                                                                            117c                        A
                                                   Lite Hibernate Cycleb                                             •                                                    T3324 = 20s                                                                           120c                        A

                                                                                                                     TAU—Occurrence is network dependent                                                                                                        48                          Ah

                                                                                                                     Calibration—Applies to eDRX 81.92s and longer                                                                                              4                           Ah

                                                   Hibernate                                                                                                                                                                                                    30                          A
                                                                                                                     Floor current during eDRX
                                                   Lite Hibernate                                                                                                                                                                                               33                          A
  eDRXd
                                                   Hibernate Cycleb                                                  •                                                    eDRX cycle (TI-eDRX) = 81.92s                                                         75e                         A
                                                   Lite Hibernate Cycleb                                             •                                                    PTW and DRX = 2.56s                                                                   80e                         A

                                                   Hibernate Cycleb                                                  •                                                    eDRX cycle (TI-eDRX) = 20.48s                                                         220e                        A
                                                   Lite Hibernate Cycleb                                             •                                                    PTW and DRX = 2.56s                                                                   227e                        A

                                                   Sleep                                                                                                                                                                                                        4.2                         mA
                                                                                                                     1.28s
                                                   Hibernate                                                                                                                                                                                                    3.5                         mA

                                                   Sleep                                                                                                                                                                                                        3.2                         mA
                                                                                                                     2.56s
  DRX                                              Hibernate                                                                                                                                                                                                    2                           mA

                                                   Sleep                                                                                                                                                                                                        2.2                         mA
                                                                                                                     10.24s
                                                   Hibernate                                                                                                                                                                                                    0.6                         mA

                                                   Running                                                           DRX independent, +KSLEEP=2 or Wake active                                                                                                  45                          mA

  Standby                                                                                                            Module registered, Idle mode, without TX                                                                                                   15                          mA
                                                                                                                     power  /  data transfer

         a.                           Values measured under following conditions:
                  - Good channel conditions (SINR > 5 dB)
                  - Static scenario
         b.                           Cycle (Lite Hibernate or Hibernate) includes boot, cell acquisition, network attach, wait for timer expiry, and back to Sleep
         c.                             Values are T3324-dependent.
         d.                           See Extended DRX (eDRX) for details.
         e.                           Values are PTW and DRX-dependent. Values will have some difference due to a number of active cycles being sampled.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               38                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 39

P o w e r  S p e c i f  i c a t  i o  n s


Ta b  l  e    3 - 1 0 :                     H L  7 8 1 0    C u  r r  e n t    C o  n s u  m p  t i o  n    -    LT E    C a t - M 1    C o  n n  e c t e d     M o  d e

  Pa r  am e t e r                                                                                            B a n d                                                O ut  pu t    P ow e r                            Avg .    C ur  r e n t   (  Typ i c a l   Va l u e s) a

  LTE Cat-M1                                                                                                 1, 2, 3, 4, 5, 8, 12,                                  23 dBm                                             200-240 mA
  •                                                    Modem State: Connected                                13, 18, 19, 20, 25,                                    0 dBm                                              120-190 mA
  •                                                    4RB DL at MCS 14 1RB_UL at                            26, 28, 66, 85
              MCS 15
  •                                                    Maximum 3 UL sub-frames and 3
              DL sub-frames every 10 ms
  •                                                    Transferring UDP payload data
              rates: concurrent 280 kbps DL + 45
              kbps UL

         a.                           Ranges reflect variations between band/channel combinations


Ta b  l  e    3 - 11 :                     H L 7  8 1 2    C u  r r e  n t    C o n  s u  m p t i  o n     —    LT  E   C  a t - M 1   C  o n  n e c  t e d    M o d  e

  Pa r  am e t e r                                                                                         Ba n d                                                    O ut  pu t    P ow e r                           Av g.    C u r r e n t    ( Ty pi  ca l    Va l u e s) a

  LTE Cat-M1                                                                                              1, 2, 3, 4, 5, 8, 12,                                     23 dBm                                           170-230 mA
  •                                                    Modem State: Connected                             13, 18, 19, 20, 25,                                       0 dBm                                            120-140 mA
  •                                                    4RB DL at MCS 14 1RB_UL at                         26, 28, 66, 85
              MCS 15
  •                                                    Maximum 3 UL sub-frames and 3
              DL sub-frames every 10 ms
  •                                                    Transferring UDP payload data
              rates: concurrent 280 kbps DL +
              45 kbps UL

         a.                           Ranges reflect variations between band/channel combinations


Ta b  l  e    3 - 1 2 :                     H L  7 8 1 0    C u  r r  e n t    C o  n s u  m p  t i o  n    -    LT E    N B - 1    C o  n n  e c t e  d    M o d  e

  Pa r  am e t e r                                                                                         Ba n d                                                    O ut  pu t    P ow e r                              Av g.    C u r r  en t    ( Ty pi  ca l    Va l u e s )

  NB1 DL peak throughput (27.2kbps)                                                                       1, 2, 3, 4, 5, 8, 12,                                     23 dBm                                           110-140 mA
  UL Subcarrier spacing: 15KHz                                                                            13, 18, 19, 20, 25,                                       0 dBm                                            90-125 mA
                                                                                                          26, 28, 66, 85
  Subcarriers downlink: 12 MCS.TBS:13
  MCS.TBS:13

  NB1 UL peak throughput (62.5kbps)                                                                                                                                 23 dBm                                           120-150 mA
  UL Subcarrier spacing: 15KHz                                                                                                                                      0 dBm                                            100-130 mA

  Subcarriers uplink:3 MCS.TBS:13


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               39                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 40

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    3 - 1 3 :                     H L  7 8 1 0    C u  r r  e n t    C o  n s u  m p  t i o  n    -    LT E    N B - 2    C o  n n  e c t e  d    M o d  e

  Pa r  am e t e r                                                                              Ba n d                                              O ut  pu t    P ow e r                         Av g.    C u r r  en t    ( Ty pi  ca l    Va l u e s )

  NB2 DL peak throughput (134kbps)                                                             1, 2, 3, 4, 5, 8, 12,                                23 dBm                                      150-220 mA
  UL Subcarrier spacing: 15KHz                                                                 13, 18, 19, 20, 25,                                  0 dBm                                       100-170 mA
                                                                                               26, 28, 66, 85
  Subcarriers downlink: 12 MCS.TBS:13
  MCS.TBS:13

  NB2 UL peak throughput (109kbps)                                                                                                                  23 dBm                                      300-360 mA
  UL Subcarrier spacing: 15KHz                                                                                                                      0 dBm                                       150-310 mA

  Subcarriers uplink:3 MCS.TBS:13


Ta b  l  e    3 - 1 4 :                     H L  7 8 1 2    C u  r r  e n t    C o  n s u  m p  t i o  n    -    LT E    N B - 1    C o  n n  e c t e  d    M o d  e

  Pa r  am e t e r                                                                              Ba n d                                              O ut  pu t    P ow e r                      Av g .   C ur  r e n t   ( Ty p i c al    Va l u e s)  a , b

  NB1 DL peak throughput (27.2kbps)                                                            1, 2, 3, 4, 5, 8, 12,                                23 dBm                                      100-110 mA
  UL Subcarrier spacing: 15KHz                                                                 13, 18, 19, 20, 25,
                                                                                               26, 28, 66, 85                                       0 dBm                                       90-100 mA

  Subcarriers downlink: 12
  MCS.TBS:13

  NB1 UL peak throughput (45.7 kbps)                                                                                                                23 dBm                                      100-120 mA
  UL Subcarrier spacing: 15KHz                                                                                                                      0 dBm                                       80-90 mA

  Subcarriers uplink: 12
  MCS.TBS:13

        a.                           Typical average current values for 1 time slot.
        b.                           Measured at 3.7V, 25°C.


Ta b  l  e    3 - 1 5 :                     H L  7 8 1 2    C u  r r  e n t    C o  n s u  m p  t i o  n    -    LT E    N B - 2    C o  n n  e c t e  d    M o d  e

  Pa r  am e t e r                                                                              Ba n d                                              O ut  pu t    P ow e r                         Av g.    C u r r  en t    ( Ty pi  ca l    Va l u e s )

  NB2 DL peak throughput (134 kbps)                                                            1, 2, 3, 4, 5, 8, 12,                                23 dBm                                      160-190 mA
  UL Subcarrier spacing: 15KHz                                                                 13, 18, 19, 20, 25,                                  0 dBm                                       120-130 mA
                                                                                               26, 28, 66, 85
  Subcarriers downlink: 12 MCS.TBS:13

  NB2 UL peak throughput (159 kbps)                                                                                                                 23 dBm                                      220-285 mA
  UL Subcarrier spacing: 15KHz                                                                                                                      0 dBm                                       150-170 mA

  Subcarriers uplink: 12 MCS.TBS:13


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               40                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 41

P o w e r  S p e c i f  i c a t  i o  n s


Ta b  l  e    3 - 1 6 :                     H L  7 8 1 2    Ty p  i c a  l    C u  r r e n  t    C o n  s u  m p t i  o n     -   2 G     C o n  n e  c t e d    M o d  e

     Pa r  am e t e r                                                                                Ba n d                                                                                                                        O u t p u t    Po w e r                                                                                                                                                       Av g .   C u r r  e nt    ( Ty p i c al    Va l u es )

    PCL5                                                                                             850/900 MHz                                                                                                                  32.5 dBm                                                                                                                                                   290 mA

    PCL19                                                                                                                                                                                                                         5 dBm                                                                                                                                                      130 mA

    PCL0                                                                                             1800/1900 MHz                                                                                                                29.5 dBm                                                                                                                                                   220 mA

    PCL15                                                                                                                                                                                                                         0 dBm                                                                                                                                                      120 mA


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               41                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 42

4: Detailed Interface Specifications                                                                                                                                                                                                                                                                                4


                                                     This chapter describes the interfaces supported by the Sierra Wireless HL781x and
                                                     provides specific voltage, timing, and circuit recommendations for those interfaces, as
                                                     appropriate
                                                     4.1 VGPIO

                                                     The VGPIO (GPIO voltage output) 1.8 V supply state is:
                                                     •                                                    ON (available)— Voltage output is high when module is in Active, Sleep, or Lite
                                                                Hibernate mode
                                                     •                                                    OFF (not available)— Voltage output is low when module is in OFF, Reset, or
                                                                Hibernate mode
                                                     VGPIO can be used to:
                                                     •                                                    Pull-up signals such as I/Os. For additional details, see I/O Behavior in Hibernate
                                                                Mode.
                                                     •                                                    Supply LED drivers
                                                     •                                                    Indicate the module power state
                                                     •                                                    Control buffering of module I/O (required in Hibernate)
                                                     Table          4-                   1 and Table                                       4-                   2 describe the VGPIO supply.


                                                     Ta b  l  e    4 - 1 :                     V G P I  O    P i n     D e s c r  i p  t i o  n

                                                        Pa d   #             S i g n al    N am  e                                                                     I / O  a         D es c r i p t  i o n

                                                       C45                   VGPIO                                                                                     PO               GPIO voltage supply

                                                              a.                           Signal direction with respect to the module

                                                     Refer to the following table for the electrical characteristics of the VGPIO supply.

                                                     Ta b  l  e    4 - 2 :                     V G P I  O    E l e  c t r i  c a l     C h a r  a c t e r  i s  t i c s

                                                        Pa r  am e t e r                                                                   M i n                   Typ                     M ax                     U n i t              R e m a r k s

                                                       Voltage level                                                                       1.75                   1.8                     1.85                      V                    Applies to Active,
                                                                                                                                                                                                                                         Sleep, and Lite
                                                                                                                                                                                                                                         Hibernate modes

                                                       Current                                   Active, Sleep                             –                      –                       25                        mA                   Total current supplied
                                                       capability                                                                                                                                                                        by VGPIO should not
                                                                                                                                                                                                                                         exceed 25 mA.

                                                                                                 Lite Hibernate                            –                      –                       1                         mA

                                                       Output capacitance                                                                  –                      –                       1                         F                   External decoupling
                                                                                                                                                                                                                                         capacitance should not
                                                                                                                                                                                                                                         exceed 1 F.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               42                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 43

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s

                                          4.1.1 I/O Behavior in Hibernate Mode

                                          The following behaviors apply, only in Hibernate mode, to I/Os that are referenced to
                                          VGPIO (i.e. UART, GPIO, Clock, UIM1, Indication, and ADC signal groups— see
                                          Table          2-                   2); they do not apply in Lite Hibernate or Sleep modes.
                                          •                                                    VGPIO is OFF (voltage output is low)


                                          Note:                The host platform should isolate these signals during module Hibernate mode to prevent
                                          back-powering the module. For details, see Hibernate—Isolation Requirements.

                                          •                                                    No I/O should be biased as no internal source exists. The maximum allowed voltage
                                                  is ±0.2V at any I/O.
                                          •                                                    All I/Os that are referenced to VGPIO will be in an undefined s tate.
                                          The host should ignore all activity on these signals until the module has initialized and
                                          reached AT-READY state (i.e. when UART1_CTS transitions from high to low (and stays
                                          low) and VGPIO is high). For timing details, see Unmanaged POWER_ON_N (Default)
                                          and Wakeup from Low Power Modes.
                                          4.2 USIM Interface

                                          The HL781x implements a USIM interface that can be used to control either:
                                          •                                                    the module’s eSIM (internal, embedded SIM—optional and SKU-dependent)
                                                  or
                                          •                                                    an external 1.8V USIM (UIM1); 3V USIM is not supported
                                          To associate USIM1 with the eSIM or external USIM, use the AT+KSIMSEL command.
                                          For details, refer to HL78xx AT Commands Interface Guide (Doc# 41111821).

                                          4.2.1 eSIM Interface

                                          eSIM is an internal interface supporting Sierra Smart Connectivity. For details about using
                                          the HL781x’s  eSIM with Sierra Smart Connectivity, refer to [6] Sierra Wireless Ready-to-
                                          Connect Module Integration Guide (Doc# 41113385). For additional information on Sierra
                                          Smart Connectivity, explore www.sierrawireless.com or contact Sierra Wireless.

                                          4.2.2 External UIM1 Interface

                                          The USIM1 interface is fully compliant with GSM 11.11 recommendations concerning
                                          USIM functions.
                                          Table          4-                   3 describes the USIM1 interface.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               43                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 44

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    4 - 3 :                     U I  M 1    P i  n    D e s  c r i  p t i  o n

  Pa d   #                              S i  gn a l   N a m e                              I  / O a                                     D e s cr  i p t i  on                                          I / O    Typ e

  C26                                   UIM1_VCC                                          PO                                           USIM1 Power supply                                              1.8V (VGPIO)

  C27                                   UIM1_CLK                                          O                                            USIM1 Clock                                                     1.8V (VGPIO)

  C28                                   UIM1_DATA                                         I/O                                          USIM1 Data                                                      1.8V (VGPIO)

  C29                                   UIM1_RESET                                        O                                            USIM1 Reset                                                     1.8V (VGPIO)

  C64                                   UIM1_DETb                                         I                                            USIM1 Detection                                                 1.8V (VGPIO)

        a.                           Signal direction with respect to the module
        b.                           Buffer is required if UIM_DET1 is powered from host; not required if powered from VGPIO. UIM1_DET can be used as GPIO3
                if external SIM is not required.


                                                  Note:                UIM1_VCC max output current is 50 mA in Active and Sleep modes, 1 mA in Lite Hibernate,
                                                  and Off in Hibernate. For UIM1 electrical interface details, see UIM1        .

                                                  4.2.3 UIM1_DET

                                                  UIM1_DET is used to detect the insertion or removal of a USIM in the USIM socket
                                                  connected to the main USIM interface (UIM1).
                                                  When a USIM is:
                                                  •                                                    Inserted— UIM1_DET is HIGH.
                                                  •                                                    Removed— UIM1_DET is LOW.


                                                  Note:                In Hibernate mode, UIM1_DET is in an undefined state.

                                                   To enable or disable the USIM detect feature, use the AT+KSIMDET command. For
                                                  details, refer to HL78xx AT Commands Interface Guide.

                                                  4.3 USB Interface

                                                  The HL781x provides a full speed USB 2.0 interface that conforms to the Universal Serial
                                                  Bus Specification, Revision 2.0.
                                                  Table          4-                   4 and Table                                       4-                   5 describe the USB interface.

Ta b  l  e    4 - 4 :                     U S B    P i  n    D e  s c r i  p  t i o  n

  Pa d   #                                                             S i g n al    N a m e                                               I /  O a                                                             D e sc r  i p t i o n

  C12                                                                  USB_D-                                                              I/O                                                                  USB Data Negative

  C13                                                                  USB_D+                                                              I/O                                                                  USB Data Positive

  C16                                                                  USB_VBUS                                                            PI                                                                   USB VBUS

        a.                           Signal direction with respect to the module


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               44                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 45

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s


Ta b  l  e    4 - 5 :                     U S B    E l  e c  t r i  c a l    C h  a r  a c t e r i  s t i  c s

  Pa r  am e t e r                                                                              M i n                                Ty p                                 M a x                               U ni  t

 Voltage at pins USB_D+ / USB_D-                                                                3.15                                 3.3                                 3.45                                 V

 USB_VBUS                                                                                       4.75                                 5.0                                 5.25                                 V


                                            Important:                 For USB operation, USB_VBUS is a mandatory connection. The host must ensure
                                            USB_VBUS is provided before establishing USB communication.
                                            When USB operation is enabled, the lowest power mode supported is Active—the module cannot
                                            enter Low Power state.
                                            When USB operation is disabled, the lowest power mode supported is Hibernate.

                                            For USB enumeration timing, refer to Unmanaged POWER_ON_N (Default) and Wakeup
                                            from OFF Mode.
                                            Simultaneous UART and USB is supported by default, but can be affected by the
                                            +KUSBCOMP command. For details, refer to HL78xx AT Commands Interface Guide.
                                            4.4 General Purpose Input/Output (GPIO)

                                            The HL781x provides several GPIOs, some of which are multiplexed with other signals,
                                            as described in Table         4-                   6. For electrical specifications, see                     Table          3-                   3.

Ta b  l  e    4 - 6 :                     G P I  O    P i n     D e s c  r i p  t i  o n  s

  Pa d   #                            S i g na l    N am e                            A l t  er  na t  e   Fu n ct  i o n                     D ef  au l  t   S t a t e  a             I  / O   Ty p e

 C1                                   GPIO1                                           -                                                       Input Pull-down                          1.8V (VGPIO)

 C10                                  GPIO2                                           Alternative default Ring                                Input Pull-down                          1.8V (VGPIO)
                                                                                      Indicator (Active High
                                                                                      Output)

 C40                                  GPIO7                                           -                                                       Input Pull-down                          1.8V (VGPIO)

 C41                                  GPIO8                                           VBAT_PA_EN (Output)                                     Input Pull-down                          1.8V (VGPIO)

 C46                                  GPIO6                                           -                                                       Input Pull-down                          1.8V (VGPIO)

 C51                                  GPIO14                                          UART3_CTS (Output)                                      Input Pull-down                          1.8V (VGPIO)

 C52                                  GPIO10                                          UART3_TX (Input)                                        Input Pull-down                          1.8V (VGPIO)

 C53                                  GPIO11                                          UART3_RTS (Input)                                       Input Pull-down                          1.8V (VGPIO)

 C54                                  GPIO15                                          UART3_RX (Output)                                       Input Pull-down                          1.8V (VGPIO)

 C64                                  GPIO3                                           UIM1_DET (Input)                                        Input Pull-down                          1.8V (VGPIO)

 C65                                  GPIO4                                           -                                                       Input Pull-down                          1.8V (VGPIO)

 C66                                  GPIO5                                           -                                                       Input Pull-down                          1.8V (VGPIO)


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               45                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 46

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


      a.                           Default state is software-controlled when module has initialized and reached AT-READY state. Default state is configurable by
             customer using AT+KGIOCFG command. For details, refer to HL78xx AT Commands Interface Guide (Doc# 41111821).

                                         Table          4-                   6 notes the default state for each signal.
                                         By default, at power up, all GPIOs are configured as inputs. During power up, power
                                         down, reset and Hibernate, the signals are in an undefined state. Therefore, the host
                                         should ignore all activity on I/Os until the module has reached AT-READY state (i.e. when
                                         UART1_CTS transitions from high to low (and stays low) and VGPIO is high). For timing
                                         details, see Unmanaged POWER_ON_N (Default) and Wake Up Signal (WAKEUP)          .
                                         4.5 Main Serial Link (UART1)

                                         The HL781x implements the UART1 serial interface (up to 921.6 kbps, default rate of
                                         115.2 kbps) for communication between the module and a PC or host processor. UART1
                                         consists of a flexible, 8-wire asynchronous serial, 1.8V interface that complies with RS-
                                         232 interface. UART1 can also be used to upgrade the module firmware locally.
                                         Simultaneous UART and USB is supported by default, but can be affected by the
                                         +KUSBCOMP command. For details, refer to HL78xx AT Commands Interface Guide.


                                         Note:                The host platform may use UART1 as an 8-wire, 4-wire, or 2-wire interface as shown in
                                         Figure         4-1, Figure        4-2, and Figure         4-3.

                                         Note that in Hibernate mode the host platform (MCU) interfaces can remain powered— it
                                         is important that the host interfaces do not back-power the module.
                                         The UART1 interface is not active during Hibernate mode, so the host should ignore all
                                         activity on UART1 during Hibernate. If the module will enter Hibernate mode, Sierra
                                         Wireless recommends adding buffer circuits to ensure UART signals are not driven high
                                         (i.e. >0.2V).
                                         Note that a buffer is not required in Lite Hibernate mode. For detailed information, refer to
                                         I/O Behavior in Hibernate Mode.
                                         Table          4-                   7 describes the UART1 interface.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               46                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 47

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s


Ta b  l  e    4 - 7 :                     U A R T  1    P i n    D  e s c r  i p  t i o  n

  Pa d   #                       S i g n al    N a m e a                  D e f a u l t    S t a t e b , c                  A ct  i v e            I  / O   Ty p e                         D e sc r i  pt  i o n

  C2                             UART1_RI                                 Output                                            L                      1.8V (VGPIO)                           Ring Indicator
                                                                                                                                                                                          Data reception, SMS, etc.

  C3                             UART1_RTS                                Input with pull-down                              L                      1.8V (VGPIO)                           Request To Send

  C4                             UART1_CTS                                Output                                            L                      1.8V (VGPIO)                           Clear To Sendd
                                                                                                                                                                                          The module is ready to receive
                                                                                                                                                                                          AT commands.

  C5                             UART1_TX                                 Input with pull-down                              -                      1.8V (VGPIO)                           Transmit data

  C6                             UART1_RX                                 Output                                            -                      1.8V (VGPIO)                           Receive data

  C7                             UART1_DTR                                Input with pull-up                                L                      1.8V (VGPIO)                           Data Terminal Readye

  C8                             UART1_DCD                                Output                                            L                      1.8V (VGPIO)                           Data Carrier Detect
                                                                                                                                                                                          Signal data connection in
                                                                                                                                                                                          progress

  C9                             UART1_DSR                                Output                                            L                      1.8V (VGPIO)                           Data Set Ready
                                                                                                                                                                                          Signal UART interface is ON

       a.                           Signals are named with respect to the host device (i.e. DTE (Data Terminal Equipment) convention—PC view). For example,
               UART1_RX is the signal used by the host to receive data from the module.
       b.                           Signal direction with respect to the module. For example, UART1_RX is an output from the module to the host.
       c.                             Default state is software-controlled when module has initialized and reached AT-READY state.
       d.                           Host can monitor UART1_CTS and VGPIO to determine when the module is ready to receive AT commands (AT-READY). The
               UART1 inter- face is not active during Hibernate mode, so the host should ignore all activity on UART1_CTS during Hibernate.
       e.                           UART1_DTR has software-controlled pull-up (PU) (if enabled by using AT+KSLEEP with the <mngt> parameter set to 0),
               which is active only when module has initialized and reached AT-READY state. When the signal is low, the module wakes in all
               operational modes except Hibernate. When the signal is high, the module can enter sleep mode or lite hibernate mode but not
               hibernate mode.


                                               Note:                If possible, it is highly recommended to add 0 on every line on the host platform to help the
                                               debug process. This will force the UART signal layout to the top PCB layer and allow access to the
                                               signal on the resistors.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               47                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 48

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n

                                        4.5.1 Ring Indicator (UART1_RI or Alternative)

                                        UART1_RI is an active-low output signal that indicates incoming events (e.g. SMS, data
                                        reception, etc.).
                                        The signal is available in all power modes except Hibernate mode. In Hibernate mode, the
                                        UART_RI signal is in an undefined state.
                                        Therefore, if a customer platform requires a RI signal to wake its host processor on SMS
                                        or IP reception, an alternative signal must be used.
                                        The AT+KRIC command can configure GPIO2 (by default) as an inverted RI signal
                                        (RI_inverse_gpio). (For details, refer to HL78xx AT Commands Interface Guide (Doc#
                                        41111821) and HL78xx Low Power Modes Application Note (Doc# 2174229)).


                                        Note:                Because GPIO2 is in an undefined state while in (and exiting) Hibernate, use the following
                                        recommendations when GPIO2 is used as an RI signal: If firmware is used, enable the internal PD
                                        on GPIO2 using AT+KRIC (default state is No Pull).

                                        4.5.2 UART1_RTS/UART1_CTS

                                        UART1_RTS (Request to Send) is an active-low input signal used for module flow control
                                        (in combination with UART1_CTS).
                                        By default, the UART1_RTS signal state is software-controlled as pull-down, and the host
                                        platform must drive this signal. The signal can be configured as a pull-up using the
                                        AT+K HWIOCFG command (minimum firmware version 4.6.8)— for details, refer to
                                        HL78xx AT Commands Interface Guide (Doc# 41111821)
                                        For detailed UART1 flow control information (including use of UART1_RTS and
                                        UART1_CTS), refer to HL78xx Low Power Modes Application Note (Doc# 2174229)).

                                        4.5.3 UART Application Examples


                                                       HL781x                                                                                                                                Customer
                                                                                                                                                                                              Platform
                                                                UART1_RX                                          TP                                     R                           RXD

                                                               UART1_CTS                                          TP                                     R                           CTS

                                                               UART1_DSR                                          TP                                     R                           DSR

                                                               UART1_DCD                                          TP                                     R                           DCD
                                                                                           Buffer
                                                                UART1_RI                   circuit                TP                                     R                            RI

                                                               UART1_DTR                                          TP                                     R
                                                                                                                                                                                      DTR
                                                                UART1_TX                                          TP                                     R                            TXD

                                                               UART1_RTS                                          TP                                     R                            RTS


                                             No te:  R i s a  0Ω  r esist or (  def ault  va lue)

                                        Figure 4-1:                       8-wire UART Application Example


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               48                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 49

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s


                                                                     HL781x                                                                                                                                                                    Customer
                                                                                                                                                                                                                                                Platform
                                                                                 UART1_RX                                                    TP                                                  R                                   RXD

                                                                                UART1_CTS                                                    TP                                                  R                                   CTS
                                                                                                                   Buffer                    TP
                                                                                  UART1_TX                         circuit                                                                       R                                   TX D
                                                                                UART1_RTS                                                    TP                                                  R                                   RTS


                                                         No te:  R i s a  0Ω  r esist or (  def ault  va lue)

                                                   Figure 4-2:                       4-wire UART Application Example


                                                                     HL781x                                                                                                                                                                    Customer
                                                                                                                                                                                                                                                Platform
                                                                               UART1_RX                                                               TP                                         R                                   RXD
                                                                                                                         Buffer                       TP
                                                                               UART1_TX                                  circuit                                                                 R                                   TX D


                                                         No te:  R i s a  0Ω  r esist or (  def ault  va lue)

                                                   Figure 4-3:                       2-wire UART Application Example


                                                   Note:                All UART signals operate at 1.8V. A voltage level shifter is required when connecting to a 3V3
                                                   domain.

                                                   4.6 Power On Signal (POWER_ON_N)

                                                   The POWER_ON_N hardware control signal can be used by the host platform to turn the
                                                   module on.
                                                   The signal is internally biased high by default. Bias voltage is dependent on the module
                                                   mode— 1.3–1.4V in Active or Sleep mode, and 1.1–1.2V in Hibernate or Lite Hibernate
                                                   mode.
                                                   The module has two possible operational modes— Host-managed and unmanaged:
                                                   •                                                    Unmanaged (default configuration)— The module starts regardless of the
                                                             POWER_ON_N state. In this mode, the POWER_ON_N signal must be left open.


                                                   Note:                If RESET_IN_N is low, the module will not start until RESET_IN_N is released.

                                                   •                                                    Host-Managed— A low-level pulse must be provided by the host to switch the module
                                                             ON. Use an open drain/open collector type circuit to drive the signal low (< 0.3V (Input
                                                             Voltage-Low (V))).
                                                   Table          4-                   8 and Table                                       4-                   9 describe the POWER_ON_N signal.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               49                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 50

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


                                                                    Ta b  l  e    4 - 8 :                     P O W  E R  _ O N _  N    P i  n    D e  s c r i  p  ti  o  n

                                                                        Pa d   #                                      S i g n al    N a m e                                                                      I / O  a                            D es c r i p t  i o n

                                                                       C59                                           POWER_ON_Nb                                                                                 I                                   Powers the module ON

                                                                               a.                           Signal direction with respect to the module
                                                                               b.                           Signal provided by host. Does not need to be buffered, and can be directly connected to module
                                                                                          using an open drain/collector type circuit.

                                                                    Ta b  l  e    4 - 9 :                     P O W  E R  _ O N _  N    E l  e c t r  i c  a l    C h  a r a  c t e r i  s t i  c s

                                                                        Pa r  am e t e r                                                                            M i n                                                Ty p                                                M a x                                               U ni  t

                                                                       Input Voltage-Low (v)                                                                        –                                                   –                                                    0.3                                                 V

                                                                    To ensure safe power on, the module VBAT (VBAT_BB/VBAT_RF) must be discharged
                                                                    below 0.3V before re-applying VBAT power.

                                                                    4.6.1 Unmanaged POWER_ON_N (Default)


                                                                                                                 COL D START                                                                                                                                      HARDWARE RESET           SOFTWARE RESET
                                                                                                                                                                                                           (RESET_IN_N)                                                                       (AT+C FUN=1,1)

                                                                                VBAT_RF
                                                                                VBAT_BB                         T1                                                                       HW  RESET                                                                                                                                                                                                                                                             AT +C FU N=1, 1


                                                                                 RESET_IN_N
                                                                                                                                                                                                              T5


                                                                                   VGPIO                                                                                                          T6
                                                                                                                             T2                                                                                           T2


                                                                                   I/O State
                                                                                XXXXXXXXX XXX XXX XXXXXXXX                                                                                                          XXXXXXXXXXXX                                                                XXXXXXXXX


                                                                                 UART1_CTS                                                T3                                                                                           T3

                                                                                                                                                          T4                                                                                           T4

                                                                                 USB Bus
                                                                                XXXXXXXXX XXX XXX XXXXXXXX                                                                                                          XXXXXXXXXXXX                                                                XXXXXXXXX
                                                                                                                                                   T8                                                                                           T8                                                          T7

                                                                            Sta te:                          OFF                   ON                  AT‐READY                                                                                                                                                                                    AT‐READY                                                                                                                 AT‐READYOFF                               ONS/ W R es et


                                                                    Figure 4-4:                       Power On and Reset Sequence (unmanaged POWER_ON_N)


                                                                    Important:                 At completion of T4/T8/T7, the module is ready to receive AT commands ("AT-
                                                                    READY") via UART1 or USB.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               50                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 51

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s


Ta b  l  e    4 - 1 0 :                     P O W  E R _ O  N _ N    T i m i  n g     ( u n  m a n  a g e  d ) a

  Pa r  am e t e r                                                                                                                     M i n                   Typ                     M ax  b                                   U ni  t

  T1: Delay between VBAT_BB and RESET_IN_N                                                                                             –                       –                       200                                       ms

  T2: Delay between RESET_IN_N and VGPIO                                                                                               –                       –                       60                                        ms

  T3: Delay between VGPIO and UART1_CTS                                                                                                –                       –                       100                                       s

  T4: Delay                                                                                                                            –                       –                       10                                        s

  T5: HW RESET_IN_N assertion time                                                                                                     100                     –                       –                                         s

  T6: Off delay between VGPIO and RESET_IN_N                                                                                           –                       –                       30                                        s

  T7: Delay between software reset and AT-READY (UART/USB)                                                                             –                       –                       10                                        s


  T8: Delay between VGPIO and USB enumeration                                                                                          –                       –                       T3max + T4max                             s

       a.                           Timing of first power cycle after FOTA/FW upgrade is not captured in this table.
       b.                           Measurements taken with HL78xx Development Kit

                                             4.7 Power Down, Off, and VBAT Removal

                                             4.7.1 Software Power Off in Unmanaged Mode

                                             To power down the module via software:
                                             1.                                  Initiate the power down process:
                                                      a.                                  Use the +cfun=0 command to stop SIM processes.
                                                      b.                                 Use the +CPWROFF command (For details, refer to HL78xx AT Commands
                                                               Interface Guide (Doc# 41111821).):
                                                               AT+CPWROFF
                                                               OK
                                                      c.                                  Immediately after receiving the "OK" response, set WAKEUP low.
                                             2.                                  Monitor VGPIO— When VGPIO is low (e.g. < 0.2 V), the module is in OFF mode.
                                                      (Note— The module can be woken from OFF mode by setting WAKEUP high. For
                                                      timing details , see Wake Up Signal (WAKEUP)
                                             3.                                  It is now safe to remove power (VBAT_BB and VBAT_RF) from the module.


                                             Note:                While the module is in OFF mode, the host platform (MCU) interfaces can remain powered.
                                             To prevent these signals from back-powering the module, the host platform should make sure to
                                             isolate them—the signals should not be driven high (i.e. > 0.2 V).

                                             If the module is back-powered, the VGPIO low value will be higher (e.g. 0.8~1.1 V).


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               51                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 52

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n

                                       4.7.2 Emergency Power Removal

                                       The Software Power Off in Unmanaged Mode procedure (which uses AT commands)
                                       should be used to safely power down the module.
                                       However, if the module’s UART and USB interfaces cannot be accessed, or are
                                       unresponsive (i.e. do not res pond after an AT command is issued (see Command
                                       Timeout appendix in HL78xx AT Commands Interface Guide), the following procedure can
                                       be used to power down the module, if necessary.

                                       Important:                 This procedure should be used with caution. If the module is interrupted while
                                       processing certain AT commands or performing a firmware upgrade, or the procedure is not
                                       followed correctly, the module may become unusable.

                                       4.                                  Set RESET_IN_N low, and keep it asserted.
                                       5.                                  Monitor VGPIO- When VGPIO is low (e.g. < 0.2 V), the module is powered down.
                                       6.                                  Remove VBAT (both VBAT_BB and VBAT_RF) power.
                                       7.                                  Monitor VBAT- When VBAT is discharged below 0.3V, de-assert RESET_IN_N.


                                       Note:                :To power up the module, it is critical that VBAT be fully discharged (or below 0.3V) and that
                                       RESET_IN_N must be de-asserted. For details, refer to Unmanaged POWER_ON_N (Default).

                                       While the module is in OFF mode, the host platform (MCU) interfaces can remain
                                       powered. To prevent these s ignals from back-powering the module, the host platform
                                       should make sure to isolate them-the signals should not be driven high (i.e. > 0.2 V).
                                       If the module is back-powered, the VGPIO low value will be higher (e.g. 0.8~1.1 V).
                                       4.8 Reset Signal (RESET_IN_N)

                                       The RESET_IN_N hardware control signal can be used to reset the module in any power
                                       state.
                                       To reset the module, assert RESET_IN_N low for 100s                     (minimum)  —   this action
                                       immediately resets the module. For timing details, s ee Figure         4-                   4 (HARDWARE RESET
                                       segment).
                                       Use an open drain/open collector type circuit to drive the signal low (< 0.3V (Input
                                       Voltage-Low (V))),
                                       Do not add a pull-up resistor on this signal as it is internally bias ed high by default. The
                                       bias voltage depends on the module operating state- 1.3-1.4V in Active and Sleep modes,
                                       and 1.1-1.2V in Hibernate and Lite Hibernate modes.


                                       Note:                For power-sensitive applications, the module does not reach minimal power consumption
                                       when held in reset. Therefore, it is not recommended to hold the module in reset state for long
                                       periods.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               52                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 53

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s


                                              Warning:                  RESET_IN_N should only be used to reset the module if it is unresponsive to AT
                                              commands and a power cycle cannot be performed. If used inappropriately (e.g. to reset during a
                                              firmware upgrade), memory corruption can occur.

                                              As an alternative, Sierra Wireless recommends implementing a software reset using AT+CFUN=1,1.
                                              For details, refer to HL78xx AT Commands Interface Guide (Doc# 41111821)


                                              Warning:                  During a module reset:
                                                                  - All I/Os will be in an undefined state.
                                                                  - I/Os must not be driven high (over 0.2 V), otherwise the module may be damaged
                                                                  - RESET_IN_N must not be set low during a power cycle, otherwise the module
                                                                    will not boot.
                                                                  - VBAT_BB must always be >3.2V when reset is asserted.

                                              Table          4-                   11 and Table                    4-                   12 describe the RESET_IN_N signal.

Ta b  l  e    4 - 11 :                     R E S E T  _ I  N _ N    P i  n    D e s c  r i  p t i  o n

  Pa d   #                                                       S i g n al    N a m e                                          I /  O a                        A c t i v e                     D e s cr  i p t i o n

  C11                                                            RESET_IN_Nb                                                    I                               L                               Reset signal

       a.                           Signal direction with respect to the module.
       b.                           Signal provided by host. Does not need to be buffered, and can be directly connected to module using an open drain/collector
              type circuit.

                                              Refer to the following table for the electrical characteristics of the RESET_IN_N interface.

Ta b  l  e    4 - 1 2 :                     R E S E T _  I N _  N   E l  e c t r  i c  a l    C h  a r a  c t e r i  s t i  c s

  Pa r  am e t e r                                                               M i n                                       Ty p                                       M a x                                       U ni  t

  Input Voltage-Low                                                              –                                          –                                           0.3                                        V

  Reset assertion time                                                           0.1                                        1                                           –                                          ms

                                              4.9 Analog to Digital Converter (ADC)

                                              The HL781x provides two general purpose ADC signals  (ADC0, ADC1). These converters
                                              are 12-bit resolution ADCs with voltage range of 0–1.8V.
                                              Typical ADC use is for monitoring external signals. The AT+KADC command is used to
                                              read the ADC values. For details, refer to HL78xx AT Commands Interface Guide.
                                              Table          4-                   13 describes the ADC signals.

Ta b  l  e    4 - 1 3 :                     A D C    P i  n    D e s  c r i  p t i  o n

  Pa d   #                                        S i g n al    N am  e                           I /  O a                                        D e sc r i  p t i o n                                       I /  O   Ty pe

  C24                                             ADC1                                            AI                                              Analog to digital converter                                1.8V (VGPIO)

  C25                                             ADC0                                            AI                                              Analog to digital converter                                1.8V (VGPIO)

       a.                           Signal direction with respect to the module.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               53                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 54

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n

                                        4.10 Clock Interface

                                        The HL781x supports two digital clock output signals.
                                        These signals are disabled by default. To enable (or disable) these signals, use the
                                        AT+K HWIOCFG command. For details, refer to HL78xx AT Commands Interface Guide.


                                        Note:                To reduce noise and radiated spurious emission (RSE), disable the clock signals if they are
                                        not being used.

                                        Table          4-                   14 describes the clock signals.

Ta b  l  e    4 - 1 4 :                     C l  o c  k    I n  t e r f a c  e    P i  n    D e  s c r i  p  t i o  n

  Pa d   #                       S i g n al    N a m e                      I /  O a                Vo l t a g e   S u pp l y   D o m a i n                 D e sc r  i p t i o n


 C22                             26M_CLKOUT                                 O                      1.8V (VGPIO)                                             26 MHz Digital Clock output

 C23                             32K_CLKOUT                                 O                      1.8V (VGPIO)                                             32.786 kHz Digital Clock output

      a.                           Signal direction with respect to the module.

                                        4.11 Debug Interfaces

                                        The HL781x provides two 4-wire debug port interfaces (Diagnostic Interface, Modem
                                        Logs) that can be used with the AT interface for full debug capability.


                                        Note:                All UART signals operate at 1.8V. A voltage level shifter is required when connecting to a 3V3
                                        domain.

                                         UART interfaces are not active during Hibernate mode, so the host should ignore all
                                        activity on UART interfaces during Hibernate. If the module will enter Hibernate mode,
                                        Sierra Wireless recommends adding buffer circuits to ensure module I/Os are not driven
                                        high (i.e. >0.2V).
                                        To enable debug interfaces, refer to HL78xx AT Commands Interface Guide.

                                        4.11.1 Diagnostic Interface

                                        The Diagnostic interface is implemented over UART0. When the module begins to boot,
                                        UART0 is enabled at 115200 baud and writes an initial boot log.
                                        Availability and behavior of UART0 after the initial boot log is written depends on the
                                        configured debug mode (using the AT command +SWITRACEMODE):
                                        •                                                    Customer mode (AT+SWITRACEMODE=CUSTOMER)— UART0 is disabled after the
                                                initial boot log is  written.
                                        •                                                    Debug mode (AT+SWITRACEMODE=LOG or AT+SWITRACEMODE=SFPLOG)—
                                                UART0 remains enabled for logging. Unless configured differently using +SWITRAC-
                                                EMODE options, the default baud rate (921600) and default flow control (enabled) are
                                                used. With flow control enabled (4-wire logging), UART0_CTS is asserted. To receive
                                                logging data, the host must assert UART0_RTS, and then use UART0_RX/


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               54                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 55

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s


                                                                        UART0_TX to receive/send data.
                                                                        With flow control disabled (2-wire logging), note that the host must be fast enough to
                                                                        capture all data streamed from the module s o that log files are not corrupted.
                                                            •                                                    Boot mode for firmware upgrades (using SFT (Standalone File Tool))— UART0
                                                                        remains enabled for 2-wire communication (flow control is disabled by default;
                                                                        UART0_CTS is not asserted, and UART0_RTS is ignored).
                                                            Note that Flow control may be enabled using AT+KBOOTCFG=1, but is not required for
                                                            successful firmware upgrades.
                                                            For SFT details, refer to HL780x Firmware Update Methods Application Note.

Ta b  l  e    4 - 1 5 :                     D i  a g  n o  s t i  c   I  n  t e r f a c  e   P i  n    D e  s c r i  p t i  o  n

   Pa d   #                S i g n al    N am  e a                         D e f a ul  t   S t a t e  b ,c                        A ct  i v e                I / O    Typ e                                    De s c r i p t i  on

  C55                     UART0_RX                                         Output                                                 —                          1.8V (VGPIO)                                      Debug Receive Data

  C56                     UART0_TX                                         Input                                                  —                          1.8V (VGPIO)                                      Debug Transmit Data

  C57                     UART0_CTS                                        Output                                                 L                          1.8V (VGPIO)                                      Debug Clear to Send

  C58                     UART0_RTS                                        Input                                                  L                          1.8V (VGPIO)                                      Debug Request to Send

         a.                           Signals are named with respect to the host device (i.e. DTE (Data Terminal Equipment) convention—PC view). For example,
                  UART0_RX is the signal used by the host to receive data from the module.
         b.                           Signal direction with respect to the module. For example, UART0_RX is an output from the module to the host.
         c.                             Default states are for the module in Debug mode with flow control enabled.
                  In Debug and Boot modes, with flow control disabled, UART0_CTS and UART0_RTS are disabled.
                  In Customer mode, all signals are disabled.


                                                            Note:                It is highly recommended to provide access through Test Points to this interface (required for
                                                            customer platform debugging).


                                                                                                                                                                                      UART0
                                                                                HL78 1x                                                                             Diagnostic Interface                                                                                                 Customer
                                                                                                                                                                                                                                                                                          Platform


                                                                                                                                                                 TP
                                                                                                                        C55                                                                                                                                                                                                                                                                                                            0
                                                                                               UART0_RX

                                                                                                                        C56                                                                                                                                                                                                                                                                                                            0TP
                                                                                                UART0_TX                              Buffer
                                                                                                                                      circuit                    TP
                                                                                                                        C57                                                                                                                                                                                                                                                                                                            0
                                                                                              UART0_C TS
                                                                                                                        C58                                                                                                                                                                                                                                                                                                            0TP
                                                                                              UART0_RTS

                                                            Figure 4-5:                       Diagnostic Interface connection example


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               55                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 56

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n

                                                                               4.11.2 Modem Logs Interface (MLI)

Ta b  l  e    4 - 1 6 :                     M o d  e m    L o  g s    I  n  t e r f a c e    P i  n    D e s  c r i  p t i  o  n

   Pa d   #                                                                              S i g n al    N am  e                                                                 I /  O a                                                                              I /  O   Ty pe                                                                        D e sc r i  pt  i o n

   C51                                                                                   GPIO14                                                                                O                                                                                     1.8V (VGPIO)                                                                          UART3_CTSb

   C52                                                                                   GPIO10                                                                                I                                                                                     1.8V (VGPIO)                                                                          UART3_TXb

   C53                                                                                   GPIO11                                                                                I                                                                                     1.8V (VGPIO)                                                                          UART3_RTSb

   C54                                                                                   GPIO15                                                                                O                                                                                     1.8V (VGPIO)                                                                          UART3_RXb

            a.                           Signal direction with respect to the module. For example, GPIO14 is an output from the module to the host.
            b.                           Signals are named with respect to the host device (i.e. DTE (Data Terminal Equipment) convention—PC view). For example,
                         UART3_RX is the signal used by the host to receive data from the module.


                                                                               Note:                To enable use of the UART3 interface for customer platform debugging, it is highly recom-
                                                                               mended to provide access through Test Points to these 4 GPIOs.


                                                                                                                                                                                                                                UART3
                                                                                                         HL781x                                                                            Modem  Logs I nt erface (MLI)                                                                                                                                               Customer
                                                                                                                                                                                                                                                                                                                                                                         Platform


                                                                                                                                                           C54                                                                                                                                                                                                                                                                                               0TP
                                                                                                      GPIO15 / UART3_RX

                                                                                                                                                           C52                                                                                                                                                                                                                                                                                               0TP
                                                                                                      GPIO10 / UART3_TX                                                      Buffer
                                                                                                                                                                             circuit                           TP
                                                                                                                                                           C51                                                                                                                                                                                                                                                                                               0
                                                                                                    GPIO14 / UART3_CTS
                                                                                                                                                           C53                                                                                                                                                                                                                                                                                               0TP
                                                                                                    GPIO11 / UART3_RTS

                                                                               Figure 4-6:                       Modem Logs Interface connection example


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               56                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 57

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s

                                                             4.12 Wake Up Signal (WAKEUP)

                                                             The WAKEUP hardware control signal is used to wake the module from low power modes
                                                             (Sleep, Lite Hibernate, Hibernate, OFF) by driving the signal high to 1.8V.
                                                             The module will not enter or return to low power mode while the WAK EUP signal is high.
                                                             Table          4-                   17 and Table                    4-                   18 describe the WAKEUP signal.

Ta b  l  e    4 - 1 7 :                     WA K E U P   P i  n    D e s  c r i  p t i  o n

   Pa d   #                                                          S i g n al    N am  e                                              I /  O a                                                           I /  O   Ty pe                                                     D e sc r i  pt  i o n

  C44                                                                WAKEUPb                                                            I                                                                 1.8V                                                               Wakes the module up
                                                                                                                                                                                                                                                                             from low power mode

         a.                           Signal direction with respect to the module.
         b.                           Signal provided by host. Signal does not need to be buffered, and can be directly connected to the module.


Ta b  l  e    4 - 1 8 :                     WA K E U P   E l  e c t r i  c a  l    C h  a r a c  t e r i  s t i  c s

   Pa r  am e t e r                                                                M i n i m  um                                                  Typ i c a l                                                     M a x i m u m                                                  U ni  t

  VIL                                                                              –                                                              –                                                              0.3                                                             V

  VIH                                                                              1.2                                                            –                                                              –                                                               V

  Wakeup assertion timea                                                           100                                                            –                                                              –                                                               s

  Internal PD                                                                      –                                                              100K                                                           –                                                               Ω

         a.                           Assertion time—Time required to keep WAKEUP at high level to ensure module can wake up successfully.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               57                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 58

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n

                                                             4.12.1 Wakeup from Low Power Modes

                                                             This section describes the module’s signal behaviors  when waking from the low power
                                                             modes defined in Table          3-                   5.

                                                             4.12.2 Wakeup from OFF Mode

                                                             Figure          4-                   7 and Table                                        4-                   19 describe signal behavior when WAK EUP is used to wake the
                                                             module from OFF mode.


                                                                                                                                                                                                 Wake up from OFF
                                                                                                                                                                                                             mode

                                                                          VBAT_RF
                                                                          VBAT_BB


                                                                           WAKEUP


                                                                            VGPIO
                                                                                                                                                                                                   T1


                                                                            I/O State
                                                                                                                            XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX


                                                                             UART1_CTS
                                                                                                                                                                                                                T2
                                                                                                                                                                                                                               T3


                                                                            USB BUS
                                                                                                                           XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                                                                                                                                                                                                                         T4

                                                                      State:                                                                            OFF                                                                                                                      ONAT‐READY

                                                             Figure 4-7:                       Wake up from OFF Mode


Ta b l e   4-1 9 :             WA K E U P   T i m i n g   ( f r o m   O F F   M o d e )

   Pa r am e t e r                                                                                                                                 M i n                              Typ                                 M a xa                                                                                   U n i t

  T1: Delay between WAKEUP and VGPIO                                                                                                               –                                  –                                  1                                                                                        ms

  T2: Delay between VGPIO and UART1_CTS                                                                                                            –                                  –                                  20                                                                                       s

  T3: Delay                                                                                                                                        –                                  –                                  4                                                                                        s

  T4: Delay between VGPIO and USB enumeration–                                                                                                                                        –                                  T2max + T3max                                                                            s

         a.                   Measurements taken with HL78xx Development Kit


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               58                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 59

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s

                                                                               4.12.3 Wakeup from Lite Hibernate Mode

                                                                               Figure          4-                   8 and Table                                        4-                   20describe the module’s signal behaviors when WAKEUP is used
                                                                               to wake the module from Lite Hibernate mode.


                                                                                                                                                                                     Lite Hibernate                                                                                       Wa keup from Lite Hibernate


                                                                                           VBAT_RF
                                                                                           VBAT_BB


                                                                                            WAKEUP

                                                                                                                                                                                                                                                                                          T1


                                                                                              VGPIO


                                                                                               UART1_CTS


                                                                                                                                                                                                                                                                                                           AT‐READY

                                                                               Figure 4-8:                       Wake up from Lite Hibernate Mode


Ta b l e   4-2 0 :               WA K E U P   T i m i n g   ( f r o m   L i t e   H i b e r  n a t e   M o d e )

   Pa r am e t e r                                                                                                                                                                                                        M i n                                                 Ty p                                                 M a xa                                                U n i t

   T1: Delay between WAKEUP and AT-READY                                                                                                                                                                                 –                                                     1                                                     80                                                    ms

            a.                      Measurements taken with HL78xx Development Kit


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               59                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 60

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n

                                                                         4.12.4 Wakeup from Hibernate Mode

                                                                         Figure          4-                   9 and                    Table          4-                   21 describe the module’s signal behaviors when WAKEUP is used
                                                                         to wake the module from Hibernate mode.


                                                                                                                                                                            Hibernate                                                                                              Wakeup from Hi be rnat e

                                                                                      VBAT_RF
                                                                                      VBAT_BB


                                                                                        WAKE UP


                                                                                         VGPIO
                                                                                                                                                                                                                                                 T1


                                                                                         I/O State
                                                                                                                        XX XXX XXXX XXX XXX XXXX XXX XXX XXX XXXX XXX XXX


                                                                                       UART1_CTS                                                                                                                                                                 T2

                                                                                                                                                                                                                                                                                     T3

                                                                                                                                                                                                                                                         ON                                  AT‐
                                                                                                                                                                                                                                                                                        READY

                                                                         Figure 4-9:                       Wake up from Hibernate Mode


Ta b  l  e    4        -  2 1 :                          WA K E  U P    T i  m i n  g    (  f  r o  m    H i b  e r  n a t e    M  o d  e )

   Pa  r  a m e  t e  r                                                                                                                                                                                   M i n                                             Ty  p                                            M a  x   a                                        U n i t

   T1: Delay between WAKEUP and VGPIO                                                                                                                                                                     –                                                –                                                 1                                                 ms

   T2: Delay between VGPIO and UART1_CTS high                                                                                                                                                             –                                                –                                                 20                                                µs

   T3: UART1_CTS high to AT-READY                                                                                                                                                                         –                                                –                                                 80                                                ms

           a.                               Measurements taken with HL78xx Development Kit


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               60                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 61

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s

                                                4.13 RF Interface

                                                The RF interface of the Sierra Wireless HL781x provides a single RF antenna connection
                                                for the transmission/reception of RF signals.
                                                Contact Sierra Wireless technical support for assistance in integrating the Sierra Wireless
                                                HL781x on applications with embedded antennas.

                                                4.13.1 RF Antenna Connection

                                                A 50 RF track (with maximum VSWR 1.1:1, and 0.5 dB loss) is recommended to
                                                connect the module’s RF_MAIN to standard RF antenna connectors (e.g. SMA, U.FL,
                                                etc).
                                                Table          4-                   22 describes the module’s RF interface.


Ta b  l  e    4 - 2 2 :                     R F    M a i  n    P i  n    D e s  c r i  p t i  o n

  Pa d   #                                            R F   S i g n al                                     I m p e d an c e                                    V S W R   R x   ( m  ax )                            V S W R   T x   ( m a x )

  C48                                                 GND                                                  –                                                   –                                                    –

  C49                                                 RF_MAIN                                              50                                                 2.5:1                                                2.5:1

  C50                                                 GND                                                  –                                                   –                                                    –

                                                4.13.2 LTE RF Interface

                                                4.13.2.1 Maximum Output Power

                                                The HL781x module’s LTE maximum transmitter output power for all bands in normal
                                                operation conditions (25°C) is specified in Table          4-                   23.

Ta b  l  e    4 - 2 3 :                     H L  7 8 1 0    /     /    H L  7 8 1 2    C o  n d  u  c t e d    T  x    M a  x   O  u  tp  u  t   P o  w e r    To l  e r a  n c e  s   -    LT E a

  LT E   B a n d s                                                                                                                    M i n               Ty p               M a x                U n i t s                N o t e s

  All bands                                                                                                                           21.5b              23                  24.5                 dBm                      Power class 3

       a.                           Under normal operating conditions (25°C)
       b.                           Additional power reduction is applied to the lowest and highest supported channels for each band — see Table        1-1 footnote "a"
               for supported Tx channel ranges. (e.g. applies to B2 channels 18602 and 19198)

                                                4.13.2.2 Rx Sensitivity

                                                The module's LTE receiver sensitivity is specified in the following tables.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               61                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 62

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    4 - 2 4 :                     H L  7 8 1 x    Ty p  i c a  l    C o  n d  u c  te  d    C a t - M 1    R X    S e  n s i  t i  v i  t y a

   LT E   B a n d                                                                   Ty pi  ca l    R ef  er  en c e   S en s i t  i v i t y   L e ve l    @   9 5 %   o f   M  ax i m u m    Th r  ou g h pu t

                                                                                    @    +2 5 ° C   ( d B m )                                                         @    C l a ss  A   ( d B m )                                                      3 G P P  L i m i  t   (  dB m ) b

  B1                                                                                -104                                                                              -102.5                                                                            -102.3

  B2                                                                                -104                                                                              -103                                                                              -100.3

  B3                                                                                -105                                                                              -103.5                                                                            -99.3

  B4                                                                                -104                                                                              -102.5                                                                            -102.3

  B5                                                                                -105                                                                              -104                                                                              -100.8

  B8                                                                                -105                                                                              -103                                                                              -99.8

  B12                                                                               -105                                                                              -103.5                                                                            -99.3

  B13                                                                               -105                                                                              -104                                                                              -99.3

  B18                                                                               -105                                                                              -104                                                                              -100.3

  B19                                                                               -105                                                                              -104                                                                              -102.3

  B20                                                                               -105                                                                              -104                                                                              -99.8

  B25                                                                               -105                                                                              -103                                                                              -100.3

  B26                                                                               -105                                                                              -104.5                                                                            -100.3c

  B28                                                                               -105                                                                              -104                                                                              -100.8

  B66                                                                               -104                                                                              -102.5                                                                            -102.3c

  B85                                                                               -105                                                                              -104                                                                              -102.3

         a.                           Test conditions per 3GPP TS 36.521-1 v13: Bandwidth: 5MHz on Reference Measurement Channel.
         b.                           Displayed limits derived from 3GPP TS 36.521-1 V16.3.0, Table 7.3EA-2, adjusted by +0.7 dB for measurement uncertainty.
         c.                             Band not defined by 3GPP therefore no associated limit.


Ta b  l  e    4 - 2 5 :                     H L  7 8 1 x    Ty p  i c a  l    C o  n d  u c  te  d    N B 1 /  N B 2    R X    S e n  s i t i  v i  t y a

   LT E   B a n d                                                                   Typ i  ca l   R e f e r  en c e   S en s i t i  vi  t y   L ev e l   @    9 5%    o f   M a x i m u m   T h r o u g hp u t

                                                                                    @   + 2 5 °C    ( d B m )                                                         @    C l a ss  A   ( d B m )                                                      3 G P P  L i m  i t   (  d Bm  ) b

  B1                                                                                -113                                                                              -111.5                                                                            -107.5

  B2                                                                                -113.5                                                                            -112.1                                                                            -107.5

  B3                                                                                -114                                                                              -112.5                                                                            -107.5

  B4                                                                                -113                                                                              -111.6                                                                            -107.5

  B5                                                                                -113.5                                                                            -112.3                                                                            -107.5

  B8                                                                                -113                                                                              -111.8                                                                            -107.5


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               62                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 63

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s


Ta b  l  e    4 - 2 5 :                     H L  7 8 1 x    Ty p  i c a  l    C o  n d  u c  te  d    N B 1 /  N B 2    R X    S e n  s i t i  v i  t y a    ( C o  n t i  n u  e d  )

  LT E   B a n d                                                        Typ i  ca l   R e f e r  en c e   S en s i t i  vi  t y   L ev e l   @    9 5%    o f   M a x i m u m   T h r o u g hp u t

                                                                        @   + 2 5 °C    ( d B m )                                             @    C l a ss  A   ( d B m )                                         3 G P P  L i m  i t   (  d Bm  ) b

  B12                                                                   -112.5                                                               -111.2                                                                -107.5

  B13                                                                   -113                                                                 -111.8                                                                -107.5

  B18                                                                   -113.5                                                               -112.2                                                                -107.5

  B19                                                                   -113.5                                                               -112.2                                                                -107.5

  B20                                                                   -113                                                                 -111.7                                                                -107.5

  B25                                                                   -113                                                                 -111.7                                                                -107.5

  B26                                                                   -113.8                                                               -112.5                                                                -107.5

  B28                                                                   -113                                                                 -111.7                                                                -107.5

  B66                                                                   -113                                                                 -111.5                                                                -107.5

  B85                                                                   -113.5                                                               -112.5                                                                -107.5

                                                                                                                                                                                                                   -107.5

                                                                                                                                                                                                                   -107.5

        a.                           Test conditions per 3GPP TS 36.521-1 v13: on DL Reference Measurement Channel defined
        b.                           Displayed limits derived from 3GPP TS 36.521-1 V16.3.0, Table 7.3F.1.3-1, adjusted by +0.7 dB for measurement uncertainty

                                                   4.13.3 2G RF Interface (HL7812 only)

                                                   The HL7812 module is a GPRS only device (no EGPRS support) supporting GSM
                                                   multislot class 10 (4 DL/2UL max (5 slots)).

                                                   4.13.3.1 Tx Output Power

                                                   The module's 2G maximum transmitter output power is specified in Table         4-                   26.

Ta b  l  e    4 - 2 6 :                     H L  7 8 1 2    C o  n  d u  c t e d    T  x    M a  x    O u  t p u  t   P o  w e r    To l  e r a  n c e  s    -   2 G  a , b

  RF   B a n d                                    M i n                              Ty p                               M a x                              Un i t  s                          N ot  es

  GSM 850                                         31.5                               32.5                               33.5                               dBm                                GMSK mode (Class 4; 2 W, 33 dBm)

  E-GSM 900                                       31.5                               32.5                               33.5                               dBm                                GMSK mode (Class 4; 2 W, 33 dBm)

  DCS 1800                                        28.5                               29.5                               30.5                               dBm                                GMSK mode (Class 1; 1 W, 30 dBm)

  PCS 1900                                        28.5                               29.5                               30.5                               dBm                                GMSK mode (Class 1; 1 W, 30 dBm)

        a.                           Stated power tolerances satisfy 3GPP TS 51.010-1 requirements for normal (25°C) and Class A (extreme) conditions.
        b.                           Stated power tolerances for input voltage of 3.7V.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               63                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 64

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


                                                         4.13.3.2 Rx Sensitivity

                                                         The module's GPRS receiver sensitivity is specified in Table          4-                   27.

Ta b  l  e    4 - 2 7 :                     Ty  p i  c a l     C o n  d u  c t e d     R X   S e n  s i  t i v  i t y    -    G P R S    B a n  d s a

  G PR S   B a n d                         P a r a m e t e r s                                                Ty pi  ca l    R ef  er  en c e   S en s i t i  vi  t y   L e ve l    @   9 5 %   o f   M a x i m u m
                                                                                                              T hr  o ug h p ut

                                                                                                              @    +2 5 ° C   ( d B m )                                     @    C l a ss  A   ( d B m )                                         S t a n da r  d   Li  m i t    ( d B m )

  GSM 850                                  10% BLER; GMSK CS1                                                 -110                                                          -108                                                                -102

  E-GSM 900                                10% BLER; GMSK CS1                                                 -110                                                          -108                                                                -102

  DCS 1800                                 10% BLER; GMSK CS1                                                 -112                                                          -110                                                                -102

  PCS 1900                                 10% BLER; GMSK CS1                                                 -112                                                          -110                                                                -102

         a.                           Stated sensitivity values satisfy 3GPP TS 51.010-1 requirements for normal (25°C) and Class A (extreme) conditions

                                                         4.14 TX Burst Indicator (TX_ON)

                                                         The HL781x provides the TX_ON signal for TX activity indication.


                                                         Note:                This signal is currently available for LTE Cat-M1. Support for LTE Cat-NB1 (HL7810 /HL7812)
                                                         and 2G (HL7812) will be available in a future firmware release.


Ta b  l  e    4 - 2 8 :                     T  X _  O N    P i n     D e s c  r i p  t i  o n

  Pa d   #                                                       S i g n al    N am  e                                         I /  O a                                                      I /  O   Ty pe                                                D e sc r i  pt  i o n

  C60                                                           TX_ON                                                         O                                                              1.8V (VGPIO)                                                  High during Tx activity

         a.                           Signal direction with respect to the module


                                                                    TX _O N


                                                                TX   ac  t iv i  t y
                                                                                                                                              Transmitt in g


                                                                                                 T adv ance


                                                         Figure 4-10:                       TX_ON State High during TX Activity


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               64                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 65

D e  t a  i l e d   I  n t  e r f  a c e   S p e c i f  i c a t  i o  n s


                                                              To enable/disable this feature, use the AT+KHWIOCFG command. For details, refer to
                                                              HL78xx AT Commands Interface Guide.

                                                              Ta b  l  e    4 - 2 9 :                     T  X _  O N    C h  a r a c  t e r i  s t i  c s

                                                                 Pa r  am e t e r                                                   Ty pi  ca l

                                                                 Tadvance                                                          30 s

                                                              4.15 Tx/Rx Activity Indicator; External RF
                                                              Voltage Control

                                                              The HL781x provides the VBAT_PA_EN signal for RF activity (Tx/Rx) indication.
                                                              Depending on customer requirements, it can be also be us ed to select the module
                                                              VBAT_RF power source during RF activity, and support antenna switching.
                                                              To enable/disable this feature, use the AT+KHWIOCFG command. For details, refer to
                                                              HL78xx AT Commands Interface Guide.
                                                              Table 4-30, Figure 4-11 and Table 4-31 describe the VBAT_PA_EN signal.


Ta b  l  e    4 - 3 0 :                     V B AT  _ PA _ E N    P i n    D  e s c r  i p  t i o  n

   Pa d   #                                                           S i g n al    N am  e                                              I /  O a                                    I /  O   Ty pe                               D e sc r  i p t i o n

                                                                      GPIO8                                                              I/O
  C41                                                                 VBAT_PA_EN                                                         O                                           1.8V (VGPIO)                                High during Tx/Rx activity


         a.                           Signal direction with respect to the module


                                                                              GPIO8 /
                                                                      VB   A T   _ P A  _ E   N


                                                                             Tx  &   Rx                                                          Tx / Rx activity                                                                                                  Tx / Rx activity
                                                                               ac tivity


                                                                                                  T adv ance                                                                                                                                                                                                                                                                                                                                                                                                                                               T delay


                                                              Figure 4-11:                       VBAT_PA_EN State during Tx/Rx Activity


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               65                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 66

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


                                                   Ta b  l  e    4 - 3 1 :                     V B AT  _ PA _ E N    C h a  r a c t e  r i  s t i c  s    (T  B C )

                                                     Pa r  am e t e r                                          M i n                                                     M a x

                                                     Tadvance                                                  0.4 ms                                                    5 ms

                                                     Tdelay                                                    10 s                                                     20 s

                                                   4.16 GNSS

                                                   The HL781x's GNSS implementation supports GPS L1 and GLONASS G1 operation.


                                                   Note:                The GNSS receiver and LTE/GSM receiver share the same RF resources, therefore GNSS
                                                   can only be used when the module is not actively connected on LTE/GSM. An example of a suitable
                                                   implementation of GNSS in an end product would be the use of GNSS positioning for asset
                                                   management applications where infrequent and no real-time position updates are required.

                                                   Table          4-                   32 describes the GNSS antenna specifications. Note that the HL781x does not
                                                   support an active GPS/GNSS antenna.

                                                   Ta b  l  e    4 - 3 2 :                     G  N S S   A n t e n  n a    S p  e c i  f i c  a t i  o n  s

                                                     Ch a r a c t e r i s t  i c s                                                                                Va l u e                                                                               U n i t

                                                     Frequency                                              GPS L1                                                1563–1587                                                                              MHz

                                                                                                            GLONASS G1                                            1593–1610                                                                              MHz

                                                     RF Impedance (RF_GNSS pad)                                                                                   50                                                                                     W

                                                     VSWR max                                                                                                     2:1                                                                                    –

                                                   4.16.1 GNSS Performance

                                                   Table          4-                   33 summarizes the HL781x module’s GNSS performance characteristics.

Ta b  l  e    4 - 3 3 :                     G  N S S    P e r f o  r m a  n c e

  Pa r  am e t e r  s                                                                         C o nd i t  i o n s                                                                          Ty pi  ca l    Va l u e

  Sensitivity                                                                                 Cold Start                                                                                  -145.8 dBm

                                                                                              Hot Start                                                                                   -152 dBm

                                                                                              Tracking                                                                                    -161 dBm

  Time To First Fix (TTFF)                                                                    Cold start, Input power -130 dBm                                                            39s

                                                                                              Hot start, Input power -130 dBm                                                             1s

  2D Position Error                                                                           Input power -130 dBm                                                                        1.29 m


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               66                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 67

5: Mechanical Drawings                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        5


                                                                                                                       For tolerances, refer to Table          1-                   2 and Table                                        1-                   3.


                                                                                                                       Figure 5-1:                       Mechanical Drawing


                                                                                                                       Note:                HL7812 shield displayed. (HL7810 shield does not have center cutouts.)


                                                                                                                       Figure 5-2:                       Dimensions Drawing


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               67                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 68

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


                                                                                                                                                                                                               Figure 5-3:                       Footprint Drawing


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               68                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 69

6: Design Guidelines                                                                                                                                                                                                             6


                                       6.1 Power Supply Design

                                       When designing the power supply, make sure VBAT_BB/VBAT_RF meet the
                                       requirements listed in Table         3-                   2— Sierra Wireless recommends adding a 30% design
                                       margin, if possible.
                                       Careful attention should be paid to the following:
                                       •                                                    Power supply design— A low-ripple, low-noise source such as LDO, battery, or
                                              switching power supply (SMPS) is recommended.
                                       •                                                    (HL7812 GSM Tx) Capacity to deliver high current peaks in a short time
                                              ·                    VBAT_BB/VBAT_RF must support peak currents with an acceptable voltage drop
                                                  that guarantees the minimum required VBAT_BB/VBAT_RF value.
                                       •                                                    VBAT_BB/VBAT_RF signal voltage must never exceed the maximum value,
                                              otherwise the module may be severely damaged.
                                              ·                    If necessary, add a voltage limiter to the module’s power supply lines to ensure
                                                  VBAT will never receive a voltage surge over 4.35V. There are a few protection
                                                  options from a basic linear regulator to a voltage limiter, as simple as a Zener diode.
                                       •                                                    ESD protection is recommended on VBAT_BB/VBAT_RF supply rails— Sierra
                                              Wireless recommends Diodes Inc part number D8V0L1B2LP3-7.
                                       •                                                    Both over-voltage protection and ESD protection devices will increase platform
                                              current consumption.
                                       •                                                    All ground pins (C30, C32, C37, C39, C48, C50, CG1–CG4, G1–G16) must be
                                              connected to the same net.
                                       6.2 UIM1

                                       UIM1 can operate at clock rates up to 5 MHz.
                                       Most UIM1 signal lines do not require a buffer during Hibernate, and can be directly
                                       connected to the UIM card or holder. A buffer is required for UIM_DET1 if powered from
                                       the host (not required if powered from VGPIO).
                                       Decoupling capacitor(s) must be added to UIM1_VCC and UIM1_DET, as close as
                                       possible to the UIM card. Decoupling capacitors for UIM1_CLK, UIM1_RST, and
                                       UIM1_DATA are recommended to be added as placeholders for potential EMC issues.
                                       The two resistors (RCLK and RDAT) should be added as placeholders to compensate for
                                       potential layout issues. Both can be populated to slew the UIM1 signals, if required.
                                       The UIM1_DATA trace should be routed away from the UIM1_CLK trace.
                                       Keep the distance between the module and the UIM holder as short as possible.
                                       Sierra Wireless recommends using the following ESD protection on the UIM1 interface:
                                       •                                                    INFINEON ESD112-B1-02EL E6327— UIM1_CLK , UIM1_DATA, UIM1_RESET
                                       •                                                    Diodes Inc D8V0L1B2LP3-7 — UIM1_VCC, UIM1_DET
                                       Figure          6-                   1 illustrates the recommended implementation of a UIM interface


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               69                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 70

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


                                                                      UIM1_V CC                                                                                                                                                                                             C1
                                                                                                                                                                 1 uF
                                                                                                                                                                 DNI                                                                                                                         100 nF                                                                                         1 nF                                                                                                               ESD


                                                                  UIM1_RESET                                                                                                                                                                                                C2


                                                                                                                                                                                            100 nF                ESD
                                                                                                                                                                                              DNI
                                                                                                                                                                                                                                                                            C3
                                                                      UIM1_CLK
                                                                                                                                                                  RCLK
                                                                                                                                                                 0ohm                                         100 nF                            ESD                         C4
                                                                                                                                                                                                                 DNI                                                        C5
                                                           HL781x                                                                                   RDATA                                                                                                                   C6    UIM Holder
                                                                                                                                                     DNI

                                                                                GN D                                                                                                                                                                                        C7

                                                                    UIM1_DAT A                                                                                                                                                                                              C8

                                                                                                                                                                                                                             100 nF                              ESD
                                                                                                                                                                                                                               DNI
                                                                            VGPIO                                                                                                                                                                                           C9

                                                                                                                                                                                 100 nF                                   ESD
                                                                                                                                                                                   DNI

                                                                      UIM1_DET                                                                                                                                                                                              C10
                                                                                                                                                                                                                                                   1 kohm
                                                                                                                                                                                                  1 nF


                                                               Figure 6-1:                       EMC and ESD Components Close to the USIM

                                                               6.3 USB Interface

                                                               The USB interfaces requires 90 differential pair routing to the host side.
                                                               For USB operation, USB_VBUS is a mandatory connection. The host must ensure
                                                               USB_VBUS is provided before establishing USB communication.
                                                               When the USB interface is externally accessible, ESD protection is required on the
                                                               USB_VBUS, USB_D+ and USB_D- signals.


                                                                                                                                                                                                                                                           USB_VBUS
                                                                                                                                                                                                                  Common                                   US B_D+
                                                                                                                                                                                                                     mode
                                                                                                                                                                                                                     choke                                 USB_D‐

                                                                                                                                                                                                                                                                                                             HL781x


                                                               Figure 6-2:                       ESD Suppressors for USB FS

                                                               Sierra Wireless recommends using the following for ESD and EMI protection:
                                                               •                                                    ESD diodes— INNOCHIPS ULCE0505A015FR for USB data lines, and Diodes Inc
                                                                           D8V0L1B2LP3-7 for USB_VBUS
                                                               •                                                    Optional common mode choke for EMI protection, depending on customer require-
                                                                           ments— Panasonic EXC24CG900U


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               70                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 71

D e s i g n   G  u i d e  l i n  e s

                                           6.4 ESD Protection for I/Os

                                           ESD protection is highly recommended where module signals (GPIO, UART, H/W control,
                                           Indication, ADC, Clock) are externally accessible and potentially subjected to ESD by the
                                           user. Sierra Wireless recommends using Diodes Inc D8V0L1B2LP3-7.
                                           6.5 Hibernate—Isolation Requirements

                                           While the module is in Hibernate mode, the host platform (MCU) interfaces can remain
                                           powered.

                                           Important:                 To prevent these signals from back-powering the module, the host platform should
                                           make sure to isolate them—the signals should not be driven high (e.g. > 0.2 V).

                                            To ensure the host platform does not back-power the module:
                                           •                                                    The host can add a buffer circuit to isolate module I/O during Hibernate. Sierra
                                                    Wireless recommends using VGPIO to tristate I/O signals.
                                           •                                                    The MCU can tristate any I/O that does not have an external PU/PD.


                                           Note:                A buffer is not required in Lite Hibernate mode.

                                           If adding a buffer circuit, consider the signal type:
                                           •                                                    Bidirectional (Input/Output) s ignals— For module I/O signals (e.g. GPIOs), an analog
                                                    switch that can tri-state both the output and the input can be used (e.g. Texas Instru-
                                                    ments TMUX1511). As  shown in Figure         6-                   3, I/O signals connected to the buffer will be
                                                    tri-stated.
                                           •                                                    Directional (Input) signals— For module inputs (e.g. UART1_TX), a logic buffer with
                                                    output tri-state mode can be used (e.g. Texas  Instruments SN74LVC1G126). As
                                                    shown in Figure          6-                   4, the signal is controlled and, when disabled, the output signal is
                                                    tri-stated.


                                           Note:                Parts and usage descriptions above are intended as examples to assist the host platform
                                           designer in developing an appropriate solution for the platform. Selection and use of specific parts is
                                           the responsibility of the host platform designer.

                                            Control of the buffer circuit is based on the status of VGPIO— for details, see VGPIO
                                           Monitoring and Buffer Control.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               71                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 72

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


                                                                                                                                                                                                    Bi‐directional  buff er
                                                                                             Various signal
                                                                                                typ es  (GPIO,
                                                                                                   UART, etc .)


                                                                                                              VGPIO                                                                                  Bu  ffer  E N

                                                                                      HL781x                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  Customer Opt i ona l  Vo l ta g e Platform
                                                                                                                                       Detectio n Circuit

                                                                     Figure 6-3:                       Example-Buffer - Bidirectional Signal


                                                                                                                                                                                                Dir ecti on al  buffe r


                                                                                                    Inpu t signal
                                                                                           (e.g. UART1_TX)


                                                                                                               VGPIO                                                                            Bu ffer  EN

                                                                                        HL781x                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Customer Opt i onal  Vo l tag e  Platform
                                                                                                                                      Det ect io n Circuit
                                                                     Figure 6-4:                       Example-Buffer - Directional Signal

                                                                     6.5.1 VGPIO Monitoring and Buffer Control

                                                                     Because the host platform can remain powered in Hibernate and Reset states, the host
                                                                     platform must react quickly, when VGPIO transitions low, to ensure signals do not back-
                                                                     power the module.
                                                                     The host platform can monitor VGPIO to determine the HL781x module’s current
                                                                     operating mode— for details, see VGPIO.
                                                                     To ensure faster detection of VGPIO transitions, Sierra Wireless recommends adding an
                                                                     optional voltage detection circuit (as shown in Figure         6-                   3 and Figure                                        6-                   4) to monitor and
                                                                     detect the trans ition low, and then control (enable/disable) the associated buffer circuit.


                                                                     Note:                VGPIO can be used to directly connect to the buffer enable signal but the host platform must
                                                                     ensure that all host outputs are not driven high (i.e. > 0.2 V) before the module enters Hibernate
                                                                     mode.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               72                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 73

D e s i g n   G  u i d e  l i n  e s

                                             6.6 Radio Frequency Integration

                                             The HL781x is equipped with an external antenna.

                                             6.6.1 Antenna Matching Circuit

                                             A 50 line matching circuit between the module, the customer's board and the RF
                                             antenna is required as  shown in Figure         6-                   5.
                                             Because matching is dependent on the customer’s platform, values marked as ‘TBD’ for
                                             the recommended components must be determined by the customer.


                                                                                          RF_GNSS

                                                     HL781x


                                                                                                                                                      TBD
                                                                                           RF_MAIN
                                                                                                                                     TBD                  TBD               33 p F


                                             Figure 6-5:                       Antenna Connection

                                             Sierra Wireless recommends using the following ESD diodes:
                                             •                                                    Panasonic EZAEG1N50AC for RF_MAIN
                                             •                                                    Diodes Inc. D5V0X1B2LP3-7 for RF_GNSS

                                             6.6.2 RF Circuit

                                             The RF signal must be routed on the application board using tracks with a 50
                                             characteristic impedance.
                                             The characteristic impedance depends on the dielectric, the track width and the ground
                                             plane spacing.
                                             It is recommended to use stripline design if the RF path is  fairly long (more than3 cm),
                                             since microstrip design is not shielded. Consequently, the RF (transmit) signal may
                                             interfere with neighboring electronic circuits. In the s ame way, the neighboring electronics
                                             (micro-controllers, etc.) may interfere with the RF (receive) signal and degrade the
                                             reception performance.
                                             The RF trace on the development board is routed from the module antenna port to the RF
                                             connector (SMA). The RF trace is designed as a 50 coplanar s tripline and its length is
                                             24.8 mm.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               73                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 74

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


                                                                                                           The following drawings show the location of the Sierra Wireless HL781x on the
                                                                                                           development board, the routing cross section and the top view of the RF trace on the
                                                                                                           development board.


                         RF connector main                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 GPS connector


                                                                                                                                                                                                                                                                                                                                                                                                                                                                   Embedded module


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        RF connector
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        (AUX)


                                                                                                           Figure 6-6:                       Module Location on Development Board


                                                                                                                                                                                                 GPS connector
                                                                                                                                                                                                 W = 11.2 Mil
                                                                                                                                                                                                 G = 24 Mil

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        RF trace end vias
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        to connect to the
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        HL781x antenna
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        port at one end
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        and the RF
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        connector at the
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        other end.


                                                                                                                                                                                      Ground vias                                                                                                                                                                                                                                                                                                                                                                                                                                                        RF trace routing

                                                                                                           Figure 6-7:                       Development Board RF Trace Design


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               74                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 75

7: Reliability Specification                                                                                                                                                                                                                                                                                                                                                         7


                                                                The Sierra Wireless HL781x will be tested against the Sierra Wireless Industrial Reliability
                                                                Specification defined below.
                                                                7.1 Preconditioning Test

                                                                Per JESD22A113, this tests the preconditioning of non-hermetic surface mount devices
                                                                prior to reliability testing.

Ta b  l  e    7 - 1 :                     P r e c o  n d  i  t i o  n i  n  g    Te s  t

   De s i g n at  i o n                                                                                               C o nd i  t i o n

  Preconditioning Test PCRM                                                                                           2 reflow cycles with Tmax 245-250°C

                                                                7.2 Performance Test

Ta b  l  e    7 - 2 :                     P e r f o  r m a n  c e    Te  s t

   De s i g n at  i o n                                                                                               C o nd i  t i o n

  Performance Test                                                                                                    Standard: N/A
  PT3T & PTRT                                                                                                         Special conditions:
                                                                                                                      •                                                    Temperature:
                                                                                                                                   •                          Class A: -30°C to +70°C
                                                                                                                                   •                          Class B: -40°C to +85°C
                                                                                                                                   •                          Rate of temperature change: ± 3°C/min
                                                                                                                      •                                                    Recovery time: 3 hours

                                                                                                                      Operating conditions: Powered

                                                                                                                      Duration: 14 days


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               75                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 76

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n

                                                                        7.3 Aging Tests

Ta b  l  e    7 - 3 :                     A g i  n  g    Te  s t s

   De s i g n at  i o n                                                                                                             C o nd i  t i o n

   High Temperature Operating Life test                                                                                             Standard: IEC 60068-2-2, Test Bb
   HTOL
                                                                                                                                    Special conditions:
                                                                                                                                    •                                                    Temperature: +85°C
                                                                                                                                    •                                                    Temperature variation: 1°C/min

                                                                                                                                    Operating conditions: Powered ON with a power cycle of 45 minutes ON and 15
                                                                                                                                    minutes Idle

                                                                                                                                    Duration: 20 days


   Thermal Shock Test                                                                                                               Standard: IEC 60068-2-14, Test Na
   TSKT                                                                                                                             Special conditions:
                                                                                                                                    •                                                    Temperature: -40°C to +85°C
                                                                                                                                    •                                                    Temperature Variation: less than 30s
                                                                                                                                    •                                                    Number of cycles: 300
                                                                                                                                    •                                                    Dwell Time: 10 minutes

                                                                                                                                    Operating conditions: Unpowered

                                                                                                                                    Duration: 7 days

   Humidity Test                                                                                                                    Standard: IEC 60068-2-3, Test Ca
   HUT                                                                                                                              Special conditions:
                                                                                                                                    •                                                    Temperature: +85°C
                                                                                                                                    •                                                    RH: 85%

                                                                                                                                    Operating conditions: Powered on, DUT is powered up for 15 minutes and OFF
                                                                                                                                    for 15 minutes.

                                                                                                                                    Duration: 10 days


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               76                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 77

R e l  i a b i  l i t  y  S p  e c i f  i c a t  i o  n

                                                                    7.4 Characterization Tests


   De s i g n at  i o n                                                                                                     C o nd i  t i o n

   Low Temperature and Cold Start                                                                                           Special conditions:
   Cycles                                                                                                                   •                                                    Temperature: -40°C
   LTCS                                                                                                                     •                                                    AT commands read or write memory

                                                                                                                            Operating conditions: 5 mins powered ON, 30 mins powered OFF (1 power
                                                                                                                            cycle)

                                                                                                                            Duration: 5 days

   Component Solder Wettability CSW                                                                                         Standard: JESD22 - B102, Method 1/Condition C, Solderability Test Method

                                                                                                                            Special conditions:
                                                                                                                            •                                                    Test method: Surface mount process simulation test (preconditioning 16 h
                                                                                                                                          ±30 minutes dry bake)

                                                                                                                            Operating conditions: Unpowered

                                                                                                                            Duration: 1 day


   Unprotected Free Fall Test FFT1                                                                                          Standard: IEC 680068-2-32, Test Ed

                                                                                                                            Special conditions:
                                                                                                                            •                                                    Number of drops: 6 drops per unit (1 drop per direction: ±X, ±Y, ±Z)
                                                                                                                            •                                                    Height: 1m

                                                                                                                            Operating conditions: Unpowered

                                                                                                                            Duration: 1 day


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               77                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 78

8: Legal Information                                                                                                                                                                                                                    8


                                        8.1 Disposing of the Product

                                        This electronic product is subject to the EU Directive 2012/19/EU for Waste
                                        Electrical and Electronic Equipment (WEEE). As such, this product must not
                                        be disposed of at a municipal waste collection point. Please refer to local
                                        regulations for directions on how to dispose of this product in an
                                        environmental friendly manner.
                                        8.2 Compliance Acceptance and Certification

                                        The Sierra Wireless HL7810/ /Sierra Wireless HL7812 is designed to be compliant with
                                        the 3GPP Release 14 E-UTRA Specification for Mobile Terminated Equipment.The Sierra
                                        Wireless HL7812 is designed to be compliant with the 3GPP Release 9 UTRA and
                                        Release 13 E-UTRA Specifications for Mobile Terminated Equipment.
                                        Final regulatory and operator certification requires regulatory agency testing and approval
                                        with the fully integrated UE host device incorporating the Sierra Wireless HL7810/ /Sierra
                                        Wireless HL7812 module.
                                        The OEM host device and, in particular, the OEM antenna design and implementation will
                                        affect the final product functionality, RF performance, and certification test results.


                                        Note:                Tests that require features not supported by the Sierra Wireless HL7810 / /Sierra Wireless
                                        HL7812 (as defined by this document) are not supported.

                                        8.3 Regulatory and Industry Approvals/
                                        Certifications

                                        The Sierra Wireless HL7810/ /Sierra Wireless HL7812 module is designed to meet, and
                                        upon commercial release, will meet the requirements of the following regulatory bodies
                                        and regulations, where applicable:
                                        •                                                    Federal Communications Commission (FCC) of the United States
                                        •                                                    The Certification and Engineering Bureau of Industry Canada (IC)
                                        •                                                    (HL7810) The National Communications Commission (NCC) of Taiwan, Republic of
                                                China
                                        •                                                    Regulatory Compliance Mark (RCM), Electrical Regulatory Authorities Council
                                                (Australia and New Zealand)
                                        •                                                    Radio Equipment Directive (RED) of the European Union
                                        •                                                    Ministry of Internal Affairs and Communications (MIC) of Japan
                                        Upon commercial release, the following industry certifications will have been obtained,
                                        where applicable:
                                        •                                                    GCF
                                        •                                                    PTCRB


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               78                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 79

L e g a  l  I n f  o r m a t  i o  n


                                           Additional certifications and details on specific country approvals may be obtained upon
                                           customer request — contact your Sierra Wireless account representative for details.
                                           Additional testing and certification may be required for the end product with an embedded
                                           HL7810 / /HL7812 module and are the responsibility of the OEM. Sierra Wireless offers
                                           professional services-based assistance to OEMs  with the testing and certification
                                           process, if required.
                                           8.4 Japan Radio and Telecom Approval

                                           The HL7810 and HL7812 modules have been granted Japan radio and telecom approvals
                                           with the approval numbers shown below.
                                           •                                                    HL7810:


                                           •                                                    HL7812:


                                           8.5 Important Compliance Information for
                                           North American Users

                                           The Sierra Wireless HL7810 and Sierra Wireless HL7812 modules have been granted
                                           modular approval for mobile applications under:
                                           •                                                    Sierra Wireless HL7810— FCC ID: N7NHL78A
                                           •                                                    Sierra Wireless HL7812— FCC ID: N7NHL78C
                                           •                                                    Sierra Wireless HL7810— IC ID: 2417C-HL78A
                                           •                                                    Sierra Wireless HL7812— IC ID: 2417C-HL78C
                                           Integrators may use these modules in their end products without additional FCC/IC
                                           (Industry Canada) certification if they meet the following conditions. Otherwise, additional
                                           FCC/IC approvals must be obtained.
                                           1.                                  The end product must us e the RF trace design approved with the HL7810 or HL7812.
                                                    The Gerber file of the trace design can be obtained from Sierra Wireless upon
                                                    request.
                                           2.                                  At least 20 cm separation distance between the antenna and the user’s body must be
                                                    maintained at all times.
                                           3.                                  To comply with FCC/IC regulations limiting both maximum RF output power and
                                                    human exposure to RF radiation, the maximum antenna gain including cable loss in a
                                                    mobile-only exposure condition must not exceed the limits stipulated in Table          8-                   1.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               79                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 80

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


Ta b  l  e    8 - 1 :                     P r o d  u  c t   N  a m e  A n  t e n  n a    G  a i n     S p  e c i  f i  c a t i  o n  s

  De v i c e                                       Te ch n o l o g y                   B a n d                             F r e q u en c y   ( M H z )                      M a x i m u m   a n t e n na   g a i n   (  dB i  )

                                                                                                                                                                             S t  an d al  on e                                C o l l  oc a t e d

                                                                                       B2                                 1850–1910                                          6                                                 6

                                                                                       B4                                 1710–1755                                          5.5                                               5.5

                                                                                       B5                                 824-829                                            6                                                 4

  Sierra                                                                               B8                                 897.5-900.5                                        6                                                 4
  Wireless HL7810                                                                      B12                                699-716                                            6                                                 4
                                                   LTE
  Sierra                                                                               B13                                777-787                                            6                                                 4
  Wireless HL7812                                                                      B25                                1850-1915                                          6                                                 6

                                                                                       B26                                814-849                                            6                                                 4

                                                                                       B66                                1710-1780                                          5.5                                               5.5

                                                                                       B85                                698-716                                            6                                                 4

  Sierra                                                                               GPRS G850                          824-849                                            3                                                 1
  Wireless HL7812                                  GPRS
                                                                                       GPRS G1900                         1850-1910                                          2                                                 2

                                                 4.                                  The HL7810 or HL7812 may transmit simultaneously with other collocated radio
                                                           transmitters within a host device, provided the following conditions are met:
                                                           ·                    Each collocated radio transmitter has been certified by FCC/IC for mobile appli-
                                                                cation.
                                                           ·                    At least 20 cm separation distance between the antennas of the collocated trans-
                                                                mitters and the user’s body must be maintained at all times.
                                                           ·                    The radiated power of a collocated transmitter must not exceed the EIRP limit stipu-
                                                                lated in Table          8-                   2.

                                                 Ta b  l  e    8 - 2 :                     H L  7 8 1 0 ,     H L 7  8 1 2    C o  l l  o  c a t e d    R  a d i  o    Tr  a n  s m i  tt e  r    S p  e c i  f i  c a t i  o n  s

                                                    De v i c e                                                  Te ch n o l o g y                          F r e q ue n c y   ( M H z )                           E I R P   Li  m i t    ( d B m )

                                                                                                                WLAN 2.4 GHz                               2400–2500                                              30

                                                    Collocated transmittersa                                    WLAN 5 GHz                                 5150–5850                                              30

                                                                                                                BT                                         2400–2500                                              16

                                                         a.                           Valid collocated transmitter combinations: WLAN+BT; WiGig+BT. (WLAN+WiGig+BT is not permit-
                                                                 ted.)

                                                 5.                                  A label must be affixed to the outside of the end product into which the HL7810 or
                                                           HL7812 is incorporated, with a statement similar to the following:
                                                           ·                    (HL7810)— This device contains FCC ID: N7NHL78A / IC: 2417C-HL78A
                                                           ·                    (HL7812)— This device contains FCC ID: N7NHL78C / IC: 2417C-HL78C


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               80                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 81

L e g a  l  I n f  o r m a t  i o  n


                                                             6.                                  A user manual with the end product must clearly indicate the operating requirements
                                                                          and conditions that must be observed to ensure compliance with current FCC/IC RF
                                                                          exposure guidelines.
                                                             The end product with an embedded HL7810 or HL7812 may also need to pass the FCC
                                                             Part 15 unintentional emission testing requirements and be properly authorized per FCC
                                                             15. If this module is intended for use in a portable device, you are responsible for
                                                             separate approval to satisfy the SAR requirements of FCC Part 2.1093 and IC RSS-102.
                                                             8.6 Legal Information – Taiwan NCC Statement

                                                             減少電磁波影響，請妥適使用.


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               81                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 82

9
9: Appendix


                                                           For more details, several references can be consulted, as detailed below.
                                                           9.1 Website Support

                                                           Check source.sierrawireless.com for the latest documentation available for HL781x
                                                           modules.
                                                           9.2 Reference Documents

                                                           [1]                    Sierra Wireless HL78xx Series Customer Process Guidelines (Doc#          41112095)
                                                           [2]                    Sierra Wireless HL78xx AT Commands Interface Guide (Doc#          41111821)
                                                           [3]                    AirPrime HL Series Development Kit User Guide (Doc#          4114877)
                                                           [4]                    HL78xx Low Power Modes Application Note (Doc#          2174229)
                                                           [5]                    HL78xx Customization Guide Application Note (Doc#          2174213)
                                                           [6]                    Sierra Wireless Ready-to-Connect Module Integration Guide (Doc#          41113385)
                                                           [7]                    Sierra Wireless HL78xx Firmware Update Methods Application Note (Doc# 2174259)


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               82                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 83

A p p e n d i x

                                                 9.3 Terms and Abbreviations


                                                   Ter  m   /  A b b r e v i a t i o n                        De f i  n i t i o n

                                                   Active state                                               All sub-systems, including the MAP process, are up and running. User
                                                                                                              can access module via UART (e.g. to configure/query module settings/
                                                                                                              states, and send/receive data.

                                                   ADC                                                        Analog to Digital Converter

                                                   AT                                                         Attention (prefix for modem commands)

                                                   AT-READY                                                   Module is initialized and ready to accept AT commands

                                                   Cat-M1                                                     LTE enhanced Machine Type Communication (eMTC) Category M1
                                                                                                              (3GPP Release 14)

                                                   Cat-NB1                                                    LTE Narrowband Internet of Things (NB-IoT) Category NB1 (3GPP
                                                                                                              Release 14)

                                                   CF3                                                        Common Flexible Form Factor

                                                   CLK                                                        Clock

                                                   DTR                                                        Data Terminal Ready

                                                   DRX                                                        Discontinuous Reception

                                                   eDRX                                                       Extended DRX

                                                   EIRP                                                       Equivalent Isotropically Radiated Power

                                                   EMC                                                        Electro-Magnetic Compatibility

                                                   EMI                                                        Electro-Magnetic Interference

                                                   EN                                                         Enable

                                                   ESD                                                        Electro-Static Discharges

                                                   ETSI                                                       European Telecommunications Standards Institute

                                                   GLONASS                                                    Global Navigation Satellite System

                                                   GND                                                        Ground

                                                   GNSS                                                       Global Navigation Satellite System

                                                   GPIO                                                       General Purpose Input Output

                                                   GPRS                                                       General Packet Radio Service

                                                   GPS                                                        Global Positioning System

                                                   GSM                                                        Global System for Mobile communications

                                                   Hi Z                                                       High impedance (Z)

                                                   IC                                                         Industry Canada

                                                   I/O                                                        Input/Output

                                                   LED                                                        Light Emitting Diode


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               83                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 84

P r o d u c t     T e c h n  i c a l    S p e c i  f i  c a t i  o n


                                                              Ter  m   /  A b b r e v i a t i o n                                    De f i  n i t i o n

                                                             MAX                                                                    Maximum

                                                             MIN                                                                    Minimum

                                                             N/A                                                                    Not Applicable

                                                             PA                                                                     Power Amplifier

                                                             PC                                                                     Personal Computer

                                                             PCB                                                                    Printed Circuit Board

                                                             PCL                                                                    Power Control Level

                                                             periodic TAU                                                           See TAU

                                                             PSM                                                                    Power Save Mode

                                                             PTW                                                                    Paging Transmission Window

                                                             PWM                                                                    Pulse Width Modulation

                                                             RF                                                                     Radio Frequency

                                                             RST                                                                    Reset

                                                             RTC                                                                    Real Time Clock

                                                             RX                                                                     Receive

                                                             SIM                                                                    Subscriber Identification Module

                                                             SINR                                                                   Signal to Interference plus Noise Ratio

                                                             SW                                                                     Software

                                                             TAU                                                                    Tracking Area Update
                                                                                                                                    •                                                    TAU—An update sent when the PSM parameters are changed or
                                                                                                                                                when the module changes location.
                                                                                                                                    •                                                    periodic TAU—Sent by the module to notify its availability to the
                                                                                                                                                network.

                                                             TBC                                                                    To Be Confirmed

                                                             TBD                                                                    To Be Determined To Be Defined

                                                             TP                                                                     Test Point

                                                             TX                                                                     Transmit

                                                             TYP                                                                    Typical

                                                             UART                                                                   Universal Asynchronous Receiver-Transmitter

                                                             UICC                                                                   Universal Integrated Circuit Card

                                                             USB                                                                    Universal Serial Bus

                                                             UIM                                                                    User Identity Module

                                                             UMTS                                                                   Universal Mobile Telecommunications System


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               84                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133

## Page 85

A p p e n d i x


                                                                                     Ter  m   /  A b b r e v i a t i o n                                                               De f i  n i t i o n

                                                                                     USIM                                                                                             UMTS Subscriber Identity Module

                                                                                     VBAT_BB                                                                                          Main Supply Voltage from Battery or DC Adapter

                                                                                     VSWR                                                                                             Voltage Standing Wave Ratio

                                                                                 9.4 Ordering Information


    M od e l   N a m e                                                              D e sc r i  p t i o n                                                                                                                             P a r t    N u m b er

   HL7810                                                                          HL7810 embedded module                                                                                                                            Contact Sierra Wireless for the latest SKU.

   HL7812                                                                          HL7812 embedded module                                                                                                                            Contact Sierra Wireless for the latest SKU.

   DEV-KIT                                                                         HL781x Development Kit                                                                                                                            6001210


Rev. 9                     August 2023                                                                                                                                                                                                                                                                                                                                                                                                                                                                               85                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       41114133
