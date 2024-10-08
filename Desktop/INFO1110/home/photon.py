'''
Name:   QILONG ZHONG
SID:    540238846
Unikey: qzho0669

Photon - A particle of light that are emitted by emitters and travels along the
circuit board. Photons have a frequency (THz) and direction. They can interact 
with components in the circuit in which it may be absorbed. When a photon is
absorbed, they no longer move.

You are free to add more attributes and methods, as long as you aren't 
modifying the existing scaffold.

Warning: Importing other components in this module will cause a circular import
error, as those components require this module to be fully initialised before
it can finish initialising itself. If you need to query the type of a component,
use the component_type attribute that each component has defined instead.
'''


class Photon:


    def __init__(self, x: int, y: int, frequency: int, direction: str):
        # only requires implementation once you reach RUN-MY-CIRCUIT
        '''
        Initialises a Photon instance given an x and y position, as well as a
        frequency and direction. symbol is '.' and absorbed is False by
        default.

        symbol:    str  - the symbol of this photon ('.')
        x:         int  - x position of this photon
        y:         int  - x position of this photon
        frequency: int  - the frequency (THz) of this photon
        direction: str  - the direction in which this photon will travel 
                          ('N', 'E', 'S' or 'W')
        absorbed:  bool - whether or not this photon has been absorbed

        Paramater
        ---------
        x         - the x position to set this photon to
        y         - the y position to set this photon to
        frequency - the frequency to set this photon to
        direction - the direction to set this photon to
        '''
        self.symbol = '.'
        self.frequency = frequency
        self.x = x
        self.absorbed = False
        self.direction = direction
        self.y = y


    def next_move(self, Nums, bound):

        if Nums < 0:
            return 0, True  
        elif Nums >= bound:
            return (bound - 1, True)
        return (Nums, False)
        

    def move(self, board_width: int, board_height: int) -> None:
        # only requires implementation once you reach RUN-MY-CIRCUIT
        '''
        Updates this photon's position to move one unit based on its direction.
        Do not move the photon if it has been absorbed.

        After moving the photon, if it is out-of-bounds based on the board_width
        and board_height given, you will need to update this photon to be 
        absorbed and set its position back so it's not out-of-bounds.

        Parameters
        ----------
        board_width  - width of circuit board 
        board_height - height of circuit board
        '''
        if not self.absorbed:
            if self.direction == "N":
                self.y, self.absorbed = self.next_move(self.y - 1, board_height)
            elif self.direction == "E":
                self.x, self.absorbed = self.next_move(self.x + 1, board_width)
            elif self.direction == "S":
                self.y, self.absorbed = self.next_move(self.y + 1, board_height)
            elif self.direction == "W":
                self.x, self.absorbed = self.next_move(self.x - 1, board_width)


    def interact_with_component(self, component, timestamp: int) -> None:
        if self.absorbed:
            return
        component_type = component.get_component_type() if hasattr(component, 'get_component_type') else None
        if component_type == 'emitter':
            pass
        elif component_type == 'receiver':
            component.absorb_photon(self, timestamp)
            self.got_absorbed()
        elif component_type == 'mirror':
            component.reflect_photon(self)

    def got_absorbed(self) -> None:
        # only requires implementation once you reach RUN-MY-CIRCUIT
        '''Updates the absorbed attribute to represent an absorption.'''
        self.absorbed = True


    def is_absorbed(self) -> bool:
        # only requires implementation once you reach RUN-MY-CIRCUIT
        '''Returns absorbed.'''
        return self.absorbed ################


    def set_direction(self, direction: str) -> None:
        # only requires implementation once you reach RUN-MY-CIRCUIT
        '''
        Sets the direction attribute of this photon. If the direction passed
        in is not 'N', 'E', 'S' or 'W', it does not set it.

        Parameters
        ----------
        direction - the new direction to set for this photon
        '''
        if direction == "N" or direction == "S":
            self.direction = direction
            return None
        elif direction == "W" or direction == "E":
            self.direction =direction
            return None
        else:
            return None

    def get_direction(self) -> str:
        # only requires implementation once you reach RUN-MY-CIRCUIT
        '''Returns direction.'''
        return self.direction

    def get_x(self) -> int:
        # only requires implementation once you reach RUN-MY-CIRCUIT
        '''Returns x.'''
        return self.x


    def get_y(self) -> int:
        # only requires implementation once you reach RUN-MY-CIRCUIT
        '''Returns y.'''
        return self.y

        
    def get_frequency(self) -> int:
        # only requires implementation once you reach RUN-MY-CIRCUIT
        '''Returns frequency.'''
        return self.frequency


    def get_symbol(self) -> str:
        # only requires implementation once you reach RUN-MY-CIRCUIT
        '''Returns symbol.'''
        return self.symbol

    

