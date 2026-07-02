# itu-v250

> Generated from the sibling PDF for local reference search and review.
> Source PDF: `itu-v250.pdf`
> Source SHA-256: `1e29ec28a967cfc5e9ce6effa1bcf9f98242ae524fe6e55ae369776c068edf39`
> Generated: 2026-07-02
> Extractor: pypdf 6.14.2
> Note: This is text extraction only; tables, columns, images, and scanned pages may need manual verification against the PDF.

## Extraction Summary

- Pages: 106
- Pages without extractable text: 2

## PDF Metadata

| Key | Value |
|---|---|
| Author | ITU-T Study Group 16 |
| CreationDate | D:20040315164317Z |
| Creator | International Telecommunication Union |
| Keywords | V.250,V,250 |
| ModDate | D:20040316091334+01'00' |
| Producer | ITU PDF Server - Electronic Publishing Service |
| Subject | SERIES V: DATA COMMUNICATION OVER THE TELEPHONE NETWORK - Control procedures |
| Title | ITU-T Rec. V.250 (07/2003) Serial asynchronous automatic dialling and control |

## Page 1

INTERNATIONAL  TELECOMMUNICATION  UNION


                                               ITU-T                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             V.250

                                               TELECOMMUNICATION                                                                                                                                                                                                                                   (07/2003)
                                               STANDARDIZATION  SECTOR
                                               OF  ITU


                                                SERIES V: DATA COMMUNICATION OVER THE
                                                TELEPHONE NETWORK
                                                Control procedures


                                                                                                                                                                                                                                                       Serial asynchronous automatic dialling and
                                                control


                                                ITU-T  Recommendation  V.250

## Page 2

ITU-T V-SERIES  RECOMMENDATIONS
                                                                                                                                                                                                                                                                                  DATA COMMUNICATION OVER THE TELEPHONE NETWORK

                                          General                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            V.1–V.9
                                          Interfaces and voiceband modems                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   V.10–V.34
                                          Wideband modems                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            V.35–V.39
                                          Error control                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           V.40–V.49
                                          Transmission quality and maintenance                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            V.50–V.59
                                          Simultaneous transmission of data and other signals                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            V.60–V.99
                                          Interworking with other networks                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      V.100–V.199
                                          Interface layer specifications for data communication                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                V.200–V.249
                                          Control procedures                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              V.250–V.299
                                          Modems on digital circuits                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     V.300–V.399


For further details, please refer to the list of ITU-T Recommendations.

## Page 3

ITU-T Recommendation V.250

                                                Serial asynchronous automatic dialling and control


Summary
This version of ITU-T Rec. V.250 integrates Amendment 1 (07/2001), Amendment 2 (03/2002) and
Amendment 3 (07/2003) with the 05/1999 version of the Recommendation.
This Recommendation defines commands and responses for use by a DTE to control a V-series DCE
using serial data interchange over an asynchronous interface. It contains four elements:
–                                                                                                                           codifies existing practice in common GSTN-DCE that use the ATtention (AT) command set;
–                                                                                                                           defines a format for orderly extension of the AT command set;
–                                                                        provides a set for standardized extensions for common functions to identify the DCE, to
                     control the DTE-DCE interface, and to control DCE-DCE protocols (signal conversion,
                     error control and data compression);
–                                                                    provides a mapping for these commands into V.25 bis frame format for use with DCEs
                     employing synchronous serial interfaces.


Source
ITU-T Recommendation V.250 was approved on 14 July 2003 by ITU-T Study Group 16
(2001-2004) under the ITU-T Recommendation A.8 procedure.


Keywords
AT Commands, data modems, data transmission, DCE control.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                                                   i

## Page 4

FOREWORD
The International Telecommunication Union (ITU) is the United Nations specialized agency in the field of
telecommunications. The ITU Telecommunication Standardization Sector (ITU-T) is a permanent organ of
ITU. ITU-T is responsible for studying technical, operating and tariff questions and issuing
Recommendations on them with a view to standardizing telecommunications on a worldwide basis.
The World Telecommunication Standardization Assembly (WTSA), which meets every four years,
establishes the topics for study by the ITU-T study groups which, in turn, produce Recommendations on
these topics.
The approval of ITU-T Recommendations is covered by the procedure laid down in WTSA Resolution 1.
In some areas of information technology which fall within ITU-T's purview, the necessary standards are
prepared on a collaborative basis with ISO and IEC.


                                                              NOTE

In this Recommendation, the expression "Administration" is used for conciseness to indicate both a
telecommunication administration and a recognized operating agency.
Compliance with this Recommendation is voluntary. However, the Recommendation may contain certain
mandatory provisions (to ensure e.g., interoperability or applicability) and compliance with the
Recommendation is achieved when all of these mandatory provisions are met.  The words "shall" or some
other obligatory language such as "must" and the negative equivalents are used to express requirements. The
use of such words does not suggest that compliance with the Recommendation is required of any party.


                                         INTELLECTUAL PROPERTY RIGHTS
ITU draws attention to the possibility that the practice or implementation of this Recommendation may
involve the use of a claimed Intellectual Property Right. ITU takes no position concerning the evidence,
validity or applicability of claimed Intellectual Property Rights, whether asserted by ITU members or others
outside of the Recommendation development process.
As of the date of approval of this Recommendation, ITU had not received notice of intellectual property,
protected by patents, which may be required to implement this Recommendation. However, implementors
are cautioned that this may not represent the latest information and are therefore strongly urged to consult the
TSB patent database.


                                                            ITU  2004
All rights reserved. No part of this publication may be reproduced, by any means whatsoever, without the
prior written permission of ITU.


ii                                                                                                                                              ITU-T Rec. V.250 (07/2003)

## Page 5

CONTENTS
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          Page
1                                                                                                     Introduction and scope ..................................................................................................                                                                                                                                          1
2 References..................................................................................................................... 1
                      2.1 Normative references......................................................................................    1
                      2.2 Informative references....................................................................................    2
3 Definitions and abbreviations.......................................................................................    3
                      3.1 Definitions......................................................................................................    3
                      3.2 Abbreviations .................................................................................................    4
4 Physical layer................................................................................................................ 4
                      4.1 Circuits ...........................................................................................................    4
                      4.2 Character formatting.......................................................................................    5
                      4.3 Data rates........................................................................................................    5
5 Syntax and procedures..................................................................................................    5
                      5.1 Alphabet .........................................................................................................    5
                      5.2                                                                                                   DTE commands lines          .....................................................................................                                                                                                                                          6
                      5.3                                                                                                   Basic Syntax commands   .................................................................................                                                                                                                                          7
                      5.4                                                                                                   Extended Syntax commands...........................................................................                                                                                                                                          8
                      5.5 Issuing commands..........................................................................................  12
                      5.6 Executing commands......................................................................................  12
                      5.7 DCE responses................................................................................................  13
                      5.8 Manufacturer-specific characteristics.............................................................  16
6 Functions ...................................................................................................................... 17
                      6.1                                                                                                   Generic DCE control      ......................................................................................                                                                                                                  18
                      6.2                                                                                                   DTE-DCE interface commands ......................................................................                                                                                                                  25
                      6.3 Call control.....................................................................................................  37
                      6.4 Modulation control commands.......................................................................  48
                      6.5                                                                                                   Error control commands      .................................................................................                                                                                                                  60
                      6.6                                                                                                   Data compression commands      .........................................................................                                                                                                                  68
                      6.7 DCE testing ....................................................................................................  73
                      6.8                                                                                                   PCM DCE commands         ....................................................................................                                                                                                                  85
                      6.9                                                                                                   V.59 Command (+TMO)  ................................................................................                                                                                                                  91
Appendix I – Summary of basic and extended format commands        ..........................................                                                                                                                   93
Appendix II – DCE configuration, dialling, negotiation and reporting, example session    .......                                                                                                                   95
Appendix III – Encapsulation of V.250 messages in V.25 bis DCE        .......................................                                                                                                                     96
                      III.1 Scope ..............................................................................................................     96
                      III.2                                                                          Encapsulation of V.250 messages       ..................................................................                                                                                                                 96
                      III.3 Applicable V.250 commands .........................................................................     96
                      III.4 Applicable V.250 responses...........................................................................     97


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                           iii

## Page 6

_No extractable text found on this page._

## Page 7

ITU-T Recommendation V.250

                                   Serial asynchronous automatic dialling and control

1                                                                                                                           Introduction and scope
This Recommendation is applicable to the interconnection of Data Terminal Equipment (DTE) and
Data Circuit-terminating Equipment (DCE) employing serial binary data operation via the ITU-T
Recs V.100-series interchange circuits.
This Recommendation contains four elements:
–                                                                     codifies existing practice in common Asynchronous GSTN-DCE that use the ATtention
               (AT) command set. It identifies the protocol elements, procedures, and behaviours that
               were found to be held in common among a large portion of DCE manufacturers. It is
               intended, as much as possible, to preserve compatibility between DCEs and DTEs. Most
               DCEs implement a number of extensions and behavioural differences beyond the
               descriptions in this Recommendation; such extensions and differences are explicitly
               permitted by this Recommendation (see 5.8);
–                                                                                                             defines a format for extension of the AT command set in an orderly fashion. It reserves the
               "+" command prefix to be used by other standardized extensions, such as those found in
               ITU-T Recs T.31 and T.32 (Asynchronous Facsimile DCE Control);
–                                                                   provides a set of standardized extensions, based on the extended "+" command format.
               These commands identify the DCE, control the DTE-DCE interface, and control DCE-DCE
               protocol behaviour (signal conversion, error control and data compression);
–                                                                                                         provides a mapping of the commands defined in this Recommendation into V.25  bis frame
               format for use with DCEs employing synchronous serial interfaces. See Appendix III.
The procedures described for automatic calling equipment conforming to this Recommendation
allow interworking with automatic answering equipment conforming to ITU-T Recs V.25 and
V.25 bis.

2 References

2.1 Normative references
The following ITU-T Recommendations and other references contain provisions which, through
reference in this text, constitute provisions of this Recommendation. At the time of publication, the
editions indicated were valid. All Recommendations and other references are subject to revision;
users of this Recommendation are therefore encouraged to investigate the possibility of applying the
most recent edition of the Recommendations and other references listed below. A list of the
currently valid ITU-T Recommendations is regularly published. The reference to a document within
this Recommendation does not give it, as a stand-alone document, the status of a Recommendation.
–                                                                                                                           ITU-T Recommendation Q.23 (1988), Technical features of push-button telephone sets.
–                                                                                                                           ITU-T Recommendation T.50 (1992), International Reference Alphabet (IRA) (Formerly
               International Alphabet No. 5 or IA5) – Information technology – 7-bit coded character set
               for information interchange.
–                                                                                                                           ITU-T Recommendation V.4 (1988), General structure of signals of International Alphabet
               No. 5 code for character oriented data transmission over public telephone networks.
–                                                                                                                           ITU-T Recommendation V.8 bis (2000), Procedures for the identification and selection of
               common modes of operation between data circuit-terminating equipments (DCEs) and


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                                        1

## Page 8

between data terminal equipments (DTEs) over the public switched telephone network and
                on leased point-to-point telephone-type circuits.
–                                                                                                                           ITU-T Recommendation V.24 (2000), List of definitions for interchange circuits between
                data terminal equipment (DTE) and data circuit-terminating equipment (DCE).
–                                                                                                                           ITU-T Recommendation V.25 (1996), Automatic answering equipment and general
                procedures for automatic calling equipment on the general switched telephone network
                including procedures for disabling of echo control devices for both manually and
                automatically established calls.
Other relevant Recommendations are listed in the Supplement 1 to this Recommendation.

2.2 Informative references
–                                                                                                                           ITU-T Recommendation T.31 (1995), Asynchronous facsimile DCE control – Service
                Class 1.
–                                                                                                                           ITU-T Recommendation T.32 (1995), Asynchronous facsimile DCE control – Service
                Class 2.
–                                                                                                                           ITU-T Recommendation V.8 (2000), Procedures for starting sessions of data transmission
                over the public switched telephone network.
–                                                                                                                           ITU-T Recommendation V.14 (1993), Transmission of start-stop characters over
                synchronous bearer channels.
–                                                                                                                           ITU-T Recommendation V.18 (2000), Operational and interworking requirements for
                DCEs operating in the text telephone mode.
–                                                                                                                           ITU-T Recommendation V.21 (1988), 300 bits per second duplex modem standardized for
                use in the general switched telephone network.
–                                                                                                                           ITU-T Recommendation V.22 (1988), 1200 bits per second duplex modem standardized for
                use in the general switched telephone network and on point-to-point 2-wire leased
                telephone-type circuits.
–                                                                                                                           ITU-T Recommendation V.22 bis (1988), 2400 bits per second duplex modem using the
                frequency division technique standardized for use on the general switched telephone
                network and on point-to-point 2-wire leased telephone-type circuits.
–                                                                                                                           ITU-T Recommendation V.23 (1988), 600/1200-baud modem standardized for use in the
                general switched telephone network.
–                                                                                                                           ITU-T Recommendation V.26 bis (1988), 2400/1200 bits per second modem standardized
                for use in the general switched telephone network.
–                                                                                                                           ITU-T Recommendation V.26 ter (1988), 2400 bits per second duplex modem using the
                echo cancellation technique standardized for use on the general switched telephone
                network and on point-to-point 2-wire leased telephone-type circuits.
–                                                                                                                           ITU-T Recommendation V.27 ter (1988), 4800/2400 bits per second modem standardized
                for use in the general switched telephone network.
–                                                                                                                           ITU-T Recommendation V.32 (1993), A family of 2-wire, duplex modems operating at data
                signalling rates of up to 9600 bit/s for use on the general switched telephone network and
                on leased telephone-type circuits.
–                                                                                                                           ITU-T Recommendation V.32 bis (1991), A duplex modem operating at data signalling
                rates of up to 14 400 bit/s for use on the general switched telephone network and on leased
                point-to-point 2-wire telephone-type circuits.


2                                                                                                                                                 ITU-T Rec. V.250 (07/2003)

## Page 9

–                                                                                                                           ITU-T Recommendation V.34 (1998), A modem operating at data signalling rates of up to
                33 600 bit/s for use on the general switched telephone network and on leased point-to-point
                2-wire telephone-type circuits.
–                                                                                                                           ITU-T Recommendation V.42 (2002), Error-correcting procedures for DCEs using
                asynchronous-to-synchronous conversion.
–                                                                                                                           ITU-T Recommendation V.42 bis (1990), Data compression procedures for data
                circuit-terminating equipment (DCE) using error correction procedures.
–                                                                                                                           ITU-T Recommendation V.44 (2000), Data compression procedures.
–                                                                                                                           ITU-T Recommendation V.54 (1988), Loop test devices for modems.
–                                                                                                                           ITU-T Recommendation V.58 (1994), Management information model for V-Series DCEs.
–                                                                                                                           ITU-T Recommendation V.59 (2000), Managed objects for diagnostic information of
                public switched telephone network connected V-series modem DCEs.
–                                                                                                                           ITU-T Recommendation V.90 (1998), A digital modem and analogue modem pair for use
                on the Public Switched Telephone Network (PSTN) at data signalling rates of up to
                56 000 bit/s downstream and up to 33 600 bit/s upstream.
–                                                                                                                           ITU-T Recommendation V.91 (1999), A digital modem operating at data signalling rates
                of up to 64 000 bit/s for use on a 4-wire circuit switched connection and on leased
                point-to-point 4-wire digital circuits.
–                                                                                                                           ITU-T Recommendation V.92 (2000), Enhancements to Recommendation V.90.
–                                                                                                                           ITU-T Recommendation X.680 (2002)│ISO/IEC 8824-1:2002, Information technology –
                Abstract Syntax Notation One (ASN.1): Specification of basic notation .
NOTE – See Supplement 1 to this Recommendation for additional informative references.

3 Definitions and abbreviations

3.1 Definitions
This Recommendation defines the following terms:
3.1.1 command state: In Command State, the DCE is not communicating with a remote station,
and the DCE is ready to accept commands. Data signals from the DTE on circuit 103 are treated as
command lines and processed by the DCE, and DCE responses are sent to the DTE on circuit 104.
The DCE enters this state upon power-up, and when a call is disconnected.
3.1.2                       online command state: In Online Command State, the DCE is communicating with a
remote station, but treats signals from the DTE on circuit 103 as command lines and sends
responses to the DTE on circuit 104. Depending on the implementation, data received from the
remote station during Online Command State may be either discarded or retained in the DCE until
Online Data State is once again entered (by a command from the DTE). Data previously transmitted
by the local DTE and buffered by the DCE may be transmitted from the buffer to the remote DCE
during Online Command State, or it may be discarded or transmission deferred until Online Data
State is once again entered. Online Command State may be entered from Online Data state by a
mechanism defined in 6.2.9 or by other manufacturer-defined means.
3.1.3                                       online data state: In Online Data State, the DCE is communicating with a remote station.
Data signals from the DTE on circuit 103 are treated as data and transmitted to the remote station,
and data received from the remote station are delivered to the DTE on circuit 104. Data and control
signals are monitored by the DCE to detect events such as loss of the remote connection and
DTE requests for disconnection or switching to Online Command State. Online Data State is
entered by successful completion of a command to originate or answer a call, by automatically


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                                        3

## Page 10

answering a call, or by a DTE command to return to Online Data State from Online Command
State.
3.1.4 direct mode: Mode of Online Data State whereby the V.24 circuits 103 (transmit data) and
104 (received data) transfer data at the same rate or rates in use on the DCE-to-DCE communication
channels. The DCE does not buffer data in either direction, nor does it implement flow control. For
DCE modulation methods that transfer data synchronously, this mode implies the use of V.14
synchronous/asynchronous protocol within the DCE.
3.1.5 buffered mode: Mode of Online Data State whereby the V.24 circuits 103 (transmit data)
and 104 (received data) transfer data at data rates independent of the rates in use on the
DCE-to-DCE communication channels. The DCE buffers the data rate differences as required.
Neither error control nor data compression is used. For DCE modulation methods that transfer data
synchronously, this mode implies the use of V.14 synchronous/asynchronous protocol within the
DCE. For DCE modulation methods that transfer data asynchronously, asynchronous start-stop
framing is used on the DCE-to-DCE communications channels.
3.1.6 leadin: Commands defined using the extended syntax defined in 5.2.3 begin with a
"+" character. The second character is reserved for a particular function or application. That
two-character sequence, "+<char>" is the Leadin.
3.1.7 [...]: Square brackets are used to indicate that the enclosed items are optional. The square
brackets themselves do not appear in the command line.
3.1.8                                                         <<...><<>: Angle brackets are used to enclose the names of other syntactical elements. When >>
those elements appear in an actual command line, the actual element is used and the angle brackets
are omitted.
All other characters, including "?", "=", parentheses, etc., shall appear in commands as written.

3.2 Abbreviations
This Recommendation uses the following abbreviations:
CCITT                                                                                        International Telephone and Telegraph Consultative Committee
IRA                                                                                                                                                 International Reference Alphabet (ITU-T Rec. T.50)
ITU-T                                                                                                      International Telecommunication Union – Telecommunication Standardization Sector

4 Physical layer
The circuits listed in 4.1 are intercepted and controlled by the DCE. The DCE is designed so that it
will function properly if only these circuits are connected or implemented. V.24 circuit designators
are listed in this clause.

4.1 Circuits
Signal Ground (Circuit 102) – Connection of this circuit is required for proper recognition of
signals on other circuits.
Transmitted Data (Circuit 103) – While in command state or online command state, data signals are
processed by the DCE and not transmitted to the remote station.
Received Data (Circuit 104) – While in command state or online command state, data received from
the remote station may be ignored (see 3.1.2 for the definition of online command state), and the
DCE generates responses on this circuit.
Data Terminal Ready (Circuit 108/2) – The DCE monitors the effect of changes in the state of this
circuit on the operation of the underlying DCE and operates accordingly. For example, if the DCE


4                                                                                                                                                 ITU-T Rec. V.250 (07/2003)

## Page 11

responds to an on-to-off transition on this circuit by disconnecting a call, the DCE will act
accordingly by returning from online data state to command state.
Received Line Signal Detector (Circuit 109) – The DCE may intercept this signal so that the
issuance of result codes can be properly coordinated with transitions on this signal.
Calling Indicator (Circuit 125) – The DCE may intercept this signal so that it can detect network
alerting signals and automatically answer, if so conditioned by the appropriate command (S0,
see 6.3.8).

4.2 Character formatting
During command state and online command state, data transmitted between the DTE and DCE shall
conform to the requirements for start-stop data transmission specified in ITU-T Rec. V.4 and ISO
1177. Parity may be even, odd, mark, space or not used. Each character shall have at least one
complete stop element. The DCE should accept commands using any combination of parity and
stop elements supported during online data state. These shall include, as a minimum, the following
combinations, as defined in Annex B/V.42, each of which consists of ten total bits (including the
start element):
–                                                                                                                           7 data bits, even parity, 1 stop element;
–                                                                                                                            7 data bits, odd parity, 1 stop element;
–                                                                                                                            7 data bits, space parity, 1 stop element;
–                                                                                                                           7 data bits, mark parity, 1 stop element (7 data bits, no parity bit, 2 stop elements);
–                                                                                                                           8 data bits, no parity, 1 stop element.
During online data state, the DCE shall be transparent to changes in data format; the use of a
particular format during command state should not restrict the use of other formats that are
supported during online data state. However, DCE responses issued to indicate transition from
online data state back to command state are issued using the same format and parity as the last
command line issued by the DTE (see 5.7), and the DTE must therefore be prepared to recognize
these responses even though the character format may have been changed.
See 6.2.11 for a command to explicitly select the DTE-DCE character format.

4.3 Data rates
The DCE shall be able to accept commands at either 1200 bit/s or 9600 bit/s. It is desirable that the
DCE be able to accept commands and automatically detect the rate being used by the DTE at all
rates supported by the DCE on the DTE-DCE interface. The DCE may provide a strap, switch, or
other facility to define the rate at which the DTE is operating; however, while the rate is so selected,
the DCE shall continue to be capable of accepting commands at either 1200 bit/s or 9600 bit/s.
See 6.2.10 for a command to explicitly select the DTE-DCE rate.
When operating in the online command state, the DCE is not required to accept commands at other
than the online data rate; i.e., the requirement to accept commands at 1200 bit/s or 9600 bit/s does
not apply during online command state.

5                                                                                                                           Syntax and procedures

5.1 Alphabet
The T.50 International Alphabet 5 (hereinafter cited as "IA5") is used in this Recommendation.
Only the low-order seven bits of each character are significant to the DCE; any eighth or
higher-order bit(s), if present, are ignored for the purpose of identifying commands and parameters.
Lower-case characters (IA5 values from 6/1 to 7/10) are considered identical to their upper-case


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                                        5

## Page 12

equivalents (IA5 values from 4/1 to 5/10) when received by the DCE from the DTE. Result codes
from the DCE which are defined in this Recommendation shall be in upper case.

5.2                                                                                       DTE commands lines
In the descriptions that follow, words enclosed in    <<angle brackets ><<                  >    are references to syntactical >>
elements defined in this Recommendation. When they appear in a command line, the brackets are
not used. Words enclosed in [square brackets] represent optional items; such items may be omitted
from the command line at the point where they are specified, and when they appear the square
brackets are not included in the command line. Other characters that appear in syntax descriptions
shall appear in the places shown.
In the following subclauses regarding DTE commands, references are made to responses issued by
the DCE which are defined in 5.7. In order to provide a clearer presentation, DCE responses are
mentioned in terms of their alphabetic format; the actual response issued will depend on the setting
of parameters that affect response formats (e.g., Q and V commands).
5.2.1                                                    Command line general format
A command line is made up of three elements: the prefix, the body, and the termination character.
The command line prefix consists of the characters "AT" (IA5 4/1, 5/4) or "at" (IA5 6/1, 7/4), or, to
repeat the execution of the previous command line, the characters " A/" (IA5 4/1, 2/15) or "a/"
(IA5 6/1, 2/15).
The body is made up of individual commands as specified later in this Recommendation. Space
characters (IA5 2/0) are ignored and may be used freely for formatting purposes, unless they are
embedded in numeric or string constants (see 5.4.2.1 or 5.4.2.2). The termination character may not
appear in the body. The DCE shall be capable of accepting at least 40 characters in the body.
The termination character may be selected by a user option (parameter  S3), the default being CR
(IA5 0/13).
5.2.2                                                   Command line editing
The character defined by parameter S5 (default, BS [IA5 0/8]) is intended to be interpreted as a
request from the DTE to the DCE to delete the previous character; the precise action undertaken is
manufacturer-specific. Any control characters (IA5 0/0 through 1/15, inclusive) that remain in the
command line after receipt of the termination character shall be ignored by the DCE.
The DCE checks characters from the DTE first to see if they match the termination character ( S3),
then the editing character (S5), before checking for other characters. This insures that these
characters will be properly recognized even if they are set to values that the DCE uses for other
purposes. If S3 and S5 are set to the same value, a matching character will be treated as matching
S3 (S3 is checked before S5).
5.2.3                                                   Command line echo
The DCE may echo characters received from the DTE during command state and online command
state back to the DTE, depending on the setting of the E command. If so enabled, characters
received from the DTE are echoed at the same rate, parity, and format as received. Echoing
characters not recognized as valid in the command line or of incomplete or improperly-formed
command line prefixes is manufacturer-specific (see 5.8).
5.2.4                                                   Repeating a command line
If the prefix "A/" or "a/" is received (IA5 4/1, 2/15 or 6/1, 2/15), the DCE shall immediately execute
once again the body of the preceding command line. No editing is possible, and no termination
character is necessary. A command line may be repeated multiple times through this mechanism, if
desired. Responses to the repeated command line shall be issued using the parity and format of the

6                                                                                                                                                 ITU-T Rec. V.250 (07/2003)

## Page 13

original command line, and the rate of the " A/". If "A/" is received before any command line has
been executed, the preceding command line is assumed to have been empty (that results in an  OK
result code).
5.2.5                                                   Types of DTE commands
There are two types of commands: action commands and parameter commands.
Action commands may be "executed" (to invoke a particular function of the equipment, which
generally involves more than the simple storage of a value for later use), or "tested" (to determine
whether or not the equipment implements the action command, and, if subparameters are associated
with the action, the ranges of subparameter values that are supported).
Parameters may be "set" (to store a value or values for later use), "read" (to determine the current
value or values stored), or "tested" (to determine whether or not the equipment implements the
parameter, and the ranges of values supported).
5.2.6                                                   DTE command syntax
Clause 5.3 defines Basic Syntax DTE commands, which are implemented in common DCE. This
Recommendation also defines Extended Syntax DTE commands in 5.4. Commands of either type
may be included in command lines, in any order.

5.3                                                                                       Basic Syntax commands
5.3.1                                                   Basic Syntax command format
The format of Basic Syntax commands, except for the D and S commands, is as follows:
<command><          >[<<number>>]
<<                  >>  <<              >>
where   <command> is either a single character, or the " &" character (IA5 2/6) followed by a single
character. Characters used in  <<command><<                           > shall be taken from the set of alphabetic characters. >>
<number><       > may be a string of one or more characters from " 0" through "9" representing a decimal
<<              >>
integer value. Commands that expect a  <<number><<                                  > are noted in the description of the command (see >>
clause 6). If a command expects  <<number><<                                   > and it is missing ( <>>               <command ><<        > is immediately >>
followed in the command line by another   <<command>>   or the termination character), the value " 0"
                                                                        <<                   >>
is assumed. If a command does not expect a  <<number ><<                                         > and a number is present, an ERROR is >>
generated. All leading "0"s in  <<number><<                         > are ignored by the DCE. >>
Additional commands may follow a command (and associated parameter, if any) on the same
command line without any character required for separation. The actions of some commands cause
the remainder of the command line to be ignored (e.g., A).
See the D command for details on the format of the information that follows it.
5.3.2 S-parameters
Commands that begin with the letter " S" constitute a special group of parameters known as
"S-parameters". These differ from other commands in important respects. The number following the
"S" indicates the "parameter number" being referenced. If the number is not recognized as a valid
parameter number, an ERROR result code is issued.
Immediately following this number, either a "?" or "=" character (IA5 3/15 or 3/13, respectively)
shall appear. "?" is used to read the current value of the indicated S-parameter; "  =" is used to set the
S-parameter to a new value.
S <<parameter_number><<                 >? >>
S <<parameter_number><<                 >=>>= [<==<value><<>] >>


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                                        7

## Page 14

If the "=" is used, the new value to be stored in the S-parameter is specified in decimal following
the "=". If no value is given (i.e., the end of the command line occurs or the next command follows
immediately), the S-parameter specified may be set to 0, or an  ERROR result code issued and the
stored value left unchanged. The ranges of acceptable values are given in the description of each
S-parameter.
If the "?" is used, the DCE transmits a single line of information text to the DTE. For S-parameters
defined in this Recommendation, the text portion of this information text consists of exactly three
characters, giving the value of the S-parameter in decimal, with leading zeroes included.

5.4                                                                                       Extended Syntax commands
5.4.1                                                   Command naming rules
Both actions and parameters have names, which are used in the related commands. Names always
begin with the character "+" (IA5 2/15). Following the "+", from one to sixteen (16) additional
characters appear in the command name. These characters shall be selected from the following set:
A through Z                                                                  (IA5 4/1 through 5/10)
0 through 9                                                                          (IA5 3/0 through 3/9)
!             (IA5 !!!       2/1)
%                   (IA5 %%% 2/5)
−             (IA5 −−−       2/13)
.             (IA5 2/14)
⁄             (IA5 ⁄⁄⁄       2/15)
:             (IA5 :         3/10)
::
_             (IA5 ___       5/15)
The first character following the " ++" shall be an alphabetic character in the range of "A" through ++
"Z". This first character generally implies the application in which a command is used or the
standards committee that defined it (e.g., command names beginning with " F" are generally
associated with facsimile-related standards). See Appendix I for information on first command
characters reserved for use by particular standards committees. All other + leadin character
sequences are reserved for future standardization by the ITU-T.
The command interpreter in the Data Circuit-terminating Equipment (DCE) considers lower-case
characters to be the same as their upper-case equivalents; therefore, command names defined in
standards referencing this Recommendation that include alphabetic characters should be defined
using only the upper-case characters.
Standards that reference this Recommendation may choose to establish internal naming conventions
that permit implicit recognition of a name as an action or as a parameter. For example, the standard
could choose to end all action names with an exclamation point ("   !!"), or all parameter names with a !!
percent sign (" %%"). This Recommendation imposes no such conventions, however. %%
5.4.2 Values
When subparameters are associated with the execution of an action, or when setting a parameter,
the command may include specification of values. This is indicated by the appearance of   <<value><<                            > in >>
the descriptions below.
<value><<<> shall consist of either a numeric constant or a string constant. >>


8                                                                                                                                                 ITU-T Rec. V.250 (07/2003)

## Page 15

5.4.2.1 Numeric constants
Numeric constants are expressed in decimal, hexadecimal, or binary. In standards that reference this
Recommendation, the definition of each command shall specify which form is used for values
associated with that command; however, such standards may, in introductory information, specify a
"default" type of numeric constant that is assumed for commands within that standard that do not
explicitly specify the type. Such standards shall also define the minimum and maximum acceptable
values.
Decimal numeric constants shall consist of a sequence of one or more of the characters "0"
(IA5 3/0) through "9" (IA5 3/9), inclusive.
Hexadecimal numeric constants shall consist of a sequence of one or more of the characters "0"
(IA5 3/0) through "9" (IA5 3/h), inclusive, and "A" (IA5 4/1) through "F" (IA5 4/6) inclusive. The
characters "A" through "F" represent the equivalent decimal values 10 through 15.
Binary numeric constants shall consist of a sequence of one or more of the characters "0" (IA5 3/0)
and "1" (IA5 3/1).
In all numeric constants, the most significant digit is specified first. Leading "  0" characters shall be
ignored by the DCE. No spaces, hyphens, periods, commas, parentheses, or other generally-
accepted numeric formatting characters are permitted in numeric constants; note in particular that
no "H" suffix is appended to the end of hexadecimal constants.
5.4.2.2 String constants
String constants shall consist of a sequence of displayable IA5 characters, each in the range from
2/0 to 7/15, inclusive, except for the characters """ (IA5 2/2) and "\" (IA5 5/12). String constants
shall be bounded at the beginning and end by the double-quote character (""", IA5 2/2).
Any character value may be included in the string by representing it as a backslash (" \") character
followed by two hexadecimal digits. For example, "\0D" is a string consisting of the single
character        <CR> (IA5 0/13). If the "\" character itself is to be represented in a string, it shall be
encoded as "\5C". The double-quote character, used as the beginning and ending string delimiter,
shall be represented within a string constant as "\22". Standards that reference this Recommendation
may prohibit use of this "\" mechanism if only displayable characters are permitted in string
constants in that standard and if the double-quote character is not permitted within string constants;
in this case, the "\" character shall be treated as any other IA5 character included within a string
constant.
A "null" string constant, or a string constant of zero length, is represented by two adjacent
delimiters ("").
Standards that reference this Recommendation shall specify, for each string value, any limitations
on the characters that may appear within the string, and the maximum and minimum acceptable
string length.
5.4.2.3 Compound values
Actions may have more than one subparameter associated with them, and parameters may have
more than one value. These are known as "compound values", and their treatment is the same in
both actions and parameters.
A compound value consists of any combination of numeric and string values (as defined in the
description of the action or parameter). The comma character (IA5 2/12) shall be included as a
separator, before the second and all subsequent values in the compound value. If a value is not
specified (i.e., defaults assumed), the required comma separator shall be specified; however, trailing
comma characters may be omitted if all associated values are also omitted.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                                        9

## Page 16

5.4.3 Action commands
5.4.3.1                                                             Action execution command syntax
There are two general types of action commands: those that have associated subparameter values
that affect only that invocation of the command, and those that have no subparameters.
If subparameters are associated with a command, the definition of the action command shall
indicate, for each subparameter, whether the specification of a value for that subparameter is
mandatory or optional. For optional subparameters, the definition shall indicate the assumed
(default) value for the subparameter if no value is specified for that subparameter; the assumed
value may be either a previous value (i.e., the value of an omitted subparameter remains the same as
the previous invocation of the same command, or is determined by a separate parameter or other
mechanism), or a fixed value (e.g., the value of an omitted subparameter is assumed to be zero).
Generally, the default value for numeric subparameters is 0, and the default value for string
subparameters is "" (empty string).
The following syntax is used for actions that have no subparameters:
+<+<name>>
++<<      >>
The following syntax is used for actions that have one subparameter:
+<+++<name><<>[=>>=<==<value><<>] >>
The following syntax is used for actions that have two or more subparameters:
+<+<name>>[==<<compound_value>>]
++<<      >> ==<<                      >>
For actions that accept subparameters, if all subparameters are defined as being optional, and the
default values for all subparameters are satisfactory, the Data Terminal Equipment (DTE) may use
the first syntax above (i.e., omit the "=" from the action execution command as well as all of the
subparameter value string).
If the named action is implemented in the DCE and other relevant criteria are met (e.g., the DCE is
in the proper state), the command shall be executed with any indicated subparameters. If   <<name><<                     > is >>
not recognized, the DCE issues the ERROR result code and terminates processing of the command
line. An ERROR is also generated if a subparameter is specified for an action that does not accept
subparameters, if too many subparameters are specified, if a mandatory subparameter is not
specified, if a value is specified of the wrong type, or if a value is specified that is not within the
supported range.
5.4.3.2                                                             Action test command syntax
The DTE may test if an action command is implemented in the DCE by using the syntax:
+<+<name>>==?
++<<      >>==
If the DCE does not recognize the indicated name, it shall return an  ERROR result code and
terminate processing of the command line. If the DCE does recognize the action name, it shall
return an OK result code. If the named action accepts one or more subparameters, the DCE shall
send an information text response to the DTE, prior to the OK result code, specifying the values
supported by the DCE for each such subparameter, and possibly additional information. The format
of this information text is defined for each action command; general formats for specification of
sets and ranges of numeric values are described in 5.7.1 and 5.7.2.
5.4.4 Parameter commands
5.4.4.1 Parameter types
Parameters may be defined as "read-only" or "read-write". "Read-only" parameters are used to
provide status or identifying information to the DTE, but are not settable by the DTE; attempting to


10                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 17

set their value is an error. In some cases (specified in the description of the individual parameter),
the DCE may ignore attempts to set the value of such parameters rather than respond with an
ERROR result code, if the continued correct operation of the interface between the DCE and DTE
will not be affected by such action. Read-only parameters may be read and tested.
"Read-write" parameters may be set by the DTE, to store a value or values for later use. Read-write
parameters may be set, read, and tested.
Parameters may take either a single value, or multiple (compound) values. Each value may be either
numeric or string; the definition of the parameter shall specify the type of value for each
subparameter. Attempting to store a string value in a numeric parameter, or a numeric value in a
string parameter, is an error.
5.4.4.2                                                             Parameter set command syntax
The definition of the parameter shall indicate, for each value, whether the specification of that value
is mandatory or optional. For optional values, the definition shall indicate the assumed (default)
value if none is specified; the assumed value may be either a previous value (i.e., the value of an
omitted subparameter retains its previous value), or a fixed value (e.g., the value of an omitted
subparameter is assumed to be zero). Generally, the default value for numeric parameters is 0, and
the default value for string parameters is  "" (empty string).
The following syntax is used for parameters that accept a single value:
+<+++<name><< >=>>=[<==<value><<>] >>
The following syntax is used for parameters that accept more than one value:
+<+++<name><< >=>>=[<==<compound_value><<           >] >>
If the named parameter is implemented in the DCE, all mandatory values are specified, and all
values are valid according to the definition of the parameter, the specified values shall be stored. If
<name><     > is not recognized, one or more mandatory values are omitted, or one or more values are of
<<          >>
the wrong type or outside the permitted range, the DCE issues the  ERROR result code and
terminates processing of the command line. An  ERROR is also generated if too many values are
specified. In case of an error, all previous values of the parameter are unaffected.
5.4.4.3                                                             Parameter read command syntax
The DTE may determine the current value or values stored in a parameter by using the following
syntax:
+<+++<name><< >? >>
If the named parameter is implemented in the DCE, the current values stored for the parameter are
sent to the DTE in an information text response. The format of this response is described in the
definition of the parameter. Generally, the values will be sent in the same form in which they would
be issued by the DTE in a parameter setting command; if multiple values are supported, they will
generally be separated by commas, as in a parameter setting command.
5.4.4.4                                                             Parameter test command syntax
The DTE may test if a parameter is implemented in the DCE, and determine the supported values,
by using the syntax:
+<+++<name><< >=>>=? ==
If the DCE does not recognize the indicated name, it returns an ERROR result code and terminates
processing of the command line. If the DCE does recognize the parameter name, it shall return an
information text response to the DTE, followed by an  OK result code. The information text
response shall indicate the values supported by the DCE for each such subparameter, and possibly


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  11

## Page 18

additional information. The format of this information text is defined for each parameter; general
formats for specification of sets and ranges of numeric values are described in 5.7.1 and 5.7.2.
5.4.5 Additional syntax rules
5.4.5.1                                                             Concatenating commands after extended syntax commands
Additional commands may follow an extended syntax command on the same command line if a
semicolon (";", IA5 3/11) is inserted after the preceding extended command as a separator. The
semicolon is not necessary when the extended syntax command is the last command on the
command line.
5.4.5.2                                                             Concatenating commands after basic format commands
Extended syntax commands may appear on the same command line after a basic syntax command
without a separator, in the same manner as concatenation of basic syntax commands.

5.5 Issuing commands
All characters in a command line shall be issued at the same data rate, and with the same parity and
format.
The DCE shall ignore any command line that is not properly terminated. The DCE may consider
30 seconds of mark idle time between any two characters as an improperly terminated command
line. In this case the DCE may or may not generate an  ERROR message. The DCE shall ignore any
characters received from the DTE that are not part of a properly-formatted command line.
If the maximum number of characters that the DCE can accept in the body is exceeded, an  ERROR
result code shall be generated after the command line is terminated.
The DTE shall not begin issuing a subsequent command line until at least one-tenth of a second has
elapsed after receipt of the entire result code issued by the DCE in response to the preceding
command line.

5.6 Executing commands
Upon receipt of the termination character, the DCE shall commence execution of the commands in
the command line in the order received from the DTE. Should execution of a command result in an
error, or a character be not recognized as a valid command, execution is terminated, the remainder
of the command line is ignored, and the ERROR result code is issued. Otherwise, if all commands
execute correctly, only the result code associated with the last command shall be issued; result
codes for preceding commands are suppressed. If no commands appear in the command line, the
OK result code is issued.
5.6.1 Aborting commands
Some action commands that require time to execute may be aborted while in progress; these are
explicitly noted in the description of the command. Aborting of commands is accomplished by the
transmission from the DTE to the DCE of any character. A single character shall be sufficient to
abort the command in progress; however, characters transmitted during the first 125 milliseconds
after transmission of the termination character shall be ignored (to allow for the DTE to append
additional control characters such as line feed after the command line termination character). To
insure that the aborting character is recognized by the DCE, it should be sent at the same rate as the
preceding command line; the DCE may ignore characters sent at other rates. When such an aborting
event is recognized by the DCE, it shall terminate the command in progress and return an
appropriate result code to the DTE, as specified for the particular command.


12                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 19

5.6.2                                                   Handling of invalid numbers and S-parameter values
The DCE shall react to undefined numbers and S-parameter values in one of three ways:
1) issue the ERROR result code, and leave the previous value of the parameter unchanged;
2) issue the OK result code, and leave the previous value of the parameter unchanged; or
3) issue the OK result code, and set the parameter value to the valid value nearest to that
             specified in the command line.
The description of each command may specify which of these three techniques shall be used to
handle invalid parameter values for that command or parameter. If the description does not specify
the handling technique, it shall be defined by the manufacturer.

5.7 DCE responses
While in command state and online command state, the DCE shall issue responses using the same
rate, word length, and parity as the most recently received DTE command line. In the event that no
DTE command has yet been received, rate, word length, and parity used will depend on the
capabilities of the DCE.
When the DCE transitions from the command state or online command state to the online data state,
the result code CONNECT should be issued at the bit rate and parity used during the command
state. When the DCE transitions from the online data state to the command state or online command
state, the result codes should be issued at the bit rate used during the online data state. Thereafter,
any unsolicited result codes should use the bit rate and parity of the last command line issued by the
DTE to the DCE.
The characters of a response shall be contiguous, with no more than 100 milliseconds of mark idle
issued between characters in addition to stop elements.
5.7.1 Responses
There are two types of responses that may be issued by the DCE: information text and result codes.
Information text responses consist of three parts: a header, text, and a trailer. The characters
transmitted for the header are determined by a user setting (see the   V command, 6.2.6). The trailer
consists of two characters, being the character having the ordinal value of parameter S3 followed by
the character having the ordinal value of parameter  S4. Information text specified in this
Recommendation always consists of a single line; information text returned in response to
manufacturer-specific commands may contain multiple lines, and the text may therefore include
IA5 CR, LF, and other formatting characters to improve readability.
Result codes consist of three parts: a header, the result text, and a trailer. The characters transmitted
for the header and trailer are determined by a user setting (see the V command, 6.2.6). The result
text may be transmitted as a number or as a string, depending on a user-selectable setting (see the    V
command).
There are three types of result codes: final, intermediate, and unsolicited.
A final result code indicates the completion of a full DCE action and a willingness to accept new
commands from the DTE.
An intermediate result code is a report of the progress of a DCE action. The CONNECT result code
is an intermediate result code (others may be defined by manufacturers). In the case of a dialling or
answering command, the DCE moves from command state to online data state, and issues a
CONNECT result code. This is an intermediate result code for the DCE because it is not prepared
to accept commands from the DTE while in online data state. When the DCE moves back to the
command state, it will then issue a final result code (such as OK or NO CARRIER).


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  13

## Page 20

Unsolicited result codes (such as RING) indicate the occurrence of an event not directly associated
with the issuance of a command from the DTE.
Table 1 indicates result codes that shall be implemented by the DCE, their numeric equivalents, and
a brief description of the use of each. In clause 6, the description of each command includes the
specific result codes that may be issued in relation to that command and the circumstances under
which they may be issued.

                                                                                                       Table 1/V.250 – Result codes

              Result code                                                   Numeric
                    (ATV1)                                                   (ATV0)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         Description
   OK                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    0                                                                                                                                                                       Acknowledges execution of a command
   CONNECT                                                                                                                                                                                                                                                                                                                                    1                                                                                                                                                                       A connection has been established; the DCE is moving from
                                                                                                                      command state to online data state
   RING                                                                                                                                                                                                                                                                                                                                                                                                                                        2                                                                                                                                                                       The DCE has detected an incoming call signal from the network
   NO CARRIER                                                                                                                                                                                                                                                                           3                                                                                                                                                                       The connection has been terminated or the attempt to establish a
                                                                                                                      connection failed
   ERROR                                                                                                                                                                                                                                                                                                                                                                                                 4                                                                                                                                                                       Command not recognized, command line maximum length
                                                                                                                      exceeded, parameter value invalid, or other problem with
                                                                                                                      processing the command line
   NO DIALTONE                                                                                                                                                                                                                                           6                                                                                                                                                                       No dial tone detected
   BUSY                                                                                                                                                                                                                                                                                                                                                                                                                              7                                                                                                                                                                       Engaged (busy) signal detected
   NO ANSWER                                                                                                                                                                                                                                                                                8                                                                                                                                                                       "@" (Wait for Quiet Answer) dial modifier was used, but
                                                                                                                      remote ringing followed by five seconds of silence was not
                                                                                                                      detected before expiration of the connection timer (S7)
   CONNECT <text>                                                                                  Manufacturer-      Same as CONNECT, but includes manufacturer-specific text
                                                                              specific                                that may specify DTE speed, line speed, error control, data
                                                                                                                      compression, or other status

5.7.2                                                   Extended syntax result codes
Extended syntax result codes may be issued in response to either basic or extended commands, or
both. The appropriate responses shall be specified in the definitions of the commands, the
responses, or both.
The general format of extended syntax result codes is the same as result codes defined in TIA-602
with regard to headers and trailers. The characters specified in S-parameters  S3 and S4 shall be used
in headers and trailers of extended syntax result codes as they are in basic format result codes. The
setting of the "V" command shall affect the headers and trailers associated with extended syntax
result codes in the same manner as basic format result codes; however, unlike basic format result
codes, extended syntax result codes have no numeric equivalent, and are always issued in alphabetic
form.
Extended syntax result codes shall be subject to suppression by the "Q1" command, as with basic
format result codes. The issuance of extended syntax result codes shall not be affected by the setting
of the "X" command.
Extended syntax result codes may be either final, intermediate, or unsolicited; the type shall be
indicated in the definition of the result code.
Extended syntax result codes shall be prefixed by the " ++" character to avoid duplication of basic ++
format result codes specified in TIA-602 and by manufacturers. Following the " ++" character, the ++
name of the result code appears; result code names shall follow the same rules as command names
(see 5.4.1). It is strongly advised that the reservation of the first character of command names noted


14                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 21

in Appendix I also be observed with regard to the assignment of names of extended syntax result
codes.
Extended syntax result codes may include the reporting of values. The definition of the result code
shall specify whether or not values are appended to the result code, and, if so, how many, their
types, and their assumed default values if omitted. When no values are to be reported, the result
code appears in the simplest form:
+<+<name>>
++<<            >>
If a single value is to be reported, the form of the result code shall be:
+<+<name>>: <<value >>
++<<            >>    <<            >>
Note that a single space character (ASCII 20h) separates the colon character (ASCII 3Ah) from the
<value><<<   >; no space appears between the result code name and the colon. If multiple values are to be >>
reported with the result code, the form is:
+<+++<name><<   >: <>><compound_value ><<                    > >>
where  <<compound_value >> follows the rules specified in 5.4.2.3.
             <<                                    >>
5.7.3                                                    Information text formats for test commands
In general, the format of information text returned by extended syntax commands shall be specified
in the definition of the command. This clause describes recommended formats for information text
returned in response to action test (for actions that accept one or more subparameters) and
parameter test commands. The definitions of the responses to such testing commands, as described
in the definitions of the associated commands in standards that reference this Recommendation,
may use this recommended format or any other suitable format that is adequately specified.
Note that the DCE may insert intermediate <CR> characters in very long information text
responses, in order to avoid overrunning DTE receive buffers. If intermediate <CR> characters are
included, the DCE shall not include the character sequences "0  <CR>" (3/0, 0/13) or "OK<CR>"
(4/15, 4/11, 0/13), so that DTE can avoid false detection of the end of these information text
responses.
5.7.3.1                                                             Range of values
When the action accepts a single numeric subparameter, or the parameter accepts only one numeric
value, the set of supported values may be presented in the information text as an ordered list of
values. The list shall be preceded by a left parenthesis ("(", IA5 2/8), and is followed by a right
parenthesis (")", IA5 2/9). If only a single value is supported, it shall appear between the
parentheses. If more than one value is supported, then the values may be listed individually,
separated by comma characters (IA5 2/12), or, when a continuous range of values is supported, by
the first value in the range, followed by a hyphen character (IA5 2/13), followed by the last value in
the range. The specification of single values and ranges of values may be intermixed within a single
information text. In all cases, the supported values shall be indicated in ascending order.
For example, the following are some examples of value range indications:
(0)                                                                                                                                                                                                                                                                                                       Only the value 0 is supported.
(1,2,3)                                                                                                                                         The values 1, 2, and 3 are supported.
(1-3)                                                                                                                                                                                                                                                               The values 1 through 3 are supported.
(0,4,5,6,9,11,12)                                                                  The several listed values are supported.
(0,4-6,9,11-12)                                                                                  An alternative expression of the above list.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  15

## Page 22

5.7.3.2                                                            Compound range of values
When the action accepts more than one subparameter, or the parameter accepts more than one
value, the set of supported values may be presented as a list of the parenthetically-enclosed value
range strings described in 5.7.3.1 above, separated by commas. For example, the information text in
response to testing an action that accepts three subparameters, and supports various ranges for each
of them, could appear as follows:
(0),(1-3),(0,4-6,9,11-12)
This indicates that the first subparameter accepts only the value 0, the second accepts any value
from 1 through 3 inclusive, and the third subparameter accepts any of the values 0, 4, 5, 6, 9, 11,
or 12.

5.8 Manufacturer-specific characteristics
This Recommendation describes characteristics universal to a large installed base of DCEs. Most
DCEs implement a number of extensions and behavioural differences beyond the descriptions in
this Recommendation. The following subclauses mention a few specific and well-known examples
of areas in which these extensions and behavioural differences exist. This is not intended to be a
comprehensive list; extensions and differences do exist in other areas. This Recommendation is not
intended to preclude or limit extensions in these or other areas.
Equipment that implements non-standard commands, values, features, or behaviours, such as
described in the following subclauses, shall be capable of being configured, by one or more
commands, parameters, or switches, so that the equipment will properly interwork with DTEs that
implement only the mandatory provisions of this Recommendation.
5.8.1 Extensions
DCEs claiming compliance to this Recommendation often include extensions in a number of areas.
This Recommendation does not preclude the use of these extensions; however, the definition of
these is totally up to the manufacturer. Other Recommendations may call out extensions as well.
Some areas in which extensions exist include:
1)                                                              command characters and commands consisting of a prefix character followed by one or
            more characters (however, the " ++" prefix is reserved for future use in this and other ++
            standards and should not be used for non-standard purposes);
2)                                                         command numbers (including additional numbers associated with commands defined in
            this Recommendation);
3)                                                            parameter values (including additional values associated with parameters defined in this
            Recommendation);
4) S-parameter numbers;
5)                                                           S-parameter values (including additional values associated with S-parameters defined in
            this Recommendation);
6)                                                                                                           command line editing characters;
7) result codes;
8)                                                                                                           dial string modifiers;
9)                                                                                                            syntax extensions to the body of the command line;
10) information responses;
11)                                                                       mechanisms to exit from online data state and return to command state or online command
            state (using, for example, particular sequences of characters, timing, or other techniques).


16                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 23

5.8.2 Behavioural differences
This Recommendation specifically and intentionally does not describe DCE behaviour in some
situations. This is generally due to variations in existing implementations. DTEs must take into
account the possibility of differences in the behaviour of various DCEs in particular situations.
The following are some examples of areas in which differences are known to exist. This is not
intended to be a comprehensive list; behaviour differences exist in other areas as well:
1)                                                                           handling of unsolicited result codes while a command line is being entered (may be sent,
               suppressed, deferred, etc.);
2)                                             answering of incoming calls while a command line is being entered (may occur, be
               deferred, etc.);
3)                                                                                                  handling of loss of carrier during online command state [may be reported immediately (and
               data rate may vary), may be deferred until attempted re-entry into online data state, etc.];
4)                                               handling of undefined command numbers, and S-parameters values (may result in an
               ERROR, clamping of value to the valid range, retention of previous value, etc.);
5)                                                                                                           execution time of actions (for example,  Z command);
6)                                                                     handling of variations in command line format and editing (for example: " AT" in mixed
               case; length of command line buffer; command line too long; whether or not space
               characters and control characters are stored in the command buffer; whether or not
               unrecognized control characters are echoed; echoing of other characters prior to receipt of
               the "AT" prefix; disposition of command lines in which the DTE changed the rate, format,
               or parity; etc.);
7)                                                                                                           displaying of S-parameter values which cannot be expressed as three decimal digits;
8)                                      states of connection establishment (e.g., handshaking) in which attempts to abort a
               command by transmission of a character to the DCE may not be recognized;
9)                                                                                handling of additional characters that appear on the same command line after a semicolon
               that terminates a dial string (see 6.1.1); i.e., whether the additional characters are ignored or
               processed as commands;
10)                                                                                   carry-over of the effect of P and T dial modifiers from one dial string to the next.

6 Functions
The following descriptions of DCE functions and associated commands include information on both
mandatory and optional capabilities. All mandatory commands, parameters, and responses shall be
implemented in devices claiming conformance to this Recommendation. If an optional capability is
implemented in a DCE, the associated command(s), parameter(s), and response(s) defined in this
Recommendation shall be implemented.
For simplicity, the following descriptions use a particular syntax; alternatives may be used when
available. For example:
1)                                                                        Result codes are described in terms of their alphabetic format, except in situations where
               the setting of a parameter directly affects the format (e.g., V and Q commands). The actual
               result code issued would depend on the setting of parameters that affect result code formats.
2)                                                                                                The description of the OK result code for each command does not mention the fact that the
               result code will be deferred if any further commands appear on the same command line
               (see 5.4).
Default values that are specified for some commands have been selected to provide proper
operation of the DCE in its initial state. Implementation of the specified defaults is desirable but not
mandatory, with the exception of S3 (which has a mandatory default value of 13). Default values
for all parameters supported shall be specified by the manufacturer.

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  17

## Page 24

Some DCE functions and associated commands may be subject to national regulations. The manner
of handling such restrictions (e.g., elimination of commands, restriction on ranges of accepted
values, handling of values outside acceptable ranges, etc.) shall be determined by the manufacturer.
Table I.2 lists all commands contained in the Recommendation, sorted alphabetically.

6.1                                                                                       Generic DCE control
6.1.1                                                   Reset to default configuration
Syntax
Z[ <<value ><<>] >>
Description
This command instructs the DCE to set all parameters to their factory defaults as specified by the
manufacturer. This may include taking into consideration the settings of hardware configuration
switches or non-volatile parameter storage (if implemented). If the DCE is connected to the line, it
is disconnected from the line, terminating any call in progress.
All of the functions of the command shall be completed before the DCE issues the result code. The
DTE should not include additional commands on the same command line after the  Z command
because such commands may be ignored.
NOTE – Because this command may take into consideration the settings of switches and non-volatile
parameter storage, it does not necessarily return the DCE to a "known state". In particular, the DCE may, as
a result of execution of this command, be placed in a state in which it appears to not respond to DTE
commands, or respond in a completely different format than was being used prior to execution of the
command.
Abortability
This command may not be aborted.
Result codes
OK  If <value><              > is recognized.
                   <<        >>
ERROR If <value><<<          > is not recognized or supported. >>
An   OK result code for this command is issued using the same rate, parity, and word format as the
DTE command line containing the command, but using the new values for parameters that affect
the format of result codes (e.g., Q, V, S3, S4).
Execution time
Execution time for this action varies widely depending on manufacturer implementation. The DTE
should not assume the amount of time required to execute this command, but await a result code or
other positive indication from the DCE that it is ready to accept a command.
Implementation
Implementation of this command is mandatory. Interpretation of   <<value>> is optional and
                                                                                                  <<       >>
manufacturer-specific.
6.1.2                                                    Set to factory-defined configuration
Syntax
&F[<<value>>]
     <<        >>


18                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 25

Description
This command instructs the DCE to set all parameters to default values specified by the
manufacturer, which may take into consideration hardware configuration switches and other
manufacturer-defined criteria.
Abortability
This command may not be aborted.
Defined values
0                                                                                                                                                                                               Set parameters to factory defaults.
(other)                                                                          Reserved for manufacturer proprietary use.
Result codes
OK  If value is valid.
ERROR                                                      If value is not recognized or not supported.
An   OK result code for this command is issued using the same rate, parity, and word format as the
DTE command line containing the command, but using the factory-defined values for other
parameters that affect the format of result codes (e.g., Q,        V,        S3,        S4) and dependent upon other
commands that may follow on the same command line.
Execution time
Execution time for this action varies widely depending on manufacturer implementation. The DTE
should not assume the amount of time required to execute this command, but await a result code or
other positive indication from the DCE that it is ready to accept a command.
Implementation
Implementation of this command is mandatory. If the value specified is not recognized or
implemented, an ERROR result code is issued.
6.1.3                                                   Request identification information
Syntax
I[<<value><<     >] >>
Description
This command causes the DCE to transmit one or more lines of information text, determined by the
manufacturer, followed by a final result code.  <<value>> may optionally be used to select from
                                                                                                 <<           >>
among multiple types of identifying information, specified by the manufacturer.
NOTE – The responses to this command may not be reliably used to determine the DCE manufacturer,
revision level, feature set, or other information, and should not be relied upon for software operation. In
particular, expecting a specific numeric response to an  I0 command to indicate which other features and
commands are implemented in a DCE dooms software to certain failure, since there are widespread
differences in manufacturer implementation among devices that may, coincidentally, respond with identical
values to this command. Software implementors should use   I commands with extreme caution, since the
amount of data returned by particular implementations may vary widely from a few bytes to several thousand
bytes or more, and should be prepared to encounter  ERROR responses if the value is not recognized.
Abortability
This command is not ordinarily abortable, but may be so in some implementations.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  19

## Page 26

Execution time
Execution time is dependent on the time required to transmit the information to the DTE. The DTE
should not assume the amount of time required to execute this command, but await a result code or
other positive indication from the DCE that it is ready to accept a command.
Implementation
Implementation of this command is optional.
6.1.4                                                    Request manufacturer identification ( +GMI)
Syntax
+GMI +++
Description
This command causes the DCE to transmit one or more lines of information text, determined by the
manufacturer, which is intended to permit the user of the DCE to identify the manufacturer.
Typically, the text will consist of a single line containing the name of the manufacturer, but
manufacturers may choose to provide more information if desired (e.g., address, telephone number
for customer service, etc.).
The total number of characters, including line terminators, in the information text returned in
response to this command shall not exceed 2048 characters. Note that the information text shall not
contain the sequence "0 <CR>" (3/0, 0/13) or "OK<CR>" (4/15, 4/11, 0/13), so that DTE can avoid
false detection of the end of this information text.
Defined values
None.
Result codes
OK                                                                         In all cases.
Execution time
Execution time is dependent on the time required to transmit the information to the DTE. The DTE
should not assume the amount of time required to execute this command, but await a result code or
other positive indication from the DCE that it is ready to accept a command.
Abortability
This command is not abortable.
Implementation
Implementation of this command is mandatory.
6.1.5                                                   Request model identification (+GMM)
Syntax
+GMM +++
Description
This command causes the DCE to transmit one or more lines of information text, determined by the
manufacturer, which is intended to permit the user of the DCE to identify the specific model of
device. Typically, the text will consist of a single line containing the name of the product, but
manufacturers may choose to provide any information desired.


20                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 27

The total number of characters, including line terminators, in the information text returned in
response to this command shall not exceed 2048 characters. Note that the information text shall not
contain the sequence "0 <CR>" (3/0, 0/13) or "OK<CR>" (4/15, 4/11, 0/13), so that DTE can avoid
false detection of the end of this information text.
Defined values
None.
Result codes
OK                                                                         In all cases.
Execution time
Execution time is dependent on the time required to transmit the information to the DTE. The DTE
should not assume the amount of time required to execute this command, but await a result code or
other positive indication from the DCE that it is ready to accept a command.
Abortability
This command is not abortable.
Implementation
Implementation of this command is mandatory.
6.1.6                                                   Request revision identification (+GMR)
Syntax
+GMR +
++
Description
This command causes the DCE to transmit one or more lines of information text, determined by the
manufacturer, which is intended to permit the user of the DCE to identify the version, revision level
or date, or other pertinent information of the device. Typically, the text will consist of a single line
containing the version of the product, but manufacturers may choose to provide any information
desired.
The total number of characters, including line terminators, in the information text returned in
response to this command shall not exceed 2048 characters. Note that the information text shall not
contain the sequence "0 <CR>" (3/0, 0/13) or "OK<CR>" (4/15, 4/11, 0/13), so that DTE can avoid
false detection of the end of this information text.
Defined values
None.
Result codes
OK                                                                         In all cases.
Execution time
Execution time is dependent on the time required to transmit the information to the DTE. The DTE
should not assume the amount of time required to execute this command, but await a result code or
other positive indication from the DCE that it is ready to accept a command.
Abortability
This command is not abortable.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  21

## Page 28

Implementation
Implementation of this command is mandatory.
6.1.7                                                    Request product serial number identification (+GSN)
Syntax
+GSN +
++
Description
This command causes the DCE to transmit one or more lines of information text, determined by the
manufacturer, which is intended to permit the user of the DCE to identify the individual device.
Typically, the text will consist of a single line containing a manufacturer determined alpha-numeric
string, but manufacturers may choose to provide any information desired.
The total number of characters, including line terminators, in the information text returned in
response to this command shall not exceed 2048 characters. Note that the information text shall not
contain the sequence "0 <CR>" (3/0, 0/13) or "OK<CR>" (4/15, 4/11, 0/13), so that DTE can avoid
false detection of the end of this information text.
Defined values
None.
Result codes
OK                                                                         In all cases.
Execution time
Execution time is dependent on the time required to transmit the information to the DTE. The DTE
should not assume the amount of time required to execute this command, but await a result code or
other positive indication from the DCE that it is ready to accept a command.
Abortability
This command is not abortable.
Implementation
Implementation of this command is optional.
6.1.8                                                    Request global object identification (+GOI)
Syntax
+GOI +++
Description
This command causes the DCE to transmit one or more lines of information text, determined by the
manufacturer, which is intended to permit the user of the DCE to identify the device, based on the
ISO system for registering unique object identifiers. Typically, the text will consist of a single line
containing numeric strings delimited by period characters.
The general format of object identifiers is defined in Annex D/X.680, the encoding rules are defined
in Annex C/X.680.
The total number of characters, including line terminators, in the information text returned in
response to this command shall not exceed 2048 characters. Note that the information text shall not
contain the sequence "0 <CR>" (3/0, 0/13) or "OK<CR>" (4/15, 4/11, 0/13), so that DTE can avoid
false detection of the end of this information text.


22                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 29

Defined values
None.
Result codes
OK                                                                         In all cases.
Execution time
Execution time is dependent on the time required to transmit the information to the DTE. The DTE
should not assume the amount of time required to execute this command, but await a result code or
other positive indication from the DCE that it is ready to accept a command.
Abortability
This command is not abortable.
Implementation
Implementation of this command is optional.
6.1.9                                                    Request complete capabilities list ( +GCAP)
Syntax
+GCAP +
++
Description
This extended-format command causes the DCE to transmit one or more lines of information text in
a specific format. The content is a list of additional capabilities command  +<name>s, which is
intended to permit the user of the DCE to identify the overall capabilities of the DCE.
In particular, if the DCE implements a particular DCE control standard that uses Extended Syntax
Commands, and if that DCE control standard includes command(s) that indicate general
capabilities, the +<name>(s) of those commands shall be reported to the DCE in response to a
+GCAP command. See Table 2.

                                                                Table 2/V.250 – Examples of required ++GCAP responses ++

          +GCAP response                                                                                                                                                                                         DCE control standard                                                                                                                                                                                                                                                                                                                                                                                                                       Description +++
   +FCLASS T.class1, +F                                                                                                                                                                         Class 1 Facsimile DCE Control
                                                                               or T.class2, +F                                                                                                  Class 2 Facsimile DCE Control
   +MS                                                                                                                                                                                                                                                                                                                                                                                                      +M commands Modulation Control:
                                                                                                                                                                                                +MS and +MR commands
   +MV18S                                                                                                                                                                                                                                                                                                                   +MV18 commands V.18 Modulation Control:
                                                                                                                                                                                                +MV18S and +MV18R
   +ES                                                                                                                                                                                                                                                                                                                                                                                                                   +E commands Error Control:
                                                                                                                                                                                                +ES, +EB, +ER, +EFCS, +ETBM
   +DS                                                                                                                                                                                                                                                                                                                                                                                                              +D commands Data Compression:
                                                                                                                                                                                                +DS and +DR
For example, a data modem that supported all capabilities described in this Recommendation may
report:
+GCAP: ++++                    +MS,  +++            +ES, +++          +DS, +++          +MV18S ++


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  23

## Page 30

If that example DCE implemented other commands, they shall also be included. If that DCE
implemented stubs (e.g., +FCLASS=0 only), it may report +FCLASS as part of its +GCAP
response.
The response is not specifically limited as to number of lines of text. Note that the information text
shall not contain the sequence "0 <CR>" (3/0, 0/13) or "OK<CR>" (4/15, 4/11, 0/13), so that DTE
can avoid false detection of the end of this information text.
It is not necessary for a DTE to inquire of the +GCAP where the application is specific to a
technology, such as facsimile where the +FCLASS command would be sufficient to determine
capabilities.
Abortability
This command is not abortable.
Implementation
Implementation of this command is mandatory. The response might be null if the DCE lacks
specific capabilities commands. A DTE that is aware of a specific DCEs capabilities might elect not
to use the +GCAP command.
6.1.10                           Country of installation (+GCI)
Parameter
+GCI=+++=<==<T.35 country code><< > >>
Description
This extended syntax command is used to indicate and select the country of installation for the
DCE. If implemented, the DCE shall use this parameter to select the settings for any operational
parameters that need to be adjusted for national regulations or telephone networks. DTE may use
this value to determine country-specific functions.
If a DCE supports legal connection in only one country, then that DCE shall report only the T.35
country code for that country in response to a read or test command, and accept only that value for a
write command.
Defined values
ITU-T Rec. T.35 defines 8-bit country codes. Annex A/T.35 lists country codes, with bits 8-1 and
the country names. For use with the +GCI parameter, the value shall be the hexadecimal equivalent
of the T.35 code, with bit 8 treated as the most significant bit and bit 1 treated as the least
significant bit. Example values: 00 for Japan; 0A for Austria; 64 for Lebanon; C4 for Zimbabwe.
Recommended default
If the DCE is specified for use in only one country, that country code shall be the default.
Otherwise, the recommended default is the expected country of sale or first installation. DCE may
use hardware means to select the country of installation, in which case the DCE shall use that to
determine the default value.
Read syntax
+GCI? +++
The DCE shall transmit information text which reports the hexadecimal numeric value
corresponding to the current setting:
+GCI:<+++<current country code><<     > >>
e.g., +GCI:3D indicates that the DCE is set for France.

24                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 31

Test syntax
+GCI=+++    =? ==
The DCE shall transmit information text which reports the list of numerical values corresponding to
the country or countries that are supported:
+GCI:(<+++     <country code ><<              > [,<country code> [,<country code>...]]) >>
e.g., +GCI:(20,73,B5) indicates that the DCE can be set for Canada, Mexico or the United States.
Implementation
This command shall be implemented in DCE that can be installed in more than one country, and
which need to adjust operating parameters in order to function correctly in those countries.

6.2                                                                                       DTE-DCE interface commands
The parameters defined in this clause control the operation of the interface between the DTE and
DCE.
6.2.1                                                   Command line termination character
Parameter
S3
Description
This S-parameter represents the decimal IA5 value of the character recognized by the DCE from the
DTE to terminate an incoming command line. It is also generated by the DCE as part of the header,
trailer, and terminator for result codes and information text, along with the S4 parameter (see the
description of the V parameter for usage).
The previous value of S3 is used to determine the command line termination character for entry of
the command line containing the S3 setting command. However, the result code issued shall use the
value of S3 as set during the processing of the command line. For example, if S3 was previously set
to 13 and the command line "ATS3==30" is issued, the command line shall be terminated with a CR ==
character (IA5 0/13), but the result code issued will use the character with the ordinal value 30
(IA5 2/14) in place of the CR.
Defined values
0 to  127                                                                                Set command line termination character to this value.
Mandatory default setting
13                                                                                                   Carriage return character (CR, IA5 0/13).
Implementation
Implementation of this parameter is mandatory. If the specified value is not recognized, an     ERROR
result code is issued.
6.2.2                                                   Response formatting character
Parameter
S4
Description
This S-parameter represents the decimal IA5 value of the character generated by the DCE as part of
the header, trailer, and terminator for result codes and information text, along with the  S3 parameter
(see the description of the V parameter for usage).


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  25

## Page 32

If the value of S4 is changed in a command line, the result code issued in response to that command
line will use the new value of S4.
Defined values
0 to 127                                                                      Set response formatting character to this value.
Recommended default setting
10                                                                                                    Line feed character (LF, IA5 0/10).
Implementation
Implementation of this parameter is mandatory. If the specified value is not recognized, an     ERROR
result code is issued.
6.2.3                                                    Command line editing character
Parameter
S5
Description
This S-parameter represents the decimal IA5 value of the character recognized by the DCE as a
request to delete from the command line the immediately preceding character (see 5.2.2).
Defined values
0 to  127                                                                                Set command line editing character to this value.
Recommended default setting
8                                                                                                                            Backspace character (BS, IA5 0/8).
Implementation
Implementation of this parameter is mandatory. If the specified value is not recognized, an     ERROR
result code is issued.
6.2.4 Command echo
Parameter
E[ <<value>>]
    <<           >>
Description
The setting of this parameter determines whether or not the DCE echoes characters received from
the DTE during command state and online command state (see 5.2.3).
Defined values
0                                                                                                                           DCE does not echo characters during command state and online command state.
1                                                                                                                           DCE echoes characters during command state and online command state.
Recommended default setting
1                                                                                                                           DCE echoes characters during command state and online command state.
Implementation
Implementation of this parameter is mandatory. If the specified value is not recognized, an     ERROR
result code is issued.


26                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 33

6.2.5                                                   Result code suppression
Parameter
Q[<<value><<             >] >>
Description
The setting of this parameter determines whether or not the DCE transmits result codes to the DTE.
When result codes are being suppressed, no portion of any intermediate, final, or unsolicited result
code – header, result text, line terminator, or trailer – is transmitted. Information text transmitted in
response to commands is not affected by the setting of this parameter.
Defined values
0                                                                                                                           DCE transmits result codes.
1                                                                                                                            Result codes are suppressed and not transmitted.
Recommended default setting
0                                                                                                                           DCE transmits result codes.
Result codes
OK                                                                                                                                             If value is 0.
(none)                                                                                     If value is 1 (because result codes are suppressed).
ERROR                                                      For unsupported values (if previous value was Q0).
(none)                                                                                     For unsupported values (if previous value was Q1).
Implementation
Implementation of this parameter is mandatory. If the specified value is not recognized, an     ERROR
result code is issued.
6.2.6                                                   DCE response format
Parameter
V[ <<value><<           >] >>
Description
The setting of this parameter determines the contents of the header and trailer transmitted with
result codes and information responses. It also determines whether result codes are transmitted in a
numeric form or an alphabetic (or "verbose") form. The text portion of information responses is not
affected by this setting.
Table 3 shows the effect of the setting of this parameter on the format of information text and result
codes. All references to  <<cr><<                                      > mean "the character with the ordinal value specified in parameter  S3"; >>
all references to  <<lf><<                          > likewise mean "the character with the ordinal value specified in parameter >>
S4". See Table 3.

                                                Table 3/V.250 – Effect of V parameter on response formats

                               V0   V1
  Information responses  <text><cr><lf> <cr><lf>
                                                                                                                                                          <text><cr><lf>
  Result codes                                                                                                                                                                                                                           <numeric code><cr>                                                                                                                                                                                                                                                                                     <cr><lf>
                                                                                                                                                          <verbose code><cr><lf>


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  27

## Page 34

Defined values
0                                                                                                                            DCE transmits limited headers and trailers and numeric text.
1                                                                                                                           DCE transmits full headers and trailers and verbose response text.
Recommended default setting
1                                                                                                                           DCE transmits full headers and trailers and verbose response text.
Result codes
0                                                                                                                                                                                               If value is 0 (because numeric response text is being used).
OK                                                                                                                                             If value is 1.
4                                                                                                                                                                                                For unsupported values (if previous value was V0).
ERROR                                                      For unsupported values (if previous value was V1).
Implementation
Implementation of this parameter is mandatory. If the specified value is not recognized, an     ERROR
result code is issued.
6.2.7                                                   Result code selection and call progress monitoring control
Parameter
X[ <<value><<             >] >>
Description
The setting of this parameter determines whether or not the DCE transmits particular result codes to
the DTE. It also controls whether or not the DCE verifies the presence of a dial tone when it first
goes off-hook to begin dialling, and whether or not engaged tone (busy signal) detection is enabled.
However, this setting has no effect on the operation of the W dial modifier, which always checks
for a dial tone regardless of this setting, nor on the busy signal detection capability of the  W and @
dial modifiers. See Table 4.

                                                                  Table 4/V.250 – Defined values for X parameter

           X <<value ><<         >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   Description >>
                      0                                                                                                                                                                           CONNECT result code is given upon entering online data state. Dial tone and busy
                                                   detection are disabled.
                      1                                                                                                                                                                      CONNECT <text> result code is given upon entering online data state. Dial tone and
                                                   busy detection are disabled.
                      2                                                                                                                                                                      CONNECT <text> result code is given upon entering online data state. Dial tone
                                                   detection is enabled, and busy detection is disabled.
                      3                                                                                                                                                                      CONNECT <text> result code is given upon entering online data state. Dial tone
                                                   detection is disabled, and busy detection is enabled.
                      4                                                                                                                                                                      CONNECT <text> result code is given upon entering online data state. Dial tone and
                                                   busy detection are both enabled.

Implementation
Implementation of this parameter is mandatory. If the specified value is not recognized, an     ERROR
result code is issued.


28                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 35

6.2.8                                                    Circuit 109 (Received line signal detector) behaviour
Parameter
&C[ <<value><<            >] >>
Description
This parameter determines how the state of circuit 109 relates to the detection of received line
signal from the distant end. Changing the parameter will take effect immediately in both the
command and online command states.
In   &C1 mode of operation, circuit 109 is not turned off until all data previously received from the
remote DCE is delivered to the local DTE. However, such buffered data shall be discarded and
circuit 109 turned off if the DTE turns off circuit 108 (if &D1 or &D2 is set).
Defined values
0                                                                                                                            The DCE always presents the ON condition on circuit 109.
1                                                                                                   Circuit 109 changes in accordance with the underlying DCE, which may include functions
                  other than the physical layer functions (e.g., ITU-T Recs V.42, V.110, V.120 and V.13).
Recommended default setting
1                                                                                                   Circuit 109 changes in accordance with the underlying DCE, which may include functions
                  other than the physical layer functions (e.g., ITU-T Recs V.42, V.110, V.120 and V.13).
Implementation
Implementation of this parameter is mandatory. If the value specified is not recognized, an     ERROR
result code is issued.
6.2.9                                                    Circuit 108 (Data terminal ready) behaviour
Parameter
&D[ <<value><<            >] >>
Description
This parameter determines how the DCE responds when circuit 108/2 is changed from the ON to
the OFF condition during online data state.
Defined values
0                                                                                                                                                                                               DCE ignores circuit 108/2.
1                                                                                                                                                                                        Upon an on-to-off transition of circuit 108/2, the DCE enters online command state and
                           issues an OK result code; the call remains connected.
2                                                                                                                                                                   Upon an on-to-off transition of circuit 108/2, the DCE instructs the underlying DCE to
                           perform an orderly cleardown of the call. The disposition of any data in the DCE
                           pending transmission to the remote DCE is controlled by the  +ETBM parameter (see
                           6.5.6) if implemented; otherwise, this data is sent before the call is cleared, unless the
                           remote DCE clears the call first (in which case pending data is discarded). The DCE
                           disconnects from the line. Automatic answer is disabled while circuit 108/2 remains
                           off.
Implementation
Implementation of this parameter is mandatory. If the value specified is not recognized, an     ERROR
result code is issued. Implementation of defined values 0 and 2 is mandatory; implementation of
defined value 1 is optional.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  29

## Page 36

6.2.10                           Fixed DTE rate (+IPR)
Parameter
+IPR=+++=<==<rate><<> >>
Description
This numeric extended-format parameter specifies the data rate at which the DCE will accept
commands, in addition to 1200 bit/s or 9600 bit/s (as required in 4.3). It may be used to select
operation at rates at which the DCE is not capable of automatically detecting the data rate being
used by the DTE. Specifying a value of 0 disables the function and allows operation only at rates
automatically detectable by the DCE. The specified rate takes effect following the issuance of any
result code(s) associated with the current command line.
The <rate> specified does not apply in online data state if Direct mode of operation is selected.
Defined values
The        <<rate>> value specified shall be the rate in bits per second at which the DTE-DCE interface
      <<     >>
should operate, e.g., "19 200" or "115 200". The rates supported by a particular DCE are
manufacturer-specific; however, the +IPR parameter should permit the setting of any rate supported
by the DCE during online operation. Rates which include a non-integral number of bits per second
should be truncated to the next lower integer (e.g., 134.5 bit/s should be specified as 134; 45.45 bit/s
should be specified as 45). If unspecified or set to 0, automatic detection is selected for the range
determined as in 4.3 and the DCE manufacturer, and the character format is also forced to
autodetect, +ICF=0 (see 6.2.11).
Recommended default setting
It is recommended that the default for this parameter be the automatic detection setting (0), which
facilitates initial DTE-DCE communications.
Read syntax
+IPR? +++
The DCE shall transmit a string of information text to the DTE, consisting of:
+IPR:<+ <rate>>
++      <<     >>
e.g., +IPR:0                                                                           if set for automatic rate detection.
e.g., +IPR:9600  if set to 9600 bit/s.
Test syntax
+IPR=+++=? ==
The DCE shall transmit one or two strings of information text to the DTE, consisting of:
+IPR:(list of supported autodetectable <+++           <rate><<> values)[,(list of fixed-only  <>>   <rate><<> values)] >>
e.g., +IPR:(0,300,1200,2400,4800,9600),(19200,38400,57600)
if the DCE can autodetect up to 9600 bit/s and can support three additional higher fixed rates.
Implementation
Implementation of this parameter is optional. If the rate specified is not supported by the DCE, an
ERROR result code shall be returned.


30                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 37

6.2.11 DTE-DCE character framing (+ICF)
Parameter
+ICF=+++            =[<format>[,<parity>]] ==
Description
This extended-format compound parameter is used to determine the local serial port start-stop
(asynchronous) character framing that the DCE shall use while accepting DTE commands and
while transmitting information text and result code, if this is not automatically determined;    +IPR=0
forces   +ICF=0 (see +IPR, 6.2.10). Note that the definition of fixed character format for online data
state is for further study.
<format ><<<                   > determines the number of bits in the data bits, the presence of a parity bit, and the >>
number of stop bits in the start-stop frame.
NOTE – The semantics of this command are derived from ITU-T Rec. V.58.
<parity><<<                 > determines how the parity bit is generated and checked, if present. >>
Defined values
See Table 5.

                                                                                                       Table 5/V.250 – Character format values

                                                                       <format ><<<                 >                                                                                                                                                                                                                                                                                                                                    Valid numeric values >>
                                                                                      0                                                                                                                           auto detect
                                                                                      1                                                                                                                           8 Data; 2 Stop
                                                                                      2                                                                                                                           8 Data; 1 Parity; 1 Stop
                                                                                      3                                                                                                                           8 Data; 1 Stop
                                                                                      4                                                                                                                           7 Data; 2 Stop
                                                                                      5                                                                                                                           7 Data; 1 Parity; 1 Stop
                                                                                      6                                                                                                                           7 Data; 1 Stop
                                                                        <parity ><<<               >                                                                                                                                                                                                                                                                                                                     Defined numeric values >>
                                                                                      0                                                                                                                          Odd
                                                                                      1                                                                                                                           Even
                                                                                      2                                                                                                                           Mark
                                                                                      3                                                                                                                           Space

Recommended default setting
For <format>:                                 3
For <parity>:                                                 3
Read syntax
+ICF? +++
The DCE shall transmit a string of information text to the DTE, consisting of:
+ICF:<+                <format>>,<<parity>>
++                     <<                              >>      <<                          >>
e.g., +ICF:3,3 for the recommended defaults.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  31

## Page 38

Test syntax
+ICF=+++           =? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+ICF:(list of supported <+++                                                        <format ><<                   > values),(list of supported <>>                                                                <parity><<                 > values) >>
e.g., +ICF:(0-6),(0-3) for all defined values.
Implementation
Implementation of this parameter is optional. If the format specified is not supported by the DCE,
an ERROR result code shall be returned.
6.2.12                           DTE-DCE local flow control (+IFC)
Parameter
+IFC=+++           =[<DCE_by_DTE> [,<DTE_by_DCE>]] ==
Description
This extended-format compound parameter is used to control the operation of local flow control
between the DTE and DCE during the data state when V.42 error control is being used, or when
fallback to non-error control mode is specified to include buffering and flow control. It accepts two
numeric subparameters:
–                                                                                                                                        <<DCE_by_DTE>>, which specifies the method to be used by the DTE to control the flow of
                           <<                                                       >>
                           received data from the DCE; and
–                                                                                                                                        <<DTE_by_DCE><<>, which specifies the method to be used by the DCE to control the flow of >>
                           transmitted data from the DTE.
Defined values
See Table 6.

                                                        Table 6/V.250 –  <<DCE_by_DTE><<                                                                                   > and <>>             <DTE_by_DCE><<                                           > values >>

       <DCE_by_DTE ><<<                                   >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Description >>
                                 0                                                                                                                                                                                                         None
                                 1                                                                                                                                                                                                         DC1/DC3 on circuit 103; do not pass DC1/DC3 characters to the remote DCE
                                 2                                                                                                                                                                                                         Circuit 133 (Ready for Receiving)
                                 3                                                                                                                                                                                                         DC1/DC3 on circuit 103 with DC1/DC3 characters being passed through to the
                                                                          remote DCE in addition to being acted upon for local flow control
                      4 to 127                                                                                                                                            Reserved for future standardization
                         Other                                                                                                                                                            Reserved for manufacturer-specific use
       <DTE_by_DCE ><<<                                   >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Description >>
                                 0                                                                                                                                                                                                         None
                                 1                                                                                                                                                                                                         DC1/DC3 on circuit 104
                                 2                                                                                                                                                                                                         Circuit 106 (Clear to Send/Ready for Sending)
                      3 to 127                                                                                                                                            Reserved for future standardization
                         Other                                                                                                                                                            Reserved for manufacturer-specific use
   NOTE – DC1 is IA5 1/1; DC3 is IA5 1/3.


32                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 39

Recommended default settings
For <DCE_by_DTE>:                                                                                                                                                2
For <DTE_by_DCE>:                                                                                                                                                2
Read syntax
+IFC? +++
The DCE shall transmit a string of information text to the DTE, consisting of:
+IFC:<+++    <DCE_by_DTE><<                    >,<>><DTE_by_DCE><<                    >  >>
e.g., +IFC:2,2 for the recommended defaults.
Test syntax
+IFC=+++   =? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+IFC:(list of supported <+                         <DCE_by_DTE>> values),(list of supported  <<DTE_by_DCE>> values)
++                                                 <<                                 >>                                                         <<                                >>
e.g., +IFC:(0-3),(0-2) for all defined values.
Implementation
Implementation of this parameter is mandatory if V.42 error control or Buffered mode is provided
in the DCE; otherwise it is optional. DCEs which do not implement circuit 106 and/or circuit 133
do not need to support the value of 2 for the corresponding subparameter.
6.2.13                           DTE-DCE local rate reporting (+ILRR)
Parameter
+ILRR=+        =<<value>>
++             ==<<             >>
Description
This extended-format numeric parameter controls whether or not the extended-format
"+ILRR:<rate>" information text is transmitted from the DCE to the DTE. The   <rate> reported shall
represent the current (negotiated or renegotiated) DTE-DCE rate. If enabled, the intermediate result
code is transmitted after any modulation, error control or data compression reports are transmitted,
and before any final result code (e.g., CONNECT) is transmitted. The  <rate> is applied after the
final result code is transmitted.
The DTE-DCE port rate will change only if neither buffered mode nor error-controlled means are
enabled (+ES=x,0) and if the negotiated carrier rate (+MRR) does not match the current DTE-DCE
port rate (set by +IPR command or autodetected from the previous command line).
The format of this intermediate result code is:
+ILRR: <+++       <rate><<    > [,<>><rx_rate><<        >]               e.g.,       +ILRR: 19 200 >>
<rate><<<  > values are decimal values. The optional  <>>                                              <rx_rate><<        > value reports the rate on circuit 104 >>
(RXD), if it is different from the rate on circuit 103 (TXD).
Defined values
See Table 7.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  33

## Page 40

Table 7/V.250 – Local port rate reporting values

            <value ><<<      >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  Description >>
                     0                                                                                                                                                                              Disables reporting of local port rate (+ILRR: is not transmitted)
                     1                                                                                                                                                                              Enables reporting of local port rate (+ILRR: is transmitted)

Recommended default setting
0
Read syntax
+ILRR? +++
The DCE shall transmit a line of information text to the DTE, consisting of:
+ILRR:<+++          <current setting ><<                       > >>
For example, with the recommended default setting, the DCE could report:
+ILRR:0
Test syntax
+ILRR=+++         =? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+ILRR:(list of supported values) +++
For example, a DCE that supported all defined settings would report:
+ILRR:(0,1)
Implementation
Implementation of this parameter and the associated intermediate result codes is mandatory for
V-series data modems conforming to this Recommendation.
6.2.14                            Select Sync Transmit Clock Source (+ICLOK)
Parameter
+ICLOK=<value>
Description
This command determines how the DTE transmit clock is generated while the DCE is in the
synchronous mode.
Values
0                                                                                                                           The DCE generates transmit clock and applies it to V.24 circuit 114.
1                                                                                                                            The DCE accepts transmit clock on V.24 circuit 113 and applies it to circuit 114.
2                                                                                                                         The DCE derives transmit clock from the receive clock on V.24 circuit 115 and applies it to
                    circuit 114.
Default value
0
Read syntax
+ICLOK?


34                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 41

The DCE shall send a line of information text to the DTE:
+ICLOK: <value>
Test syntax
+ICLOK=?
The DCE shall send a line of information text to the DTE:
+ICLOK: (range of supported values)
Implementation
Optional
6.2.15                           Select Long Space Disconnect Option (+ILSD)
Parameter
+ILSD=<value>
Description
This command determines if the DCE shall disconnect a call upon receiving a long space (1.6-s
break) signal from the distant end and if the DCE shall send a long space to cause a disconnect.
If enabled, the modem shall send a 4-s break (continuous space) before performing signal converter
cleardown (if any) and before going on-hook, when instructed to hang up by the DTE.
Long Space Disconnect is applicable in Direct mode and Buffered mode.
Values
0                                                                                                                            Disable long space disconnect
1                                                                                                                            Enable long space disconnect
Default value
0
Read syntax
+ILSD?
The DCE shall send a line of information text to the DTE:
+ILSD: <value>
Test syntax
+ILSD=?
The DCE shall send a line of information text to the DTE:
+ILSD: (0,1)
Implementation
Optional
6.2.16                            Select Data Set Ready Option (+IDSR)
Parameter
+IDSR=<value>
Description
This parameter determines how V.24 circuit 107 (Data Set Ready, DSR) shall behave.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  35

## Page 42

Values
0                                                                                                                           DSR is always ON.
1                                                                                            DSR functions as defined in ITU-T Rec. V.24 and the relevant V-series Recommendation
             for the signal converter in use.
2                                                                                                                           DSR is always ON except for 5 s after disconnect.
Default value
0
Read syntax
+IDSR?
The DCE shall send a line of information text to the DTE:
+IDSR: <value>
Test syntax
+IDSR=?
The DCE shall send a line of information text to the DTE:
+IDSR: (range of supported values)
Implementation
Optional
6.2.17                           Select Synchronous Mode RTS Option (+IRTS)
Parameter
+IRTS=<value>[,<delay>]
Description
This parameter configures the operation of V.24 circuit 105 (Request to Send, RTS) and circuit 106
(Ready for Sending, or CTS), while the DCE is operating in Synchronous Mode. In any operating
mode where the DTE interface is asynchronous (i.e., Direct, Buffered, Error Control, or
Synchronous Access Modes) the setting of this parameter is ignored. In this case, circuit 105 is
assumed ON, and the actual state of this circuit, if present, is ignored. The operation of circuit 106
in this case is determined by the +IFC parameter.
NOTE – In many DCE, circuits 105 and 133 share the same signal lead. In those cases, the setting of the
+IFC parameter determines which circuit is in effect at the signal lead.
Values
0                                                             While in online state, circuit 106 tracks circuit 105 according to the relevant V-series
             Recommendation for the modulation, with an additional delay equal to the value of
             <delay>, in units of 10 ms.
1                                                                                                                           Circuit 106 is clamped ON, and circuit 105 is ignored.
Default value
0
Read syntax
+IRTS?
The DCE shall send a line of information text to the DTE:
+IRTS: <value>


36                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 43

Test syntax
+IRTS=?
The DCE shall send a line of information text to the DTE:
+IRTS: (0,1)
Implementation
Optional

6.3 Call control
This clause defines action commands and parameters used to make and maintain calls. It defines
result codes generated in executions of these action commands. It also defines one unsolicited DCE
result code, RING (see 6.3.4).
6.3.1 Dial
Syntax
D[<dial_string>][;]
Description
This command instructs the DCE to originate a call. This may include several steps, depending
upon the DCE type, such as: connecting to the line (going off-hook), waiting for the network to
indicate readiness to receive call addressing information (wait for dial tone), signalling call
addressing information to the network (dialling the number), monitoring the line for call progress
signals (e.g., busy), and instructing the underlying DCE to start the call origination procedure
(modulation handshaking).
All characters appearing on the same command line after the "D" are considered part of the call
addressing information to be signalled to the network, or modifiers used to control the signalling
process (collectively known as a "dial string"), up to a semicolon character (IA5 3/11) or the end of
the command line. If the dial string is terminated by a semicolon, the DCE does not start the call
origination procedure as defined for the underlying DCE, but instead returns to command state after
completion of the signalling of call addressing information to the network.
Any characters appearing in the dial string that the DCE does not recognize as a valid part of the
call addressing information or as a valid modifier shall be ignored. This permits characters such as
parentheses and hyphens to be included that are typically used in formatting of telephone numbers.
NOTE 1 – The behaviour of the  D command may be modified if DTE control of V.8 or V.8  bis is enabled;
refer to Annex A in this case.
Abortability
The    D command may be aborted in the manner described in 5.6.1. If the DCE is connected to the
line, it disconnects from the line in an orderly manner as required by the state of the connection.
Aborting the connection by reception of a character is generally possible at any time before the
DCE enters online data state, but may not be possible during some states of connection
establishment such as handshaking. The DCE shall issue a final result code; which result code to
issue shall be determined by the manufacturer, and may depend upon the state of the connection at
the time the character was received from the DTE. If a  CONNECT or CONNECT      <<text><<                                                                     > result >>
code is received by the DTE, this indicates that the attempt to abort the command was not
successful, possibly due to the state of connection establishment at the time the character was sent.
See Table 8.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  37

## Page 44

Table 8/V.250 – D command result codes

               Alphabetic                                                Numeric
                    (ATV1)                                                (ATV0)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              Description
   CONNECT                                                                                                                                                                                                                                                                                   1                                                                                                                                             If connection is successfully established and  X0 is selected. This
                                                                                                                result code is transmitted immediately before circuit 109 is turned
                                                                                                                on
   CONNECT  <<text ><<                                 >                                                                                                                                                 –                                                                                                                                             If connection is successfully established and  Xn is selected where >>
                                                                                                                "n" is any value other than 0. This result code is transmitted
                                                                                                                immediately before circuit 109 is turned on. The contents of    <<text ><<                                                                                                                 > >>
                                                                                                                are manufacturer-specific, and may include indication of DTE
                                                                                                                interface speed, line speed, error control and data compression
                                                                                                                techniques in use, and other information
   NO CARRIER                                                                                                                                                                                                                     3                                                                                                                                             If a connection cannot be established, or was aborted by reception
                                                                                                                of an additional character from the DTE
   ERROR                                                                                                                                                                                                                                                                                                                                                4                                                                                                                                             If issued while in online command state
   BUSY                                                                                                                                                                                                                                                                                                                                                                                         7                                                                                                                                             If busy signal detection is enabled or the  W or @ dial modifier is
                                                                                                                used, and a busy signal is detected
   NO ANSWER                                                                                                                                                                                                                                 8                                                                                                                                             If the "@" dial modifier is used, and remote ringing followed by
                                                                                                                five seconds of silence is not detected before the expiration of the
                                                                                                                connection timer defined by  S7
   NO DIALTONE                                                                                                                                                                                        6                                                                                                                                             If dial tone detection is enabled or the  W dial modifier is used, and
                                                                                                                no dial tone is detected within the associated timeout period
   OK                                                                                                                                                                                                                                                                                                                                                                                                                                          0                                                                                                                                             If command is aborted by either reception of an additional character
                                                                                                                from the DTE or by the DTE turning off circuit 108 (if &D1 or
                                                                                                                &D2 is selected; see 6.2.9), or if the dial string is terminated by a
                                                                                                                semicolon character

Execution time
Execution time for this action varies widely depending on the call origination procedure of the
underlying DCE and the time required to determine whether or not a connection is successfully
established.
Implementation
Implementation of the D command and all associated result codes is mandatory. The elements of
the dial string are discussed in the following subclauses.
The steps necessary for establishing a call are dependent upon the type of DCE in use and national
requirements.
NOTE 2 – Some applications, such as call-back security, may require a modem to originate a call using the
frequencies normally reserved for an answering modem. No dial modifier is specified in this
Recommendation for this purpose. However, it can be accomplished by terminating the D command with a
semicolon, and following the semicolon with an  A (Answer) command.
6.3.1.1 Dialling digits
Syntax
A string of 0 or more of the characters:
"0 1 2 3 4 5 6 7 8 9 * #  ++ A B C D" ++


38                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 45

Description
For each digit, the DCE signals the digit to the network as part of the call addressing sequence. For
GSTN applications, refer to ITU-T Rec. Q.23 and national regulations for information on signalling
procedures.
Optional dial modifiers and parameters can affect the signalling of call addressing information (for
example, whether pulse or DTMF signalling is to be used in GSTN applications).
Implementation
The dialling digits 0 through 9 shall be implemented. If DTMF dialling is implemented,   *,  #,  A,  B,
C, and D characters shall be implemented. If the DCE is designed to operate with network services
that translate "+" to the international access code, then " +" shall be implemented.
6.3.1.2                                                             Pause during dialling
Syntax
"," (comma, IA5 2/12)
Description
In GSTN applications, causes a pause in the signalling of addressing information (dialling). The
duration of the pause is specified by parameter S8 (see 6.3.10).
Implementation
The comma dial modifier shall be implemented.
6.3.1.3                                                              Select tone dialling (dial modifier)
Syntax
T
Description
Causes subsequent dial digits to be signalled using DTMF. The effect of the T modifier may carry
forward to subsequent D commands (i.e., once a T dial modifier is used, all subsequent dialling uses
DTMF tones until a P dial modifier or command is issued); however, it is recommended that the
DTE explicitly specify pulse or DTMF dialling with the appropriate dial modifier (  P or T) at the
beginning of each dial string.
Implementation
Implementation of this dial modifier is mandatory; however, if DTMF dialling is not implemented,
this modifier will have no effect.
6.3.1.4                                                             Select pulse dialling (dial modifier)
Syntax
P
Description
Causes subsequent dial digits to be signalled using pulse dialling. The effect of the  P modifier may
carry forward to subsequent D commands (i.e., once a P dial modifier is used, all subsequent
dialling uses pulse dialling until a  T dial modifier or command is issued); however, it is
recommended that the DTE explicitly specify pulse or DTMF dialling with the appropriate dial
modifier (P or T) at the beginning of each dial string.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  39

## Page 46

Implementation
Implementation of this dial modifier is mandatory; however, if pulse dialling is not implemented,
this modifier will have no effect.
6.3.1.5                                                              Register recall/hook flash
Syntax
"!" (exclamation point, IA5 2/1)
Description
Causes the DCE to go on-hook for a specified period of time, and then return off-hook for at least a
specified period of time before continuing with the remainder of the dial string. The specified
period of time is normally one-half second, but may be governed by national regulations.
Implementation
Implementation of this dial modifier is mandatory in devices intended for operation on the GSTN.
6.3.1.6                                                             Wait for dial tone
Syntax
W
Description
Causes the DCE to listen for dial tone on the line. If a valid dial tone is detected, the DCE continues
processing the remainder of the dial string.
If the DCE decides to abort dialling because the dial tone does not occur within the connection
timeout period specified by parameter S7, the NO DIALTONE or NO CARRIER result code is
issued and the remainder of the command line is ignored.
The DCE may, but is not required to, detect busy signal while listening for dial tone; this capability
may be conditioned upon the setting of the   X command. The BUSY or NO CARRIER result codes
may be issued if the DCE detects a busy signal while listening for dial tone; in this event, the
remainder of the command line is ignored.
Implementation
Implementation of this dial modifier is mandatory for devices intended for operation on the GSTN.
The amount of time that dial tone must be present to be considered "detected" may be governed by
national regulations, and in the absence of such regulations is manufacturer-specific.
6.3.1.7                                                             Wait for quiet answer
Syntax
@ (at sign, IA5 4/0)
Description
Causes the DCE to listen for remote ringing, followed by five seconds of silence on the line. If
silence is detected for this period, the DCE continues processing the remainder of the dial string.
If the DCE decides to abort dialling because the required period of silence does not occur within the
timeout period specified by parameter S7, the NO ANSWER or NO CARRIER result code is
issued and the remainder of the command line is ignored.
The DCE may, but is not required to, detect busy signal while listening for silence; this capability
may be conditioned upon the setting of the   X command. The BUSY or NO CARRIER result codes


40                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 47

may be issued if the DCE detects a busy signal while listening for silence; in this event, the
remainder of the command line is ignored.
Implementation
Implementation of this dial modifier is mandatory for devices intended for operation on the GSTN.
The duration of the period of silence for which the DCE listens may be governed by national
regulations.
6.3.1.8               Invoke stored string
Syntax
S=<location>
Description
Causes the string stored at <location> by the +ASTO command to be processed.
The S is followed by "=<location>" where <location> is in the range 0 to one (less than the number
of storage locations). The last digit of <location> is recognized by the presence of a non-digit
character or the end of the command line. Any characters on the command line after the last digit of
<location> shall be ignored. Other (non-stored) dial string characters may precede the S in the
command line.
An out of range value for <location> shall cause an ERROR result code. If the character
immediately following the S is not an "=" or there is no digit following the "=", then all characters
after the S are ignored, and stored number 0 is dialled.
Implementation
Implementation of this dial modifier is optional.
6.3.2                                                    Select tone dialling (command)
Syntax
T
Description
Causes subsequent D commands to assume that DTMF dialling is to be used unless otherwise
specified. Once a T command is used, all subsequent dialling uses DTMF tones until a P command
or dial modifier is issued.
Implementation
Implementation of this command is mandatory; however, if DTMF dialling is not implemented, this
command will have no effect.
6.3.3                                                   Select pulse dialling (command)
Syntax
P
Description
Causes subsequent D commands to assume that pulse dialling is to be used unless otherwise
specified. Once a P command is used, all subsequent dialling uses pulse dialling until a   T command
or dial modifier is issued.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  41

## Page 48

Implementation
Implementation of this command is mandatory; however, if pulse dialling is not implemented, this
modifier will have no effect.
6.3.4                                                   Incoming call indication
The Incoming Call Indication is an unsolicited result code.
Syntax
RING                                                                                          Alphabetic form (ATV1).
2                                                                                                                                                                                               Numeric form (ATV0).
Description
This result code is issued by the DCE to report an incoming call to the DTE. Interpretation of
indications from the network to determine what constitutes a "ring" is defined by national
regulations. This result code should be repeated each time the network repeats the incoming call
indication.
The transmitting of RING result codes from the DCE to the DTE may be suppressed during
command entry and execution (see 5.8.2). Circuit 125, if provided, may be unaffected by the status
of command entry and execution and continue to indicate incoming calls even though transmitting
of RING result codes is suppressed.
Implementation
Implementation of this result code is mandatory.
6.3.5 Answer
Syntax
A
Description
This command instructs the DCE to immediately connect to the line and start the answer sequence
as specified for the underlying DCE.
Any additional commands that appear after  A on the same command line are ignored.
NOTE – The behaviour of the  A command may be modified if DTE control of V.8 or V.8  bis is enabled;
refer to Annex A in this case.
Abortability
The    A command may be aborted in the manner described in 5.6.1. If the DCE is connected to the
line, it disconnects from the line in an orderly manner as required by the state of the connection.
Aborting the connection by reception of a character is generally possible at any time before the
DCE enters online data state, but may not be possible during some states of connection
establishment, such as handshaking. The DCE shall issue a final result code; which result code to
issue shall be determined by the manufacturer, and may depend upon the state of the connection at
the time the character was received from the DTE. If a  CONNECT or CONNECT      <<text><<                                         > result >>
code is received by the DTE, this indicates that the attempt to abort the command was not
successful, possibly due to the state of connection establishment at the time the character was sent.
See Table 9.


42                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 49

Table 9/V.250 – A command result codes

                Alphabetic                                                       Numeric
                      (ATV1)                                                      (ATV0)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              Description
   CONNECT                                                                                                                                                                                                                                                                                   1                                                                                                                                             If connection is successfully established and  X0 is selected. This
                                                                                                                            result code is transmitted immediately before circuit 109 is turned
                                                                                                                            on
   CONNECT  <<text ><<                                       >                                                                                                                                                 –                                                                                                                                             If connection is successfully established and  Xn is selected where >>
                                                                                                                            "n" is any value other than 0. This result code is transmitted
                                                                                                                            immediately before circuit 109 is turned on. The contents of    <<text ><<                                                                                                                                     > >>
                                                                                                                            are manufacturer-specific, and may include indication of DTE
                                                                                                                            interface speed, line speed, error control and data compression
                                                                                                                            techniques in use, and other information
   NO CARRIER                                                                                                                                                                                                                     3                                                                                                                                             If a connection cannot be established, or was aborted by reception
                                                                                                                            of an additional character from the DTE
   ERROR                                                                                                                                                                                                                                                                                                                                                4                                                                                                                                             If issued while in online command state
   OK                                                                                                                                                                                                                                                                                                                                                                                                                                          0                                                                                                                                             If command is aborted by either reception of an additional character
                                                                                                                            from the DTE or by the DTE turning off circuit 108 (if &D1 or
                                                                                                                            &D2 is selected; see 6.2.9), or if the dial string is terminated by a
                                                                                                                            semicolon character

Execution time
Execution time for this action varies widely depending on the answer sequence of the underlying
DCE and the time required to determine whether or not a connection is successfully established.
Implementation
Implementation of this command is mandatory.
6.3.6 Hook control
Syntax
H[<<value>>]
         <<                      >>
Description
This command instructs the DCE to disconnect from the line, terminating any call in progress. All
of the functions of the command shall be completed before the DCE issues any result code.
NOTE – When used with modem-on-hold procedures per V.92, the call may be terminated without
disconnecting from the line.  Other V.250 commands such as AT+PMHF may then be used to cause the
PSTN to switch to another line for placing another outgoing call or accepting another incoming call.
Abortability
This action may not be aborted.
Defined values
0                                                                                                                           Disconnect from line and terminate call.
Result codes
OK                                                                                                                                             The result code is issued after circuit 109 is turned off, if it was previously on.
ERROR If <value><<<                                                       > is not recognized or supported. >>


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  43

## Page 50

Execution time
Execution time for this action varies widely depending on the call termination procedure of the
underlying DCE and manufacturers' implementation. The DTE should wait for the result code
before proceeding with subsequent commands.
Implementation
Implementation of this command is mandatory. If the value specified is not recognized or
implemented, an ERROR result code shall be generated.
6.3.7                                                   Return to online data state
Syntax
O[<<value>>]
     <<           >>
Description
Causes the DCE to return to online data state and issue a  CONNECT or CONNECT   <<text ><<                                                                                > result >>
code.
Abortability
This command may not be aborted.
Defined values
0                                                                                                         Return to online data state from online command state. Also used to retrain after a
                      modem-on-hold transaction or to reconnect to a modem that has been placed in
                      anon-hold state per V.92.
(other)                                                                          Reserved for manufacturer proprietary use.
Result codes
See Table 10.

                                                   Table 10/V.250 – O command result codes
                  CONNECT                                                                                                                                                                                                                               If connection is successfully resumed and X0 is selected
                  CONNECT <text>                                                                                           If connection is successfully resumed and Xn is selected
                                                             where "n" is any value other than 0
                  NO CARRIER                                                                                                                                                                If connection is not successfully resumed
                  ERROR                                                                                                                                                                                                                                                                                           If <value> is not recognized or supported

Implementation
Implementation of this command is mandatory.
6.3.8 Automatic answer
Parameter
S0
Description
This S-parameter controls the automatic answering feature of the DCE. If set to 0, automatic
answering is disabled. If set to a non-zero value, the DCE shall cause the DCE to answer when the
incoming call indication (ring) has occurred the number of times indicated by the value (see 6.1.2).
For example, in GSTN modem applications, setting this parameter to 1 will cause the modem to
answer an incoming call on the first ring.


44                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 51

Defined values
0                                                                                                                                                                                                 Automatic answering is disabled.
1 to  255                                                                                Enable automatic answering on the ring number specified.
Recommended default setting
0                                                                                                                                                                                                 Automatic answering is disabled.
Implementation
Implementation of this parameter is mandatory. The value 0 shall be supported (for interworking
with DTEs that wish to disable automatic answering); values other than 0 may be supported.
National regulations may limit the allowable non-zero values.
6.3.9                                                   Pause before blind dialling
Parameter
S6
Description
This parameter specifies the amount of time, in seconds, that the DCE shall wait between
connecting to the line and signalling call addressing information to network (dialling), when dial
tone detection is not implemented or enabled.
Defined values
2 to 10                                                                                  Number of seconds to wait before blind dialling.
Recommended default setting
2                                                                                                                                                                                               Wait two seconds before blind dialling.
Implementation
Implementation of this parameter is mandatory. However, the effect of settings may be governed by
national regulations (some countries may not permit blind dialling, or place a limit on the maximum
pause before dialling begins).
6.3.10                           Connection completion timeout
Parameter
S7
Description
This parameter specifies the amount of time, in seconds, that the DCE shall allow between either
answering a call (automatically or by the  A command) or completion of signalling of call
addressing information to network (dialling), and establishment of a connection with the remote
DCE. If no connection is established during this time, the DCE disconnects from the line and
returns a result code indicating the cause of the disconnection (see the descriptions of the  A and D
commands and related dial modifiers for more information).
Defined values
1 to  255                              Number of seconds in which connection must be established or call will be
                            disconnected.
Implementation
Implementation of this parameter is mandatory. The effect of settings may be governed by national
regulations.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  45

## Page 52

6.3.11                            Comma dial modifier time
Parameter
S8
Description
This parameter specifies the amount of time, in seconds, that the DCE shall pause, during signalling
of call addressing information to the network (dialling), when a " ," (comma) dial modifier is
encountered in a dial string.
Defined values
0                                                                                                                                                                                               DCE does not pause when "," encountered in dial string.
1 to 255                                                                     Number of seconds to pause.
Recommended default setting
2                                                                                                                                                                                               DCE pauses two seconds when "," is encountered.
Implementation
Implementation of this parameter is mandatory. The effect of settings may be governed by national
regulations.
6.3.12                            Automatic disconnect delay
Parameter
S10
Description
This parameter specifies the amount of time, in tenths of a second, that the DCE will remain
connected to the line (off-hook) after the DCE has indicated the absence of received line signal. If
the received line signal is once again detected before the time specified in  S10 expires, the DCE
remains connected to the line and the call continues.
Defined values
1 to 254                                                                     Number of tenths of a second of delay.
Implementation
Implementation of this parameter is mandatory. Effect of some settings may be governed by
national regulations.
6.3.13 Monitor speaker loudness
Parameter
L[ <<value><<   >] >>
Description
This parameter controls the volume of the monitor speaker. The specific loudness level indicated by
"low", "medium", and "high" is manufacturer-specific, although they are intended to indicate
increasing volume.


46                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 53

Defined values
See Table 11.

                                                                                                              Table 11/V.250 – Speaker loudness values

                               <value ><                >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  Description
                               <<                       >>
                                            0                                                                                                                                                                     Low speaker volume
                                            1                                                                                                                                                                     Low speaker volume
                                            2                                                                                                                                                                     Medium speaker volume
                                            3                                                                                                                                                                     High speaker volume

Implementation
Implementation of this parameter is mandatory; however, if there is no monitor speaker, if the
volume of the speaker is fixed, or if the volume is controllable only via a hardware control, the
setting of this parameter will be ignored.
6.3.14 Monitor speaker mode
Parameter
M[<<value><<                            >] >>
Description
This parameter controls when the monitor speaker is on. The speaker shall be off while the DCE is
on-hook, and may be on when the DCE is off-hook, depending on the setting of this parameter. If
the setting of this parameter is changed while the DCE is already off-hook, it is desirable that the
speaker be immediately set to reflect the new setting.
Defined values
See Table 12.

                                                                                                                    Table 12/V.250 – Speaker mode values

                     <value ><<<               >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  Description >>
                                  0                                                                                                                                                                                       Speaker is always off
                                  1                                                                                                                                                                                       Speaker on until DCE informs DCE that carrier has been detected
                                  2                                                                                                                                                                                       Speaker is always on when DCE is off-hook

Implementation
Implementation of this parameter is mandatory; however, if there is no monitor speaker, the setting
of this parameter will be ignored.
6.3.15                           Store telephone number (+ASTO)
Parameter
+ASTO=<location>,<dial_string>
Description
This parameter stores dialing strings, which may be invoked later by the  S=<location> dial modifier
(see 6.3.1.8).


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  47

## Page 54

The following characters are storable in dial strings:
0123456789ABCD ##*+##            ,"TPW@!;
Other characters are ignored and not stored. Disposition of characters following a ";" dial modifier
in a dial string is not specified. However, it is recommended that if such characters are not stored,
they should be ignored.
Lower-case letters entered are converted into upper case for storage. If the string of "storable"
characters will not fit into the available space, then no change to the pre-existing stored string will
occur. The command shall return the ERROR result code.
The double-quote (") character, as such, is not permitted in a string constant and must be replaced
by the combination "\22" in <dial_string>. However, the actual (") character is stored (see 5.4.2.2).
Values
location:                                                           0 to (one less than maximum number of storage locations).
dial_string:        the stored phone number.
Default values
No values stored.
Read syntax
+ASTO?
The DCE shall return the location numbers and dial string, one pair per line for each location that
has a value stored, for example:
+ASTO: 0,555-1234
+ASTO: 3,555-4321
Test syntax
+ASTO=?
The DCE shall return the valid range of location numbers and the maximum length of a dial string,
for example:
+ASTO: (0-3),(20)

6.4                                                                                       Modulation control commands
This clause defines control commands for data modems and for modems defined for use in text
telephones in ITU-T Rec. V.18.
6.4.1                                                   Modulation selection (+MS)
Parameter
+MS=+++=[<==<carrier><<>[,<>><automode><<   >[,<>><min_rate ><<>[,<>><max_rate><<   >[,<>><min_rx_rate><<    >[,<>><max_rx_rate><<    >]]]]]>>
           ]
Description
This extended-format compound parameter is used to control the manner of operation of the
modulation capabilities in the DCE. For DCE that supports a primary and an auxiliary channel, this
parameter applies to the primary channel. It accepts six subparameters:
–                                                                                                                                        <<carrier>>, a string which specifies the preferred modem carrier to use in originating or
           <<           >>
           answering a connection. <carrier> values are strings of up to eight characters, consisting
           only of numeric digits and upper-case letters. <carrier> values for ITU standard


48                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 55

modulations shall take the form: <letter><1-4 digits><other letters as needed>. Defined
                values are shown in Table 13 below.
The DCE may also automatically switch to another modulation (automode), subject to several
constraints:
–                                        The DCE might not support some other modulations, regardless of the automode
                mechanism supported.
–                                                                                                   The DCE might not be able to automatically switch from the current modulation  <carrier>
                to some other modulations, restricted by the selected modulation standard and by the DCE
                manufacturer's technology. For example, there is no standard way to automode from ITU-T
                Rec. V.32 bis to ITU-T Rec. V.27 ter.
–                                                                                                                           The DTE may disable automode operation; see <automode> below.
–                                      The DTE may constrain the range of modulations available; see <min_rate> and
                <max_rate> below.
–                                                                       The DTE may selectively disable some modulations by reading, editing and writing the
                +MA parameter (see 6.4.2).
If the DTE issues a +MS=<carrier> command to the DCE, and if the DCE supports the +MA
parameter, the DCE shall reinitialize the  +MA parameter.
–                                                                                                                                        <<automode><<>, an optional numeric value which enables or disables automatic modulation >>
                negotiation (e.g., Annex A/V.32 bis or ITU-T Rec. V.8). The default value shall be enabled
                if it is defined for the associated modulation (e.g., ITU-T Rec. V.32 bis, ITU-T Rec. V.8 or
                ITU-T Rec. V.34); however, there are modulations for which there is no automatic
                negotiation defined (e.g., ITU-T Rec. V.26 bis).
–                                                                                                                                        <<min_rate><<>    and      <>><min_rx_rate><<>, optional numeric values which specify the lowest value >>
                at which the DCE may establish a connection. If unspecified (set to 0), they are determined
                by the modulation means selected in the <carrier> and <automode> settings. Values for this
                subparameter are decimal encoded, in units of bit/s.
–                                                                                                                                        <<max_rate><<> and <>><max_rx_rate><<>, optional numeric values which specify the highest value >>
                at which the DCE may establish a connection. If unspecified (set to 0), they are determined
                by the modulation means selected in the  <carrier> and <automode> settings and by the
                current DTE-DCE rate. Non-zero values for this subparameter are decimal encoded, in
                units of bit/s.
–                                                                                                                                        <<min_rx_rate><<> and <>><max_rx_rate ><<> may be used to condition distinct limits for the >>
                receive direction as distinct from the transmit direction. For example, these can be used to
                select either direction for asymmetric modulations like ITU-T Rec. V.23 with constant
                carrier.
NOTE 1 – ITU-T Rec. V.34 has provisions for selectively enabling modulation rates in any combination,
selectively disabling any rate. Future versions of this Recommendation may define additional optional
subparameters to control this V.34 feature.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  49

## Page 56

Defined values
For  <<carrier><<                                        >: >>

                                                                                         Table 13/V.250 – Standard modulation  <<carrier><<                                                                                                                                                                                > strings >>

                               <carrier><<<                        >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   Description >>
                                                                                                                                                                                                                                                                                                 ITU-T standard modulations
                                           V21                                                                                                                                                                                                       ITU-T Rec. V.21
                                           V22                                                                                                                                                                                                       ITU-T Rec. V.22
                                        V22B                                                                                                                                                                                         ITU-T Rec. V.22 bis
                                        V23S                                                                                                                                                                                           ITU-T Rec. V.23, with Switched carrier, TDM
                                       V23C                                                                                                                                                                                       ITU-T Rec. V.23, with Constant carrier, asymmetric FDM
                                        V26B                                                                                                                                                                                         ITU-T Rec. V.26 bis
                                    V27TC                                                                                                                                                                         ITU-T Rec. V.27 ter, with Constant carrier, FDM
                                           V32                                                                                                                                                                                                       ITU-T Rec. V.32
                                        V32B                                                                                                                                                                                         ITU-T Rec. V.32 bis
                                           V34                                                                                                                                                                                                       ITU-T Rec. V.34
                                           V90                                                                                                                                                                                                       ITU-T Rec. V.90
                                           V91                                                                                                                                                                                                       ITU-T Rec. V.91
                                           V92                                                                                                                                                                                                       ITU-T Rec. V.92
    NOTE – Manufacture proprietary strings may be defined; they shall not begin with the "V" character.
For  <<automode>>:
                  <<                                                   >>


                                                 0                                                                                                                                                                                                                                  Disabled
                                                 1                                                                                                                                                                                                                                  Enabled, with ITU-T Rec. V.8 or Annex A/V.32  bis where applicable

Recommended default settings
For <carrier>:                                   Manufacturer-specific
For <automode>:                                                    1                                                                                                                                                  (If  possible)
For <min_rate>:                                                                               0
For <max_rate>:                                                              0                                                                                                                                      Maximum supported by <carrier>
For <min_rx_rate>:      0                                                                                                                                                  If  implemented
For <max_rx_rate>: 0                                                                                                                                                  If  implemented
Read syntax
+MS? +++
The DCE shall transmit a string of information text to the DTE, reporting the current     +MS
subparameter settings, consisting of:
+MS: <+                      <carrier>>,<<automode >>,<<min_rate>>,<<max_rate>>,<<min_rx_rate >>,<<max_rx_rate>>
++                           <<                                     >>       <<                                                   >>       <<                                               >>       <<                                                 >>       <<                                                               >>       <<                                                                 >>
NOTE 2 – The current active settings are reported under control of the +MR parameter.
Optional subparameters do not need to be reported if not implemented or set to 0.


50                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 57

e.g.,         +MS: V32B,1,1200,14400 if set to ITU-T Rec. V.32 bis, automode, explicit limits, but no
distinct receive and transmit rate limits.
Test syntax
+MS=? +
++
The DCE shall transmit a string of information text to the DTE, consisting of:
+MS: (list of supported  <+++                   <carrier><<    > values),(list of supported  <>>                      <automode ><<       > values),(list of >>
supported                          <<min_rate><<> values),(list of supported  <>>                <max_rate><<       > values),(list of supported >>
<min_rx_rate><          > values), (list of supported  <<max_rx_rate>> values)
<<                      >>                                                 <<                       >>
Optional subparameters do not need to be reported if not implemented in the DCE.
For example, a DCE that supported the following modulations: ITU-T Recs V.21, V.22, V.22 bis,
V.32 and V.32 bis, with Automode, could report:
+MS: (V21,V22,V22B,V32,V32B),(0,1),(0,300-14400),(0,300-14400)
Implementation
Implementation of this parameter is mandatory for V-series data modems conforming to this
Recommendation.
6.4.2                                                    Modulation automode control (+MA)
Parameter
+MA=+++  =[<==<carrier><<   >[,<>><carrier><<   >[,<>><carrier><<  >[,...]]]] >>
Description
This extended-format compound parameter is a list of modulations that the DCE may use to connect
with the remote DCE in Automode operation, for answering or originating data calls, as additional
alternatives to the modulation specified in +MS=<carrier>. The use of automode is controlled by the
+MS=,<automode> subparameter.
This parameter is an optional extension to the  +MS command (see 6.4.1). The implied highest
priority modulation is specified in the <carrier> subparameter for the +MS command. As an
extension of the +MS command, this parameter is reset to the manufacturer-determined default
setting whenever +MS=<carrier> is changed by the DTE, subject to the constraints listed below
under recommended defaults.
If the DTE writes values to +MA that are not supported for the current +MS=<carrier> setting, the
DCE shall return ERROR.
If the automode priority has any meaning in context of the modulations specified (or depending on
the availability of general mechanisms like ITU-T Rec. V.8), the order of   <carrier> values
determines priority.
<carrier> values omitted are not available for Automode negotiation, even if the DCE is capable of
them. For example, if <carrier> value V26T (ITU-T Rec. V.26 ter) is omitted from the  +MA list,
this means that the DCE is not configured to automatically switch to this modulation, given the
current setting of the +MS=<carrier> subparameter, even if the DCE is capable of ITU-T
Rec. V.26 ter.
Defined values
Valid     <carrier> values are defined in Table 13. +MA takes a variable number of <carrier> values,
limited to those <carrier> values indicated by the DCE in response to a +MS=? command


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  51

## Page 58

(see 6.4.1). If the DTE includes any <carrier> values that the DCE does not support, the DCE shall
return an ERROR final result code.
Recommended default settings
This is manufacturer determined, each time  +MS=<carrier> is changed.
The valid settings for +MA are constrained by five factors:
–                                                                                                                            the modulation types supported in the DCE;
–                                                                                                                             the current modulation selected in +MS;
– the current <max_rate> and <max_rx_rate> selected in +MS;
– the current <min_rate> and <min_rx_rate> selected in +MS;
–                                                                                                                           by the DCE's technology for automatic modulation selection.
For example, if a DCE supported all V-series standard modulations from ITU-T Rec. V.21 up to
ITU-T Rec. V.34, but if +MS=V32B (ITU-T Rec. V.32 bis) and the only Automode technology
supported in the DCE is Annex A/V.32 bis, then this device might only apply Automode between
ITU-T Recs V.32 bis, V.32 and V.22 bis. For that example, the default would be +MA=V32,V22B.
If the <min_rate> subparameter was set to 9600, then ITU-T Rec. V.22 bis could not be an available
Automode choice, and the default would be  +MA=V32.
Read syntax
+MA? +++
The DCE shall transmit a line of information text to the DTE, consisting of:
+MA: <+  <current list of <<carrier>> values>><<CR>>
++       <<                      <<           >>          >><<     >>
For example, a DCE capable of Annex A/V.32 bis Automode operation and set for
+MS=V32B,1,2400,14400; the DCE could report:
+MA=+  =V32
++     ==
Test syntax
+MA=+++=? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+MA: (list of supported  <+++       <carrier><<  > values) >>
For example, a DCE that is set for a top modulation of V.34 (+MS=V34,1,300,28800) with V.8
negotiation and several symmetric duplex modulations could report:
+MA: (V32B,V32,V26B,V22B,V22,V21)
which indicates ability to Automode to ITU-T Recs V.32 bis, V.32, V.26 ter, V.22 bis, V.22 and
V.21 in that order of preference.
Implementation
This command is optional.
6.4.3                                                   Modulation reporting control (+MR)
Parameter
+MR +++


52                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 59

Description
This extended-format numeric parameter controls whether or not the extended-format
+MCR:<carrier> and +MRR:<rate> intermediate result codes are transmitted from the DCE to the
DTE. The <carrier> reported shall represent the current (negotiated or renegotiated) modulation
<carrier>. If enabled, the intermediate result codes are transmitted at the point during connect
negotiation (handshaking) at which the DCE has determined which modulation and rate will be
used, before any Error Control or Data Compression reports are transmitted, and before the
intermediate result code CONNECT is transmitted.
The format of this information text is:
+MCR: <+++            <carrier ><<          >    e.g., >>                              +MCR: V32B
+MRR: <+++            <rate><<      >[,<>> <rx_rate><<            >] e.g., >>          +MRR: 14400
<carrier><            > string values are defined in Table 13.
<<                    >>
<rate><<<     > values are the decimal transmit rates in bits/s, or set to 0 if negotiation failed (e.g., V.32 bis >>
cleardown).
<rx_rate><<<           > may be reported if the modulation negotiated has a different rate for the RXD channel >>
than for the transmit channel.
Defined values
See Table 14.

                                                                Table 14/V.250 – Modulation reporting values

        <value ><        >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  Description
        <<               >>
                 0                                                                                                                                                Disables reporting of modulation connection (  +MCR: and +MRR: are not transmitted)
                 1                                                                                                                                                Enables reporting of modulation connection (  +MCR: and +MRR: are transmitted)

Recommended default setting
0
Read syntax
+MR? +
++
The DCE shall transmit a line of information text to the DTE, consisting of:
+MR:<+++        <current setting><<                        > >>
For example, with the recommended default setting, the DCE could report:
+MR:0
Test syntax
+MR=+++      =? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+MR:(list of supported values) +
++
For example, a DCE that supported all defined settings would report:
+MR:(0,1)


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  53

## Page 60

Implementation
Implementation of this parameter and associated intermediate result codes is mandatory for V-series
data modems.
6.4.4                                                   V.18 Selection (+MV18S)
Parameter
+MV18S=+++                       =[<==   <mode><<                  >[,<>>     <dflt_ans_mode ><<                                           >[,<>>    <fbk_time_enable><<                                                  >[,<>>    <ans_msg_enable><<                                                  >[,<probing_en>>>
                             ]]]]]
Description
This extended-format compound parameter is used to control the manner of operation of the V.18
capabilities (if present) in the DCE. It accepts five numeric subparameters:
–                                                                                                                                        <<mode><<>, which specifies the calling mode of operation; >>
–                                                                                                                                        <<dflt_ans_mode><<>, which specifies the preferred fallback mode of operation when the DCE >>
                             is operating as the answerer; and
–                                                                                                                                        <<fbk_time_enable ><<>, which specifies the enabling of re-acquisition after two seconds of no >>
                             transmission;
–                                                                                                                                        <<ans_msg_enable><<>, which specifies the enabling of the answer message as part of >>
                             continuous carrier mode probes;
–                                                                                                                                       <probing_en >>, which controls activation of the probing in answer mode. Disabling the >>
                             probing will cause the V.18 DCE to enter the automoding monitor mode when answering.
Defined values
See Table 15.

                                                                                                         Table 15/V.250 – V.18 operation modes

                      <mode>
      <dflt_ans_mode>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          Description
                                   0                                                                                                                                                                                                 Disables V.18 operation
                                   1                                                                                                                                                                                                      V.18 operation, auto detect mode (Note 1)
                                   2                                                                                                                                                                                                      V.18 operation, connect in 5-bit (Baudot) mode at 45.5 bit/s
                                 12                                                                                                                                                                                           V.18 operation, connect in 5-bit (Baudot) mode at 50 bit/s
                                   3                                                                                                                                                                                                      V.18 operation, connect in DTMF mode
                                   4                                                                                                                                                                                                      V.18 operation, connect in EDT mode
                                   5                                                                                                                                                                                                      V.18 operation, connect in V.21 mode (Note 2)
                                   6                                                                                                                                                                                                      V.18 operation, connect in V.23 mode (Note 2)
                                   7                                                                                                                                                                                                      V.18 operation, connect in Bell 103-type mode (Note 2)
                                 15                                                                                                                                                                                           V.18 operation, connect in V.21 answer mode (Note 3)
                                 16                                                                                                                                                                                           V.18 operation, connect in V.23 master mode (Note 3)
                                 17                                                                                                                                                                                           V.18 operation, connect in Bell 103 answer mode (Note 3)
   <fbk_time_enable>
                                   0                                                                                                                                                                                                       Disable
                                   1                                                                                                                                                                                                      Enable


54                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 61

Table 15/V.250 – V.18 operation modes

   <ans_msg_enable> Description
                               0                                                                                                                                                                                                       Disable
                               1                                                                                                                                                                                                      Enable
           <probing_en>
                               0                                                                                                                                                                                                      Disable the probing
                               1                                                                                                                                                                                                      Enable the probing
                               2                                                                                                                                                                                                      Initiate probing (expire Ta Timer)
   NOTE 1 – There is no option to select calling or answer mode for V.18.
   NOTE 2 – Calling mode implies transmit on channel 1 and receive on channel 2.
   NOTE 3 – Answer mode implies transmit on channel 2 and receive on channel 1.

Recommended default settings
For <mode>:                                                                                                                                                                                                                                                                                                                0
For <dflt_ans_enable>:                                                                                                           0
For <fbk_time_enable>:                                                                                        0
For <ans_msg_enable>:                                                                                              0
For <probing_en>:                                                                                                                                                                                                1
Read syntax
+MV18S? +
++
The DCE shall transmit a line of information text to the DTE, consisting of:
+MV18S: <+++                       <mode ><<              >,<>> <dflt_ans_mode><<                                       >,<>> <fbk_time_enable><<                                           >,<>> <ans_msg_enable ><<                                          > >>
For example, with the recommended default settings, the DCE could report:
+MV18S: 0,0,0,0
which selects disabled mode, with re-acquisition after inactivity disabled.
Test syntax
+MV18S=+++                    =? ==
The DCE shall transmit a line of information text to the DTE, consisting of:
+MV18S:(range of supported <+++                                                                               <mode ><<              >s),(range of supported <>>                                                               <dflt_ans_mode><<                                      >s),(range of >>
supported  <<fbk_time_enable ><<                                                                  >s),(range of supported  <>>                                                       <ans_msg_enable><<                                          >s) >>
For example, a completely capable DCE could report:
+MV18S:(0-7),(0-4),(0,1),(0,1)
Implementation
Implementation of this parameter is mandatory if ITU-T Rec. V.18 is implemented in the DCE.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  55

## Page 62

6.4.5                                                   V.18 Reporting control (+MV18R)
Parameter
+MV18R=+++             =<==<value ><<      > >>
Description
This extended-format numeric parameter controls whether or not the extended-format " +MV18R:"
result code is transmitted from the DCE to the DTE. The  +MV18:<type> reported shall represent
the current (negotiated or renegotiated) V.18 <type>. If enabled, the result code is transmitted at the
point during connect negotiation (handshaking) at which the DCE has determined which
modulation and format will be used (if any) for ITU-T Rec. V.18. The format of this result code is
the following (see Table 16):

                                   Table 16/V.250 – V.18 connection report intermediate result codes
                             +MV18: 5BIT50                                                                                                                    Indicates connection with 5-bit mode
                             +MV18: 5BIT45                                                                                                                    Indicates connection with 5-bit mode
                                +MV18: EDT                                                                                                                                             Indicates connection with EDT
                              +MV18: DTMF                                                                                                                          Indicates connection with DTMF
                       +MV18: V21C (Note)                                                                         Indicates connection with ITU-T Rec. V.21
                       +MV18: V21A (Note)                                                                     Indicates connection with ITU-T Rec. V.21
                              +MV18: V23M                                                                                                                                Indicates connection with ITU-T Rec. V.23 in Master Mode
                                                                                   (sending on 1200 bit/s, receiving on 75 bit/s)
                               +MV18: V23S                                                                                                                                         Indicates connection with ITU-T Rec. V.23 in Slave Mode,
                                                                                   (sending on 75 bit/s, receiving on 1200 bit/s)
                      +MV18: B103C (Note)                                                            Indicates connection with Bell 103-type modulation
                      +MV18: B103A (Note)                                                             Indicates connection with Bell 103-type modulation
                                 +MV18: V18                                                                                                                                                     Indicates both DCEs are in ITU-T Rec. V.18
                   NOTE – "C" indicates modem is in call mode, i.e., transmitting on channel 1 and receiving
                   on channel 2. "A" indicates modem is in answer mode.
The +MV18 result code, if enabled, is issued by the DCE in place of any other modulation reporting
when V.18 connection occurs (e.g., +MCR). If the +MV18 parameters are set to disable V.18
operation, the effect is to override an enable setting of +MV18R.
Defined values
See Table 17.

                                                                    Table 17/V.250 – V.18 Reporting values

                                         <value ><<<     >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 Description >>
                                                 0                                                                                                                                                                              Disables reporting of ITU-T Rec. V.18 connection
                                                 1                                                                                                                                                                              Enables reporting of ITU-T Rec. V.18 connection

Recommended default setting
0
Read syntax
+MV18R? +++


56                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 63

The DCE shall transmit a line of information text to the DTE, consisting of:
+MV18R: <+++          <current setting ><<               > >>
For example, with the recommended default setting, the DCE could report:
+MV18R: 0
Test syntax
+MV18R=+++         =? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+MV18R: (list of supported values) +
++
For example, a DCE that supported both defined settings would report:
+MV18R: (0,1)
Implementation
Implementation of this parameter (and the   +MV18 result codes) is mandatory if ITU-T Rec. V.18 is
implemented in the DCE.
6.4.6                                                   V.18 Answering message editing (+MV18AM)
Parameter
+MV18AM=+++             = [<==<message ><<        >] >>
Description
This extended-format string parameter contains the answer message stored in the DCE and used as
a probe (see 6.4.7) in the automode answer mode. The command  +MV18AM="" sets this to the null
string. The command +MV18AM=<message> appends <message> to the stored string, up to the
maximum length supported by the DCE.
Defined values
message: the stored message as a string constant.
Recommended default setting
Manufacturer-specific, dependent on country of installation.
Read syntax
+MV18AM? +++
The DCE shall transmit the text of the stored answer message to the DTE. For example, for
installation in an English speaking country, it could report:
+MV18AM: "Hello, GA"
Test syntax
+MV18AM=+++             =? ==
The DCE shall transmit the maximum message length allowable, as a decimal value. For example,
if the DCE could handle a maximum message of 100 characters, it shall report:
+MV18AM:100
Implementation
Implementation of this parameter is mandatory if ITU-T Rec. V.18 is implemented in the DCE.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  57

## Page 64

6.4.7                                                   Order of probes (+MV18P)
Parameter
+MV18P=+++            =[<==<probe_mode><<                     >[,<>><probe_mode><<                     >[,...]]] >>
Description
This extended-format compound parameter is a list of text telephone modes that specify the order of
the modes in which to send probes during the automoding answering process. The order is of
importance for minimizing the connect time when answering calls. The  <probe_mode> values 2-7,
are defined in Table 18. The order determines the probing order, with the first value specified
representing the first probe tried.
Defined values
See Table 18.

                                                                                Table 18/V.250 – Probe order

                               <probe_mode ><<<               >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      Description >>
                                               2                                                                                                                                                                                   Send probe message in 5-bit (Baudot) mode
                                               3                                                                                                                                                                                   Send probe message in DTMF mode
                                               4                                                                                                                                                                                   Send probe message in EDT mode
                                               5                                                                                                                                                                                   Send ITU-T Rec. V.21 carrier as a probe
                                               6                                                                                                                                                                                   Send ITU-T Rec. V.23 carrier as a probe
                                               7                                                                                                                                                                                   Send Bell 103 carrier as a probe

Recommended default setting
Manufacturer-specific, based on national regulations or common practice.
Read syntax
+MV18P? +
++
The DCE shall transmit a line of information text to the DTE, consisting of:
+MV18P: <+++             <current priority list of probe_mode settings ><<                                                               > >>
For example, if the DCE is set to support the above list in that priority order, the DCE could report:
+MV18P: 2,3,4,5,6,7 +++
Test syntax
+MV18P=+++            =? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+MV18P: (list of supported values) +
++
For example, a DCE that supported all values shall report:
+MV18P: (2-7) +++
Implementation
Implementation of this parameter is mandatory if ITU-T Rec. V.18 is implemented in the DCE.


58                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 65

6.4.8                                                    Seamless rate change enable (+MSC)
Parameter
+MSC=<src_v34>
Description
This extended-format compound numeric parameter controls whether or not seamless rate change
procedures are enabled during V.34 operation.
NOTE 1 – The addition of other subparameters to control other aspects of Seamless Rate Change Operation,
and control of V.90 SRC Operation, is for further study. The effect that enabling seamless rate change may
have on other modem characteristics, such as startup time, is for further study.
The results of seamless rate change negotiation are reported with the  +MSCR indication, which is
enabled with the same +MR command that enables other modulation reports such as +MRR. The
form of the indication is as follows:
+MSCR:<v34_src_mode>     e.g., +MSCR: 1
<v34_scr_mode>   has a value of zero for no V.34 seamless rate change (SRC), and a value of one
for V.34 SRC operation.
NOTE 2 – Additional reported values are for further study.
Defined values
See Table 19.

                                                  Table 19/V.250 – Seamless rate change control values

                                                 <src_v34> Description
                                                           0                                                                                                                                                                                            Disables V.34 seamless rate change
                                                           1                                                                                                                                                                                            Enables V.34 seamless rate change

Recommended default setting
1
Read syntax
+MSC?
The DCE shall transmit a line of information text to the DTE, consisting of:
+MSC: <current setting>
For example, with the recommended default setting, the DCE could report:
+MSC: 1
Test syntax
+MSC=?
The DCE shall transmit a string of information text to the DTE, consisting of:
+MSC: (list of supported values)
For example, a DCE that supported all defined settings would report:
+MSC: (0,1)


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  59

## Page 66

6.5                                                                                       Error control commands
This Recommendation contains parameters to condition DCE use of standard V.42 LAPM and
Alternative Error Control Procedures, and Buffered modes. Support for the selection, control and
reporting of other error control procedures is beyond the scope of this Recommendation.
6.5.1                                                   Error control selection (+ES)
Parameter
+ES =+++     = [<==   <orig_rqst><<                      >[,<>>   <orig_fbk ><<                    >[,<>>   <ans_fbk ><<                   >]]] >>
Description
This extended-format compound parameter is used to control the manner of operation of the V.42
protocol in the DCE (if present). It accepts three numeric subparameters:
–                                                                                                                                        <<orig_rqst><<>, which specifies the initial requested mode of operation when the DCE is >>
                         operating as the originator;
–                                                                                                                                        <<orig_fbk >>, which specifies the acceptable fallback mode of operation when the DCE is
                         <<                               >>
                         operating as the originator;
–                                                                                                                                        <<ans_fbk ><<>, which specifies the acceptable fallback mode of operation when the DCE is >>
                         operating as the answerer.
Defined values
See Table 20.

                                                              Table 20/V.250 – Error control operation subparameters

                <orig_rqst ><<<                 >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     Description >>
                                0                                                                                                                                                        Direct mode
                                1                                                                                                                                                           Initiate call with Buffered mode only
                                2                                                                                                                                                           Initiate V.42 without Detection Phase. If ITU-T Rec. V.8 is in use, this is a request
                                                               to disable V.42 Detection Phase
                                3                                                                                                                                                           Initiate V.42 with Detection Phase
                                4                                                                                                                                                           Initiate Alternative Protocol
                 <orig_fbk ><<<                >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           Description >>
                                0                                                                                                                                                           Error control optional (either LAPM or Alternative acceptable); if error control
                                                               not established, maintain DTE-DCE data rate and use Buffered mode with flow
                                                               control during non-error control operation
                                1                                                                                                                                                           Error control optional (either LAPM or Alternative acceptable); if error control
                                                               not established, change DTE-DCE data rate to match line rate and use Direct
                                                               mode
                                2                                                                                                                                                           Error control required (either LAPM or Alternative acceptable); if error control
                                                               not established, disconnect
                                3                                                                                                                                                           Error control required (only LAPM acceptable); if error control not established,
                                                               disconnect
                                4                                                                                                                                                           Error control required (only Alternative protocol acceptable); if error control not
                                                               established, disconnect


60                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 67

Table 20/V.250 – Error control operation subparameters

                   <ans_fbk ><<<                 >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 Description >>
                                  0                                                                                                                                                        Direct mode
                                  1                                                                                                                                                           Error control disabled, use Buffered mode
                                  2                                                                                                                                                           Error control optional (either LAPM or Alternative acceptable); if error control
                                                                  not established, maintain DTE-DCE data rate and use local buffering and flow
                                                                  control during non-error control operation
                                  3                                                                                                                                                           Error control optional (either LAPM or Alternative acceptable); if error control
                                                                  not established, change DTE-DCE data rate to match line rate and use Direct
                                                                  mode
                                  4                                                                                                                                                           Error control required (either LAPM or Alternative acceptable); if error control
                                                                  not established, disconnect
                                  5                                                                                                                                                           Error control required (only LAPM acceptable); if error control not established,
                                                                  disconnect
                                  6                                                                                                                                                           Error control required (only Alternative protocol acceptable); if error control not
                                                                  established, disconnect

Recommended default settings
For <orig_rqst>:                                                                   3
For <orig_fbk>:                                                                         0
For <ans_fbk>:                                                            2
Read syntax
+ES? +++
The DCE shall transmit a string of information text to the DTE, consisting of:
+ES: <+++          <orig_rqst><<                        >,<>> <orig_fbk><<                       >,<>> <ans_fbk ><<                     > >>
e.g., +ES: 3,0,2<CR> for the recommended defaults.
Test syntax
+ES =+++      =? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+ES: (list of supported <+++                                                               <orig_rqst><<                        > values),(list of supported <>>                                                                          <orig_fbk ><<                     > values),(list of >>
supported  <<ans_fbk ><<                                             > values) >>
e.g., +ES: (0-4),(0-4),(0-5) for all defined values.
Implementation
Implementation of this parameter is mandatory if V.42 error control or Buffered mode is
implemented in the DCE.
6.5.2                                                   Break handling in error control operation (+EB)
Parameter
+EB=+++        = [<==   <break_selection><<                                        >[,<>>    <timed ><<              >[,<>>    <default_length><<                                     >]]] >>


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  61

## Page 68

Description
This extended-format compound parameter is used to control the manner of V.42 operation (if
present in the DCE). It accepts three numeric subparameters:
–                                                                                                                                        <<break_selection><<>, which specifies the type of break to be signalled to the remote DCE >>
                                upon detecting a break from the local DTE (see ITU-T Rec. V.42 for definition of the
                                different break types);
–                                                                                                                                        <<timed><<>, which specifies if the break signal to be signalled to the remote DCE is timed or >>
                                not;
–                                                                                                                                        <<default_length >>, which specifies the amount of time in tens of milliseconds that a break
                                <<                                                                 >>
                                should be signalled to the local DTE when an indication of break is received from the
                                remote DCE without a break length explicitly indicated.
Defined values
See Table 21.

                                                                                                    Table 21/V.250 – Break control subparameters

                            <break_selection ><<<                                             >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         Description >>
                                                             0                                                                                                                                                                                                                            Ignore break (do not signal to remote DCE)
                                                             1                                                                                                                                                                                                                            Non-expedited, non-destructive
                                                             2                                                                                                                                                                                                                            Expedited, non-destructive
                                                             3                                                                                                                                                                                                                            Expedited and destructive
                                               <timed ><<<                 >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      Description >>
                                                             0                                                                                                                                                                                                                            Any transmitted V.42 L-SIGNAL shall not indicate break signal length
                                                             1                                                                                                                                                                                                                            Any transmitted V.42 L-SIGNAL shall indicate break signal length
                              <default_length ><<<                                          >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    Description >>
                                                             0                                                                                                                                                                                                                            Do not deliver break to DTE
                                                 1 to 254                                                                                                                                                               Default break length of 0.01 to 2.54 seconds
                                                    Other                                                                                                                                                                               Higher values may be supported

Recommended default settings
For <break_selection>:                                                                                                                       1
For <timed>:                                                                                       0
For <default_length>:                                                                                                                                       30
Read syntax
+EB? +++
The DCE shall transmit a string of information text to the DTE, consisting of:
+EB: <+                 <break_selection>>,<<timed>>,<<default_length >><<CR>>
++                      <<                                                                      >>      <<                            >>      <<                                                                >><<                      >>
e.g., +EB: 1,0,30<CR> to report the recommended default settings.


62                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 69

Test syntax
+EB=+++      =? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+EB: (range of supported <+++                                                         <break_selection><<                                  > values),(range of supported  <>>                                                                <timed><<             > values), >>
(range of supported  <<default_length ><<                                                                      > values)<>>               <CR><<         > >>
e.g., +EB: (0-3),(0,1),(0-200) for all defined selections and break lengths from 0.01 to two seconds.
Implementation
Implementation of this parameter is mandatory if V.42 error control or Buffered mode is
implemented in the DCE.
6.5.3                                                   Selective repeat (+ESR)
Parameter
+ESR=+            =[<<value>>]
++                ==    <<                  >>
Description
This extended-format numeric parameter controls the use of the selective reject (SREJ) option in
ITU-T Rec. V.42 (if present in the DCE).
Defined values
See Table 22.

                                                                                   Table 22/V.250 – Selective repeat values

                    <value ><<<        >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       Description >>
                              0                                                                                                                        Do not use SREJ
                              1                                                                                                                        Use SREJ if available in remote DCE; continue without it if not
                              2                                                                                                                        Use SREJ FCS if available in remote DCE; disconnect if SREJ is not available

Recommended default value
1
Read syntax
+ESR? +++
The DCE shall transmit a string of information text to the DTE, consisting of:
+ESR: <+++            <current setting><<                              > >>
e.g., +ESR: 1<CR> for the recommended default.
Test syntax
+ESR=+++          =? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+ESR: (list of supported values) +++
e.g., +ESR: (0-2) for all defined values.
Implementation
Implementation of this parameter is optional.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  63

## Page 70

6.5.4                                                    32-bit frame check sequence (+EFCS)
Parameter
+EFCS=+++      =[<==<value><<     >] >>
Description
This extended-format numeric parameter controls the use of the 32-bit frame check sequence option
in ITU-T Rec. V.42 (if present in the DCE).
Defined values
See Table 23.

                                                   Table 23/V.250 – Frame check sequence values

               <value ><<<  >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Description >>
                     0                                                                                                                  Use 16-bit FCS
                     1                                                                                                                  Use 32-bit FCS if available in remote DCE; otherwise use 16-bit FCS
                     2                                                                                                                  Use 32-bit FCS if available in remote DCE; otherwise disconnect

Recommended default value
1
Read syntax
+EFCS? +++
The DCE shall transmit a string of information text to the DTE, consisting of:
+EFCS: <+++       <current setting><<                > >>
e.g., +EFCS: 1 for the recommended default.
Test syntax
+EFCS=+        =?
++             ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+EFCS: (list of supported values) +++
e.g., +EFCS: (0-2) for all defined values.
Implementation
Implementation of this parameter is mandatory if V.42 error control is implemented in the DCE.
6.5.5                                                   Error control reporting (+ER)
Parameter
+ER=+    =[<<value>>]
++       ==   <<            >>
Description
This extended-format numeric parameter controls whether or not the extended-format "+ER:"
intermediate result code is transmitted from the DCE to the DTE. The   +ER:<type> reported shall
represent the current (negotiated or renegotiated) DCE-DCE error control type. If enabled, the
intermediate result code is transmitted at the point during error control negotiation (handshaking) at
which the DCE has determined which error control protocol will be used (if any), before the final


64                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 71

result code (e.g., CONNECT) is transmitted. The format of this result code is the following (see
Table 24):

                                                 Table 24/V.250 – Error control report intermediate result codes

                                      +ER: NONE                                                                                                                         Error control is not in use +
                                      ++
                                     +ER: LAPM                                                                                                                        ITU-T Rec. V.42 LAPM protocol is in use +++
                                         +ER: ALT                                                                                                                                           ITU-T Rec. V.42 Alternative protocol is in use +++
The         +ER intermediate result code, if enabled, is issued after the modulation report ( +MCR and
+MRR) and before the data compression report (+DR).
Defined values
See Table 25.

                                                                                      Table 25/V.250 – Error control reporting

                          <value ><<<         >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Description >>
                                    0                                                                                                                                               Error control reporting disabled (no   +ER intermediate result code transmitted)
                                    1                                                                                                                                            Error control reporting enabled ( +ER intermediate result code transmitted)

Recommended default setting
0
Read syntax
+ER? +++
The DCE shall transmit a line of information text to the DTE, consisting of:
+ER: <+++          <current setting><<                                 > >>
For example, with the recommended default setting, the DCE could report:
+ER: 0 +++
Test syntax
+ER=+++       =? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+ER: (list of supported values) +++
For example, a DCE that supported all defined settings would report:
+ER: (0,1) +++
Implementation
Implementation of this parameter and associated intermediate result codes are mandatory if V.42
error control is implemented in the DCE.
6.5.6                                                   Call termination buffer management (+ETBM)
Parameter
+ETBM=+++                 =[<==  <pending_TD ><<                              >[,<>>  <pending_RD><<                               >[, <>>  <timer><<             >]]] >>


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  65

## Page 72

Description
This extended-format compound parameter controls the handling of data remaining in DCE buffers
upon call termination. It accepts three numeric subparameters:
–                                                                                                                                        <<pending_TD><<>, which controls how previously-transmitted data remaining in the DCE >>
                           buffers should be handled when the local DTE requests disconnection of the call;
–                                                                                                                                        <<pending_RD><<>, which controls how previously-received data remaining in the DCE >>
                           buffers should be handled when the remote DCE disconnects the call; and
–                                                                                                                                        <<timer><<>, which sets a maximum time-limit on how long the DCE will attempt to deliver >>
                           the buffered data before abandoning the attempt and discarding remaining data.
Circuit 109 is held in the ON condition until all pending data is delivered or discarded.
Defined values
See Table 26.

                                           Table 26/V.250 – Call termination buffer management subparameters

     <pending_TD ><<<                              >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Description >>
                            0                                                                                                                                                                                 Discard all buffered data immediately and disconnect
                            1                                                                                                                                                                                 Attempt until all data is delivered and acknowledged (ignore timer); if remote DCE
                                                                 disconnects, discard remainder
                            2                                                                                                                                                                                 Attempt until all data is delivered and acknowledged; if timer expires or remote DCE
                                                                 disconnects, discard remainder
     <pending_RD ><<<                              >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              Description >>
                            0                                                                                                                                                                                 Discard all buffered data immediately and disconnect
                            1                                                                                                                                                                                 Attempt until all data is delivered (ignore timer); if local DTE requests disconnect,
                                                                 discard remainder
                            2                                                                                                                                                                                 Attempt until all data is delivered; if timer expires or local DTE requests disconnect,
                                                                 discard remainder
                 <timer><              >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 Description
                 <<                    >>
                   0 to 30                                                                                                                               Delivery timer value in seconds
                    Other                                                                                                                                    Higher values may be supported at manufacturer's option

Recommended default values
For <pending_TD>:                           1
For <pending_RD>:                              1
For <timer>:    20
Read syntax
+ETBM? +++
The DCE shall transmit a line of information text to the DTE, consisting of:
+ETBM: <+++                       <pending_TD ><<                                  >,<>>  <pending_RD><<                                    >, <>> <timer><<               > >>
For example, with the recommended default settings, the DCE could report:
+ETBM: 1,1,20


66                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 73

Test syntax
+ETBM=+++        =? ==
The DCE shall transmit a string of information text to the DTE, consisting of:
+ETBM: (list of supported <+++                                        <pending_TD ><<              > values),(list of supported <>>                                      <pending_RD><<                > >>
values),(list of supported  <<timer><<                              > values) >>
For example, a DCE that supported all defined settings could report:
+ETBM: (0-2),(0-2),(0-30)
Implementation
Implementation of this parameter is mandatory if V.42 error control or Buffered mode is
implemented in the DCE.
6.5.7 Window Size (+EWIND)
Parameter
+EWIND=<value1>[,<value2>]
Description
This parameter allows the user to set the maximum number of unacknowledged frames allowed at
the link layer (window size), N401. Changes to this value take effect when the next connection is
established.
Value1 is the desired window size in the transmit direction, value2 for the receive direction. If
value2 is 0 or is not included, then value1 is used for value2.
Value2 is optional for the DCE. If not supported by the DCE, then it must accept value 0 without
error.
Defined values
1-127
Recommended default setting
15 (per ITU-T Rec. V.42).
Read syntax
+EWIND?
The DCE shall transmit the following information text to the DTE:
+EWIND: <value1>,<value2>
If the DCE does not support a separate value2, then the information text shall report 0 as value2.
Test syntax
+EWIND=?
The DCE shall transmit the supported range to the DTE as in the following example:
+EWIND: (1-127),(<value2 range>)
If value2 is not supported by the DCE, then the test response shall have the value 0 for value2
range.
Implementation
Optional


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  67

## Page 74

6.5.8                                                   Frame Length (+EFRAM)
Parameter
+EFRAM=<value1>[,<value2>]
Description
This parameter indicates the maximum link layer frame information field size that shall be
attempted with the protocol. The values equal the information field size in octets. The desired frame
sizes shall be the smaller of the sizes indicated by the values of +EFRAM and any restrictions
imposed by the particular link layer protocol in use.
Value1 is for the sending direction, value2 for the receiving. If value2 is not specified or has
value 0, then value1 shall be used for both directions of transmission.
Defined values
1 to 65535 bytes.
Value2 is optional for the DCE. If value2 is not supported, then a value2 of 0 must be accepted by
the DCE without error.
NOTE – A DCE may support a smaller range and may round the value to the nearest power of 2.
Recommended default setting
128 (per ITU-T Rec. V.42).
Read syntax
+EFRAM?
The DCE shall transmit the following information text to the DTE.
+EFRAM: <value1>,<value2>
Test syntax
+EFRAM=?
The DCE shall transmit the supported range of values to the DTE as in the following example:
+EFRAM: (16-4096),<value2 range>
The value2 range shall be 0 if a separate value2 is not supported.
Implementation
Optional

6.6                                                                                       Data compression commands
This clause contains parameters to condition the DCE to use standard Data Compression
Procedures.
6.6.1 V.42 bis data compression (+DS)
Parameter
+DS=[ <<direction>>[,<<compression_negotiation>>[,<<max_dict>>[,<<max_string>>]]]]
         <<              >> <<                                      >>  <<             >>  <<                 >>
Description
This extended-format compound parameter controls the V.42 bis data compression function if
provided in the DCE. It accepts four numeric subparameters:
–                                                                                                                                        <<direction>>, which specifies the desired direction(s) of operation of the data compression
           <<             >>
           function; from the DTE point of view;

68                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 75

–                                                                                                                                        <<compression_negotiation><<>, which specifies whether or not the DCE should continue to >>
                                  operate if the desired result is not obtained;
–                                                                                                                                        <<max_dict ><<>, which specifies the maximum number of dictionary entries which should be >>
                                  negotiated (may be used by the DTE to limit the codeword size transmitted, based on its
                                  knowledge of the nature of the data to be transmitted);
–                                                                                                                                        <<max_string><<>, which specifies the maximum string length to be negotiated (V.42 bis P2). >>
Defined values
See Table 27.

                                                                              Table 27/V.250 – Data compression control subparameters

                                     <direction ><<<                           >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Description >>
                                                          0                                                                                                                                                                                                                                                                                          Negotiated ... no compression (V.42  bis P0 = 0)
                                                          1                                                                                                                                                                                                                                                                                          Transmit only
                                                          2                                                                                                                                                                                                                                                                                          Receive only
                                                          3                                                                                                                                                                                                                                                                                          Both directions, accept any direction (V.42 bis P0 = 11)
     <compression_negotiation ><<<                                                                             >                                      >>
                                                          0                                                                                                                                                                                                                                                                                          Do not disconnect if ITU-T Rec. V.42  bis is not negotiated by the remote
                                                                                                                               DCE as specified in <direction>
                                                          1                                                                                                                                                                                                                                                                                          Disconnect if ITU-T Rec. V.42 bis is not negotiated by the remote DCE as
                                                                                                                               specified in <direction>
                                     <max_dict ><<<                            >                                                                                                                                                                                      512 to 65535 >>
                                 <max_string ><<<                                   >                                                                                                                                                                    6 to 250 >>

Recommended default settings
For <direction>:                                                                                                                                                                                                                                                                                                                                      3
For <compression_negotiation>:             0
For <max_dict>:                                                                                                                                                                                                                                                                                                                               Determined by the manufacturer (see Appendix II/V.42 bis)
For <max_string>:                                                                                                                                                                                                                                                                                        6
Read syntax
+DS?
The DCE shall transmit a string of information text to the DTE, consisting of:
+DS=<<direction><<                                                    >,<>>   <compression_negotiation ><<                                                                                        >,<>>   <max_dict ><<                                  >,<>>   <max_string ><<                                         > >>
e.g., +DS:3,0,8192,6 for the recommended defaults and 8K max dictionary.
Test syntax
+DS=?
The DCE shall transmit a string of information text to the DTE, consisting of:
+DS: (list of supported  <<direction><<                                                                                                                          > values),(list of supported  <>>                                                                                                     <compression_negotiation><<                                                                                        > >>
values),(list of supported  <<max_dict><<                                                                                                                     > values),(list of supported <>>                                                                                        <max_string><<                                          > values) >>
e.g., +DS: (0-3),(0-2),(512-8192),(6-250).


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  69

## Page 76

Implementation
Implementation of this parameter is mandatory if V.42 bis data compression is implemented in the
DCE.
6.6.2                                                   V.44 Data Compression (+DS44)
Parameter
+DS44=[ <<direction><<                                    >[,<>>  <compression_negotiation ><<                                                    >[,<>>   <capability ><<                   >[,<>>  <max_codewords_tx ><<                                          >[,<>>  <max<<
_codewords_rx >>[,<>>                                 <max_string_tx><<                                >[, <>> <max_string_rx><<                                >[,<>>  <max_history_tx ><<                                 >[,<>>  <max_history_rx><<                                   >]>>
]]]]]]]]
Description
This extended-format compound parameter controls the V.44 data compression function if provided
in the DCE. It accepts nine numeric subparameters:
–                                                                                                                                        <<direction><<>, which specifies the desired direction(s) of operation of the data compression >>
                       function; from the DTE point of view;
–                                                                                                                                        <<compression_negotiation><<>, which specifies whether or not the DCE should continue to >>
                       operate if the desired result is not obtained;
–                                                                                                                                        <<capability><<>, which specifies the use of stream method, packet method, multi-packet >>
                       method;
–                                                                                                                                        <<max_codewords_tx ><<>, which specifies the maximum number of codewords which should >>
                       be negotiated in the transmit direction;
–                                                                                                                                        <<max_codewords_rx ><<>, which specifies the maximum number of codewords which should >>
                       be negotiated in the receive direction;
–                                                                                                                                        <<max_string_tx ><<>, which specifies the maximum string length to be negotiated in the >>
                       transmit direction;
–                                                                                                                                        <<max_string_rx><<>, which specifies the maximum string length to be negotiated in the >>
                       receive direction;
–                                                                                                                                        <<max_history_tx ><<>, which specifies the maximum size of the history buffer to be >>
                       negotiated in the transmit direction;
–                                                                                                                                        <<max_history_rx ><<>, which specifies the maximum size of the history buffer to be >>
                       negotiated in the receive direction.
Defined values
See Table 28.

                                                      Table 28/V.250 – Data compression control subparameters

                          <direction ><                >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Description
                          <<                           >>
                                        0                                                                                                                                                                                                                                                                                          Negotiated ... no compression
                                        1                                                                                                                                                                                                                                                                                          Transmit only
                                        2                                                                                                                                                                                                                                                                                          Receive only
                                        3                                                                                                                                                                                                                                                                                          Both directions, accept any direction


70                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 77

Table 28/V.250 – Data compression control subparameters

     <compression_negotiation ><<<                                                                           >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           Description >>
                                                         0                                                                                                                                                                                                                                                                                          Do not disconnect if ITU-T Rec. V.44 is not negotiated by the remote
                                                                                                                             DCE as specified in <direction>
                                                         1                                                                                                                                                                                                                                                                                          Disconnect if ITU-T Rec. V.44 is not negotiated by the remote DCE as
                                                                                                                             specified in <direction>
                                   <capability ><<<                             >                                                                                                                                                                                  >>
                                                         0                                                                                                                                                                                                                                                                                    Stream method
                                                         1                                                                                                                                                                                                                                                                                    Packet method
                                                         2                                                                                                                                                                                                                                                                                          Multi-packet method
                <max_codewords_tx ><<<                                                            > >>                                                                                         256 to 65536
                <max_codewords_rx ><                                                               >                                                                                       256 to 65536
                <<                                                                                 >>
                          <max_string_tx ><<<                                            >                                                                                                                                     32 to 255 >>
                         <max_string_rx ><<<                                             >                                                                                                                                   32 to 255 >>
                       <max_history_tx ><<<                                                > >>   ≥≥ 512 ≥≥
                       <max_history_rx ><                                                  >                                 ≥ 512
                       <<                                                                  >>   ≥≥≥

Recommended default settings
For <direction>:                                                                                                                                                                                                                                                                                                                                      3
For <compression_negotiation>:             0
For <capability>:                                                                                                                                                                                                                                                                                                                   0
For <max_codewords_tx>:                                                                                                                         Determined by the manufacturer (see Appendix I/V.44)
For <max_codewords_rx>:                                                                                                                      Determined by the manufacturer (see Appendix I/V.44)
For <max_string_tx>:                                                                                                                                                                                                                            Determined by the manufacturer (see Appendix I/V.44)
For <max_string_rx>:                                                                                                                                                                                                                         Determined by the manufacturer (see Appendix I/V.44)
For <max_history_tx>:                                                                                                                                                                                                    Determined by the manufacturer (see Appendix I/V.44)
For <max_history_rx>:                                                                                                                                                                                                 Determined by the manufacturer (see Appendix I/V.44)
Read syntax
+DS44?
The DCE shall transmit a string of information text to the DTE, consisting of:
+DS44: <<direction><<                                                        ><>><compression_negotiation ><<                                                                                       >,<>>   <capability><<                                   >,<>>   <max_codewords_tx><<                                                                      >, >>
<max_codewords_rx ><<<                                                                    >, <>>  <max_string_tx ><<                                                   >,<>>   <max_string_rx><<                                                     >, <>>  <max_history_tx ><<                                                       >, >>
<max_history_rx> <
<<
e.g., +DS44:3,0,0,1024,1024,255,255,3072,3072.
Test syntax
+DS44=?
The DCE shall transmit a string of information text to the DTE, consisting of:
+DS44: (list of supported  <<direction>> values),(list of supported  <<compression_negotiation>>
                                                                                                                       <<                                           >>                                                                                                                            <<                                                                                                                >>
values),(list of supported <<capability ><<                                                                                                                                   > values),(list of supported <>>                                                                                                            <max_codewords_tx ><<                                                                     > >>


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  71

## Page 78

values),(list of supported <<max_codewords_rx ><<                                                             > values),(list of supported  <>>                                  <max_string_tx><<                   > >>
values),(list of supported <<max_string_rx><<                                                         > values),(list of supported  <>>                                       <max_history_tx><<                     > >>
values),(list of supported  <<max_history_rx><<                                                  > values) >>
Implementation
Implementation of this parameter is mandatory if V.44 data compression is implemented in the
DCE.
6.6.3                                                   Data compression reporting (+DR)
Parameter
+DR=<<value >>
             <<              >>
Description
This extended-format numeric parameter controls whether or not the extended-format "+DR."
intermediate result code is transmitted from the DCE to the DTE. The +DR:<type> reported shall
represent the current (negotiated or renegotiated) DCE-DCE data compression type. If enabled, the
intermediate result code is transmitted at the point after error control negotiation (handshaking) at
which the DCE has determined which data compression technique will be used (if any) and the
direction of operation. The format of this result code is the following (see Table 29):

                          Table 29/V.250 – Data compression reporting intermediate result codes

                       +DR: NONE                                                                                         Data compression is not in use +++
                        +DR: V42B                                                                                                    ITU-T Rec. V.42 bis is in use in both directions +
                        ++
                    +DR: V42B RD                                                               ITU-T Rec. V.42 bis is in use in receive direction only +++
                    +DR: V42B TD                                                                ITU-T Rec. V.42 bis is in use in transmit direction only +++
                          +DR: V44  +++                                                                                                                           ITU-T Rec. V.44 is in use in both directions
                      +DR: V44 RD  +                                                                                              ITU-T Rec. V.44 is in use in receive direction only
                      ++
                      +DR: V44 TD  +++                                                                                            ITU-T Rec. V.44 is in use in transmit direction only
The      +DR intermediate result code, if enabled, is issued after the Error Control Report (  +ER) and
before the final result code (e.g., CONNECT).
Defined values
See Table 30.

                                                Table 30/V.250 – Data compression reporting values

             <value ><     >                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Description
             <<            >>
                    0                                                                                                                                          Data compression reporting disabled (no  +DR result code transmitted)
                    1                                                                                                                                          Data compression reporting enabled ( +DR result code transmitted)

Recommended default setting
0


72                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 79

Read syntax
+DR? +++
The DCE shall transmit a line of information text to the DTE, consisting of:
+DR: <+                   <current setting>>
++                        <<                                                                     >>
For example, with the recommended default setting, the DCE could report:
+DR: 0 +++
Test syntax
+DR=? +
++
The DCE shall transmit a string of information text to the DTE, consisting of:
+DR: (list of supported values) +++
For example, a DCE that supported all defined settings would report:
+DR: (0,1) +
++
Implementation
Implementation of this parameter and the associated intermediate result code is mandatory if data
compression is implemented in the DCE.

6.7 DCE testing
This clause contains a set of +T (test) commands and parameters that are based on the test objects of
ITU-T Rec. V.58.
The parameters correspond as closely as possible to V.58 objects. In some cases the parameters
were structured to more in keeping with AT command practice.
6.7.1                                                    List of test commands and parameters
The following commands and parameters are defined in this clause:
+TE140                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Enable                                                                                                             Ckt                                                                                                          140
+TE141                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                Enable                                                                                                             Ckt                                                                                                          141
+TERDL                                             Enable RDL From Remote
+TEPDL                                                  Enable Front Panel RDL
+TEPAL                                                  Enable Front Panel Analogue Loop
+TALS                                                                      Analogue Loop Status
+TDLS                                                                     Local Digital Loop Status
+TRDLS                                                Remote Digital Loop Status
+TADR                                                                     Local V.54 Address
+TMODE                             Set V.54 Mode
+TTER                                                                      Test Error Rate
+TNUM                                                           Errored Bit and Block Counts
+TLDL                                                                     Local Digital Loop
+TRDL                                                                     Request Remote Digital Loop
+TAL                                                                                               Local Analogue Loop

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  73

## Page 80

+TSELF Self Test
+TRES                                                                       Self Test Result
6.7.2                                                   Test commands and parameters
6.7.2.1                                                             Enable Ckt 140 (+TE140)
Parameter
+TE140=<value>
Description
This parameter enables or disables DCE response to signals on V.24 circuit 140, which controls
remote digital loop (V.54 loop 2).
Values
0 Disabled
1 Enabled
Recommended default setting
0 Disabled
Read syntax
+TE140?
The DCE shall transmit the following information text to the DTE:
+TE140: <value>
Test syntax
+TE140=?
The DCE shall transmit the following information text to the DTE:
+TE140: (0,1)
Implementation
Optional
6.7.2.2                                                             Enable Ckt 141 (+TE141)
Parameter
+TE141=<value>
Description
This parameter enables or disables DCE response to signals on circuit 141, which controls local
analogue loop test (V.54 loop 3).
Defined values
0 Response is disabled
1 Response is enabled
Recommended default setting
0 Disabled


74                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 81

Read syntax
+TE141?
The DCE shall transmit the following information text to the DTE:
+TE141: <value>
Test syntax
+TE141=?
The DCE shall transmit the following information text to the DTE:
+TE141: (0,1)
Implementation
Optional
6.7.2.3                                                             Enable RDL From Remote (+TERDL)
Parameter
+TERDL=<value>
Description
This parameter enables the local DCE response to a digital loop command (V.54 loop 2 initiation)
from the remote DCE.
Defined values
0                                                                                                                           Local DCE will ignore command from remote
1                                                                                                                           Local DCE will obey command from remote
Recommended default setting
0
Read syntax
+TERDL?
The DCE shall send the following information text to the DTE:
+TERDL: <value>
Test syntax
+TERDL=?
The DCE shall send the following information text to the DTE:
+TERDL: (0,1)
Implementation
Optional
6.7.2.4                                                             Enable Front Panel RDL (+TEPDL)
Parameter
+TEPDL=<value>
Description
This parameter enables the sending of RDL (V.54 loop 2 initiation) commands to the remote DCE
from the front panel control.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  75

## Page 82

Defined values
0 Disabled
1 Enabled
Recommended default setting
0
Read syntax
+TEPDL?
The DCE shall send the following information text to the DTE:
+TEPDL: <value>
Test syntax
+TEPDL=?
The DCE shall send the following information text to the DTE:
+TEPDL: (0,1)
Implementation
Optional
6.7.2.5                                                             Enable Front Panel Analogue Loop (+TEPAL)
Parameter
+TEPAL=<value>
Description
This parameter enables initiation of local analogue loop by the front panel (V.54 loop 3).
Defined values
0 Disabled
1 Enabled
Recommended default setting
0
Read syntax
+TEPAL?
The DCE shall send the following information text to the DTE:
+TEPAL: <value>
Test syntax
+TEPAL=?
The DCE shall send the following information text to the DTE:
+TEPAL: (0,1)
Implementation
Optional


76                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 83

6.7.2.6                                                             Analogue Loop Status (+TALS)
Parameter
+TALS?
Description
This read-only parameter reports the current status of the local analogue loop (V.54 loop 3).
Values
0 Inactive
1 V.24 circuit 141 invoked
2                                                                                                                           Front panel invoked
3                                                                                                                           Network management system invoked
Recommended default setting
0
Read syntax
+TALS?
The DCE shall send the following information text to the DTE:
+TALS: <value>
Test syntax
+TALS=?
The DCE shall send the following information text to the DTE:
+TALS: (range of supported values)
Implementation
Optional
6.7.2.7                                                             Local Digital Loop Status (+TDLS)
Parameter
+TDLS?
Description
This read-only parameter reports the status of the local digital loop (V.54 loop 1).
Defined values
0 Disabled
1 Enabled, inactive
2                                                                                                                           Front panel invoked
3                                                                                                                           Network management system invoked
4 Remote invoked
Recommended default setting
0


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  77

## Page 84

Read syntax
+TDLS?
The DCE shall send the following information text to the DTE:
+TDLS: <value>
Test syntax
+TDLS=?
The DCE shall send the following information text to the DTE:
+TDLS: (range of supported values)
Implementation
Optional
6.7.2.8                                                             Remote Digital Loop Status (+TRDLS)
Parameter
+TRDLS?
Description
This read-only parameter reports the status of the remote local digital loop (V.54 loop 2).
Defined values
0 Disabled
1 Enabled, inactive
2                                                                                                                            V.24 circuit 140 invoked
3                                                                                                                           Front panel invoked
4                                                                                                                           Network management system invoked
Recommended default setting
0
Read syntax
+TRDLS?
The DCE shall send the following information text to the DTE:
+TRDLS: <value>
Test syntax
+TRDLS=?
The DCE shall send the following information text to the DTE:
+TRDLS: (range of supported values)
Implementation
Optional
6.7.2.9                                                             Local V.54 Address (+TADR)
Parameter
+TADR=<value>


78                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 85

Description
This parameter is the V.54 address of the local DCE.
Defined values
See Table 4/V.54
Recommended default setting
0
Read syntax
+TADR?
The DCE shall send the following information text to the DTE:
+TADR: <value>
Test syntax
+TADR=?
The DCE shall send the following information text to the DTE:
+TADR: (range of supported V.54 address values)
Implementation
Optional
6.7.2.10                                     Set V.54 Mode (+TMODE)
Parameter
+TMODE=<value>
Description
This parameter selects the V.54 mode: point-to-point or multipoint.
Defined values
0 Point-to-point
1                                                                                                                           Multipoint or tandem
Recommended default setting
0
Read syntax
+TMODE?
The DCE shall send the following information text to the DTE:
+TMODE: <value>
Test syntax
+TMODE=?
The DCE shall send the following information text to the DTE:
+TMODE: (0,1)
Implementation
Optional


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  79

## Page 86

6.7.2.11                                     Test Error Rate (+TTER)
Syntax
+TTER=<type>,<block_length>,<blocks>,<pattern>
Description
This command starts and stops a bit error rate or block error rate test. A suitable loop must be
established before this test can proceed. The DCE remains in Command State after this command is
executed. The parameter +TTER=0 stops the test. When the test is stopped, the block and bit error
count is preserved and can be determined by the +TNUM parameter.
Defined values
Type                                                                         0                                            Stop the test
                                                                                                                                                                                                                                                                                           1                                            Bit error rate
                                2 Block error rate
                                3 Both
Block length                                                           1-65535 bits
Blocks                                                     1-65535 blocks
Pattern                                                                                                                                                    0                                            63-bit pseudo-random pattern
                                1 511-bit pattern
                                2 2047-bit pattern
                                3 All ones
                                                                                                                                                                                                                                                                                              4                                            Alternating ones and zeroes
Execution time
Command execution consists of starting or stopping the test in question. In the case of starting a
test, the test continues to run until the specified block count is reached or a type of 0 is sent to the
DCE. The time depends on the current DCE speed.
Read syntax
+TTER?
The DCE shall send the following information text to the DTE:
+TTER: <test type in progress>,<block length>,<remaining blocks in test>,<pattern in use>
Test syntax
+TTER=?
The DCE shall send the following information text to the DTE:
+TTER: (range of supported type),(range of supported block_length),(range of supported
blocks),(range of supported pattern)
Implementation
Optional
6.7.2.12                                     Errored Bit and Block Counts (+TNUM)
Parameter
+TNUM?


80                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 87

Description
The values of this parameter are the number of errored bits and blocks received during the current
or last error rate test. This is a read-only, double-valued parameter. The response to +TNUM? is the
number of bit and block errors detected during the current or previous test. If either of the error
counts is not available, the number displayed is 0. See also the +TTER command.
Defined values
bit_errors        0-65535
block_errors 0-65535
Read syntax
+TNUM?
The DCE shall send the following information text to the DTE:
+TTER: <number of bit errors>,<number of block errors>
Test syntax
+TTER=?
The DCE shall send the following information text to the DTE:
+TTER: (range of supported bit_errors),(range of supported block_errors)
Implementation
Optional
6.7.2.13                                     Local Digital Loop (+TLDL)
Syntax
+TLDL=<value>
Description
This command starts a digital loop of the local DCE. The test stops when the command +TLDL=0
is received by the DCE. This command is valid only while the DCE is connected to a remote DCE
(Online Command State).
When the DCE is in digital loop mode, all characters sent from the DTE to the DCE are looped and
returned to the DTE unless an error rate test is invoked. See the +TTER command.
The DCE must be placed in Online Command State in order to accept a command to stop the local
digital loop.
Defined values
0 Stop test
1 Start test
Execution time
Command execution consists of starting or stopping the test in question. The test continues to run
until a +TLDL=0 command is sent to the DCE.
Read syntax
+TLDL?
The DCE shall send the following information text to the DTE:
+TLDL: 0                 If a test is not in progress


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  81

## Page 88

+TLDL: 1                 If a test is in progress
Test syntax
+TLDL=?
The DCE shall send the following information text to the DTE:
+TLDL: (0,1)
Implementation
Optional
6.7.2.14                                     Request Remote Digital Loop (+TRDL)
Syntax
+TRDL=<value>
Description
This command starts or stops a digital loop (V.54 loop 2) at the remote DCE. This command is
valid only while the DCE is in the online command state.
After issuing this command, the DTE will normally issue the ATO command to return to the online
state.
An        OK result code is returned and V.24 circuit 107 (DSR) is turned off after the remote DCE
signals acceptance of the command.
If confirmation is not received, then the DCE shall return the ERROR result code.
Defined values
0 Stop RDL
1 Start RDL
Execution time
Command execution consists of starting or stopping the test in question. The test continues to run
until a +TRDL=0 command is sent to the DCE.
Abortability
Command execution consists of the transient action of starting or stopping the test in question and is
thus not abortable.
Read syntax
+TRDL?
The DCE shall send the following information text to the DTE:
+TRDL: 0              If a test is not in progress
+TRDL: 1              If a test is in progress
Test syntax
+TRDL=?
The DCE shall send the following information text to the DTE:
+TRDL: (0,1)
Implementation
Optional


82                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 89

6.7.2.15                                     Local Analogue Loop (+TAL)
Syntax
+TAL=<action>,<band>
Description
This command starts or stops a local analogue loop (V.54 loop 3). For the case of starting a loop,
the DCE issues an OK result code and returns to online command state if the loop initiation was
successful, and issues a NO CARRIER result code and returns to command state if not successful.
The <band> subparameter is applicable to frequency divided duplex modems. The DCE continues
in loop 3 mode until stopped by a subsequent +TAL=0 command.
Non-zero values of <action> are not valid in online command state.
Unless an error rate test is invoked, the DTE will normally enter Data State with ATO. In Data
State, all characters sent to the DCE are looped back to the DTE; the DTE must cause a change to
the command state in order to command an end to the loop condition.
If an error rate test is enabled, that test continues until stopped by command. At the end of the error
rate test, the DCE remains in the looped condition, until the loop is disabled.
Defined values
Action                                                                                     0                                            Disable analogue loop
                                                                                                                                                                                                                       1                                            Enable analogue loop
Band                                                                                                                  0                                            Low frequency band
                                                                                                                                                                                                                       1                                            High frequency band
NOTE 1 – If <band> is omitted, 0 is assumed.
NOTE 2 – If a valid <band> is supplied but is not needed, the DCE shall ignore it.
Execution time
Command execution consists of starting or stopping the test in question. The test continues to run
until a +TAL=0 command is sent to the DCE.
Abortability
Command execution consists of the transient action of starting or stopping the test in question and is
thus not abortable.
Read syntax
+TAL?
The DCE shall send the following information text to the DTE:
+TAL: 0                                                 If a test is not in progress
+TAL: 1                                                 If a test is in progress
Test syntax
+TAL=?
The DCE shall send the following information text to the DTE:
+TAL: (0,1),(range of supported band values)
Implementation
Optional


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  83

## Page 90

6.7.2.16 Self Test (+TSELF)
Syntax
+TSELF=<value>
Description
This command invokes a self test of the modem. The nature of this test is not specified; however it
shall include checks on the operation of hardware components and memory. It is assumed that the
test duration is short (typically no longer than 5 seconds). A full test of DCE functions is assumed
to be intrusive, i.e., would interfere with normal operation; a partial test is assumed to be
non-intrusive, i.e., could typically be performed during online command state, but only provides a
cursory check on DCE operation.
Defined values
0                                                                                                                           Intrusive full test
1                                                                                                                           Safe partial test
Execution time
The execution time is implementation-specific: typically no longer than 5 seconds.
Abortability
This command is not ordinarily abortable, but may be so in some implementations.
Test syntax
+TSELF=?
The DCE shall send the following information text to the DTE:
+TSELF: (range of supported values)
Implementation
Optional
6.7.2.17                                     Self Test Result (+TRES)
Parameter
+TRES?
Description
This read-only parameter contains the result of the last self test conducted since power up or reset.
If a test has not been conducted, then the value shall be 0.
Defined values
0 No test
1 Pass
2 Fail
Recommended default setting
0


84                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 91

Read syntax
+TRES?
The DCE shall send the following information text to the DTE:
+TRES: <value>
Test syntax
+TRES=?
The DCE shall send the following information text to the DTE:
+TRES: (0-2)
Implementation
Optional

6.8 PCM DCE commands
This clause contains a set of +P (PCM DCE) commands and parameters to condition and control
DCE use of ITU-T Rec. V.92.
6.8.1                                                   Call Waiting enable (+PCW)
Parameter
+PCW=[<call waiting >>] >>
Description
This extended-format compound numeric parameter controls the action to be taken upon detection
of call waiting in a V.92 DCE.
Defined values
See Table 31.

                                                                                                          Table 31/V.250 – Call Waiting Values

                                                                     <call waiting >>                                                                                                                                                                                                                                                                                                                                                                                                                              Description >>
                                                                                           0                                                                                                                                                                                                                    Toggle V.24 Circuit 125 and collect Caller ID
                                                                                                                                        if enabled by +VCID
                                                                                           1                                                                                                                                                                                                                    Hang up
                                                                                           2                                                                                                                                                                                                                    Ignore V.92 call waiting

Default setting
0
Read syntax
+PCW?
The DCE shall transmit a line of information text to the DTE, consisting of:
+PCW: <call waiting>
For example, with the default setting, the DCE could report:
+PCW: 0


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  85

## Page 92

Test syntax
+PCW=?
The DCE shall transmit a string of information text to the DTE, consisting of:
+PCW: (list of supported values of <call waiting>)
For example, a DCE that supported all defined settings would report:
+PCW: (0,1,2)
Implementation
Implementation of this parameter is mandatory if ITU-T Rec. V.92 is implemented in the DCE.
6.8.2                                                   Modem-on-Hold enable (+PMH)
Parameter
+PMH=[<value>]
Description
This extended-format compound numeric parameter controls whether or not modem-on-hold
procedures are enabled during V.92 operation.
Defined values
See Table 32.

                                               Table 32/V.250 – Modem-on-hold enable

                                             <value> Description
                                                   0                                                                                                                                                                                              Enables V.92 modem on hold
                                                   1                                                                                                                                                                                              Disables V.92 modem on hold

Default setting
0
Read syntax
+PMH?
The DCE shall transmit a line of information text to the DTE, consisting of:
+PMH: <current setting>
For example, with the default setting, the DCE could report:
+PMH: 0
Test syntax
+PMH=?
The DCE shall transmit a string of information text to the DTE, consisting of:
+PMH: (list of supported values)
For example, a DCE that supported all defined settings would report:
+PMH: (0,1)


86                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 93

Implementation
Implementation of this parameter is mandatory if ITU-T Rec. V.92 is implemented in the DCE.
6.8.3                                                   Modem-on-Hold Timer (+PMHT)
This extended-format compound numeric parameter controls whether or not the modem will grant
or deny a Modem-on-hold (MOH) request as well as setting the Modem-on-Hold Timeout.
Defined values
See Table 33.

                                                                                            Table 33/V.250 – Modem-on-Hold Timer values

                                                                                                  <value> Description
                                                                                                              0                                                                                                                                                                                              Deny V.92 Modem-on-hold Request
                                                                                                              1                                                                                                                                                                                              Grant MOH with 10-second timeout
                                                                                                              2                                                                                                                                                                                              Grant MOH with 20-second timeout
                                                                                                              3                                                                                                                                                                                              Grant MOH with 30-second timeout
                                                                                                              4                                                                                                                                                                                              Grant MOH with 40-second timeout
                                                                                                              5                                                                                                                                                                                              Grant MOH with 1-minute timeout
                                                                                                              6                                                                                                                                                                                              Grant MOH with 2-minute timeout
                                                                                                              7                                                                                                                                                                                              Grant MOH with 3-minute timeout
                                                                                                              8                                                                                                                                                                                              Grant MOH with 4-minute timeout
                                                                                                              9                                                                                                                                                                                              Grant MOH with 6-minute timeout
                                                                                                            10                                                                                                                                                                                   Grant MOH with 8-minute timeout
                                                                                                            11                                                                                                                                                                                   Grant MOH with 12-minute timeout
                                                                                                            12                                                                                                                                                                                   Grant MOH with 16-minute timeout
                                                                                                            13                                                                                                                                                                                   Grant MOH with indefinite timeout

Read syntax
+PMHT?
The DCE shall transmit a line of information text to the DTE, consisting of:
+PMHT: <current setting>
For example, with <value> set to Deny V.92 Modem-on-hold Request, the DCE would report:
+PMHT: 0
Test syntax
+PMHT=?
The DCE shall transmit a string of information text to the DTE, consisting of:
+PMHT: (list of supported values)
For example, a DCE that supported all defined settings would report:
+PMHT: (0,1,2,3,4,5,6,7,8,9,10,11,12,13)
Implementation
Implementation of this parameter is mandatory if ITU-T Rec. V.92 is implemented in the DCE.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  87

## Page 94

6.8.4                                                   Initiate Modem on Hold (+PMHR)
Parameter
+PMHR
Description
This extended-format command requests the DCE to initiate or to confirm a modem-on-hold
procedure. The DCE shall return ERROR if Modem on Hold is not enabled or if the DCE is in an
idle condition. The DCE shall return the string response +PMHR: <value> where <value> is a
decimal value corresponding to the Modem-on-Hold timer value received or the request status
during the DCE's modem on hold exchange procedure as defined in Table 34. This response may be
delayed depending upon the context under which the +PMHR command is made, i.e., if the
+PMHR is in response to an incoming Modem on Hold or if it is initiating a request.
Defined values
None.
Read Syntax
+PMHR

                                                                Table 34/V.250 – Modem-on-Hold Response values

                                            <value> Description
                                                     0                                                                                                                                                                            V.92 Modem-on-Hold Request Denied or not available. The
                                                                                   modem may initiate another Modem-on-hold request later.
                                                     1                                                                                                                                                                            MOH with 10-second timeout Granted
                                                     2                                                                                                                                                                            MOH with 20-second timeout Granted
                                                     3                                                                                                                                                                            MOH with 30-second timeout Granted
                                                     4                                                                                                                                                                            MOH with 40-second timeout Granted
                                                     5                                                                                                                                                                            MOH with 1-minute timeout Granted
                                                     6                                                                                                                                                                            MOH with 2-minute timeout Granted
                                                     7                                                                                                                                                                            MOH with 3-minute timeout Granted
                                                     8                                                                                                                                                                            MOH with 4-minute timeout Granted
                                                     9                                                                                                                                                                            MOH with 6-minute timeout Granted
                                                    10                                                                                                                                                                 MOH with 8-minute timeout Granted
                                                    11                                                                                                                                                                 MOH with 12-minute timeout Granted
                                                    12                                                                                                                                                                 MOH with 16-minute timeout Granted
                                                    13                                                                                                                                                                 MOH with indefinite timeout Granted
                                                    14                                                                                                                                                                 MOH Request denied. Future requests will also be denied
                                                                                   during this session.

Implementation
Implementation of this parameter is mandatory if ITU-T Rec. V.92 is implemented in the DCE.
6.8.5                                                    PCM upstream ignore (+PIG)
Parameter
+PIG=[<value>]


88                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 95

Description
This extended-format compound numeric parameter controls the use of PCM upstream in a V.92
DCE.
Defined values
See Table 35.

                                                                 Table 35/V.250 – PCM upstream ignore values

                                                                  <value> Description
                                                                          0                                                                                                                                                                                                                        Enable PCM upstream
                                                                          1                                                                                                                                                                                                                        Disable PCM upstream

Default setting
0
Read syntax
+PIG?
The DCE shall transmit a line of information text to the DTE, consisting of:
+PIG: <current setting>
For example, with the default setting, the DCE could report:
+PIG: 0
Test syntax
+PIG=?
The DCE shall transmit a string of information text to the DTE, consisting of:
+PIG: (list of supported values)
For example, a DCE that supported all defined settings would report:
+PIG: (0,1)
Implementation
Implementation of this parameter is mandatory if ITU-T Rec. V.92 is implemented in the DCE.
6.8.6                                                    V.92 Modem-on-Hold Hook Flash (+PMHF)
Parameter
+PMHF
Description
This command causes the DCE to go on-hook for a specified period of time, and then return
on-hook. The specified period of time is normally one-half second, but may be governed by
national regulations. If this command is initiated and the modem is not On Hold, ERROR is
returned. This command applies only to V.92 Modem on Hold.
Defined values
None.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  89

## Page 96

Implementation
Implementation of this parameter is mandatory if ITU-T Rec. V.92 is implemented in the DCE.
6.8.7                                                   V.92 Phase 1 and Phase 2 Control (+PQC)
Parameter
+PQC=<value>
Description
This extended-format compound numeric parameter controls the global enabling or disabling of the
V.92 shortened Phase 1 and Phase 2 startup procedures, not the initiation thereof. This command is
used in conjunction with the +PSS command.
Defined values
See Table 36.

                                                 Table 36/V.250 – Phase 1 and Phase 2 values

                                      <value> Description
                                            0                                                                                                                                                          Enable Short Phase 1 and Short Phase 2
                                            1                                                                                                                                                          Enable Short Phase 1
                                            2                                                                                                                                                          Enable Short Phase 2
                                            3                                                                                                                                                          Disable short Phase 1 and Short Phase 2

Default setting
0
Read syntax
+PQC?
The DCE shall transmit a line of information text to the DTE, consisting of:
+PQC: <current setting>
For example, with the default setting, the DCE could report:
+PQC: 0
Test syntax
+PQC=?
The DCE shall transmit a string of information text to the DTE, consisting of:
+PQC: (list of supported values)
For example, a DCE that supported all defined settings would report:
+PQC: (0,1,2,3)
Implementation
Implementation of this parameter is mandatory if ITU-T Rec. V.92 is implemented in the DCE.
6.8.8                                                   Use Short Sequence (+PSS)
Parameter
+PSS=<value>

90                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 97

Description
This extended-format compound numeric parameter causes a calling DCE to force either a V.92
short or full startup sequence as defined by the +PQC command on the next and subsequent
connections.
Defined values
See Table 37.

                                                               Table 37/V.250 – Use Short Sequence Values

             <value> Description

                     0                                                                                                                                                                       The DCEs decide whether or not to use the short startup procedures. The short startup procedures shall only be used if enabled by the +PQC command.

                     1                                                                                                                                                                       Forces the use of the short startup procedures on the next and subsequent connections if they are enabled by the +PQC command.

                     2                                                                                                                                                                       Forces the use of the full startup procedures on the next and subsequent connections independent of the setting of the +PQC command.

Default setting
0
Read syntax
+PSS?
The DCE shall transmit a line of information text to the DTE, consisting of:
+PSS: <current setting>
For example, with the default setting, the DCE could report:
+PSS: 0
Text syntax
+PSS=?
The DCE shall transmit a string of information text to the DTE, consisting of:
+PSS: (list of supported values)
For example, a DCE that supported all defined settings would report:
+PSS: (0,1,2)
Implementation
Implementation of this parameter is mandatory if ITU-T Rec. V.92 is implemented in the DCE.

6.9                                                                                       V.59 Command (+TMO)
This extended-format command causes the DCE to transmit one or more lines of information text in
specific formats. The command retrieves the information from the managed objects in ITU-T Rec.
V.59. The command can be used in three ways as described in the following clauses.
6.9.1                                                    Repeat last +TMO command
Syntax
+TMO


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  91

## Page 98

Description
The +TMO command without extensions will cause the DCE to repeat the last +TMO command
that was issued.
NOTE – For all common mid-level objects retrieved by the +TMO command, only the one applicable to the
most recent modulation used, irrespective of how many modulations the modem has operated in during the
previous connection, is returned.
6.9.2                                                   Retrieve diagnostic supported
Syntax
+TMO [<list level><n>]=?
Defined list levels:
0                                                                       The DCE shall transmit information text which reports the list of all objects support as
             defined in ITU-T Rec. V.59.
1                                                                    The DCE shall transmit information text which reports the list of all high-level objects
             supported as defined in ITU-T Rec. V.59.
2                                                                 The DCE shall transmit information text which reports the list of all mid-level objects
             supported as defined in ITU-T Rec. V.59.
3                                                                The DCE shall transmit information text which reports the list of all low-level objects
             supported as defined in ITU-T Rec. V.59.
4                                                                                                                            The DCE shall transmit 0 if it supports object names, and 1 if it supports tagIDs.
Defined <n>:
n                                                                                           If present, the object names are returned; if not present, tagIDs are returned. n shall not be
             used with list level 4. If a DCE supports only tagIDs and n is included with the +TMO
             command, ERROR will be returned.
For example, a DCE that supported both object names and tagIDs would report:
+TMO 4=? (0,1)
6.9.3                                                    Retrieve specific diagnostic information
Syntax
+TMO <tagID or Name> <all or only>
Description
This command retrieves the diagnostic identified by either the V.59 tagID or the name. The
response from the DCE shall be in the same form as the request, i.e., a tagID will return a response
identified by the tagID. A named diagnostic will return the name and the requested information.
A two-digit tagID indicates that the request is for the high-level V.59 objects. A four-digit tagID
indicates that the request is for a mid-level or a low-level V.59 object.
<all or only> specifies if any or all sub-objects of a high- or mid-level objects are returned in
response to the command.
For example:
+TMO <Name> <all or only>
+TMO V92 All                                                                                         would return all the diagnostics defined for ITU-T Rec. V.92 in ITU-T
                                       Rec. V.59.
+TMO V92 rxHistory                             would only return the rx rate history of the V.92 diagnostic as defined in
                                       ITU-T Rec. V.59.

92                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 99

+TMO <tagID> <all or only>
+TMO 09                                                                                                                                                                                                                                                                         would return the entire V.90 object.
+TMO 0900                                                                                                                                                                                                                       would return mode V.90 object only.


                                                                                                                                                                                                              Appendix I

                                                                                        Summary of basic and extended format commands

                                                                                                     Table I.1/V.250 – Defined leading character sequences

                         Leadin                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Includes commands related to
                                   +A                                                                                                                                                         Call control (network Addressing) issues, common, PSTN, ISDN, ITU-T Rec. X.25, +
                                   ++
                                                                                          switched digital
                                   +C                                                                                                                                                         Digital Cellular extensions +
                                   ++
                                   +D                                                                                                                                                         Data Compression, ITU-T Rec. V.42 bis +++
                                   +E                                                                                                                                                          Error Control, ITU-T Rec. V.42 +++
                                   +F                                                                                                                                                            Facsimile, ITU-T Rec. T.30, etc. +++
                                   +G                                                                                                                                                        Generic issues such as identity and capabilities +
                                   ++
                                     +I                                                                                                                                                                 DTE-DCE Interface issues, ITU-T Rec. V.24, etc. +++
                                  +M                                                                                                                                                    Modulation, ITU-T Rec. V.32  bis, etc. +++
                                   +P                                                                                                                                                           PCM DCE commands, ITU-T Rec. V.92
                                    +S                                                                                                                                                             Switched or Simultaneous Data Types +
                                    ++
                                   +T                                                                                                                                                          Test issues +++
                                   +V                                                                                                                                                         Voice extensions +++
                                 +W                                                                                                                                                   Wireless extensions +++
     NOTE – See Supplement 1 to this Recommendation for a current summary of other Standards that are
     based on this Recommendation.


                                                                                                                                     Table I.2/V.250 – Summary of commands

                   Name Type Syntax Reference                   Description
     &C                                                                                                                                                                                             Parameter                                                   Basic                                                                                                            6.2.8                                                                                                                                             Circuit 109 (Received line signal detector)
                                                                                                                                                                                                                                                    behaviour
     &D                                                                                                                                                                                           Parameter                                                   Basic                                                                                                            6.2.9                                                                                                                                             Circuit 108 (Data terminal ready) behaviour
     &F                                                                                                                                                                                                  Action                                                                                                             Basic                                                                                                            6.1.2                                                                                                                                             Set to factory-defined configuration
     +DR                                                                                                                                                                       Parameter                                                   Extended                                       6.6.2                                                                                                                                             Data compression reporting
     +DS Parameter Extended 6.6.1 Data compression
     +DS44                                                                                                                                Parameter                                                   Extended                                       6.6.2                                                                                                                                             V.44 Data compression
     +EB                                                                                                                                                                            Parameter                                                   Extended                                       6.5.2                                                                                                                                             Break handling in error control operation
     +EFCS                                                                                                                           Parameter                                                   Extended                                       6.5.4                                                                                                                                             32-bit frame check sequence
     +ER Parameter Extended 6.5.5 Error control reporting
     +ES Parameter Extended 6.5.1 Error control selection
     +ESR Parameter Extended 6.5.3 Selective repeat


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  93

## Page 100

Table I.2/V.250 – Summary of commands

                  Name Type Syntax Reference                   Description
     +ETBM                                                                                                          Parameter                                                   Extended                                       6.5.6                                                                                                                                             Call termination buffer management
     +GCAP                                                                                                               Action                                                                                                             Extended                                       6.1.9                                                                                                                                             Request complete capabilities list
     +GCI Parameter Extended 6.1.10 Country of installation
     +GMI Action Extended 6.1.4 Request manufacturer identification
     +GMM Action Extended 6.1.5  Request model identification
     +GMR                                                                                                                                Action                                                                                                             Extended                                       6.1.6                                                                                                                                             Request revision identification
     +GOI                                                                                                                                                      Action                                                                                                             Extended                                       6.1.8                                                                                                                                             Request global object identification
     +GSN                                                                                                                                            Action                                                                                                             Extended                                       6.1.7                                                                                                                                             Request product serial number identification
     +ICF                                                                                                                                                                Parameter                                                   Extended                                       6.2.11                                                                                                                       DTE-DCE character framing
     +IFC                                                                                                                                                                Parameter                                                   Extended                                       6.2.12                                                                                                                       DTE-DCE local flow control
     +ILRR                                                                                                                                Parameter                                                   Extended                                       6.2.13                                                                                                                       DTE-DCE local rate reporting
     +IPR                                                                                                                                                                Parameter                                                   Extended                                       6.2.10                                                                                                                       Fixed DTE rate
     +MA                                                                                                                                                             Parameter                                                   Extended                                       6.4.2                                                                                                                                             Modulation automode control
     +MR                                                                                                                                                                Parameter                                                   Extended                                       6.4.3                                                                                                                                             Modulation reporting control
     +MS Parameter Extended 6.4.1 Modulation selection
     +MV18AM                                          Parameter                                                  Extended                                      6.4.6                                                                                                                                         Answering message editing
     +MV18P                                                                                         Parameter                                                   Extended                                       6.4.7                                                                                                                                             Order of probes
     +MV18R                                                                                    Parameter                                                   Extended                                       6.4.5                                                                                                                                             V.18 reporting control
     +MV18S Parameter Extended 6.4.4      V.18 selection
     +PCW                                                                                                                                     Parameter                                                   Extended                                       6.8.1                                                                                                                                             Call Waiting enable
     +PMH Parameter Extended 6.8.2 Modem-on-Hold enable
     +PMHT Parameter Extended 6.8.3    Modem-on-Hold Timer
     +PMHR                                                                                                        Action                                                                                                             Extended                                       6.8.4                                                                                                                                             Initiate Modem on Hold
     +PIG                                                                                                                                                             Parameter                                                   Extended                                       6.8.5                                                                                                                                             PCM upstream ignore
     +PMHF                                                                                                             Parameter                                                   Extended                                       6.8.6                                                                                                                                             V.92 Modem-on-Hold Hook Flash
     +PQC                                                                                                                                               Parameter                                                   Extended                                       6.8.7                                                                                                                                             V.92 Phase 1 and Phase 2 Control
     +PSS                                                                                                                                                           Parameter                                                   Extended                                       6.8.8                                                                                                                                             Use Short Sequence
     +TMO Parameter Extended 6.9    V.59 command
     A Action Basic 6.3.5 Answer
     D Action Basic 6.3.1 Dial
     E Parameter Basic 6.2.4 Command echo
     H Action Basic 6.3.6 Hook control
     I                                                                                                                                                                                                                                              Action                                                                                                             Basic                                                                                                            6.1.3                                                                                                                                             Request identification information
     L                                                                                                                                                                                                                                  Parameter                                                   Basic                                                                                                            6.3.13                                                                                                                       Monitor speaker loudness
     M Parameter Basic 6.3.14 Monitor speaker mode
     O                                                                                                                                                                                                                             Action                                                                                                             Basic                                                                                                            6.3.7                                                                                                                                             Return to online data state
     P                                                                                                                                                                                                                                    Parameter                                                   Basic                                                                                                            6.3.3                                                                                                                                             Select pulse dialling
     Q                                                                                                                                                                                                                             Parameter                                                   Basic                                                                                                            6.2.5                                                                                                                                             Result code suppression
     S0 Parameter Basic 6.3.8 Automatic answer
     S10                                                                                                                                                                                        Parameter                                                   Basic                                                                                                            6.3.12                                                                                                                       Automatic disconnect delay


94                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 101

Table I.2/V.250 – Summary of commands

                         Name Type Syntax Reference                   Description
       S3                                                                                                                                                                                                              Parameter                                                   Basic                                                                                                            6.2.1                                                                                                                                             Command line termination character
       S4                                                                                                                                                                                                              Parameter                                                   Basic                                                                                                            6.2.2                                                                                                                                             Response formatting character
       S5                                                                                                                                                                                                              Parameter                                                   Basic                                                                                                            6.2.3                                                                                                                                             Command line editing character
       S6 Parameter Basic 6.3.9 Pause before blind dialling
       S7                                                                                                                                                                                                              Parameter                                                   Basic                                                                                                            6.3.10                                                                                                                       Connection completion timeout
       S8                                                                                                                                                                                                              Parameter                                                   Basic                                                                                                            6.3.11                                                                                                                       Comma dial modifier time
       T                                                                                                                                                                                                                                  Parameter                                                   Basic                                                                                                            6.3.2                                                                                                                                             Select tone dialling
       V                                                                                                                                                                                                                             Parameter                                                   Basic                                                                                                            6.2.6                                                                                                                                             DCE response format
       X                                                                                                                                                                                                                             Parameter                                                   Basic                                                                                                            6.2.7                                                                                                                                             Result code selection and call progress monitoring
                                                                                                                                                                                                                                                                                                                                     control
       Z                                                                                                                                                                                                                                  Action                                                                                                             Basic                                                                                                            6.1.1                                                                                                                                             Reset to default configuration


                                                                                                                                                                                                                                                                               Appendix II

                                   DCE configuration, dialling, negotiation and reporting, example session

                                                                                                                                                                                                                                                                        Table II.1/V.250

                                                  DTE                                                                                                              DCE
                                Command                                                                                                              Response                                                                                                                                                                                                                                                     DCE action                                                                                                                                                                                                                                                                                                                              Line  condition                                                                                                                        Reference
       AT+GCAP                                                                                                                                                                                                             +MS, +ES,    Indicate areas of capabilities                                                                                                               On-hook                                                                                                                                                                            6.1.9
                                                                                                                                          +DS
                                                                                                                                          OK
       AT&F                                                                                                                                                                                                                                                                            OK                                                                                                                                                                                                                Initialize parameters to factory On-hook 6.1.2
                                                                                                                                                                                                                                        default
       AT&D2                                                                                                                                                                                                                                               OK                                                                                                                                                                                                                Set-up of DTR hangup                                                                                                                                                                                                                   On-hook                                                                                                                                                                            6.2.9
       AT+MS=11,1;                                                                                                                                                                                                                      Set-up modulation enable                                                                                                                                                                                       On-hook 6.4.1
       +MR=1                                                                                                                              OK                                                                                            reports                                                                                                                                                                                                                                                                                                                         6.4.3

       AT+ES=3,0,2;                                                                                                                                                                                                                     Set-up error control enable                                                                                                                                                                                    On-hook 6.5.1
       +ER=1                                                                                                                              OK                                                                                            reports                                                                                                                                                                                                                                                                                                                         6.5.5

       AT+DS=3,1;                                                                                                                                                                                                                       Set-up compression enable                                                                                                                                                                                      On-hook 6.6.1
       +DR=1                                                                                                                              OK                                                                                            reports                                                                                                                                                                                                                                                                                                                         6.6.2

       AT+IFC=2,2                                                                                                                                                            OK                                                                                                                                                                                                                Set-up flow control                                                                                                                                                                                                                                                                              On-hook                                                                                                                                                                            6.2.12
       AT+IPR=57600;                                                                                                                                                                                                                    Set-up local port rate enable                                                                                                                                                                                  On-hook 6.2.12
      +ILRR=1                                                                                                                             OK                                                                                            reports                                                                                                                                                                                                                                                                                                                         6.2.13


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  95

## Page 102

Table II.1/V.250

                                  DTE                                                                        DCE
                      Command                                                                       Response                                                                                                                                                                                                                                                     DCE action                                                                                                                                                                                                                                                                                                                              Line  condition                                                                                                                        Reference
    ATDT<number>                                                                                                                                             Off-hook 6.3.1
                                                                                                                                                                                                                                                                                                       Dial <number>
                                                                                             +MCR:                                                                                                                                                                                                     Carrier type                                                               6.4.3
                                                                                             V32B                                                                                                                                                                                                      Carrier rate                                                               6.4.3
                                                                                             +MRR:                                                                                                                                                                                                     Error control                                                              6.4.5
                                                                                             14400                                                                                                                                                                                                     Compression                                                                6.5.5
                                                                                             +ER: LAPM                                                                                                                                                                                                 Local port rate                                                            6.6.2
                                                                                             +DR: V42B                                                                                                                                                                                                 Result code
                                                                                             +ILRR:
                                                                                             57600
                                                                                             CONNECT
                                                                                             <text>
    <data> –>                                                                                                                                                                                                                                    <– <data>                                                                                              Data connection                                                                                                                                                                                                                                                                                                                                     Connection
    <negate ckt 108/2>                                               OK                                                                                                                                                                                                                Hang up                                                                                                                                                                                                                                                                                                                                                                                                                                                                             Hang up                                                                                                                                                                                  6.3.6


                                                                                                                                                                                   Appendix III

                                                                                  Encapsulation of V.250 messages in V.25   bis DCE

III.1 Scope
This appendix defines the means to use V.250 messages in a V.25 bis compliant DCE.
This Recommendation contains three types of messages:
–                                                                                                                           commands, with parameters as needed;
–                                                                                                                           final or intermediate result codes;
– information text.

III.2                                                       Encapsulation of V.250 messages
A DCE compliant with ITU-T Rec. V.25 bis and this appendix shall implement two new opcodes.
See Table III.1.

                                                     Table III.1/V.250 – V.250 opcodes for encapsulation of V.250 messages

                   Op Code                                                                                                                                                                                                                              Description                                                                                                                                                                                                                                                                                                              Messages                                                                                                                                                                                                                                                                                                                                                           Examples
     EXC EXtended Command Commands                                                          EXC+GMI?
                                                                                                                                                                                                                                                                                                                    EXC+MR=1
     EXI                                                                                                                                                                                                                                                                EXtended Indication                                                                                                                                      Result codes, EXI+MCR: V32B
                                                                                                                                                                                                  information text                                                                                                  EXI+MRR: 14400


96                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 103

Unless otherwise noted in this appendix, any valid individual V.250 command, information text or
result code may be encapsulated as a V.25 bis message. The V.250 command may include any
necessary parameter values, of numeric, string or compound type.

III.3 Applicable V.250 commands
See Table III.2.

                                                                                             Table III.2/V.250 – V.250 commands for use in V.25 bis DCE

                       Name Type Reference             Description
     &C                                                                                                                                                                                                                 Parameter                                                                                                                                                       6.2.8                                                                                                                                                                                        Circuit 109 (Received line signal detector) behaviour
     &F                                                                                                                                                                                                                      Action                                                                                                                                                                                                                 6.1.2                                                                                                                                                                                        Set to factory-defined configuration
     +GCAP                                                                                                                                   Action                                                                                                                                                                                                                 6.1.9                                                                                                                                                                                        Request complete capabilities list
     +GMI Action   6.1.4  Request manufacturer identification
     +GMM Action      6.1.5    Request model identification
     +GMR                                                                                                                                                    Action                                                                                                                                                                                                                 6.1.6                                                                                                                                                                                        Request revision identification
     +GOI                                                                                                                                                                          Action                                                                                                                                                                                                                 6.1.8                                                                                                                                                                                        Request global object identification
     +GSN                                                                                                                                                                Action                                                                                                                                                                                                                 6.1.7                                                                                                                                                                                        Request product serial number identification
     +GCI Parameter 6.1.10 Country of installation
     +MA                                                                                                                                                                                 Parameter                                                                                                                                                       6.4.2                                                                                                                                                                                        Modulation automode control
     +MR                                                                                                                                                                                   Parameter                                                                                                                                                       6.4.3                                                                                                                                                                                        Modulation reporting control
     +MS Parameter 6.4.1 Modulation selection
     L                                                                                                                                                                                                                                                     Parameter                                                                                                                                                       6.3.13                                                                                                                                                                  Monitor speaker loudness
     M Parameter 6.3.14 Monitor speaker mode
     S6 Parameter 6.3.9 Pause before blind dialling
     S7                                                                                                                                                                                                                                  Parameter                                                                                                                                                       6.3.10                                                                                                                                                                  Connection completion timeout
     S10                                                                                                                                                                                                            Parameter                                                                                                                                                       6.3.12                                                                                                                                                                  Automatic disconnect delay
     Z                                                                                                                                                                                                                                                     Action                                                                                                                                                                                                                 6.1.1                                                                                                                                                                                        Reset to default configuration
All other V.250 commands are either not applicable or reserved for future study.


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ITU-T Rec. V.250 (07/2003)                                                                                                                  97

## Page 104

III.4 Applicable V.250 responses
See Table III.3.

                                                                                            Table III.3/V.250 – V.250 commands for use in V.25 bis DCE

                                      Name Type Reference   Description
     +MCR: <carrier>                                                                          Intermediate result                                                      6.4.3                                                                                                                                                                                   DCE-DCE carrier report
     +MRR: <rate>                                                                                                                             Intermediate result                                                       6.4.3                                                                                                                                                                                        DCE-DCE carrier rate report
     +GMI response                                                                                                               Information text                                                                                                   6.1.4                                                                                                                                                                                        Mfg ID
     +GMM response                                                                                       Information text                                                                                                   6.1.5                                                                                                                                                                                        Model ID
     +GMR response                                                                                                Information text                                                                                                   6.1.6                                                                                                                                                                                        Revision ID
     +GOI response                                                                                                                      Information text                                                                                                   6.1.8                                                                                                                                                                                        Object ID
     +GSN response                                                                                                             Information text                                                                                                   6.1.7                                                                                                                                                                                        Serial number
     +GCAP response                                                                               Information text                                                                                                   6.1.9                                                                                                                                                                                        Capabilities
All other V.250 indications are either not applicable or reserved for future study.


98                                                                                                                           ITU-T Rec. V.250 (07/2003)

## Page 105

_No extractable text found on this page._

## Page 106

SERIES OF ITU-T RECOMMENDATIONS


Series A                                                                            Organization of the work of ITU-T

Series B                                                                              Means of expression: definitions, symbols, classification

Series C                                                                              General telecommunication statistics

Series D                                                                            General tariff principles

Series E                                                                                 Overall network operation, telephone service, service operation and human factors

Series F                                                                                   Non-telephone telecommunication services

Series G                                                                            Transmission systems and media, digital systems and networks

Series H                                                                            Audiovisual and multimedia systems

Series I                                                                                             Integrated services digital network

Series J                                                                                          Cable networks and transmission of television, sound programme and other multimedia signals

Series K                                                                            Protection against interference

Series L                                                                                 Construction, installation and protection of cables and other elements of outside plant

Series M                                                                    TMN and network maintenance: international transmission systems, telephone circuits,
                                 telegraphy, facsimile and leased circuits

Series N                                                                            Maintenance: international sound programme and television transmission circuits

Series O                                                                            Specifications of measuring equipment

Series P                                                                                   Telephone transmission quality, telephone installations, local line networks

Series Q                                                                            Switching and signalling

Series R                                                                              Telegraph transmission

Series S                                                                                   Telegraph services terminal equipment

Series T                                                                                 Terminals for telematic services

Series U                                                                            Telegraph switching

Series V                                                                       Data communication over the telephone network

Series X                                                                            Data networks and open system communications

Series Y                                                                            Global information infrastructure and Internet protocol aspects

Series Z                                                                                 Languages and general software aspects for telecommunication systems


                                                                                                                                                                                                                   Printed in Switzerland
                                                                                                                                                                                                                                        Geneva, 2004
