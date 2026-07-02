# westermo-td22

> Generated from the sibling PDF for local reference search and review.
> Source PDF: `westermo-td22.pdf`
> Source SHA-256: `d85d3db6333d2d79e887f0823c0c1cf0405df081bf723c84419e2f4dbe45ef43`
> Generated: 2026-07-02
> Extractor: pypdf 6.14.2
> Note: This is text extraction only; tables, columns, images, and scanned pages may need manual verification against the PDF.

## Extraction Summary

- Pages: 51
- Pages without extractable text: 0

## PDF Metadata

| Key | Value |
|---|---|
| CreationDate | D:19990107120223 |
| Creator | QuarkXPress Passportª: PSPrinter 8.3.1 |
| ModDate | D:19990107120422 |
| Producer | Acrobat Distiller 3.01 för Power Macintosh |
| Title | TD-22 ¥ 6177-2203 ENG |

## Page 1

INSTALLATION MANUAL
TD-22 AC                                6177-2203
TD-22 DC


                               Galvanic  Transient   CE
                                IsolationProtectionApproved


                                    PTT Modem


                                        www.westermo.se

## Page 2

TABLE OF CONTENTS

INTRODUCTION       ................................................................................................................................................          4

SAFETY    ...................................................................................................................................................................................          4

SPECIFICATIONS         ..................................................................................................................................................          5

INSTALLATION     .......................................................................................................................................................          5

RS-232 CONNECTIONS     ...........................................................................................................................          6

LINE CONNECTIONS   ..................................................................................................................................          6

TYPICAL TD-22 LINE CONNECTIONS     ..........................................................................          7

DIP SWITCH SETUP    ............................................................................................................................         8–10

LED STATUS INDICATORS      ............................................................................................................         11

DTE COMMAND LINES     ........................................................................................................................         12

AT COMMAND SET      .......................................................................................................................         13–32

RESULT CODES     ..................................................................................................................................................         33

S-REGISTERS  .................................................................................................................................................         34–42

BLOCK DIAGRAM ..........................................................................................................................................         42

APPLICATION EXAMPLES     ....................................................................................................         43–46

GLOSSARY   ......................................................................................................................................................         47–50


6177-2203                                                                                                                                                                                            3

## Page 3

Introduction
The Westermo TD-22 is an industrialised dial and leased line modem. This modem has been
developed to be used in industrial applications and has some features you would not expect to find on
a normal modem.

Terminal data rates of up to 19.2 kbit/sec can be handled using data compression and error
correction. Direct mode connections with the maximum line modulation speed of  2,400 bit/sec can
be made.

Lease line 2 and 4 wire connections can be made as well as multidrop half duplex V.23 connections.
The modem can also be used on ordinary twisted pair cables to provide long distance asynchronous
communications.

A        watchdog facility continually monitors the power supply and internal hardware as well as the
operational software. In the event of a problem the modem automatically resets. This feature has
been included to make the unit more suitable for use in unmanned locations.

The TD-22 is available in two standard versions for power supply by 230VAC or 12-36VDC. Special
115VAC and 36-55VDC models are also available on request.

Westermo have implemented commands often left out of standard modems like &D to allow
hardware controlled dial out on receipt of a hardware signal and &A        to prevent aborts on connection
when extra characters are received.

The TD-22 can handle 11 data bits and has a special 2 stop bit mode to allow the unit to be used in
applications many modems can’t handle.

The TD-22 has been designed with the engineer in mind, hence the extensive information on the
command set, S registers, DIP         switched and error codes. We have endeavoured to include all
necessary information however if you need more please do not hesitate to call us.

Safety
This equipment should only be installed by professional service personnel. If the unit is intended for
permanent connection to mains supply, there should be a readily accessible
disconnect device (circuit breaker) incorporated into the fixed wiring.

All ports on this equipment are designed for connection to TNV          circuits.
The mains connection is classified as excessive voltage
Description of the above classifications are given in EN60950:1992.


4                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         6177-2203

## Page 4

Specifications

       Modulation                                                                                                                                                                                                                                                                                                                                                                                                                              CCITT         V.22bis, 2400 bit/s
                                                                                                                                                                                               CCITT         V.22, Bell 212A, 1200 bit/s
                                                                                                                                                                                               CCITT         V.21, Bell 103, 300 bit/s
                                                                                                                                                                                               CCITT         V.23, 1200/75 bit/s & 1200 Hdx
       Dial up                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          Tone signals DTMF
       Settings                                                                                                                                                                                                                                                                                                                                                                                                                                                                          AT-commands & switches
       Transmission                                                                                                                                                                                                                                                                                                                                                                                            Asynchronous & Synchronous
       Transmission speed, DTE                                                                                                                                                                                            300, 600, 1200, 2400, 9600, 19200 bit/s
       Compression                                                                                                                                                                                                                                                                                                                                                                                            V.42bis & MNP5 (Not leased Line)
       Characters                                                                                                                                                                                                                                                                                                                                                                                                                              up to 11 bits
       Error correction                                                                                                                                                                                                                                                                                                                                                         V.42, MNP         2-4 & MNP         10 (Not leased line)
       Interface                                                                                                                                                                                                                                                                                                                                                                                                                                                                EIA        RS-232-C/V.24/V.28
       Line interface                                                                                                                                                                                                                                                                                                                                                                                       RJ12 or 4-pole screw connector
       Line                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    2-wire for dial up connections
                                                                                                                                                                                               2- or 4-wire for leased line connections
       REN, Ringer Equivalence Number                                                             1
       Power                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    230 VAC -10% +15%, 48-62 Hz (TD-22 AC)
                                                                                                                                                                                               12-36 VDC (TD-22 DC)
                                                                                                                                                                                               115 VAC or 36-60 VDC can be delivered as optional
       Power consumption                                                                                                                                                                                                                                                                                         25 mA        at 230 VAC
                                                                                                                                                                                               200 mA        at 12 VDC
       Isolation                                                                                                                                                                                                                                                                                                                                                                                                                                                                      Between line, RS-232 connection and power 1500V.
       Temperature/humidity                                                                                                                                                                                                                                                             5-50°C surrounding temperature
                                                                                                                                                                                               0-95% RH, without condensation
                                                                                                                                                                                               Extended temperature can be delivered as an option
       0-95% RH, without condensation                                                                                  Extended temperature can be delivered as an option
       Size, mm (W*H*D)                                                                                                                                                                                                                                                                                                         55 * 100 * 128
       Weight, kg                                                                                                                                                                                                                                                                                                                                                                                                                                    0.6 (TD-22 AC) & 0.4 (TD-22 DC)
       Indications                                                                                                                                                                                                                                                                                                                                                                                                                                  PWR, LINE, ANS, REL, TD, RD, RTS, DCD, DTR & DSR

Installation
The modem should be
connected in the following                                                                                                                                                                                                                                                                                                                                                                                                        Screw-block
way:                                                                                                                                                                                                                                                                                                                                                                                                                              for RS-232
Power connection is made                                                                                                                                                                                                                                                                                                                                                                                                          connection
through screw-block at bottom                                                                                                                                           Light emitting
right corner.                                                                                                                                                                                          diodes
For 115V        AC or 230V        AC it is
a 3-pole connector, and for 12-
36 or 36-55V          DC a 2-pole                                                                                                                                                                                                                                                                                                                                                                                              25-pole D-sub
connector                                                                                                                                                                                                                                                                                                                                                                                                                      for RS-232
Computers or other equipment                                                                                                                                                                                                                                                                                                                                                                                                   connection
are connected through an
RS-232 connection .                                                                                                                                           Line connection
This connection can be made                                                                                                                                                                               RJ-12
either to the 25-pole D-sub or
the 9-pole screw connector.
Do not use ribbon cable for                                                                                                                                   Line connection                                                                                                                                                                                                                                                  PWR
RS-232 connections.                                                                                                                                                             screw block                                                                                                                                                                                                                                    connection

6177-2203                                                                                                                                                                                                                                                                                                                                                                                                                                                                          5

## Page 5

RS-232 Connections
Pin outs for the  25-pole D-sub and 9-pole screw terminal:

            25 Pin                                                  Screw                                                                                         Direction                                                                           Name                                                 Description
            D-sub                                                        Terminal                                                       DCE-DTE
                                1                                                                                                                                                                                                                                                                                                                                   - - -                                                                                       PE                                                                                              Protective earth
                                2                                                                                          8                                                                                                                                                                                                                                                                                                                                                                                       TXD                                                                        Transmit data‹
                                3                                                                                          7                                                                                                                                                                                                                                                                                                                                                                                       RXD                                                                    Receive data
                                4                                                                                          6                                                                                                                                                                                                                                                                                                                                                                                       RTS                                                                        Request to send‹
                                5                                                                                          5                                                                                                                                                                                                                                                                                                                                                                                       CTS                                                                        Clear to send
                                6                                                                                          2                                                                                                                                                                                                                                                                                                                                                                                       DSR                                                                    Modem ready
                                7                                                                                          1                                                                                                                                                                                                                     - - -                                                                                                          SG                                                                                           Signal ground
                                8                                                                                          4                                                                                                                                                                                                                                                                                                                                                                                       DCD                                                                  Data carrier detect
                                9                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 Continuous high
                      10                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 Continuous low
                      12                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    DRS                                                                    Speed indication (1200/2400)
                      15                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    TXC                                                                        Synchronous TXD clock from modem
                      17                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    RXC                                                                    Synchronous RXD clock from modem
                      20                                                                                          3                                                                                                                                                                                                                                                                                                                                                                                       DTR                                                                      Data terminal ready‹
                      21                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    RDL                                                                        Request of remote digital loopback‹
                      22                                                                                          9                                                                                                                                                                                                                                                                                                                                                                                       RI                                                                                                          Ring indicator
                      23                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    DRS                                                                    Data speed select (1200/2400)‹
                      24                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    EXC                                                                      External synchronous clock‹
                      25                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    TI                                                                                                              Test indication signal
The other pins in the 25-pole D-sub should be left unconnected.


                                                                                                                                                                                                                                                                                                                                                                    Line connection
1                                                                                                                                                                                                                                                                                                                                                                   The telephone line is connected to the 6-pole RJ-12
:                                                                                                                                                                                                                                                                                                                                                                   connector or to the 4-pole screw block in the bottom
6                                                                                                                                                                                                                                                                                                                                                                   left side.
                                                                                                                                                                                                                                                                                                                                                                    When using the screw-block a strap plug supplied must
                                                                                                                                                                                                                                                                                                                                                                    be placed in the RJ-12 connector. If this is not done the
                                                                                                                                                                                                                                                                                                                                                                    outgoing signal will not be connected to the screw-
                                                                                                                                                                                                                                                                                                                                                                    block.

                                       1 2 3          4


2-wire lines are connected to the two middle pins
(3 & 4) in the RJ-12 plug or the TX screws
(1 & 2).
In the UK modems the RJ-12 connection is on pin
2 & 5.
4-wire lines are connected to the screw terminal,
transmitter to TX (1 & 2) and receiver to RX (3 & 4).


6                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         6177-2203

## Page 6

Typical TD-22 line connections

4-position
screw-terminal


                                               Leased line 2-wire


4-position
screw-terminal


                                               Leased line 4-wire


Handshaking                      The TD-22 is delivered with a factory setting for ”hardware hand-
                                 shake” with RTS-CTS which means that if only TX, RX and GND
                                 are connected no data will be sent on the receiving modem’s RS-232
                                 connection unless RTS is high. The problem can be solved by placing

    9 8  7 6  5 4  3 2  1        a jumper between RTS (screw terminal no 6) and for example DSR
                                 (screw terminal no 2) or by disabling the handshake with the command
                                 AT&K0.


4-position
screw-terminal
                                     Telephone                               Telephone
                                     exchange                                exchange        Telephone modem
                                                    Dial up line 2-wire


RJ-12 connector


       3                             Telephone                               Telephone
       4                             exchange                                exchange         Telephone modem
                                                    Dial up line 2-wire


6177-2203                                                                                                        7

## Page 7

DIP Switch Setup
                                                 Disconnect power before changing DIP-switches.
                                                 Take ESD-protection when changing switches.

 The DIP-switches can be used to provide the following settings.
 The DIP-switches are underneath the top lid of the modem. (1 = On, 0 = Off)
 Non defined switches will be in off position.


 SW1:

              4321
                                                                                   0                                                                                                                                                    Dial-up line
                                                                                   1                                                                       0                                                          Leased line, switch off echo and result code. Answering
                                                                                   1                                                                       1                                                          Leased line, switch off echo and result code. Dialling.

              1                                                                                                                                                                                                                                                                                                                               All commands ignored, including +++


SW2:

            8765 4321

                                                                                                                                                                                                                                                                      0                                                   SW2:2 to 7 not used
                                                                                                                                                                                                                                                                      1                                                   For use of SW2:2 to 7
                                                                                                                                                                                                                                                                                                  Choice of clock:                                                                                                                                  Clock source:
                                                                                                                                                                                               0                                                                   0                                                                                                                                           Asynchronous
                                                                                                                                                                                               0                                                                   1                                                                                                                                           Synchronous Ext.                                                                                                        Clock from D-sub pin 24

                                                                                                                                                                                               1                                                                   0                                                                                                                                           Synchronous Int.                                                                                                                   The modem creates clock
                                                                                                                                                                                               1                                                                   1                                                                                                                                           Synchronous slave                                                                Clock from line
                                                                                                                                                            1                                                                                                                                                                                                                                                                                                                        DTR/DSR disconnected         (AT&S0&D0&C0)
                                                                                 1                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     V.25 commands enabled
                                              1                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            Remote configuration enabled
            1                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 Callback security enabled


SW3:

             8765 4321
                                                                                                                                                                                                                                                                      0                                                               4-wire line connection
                                                                                                                                                                                                                                                                      1                                                               2-wire line connection
                                                                                                                                                                                                                                   0
                                                                                                                                                                                                1                                                                                                                                                                                                                                            REL-mode disconnected

8                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         6177-2203

## Page 8

SW4:

                       87654321                                                                                                                             Select speed and parity towards
                                                                                                                                                                                                                                                                                                                                                                                                                                                                       computer/terminal
                                                                                                                                                                                                                                                 0                                                                  0                                                                       0                                                                     0                                                        Automatic speed and format setting up
                                                                                                                                                                                                                                                 0                                                                  0                                                                       0                                                                     1                                                         300 Baud

                                                                                                                                                                                                                                                 0                                                                  0                                                                       1                                                                     0                                                         600 Baud
                                                                                                                                                                                                                                                 0                                                                  0                                                                       1                                                                     1                                                         1200 Baud
                                                                                                                                                                                                                                                 0                                                                  1                                                                       0                                                                     0                                                         2400 Baud
                                                                                                                                                                                                                                                 0                                                                  1                                                                       0                                                                     1                                                         4800 Baud
                                                                                                                                                                                                                                                 0                                                                  1                                                                       1                                                                     0                                                         9600 Baud

                                                                                                                                                                                                                                                 0                                                                  1                                                                       1                                                                     1                                                         19200 Baud
                                                                            0                                                                  0                                                                     0                                                                                                                                                                                                                                                                                                                                                                                                                         7,N
                                                                            0                                                                  0                                                                     1                                                                                                                                                                                                                                                                                                                                                                                                                         7,E
                                                                            0                                                                  1                                                                     0                                                                                                                                                                                                                                                                                                                                                                                                                         7,O
                                                                            0                                                                  1                                                                     1                                                                                                                                                                                                                                                                                                                                                                                                                         8,N
                                                                            1                                                                  0                                                                     0                                                                                                                                                                                                                                                                                                                                                                                                                         8,E

                                                                            1                                                                  0                                                                     1                                                                                                                                                                                                                                                                                                                                                                                                                         8,O
                                                                            1                                                                  1                                                                     0                                                                                                                                                                                                                                                                                                                                                                                                                         8,O*                             Sets direct-mode AT&Q0
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     (Used for 8 bits, optional parity)
                                                                            1                                                                  1                                                                     1                                                                                                                                                                                                                                                                                                                                                                                                                         8,N*                               Sets direct-mode AT&Q0
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     (Used for 7 bits, optional parity)

                       1                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          2-stop bits

*)                    The modem use this format when sending information to the DTE only in command mode.


6177-2203                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         9

## Page 9

SW5:

                            8765321                                                                                                                                                                                                        Select Line Modulation
                                                                                                                                                                                                                                                                                           0                                                                    0                                                                      0                                                               Use saved parameters
                                                                                                                                                                                                                                                                                           0                                                                    0                                                                      1                                                               V.21 300 bps (0-300)                                                                                                                                                                                                                                  ATF1

                                                                                                                                                                                                                                                                                           0                                                                    1                                                                      0                                                               V.23 Hdx                                                                                                                                                                                                                                                                                                                                                                                                                        ATF3 %F3
                                                                                                                                                                                                                                                                                           0                                                                    1                                                                      1                                                               V.23 1200/75 bps                                                                                                                                                                                                                                                                                        ATF3
                                                                                                                                                                                                                                                                                           1                                                                    0                                                                      0                                                               V.22 1200 bps                                                                                                                                                                                                                                                                                                                                       ATF4
                                                                                                                                                                                                                                                                                           1                                                                    0                                                                      1                                                               V.22 bis 2400 bps                                                                                                                                                                                                                                                                                 ATF5
                                                                                                                                                                                                                                                                                           1                                                                    1                                                                      1                                                               Auto-detect mode                                                                                                                                                                                                                                                                                    ATF0


10                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 10

LED Status Indicators

     PWR:                                                                      Full Intensity                                                                                                                                                                                                                                                                                                                                                                                                                                                   The modem is functioning normally
                                           Half intensity                                                                                                                                                                                                                                                                                                                                                                                                                                                The modem is in test mode or remote
                                                                                                                                                                                                configuration mode
                                           Occasional flashing with speaker click                                                     Power supply problem
                                           1:6, on:off ratio                                                                                                                                                                                                                                                                                                                                                                                                                RAM error
                                           On/off with speaker click                                                                                                                                                                                                                                                                 Modem unable to start
     LINE                                                                                   LED lights up when the modem has the line
     ANS                                                                                         LED flashes when a ring is detected on the line. The ANS indicator shines constantly
                                           when answering an incoming call and remains lit thereafter to indicate the modem is
                                           in autoanswer mode.
     REL                                                                                            When the modem is in error correcting and compressing mode this LED is on.When
                                           the modem is in direct or normal mode this LED remains off.
     TD                                                                                                                   Transmitted Data: Shows data received from the local RS-232 port
     RD                                                                                                               Received Data: Shows data leaving the modem on the RS-232 port

     RTS                                                                                            Request to Send signal from the DTE
     DCD                                                                                       Data Carrier Detect modem signal
     DSR                                                                                         Data Set Ready modem signal

Please also refer to AT&C, AT\N, AT&T, ATS0


6177-2203                                                                                                                                                                                                                                                                                                                                                11

## Page 11

DTE Command Lines
A        command line is a string of characters sent from a DTE to the modem (DCE) while the modem is
in a command state. A        command line has a prefix, a body, and a terminator. Each command line
(with the exception of the A/ command) must begin with the character sequence AT          and must be
terminated by a carriage return. Commands entered in upper case or lower case are accepted, but
both the A        and T          must be of the same case, i.e., ”AT” = ASCII 065, 084 or ”at” = ASCII 097, 116.

The body is a string of commands restricted to printable ASCII characters (032 – 126). Space
characters (ASCII 032) and control characters other than CR (ASCII 013) and BS (ASCII 008) in the
command string are ignored. The default terminator is the ASCII <CR> character. Characters that
precede the AT          prefix are ignored. The command line interpretation begins upon receipt of the
carriage return character.
Characters within the command line are parsed as commands with associated parameter values. The
basic commands consist of single ASCII characters, or single characters preceded by a prefix character
e.g., &           or %            or \           or )          followed by a decimal parameter. Missing decimal parameters are evaluated as 0.

The modem supports the editing of command lines by recognising a backspace character. When
modem echo is enabled, the modem responds to receipt of a backspace or delete by echoing a
backspace character, a space character, and another backspace. The hex value to be used for the
backspace character is programmable through register S5. Values equal to 0 or greater than 127, or
the value which corresponds to the carriage return character, cannot be used for the backspace
character. This editing is not applicable to the AT          header of a command. A        command line may be
aborted at any time by entering < ctrl-x > (18h).
The AT          sequence may be followed by any number of commands in sequence, except for commands
such as Z, D, or A. Commands following commands Z, D, or A        on the same command line will be
ignored. The maximum number of characters on any command line is 39 (including ”A” and ”T”). If
a syntax error is found anywhere in a command line command, the remainder of the line will be
ignored and the ERROR result code will be returned.
Most commands entered with parameters out of range will not be accepted and the ERROR response
will be returned to the DTE.

Commands will only be accepted by the modem once the previous command has been fully
executed, which is normally indicated by the return of an appropriate result code. Execution of
commands D and A, either as a result of a direct command or a re-execute command, will be aborted
if another character is entered before completion of the handshake.

When the modem has established a connection and has entered on-line data mode, it is possible to
break into the data transmission in order to issue further commands to the modem in an on-line
command mode. This is achieved by the DTE sending to the modem a sequence of three ASCII
characters specified by register S2. The default character is ‘+’. The timing of the three characters
must comply with specific time constraints. There is a guard time before the first character (the pre-
sequence time), a guard time following the third character (the post-sequence time), and a guard
time-out between the first and second characters and between the second and third characters (the
inter-character time). These times are controlled by the value recorded in register S12.


12                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 12

AT COMMAND SET
The modem will respond to the commands detailed below. Parameters applicable to each command
are listed with the command description.
A/ – Re-execute Command
The modem behaves as though the last command line had been re-sent by the DTE. ”A/” will repeat
all the commands in the command buffer.
The principal application of this command is to place another call (using the Dial command) that
failed to connect due to a busy line, no answer, or a wrong number. This command must appear alone
on a command line. This command should not be terminated by a carriage return.
A – Answer
The modem will go off-hook and attempt to answer an incoming call if correct conditions are met.
Upon successful completion of answer handshake, the modem will go on-line in answer mode. This
command may be affected by the state of Line Current Sense, if enabled. (Most countries do not
require Line Current Sense.) Operation is also dependent upon country-specific requirements.
Please also refer to ATDn, S0, S1, S7, S8, S9 and S30.
\An – Select Maximum MNP Block Size
The modem will operate an MNP         error corrected link using a maximum block size controlled by the
parameter supplied. The parameter value, if valid, is written to S40 bits 6 and 7.
\A0                                                                                                                                                                        64 characters.
\A1                                                                                                                                                                        128 characters.
\A2                                                                                                                                                                        192 characters. (Default.)
\A3                                                                                                                                                                        256 characters.
Please also refer to AT\N
&An – Dial Abort Option
The modem normally aborts the connection negotiation if a character is received from DTE during
the connection phase. This command give the user the option to let the modem ignore incoming
characters.
&A0                                                                                                                                                    Enable abort. (Default.)
&A1                                                                                                                                                    Disable abort.
Please also refer to AT&D
Bn – CCITT or Bell
When the modem is configured to allow either option, the modem will select Bell (American
standard) or CCITT          (European standard) modulation for a line speed connection of 300 or 1200 bps
according to the parameter supplied. Any other line speed will use a CCITT          modulation standard.
B0                                                                                                                                                                                      Selects CCITT          operation at 300 or 1200 bps. (Default.)
B1                                                                                                                                                                                      Selects BELL         operation at 300 or 1200 bps
Please also refer to ATFn, S27


6177-2203                                                                                                                                                                             13

## Page 13

&Bn – DTR Dial Option
This command interprets the OFF to ON transition of the DTR signal from the DTE to enable the
modem to dial the telephone number stored with ATZ0=...... The parameter value, if valid, is written
to S27 bits 7.
&B0                                                                                                                                                      No action. (Default.)
&B1                                                                                                                                                      Dial on OFF to ON transition of the DTR signal.
Please also refer to AT&Z and AT&D.
\Bn – Transmit Break to Remote
In non-error correction mode, the modem will transmit a break signal to the remote modem with a
length in multiples of 100 ms according to parameter specified. If a number in excess of 9 is entered,
9 is used. The command works in conjunction with the \K command.
In error correction mode, the modem will signal a break through the active error correction protocol,
giving no indication of the length.
\B1-\B9                                                                                                  Break length in 100 ms units. (Default = 3.)

Note: When the modem receives a break from the remote modem, break is passed to the DTE as
follows: In non-error correction mode direct, the break length is passed; in non-error correction mode
normal and in error correction mode, a 300 ms break is passed.
&Cn – DCD Option
The modem controls the DCD output in accordance with the parameter supplied. The parameter
value, if valid, is written to S21 bit 5.
On leased line DCD always follows the state of the carrier.
&C0                                                                                                                                                      DCD remains ON at all times. (Default.)
&C1                                                                                                                                                      DCD follows the state of the carrier.
%C – Enable/Disable Data Compression
Enables or disables data compression negotiation. The modem can only perform data compression on
an error corrected link. The parameter value, if valid, is written to S41 bits 0, 1 and S46 bit 1.
%C0                                                                                                                                                    Disables data compression.
%C1                                                                                                                                                    Enables MNP         5 data compression negotiation.
%C2                                                                                                                                                    Enables V.42 bis data compression.
%C3                                                                                                                                                    Enables both V.42 bis and MNP         5 data compression. (Default)
Please also refer to AT\Nn.
*Cn – Password for remote control
This command directs the modem to ask for a password, ”ENTER PASSWORD”. In order to execute
remote configuration the calling modem will have to present the password before making any
editing. The password must consist of between 6 and 12 letters. The default setting is ”QWERTY”.
This command is available only for MNP         connections.
Please also refer to AT\Nn, AT*E and AT*R.
Not available in remote programming mode (AT*R)


14                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 14

Dn – Dial
This command directs the modem to go on-line, dial according to the string entered and attempt to
establish a connection. If no dial string is supplied, the modem will go on-line and attempt the
handshake in originate mode.
Dial Modifiers.

The valid dial string parameters are described below.

0-9                                                                                                                                                                               DTMF digits 0 to 9.
*                                                                                                                                                                                                                 The 'star' digit (tone dialling only).
#                                                                                                                                                                                                                 The 'gate' digit (tone dialling only).
A-D                                                                                                                                                            DTMF digits A, B, C, and D. Some countries may prohibit sending of these digits
                                   during dialling.
J                                                                                                                                                                                                                    Perform MNP         10 link negotiation at the highest supported speed (for this call only).
                                   (also refer to AT*H)
K                                                                                                                                                                                                       Enable power level adjustment during MNP         10 link negotiation (for this call only).
                                   (also refer to ATMn)
L                                                                                                                                                                                                           Re-dial last number: the modem will re-dial the last valid telephone number. The L
                                   must be immediately after the D with all the following characters ignored.
P                                                                                                                                                                                                              Select pulse dialling. In most countries is this not available
T                                                                                                                                                                                                           Select tone dialling (DTMF) tone dialling is normally used in this modem.
S=n                                                                                                                                                                   Dial the number stored in the directory (n = 0 to 3 or 0 to 19 depending on the modem
                                   model). (See &Z.)
W                                                                                                                                                                                              Wait for dial tone: the modem will wait for dial tone before dialling the digits
                                   following ”W”. If dial tone is not detected within the time specified by S6 the modem
                                   will abort the rest of the sequence, return on-hook, and generate an error message.
,                                                                                                                                                                                                                          Dial pause: the modem will pause for a time specified by S8 before dialling the digits
                                   following ”,”.
;                                                                                                                                                                                                                         Return to command state. Added to the end of a dial string, this causes the modem to
                                   return to the command state after it processes the portion of the dial string preceding
                                   the ”;”. This allows the user to issue additional AT          commands while remaining off-
                                   hook. The additional AT          commands may be placed in the original command line
                                   following the ”;” and/or may be entered on subsequent command lines. The modem
                                   will enter call progress only after an additional dial command is issued without the ”;”
                                   terminator. Use ”H” to abort the dial in progress, and go back on-hook.
^                                                                                                                                                                                                                  Toggles calling tone enable/disable: applicable to current dial attempt only.
(-),<space>                                           Ignored: may be used to format the dial string.

If the ATD.. command returns ERROR the ATX0 is probably set in a country that does not allow
blind dialling.
Please also refer to ATA, ATX, AT&Z, S6, S7, S8, S9, S30.


6177-2203                                                                                                                                                                                                                                                     15

## Page 15

&Dn – DTR Option
This command interprets the ON to OFF transition of the DTR signal from the DTE in accordance
with the parameter supplied. The parameter value, if valid, is written to S21 bits 3 and 4.

&D0                                                                                                                                                   DTR drop is interpreted according to the setting as follows: (Default.)
                                        &Q0, &Q5 or &Q6
                                                                DTR is ignored (assumed ON). Allows operation with DTEs which do
                                                                not provide DTR.
                                        &Q1 or &Q4
                                                                DTR drop causes the modem to hang up.
                                        &Q2 or &Q3
                                                                DTR drop causes the modem to hang up. Auto-answer goes off.

&D1                                                                                                                                                    DTR drop is interpreted according to the setting as follows:
                                        &Q0, &Q1, &Q4, &Q5 or &Q6
                                                                The modem returns to command state without disconnecting.
                                        &Q2 or &Q3
                                                                DTR drop causes the modem to hang up. Auto-answer goes off.

&D2                                                                                                                                                    DTR drop causes the modem to hang up. Auto-answer is inhibited.

&D3                                                                                                                                                    DTR drop is interpreted according to the setting as follows:
                                        &Q0, &Q1, &Q4, &Q5 or &Q6
                                                                DTR drop causes the modem to perform a soft reset as if the Z command were
                                                                received. The &Y         setting determines which profile is loaded.
                                        &Q2 or &Q3
                                                                DTR drop causes the modem to hang up. Auto-answer goes off.
Please also refer to AT&M, AT&Q, S21.

%D0                                                                                                                                                  Disconnect the connection at a signal level below -43dBm. (Default.)
%D1                                                                                                                                                  Disconnect the connection at a signal level below the value stored in S24.
Please also refer to S24
En – Command Echo
The modem enables or disables the echo of characters to the DTE according to the parameter
supplied. The parameter value, if valid, is written to S14 bit 1.

E0                                                                                                                                                                                        Disables command echo.
E1                                                                                                                                                                                        Enables command echo. (Default.)
Please also refer to ATQ


16                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 16

%En – Enable/Disable Line Quality Monitor and Auto-
Retrain or Fallback/Fall Forward (V.22bis only)
Controls whether or not the modem will automatically monitor the line quality and request a retrain
(%E1) or fall back when line quality is insufficient or fall forward when line quality is sufficient
(%E2). The parameter value, if valid, is written to S41 bits 2 and 6.
If enabled, the modem attempts to retrain for a maximum of 30 seconds.

%E0                                                                                                                                                      Disable line quality monitor and auto-retrain.
%E1                                                                                                                                                      Enable line quality monitor and auto-retrain (default).
Please also refer to ATO
*E – Exit Remote configuration mode
Upon receipt of this command from the telephone line, the modem will exit remote configuration
mode and transmit the OK result code to the line.Please also refer to S202.
Fn – Select Line Modulation
This command selects the line modulation according to the parameter supplied. The line modulation
is fixed unless Automode is selected. This command interacts with the S37 and the N command. The
parameter value, if valid, is written to S37 bits 0-4. To select line modulation it is recommended that
either the F command, or a combination of the S37 and the N command are used but not both.

F0                                                                                                                                                                                          Selects auto-detect mode. Sets N1 and sets S31 bit 1. In this mode, the modem
                       configures for Automode operation. All connect speeds supported by the modem are
                       possible according to the remote modem's preference. The contents of S37 are ignored
                       as is the sensed DTE speed.
F1                                                                                                                                                                                          Selects V.21 or Bell 103 according to the B setting as the only acceptable line speed
                       resulting in a subsequent connection. Sets N0, sets S37 to 1, and clears S31 bit 1.
                       This command is equivalent to the command string: ATN0S37=1.
F2                                                                                                                                                                                          Not supported.
F3                                                                                                                                                                                          Selects V.23 as the only acceptable line modulation for a subsequent connection.
                       Originator is at 75 bps and answer is at 1200 bps. Sets N0, sets S37 to 7, and clears
                       S31 bit 1. This command is equivalent to the command string: ATN0S37=7.
F4                                                                                                                                                                                          Selects V.22 1200 or Bell 212A        according to the B command setting as the only
                       acceptable line speed for a subsequent connection. Sets N0, sets S37 to 5, and clears
                       S31 bit 1. This command is equivalent to the command string: ATN0S37=5.
F5                                                                                                                                                                                          Selects V.22 bis as the only acceptable line modulation for a subsequent connection.
                       Sets N0, sets S37 to 6, and clears S31 bit 1. This command is equivalent to the
                       command string: ATN0S37=6.
Please also refer to ATN, ATB, S37
&F – Recall (restore) factory configuration
This command restores the original factory setting of all parameters. The factory defaults are
identified for each command and in the S-register descriptions. When the modem is restarted the
parameters set by the customer will be restored. By using the &W          command the factory settings will
be valid also after restart of the modem.


6177-2203                                                                                                                                                                 17

## Page 17

%Fn – Split-Speed direction select, V.23
Determines which direction (transmit or receive) has the 75bps channel and which has the 1200bps
channel. This command is only valid if the \W1 command has been executed.

%F1                                                                                                                                                        Selects 75Tx/1200Rx. resets S28 bits 1 and 2. (Default.)
%F2                                                                                                                                                        Selects 1200Tx/75Rx. resets S28 bits 1 and resets S28 bit 2.
%F3                                                                                                                                                        Selects 1200Tx/1200Rx. half duplex.
Please also refer to ATF, ATN, AT\W, S28, S37
\F – display telephone directory
The modem displays the telephone directory entries which were stored with the &Z command.
Please also refer to AT&Z, ATD
&Gn – Select Guard Tone
The modem generates the guard tone selected by this command according to the parameter supplied
(DPSK modulation modes only). The parameter value, if valid, is written to S23 bits 6 and 7.

&G0                                                                                                                                                    Disables guard tone. (Default.)
&G1                                                                                                                                                    Disables guard tone.
&G2                                                                                                                                                    Selects 1800 Hz guard tone.

This command may not be permitted in some countries.

\Gn – Modem-to-Modem Flow Control (XON/XOFF)
In non-error correction mode, the modem enables or disables the generation or recognition of
modem-to-modem XON/XOFF flow control according to the parameter supplied. The parameter
value, if valid, is written to S41 bit 3.
In error correction mode, the setting of modem-to-modem XON/XOFF flow control is ignored.
However, the serial port flow control settings (AT&K) remain active during a reliable link.
Due to the buffering system used in the modem, modem-to-modem flow control is normally disabled.

\G0                                                                                                                                                                        Disables modem-to-modem XON/XOFF flow control. (Default.)
\G1                                                                                                                                                                        Enables modem-to-modem XON/XOFF flow control.
Please also refer to AT&K, S41


18                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 18

Hn – Disconnect (hang up)
This command initiates a hang up sequence.
This command may not be available for some countries due to PTT          restrictions.

H0                                                                                                                                                                                   The modem will release the line if the modem is currently on-line, and will terminate
                                       any test (AT&T) that is in progress. Country specific, modulation specific, and error
                                       correction protocol specific (S38) processing is handled outside of the H0 command.
H1                                                                                                                                                                                   If on-hook, the modem will go off-hook and enter command mode. The modem will
                                       return on-hook after a period of time determined by S7.
Please also refer to S7. Not available in remote programming mode (AT*R).
*Hn – MNP10 connection speed
This command controls the line speed at initial hook-up between two MNP10 modems. The value is
stored in register S40, bit 2.

*H0                                                                                                                                                               Connection is executed at maximum available speed. (Default.)
*H1                                                                                                                                                               Connection is executed at 1200 Baud.
Please also refer to AT\N
In – Identification
The modem reports to the DTE the requested result according to the command parameter.

I0                                                                                                                                                                                                   Reports product code.
I1                                                                                                                                                                                                   Reports a precomputed checksum (see firmware release notes).
I2                                                                                                                                                                                                   Reports ”OK”.
I3                                                                                                                                                                                                   Reports firmware revision.
I4                                                                                                                                                                                                   Reports OEM identifier string.
I5                                                                                                                                                                                                   Reports Country Code parameter (e.g., ”022”).
I6                                                                                                                                                                                                   Reports modem data pump model and internal code revision
                                       (e.g., RC144DPL         Rev CE).
I9                                                                                                                                                                                                   Reports date and time for firmware revision.
&Kn – Flow Control
This command defines the DTE/DCE (terminal/modem) flow control mechanism. The parameter
value, if valid, is written to S39 bits 0, 1, and 2.

&K0                                                                                                                                                    Disables flow control.
&K3                                                                                                                                                    Enables RTS/CTS flow control. (Default for data modem modes.)
&K4                                                                                                                                                    Enables XON/XOFF flow control.


6177-2203                                                                                                                                                                                                                                                                            19

## Page 19

\Kn – Break Control
Controls the response of the modem to a break received from the DTE or the remote modem or the
\B command according to the parameter supplied. The parameter value, if valid, is written to S40 bits
3, 4, and 5.
The response is different in three separate states.
The first state is where the modem receives a break from the DTE when the modem is operating in
data transfer mode:

\K0                                                                                                                                                                        Enter on-line command mode, no break sent to the remote modem.
\K1                                                                                                                                                                        Clear data buffers and send break to remote modem.
\K2                                                                                                                                                                        Enter on-line command mode, no break sent to the remote modem. Same as 0.
\K3                                                                                                                                                                        Send break to remote modem immediately.
\K4                                                                                                                                                                        Enter on-line command mode, no break sent to the remote modem. Same as 0.
\K5                                                                                                                                                                        Send break to remote modem in sequence with transmitted data. (Default.)

The second case is where the modem is in the on-line command state (waiting for AT          commands)
during a data connection, and the \B is received in order to send a break to the remote modem:

\K0                                                                                                                                                                        Clear data buffers and send break to remote modem.
\K1                                                                                                                                                                        Clear data buffers and send break to remote modem. Same as 0.
\K2                                                                                                                                                                        Send break to remote modem immediately.
\K3                                                                                                                                                                        Send break to remote modem immediately. Same as 2.
\K4                                                                                                                                                                        Send break to remote modem in sequence with data.
\K5                                                                                                                                                                        Send break to remote modem in sequence with data. Same as 4. (Default.)

The third case is where a break is received from a remote modem during a non-error corrected
connection:

\K0                                                                                                                                                                        Clears data buffers and sends break to the DTE.
\K1                                                                                                                                                                        Clears data buffers and sends break to the DTE. Same as 0.
\K2                                                                                                                                                                        Send a break immediately to DTE.
\K3                                                                                                                                                                        Send a break immediately to DTE. Same as 2.
\K4                                                                                                                                                                        Send a break in sequence with received data to DTE.
\K5                                                                                                                                                                        Send a break in sequence with received data to DTE. Same as 4. (Default.)
-Kn Extended Services
This command allows the conversion of a V.42 LAPM to a MNP10 connection. The parameter value
is written to S40 bit 0.

-K0                                                                                                                                                                      No V.42 LAPM to MNP10 conversion (Default.)
-K1                                                                                                                                                                      V.42 LAPM to MNP10 conversion
-K2                                                                                                                                                                     V.42 LAPM to MNP10 conversion, The conversion is blocked during V.42 LAPM
                                                reply sequence detection.
Please also refer to AT\N


20                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 20

Ln – Speaker Volume
The modem sets the speaker volume control according to the parameter supplied. The parameter
value, if valid, is written to S22 bits 0 and 1.

L0                                                                                                                                                                                        Speaker off.
L1                                                                                                                                                                                        Low volume. (Default.)
L2                                                                                                                                                                                        Medium volume.
L3                                                                                                                                                                                        High volume.
Please also refer to ATM
%L – Return received line signal quality
Returns a value which indicates the received signal level. The value returned is a direct indication
(DAA        dependent) of the receive level at the MDP, not at the telephone line connector. For example
007= -7dBm, 033 = -33dBm. For leased line the value should be better than -40dBm. The noise
should be at least 6dBm lower than the signal itself in order for the communication to work properly.
Please also refer to AT%Q
\Ln – MNP Block Stream Mode Select
At connection time this command controls the selection between block and stream modes of
operation in MNP. The parameter value, if valid, is written to S41 bit 4.

\L0                                                                                                                                                                             Use stream mode for MNP         connection. (Default).
\L1                                                                                                                                                                            Use interactive block mode for MNP         connection. This command will accept block
                              mode but implement stream mode.
Please also refer to AT\N


6177-2203                                                                                                                                                                                                             21

## Page 21

*L – Display secure access (callback) directory
                                                       The modem will display all secure access (callback) directory entries.
                                                       Format: Entry Number – Password: Callback Number.
                                                       Example:
                                                       0-ABRAKADABRA                                                                                                                                                        (password entered; no callback number)
                                                       1-ABCDE:012-3456789                                                                                                     (password and number)
                                                       2-
                                                       3-
                                                       4-
                                                       5-
                                                       6-
                                                       7-
                                                       8-
                                                       9-
                                                       10-
                                                       11 -
                                                       12-
                                                       13-
                                                       14-
                                                       15-
                                                       16-
                                                       17-
                                                       18-
                                                       19-

                                                       OK

Please also refer to AT*P
Not available in remote programming mode, AT*R.
Mn – Speaker Control
This command selects when the speaker will be on or off. The parameter value, if valid, is written to
S22 bits 2 and 3.

M0                                                                                                                                                                             Speaker is always off.
M1                                                                                                                                                                             Speaker is on during call establishment, but off when receiving carrier. (Default.)
M2                                                                                                                                                                             Speaker is always on.
M3                                                                                                                                                                             Speaker is off when receiving carrier and during dialling, but on during answering.
Please also refer to ATL


22                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 22

&Mn – Asynchronous/Synchronous Mode Selection
This command determines the DTR operating mode. The modem treats the &M command as a subset
of the &Q command.

&M0                                                                                                                                             Selects direct asynchronous operation. Note that the command sequence &M0\N0
                        selects normal buffered mode, but the command sequence \N0&M0 selects direct
                        mode. This is because the \N0 command is analogous to the &Q6 command.
                        The value 000b is written in the S27 bits 3, 1 and 0 respectively. (See &Q)
&M1                                                                                                                                             Selects synchronous connect mode with asynchronous off-line command mode.
                        The value 001b is written to S27 bits 3,1, and 0, respectively.
&M2                                                                                                                                             Selects synchronous connect mode with asynchronous off-line command mode. same
                        as &M1 except that &M2 enables DTR dialling of direct slot 0. The modem will
                        disconnect if DTR is OFF for more than the period in S25 (in units of hundredths of a
                        second) the data connection will be synchronous. The value 010b is written to S27 bits
                        3,1 and 0 respectively.
&M3                                                                                                                                             Selects synchronous connect mode. This mode allows DTR to act as a voice/data
                        switch. The call is manually initiated while DTR is inactive. When DTR becomes
                        active, the handshake proceeds in originate or answer mode according to S14 bit 7.
                        The value 011b is written to S27 bits 3,1 and 0 respectively.
Please also refer to AT&D, AT&Q, AT\N
)Mn – Enable Cellular Power Level Adjustment
Enables or disables automatic adjustment of the transmit power level during link negotiation
for reliable links to accommodate the signalling requirements of cellular telephone equipment.
The parameter value, if valid, is written to S40 bit 2.

)M0                                                                                                                                                               Disables power level adjustment during MNP10 link negotiation. (Default.)
)M1                                                                                                                                                               Enables power level adjustment during MNP10 link negotiation.
Please also refer to AT\N
Nn – Automode Enable
This command enables or disables Automode detection. The parameter value, if valid, is written to
S31 bit 1.

N0                                                                                                                                                                                   Automode detection is disabled. A        subsequent handshake will be conducted according
                        to the contents of S37 or, if S37 is zero, according to the most recently sensed DTE
                        speed.
N1                                                                                                                                                                                   Automode detection is enabled. A        subsequent handshake will be conducted according
                        to the Automode algorithm supported by the modem. This command is equivalent to
                        F0. (Default.)
Please also refer to ATF


6177-2203                                                                                                                                                                   23

## Page 23

\Nn – Operating Mode
This command controls the preferred error correcting mode to be negotiated in a subsequent data
connection. This command is affected by the OEM firmware configuration.
\N0                                                                                                                                                                        Selects normal speed buffered mode (disables error-correction mode). (Forces &Q6.)
\N1                                                                                                                                                                        Selects direct mode and is equivalent to &M0, &Q0 mode of operation. (Forces &Q0.)
                               In this mode the serial port is directly connected to the data pump, which results in the
                               lowest possible delay time. This is useful in i.e. the case of polled PLC systems where
                               the response time is critical.
\N2                                                                                                                                                                        Selects reliable (error-correction) mode. The modem will first attempt a LAPM
                               connection and then an MNP         connection. Failure to make a reliable connection results
                               in the modem hanging up. (Forces &Q6, S36=4, and S48=7.)
\N3                                                                                                                                                                        Selects auto reliable mode. This operates the same as \N2 except failure to make a
                               reliable connection results in the modem falling back to the speed buffered normal
                               mode, \N0. (Forces &Q5, S36=7, and S48=7.)
\N4                                                                                                                                                                        Selects LAPM error-correction mode. Failure to make an LAPM error-correction
                               connection results in the modem hanging up. (Forces &Q5 and S48=0.) Note: The -K1
                               command overrides the \N4 command.
\N5                                                                                                                                                                        Selects MNP         error-correction mode. Failure to make an MNP         error-correction
                               connection results in the modem hanging up. (Forces &Q5, S36=4, and S48=128.)
(Default \N3)
Please also refer to AT&M, AT&Q, S36, S48
On – Return to On-Line Data Mode
This command determines how the modem will enter the on-line data mode. If the modem is in the
on-line command mode, the enters the on-line data mode with or without a retrain. If the modem is in
the off-line command mode (no connection), ERROR is reported.
O0                                                                                                                                                                                   Enters on-line data mode without a retrain. Handling is determined by the Call
                               Establishment task. Generally, if a connection exists, this command connects the DTE
                               back to the remote modem after an escape (+++).
O1                                                                                                                                                                                   Enters on-line data mode with a retrain before returning to on-line data mode.
Please also refer to S2, S12
*P – Store Dial Back Number and Password
This command causes the modem to store a password and to store or delete a corresponding
telephone number in NVRAM. The password will be used to match the supplied by a  remote modem
when secure access is used. The modem will use the telephone number to dial back the remote
modem. The password must be between 6 and 12 characters in length. The telephone number length
is 40 characters maximum. If the number to be dialled back (along with the final colon) is omitted, a
password check will be performed, but no callback will occur. Up to 20 passwords/telephone number
pairs may be entered.

                               AT*Pn: <password>:<number to be dialled back>
                               Parameters: 0 to 19
Please also refer to AT*L
Not available in remote programming mode (AT*R)


24                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 24

Qn – Quiet Results Codes Control
The command enables or disables the sending of result codes to the DTE according to the parameter
supplied. The parameter value, if valid, is written to S14 bit 2.

Q0                                                                                                                                                                                   Enables result codes to the DTE. (Default.)
Q1                                                                                                                                                                                   Disables result codes to the DTE.
Please also refer to ATV, ATE
&Qn – Asynchronous/Synchronous Mode
This command is used to control the connection modes permitted. It is used in conjunction with S36
and S48. (Also, see \N.) When the &Q0 to &Q4 command is issued to select the mode, the subsequent
connect message will report the DCE speed regardless of the W          command and S95 settings.

&Q0                                                                                                                                                    Selects direct asynchronous operation (&M0).
&Q1                                                                                                                                                    Selects synchronous operation and asynchronous commands (&M1).
&Q2                                                                                                                                                    Selects synchronous operation and asynchronous commands (&M2)
&Q3                                                                                                                                                    Selects synchronous operation with DTR as ”VOICE/DATA” switch (&M3)
&Q4                                                                                                                                                   Selects AutoSync operation. The value 100b is written to S27 bits 3, 1, and 0,
                        respectively.
                        AutoSync operation, when used in conjunction with the Hayes Synchronous Interface
                        (HSI) capability in the DTE, provides synchronous communication capability from an
                        asynchronous terminal.
                        Starting AutoSync.           Set registers S19, S20, and S25 to the desired values before
                        selecting AutoSync operation with &Q4. After the CONNECT          message is issued, the
                        modem waits the period of time specified by S25 before examining DTR. If DTR is on,
                        the modem enters the synchronous operating state; if DTR is off, the modem terminates
                        the line connection and returns to the command state.
                        Stopping AutoSync.        AutoSync operation is stopped upon loss of carrier or the on-to-
                        off transition of DTR. Loss of carrier will cause the modem to return to the command
                        state. An on-to-off transition of DTR will cause the modem to return to the command
                        state and either not terminate the line connection (&D1 active) or terminate the line
                        connection (any other &Dn command active).
&Q5                                                                                                                                                   The modem will try to negotiate an error-corrected link. The modem can be configured
                        using S36 to determine whether a failure will result in the modem returning on-hook or
                        will result in fallback to an connection. The value 101b is written to S27 bits 3, 1, and
                        0, respectively. (Default.)
&Q6                                                                                                                                                   Selects asynchronous operation in normal mode (speed buffering). The value 110b is
                        written to S27 bits 3, 1, and 0, respectively.
%Q – Line Signal Quality
Reports the line signal quality at V.22 and V.22bis. Returns the higher order byte of the EQM value.
Based on the EQM value, retrain or fallback/fall forward may be initiated if enabled by %E1.
A        low value is good.
Please also refer to AT%L


6177-2203                                                                                                                                                                      25

## Page 25

&Rn – RTS/CTS Option
This selects how the modem controls CTS. CTS operation is modified if hardware flow control is
selected (see &K command). The parameter value, if valid, is written to S21 bit 2.

&R0                                                                                                                                                      In Synchronous mode the CTS signals follows the status of RTS. The delay between
                               RTS and CTS is set in register S26. In asynchronous mode the CTS acts according to
                               V.25 bis handshake.
&R1                                                                                                                                                      In synchronous mode CTS is always high. In asynchronous mode CTS will only drop if
                               required by flow control. (Default.)
*R – Request Remote Configuration Mode
This command from the DTE requests the local modem attempt to place the remote modem in
remote configuration mode. The command will only be accepted if the local modem is in on-line
command state during an MNP         error corrected link. Enter the password (from 6 to 12 characters in
length) after the REMOTE PASSWORD prompt is displayed by the local DTE. The entered
password is inserted in a remote configuration request (a special MNP         frame) and is sent to the
remote modem.
Following a successful request, indicated by the display of the !AT          prompt by the local DTE, the
local DTE may send commands to the remote modem. These commands, a subset of the normal
commands available, should be entered without the ”AT” header. Some commands are prohibited and
others may produce unpredictable results. To exit the remote configuration mode, enter the *E
command or the escape sequence defined by register S202. The default password is QWERTY.
Please also refer to AT*C, AT*E, AT/N and S202.
Sn – Read/Write S-Register
The modem selects an S-Register, performs an S-Register read or write function, or reports the value
of an S-Register.

n                                                                                                                                                                                                                 Establishes S-Register n as the last register accessed.
n=v                                                                                                                                                                     Sets S-Register n to the value v.
n?                                                                                                                                                                                               Reports the value of S-Register n.

The parameter n can be omitted, in which case the last S-Register accessed will be assumed.
The S can be omitted for AT= and AT?, in which case the last S-Register accessed will be assumed.

For example:
                               ATS7 establishes S7 as the last accessed register.
                               AT=40 sets the contents of the last register accessed to 40.
                               AT? returns the content of the default register.
                               ATS=20 sets the contents of the last register accessed to 20.
                               ATS6=30 sets the content of register S6 to 30.
                               ATS2? returns the content of  register S2
Please also refer to the S-register section later in this manual.


26                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 26

&Sn – DSR Override
This command selects how the modem will control DSR. The parameter value, if valid, is written to
S21 bit 6.

&S0                                                                                                                                                           DSR will remain ON at all times. (Default.)
&S1                                                                                                                                                          DSR will become active after answer tone has been detected and inactive after the
                          carrier has been lost.
Please also refer to AT&D, AT&C
\S – Report Active Configuration
The modem reports the current (active) configuration for display.

Example:

AT\S

CMD DESCRIPTION / OPTION                              CMD DESCRIPTION / OPTION                                    CMD DESCRIPTION / OPTION

          COUNTRY.........SWE                                &A  CHR ABORT OPT....NO                                    *H  NEG. SPEED.....HIGH
          DTE BPS........9600                               &B  DTR DIAL OPTION..NO                                    S0  RINGS TO ANS....002
          DTE PARITY....8NONE                               &C  DCD OPTION.......ON                                     S1  RING COUNT......000
          LINE SPEED.....NONE                               &D  DTR OPTION........0                                     S2  <ESC> CHAR......043
B                             BELL MODE.......OFF                               &G  GUARD TONE.....NONE                                     S3  <CR> CHAR.......013
E                             CMD ECHO.........ON                               &K  FLOW CONTROL....RTS                                     S4  <LF> CHAR.......010
F                             LINE MODE......AUTO                               &L  NETWORK........PSTN                                      S5  <BS> CHAR.......008
L                             SPKR VOLUME.....LOW                               &Q  ASYNC/SYNC........5                                      S7  CONNECT TIME....050
M                             SPKR CONTROL...CALL                               &R  RTS/CTS........AUTO                                      S8  PAUSE TIME......002
N                             AUTO MODE........ON                               &S  DSR OPT...........0                                     S12 ESC GUARD TIME..050
Q                             QUIET...........OFF                                &T  ENABLE RDL.......NO                                     S30 CONNECT INACT...000
V                             RESULT FORM....LONG                               &X  SYNC CLOCK......INT                                     S32 XON CHAR........017
W                             EC MSG............0                               &Y  PROFILE.......NVM.0                                      S33 XOFF CHAR.......019
X                             EXT RESULTS.......4                               \A  MAX BLK SIZE....192                                    S36 FALLBACK ACTION.007
Y                             LONG SPACE DISC..NO                              \G  REMOTE FLOW.....OFF                                     S37 MODE SELECT.....000
%C                    COMPRESSION....BOTH                                \K  BRK OPT...........5                                     S48 V42 NEG CTRL....007
-K                    EXT. SERVICES.....0                               \N  ECL MODE.......AUTO                                     S95 RES. CODE.......000

OK
Please also refer to AT&V


6177-2203                                                                                                                                                                                   27

## Page 27

&Tn – Test and Diagnostics
The modem will perform selected test and diagnostic functions according to the parameter supplied.
A        test can be run only when in an asynchronous operation in non-error-correction mode (normal or
direct mode). To terminate a test in progress, the escape sequence must be entered first, except for
parameters 7 and 8 (see Section 3.1.3). If S18 is non-zero, a test will terminate automatically after the
time specified by S18 and display the OK message.

&T0                                                                                                                                                        Terminates test in progress. Clears S16.
&T1                                                                                                                                                        Initiates local analogue loopback, V.54 Loop 3. Sets S16 bit 0. If a connection exists
                                   when this command is issued, the modem hangs up. The CONNECT          XXXX message
                                   is displayed upon the start of the test.
&T2                                                                                                                                                        Returns ERROR.
&T3                                                                                                                                                        Initiates local digital loopback, V.54 Loop 2. Sets S16 bit 2. If no connection exists,
                                   ERROR is returned. Sets S16 bit 4 when the test is in progress.
&T4                                                                                                                                                        Enables digital loopback acknowledgement for remote request, i.e., an RDL         request
                                   from a remote modem is allowed. Sets S23 bit 0.
&T5                                                                                                                                                        Disables digital loopback acknowledgement for remote request, i.e., an RDL         request
                                   from a remote modem is denied. Clears S23 bit 0. (Default.)
&T6                                                                                                                                                        Requests a remote digital loopback (RDL), V.54 Loop 2, with self test. (In self test, a
                                   test pattern is looped back and checked by the modem.) If no connection exists,
                                   ERROR is returned. When the test is terminated, due to the time setting according to
                                   S18 has passed or due to the execution of the &T0 or H command, the modem will
                                   report the number of errors occurred. S16 bit 6 will be set at the start of the test
                                   sequence.
&T7                                                                                                                                                        Initiates local analogue loopback, V.54 Loop 3, with self test as in &T6 above. If a
                                   connection exists, the modem hangs up before the test is initiated. When the test is
                                   terminated either via expiration of S18, or via the &T0 or H command, the number of
                                   detected errors is reported to the DTE. Sets S16 bit 6 when the test is in progress.
Please also refer to S18
Vn – Result Code Form
This command selects the sending of short-form or long-form result codes to the DTE. The
parameter, if valid, is written to S14 bit 3.

V0                                                                                                                                                                                   Enables short-form (terse) result codes. Line feed is not issued before a short-form
                                   result code.
V1                                                                                                                                                                                   Enables long-form (verbose) result codes. (Default.)
Please also refer to ATQn


28                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              6177-2203

## Page 28

&V – Display Current Configuration and Stored Profiles
Reports the current (active) configuration, the stored (user) profiles, and the first four stored
telephone numbers. The stored profiles and telephone numbers are not displayed if the NVRAM is
not installed or is not operational as detected by the NVRAM test during reset processing.

AT&V
ACTIVE PROFILE:
B0 E1 L1 M1 N1 Q0 T V1 W0 X4 Y0 &C0 &D0 &G0 &J0 &K3 &Q5 &R1 &S0 &T5 &X0 &Y0
S00:002 S01:000 S02:043 S03:013 S04:010 S05:008 S06:010 S07:050 S08:002 S09:006
S10:014 S11:095 S12:050 S18:000 S25:005 S26:001 S36:007 S37:000 S38:020 S44:020
S46:138 S48:007 S95:000
STORED PROFILE 0:
B0 E1 L1 M1 N1 Q0 T V1 W0 X4 Y0 &C0 &D0 &G0 &J0 &K3 &Q5 &R1 &S0 &T5 &X0
S00:002 S02:043 S06:010 S07:050 S08:002 S09:006 S10:014 S11:095 S12:050 S18:000
S36:007 S37:000 S40:168 S41:195 S46:138 S95:000
STORED PROFILE 1:
B0 E1 L1 M1 N1 Q0 T V1 W0 X4 Y0 &C0 &D0 &G0 &J0 &K3 &Q5 &R1 &S0 &T5 &X0
S00:002 S02:043 S06:010 S07:050 S08:002 S09:006 S10:014 S11:095 S12:050 S18:000
S36:007 S37:000 S40:168 S41:195 S46:138 S95:000
TELEPHONE NUMBERS:
0=                                                                                                                                                                                                                                          1=
2=                                                                                                                                                                                                                                          3=
OK
Please also refer to AT\S
Wn – Connect Message Control
This command controls the format of CONNECT          messages. The parameter value, if valid, is written
to S31 bits 2 and 3. Note that the Wn command can be overridden by register S95 bits (see S95
description).
W0                                                                                                                                                                          Upon connection, the modem reports only the DTE speed. Subsequent responses are
                      disabled. (Default.)
W1                                                                                                                                                                          Upon connection, the modem reports the line speed, the error correction protocol, and
                      the DTE speed, respectively. Subsequent responses are disabled.
W2                                                                                                                                                                          Upon connection, the modem reports the DCE speed. Subsequent responses are
                      disabled.
Please also refer to AT&Q
&Wn – Store Current Configuration
Saves the current (active) configuration (profile), including S-Registers, in one of the two user
profiles in NVRAM as denoted by the parameter value.
The current configuration is comprised of a list of storable parameters illustrated in the &V
command. These settings are restored to the active configuration upon receiving an Zn command or
at power up (see &Yn command).
&W0                                                                                                                                           Store the current configuration as profile 0.
&W1                                                                                                                                           Store the current configuration as profile 1.
Please also refer to AT&Y


6177-2203                                                                                                                                                      29

## Page 29

\W – Split Speed Operation
This command supports a split-speed DCE/DTE interface for applications such as Viewdata terminals
which require a transmit speed of 75 bps and receive speed of 1200 bps at the DTE interface. The
parameter value, if valid, is written to S28 bit 0.

\W0                                                                                                                                                               Disables split-speed mode. (Default.)
\W1                                                                                                                                                               Enables split-speed mode. V.23 operation is also forced as though F3 had been entered.
                              Note that Fn command determines the screen direction.
Please also refer to ATF3 and AT%F
Xn – Extended Result Codes:
This command selects which subset of the result messages will be used by the modem to inform the
DTE of the results of commands.
Blind dialling is enabled or disabled by country parameters. If the user wishes to enforce dial tone
detection, a ”W” can be placed in the dial string (see D command). Note that the information below
is based upon the default implementation of the X results table.

X0                                                                                                                                                                                   Disables monitoring of busy tones unless forced otherwise by country requirements;
                              send only OK, CONNECT, RING, NO CARRIER, ERROR, and NO ANSWER result
                              codes. Blind dialling is enabled/disabled by country parameters. If busy tone detection
                              is enforced and busy tone is detected, NO CARRIER will be reported. If dial tone
                              detection is enforced or selected and dial tone is not detected, NO CARRIER will be
                              reported instead of NO DIAL        TONE. The value 000b is written to S22 bits 6, 5, and 4,
                              respectively.
X1                                                                                                                                                                                   Disables monitoring of busy tones unless forced otherwise by country requirements;
                              send only OK, CONNECT, RING, NO CARRIER, ERROR, NO ANSWER, and
                              CONNECT          XXXX (XXXX = rate). Blind dialling enabled/disabled by country
                              parameters. If busy tone detection is enforced and busy tone is detected, NO CARRIER
                              will be reported instead of BUSY. If dial tone detection is enforced or selected and dial
                              tone is not detected, NO CARRIER will be reported instead of NO DIAL        TONE. The
                              value 100b is written to S22 bits 6, 5, and 4, respectively.
X2                                                                                                                                                                                   Disables monitoring of busy tones unless forced otherwise by country requirements;
                              send only OK, CONNECT, RING, NO CARRIER, ERROR, NO DIALTONE, NO
                              ANSWER, and CONNECT          XXXX. If busy tone detection is enforced and busy tone is
                              detected, NO CARRIER will be reported instead of BUSY. If dial tone detection is
                              enforced or selected and dial tone is not detected, NO DIAL        TONE will be reported
                              instead of NO CARRIER. The value 101b is written to S22 bits 6, 5, and 4,
                              respectively.
X3                                                                                                                                                                                   Enables monitoring of busy tones; send only OK, CONNECT, RING, NO CARRIER,
                              ERROR, NO ANSWER, and CONNECT          XXXX. Blind dialling is enabled/disabled by
                              country parameters. If dial tone detection is enforced and dial tone is not detected, NO
                              CARRIER will be reported. The value 110b is written to S22 bits 6, 5, and 4,
                              respectively.
X4                                                                                                                                                                                   Enables monitoring of busy tones; send all messages. The value 111b is written to S22
                              bits 6, 5, and 4, respectively. (Default.)
Please also refer to ATD, ATQn, ATVn, ATWn.


30                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 30

&Xn – Select Synchronous Clock Source
Selects the source of the transmit clock for the synchronous mode of operation. The parameter value
if valid is written to S27 bits 4 and 5.
In asynchronous mode, the transmit and receive clocks are turned OFF. In synchronous mode, the
clocks are turned ON with the frequency of 1200Hz or faster corresponding to the speed that is
selected for modem operation.

&X0                                                                                                                                                   Selects internal timing. The modem generates the transmit clock signal and applies it to
                         the TXCLK serial interface. (Default.)
&X1                                                                                                                                                   Selects external timing. The local DTE sources the transmit clock signal on the EXC
                         input of the serial interface. The modem applies this clock to the TXC output at the
                         serial interface.
&X2                                                                                                                                                   Selects slave receive timing. The modem derives the transmit clock signal from the
                         incoming carrier and applies it to the TXC output at the serial interface.
Yn – Long Space Disconnect
This command enables/disables the generation and response to long space disconnect. The parameter
value, if valid, is written to S21 bit 7.

Y0                                                                                                                                                                                   Disables long space disconnect. (Default.)
Y1                                                                                                                                                                                   Enables long space disconnect. In non-error correction mode, the modem will send a
                         long space of four seconds prior to going on-hook. In error correction mode, the
                         modem will respond to the receipt of a long space (i.e., a break signal greater than 1.6
                         seconds) by going on-hook.
&Yn – Designate a Default Reset Profile
Selects which user profile will be used after a hard reset.

                         &Y0                                                                               The modem will use profile 0.
                         &Y1                                                                               The modem will use profile 1.
Please also refer to AT&W


6177-2203                                                                                                                                                                            31

## Page 31

Zn – Soft Reset and Restore Profile
The modem performs a soft reset and restores (recalls) the configuration profile according to the
parameter supplied. If no parameter is specified, zero is assumed.

Z0                                                                                                                                                                                        Soft reset and restore stored profile 0.
Z1                                                                                                                                                                                        Soft reset and restore stored profile 1.
Please also refer to AT&W
&Zn – Store Telephone Number
The modem can store up to 20 telephone numbers and each telephone number dial string can contain
up to 45 digits.

AT&Zn=x                                                           n = 0 to 19 and x = dial string.

Example:
&Z1=0W112233
Dial zero, await tone and then dial the rest of the number.
Please also refer to AT\F, AT&V


32                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 32

Result Codes
When a command is sent from the terminal the modem replies with a result code. The reply may be
either in Long Form i.e. text (V1) or in Short Form, i.e. a two digit code (V0). The result code in
Long Form is followed by <CR><LF> and in Short Form by <CR>. The result codes are enabled by
using the Q1 command.
The result codes and their          short forms are as follows:
00                                                                                                                                                                                            OK                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               The OK code is returned to acknowledge execution
                                                                                                                                                                                                                                                                                                                                                                  of a command line.
01                                                                                                                                                                                            CONNECT                                                                                                                                                                                                                                                                                                                                                                                                          The modem will send this result code upon
                                                                                                                                                                                                                                                                                                                                                                  connecting with 300 baud.
02                                                                                                                                                                                            RING                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      Incoming ringing is detected on the line.
03                                                                                                                                                                                            NO CARRIER                                                                                                                                                                                                                                                                                                                                                     No carrier is detected.
04                                                                                                                                                                                            ERROR                                                                                                                                                                                                                                                                                                                                                                                                                                                                  The modem will send this result code when it´s
                                                                                                                                                                                                                                                                                                                                                                  unable to execute a command contained on the
                                                                                                                                                                                                                                                                                                                                                                  command line.
05                                                                                                                                                                                            CONNECT          1200                                                                                                                                                                                                                                                                                                               For connections with 1200 baud
06                                                                                                                                                                                            NO DIALTONE                                                                                                                                                                                                                                                                                                                            No dial tone is received.
07                                                                                                                                                                                            BUSY                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             Busy (engaged) signal is detected on the line.
08                                                                                                                                                                                            NO ANSWER                                                                                                                                                                                                                                                                                                                                                           No answer from remote modem detected on the
                                                                                                                                                                                                                                                                                                                                                                  line until the expiration of the time S7.
09                                                                                                                                                                                            CONNECT          600                                                                                                                                                                                                                                                                                                                                    For connections with 600 baud on the DTE.
10                                                                                                                                                                                            CONNECT          2400                                                                                                                                                                                                                                                                                                               For connections with 2400 baud on the DTE
11                                                                                                                                                                                              CONNECT          4800                                                                                                                                                                                                                                                                                                               For connections with 4800 baud on the DTE
12                                                                                                                                                                                            CONNECT          9600                                                                                                                                                                                                                                                                                                               For connections with 9600 baud on the DTE
16                                                                                                                                                                                            CONNECT          19200                                                                                                                                                                                                                                                                                           For connections with 19200 baud on the DTE
22                                                                                                                                                                                            CONNECT          75TX/1200RX                                                                                                                                             V.23 connections with 75 on TX and 1200 baud
                                                                                                                                                                                                                                                                                                                                                                  on RX
23                                                                                                                                                                                            CONNECT          1200TX/75RX                                                                                                                                   V.23 connections with 1200 on TX and 75 baud
                                                                                                                                                                                                                                                                                                                                                                  on RX
24                                                                                                                                                                                            DELAYED                                                                                                                                                                                                                                                                                                                                                                                                              If dialled number not allowed to dial yet
32                                                                                                                                                                                            BLACKLISTED                                                                                                                                                                                                                                                                                                                     If dialled number is not allowed
33                                                                                                                                                                                            FAX                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           If it's a fax connection
34                                                                                                                                                                                            +FCERROR                                                                                                                                                                                                                                                                                                                                                                                          Error in fax connections
35                                                                                                                                                                                            DATA                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 Connected as data modem
40                                                                                                                                                                                            CARRIER 300                                                                                                                                                                                                                                                                                                                                                   300 baud carrier
42                                                                                                                                                                                            CARRIER 600                                                                                                                                                                                                                                                                                                                                                   600 baud carrier
44                                                                                                                                                                                            CARRIER 1200/75                                                                                                                                                                                                                                                                           V.23 1200/75 baud carrier
45                                                                                                                                                                                            CARRIER 75/1200                                                                                                                                                                                                                                                                           V.23 75/1200 baud carrier
46                                                                                                                                                                                            CARRIER 1200                                                                                                                                                                                                                                                                                                                              1200 baud carrier
47                                                                                                                                                                                            CARRIER 2400                                                                                                                                                                                                                                                                                                                    2400 baud carrier
66                                                                                                                                                                                            COMPRESSION: CLASS 5                                                                                                                 MNP         class 5 compression aktive
67                                                                                                                                                                                            COMPRESSION: V.42 bis                                                                                                                                                    V.42 bis compression aktive
69                                                                                                                                                                                            COMPRESSION: NONE                                                                                                                                                                        No compression aktive
70                                                                                                                                                                                            PROTOCOL: NONE                                                                                                                                                                                                                                              No error correction aktive
77                                                                                                                                                                                            PROTOCOL: LAPM                                                                                                                                                                                                                                    LAPM error correction aktive
80                                                                                                                                                                                            PROTOCOL: ALT                                                                                                                                                                                                                                                                                      MNP4 error correction aktive
81                                                                                                                                                                                            PROTOCOL: ALT-CELLULAR                                               MNP10 with Cellular aktive
100                                                                                                                                                                        NOVRAM ERROR                                                                                                                                                                                                                                                                    Error in memory
101                                                                                                                                                                        NO NOVRAM                                                                                                                                                                                                                                                                                                                                                No memory mounted
102                                                                                                                                                                        SWITCH ERROR                                                                                                                                                                                                                                                                                             Error in switches
103                                                                                                                                                                        INACTIVITY        TIMEOUT                                                                                                                                                                 Inactivity time limit reached

6177-2203                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        33

## Page 33

S-REGISTERS
Below follows a description of the S-registers and its different components. Note that some
parameters can not be changed due to local PTT-regulations in different countries. Where nothing
else is stated the registers are stored by using the AT&W          command. Some registers are read only
registers and can not be changed from the DTE.
S0 – Number          of Rings to Auto-Answer
Sets the number of the rings required before the modem automatically answers a call. Zero disables
auto-answer mode.
Default 2 {0..255} Affected by country setting.
Please also refer to S1.
S1 – Ring Counter          (Read only register)
Incremented each time the modem detects a ring signal. Cleared if no rings occur after a short period
of time.
Please also refer to S0.
S2 – Escape Character
S2 holds the decimal value of the ASCII character used as the escape character. The default value
corresponds to an ASCII '+'. A        value over 127 disables the escape process, i.e., no escape character
will be recognised.
Default 43 (+) {0..255} ASCII decimal.
S3 – Carriage Return Character          (Not stored with the AT&W          command!)
Sets the command line and result code terminator character..
Default: 13 (Carriage Return) {0..127} ASCII decimal.
S4 – Line Feed Character          (Not stored with the AT&W          command!)
Sets the character recognised as a line feed.
Default 10 (Line Feed) {0..27} ASCII decimal.
S5 – Backspace Character          (Not stored with the AT&W          command!)
Sets the character recognised as a backspace. The modem will not recognise the Backspace character
if it is set to a value that is greater than 32.
Default 8 (Backspace) {0..32} ASCII decimal.
S6 – Wait Time for          Dial Tone
Sets the length of time, in seconds, that the modem will wait before starting to dial after going off-
hook when blind dialling.
Default 2 {2..255} seconds.
S7 – Wait Time For          Carrier        After          Dial
Sets the length of time, in seconds, that the modem will wait for carrier before hanging up.
Default 50 {1-255} seconds.
S8 – Pause Time For          Dial Delay
Sets the time, in seconds, that the modem must pause when the `,` dial modifier is encountered in the
dial string.
Default 2 {0-255} seconds.
S9 – Carrier          Detect Response Time
Sets the time, in tenths of a second, that the carrier must be present before the modem considers it
valid.
Default 6 (0.6 second) {1-255} tenths of a second.


34                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 34

S10 – Lost Carrier         To Hang Up Delay
Sets the length of time, in tenths of a second, that the modem waits before hanging up after a loss of
carrier. When register S10 is set to 255, the modem functions as if a carrier were always present.
The actual interval the modem waits before disconnecting is the value in register S10 minus the value
in register S9. Therefore, the S10 value must be greater than the S9 value or else the modem
disconnects before it recognises the carrier.
Default 14 (1.4 seconds) {1-255} tenths of a second.
S11 – DTMF         tone time (Only for USA)
The time a DTMF tone will be activated during dialling.
Default 95 {50..255} thousands of a second.
S12 – Escape Prompt Delay (EPD)
Time before entry/exit of command mode.
Default 50 (1 second) {0-255} fiftieths of a second.
S14 – General Bit Mapped Options Status (Read only register)
Indicates the status of command options.

Bit 0                                                                                                                                          This bit is ignored.
Bit 1                                                                                                                                          Command echo (En)
                                                         0 =                                                                                                                                                                                                                                              Disabled (E0)
                                                         1 =                                                                                                                                                                                                                                              Enabled (E1) (Default.)
Bit 2                                                                                                                                          Quiet mode (Qn)
                                                         0 =                                                                                                                                                                                                                                              Send result codes (Q0) (Default.)
                                                         1 =                                                                                                                                                                                                                                              Do not send result codes (Q1)
Bit 3                                                                                                                                          Result codes (Vn)
                                                         0 =                                                                                                                                                                                                                                              Numeric (V0)
                                                         1 =                                                                                                                                                                                                                                              Verbose (V1) (Default.)
Bit 4                                                                                                                                          Reserved
Bit 5                                                                                                                                          Tone (T)/Pulse (P)
                                                         0 =                                                                                                                                                                                                                                              Tone (T) (Default.)
                                                         1 =                                                                                                                                                                                                                                              Pulse (P)
Bit 6                                                                                                                                          2/4 wire line interface (AT&H)
                                                         0 =                                                                                                                                                                                                                                              2 wire (&H0) (Default.)
                                                         1 =                                                                                                                                                                                                                                              4 wire (&H1)
Bit 6                                                                                                                                          Reserved
Bit 7                                                                                                                                          Originate/Answer
                                                         0 =                                                                                                                                                                                                                                              Answer
                                                         1 =                                                                                                                                                                                                                                              Originate (Default.)


6177-2203                                                                                                                                                                                                                                                                                                                                                                                                                 35

## Page 35

S16 – General Bit Mapped Test Options Status (&T) (Read only register, not stored with &W)
Indicates the test in progress status.

Bit 0                                                                                                                                          Local analogue loopback
                                                                                    0 =                                                                                                                                                                                                                                              Disabled (Default.)
                                                                                    1 =                                                                                                                                                                                                                                              Enabled (&T1)
Bit 1                                                                                                                                          Not used
Bit 2                                                                                                                                          Local digital loopback
                                                                                    0 =                                                                                                                                                                                                                                              Disabled (Default.)
                                                                                    1 =                                                                                                                                                                                                                                              Enabled (&T3)
Bit 3                                                                                                                                          Remote digital loopback (RDL) status
                                                                                    0 =                                                                                                                                                                                                                                              Modem not in RDL         (Default.)
                                                                                    1 =                                                                                                                                                                                                                                              RDL         in progress
Bit 4                                                                                                                                          RDL         requested (AT&T6)
                                                                                    0 =                                                                                                                                                                                                                                              RDL         not requested (Default.)
                                                                                    1 =                                                                                                                                                                                                                                              RDL         requested (&T6)
Bit 5                                                                                                                                                    RDL         with self test
                                                                                    0 =                                                                                                                                                                                                                                              Disabled (Default.)
                                                                                    1 =                                                                                                                                                                                                                                              Enabled (&T7)
Bit 6                                                                                                                                          Local analogue loopback (LAL) with self test
                                                                                    0 =                                                                                                                                                                                                                                              Disabled (Default.)
                                                                                    1 =                                                                                                                                                                                                                                              Enabled (&T8)
Bit 7                                                                                                                                          Radio sub carrier option (AT&E)
                                                                                    0 =                                                                                                                                                                                                                                              Disabled (&E0) (Default.)
                                                                                    1 =                                                                                                                                                                                                                                              Enabled (&E1)
S18 – Test Timer
Sets the length of time, in seconds, that the modem conducts a test (commanded by &Tn) before
returning to the command mode. If this register value is zero, the test will not automatically
terminate.
Default 0 {0-255} seconds
S19 – AutoSync Bit Mapped Options
Normally not editable by the user.
S20 – AutoSync HDLC Address or          BSC Sync Character
Normally not editable by the user.
S21 – V.24/General Bit Mapped Options Status (Read only register)
Indicates the status of command options.

Bit 0                                                                                                                                                    Set by &Jn command but ignored otherwise.
                                                                                    0 =                                                                                                                                                                                                                                              &J0 (Default.)
                                                                                    1 =                                                                                                                                                                                                                                              &J1
Bit 1                                                                                                                                          Reserved
Bit 2                                                                                                                                          CTS behaviour (&Rn)
                                                                                    0 =                                                                                                                                                                                                                                              CTS tracks RTS (&R0)
                                                                                    1 =                                                                                                                                                                                                                                              CTS always on (&R1) (Default.)
Bits 3-4                                                                                        DTR behaviour (&Dn)
                                                                                    0 =                                                                                                                                                                                                                                              &D0 selected (Default.)
                                                                                    1 =                                                                                                                                                                                                                                              &D1 selected
                                                                                    2 =                                                                                                                                                                                                                                              &D2 selected
                                                                                    3 =                                                                                                                                                                                                                                              &D3 selected


36                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 36

Bit 5                                                                                                                                          RLSD (DCD) behaviour (&Cn)
                                                                                                            0 =                                                                                                                                                                                                                                              &C0 selected (Default.)
                                                                                                            1 =                                                                                                                                                                                                                                              &C1 selected
Bit 6                                                                                                                                          DSR behaviour (&Sn)
                                                                                                            0 =                                                                                                                                                                                                                                              &S0 selected (Default.)
                                                                                                            1 =                                                                                                                                                                                                                                              &S1 selected
Bit 7                                                                                                                                          Long space disconnect (Yn)
                                                                                                            0 =                                                                                                                                                                                                                                              Y0 (Default.)
                                                                                                            1 =                                                                                                                                                                                                                                              Y1
S22 – Speaker/Results Bit Mapped Options Status (Read only register)
Indicates the status of command options.

Bits 0-1                                                                                                   Speaker volume (Ln)
                                                                                                            0 =                                                                                                                                                                                                                                              Off (L0)
                                                                                                            1 =                                                                                                                                                                                                                                              Low (L1) (Default.)
                                                                                                            2 =                                                                                                                                                                                                                                              Medium (L2)
                                                                                                            3 =                                                                                                                                                                                                                                              High (L3)
Bits 2-3                                                                                        Speaker control (Mn)
                                                                                                            0 =                                                                                                                                                                                                                                              Disabled (M0)
                                                                                                            1 =                                                                                                                                                                                                                                              Off on carrier (M1) (Default.)
                                                                                                            2 =                                                                                                                                                                                                                                              Always on (M2)
                                                                                                            3 =                                                                                                                                                                                                                                              On during handshake (M3)
Bits 4-6                                                                                                   Limit result codes (Xn)
                                                                                                            0 =                                                                                                                                                                                                                                              X0
                                                                                                            4 =                                                                                                                                                                                                                                              X1
                                                                                                            5 =                                                                                                                                                                                                                                              X2
                                                                                                            6 =                                                                                                                                                                                                                                              X3
                                                                                                            7 =                                                                                                                                                                                                                                              X4 (Default.)
Bit 7                                                                                                                                          Dial abort option (AT&a)
                                                                                                            0 =                                                                                                                                                                                                                                              Do abort (&A0) (Default.)
                                                                                                            1 =                                                                                                                                                                                                                                              No abort (&A1)
S23 – General Bit Mapped Options Status (Read only register)
Indicates the status of command options.

Bit 0                                                                                                                                                    Grant RDL
                                                                                                            0 =                                                                                                                                                                                                                                              RDL         not allowed (&T5) (Default.)
                                                                                                            1 =                                                                                                                                                                                                                                              RDL         allowed (&T4)
Bits 1-3                                                                                        DTE Rate
                                                                                                            0 =                                                                                                                                                                                                                                              0 – 300 bps
                                                                                                            1 =                                                                                                                                                                                                                                              600 bps
                                                                                                            2 =                                                                                                                                                                                                                                              1200 bps
                                                                                                            3 =                                                                                                                                                                                                                                              2400 bps
                                                                                                            4 =                                                                                                                                                                                                                                              4800 bps
                                                                                                            5 =                                                                                                                                                                                                                                              9600 bps (Default.)
                                                                                                            6 =                                                                                                                                                                                                                                              19200 bps
Bits 4-5                                                                                        Assumed DTE parity
                                                                                                            0 =                                                                                                                                                                                                                                              even
                                                                                                            1 =                                                                                                                                                                                                                                              not used
                                                                                                            2 =                                                                                                                                                                                                                                              odd
                                                                                                            3 =                                                                                                                                                                                                                                              none (Default.)


6177-2203                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      37

## Page 37

Bits 6-7                                                                                        Guard tone (&Gn)
                                                                                       0 =                                                                                                                                                                                                                                              None (&G0)
                                                                                       1 =                                                                                                                                                                                                                                              None (&G1)
                                                                                       2 =                                                                                                                                                                                                                                              1800 Hz (&G2) (Default.)
S24 – Receiver          disconnect level
If %D1 is set, will the value in this register set the level of the lowest accepted linesignal
Default 0 {0-43} (dBm)
S25 – Delay To DTR
Sets the length of time that the modem will ignore DTR for taking the action specified by &Dn.
Default 5 {0-255} hundredths of a second)
S26 – RTS to CTS delay
Sets the length of time between RTS and CTS.
Only working in synchronous connections
Default 1 {0-255} hundredths of a second)
S27 – Bit Mapped Options Status
Indicates the status of command options.

Bits 0 -1,3 —      Asynchronous selection (&Qn)
31   0
0                                                                                                                                                                                                                 0                                                                                                                                                                                                                                                                                0                                                                                                                                                            =                                                                                                                       &Q0
1                                                                                                                                                                                                                 0                                                                                                                                                                                                                                                                                0                                                                                                                                                =                                                                                                                                  &Q4
1                                                                                                                                                                                                                 0                                                                                                                                                                                                                                                                                1                                                                                                                                                            =                                                                                                                       &Q5 (Default.)
1                                                                                                                                                                                                                 1                                                                                                                                                                                                                                                                                0                                                                                                                                                            =                                                                                                                       &Q6
Bit 2                                                                                                                                                    Reserved
Bits 4-5                                                                                        Internal clock select (&Xn)
                                                                                       0 =                                                                                                                                                                                                                                              Internal clock (&X0) (Default.)
                                                                                       1 =                                                                                                                                                                                                                                              External clock (&X1)
                                                                                       2 =                                                                                                                                                                                                                                              Slave clock (&X2)
Bit 6                                                                                                                                          CCITT/Bell mode select (Bn)
                                                                                       0 =                                                                                                                                                                                                                                              CCITT          mode (B0) (Default.)
                                                                                       1 =                                                                                                                                                                                                                                              Bell mode (B1)
Bit 7                                                                                                                                          DTR dial option (AT&B)
                                                                                       0 =                                                                                                                                                                                                                                              No DTR dial (&B0) (Default.)
                                                                                       1 =                                                                                                                                                                                                                                              Dial on DTR low to high transition (&B1)
S28 – Bit Mapped Options Status (Read only register)

Bits 0-1                                                                                        Reserved
Bit 2                                                                                                                                          Reserved (always 0).
Bits 3-4                                                                                        Pulse dialling (&Pn)
                                                                                       0 =                                                                                                                                                                                                                                              39%-61% make/break ratio at 10 pulses per second (&P0) (Default.)
                                                                                       1 =                                                                                                                                                                                                                                              33%-67% make/break ratio at 10 pulses per second (&P1)
                                                                                       2 =                                                                                                                                                                                                                                              39%-61% make/break ratio at 20 pulses per second (&P2)
                                                                                       3 =                                                                                                                                                                                                                                              33%-67% make/break ratio at 20 pulses per second (&P3)
Bit 5                                                                                                                                          Reserved
Bits 6-7                                                                                        MNP         Link Negotiation Speed (*Hn)
                                                                                       0 =                                                                                                                                                                                                                                              Link negotiation at highest speed (*H0) (Default.)
                                                                                       1 =                                                                                                                                                                                                                                              Link negotiation at 1200 bps (*H1)


38                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 38

S29 – Flash Dial Modifier         Time (Not stored with the AT&W          command. Only for Germany)
Sets the length of time that the modem will go on-hook when it encounters the flash (!) dial modifier
in the dial string.
Default 0 (disabled) {0-255} 10 ms intervals.
S30 – Disconnect Inactivity Timer
Sets the length of time that the modem will stay on-line before disconnecting when no data is sent or
received.
Default 0 (disabled) {0-255} ten seconds (0-2550 seconds)
S31 – Bit Mapped Options Status (Read only)

Bit 0                                                                                                                                          Reserved
Bit 1                                                                                                                                          Controls auto line speed detection (Nn)
                                0 =                                                                                                                                                                                                                                              Disabled (N0)
                                1 =                                                                                                                                                                                                                                              Enabled (N1) (Default.)
Bits 2-3                                                                                        Controls error correction progress messages (Wn)
                                0 =                                                                                                                                                                                                                                              DTE speed only (W0) (Default.)
                                1 =                                                                                                                                                                                                                                              Full reporting (W1)
                                2 =                                                                                                                                                                                                                                              DCE speed only (W2)
Bit 3                                                                                                                                                    Reserved
Bits 4-7                                                                                        Reserved
S32 – XON Character
Sets the value of the XON character.
Default 17 {0-255} ASCII decimal.
S33 – XOFF         Character
Sets the value of the XOFF character.
Default 19 {0-255} ASCII decimal.
S36 – LAPM Failure Control
This value indicates what should happen upon a LAPM connect failure.
0 = Modem disconnects.
1 = Modem stays on-line and a Direct mode connection is established. (AT\N1)
2 = Reserved.
3 = Modem stays on-line and a Normal mode connection is established. (AT\N0)
4 = An MNP         connection is attempted and if it fails, the modem disconnects. (AT\N5)
5 = An MNP         connection is attempted and if it fails, a Direct mode connection is established.
6 = Reserved.
7 = An MNP         connection is attempted and if it fails, a Normal mode connection is established. (AT\N3)
Default 7 {0..7}
Please also refer to S48,          AT\N
S37 – Desired Line Connection Speed
Bits 0-3                                                                                                  Desired line connection speed. This is interlinked with the Fn command. If an invalid
                                number is entered, the number is accepted into the register, but S37 will act as if the
                                default value has been entered.
0                                              =              Attempt auto mode connection (F0). (Default)
1-3  =              Attempt to connect at 300 bps (F1).
4 = Reserved.
5                                    =              Attempt to connect at V.22 1200 bps (F4).
6                                    =              Attempt to connect at V.22 bis 2400 bps (F5).
7                                    =              Attempt to connect at V.23 (F3).


6177-2203                                                                                                                                                                                                                             39

## Page 39

S38 – Delay Before Forced Hang Up (Not stored with the AT&W          command!)
This register specifies the delay between the modem's receipt of the H command to disconnect (or
ON-to-OFF transition of DTR if the modem is programmed to follow the signal), and the disconnect
operation. This register can be used to ensure that data in the modem buffer is sent before the modem
disconnects. 255 makes the modem stay connected until the buffer is empty, or the carrier is lost.
Default 20 {0-255} seconds
S39 – Flow Control Bit Mapped Options Status          (Read only register)
Bits 0-2                                                                                                   Status of command options
                                                                                 0 =                                                                                                                                                                                                                                              No flow control
                                                                                 3 =                                                                                                                                                                                                                                              RTS/CTS (&K3) (Default.)
                                                                                 4 =                                                                                                                                                                                                                                              XON/XOFF (&K4)
Bits 3-7                                                                                        Reserved
S40 – General Bit Mapped Options Status (Read only register)
Indicates the status of command options.

Bit 0-1                                                                                                                  MNP         Extended Services (-Kn)
                                                                                 0 =                                                                                                                                                                                                                                              Disable extended services (-K0) (Default.)
                                                                                 1 =                                                                                                                                                                                                                                              Enable extended services (-K1)
                                                                                 2 =                                                                                                                                                                                                                                              Enable extended services (-K2)
Bit 2                                                                                                                                                    Reserved.
Bits 3-5                                                                                                   Break Handling (\Kn)
                                                                                 0 =                                                                                                                                                                                                                                              \K0
                                                                                 1 =                                                                                                                                                                                                                                              \K1
                                                                                 2 =                                                                                                                                                                                                                                              \K2
                                                                                 3 =                                                                                                                                                                                                                                              \K3
                                                                                 4 =                                                                                                                                                                                                                                              \K4
                                                                                 5 =                                                                                                                                                                                                                                              \K5 (Default.)
Bits 6-7                                                                                                   MNP         block size (\An)
                                                                                 0 =                                                                                                                                                                                                                                              64 chars (\A0)
                                                                                 1 =                                                                                                                                                                                                                                              128 chars (\A1)
                                                                                 2 =                                                                                                                                                                                                                                              192 chars (\A2) (Default.)
                                                                                 3 =                                                                                                                                                                                                                                              256 chars (\A3)
S41 – General Bit Mapped Options Status (Read only register)
Indicates the status of command options.

Bits 0 -1                                                                             Compression selection (%Cn)
                                                                                 0 =                                                                                                                                                                                                                                              Disabled (%C0)
                                                                                 1 =                                                                                                                                                                                                                                              MNP         5 (%C1)
                                                                                 2 =                                                                                                                                                                                                                                              V.42 bis (%C2)
                                                                                 3 =                                                                                                                                                                                                                                              MNP         5 and V.42 bis (%C3) (Default.)
Bit 2, 6                                                                                                          Auto retrain and fallback/fall forward (%En)
                                                                                 Bit 6                                                                                                                                                                                                                   Bit 2
                                                                                 0                                                                                                                                                                                                                                                                      0                                                                           = Retrain and fallback/fall forward disabled (%E0) (Default.)
                                                                                 0                                                                                                                                                                                                                                                                                1                                                                           = Retrain enabled (%E1)
                                                                                 1                                                                                                                                                                                                                                                                                0                                                                           = Fallback/fall forward enabled (%E2)


40                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 40

Bit 3                                                                                                                                          Modem-to-modem flow control
                                                    0 =                                                                                                                                                                                                                                              Disabled (\G0) (Default.)
                                                    1 =                                                                                                                                                                                                                                              Enabled (\G1)
Bits 4-5                                                                                        Reserved
Bit 7                                                                                                                                          Enable fallback to V.22 bis/V.22 (-Qn)
                                                    0 =                                                                                                                                                                                                                                              Disabled (-Q0)
                                                    1 =                                                                                                                                                                                                                                              Enabled (-Q1) (Default.)
S46 – V.42bis, Data Compression Control
136                                                                                                                                                                        Execute error correction protocol with no compression.
138                                                                                                                                                                        Execute error correction protocol with compression. (Default.)
S48 – V.42 Negotiation Action
0                                                                                                                                                                                                                 Force LAPM connection.
7                                                                                                                                                                                                                 Enable negotiation. (Default.)
128                                                                                                                                                              Force action specified in S36.
Please also refer to S36.

S80 – Soft-Switch Functions           (Stored directly in the E2            memory)
If switch SW2:1 is OFF, S80 will not be used
Bits 0                                                                                                                                    Selection of type of command
                                                    0 =                                                                                                                                                                                                                                              AT
                                                    1 =                                                                                                                                                                                                                                              V.25
Bit 2                                                                                                                                                    Auto line quality and retain monitoring
                                                    0 =                                                                                                                                                                                                                                              No line monitoring or renegotiation (AT0)
                                                    1 =                                                                                                                                                                                                                                              Line monitoring or renegotiation (AT1)
Bit 3                                                                                                                                          Modem-to-modem flow control
                                                    0 =                                                                                                                                                                                                                                              Disabled (AT0)
                                                    1 =                                                                                                                                                                                                                                              Enabled (AT1)
Bit 4                                                                                                                                          MNP         block or digit flow control
                                                    0 =                                                                                                                                                                                                                                              Disabled (AT0)
                                                    1 =                                                                                                                                                                                                                                              Enabled (AT1)
S82 – Break control for          LAPM connections
3 – Break is sent immediately. Data in the buffer is stored
7 – Break is sent immediately. Data in the buffer is lost
128 – Break is buffered (Default.)
S86 – Call Failure Reason Code (Read only register, not stored with the AT&W          command!)
This register is written with an indication of the error cause after a dialling error.
Default 0 {0..255}
S91 – PSTN Transmit Attenuation Level           (Stored directly in the E2            memory)
Default 13 {0..15} dBm (Corresponding to 0 to -15 dBm transmit level.) Country dependant.
S92 – Fax Transmit Attenuation Level (Stored directly in the E2            memory)
Default 13 {0..15} dBm (Corresponding to 0 to -15 dBm transmit level.) Country dependant.
S95 – Extended Result Codes
Set by \V.
Default 0 {0..7}
S99 – Leased Line Transmission Level (Stored directly in the E2            memory)
Default 10 {0..15} dBm (Corresponding to 0 to -15 dBm transmit level.) Country dependant.


6177-2203                                                                                                                                                                                                                                                                                                                                                                          41

## Page 41

S200 – Bit Mapped Register
Bit 0                                                                                                                                                    If not set is normal faxspeeeds used, or else it is a special function.
Bit 6 and 7                                               Equal to \C command (Normally not available).
S202 – Remote Access Escape Character
Default 170 {0..255}


                                                                                                                                                                             Block diagram


                                                                                                                                                                                                                                                                                                                                                                                                                           Telephone
                                                                                                                                                                                                                                                                                                                                                                                                                           Line/
                                                                                                                                                                                                                       DSP                                                                                                                                                                                                 Leased
                                                                                                                RS-232                                                                                                                                                                                                   Line                                                                                              Line
                              V24/                                                                            Interface                                                                                                                                                                                       Interface
                RS-232


                                                                                                                              L                                                                                             t

                                                                                                                             E                                                                                         CPU
                                                                                                                             D                                                                                                                                                                                    Watch-
                                                                                                                             S                                                                                                                                                                                            dog

                      Power                                                                                                                                                                                                                                                                                               +5V
                      supply                                                                                                                                                                                                                                                                           E2-
                                                                                                                           Dipswitch                                                                                                                                                          PROM
                                                                                                                                                                                                                            t
                                                                                                                                                                                                               Memory


                                                                                                                                                                                                                                                                                                                                                    +5V
                                                                                                                                              Insulated power supply                                                                                                                                                                                     0V
                                                                                                                                                                                                                                                                                                                                                      -5V


42                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 42

Example1:
Several commands can be stacked on each command-line. For example
ATEQ1\N4D12345
can be used instead of

                                 ATE0
                                 OK
                                 ATQ1
                                 OK
                                 AT\N4
                                 OK
                                 ATD12345

with the same result


To use MINICALL (Pager systems)


                                                                                                                                                                                                                                                                             tt


ATS6=60                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  Waits for tone in maximum 60 seconds.
OK
ATD^0746xxxxxxW0yyyyyyyy#,.                                                                                                                                                         Dot gives disconnection.
                       ***********   ***********                                                                                                                                                                                                Comma gives a pause.
                                                                                                                                                                                                       The telephone number shall end with # according to
                                                                                                                                                                                                       instructions.
                                                                                                                                                                                                       Area code and telephone number that will be shown
                                                                                                                                                                                                       on the MINICALL.
                                                                                                                                                                                                       Waiting for tone.
                                                                                                                                                                                                       Number to MINICALL         search.
                                                                                                                                                                                                       Takes away calling tone.
OK                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           With succesful dial-up.
Or
NO DIALTONE                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              With failure.


6177-2203                                                                                                                                                                                                                                                                                                                                                                                                                    43

## Page 43

To use multidrop V23 in half                                                                                                  duplex


Multidrop connection is possible only when the modem is set for V23 half duplex. This allows a
maximum line transmission speed to 1200 baud. The speed at the serial port can however be higher.
       The carrier (the transmission) is activated with the RTS signal in the RS-232 interface and the
carrier remains high until the transmission buffer is empty.
The actual number of modems that can be multidropped depends on the line quality and the range is
dependant on the number of attached devices.
V23 half duplex is achieved by switching ON SW1:2 and SW5:2 and switching OFF the rest of SW1
and SW5.
Frequently used settings for PLC-systems
and industrial applications.


Most PLC-systems and other industrial applications require the same changes to the standard
settings.

The most commonly encountered problems concern speed, parity and control signals from the
connected equipment.

Speed and parity are changed with the switches under the cover in block SW4. If this action does not
solve the problem the modem’s answering codes and possible echoing of commands might be the
source of the difficulty. Here below follows a list of commands that might resolve the problems.
(The commands may of course be placed on one single command line if desired, as per the example1):

                    ATV0                                                                                                                                                                 Gives the answering codes in short format. (digits)
                    ATQ1                                                                                                                                                                 No result codes are sent on the RS-232 connection.
                    ATE0                                                                                                                                                                      Commands that are sent from the terminal/computer etc. are not echoed back
                                                           to the RS-232 connection.
                    AT&C1                                                                                                                                    DCD will follow the carrier on the line.
                    AT&K0                                                                                                                                  No handshaking.
                    AT&A1                                                                                                                                  Character abort option on.

For further information regarding these commands please refer to the specific section of this manual.


44                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              6177-2203

## Page 44

Dial up with hardware signalling

                                       External signal


Dial-up can be made by applying an external signal to the DTR-pin in the RS-232 contact. A        typical
application is an alarm signal from a PLC.

The modem reacts on the first rising edge which means that one pulse is sufficient. The signal-level
should be in compliance with the RS-232 standard. If only a relay contact is available the DSR-pin
will provide a suitable voltage.

The modem does not have a redialling function. If necessary this must be handled by other
equipment connected. We advise the DCD signal to be used as a control indicating whether
connection was made or not.

            AT&B1                                                                                                                                    Dial-up using DTR.
            AT&D3                                                                                                                                  Hang-up if DTR goes low.
            AT&Z0=nn                                                                       The number to dial, nn, is stored in memory position 0.
Please also refer to “To use minicall”, as the minicall and the dial-up with hardware signalling can
be used together.


6177-2203                                                                                                                                                       45

## Page 45

Leased line applications


Leased line connections can be done using either 2-wire or 4-wire.

When renting a 4-wire line from a telephone company one pair of cables for transmission and one
pair for reception are usually provided.

The maximum transmission distance depends on the attenuation of the line. To have a good error rate
it is recommended to keep the attenuation under 20 dB, which is approximately 20 km in distance.
When renting lines from a telephone company a longer transmission distance is normally possible as
the signals are probably transmitted over PCM-lines, i.e. fibre optic or other media with low
attenuation.

The best way to set the modem for leased line applications is to use the dip switches.

The settings should be as follows:
                                                                              SW1:1 and 2 in modem no 1 ON and only SW1:2 in modem no 2 ON.
                                                                              SW3:1 shall be ON for 2-wire and OFF for 4-wire.
                                                                              SW3:3 shall be ON. Sets error correction and data compression OFF.
                                                                              SW4 sets speed and parity for the RS-232 port.
                                                                              SW5 decides line-speed. Must be set in direct mode applications.


46                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              6177-2203

## Page 46

Glossary

ASCII
A        binary code system which defines 128 characters using different combinations of 1s and 0s. ASCII
= American Standard Code for Information Interchange.
Asynchronous Data
transmission where the characters are transmitted one at a time, starting with a start bit and ending
with a stop bit. About 90–95% of all serial data communications are asynchronous.

Baud
The number of data symbols transmitted every second. In local data communications, baud = bit/s.
In telecommunication, each symbol may encode several bits.
Buffer
A        memory for storing data for a short time, e.g. until the receiver is ready

Byte
A        character comprising binary numbers, e.g. an ASCII character which consists of 7–8 data bits, each
corresponding to an alphanumerical character.
DCE
Data Communication Equipment. Like a Modem, protocol converter or line driver.
DIN rail
Deutsche Industri Norme standard mounting for the installation of equipment in an apparatus cubicle.
DTE
Data Terminal Equipment like PC,s, terminals and printers.

Data Compression and Error Correction
V.42
CCITT’s error correction protocol incorporating LAPM. If the V.42 connection fails then MNP         will
be tried.

LAPM
Link Access Procedure for Modems. An error correction method used in transmissions via PTT
modems.

MNP
Microcom Networking Protocol. Several methods for error correction and data compression for PTT
modems.
MNP                             1:                  Asynchronous Protocol, half duplex.
MNP                             2:                  Asynchronous Protocol, full duplex, Data is divided into blocks (data rate actually slows).
MNP                             3:                  Synchronous Protocol, full duplex. Data in blocks (higher speeds with no errors).
MNP                             4:                  Similar to 3, but with smaller data blacks allowing for faster data rates with no errors.
MNP                             5:                  Level 4 with data compression, gives about double the data rate.
MNP         10:                 Development of MNP5 with dynamic line monitoring and block size adjustment, used on
              very bad lines.
ARQ
Automatic Repeat reQuest. When incorrect data is detected a modem a request to retransmit the data
is made to the remote modem.


6177-2203                                                                                                                             47

## Page 47

V.42bis.
Data Compression technique used by modems rather than MNP5, because it offers better
compression on already compressed data.
Data Rate
In modems this is often different to the baud rate. For instance the Data Rate of V.32bis is 14,400bps
and the baud is 2400 symbols/second.
Direct Mode
The Data to be transmitted is sent directly to the data pump. No compression, error correction or
buffering occurs, allowing the data to be transmitted across the link unaltered by the modem.

Duplex
Means that the communication is bi-directional. In half duplex, the devices take turns sending and
receiving. In full duplex, sending and receiving can take place simultaneously.
Handshaking
Confirmations and status signals sent between communicating devices in order to check the data
stream. There are two general types, hardware (RTS/CTS) and software (XON/XOFF). In hardware
handshaking the RS232 status lines are used to control data flow, whereas with the software method
characters are transmitted to control the data.
Hayes commands
A        set of commands for controlling PTT          modems. Often refereed to as the AT          command set. Most
modems support these commands however there are many variations and some modems will support
commands that others can not.
LED
Light-Emitting Diode. A        semi-conductor which emits light when it receives an electrical current.
Used as indicators.
Lease Line
A        private point to point connection provided by a national or local telecommunication company.
These can be 2 or 4 wire point to point or even in a multidrop configuration.

Modem
Acronym of the words modulator and demodulator. Modulates or transforms the signal from
computer equipment into electrical signals for transmission. The receiver has a similar modem which
retransforms the signal, demodulation.
Modem Modulation Standards
V.21                                                                                                                                                         300bps, similar to Bell 103
V.23                                                                                                                                                         1200/75 bps Split speed line
V.23hdx                                                                                             1200 multidrop lease line standard
V.22                                                                                                                                                         1200 bps full duplex
V.22bis                                                                                                          2400 bps full duplex
V.32                                                                                                                                                         9600 bps full duplex
V.32bis                                                                                                          14,400 bps full duplex


48                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 48

Modulation Techniques
DPSK
Differential Phase Shift Keying. employed in data rates up to 4800bps.
FSK
Frequency Shift Keying. Used in the lowest data rate standards.
QAM
Quadrature Amplitude Modulation. A        technique used for data rates up to 9600.
TCM
Trellis Coded Modulation. Used in the highest speed modulations.

NVRAM
Non Volatile Random Access Memory. Used by the modem to store profile information and numbers
even when the unit has no power.
Normal Mode
A        non error corrected connection between two modems. The data is however speed buffered.
Off Hook
Term used to describe when the modem is not connected to a telephone line.

On Hook
Term used to describe when the modem is using the telephone line.

PSTN
Public Switched Telephone Network.

Parity
A        mathematically-derived bit which is added by the transmitter. The receiver checks the sum of the
parity bit to detect any error in transmission.
REN Number
The REN number of the TD-22 is 1
The Ringer Equivalence Number (REN) is a measure of apparatus performance when used with other
equipment connected to the same telephone line. The sum of the individual REN’s connected to a
single line should not exceed 4.


6177-2203                                                                                                             49

## Page 49

RS232 Signals
TD
Transmitted Data. Data going from DTE to DCE.
RD
Received Data. The Data going from DCE  to DTE.
RT S
Request to Send. Hardware handshake generated by the DTE to determine if the DCE is ready to
receive data. Expected response is from the CTS line.
CTS
Clear To Send. Hardware handshake by DCE in response to an RTS signal.
DSR
Data Set Ready. A        signal from the DCE used to say that is powered and usable.
DTR
Data Terminal Ready. A        signal from the DTE showing it is powered and usable.
DCD
Data Carrier Detect. A        signal from the DCE showing that a carrier is present and a line is ready for
data transmission.
Simplex
Unidirectional communication.
Start bit
Marks the start of a transmission. In asynchronous transmission each character is preceded by a start
bit.

Stop bit
One or more stop bits mean that the character has been transmitted. Systems that use two stop bits
can cause problems with some modems as the modem will remove the second stop bit to gain better
data throughput and then not put it back.


50                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     6177-2203

## Page 50

OWN COMMENTS


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................


.....................................................................................................................................................................................................................

6177-2203                                                                                                                                                                                                                                                      51

## Page 51

Westermo Teleindustri AB have distributors in several countries,
                                         contact us for further information.


                          Westermo Teleindustri AB • S-640 40 Stora Sundby, Sweden
                                      Phone +46 16 612 00                  Fax +46 16 611 80
                     E-mail: info@westermo.se • Westermo Web site: www.westermo.se

                                                        Subsidiaries
Westermo Data Communications Ltd                                   Westermo Data Communications GmbH
Solent Business Centre • Millbrook Road West                       Bruchsaler Straße 18, 68753 Waghäusel
Millbrook, Southampton • SO15 0HW                                  Tel.: +49(0)7254-95400-0 • Fax.:+49(0)7254-95400-9
Phone: +44(0)1703-704 611 • Fax.:+44(0)1703 702 682                E-Mail: westermo.germany@t-online.de
E-Mail: sales@westermo.co.uk
