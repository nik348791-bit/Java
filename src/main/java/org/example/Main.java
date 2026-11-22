package org.example;

public class Main {
    public static void main(String[] args) {
        TestRecognizer recognizer = new TestRecognizer();

        String[] examples = {
                "abcTESTabc",
                "abcTES",
                "TTEST",
                "TETEST"
        };

        for (String str : examples) {
            System.out.println("Рядок: \"" + str + "\"");
            System.out.println("  За схемою:   " + recognizer.recognizeStrictByDiagram(str));
            System.out.println("  Виправлений: " + recognizer.recognizeFixed(str));
        }
    }
}