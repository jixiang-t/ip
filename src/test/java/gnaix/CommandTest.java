package gnaix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandTest {

    @Test
    void fromString_lowercaseCommand_commandReturned() {
        assertEquals(Command.TODO, Command.fromString("todo"));
    }

    @Test
    void fromString_uppercaseCommand_commandReturned() {
        assertEquals(Command.DEADLINE, Command.fromString("DEADLINE"));
    }

    @Test
    void fromString_mixedCaseCommand_commandReturned() {
        assertEquals(Command.EVENT, Command.fromString("EvEnT"));
    }

    @Test
    void fromString_unknownCommand_unknownReturned() {
        assertEquals(Command.UNKNOWN, Command.fromString("hello"));
    }
}