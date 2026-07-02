# thirdparty-bipom-sierra-em75xx-at-command-reference-rev2

> Generated from the sibling PDF for local reference search and review.
> Source PDF: `thirdparty-bipom-sierra-em75xx-at-command-reference-rev2.pdf`
> Source SHA-256: `46090bf6ce8039be3bcf5376af1ff9de9dd0c09cbf4adaff48f0161f017432aa`
> Generated: 2026-07-02
> Extractor: pypdf 6.14.2
> Note: This is text extraction only; tables, columns, images, and scanned pages may need manual verification against the PDF.

## Extraction Summary

- Pages: 140
- Pages without extractable text: 2

## PDF Metadata

| Key | Value |
|---|---|
| Author | Sierra Wireless |
| CreationDate | D:20190129095643Z |
| Creator | FrameMaker 2015.0.5 |
| ModDate | D:20190129111334-08'00' |
| Producer | Acrobat Distiller 19.0 (Windows) |
| Title | AirPrime EM75xx AT Command Reference |

## Page 1

AirPrime EM75xx

           AT Command Reference


                                      41111748
                                        Rev. 2

## Page 2

AirPrime EM75xx AT Command Reference


Important                                    Due to the nature of wireless communications, transmission and reception of data
Notice                                       can never be guaranteed. Data may be delayed, corrupted (i.e., have errors) or be
                                             totally lost. Although signif icant delays or losses of da ta are rare when wireless
                                             devices such as the Sierra Wireless modem are used in a normal manner with a
                                             well-constructed network, the Sierra Wireless modem should not be used in
                                             situations where failure to transmit or receive data could result in damage of any
                                             kind to the user or any other party, including but not limited to personal injury,
                                             death, or loss of proper ty. Sierra Wireless accepts  no responsibility for damages
                                             of any kind resulting from delays or errors in data transmitted or received using
                                             the Sierra Wireless modem, or for failure of the Sierra Wireless modem to
                                             transmit or receive such data.

Safety and                                   Do not operate the Sierra Wireless modem in areas where blasting is in progress,
Hazards                                      where explosive atmospheres may be present, near medical equipment, near life
                                             support equipment, or any equipment which may be susceptible to any form of
                                             radio interference. In such areas, the Sierra Wireless modem    MUST BE
                                             POWERED OFF. The Sierra Wireless modem can transmit signals that could
                                             interfere with this equipment.
                                             Do not operate the Sierra Wireless modem in any aircraft, whether the aircraft is
                                             on the ground or in flight. In aircraft, the Sierra Wireless modem     MUST BE
                                             POWERED OFF. When operating, the Sierra Wireless modem can transmit
                                             signals that could interfere with various onboard systems.


                                             Note:                Some airlines may permit the use of cellular phones while the aircraft is on the
                                             ground and the door is open. Sierra Wireless modems may be used at this time.

                                             The driver or operator of any vehicle sh   ould not operate the Sierra Wireless
                                             modem while in control of a vehicle. Do ing so will detract from the driver or
                                             operator's control and operation of that vehicle. In some states and provinces,
                                             operating such communications devices while in control of a vehicle is an offence.

Limitation of                                The information in this manual is subject to change without notice and does not
Liability                                    represent a commitment on th e part of Sierra Wirele ss. SIERRA WIRELESS AND
                                             ITS AFFILIATES SPECIFICALLY DISCLA IM LIABILITY FOR ANY AND ALL
                                             DIRECT, INDIRECT, SPECIAL, GENERA L, INCIDENTAL, CONSEQUENTIAL,
                                             PUNITIVE OR EXEMPLARY DAMAGES INCLUDING, BUT NOT LIMITED TO,
                                             LOSS OF PROFITS OR REVENUE OR ANTICIPATED PROFITS OR REVENUE
                                             ARISING OUT OF THE USE OR INABILITY TO USE ANY SIERRA WIRELESS
                                             PRODUCT, EVEN IF SIERRA WIRELESS AND/OR ITS AFFILIATES HAS BEEN
                                             ADVISED OF THE POSSIBILITY OF SUCH DAMAGES OR THEY ARE
                                             FORESEEABLE OR FOR CLAIMS BY ANY THIRD PARTY.
                                             Notwithstanding the foregoing, in no event shall Sierra Wireless and/or its
                                             affiliates aggregate liability ar ising under or in connection  with the Sierra Wireless
                                             product, regardless of the number of events,    occurrences, or claims giving rise to
                                             liability, be in excess of the price paid by the purchase r for the Sierra Wireless
                                             product.


2                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41111748

## Page 3

Preface

Patents                                                                                                                                                                                                                                                                                                                         This product may contain technology developed by or for Sierra Wireless Inc. This
                                                                                  product includes technology license d from QUALCOMM®. This product is
                                                                                  manufactured or sold by Sier ra Wireless Inc. or its affiliates under one or more
                                                                                  patents licensed from MMP Portfolio Licensing.

Copyright                                                                                                                                                                                                                        ©2019 Sierra Wireless. All rights reserved.

Trademarks                                                                                                                                                                           Sierra Wireless®, AirPrime®, AirLink®, AirVantage® and the Sierra Wireless logo
                                                                                  are registered trademarks of Sierra Wireless, Inc.
                                                                                  Windows® and Windows Vista ® are registered trademarks of Microsoft
                                                                                  Corporation.
                                                                                  QUALCOMM® is a registered trademark of QUALCOMM Incorporated. Used
                                                                                  under license.
                                                                                  Other trademarks are the property of their respective owners.

Contact
Information
                                                                                    Sales information and technical                                                                             Web: sierrawireless.com/company/contact-us/
                                                                                    support, including warranty and returns                                                                     Global toll-free number: 1-877-687-7795
                                                                                                                                                                                                6:00 am to 5:00 pm PST

                                                                                    Corporate and product information                                                                           Web: sierrawireless.com

Revision
History


   Revision                         Release date                                             Changes
   number

      1                             February 2018                                            Created document


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     3

## Page 4

AirPrime EM75xx AT Command Reference


  Revision                     Release date                                    Changes
  number

     2                        January 2019                                     Updated Modem Status Commands chapter
                                                                                      •                            Updated !DATALOOPBACK (password required), !IMPREF (<carrier_sub-
                                                                                              config>), !PCINFO (response format), !PCTEMP (response format), !PCTEM-
                                                                                              PLIMITS (default parameter values),  !PCVOLTLIMITS (<hw> replaced <lw>,
                                                                                              defaults changed), !USBCOMP (<config         type>,<config         type         desc>)
                                                                                                                          Updated !CUSTOM customizations  —  removed AUTONETWORKMODE,
                                                                                              CMCLIENT, GMMCAUSE7REMAP, IMSIREFRESH, ISVOICEN, LTEREJ-
                                                                                              DELAY, NOROAM, RRCREL7CAPDIS, STKUIN, WIN7MBOPTIONS
                                                                                                                          Added !LTECA, !TMSTATUS, !USBSPEED
                                                                               Updated Diagnostic Commands chapter
                                                                                                                          Updated !RXDEN (usage note),
                                                                                                                          Added !LTERXCONTROL
                                                                               Updated Test Commands chapter
                                                                                                                          Updated !DAFTMACT (description), !DALGRXAGC (usage requirements),
                                                                                              !DALGTXAGC (usage requirements),
                                                                                                                          Added !DAGFTMRXAGC, !DALTXCONTROL, !DARCONFIG, !DARCON-
                                                                                              FIGDROP, !DAWTXCONTROL
                                                                                                                          Removed unimplemented commands, !DAWINFO,
                                                                               Updated GPS Commands chapter
                                                                                                                          Updated !GPSNMEASENTENCE (new sentence types)
                                                                               Updated AirVantage Commands chapter
                                                                                                                          Updated +WDSC <timer_n> parameter details
                                                                               Updated Supported GSM/WCDMA AT Commands chapter
                                                                                                                          Added +CCHC, +CCHO, +CGLA, +CPINR


4                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41111748

## Page 5

Contents

                        About This Guide    . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    8
                                Introduction. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      8
                                Command access. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      8
                                Command timing        . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      8
                                        Interval timing . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    8
                                        Escape sequence guard time           . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    9
                                Result codes. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      9
                                References    . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      9
                                Terminology and acronyms      . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .     9
                                Current firmware versions        . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      9
                                        Version   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    9
                                        Upgrading           . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    9
                                Document structure      . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      9
                                Conventions         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      14


                        AT Password Commands            . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    15
                                Introduction. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      15
                                Command summary     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      15
                                Command reference. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      16


                         Modem Status, Customization, and Reset Commands  . . . . . . . . . . . . . . . .    18
                                Introduction. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      18
                                Command summary     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      18
                                Command reference. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      20


                        Diagnostic Commands   . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    52
                                Introduction. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      52
                                Command summary     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      52
                                Command reference. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      53


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          5

## Page 6

AirPrime EM75xx AT Command Reference


                         Test Commands        . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           58
                                  Introduction     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             58
                                  Command summary          . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             59
                                  Command reference     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             60


                         Memory Management Commands    . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           71
                                  Introduction     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             71
                                  Command summary          . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             71
                                  Command reference     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             72


                         GNSS Commands            . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           75
                                  Introduction     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             75
                                  Command summary          . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             75
                                  Command reference     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             76
                                           Error codes    . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           93


                         SIM Commands         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           96
                                  Introduction     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             96
                                  Command summary          . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             96
                                  Command reference     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             97


                         OMA-DM Commands           . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .           98
                                  Introduction     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             98
                                  Command summary          . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             98
                                  Command reference     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             99


                         SAR Backoff and Thermal Control Commands . . . . . . . . . . . . . . . . . . . . . .           102
                                  Introduction     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             102
                                  Command summary          . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             102
                                  Command reference     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .             103


6                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41111748

## Page 7

Contents


                                 AirVantage Commands         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .    108
                                            Introduction. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      108
                                            Command summary     . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      108
                                            Command reference. . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      109


                                 Supported GSM   /   WCDMA AT Commands            . . . . . . . . . . . . . . . . . . . . . . . . . .    119


                                 Band Definitions        . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      128


                                 ASCII Table  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      130


                                 Index (AT commands)       . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      131


                                 Index         . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .      135


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          7

## Page 8

1: About This Guide                                                                                                                                                                       1

                                                                          Introduction

                                                                          This document describes supported standard and proprietary AT
                                                                          commands available for Sierra Wireless AirPrime  ® products, and
                                                                          provides details where commands   vary from the standards. These
                                                                          commands are intended for use by OEMs, and are supplemental to
                                                                          the standard AT commands for GSM devices defined by the 3GPP
                                                                          (3rd Generation Partnership Project) in   TS 27.007 AT command set
                                                                          for User Equipment (UE)  and TS 27.005 Use of Data Terminal
                                                                          Equipment  —  Data Circuit term                inating Equipment (DTE-DCE)
                                                                          interface for Short Message Service (SMS) and Cell Broadcast
                                                                          Service (BSE).


                                                                          Note:                When designing applications that use these AT commands, use
                                                                          Skylight™             or other Sierra Wireless applications as functionality templates to
                                                                          ensure proper use of command groups. For questions or concerns relating to
                                                                          command implementation, please contact your Sierra Wireless account
                                                                          representative.

                                                                          Command access

                                                                          Most commands in this reference are password-protected. To use
                                                                          these commands, you must enter the correct password using
                                                                          AT!ENTERCND on page         16. Once the password is entered, all
                                                                          commands are available and remain available until the modem is
                                                                          reset or powered off and on.
                                                                          The password assigned to   AT!ENTERCND is unique to each carrier
                                                                          and is configured onto the modem during manufacture. If you do not
                                                                          know your password, contact your Sierra Wireless Account Manager.

                                                                          Command timing

                                                                          Interval timing

                                                                          Some commands require time to process before additional
                                                                          commands are entered. For example, the modem returns     OK when it
                                                                          receives AT!DAFTMACT. If AT!DARCONFIG is received too soon after
                                                                          this, the modem returns an error.
                                                                          When building automated test scripts, ensure that sufficient delays
                                                                          are embedded, where necessary, to avoid these errors.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          8

## Page 9

About This Guide

                                                       Escape sequence guard time

                                                       The AT escape sequence “+++” requires a guard time of 1.0         seconds before and
                                                       after it is used.
                                                       Result codes

                                                       Result codes are not shown in the command tables unless special conditions
                                                       apply. Generally the result code  OK is returned when the command has been
                                                       executed. ERROR may be returned if parameters are out of range, and is returned
                                                       if the command is not recognized or is not permitted in the current state or
                                                       condition of the modem.

                                                       References

                                                       This guide covers the command sets used by OEMs, designers and testers of
                                                       Sierra Wireless AirPrime products,   plus general operational use commands.
                                                       You may also want to consult the other documents available on our website at
                                                       www.sierrawireless.com.
                                                       Terminology and acronyms

                                                       This document makes wide use of acronyms that are in common use in data
                                                       communications and cellular technology.
                                                       Current firmware versions

                                                       Ve   r   s   i   o   n

                                                       To determine your firmware revision, enter the identification command AT+GMR.

                                                       Upgrading

                                                       If your modem firmware is an earlier version, you can acquire updated firmware
                                                       by contacting your account manager.
                                                       Document structure

                                                       This document describes the proprietary commands listed in the tables below   —
                                                        each table corresponds to a specific chapter.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   9

## Page 10

AirPrime EM75xx AT Command Reference


                                                        AT Password Commands   —   Commands used to enable access to password-
                                                        protected AT commands and to set the AT command password.

                      Table 1-1:                AT password commands

                        Command                                                   Description                                                                                                               Page

                          !ENTERCND                                               Enable access to password-protected commands                                                                              16

                          !SETCND                                                 Set AT command password                                                                                                   17

                                                         Modem Status, Customization, and Reset Commands      —  Commands used to
                                                        determine modem status, adjust customization settings, and reset the modem.

                    Table 1-2:                Modem status commands

                      Command                                                    Description                                                                                                               Page

                         !ANTSEL                                                Set  /  query external antenna select configuration                                                                       20

                         !BAND                                                  Select  /  return frequency band set                                                                                      22

                         !BOOTHOLD                                              Reset modem and wait in bootloader for firmware download                                                                  24

                         !CUSTOM                                                Set  /  return customization settings                                                                                     25

                         !DATALOOPBACK                                          Enable  /  disable and configure loopback mode                                                                            28

                         !GCFEN                                                 Enable  /  disable GCF test mode                                                                                          29

                         !GETBAND                                               Return the current active band                                                                                            29

                         !GSTATUS                                               Return operational status                                                                                                 30

                         !HWID                                                  Display hardware version                                                                                                  30

                         !IMPREF                                                Query  /  set Image Management preferences                                                                                31

                         !LTECA                                                 Enable  /  disable LTE Carrier Aggregration or Display supported LTE                                                      33
                                                                                CA pairs

                         !LTEINFO                                               Display LTE network information                                                                                           35

                         !NVENCRYPTIMEI                                         Write unencrypted IMEI to modem                                                                                           37

                         !NVPLMN                                                Provision  /  display PLMN list for Network Personalization locking                                                       38

                         !PCINFO                                                Return power control status information                                                                                   39

                         !PCOFFEN                                               Set  /  return Power Off Enable state                                                                                     40

                         !PCTEMP                                                Return current temperature information                                                                                    40

                         !PCTEMPLIMITS                                          Set  /  report temperature state limit values                                                                             41

                         !PCVOLT                                                Return current power supply voltage information                                                                           42

                         !PCVOLTLIMITS                                          Set  /  report power supply voltage state limit values                                                                    43

                         !PRIID                                                 Set  /  report module PRI part number and revision                                                                        44

                         !RESET                                                 Reset modem                                                                                                               44

                         !SCACT                                                 Activate  /  deactivate data connection                                                                                   45


10                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 11

About This Guide


                    Table 1-2:                Modem status commands (Continued)

                      Command                                                  Description                                                                                                            Page

                        !TMSTATUS                                              Report Thermal Mitigation Status                                                                                      46

                        !USBCOMP                                               Set   /  report USB interface configuration                                                                           47

                        !USBINFO                                               Return information from active USB descriptor                                                                         48

                        !USBPID                                                Set  /  report product ID in USB descriptor                                                                           49

                        !USBSPEED                                              Set  /  report USB speed                                                                                              50

                        &V                                                     Return operating mode AT configuration parameters                                                                     51

                                                       Diagnostic Commands    —   Commands used to select frequency bands and
                                                       diagnose problems.

                     Table 1-3:                Diagnostic commands

                      Command                                                  Description                                                                                                            Page

                        !BCFWUPDATESTATUS                                      Report status of most recent firmware update attempt                                                                  53

                        !ERR                                                   Display diagnostic information                                                                                        54

                        !GCCLR                                                 Clear crash dump data                                                                                                 54

                        !GCDUMP                                                Display crash dump data                                                                                               55

                        !LTERXCONTROL                                          Enable  /  disable LTE receive (Rx) diversity during Carrier                                                          56
                                                                               Aggregation

                        !RXDEN                                                 Enable  /  disable WCDMA/  LTE  /  TD-SCDMA receive (Rx) diversity                                                    57

                                                       Test Commands   —  Commands required to place the modem in particular modes
                                                       of operation, test host connectivity, and to configure the transmitters and receivers
                                                       for test measurements.

                    Table 1-4:                Test commands

                      Command                                                  Description                                                                                                            Page

                        !DACGPSCTON                                            Return GPS CtoN and frequency measurement                                                                             60

                        !DACGPSMASKON                                          Set CGPS IQ log mask                                                                                                  60

                        !DACGPSSTANDALONE                                      Enter  /  exit StandAlone (SA) RF mode                                                                                61

                        !DACGPSTESTMODE                                        Start  /  stop CGPS diagnostic task                                                                                   61

                        !DAFTMACT                                              Put modem into Factory Test Mode                                                                                      62

                        !DAFTMDEACT                                            Put modem into online mode from Factory Test Mode                                                                     62

                        !DAGFTMRXAGC                                           Get FTM Rx AGC (Primary or Diversity)                                                                                 63

                        !DALGRXAGC                                             Return Rx AGC value (LTE only)                                                                                        64

                        !DALGTXAGC                                             Return Tx AGC value and transmitter parameters (LTE only)                                                             65

                        !DALTXCONTROL                                          Configure LTE Tx parameters (LTE only)                                                                                67


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               11

## Page 12

AirPrime EM75xx AT Command Reference


                     Table 1-4:                Test commands (Continued)

                       Command                                                     Description                                                                                                                  Page

                         !DAOFFLINE                                               Place modem offline                                                                                                           68

                         !DARCONFIG                                               Set Band and Channel                                                                                                          69

                         !DARCONFIGDROP                                           Drop Radio Configurations                                                                                                     70

                         !DAWTXCONTROL                                            Configure WCDMA Tx Power (WCDMA only)                                                                                         70

                                                         Memory Management Commands     —  Commands that control the data stored in
                                                         non-volatile memory of the modem.

                     Table 1-5:                Memory management commands

                       Command                                                     Description                                                                                                                  Page

                         !NVBACKUP                                                Back up device configuration                                                                                                  72

                         !RMARESET                                                Back up device configuration                                                                                                  74

                                                         GNSS Commands   —  Supported on GNSS-enabled modems only.

                    Table 1-6:                GNSS commands

                       Command                                                     Description                                                                                                                  Page

                         !GPSAUTOSTART                                            Configure GPS auto-start features                                                                                             76

                         !GPSCLRASSIST                                            Clear specific GPS assistance data                                                                                            78

                         !GPSCOLDSTART                                            Clear all GNSS assistance data                                                                                                79

                         !GPSEND                                                  End an active session                                                                                                         79

                         !GPSFIX                                                  Initiate GPS position fix                                                                                                     80

                         !GPSLBSAPN                                               Set GPS LBS APNs                                                                                                              81

                         !GPSLOC                                                  Return last known location of the modem                                                                                       83

                         !GPSMOMETHOD                                             Set  /  report GPS MO method                                                                                                  84

                         !GPSNMEACONFIG                                           Enable and set NMEA data output rate                                                                                          85

                         !GPSNMEASENTENCE                                         Set  /  report NMEA sentence type                                                                                             86

                         !GPSPORTID                                               Set  /  report port ID to use over TCP/IP                                                                                     87

                         !GPSSATINFO                                              Request satellite information                                                                                                 88

                         !GPSSTATUS                                               Request current status of a position fix session                                                                              89

                         !GPSSUPLURL                                              Set  /  report SUPL server URL                                                                                                90

                         !GPSSUPLVER                                              Set  /  report SUPL server version                                                                                            91

                         !GPSTRACK                                                Initiate local tracking (multiple fix) session                                                                                92

                         +WANT                                                    Enable  /  disable GNSS antenna power                                                                                         93


12                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 13

About This Guide


                                                        SIM Commands    —  Commands used to communicate with an installed (U)SIM.

                    Table 1-7:                SIM commands

                       Command                                                   Description                                                                                                              Page

                         +UIMS                                                  Select active SIM interface                                                                                               97

                                                        OMA-DM Commands    —  Commands used to configure DM (Device Management)
                                                        accounts, sessions, and host–device–server interactions.

                    Table 1-8:                OMA-DM commands

                       Command                                                   Description                                                                                                              Page

                         !HOSTDEVINFO                                           Configure host device details                                                                                             99

                         !IMSTESTMODE                                           Enable  /  disable IMS test mode                                                                                          100

                         !OSINFO                                                Configure host device operating system information                                                                        101

                                                        SAR Backoff and Thermal Control Commands    —  Commands used to configure
                                                        SAR options, and thermal mitigation   algorithm parameters and limits.

                    Table 1-9:                SAR backoff and thermal control commands

                       Command                                                   Description                                                                                                              Page

                         !MAXPWR                                                Set  /  report maximum Tx power                                                                                           103

                         !SARBACKOFF                                            Set  /  report offset from maximum Tx power                                                                               104

                         !SARINTGPIOMODE                                        Set  /  report default pull mode for SAR interrupt GPIOs                                                                  105

                         !SARSTATE                                              Set  /  report SAR backoff state                                                                                          106

                         !SARSTATEDFLT                                          Set  /  report default SAR backoff state                                                                                  107

                                                        AirVantage Commands  —  Commands used to interact with AirVantage.

                    Table 1-10:                AirVantage commands

                       Command                                                   Description                                                                                                              Page

                         +WDSC                                                  Configure AirVantage Management Services                                                                                  109

                         +WDSE                                                  Display most recent AirVantage Management Services error                                                                  111

                         +WDSG                                                  Display AirVantage Management Services status information                                                                 11   2

                         +WDSI                                                  Activate  /  deactivate AirVantage Management Services unsolicited                                                        11   3
                                                                                notifications

                         +WDSI (notification)                                   AirVantage Management Services events  —  Unsolicited notification                                                        11   4

                         +WDSR                                                  Reply to AirVantage server request                                                                                        11   6

                         +WDSS                                                  Configure  /  connect AirVantage Management Services session                                                              11   7


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               13

## Page 14

AirPrime EM75xx AT Command Reference

                                                    Conventions

                                                    The following format conventions are used in this reference:
                                                    Character codes or keystrokes that are described with words or standard
                                                    abbreviations are shown within angle brackets using a different font, such as
                                                    <CR> for Carriage Return and  <space> for a blank space character.
                                                    Numeric values are decimal unless prefixed as noted below.
                                                    Hexadecimal values are shown with a prefix of 0x, i.e. in the form 0x3D.
                                                    Binary values are shown with a prefix of 0b, i.e. in the form 0b00111101.
                                                    Command and register syntax is noted using an alternate font:     !CHAN  =  <c>[,b].
                                                    The leading “AT” characters are not shown but must be included before all
                                                    commands except as noted in the reference tables.
                                                    Characters that are required are shown in uppercase; parameters are noted in
                                                    lowercase. Required parameters are enclosed in angle brackets (    <n>) while
                                                    optional parameters are enclosed within square brackets (    [x]). The brackets are
                                                    not to be included in the command string.
                                                    Commands are presented in table format. Each chapter covers the commands
                                                    related to that subject and presents a summary table to help you locate a needed
                                                    command. Commands are in ASCII alphabetical order in the body of each
                                                    chapter.
                                                    Any default settings are noted in the command tables. Note that these are the
                                                    factory default settings and   not the default parameter value assumed if no
                                                    parameter is specified.
                                                    Result Code                                  This is a numeric or text code that is returned after all commands
                                                    (except resets)  —   text codes are returned if verbose responses are enabled. Only
                                                    one result code is returned for a command line regardless of the number of
                                                    individual commands contained on the line.
                                                    Response                                     This term indicates a response from the modem that is issued prior to
                                                    a result code. Reading registers or issu ing commands that report information will
                                                    provide a response followed by a result code unless the command generates an
                                                    error.
                                                    Responses and result codes  from the modem, or host system software prompts,
                                                    are shown in this font:
                                                            CONNECT 14400


14                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 15

2: AT Password Commands                                                                                                                                                                                                                                                                                        2
                                                                                                                         Introduction

                                                                                                                         Many AT commands described in this document are password-
                                                                                                                         protected. This chapter describes how to enter or change the
                                                                                                                         password used to gain access to the protected commands.
                                                                                                                         Command summary

                                                                                                                         Table         2-1 on page          15 lists the commands described in this chapter.

                                    Table 2-1:                AT password commands

                                       Command                                                                                     Description                                                                                                                                                                   Page

                                           !ENTERCND                                                                               Enable access to password-protected commands                                                                                                                                  16

                                           !SETCND                                                                                 Set AT command password                                                                                                                                                       17


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      15

## Page 16

AirPrime EM75xx AT Command Reference

                                                                     Command reference

Table 2-2:                AT command password details

  Command                                                    Description

  !ENTERCND                                                  Enable access to password-protected commands

                                                             To gain access to password-protected AT commands (unlock the commands), enter the
                                                             password correctly using this command. The initial password is configured onto the modem
                                                             during manufacture.
                                                             After unlocking the protected command, the password can be changed using !SETCND. If you
                                                             do not know the password, contact your Sierra Wireless account manager.
                                                             Once the password has been entered correctly, the password-protected AT commands are
                                                             available until the modem is reset or powered off and on.

                                                             Warning:                  !ENTERCND does not accept blank passwords. If the password has been
                                                             cleared (using !SETCND), you will not be able to use password-protected commands, and
                                                             will have to contact Sierra Wireless for help to reset the password.


                                                             Password required: Yes  —  Query format only.

                                                             Usage:
                                                                                                 Execution:                                                 AT!ENTERCND  =  <“key”>
                                                                    Response:                                               OK
                                                                    Purpose:                                                                        Unlock password-protected commands.
                                                                                                 Query:                                                                                                          AT!ENTERCND?
                                                                    Response:                                               <key> (if unlocked)
                                                                    Purpose:                                                                        This command is password-protected. After entering the password correctly
                                                                                                   using the execution operation (“ =  ”), you can use this command to display
                                                                                                   the password as a reminder.

                                                             Parameters:
                                                             <“key”> (Password stored in NV memory)
                                                                                                        Password must be entered with quotation marks.
                                                                            (For example, AT!ENTERCND  =  ”ExamplePW”.)
                                                                                                        Password length: 4–10 characters (0–9, A–Z, upper or lower case)
                                                                                                        Characters may be entered in ASCII format, or in Hex format. (For example:
                                                                            “myPass3” or “ABCDEF01234”.)


16                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 17

AT Password Commands


Table 2-2:                AT command password details (Continued)

   Command                                                                 Description

   !SETCND                                                                 Set AT command password

                                                                           Change the password used for the !ENTERCND command.

                                                                           Password required: Yes

                                                                           Usage:
                                                                                                               Execution:                                                 AT!SETCND  =  <“key”>
                                                                                   Response:                                               OK
                                                                                   Purpose:                                                                        Sets <“Key”> as the new password for accessing protected commands.

                                                                           Parameters:
                                                                           <“key”> (New password)
                                                                                                                        Password must be entered with quotation marks (for example,
                                                                                             AT!SETCND  =  ”NewPW”).
                                                                                                                        Password length: 4–10 characters (0–9, A–Z, upper or lower case)
                                                                                                                        Characters may be entered in ASCII format, or in Hex format. (For example:
                                                                                             “myPass3” or “ABCDEF01234”.)

                                                                           Warning:                  Do NOT enter a null password (that is, the <“Key”> cannot be ““) — you will NOT
                                                                           be able to use password-protected commands, and will have to contact Sierra Wireless for
                                                                           help to reset the password.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               17

## Page 18

3:                    Modem Status, Customization, and Reset                                                                                                                                               3
                        Commands
                                                                                 Introduction
                                                                                 This chapter describes commands used to reset the modem, adjust
                                                                                 customization settings, retrieve th e hardware version, and monitor
                                                                                 the temperature, voltage, and modem status.
                                                                                 Command summary

                                                                                 Table         3-1 lists the commands described in this chapter.

                        Table 3-1:                Modem status commands

                          Command                                                       Description                                                                                                         Page

                            !ANTSEL                                                    Set  /  query external antenna select configuration                                                                  20

                            !BAND                                                      Select  /  return frequency band set                                                                                 22

                            !BOOTHOLD                                                  Reset modem and wait in bootloader for firmware download                                                             24

                            !CUSTOM                                                    Set  /  return customization settings                                                                                25

                            !DATALOOPBACK                                              Enable  /  disable and configure loopback mode                                                                       28

                            !GCFEN                                                     Enable  /  disable GCF test mode                                                                                     29

                            !GETBAND                                                   Return the current active band                                                                                       29

                            !GSTATUS                                                   Return operational status                                                                                            30

                            !HWID                                                      Display hardware version                                                                                             30

                            !IMPREF                                                    Query  /  set Image Management preferences                                                                           31

                            !LTECA                                                     Enable  /  disable LTE Carrier Aggregration or Display supported                                                     33
                                                                                       LT   E       C   A     p   a   i   r   s

                            !LTEINFO                                                   Display LTE network information                                                                                      35

                            !NVENCRYPTIMEI                                             Write unencrypted IMEI to modem                                                                                      37

                            !NVPLMN                                                    Provision  /  display PLMN list for Network Personalization                                                          38
                                                                                       locking

                            !PCINFO                                                    Return power control status information                                                                              39

                            !PCOFFEN                                                   Set  /  return Power Off Enable state                                                                                40

                            !PCTEMP                                                    Return current temperature information                                                                               40

                            !PCTEMPLIMITS                                              Set  /  report temperature state limit values                                                                        41

                            !PCVOLT                                                    Return current power supply voltage information                                                                      42

                            !PCVOLTLIMITS                                              Set  /  report power supply voltage state limit values                                                               43


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      18

## Page 19

Modem Status, Customization, and Reset Commands


Table 3-1:                Modem status commands (Continued)

                                          Command                                                                                          Description                                                                                                                                                                             Page

                                              !PRIID                                                                                       Set  /  report module PRI part number and revision                                                                                                                                      44

                                              !RESET                                                                                       Reset modem                                                                                                                                                                             44

                                              !SCACT                                                                                       Activate  /  deactivate data connection                                                                                                                                                 45

                                              !TMSTATUS                                                                                    Report Thermal Mitigation Status                                                                                                                                                        46

                                              !USBCOMP                                                                                     Set  /  report USB interface configuration                                                                                                                                              47

                                              !USBINFO                                                                                     Return information from active USB descriptor                                                                                                                                           48

                                              !USBPID                                                                                      Set  /  report product ID in USB descriptor                                                                                                                                             49

                                              !USBSPEED                                                                                    Set  /  report USB speed                                                                                                                                                                50

                                              &V                                                                                           Return operating mode AT configuration parameters                                                                                                                                       51


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               19

## Page 20

AirPrime EM75xx AT Command Reference

                                                             Command reference

Table 3-2:                Modem status, cust                                     omization, and reset commands

  Command                                                         Description

 !ANTSEL                                                          Set   /   query external antenna select configuration

                                                                  Configure the modem to use available GPIOs to select which antenna to use for each
                                                                  specified frequency band. (Any of the available GPIOs that are not needed for a specific
                                                                  band should be configured as not required.)
                                                                  When the modem switches to a frequency band that has been configured using this
                                                                  command, the GPIOs are driven as specified and the host uses them to tune the
                                                                  external antenna appropriately. (This applies whether this is a primary band, or as the
                                                                  secondary component carrier as part of LTE CA (Carrier Aggregation)). If the modem
                                                                  switches to a band that has not been configured, the host uses the default antenna.


                                                                  Note:               Frequency bands are RAT-independent. For example, Band 5 corresponds to
                                                                  any 850-band technology (CDMA, WCDMA, LTE, GSM).

                                                                  When designing the system, and configuring the device:
                                                                                                     Perform system level testing to ensure that the antenna switching feature does not
                                                                        introduce any handover issues. The tunable antenna should be designed to ensure
                                                                        that it can retune in <         5        µs (recommended) and <         10        µs (maximum).
                                                                                                     Make sure there are no conflicts between primary (PCell) and secondary (SCell)
                                                                        cells for all supported LTE CA combinations, since a conflict can detune the PCell
                                                                        during LTE CA, resulting in reduced performance. (A conflict occurs when the
                                                                        primary band is configured to drive a GPIO one way (high or low), and the
                                                                        secondary is configured to drive the same GPIO the other way (low or high).

                                                                  Password required: Yes
                                                                  Reset required to apply changes:  Yes
                                                                  Persistent across power cycles: Yes

                                                                  Usage:
                                                                                                      Execution:                                                 AT!ANTSEL  =  <band>, <gpio1>, <gpio2>, <gpio3>[, <gpio4>]
                                                                        Response:                                               OK
                                                                        Purpose:                                                                        Configure the GPIOs for the specified <band>.
                                                                                                      Query:                                                                                                         AT!ANTSEL?
                                                                        Response:                                               BAND <band a>: <gpio1>, <gpio2>, <gpio3>[, <gpio4>]
                                                                                                   BAND <band b>: <gpio1>, <gpio2>, <gpio3>[, <gpio4>]
                                                                                                   ...
                                                                                                   Conflicts:    (Note: Heading appears only if there are conflicts.)
                                                                                                   <band q>+<band r>: <gpio1>, <gpio2>, <gpio3>[, <gpio4>]
                                                                                                                                             (Note: GPIOs in conflict appear as ‘C’)
                                                                                                   ...
                                                                                                   OK

                                                                  (Continued on next page)


20                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 21

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                               Description

   !ANTSEL (continued)                                                                   Set   /   query external antenna select configuration (continued)

                                                                                                 Examples:                                                  BAND 2: 1, 0, 1, 1
                                                                                                                                     BAND 5: 1, 1, 2, 2

                                                                                                                                     Conflict:
                                                                                                                                     B2 + B5: 1, C, 1, 1(<gpio2> has a conflict (‘C’))
                                                                                                                                     B5 + B2: 1, C, 1, 1

                                                                                                                                     OK
                                                                                                 Purpose:                                                                        Display the current external antenna select configuration.
                                                                                                                             Query List:                                            AT!ANTSEL  =  ?
                                                                                                 Purpose:                                                                        Display valid parameter values and command format.

                                                                                         Parameters:
                                                                                         <band> (RF band)
                                                                                                                                     low- or high-frequency 3GPP band number, as appropriate. (See Table         13-2 on
                                                                                                          page         129 for a full list of low-, mid-, and high-frequency bands.)
                                                                                                                                     Valid range: 0–69. Band support is product specific  —  see the device’s Product
                                                                                                          Specification or Product Technical Specification document for d etails.
                                                                                         <gpio1>, <gpio2>, <gpio3>, <gpio4> (GPIO configurations.)
                                                                                                                                     0  =  Logic low
                                                                                                                                     1  =  Logic high
                                                                                                                                     2  =  Not used for antenna selection (Default value for <gpio4> if not specified.)
                                                                                                                                     Note: <gpio4> availability is device-specific  —  see the appropriate Product
                                                                                                          Technical Specification for details.)
                                                                                                                                     gpio1–4 correspond to ANT_CTRL0–3


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               21

## Page 22

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

  Command                                                                 Description

  !BAND                                                                   Select   /   return frequency band set

  Note:               The ‘Basic’ command                                 Configure the modem to operate on a set of frequency bands, look up available sets,
  and response versions are                                               create new sets, or return the current selection.

  used if you haven’t entered the                                         Password required: Yes  —  Execution (Extended) format
  required password. (See
  Command access on page        8.)                                       Usage:
                                                                                                              Execution (Basic):
                                                                                                                AT!BAND  =  <Index>
                                                                                 Response:                                               OK
                                                                                 Purpose:                                                                        Select an existing set of bands.
                                                                                                              Execution (Extended):
                                                                                                                AT!BAND  =  <Index>, ”<Name>”, < GWmask>[, <Lmask>[, <Lmask2>[,
                                                                                                                <Tdsmask>[, <Lmask3>,<Lmask4>]]]]
                                                                                 Response:                                               OK
                                                                                 Purpose:                                                                        Create a new set of bands.
                                                                                 Query:                  AT!BAND?
                                                                                 Response:                                                  Index, Name,       GW Band Mask   L Band Mask 1    TDS Band Mask
                                                                                                               L Band Mask 2     L Band Mask 3   L Band Mask 4 <CR><LF>
                                                                                                               <Index>, <Name>    <GWmask>    <Lmask1>   <Tdsmask>
                                                                                                               <Lmask2>   <Lmask3>   <Lmask4>
                                                                                                               OK
                                                                                                  or                                                         (If the current band mask doesn’t match a band set)
                                                                                                               Unknown band mask. Use AT!BAND to set band.
                                                                                                               <Index>
                                                                                                               OK
                                                                                 Purpose:                                                                        Report the current band selection. (<GWmask>, <Lmask>, and
                                                                                                               <Tdsmask> will appear only in Extended responses, and only if appli-
                                                                                                               cable.)
                                                                                                              Query List:                                            AT!BAND  =  ?
                                                                                 Purpose:                                                                        Returns the command format and valid parameter values.

                                                                          Parameters:
                                                                          <Index> (Index of a band set. Use the Query List command to display all supported
                                                                                         sets)
                                                                                                                     Valid range: 0–13 (Hexadecimal. There are 20 possible values.)
                                                                          <Name> (Name of the band set)
                                                                                                                     ASCII string  —  Up to 30 characters
                                                                          <GWmask> (GSM  /  WCDMA bands included in the set)
                                                                                                                     Format: 64-bit bitmask
                                                                                                                     Example values (Available bands are device-dependent. Use the extended query
                                                                                         command to display the list of bands available for your device.):

                                                                                         0000000000000001  —  BC0-A
                                                                                         0000000000000002  —  BC0-B
                                                                                         ...
                                                                                         0000000080000000  —  BC15
                                                                                         0002000000000000  —  W900
                                                                                         1000000000000000  —  B19 (850)

                                                                          (Continued on next page)

22                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 23

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

  Command                                                                 Description

  !BAND (continued)                                                      Select   /   return frequency band set (continued)

                                                                         <Lmask1> (LTE bands included in the set)
                                                                                                                     Format: 64-bit bitmask
                                                                                                                     Example values (Available bands are device-dependent. Use the extended query
                                                                                        command to display the list of bands available for your device.):
                                                                                                                            0000000000000001  —  Band 1
                                                                                               0000000000000002  —  Band 2
                                                                                               ...
                                                                                               0000008000000000  —  Band 40
                                                                                               0000010000000000  —  Band 41
                                                                                                                     Note  —  The full list of bands in the set is spread across <Lmask1>–<Lmask4>.
                                                                         <Lmask2> (LTE bands included in the set)
                                                                                                                     Format: 64-bit bitmask
                                                                                                                     Example values (Available bands are device-dependent. Use the extended query
                                                                                        command to display the list of bands available for your device.):
                                                                                                                            0000000000000002  —  Band 66
                                                                                                                     Note  —  The full list of bands in the set is spread across <Lmask1>–<Lmask4>.
                                                                         <Lmask3> (Reserved for future use)
                                                                                                                     Format: 64-bit bitmask
                                                                                                                     Required value: 0000000000000000
                                                                                                                     Note  —  The full list of bands in the set is spread across <Lmask1>–<Lmask4>.
                                                                         <Lmask4> (LTE bands included in the set)
                                                                                                                     Format: 64-bit bitmask
                                                                                                                     Example values (Available bands are device-dependent. Use the extended query
                                                                                        command to display the list of bands available for your device.):
                                                                                                                            0800000000000000  —  B252
                                                                                                                            4000000000000000  —  B255
                                                                                                                     Note  —  The full list of bands in the set is spread across <Lmask1>–<Lmask4>.
                                                                         <Tdsmask> (TD-SCDMA bands included in the set)
                                                                                                                     Format: 64-bit bitmask
                                                                                                                     Example values (Available bands are device-dependent. Use the extended query
                                                                                        command to display the list of bands available for your device.):
                                                                                                                            0000000000000001  —  TDS B34
                                                                                               0000000000000002  —  TDS B35
                                                                                               0000000000000004  —  TDS B36
                                                                                               0000000000000008  —  TDS B38
                                                                                               0000000000000010  —  TDS B40
                                                                                               0000000000000020  —  TDS B39


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               23

## Page 24

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                                                          Description

   !BOOTHOLD                                                                                                        Reset modem and wait in bootloader for firmware download

                                                                                                                    Prepare for a firmware download by resetting the modem and waiting in ‘boot and hold’
                                                                                                                    mode.

                                                                                                                    Password required: No

                                                                                                                    Usage:
                                                                                                                                                        Execution:                                                AT!BOOTHOLD
                                                                                                                               Response:                                               OK
                                                                                                                               Purpose:                                                                        Force the modem to backup user NV options, reset, and then wait in
                                                                                                                                                                               boot and hold mode for a firmware download.


24                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 25

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                    Description

  !CUSTOM                                                                     Set   /   return customization settings

  Note:               Some customizations                                     Set or return several customization values.
  may not be available for                                                    Password required: Yes
  certain chipsets, firmware
  revisions, or devices.                                                      Usage:
                                                                                                                  Execution:                                                 AT!CUSTOM  =  <customization>, <value>
                                                                                     Response:                                               OK
                                                                                     Purpose:                                                                        Assign <value> to a specific <customization> setting.
                                                                                                                  Query:                                                                                                        AT!CUSTOM?
                                                                                     Response:                                               (list of enabled <customization>s)
                                                                                                                      OK
                                                                                     Purpose:                                                                        Display customizations that are currently enabled.
                                                                                                                  Query list:                                                      AT!CUSTOM  =  ?
                                                                                     Purpose:                                                                       Return a list of valid <customization> values.

                                                                              Parameters:
                                                                              <value> (Value being assigned to a specific <customization> setting)
                                                                                                                          Descriptions are included in each of the customizations described below.
                                                                                                                          Numeric value. Valid range depends on the <customization> type.
                                                                              <customization> (String identifying customization setting. The default value for all
                                                                                              customizations is 0.)

                                                                              Note:                Use quotation marks around the customization string. For example,
                                                                              AT!CUSTOM  =  ”CSDOFF”,0.

                                                                                                                          “CFUNPERSISTEN“  —  Enable  /  disable persistence (across power cycles) of
                                                                                              AT+CFUN setting.
                                                                                              <value>:
                                                                                                                                 0 = Disable (+CFUN setting does not persist across power cycle)
                                                                                                                                 1 = Enable (+CFUN setting persists across power cycle)
                                                                                                                          “CSVOICEREJECT” (Pending future upgrade)  —  Enable  /  disable ability to ignore
                                                                                              incoming voice call pages from the network.
                                                                                              <value>:
                                                                                                                                 0 = Process pages as per device capabilities (default)
                                                                                                                                  1 = Ignore paging (type 1 and 2) messages
                                                                                                                                 2 = Reject call setup (voice and circuit-switched VT), returning cause code 88
                                                                                                     (Incompatible destination)
                                                                                                                                 3 = Process voice pages as per device capabilities, and reject call setup
                                                                                                     (circuit-switched VT), returning cause code 88 (Incompatible destination)
                                                                                                                                  4 = Reject voice pages, returning cause code 65 (Bearer service not imple-
                                                                                                     mented), and reject call setup (circuit-switched VT), returning cause code 88
                                                                                                     (Incompatible destination)

                                                                              (Continued on next page)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               25

## Page 26

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

  Command                                                                       Description

  !CUSTOM                                                                       Set   —   query customization settings (continued)
  (continued)
                                                                                                                            “FASTENUMEN“  —  Enable  /  disable fast enumeration for warm  /  cold boot.
                                                                                                <value>:
                                                                                                                                   0 = Disable fast enumeration (Default)
                                                                                                                                   1 = Enable fast enumeration for cold boot and disable for warm boot
                                                                                                                                   2 = Enable fast enumeration for warm boot and disable for cold boot
                                                                                                                                   3 = Enable fast enumeration for warm and cold boot
                                                                                                                            “GPIOSARENABLE”  —  Indicate whether SAR backoff is controlled by GPIOs or
                                                                                                by AT commands.
                                                                                                <value>:
                                                                                                                                   0 = Controlled by AT commands (default)
                                                                                                                                   1 = Controlled by GPIOs
                                                                                                                            “GPSENABLE”  —  Enable  /  disable the GPS feature.
                                                                                                <value>:
                                                                                                                                    0 = GPS disabled
                                                                                                                                   1 = MO & MT enabled regardless of GPS_DISABLE setting
                                                                                                                                    2 = MO enabled regardless of GPS_DISABLE setting
                                                                                                                                    3 = MT enabled regardless of GPS_DISABLE setting
                                                                                                                                   4 = MO & MT enabled but are gated by GPS_DISABLE setting
                                                                                                                                   5 = MO enabled but is gated by GPS_DISABLE setting
                                                                                                                                   6 = MT enabled but is gated by GPS_DISABLE setting
                                                                                                                                    <value> + 80 = Disable GLONASS
                                                                                                        (For example, 84 = MO & MT narrow-band GPS enabled, but gated by
                                                                                                        GPS_DISABLE setting.)
                                                                                                                            “GPSLPM”  —  Enable  /  disable GPS in Low Power Mode.
                                                                                                <value>:
                                                                                                                                   0 = Enable  —  GPS engine remains enabled when modem enters LPM
                                                                                                        (Default)
                                                                                                                                    1 = Disable  —  GPS engine is disabled when modem enters LPM
                                                                                                                            “GPSREFLOC”  —  Enable  /  disable reference GPS location reporting.
                                                                                                <value>:
                                                                                                                                   0 = Enable (Default)
                                                                                                                                   1 = Disable
                                                                                                                            “GPSSEL”  —  Select GPS antenna (useful only for devices with both a GPS and a
                                                                                                shared GPS  /  Rx diversity antenna).
                                                                                                <value>:
                                                                                                                                    0 = Use dedicated GPS antenna (Default)
                                                                                                                                   1 = Use shared GPS  /  Rx diversity antenna
                                                                                                                            “IPV6ENABLE”  —  Enable  /  disable IPV6 support.
                                                                                                <value>:
                                                                                                                                   0 = Disable IPV6
                                                                                                                                   1 = Enable IPV6 (Default)

                                                                                (Continued on next page)


26                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 27

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

  Command                                                                  Description

  !CUSTOM                                                                  Set   /   query customization settings (continued)
  (continued)
                                                                                                                     “NETWORKNAMEFMT” (Pending future upgrade)  —  Set MBIM provider nam e
                                                                                          format for vanui (roaming).
                                                                                          <value>:
                                                                                                                             0 = Display one of: SPN, LongName, or ShortName, by order of priority
                                                                                                 (Default QCT behavior)
                                                                                                                             1 = Display one of: LongName or ShortName
                                                                                                                             2 = Display [SPN] - [LongName/ShortName] (Note: May be truncated.)
                                                                                                                             3 = Display [LongName/ShortName] - [SPN] (Note: May be truncated.)
                                                                                                                      “PCSCDISABLE” (Pending future upgrade)  —  Determine functionality of PCSC,
                                                                                          GSM Algorithm and Authenticate commands, and +CIMI command.
                                                                                          <value>:
                                                                                                                              0–7 (Default value = 0  —  all functions enabled)
                                                                                                                                     Bit 0: PCSC (0  =  Enable, 1  =  Disable)
                                                                                                                                     Bit 1: GSM Algorithm and Authenticate commands (0  =  Enable, 1  =  Disable)
                                                                                                                                     Bit 2: AT+CIMI outputs IMSI (0  =  Enable, 1  =  Disable)
                                                                                                                      “QMIDETACHEN” (Pending future upgrade)  —  Enable  /  disable QMI NAS detach.
                                                                                          <value>:
                                                                                                                             0 = Disable  —  QMI detach request returns NO_EFFECT response, and no
                                                                                                 action is taken.
                                                                                                                             1 = Enable  —  QMI detach request is acted on, and appropriate response is
                                                                                                 returned based on the detach result.
                                                                                                                      “SIMHOTSWAPDIS”  —  Configure SIM hotswap feature
                                                                                          <value>:
                                                                                                                             0 = Enable UIM1 and UIM2 (default)
                                                                                                                             1 = Disable UIM1, enable UIM2
                                                                                                                             2 = Enable UIM1, disable UIM2
                                                                                                                             3 = Disable UIM1 and UIM2
                                                                                                                      “SIMLPM”  —  Indicate default SIM power state during Low Power Mode.
                                                                                          <value>:
                                                                                                                              0 = QCT default behavior (same as <value>=2) (Default)
                                                                                                 Note  —  The default behavior could change in future revisions. Use <value>=2
                                                                                                 if you need to guarantee the described behavior.
                                                                                                                             1 = SIM remains powered in LPM
                                                                                                                              2           =           Power down SIM with AT+CFUN=0; Power up SIM with AT+CFUN=1
                                                                                                                      “SINGLEAPNSWITCH” (Pending future upgrade)  —  Indicate device behavior
                                                                                          when changing APN name, username, or password.
                                                                                          <value>:
                                                                                                                             0 = Do nothing
                                                                                                                             1 = Device detaches and re-attaches after changing APN information
                                                                                                                             2 = Power-cycle the UE
                                                                                                                      “UIM2ENABLE”  —  Enable  /  disable UIM2 slot support.
                                                                                          <value>:
                                                                                                                             0 = Disable
                                                                                                                             1 = Enable (Default)

                                                                           (Continued on next page)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               27

## Page 28

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

  Command                                                                          Description

  !CUSTOM                                                                         Set   /   query customization settings (continued)
  (continued)
                                                                                                                               “USBSERIALENABLE”  —  Use IMEI as serial number in USB descriptor (USBD).
                                                                                                   <value>:
                                                                                                                                      0 = Same as 1 (Default)
                                                                                                                                      1 = Use IMEI as USB serial number
                                                                                                                                       2 = Set serial number as NULL in the USBD
                                                                                                                                       3 = Use hard-coded default (0123456789ABCDEF) in the USBD
                                                                                                                               “WAKEHOSTEN” (Pending future upgrade)  —  Enable  /  disable host wake-up via
                                                                                                   SMS or incoming data packet.
                                                                                                   <value>:
                                                                                                                                      0 = Disable  —  Host will not wake when SMS or incoming data packet is
                                                                                                           received. (Default)
                                                                                                                                      1 = Wake host when simple SMS is received.
                                                                                                                                      2 = Wake host when incoming data packet is received.
                                                                                                                                      3 = Wake host when simple SMS or incoming data packet is received.

  !DATALOOPBACK                                                                   Enable   /   disable and configure loopback mode

                                                                                  Enable or disable loopback mode and the loopback multiplier, or display the current
                                                                                  settings.

                                                                                  Password required: Yes
                                                                                  Reset required to apply changes:  Yes
                                                                                  Persistent across power cycles: Yes

                                                                                  Usage:
                                                                                                                      Query:                                                                                                          AT!DATALOOPBACK?
                                                                                          Response:                                               !DATALOOPBACK:
                                                                                                                            Data Loopback Mode; <loopback_mode>
                                                                                                                            Replication Count: <loopback_multiplier>
                                                                                                                            OK
                                                                                          Purpose:                                                                        Display the loopback mode state, and loopback multiplier.
                                                                                                                      Execution:                                                 AT!DATALOOPBACK=<loopback_mode>, <loopback_multiplier>
                                                                                          Response:                                               OK
                                                                                          Purpose:                                                                        Enable  /  disable loopback mode, and set the loopback multiplier.
                                                                                                                      Query list:                                                      AT!DATALOOPBACK  =  ?
                                                                                          Purpose:                                                                        Returns a list of valid parameter values.

                                                                                  Parameters:
                                                                                  <loopback_mode> (Loopback mode state)
                                                                                                                               0  =  Disable data loopback mode
                                                                                                                               1  =  Enable data loopback mode
                                                                                  <loopback_multiplier> (Number of downlink bytes sent for each uplink byte (replication
                                                                                                   count))
                                                                                                                               Decimal value
                                                                                                                               Maximum  =  6


28                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 29

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                                        Description

   !GCFEN                                                                                        Enable   /   disable GCF test mode

                                                                                                 Place the modem in GCF testing mode or normal operating mode.

                                                                                                 Password required: Yes  —  Execution format only

                                                                                                 Usage:
                                                                                                                                     Execution:                                                 AT!GCFEN  =   <enableFlag>
                                                                                                           Response:                                               OK
                                                                                                           Purpose:                                                                        Place the modem in GCF testing mode or normal operating mode.
                                                                                                                                     Query:                                                                                                         AT!GCFEN?
                                                                                                           Response:                                               !GCFEN:
                                                                                                                                                   <enableFlag>
                                                                                                                                                   OK
                                                                                                           Purpose:                                                                        Display the modem’s current mode.
                                                                                                                                     Query List:                                           AT!GCFEN  =  ?
                                                                                                           Purpose:                                                                        Return a list of supported <enableFlag> values.

                                                                                                 Parameters:
                                                                                                 <enableFlag> (Enable  /  disable GCF testing)
                                                                                                                                               0 = Disable GCF test mode (Default) — This value is used for normal operations.
                                                                                                                                               1 = Enable GCF test mode.
   !GETBAND                                                                                      Return the current active band

                                                                                                 Return the active band currently being used by the modem.

                                                                                                 Password required: No

                                                                                                 Usage:
                                                                                                                                     Query:                                                                                                         AT!GETBAND?
                                                                                                           Response:                                               !GETBAND: <active band description>
                                                                                                                                                   OK
                                                                                                                                  or                                                         Unknown
                                                                                                                                                   OK
                                                                                                                                  or                                                         No Service
                                                                                                                                                   OK
                                                                                                           Purpose:                                                                        Return a description of the current active band, or return an error
                                                                                                                                                   message.


                                                                                                 Note:                Due to stack implementation requirements, !GETBAND reports W800 for both
                                                                                                 W800 and W850.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               29

## Page 30

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

    Command                                                                                                                          Description

   !GSTATUS                                                                                                                         Return operational status

                                                                                                                                    Return specific details about the current operational status of the modem. The response
                                                                                                                                    details vary depending on the current RAT. Contact Sierra Wireless for further details if
                                                                                                                                    required.

                                                                                                                                    Password required: No

                                                                                                                                    Usage:
                                                                                                                                                                        Query:                                                                                                        AT!GSTATUS?
                                                                                                                                                 Response (Example shown is for LTE; fields will vary depending on RAT)
                                                                                                                                                                                                        !GSTATUS:
                                                                                                                                                                                                        Current Time:                                                                                                               <ctime>                                                                             Temperature:                                                 <temp>
                                                                                                                                                                                                        Reset Counter:                                                                                         <rcounter>                                   Mode:                                                                                                                                                      <mode>
                                                                                                                                                                                                        System mode:                                                                                                   <smode>                                                        PS state:                                                                                                            <PSstate>
                                                                                                                                                                                                        LTE band:                                                                                                                                                                 <lband>                                                                           LTE bw:                                                                                                                            <lbw>
                                                                                                                                                                                                        LTE Rx chan:                                                                                                                  <lrchan>                                                                  LTE Tx chan:                                                   <ltchan>
                                                                                                                                                                                                        LTE CA state:                                                                                                              <CAstate>                                       LTE Scell band: <SCband>
                                                                                                                                                                                                        LTE Scell bw:                                                                                                                <SCbw>                                                                    LTE Scell chan: <SCchan>
                                                                                                                                                                                                        EMM state:                                                                                                                                               <emmstate>            <emmdesc>
                                                                                                                                                                                                        RRC state:                                                                                                                                                     <rrcstate>
                                                                                                                                                                                                        IMS reg state:                                                                                                          <imsstate>

                                                                                                                                                                                                        PCC RxM RSSI:                                                                     <PRxMrssi>               RSRP (dBm): <PRxMrsrp>
                                                                                                                                                                                                        PCC RxD RSSI:                                                                         <PRxDrssi>                   RSRP (dBm): <PRxDrsrp>
                                                                                                                                                                                                        SCC RxM RSSI:                                                                     <SRxMrssi>               RSRP (dBm): <SRxMrsrp>
                                                                                                                                                                                                        SCC RxD RSSI:                                                                         <SRxDrssi>                   RSRP (dBm): <SRxDrsrp>
                                                                                                                                                                                                        Tx Power:                                                                                                                                                                 <TXpower>                        TAC: <tac>
                                                                                                                                                                                                        RSRQ (dB):                                                                                                                                      <rsrq>                                                                                                  Cell ID: <cellid>
                                                                                                                                                                                                        SINR (dB):                                                                                                                                                      <sinr>
   !HWID                                                                                                                            Display hardware version

                                                                                                                                    Display the device’s hardware version number.

                                                                                                                                    Password required: Yes

                                                                                                                                    Usage:
                                                                                                                                                                        Query:                                                                                                        AT!HWID?
                                                                                                                                                 Response:                                               Revision: <MajorVer>.<MinorVer>
                                                                                                                                                                                                        OK
                                                                                                                                                 Purpose:                                                                        Display hardware version number.
                                                                                                                                                                        Query List:                                           AT!HWID=  ?
                                                                                                                                                 Purpose:                                                                       Return the query command format.

                                                                                                                                    Parameters:
                                                                                                                                    <MajorVer> (Major versioning number)
                                                                                                                                                                                      0–9
                                                                                                                                    <MinorVer> (Minor versioning number)
                                                                                                                                                                                      0–9


30                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 31

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

  Command                                                                Description

  !IMPREF                                                                Query   /   set Image Management preferences
                                                                         Indicate which firmware image (firmware plus carrier configuration) should be selected
                                                                         from those available on the device, or enable SIM-based image switching. Use the
                                                                         query format to list the configuration pairs that are currently downloaded and preferred.

                                                                         Password required: No

                                                                         Usage:
                                                                                                             Execution:                                                 AT!IMPREF  =  <preference>
                                                                                Response:                                               OK
                                                                                Purpose:                                                                        Indicate which image should be used (the preferred image), or enable
                                                                                                              SIM-based image switching.
                                                                                                             Query:                                                                                                         AT!IMPREF?
                                                                                Response:!                                      !IMPREF:
                                                                                                              preferred fw version: <firmware-ver>
                                                                                                              preferred carrier name: <carrier-name>
                                                                                                              preferred config name: <carrier-config>
                                                                                                              preferred subpri index: <carrier-sub-config>
                                                                                                              current fw version: <firmware-ver>
                                                                                                              current carrier name: <carrier-name>
                                                                                                              current config name: <carrier-config>
                                                                                                              current subpri index: <carrier-sub-config>

                                                                                                              [<mismatch information>]
                                                                                                              OK
                                                                                                 or
                                                                                                              !IMPREF
                                                                                                              <invalid image>
                                                                                                              OK
                                                                                Purpose:                                                                        Query (show) the preferred and current images (firmware plus carrier
                                                                                                              configuration pairs), or if an image setting does not exist, a message
                                                                                                              will be displayed, as shown.
                                                                         Parameters:
                                                                         <preference> (The preferred carrier, or a flag to enable SIM-based image switching)
                                                                                                                    Valid values:
                                                                                                                           <carrier-name>  —  Module will search for a matching carrier PRI and the
                                                                                               firmware required for that PRI. If found, the new image preference is set.
                                                                                                                          “AUTO-SIM”  —  Enable SIM-based switching. (To disable SIM-based
                                                                                               switching, select a <carrier-name> instead.)
                                                                         <carrier-name> (Unique code identifying the carrier that the firmware was designed for)
                                                                                                                    ASCII string
                                                                         <firmware-ver> (Unique firmware version number assigned by Sierra Wireless)
                                                                                                                    ASCII string
                                                                         <carrier-config> (Unique code identifying the carrier and configuration details)
                                                                                                                    ASCII string
                                                                         <carrier-sub-config> (Sub-configuration for carrier PRI for custom ICCID/IMSI ranges)
                                                                                                                    ASCII string

                                                                         (Continued on next page)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               31

## Page 32

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                                           Description

  !IMPREF                                                                                            Query   /   set Image Management preferences (continued)
                                                                                                     <mismatch information> (Message indicating a field mismatch between the current and
                                                                                                                         preferred image settings)
                                                                                                                                                   ASCII string (quotation marks do not appear):
                                                                                                                                                            “fw version mismatch”
                                                                                                                                                            “carrier name mismatch”
                                                                                                                                                            “config name mismatch”
                                                                                                     <invalid image> (Message indicating an image does not exist)
                                                                                                                                                   ASCII string (quotation marks do not appear):
                                                                                                                                                            “preferred image setting does not exist”
                                                                                                                                                            “current image setting does not exist”
                                                                                                     Example(s):
                                                                                                                                        AT!IMPREF=”ABC”   (where “ABC” is a carrier name)
                                                                                                                                        AT!IMPREF=”AUTO-SIM”   (to enable SIM-based switching)


32                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 33

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                    Description

  !LTECA                                                                     Enable   /   disable LTE Carrier Aggregration or Display supported LTE
                                                                             CA pairs

                                                                             Enable or disable LTE Carrier Aggregation (CA), or (when enabled) display the list of
                                                                             LTE CA pairs supported by the hardware, the permitted combinations due to module
                                                                             band support, and depending on module provisioning, a “prune_ca” list of the actual set
                                                                             of allowed CA combinations (a subset of the combinations that the module supports).

                                                                             Password required: No

                                                                             Usage:
                                                                                                                 Execution:                                                AT!LTECA  =  <flag>
                                                                                     Response:                                               OK
                                                                                     Purpose:                                                                        Enable or disable LTE CA.
                                                                                                                 Query:                                                                                                        AT!LTECA?
                                                                                     Response:                                               Hardware:
                                                                                                                     <Bd><class>[_<Bd><class>[_<Bd><class>]]:<Bu><class>[,<Bu><cla
                                                                                                                     ss>[,<Bu><class>]]
                                                                                                                     ...
                                                                                                                     Permitted Bands:
                                                                                                                     <Bd><class>[_<Bd><class>[_<Bd><class>]]:<Bu><class>[,<Bu><cla
                                                                                                                     ss>[,<Bu><class>]]
                                                                                                                     ...

                                                                                                                     (If Prune_ca_combos does not exist)
                                                                                                                     Prune_ca_combos:
                                                                                                                     Empty

                                                                                                                     (If Prune_ca_combos exists)
                                                                                                                     Prune_ca_combos:
                                                                                                                     <Bd><class>[-<Bd><class>[-<Bd><class>]]-<bcs>

                                                                                                                     OK
                                                                                     Purpose:                                                                       Return LTE network measurements.
                                                                                                                 Query List:                                           AT!LTECA=  ?
                                                                                     Purpose:                                                                       Return the execution command format and valid parameter values.

                                                                             Parameters:
                                                                             <flag> (Enable  /  disable LTE CA)
                                                                                                                         0  —  Disable CA
                                                                                                                         1  —  Enable CA
                                                                             <Bd> (LTE downlink band)
                                                                                                                         Band numbers vary depending on device type, SKU, and PRI configuration. To
                                                                                             view the device’s supported bands, see !BAND.
                                                                             <Bu> (LTE uplink band)
                                                                                                                         Band numbers vary depending on device type, SKU, and PRI configuration. To
                                                                                             view the device’s supported bands, see !BAND.
                                                                             <class> (Aggregated transmission bandwidth configuration)
                                                                                                                         Valid values: ‘A’–’I’

                                                                             (Continued on next page)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               33

## Page 34

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

  Command                                                                           Description

  !LTECA (continued)                                                                Enable   /   disable LTE Carrier Aggregration or Display supported LTE
                                                                                    CA pairs (continued)

                                                                                    <bcs> (Bandwidth Combination Set)
                                                                                                                               Integer value,   0. See 3GPP specification for details.
                                                                                    Example(s):
                                                                                                                        Example where Prune_ca_combos does not exist:
                                                                                               AT   !   LT   E   C   A   ?
                                                                                               Hardware:
                                                                                               12A_30A_66A:12A,30A,66A
                                                                                               13A_66A_66A:13A,66A1
                                                                                               3A_66B:13A,66A
                                                                                               ...

                                                                                               Permitted Bands:
                                                                                               12A_30A_66A:12A,30A,66A
                                                                                               13A_66A_66A:13A,66A1
                                                                                               3A_66B:13A,66A
                                                                                               ...

                                                                                               Prune_ca_combos:
                                                                                               Empty

                                                                                               OK
                                                                                                                       Example where Prune_ca_combos exists:
                                                                                               AT   !   LT   E   C   A   ?
                                                                                               Hardware:
                                                                                               12A_30A_66A:12A,30A,66A
                                                                                               13A_66A_66A:13A,66A1
                                                                                               3A_66B:13A,66A
                                                                                               ...

                                                                                               Permitted Bands:
                                                                                               12A_30A_66A:12A,30A,66A
                                                                                               13A_66A_66A:13A,66A1
                                                                                               3A_66B:13A,66A
                                                                                               ...

                                                                                               Prune_ca_combos:
                                                                                               1A-8A-0
                                                                                               1A-8A-1
                                                                                               1A-8A-2
                                                                                               2C-0
                                                                                               2A-2A-0
                                                                                               ...

                                                                                               OK


34                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 35

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                       Description

  !LTEINFO                                                                       Display LTE network information

                                                                                 Display LTE network information.

                                                                                 Password required: No

                                                                                 Usage:
                                                                                                                     Query:                                                                                                         AT!LTEINFO?
                                                                                        Response:                                               !LTEINFO:
                                                                                                                          Serving: ...<list of applicable parameters>
                                                                                                                          IntraFreq: ...<list of applicable parameters>
                                                                                                                          InterFreq: ...<list of applicable parameters>
                                                                                                                          GSM: ...<list of applicable parameters>
                                                                                                                          WCDMA: ...<list of applicable parameters>
                                                                                                                          CDMA 1x: ...<list of applicable parameters>
                                                                                                                          CDMA HRPD: ...<list of applicable parameters>

                                                                                        Purpose:                                                                       Return LTE network measurements.

                                                                                 Parameters:
                                                                                 <earfcn> (E-UTRA absolute radio frequency channel number of the serving cell)
                                                                                                                             16-bit decimal
                                                                                 <mcc> (MCC code)
                                                                                                                             16-bit decimal
                                                                                 <mnc> (MNC code)
                                                                                                                             16-bit decimal
                                                                                 <tac> (Tracking area code)
                                                                                                                             16-bit decimal
                                                                                 <cid> (LTE Serving cell id)
                                                                                                                             16-bit hexadecimal
                                                                                 <bd> (Serving cell operating band)
                                                                                                                             8-bit decimal
                                                                                 <d> (Transmission bandwidth configuration of serving cell on the downlink)
                                                                                                                             8-bit decimal
                                                                                 <u> (Transmission bandwidth configuration of serving cell on the uplink)
                                                                                                                             8-bit decimal
                                                                                 <snr> (Average RSSNR of the serving cell over last measurement period in decibels)
                                                                                                                             8-bit decimal
                                                                                 <pci> (Physical cell ID)
                                                                                                                             16-bit decimal
                                                                                 <rsrq> (Current Reference Signal Receive Quality as measured by L1)
                                                                                                                             16-bit decimal
                                                                                 <rsrp> (Current Reference Signal Receive Power in dBm x10 as measured by L1)
                                                                                                                             16-bit decimal
                                                                                 <rssi> (Current Received Signal Strength Indication as measured by L1)
                                                                                                                             16-bit decimal

                                                                                 (Continued on next page)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               35

## Page 36

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

  Command                                                                           Description

  !LTEINFO (continued)                                                             Display LTE network information (continued)

                                                                                   <rxlv> (Cell selection Rx level (Srxlev) value)
                                                                                                                                16-bit decimal
                                                                                   <thresholdlow> (Cell Srxlev low threshold)
                                                                                                                                8-bit decimal
                                                                                   <thresholdhi> (Cell Srxlev high threshold)
                                                                                                                                8-bit decimal
                                                                                   <priority> (Cell reselection priority)
                                                                                                                                8-bit decimal
                                                                                   <threshl> (Reselection threshold for low priority layers)
                                                                                                                                8-bit decimal
                                                                                   <threshh> (Reselection threshold for high priority layers)
                                                                                                                                8-bit decimal
                                                                                   <prio> (Priority of this frequency group)
                                                                                                                                8-bit decimal
                                                                                   <ncc> (Bitmask identifying whether neighbor with a particular Network Color Code is to
                                                                                                    be reported)
                                                                                                                                8-bit decimal
                                                                                   <arfcn> (GSM frequency being reported)
                                                                                                                                16-bit decimal
                                                                                   <1900> (Band indicator for the GSM ARFCN, only valid if arfcn is in the overlapping
                                                                                                    region)
                                                                                                                                boolean
                                                                                   <valid> (Flag indicating whether the BSIC ID is valid)
                                                                                                                                boolean
                                                                                   <bsic> (BSIC ID)
                                                                                                                                8-bit decimal
                                                                                   <uarfcn> (WCDMA layer frequency)
                                                                                                                                16-bit decimal
                                                                                   <psc> (Scrambling code)
                                                                                                                                16-bit decimal
                                                                                   <rscp> (Absolute power level of the CPICH as received by the UE in dBm x10)
                                                                                                                                16-bit decimal
                                                                                   <ecn0> (Ratio of received energy per PN chip for the CPICH to the total received power
                                                                                                    spectral density at the UE antenna connector)
                                                                                                                                16-bit decimal
                                                                                   <chan> (Channel number)
                                                                                                                                16-bit decimal
                                                                                   <bc> (Band class)
                                                                                                                                16-bit decimal
                                                                                   <offsey> (The neighbor cell Pilot PN offset)
                                                                                                                                16-bit decimal
                                                                                   <phase> (The neighbor cell Pilot PN phase)
                                                                                                                                16-bit decimal
                                                                                   <str> (The neighbor cell Pilot EC/IO)
                                                                                                                                16-bit decimal

36                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 37

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                              Description

   !NVENCRYPTIMEI                                                                       Write unencrypted IMEI to modem

                                                                                        Write an unencrypted IMEI to a modem if the modem does not already have an IMEI  —
                                                                                        the command can only be used once per modem.
                                                                                        The IMEI is a fifteen digit string formed by concatenating the following elements:
                                                                                                    TAC code (8 digits)
                                                                                                    SN (Serial number) (6 digits)
                                                                                                    CheckDigit (1 digit calculated from TAC code and SN)
                                                                                        The CheckDigit is calculated as follows:
                                                                                        1.                                   Label the fourteen digits in the TAC and SN as:
                                                                                                          TAC:                          D14..D7
                                                                                                          SN:                                           D6..D1
                                                                                                    For example:
                                                                                                          TAC = 12345678 (‘1’ is D14, ‘8’ is D7)
                                                                                                          SN =                901234 (‘9’ is D6, ‘4’ is D1)
                                                                                        2.                                   Double the value of each odd-labeled digit (D13, D11, ..., D1).
                                                                                        3.                                   Add the values of each individual digit from the result of Step 2.
                                                                                        4.                                   Add the even-labeled digits (D14, D12, ..., D2) to the result of Step 3.
                                                                                        5.                                   Check the last digit of the result of Step 4. If it is ‘0’, the CheckDigit is 0; if it is not
                                                                                                    ‘0’, subtract it from 10 to get the CheckDigit.
                                                                                        For example:
                                                                                                    TAC (12345678)                                                                                                                        SN (901234)
                                                                                                    Step 1: Label the digits of the TAC and SN.
                                                                                                    D14D13D12D11D10D9D8D7D6D5D4D3D2D1

                                                                                                       12345678901234
                                                                                                    Step 2: Double the odd-labeled values:
                                                                                                    D14    D13                  D12                    D11                  D10                                D9                                D8                                D7                                D6                                D5                                D4                                D3                                D2                                D1

                                                                                                       1                                              4                                              3                                              8                                              5                            12                                              7                            16                                              9                                              0                                              1                                              4                                              3                                              8
                                                                                                    Step 3:             Add each digit of the odd-labeled values:
                                                                                                                         4 + 8 + (1 + 2) + (1 + 6) + 0 + 4 + 8 = 34
                                                                                                    Step 4:             Add each digit of the even-labeled values to the Step 3 total:
                                                                                                                         1 + 3 + 5 + 7 + 9 + 1 + 3 +34 = 63
                                                                                                    Step 5:             Check last digit of Step 4 total.
                                                                                                                         CheckDigit = 10 - 3 = 7
                                                                                                    Result:               IMEI                 = TAC:SN:CheckDigit
                                                                                                                                        = 123456789012347

                                                                                        Password required: Yes

                                                                                        Usage:
                                                                                                                            Execution:                                                 AT!NVENCRYPTIMEI  =  <P1>, <P2>, <P3>, <P4>, <P5>, <P6>, <P7>,
                                                                                                                                     <P8>
                                                                                                Response:                                               OK
                                                                                                Purpose:                                                                        Write the unencrypted IMEI to the modem.

                                                                                        (Continued on next page)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               37

## Page 38

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

  Command                                                                               Description

  !NVENCRYPTIMEI                                                                        Write unencrypted IMEI to modem (continued)
  (continued)
                                                                                        Parameters:
                                                                                        <P1> to <P8> (IMEI segments)
                                                                                                                                     <P1> = IMEI[0..1]; <P2> = IMEI[2..3]; ...; <P8> = IMEI[14..15]
                                                                                                                                     <P1> to <P4> represent the TAC
                                                                                                                                     <P5> to <P7> represent the SNR
                                                                                                                                     <P8> represents the CheckDigit plus a padding digit (‘0’)
                                                                                        Example(s):
                                                                                        Using the example IMEI shown above:
                                                                                                    AT!NVENCRYPTIMEI  =  12,34,56,78,90,12,34,70

  !NVPLMN                                                                               Provision   /   display PLMN list for Network Personalization locking

                                                                                        Provision or display the list of PLMN (MCC  /  MNC pairs) used for Network
                                                                                        Personalization locking.
                                                                                        Use the execution format to provision the list ONE TIME ONLY. After the list is
                                                                                        provisioned, it can only be displayed, not updated.

                                                                                        Password required: Yes

                                                                                        Usage:
                                                                                                                            Query:                                                                                                          AT!NVPLMN?
                                                                                                Response:                                               <MCC> <MNC>
                                                                                                                                     ...
                                                                                                                                     OK
                                                                                                Purpose:                                                                       Return a list of up to fifty NV items that can be read or written.
                                                                                                                            Execution:                                                 AT!NVPLMN  =  <MCC1>, <MNC1>, ..., <MCCn>, <MNCn>
                                                                                                Response:                                               OK
                                                                                                Purpose:                                                                        Add up to six MCC  /  MNC pairs to the PLMN list
                                                                                                Note:                                                                                                                             Execution can be performed one time only (all MCC  /  MNC pairs must
                                                                                                                                     be set at the same time).

                                                                                        Parameters:
                                                                                        <MCC> (Mobile Country Code)
                                                                                                                                     3 digits
                                                                                        <MNC> (Mobile Network Code)
                                                                                                                                     2 digits


38                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 39

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                                        Description

   !PCINFO                                                                                        Return power control status information

                                                                                                  Return the modem’s power control status information.

                                                                                                  Password required: No

                                                                                                  Usage:
                                                                                                                                      Query:                                                                                                         AT!PCINFO?
                                                                                                           Response:                                               State: <state>
                                                                                                                                                   LPM voters - Temp:<vote>, Volt:<vote>, User:<vote>, W_DISABLE:
                                                                                                                                                   <vote>, IMSWITCH:<vote>, BIOS:<vote>,
                                                                                                                                                   LWM2M:<vote>,OMADM:<vote>, FOTA:<vote>, RFCAL:<vote>

                                                                                                                                                   LPM persistence - <userlpm>

                                                                                                                                                   OK
                                                                                                           Purpose:                                                                        Return power control information.

                                                                                                  Parameters:
                                                                                                  <state> (The modem’s power mode)
                                                                                                                                                “Low Power Mode”
                                                                                                                                                “Online”
                                                                                                                                                “Offline”
                                                                                                  <vote> (LPM requested flag)
                                                                                                                                                0  —  LPM requested
                                                                                                                                                1  —  LPM not requested
                                                                                                  <userlpm> (Current state of user-initiated Low Power Mode)
                                                                                                                                                0  —  Host GUI has not requested LPM
                                                                                                                                                1  —  Host GUI has requested LPM


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               39

## Page 40

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                                    Description

  !PCOFFEN                                                                                    Set   /   return Power Off Enable state

                                                                                              The modem can be configured to enter low power mode or power off when W_DISABLE
                                                                                              is asserted. (This is called the Power Off Enable feature.)
                                                                                              Use this command to indicate or set the Power Off Enable feature state.

                                                                                              Password required: Yes

                                                                                              Usage:
                                                                                                                                  Execution:                                                 AT!PCOFFEN  =  <state>
                                                                                                       Response:                                               OK
                                                                                                       Purpose:                                                                       Set the current state.
                                                                                                                                  Query:                                                                                                        AT!PCOFFEN?
                                                                                                       Response:                                               <state>
                                                                                                                                              OK
                                                                                                       Purpose:                                                                        Report the current <state>.

                                                                                              Parameters:
                                                                                              <state> (Current state of Power Off Enable)
                                                                                                                                           0  —  Modem will enter LPM (low power mode) when W_DISABLE is asserted.
                                                                                                                                           1  —  Power off modem
                                                                                                                                           2  —  Ignore changes on W_DISABLE.
  !PCTEMP                                                                                     Return current temperature information

                                                                                              Return the module’s temperature state and actual temperature.

                                                                                              Password required: No

                                                                                              Usage:
                                                                                                                                  Query:                                                                                                          AT!PCTEMP?
                                                                                                       Response:                                              Temp state: <state>
                                                                                                                                              Temperature: <temperature> C
                                                                                                                                              OK
                                                                                                       Purpose:                                                                       Return the module’s temperature information.

                                                                                              Parameters:
                                                                                              <state> (Temperature state):
                                                                                                                                  Valid values:
                                                                                                                                           “Normal”
                                                                                                                                           “High Warning”
                                                                                                                                           “High Critical”
                                                                                                                                           “Low Critical”
                                                                                              <temperature> (Current temperature):
                                                                                                                                  Current temperature in degrees Celsius. This is the temperature reported by a
                                                                                                       thermistor positioned near the power amplifiers.
                                                                                                                                  Decimal ASCII


40                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 41

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                      Description

  !PCTEMPLIMITS                                                                 Set   /   report temperature state limit values

                                                                                Certain modem functionality is affected by the modem’s temperature state. The possible
                                                                                temperature states are high critical, high warning, high normal, low normal, and low
                                                                                critical.
                                                                                Use this command to report or set the limits that correspond to these temperature
                                                                                states.
                                                                                To display the current temperature and temperature state, see !PCTEMP on page         40.


                                                                                Note:                All temperatures are in Celsius.


                                                                                Password required: Yes

                                                                                Usage:
                                                                                                                    Execution:                                                 AT!PCTEMPLIMITS  =  <hc>,<hw>,<hn>,<ln>,<lc>
                                                                                       Response:                                               OK
                                                                                       Purpose:                                                                        Set the temperature limits for each state (all five values must be
                                                                                                                        specified).
                                                                                                                    Query:                                                                                                          AT!PCTEMPLIMITS?
                                                                                       Response:                                               HI CRIT: <hc>
                                                                                                                        HI WARN: <hw>
                                                                                                                        HI NORM: <hn>
                                                                                                                        LO NORM: <ln>
                                                                                                                        LO CRIT: <lc>
                                                                                       Purpose:                                                                        Return the temperature limits for each state.

                                                                                Parameters:
                                                                                <hc> (High Critical)
                                                                                                                            Temperature limit varies by device (see device Product Specification Document
                                                                                                or Product Technical Specification).
                                                                                                                            Default = 105°C
                                                                                <hw> (High Warning)
                                                                                                                            Temperature limit varies by device (see device Product Specification Document
                                                                                                or Product Technical Specification).
                                                                                                                            Default = 85°C
                                                                                <hn>(High Normal)
                                                                                                                            Temperature limit varies by device (see device Product Specification Document
                                                                                                or Product Technical Specification).
                                                                                                                            Default = 70°C
                                                                                <ln> (Low Normal)
                                                                                                                            Temperature limit varies by device (see device Product Specification Document
                                                                                                or Product Technical Specification).
                                                                                                                            Default = -30°C
                                                                                <lc> (Low Critical)
                                                                                                                            Temperature limit varies by device (see device Product Specification Document
                                                                                                or Product Technical Specification).
                                                                                                                            Default = -45°C


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               41

## Page 42

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                                                           Description

   !PCVOLT                                                                                                           Return current power supply voltage information

                                                                                                                     Return the module’s power control supply state and actual voltage.

                                                                                                                     Password required: No

                                                                                                                     Usage:
                                                                                                                                                         Query:                                                                                                         AT!PCVOLT?
                                                                                                                                Response:                                               Volt state: Normal
                                                                                                                                                                                Power supply voltage: <voltage> mV (<raw> cnt)
                                                                                                                                                                                OK
                                                                                                                                Purpose:                                                                        Return the module’s voltage information.

                                                                                                                     Parameters:
                                                                                                                     <state> (Power supply state):
                                                                                                                                                         Valid values:
                                                                                                                                                                     “Normal”
                                                                                                                                                                     “High Critical”
                                                                                                                                                                     “Low Warning”
                                                                                                                                                                     “Low Critical”
                                                                                                                     <voltage>:
                                                                                                                                                        Current voltage reading in mV.
                                                                                                                                                         Decimal ASCII

                                                                                                                     <raw>:
                                                                                                                                                         ADC (Analog  /  digital convertor) reading
                                                                                                                                                         Decimal ASCII


42                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 43

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                         Description

  !PCVOLTLIMITS                                                                    Set   /   report power supply voltage state limit values

                                                                                   Certain modem functionality is affected by the modem’s power supply voltage state. The
                                                                                   possible voltage states are high critical, high warning, high normal, low normal, and low
                                                                                   critical.
                                                                                   Use this command to report or set the limits that correspond to these voltage states.

                                                                                   Password required: Yes

                                                                                   Usage:
                                                                                                                       Execution:                                                 AT!PCVOLTLIMITS  =  <hc>,<hw>,<hn>,<ln>,<lc>
                                                                                           Response:                                               OK
                                                                                           Purpose:                                                                        Set the voltage limits for each state (all five values must be specified).
                                                                                                                       Query:                                                                                                         AT!PCVOLTLIMITS?
                                                                                           Response:                                               HI CRIT: <hc>
                                                                                                                             HI WARN: <hw>
                                                                                                                             HI NORM: <hn>
                                                                                                                             LO NORM: <ln>
                                                                                                                             LO CRIT: <lc>
                                                                                           Purpose:                                                                        Return the voltage limits for each state.

                                                                                   Parameters:
                                                                                   <hc> (High Critical)
                                                                                                                               Voltage limit varies by device (see device Product Specification Document or
                                                                                                    Product Technical Specification)
                                                                                                                               Default = 4600         mV
                                                                                   <hw> (High Warning)
                                                                                                                               Voltage limit varies by device (see device Product Specification Document or
                                                                                                    Product Technical Specification)
                                                                                                                               Default = 4400         mV
                                                                                   <hn> (High Normal)
                                                                                                                               Voltage limit varies by device (see device Product Specification Document or
                                                                                                    Product Technical Specification)
                                                                                                                               Default = 3300         mV
                                                                                   <ln> (Low Normal)
                                                                                                                               Voltage limit varies by device (see device Product Specification Document or
                                                                                                    Product Technical Specification)
                                                                                                                               Default = 3135         mV
                                                                                   <lc> (Low Critical)
                                                                                                                               Voltage limit varies by device (see device Product Specification Document or
                                                                                                    Product Technical Specification)
                                                                                                                               Default = 2900         mV


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               43

## Page 44

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                                      Description

  !PRIID                                                                                       Set   /   report module PRI part number and revision

                                                                                               Report or set the module’s customer and carrier PRI part numbers and revisions.

                                                                                               Password required: Yes  —  Execution format only

                                                                                               Usage:
                                                                                                                                   Execution:                                                 AT!PRIID  =  “<priPN>”,”<priRev>”,”<pri_cust>”
                                                                                                        Response:                                               OK
                                                                                                        Purpose:                                                                        Set the module’s PRI part number (<priPn>), revision (<priRev>), and
                                                                                                                                                customer name (<pri_cust>).
                                                                                                                                   Query:                                                                                                         AT!PRIID?
                                                                                                        Response:                                               PRI Part Number: <priPn>
                                                                                                                                                Revision: <priRevDisplay>
                                                                                                                                                Customer: <pri_cust>

                                                                                                                                                Carrier PRI: <bcVersion>
                                                                                                                                                OK
                                                                                                        Purpose:                                                                       Return the module’s PRI information.

                                                                                               Parameters:
                                                                                               <priPn> (PRI part number)
                                                                                                                                             7-digit ASCII number
                                                                                                                                             Example: 9991234
                                                                                               <priRev> (PRI revision number being written to the module)
                                                                                                                                             4-digit ASCII: XXYY (implied ‘.’ between XX and YY)
                                                                                                                                             Example: 0100
                                                                                               <priRevDisplay> (PRI revision number being read from the module)
                                                                                                                                             4-digit ASCII: XX.YY
                                                                                                                                             Example: 01.00
                                                                                               <pri_cust> (PRI customer name)
                                                                                                                                             ASCII string
                                                                                                                                            Example: “Generic Operator”
                                                                                               <bcVersion> (BC version)
                                                                                                                                             ASCII string

  !RESET                                                                                       Reset modem

                                                                                               Perform a modem reset.

                                                                                               Password required: No

                                                                                               Usage:
                                                                                                                                   Execution:                                                 AT!RESET
                                                                                                        Response:                                               OK
                                                                                                        Purpose:                                                                       Reset the modem.


44                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 45

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                                       Description

   !SCACT                                                                                        Activate   /   deactivate data connection

                                                                                                 Activate or deactivate a specific data connection between the host and network.


                                                                                                 Note:                This command does not work on Windows        8 or Windows        7 systems operating in
                                                                                                 MBIM mode. For those systems, use Microsoft APIs to initiate  /  end a data connection.


                                                                                                 Password required: No

                                                                                                 Usage:
                                                                                                                                     Query:                                                                                                        AT!SCACT?[<pid>]
                                                                                                          Response:                                               !SCACT: <pid>,<state>
                                                                                                                                                  ...       (additional <pid>/<state> combinations)
                                                                                                                                                  OK
                                                                                                          Purpose:                                                                        Display a list of all defined connections and their current state, or
                                                                                                                                                  display a specified connection and its state.
                                                                                                                                     Execution:                                                 AT!SCACT=<state>,<pid>
                                                                                                          Response:                                               OK
                                                                                                          Purpose:                                                                       Activate or deactivate a specific data connection.

                                                                                                 Parameters:
                                                                                                 <pid> (PDN connection ID)
                                                                                                                                              Valid values:
                                                                                                                                                        UMTS:
                                                                                                                                                                  1–16
                                                                                                                                                                  Default: 1 (all networks except Verizon), 3 (Verizon)
                                                                                                                                                       CDMA:
                                                                                                                                                                  101–107
                                                                                                                                                                  Default: 101 (all networks except Verizon), 3 (Verizon)
                                                                                                 <state> (Current state of specified <pid>)
                                                                                                                                              0  = Deactivated
                                                                                                                                              1  =  Activated
                                                                                                                                              Any other value causes command execution to return ERROR.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               45

## Page 46

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

     Command                                                                                                                                            Description

    !TMSTATUS                                                                                                                                           Report Thermal Mitigation Status

                                                                                                                                                        Report the thermal mitigation status of all available Thermal Mitigation Devices (TMD) in
                                                                                                                                                        the module.

                                                                                                                                                        Password required: No

                                                                                                                                                        Usage:
                                                                                                                                                                                            Query:                                                                                                        AT!TMSTATUS?
                                                                                                                                                                      Response:                                               Device                                                                                                                                                                                                                                                                                   Level
                                                                                                                                                                                                                                     pa                                                                                                                                                                                                                                                                                                                                                    <status>
                                                                                                                                                                                                                                     modem                                                                                                                                                                                                                                                                            <status>
                                                                                                                                                                                                                                     cpuv_restriction_cold                                                                      <status>
                                                                                                                                                                                                                                     modem_current                                                                                                                                                     <status>
                                                                                                                                                                                                                                     cpr_cold                                                                                                                                                                                                                                                            <status>
                                                                                                                                                                                                                                     vbatt_low                                                                                                                                                                                                                                              <status>

                                                                                                                                                                                                                                     OK
                                                                                                                                                                      Purpose:                                                                       Display the thermal mitigation status of the module’s TMDs.
                                                                                                                                                                                            Query List:                                           AT!TMSTATUS  =  ?
                                                                                                                                                                      Purpose:                                                                        Display valid execution format and parameter values.

                                                                                                                                                        Parameters:
                                                                                                                                                        <status> (Mitigation level)
                                                                                                                                                                                                           Valid range: 0–3
                                                                                                                                                                                                                         ‘modem’ mitigation levels:
                                                                                                                                                                                                                                          0  —  No mitigation
                                                                                                                                                                                                                                          1  —  DL data rate throttling
                                                                                                                                                                                                                                          3  —  No data calls
                                                                                                                                                                                                                          ‘pa’ mitigation levels:
                                                                                                                                                                                                                                          0  —  No mitigation
                                                                                                                                                                                                                                          1  —  UL data rate throttling
                                                                                                                                                                                                                                          2  —  UL rate throttling and Tx power limiting
                                                                                                                                                                                                                                          3  —  No data calls


46                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 47

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                       Description

  !USBCOMP                                                                       Set   /   report USB interface configuration

                                                                                 Set or display the device’s USB interface configuration.
                                                                                 By default, devices are typically configured to use a USB composition that presents a
                                                                                 minimal set of interfaces from a list of available interfaces. This command is used to add
                                                                                 or remove interfaces from the configuration.

                                                                                 Password required: Yes

                                                                                 Usage:
                                                                                                                     Execution:                                                 AT!USBCOMP   =  <Config         Index>,<Config        Type>,<Interface        bitmask>
                                                                                        Response:                                               OK
                                                                                        Purpose:                                                                        Set the current composition. For the change to take effect, you must
                                                                                                                          reset the modem.
                                                                                                                     Query:                                                                                                          AT!USBCOMP?
                                                                                        Response:                                               Config Index: <Config Index>
                                                                                                                          Config Type: <Config Type> <Config Type Desc>
                                                                                                                          Interface bitmask: <Interface bitmask> <Bitmask Desc>
                                                                                                                          OK
                                                                                        Purpose:                                                                        Report the current interface composition.
                                                                                                                     Query List:                                            AT!USBCOMP  =  ?
                                                                                        Purpose:                                                                        Display valid execution format and parameter values, and examples.

                                                                                 Parameters:
                                                                                 <Config Index> (USB composition)
                                                                                                                             Valid value: 1
                                                                                                                             Use AT!USBCOMP  =  ? to view the configurations available for the device.
                                                                                                 Available configurations are identified as “SUPPORTED”.
                                                                                 <Config Type> (USB composition)
                                                                                                                             1  —  Generic. This option is used for all customized VID  /  PID.
                                                                                                                             2  —  USBIF-MBIM. This option is used only for Sierra PIDs 68B1 and 9068.
                                                                                                                             3  —  RNDIS. This option is used only for Sierra PIDs 68B1 and 9068.
                                                                                 <Config Type Desc> (Configuration description)
                                                                                                                             “(Generic)”  —  Description of <Config Type> = 1.
                                                                                                                             “(USBIF-MBIM)”  —  Description of <Config Type> = 1.
                                                                                                                             “(RNDIS)”  —  Description of <Config Type> = 1.
                                                                                 <Interface bitmask> (USB composition)
                                                                                                                             Bitmask representing all enabled interfaces
                                                                                                                             Format: 32-bit bitmask
                                                                                                                             Valid values (available interfaces are device-dependent):
                                                                                                                                     0x00000001  —  DIAG
                                                                                                                                     0x00000004  —  NMEA
                                                                                                                                     0x00000008  —  MODEM
                                                                                                                                     0x00000100  —  RMNET0
                                                                                                                                     0x00000400  —  RMNET1
                                                                                                                                     0x00001000  —  MBIM
                                                                                                                                     0x00010000  —  AUDIO
                                                                                 <Bitmask Desc> (Interface bitmask description)
                                                                                                                             List of interface descriptions corresponding to <Interface bitmask> components
                                                                                                                             Example: “(diag, nmea, modem, mbim)”

Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               47

## Page 48

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                                          Description

  !USBINFO                                                                                          Return information from active USB descriptor

                                                                                                    Return information from the active USB descriptor.

                                                                                                    Password required: No

                                                                                                    Usage:
                                                                                                                                        Query:                                                                                                        AT!USBINFO?
                                                                                                              Response:                                               VID:                                                                                                                         <vendor_id>
                                                                                                                                                       APP PID:                                                         <app_product_id>
                                                                                                                                                       BOOT PID:                              <boot_product_id>
                                                                                                                                                       Manufacturer: <product_manufacturer>
                                                                                                                                                       Product: <product_name>
                                                                                                              Purpose:                                                                        Display USB descriptor information.

                                                                                                    Parameters:
                                                                                                    <vendor_id> (Vendor ID):
                                                                                                                                        Valid range: 0000–FFFF
                                                                                                    <app_product_id> (Product ID used when modem is in application mode):
                                                                                                                                        Valid range: 0000–FFFF
                                                                                                    <boot_product_id> (Product ID used when modem is in boot loader mode):
                                                                                                                                        Valid range: 0000–FFFF

                                                                                                    <product_manufacturer> (Manufacturer string):
                                                                                                                                       ASCII string (32 characters maximum)
                                                                                                                                       Example: “Sierra Wireless, Incorporated”
                                                                                                    <product_name> (Product string):
                                                                                                                                       ASCII string (64 characters maximum)
                                                                                                                                       Example: “EM7565”


48                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 49

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                                 Description

   !USBPID                                                                                 Set   /   report product ID in USB descriptor

                                                                                           Use this command to set the device’s product ID in the USB descriptor. (Some devices
                                                                                           may support more than one product ID.)


                                                                                           Note:                If a custom PID is used for <app product_id>, then the <boot product_id> must
                                                                                           be set at the same time.


                                                                                           Password required: Yes

                                                                                           Usage:
                                                                                                                               Execution:                                                 AT!USBPID  =  <app product_id> [,<boot product_id>]
                                                                                                    Response:                                               OK
                                                                                                    Purpose:                                                                        Set the application and boot product IDs in the USB descriptor.
                                                                                                                               Query:                                                                                                         AT!USBPID?
                                                                                                    Response:                                               !USBPID:
                                                                                                                                         <app product_id>, <boot product_id>
                                                                                                                                         OK
                                                                                                    Purpose:                                                                        Report the product ID that is stored in the USB descriptor.
                                                                                                                               Query List:                                            AT!USBPID  =  ?
                                                                                                    Purpose:                                                                        Display a list of default (non-custom) product IDs for the device.

                                                                                           Parameters:
                                                                                           <app product_id>
                                                                                                                                        Hexadecimal ASCII value.
                                                                                                                                        Valid range: 0000–FFFF
                                                                                           <boot product_id>
                                                                                                                                        Hexadecimal ASCII value.
                                                                                                                                        Valid range: 0000–FFFF
                                                                                                                                        In the Execution command format, if the <app product_id> is a custom PID>,
                                                                                                             then the <boot product_id> must be set at the same time. (To check if the
                                                                                                             <app         product_id> is a custom PID, use AT!USBPID=? to see a list of all available
                                                                                                             non-custom PIDs.)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               49

## Page 50

AirPrime EM75xx AT Command Reference


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

   Command                                                                                              Description

   !USBSPEED                                                                                            Set   /   report USB speed

                                                                                                        Use this command to set the device’s maximum supported USB speed, and to display
                                                                                                        the maximum supported speed and current speed.

                                                                                                        Password required: Yes
                                                                                                        Reset required to apply changes:  Yes
                                                                                                        Persistent across power cycles: Yes

                                                                                                        Usage:
                                                                                                                                            Execution:                                                 AT!USBSPEED  =   <max_supported_speed>
                                                                                                                  Response:                                               OK
                                                                                                                  Purpose:                                                                        Set the device’s maximum supported USB speed.
                                                                                                                                            Query:                                                                                                          AT!USBSPEED?
                                                                                                                  Response:                                               SUPPORTED: <max_supported_speed>
                                                                                                                                                             CURRENT                                              : <current_usb_speed>
                                                                                                                                                             OK
                                                                                                                  Purpose:                                                                        Report the device’s maximum and current speeds.
                                                                                                                                            Query List:                                            AT!USBSPEED=  ?
                                                                                                                  Purpose:                                                                        Display valid execution format and parameter values.

                                                                                                        Parameters:
                                                                                                        <max_supported_speed>
                                                                                                                                                      0  —  High Speed (USB        2.0)
                                                                                                                                                      1  —  Super Speed (USB         3.0)
                                                                                                        <current_usb_speed>
                                                                                                                                                      ASCII string (quotation marks not included)
                                                                                                                                                      Valid values:
                                                                                                                                                                 “Super-Speed”
                                                                                                                                                                 “High-Speed”


50                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 51

Modem Status, Customization, and Reset Commands


Table 3-2:                Modem status, custom                                        ization, and reset commands (Cont                                               inued)

  Command                                                          Description

  &V                                                               Return operating mode AT configuration parameters

                                                                   Return the status of all AT command parameters that apply to the current operating
                                                                   mode.

                                                                   Password required: No

                                                                   Usage:
                                                                                                       Execution:                                                AT&V
                                                                         Response:                                               &C: 2; &D: 2; &F: 0; E: 1; L: 0; M: 0; Q: 0; V: 1; X: 0; Z: 0; S0: 0; S2:
                                                                                                    43;
                                                                                                    S3: 13; S4: 10; S5: 8; S6: 2; S7: 50; S8: 2; S9: 6; S10: 14; S11: 95;
                                                                                                    +FCLASS: 0; +ICF: 3,3; +IFC: 2,2; +IPR: 115200; +DR: 0; +DS:
                                                                                                    0,0,2048,6;+WS46: 12; +CBST: 0,0,1;+CRLP:
                                                                                                    (61,61,48,6,0),(61,61,48,6,1),(240,240,52,6,2);+CV120: 1,1,1,0,0,0;
                                                                                                    +CHSN: 0,0,0,0; +CSSN: 0,0; +CREG: 0; +CGREG: 0;+CFUN:;
                                                                                                    +CSCS: "IRA"; +CSTA: 129; +CR: 0; +CRC: 0; +CMEE: 2;
                                                                                                    +CGDCONT: (1,"IP","","",0,0); +CGDSCONT: ; +CGTFT: ;
                                                                                                    +CGEQREQ: ; +CGEQMIN: ; +CGQREQ: ; +CGQMIN: ;+CGEREP:
                                                                                                    0,0; +CGDATA: "PPP"; +CGCLASS: "A"; +CGSMS: 3; +CSMS:
                                                                                                    0;+CMGF: 0; +CSCA: "",; +CSMP: ,,0,0; +CSDH: 0; +CSCB: 0,"","";
                                                                                                    +FDD: 0;+FAR: 0; +FCL: 0; +FIT: 0,0; +ES: ,,; +ESA: 0,,,,0,0,255,;
                                                                                                    +CMOD: 0;+CVHU: 0; +CPIN: ,; +CMEC: 0,0,0; +CKPD: 1,1; +CGATT:
                                                                                                    0; +CGACT: 0;+CPBS: "SM"; +CPMS: "SM","SM","SM"; +CNMI:
                                                                                                    0,0,0,0,0; +CMMS: 0; +FTS: 0;+FRS: 0; +FTH: 3; +FRH: 3; +FTM: 96;
                                                                                                    +FRM: 96; +CCUG: 0,0,0;+COPS: 0,0,"";  +CUSD: 0; +CAOC: 1;
                                                                                                    +CCWA: 0; +CPOL: 0,2,""; +CTZR: 0;+CLIP: 0; +COLP: 0; +CMUX:
                                                                                                    0,0,5,31,10,3,30,10,2;!CMUX: 0,0,5,31,10,3,30,10,2
                                                                                                    OK
                                                                                                    Note: this is an example only. The supported commands may
                                                                                                    vary by device/SKU.
                                                                         Purpose:                                                                        Display command parameters.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               51

## Page 52

4: Diagnostic Commands                                                                                                                                                                                                                                                                  4

                                                                                                                Introduction

                                                                                                                This chapter describes commands used to diagnose modem
                                                                                                                problems.
                                                                                                                Command summary

                                                                                                                The table below lists the commands described in this chapter.

                                 Table 4-1:                Diagnostic commands

                                    Command                                                                              Description                                                                                                                                                      Page

                                       !BCFWUPDATESTATUS                                                                 Report status of most recent firmware update attempt                                                                                                             53

                                       !ERR                                                                              Display diagnostic information                                                                                                                                   54

                                       !GCCLR                                                                            Clear crash dump data                                                                                                                                            54

                                       !GCDUMP                                                                           Display crash dump data                                                                                                                                          55

                                       !LTERXCONTROL                                                                     Enable  /  disable LTE receive (Rx) diversity during Carrier                                                                                                     56
                                                                                                                         Aggregation

                                       !RXDEN                                                                            Enable  /  disable WCDMA/  LTE  /  TD-SCDMA receive (Rx)                                                                                                         57
                                                                                                                         diversity


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      52

## Page 53

Diagnostic Commands

                                                                             Command reference

Table 4-2:                Diagnostic command details

   Command                                                                         Description

  !BCFWUPDATESTATUS                                                                Report status of most recent firmware update attempt

                                                                                   Return the status of the most recent firmware update attempt made since the last cold
                                                                                   restart.

                                                                                   Password required: No

                                                                                   Usage:
                                                                                                                       Execution:                                                 AT!BCFWUPDATESTATUS
                                                                                          Response:                                               !BCFWUPDATESTATUS: <result>
                                                                                                              or
                                                                                                                             !BCFWUPDATESTATUS: <result>
                                                                                                                            Failed IMG TYPE <type>, DATA <data>, PART <part>
                                                                                                                            OK
                                                                                          Purpose:                                                                        Return the status of the most recent firmware update attempt. The
                                                                                                                            second response format appears only if <result> = “FAILED”.

                                                                                   Parameters:
                                                                                   <result> (Status of last firmware update attempt)
                                                                                                                               ASCII string:
                                                                                                                                      “UNKNOWN”  —  Status of last attempt is unknown.
                                                                                                                                      “SUCCESS”   —  Last update was successful.
                                                                                                                                      “FAILED”  —  Last update failed.
                                                                                   <type> (Firmware image type that failed to update)
                                                                                                                               ASCII string
                                                                                                                               Note: Parameter appears only if <result> is FAILED
                                                                                   <data> (Reference data for failed image)
                                                                                                                               Location of the reference data as an offset in the CWE image
                                                                                                                               Valid range: 0–(232-1)
                                                                                                                               Note: Parameter appears only if <result> is FAILED
                                                                                   <part> (Partition associated with the failed image)
                                                                                                                               ASCII string
                                                                                                                               Applies only to configuration updates
                                                                                                                               Note: Parameter appears only if <result> is FAILED


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               53

## Page 54

AirPrime EM75xx AT Command Reference


Table 4-2:                Diagnostic command details (Continued)

   Command                                                                                       Description

  !ERR                                                                                          Display diagnostic information

                                                                                                This command is used to display diagnostic information (logged error conditions) that
                                                                                                Sierra Wireless uses to assist in resolving technical issues.

                                                                                                Password required: No

                                                                                                Usage:
                                                                                                                                    Execution:                                                AT!ERR  =  0
                                                                                                         Response:                                               OK
                                                                                                         Purpose:                                                                        Clear the logged error conditions. Use this command before running
                                                                                                                                                 tests to make sure that details displayed using AT!ERR are relevant to
                                                                                                                                                 the tests being performed.
                                                                                                                                    Query:                                                                                                        AT!ERR
                                                                                                         Response:                                               00 [F] <count> <file> <line>
                                                                                                                                                 ...
                                                                                                                                                 nn [F] <count> <file> <line>
                                                                                                                                                 OK
                                                                                                         Purpose:                                                                        Return all logged error conditions that are stored in NVRAM.

                                                                                                Parameters:
                                                                                                <count> (Number of occurrences)
                                                                                                                                              Valid range: 0x00–0xFF
                                                                                                <file> (Log file name)
                                                                                                                                              Name of log file using ASCII characters
                                                                                                <line> (Line number in log file)
                                                                                                                                              Valid range: 1–99999

  !GCCLR                                                                                        Clear crash dump data

                                                                                                Clear crash dump data.

                                                                                                Password required: No

                                                                                                Usage:
                                                                                                                                    Execution:                                                 AT!GCCLR
                                                                                                         Response:                                               Crash data cleared
                                                                                                                                                 OK
                                                                                                         Purpose:                                                                        Clear crash dump data.

                                                                                                Parameters:
                                                                                                None


54                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 55

Diagnostic Commands


Table 4-2:                Diagnostic command details (Continued)

     Command                                                                                                                                     Description

     !GCDUMP                                                                                                                                    Display crash dump data

                                                                                                                                                Display crash dump data.

                                                                                                                                                Password required: No

                                                                                                                                                Usage:
                                                                                                                                                                                    Execution:                                                 AT!GCDUMP
                                                                                                                                                              Response:                                               <crash dump data>
                                                                                                                                                                                                                         OK
                                                                                                                                                                                                or                                                         No crash data available
                                                                                                                                                                                                                         OK
                                                                                                                                                              Purpose:                                                                        Display crash dump data.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               55

## Page 56

AirPrime EM75xx AT Command Reference


Table 4-2:                Diagnostic command details (Continued)

  Command                                                                           Description

  LTERXCONTROL                                                                      Enable   /   disable LTE receive (Rx) diversity during Carrier
                                                                                    Aggregation

                                                                                    Enable or disable LTE receive diversity for individual component carriers (PCC or SCC)
                                                                                    during Carrier Aggregation (CA). The new state takes effect immediately, and reverts to
                                                                                    the default state when the modem is reset.


                                                                                    Note:               !LTERXCONTROL should be issued during an active CA call.


                                                                                    Note:                  When using !LTERXCONTROL to disable any chain, make sure !RXDEN is set to
                                                                                    enable all chains.


                                                                                    Note:               Due to firmware design, LTE open-loop Tx power should be set to 20        dBm when
                                                                                    measuring SISO sensitivity with Rx diversity as the primary path.


                                                                                    Password required: Yes
                                                                                    Reset required to apply changes:  No
                                                                                    Persistent across power cycles: No

                                                                                    Usage:
                                                                                                                        Execution:                                                 AT!LTERXCONTROL  =  <cc_id>,<selection>
                                                                                            Response:                                               OK
                                                                                            Purpose:                                                                        Configure the component carrier as primary Rx, diversity Rx, or both.
                                                                                                                        Query List:                                            AT!LTERXCONTROL=  ?
                                                                                            Purpose:                                                                        Returns the command format and valid parameter values.

                                                                                    Parameters:
                                                                                    <cc_id> (Component carrier ID)
                                                                                                                                0  —  PCC (Primary cell)
                                                                                                                                1  —  SCC1 (Secondary cell)
                                                                                                                                2  —  SCC2 (Secondary cell)
                                                                                                                                3  —  SCC3 (Secondary cell)
                                                                                    <selection> (Rx chain to enable)
                                                                                                                                1  —  Primary Rx only
                                                                                                                                2  —  Diversity Rx only
                                                                                                                                3  —  Primary Rx and Diversity Rx


56                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 57

Diagnostic Commands


Table 4-2:                Diagnostic command details (Continued)

   Command                                                                          Description

  !RXDEN                                                                            Enable   /   disable WCDMA /   LTE   /   TD-SCDMA receive (Rx) diversity

                                                                                    Enable or disable WCDMA/  LTE  /  TD-SCDMA receive diversity, or establish receive
                                                                                    diversity as the primary path. The new state takes effect the next time the modem is
                                                                                    reset.


                                                                                    Note:                During LTE Carrier Aggregation (CA), this command works only on the Primary
                                                                                    Component Carrier (PCC). To control Secondary Component Carrier (SCC) chains, use
                                                                                    !LTERXCONTROL..
                                                                                    If !LTERXCONTROL is used to also control the PCC, !RXDEN must not be used.


                                                                                    Note:                  When using !LTERXCONTROL to disable any chain, make sure !RXDEN is set to
                                                                                    enable all chains.


                                                                                    Note:                Due to firmware design, LTE open-loop Tx power should be set to 20        dBm when
                                                                                    measuring SISO sensitivity with Rx diversity as the primary path.


                                                                                    Password required: Yes  —  Execution format only

                                                                                    Usage:
                                                                                                                        Execution:                                                 AT!RXDEN  =  <state>
                                                                                            Response:                                               OK
                                                                                            Purpose:                                                                        Set the current receive diversity state.
                                                                                                                        Query:                                                                                                          AT!RXDEN?
                                                                                            Response:                                               !RXDEN:
                                                                                                                              <state>
                                                                                                                              OK
                                                                                            Purpose:                                                                       Return the current receive diversity <state>.
                                                                                                                        Query List:                                           AT!RXDEN  =  ?
                                                                                            Purpose:                                                                       Return a list of available <state> values to use in this command.

                                                                                    Parameters:
                                                                                    <state> (Current  /   requested receive diversity state)
                                                                                                                                0 = Rx diversity disabled
                                                                                                                                1 = Rx diversity enabled
                                                                                                                                2 = Rx diversity is primary path
                                                                                                     (See note above for measuring SISO sensitivity.)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               57

## Page 58

5: Test Commands                                                                                                                                                                                                                                                                                                                                                                                 5

                                                                                                                                                          Introduction


                                                                                                                                                          Note:                Full test commands support is pending future firmware upgrade.

                                                                                                                                                          To obtain regulatory approval and carrier approvals for your product,
                                                                                                                                                          you may be required to perform tests on the radio component of the
                                                                                                                                                          embedded modem. This chapter describes AT commands used to
                                                                                                                                                          perform those tests.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      58

## Page 59

Test Commands

                                                                  Command summary

                                                                  The table below lists the commands described in this chapter.

                           Table 5-1:                Test commands

                               Command                                                                 Description                                                                                                                              Page

                                  !DACGPSCTON                                                          Return GPS CtoN and frequency measurement                                                                                                60

                                  !DACGPSMASKON                                                        Set CGPS IQ log mask                                                                                                                     60

                                  !DACGPSSTANDALONE                                                    Enter  /  exit StandAlone (SA) RF mode                                                                                                   61

                                  !DACGPSTESTMODE                                                      Start  /  stop CGPS diagnostic task                                                                                                      61

                                  !DAFTMACT                                                            Put modem into Factory Test Mode                                                                                                         62

                                  !DAFTMDEACT                                                          Put modem into online mode from Factory Test Mode                                                                                        62

                                  !DAGFTMRXAGC                                                         Get FTM Rx AGC (Primary or Diversity)                                                                                                    63

                                  !DALGRXAGC                                                           Return Rx AGC value (LTE only)                                                                                                           64

                                  !DALGTXAGC                                                           Return Tx AGC value and transmitter parameters (LTE only)                                                                                65

                                  !DALTXCONTROL                                                        Configure LTE Tx parameters (LTE only)                                                                                                   67

                                  !DAOFFLINE                                                           Place modem offline                                                                                                                      68

                                  !DARCONFIG                                                           Set Band and Channel                                                                                                                     69

                                  !DARCONFIGDROP                                                       Drop Radio Configurations                                                                                                                70

                                  !DAWTXCONTROL                                                        Configure WCDMA Tx Power (WCDMA only)                                                                                                    70


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               59

## Page 60

AirPrime EM75xx AT Command Reference

                                                                          Command reference

Table 5-2:                Test command details

  Command                                                                      Description

  !DACGPSCTON                                                                  Return GPS CtoN and frequency measurement

                                                                               Return the GPS CtoN and frequency measurement.

                                                                               Requirements:
                                                                               1.                                  AT!DACGPSTESTMODE=1  (to start CGPS diagnostic task)
                                                                               2.                                   AT!DACGPSSTANDALONE=1  (to enter SA RF mode)
                                                                               3.                                   AT!DACGPSMASKON  (to enable log mask)

                                                                               Password required: Yes

                                                                               Usage:
                                                                                                                   Execution:                                                 AT!DACGPSCTON
                                                                                       Response:                                              CtoN=<CtoN>, Freq=<freq>
                                                                                                                       OK
                                                                                       Purpose:                                                                        Return CtoN and frequency measurements.

                                                                               Parameters:
                                                                               <CtoN> (Signal strength calculated in dBHz as part of WBIQ test)
                                                                                                                           Uint32
                                                                               <freq> (Frequency in Hz calculated as part of WBIQ test)
                                                                                                                           Int32
  !DACGPSMASKON                                                                Set CGPS IQ log mask

                                                                               Set CGPS IQ (0x138a) log mask.

                                                                               Password required: Yes

                                                                               Usage:
                                                                                                                   Execution:                                                 AT!DACGPSMASKON
                                                                                       Response:                                               73000000030000000000000001000000DC03
                                                                                                                       000000000000000000000000000000000000
                                                                                                                       000000000000000000000000000000000000
                                                                                                                       000000000000000000000000000000000000
                                                                                                                       000000000000000000000000000000000000
                                                                                                                       000000000000000000000000000000000000
                                                                                                                       000000000000000000000000000000000000
                                                                                                                       000000000000000400000000000000000000

                                                                                                                       OK
                                                                                       Purpose:                                                                        Set log mask.


60                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 61

Test Commands


Table 5-2:                Test command details (Continued)

   Command                                                                                           Description

   !DACGPSSTANDALONE                                                                                Enter   /   exit StandAlone (SA) RF mode

                                                                                                    Enter  /  exit SA RF mode.

                                                                                                    Requirements:
                                                                                                    1.                                   AT!DACGPSTESTMODE=1  (to start CGPS diagnostic task)

                                                                                                    Password required: Yes

                                                                                                    Usage:
                                                                                                                                        Query:                                                                                                          AT!DACGPSSTANDALONE=<state>
                                                                                                              Response:                                               4B0D65001400
                                                                                                                                                       OK
                                                                                                                                      or
                                                                                                                                                       Error
                                                                                                              Purpose:                                                                        Enter  /  exit SA RF mode

                                                                                                    Parameters:
                                                                                                    <state> (SA RF mode state)
                                                                                                                                                 0  = Enter SA RF mode
                                                                                                                                                  1  =  Exit SA RF mode

   !DACGPSTESTMODE                                                                                  Start   /   stop CGPS diagnostic task

                                                                                                    Start  /stop the CGPS diagnostic task.

                                                                                                    Password required: Yes

                                                                                                    Usage:
                                                                                                                                        Execution:                                                 AT!DACGPSTESTMODE=<mode>
                                                                                                              Response:                                              (for start):
                                                                                                                                                       4B0D0800
                                                                                                                                                       OK

                                                                                                                                                       (for stop):
                                                                                                                                                       4B0D0C00
                                                                                                                                                       OK
                                                                                                                                      or
                                                                                                                                                       Error
                                                                                                              Purpose:                                                                        Start or stop the CGPS diagnostic test.

                                                                                                    Parameters:
                                                                                                    <mode> (CGPS diagnostic task mode)
                                                                                                                                          0= Stop
                                                                                                                                          1=Start


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               61

## Page 62

AirPrime EM75xx AT Command Reference


Table 5-2:                Test command details (Continued)

  Command                                                                            Description

  !DAFTMACT                                                                          Put modem into Factory Test Mode

                                                                                     Place the modem in FTM (Factory Test Mode). FTM is a non-signaling mode that allows
                                                                                     the radio component to be manually configured to conduct certain types of tests.


                                                                                     Note:                When this command executes successfully, the modem responds with the value
                                                                                     290300. Any other response indicates an error.


                                                                                     Password required: Yes

                                                                                     Usage:
                                                                                                                         Query:                                                                                                         AT!DAFTMACT
                                                                                             Response:                                               290300 (Success. Any other response indicates an error.)
                                                                                                                                OK
                                                                                             Purpose:                                                                        Place modem in FTM mode.

  !DAFTMDEACT                                                                        Put modem into online mode from Factory Test Mode

                                                                                     This command takes the modem out of FTM and puts the modem back into online
                                                                                     mode. (The command !DAFTMACT puts the modem into FTM.)


                                                                                     Note:                When this command executes successfully, the modem responds with the value
                                                                                     290400. Any other response indicates an error.


                                                                                     Password required: Yes

                                                                                     Usage:
                                                                                                                         Query:                                                                                                          AT!DAFTMDEACT
                                                                                             Response:                                               290400 (Success. Any other response indicates an error.)
                                                                                                                                OK
                                                                                             Purpose:                                                                        Place modem in online mode (from FTM mode).


62                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 63

Test Commands


Table 5-2:                Test command details (Continued)

   Command                                                                                           Description

   !DAGFTMRXAGC                                                                                      Get FTM Rx AGC (Primary or Diversity)

                                                                                                     Get the FTM Rx AGC on the primary or diversity path.

                                                                                                     Requirements:
                                                                                                     Before using this command:
                                                                                                                                                    !DAFTMACT must be issued to put the modem into FTM.
                                                                                                                                                     !DARCONFIG must be issued to set the technology, band, and channel.

                                                                                                     Password required: Yes

                                                                                                     Usage:
                                                                                                                                         Execution:                                                 AT!DAGFTMRXAGC  =  <carrier>, <technology>, <LNA         Index>, <path>
                                                                                                               Response:                                               <rssi>
                                                                                                                                                        OK
                                                                                                               Purpose:                                                                       Return the FTM Rx AGC value.

                                                                                                     Parameters:
                                                                                                     <carrier> (Carrier ID)
                                                                                                                                           0—PCC
                                                                                                     <technology> (Radio access technology (RAT))
                                                                                                                                                   RAT support is device-dependent.
                                                                                                                                                   0  —  CDMA
                                                                                                                                                   1  —  WCDMA
                                                                                                                                           2—GSM
                                                                                                                                           3—LTE
                                                                                                     <LNA Index> (LNA offset index)
                                                                                                                                                   0  =  R0 (Highest gain)
                                                                                                                                           1=R1
                                                                                                                                           2=R2
                                                                                                                                                   3  =  R3 (Lowest gain)
                                                                                                     <path> (Rx path)
                                                                                                                                           0—Primary Rx
                                                                                                                                                   1  —  Diversity Rx
                                                                                                     <rssi> (RSSI, in dBm)
                                                                                                                                                   Dynamic Rx AGC


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               63

## Page 64

AirPrime EM75xx AT Command Reference


Table 5-2:                Test command details (Continued)

  Command                                                                               Description

  !DALGRXAGC                                                                            Return Rx AGC value (LTE only)

                                                                                        Return the Rx AGC (Automatic Gain Control) value and LNA gain states for each RF
                                                                                        path (e.g. main and diversity).
                                                                                        The AGC value can be converted to RSSI (Received Signal Strength Indicator) in dBm:
                                                                                                          if (<AGC_value> < 511)
                                                                                                                       <RX_dBm> = -106 + (  ( <AGC_value> + 512 ) / 12 )
                                                                                                          else
                                                                                                                       <RX_dBm> = -106 + (  ( (<AGC_value>-1024) + 512 ) / 12 )

                                                                                        Requirements:
                                                                                                                                    The modem must be in LTE mode.

                                                                                        Password required: Yes

                                                                                        Usage:
                                                                                                                            Execution:                                                 AT!DALGRXAGC or AT!DALGRXAGC?
                                                                                                Response:                                               Paths: <paths>
                                                                                                                                    Rx<n>:  AGC: <agc> dBm  LNA: <lna>  Chain:  <chain>
                                                                                                                                    Rx<n>:  AGC: <agc> dBm  LNA: <lna>  Chain:  <chain>
                                                                                                                                    OK
                                                                                                Purpose:                                                                        Return the <AGC         value> and LNA gain states for each RF path.

                                                                                        Parameters:
                                                                                        <paths> (Number of receive paths)
                                                                                                                                    2
                                                                                        <agc> (AGC value in dBm)
                                                                                                                                    Valid values: Dynamic Rx range
                                                                                        <LNA Index> (LNA offset index)
                                                                                                                                    0  =  R0 (Highest gain)
                                                                                                                            1=R1
                                                                                                                            2=R2
                                                                                                                                    3  =  R3 (Lowest gain)
                                                                                        <chain> (Receive paths)
                                                                                                                            0=Rx Main
                                                                                                                                    1  =  Rx Diversity


64                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 65

Test Commands


Table 5-2:                Test command details (Continued)

   Command                                                                       Description

  !DALGTXAGC                                                                     Return Tx AGC value and transmitter parameters (LTE only)

                                                                                 Return the Tx AGC (Automatic Gain Control) value and other transmitter parameters.

                                                                                 Requirements:
                                                                                                                             The modem must be in LTE mode.
                                                                                                                             Must be in an active call (for example, when connected to a call box or live
                                                                                                 network)

                                                                                 Password required: Yes

                                                                                 Usage:
                                                                                                                     Execution:                                                 AT!DALGTXAGC or AT!DALGTXAGC?
                                                                                        Response:                                               Paths: <paths>
                                                                                                                         Tx<n>:AGC: <agc> dBm  RBi: <rbi>  RB: <rbn>  PA: <pa>
                                                                                                                                  TxGainIdx: <txgi> MTPL: <mtpl> dBm  IQgain: <iq>
                                                                                                                                  MPR: <mpr>  AMPR: <ampr>  NS: <ns>
                                                                                                                                  SARmpr: <sarmpr>  PDet Mode: <mode>
                                                                                                                                  PDetAGC: <pagc>  PDet: <pdbm>  Traw: <traw>
                                                                                                                                  Tscaled: <tscaled>  Tidx: <tidx>  Trem: <trem>
                                                                                                                         OK
                                                                                        Purpose:                                                                        Return transmitter parameters and the transmit <AGC value>.

                                                                                 Parameters:
                                                                                 <paths> (Number of transmit paths)
                                                                                                                             1 (Tx)
                                                                                 <agc> (Tx AGC value in dBm)
                                                                                                                             Valid range: -70 to +23
                                                                                 <rbi>
                                                                                                                             Start resource block index
                                                                                 <rbn> (Number of resource blocks)
                                                                                                                             Valid range: 0–50
                                                                                 <pa> (PA gain state)
                                                                                                                             Valid range: 0–3
                                                                                 <txgi>
                                                                                                                             Tx gain index
                                                                                 <mtpl> (Max Tx power limit)
                                                                                                                             Max value: +23
                                                                                 <iq>
                                                                                                                             Digital IQ gain scaling
                                                                                 <mpr> (Maximum power reduction)
                                                                                                                             See 3GPP 36.101 for details
                                                                                 <ampr> (Additional Max power reduction)
                                                                                                                             See 3GPP 36.101 for details
                                                                                 <ns> (Network Signaled (NS) value)
                                                                                                                             See 3GPP 36.101 for details

                                                                                 (Continued on next page)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               65

## Page 66

AirPrime EM75xx AT Command Reference


Table 5-2:                Test command details (Continued)

   Command                                                                                               Description

   !DALGTXAGC                                                                                           Return Tx AGC value and transmitter parameters (LTE only)
   (continued)                                                                                          (continued)

                                                                                                        <mode> (HDET (power detector) mode)
                                                                                                                                                       Valid values:
                                                                                                                                                                L (Lower power)
                                                                                                                                                                H (Higher power)
                                                                                                        <padc>
                                                                                                                                                       HDET ADC
                                                                                                        <pdbm>
                                                                                                                                                       HDET dBm
                                                                                                        <traw> (Raw thermistor ADC value)
                                                                                                                                                       Valid range: 0–4095
                                                                                                        <tscaled> (Scaled thermistor value)
                                                                                                                                                       Valid range: 0–255
                                                                                                                                                       Value is scaled from <traw> based on calibrated min  /  max <traw> values for the
                                                                                                                             supported temperature range.
                                                                                                        <tidx> (Temperature compensation bin)
                                                                                                                                                       Valid range: 0–7
                                                                                                        <trem>
                                                                                                                                                       Temperature compensation remainder bin


66                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 67

Test Commands


Table 5-2:                Test command details (Continued)

   Command                                                                          Description

  !DALTXCONTROL                                                                     Configure LTE Tx parameters (LTE only)

                                                                                    Configure LTE Tx parameters, including Tx power, waveform, modulation, net signal
                                                                                    values, number of resource blocks, and start resource block.

                                                                                    Requirements:
                                                                                    Before using this command:
                                                                                                                                 !DAFTMACT must be issued to put the modem into FTM.
                                                                                                                                  !DARCONFIG must be issued to set the technology, band, and channel.

                                                                                    Password required: Yes

                                                                                    Usage:
                                                                                                                        Execution:                                                 AT!DALTXCONTROL=<carrier><enable>,[<tx_pwr>,<waveform>,
                                                                                                                               <mod>,<ns_val>,<num_RB>,<start_RB>]
                                                                                            Response:                                               OK
                                                                                            Purpose:                                                                        Set the LTE Tx parameters.

                                                                                    Parameters:
                                                                                    <carrier> (Carrier ID)
                                                                                                                        0—PCC
                                                                                    <enable> (Enable  /  disable Tx power output)
                                                                                                                                0  —  Disable
                                                                                                                                1  —  Enable
                                                                                    <tx_pwr> (Desired Tx power in dBm)
                                                                                                                                Valid range: -57 to 23
                                                                                                                                Value is ignored if <enable>  =  0.
                                                                                    <waveform> (Tx waveform)
                                                                                                                                0  =  1         MHz offset CW (Continuous Wave)
                                                                                                                                1  =  LTE PUSCH (Physical Uplink Shared Channel)
                                                                                                                                2  =  LTE PUCCH (Physical Uplink Control Channel)
                                                                                                     Note: LTE PUCCH is not supported by EM75xx modules.
                                                                                                                                3  =  LTE PRACH (Physical Random Access Channel)
                                                                                                                                4  =  LTE SRS (Sounding Reference Signal)
                                                                                                                                5  =  UpPTS (Uplink Pilot Time Slot)
                                                                                    <mod> (Tx modulation)
                                                                                                                                0  —  QPSK
                                                                                                                        1—16     QAM
                                                                                                                        2—64     QAM
                                                                                    <ns_val> (LTE NS (Net Sig))
                                                                                                                                Valid range: 1–32
                                                                                                                                This value affects maximum output power.
                                                                                    <num_RB> (Number of resource blocks
                                                                                                                                Valid range: 0–100
                                                                                    <start_RB> (PUSCH starting resource block index)
                                                                                                                                Valid range: 0–255


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               67

## Page 68

AirPrime EM75xx AT Command Reference


Table 5-2:                Test command details (Continued)

     Command                                                                                                                                           Description

    !DAOFFLINE                                                                                                                                        Place modem offline

                                                                                                                                                      Put the modem offline.

                                                                                                                                                      Password required: Yes

                                                                                                                                                      Usage:
                                                                                                                                                                                          Execution:                                                AT!DAOFFLINE
                                                                                                                                                                     Response:                                               OK
                                                                                                                                                                     Purpose:                                                                        Put the modem offline.

                                                                                                                                                      Parameters:
                                                                                                                                                                     None


68                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 69

Test Commands


Table 5-2:                Test command details (Continued)

   Command                                                                                           Description

   !DARCONFIG                                                                                       Set Band and Channel

                                                                                                    Configure and tune the module’s radio to a specific RAT, band, and channel.

                                                                                                    Requirements:
                                                                                                    Before using this command:
                                                                                                                                                   !DAFTMACT must be issued to put the modem into FTM.

                                                                                                    Password required: Yes

                                                                                                    Usage:
                                                                                                                                        Execution:                                                 AT!DARCONFIG=<carrier>,<technology>,<band>,<channel>[,
                                                                                                                                                        <lte_bw>]
                                                                                                              Response:                                               OK
                                                                                                              Purpose:                                                                        Set the selected RAT’s band and channel (and bandwidth, for LTE).

                                                                                                    Parameters:
                                                                                                    <carrier> (Carrier ID)
                                                                                                                                          0—PCC
                                                                                                    <technology> (Radio access technology (RAT))
                                                                                                                                                  RAT support is device-dependent
                                                                                                                                                  0  —  CDMA
                                                                                                                                                  1  —  WCDMA
                                                                                                                                          2—GSM
                                                                                                                                          3—LTE
                                                                                                    <band> (Band number)
                                                                                                                                                  Valid range: 1–66
                                                                                                                                                  e.g. ‘1’ is LTE         B1 or WCDMA       B1
                                                                                                    <channel> (Uplink channel number for selected <band>)
                                                                                                                                                  Integer value
                                                                                                                                                  <band>-dependent
                                                                                                    <lte_bw> (LTE bandwidth)
                                                                                                                                          0—1.4     MHz
                                                                                                                                          1—3    MHz
                                                                                                                                          2—5    MHz
                                                                                                                                          3—10     MHz
                                                                                                                                          4—15     MHz
                                                                                                                                          5—20     MHz


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               69

## Page 70

AirPrime EM75xx AT Command Reference


Table 5-2:                Test command details (Continued)

  Command                                                                             Description

  !DARCONFIGDROP                                                                      Drop Radio Configurations

                                                                                      Drop the radio configurations that were previously set using !DARCONFIG.
                                                                                      This command must be used when switching between technologies (RATs).

                                                                                      Requirements:
                                                                                      Before using this command:
                                                                                                                                   !DAFTMACT must be issued to put the modem into FTM.

                                                                                      Password required: Yes

                                                                                      Usage:
                                                                                                                          Execution:                                                 AT!DARCONFIGDROP=<technology>
                                                                                              Response:                                               OK
                                                                                              Purpose:                                                                       Drop the current configurations for the selected RAT (<technology>).

                                                                                      Parameters:
                                                                                      <technology> (Radio access technology (RAT))
                                                                                                                                  RAT support is device-dependent
                                                                                                                                  0  —  CDMA
                                                                                                                                  1  —  WCDMA
                                                                                                                          2—GSM
                                                                                                                          3—LTE

  !DAWTXCONTROL                                                                       Configure WCDMA Tx Power (WCDMA only)

                                                                                      Configure the Tx power for WDCMA.

                                                                                      Requirements:
                                                                                      Before using this command:
                                                                                                                                  The modem must be in WCDMA mode.
                                                                                                                                   !DAFTMACT must be issued to put the modem into FTM.
                                                                                                                                    !DARCONFIG must be issued to set the technology, band, and channel.

                                                                                      Password required: Yes

                                                                                      Usage:
                                                                                                                          Execution:                                                 AT!DAWTXCONTROL=<enable>,<power_dBm>
                                                                                              Response:                                               OK
                                                                                              Purpose:                                                                       Set the WCDMA Tx parameters.

                                                                                      Parameters:
                                                                                      <enable> (Enable  /  disable Tx power output)
                                                                                                                                  0  —  Disable
                                                                                                                                  1  —  Enable
                                                                                      <power_dBm> (Desired Tx power in dBm)
                                                                                                                                  Valid range: -57 to 23
                                                                                                                                  Value is ignored if <enable>  =  0.


70                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 71

6: Memory Management Commands                                                                                                                                                                                                                                                                                 6

                                                                                                                         Introduction

                                                                                                                         The modem uses non-volatile memory to store:
                                                                                                                                                                            Factory calibration data
                                                                                                                                                                             Settings made in a host application such as Skylight.
                                                                                                                         The commands in this chapter allow you to back up and restore the
                                                                                                                         data in non-volatile memory.
                                                                                                                         Command summary

                                                                                                                         The table below lists the commands described in this chapter:

                                    Table 6-1:                Memory management commands

                                       Command                                                                                     Description                                                                                                                                                                   Page

                                           !NVBACKUP                                                                               Back up device configuration                                                                                                                                                  72

                                           !RMARESET                                                                               Restore device to saved restore point                                                                                                                                         74


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      71

## Page 72

AirPrime EM75xx AT Command Reference

                                                                               Command reference

Table 6-2:                Memory management command details

  Command                                                                Description

  !NVBACKUP                                                              Back up device configuration

                                                                         Save the device’s current configuration as a ‘restore point’. The restore point can then be
                                                                         restored at a later time if necessary, using !RMARESET on page         74.

                                                                         Password required: No

                                                                         Usage:
                                                                                                             Execution:                                                 AT!NVBACKUP  =  <restore point>[,<name>]
                                                                                 Response:                                               !NVBACKUP:
                                                                                                                    Items Saved:                                                                                        <saved>
                                                                                                                    Items Skipped:                                                               <skipped>
                                                                                                                    OK
                                                                                 Purpose:                                                                        Save the current device configuration to the indicated <restore         point>.
                                                                                                                    Note: The restore point replaces the existing same-numbered restore point
                                                                                                                    (if present), and deletes higher-numbered restore points.
                                                                                                             Query:                                                                                                          AT!NVBACKUP?
                                                                                 Response:                                               !NVBACKUP:
                                                                                                                    <restore        point> <name>
                                                                                                                    ...
                                                                                                                    OK
                                                                                 Purpose:                                                                        Display all available restore points.

                                                                         Usage notes:
                                                                                                             When saving a restore point:
                                                                                                                     The existing <restore         point> is replaced (if present).
                                                                                                                     Higher-numbered restore points are deleted.
                                                                                                             If a <name> is not specified, the file is saved as “unnamed” or “Latest”, depending on the
                                                                                 <restore        point>.

                                                                         Parameters:
                                                                         <restore         point> (Type of saved restore point)
                                                                                                                     Valid range: 0–3
                                                                                                                     0  =  Factory-calibrated configuration (Cannot be replaced)
                                                                                                                     1  =  Sierra-provided SKU configuration (Cannot be replaced)
                                                                                                                     2  =  Save the current configuration using a specified file <name>. If no <name> is
                                                                                          specified, save as “unnamed”.
                                                                                                                     3  =  Save the current configuration as the ‘Latest’ restore point.
                                                                                          Note: The category 3 restore point is also generated automatically after a successful
                                                                                          reconfiguration (e.g. after an image switch or firmware update).

                                                                         (Continued on next page)


72                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 73

Memory Management Commands


Table 6-2:                Memory management command details (Continued)

   Command                                                                            Description

   !NVBACKUP                                                                          Back up device configuration (continued)

                                                                                      <name> (Name used to store the restore point)
                                                                                                                                    0–32 ASCII characters
                                                                                                                                              <restore point> = 0  —  “Factory” (Factory-calibrated configuration, pre-SKU)
                                                                                                                                             <restore point> = 1  —  “Provision” (Sierra-provisioned SKU configuration)
                                                                                                                                              <restore point> = 2  —  User-defined name provided when restore         point was saved,
                                                                                                                    or “unnamed” if no name was provided
                                                                                                                                             <restore point> = 3  —  “Latest” (Latest saved configuration)
                                                                                      <saved> (Number of saved items)
                                                                                                                                    0–(232 - 1)
                                                                                      <skipped> (Number of skipped items)
                                                                                                                                    0–(232 - 1)
                                                                                                                                    Note: Does not display if 0


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               73

## Page 74

AirPrime EM75xx AT Command Reference


Table 6-2:                Memory management command details (Continued)

  Command                                                              Description

  !RMARESET                                                            Restore device to saved restore point

                                                                       Restore the device to a previously saved restore point.
                                                                       (To save a restore point, see !NVBACKUP on page         72.)

                                                                       Password required: Yes
                                                                       Reset required to apply changes:  Yes

                                                                       Usage:
                                                                                                           Execution:                                                 AT!RMARESET  =  <restore         point>
                                                                               Response:                                               !RMARESET: DEVICE REBOOT REQUIRED
                                                                                                                 Items Restored:                                                 <restored count>
                                                                                                                 Items Deleted:                                                                     <deleted count>
                                                                                                                 Items Defaulted:                                          <defaulted count>
                                                                                                                 Items Skipped:                                                               <skipped count>
                                                                                                                 OK
                                                                               Purpose:                                                                        Restore device to the specified <restore         point> (configuration). A reboot is
                                                                                                                 required to take effect.
                                                                                                           Query:                                                                                                          AT!RMARESET?
                                                                               Response:                                               !RMARESET:
                                                                                                                 <restore         point> <name>
                                                                                                                 ...
                                                                                                                 OK
                                                                               Purpose:                                                                        Display all available restore points.

                                                                       Parameters:
                                                                       <restore_point> (Saved restore point)
                                                                                                                   0  =  Factory-calibrated configuration (Note: For information only, cannot be restored.)
                                                                                                                   1  =  Sierra-provided SKU configuration
                                                                                                                   2  =  Restore to the restore point that was saved earlier using !NVBACKUP on page         72.
                                                                                                                   3  =  Restore to the latest saved restore point (saved earlier using !NVBACKUP or
                                                                                       automatically when the device was successfully reconfigured, e.g. after an image
                                                                                       switch or firmware update)
                                                                       <name> (Descriptive name of <restore_point>)
                                                                                                                   ASCII string, varies by <restore point>:
                                                                                                                           <restore point> = 0  —  “Factory” (Factory-calibrated configuration, pre-SKU)
                                                                                                                          <restore point> = 1  —  “Provision” (Sierra-provisioned SKU configuration)
                                                                                                                          <restore point> = 2  —  User-defined name provided when using !NVBACKUP to
                                                                                                save a configuration, or “unnamed” if no name was provided
                                                                                                                          <restore point> = 3  —  User-defined name provided when using !NVBACKUP to
                                                                                                save a configuration, or “Latest” (Latest saved configuration)


74                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 75

7: GNSS Commands                                                                                                                                                                                                    7

                                                                                    Introduction

                                                                                    This chapter describes commands used to access GNSS
                                                                                    functionality in supporting modules.
                                                                                    When using these commands, the following considerations apply:
                                                                                                                                                   GNSS is typically enabled by default; however, it may be disabled
                                                                                             by default for some SKUs. If so, enable GNSS using
                                                                                             AT!CUSTOM  =  ”GPSENABLE”
                                                                                                                                        If supported by the modem, gpsOneXTRA is enabled (over the
                                                                                             NDIS interface) by default when GNSS is enabled, and it
                                                                                             generates data traffic.
                                                                                    Command summary

                                                                                    The table below lists the commands described in this chapter.

                         Table 7-1:                GNSS commands

                           Command                                                             Description                                                                                                           Page

                              !GPSAUTOSTART                                                    Configure GPS auto-start features                                                                                     76

                              !GPSCLRASSIST                                                    Clear specific GPS assistance data                                                                                    78

                              !GPSCOLDSTART                                                    Clear all GNSS assistance data                                                                                        79

                              !GPSEND                                                          End an active session                                                                                                 79

                              !GPSFIX                                                          Initiate GPS position fix                                                                                             80

                              !GPSLBSAPN                                                       Set GPS LBS APNs                                                                                                      81

                              !GPSLOC                                                          Return last known location of the modem                                                                               83

                              !GPSMOMETHOD                                                     Set  /  report GPS MO method                                                                                          84

                              !GPSNMEACONFIG                                                   Enable and set NMEA data output rate                                                                                  85

                              !GPSNMEASENTENCE                                                 Set  /  report NMEA sentence type                                                                                     86

                              !GPSPORTID                                                       Set  /  report port ID to use over TCP/IP                                                                             87

                              !GPSSATINFO                                                      Request satellite information                                                                                         88

                              !GPSSTATUS                                                       Request current status of a position fix session                                                                      89

                              !GPSSUPLURL                                                      Set  /  report SUPL server URL                                                                                        90

                              !GPSSUPLVER                                                      Set  /  report SUPL server version                                                                                    91


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      75

## Page 76

AirPrime EM75xx AT Command Reference


Table 7-1:                GNSS commands (Continued)

   Command                                                                                           Description                                                                                                                                                                  Page

      !GPSTRACK                                                                                      Initiate local tracking (multiple fix) session                                                                                                                               92

      +WANT                                                                                          Enable  /  disable GNSS antenna power                                                                                                                                        93

                                                                                      Command reference

Table 7-2:                GNSS command details

   Command                                                                                          Description

   !GPSAUTOSTART                                                                                   Configure GPS auto-start features

                                                                                                   Configure the GPS auto-start features. Any changes take effect the next time the
                                                                                                   modem is reset.


                                                                                                   Note:                If auto-start is enabled, another GPS session cannot be started.


                                                                                                   Password required: No

                                                                                                   Usage:
                                                                                                                                       Execution:                                                 AT!GPSAUTOSTART  =  <function>[, <fixtype>, <maxtime>,
                                                                                                                                                   <maxdist>, <fixrate>]
                                                                                                            Response:                                               OK
                                                                                                                                  or                                                         ERROR
                                                                                                            Purpose:                                                                        Assign start values for various GPS settings
                                                                                                                                       Query:                                                                                                          AT!GPSAUTOSTART?
                                                                                                            Response:                                               !GPSAUTOSTART
                                                                                                                                                    function: <function>
                                                                                                                                                    fixtype:  <fixtype>
                                                                                                                                                    maxtime: <maxtime> seconds
                                                                                                                                                    maxdist: <maxdist> meters
                                                                                                                                                    fixrate: <fixrate> seconds
                                                                                                                                                  OK
                                                                                                            Purpose:                                                                         Display the current values for auto-start features
                                                                                                                                       Query List:                                            AT!GPSAUTOSTART  =  ?
                                                                                                            Purpose:                                                                        Return the expected command format.

                                                                                                   (Continued on next page)


76                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 77

GNSS Commands


Table 7-2:                GNSS command details (Continued)

    Command                                                                                                   Description

    !GPSAUTOSTART                                                                                             Configure GPS auto-start features (continued)

                                                                                                              Parameters:
                                                                                                              <function> (Enable  /  disable the feature)
                                                                                                                                                            0  =  Disabled
                                                                                                                                                            1  =  Enabled at boot (GPS tracking session starts automatically when modem
                                                                                                                                  is reset)
                                                                                                                                                            2  =  Enabled when NMEA port is opened
                                                                                                              <fixtype> (Type of fix to establish)
                                                                                                                                                            1  =  Standalone (not supported by a mobile station)
                                                                                                                                                            2  =  MS-based only
                                                                                                                                                           3  =  MS-assisted only
                                                                                                              <maxtime> (Maximum time to wait for a position fix)
                                                                                                                                                            Valid range: 0–255  —  Number of seconds to wait
                                                                                                              <maxdist> (Requested accuracy of fix)
                                                                                                                                                            Entered in decimal format
                                                                                                                                                            Valid range:
                                                                                                                                                                      0–4294967279 meters
                                                                                                                                                                      4294967280  =  No preference
                                                                                                              <fixrate> (Time to wait between fixes)
                                                                                                                                                            Valid range: 1–65535           seconds


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               77

## Page 78

AirPrime EM75xx AT Command Reference


Table 7-2:                GNSS command details (Continued)

   Command                                                                               Description

  !GPSCLRASSIST                                                                         Clear specific GPS assistance data

                                                                                        Clear one or more types of assistance data from the modem. This forces a cold start
                                                                                        for GPS acquisition the next time a session starts.
                                                                                        The command is only available when there is no active GPS session  —  the GPS
                                                                                        receiver is off and no position fix is being calculated.
                                                                                        This command is equivalent to !GPSCOLDSTART when all parameters (except
                                                                                        <alm>) are set to ‘1’.

                                                                                        Password required: Yes

                                                                                        Usage:
                                                                                                                            Execution:                                                 AT!GPSCLRASSIST   =  <eph>, <alm>, <pos>, <time>, <iono>
                                                                                                Response:                                               OK
                                                                                                                    or                                                         Command ignored
                                                                                                                                  OK
                                                                                                Purpose:                                                                        Clear each assistance data type that is flagged as ‘1’.
                                                                                                                            Query List:                                            AT!GPSCLRASSIST   =  ?
                                                                                                Purpose:                                                                        Return the expected command format and supported values.

                                                                                        Parameters:
                                                                                        <eph> (Ephemeris assistance data)
                                                                                                                                     0  =  Ignore (Do not clear the ephemeris assistance data)
                                                                                                                                     1  =  Clear this assistance data type  —  Clears GPS, GLONASS, and SBAS
                                                                                                         ephemeris assistance data.
                                                                                        <alm> (Almanac assistance data)
                                                                                                                                     0  =  Ignore (Do not clear the almanac assistance data)
                                                                                                                                     1  =  Clear this assistance data type  —  Clears GPS, GLONASS, and SBAS
                                                                                                         almanac assistance data.
                                                                                        <pos> (Position assistance data)
                                                                                                                                     0  =  Ignore (Do not clear the position assistance data)
                                                                                                                                     1  =  Clear this assistance data type
                                                                                        <time> (Time reference)
                                                                                                                                     0  =  Ignore (Do not clear the time reference)
                                                                                                                                     1  =  Clear the time reference
                                                                                        <iono> (Ionosphere assistance data)
                                                                                                                                     0  =  Ignore (Do not clear the ionosphere assistance data)
                                                                                                                                     1  =  Clear this assistance data type


78                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 79

GNSS Commands


Table 7-2:                GNSS command details (Continued)

   Command                                                                                   Description

   !GPSCOLDSTART                                                                             Clear all GNSS assistance data

                                                                                             Clear GNSS assistance details from the modem and put the modem into a coldstart
                                                                                             state. Data cleared includes Ephemeris, Previous Position, Ionosphere, and GPS
                                                                                             time  —  almanac data is not cleared. This forces a cold start for GPS acquisition the
                                                                                             next time a session starts.
                                                                                             The command is only available when there is no active GPS session  —  the GPS
                                                                                             receiver is off and no position fix is being calculated.

                                                                                             Requirements:
                                                                                                                                         Before using this command, end all active GNSS sessions using
                                                                                                              AT!GPSEND=0,255

                                                                                             Password required: Yes

                                                                                             Usage:
                                                                                                                                 Execution:                                                 AT!GPSCOLDSTART
                                                                                                     Response:                                               OK
                                                                                                     Purpose:                                                                         Clear the modem’s GPS details

                                                                                             Parameters:
                                                                                                     None

   !GPSEND                                                                                   End an active session

                                                                                             End an active position fix session.

                                                                                             Password required: No

                                                                                             Usage:
                                                                                                                                 Execution:                                                 AT!GPSEND  =   <sessType>[, <sessionID>]
                                                                                                     Response:                                               ERRCODE = <value>
                                                                                                                                        OK
                                                                                                                         or                                                         OK
                                                                                                     Purpose:                                                                        End the current session.

                                                                                             Parameters:
                                                                                             <sessType> (Type of session to end)
                                                                                                                                         0  =  Position fix session
                                                                                             <sessionID> (ID of the session to end)
                                                                                                                                         255  =  End all sessions
                                                                                                                                         0–254  =  Reserved
                                                                                             <value> (Error code returned when command fails for any reason)
                                                                                                                                        See Ta     b      l     e                    7     -     3 on page         93 for a list of possible error codes.
                                                                                                                                         N/A=  Not available


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               79

## Page 80

AirPrime EM75xx AT Command Reference


Table 7-2:                GNSS command details (Continued)

   Command                                                                                        Description

   !GPSFIX                                                                                       Initiate GPS position fix

                                                                                                 Initiate a GPS position fix.

                                                                                                 Password required: No

                                                                                                 Usage:
                                                                                                                                     Execution:                                                 AT!GPSFIX  =  <fixType>, <maxTime>, <maxDist>
                                                                                                          Response:                                               Fix initiated
                                                                                                                                               OK
                                                                                                                               or                                                         ERROR CODE = <value>
                                                                                                                                               OK
                                                                                                          Purpose:                                                                        Initiate a time-limited position fix with a specified accuracy.
                                                                                                                                     Query List:                                            AT!GPSFIX  =  ?
                                                                                                          Purpose:                                                                        Return supported <fixType>, <maxTime>, and <maxDist> values.

                                                                                                 Parameters:
                                                                                                 <fixType> (Type of fix to establish)
                                                                                                                                              1  =  Standalone (not supported by a mobile station)
                                                                                                                                              2  =  MS-based only
                                                                                                                                             3  =  MS-assisted only
                                                                                                 <maxTime> (Maximum time to wait for a position fix)
                                                                                                                                              Valid range: 0–255         seconds
                                                                                                 <maxDist> (Requested accuracy of fix)
                                                                                                                                              Entered in decimal format
                                                                                                                                              Valid range:
                                                                                                                                                        0–4294967279 meters
                                                                                                                                                        4294967280  =  No preference
                                                                                                 <value> (Error code returned when command fails for any reason)
                                                                                                                                             See Table         7-3 on page         93 for a list of possible error codes.
                                                                                                                                              N/A—  Not available

                                                                                                 Example(s):
                                                                                                 AT!GPSFIX  =  1, 15, 10 requests a standalone position fix to 10 meters accuracy. The
                                                                                                 request will fail (timeout) if the modem cannot determine a position fix within
                                                                                                 15         seconds.

                                                                                                 Related commands:
                                                                                                                                      !GPSSTATUS (page         89)—Use this command while the tracking session is in
                                                                                                          progress.
                                                                                                                                      !GPSLOC (page         83)—Use this command after the session completes to obtain
                                                                                                          the result.


80                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 81

GNSS Commands


Table 7-2:                GNSS command details (Continued)

   Command                                                                                       Description

   !GPSLBSAPN                                                                                    Set GPS LBS APNs

                                                                                                 Set the GPS LBS APNs to be used for various RATs (Radio Access Technologies).

                                                                                                 Password required: Yes

                                                                                                 Usage:
                                                                                                                                     Execution (Add):
                                                                                                                                               AT!GPSLBSAPN  =  <operation>,<ratmask>,<IPtype>,<APN>
                                                                                                          Execution (Delete one):
                                                                                                                                               AT!GPSLBSAPN  =  <operation>,<ratmask>
                                                                                                          Execution (Delete all):
                                                                                                                                               AT!GPSLBSAPN  =  <operation>
                                                                                                          Response:                                               OK
                                                                                                                               or                                                         ERROR
                                                                                                          Purpose:                                                                       Set the APN to be used for the specified <ratmask>, or  delete the
                                                                                                                                               APN for a single <ratmask> or all RATs.
                                                                                                                                     Query:                                                                                                          AT!GPSLBSAPN?
                                                                                                          Response:                                               <ratmask>, <IPType>, <APN>
                                                                                                                                               <ratmask>, <IPType>, <APN>
                                                                                                                                               ...
                                                                                                                                               OK
                                                                                                                               or                                                         OK (if no ID has been set)
                                                                                                          Purpose:                                                                        Display the APNs currently assigned for each RAT.
                                                                                                                                     Query List:                                            AT!GPSLBSAPN  =  ?
                                                                                                          Purpose:                                                                        Display valid parameter options.

                                                                                                 Parameters:
                                                                                                 <operation> (Add or delete APNs)
                                                                                                                                              1  =  Add an APN for a specific <ratmask> and <IPtype>.
                                                                                                                   Note: All paramters are required.


                                                                                                 Note:                To change an APN that has been set for a RAT, you must first delete the
                                                                                                 current APN, then add the new APN.

                                                                                                                                              2  =  Delete the APN for a specific <ratmask>
                                                                                                                   Note: Only <ratmask> is required.
                                                                                                                                              3  =Delete all APNs
                                                                                                                   Note: No other parameters are required.
                                                                                                 <ratmask> (Radio access technology)
                                                                                                                                              Valid values (values shown are in hexadecimal format):
                                                                                                                                                      01  =  CDMA
                                                                                                                                                      02  =  HDR
                                                                                                                                                      04  =  GSM
                                                                                                                                                       08  =  WCDMA
                                                                                                                                                      10  =  LTE

                                                                                                 (Continued on next page)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               81

## Page 82

AirPrime EM75xx AT Command Reference


Table 7-2:                GNSS command details (Continued)

    Command                                                                                                                              Description

    !GPSLBSAPN (continued)                                                                                                               Set GPS LBS APNs (continued)

                                                                                                                                         <IPtype> (Internet Protocol version)
                                                                                                                                                                                          Character string, entered without quotation marks
                                                                                                                                                                                          Valid values:
                                                                                                                                                                                                      IPV4
                                                                                                                                                                                                      IPV6
                                                                                                                                                                                                      IPV4V6
                                                                                                                                         <APN> (Access Point Name)
                                                                                                                                                                                          Character string, entered with quotation marks
                                                                                                                                                                                          Examples: “mycompany.mnc987.mcc123.gprs”, “ourinternet”


82                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 83

GNSS Commands


Table 7-2:                GNSS command details (Continued)

   Command                                                                              Description

   !GPSLOC                                                                             Return last known location of the modem

                                                                                       Return the details obtained during the most recent position location session, if
                                                                                       available.

                                                                                       Password required: No

                                                                                       Usage:
                                                                                                                           Query:                                                                                                          AT!GPSLOC?
                                                                                               Response:                                               Unknown (No information is available)
                                                                                                                                OK
                                                                                                                  or                                                         Not Available (No information is available)
                                                                                                                                OK
                                                                                                                  or                                                          Lat: <latitude>
                                                                                                                                Lon: <longitude>
                                                                                                                                Ti m e :   < t i m e >
                                                                                                                                LocUncAngle: <luAngle> LocUncA: <luA> LocUncP: <luP> HEPE:
                                                                                                                                <hepe>
                                                                                                                                <fixType>
                                                                                                                                Altitude: <altitude> LocUncVe: <luV>
                                                                                                                                Heading: <heading> VelHoriz: <vH> VelVert: <vV>
                                                                                                                                OK (Altitude and heading only appear if data was collected as part
                                                                                                                                of the most recent fix.)
                                                                                               Purpose:                                                                        Return last position location details.

                                                                                       Parameters:
                                                                                       <latitude> (Latitude at last position fix)
                                                                                                                                   Example: “49 Deg 10 Min 21.49 Sec N (0x008BDE6C)”
                                                                                       <longitude> (Longitude at last position fix)
                                                                                                                                   Example: “123 Deg 4 Min 14.76 Sec W (0xFEA1EE9A)”
                                                                                       <time> (Time at which last position fix was taken)
                                                                                                                                   Example: “2009 01 30 4 20:27:18 (GPS)”
                                                                                       <luAngle> (Location uncertainty angle of returned position)
                                                                                                                                   Example: “11.2 deg”
                                                                                       <luA> (Standard deviation of axis along <luAngle>)
                                                                                                                                   Example: “6.0 m”
                                                                                       <luP> (Standard deviation of axis perpendicular to <luAngle>)
                                                                                                                                   Example: “6.0 m”
                                                                                       <hepe> (Horizontal Estimated Positional Error)
                                                                                                                                   Example: “8.485 m”
                                                                                       <fixType> (2D or 3D fix)
                                                                                                                                   Example: “2D Fix” or “3D Fix”
                                                                                       <altitude> (Altitude in meters at which last position fix was taken)
                                                                                                                                   Only present if <fixType> is 3D
                                                                                                                                   Example: “-1 m”
                                                                                       <luV> (Vertical uncertainty in meters)
                                                                                                                                   Only present if <fixType> is 3D
                                                                                                                                   Example: “3.0 m”

                                                                                       (Continued on next page)

Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               83

## Page 84

AirPrime EM75xx AT Command Reference


Table 7-2:                GNSS command details (Continued)

   Command                                                                                                     Description

   !GPSLOC (continued)                                                                                        Return last known location of the modem (continued)

                                                                                                              <heading> (Direction of MS)
                                                                                                                                                             Example: “0.0 deg”
                                                                                                              <vH> (Horizontal velocity)
                                                                                                                                                             Example: “0.0 m/s”
                                                                                                              <vV> (Vertical velocity)
                                                                                                                                                             Example: “0.0 m/s”

   !GPSMOMETHOD                                                                                               Set   /   report GPS MO method

                                                                                                              Set or report the GPS MO method (session type) that a mobile-originated GPS
                                                                                                              session should use (Control plane or User plane).

                                                                                                              Password required: Yes
                                                                                                              Reset required to apply changes: Yes

                                                                                                              Usage:
                                                                                                                                                  Execution:                                                 AT!GPSMOMETHOD  =  <MO_method>
                                                                                                                        Response:                                               OK
                                                                                                                                                or                                                         ERROR
                                                                                                                        Purpose:                                                                         Indicate the MO method to use.
                                                                                                                                                  Query:                                                                                                          AT!GPSMOMETHOD?
                                                                                                                        Response:                                               <MO_method>
                                                                                                                                                                  OK
                                                                                                                        Purpose:                                                                        Return the current <MO_method> setting.

                                                                                                              Parameters:
                                                                                                              <MO_method> (MO method)
                                                                                                                                                             0  =  CP (Control Plane)
                                                                                                                                                             1  =  UP (User Plane)


84                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 85

GNSS Commands


Table 7-2:                GNSS command details (Continued)

    Command                                                                                                   Description

    !GPSNMEACONFIG                                                                                            Enable and set NMEA data output rate

                                                                                                              Enable or disable NMEA data output, and set the output rate for use with
                                                                                                              !GPSTRACK.

                                                                                                              Requirements:
                                                                                                                                                            NMEA streaming must be enabled using !GPSNMEA before this command
                                                                                                                                  will work.

                                                                                                              Password required: Yes

                                                                                                              Usage:
                                                                                                                                                  Execution:                                                 AT!GPSNMEACONFIG   =  <enable>[,<outputRate>]
                                                                                                                       Response:                                               OK
                                                                                                                                               or                                                         ERROR
                                                                                                                       Purpose:                                                                        Enable or disable NMEA output and set rate.
                                                                                                                                                  Query:                                                                                                          AT!GPSNMEACONFIG?
                                                                                                                       Response:                                               Enabled: <enable>
                                                                                                                                                                 Output Rate: <outputRate>
                                                                                                                                                                 OK
                                                                                                                       Purpose:                                                                        Return the current <timeout> period.
                                                                                                                                                  Query List:                                            AT!GPSNMEACONFIG   =  ?
                                                                                                                       Purpose:                                                                        Return valid parameter values.

                                                                                                              Parameters:
                                                                                                              <enable> (Enable  /  disable NMEA data output)
                                                                                                                                                            0  =  Disable. (Note: <outputRate> is ignored)
                                                                                                                                                            1  =  Enable. (Note: <outputRate> is required)
                                                                                                              <outputRate> (NMEA data output rate  —  time between outputs)
                                                                                                                                                            Valid range: 1–255         seconds


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               85

## Page 86

AirPrime EM75xx AT Command Reference


Table 7-2:                GNSS command details (Continued)

   Command                                                                                 Description

  !GPSNMEASENTENCE                                                                         Set   /   report NMEA sentence type

                                                                                           Set or report the current GPS NMEA sentence types.

                                                                                           Requirements:
                                                                                                                                       NMEA streaming must be enabled using !GPSNMEA before this command
                                                                                                            will work.

                                                                                           Password required: Yes

                                                                                           Usage:
                                                                                                                               Execution:                                                 AT!GPSNMEASENTENCE  =  <nmea type>
                                                                                                   Response:                                               OK
                                                                                                                       or                                                         ERROR
                                                                                                   Purpose:                                                                        Enable or disable NMEA sentence types.
                                                                                                                               Query:                                                                                                          AT!GPSNMEASENTENCE?
                                                                                                   Response:                                               !GPSNMEASENTENCE: <nmea type>
                                                                                                                                     OK
                                                                                                   Purpose:                                                                        Indicate the currently enabled GPS NMEA sentence types.
                                                                                                                               Query List:                                            AT!GPSNMEASENTENCE  =  ?
                                                                                                   Response:                                               !GPSNMEASENTENCE: (00-1FFFFF)
                                                                                                                                     OK
                                                                                                   Purpose:                                                                        Indicates Execution format. See parameter description below for
                                                                                                                                     details of supported values.

                                                                                           Parameters:
                                                                                           <nmea type> (NMEA sentence types)
                                                                                                                                       2-byte hex format mask (Note: In the execution format, do not include ‘0x’
                                                                                                            before the mask value)
                                                                                                                                       Each bit: 0  =  Disabled; 1  =  Enabled
                                                                                                                                       Bit 0: GPGGA (GPS fix data)
                                                                                                                                       Bit 1: GPRMC (GPS recommended minimum data)
                                                                                                                                       Bit 2: GPGSV (GPS satellites in view)
                                                                                                                                      Bit 3: GPGSA (GPS overall satellite data)
                                                                                                                                       Bit 4: GPVTG (GPS vector track and speed over the ground)
                                                                                                                                       Bit 5: Reserved
                                                                                                                                       Bit 6: GLGSV (GLONASS satellites in view)
                                                                                                                                       Bit 7: GNGSA (GLONASS overall satellite data)
                                                                                                                                       Bit 8: GNGNS (Time, position, and fix related data for GLONASS receiver)
                                                                                                                                       Bit 9: GARMC (Galileo recommended minimum data)
                                                                                                                                       Bit 10: GAGSV (Galileo satellites in view)
                                                                                                                                      Bit 11: GAGSA (Galileo overall satellite data)
                                                                                                                                       Bit 12: GAVTG (Galileo Vector track and speed over the ground)
                                                                                                                                       Bit 13: PSTIS (GPS session start indication)
                                                                                                                                       Bit         14: ExGSV (Extended GSV). This option modifies the output format of the
                                                                                                            GPGSV sentence to display azimuth and elevation as float values. If this bit is
                                                                                                            not set, the values appear in decimal format.
                                                                                                                                       Bit 15: GAGGA (Galileo time, position, and fix related data)
                                                                                                                                      Bit         16: PQGSA (QZSS GSA)

                                                                                           (Continued on next page)

86                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 87

GNSS Commands


Table 7-2:                GNSS command details (Continued)

    Command                                                                                                Description

    !GPSNMEASENTENCE                                                                                      Set   /   report NMEA sentence type (continued)
    (continued)
                                                                                                                                                        Bit         17: PQGSV (QZSS GSV)
                                                                                                                                                        Bit         18: GAGNS (Galileo fix data)
                                                                                                                                                        Bit         19: GPDTM (GPS datum reference information)
                                                                                                                                                        Bit         20: Proprietary sentences for debugging

    !GPSPORTID                                                                                            Set   /   report port ID to use over TCP/IP

                                                                                                          Set or report the port ID of the SUPL server to use when using TCP/IP as the
                                                                                                          transport mechanism for SUPL. The command can also be used when the FQDN is
                                                                                                          auto-generated from the IMSI.

                                                                                                          Password required: Yes

                                                                                                          Usage:
                                                                                                                                              Execution:                                                 AT!GPSPORTID  =  <portid>
                                                                                                                    Response:                                               OK
                                                                                                                                           or                                                         ERROR
                                                                                                                    Purpose:                                                                        Queue the request to set the port ID.
                                                                                                                                              Query:                                                                                                          AT!GPSPORTID?
                                                                                                                    Response:                                               <portid>
                                                                                                                                                            OK
                                                                                                                    Purpose:                                                                        Return the port ID currently being used

                                                                                                          Parameters:
                                                                                                          <port ID> (Port ID to use over TCP/IP)
                                                                                                                                                        Valid range: 0–65535

                                                                                                          Related commands
                                                                                                                                                        !GPSSUPLURL (p.90)  —  Set  /  return SUPL server URL used for TCP/IP


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               87

## Page 88

AirPrime EM75xx AT Command Reference


Table 7-2:                GNSS command details (Continued)

   Command                                                                               Description

  !GPSSATINFO                                                                            Request satellite information

                                                                                         Return the following information for up to twelve satellites in view (including those
                                                                                         used in the latest position fix): satellite vehicle number (SV), elevation (ELEV),
                                                                                         azimuth (AZI), and signal to noise ratio (SNR).
                                                                                         The information returned is valid regardless of the current fix mode or whether the
                                                                                         PDE or the modem performs the fix calculations.

                                                                                         Password required: No

                                                                                         Usage:
                                                                                                                             Query:                                                                                                         AT!GPSSATINFO?
                                                                                                 Response:                                               NO SAT INFO
                                                                                                                                   OK
                                                                                                                     or                                                         Satellites in view: <numSats>  (Timestamp of sat. info)
                                                                                                                                   * SV: <SV 1>  ELEV:<ELEV 1>  AZI:<AZI 1>  SNR:<SNR 1>
                                                                                                                                   ...
                                                                                                                                   * SV: <SV n>  ELEV:<ELEV n>  AZI:<AZI n>  SNR:<SNR n>
                                                                                                                                   OK
                                                                                                 Purpose:                                                                        Return the number of satellites in view (including those used in the
                                                                                                                                   latest position fix) and details for each satellite (or return an error
                                                                                                                                   message).


                                                                                         Note:                An asterisk (*) at the beginning of a line indicates the satellite was used in the
                                                                                         fix location calculation.


                                                                                         Parameters:
                                                                                         <numSats> (Number of satellites in view)
                                                                                                                                     Valid range: 1–12
                                                                                         <SV n> (Satellite vehicle number for the nth satellite in the list)
                                                                                                                                     Valid ranges:
                                                                                                                                              1–32 (GPS)
                                                                                                                                              65–96 (GLONASS)
                                                                                                                                              193–197 (QZS)
                                                                                                                                              201–237 (Beidou)
                                                                                                                                              301–336 (Galileo)
                                                                                         <ELEV n> (Satellite elevation relative to modem location, in degrees)
                                                                                                                                     Valid range: 0–90
                                                                                         <AZI n> (Satellite azimuth relative to modem location, in degrees)
                                                                                                                                     Valid range: 0–360
                                                                                         <SNR n> (Signal to noise ratio, in dB)
                                                                                                                                     Valid range: 0–99


88                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 89

GNSS Commands


Table 7-2:                GNSS command details (Continued)

   Command                                                                                 Description

   !GPSSTATUS                                                                              Request current status of a position fix session

                                                                                           Return the current status of a position fix session.

                                                                                           Password required: No

                                                                                           Usage:
                                                                                                                               Query:                                                                                                          AT!GPSSTATUS?
                                                                                                   Response:                                               <year> <month> <day> <day of week> <time of day> Last Fix
                                                                                                                                     Status = <status>
                                                                                                                                     <year> <month> <day> <day of week> <time of day> Fix Session
                                                                                                                                     Status = <status>
                                                                                                   Purpose:                                                                        Return timestamps and status of a position fix session.

                                                                                           Parameters (Timestamp):
                                                                                           <year>
                                                                                                                                       Example: “2007”
                                                                                           <month>
                                                                                                                                       01–12 (Jan–Dec)
                                                                                           <day>
                                                                                                                                       01–31
                                                                                           <day of week>
                                                                                                                                       0–6 (0  =  Monday)
                                                                                           <time of day>
                                                                                                                                       24-hour clock format
                                                                                                                                       Example: “13:25:48”

                                                                                           Parameters (Status):
                                                                                           <status> (Session status)
                                                                                                                                       “NONE”: No session of this type has occurred since the modem powered up.
                                                                                                                                                The timestamp is the current time.
                                                                                                                                       “ACTIVE”: A session of this type is currently active.
                                                                                                                                                The timestamp is the time when the session entered this state.
                                                                                                                                       “SUCCESS”: The most recent session of this type succeeded.
                                                                                                                                                The timestamp is the time when the previous session completed success-
                                                                                                                    fully.
                                                                                                                                       “FAIL”: The most recent session of this type failed.
                                                                                                                                                The timestamp is the time when the previous session failed.
                                                                                                                                                An error code is displayed with the “FAIL” string. See Table         7-3 on
                                                                                                                    page        93 for a list of error codes.

                                                                                           Example(s):
                                                                                           AT!GPSSTATUS? returns:
                                                                                           2007 01 06 6 00:25:01 Last Fix Status = SUCCESS
                                                                                           2007 01 06 6 00:25:02 Fix Session Status = ACTIVE


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               89

## Page 90

AirPrime EM75xx AT Command Reference


Table 7-2:                GNSS command details (Continued)

   Command                                                                                                   Description

   !GPSSUPLURL                                                                                              Set   /   report SUPL server URL

                                                                                                            Set or return the URL of the SUPL server to be used when TCP/IP is used as the
                                                                                                            transport mechanism for location processing. Use !GPSPORTID to set the port ID.

                                                                                                            Password required: Yes

                                                                                                            Usage:
                                                                                                                                                Execution:                                                 AT!GPSSUPLURL  =  ”<suplURL>”
                                                                                                                      Response:                                               OK
                                                                                                                                              or                                                         ERROR
                                                                                                                      Purpose:                                                                         Identify the SUPL server URL.
                                                                                                                                                Query:                                                                                                          AT!GPSSUPLURL?
                                                                                                                      Response:                                               <suplURL>
                                                                                                                                                               OK
                                                                                                                      Purpose:                                                                        Return the SUPL server’s URL..
                                                                                                                                                Query List:                                            AT!GPSSUPLURL  =  ?
                                                                                                                      Purpose:                                                                        Return the execution command format.

                                                                                                            Parameters:
                                                                                                            <suplURL> (SUPL server URL)
                                                                                                                                                          Must be a fully qualified domain name (FQDN) or address
                                                                                                                                                          Examples: “supl.url.net”, “123.123.123.123”
                                                                                                                                                          The <suplURL> is not checked for correctness  —  if the string is invalid, the
                                                                                                                                modem will not be able to perform MS-assisted GPS fixes.

                                                                                                            Example(s):
                                                                                                            AT!GPSSUPLURL =  ”supl.url.net”
                                                                                                            AT!GPSSUPLURL =  ”123.123.123.123”


90                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 91

GNSS Commands


Table 7-2:                GNSS command details (Continued)

     Command                                                                                                                               Description

     !GPSSUPLVER                                                                                                                           Set   /   report SUPL server version

                                                                                                                                           Set or return the version of the SUPL server.

                                                                                                                                           Password required: Yes

                                                                                                                                           Usage:
                                                                                                                                                                               Execution:                                                 AT!GPSSUPLURL  =  <supl ver>”
                                                                                                                                                       Response:                                               OK
                                                                                                                                                                                     or                                                         ERROR
                                                                                                                                                       Purpose:                                                                        Identify the SUPL server version.
                                                                                                                                                                               Query:                                                                                                          AT!GPSSUPLVER?
                                                                                                                                                       Response:                                               <supl ver>
                                                                                                                                                                                                           OK
                                                                                                                                                       Purpose:                                                                        Return the SUPL server’s version.
                                                                                                                                                                               Query List:                                            AT!GPSSUPLVER=  ?
                                                                                                                                                       Purpose:                                                                        Return the execution command format.

                                                                                                                                           Parameters:
                                                                                                                                           <supl ver> (SUPL server version)
                                                                                                                                                                                           1  —  SUPL version 1
                                                                                                                                                                                           2  —  SUPL version 2


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               91

## Page 92

AirPrime EM75xx AT Command Reference


Table 7-2:                GNSS command details (Continued)

   Command                                                                          Description

  !GPSTRACK                                                                         Initiate local tracking (multiple fix) session

                                                                                    Initiate a local tracking session comprising a specific number of position fixes taken
                                                                                    at regular time intervals.

                                                                                    Password required: No

                                                                                    Usage:
                                                                                                                        Execution:                                                 AT!GPSTRACK = <fixType>, <maxTime>, <maxDist>, <fixCount>,
                                                                                                                            <fixRate>
                                                                                            Response:                                               Fix initiated
                                                                                                                            OK
                                                                                                              or                                                         ERROR CODE = <value>
                                                                                                                            OK
                                                                                            Purpose:                                                                        Initiate a series of time-limited position fixes.
                                                                                                                        Query List:                                            AT!GPSTRACK  =  ?
                                                                                            Purpose:                                                                        Return supported <fixType>, <maxTime>, <maxDist>, <fixCount>,
                                                                                                                            and <fixRate> values.

                                                                                    Parameters:
                                                                                    <fixType> (Type of fix to establish)
                                                                                                                                1  =  Standalone (not supported by a mobile station)
                                                                                                                                2  =  MS-based only
                                                                                                                               3  =  MS-assisted only
                                                                                    <maxTime> (Maximum time to wait for satellite information)
                                                                                                                                Valid range: 0–255         seconds
                                                                                    <maxDist> (Requested accuracy of fix)
                                                                                                                                Entered in decimal format
                                                                                                                                Valid range:
                                                                                                                                        0–4294967279        meters
                                                                                                                                        4294967280  =  No preference
                                                                                    <fixCount> (Number of position fixes requested)
                                                                                                                                Valid range: 1–1000 (1000  —  Take a continuous series of position fixes)
                                                                                    <fixrate> (Amount of time to wait between fix attempts)
                                                                                                                                Valid range: 0–1799999         seconds

                                                                                    Failure conditions:
                                                                                    The request fails if the tracking session fails to initiate.
                                                                                    If the request fails, the message ERROR CODE = <value> is returned. See Table         7-3
                                                                                    on page         93 for a list of error codes.


                                                                                    Note:                The ‘time to first fix’ may require more time than subsequent fixes, if almanac,
                                                                                    ephemeris, or location data needs to be updated. (Almanac data is valid for 3–4
                                                                                    days, ephemeris for 30–120 minutes, and location data for 4 minutes). To avoid a
                                                                                    timeout error (time spent > <maxtime>), your application could precede the
                                                                                    !GPSTRACK call with a single position fix (AGPSFIX) with a greater <maxTime>
                                                                                    value.


                                                                                    (Continued on next page)

92                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 93

GNSS Commands


Table 7-2:                GNSS command details (Continued)

   Command                                                                          Description

   !GPSTRACK                                                                        Initiate local tracking (multiple fix) session (continued)
   (continued)
                                                                                    Example(s):
                                                                                    AT!GPSTRACK  =  1, 15, 10, 20, 60 requests a series of 20 standalone position fixes to
                                                                                    10 meters accuracy  —                  fixes are taken every 60         seconds.
                                                                                    One of the following responses will be received:
                                                                                                                               “OK” if the request is successful, or
                                                                                                                               “ERROR CODE = <value>” if the request fails for any reason. See Table         7-3
                                                                                                   on page         93 for a list of error codes.

                                                                                    Related commands:
                                                                                                                         !GPSSTATUS—Use this command while the tracking session is in progress.
                                                                                                                         !GPSLOC—Use this command after the session completes to obtain the result.
   +WANT                                                                             Enable   /   disable GNSS antenna power

                                                                                    Enable or disable GNSS antenna power (3.3V).

                                                                                    Password required: No
                                                                                    Persistent across power cycles: Yes

                                                                                    Usage:
                                                                                                                        Execution:                                                 AT+WANT  =  <enable>
                                                                                           Response:                                               OK
                                                                                           Purpose:                                                                        Enable or disable the GNSS antenna power (3.3V).
                                                                                                                        Query List:                                            AT+WANT=  ?
                                                                                           Purpose:                                                                        Display valid execution format and parameter values.

                                                                                    Parameters:
                                                                                    <enable> (Enable  /  disable GNSS antenna power)
                                                                                                                               0  =  Disable
                                                                                                                               1  =  Enable

                                                                        Error codes

                                                                        Table         7-3 describes error codes that can be returned by !GPSEND (page         79),
                                                                        !GPSSTATUS (page         89), and !GPSTRACK (page         92).
                                                                        Table         7-4 on page         95 describes error codes that can be returned by !GPSFIX
                                                                        (page         80)

Table 7-3:               AT command error codes                                     (!GPSEND, !GPSSTATUS, !GPSTRAC                                         K)

   Error code                            Description

      0                                  Phone is offline

      1                                  No service

      2                                  No connection with PDE (Position Determining Entity)

      3                                  No data available


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               93

## Page 94

AirPrime EM75xx AT Command Reference


Table 7-3:               AT command error codes                                     (!GPSEND, !GPSS                      TATUS, !GPSTRAC                 K) (Continued)

   Error code                             Description

      4                                   Session Manager is busy

      5                                   Reserved

      6                                   Phone is GPS-locked

      7                                   Connection failure with PDE

      8                                   Session ended because of error condition

      9                                   User ended the session

      10                                  End key pressed from UI

      11                                  Network session was ended

      12                                  Timeout (for GPS search)

      13                                  Conflicting request for session and level of privacy

      14                                  Could not connect to the network

      15                                  Error in fix

      16                                  Reject from PDE

      17                                  GPS is disabled

      18                                  Ending session due to E911 call

      19                                  Server error

      20                                  Reserved

      21                                  Reserved

      22                                  Unknown system error

      23                                  Unsupported service

      24                                  Subscription violation

      25                                  Desired fix method failed

      26                                  Reserved

      27                                  No fix reported because no Tx confirmation was received

      28                                  Network indicated normal end of session

      29                                  No error specified by the network

      30                                  No resources left on the network

      31                                  Position server not available

      32                                  Network reported an unsupported version of protocol


94                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 95

GNSS Commands


Table 7-4:               AT command error codes (!GPSFIX)

                                  Error code                             Description

                                      0                                  No error

                                      1                                  Invalid client ID

                                      2                                  Bad service parameter

                                      3                                  Bad session type parameter

                                      4                                  Incorrect privacy parameter

                                      5                                  Incorrect download parameter

                                      6                                  Incorrect network access parameter

                                      7                                  Incorrect operation parameter

                                      8                                  Incorrect number of fixes parameter

                                      9                                  Incorrect server information parameter

                                      10                                 Error in timeout parameter

                                      11                                 Error in QOS accuracy threshold parameter

                                      12                                 No active session to terminate

                                      13                                 Session is active

                                      14                                 Session is busy

                                      15                                 Phone is offline

                                      16                                 Phone is CDMA locked

                                      17                                 GPS is locked

                                      18                                 Command is invalid in current state

                                      19                                 Connection failure with PDE

                                      20                                 PDSM command buffer unavailable to queue command

                                      21                                 Search communication problem

                                      22                                 Temporary problem reporting position determination results

                                      23                                 Error mode not supported

                                      24                                 Periodic NI in progress

                                      25                                 Unknown error

                                      26                                 Unknown error


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               95

## Page 96

8: SIM Commands                                                                                                                                                                                                                                                                                                                                                                                                         8
                                                •                  Introduction
                                                •                  Command summary
                                                •                  Command
                                                                 reference                                                                                                                                                                                                                             Introduction
                                                                                                                                                                   This chapter describes commands used to communicate with an
                                                                                                                                                                   installed (U)SIM.
                                                                                                                                                                   Command summary

                                                                                                                                                                   Table         8-1 lists the commands described in this chapter:

                                                Table 8-1:                SIM command passwords

                                                     Command                                                                                                                     Description                                                                                                                                                                                                                                Page

                                                          !UIMS                                                                                                                 Select active SIM interface                                                                                                                                                                                                                97


Rev 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                              Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      96

## Page 97

Command reference

Table 8-2:                SIM command details

    Command                                                                                                Description

   !UIMS                                                                                                   Select active SIM interface
                                                                                                           On a module that supports multiple SIM interfaces, select the active SIM interface.
                                                                                                           To enable  /  disable UIM2 slot support, use AT!CUSTOM=”UIM2ENABLE”. See page         27
                                                                                                           for option values.
                                                                                                           Password required: No

                                                                                                           Usage:
                                                                                                                                               Execution:                                                 AT!UIMS  =  <uim_slot>
                                                                                                                     Response:                                               OK
                                                                                                                     Purpose:                                                                        Configure the module to use the selected SIM interface.
                                                                                                                                               Query:                                                                                                         AT!UIMS?
                                                                                                                     Response:                                               !UIMS: <uim_slot>
                                                                                                                                                                  OK
                                                                                                                     Purpose:                                                                        Display the currently selected interface.
                                                                                                                                               Query List:                                           AT!UIMS=  ?
                                                                                                                     Purpose:                                                                       Return the command format and the supported parameter values.
                                                                                                           Parameters:
                                                                                                           <uim> (SIM interface):
                                                                                                                                              0  =  UICC1  —  External UIM interface #1
                                                                                                                                              1  =  UICC2  —  External UIM interface #2


Rev 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                        Proprietary  and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            97

## Page 98

9: OMA-DM Commands                                                                                                                                                                                                                                                                                                 9

                                                                                                                          Introduction

                                                                                                                          This chapter describes commands used to configure DM (Device
                                                                                                                          Management) accounts, sessions, and host–device–server
                                                                                                                          interactions.
                                                                                                                          Command summary

                                                                                                                          The table below lists the commands described in this chapter.

                                    Table 9-1:                OMA-DM commands

                                       Command                                                                                           Description                                                                                                                                                                 Page

                                           !HOSTDEVINFO                                                                                  Configure host device details                                                                                                                                               99

                                           !IMSTESTMODE                                                                                  Enable  /  disable IMS test mode                                                                                                                                            100

                                           !OSINFO                                                                                       Configure host device operating system information                                                                                                                          101


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      98

## Page 99

OMA-DM Commands

                                                                                  Command reference

Table 9-2:                OMA-DM command details

   Command                                                                              Description

   !HOSTDEVINFO                                                                         Configure host device details

                                                                                        Configure the host device details that will be reported by OMA       DM for AT&T devices, to
                                                                                        comply with AT&T <CDR-DVM-4532> requirement.
                                                                                        To configure host device operating system information, see !OSINFO on page         101.

                                                                                        Note:                In the Execution format, if a parameter is not entered then the value on the
                                                                                        device does not change.


                                                                                        Password required: Yes  —  Execution formation only

                                                                                        Usage:
                                                                                                                            Execution:                                                 AT!HOSTDEVINFO  =  “<hostman>”[, “<hostmod>”[, “<hostswv>”[,
                                                                                                                                    “<hostplasmaid>”]]]
                                                                                                Response:                                               OK
                                                                                                                     or                                                         ERROR
                                                                                                Purpose:                                                                       Set some or all host device detail parameters.
                                                                                                                            Query:                                                                                                         AT!HOSTDEVINFO?
                                                                                                Response:                                               HostMan:                                                                                                    <hostman>
                                                                                                                                    HostMod:                                                                                                      <hostmod>
                                                                                                                                    HostSwV:                                                                                                   <hostswv>
                                                                                                                                    HostPlasmaID:                        <hostplasmaid>
                                                                                                                                    OK
                                                                                                Purpose:                                                                       Display current host device details.
                                                                                                                            Query List:                                           AT!HOSTDEVINFO  =  ?
                                                                                                Purpose:                                                                        Display the execution command format and parameter values.

                                                                                        Parameters:
                                                                                        <hostman> (Host device manufacturer’s name)
                                                                                                                                    256 characters maximum
                                                                                        <hostmod> (Host device model name)
                                                                                                                                    256 characters maximum
                                                                                        <hostswv> (Host software version)
                                                                                                                                    256 characters maximum
                                                                                        <hostplasmaid> (Host Plasma ID)
                                                                                                                                    256 characters maximum

                                                                                        Example(s):
                                                                                                                           AT!HOSTDEVINFO=”Manufacturer”,,”1.0”,
                                                                                                This sets the <hostman> and <hostswv> values. The values for <hostmod> and
                                                                                                <hostplasmaid> do not change.
                                                                                                                           AT!HOSTDEVINFO=”Manufacturer”
                                                                                                This sets the <hostman> value. The values for all other parameters do not change.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               99

## Page 100

AirPrime EM75xx AT Command Reference


Table 9-2:                OMA-DM command details (Continued)

   Command                                                                                                             Description

   !IMSTESTMODE                                                                                                       Enable   /   disable IMS test mode

                                                                                                                      Enable  /  disable IMS (IP Multimedia Subsystem) test mode.
                                                                                                                      If IMS test mode is enabled:
                                                                                                                                                          IMS registration attempts will not occur
                                                                                                                                                          SMS over IMS is not supported

                                                                                                                      Password required: Yes

                                                                                                                      Usage:
                                                                                                                                                          Execution:                                                 AT!IMSTESTMODE  =  <mode>
                                                                                                                                 Response:                                               OK
                                                                                                                                 Purpose:                                                                        Enable  /  disable IMS test mode.
                                                                                                                                                          Query:                                                                                                         AT!IMSTESTMODE?
                                                                                                                                 Response:                                               IMS Test Mode Enabled
                                                                                                                                                              or                                                         IMS Test Mode Disabled
                                                                                                                                 Purpose:                                                                       Return the current state of IMS Test Mode.

                                                                                                                      Parameters:
                                                                                                                      <mode> (IMS Test Mode state)
                                                                                                                                                                      0  =  Disable
                                                                                                                                                              1=Enable


100                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 101

OMA-DM Commands


Table 9-2:                OMA-DM command details (Continued)

   Command                                                                                  Description

   !OSINFO                                                                                  Configure host device operating system information

                                                                                            Configure the host device operating system name and version that will be reported by
                                                                                            OMA       DM for AT&T devices, to comply with AT&T <CDR-DVM-4533> requirement.
                                                                                            To configure host device details, see !HOSTDEVINFO on page         99.


                                                                                            Note:                In the Execution format, if a parameter is not entered then the value on the
                                                                                            device does not change.


                                                                                            Password required: Yes  —  Execution format only

                                                                                            Usage:
                                                                                                                                Execution:                                                 AT!OSINFO  =  “<osname>”[, “<osversion>”]
                                                                                                     Response:                                               OK
                                                                                                                          or                                                         ERROR
                                                                                                     Purpose:                                                                       Set host device operating system information parameters.
                                                                                                                                Query:                                                                                                         AT!OSINFO?
                                                                                                     Response:                                               OSName:                                                                                                <osname>
                                                                                                                                           OSVersion:                                                                            <osversion>
                                                                                                                                           OK
                                                                                                     Purpose:                                                                       Display current host device operating system information.
                                                                                                                                Query List:                                           AT!OSINFO  =  ?
                                                                                                     Purpose:                                                                        Display the execution command format and parameter values.

                                                                                            Parameters:
                                                                                            <osname> (Host device operating system name)
                                                                                                                                         256 characters maximum
                                                                                            <osversion> (Host device operating system version)
                                                                                                                                         256 characters maximum

                                                                                            Example(s):
                                                                                                                               AT!OSINFO=”An OS Name”,”1.0”
                                                                                                     This sets both parameters.
                                                                                                                               AT!OSINFO=,”1.0”
                                                                                                     This sets the <osversion> value. The value for the <osname> does not change.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  101

## Page 102

10: SAR Backoff and Thermal Control                                                                                                                                                                                           10
                          Commands

                                                                                        Introduction

                                                                                        This chapter describes:
                                                                                                                                            SAR-related commands (Specific Absorption Rate)  —  SAR
                                                                                                 commands are used to meet regulatory requirements for the
                                                                                                 OEM host device by managing the modem’s SAR backoff state.
                                                                                                 OEMs should carefully evaluate their use of these commands
                                                                                                 and their impact on device operation.


                                                                                        Note:                Operators may require OEMs to disclose SAR settings and theory of
                                                                                        operation for applicable certifications.

                                                                                                                                            Thermal mitigation-related commands  —   These commands may
                                                                                                 affect the host device’s performance. OEMs should carefully
                                                                                                 evalute their use of these commands to ensure that the device
                                                                                                 meets performance expectations.
                                                                                        Command summary

                                                                                        The table below lists the commands described in this chapter.

                          Table 10-1:                SAR backoff and                                    thermal control commands

                            Command                                                                Description                                                                                                                 Page

                               !MAXPWR                                                             Set  /  report maximum Tx power                                                                                             103

                               !SARBACKOFF                                                         Set  /  report offset from maximum Tx power                                                                                 104

                               !SARINTGPIOMODE                                                     Set  /  report default pull mode for SAR interrupt GPIOs                                                                    105

                               !SARSTATE                                                           Set  /  report SAR backoff state                                                                                            106

                               !SARSTATEDFLT                                                       Set  /  report default SAR backoff state                                                                                    107


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  102

## Page 103

Thermal Mitigation Commands

                                                                               Command reference

Table 10-2:                Thermal mitigation command details

   Command                                                                           Description

   !MAXPWR                                                                          Set   /   report maximum Tx power

                                                                                    Set or report the maximum Tx power for a specific band.


                                                                                    Caution:               Any adjustments of Tx power may impact regulatory certification of the
                                                                                    module in the host platform. The OEM is responsible for ensuring that the final module
                                                                                    configuration in the host platform meets all regulatory requirements.


                                                                                    Note:                Increasing the Tx power affects the module’s current consumption and thermal
                                                                                    performance.


                                                                                    Password required: Yes

                                                                                    Usage:
                                                                                                                        Execution (WCDMA/  LTE):
                                                                                                                               AT!MAXPWR  =  <band>,<tech>,<max_tx_pwr>
                                                                                            Response:                                               OK
                                                                                            Purpose:                                                                        Set the maximum Tx power for the specified band  /  technology combi-
                                                                                                                               nation.
                                                                                                                        Query:                                                                                                          AT!MAXPWR?<band>,<tech>
                                                                                            Response:                                               <maxpwr> dBm
                                                                                                                               OK
                                                                                            Purpose:                                                                       Indicate the maximum Tx power for the specified band  /  te chnology
                                                                                                                               combination.
                                                                                                                        Query list:                                                       AT!MAXPWR  =  ?
                                                                                            Purpose:                                                                        Display valid execution format and parameter values.

                                                                                    Parameters:
                                                                                    <band> (RF band)
                                                                                                                                 3GPP band number. For a full listing of 3GPP band numbers, see Table        13-2 on
                                                                                                     page         129.
                                                                                                                                 Band support is product specific  —  see the device’s Product Specification or
                                                                                                     Product Technical Specification document for details.
                                                                                                                                 Valid range: 0–71
                                                                                    <tech> (Network technology)
                                                                                                                                 0  =  WCDMA
                                                                                                                         2=LTE
                                                                                    <maxpwr> (Maximum Tx power in dB)
                                                                                                                                 Valid range: 20.0–24.5


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  103

## Page 104

AirPrime EM75xx AT Command Reference


Table 10-2:                Thermal mitigation                                     command details (Continued)

  Command                                                                              Description

  !SARBACKOFF                                                                         Set   /   report offset from maximum Tx power

                                                                                      Set or report the offset from maximum Tx power limit for a specific band  /  technology/
                                                                                      backoff state combination.
                                                                                      Changes take place after the next modem reset.

                                                                                      Password required: Yes

                                                                                      Usage:
                                                                                                                          Execution (WCDMA, LTE):
                                                                                                                                   AT!SARBACKOFF  =  <tech>,<band>,<state>,<Backoff         offset>
                                                                                              Response:                                               OK
                                                                                              Purpose:                                                                        Set the maximum Tx power for the tech  /  band  /  state combination.
                                                                                                                          Execution (GSM):
                                                                                                                                   AT!SARBACKOFF  =  <tech>,<band>,<slot>,<state>,<modulation>,
                                                                                                                                   <offset>
                                                                                              Response:                                               OK
                                                                                              Purpose:                                                                        Set the maximum Tx power for the tech  /  band  /  state combination.
                                                                                                                          Query (WCDMA, LTE):
                                                                                                                                   AT!SARBACKOFF?<tech>,<band>,<state>
                                                                                              Response:                                               <offset> dBm
                                                                                                                   or
                                                                                                                                   NV Not Set

                                                                                                                                  OK
                                                                                              Purpose:                                                                        Display the offset from maximum Tx power for the tech/band/state
                                                                                                                                  combination.
                                                                                                                          Query (GSM):
                                                                                                                                   AT!SARBACKOFF?<tech>,<band>,<slot>,<state>,<modulation>
                                                                                              Response:                                               <offset> dBm
                                                                                                                   or
                                                                                                                                   NV Not Set

                                                                                                                                  OK
                                                                                              Purpose:                                                                        Display the offset from maximum Tx power for the tech/band/state
                                                                                                                                  combination.
                                                                                                                          Query list:                                                       AT!SARBACKOFF  =  ?
                                                                                              Purpose:                                                                        Display valid execution format and parameter values for LTE  /
                                                                                                                                  WCDMA /  CDMA and GSM queries.

                                                                                      Parameters:
                                                                                      <tech> (Network technology)
                                                                                                                                   0  =  WCDMA
                                                                                                                           2=LTE
                                                                                                                           3=GSM
                                                                                                                                   4  =  TD-SCDMA
                                                                                      <band> (RF band)
                                                                                                                                   0–41
                                                                                                                                   Band support is device-dependent. See the device’s Product Technical Specifi-
                                                                                                        cation for details.
                                                                                      (Continued on next page)

104                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 105

Thermal Mitigation Commands


Table 10-2:                Thermal mitigation                                      command details (Continued)

   Command                                                                                 Description

   !SARBACKOFF                                                                             Set   /   report offset from maximum Tx power (continued)
   (continued)
                                                                                           <slot> (Tx slot. GSM only)
                                                                                                                                        1–5
                                                                                           <state> (SAR backoff state)
                                                                                                                                        0  =  No backoff
                                                                                                                                        1–8  =  Backoff state 1 to 8
                                                                                           <modulation> (Modulation method. GSM only.)
                                                                                                                                        0  =  GMSK (GPRS)
                                                                                                                                        1  =  8PSK (EDGE)
                                                                                           <Backoff        offset> (Offset from max Tx power, in dBm)
                                                                                                                                        Valid values: use the Query List command to display valid values.
                                                                                                                                        Value may be integer or decimal. (For example, 4 or 6.8)
   !SARINTGPIOMODE                                                                         Set   /   report default pull mode for SAR interrupt GPIOs

                                                                                           Set or report the default pull mode (high  /  low) for SAR interrupt GPIOs. This setting
                                                                                           applies to all SAR interrupt GPIOs.

                                                                                           Password required: Yes

                                                                                           Usage:
                                                                                                                               Execution:                                                 AT!SARINTGPIOMODE  =  <mode>
                                                                                                   Response:                                               OK
                                                                                                   Purpose:                                                                        Set the default pull mode for all SAR interrupt GPIOs.
                                                                                                                               Query:                                                                                                          AT!SARINTGPIOMODE?
                                                                                                   Response:                                               <mode>
                                                                                                                                         OK
                                                                                                   Purpose:                                                                        Indicate the default pull mode.
                                                                                                                               Query list:                                                       AT!SARINTGPIOMODE  =  ?
                                                                                                   Purpose:                                                                        Display valid execution format and parameter values.

                                                                                           Parameters:
                                                                                           <mode> (SAR GPIO interrupt pull mode default setting)
                                                                                                                                        0  =  Standard mode  —  Default pull is HIGH  /  DAL_GPIO_PULL_UP
                                                                                                                                        1  =  Inverse mode  —  Default pull is LOW  /  DAL_GPIO_PULL_DOWN


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  105

## Page 106

AirPrime EM75xx AT Command Reference


Table 10-2:                Thermal mitigation                                     command details (Continued)

   Command                                                                                                      Description

   !SARSTATE                                                                                                    Set   /   report SAR backoff state

                                                                                                                Set or report the current SAR (Specific Absorption Rate) backoff state.


                                                                                                                Note:                 This setting is not persistent. To change the default backoff state (persistent), use
                                                                                                                !SARSTATEDFLT.


                                                                                                                Password required: No
                                                                                                                Persistent across power cycles: No

                                                                                                                Usage:
                                                                                                                                                    Execution:                                                 AT!SARSTATE  =  <state>
                                                                                                                          Response:                                               OK
                                                                                                                          Purpose:                                                                        Temporarily set the SAR backoff state.
                                                                                                                                                    Query:                                                                                                          AT!SARSTATE?
                                                                                                                          Response:                                               !SARSTATE: <state>
                                                                                                                                                                         OK
                                                                                                                          Purpose:                                                                       Indicate the current SAR backoff state.
                                                                                                                                                    Query list:                                                      AT!SARSTATE  =  ?
                                                                                                                          Purpose:                                                                        Display valid execution format and parameter values.

                                                                                                                Parameters:
                                                                                                                <state> (SAR backoff state)
                                                                                                                                                               0  =  No backoff
                                                                                                                                                               1–8  =  Backoff state 1 to 8


106                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 107

Thermal Mitigation Commands


Table 10-2:                Thermal mitigation                                      command details (Continued)

    Command                                                                                                  Description

   !SARSTATEDFLT                                                                                             Set   /   report default SAR backoff state

                                                                                                             Set or report the default (persistent) SAR (Specific Absorption Rate) backoff state.


                                                                                                             Note:                This setting is persistent. To temporarily change the backoff state, use
                                                                                                             !SARSTATE.


                                                                                                             Password required: No
                                                                                                             Persistent across power cycles: Yes

                                                                                                             Usage:
                                                                                                                                                 Execution:                                                 AT!SARSTATE  DFLT=   <state>
                                                                                                                       Response:                                               OK
                                                                                                                       Purpose:                                                                        Set the default SAR backoff state.
                                                                                                                                                 Query:                                                                                                          AT!SARSTATEDFLT?
                                                                                                                       Response:                                               !SARSTATEDFLT: <state>
                                                                                                                                                                    OK
                                                                                                                       Purpose:                                                                       Indicate the default SAR backoff state.
                                                                                                                                                 Query list:                                                       AT!SARSTATE  DFLT=   ?
                                                                                                                       Purpose:                                                                        Display valid execution format and parameter values.

                                                                                                             Parameters:
                                                                                                             <state> (SAR backoff state)
                                                                                                                                                            0  =  No backoff
                                                                                                                                                            1–8  =  Backoff state 1 to 8


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  107

## Page 108

11: AirVantage Commands                                                                                                                                                                                                                                             11

                                                                                                        Introduction

                                                                                                        This chapter describes AirVantage (AV) related commands.
                                                                                                        Command summary

                                                                                                        Table         11-1 lists the commands described in this chapter.

                               Table 11-1:                AirVantage commands

                       Command                                                                    Description                                                                                                                                                           Page

                          +WDSC                                                                   Configure AirVantage Management Services                                                                                                                              109

                          +WDSE                                                                   Display most recent AirVantage Management Services error                                                                                                              111

                          +WDSG                                                                   Display AirVantage Management Services status information                                                                                                             11   2

                          +WDSI                                                                   Activate  /  deactivate AirVantage Management Services unsolicited                                                                                                    11   3
                                                                                                  notifications

                          +WDSI (notification)                                                    AirVantage Management Services events  —  Unsolicited notification                                                                                                    11   4

                          +WDSR                                                                   Reply to AirVantage server request                                                                                                                                    11   6

                          +WDSS                                                                   Configure  /  connect AirVantage Management Services session                                                                                                          11   7


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  108

## Page 109

AirVantage Commands

                                                                               Command reference

Table 11-2:                AirVantage Device Services command details

   Command                                                             Description

   +WDSC                                                                Configure AirVantage Management Services

                                                                       Configure the following AirVantage Management Services parameters:
                                                                                                          User agreement for connection, package download and package install
                                                                                                          Polling mode to make a connection to the AirVantage server
                                                                                                          Retry mode to attempt a new connection to the AirVantage server when the WWAN DATA
                                                                               service is temporarily out of order or when an http  /  coap error occurs
                                                                       SIM card requirement: Not required
                                                                       Password required: No
                                                                       Persistent across power cycles: Yes (<State>, <Timer_1>, <Timer_n>

                                                                       Usage:
                                                                                                          Execution (<Mode> = 0, 1, 2, 3, 5):
                                                                                                                  AT+WDSC=<Mode>,<State>
                                                                               Response:                                               OK
                                                                               Purpose:                                                                        Enable or disable the selected <Mode>.
                                                                                                           Execution (<Mode> = 4):
                                                                                                                  AT+WDSC=<Mode>,<Timer_1>[[,<Timer_2>]...[,<Timer_n>]]
                                                                               Response:                                               OK
                                                                               Purpose:                                                                        Set interval timers for successive connection attempts.
                                                                                                           Query:                                                                                                          AT!WDSC?
                                                                               Response:                                               +WDSC: 0,<State>
                                                                                                                  +WDSC: 1,<State>
                                                                                                                  +WDSC: 2,<State>
                                                                                                                  +WDSC: 3,<State>
                                                                                                                  +WDSC: 4,<Timer_1>[[,<Timer_2>]...[,<Timer_n>]]
                                                                                                                  +WDSC: 5,<State>
                                                                                                                  OK
                                                                               Purpose:                                                                        Show the current <Mode> configurations.
                                                                                                           Query List:                                            AT!WDSC=  ?
                                                                               Purpose:                                                                        Display valid execution format and parameter values.

                                                                       (Continued on next page)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  109

## Page 110

AirPrime EM75xx AT Command Reference


Table 11-2:                AirVantage Device Services command details (Continue                                                                           d)

  Command                                                 Description

  +WDSC                                                   Configure AirVantage Management Services (continued)

                                                          Parameters:
                                                          <Mode> (Mode being configured)
                                                                                                     0  =  Reserved for future use
                                                                                                     1  =  User agreement for package download. When enabled, the module returns an
                                                                        unsolicited notification to request an agreement before downloading any package. See
                                                                        +WDSI on page         113 for details.
                                                                                                     2  =  User agreement for package install. When enabled, the module returns an
                                                                        unsolicited notification to request an agreement before installing any package. See
                                                                        +WDSI on page         113 for details.
                                                                                                     3  =  Polling mode. When enabled (<State> > 0), the module waits for the number of
                                                                        minutes specifed in <State>, then will initiate a connection to the AirVantage server
                                                                        based if the device is registered on the network.
                                                                                                     4  =  Retry mode. If an error occurs during a connection to the AirVantage server (e.g.
                                                                        WWAN DATA establishment failed, http error code received), the module will initiate a
                                                                        new connection according to the defined timers. (Note: This is a persistent setting.)
                                                                                                     5  =  User agreement for device reboot. When enabled, the module returns an
                                                                        unsolicited notification to request an agreement before rebooting the device.
                                                          <State> (For <Mode> = 0, 1, 2, 5: Activation state of <Mode>)
                                                                                                     0  =  Disabled (Default value)
                                                                                             1=Enabled
                                                          <State> (For <Mode> = 3: Activation state  /  timer of <Mode>)
                                                                                                     0  =  Disabled (Default value)
                                                                                                     1–525600  =  Polling timer (in minutes)
                                                          <Timer_1>..<Timer_n> (Connection attempt interval timers)
                                                                                                     The number of minutes to wait after connection attempt (n-1) before making
                                                                        connection attempt (n). (Note: There is a maximum of 8 connection attempts.)
                                                                                                    <Timer_1>  —  Timer between the first failed connection and the next attempt.
                                                                                                            Valid range: 0–20160 (0  —  Retry mode is deactivated)
                                                                                                           Default value: 15
                                                                                                    <Timer_n>  —  Timer between the nth failed connection attempt and the (n+1)th
                                                                        connection (n8).
                                                                                                            Valid range: 1–20160
                                                                                                           Default value: 15
                                                                                                                   <Timer_2>  =  60 (Time to wait after second failed connection attempt.)
                                                                                                                   <Timer_3>  =  240 (Time to wait after third failed connection attempt.)
                                                                                                                   <Timer_4>  =  960 (Time to wait after fourth failed connection attempt.)
                                                                                                                   <Timer_5>  =  2880 (Time to wait after fifth failed connection attempt.)
                                                                                                                   <Timer_6>  =  10080 (Time to wait after sixth failed connection attempt.)
                                                                                                                   <Timer_7>  =  10080 (Time to wait after seventh failed connection attempt.)
                                                                                                                   <Timer_8> not used


                                                          Note:                 The <State>, <Timer_1>, and <Timer_n> parameters are stored in NV without sending
                                                          the &W command. The &F command does not affect these values.


110                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 111

AirVantage Commands


Table 11-2:                AirVantage Device Services command details (Continue                                                                           d)

      Command                                                                                                                        Description

     +WDSE                                                                                                                          Display most recent AirVantage Management Services error

                                                                                                                                    Display the most recent HTTP(S) response received by the device for the package download.

                                                                                                                                    Requirements:
                                                                                                                                                                                        AirVantage Management Services must be activated (See +WDSG on page         112 for
                                                                                                                                                                    details).
                                                                                                                                                                                        Session must be initiated using AT+WDSS=1,1. (See +WDSS on page         117 for details).
                                                                                                                                    SIM card requirement: Not required
                                                                                                                                    Password required: No

                                                                                                                                    Usage:
                                                                                                                                                                        Execution:                                                AT+WDSE
                                                                                                                                                   Response:                                               [+WDSE: <HTTP_Status>]
                                                                                                                                                                                                                    OK
                                                                                                                                                                                         or
                                                                                                                                                                                                                     +CME ERROR: 3
                                                                                                                                                                                                                    (If AirVantage Management services are not in the Activated state.)
                                                                                                                                                   Purpose:                                                                       Display most recent response. (If HTTP /  HTTPS is not yet used, return only
                                                                                                                                                                                                                    OK.)

                                                                                                                                    Parameters:
                                                                                                                                    <HTTP_Status> (Standard HTTP status code)
                                                                                                                                                                                        none  —  No response shown if HTTP/  HTTPS has not yet been used.
                                                                                                                                                                                        Supported statuses:
                                                                                                                                                                                                        1xx Informational:
                                                                                                                                                                                    100 (Continue)                                                                                                                                                                                                                                                                                                                                                                                       101 (Switching protocols)
                                                                                                                                                                                                       2xx Success:
                                                                                                                                                                                    200 (OK)                                                                                                                                                                                                                                                                                                                                                                                                                                                                         201 (Created)
                                                                                                                                                                                    202 (Accepted)                                                                                                                                                                                                                                                                                                                                                                                  203 (Non-authoritative information)
                                                                                                                                                                                    204 (No content)                                                                                                                                                                                                                                                                                                                                                             205 (Reset content)
                                                                                                                                                                                    206 (Partial content)
                                                                                                                                                                                                        3xx Redirection:
                                                                                                                                                                                    300 (Multiple choices)                                                                                                                                                                                                                                                                                   301 (Moved permanently)
                                                                                                                                                                                    302 (Found)                                                                                                                                                                                                                                                                                                                                                                                                                              303 (See other)
                                                                                                                                                                                    304 (Not modified)                                                                                                                                                                                                                                                                                                                                   305 (Use proxy)
                                                                                                                                                                                    307 (Temporary redirect)
                                                                                                                                                                                                       4xx Client Error:
                                                                                                                                                                                    400 (Bad request)                                                                                                                                                                                                                                                                                                                                          401 (Unauthorized)
                                                                                                                                                                                    402 (Payment required)                                                                                                                                                                                                                                                         403 (Forbidden)
                                                                                                                                                                                    404 (Not found)                                                                                                                                                                                                                                                                                                                                                                             405 (Method not allowed)
                                                                                                                                                                                    406 (Not acceptable)                                                                                                                                                                                                                                                                                                 407 (Proxy authentication required)
                                                                                                                                                                                    408 (Request time-out)                                                                                                                                                                                                                                                                  409 (Conflict)
                                                                                                                                                                                    410         (Gone)                                                                                                                                                                                                                                                                                                                                                                                                                                            411         (Length required)
                                                                                                                                                                                    412         (Precondition failed)                                                                                                                                                                                                                                             413         (Request entity too large)
                                                                                                                                                                                    414         (Request URI too large)                                                                                                                                                                                        415 (Unsupported media type)
                                                                                                                                                                                    416         (Requested range not satisfiable)                                                417         (Expectation failed)
                                                                                                                                                                                                       5xx Server Error:
                                                                                                                                                                                    500         (Internal server error)                                                                                                                                                                                                                            501         (Not implemented)
                                                                                                                                                                                    502         (Bad gateway)                                                                                                                                                                                                                                                                                                                              503         (Service unavailable)
                                                                                                                                                                                    504         (Gateway time-out)                                                                                                                                                                                                                                                           505         (HTTP version not supported)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  111

## Page 112

AirPrime EM75xx AT Command Reference


Table 11-2:                AirVantage Device Services command details (Continue                                                                           d)

  Command                                                        Description

  +WDSG                                                          Display AirVantage Management Services status information

                                                                 Display general AirVantage Management Services status details.
                                                                 SIM card requirement: Not required
                                                                 Password required: No

                                                                 Usage:
                                                                                                     Execution:                                                 AT+WDSG
                                                                        Response:                                               +WDSG: <Status>, <Value>
                                                                                                        +WDSG: <Status>, <Value>
                                                                                                        OK
                                                                        Purpose:                                                                       Returns the current <Value>s for <Status>=1 and <Status>=2.
                                                                 Parameters:
                                                                 <Status> (Information type to display)
                                                                                                            0  —  AirVantage Management Services activation state
                                                                                                                    For <Value>=2 and <Value>=3, connection parameters are automatically provi-
                                                                                        sioned and no actions are required by the user.
                                                                                                                    Device is activated (<Value>=3) when a dedicated APN (Access Point Name) is set
                                                                                        manually or automatically in the first session. See +WDSS on page         117 for details.
                                                                                                            1  —  Session and package indication
                                                                 <Value> (Detail for the <Status>)
                                                                                                            For <Status>  =  0:
                                                                                                                    0  —  AirVantage Management Services prohibited. Management Services will never
                                                                                        be activated.
                                                                                                                    1  —  AirVantage Management Services deactivated. Connection parameters to an
                                                                                        AirVantage server must be provisioned.
                                                                                        This is the default state when a device has never been activated (first use of device
                                                                                        services on this device).
                                                                                                                    2  —  AirVantage Management Services must be provisioned. A bootstrap session is
                                                                                        required.
                                                                                                                    3  —  AirVantage Management Services are activated.
                                                                                                            For <Status>  =  1:
                                                                                                                    0  —  No session or package.
                                                                                                                   1  —  A session is under treatment.
                                                                                                                   2  —  A package is available on the server.
                                                                                                                    3  —  A package was downloaded and ready to install.
                                                                                                                    Note: If a package is downloaded unsuccessfully, the <Value> is set to 0. If it
                                                                                        downloads successfully, the <Value> is set to 3.


112                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 113

AirVantage Commands


Table 11-2:                AirVantage Device Services command details (Continue                                                                           d)

   Command                                                            Description

  +WDSI                                                              Activate   /   deactivate AirVantage Management Services unsolicited
                                                                     notifications

                                                                     Activate  /  deactivate specific AirVantage Management Services unsolicited notifications.

                                                                     Requirements:
                                                                                                                  To receive unsolicited notifications, AirVantage Management Services must be
                                                                                      activated (see +WDSG on page         112 for details).
                                                                     SIM card requirement: Not required
                                                                     Password required: No
                                                                     Reset required to apply changes:  No
                                                                     Persistent across power cycles: Yes

                                                                     Usage:
                                                                                                         Execution:                                                AT+WDSI  =  <Level>
                                                                             Response:                                               OK
                                                                             Purpose:                                                                        Activate  /  deactivate identifications as specified by <Level>.
                                                                                                         Query:                                                                                                        AT+WDSI?
                                                                             Response:                                               +WDSI: <Level>]
                                                                                                                OK
                                                                             Purpose:                                                                       Indicate current state (activated  /  deactivated) of indications using the
                                                                                                                <Level> bitmask parameter.
                                                                                                         Query List:                                           AT+WDSI=  ?
                                                                             Purpose:                                                                        Display valid execution format and parameter values.
                                                                     Parameters:
                                                                     <Level> (Unsolicited AirVantage Management Services notifications bit mask)
                                                                                                                  Bit mask indicating which notifications to enable  /  disable entered as integer value
                                                                                                                  Default: 0  = No indications activated
                                                                                                                  Bit value:
                                                                                                                          0  =  Indication deactivated
                                                                                                                          1  =  Indication activated
                                                                                                                  Range: 0–8191. Add the values of each bit listed below. (See +WDSI on page        114 for
                                                                                      <Event> details.)
                                                                                                                          1 (Bit         0)     —  Initialization end indication (<Event> = 0)
                                                                                                                         2 (Bit        1)  —  Server request for user agreement indication (<Event> = 1, 2, 3, 24)
                                                                                                                          4 (Bit         2)  —  Authentication indications (<Event> = 4, 5)
                                                                                                                          8 (Bit         3)  —  Session indication (<Event> = 6, 7, 8)
                                                                                                                          16 (Bit         4)  —  Package download indications (<Event> = 9, 10, 11)
                                                                                                                          32 (Bit         5)  —  Certified downloaded package indication (<Event> = 12, 13)
                                                                                                                          64 (Bit         6)  —  Update indications (<Event> = 14, 15, 16)
                                                                                                                          128 (Bit         7)  —  Fallback indication (<Event> = 17)
                                                                                                                          256 (Bit         8)  —  Download progress indication (<Event> = 18)
                                                                                                                          512 (Bit         9)  —  Memory preemption indication (<Event> = 19)
                                                                                                                         1024 (Bit        10)  —  User PIN request indication for bootstrap (<Event> = 20)
                                                                                                                          2048 (Bit         11)  —  Reserved
                                                                                                                          4096 (Bit         12)  —  Bootstrap event indication (<Event> = 23)


                                                                     Note:               <Level> is stored in NV without sending the &W command. Default value can be
                                                                     restored using &F.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  113

## Page 114

AirPrime EM75xx AT Command Reference


Table 11-2:                AirVantage Device Services command details (Continue                                                                           d)

  Command                                             Description

 +WDSI                                                AirVantage Management Services events   —   Unsolicited notification
 (notification)
                                                      Unsolicited notification received for various AirVantage Management Services events.

                                                      Requirements:
                                                                                                To receive unsolicited notifications, AirVantage Management Services must be
                                                                   activated (see +WDSG on page         112 for details).

                                                      Notification format:
                                                            +WDSI: <Event>[,<Data>]


                                                      Note:                <Event> parameter descriptions below indicate when a <Data> parameter is included
                                                      in the response.

                                                      Examples:
                                                                                                +WDSI: 9,1000
                                                                   Package will be downloaded, size is 1000 bytes
                                                                                                +WDSI: 18,1
                                                                   1% of package has been downloaded
                                                                                                +WDSI: 18, 100
                                                                   Entire package (100%) has been downloaded
                                                                                                +WDSI: 11,2
                                                                   Package download failue due to HTTP(S) error (see +WDSE on page         111 for error
                                                                   values)
                                                      Parameters:
                                                      <Event> (AirVantage Management Services event)
                                                                                                0  —  AirVantage Management Services are initialized and can be used. (Note:
                                                                   Management Services are initialized when the SIM PIN code is entered and a
                                                                   dedicated NAP is configured. See +WDSS on page         117 for details.)
                                                                                                1  —  AirVantage server requests that the device make a connection. The device
                                                                   requests a user agreement to allow the module to make the connection. The response
                                                                   can be sent using +WDSR (see +WDSR on page         116) and this indication can be
                                                                   returned by the device if the user has activated the user agreement for connection
                                                                   (see +WDSC on page         110 for details).
                                                                                                2  —  AirVantage server requests that the device make a package download. The device
                                                                   requests a user agreement to allow the module to make the download. The response
                                                                   can be sent using +WDSR (see +WDSR on page         116) and this indication can be
                                                                   returned by the device if the user has activated the user agreement for download (see
                                                                   +WDSC on page         110 for details).
                                                                                                3  —  Device has downloaded a package. The device requests a user agreement to
                                                                   install the downloaded package. The response can be sent using +WDSR (see
                                                                   +WDSR on page         116) and this indication can be returned by the device if the user has
                                                                   activated the user agreement for install (see +WDSC on page         110 for details).
                                                                                                4  —  Module starts authentication with the server.
                                                                                                5  —  Authentication with the server failed.
                                                                                                6  —  Authentication has succeeded and session with the server has started.
                                                                                                7  —  Session with the server failed.

                                                      (Continued on next page)


114                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 115

AirVantage Commands


Table 11-2:                AirVantage Device Services command details (Continue                                                                           d)

  Command                                                    Description

  +WDSI (notification)                                      AirVantage Management Services events   —   Unsolicited notification
                                                            (continued)

                                                                                                        8  —  Session with the server is finished.
                                                                                                        9  —  Package is available on the server and can be downloaded by the module. A
                                                                           <Data> parameter is returned indicating the package size in kB.

                                                                                                        10  —  Package was successfully downloaded and stored in flash.
                                                                                                        11  —  One of the following issues happened during the package download:
                                                                                                              If the download did not start (a +WDSI <Event>=9 indication has not been
                                                                                  received), there is not enough space in the device to download the package.
                                                                                                               If the download started (a +WDSI <Event>=9 indication has been received), a flash
                                                                                  problem implies that the package has not been saved in the device.
                                                                                                        12  —  Downloaded package is certified to be sent by the AirVantage server.
                                                                                                        13  —  Downloaded package is not certified to be sent by the AirVantage server.
                                                                                                        14  —  Update will be launched.
                                                                                                        15  —  OTA update client has finished unsuccessfully.
                                                                                                        16  —  OTA update client has finished successfully.
                                                                                                        17  —  Reserved
                                                                                                        18  —  Download progress:
                                                                                                               No <Data> parameter  —  Download start
                                                                                                              <Data> parameter  —  Percentage progress
                                                                                                        19–22  —  Reserved
                                                                                                        23  —  Session type (only in LWM2M protocol)
                                                                                                        24  —  AirVantage server requests that the device make a reboot. The device requests a
                                                                           user agreement to allow the module to reboot. The response can be sent using
                                                                           +WDSR (see +WDSR on page         116) and this indication can be returned by the device if
                                                                           the user has activated the user agreement for connection (see +WDSC on page         110
                                                                           for details).
                                                            <Data> (Additional data for specific <Event>s)
                                                                                                        (<Event>  =  5) To be defined
                                                                                                        (<Event>  =  9) Package size:
                                                                                                               Package size in bytes, which will be downloaded
                                                                                                              Preempted DOTA area size needed to download an update package
                                                                                                              If preemption is not made, this parameter is not returned for this event.
                                                                                                              If a reverse package is not downloaded and stored, the preempted area will be
                                                                                  released after the installation.
                                                                                                        (<Event>  =  11) Download failure reason:
                                                                                                               0  =  Insufficient memory in device to save firmware update package. Package was
                                                                                  not downloaded.
                                                                                                              1  =  HTTP/HTTPS error occurred. See +WDSE on page         111 for possible error
                                                                                  values.
                                                                                                               2  =  Corrupted firmware update package, did not store correctly. Reasons include (or
                                                                                  example), mismatched CRCs between actual and expected, or signature check
                                                                                  error.
                                                                                                        (<Event>  =  18) Download progress:
                                                                                                              Integer value (% complete)
                                                                                                        (<Event>  =  23) Session event type:
                                                                                                              0  =  Bootstrap session
                                                                                                               1  =  Device management session


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  115

## Page 116

AirPrime EM75xx AT Command Reference


Table 11-2:                AirVantage Device Services command details (Continue                                                                           d)

  Command                                                        Description

  +WDSR                                                          Reply to AirVantage server request

                                                                 Reply to a user agreement request (see +WDSI on page         114 for details) from the module.
                                                                 SIM card requirement: Required, and PIN         1  /  CHV         1 code must be entered.
                                                                 Password required: No

                                                                 Usage:
                                                                                                     Execution:                                                 AT+WDSR  =  <Reply>[,<Timer>]
                                                                         Response:                                               OK
                                                                         Purpose:                                                                        Send <Reply> to a user agreement request from the module. For specific
                                                                                                         <Reply> types, include a <Timer> to have the module send a new user
                                                                                                         agreement request after the specified delay.
                                                                                                     Query List:                                            AT+WDSR=  ?
                                                                         Purpose:                                                                        Display valid execution format and parameter values.
                                                                 Parameters:
                                                                 <Reply> (Reply type)
                                                                                                             0  —  Reserved for future use
                                                                                                             1  —  Reserved for future use
                                                                                                             2  —  Delay or refuse to download. New user agreement request to be sent by module
                                                                                 after <Timer> minutes:
                                                                                                                    Delay  —  <Timer> must be > 0, or blank (Default 30). New user agreement request
                                                                                         to be sent by module after <Timer> minutes.
                                                                                                                    Refuse  —  <Timer>  =  0. Usage restrictions include:
                                                                                                                             Option available only if OMA DM protocol is used.
                                                                                                                             Not supported for install request (AT+WDSR=5,0). Returns +CME         ERROR: 3
                                                                                                                             Not supported for device reboot request (AT+WDSR=7,0). Returns
                                                                                                 +CME_ERROR:        3
                                                                                                             3  —  Accept the download (download it now)
                                                                                                             4  —  Accept the install (install it now)
                                                                                                             5  —  Delay the install. New user agreement request to be sent by module after <Timer>
                                                                                 minutes.
                                                                                                             6  —  Accept the device reboot (reboot now)
                                                                                                             7  —  Delay the device reboot. New user agreement request to be sent by module after
                                                                                 <Timer> minutes.
                                                                                                             Note: If the module is powered down before a delay (install, download, or reboot)
                                                                                 finishes, the new user agreement request will be returned during the next start up.
                                                                 <Timer> (Interval before new user agreement request to be sent by module)
                                                                                                             Applies to <Reply> types 2, 5, 7
                                                                                                             Valid values:
                                                                                                                     Valid range: 0–1440 (minutes)
                                                                                                                    0  —  If <Reply>  =  2 and OMA DM protocol is used, refuse the user agreement
                                                                                         request.
                                                                                                                    Default (if not specified): 30 (minutes)


116                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 117

AirVantage Commands


Table 11-2:                AirVantage Device Services command details (Continue                                                                           d)

  Command                                                      Description

  +WDSS                                                        Configure   /   connect AirVantage Management Services session

                                                               Configure a dedicated access point name (APN), and initiate a connection to the AirVantage
                                                               server. Also used to activate an automatic registration to the AirVantage server.

                                                               Activating dedicated PDP context:
                                                                                                   If a dedicated NAP has not been defined using this command, and a session is requested
                                                                      (via AT command, or via an SMS notification (SMS only in the OMA DM protocol use
                                                                      case), the module uses an APN that has been defined using AT+CGDCONT to activate
                                                                      the dedicated PDP context. This APN will be recorded to configure the AirVantage
                                                                      server’s APN and it will be used to activate the dedicated PDP context for the next
                                                                      sessions.
                                                                                                  If the PDP context cannot be activated because the AirVantage server’s APN is miscon-
                                                                      figured, the module uses an APN defined using AT+CGDCONT command to activate the
                                                                      dedicated PDP context. However, the initial APN configuration is not erased.
                                                               SIM card requirement: Required, and PIN         1  /  CHV         1 code must be entered.
                                                               Password required: No
                                                               Persistent across power cycles: Yes (<Apn> only)

                                                               Usage:
                                                                                                   Execution (<Mode> = 0):
                                                                                                     AT+WDSS  =  <Mode>,<Apn>[,<User>[,<Pwd>]]
                                                                      Response:                                               OK
                                                                      Purpose:                                                                        Configure the AirVantage server connection.
                                                                                                   Execution (<Mode> = 1):
                                                                                                     AT+WDSS  =  <Mode>,<Action>
                                                                      Response:                                               OK
                                                                      Purpose:                                                                        Connect to  /  disconnect from the AirVantage server
                                                                                                   Query:                                                                                                          AT+WDSS?
                                                                      Response:                                               [+WDSS: 0,<Apn>[,<User>]
                                                                                                     +WDSS: 1,<Action>]
                                                                                                     OK
                                                                      Purpose:                                                                       Return the current AirVantage server configuration details. If no APN has
                                                                                                     been defined, return only OK.
                                                                                                   Query List:                                            AT+WDSS=  ?
                                                                      Purpose:                                                                        Display valid execution format and parameter values.
                                                               Parameters:
                                                               <Mode> (Connection method)
                                                                                                          0  —  PDP context configuration for AirVantage server
                                                                                                          1  —  User-initiated connection to the AirVantage server
                                                               <Apn> (AirVantage server access point name)
                                                                                                          ASCII string
                                                                                                          Max length: 50 characters
                                                                                                          Note: Stored in NV.

                                                               (Continued on next page)


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  117

## Page 118

AirPrime EM75xx AT Command Reference


Table 11-2:                AirVantage Device Services command details (Continue                                                                           d)

   Command                                                                       Description

  +WDSS (continued)                                                             Configure   /   connect AirVantage Management Services session
                                                                                (continued)

                                                                                <User> (AirVantage server APN login)
                                                                                                                              ASCII string
                                                                                                                              Max length: 30 characters
                                                                                                                              Note: Stored in flash without using &W. &F does not affect this parameter.
                                                                                <Pwd> (AirVantage server APN password)
                                                                                                                              ASCII string
                                                                                                                              Max length: 30 characters
                                                                                                                              Note: Stored in flash without using &W. &F does not affect this parameter.
                                                                                <Action> (Connect to  /  disconnect from AirVantage server)
                                                                                                                              0  —  Release connection (Default)
                                                                                                                              1  —  Establish connection


                                                                                Note:                <User> and <Pwd> are stored in flash without sending the &W command. &F does not
                                                                                affect these values. <Apn> is stored in NV.


118                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 119

12: Supported GSM     /     WCDMA AT Commands                                                                                                                                                       12
                                                                              This chapter identifies standard AT commands that are supported by
                                                                              most Sierra Wireless AirPri me devices. These commands:
                                                                                                                                  Control serial communications over an asynchronous interface
                                                                                      (ITU-T Serial Asynchronous  Dialling and Control (Recommen-
                                                                                      dation V.250), available on the International Telecommunication
                                                                                      Union web site, www.itu.int).
                                                                                      See Table         12-1 below.
                                                                                                                                  Control SMS functions for devices on GSM   /  WCDMA networks
                                                                                      (3GPP TS 27.005 , available on the 3GPP web site,
                                                                                      www.3gpp.org)
                                                                                      See Table         12-2 on page         121.
                                                                                                                                       Control devices operating on GSM   /   WCDMA networks (   3GPP TS
                                                                                      27.007, available on the 3GPP web site,  www.3gpp.org)
                                                                                      See Table         12-3 on page         122.
                                                                              The tables below identify whether each command is supported on
                                                                              Sierra Wireless UMTS devices. An “N/A” in the Supported column of
                                                                              the table indicates that the command is related to a feature (such as
                                                                              voice) that is not available on the modems.
                                                                              Commands that are partially supported include descriptions
                                                                              identifying any limitations on command usage. Also, some
                                                                              commands are described in more detail in other chapters  —  the
                                                                              descriptions for these commands link to those detailed entries (for
                                                                              example, &V in Table         12-1 on page         119).

                       Table 12-1:                Supported ITU-T Recommendation V.250 AT commands

                        Command                               Description                                                                                                           Supported
                                                                                                                                                                                    =Yes;  =No

                           &C                                 Set Data Carrier Detected (Received line signal detector) function                                                                 
                                                              mode

                           &D                                 Set Data Terminal Ready function mode                                                                                              

                           &F                                 Set all current parameters to manufacturer’s defaults                                                                              

                           &S                                 Set DSR signal                                                                                                                     

                           &T                                 Auto tests                                                                                                                         

                           &V                                 Return operating mode AT configuration parameters                                                                                  

                           &W                                 Store current parameter to user-defined profile                                                                                    

                           +DR                                V42bis data compression report                                                                                                     

                           +DS                                V42bis data compression                                                                                                            

                           +GCAP                              Request complete TA capabilities list                                                                                              

                           +GMI                               Request manufacturer identification                                                                                                


Rev 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                        Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         119

## Page 120

AirPrime EM75xx AT Command Reference


Table 12-1:                Supported ITU-T Recomm                                           endation V.250 AT commands (Co                                  ntinued)

                            Command                                   Description                                                                                                                          Supported
                                                                                                                                                                                                           =Yes;  =No

                              +GMM                                    Request TA model identification                                                                                                                     

                              +GMR                                    Request TA revision identification                                                                                                                  

                              +GOI                                    Request global object identification                                                                                                                

                              +GSN                                    Request TA serial number identification                                                                                                             

                              +ICF                                    Set TE-TA control character framing                                                                                                                 

                              +IFC                                    Set TE-TA local data flow control                                                                                                                   

                              +ILRR                                   Set TE-TA local rate reporting mode                                                                                                                 

                              +IPR                                    Set fixed local rate                                                                                                                                

                              A                                       Answer incoming call                                                                                                                                

                              A/                                      Re-issues last AT command given                                                                                                                     

                              D                                       Dial                                                                                                                                                

                              D><MEM><N>                              Originate call to phone number in memory <MEM>                                                                                                      

                              D><N>                                   Originate call to phone number in current memory                                                                                                    

                              D><STR>                                 Originate call to phone number in memory which corresponds to                                                                                       
                                                                      alphanumeric field <STR>

                              DL                                      Redial last telephone number used                                                                                                                   

                              E                                       Set command echo mode                                                                                                                               

                              H                                       Disconnect existing connections                                                                                                                     

                              I                                       Display product identification information                                                                                                          

                              L                                       Set monitor speaker loudness                                                                                                                        

                              M                                       Set monitor speaker mode                                                                                                                            

                              O                                       Switch from command mode to data mode                                                                                                               

                              P                                       Select pulse dialing                                                                                                                                

                              Q                                       Set Result code presentation mode                                                                                                                   

                              S0                                      Set number of rings before automatically answering the call                                                                                         

                              S10                                     Set disconnect delay after indicating the absence of data carrier                                                                                   

                              S3                                      Set command line termination character                                                                                                              

                              S4                                      Set response formatting character                                                                                                                   

                              S5                                      Set command line editing character                                                                                                                  

                              S6                                      Set pause before blind dialing                                                                                                                      

                              S7                                      Set number of seconds to wait for connection completion                                                                                             


120                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 121

Table 12-1:                Supported ITU-T Recomm                                           endation V.250 AT commands (Co                                ntinued)

                           Command                                   Description                                                                                                                       Supported
                                                                                                                                                                                                       =Yes;  =No

                              S8                                     Set number of seconds to wait when comma dial modifier used                                                                                     

                              T                                      Select tone dialing                                                                                                                             

                              V                                      Set result code format mode                                                                                                                     

                              X                                      Set connect result code format and call monitoring                                                                                              

                              Z                                      Set all current parameters to user-defined profile                                                                                              

                         Table 12-2:                Supported 27.005 AT commands

                           Command                                   Description                                                                                                                       Supported
                                                                                                                                                                                                       =Yes;  =No

                              +CBM                                   Cell broadcast message directly displayed                                                                                                       

                              +CBMI                                  Cell broadcast message stored in memory at specified <index>                                                                                     
                                                                     location

                              +CDS                                   SMS status report after sending a SMS                                                                                                           

                              +CDSI                                  Incoming SMS status report                                                                                                                      

                              +CMGC                                  Send command                                                                                                                                    

                              +CMGD                                  Delete message                                                                                                                                  

                              +CMGF                                  Message format                                                                                                                                  

                              +CMGL                                  List messages                                                                                                                                   

                              +CMGR                                  Read message                                                                                                                                    

                              +CMGS                                  Send message                                                                                                                                    

                              +CMGW                                  Write message to memory                                                                                                                         

                              +CMMS                                  More messages to send                                                                                                                           

                              +CMNA                                  New message acknowledgement to ME/TA                                                                                                            

                              +CMS ERROR:                            SMS error (mobile or network error)                                                                                                             
                               <err>

                              +CMSS                                  Send message from storage                                                                                                                       

                              +CMT                                   Incoming message directly displayed                                                                                                             

                              +CMTI                                  Incoming message stored in <mem> (“SM” - (U)SIM message                                                                                         
                                                                     storage) at location <index>

                              +CNMA                                  New message acknowledgement to mobile equipment                                                                                                 

                              +CNMI                                  New message indications to TE                                                                                                                   

                              +CPMS                                  Preferred message storage                                                                                                                       

                              +CRES                                  Restore settings                                                                                                                                 


Rev 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                        Proprietary  and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           121

## Page 122

AirPrime EM75xx AT Command Reference


                          Table 12-2:                Supported 27.005 AT commands (Continued)

                            Command                                     Description                                                                                                                              Supported
                                                                                                                                                                                                                 =Yes;  =No

                               +CSAS                                    Save settings                                                                                                                                           

                               +CSCA                                    Service center address                                                                                                                                  

                               +CSCB                                    Select cell broadcast message types                                                                                                                     

                               +CSDH                                    Show text mode parameters                                                                                                                               

                               +CSMP                                    Set text mode parameters                                                                                                                                

                               +CSMS                                    Select message service                                                                                                                                  

                          Table 12-3:                Supported 27.007 AT commands

                            Command                                     Description                                                                                                                              Supported
                                                                                                                                                                                                                 =Yes;  =No

                               C                                        ITU T V.24 circuit 109 carrier detect signal behavior command                                                                                       Partial
                                                                        Format
                                                                                                                            C<value>
                                                                        Limitations
                                                                                                                            Default <value> = 2
                                                                                                                            <value> = 2 causes the AT/Data carrier detect pin to ‘wink’
                                                                                 (briefly switch off and on) when data calls end.
                                                                                                                           <value> = 0 or 1 performs as defined in the standard

                               +CACM                                    Accumulated call meter                                                                                                                                  

                               +CACSP                                   Voice Group or Voice Broadcast Call State Attribute Presentation                                                                                      N/A

                               +CAEMLPP                                 eMLPP Priority Registration and Interrogation                                                                                                           

                               +CAHLD                                   Leave an ongoing Voice Group or Voice Broadcast Call                                                                                                  N/A

                               +CAJOIN                                  Accept an incoming Voice Group or Voice Broadcast Call                                                                                                N/A

                               +CALA                                    Alarm                                                                                                                                                 N/A

                               +CALCC                                   List current Voice Group and Voice Broadcast Calls                                                                                                    N/A

                               +CALD                                    Delete alarm                                                                                                                                          N/A

                               +CALM                                    Alert sound mode                                                                                                                                        

                               +CAMM                                    Accumulated call meter maximum                                                                                                                          

                               +CANCHEV                                 NCH Support Indication                                                                                                                                  

                               +CAOC                                    Advice of Charge                                                                                                                                        

                               +CAPD                                    Postpone or dismiss an alarm                                                                                                                          N/A

                               +CAPTT                                   Talker Access for Voice Group Call                                                                                                                    N/A

                               +CAREJ                                   Reject an incoming Voice Group or Voice Broadcast Call                                                                                                N/A

                               +CAULEV                                  Voice Group Call Uplink Status Presentation                                                                                                           N/A


122                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 123

Table 12-3:                Supported 27.007 AT commands (Continued)

                             Command                                     Description                                                                                                                                 Supported
                                                                                                                                                                                                                     =Yes;  =No

                                +CBC                                     Battery charge                                                                                                                                            

                                +CBST                                    Select bearer service type                                                                                                                                

                                +CCCM                                    Current call meter value                                                                                                                                   

                                +CCFC                                    Call forwarding number and conditions                                                                                                                     

                                +CCHC                                    Close logical channel                                                                                                                                     

                                +CCHO                                    Open logical channel                                                                                                                                      

                                +CCLK                                    Clock                                                                                                                                                    N/A

                                +CCUG                                    Closed user group                                                                                                                                         

                                +CCWA                                    Call waiting                                                                                                                                              

                                +CCWE                                    Call Meter maximum event                                                                                                                                   

                                +CDIP                                    Called line identification presentation                                                                                                                    

                                +CDIS                                    Display control                                                                                                                                            

                                +CEER                                    Extended error report                                                                                                                                      

                                +CFUN                                    Set phone functionality                                                                                                                               Partial
                                                                         Format
                                                                                                                            +CFUN = [ <fun> [, <rst>] ]
                                                                         Limitations
                                                                                                                             Valid <fun> values:
                                                                                  ·                      0 (minimum functionality, low power draw)
                                                                                  ·                      1 (full functionality, high power draw)

                                +CGACT                                   PDP context activate or deactivate                                                                                                                        

                                +CGANS                                   Manual response to a network request for PDP context activation                                                                                            

                                +CGATT                                   PS attach or detach                                                                                                                                       

                                +CGAUTO                                  Automatic response to a network request for PDP context                                                                                                    
                                                                         activation

                                +CGCLASS                                 GPRS mobile station class                                                                                                                                 

                                +CGCLOSP                                 Configure local octet stream PAD parameters                                                                                                                

                                +CGCMOD                                  PDP Context Modify                                                                                                                                         

                                +CGDATA                                  Enter data state                                                                                                                                          

                                +CGDCONT                                 Define PDP Context                                                                                                                                        

                                +CGDSCONT                                Define Secondary PDP Context                                                                                                                              

                                +CGEQMIN                                 3G Quality of Service Profile (Minimum acceptable)                                                                                                        

                                +CGEQNEG                                 3G Quality of Service Profile (Negotiated)                                                                                                                


Rev 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                        Proprietary  and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           123

## Page 124

AirPrime EM75xx AT Command Reference


                        Table 12-3:                Supported 27.007 AT commands (Continued)

                           Command                                  Description                                                                                                                       Supported
                                                                                                                                                                                                      =Yes;  =No

                             +CGEQREQ                               3G Quality of Service Profile (Requested)                                                                                                      

                             +CGEREP                                Packet Domain event reporting                                                                                                                  

                             +CGEV                                  GPRS network event indication                                                                                                                  

                             +CGLA                                  Generic UICC logical channel access                                                                                                            

                             +CGMI                                  Request manufacturer identification                                                                                                            

                             +CGMM                                  Request model identification                                                                                                                   

                             +CGMR                                  Request revision identification                                                                                                                

                             +CGPADDR                               Show PDP address                                                                                                                               

                             +CGQMIN                                Quality of Service Profile (Minimum acceptable)                                                                                                

                             +CGQREQ                                Quality of Service Profile (Requested)                                                                                                         

                             +CGREG                                 GPRS network registration status                                                                                                               

                             +CGSMS                                 Select service for MO SMS messages                                                                                                             

                             +CGSN                                  Request product serial number identification                                                                                                   

                             +CGTFT                                 Traffic Flow Template                                                                                                                          

                             +CHLD                                  Call related supplementary services                                                                                                            

                             +CHSA                                  HSCSD non-transparent asymmetry configuration                                                                                                 N/A

                             +CHSC                                  HSCSD current call parameters                                                                                                                 N/A

                             +CHSD                                  HSCSD device parameters                                                                                                                       N/A

                             +CHSR                                  HSCSD parameters report                                                                                                                       N/A

                             +CHST                                  HSCSD transparent call configuration                                                                                                          N/A

                             +CHSU                                  HSCSD automatic user initiated upgrading                                                                                                      N/A

                             +CHUP                                  Hangup call                                                                                                                                    

                             +CIEV                                  Indicator event                                                                                                                                

                             +CIMI                                  Request international mobile subscriber identity                                                                                               

                             +CIND                                  Indicator control                                                                                                                              

                             +CKEV                                  Key press or release event                                                                                                                      

                             +CKPD                                  Keypad control                                                                                                                                  

                             +CLAC                                  List all available AT commands                                                                                                                  

                             +CLAE                                  Language Event                                                                                                                                  

                             +CLAN                                  Set Language                                                                                                                                    


124                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 125

Table 12-3:                Supported 27.007 AT commands (Continued)

                             Command                                    Description                                                                                                                               Supported
                                                                                                                                                                                                                  =Yes;  =No

                                +CLCC                                   List current calls                                                                                                                                       

                                +CLCK                                   Facility lock                                                                                                                                            

                                +CLIP                                   Calling line identification presentation                                                                                                                 

                                +CLIR                                   Calling line identification restriction                                                                                                                  

                                +CLVL                                   Set  /  return internal loudspeaker volume                                                                                                               

                                +CMAR                                   Master Reset                                                                                                                                             

                                +CME ERROR:                             Mobile Termination error result code                                                                                                                     
                                 <err>

                                +CMEC                                   Mobile Termination control mode                                                                                                                          

                                +CMEE                                   Report Mobile Termination error                                                                                                                          

                                +CMER                                   Mobile Termination event reporting                                                                                                                       

                                +CMOD                                   Call mode                                                                                                                                                

                                +CMUT                                   Enable  /  disable uplink voice muting                                                                                                                   

                                +CMUX                                   Multiplexing mode                                                                                                                                        
                                                                                                                                                                                                                      (When MUX
                                                                                                                                                                                                                  mode configured
                                                                                                                                                                                                                           on USB
                                                                                                                                                                                                                         interface.)

                                +CNUM                                   Subscriber number                                                                                                                                        

                                +COLP                                   Connected line identification presentation                                                                                                               

                                +COPN                                   Read operator names                                                                                                                                      

                                +COPS                                   Operator selection                                                                                                                                       

                                +CPAS                                   Phone activity status                                                                                                                                    

                                +CPBF                                   Find phonebook entries                                                                                                                                   

                                +CPBR                                   Read phonebook entries                                                                                                                                   

                                +CPBS                                   Select phonebook memory storage                                                                                                                          

                                +CPBW                                   Write phonebook entry                                                                                                                                    

                                +CPIN                                   Enter PIN                                                                                                                                                

                                +CPINR                                  Remaining PIN retries                                                                                                                                    

                                +CPLS                                   Preferred PLMN list selection                                                                                                                            

                                +CPOL                                   Preferred operator list                                                                                                                                  

                                +CPROT                                  Enter protocol mode                                                                                                                                      

                                +CPUC                                   Price per unit and currency table                                                                                                                        


Rev 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                        Proprietary  and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           125

## Page 126

AirPrime EM75xx AT Command Reference


                          Table 12-3:                Supported 27.007 AT commands (Continued)

                             Command                                      Description                                                                                                                                   Supported
                                                                                                                                                                                                                        =Yes;  =No

                                +CPWC                                     Power class                                                                                                                                                  

                                +CPWD                                     Change password                                                                                                                                              

                                +CR                                       Service reporting control                                                                                                                                    

                                +CRC                                      Cellular result codes                                                                                                                                        

                                +CREG                                     Network registration                                                                                                                                         

                                +CRING                                    Incoming call type                                                                                                                                           

                                +CRLP                                     Radio link protocol                                                                                                                                          

                                +CRMP                                     Ring Melody Playback                                                                                                                                       N/A

                                +CRSL                                     Ringer sound level                                                                                                                                         N/A

                                +CRSM                                     Restricted SIM access                                                                                                                                        

                                +CSCC                                     Secure control command                                                                                                                                       

                                +CSCS                                     Select TE character set                                                                                                                                      

                                +CSDF                                     Settings date format                                                                                                                                       N/A

                                +CSGT                                     Set Greeting Text                                                                                                                                          N/A

                                +CSIL                                     Silence Command                                                                                                                                            N/A

                                +CSIM                                     Generic SIM access                                                                                                                                           

                                +CSNS                                     Single numbering scheme                                                                                                                                      

                                +CSQ                                      Signal quality                                                                                                                                               

                                +CSSN                                     Supplementary service notifications                                                                                                                          

                                +CSTA                                     Select type of address                                                                                                                                       

                                +CSTF                                     Settings time format                                                                                                                                         

                                +CSVM                                     Set Voice Mail Number                                                                                                                                        

                                +CTFR                                     Call deflection                                                                                                                                              

                                +CTZR                                     Time Zone Reporting                                                                                                                                        N/A

                                +CTZU                                     Automatic Time Zone Update                                                                                                                                   

                                +CUSD                                     Unstructured supplementary service data                                                                                                                      

                                +CV120                                    V.120 rate adaptation protocol                                                                                                                               

                                +CVHU                                     Voice Hangup Control                                                                                                                                         

                                +CVIB                                     Vibrator mode                                                                                                                                              N/A

                                D                                         ITU T V.25ter [14] dial command                                                                                                                              


126                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    41111748

## Page 127

Table 12-3:                Supported 27.007 AT commands (Continued)

                                              Command                                                                Description                                                                                                                                                                                                                 Supported
                                                                                                                                                                                                                                                                                                                                                 =Yes;  =No

                                                   D*99#                                                            Sets up a packet data call (PDP context) based on profile ID #1                                                                                                                                                                                     

                                                   D*99***<n>#                                                      Sets up a packet data call (PDP context) based on profile ID #<n>                                                                                                                                                                                   
                                                                                                                    (<n> is the <cid> in the +CGDCONT command)

                                                   +VTD                                                             Tone duration                                                                                                                                                                                                                                       

                                                   +VTS                                                             DTMF and arbitrary tone generation                                                                                                                                                                                                                  

                                                   +WS46                                                            PCCA STD 101 [17] select wireless network                                                                                                                                                                                                            


Rev 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                        Proprietary  and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           127

## Page 128

13: Band Definitions                                                                                                                                                                                                    13
                                                                                      Some commands described in this document include input and/or
                                                                                      output ‘band’ parameters, where the band value is one of the
                                                                                      following:
                                                                                                                                          An enumerated value representing a network technology and
                                                                                              band (Table          13-1)
                                                                                                                                          A 3GPP band number ( Table         13-2 on page         129)


                                                                                      Note:                Band support is product-specific  —  see the device’s Product Specifi-
                                                                                      cation Document or Product Technical Specification for details.


                         Table 13-1:                Band    /    technology enumerations                                                a

                <band>                 Description                      <band>                 Description                    <band>                 Description                     <band>                Description

                       0                      CDMA                            22               WCDMA 800                            42                     LTE B4                          60                   LTE B24

                       2                       Sleep                          25              WCDMA BC3                             43                     LTE B2                          61                   LTE B25

                       5                  CDMA 800                            26               CDMA BC14                            44                     LTE B3                          62                   LTE B26

                       6                 CDMA 1900                            27               CDMA BC11                            45                     LTE B5                          63                   LTE B27

                       7                        HDR                           28              WCDMA BC4                             46                     LTE B6                          64                   LTE B28

                       8                 CDMA 1800                            29              WCDMA BC8                             47                     LTE B8                          65                   LTE B29

                       9                WCDMA IMT                             30                    MF 700                          48                     LTE B9                          66                   LTE B30

                      10                   GSM 900                            31              WCDMA BC9                             49                    LTE B10                          67                   LTE B31

                      11                  GSM 1800                            32               CDMA BC15                            50                    LTE B12                          68                   LTE B32

                      12                  GSM 1900                            33               CDMA BC10                            51                    LTE B14                          69                   LTE B33

                      14                     JCDMA                            34                     LTE B1                         52                    LTE B15                          70                   LTE B34

                      15             WCDMA 1900A                              35                     LTE B7                         53                    LTE B16                          71                   LTE B35

                      16             WCDMA 1900B                              36                   LTE B13                          54                    LTE B18                          72                   LTE B36

                      17                  CDMA 450                            37                   LTE B17                          55                    LTE B19                          73                   LTE B37

                      18                   GSM 850                            38                   LTE B38                          56                    LTE B20                          74                   LTE B39

                      19                        IMT                           39                   LTE B40                          57                    LTE B21                          75             WCDMA BC19

                      20                   HDR 800                            40             WCDMA BC11                             58                    LTE B22                          76                   LTE B41

                      21                  HDR 1900                            41                   LTE B11                          59                    LTE B23

                    a.                          Band values not listed (e.g. 1, 3, 4) are reserved.


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  128

## Page 129

Band Definitions


Table 13-2:                3GPP bands

                                             Frequency bands (MHz)                                                                                              Frequency bands (MHz)

  Band              Ty  p  e                    Rx                                   Tx                            Band               Ty  p  e                     Rx                                   Tx

       1             Mid                1920–1980                            2110–2170                                24                Mid             1626.5–1660.5                           1525–1559

       2             Mid                1850–1910                            1930–1990                                25                Mid                1850–1915                            1930–1995

       3             Mid                1710–1785                            1805–1880                                26                Low                   859–894                             814–849

       4             Mid                1710–1755                            2110–2155                                27                   -                 Reserved                             Reserved

       5             Low                   824–849                              869–894                               28                Low                   758–803                             703–748

       6             Low                   830–840                              875–885                               29                   -                 Reserved                             Reserved

       7            High                2500–2570                            2620–2690                                30               High                2350–2360                            2305–2315

       8             Low                   880–915                              925–960                            31–32                   -                 Reserved                             Reserved

       9             Mid             1749.9–1784.9                        1844.9–1879.9                               33                Mid                1900–1920                            1900–1920

      10             Mid                1710–1770                            2110–2170                                34                Mid                2010–2025                            2010–2025

      11             Mid             1427.9–1447.9                        1475.9–1495.9                               35                Mid                1850–1910                            1850–1910

      12             Low                   699–716                              729–746                               36                Mid                1930–1990                            1930–1990

      13             Low                   777–787                              746–756                               37                Mid                1910–1930                            1910–1930

      14             Low                   788–798                              758–768                               38               High                2570–2620                            2570–2620

      15                -                 Reserved                             Reserved                               39                Mid                1880–1920                            1880–1920

      16                -                 Reserved                             Reserved                               40               High                2300–2400                            2300–2400

      17             Low                   704–716                              734–746                               41               High                2496–2690                            2496–2690

      18             Low                   815–830                              860–875                               42               High                3400–3600                            3400–3600

      19             Low                   830–845                              875–890                               43               High                3600–3800                            3600–3800

      20             Low                   832–862                              791–821                            44–45                   -                 Reserved                             Reserved

      21             Mid             1447.9–1462.9                        1495.9–1510.9                               46               High                5150–5925                            5150–5925

      22                -                 Reserved                             Reserved                            47–65                   -                 Reserved                             Reserved

      23             Mid                2000–2020                            2180–2200                                66                Mid                2110–2200                            1710–1780


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                 Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  129

## Page 130

14: ASCII Table                                                                                                                                                                                                                                         14


                             Table 14-1:               ASCII values

                                   Char                 Dec              Hex                Char                 Dec              Hex                Char                 Dec              Hex                Char                 Dec              Hex

                                      NUL                  0               00                   SP                 32               20                    @                 64               40                      ‘               96               60
                                      SOH                  1               01                      !               33               21                     A                65               41                     a                97               61
                                      STX                  2               02                     “                34               22                     B                66               42                     b                98               62
                                      ETX                  3               03                     #                35               23                     C                67               43                     c                99               63
                                      EOT                  4               04                     $                36               24                     D                68               44                     d               100               94
                                      ENQ                  5               05                     %                37               25                     E                69               45                     e               101               95
                                      ACK                  6               06                     &                38               26                     F                70               46                      f              102               96
                                      BEL                  7               07                      ’               39               27                     G                71               47                     g               103               97
                                       BS                  8               08                      (               40               28                     H                72               48                     h               104               98
                                       HT                  9               09                      )               41               29                      I               73               49                      i              105               99
                                        LF                10               0A                      *               42               2A                     J                74               4A                      j              106               6A
                                        VT                11               0B                     +                43               2B                     K                75               4B                     k               107               6B
                                        FF                12               0C                      ,               44               2C                     L                76               4C                      l              108               6C
                                       CR                 13               0D                      -               45               2D                     M                77               4D                     m               109               6D
                                       SO                 14               0E                      .               46               2E                     N                78               4E                     n               11  0             6E
                                        SI                15               0F                      /               47               2F                     O                79               4F                     o               111               6F
                                      DLE                 16               10                     0                48               30                     P                80               50                     p               11  2             70
                                      XON                 17               11                     1                49               31                     Q                81               51                     q               11  3             71
                                      DC2                 18               12                     2                50               32                     R                82               52                      r              11  4             72
                                    XOFF                  19               13                     3                51               33                     S                83               53                     s               11  5             73
                                      DC4                 20               14                     4                52               34                     T                84               54                      t              11  6             74
                                      NAK                 21               15                     5                53               35                     U                85               55                     u               11  7             75
                                      SYN                 22               16                     6                54               36                     V                86               56                     v               11  8             76
                                      ETB                 23               17                     7                55               37                    W                 87               57                     w               11  9             77
                                      CAN                 24               18                     8                56               38                     X                88               58                     x               120               78
                                       EM                 25               19                     9                57               39                     Y                89               59                     y               121               79
                                      SUB                 26               1A                      :               58               3A                     Z                90               5A                     z               122               7A
                                      ESC                 27               1B                      ;               59               3B                      [               91               5B                      {              123               7B
                                        FS                28               1C                     <                60               3C                      \               92               5C                      |              124               7C
                                       GS                 29               1D                      =               61               3D                      ]               93               5D                      }              125               7D
                                       RS                 30               1E                     >                62               3E                     ^                94               5E                     ~               126               7E
                                       US                 31               1F                     ?                63               3F                     _                95               5F                  DEL                127               7F


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  130

## Page 131

Index (AT commands)


              A                                                                                              +CDIP, called line identification presentation, 123
                                                                                                             +CDIS, display control, 123
              A, answer incoming call, 120                                                                   +CDS, SMS status report after sending a SMS, 121
              A/, re-issue last AT command, 120                                                              +CDSI, incoming SMS status report, 121
              !ANTSEL, set/query external antenna select configura-                                          +CEER, extended error report, 123
              tion, 20                                                                                       +CFUN, set phone functionality, 123
                                                                                                             +CGACT, PDP context activate or deactivate , 123
              B                                                                                              +CGANS, manual response to network request for PDP
                                                                                                             context activation, 123
              !BAND, set/query frequency bands, 22                                                           +CGATT, PS attach or detach, 123
              !BCFWUPDATESTATUS, report status of last firmware                                              +CGAUTO, automatic response to network request for
              update attempt, 53                                                                             PDP context activation, 123
              !BOOTHOLD, reset modem and wait for f/w download,                                              +CGCLASS, GPRS mobile station class, 123
              24                                                                                             +CGCLOSP, configure local octet stream PAD parame-
                                                                                                             ters, 123
                                                                                                             +CGCMOD, PDP context modify, 123
              C                                                                                              +CGDATA, enter data state, 123
                                                                                                             +CGDCONT, define PDP context, 123
              &C, set data carrier detected, 119                                                             +CGDSCONT, define secondary PDP context, 123
              C, ITU T v.24 circuit 109 carrier detect signal behavior                                       +CGEQMIN, 3G QoS profile (minimum acceptable), 123
              command, 122                                                                                   +CGEQNEG, 3G QoS profile (negotiated), 123
              +CACM, accumulated call meter, 122                                                             +CGEQREQ, 3G QoS profile (requested), 124
              +CACSP, voice group or voice broadcast call state attrib-                                      +CGEREP, packet domain event reporting, 124
              ute presentation, 122                                                                          +CGEV, GPRS network event indication, 124
              +CAEMLPP, eMLPP priority registration and interroga-                                           +CGIEV, indicator event, 124
              tion, 122                                                                                      +CGLA, generic UICC logical channel access , 124
              +CAHLD, leave an ongoing voice group or voice broad-                                           +CGMI, request manufacturer identification, 124
              cast call, 122                                                                                 +CGMM, request model identification, 124
              +CAJOIN, accept incoming voice group or voice broad-                                           +CGMR, request revision identification, 124
              cast call, 122                                                                                 +CGPADDR, show PDP address, 124
              +CALA, alarm, 122                                                                              +CGQMIN, QoS profile (minimum acceptable), 124
              +CALCC, list current voice group and voice broadcast                                           +CGQREQ, QoS profile (requested), 124
              call, 122                                                                                      +CGREG, GPRS network registration status, 124
              +CALD, delete alarm, 122                                                                       +CGSMS, select service for MO SMS messages , 124
              +CALM, alert sound mode, 122                                                                   +CGSN, request product serial number identification,
              +CAMM, accumulated call meter maximum , 122                                                    124
              +CANCHEV, NCH support indication, 122                                                          +CGTFT, traffic flow template, 124
              +CAOC, advice of charge, 122                                                                   +CHLD, call-related supplementary services, 124
              +CAPD, postpone or dismiss an alarm, 122                                                       +CHSA, HSCSD non-transparent asymmetry configura-
              +CAPTT, talker access for voice group call, 122                                                tion, 124
              +CAREJ, reject incoming voice group or voice broadcast                                         +CHSC, HSCSD current call parameters, 124
              call, 122                                                                                      +CHSD, HSCSD device parameters, 124
              +CAULEV, voice group call uplink status presentation,                                          +CHSR, HSCSD parameters report, 124
              122                                                                                            +CHST, HSCSD transparent call configuration, 124
              +CBC, battery charge, 123                                                                      +CHSU, HSCSD automatic user initiated upgrading, 124
              +CBM, cell broadcast message directly displayed, 121                                           +CHUP, hangup call, 124
              +CBMI, cell broadcast message stored in memory at                                              +CIMI, request international mobile subscriber identity,
              specified location, 121                                                                        124
              +CBST, select bearer service type, 123                                                         +CIND, indicator control, 124
              +CCCM, current call meter value, 123                                                           +CKEV, key press or release event, 124
              +CCFC, call forwarding number and conditions, 123                                              +CKPD, keypad control, 124
              +CCHC, close logical channel, 123                                                              +CLAC, list all available AT commands, 124
              +CCHO, open logical channel, 123                                                               +CLAE, language event, 124
              +CCLK, clock, 123                                                                              +CLAN, set language, 124
              +CCUG, closed user group, 123                                                                  +CLCC, list current calls, 125
              +CCWA, call waiting, 123                                                                       +CLCK, facility lock, 125
              +CCWE, call meter maximum event, 123                                                           +CLIP, calling line identification presentation, 125


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  131

## Page 132

AirPrime EM75xx AT Command Reference


+CLIR, calling line identification restriction, 125                                                     +CSDH, show text mode parameters, 122
+CLVL, sets/returns internal loudspeaker volume, 125                                                    +CSGT, set greeting text, 126
+CMAR, master reset, 125                                                                                +CSIL, silence command, 126
+CME ERROR, mobile termination error result code , 125                                                  +CSIM, generic SIM access, 126
+CMEC, mobile termination control mode, 125                                                             +CSMP, set text mode parameters, 122
+CMEE, report mobile termination error, 125                                                             +CSMS, select message service, 122
+CMER, mobile termination event reporting, 125                                                          +CSNS, single numbering scheme, 126
+CMGC, send command, 121                                                                                +CSQ, signal quality, 126
+CMGD, delete message, 121                                                                              +CSSN, supplementary service notifications, 126
+CMGF, message format, 121                                                                              +CSTA, select type of address, 126
+CMGL, list messages, 121                                                                               +CSTF, settings time format, 126
+CMGR, read message, 121                                                                                +CSVM, set voice mail number, 126
+CMGS, send message, 121                                                                                +CTFR, call deflection, 126
+CMGW, write message to memory, 121                                                                     +CTZR, time zone reporting, 126
+CMMS, more messages to send, 121                                                                       +CTZU, automatic time zone update, 126
+CMNA, new message acknowledgement to ME/TA, 121                                                        +CUSD, unstructured supplementary service data, 126
+CMOD, call mode, 125                                                                                   !CUSTOM, customization settings, 25
+CMS ERROR, SMS error (mobile or network error), 121                                                           CFUNPERSISTEN, AT+CFUN setting persists across
+CMSS, send message from storage , 121                                                                                       power cycle?, 25
+CMT, incoming message directly displayed, 121                                                                 CSVOICEREJECT, enable incoming voice call pages ig-
+CMTI, incoming message stored at specific memory location,                                                                  nore, 25
121                                                                                                            FASTENUMEN, Enable  /  disable fast enumeration, 26
+CMUT, enables/disables uplink voice muting, 125                                                               GPIOSARENABLE, Control SAR backoff by GPIOs or by
+CMUX, multiplexing mode, 125                                                                                                AT commands, 26
+CNMA, new message acknowledgement to ME, 121                                                                  GPSENABLE, enable GPS, 26
+CNMI, new message indications to TE, 121                                                                      GPSLPM, enable GPS in low power mode, 26
+CNUM, subscriber number, 125                                                                                  GPSLPM, enable low power mode GPS, 26
+COLP, connected line identification presentation, 125                                                         GPSREFLOC, enable GPS location reporting, 26
+COPN, read operator names, 125                                                                                GPSSEL, select GPS antenna type, 26
+COPS, operator selection, 125                                                                                 IPV6ENABLE, enable  /  disable IPV6 support, 26
+CPAS, phone activity status, 125                                                                              NETWORKNAMEFMT, set MBIM provider name format for
+CPBR, read phonebook entries, 125                                                                                           vanui (roaming), 27
+CPBS, select phonebook memory storage, 125                                                                    PCSCDISABLE, set PCSC functionality, 27
+CPBW, write phonebook entry, 125                                                                              QMIDETACHEN, Enable  /  disable QMI NAS, 27
+CPFB, find phonebook entries, 125                                                                             REL8FASTDORMDIS, Enable  /  disable Release 8 fast dor-
+CPIN, enter PIN, 125                                                                                                        mancy feature, 27
+CPINR, remaining PIN retries, 125                                                                             RRCREL7CAPDIS, Configure RRC Release 7 capability,
+CPLS, Preferred PLMN list selection , 125                                                                                   27
+CPMS, preferred message storage, 121                                                                          SIMHOTSWAPDIS, Configure SIM hotswap feature, 27
+CPOL, preferred operator list, 125                                                                            SIMLPM, set default low power mode SIM power state, 27
+CPROT, enter protocol mode, 125                                                                               SINGLEAPNSWITCH, device behaviour when APN details
+CPUC, price per unit and currency table, 125                                                                                change, customize, 27
+CPWC, power class, 126                                                                                        SKUID, set device SKU ID, 27
+CPWD, change password, 126                                                                                    UIM2ENABLE, Enable  /  disable UIM2 slog support , 27
+CR, service reporting control, 126                                                                            USBSERIALENABLE, use IMEI as USB serial number, 28
+CRC, cellular result code, 126                                                                                WAKEHOSTEN, Host wake-up method, enable / disable ,
+CREG, network registration, 126                                                                                             28
+CRES, restore settings, 121                                                                            !CUSTOM, customization settings, set/query, 75
+CRING, incoming call type, 126                                                                         +CV120, v.120 rate adaption protocol, 126
+CRLP, radio link protocol, 126                                                                         +CVHU, voice hangup control, 126
+CRMP, ring melody playback, 126                                                                        +CVIB, vibrator mode, 126
+CRSL, ringer sound level, 126
+CRSM, restricted SIM access, 126                                                                       D
+CSAS, save settings, 122
+CSCA, service center address, 122                                                                      &D, set DTR function mode, 119
+CSCB, select cell broadcast message type, 122                                                          D, dial, 120
+CSCC, secure control command, 126                                                                      D, ITU T V.25ter dial command, 126
+CSCS, select TE character set, 126                                                                     D’99’’’<n>#, set up packet data call based on profile ID #<n>,
+CSDF, settings date format, 126                                                                        127


132                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41111748

## Page 133

Index    —    AT Commands


D’99#, set up packet call based on profile ID #1, 127                                                  !GPSMOMETHOD, query/set GPS MO method , 84
D><MEM><N>, originate call to phone number in memory ,                                                 !GPSNMEACONFIG, enable/set NMEA data output rate, 85
120                                                                                                    !GPSNMEASENTENCE, set/get NMEA sentence type , 86
D><N>, originate call to phone number in current memory,                                               !GPSPORTID, query/set TCP/IP port ID, 87
120                                                                                                    !GPSSATINFO, request satellite information , 88
D><STR>, originate call to phone number corresponding to a/                                            !GPSSTATUS, request position fix session status, 80, 89, 93
n field, 120                                                                                           !GPSSUPLURL, query/set SUPL server URL, 90
!DACGPSCTON, return CtoN and frequency measurement,                                                    !GPSSUPLVER, query/set SUPL server version , 91
60                                                                                                     !GPSTRACK, initiate multiple-fix tracking session, 92, 93
!DACGPSMASKON, set CGPS IQ log mask , 60                                                               +GSN, request TA serial number identification, 120
!DACGPSSTANDALONE, enter/exit StandAlone (SA) RF                                                       !GSTATUS, return operational status, 30
mode, 61
!DACGPSTESTMODE, start  /  stop CGPS diagnostic task , 61                                              H
!DAFTMACT, put modem into FTM mode, 8, 62
!DAFTMDEACT, put modem into online mode, 62                                                            H, disconnect existing connections, 120
!DAGFTMRXAGC, get FTM Rx AGC on Primary or Diversity                                                   !HOSTDEVINFO, set  /  report host device details, 99
path, 63                                                                                               !HWID, display hardware version, 30
!DALGRXAGC, return Rx AGC (LTE mode), 64
!DALGTXAGC, return Tx AGC (LTE mode), 65
!DALTXCONTROL, configure LTE Tx parameters, 67                                                         I
!DAOFFLINE, place modem offline, 68
!DARCONFIG, set band and channel, 69                                                                   I, display product identification information, 120
!DARCONFIGDROP, drop radio configurations, 70                                                          +ICF, set TE-TA control character framing, 120
!DATALOOPBACK, enable / disable and configure loopback                                                 +IFC, set TE-TA local data flow control, 120
mode, 28                                                                                               +ILRR, set TE-TA local rate reporting mode, 120
!DAWTXCONTROL, configure WCDMA Tx parameters, 70                                                       !IMPREF, query  /  set Image management preferences, 31
DL, redial last phone number used, 120                                                                 !IMSTESTMODE, enable  /  disable IMS test mode, 100
+DR, V42bis compression report, 119                                                                    +IPR, set fixed local rate, 120
+DS, V42bis data compress, 119
E                                                                                                      L

                                                                                                       L, set monitor speaker loudness, 120
E, set command echo mode, 120                                                                          !LTECA, enable / disable LTE CA, or display supported LTE
!ENTERCND, enable protected command access, 8, 16                                                      CA pairs, 33
!ERR, display diagnostic information, 54                                                               !LTEINFO, display LTE network information, 35
                                                                                                       !LTERXCONTROL, enable/disable LTE Rx diversity during
F                                                                                                      CA, 56


&F, set current parameters to defaults, 119                                                            M
G                                                                                                      M, set monitor speaker mode, 120
                                                                                                       !MAXPWR, query/set maximum Tx power for specific band ,
+GCAP, Request complete TA capabilities list, 119                                                      103
!GCCLR, clear crash dump data, 54
!GCDUMP, display crash dump data, 55                                                                   N
!GCFEN, enable/disable GCF test mode, 29
!GETBAND, return current active band , 29                                                              !NVBACKUP, back up device configuration , 72
+GMI, request manufacturer identification , 119                                                        !NVENCRYPTIMEI, write IMEI to modem, 37
+GMM, request TA model identification, 120                                                             !NVPLMN, provision  /  dispaly PLMN list for Network Personal-
+GMR, request TA revision identification, 9, 120                                                       ization, 38
+GOI, request global object identification, 120
!GPSAUTOSTART, configure GPS auto-start features, 76                                                   O
!GPSCLRASSIST, clear selected GPS assistance data , 78
!GPSCOLDSTART, clear all GPS assistance data , 78, 79                                                  O, switch from command mode to data mode, 120
!GPSEND, end active position fix session, 79, 93                                                       !OSINFO, set / report host device operating system informa-
!GPSFIX, initiate GPS position fix, 80, 92, 93, 95                                                     tion, 101
!GPSLBSAPN, set GPS LBS APNs, 81
!GPSLOC, return last know modem location, 80, 83, 93


Rev. 2 Jan.19                                                                                                                                                                                                                                                                                                                                                                       Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      133

## Page 134

AirPrime EM75xx AT Command Reference


 P                                                                                                                    T

 P, select pulse dialing, 120                                                                                         &T, auto tests, 119
 !PCINFO, return power control status information, 39                                                                 T, select tone dialing, 121
 !PCOFFEN, query/set Power Off Enable state, 40                                                                       !TMSTATUS, report thermal mitigation tatus, 46
 !PCTEMP, return current temperature information, 40
 !PCTEMPLIMITS, query/set temperature state limits, 41                                                                U
 !PCVOLT, return current power supply voltage information , 42
 !PCVOLTLIMITS, query/set power supply voltage state limits,                                                          !UIMS, select SIM interface, 97
 43                                                                                                                   !USBCOMP, query/set USB interface configuration, 47
 !PRIID, query/set PRI part number and revision, 44                                                                   !USBINFO, return information from active USB descriptor, 48
                                                                                                                      !USBPID, query/set USB descriptor product ID, 49
 Q                                                                                                                    !USBSPEED, query/set USB speed, 50

 Q, set result code presentation mode, 120                                                                            V

 R                                                                                                                    &V, return AT configuration parameters, 51, 119
                                                                                                                      V, set result code format mode, 121
 !RESET, reset the modem, 44                                                                                          +VTD, tone duration, 127
 !RMARESET, restore device to saved restore point, 74                                                                 +VTS, DTMF and arbitrary tone generation, 127
 !RXDEN, enable/disable WCDMA  /  LTE  /  TD-SCDMA Rx diversi-
 ty, 57                                                                                                               W

 S                                                                                                                    &W, Store parameter to user-defined profile, 119
                                                                                                                      +WANT, enable GNSS antenna power, 93
 &S, set DSR signal, 119                                                                                              +WDSC, configure AirVantage Management Services, 109
 S0, set number of rings before auto-answer, 120                                                                      !WDSE, display last AirVantage Management Services error ,
 S10, set disconnect delay after indicating absence of data carri-                                                    111
 er, 120                                                                                                              +WDSG, display AirVantage Management Services status , 112
 S3, set command line termination character, 120                                                                      +WDSI, activate / deactivateAirVantage Management Services
 S4, set response formatting character, 120                                                                           unsolicited notifications, 113
 S5, set command line editing character, 120                                                                          +WDSI, AirVantage Management Services event, unsolicited
 S6, set pause before blind dialing, 120                                                                              notification, 114
 S7, set number of seconds to wait for connection completion,                                                         +WDSR, reply to AirVantage server request, 116
 120                                                                                                                  +WDSS, AirVantage Management Services session configure  /
 S8, set number of seconds to wait when comma dial modifier                                                            connect, 117
 used, 121                                                                                                            +WS46, PCCA STD 101 select wireless network, 127
 !SARBACKOFF, query/set offset from max Tx power, 104
 !SARINTGPIOMODE, query/set default pull mode for SAR GPI-                                                            X
 Os, 105
 !SARSTATE, query/set SAR backoff state, 106                                                                          X, set connect result code format and call monitoring , 121
 !SARSTATEDFLT, query/set default SAR backoff state, 107
 !SCACT, activate  /  deactivate data connection, 45
 !SETCND, set AT command password, 17                                                                                 Z

                                                                                                                      Z, set all current parameters to user-defined profile, 121


134                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41111748

## Page 135

Index


                 Symbols                                                                                                                 set,  22
                                                                                                                                 boot and hold. See bootloader.
                 +++,  9                                                                                                         bootloader
                                                                                                                                         wait for firmware update,  24
                 Numerics                                                                                                        bootup time, return,  30


                 3GPP                                                                                                            C
                         27.005 commands, list,  121
                         27.007 commands, list,  122                                                                             +CFUN persistence, customization,  25
                                                                                                                                 channel
                                                                                                                                         set,  69
                 A                                                                                                               channel number
                 AGC                                                                                                                     current GSM, return,  30
                         read Rx AGC in dBm for CDMA and WCDMA                                                                           current WCDMA, return,  30
                                           modes,  100                                                                           command access password,  8
                         Rx value (LTE), return,  64                                                                             control plane, GPS MO method,  84
                         Tx value (LTE), return,  65                                                                             crash data
                 airplane mode. See Low Power Mode                                                                                       display,  55
                 AirVantage                                                                                                      crash dump data, clear,  54
                         Management Services                                                                                     CtoN, return measurement,  60
                                  configure,  109                                                                                customization
                                  error, display most recent,  111                                                                       modem functions,  25

                                  session, configure  /  connect,  117
                                  status, display,  112                                                                          D
                                  unsolicited notifications, activate,  113
                         Management Services, unsolicited notifications,                                                         data connection, activate  /  deactivate,  45
                                           114                                                                                   device behaviour when APN details change, customize,
                         Server                                                                                                  27
                                  reply to server request,  116                                                                  device, back up configurations,  72
                 antenna                                                                                                         diagnostic
                         select configuration, external,  20                                                                             commands, list,  52
                 ASCII table,  130                                                                                                       information, display,  54
                 AT command parameters, display,  51                                                                             diversity, receive, enable/disable,  56,  57
                 AT commands                                                                                                     DM
                         3GPP 27.005 commands, list,  121                                                                                host device details,  99
                         3GPP 27.007 commands, list,  122                                                                                host device operating system information,  101
                         access, password,  8                                                                                    document
                         GPS command error codes,  93,  95                                                                               format conventions,  14
                         guard timing, escape sequence,  9
                         ITU-T V.250 commands, list,  119                                                                        E
                         password commands,  15,  18,  108
                         password protected, access,  16                                                                         error conditions, display log,  54
                         password, changing,  17                                                                                 escape sequence guard time,  9
                         timing, entry,  8
                 B                                                                                                               F

                 backup device configuration,  72                                                                                factory test mode. See FTM.
                 band                                                                                                            fast enumeration, enable  /  disable,  26
                         current active band, return,  29                                                                        firmware
                         current GSM, return,  30                                                                                        update, wait in bootloader mode,  24
                         current WCDMA, return,  30                                                                              firmware update, status of last attempt ,  53
                         set,  69                                                                                                firmware, upgrading,  9
                 bands                                                                                                           flight mode. See Low Power Mode
                         available,  22                                                                                          format
                         current,  22                                                                                                    documentation conventions,  14


Rev. 2  Jan.19                                                                                                                                                                                                                                                                                                                                                                    Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  135

## Page 136

AirPrime EM75xx AT Command Reference


 frequency bands. See bands.                                                                                                         SUPL server version, query/set,  91
 FTM                                                                                                                                 support, customization,  26
         activate FTM modem mode,  62                                                                                                time reference, clear,  78
         deactivate FTM modem mode,  62,  63                                                                                         time, last fix,  83
                                                                                                                                     tracking (multiple fix) session, initiate,  92
 G                                                                                                                                   uncertainty, last fix,  83
                                                                                                                                     velocity, last fix,  83
 GCF testing                                                                                                                GSM
         test mode, enable/disable,  29                                                                                              Algorithm and Authenticate, enable/disable,  27
 Global Certification Forum testing. See GCF testing.                                                                       guard time, AT escape sequence,  9
 GMM state, return,  30
 GNSS                                                                                                                       H
         antenna power, enable,  93
 Gobi Image Management                                                                                                      hardware version, display,  30
         preferences, set,  31                                                                                              high speed USB, set,  50
 GPIO                                                                                                                       Host wake-up method, enable  /  disable,  28
         SAR interrupt, pull mode (default),  105
 GPS                                                                                                                        I
         accuracy, configure,  76
         almanac data, clear,  78                                                                                           IMEI
         altitude, last fix,  83                                                                                                     using as serial number,  28
         assistance data                                                                                                             write to modem, unencrypted,  37
                  clear all,  79                                                                                            IPV6 support, enable  /  disable,  26
                  clear specific,  78                                                                                       ITU-T V.250 commands, list,  119
         AT command error codes,  93,  95
         auto-start features, configure,  76
         command list,  12,  13,  75                                                                                        L
         enter  /  exit StandAlone (SA) RF mode,  61
         ephemeris data, clear,  78                                                                                         loopback mode, enable  /  disable and configure,  28
         fix period, configure,  76                                                                                         low power mode customization, GPS ,  26
         fix session                                                                                                        LPM
                  end,  79                                                                                                           SIM, default state,  27
                  initiate,  80                                                                                             LPM. See Low Power Mode
                  status, report,  89                                                                                       LTE
         fix type                                                                                                                    CA pairs supported, display,  33
                  configure,  76                                                                                                     CA, enable  /  disable,  33
                  last fix,  83                                                                                                      network information, display,  35
         fix wait time, configure,  76                                                                                               receive diversity during CA, enable/disable,  56
         heading, last fix,  83                                                                                                      receive diversity, enable/disable,  57
         horizontal estimated positional error, last fix,  83                                                               LTE bandwidth
         ionosphere data, clear,  78                                                                                                 set,  69
         latitude, last fix,  83
         LBS APNs, set,  81                                                                                                 M
         location details, most recent,  83
         location uncertainty angle, last fix,  83                                                                          memory management
         longitude, last fix,  83                                                                                                    command list,  71
         low power mode, customization,  26                                                                                 MM
         low power mode, enable  /  disable,  26                                                                                     state and substate, return,  30
         MO method, query/set,  84                                                                                          mode acquired by modem, return,  30
         multiple fix (tracking) session, initiate ,  92                                                                    modem
         port ID over TCP/IP, query/set,  87                                                                                         customizations,  25
         position data, clear,  78                                                                                                   FTM mode
         reference location reporting, enable  /  disable,  26                                                                               activate,  62
         return CtoN and frequency measurement,  60                                                                                          deactivate,  62,  63
         satellite information, request,  88                                                                                         IMEI, write unencrypted,  37
         select antenna,  26                                                                                                         mode, return,  30
         set CGPS IQ log mask,  60                                                                                                   online mode, activate,  62,  63
         start  /  stop CGPS diagnostic task,  61                                                                                    operational status, return,  30
         SUPL server URL, query/set,  90


136                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41111748

## Page 137

Index


        place offline,  68                                                                                                  SAR backoff state
        PRI part number and revision, query/set,  44                                                                                 current, query  /  set,  106
        reset,  44                                                                                                                   default, query  /  set,  107
        reset, wait for firmware update,  24                                                                                scripts
        SKU ID, assign,  27                                                                                                          testing, command timing,  8
        temperature                                                                                                         serial number, using IMEI as,  28
                 limits, query/set,  41                                                                                     SIM
        voltage limits, query/set,  43                                                                                               default state in low power mode,  27
                                                                                                                                     interface, select,  97
N                                                                                                                           SIM hotswap, configure,  27
                                                                                                                            SIM Toolkit. See STK.
network                                                                                                                     SKU ID, assign,  27
        personalization                                                                                                     super speed USB, set,  50
                 PLMN list provision  /  display ,  38
NMEA data output rate, enable/set,  85                                                                                      T
NMEA sentence type, get/set,  86
                                                                                                                            TD-SCDMA
O                                                                                                                                    receive diversity, enable/disable,  57
                                                                                                                            temperature
offline, place modem,  68                                                                                                            current, return,  40
OMA-DM                                                                                                                               limits, query/set,  41
        command list,  98,  102                                                                                                      return,  30
                                                                                                                                     state, return,  40
                                                                                                                            test
P                                                                                                                                    scripts, command timing,  8
                                                                                                                            test radio configuration
PAD                                                                                                                                  drop,  70
        command list,  13                                                                                                   testing
password                                                                                                                             command list,  59
        changing,  17                                                                                                       thermal mitigation, status,  46
        commands, list,  15,  18,  108                                                                                      timing
        protected commands, access,  16                                                                                              AT command entry,  8
        requirements,  8                                                                                                             AT guard time,  9
PCSC, enable/disable,  27                                                                                                            test script commands,  8
PLMN                                                                                                                        Tx
        network personalization, provision  /  display list,  38                                                                     AGC reading (LTE), return,  65
power                                                                                                                                LTE parameters, configure,  67
        control status details, return,  39                                                                                          WCDMA parameters, configure,  70
        offset from max Tx, set  /  query,  104
        power off, W_Disable,  40                                                                                           U
        Tx (max), set  /  query,  103
PRI, part number and revision, query/set,  44                                                                               UIM2 support, enable  /  disable,  27
product ID, set in USB descriptor,  49                                                                                      unlock protected commands,  16
PS state, return,  30                                                                                                       USB
                                                                                                                                     descriptor  —  product ID, query/set            ,  49
R                                                                                                                                    interface configuration, query/set,  47
                                                                                                                                     speed, query/set,  50
receive diversity, enable/disable,  56,  57                                                                                 USB descriptor information, display,  48
reference documents, location,  9                                                                                           user plane, GPS MO method,  84
reset modem,  24,  44
restore device to saved restore point,  74                                                                                  V
result codes, displaying in document,  9
Rx                                                                                                                          vanui
        AGC reading (LTE), return,  64                                                                                               MBIM provider name format, roaming,  27
                                                                                                                            version
S                                                                                                                                    hardware, display,  30
                                                                                                                            voice
SAR backoff control method,  26                                                                                                      call pages, enable/disable ’ignore’ capability,  25


Rev. 2 Jan.19                                                                                                                                                                                                                                                                                                                                                                       Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      137

## Page 138

AirPrime EM75xx AT Command Reference


  voltage                                                                                                                                                                                                                                                                                                            W
                       actual, return,  42
                       raw reading, return,  42                                                                                                                                                                                                                                                                      W_Disable, power off enable,  40
                       state, return,  42                                                                                                                                                                                                                                                                            WCDMA
  voltage limits, query/set,  43                                                                                                                                                                                                                                                                                                          receive diversity, enable/disable,  57
                                                                                                                                                                                                                                                                                                                     WWAN Disable. See Low Power Mode


138                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         Proprietary and Confidential                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      41111748

## Page 139

_No extractable text found on this page._

## Page 140

_No extractable text found on this page._
