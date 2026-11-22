package org.example;

public class TestRecognizer {

    public enum State {
        S("S"),
        ONE("1"),
        TWO("2"),
        THREE("3"),
        F("F");

        private final String label;

        State(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public State recognizeStrictByDiagram(String input) {
        if (input == null) {
            return State.S;
        }

        State currentState = State.S;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            currentState = nextStateDiagram(currentState, ch);
        }

        return currentState;
    }

    private State nextStateDiagram(State current, char ch) {
        return switch (current) {
            case S -> (ch == 'T') ? State.ONE : State.S;
            case ONE -> (ch == 'E') ? State.TWO : State.S;
            case TWO -> (ch == 'S') ? State.THREE : State.S;
            case THREE -> (ch == 'T') ? State.F : State.S;
            case F -> State.F;
        };
    }

    public State recognizeFixed(String input) {
        if (input == null) {
            return State.S;
        }

        State currentState = State.S;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            currentState = nextStateFixed(currentState, ch);
        }

        return currentState;
    }

    private State nextStateFixed(State current, char ch) {
        return switch (current) {
            case S -> (ch == 'T') ? State.ONE : State.S;
            case ONE -> {
                if (ch == 'E') yield State.TWO;
                if (ch == 'T') yield State.ONE;
                yield State.S;
            }
            case TWO -> {
                if (ch == 'S') yield State.THREE;
                if (ch == 'T') yield State.ONE;
                yield State.S;
            }
            case THREE -> (ch == 'T') ? State.F : State.S;
            case F -> State.F;
        };
    }
}