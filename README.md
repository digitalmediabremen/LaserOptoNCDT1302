# LaserOptoNCDT1302

driver for laser optical displacement measurement device MICRO-EPSILON optoNCDT 1302

## how to connect the device to an arduino board

the device communicates via the serial interface RS422. in order to connect the device to an ardunio board an extra component is required. the transceiver IC [MAX485](https://www.maximintegrated.com/en/products/interface/transceivers/MAX485.html) is capable of converting RS422 ( and RS485 ) to TTL voltage levels. see [MAX485 datasheet, p7](https://datasheets.maximintegrated.com/en/ds/MAX1487-MAX491.pdf) for a typical operating circuit.

## resources

- [Instruction Manual](http://www.micro-epsilon.pl/download/man--optoncdt-1302--en.pdf)
