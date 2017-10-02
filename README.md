# simple-gcode-sender-P5
Comes with a gui! There are tons of good gcode senders. I'm very fond of [pronterface or printrun](http://www.pronterface.com), there are also great [processing](https://processing.org) libraries such as the text based [gctrl](https://github.com/damellis/gctrl). However, for my digital fabrication course at KAIST I needed something similar but dead simple with a gui to select the serial port and easy enough to hack some custom machines.

# Installation

First get and install processing https://processing.org

Then, install the controlP5 gui library. Open processing, choose menu "sketch", "Import Library...", "Add Library...". Find the "controlP5" library and install.

# Run

Open the "simple_gcode_sender_P5.pde" sketch. Select a serial port and connect to your machine. Currently tested with Marlin firmware.

The default Baudrate is 250000, other rates you have to set in the source (line 7).


