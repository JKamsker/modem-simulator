# thirdparty-digikey-airprime-hl78xx-devkit

> Generated from the sibling PDF for local reference search and review.
> Source PDF: `thirdparty-digikey-airprime-hl78xx-devkit.pdf`
> Source SHA-256: `901902316fed5a228dd13707208b3ba39a200516d7580a68a28997d2af5d383c`
> Generated: 2026-07-02
> Extractor: pypdf 6.14.2
> Note: This is text extraction only; tables, columns, images, and scanned pages may need manual verification against the PDF.

## Extraction Summary

- Pages: 68
- Pages without extractable text: 0

## PDF Metadata

| Key | Value |
|---|---|
| CreationDate | D:20221107140847 |
| Creator | PDFium |
| Producer | PDFium |

## Page 1

AirPrime HL78xx


                  Development Kit User Guide


                                                         41112256
                                                             3.0
                                                   December 04, 2018

## Page 2

Development Kit User Guide
    Important Notice

    Due to the nature of wireless communications, transmission and reception of data can never be
    guaranteed. Data may be delayed, corrupted (i.e., have errors) or be totally lost. Although significant
    delays or losses of data are rare when wireless devices such as the Sierra Wireless modem are used
    in a normal manner with a well-constructed network, the Sierra Wireless modem should not be used
    in situations where failure to transmit or receive data could result in damage of any kind to the                 user or
    any other party, including but not limited to personal injury, death, or loss of property. Sierra Wireless
    accepts no responsibility for damages of any kind resulting from delays or errors in data transmitted or
    received using the Sierra Wireless modem, or for failure of the Sierra Wireless modem to transmit or
    receive such data.
    Safety and Hazards

    Do not operate the Sierra Wireless modem in areas where cellular modems are not advised without
    proper device certifications. These areas include environments where cellular radio can interfere such
    as explosive atmospheres, medical equipment, or any other equipment which may be susceptible to
    any form of radio interference. The Sierra Wireless modem can transmit signals that could interfere
    with this equipment. Do not operate the Sierra Wireless modem in any aircraft, whether the aircraft is
    on the ground or in flight. In aircraft, the Sierra Wireless modem MUST BE POWERED OFF. When
    operating, the Sierra Wireless modem can transmit signals that could interfere with various onboard
    systems.

    Note:          Some airlines may permit the use of cellular phones while the aircraft is on the ground and the door
                   is open. Sierra Wireless modems may be used at this time.
    The driver or operator of any     vehicle should not operate the    Sierra Wireless    modem while in control of
    a vehicle. Doing     so will detract from the driver or operator’s control and operation of that vehicle. In
    some states and provinces, operating such communications devices while in control of a vehicle is an
    offence.
    Limitations of Liability

    This manual is provided “as is”. Sierra Wireless makes no warranties of any     kind, either expressed or
    implied, including any implied warranties of merchantability, fitness for a particular   purpose, or
    noninfringement. The recipient of the manual shall endorse all risks arising from its use.
    The information in this manual is subject to change without notice and     does not represent a
    commitment on the part    of Sierra Wireless. SIERRA    WIRELESS AND ITS AFFILIATES
    SPECIFICALLY DISCLAIM LIABILITY FOR ANY AND ALL DIRECT, INDIRECT, SPECIAL,
    GENERAL, INCIDENTAL, CONSEQUENTIA   L, PUNITIVE    OR EXEMPLARY DAMAGES INCLUDING,
    BUT NOT LIMITED TO, LOSS OF PROFITS OR REVENUE OR ANTICIPATED PROFITS OR
    REVENUE ARISING OUT OF THE USE OR INABILITY TO USE ANY SIERRA WIRELESS
    PRODUCT, EVE N IF SIERRA WIRELESS    AND/OR ITS AFFILIATES HAS BEEN ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGES OR THEY ARE FORESEEABLE OR FOR CLAIMS BY ANY
    THIRD PARTY.
    Notwithstanding the foregoing, in no eve  nt shall Sierr   a Wireless    and/or its affiliates aggregate liability
    arising under or in connection     with the Sierra Wireless     product, regardless of the number of events,
    occurrences, or claims giving rise to liability, be in excess   of the price paid by    the purchaser for the
    Sierra Wireless product.


41112256                                    Rev 3.0                                          December 04, 2018                2

## Page 3

Development Kit User Guide

    Patents

    This product may contain technology developed by or for Sierra Wireless Inc.
    This product is manufactured or sold by Sierra Wireless Inc. or its affiliates under one or more patents
    licensed from MMP Portfolio Licensing.
    Copyright

    © 2018 Sierra Wireless. All rights reserved.
    Trademarks

    Sierra Wireless®, AirPrime®, AirLink®, AirVantage®, WISMO®, ALEOS® and the Sierra Wireless and
    Open AT logos are registered trademarks of Sierra Wireless, Inc. or one of its subsidiaries.
    Watcher® is a registered trademark of NETGEAR, Inc., used under license.
    Windows® and Windows Vista® are registered trademarks of Microsoft Corporation.
    Macintosh® and Mac OS X® are registered trademarks of Apple Inc., registered in the U.S. and other
    countries.
    QUALCOMM® is a registered trademark of QUALCOMM Incorporated. Used under license.

    Other trademarks are the property of their respective owners.
    Contact Information


      Sales information and technical support, including          Web: sierrawireless.com/company/contact-us/
      warranty and returns                                        Global toll-free number: 1-877-687-7795
                                                                  6:00 am to 5:00 pm PST

      Corporate and product information                           Web: sierrawireless.com


41112256                                   Rev 3.0                                         December 04, 2018               3

## Page 4

Development Kit User Guide

    Document History


      Version       Date                     Updates

      1.0           August 06, 2018          Creation
                                             Updated:
      1.1           August 08, 2018              •    Document formatting
                                                 •    3.8 GPIO
      2.0           November 06, 2018        Added 4.4 Low Power Consumption Measurement
                                             Updated Figure 11 Power Supply Configurations
      3.0           December 04, 2018        Updated for development kit board Rev4


41112256                                  Rev 3.0                                         December 04, 2018               4

## Page 5

Contents

       1.     OVERVIEW ........................................................................................................ 10
       2.     GENERAL DESCRIPTION ................................................................................. 11
              2.1.          RoHS Compliance ............................................................................................................. 11
              2.2.          Development Kit ................................................................................................................ 12
                     2.2.1.           Features ................................................................................................................... 14
                     2.2.2.           Connectors and Component Placement .................................................................. 15
                     2.2.3.           Snap-In Connector ................................................................................................... 21
                     2.2.4.           Test Ports ................................................................................................................. 23
       3.     INTERFACES ..................................................................................................... 27
              3.1.          Power ................................................................................................................................. 27
                     3.1.1.           Power Supply ........................................................................................................... 27
                     3.1.2.           Module Orientation Detection .................................................................................. 30
                     3.1.3.           Internal Power Supply .............................................................................................. 31
                     3.1.4.           POWER_ON_N ....................................................................................................... 32
              3.2.          Control Functions .............................................................................................................. 33
                     3.2.1.           FAST_SHUTDOWN ................................................................................................ 33
                     3.2.2.           WAKE-UP ................................................................................................................ 34
                     3.2.3.           RESET_IN_N ........................................................................................................... 35
              3.3.          USB (Main) ........................................................................................................................ 36
              3.4.          Audio .................................................................................................................................. 37
              3.5.          UIM/SIM1 ........................................................................................................................... 40
              3.6.          UART1 ............................................................................................................................... 42
                     3.6.1.           RS232-UART1 ......................................................................................................... 42
                     3.6.2.           USB-UART1 ............................................................................................................. 43
                     3.6.3.           RS232-UART1 or USB-UART1 ............................................................................... 44
              3.7.          UART0 ............................................................................................................................... 45
                     3.7.1.           RS232-UART0 ......................................................................................................... 45
                     3.7.2.           USB-UART0 ............................................................................................................. 46
                     3.7.3.           RS232-UART0 or USB-UART0 ............................................................................... 47
              3.8.          GPIO .................................................................................................................................. 48
                     3.8.1.           GPIO Control Signals .............................................................................................. 49
              3.9.          ADC ................................................................................................................................... 50
              3.10.         EXT_GPS_LNA_EN .......................................................................................................... 51
              3.11.         Antenna Detection Circuit .................................................................................................. 52
              3.12.         Clock Out ........................................................................................................................... 53
              3.13.         TX-ON ................................................................................................................................ 54
              3.14.         VGPIO ............................................................................................................................... 55
              3.15.         RTC Backup Battery .......................................................................................................... 56
              3.16.         RF and GNSS Antenna ..................................................................................................... 57


41112256                                                       Rev 3.0                                                               December 04, 2018                               5

## Page 6

Development Kit User Guide

            3.17.        Diversity Antenna .............................................................................................................. 58
            3.18.        JTAG .................................................................................................................................. 59
            3.19.        Board to Board Connector ................................................................................................. 60
      4.    GETTING STARTED .......................................................................................... 61
            4.1.         Setting Up .......................................................................................................................... 61
            4.2.         RF Communications .......................................................................................................... 63
                   4.2.1.          Configure the COM Port .......................................................................................... 63
                   4.2.2.          Make a Voice Call .................................................................................................... 64
            4.3.         GNSS Communications ..................................................................................................... 65
            4.4.         Low Power Consumption Measurement ........................................................................... 66
                   4.4.1.          Global Current Consumption ................................................................................... 66
                   4.4.2.          Isolated Development Kit and Module Power Supply ............................................. 66
                   4.4.3.          All Power Supplies are Isolated ............................................................................... 66
      5.    ESD PROTECTION ............................................................................................ 67
      6.    REFERENCE DOCUMENTS .............................................................................. 68
      7.    SCHEMATICS, PCA DIAGRAMS AND SILKSCREEN ..................................... 69


41112256                                                 Rev 3.0                                                         December 04, 2018                           6

## Page 7

List of Figures


      Figure 1.            Development Kit – Top View ........................................................................................... 12
      Figure 2.            Development Kit – Bottom View ...................................................................................... 13
      Figure 3.            Available Connectors and Components – Top ................................................................ 15
      Figure 4.            Available Connectors and Components – Bottom .......................................................... 16
      Figure 5.            Snap-In Connector .......................................................................................................... 21
      Figure 6.            Snap-In Connector with Interposer ................................................................................. 21
      Figure 7.            Snap-In Connector with Interposer and an HL78xx Module ........................................... 21
      Figure 8.            Snap-In Connector with HL78xx Module and Cover ....................................................... 22
      Figure 9.            VBATT_APPLI Connector (on the top side of the Development Kit) .............................. 27
      Figure 10.           VBATT_BB and VBATT_RF Connectors (on the bottom side of the Development Kit)                                                        . 27
      Figure 11.           Power Supply Configurations .......................................................................................... 28
      Figure 12.           Single Power Supply Configuration ................................................................................. 28
      Figure 13.           Separate Power Supplies for the Development Kit and Module Configurations ............. 28
      Figure 14.           Separate VBATT_APPLI, VBATT_BB and VBATT_RF Configuration ........................... 29
      Figure 15.           Module in Correct Orientation ......................................................................................... 30
      Figure 16.           FLASH_LED for Module in Correct Orientation .............................................................. 30
      Figure 17.           Module in Incorrect Orientation or No Module Present ................................................... 30
      Figure 18.           FLASH_LED for Module in Incorrect Orientation or No Module Present ........................ 30
      Figure 19.           Internal Power Supply ..................................................................................................... 31
      Figure 20.           POWER_ON_N Pushbutton ............................................................................................ 32
      Figure 21.           FAST_SHUTDOWN Pushbutton ..................................................................................... 33
      Figure 22.           WAKE-UP Pushbutton .................................................................................................... 34
      Figure 23.           RESET_IN_N Pushbutton ............................................................................................... 35
      Figure 24.           Micro-AB USB Connector ................................................................................................ 36
      Figure 25.           Main USB Interface ......................................................................................................... 36
      Figure 26.           Audio Interface ................................................................................................................ 37
      Figure 27.           4-pin RJ22 Handset Connector for Audio Signals ........................................................... 37
      Figure 28.           Audio Jack Connector for Audio Signals ......................................................................... 38
      Figure 29.           SIM1 Interface (bottom side) ........................................................................................... 40
      Figure 30.           SIM1 Interface (top side) ................................................................................................. 40
      Figure 31.           DB-9 Female Connector .................................................................................................. 42
      Figure 32.           RS232-UART1 Interface ................................................................................................. 42
      Figure 33.           USB-UART1 Interface ..................................................................................................... 43
      Figure 34.           RS232-UART1 or USB-UART1 Switch Configuration..................................................... 44
      Figure 35.           RS232-UART0 Interface ................................................................................................. 45
      Figure 36.           USB-UART0 Interface ..................................................................................................... 46
      Figure 37.           RS232-UART0 or USB-UART0 Switch Configuration..................................................... 47

41112256                                                Rev 3.0                                                         December 04, 2018                         7

## Page 8

Development Kit User Guide

      Figure 38.            GPIO Signals ................................................................................................................... 48
      Figure 39.            GPIOs Control Switches .................................................................................................. 49
      Figure 40.            ADC Signals .................................................................................................................... 50
      Figure 41.            EXT_GPS_LNA_EN Signal ............................................................................................. 51
      Figure 42.            RF and GNSS Antenna Detection ................................................................................... 52
      Figure 43.            Clock Out Signals ............................................................................................................ 53
      Figure 44.            TX-ON Signal .................................................................................................................. 54
      Figure 45.            VGPIO Signal .................................................................................................................. 55
      Figure 46.            RTC Backup Battery ........................................................................................................ 56
      Figure 47.            RF and GNSS Antenna Connectors ............................................................................... 57
      Figure 48.            RF Diversity Antenna Connector ..................................................................................... 58
      Figure 49.            JTAG Connector .............................................................................................................. 59
      Figure 50.            Board To Board Connectors ............................................................................................ 60
      Figure 51.            Fully Setup Development Kit with a UART Connection .................................................. 62
      Figure 52.            Fully Setup Development Kit with a USB Connection ..................................................... 62
      Figure 53.            AT Communication with the HL78xx ............................................................................... 63


41112256                                                    Rev 3.0                                                           December 04, 2018                            8

## Page 9

List of Tables

      Table 1.              Supported Module Variants ............................................................................................. 10
      Table 2.              Connector and Switch Description .................................................................................. 17
      Table 3.              Available Connector, Switch and Jumper Solder Pads ................................................... 18
      Table 4.              Available Tests Points ..................................................................................................... 19
      Table 5.              Development Kit Test Ports ............................................................................................. 23
      Table 6.              Power Supply Pin Description ......................................................................................... 29
      Table 7.              Power Supply Electrical Characteristics .......................................................................... 29
      Table 8.              Bad Configuration ............................................................................................................ 30
      Table 9.              Internal Power Supply Description .................................................................................. 31
      Table 10.             POWER_ON_N Pin Description...................................................................................... 32
      Table 11.             FAST_SHUTDOWN Pin Description ............................................................................... 33
      Table 12.             WAKE-UP Pin Description .............................................................................................. 34
      Table 13.             RESET_IN_N Pin Description ......................................................................................... 35
      Table 14.             Main USB Connector Pin Description ............................................................................. 36
      Table 15.             Main USB Pin Description ............................................................................................... 36
      Table 16.             Audio Handset Connector Pin Description ...................................................................... 38
      Table 17.             Microphone (CN601) Connector Pin Description ............................................................ 38
      Table 18.             Earphone (CN600) Connector Pin Description ............................................................... 38
      Table 19.             PCM Pin Description ....................................................................................................... 39
      Table 20.             SIM1 Connector Pin Description ..................................................................................... 40
      Table 21.             SIM1 Pin Description ....................................................................................................... 41
      Table 22.             RS232-UART1 Connector Pin Description ..................................................................... 42
      Table 23.             USB-UART1 Connector Pin Description ......................................................................... 43
      Table 24.             UART1 Pin Description ................................................................................................... 44
      Table 25.             RS232-UART0 Connector Pin Description ..................................................................... 45
      Table 26.             USB-UART0 Connector Pin Description ......................................................................... 46
      Table 27.             UART0 Pin Description ................................................................................................... 47
      Table 28.             GPIO Pin Description ...................................................................................................... 48
      Table 29.             GPIO Control Switch Configuration ................................................................................. 49
      Table 30.             ADC Pin Description ........................................................................................................ 50
      Table 31.             EXT_GPS_LNA_EN Pin Description .............................................................................. 51
      Table 32.             RF Antenna Detection Pin Description ............................................................................ 52
      Table 33.             GNSS Antenna Detection Pin Description ...................................................................... 52
      Table 34.             Clock Out Pin Description ............................................................................................... 53
      Table 35.             TX-ON Pin Description .................................................................................................... 54
      Table 36.             VGPIO Pin Description .................................................................................................... 55
      Table 37.             VBAT_RTC Pin Description ............................................................................................ 56

41112256                                                   Rev 3.0                                                          December 04, 2018                            9

## Page 10

1.        Overview


    This document describes the AirPrime HL78xx Series Development Kit Rev4 (PCB board version:
    5303247) and how it integrates with the AirPrime HL78xx series of embedded modules via a specific
    snap-in connector. It also briefly describes the different interfaces and peripheral connections
    supported by the Development Kit and provides schematics to facilitate the user’s understanding and
    configuration of the Development Kit board for their own application use.
    The Development Kit may be used to develop both software and hardware applications based on
    embedded modules from the AirPrime HL78xx series.
    The following table enumerates the different HL78xx variants that can be used with the Development
    Kit.


    Table 1.     Supported Module Variants

      Variant Name          Description

      HL7800                1.8V, LTE Cat-M1 and Cat-NB1, and capable with GLONASS support
      HL7802                1.8V, LTE Cat-1 and Cat-NB1, GSM/GPRS class 10, E-GSM 900/DCS 1800, and capable
                            with GLONASS support


    For more information about the AirPrime HL78xx series of embedded modules, refer to the product
    technical specifications listed in section 6 Reference Documents.


41112256                                    Rev 3.0                                          December 04, 2018              10

## Page 11

2.       General Description


    This section gives a brief overview of the Development Kit and briefly describes the interfaces and
    special jumper pads available; and lists all available test points on the Development Kit board.

    2.1.          RoHS Compliance

    The AirPrime HL78xx Series Development Kit Rev4 is compliant with RoHS (Restriction of Hazardous
    Substances in Electrical and Electronic Equipment) Directive 2011/65/EU which sets limits for the use
    of certain restricted hazardous substances. This directive states that “from 1st July 2006, new
    electrical and electronic equipment put on the market does not contain lead, mercury, cadmium,
    hexavalent chromium, polybrominated biphenyls (PBB) or polybrominated diphenyl ethers (PBDE)”.
    The AirPrime HL78xx series of embedded modules are also compliant with this directive.


41112256                                  Rev 3.0                                       December 04, 2018              11

## Page 12

Development Kit User Guide                                                                      General Description

    2.2.         Development Kit


    Figure 1.    Development Kit – Top View


41112256                                Rev 3.0                                     December 04, 2018            12

## Page 13

Development Kit User Guide                                                                                    General Description


     Figure 2.      Development Kit – Bottom View


41112256                                      Rev 3.0                                            December 04, 2018                13

## Page 14

Development Kit User Guide                                                                                General Description

    2.2.1.            Features

    Interfaces available on the Development Kit board include:
         •    Power supply connectors
              ▪    Module’s radio frequency main supply (VBATT_RF)
              ▪    Module’s baseband main supply (VBATT_BB)
              ▪    Application development kit’s main supply (VBATT_APPLI)
         •    Automatic detection of module’s wrong orientation
         •    ON/OFF switch
         •    RESET_IN_N pushbutton
         •    WAKE-UP switch
         •    FAST_SHUTDOWN pushbutton
         •    Tests points (TP) to access all signals of the embedded module
         •    Main serial link, selectable by RS232 or USB connectors, for UART1 function with full signals
         •    Auxiliary serial link, selectable by RS232 or USB connectors, for UART0 function with 4
              signals (debug interface)
         •    Full speed main USB connector
         •    SIM 1.8V (with SIM presence management)
         •    Audio connectors: handset, microphone/loudspeaker
         •    PCM digital/analog audio via external codec
         •    Audio amplifier selectable or not via switch
         •    GPIOs
         •    ADCs
         •    TX-ON (RF transmit signal)
         •    System 26Mhz clock out
         •    Real time 32Khz clock out
         •    VGPIO reference voltage out
         •    8x DIP switches for GPIO logic input/output control
         •    LEDs for several indications
         •    RTC back-up battery
         •    JTAG connector
         •    RF connector and detection circuit antenna
         •    GNSS connector and detection circuit antenna
         •    LNA enable by GNSS
         •    Snap-in connector (for plugging in the HL78xx series modules)
         •    External board-to-board connectors (unsoldered by default)
         •    Diversity 4G RF connector antenna (unsoldered by default)

    Refer to section 3 Interfaces for detailed information about these interfaces.

41112256                                    Rev 3.0                                          December 04, 2018              14

## Page 15

Development Kit User Guide                                                                        General Description

    2.2.2.          Connectors and Component Placement

    Refer to the following figure for the location of connectors and other components on the Development
    Kit.


    Figure 3.     Available Connectors and Components – Top


41112256                                Rev 3.0                                       December 04, 2018            15

## Page 16

Development Kit User Guide                                                                                     General Description


     Figure 4.      Available Connectors and Components – Bottom


41112256                                      Rev 3.0                                            December 04, 2018                 16

## Page 17

Development Kit User Guide                                                                                General Description

    The following table describes the connectors and switches available on the Development Kit and the
    table after describes the different connections available.


    Table 2.     Connector and Switch Description

      Connector/Switch             Description                                            HL7800             HL7802

      SW200                        FAST_SHUTDOWN push button                              ✓                  ✓
      SW201                        RESET push button                                      ✓                  ✓
      SW202                        WAKE-UP switch                                         ✓                  ✓
      SW203                        POWER_ON_N switch                                      ✓                  ✓
      CN300                        USB-UART0 (dwl and debug port)                         ✓                  ✓
      CN301                        SIM1                                                   ✓                  ✓
      CN302                        UIM1_DET/GPIO3                                         ✓                  ✓
      CN400                        USB-UART1                                              ✓                  ✓
      CN401                        USB main                                               ✓                  ✓
      CN500                        RS232-UART1                                            ✓                  ✓
      SW500                        USB-UART1 or RS232-UART1 selector                      ✓                  ✓
      SW600                        Audio amplifier selector                               ✓                  ✓
      CN600                        Audio jack loudspeaker                                 ✓                  ✓
      CN601                        Audio jack microphone                                  ✓                  ✓
      CN602                        RJ22 handset                                           ✓                  ✓
      U700                         Snap-in connector                                      ✓                  ✓
      CN800                        4V, 3.75A power jack (VBATT_APPLI)                     ✓                  ✓
      CN801                        4V, 3.75A power (VBATT_BB)                             ✓                  ✓
      CN802                        4V, 3.75A power (VBATT_RF)                             ✓                  ✓
      CN803                        VBATT_BB to VBATT_RF                                   ✓                  ✓
      CN804                        VBATT_APPLI to VBATT_BB and/or                         ✓                  ✓
                                   VBATT_RF
      CN900                        GNSS connector                                         ✓                  ✓
      CN901                        RF main connector                                      ✓                  ✓
      CN902                        RF diversity connector                                 -                  -
      CN903                        Antenna detection circuit enable for                   ✓                  ✓
                                   GNSS/ADC1
      CN904                        Antenna detection circuit enable for                   ✓                  ✓
                                   GNSS/GPIO1
      CN905                        Antenna detection circuit enable for RF/GPIO5          ✓                  ✓
      CN906                        Antenna detection circuit enable for RF/ADC0           ✓                  ✓
      CN1200 and CN1201            Board to board connector (Development Kit to           ✓                  ✓
                                   socket board connection) (unsoldered)
      CN1100                       GPIOs dip switch output/input                          ✓                  ✓
      CN1101                       GPIOs dip switch high/low level                        ✓                  ✓
      CN1300                       JTAG connector (unsoldered)                            ✓                  ✓
      SW1300                       VBAT_RTC switch                                        ✓                  ✓
      CN1301                       VBAT_RTC signal                                        ✓                  ✓
      CN1400                       USB-UART0 (dwl and debug port)                         ✓                  ✓
      SW1400                       USB-UART0 or RS232-UART0 selector                      ✓                  ✓


41112256                                    Rev 3.0                                          December 04, 2018               17

## Page 18

Development Kit User Guide                                                                                          General Description

     Table 3.     Available Connector, Switch and Jumper Solder Pads

       Connector, Switch and               Connection
       Jumper Solder Pads

       SW200                                    •    Push button (level ‘0’) to enable FAST_SHUTDOWN
                                                •    No push button (level ‘1’) to disable FAST_SHUTDOWN
       SW201                                    •    Push button (level ‘0’) to enable RESET
                                                •    No push button (level ‘1’) to disable RESET
       SW202                                    •    Push button (level ‘0’) to enable WAKE-UP
                                                •    No push button (level ‘1’) to disable WAKE-UP
       SW203                                    •    Switch to “ON” (level ‘1’) to enable POWER_ON_N on the module
                                                •    Switch to “OFF” (level ‘0’) to disable POWER_ON_N on the module
                                           Short with a jumper to enable SIM insertion detection
       CN302                               This connector is shorted by default via a jumper:
                                                •    Jumper connected = UIM1_DET/GPIO3 (SIM detection)
                                                •    Jumper disconnected = GPIO3 (GPIO only)
       SW500                                    •    Switch to “UART1” (level ‘1’) for RS232        -UART1 connector
                                                •    Switch to “USB1” (level ‘0’) for USB       -UART1 connector
       SW600                                    •    Switch to “ON” (level ‘1’) to enable amplifier for audio out
                                                •    Switch to “OFF” (level ‘0’) to disable      amplifier for audio out
                                           This jumper solder pad is solder      ed by default
       F600                                     •    Soldered = enable output amplifier speaker+
                                                •    Not soldered = disable output amplifier speaker+
                                           This jumper solder pad is soldered by default
       F601                                     •    Soldered = enable output amplifier speaker          -
                                                •    Not soldered = disable output amplifier speaker-
                                           This connector is shorted by default via a jumper
       CN803                                    •    Jumper connected =        VBATT_BB is connected to VBATT_RF
                                                •    Jumper disconnected = VBATT_BB is not connected to VBATT_RF
                                           This connector is shorted by default via a jumper
                                                •    Jumper connected = VBATT_APPLI is connected to VBATT_BB
       CN804                                         and/or VBATT_RF
                                                •    Jumper disconnected = VBATT_APPLI is not connected to
                                                     VBATT_BB and/or VBATT_RF
                                           This connector is shorted by defaul      t via a jumper
       CN903                                    •    Jumper connected =        antenna detection circuit enable for GNSS
                                                     antenna
                                                •    Jumper disconnected = ADC1 application
                                           This connector is shorted by default via a jumper
       CN904                                    •    Jumper connected =        antenna detection circuit enable for GNSS
                                                     antenna
                                                •    Jumper disconnected = GPIO1 application
                                           This connector is shorted by default       via a jumper
       CN905                                    •    Jumper connected =        antenna detection circuit enable for RF antenna
                                                •    Jumper disconnected = GPIO5 application
                                           This connector is shorted by default via a jumper
       CN906                                    •    Jumper connected = antenna detection circuit enable for RF antenna
                                                •    Jumper disconnected = ADC0 application
                                                •    Switch to “OUT” to enable GPIO2, 6, 7, 8, 10, 11, 14, 15 (default
       CN1100                                        setting)
                                                •    Switch to “IN” to enable GPIO2, 6, 7, 8, 10, 11, 14, 15


41112256                                        Rev 3.0                                              December 04, 2018                  18

## Page 19

Development Kit User Guide                                                                               General Description


      Connector, Switch and            Connection
      Jumper Solder Pads

      CN1101                                •   Switch to “HIGH” to enable GPIO2, 6, 7, 8, 10, 11, 14, 15 pull up
                                            •   Switch to “LOW” to enable GPIO2, 6, 7, 8, 10, 11, 14, 15 pull down
      SW1300                                •   Switch to “ON” (level ‘1’) = back-up battery RTC is present
                                            •   Switch to “OFF” (level ‘0’) = back-up battery RTC is not present
                                       This connector is shorted by default via a jumper
      CN1301                                •   Jumper connected = back-up battery RTC is present
                                            •   Jumper disconnected = back-up battery RTC is not present
      SW1400                                •   Switch to “UART0” (level ‘1’) for RS232-UART0 connector
                                            •   Switch to “USB0” (level ‘0’) for USB-UART0 connector


    Table 4.     Available Tests Points

      Test Points           Description

      TP200                 FAST_SHUTDOWN
      TP201                 RESET_IN_N
      TP202                 WAKE-UP
      TP203                 3V1_PERM
      TP204                 POWER_ON_N
      TP205                 1V8_PERM
      TP206                 GND
      TP207                 GND
      TP208                 GND
      TP209                 GND
      TP300                 UIM1_DATA
      TP301                 UIM1_VCC
      TP302                 UIM1_RST
      TP303                 UIM1_CLK
      TP400                 USB_VBUS
      TP500                 UART1_CTS
      TP501                 UART1_RX
      TP502                 UART1_RTS
      TP503                 UART1_TX
      TP504                 UART1_DSR
      TP505                 UART1_DCD
      TP506                 UART1_DTR
      TP507                 UART1_RI
      TP600                 PCM_CLK
      TP601                 PCM_SYNC
      TP602                 PCM_IN
      TP603                 PCM_OUT
      TP704                 GND_C30
      CN603                 MIC+ and MIC-
      CN604                 SPK+ and SPK-


41112256                                   Rev 3.0                                          December 04, 2018              19

## Page 20

Development Kit User Guide                                                                         General Description


      Test Points         Description

      TP800               VBATT_APPLI
      TP801               VBATT_BB
      TP802               VBATT_RF
      TP1200              GND
      TP1201              GND
      TP1202              GND
      TP1203              GND
      TP1204              GND
      TP1205              GND
      TP1206              EXT_GPS_LNA_EN
      TP1207              32K_CLKOUT (32,768 Khz)
      TP1208              26M_CLKOUT (26 Mhz)
      TP1209              GPIO5
      TP1210              TX_ON
      TP1211              UIM1_DET/GPIO3
      TP1212              ADC0
      TP1213              ADC1
      TP1214              VGPIO – reference voltage output
      TP1215              GPIO2
      TP1216              GPIO1
      TP1217              GPIO11
      TP1218              GPIO7
      TP1219              GPIO8
      TP1220              GPIO6
      TP1221              GPIO15
      TP1222              GPIO10
      TP1223              GPIO14


41112256                                 Rev 3.0                                       December 04, 2018             20

## Page 21

Development Kit User Guide                                                                                    General Description

     2.2.3.           Snap-In Connector

     The snap-in connector houses the embedded module and allows easy switching between any of the
     supported HL78xx series embedded modules.


     Figure 5.      Snap-In Connector


     Figure 6.      Snap-In Connector with Interposer


     Figure 7.      Snap-In Connector with Interposer and an HL78xx Module


     Note:          Ensure that the HL78xx module is positioned properly inside the snap-in connector to make it work
                    properly with the Development Kit.


41112256                                     Rev 3.0                                             December 04, 2018                21

## Page 22

Development Kit User Guide                                                                                    General Description

     After plugging an HL78xx module and interposer in the snap-in connector, attach the snap-in cover as
     shown in the figure below.


     Figure 8.      Snap-In Connector with HL78xx Module and Cover


41112256                                      Rev 3.0                                            December 04, 2018                22

## Page 23

Development Kit User Guide                                                                                                                                        General Description


    2.2.4.           Test Ports

    There are a total of 66 test ports available on the Development Kit. The following table lists the test port serigraphy and the corresponding signal names of the
    applicable HL78xx module.
    For more information about these signals, refer to the product technical specifications listed in section 6 Reference Documents.


    Table 5.    Development Kit Test Ports


      AirPrime HL78xx Series Development Kit                                                                                  AirPrime HL78xx Series Embedded Modules


      Test Port #       Test Port Serigraphy #          Board to Board             Test Points #        Connector #           HL7800 Pin Out                HL7802 Pin Out
                                                        Connector Pin #                                                       Signal Name                   Signal Name

      C1                GPIO1                           CN1200.32                  TP1214               CN904                 GPIO1                         GPIO1
      C2                RI1                             CN1201.71                  TP504                                      UART1_RI                      UART1_RI
      C3                RTS1                            CN1201.73                  TP501                                      UART1_RTS                     UART1_RTS
      C4                CTS1                            CN1201.79                  TP503                                      UART1_CTS                     UART1_CTS
      C5                TXD1                            CN1201.81                  TP500                                      UART1_TX                      UART1_TX
      C6                RXD1                            CN1201.85                  TP502                                      UART1_RX                      UART1_RX
      C7                DTR1                            CN1201.77                  TP505                                      UART1_DTR                     UART1_DTR
      C8                DCD1                            CN1201.75                  TP506                                      UART1_DCD                     UART1_DCD
      C9                DSR1                            CN1201.83                  TP507                                      UART1_DSR                     UART1_DSR
      C10               GPIO2                           CN1200.30                  TP1213                                     GPIO2                         GPIO2
      C11               RESET                           CN1200.37                  TP201                                      RESET_IN_N                    RESET_IN_N
      C12               -                               -                          -                    CN401                 USB_DN                        USB_DN
      C13               -                               -                          -                    CN401                 USB_DP                        USB_DP
      C14               NC                                                                                                    NC                            NC
      C15               NC                                                                                                    NC                            NC
      C16               VBUS                            -                          TP400                CN401                 USB_VBUS                      USB_VBUS


41112256                                                         Rev 3.0                                                        December 04, 2018                                     23

## Page 24

Development Kit User Guide                                                                                                                                 General Description


     AirPrime HL78xx Series Development Kit                                                                              AirPrime HL78xx Series Embedded Modules


     Test Port #       Test Port Serigraphy #         Board to Board           Test Points #        Connector #          HL7800 Pin Out               HL7802 Pin Out
                                                      Connector Pin #                                                    Signal Name                  Signal Name

     C17               NC                                                                                                NC                           NC
     C18               NC                                                                                                NC                           NC
     C19               NC                                                                                                NC                           NC
     C20               NC                                                                                                NC                           NC
     C21               Back-Up Battery                CN1200.35                                     CN1301               VBAT_RTC                     VBAT_RTC
     C22               26MHZ                          CN1201.84                TP1206                                    26M_CLKOUT                   26M_CLKOUT
     C23               32KHZ                          CN1201.76                TP1207                                    32K_CLKOUT                   32K_CLKOUT
     C24               ADC1                           CN1200.55                TP1211               CN903                ADC1                         ADC1
     C25               ADC0                           CN1200.53                TP1210               CN906                ADC0                         ADC0
     C26               UIM1_VCC                       CN1200.43                TP307                                     UIM1_VCC                     UIM1_VCC
     C27               UIM1_CLK                       CN1200.49                TP306                                     UIM1_CLK                     UIM1_CLK
     C28               UIM1_DATA                      CN1200.45                TP304                                     UIM1_DATA                    UIM1_DATA
     C29               UIM1_RST                       CN1200.47                TP305                                     UIM1_RST                     UIM1_RST
     C30               GND                                                                                               GND                          GND
     C31               NC                                                                                                NC                           NC
     C32               GND                                                                                               GND                          GND
     C33               PCM_OUT                        CN1200.76                TP603                                     PCM_OUT                      PCM_OUT
     C34               PCM_IN                         CN1200.78                TP602                                     PCM_IN                       PCM_IN
     C35               PCM_SYNC                       CN1200.80                TP601                                     PCM_SYNC                     PCM_SYNC
     C36               PCM_CLK                        CN1200.74                TP600                                     PCM_CLK                      PCM_CLK
     C37               GND                                                                                               GND                          GND
     C38               GNSS                           -                        -                    CN900                GNSS_ANT                     GNSS_ANT
     C39               GND                                                                                               GND                          GND
     C40               GPIO07                         CN1200.40                TP1216                                    GPIO7                        GPIO7


41112256                                                      Rev 3.0                                                      December 04, 2018                                   24

## Page 25

Development Kit User Guide                                                                                                                                 General Description


     AirPrime HL78xx Series Development Kit                                                                              AirPrime HL78xx Series Embedded Modules


     Test Port #       Test Port Serigraphy #         Board to Board           Test Points #        Connector #          HL7800 Pin Out               HL7802 Pin Out
                                                      Connector Pin #                                                    Signal Name                  Signal Name

     C41               GPIO08                         CN1200.42                TP1217                                    GPIO8                        GPIO8
     C42               NC                                                                                                NC                           NC
     C43               LNA_EN                         CN1201.32                TP1204                                    EXT_LNA_GPS_EN               EXT_LNA_GPS_EN
     C44               WAKEUP                         CN1200.70                TP202                                     WAKEUP                       WAKEUP
     C45               VGPIO                          CN1200.20                TP1212                                    VGPIO                        VGPIO
     C46               GPIO06                         CN1200.60                TP1218                                    GPIO6                        GPIO6
     C47               NC                                                                                                NC                           NC
     C48               GND                                                                                               GND                          GND
     C49               RF                             -                        -                    CN901                PRI_ANT                      PRI_ANT
     C50               GND                                                                                               GND                          GND
     C51               GPIO14                         CN1200.68                TP1221                                    GPIO14                       GPIO14
     C52               GPIO10                         CN1200.66                TP1220                                    GPIO10                       GPIO10
     C53               GPIO11                         CN1200.38                TP1215                                    GPIO11                       GPIO11
     C54               GPIO15                         CN1200.64                TP1219                                    GPIO15                       GPIO15
     C55               RX0                            CN1201.8                 TP1402                                    UART0_RX                     UART0_RX
     C56               TX0                            CN1201.6                 TP1400                                    UART0_TX                     UART0_TX
     C57               CTS0                           CN1201.4                 TP1403                                    UART0_CTS                    UART0_CTS
     C58               RTS0                           CN1201.10                TP1401                                    UART0_RTS                    UART0_RTS
     C59               POWER_ON                       CN1200.33                TP204                                     POWER_ON_N                   POWER_ON_N
     C60               TX_ON                          CN1200.29                TP1208                                    TX_ON                        TX_ON
     C61               VBATT_RF                       CN1200.2, CN1200.4       TP802                                     VBATT_RF                     VBATT_RF
                                                      CN1200.6, CN1200.8
     C62               VBATT_RF                       CN1200.2, CN1200.4       TP802                                     VBATT_RF                     VBATT_RF
                                                      CN1200.6, CN1200.8


41112256                                                      Rev 3.0                                                      December 04, 2018                                   25

## Page 26

Development Kit User Guide                                                                                                                                General Description


     AirPrime HL78xx Series Development Kit                                                                             AirPrime HL78xx Series Embedded Modules


     Test Port #       Test Port Serigraphy #        Board to Board            Test Points #      Connector #           HL7800 Pin Out              HL7802 Pin Out
                                                     Connector Pin #                                                    Signal Name                 Signal Name

     C63               VBATT_BB                      CN1200.1, CN1200.3,       TP801                                    VBATT_BB                    VBATT_BB
                                                     CN1200.5
     C64               UIM1_DET/GPIO03               CN1200.51                 TP1209             CN302                 UIM1_DET/GPIO3              UIM1_DET/GPIO3
     C65               FAST_SHD                      CN1200.69                 TP200                                    FAST_SHUTDOWN               FAST_SHUTDOWN
     C66               GPIO05                        CN1201.90                 TP1207             CN905                 GPIO5                       GPIO5


41112256                                                      Rev 3.0                                                     December 04, 2018                                  26

## Page 27

3.       Interfaces

    3.1.          Power

    3.1.1.           Power Supply

    Three power supply sources are available on the Development Kit :
         •    DC power jack connector via CN800
         •    Connector via CN801
         •    Connector via CN802


    Figure 9.     VBATT_APPLI Connector (on the top side of the Development Kit)


    Figure 10.    VBATT_BB and VBATT_RF Connectors (on the bottom side of the Development Kit)

    Three powers supplies can be used to supply the Development Kit and the HL78xx module. They can
    be used to supply power to VBATT_APPLI, VBATT_BB and VBATT_RF separately or they can
    provide supply collectively depending on CN803 and/or CN804 jumper configurations.
    To measure the global current consumed by the HL78xx module (baseband + radio frequency),
    disconnect CN803 and supply the Development Kit separately.
    VBATT_BB and VBATT_RF of the embedded module can be measured separately or as a total
    current drain depending on the configurations of CN803 and CN804.


41112256                                  Rev 3.0                                        December 04, 2018              27

## Page 28

Development Kit User Guide                                                                                                          Interfaces


     Refer to the following figure for possible configuration settings:


     Figure 11.       Power Supply Configurations

     Refer to the following pictures for connector and jumper configurations.


     Figure 12.       Single Power Supply Configuration


     Figure 13.       Separate Power Supplies for the Development Kit and Module Configurations


41112256                                         Rev 3.0                                                December 04, 2018                   28

## Page 29

Development Kit User Guide                                                                                                  Interfaces


     Figure 14.     Separate VBATT_APPLI, VBATT_BB and VBATT_RF Configuration

     The state of VBATT_APPLI is indicated by a green    LED and can be controlled by a test point.
     Similarly, the state of VBATT_BB and VBATT_RF are indicated by a green LED and can be
     controlled by two test points.

     Note:          The green LED, D807, is always activated regardless of the connection of jumpers CN803 and
                    CN804.


     Table 6.     Power Supply Pin Description

      HL78xx Signal Name              HL78xx Pin Out           I/O      LED          Test Point /       Development Kit
                                                                                     Jumper             Signal Name

      VBATT_APPLI                     -                        I        D803         TP800              DC_PWR
      VBATT_BB                        C63                      I        D807         TP801              VBATT
      VBATT_RF                        C61, C62                 I                     TP802              VBATT_PA


     Refer to the following table for the electrical characteristics of the power supplies.


     Table 7.     Power Supply Electrical Characteristics

      Power Supply                                            Vmin.                    Vnom.                    Vmax.

      VBATT_APPLI (V)                                         3.6                      4                        4.35
      VBATT_BB (V)                                            3.2*                     3.7                      4.35
      VBATT_RF (V) Full Specification                         3.2*                     3.7                      4.35
      VBATT_RF (V) Extended Range                             2.8**                    3.7                      4.35

          *          This value must be guaranteed during the burst.
          **         No guarantee of 3GPP performances over extended range.


     For more information, refer to the product technical specifications listed in section 6 Reference
     Documents.


41112256                                      Rev 3.0                                             December 04, 2018                29

## Page 30

Development Kit User Guide                                                                                                  Interfaces

     3.1.2.            Module Orientation Detection

     The Development Kit includes a system for automatically detecting the module’s orientation, as well
     as the absence of a module, inside the snap-in connector.


     Figure 15.     Module in Correct Orientation


     Figure 16.     FLASH_LED for Module in Correct Orientation


     Figure 17.     Module in Incorrect Orientation or No Module Present


     Figure 18.     FLASH_LED for Module in Incorrect Orientation or No Module Present

     One red (flashing) LED is available to indicate the module’s wrong orientation.


     Table 8.     Bad Configuration

      HL78xx               HL78xx Pin         I/O       Voltage          LED             Test Point /        Development Kit
      Signal Name          Out                          Level                            Jumper              Signal Name

      -                    -                  -         -                D806 (red)      -                   BAD CONFIG


41112256                                      Rev 3.0                                            December 04, 2018                 30

## Page 31

Development Kit User Guide                                                                                        Interfaces

    3.1.3.           Internal Power Supply

    The Development Kit includes two internal powers supplies that are permanently activated. These two
    powers supplies are powered by the VBAT_APPLI power supply.


    Figure 19.     Internal Power Supply


    Table 9.    Internal Power Supply Description

      HL78xx Signal        HL78xx          I/O     Voltage Level       LEDs        Test Point /      Development Kit
      Name                 Pin Out                                                 Jumper            Signal Name

      -                    -               O       1V8 / 200mA         D201        TP205             LDO_1V8_PERM
      -                    -               O       3V1 / 300mA         D200        TP203             LDO_3V1_PERM


41112256                                  Rev 3.0                                         December 04, 2018              31

## Page 32

Development Kit User Guide                                                                                          Interfaces

    3.1.4.           POWER_ON_N

    The Development Kit includes a POWER_ON_N switch, SW203, to start the HL78xx module.
    Once the Development Kit is connected to an external source, the HL78xx module will start
    monitoring the POWER_ON_N pin for a power on event.
    The module may be enabled by switching SW203 to the “ON” position, and disabled by moving it to
    the “OFF” position.
    A green LED, D1106, indicates the POWER_ON_N state. When this LED is lit, it indicates that the
    module is powered on.
    The module can be powered off by disconnecting the Development Kit from the power source or by
    issuing the appropriate AT command. For more information about AT commands, refer to document
    [1] AirPrime HL78xx Series AT Commands Interface Guide.


    Figure 20.     POWER_ON_N Pushbutton


    Note:          The HL78xx module will start regardless of whether switch SW203 is in the “ON” or “OFF” position.


    Table 10.   POWER_ON_N Pin Description

      HL78xx Signal            HL78xx          I/O     Voltage        LED          Test Point /      Development Kit
      Name                     Pin Out                 Level                       Jumper            Signal Name

      POWER_ON_N               C59             I       1.8V           D1106        TP204             POWER_ON


41112256                                   Rev 3.0                                         December 04, 2018              32

## Page 33

Development Kit User Guide                                                                                Interfaces

    3.2.         Control Functions

    3.2.1.         FAST_SHUTDOWN

    The Development Kit includes a FAST_SHUTDOWN pushbutton to quickly shut down the HL78xx
    module.


    Figure 21.   FAST_SHUTDOWN Pushbutton

    The state of FAST_SHUTDOWN is indicated by a green LED and can be controlled by a test point.


    Table 11.  FAST_SHUTDOWN Pin Description

     HL78xx Signal            HL78xx Pin      I/O    Voltage      LEDs       Test Point /     Development Kit
     Name                     Out                    Level                   Jumper           Signal Name

     FAST_SHUTDOWN            C65             I      1.8V         D1104      TP200            FAST_SHD


41112256                               Rev 3.0                                      December 04, 2018           33

## Page 34

Development Kit User Guide                                                                                         Interfaces

    3.2.2.           WAKE-UP

    The Development Kit includes a WAKE-UP switch to wake the HL78xx module up.
    The WAKE-UP pushbutton starts a wake when it is always activated.
    The WAKE-UP pin is used to wake up the system from ultra-low power modes (from OFF mode,
    Sleep mode, FAST_SHUTDOWN, or after a software power off). This signal should be set to high
    level (external 1.8V) for at least a few milliseconds until the system is active to wake the module up
    from these modes.


    Figure 22.     WAKE-UP Pushbutton

    The state of WAKE-UP is indicated by a green LED and can be controlled by a test point.


    Table 12.   WAKE-UP Pin Description

      HL78xx Signal          HL78xx Pin        I/O     Voltage       LED            Test Point /      Development Kit
      Name                   Out                       Level                        Jumper            Signal Name

      WAKE-UP                C44               I       1.8V          D1112          TP202             WAKEUP


41112256                                   Rev 3.0                                         December 04, 2018              34

## Page 35

Development Kit User Guide                                                                                          Interfaces

    3.2.3.           RESET_IN_N

    The Development Kit includes a RESET_IN_N pushbutton to reset the HL78xx module.
    The RESET_IN_N pushbutton starts a general reset when it is pushed. Reset can only be executed
    after the module has been switched ON.

    Note:          An operating system reset is preferred to a hardware reset.


    Figure 23.     RESET_IN_N Pushbutton

    The state of RESET_IN_N is indicated by a green LED and can be controlled by a test point.


    Table 13.   RESET_IN_N Pin Description

      HL78xx Signal          HL78xx          I/O     Voltage          LED            Test Point /      Development Kit
      Name                   Pin Out                 Level                           Jumper            Signal Name

      RESET_IN_N             C11             I       1.8V             D1105          TP201             RESET


41112256                                   Rev 3.0                                          December 04, 2018              35

## Page 36

Development Kit User Guide                                                                                           Interfaces

    3.3.           USB (Main)

    The main USB connection on the Development Kit is available from CN401 and can be used to
    communicate with the HL78xx module directly via a PC.
    CN401 is a receptacle USB Micro-AB connector.


    Figure 24.     Micro-AB USB Connector


    Figure 25.     Main USB Interface


    Table 14.    Main USB Connector Pin Description

      Pin #                   Signal Name              I/O                     I/O Type                Description

      1                       USB_VBUS                 I                       USB                     +5 VDC
      2                       USB_DN                   I/O                     USB                     Data -
      3                       USB_DP                   I/O                     USB                     Data +
      4                       NC                       I                       USB                     USB OTG ID
      5                       GND                                                                      Ground


    A blue LED, D407, indicates the USB_VBUS state. When this LED is lit, it indicates that the USB
    cable is plugged into the receptacle USB Micro-AB connector and is available for use.
    One test point is available to control the state of USB_VBUS.


    Table 15.    Main USB Pin Description

      HL78xx             HL78xx                Voltage                  Test Point / PCB             Development Kit
      Signal             Pin Out       I/O     Level          LED       Pad                          Signal Name
      Name

      USB_VBUS           C16           I       5V             D407      TP400                        VBUS
      USB_DP             C13           I/O     3.3V           -         TP401 (pads)                 -
      USB_DN             C12           I/O     3.3V           -         TP402 (pads)                 -


41112256                                   Rev 3.0                                          December 04, 2018              36

## Page 37

Development Kit User Guide                                                                                                Interfaces

     3.4.           Audio

     The headset jack available on the Development Kit, CN602, is a 4-pin RJ22 and allows the HL78xx
     module to connect to an audio interface.
     An audio codec, W681360YG from WINBOND, is mounted on the Development Kit and is used to
     translate the embedded module’s digital audio interface or PCM interface into an analog audio signal.
     This was done because performance and functionality tests are run under analog audio levels, and
     because final customer applications are also in analog.
     An audio amplifier chipset is mounted on the Development Kit ; the audio amplifier interface can be
     disabled by switching SW600 to the “OFF” position and enabled by switching it to the “ON” position.
     A green LED, D601, indicates the audio amplifier state. When this LED is lit, it indicates that the audio
     amplifier is activated.
     Jumper solder pads F600 and F601 are used to enable or disable the audio amplifier speaker signals
     with headset jack RJ22, CN602. These two jumpers solder pads are soldered by default.
     When the audio amplifier interface is switched ON, it is recommended to use audio jack, CN601, for
     microphone and for amplified earphone.


     Figure 26.     Audio Interface


     Figure 27.     4-pin RJ22 Handset Connector for Audio Signals

     Refer to the following table for the audio handset connector pin description.


41112256                                     Rev 3.0                                            December 04, 2018               37

## Page 38

Development Kit User Guide                                                                                          Interfaces


    Table 16.   Audio Handset Connector Pin Description

                                           I/O                               Handset                  Development Kit
      Pin #      Signal Name        I/O    Type        Description           Connector / Test         Signal Name
                                                                             Point

      1          MICRO+             I      Analog      Main microphone       CN602-1 / CN603-2        MIC+
                                                       negative input
      4          MICRO-             I      Analog      Main microphone       CN602-4 / CN603-1        MIC-
                                                       positive input
      3          SPEAKER+           O      Analog      Main speaker          CN602-2 / CN604-2        SPK+
                                                       positive output
      2          SPEAKER-           O      Analog      Main speaker          CN602-3 / CN604-1        SPK-
                                                       negative output


    Both microphone and speaker signals are configured in differential mode.


    Figure 28.     Audio Jack Connector for Audio Signals

    Refer to the following tables for the audio jack 3.5mm connector pin description.


    Table 17.   Microphone (CN601) Connector Pin Description

      Pin    Signal Name        I/O      I/O Type      Description                            Development Kit
      #                                                                                       Signal Name

      4      MICRO-             I        Analog        Main microphone negative input         MIC
      2      MICRO+             I        Analog        Main microphone positive input         MIC
      1      GND                                       Ground                                 MIC


    Table 18.   Earphone (CN600) Connector Pin Description

      Pin #      Signal Name        I/O       I/O Type      Description                              Development Kit
                                                                                                     Signal Name

      4          AMPL_SPK-          O         Analog        Amplifier speaker negative output        SPK_AMPL
      2          AMPL_SPK+          O         Analog        Amplifier speaker positive output        SPK_AMPL
      1          AMPL_SPK-          O         Analog        Amplifier speaker negative output        SPK_AMPL


41112256                                   Rev 3.0                                         December 04, 2018              38

## Page 39

Development Kit User Guide                                                                                             Interfaces


    Both microphone and speaker amplifier signals are configured in differential mode.
    Four test points are available to control the state of the four PCM signals of the HL78xx module.
    Refer to the following table for the PCM pin description.


    Table 19.    PCM Pin Description

      HL78xx              HL78xx       I/O     I/O           Voltage        LED        Test Point /      Development Kit
      Signal Name         Pin Out              Type          Level                     Jumper            Signal Name

      PCM_CLK             C36          I/O     Digital       1.8V           -          TP600             PCM_CLK
      PCM_IN              C34          I       Digital       1.8V           -          TP602             PCM_IN
      PCM_OUT             C33          O       Digital       1.8V           -          TP603             PCM_OUT
      PCM_SYNC            C35          I/O     Digital       1.8V           -          TP601             PCM_SYNC


41112256                                    Rev 3.0                                           December 04, 2018               39

## Page 40

Development Kit User Guide                                                                                                  Interfaces

     3.5.           UIM/SIM1

     The Development Kit has one SIM connector, SIM1, CN301.


     Figure 29.     SIM1 Interface  (bottom side)


     Figure 30.     SIM1 Interface  (top side)


     Note:          ESD protection is available on all SIM     1 signals.
     Refer to the following table for the SIM         1 connector pin description.


     Table 20.    SIM1 Connector Pin Description

      Pin   #              Signal Name                 I/O            I/O Type          Description

      1                    UIM1_VCC                    O              1V7 << 1V9        SIM Power Supply
      2                    UIM1_RESET                  O              1V7 << 1V9        SIM Reset
      3                    UIM1_CLK                    O              1V7 << 1V9        SIM Clock
      4                    CC4                         Not used
      5                    GND                                                          Ground
      6                    VPP                         Not used
      7                    UIM1_DATA                   I/O            1V7 << 1V9        SIM Data
      8                    CC8                         Not used
      9                    1V8_PERM                    I              VIO*              VIO supply from the Development Kit
      10                   UIM1_DET/GPIO3              I              VIO*              SIM Card Detect
      11, 12, 13, 14       GND                                                          Ground casing

          *          VIO = 1.8V (1V8_PERM) from the Development Kit.


41112256                                      Rev 3.0                                             December 04, 2018                40

## Page 41

Development Kit User Guide                                                                                             Interfaces


    Four test points are available to control the state of the four SIM1 signals of the HL78xx module, and
    one jumper is available to control the status of the SIM1 detection signal of the module.
    Refer to the following table for the SIM1 pin description.


    Table 21.    SIM1 Pin Description

      HL78xx Signal             HL78xx          I/O      Voltage        LED     Test Point /           Development Kit
      Name                      Pin Out                  Level                  Jumper                 Signal Name

      UIM1_VCC                  C26             O        1.8V           -       TP301                  UIM1_VCC
      UIM1_DATA                 C28             I/O      1.8V           -       TP300                  UIM1_DATA
      UIM1_RST                  C29             O        1.8V           -       TP302                  UIM1_RST
      UIM1_CLK                  C27             O        1.8V           -       TP303                  UIM1_CLK
      UIM1_DET/GPIO3            C64             I        1.8V           -       CN302, TP1213          UIM1_DET, GPIO03


41112256                                    Rev 3.0                                          December 04, 2018               41

## Page 42

Development Kit User Guide                                                                                             Interfaces

    3.6.           UART1

    3.6.1.            RS232-UART1

    The serial link connection, RS232-UART1, on the Development Kit is available from CN500, which is
    a SUB-D 9-pin female connector via three transceivers at voltage level 1.8V.
    This interface is used to communicate between the module and a PC or host processor.


    Figure 31.     DB-9 Female Connector


    Figure 32.     RS232-UART1 Interface


    Table 22.    RS232-UART1 Connector Pin Description

      Pin #           Signal Name*            I/O          I/O Type                     Description

      1               RS232_DCD1              O            RS232 (V24/V28)              Data carrier detect
      2               RS232_RX1               O            RS232 (V24/V28)              Receive serial data
      3               RS232_TX1               I            RS232 (V24/V28)              Transmit serial data
      4               RS232_DTR1              I            RS232 (V24/V28)              Data terminal ready
      5               GND                                                               Ground
      6               RS232_DSR1              O            RS232 (V24/V28)              Data set ready
      7               RS232_RTS1              I            RS232 (V24/V28)              Request to send
      8               RS232_CTS1              O            RS232 (V24/V28)              Clear to send
      9               RS232_RI1               O            RS232 (V24/V28)              Ring indicator

         *          Signal view from PC side.


41112256                                    Rev 3.0                                           December 04, 2018               42

## Page 43

Development Kit User Guide                                                                                           Interfaces

    3.6.2.           USB-UART1

    The USB-UART1 connection on the Development Kit is available from CN400, which is a receptacle
    USB Micro-AB connector via a USB-UART transceiver and voltage level translator at level 1.8V. Refer
    to Figure 24 Micro-AB USB Connector for connector reference.
    This interface is used to communicate between the module and a PC or host processor.
    For detailed information about the USB-UART transceiver embedded on the Development Kit, refer to
    http://www.ftdichip.com/Products/ICs/FT231X.html.
    A blue LED, D403, indicates the USB-UART1 state. When this LED is lit, it indicates that                   a USB cable
    is plugged into the receptacle and is available for use.


    Figure 33.     USB-UART1 Interface


    Table 23.    USB-UART1 Connector Pin Description

      Pin #            Signal Name                 I/O            I/O Type            Description

      1                USB_UART1_VBUS              I              USB                 +5 VDC
      2                USB_DN                      I/O            USB                 Data -
      3                USB_DP                      I/O            USB                 Data +
      4                NC                          I              USB                 USB OTG ID
      5                GND                                                            Ground


41112256                                   Rev 3.0                                          December 04, 2018               43

## Page 44

Development Kit User Guide                                                                                         Interfaces

    3.6.3.           RS232-UART1 or USB-UART1

    The RS232-UART1 interface can be enabled by switching SW500 to the “UART1” position.
    A green LED, D502, indicates the RS232-UART1 state. When this LED is lit, it indicates that the
    RS232-UART1 interface is available for use.
    Similarly, the USB-UART1 interface can be enabled by switching SW500 to the “USB1” position.
    A green LED, D501, indicates the USB-UART1 state. When this LED is lit, it indicates that the USB-
    UART1 interface is available for use.


    Figure 34.     RS232-UART1 or USB-UART1 Switch Configuration

    The state of UART1 is indicated by eight green LEDs and can be controlled by eight test points.


    Table 24.   UART1 Pin Description

      HL78xx Signal        HL78xx          I/O     Voltage Level        LED          Test Point      Development Kit
      Name*                Pin Out                                                                   Signal Name

      UART1_TX             C5              I       1.8V                 D1011        TP500           TXD1
      UART1_RX             C6              O       1.8V                 D1012        TP502           RXD1
      UART1_RTS            C3              I       1.8V                 D1009        TP501           RTS1
      UART1_CTS            C4              O       1.8V                 D1010        TP503           CTS1
      UART1_DSR            C9              O       1.8V                 D1015        TP507           DSR1
      UART1_DTR            C7              I       1.8V                 D1013        TP505           DTR1
      UART1_DCD            C8              O       1.8V                 D1014        TP506           DCD1
      UART1_RI             C2              O       1.8V                 D1008        TP504           RI1

         *         Signal view from PC side.


41112256                                   Rev 3.0                                        December 04, 2018              44

## Page 45

Development Kit User Guide                                                                                              Interfaces

    3.7.           UART0

    3.7.1.            RS232-UART0

    The serial link, RS232-UART0, connection on the Development Kit is available from CN1400, which is
    a SUB-D 9-pin female connector via a transceiver at voltage level 1.8V. Refer to Figure 31 DB-9
    Female Connector for connector reference.
    This interface is used to communicate with the module’s debug trace and download port interface via a PC.


    Figure 35.     RS232-UART0 Interface


    Table 25.    RS232-UART0 Connector Pin Description

      Pin #         Signal Name*           I/O          I/O Type                     Description

      1             Not used               -            -                            -
      2             RS232_RX0              O            RS232 (V24/V28)              Receive serial data
      3             RS232_TX0              I            RS232 (V24/V28)              Transmit serial data
      4             Not used               -            -                            -
      5             GND                                                              Ground
      6             Not used               -            -                            -
      7             RS232_RTS0             I            RS232 (V24/V28)              Request to send
      8             RS232_CTS0             O            RS232 (V24/V28)              Clear to send
      9             Not used               -            -                            -

         *          Signal view from PC side .


41112256                                    Rev 3.0                                           December 04, 2018                45

## Page 46

Development Kit User Guide                                                                                           Interfaces

    3.7.2.            USB-UART0

    The USB-UART0 connection on the Development Kit is available from CN300, which is a receptacle
    USB Micro-AB connector via a USB-UART transceiver and voltage level translator at level 1.8V. Refer
    to Figure 24 Micro-AB USB Connector for connector reference.
    This interface is used to communicate with the module’s debug trace and download port interface via
    a PC.
    For detailed information about the USB-UART transceiver embedded on the Development Kit, refer to
    http://www.ftdichip.com/Products/ICs/FT231X.html.
    A blue LED, D303, indicates the USB-UART0 state. When this LED is lit, it indicates that a USB cable
    is plugged into the receptacle and is available for use.


    Figure 36.     USB-UART0 Interface


    Table 26.    USB-UART0 Connector Pin Description

      Pin #          Signal Name                  I/O          I/O Type           Description

      1              USB_UART0_VBUS               I            USB                +5 VDC
      2              USB_DN                       I/O          USB                Data -
      3              USB_DP                       I/O          USB                Data +
      4              NC                           I            USB                USB OTG ID
      5              GND                                                          Ground


41112256                                    Rev 3.0                                         December 04, 2018               46

## Page 47

Development Kit User Guide                                                                                          Interfaces

    3.7.3.           RS232-UART0 or USB-UART0

    The RS232-UART0 interface can be enabled by switching SW1400 to the “UART0” position.
    A green LED, D1402, indicates the RS232-UART0 state. When this LED is lit, it indicates that the
    RS232-UART0 interface is available for use.
    Similarly, the USB-UART0 interface can be enabled by switching SW1400 to the “USB0” position.
    A green LED, D1401, indicates the USB-UART0 state. When this LED is lit, it indicates that the USB-
    UART0 interface is available for use.


    Figure 37.     RS232-UART0 or USB-UART0 Switch Configuration

    The state of UART0 is indicated by four green LEDs and can be controlled by four test points.


    Table 27.   UART0 Pin Description

      HL78xx              HL78xx Pin         I/O   Voltage Level        LED         Test Point        Development Kit
      Signal Name*        Out                                                                         Signal Name

      UART0_TX            C56                I     1.8V                 D1101       TP1400            TXD0
      UART0_RX            C55                O     1.8V                 D1100       TP1402            RXD0
      UART0_RTS           C58                I     1.8V                 D1103       TP1401            RTS0
      UART0_CTS           C57                O     1.8V                 D1102       TP1403            CTS0

         *         Signal view from PC side.


41112256                                   Rev 3.0                                         December 04, 2018              47

## Page 48

Development Kit User Guide                                                                                           Interfaces

    3.8.           GPIO

    The Development Kit provides all GPIO signals from the HL78xx module.


    Figure 38.     GPIO Signals

    The state of GPIOs are indicated by eleven green LEDs and can be controlled by eleven test points.


    Table 28.    GPIO Pin Description

      HL78xx Signal            HL78xx       I/O     Voltage       LED           Test Point /         Development Kit
      Name                     Pin Out              Level                       Jumper               Signal Name

      GPIO1                    C1           I/O     1.8V          D1000         TP1216               GPIO01
      GPIO2                    C10          I/O     1.8V          D1001         TP1215               GPIO02
      GPIO3 / UIM1_DET         C64          I/O     1.8V          D1002         TP1213 / CN302       GPIO03 / UIM1_DET
      GPIO5                    C66          I/O     1.8V          D1003         TP1209 / CN905       GPIO05
      GPIO6                    C46          I/O     1.8V          D1004         TP1220               GPIO06
      GPIO7                    C40          I/O     1.8V          D1005         TP1218               GPIO07
      GPIO8                    C41          I/O     1.8V          D1006         TP1219               GPIO08
      GPIO10                   C52          I/O     1.8V          D1007         TP1222               GPIO10
      GPIO11                   C53          I/O     1.8V          D1107         TP1217               GPIO11
      GPIO14                   C51          I/O     1.8V          D1109         TP1223               GPIO14
      GPIO15                   C54          I/O     1.8V          D1108         TP1221               GPIO15

    Note:          Ensure that CN1100 is set to position “OUT” when testing GPIOs set as output signals.


41112256                                   Rev 3.0                                          December 04, 2018               48

## Page 49

Development Kit User Guide                                                                                        Interfaces

    3.8.1.           GPIO Control Signals

    Two switch sets, CN1100 and CN1101, are available on the Development Kit for GPIO test purposes.


    Figure 39.     GPIOs Control Switches

    CN1100 enables GPIOx (where x = 2, 6, 7, 8, 10, 11, 14 and 15), while CN1101 enables these GPIOs
    to be connected to 1V8_PERM either as 1kΩ pull-ups or 100Ω pull-lows.
    GPIO settings can be set or reset using AT commands. For more information about AT commands,
    refer to document [1] AirPrime HL78xx Series AT Commands Interface Guide.


    Table 29.   GPIO Control Switch Configuration

      GPIO Mode                        CN1100 Position                            CN1101 Position

      Output                           OUT                                        -
      Input                            IN                                         LOW
                                                                                  HIGH


41112256                                  Rev 3.0                                         December 04, 2018              49

## Page 50

Development Kit User Guide                                                                                             Interfaces

    3.9.           ADC

    Two ADC signals are available on the Development Kit.


    Figure 40.     ADC Signals

    Two test points are available to control the state of the two ADC signals.


    Table 30.    ADC Pin Description

      HL78xx Signal          HL78xx Pin         I/O      Voltage      LED          Test Point /          Development Kit
      Name                   Out                         Level*                    Jumper                Signal Name

      ADC0                   C25                O        1.8V         -            TP1212 / CN906        ADC0
      ADC1                   C24                O        1.8V         -            TP1213 / CN903        ADC1

         *          ADCx voltage = 0.0V to 1.8V.


41112256                                    Rev 3.0                                          December 04, 2018               50

## Page 51

Development Kit User Guide                                                                                    Interfaces

    3.10.         EXT_GPS_LNA_EN

    The Development Kit provides an EXT_GPS_LNA_EN signal from the HL78xx module. The
    EXT_GPS_LNA_EN signal indicates whether the GNSS receiver is active and can be used to enable
    an external LNA (for active antenna).


    Figure 41.    EXT_GPS_LNA_EN Signal

    The state of EXT_GPS_LNA_EN is indicated by a green LED and can be controlled by a test point.


    Table 31.   EXT_GPS_LNA_EN Pin Description

      HL78xx Signal           HL78xx         I/O   Voltage       LED        Test Point /        Development Kit
      Name                    Pin Out              Level                    Jumper              Signal Name

      EXT_GPS_LNA_EN          C43            O     1.8V          D1112      TP1206 / CN904      LNA_EN


41112256                                 Rev 3.0                                       December 04, 2018             51

## Page 52

Development Kit User Guide                                                                                        Interfaces

    3.11.         Antenna Detection Circuit

    The Development Kit provides two antenna detection circuits for the RF and GNSS connectors, and a
    GNSS antenna bias circuit (for active antenna).


    Figure 42.    RF and GNSS Antenna Detection


    Table 32.   RF Antenna Detection Pin Description

      HL78xx              HL78xx Pin        I/O     Voltage       LED          Test Point            Development Kit
      Signal Name         Out                       Level                                            Signal Name

      ADC0                C25               O       1.8V          -            TP1212 / CN906        ADC0
      GPIO5               C66               I/O     1.8V          D1003        TP1209 / CN905        GPIO05


    Table 33.   GNSS Antenna Detection Pin Description

      HL78xx Signal           HL78xx         I/O    Voltage       LED         Test Point           Development Kit
      Name                    Pin Out               Level                                          Signal Name

      ADC1                    C24            O      1.8V          -           TP1213 / CN903       ADC1
      EXT_GPS_LNA_EN          C43            O      1.8V          D1112       TP1206 / CN904       LNA_EN


41112256                                  Rev 3.0                                         December 04, 2018             52

## Page 53

Development Kit User Guide                                                                                            Interfaces

    3.12.          Clock Out

    Two clocks out signals are available on the Development Kit from the HL78xx module.


    Figure 43.     Clock Out Signals

    Two test points are available to control the state of the two clocks out signals.


    Table 34.    Clock Out Pin Description

      HL78xx Signal         HL78xx          I/O     Voltage Level           LED      Test Point /       Development Kit
      Name                  Pin Out                                                  Jumper             Signal Name

      32K_CLKOUT*           C23             O       1.8V                    -        TP1207             32KHZ
      26M_CLKOUT**          C22             O       1.8V                    -        TP1208             26MHZ

         *          32K_CLKOUT = 32.768 Khz
         **         26M_CLKOUT = 26 Mhz


41112256                                    Rev 3.0                                         December 04, 2018               53

## Page 54

Development Kit User Guide                                                                                          Interfaces

    3.13.          TX-ON

    The Development Kit provides a TX-ON signal from the HL78xx module. The TX-ON indication status
    signal depends on the module’s transmitter state.


    Figure 44.     TX-ON Signal


    The state of TX-ON is indicated by a green LED and can be controlled by a test point.


    Table 35.   TX-ON Pin Description

      HL78xx Signal          HL78xx    Pin     I/O     Voltage       LED         Test Point /          Development Kit
      Name                   Out                       Level                     Jumper                Signal Name

      TX-ON                  C60               O       1.8V          D1111       TP1210                TX-ON


41112256                                   Rev 3.0                                          December 04, 2018              54

## Page 55

Development Kit User Guide                                                                                       Interfaces

    3.14.         VGPIO

    One VGPIO power supply signal is available on the Development Kit from the HL78xx module.


    Figure 45.    VGPIO Signal

    One test point is available to control the state of VGPIO.


    Table 36.   VGPIO Pin Description

      HL78xx Signal         HL78xx Pin        I/O      Voltage       LED        Test Point /        Development Kit
      Name                  Out                        Level                    Jumper              Signal Name

      VGPIO                 C45               O        1.8V          -          TP1214              VGPIO


41112256                                  Rev 3.0                                        December 04, 2018             55

## Page 56

Development Kit User Guide                                                                                           Interfaces

    3.15.          RTC Backup Battery

    The Development Kit provides an input signal, VBAT_RTC, for connecting a coin battery or external
    power supply, which is used as a backup power supply to preserve the date and time when VBATT is
    switched OFF (no VBATT).
    The VBAT_RTC interface can be enabled by switching SW1300 to the “ON” position and shorting
    jumper CN1301. To disable this interface, switch SW1300 to the “OFF” position (jumper CN1301 can
    either be shorted or not).
    A green LED, D1300, indicates the VBAT_RTC state if jumper solder pad F1300 is soldered. When
    this LED is lit, it indicates that a power supply is present on VBAT_RTC.


    Figure 46.     RTC Backup Battery


    Table 37.    VBAT_RTC Pin Description

      HL78xx Signal         HL78xx           I/O    Voltage Level        LED         Test Point    /   Development Kit
      Name                  Pin Out                                                  Jumper            Signal Name

      VBAT_RTC              C21              I      2.2V to 4.35V*       D1300       CN1301            BACKUP BATT

         *          Development Kit RTC backup batter voltage = 3.1 V.


41112256                                   Rev 3.0                                          December 04, 2018               56

## Page 57

Development Kit User Guide                                                                                 Interfaces

    3.16.        RF and GNSS Antenna

    Two SMA connectors are available on the Development Kit for RF and GNSS antenna connections:
        •    RF antenna via CN901
        •    GNSS antenna via CN900


    Figure 47.   RF and GNSS Antenna Connectors


41112256                                Rev 3.0                                     December 04, 2018            57

## Page 58

Development Kit User Guide                                                                                        Interfaces

    3.17.         Diversity Antenna

    An SMA connector is available on the Development Kit for RF diversity (DIV) antenna connection via
    CN902. This is not connected by default.


    Figure 48.    RF Diversity Antenna Connector


41112256                                  Rev 3.0                                        December 04, 2018              58

## Page 59

Development Kit User Guide                                                                                             Interfaces

    3.18.          JTAG

    A JTAG connector is available on the Development Kit via CN1300 for debug. This is not connected
    by default.


    Figure 49.     JTAG Connector


    Warning:       This interface is not available for customer use.


41112256                                    Rev 3.0                                           December 04, 2018               59

## Page 60

Development Kit User Guide                                                                                        Interfaces

    3.19.          Board to Board Connector

    Two 100 pts board to board connectors are available on the Development Kit for plugging in a socket-
    up board. These are not connected by default.
         •     100 pts connectors via CN1200
         •     100 pts connectors via CN1201


    Figure 50.     Board To Board Connectors


    Warning:       This interface is not available for customer use.


41112256                                  Rev 3.0                                         December 04, 2018              60

## Page 61

4.        Getting Started

    This section describes how the Development Kit is set up as well as describes communications
    testing, making calls and debugging with an embedded module.

    4.1.           Setting Up

    Perform the following steps before powering the Development Kit on:
         1.   Ensure that switches and connectors are configured accordingly. By default, the Development
              Kit board is configured from the factory before shipment.
         2.   Plug an HL78xx module to the snap-in connector with an interposer and attach the snap-in
              cover.
         3.   Insert a SIM or USIM card in the SIM slot, CN301, if communications are required.
         4.   Connect the HL78xx module to a PC using any of the following methods:
              ▪    Connect the RS232 cable between the PC port and CN500 of the Development Kit for
                   UART1 connection and switch SW500 to position “UART1”.
                   By default, baud rate = 115.2Kbps, data bits = 8, parity = N, and stop bits = 1.
              ▪    Connect the USB cable between the PC port and CN400 of the Development Kit for
                   UART1 connection and switch SW500 to position “USB1”.
              ▪    Connect the USB cable between the PC port and CN401 of the Development Kit for the
                   main USB connection.
         5.   Connect an RF antenna to CN901 of the Development Kit.
         6.   Connect a GNSS antenna to CN900 of the Development Kit.
         7.   Connect a handset to CN602 for audio communication and switch SW600 to either “ON” or
              “OFF” depending on whether an audio amplifier is used, and F600 and F601 solder pads are
              soldered to enable the audio amplifier (not soldered by default).
         8.   Connect a 4V power DC jack to CN800 and check if jumpers are plugged on CN803 and
              CN804. Note that the Development Kit may be supplied with power depending on jumper
              configurations and power supplies CN800, CN801 and CN802. Refer to section 3.1.1 Power
              Supply for more information on supplying power to the Development Kit.

    The Development Kit should look like either of the following figures after it has been properly set up.


41112256                                   Rev 3.0                                         December 04, 2018               61

## Page 62

Development Kit User Guide                                                                                                   Getting Started


     Figure 51.      Fully Setup Development Kit with a UART Connection


     Figure 52.      Fully Setup Development Kit with a USB Connection


41112256                                         Rev 3.0                                                December 04, 2018                  62

## Page 63

Development Kit User Guide                                                                                 Getting Started

    4.2.          RF Communications

    4.2.1.           Configure the COM Port

    4.2.1.1.           RS232 Serial Link

    Configure the RS232 serial COM port settings by selecting the port which is connected to the
    Development Kit and specifying the following port settings.
         •    Bits per second:  115200
         •    Data bits: 8
         •    Parity: None
         •    Stop bits: 1
         •    Flow control: Hardware

    4.2.1.2.           USB Port

    Note:         The provided USB driver should be installed in the host computer when using this method to
                  configure the COM port.
    The HL78xx module is automatically detected when the USB cable from the Development Kit is
    connected to the PC.
    Test communications using a PC terminal emulator (HyperTerminal or Clear Terminal, for example)
    by entering AT. The module should answer with OK.


    Figure 53.    AT Communication with the HL78xx


41112256                                  Rev 3.0                                        December 04, 2018              63

## Page 64

Development Kit User Guide                                                                                    Getting Started

    4.2.2.           Make a Voice Call

    Follow these steps to make a voice call:
         1.   Ensure that:
              ▪    Cable power DC jack is connected to CN800.
              ▪    CN803 and CN804 are connected with a jumper.
              ▪    SW203 is switched to position “ON” to start the module.
              ▪    COM port is connected by either UART1 or main USB:
                   ▪    If connecting via the UART1 serial port com, do either of the following:
                        ▪    Connect the UART RS232 cable to CN500 with SW500 in position “UART1”.
                        ▪    Connect the USB cable to CN400 with SW500 in position “USB1”.
                   ▪    If connecting via the main USB port com, connect the USB cable to CN401.
              ▪    SIM card is inserted in SIM holder, CN301.
              ▪    CN302 is connected with a jumper (if using SIM presence detection).
              ▪    RF antenna is connected to CN901.
              ▪    A handset is connected to CN602.
              ▪    SW600 is switched to either “ON” or “OFF” depending on whether an audio amplifier is
                   used.
              ▪    F600 and F601 solder pads are soldered to enable the audio amplifier (these are
                   soldered by default).
         2.   Once the SIM card is ready, the module will respond with +CPIN: READY. Otherwise, it will
              return ERROR.


         3.   Enter ATD<phone number>; to make a call. For example, enter ATD13800138000;.


41112256                                   Rev 3.0                                          December 04, 2018              64

## Page 65

Development Kit User Guide                                                                                  Getting Started

    4.3.          GNSS Communications

    To get GNSS output, ensure that:
         •    Cable power DC jack is connected to CN800.
         •    CN803 and CN804 are connected with a jumper.
         •    SW203 is switched to position “ON” to start the module.
         •    COM port is connected by either UART1 or main USB:
              ▪   If connecting via the UART1 serial port com, do either of the following:
                  ▪    Connect the UART RS232 cable to CN500 with SW500 in position “UART1”.
                  ▪    Connect the USB cable to CN400 with SW500 in position “USB1”.
              ▪   If connecting via the main USB port com, connect the USB cable to CN401.
         •    GNSS antenna is connected to CN900.
         •    Jumper CN904 is shorted if using GNSS antenna bias circuit (for active antenna), or jumper
              CN904 is not shorted if using GNSS antenna (for passive antenna).


41112256                                  Rev 3.0                                         December 04, 2018             65

## Page 66

Development Kit User Guide                                                                           Getting Started

    4.4.         Low Power Consumption Measurement

    Put the HL78xx module inside the snap-in connector in the correct position; refer to section 3.1.2
    Module Orientation Detection for details.
    To get low power consumption measurement in the HL78xx, ensure that the settings and
    configurations specified in section 3.1.1 Power Supply and Figure 11, Figure 12, Figure 13, and
    Figure 14 are followed, or refer to the following sub-sections for different measurement details.

    4.4.1.          Global Current Consumption

    Setup the Development Kit as follows to measure the global current consumption of the HL78xx
    module (baseband + radio frequency) and Development Kit (normal use):

        1.   Plug power supply VBATT_APPLI on CN800.
        2.   Plug jumper CN804.
        3.   Plug jumper CN803.

    4.4.2.          Isolated Development Kit and Module Power
                    Supply

    Follow these steps to only measure the consumption of the HL78xx module (baseband + radio
    frequency) separately from the consumption of the Development Kit:

        1.   Plug power supply VBATT_APPLI on CN800.
        2.   Plug power supply VBATT_BB on CN801 (VBATT_RF on CN802 = NC); or plug power supply
             VBATT_RF on CN802 (VBATT_BB on CN801 = NC).
        3.   Unplug jumper CN804.
        4.   Plug jumper CN803.

    4.4.3.          All Power Supplies are Isolated

    Follow these steps to isolate the consumption of the Development Kit, and to measure the module’s
    baseband consumption and radio frequency consumption separately:

        1.   Plug power supply VBATT_APPLI on CN800.
        2.   Plug power supply VBATT_BB on CN801.
        3.   Plug power supply VBATT_RF on CN802.
        4.   Unplug jumper CN804.
        5.   Unplug jumper CN803.


41112256                                Rev 3.0                                     December 04, 2018            66

## Page 67

5.        ESD Protection

    External ESD protection is available on the Development Kit for the following connectors:
         •    UIM/SIM1 connector
         •    USB main connector
         •    USB-UART0 connector
         •    USB-UART1 connector
         •    RF connector
         •    GNSS connector

    Other interface signals protected on the module are:
         •    RS232-UART1 signals with the MAX13235 transceiver
         •    RS232-UART2 signals with the MAX13235 transceiver

    Caution:       As the test points on the Development Kit are not protected against ESD discharge and they are
                   directly connected to the signal pins of the embedded module, users must be careful when using
                   these TP signals.


41112256                                   Rev 3.0                                         December 04, 2018               67

## Page 68

6.       Reference Documents

        [1]    AirPrime HL78xx Series AT Commands Interface Guide
               Reference Number: 41111821
        [2]    AirPrime HL7800 and HL7800-M Product Technical Specification
               Reference Number: 41111094
        [3]    AirPrime HL7802 Product Technical Specification
               Reference Number: TBD


41112256                              Rev 3.0                                    December 04, 2018           68
